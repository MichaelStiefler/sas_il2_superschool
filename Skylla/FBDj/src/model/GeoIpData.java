package model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.ini4j.Ini;
import org.json.JSONObject;

import mainController.MainController;

public class GeoIpData extends JsonData {
    
    private static final String OVERRIDE_INI   = "override.ini";

    
    public String getContinent() {
        return customFields.get("continent").getData();
    }
    public String getCountry() {
        return customFields.get("country").getData();
    }
    public String getCountryCode() {
        return customFields.get("countrycode").getData();
    }
    public String getRegion() {
        return customFields.get("region").getData();
    }
    public String getCity() {
        return customFields.get("city").getData();
    }
    public String getLatitude() {
        return customFields.get("latitude").getData();
    }
    public String getLongitude() {
        return customFields.get("longitude").getData();
    }
    public String getIsp() {
        return customFields.get("isp").getData();
    }
    public String getIpType() {
        return customFields.get("iptype").getData();
    }
    public String getBusinessName() {
        return customFields.get("businessname").getData();
    }
    public String getBusinessWebsite() {
        return customFields.get("businesswebsite").getData();
    }
    public String getIpName() {
        return customFields.get("ipname").getData();
    }
    public String getStatus() {
        return customFields.get("status").getData();
    }
    public String getMessage() {
        return customFields.get("message").getData();
    }
    public HashMap<String, CustomField> getCustomFields() {
        return customFields;
    }
    public String getCustomFieldValue(String key) {
        return customFields.get(key).getData();
    }

    public JSONObject getRawData() {
        return json;
    }
    
    public GeoIpData(Ini.Section section, Pilot pilot) {
        super(section, pilot.ipAddress);
        this.applyOverrideValues(pilot);
        MainController.writeDebugLogFile(2, "GeoIpData <Init>-");
    }
    
    private void applyOverrideValues(Pilot pilot) {
        MainController.writeDebugLogFile(2, "GeoIpData applyOverrideValues1+");
        File geoIpIniFile = new File(OVERRIDE_INI);
        try {
            Ini ini = new Ini(new FileReader(geoIpIniFile));
            Set<String> sections = ini.keySet();
            sections.forEach(s -> this.applyOverrideValues(pilot, ini.get(s)));
        } catch (IOException ioe) {
            System.out.println("IO Exception occured in GeoIpData.applyOverrideValues:");
            ioe.printStackTrace();
        }
        MainController.writeDebugLogFile(2, "GeoIpData applyOverrideValues1-");
    }
    
    private void applyOverrideValues(Pilot pilot, Ini.Section section) {
        MainController.writeDebugLogFile(2, "GeoIpData applyOverrideValues2+");
        for (int matchIndex = 1;;matchIndex++) {
            String matchKey= "match" + matchIndex;
            String valueKey= "value" + matchIndex;
            if (!section.containsKey(matchKey) || !section.containsKey(valueKey)) break;
            if (section.get(matchKey).equalsIgnoreCase("username")) {
                if (!section.get(valueKey).equalsIgnoreCase(pilot.getAsciiTextName())) {
                    MainController.writeDebugLogFile(2, "GeoIpData applyOverrideValues2+ 1-");
                    return;
                }
            } else {
                if (!customFields.containsKey(section.get(matchKey))) {
                    MainController.writeDebugLogFile(2, "GeoIpData applyOverrideValues2+ 2-");
                    return;
                }
                if (!customFields.get(section.get(matchKey)).getData().equalsIgnoreCase(section.get(valueKey))) {
                    MainController.writeDebugLogFile(2, "GeoIpData applyOverrideValues2+ 3-");
                    return;
                }
            }
        }
        for (int overrideIndex = 1;;overrideIndex++) {
            String overrideKey= "override" + overrideIndex;
            String valueKey= "newvalue" + overrideIndex;
            if (!section.containsKey(overrideKey) || !section.containsKey(valueKey)) break;
            if (!customFields.containsKey(section.get(overrideKey))) continue;
            customFields.get(section.get(overrideKey)).setData(section.get(valueKey));
        }
        MainController.writeDebugLogFile(2, "GeoIpData applyOverrideValues2+ -");
    }

}
