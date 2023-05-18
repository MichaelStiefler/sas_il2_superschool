package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.ini4j.Ini;

import mainController.LogController;
import mainController.MainController;
import utility.Time;

/**
 * Stores Pilot Geo / IP Information
 * @author Storebror
 *
 */
public class PilotGeoIPInformation {
    private static final String GEOIP_INI   = "geoip.ini";
    private static final String WHITELIST_INI   = "whitelist.ini";

    private Pilot pilot;
    private GeoIpData geoIpData;
    private List<AbuseData> abuseDataList;
    private double trustLevelLimit = 0.5D;
    private int connectionLimit = -1;
    private String geoIpServiceName;
    private List<String> abuseDetectionServiceNames;
    
    public GeoIpData getGeoIpData() {
        return geoIpData;
    }

    public PilotGeoIPInformation(Pilot pilot) {
        this.pilot = pilot;
        File geoIpIniFile = new File(GEOIP_INI);
        try {
            Ini ini = new Ini(new FileReader(geoIpIniFile));
            if (!readSettings(ini.get("Common"))) {
                System.out.println("Couldn't initialize PilotGeoIPController, Error in readSettings().");
                return;
            }
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.connectionLimit);
            executor.execute(() -> getGeoIpInfo(ini.get(this.geoIpServiceName)));
            if (this.abuseDetectionServiceNames != null) {
                this.abuseDataList = Collections.synchronizedList(new ArrayList<>());
                this.abuseDetectionServiceNames.forEach(s -> executor.execute(() -> this.getAbuseInfo(ini.get(s))));
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            MainController.writeDebugLogFile(2, "All Data gathering Threads finished!");
            LogController.writeIPLogFile("Pilot " + pilot.getAsciiTextName() + ", IP Address " + pilot.getIPAddress() + " from " + this.getGeoIpData().getCity() + " (" + this.getGeoIpData().getCountry() + ") Name whitelisted: " + this.isWhitelisted(pilot.getAsciiTextName()) + ", IP whitelisted: " + this.isWhitelisted(pilot.getIPAddress()) + ", Trust Level: " + this.trustLevelPercent() + "%");
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.<Init>:");
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException occured in PilotGeoIPController.<Init>:");
            e.printStackTrace();
        }
    }
    
    private boolean readSettings(Ini.Section commonSection) {
        try {
            this.trustLevelLimit = commonSection.get("trustLevelLimit", double.class, 0.5D);
            this.connectionLimit = commonSection.get("connectionLimit", int.class, -1);
            if (this.connectionLimit == -1) this.connectionLimit = Integer.MAX_VALUE;
            if (this.connectionLimit < 1) this.connectionLimit = 1;
            this.geoIpServiceName = commonSection.get("geoIpService");
            String abuseDetectionServiceNamesRaw = commonSection.get("abuseDetectionServices");
            if (abuseDetectionServiceNamesRaw != null) this.abuseDetectionServiceNames = Arrays.asList(abuseDetectionServiceNamesRaw.split(","));
            return true;            
        } catch (Exception e) {
            System.out.println("IO Exception occured in PilotGeoIPController.readSettings:");
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean getGeoIpInfo(Ini.Section geoIpServiceSection) {
        try {
            this.geoIpData = new GeoIpData(geoIpServiceSection, this.pilot);
            return true;
        } catch (Exception e) {
            System.out.println("IO Exception occured in PilotGeoIPController.getGeoIpInfo:");
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean getAbuseInfo(Ini.Section abuseDetectionServiceSection) {
        try {
            this.abuseDataList.add(new AbuseData(abuseDetectionServiceSection, this.pilot.ipAddress));
            return true;
        } catch (Exception e) {
            System.out.println("IO Exception occured in PilotGeoIPController.getGeoIpInfo:");
            e.printStackTrace();
            return false;
        }
    }
    
    private String createRegexFromGlob(String glob) {
        StringBuilder out = new StringBuilder("^");
        for(int i = 0; i < glob.length(); ++i) {
            final char c = glob.charAt(i);
            switch(c) {
                case '*': out.append(".*"); break;
                case '?': out.append('.'); break;
                case '.': out.append("\\."); break;
                case '\\': out.append("\\\\"); break;
                default: out.append(c);
            }
        }
        out.append('$');
        return out.toString();
    }
    
    public boolean isWhitelisted(String s) {
        try (BufferedReader whitelistReader = new BufferedReader(new FileReader(new File(WHITELIST_INI)))) {
            String line = whitelistReader.readLine();
            while (line != null) {
                if (s.matches(createRegexFromGlob(line))) return true;
                line = whitelistReader.readLine();
            }
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in PilotGeoIPController.getIpIntelContact:");
            ioe.printStackTrace();
        }
        return false;
    }

    public int trustLevelPercent() {
        return (int)(trustLevel() * 100D);
    }
    
    public double trustLevel() {
        double trustLevelTotal = 0D;
        double weightTotal = 0D;
        
        for (AbuseData a:this.abuseDataList) {
            trustLevelTotal += a.getTrustLevel() * a.getWeight();
            weightTotal += a.getWeight();
        }
        if (weightTotal == 0D) return 1D;
        return Math.max(0D, Math.min(1D, trustLevelTotal / weightTotal));
    }
    
    public boolean isTrusted() {
        return  this.trustLevel() > this.trustLevelLimit || isWhitelisted(pilot.asciiTextName) || isWhitelisted(pilot.ipAddress);
    }

    
    public static void main(String[] args) {
        MainController.startupConfigInitialize(args);
        MainController.configInitialize();
        MainController.wpInitialize();
        ConfigurationItem configItem = new ConfigurationItem("debugLevel", "Log Detail (0, 1, 2)", ConfigurationItem.ConfigItemType.INTEGER, 1, 0, 2);
        MainController.CONFIG.getDynamicVariables().put("debugLevel", configItem);
        
        //Pilot pilot = new Pilot("John Doe", "178.155.5.154", 1946, Time.getTime());
        //Pilot pilot = new Pilot("Storebror", "84.46.52.97", 1946, Time.getTime());
        Pilot pilot = new Pilot("Storebror", "213.209.105.34", 1946, Time.getTime());
        
        PilotGeoIPInformation pilotGeoIPInformation = new PilotGeoIPInformation(pilot);
        System.out.println("Pilot " + pilot.getAsciiTextName() + ", IP Address " + pilot.getIPAddress() + " from " + pilotGeoIPInformation.getGeoIpData().getCity() + " (" + pilotGeoIPInformation.getGeoIpData().getCountry() + ")");
        System.out.println("Trusted: " + pilotGeoIPInformation.isTrusted());
        System.out.println("IP Whitelisted: " + pilotGeoIPInformation.isWhitelisted(pilot.getIPAddress()));
        System.out.println("Name Whitelisted: " + pilotGeoIPInformation.isWhitelisted(pilot.getAsciiTextName()));
        System.out.println("TLVL: " + pilotGeoIPInformation.trustLevel());
        System.exit(0);
    }

}
