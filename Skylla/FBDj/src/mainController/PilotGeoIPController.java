package mainController;

import java.io.IOException;

import com.maxmind.geoip.LookupService;

class PilotGeoIPController {
    public static LookupService initialize(String geoDBFile) {
        LookupService newLookupService = null;

        try {
            newLookupService = new LookupService(geoDBFile, LookupService.GEOIP_MEMORY_CACHE);
            MainController.writeDebugLogFile(2, "PilotGeoIPController.initialize(): LookupService initialized.");
        } catch (IOException e) {
            MainController.writeDebugLogFile(1, "PilotGeoIPController.initialize(): IOException!\n" + e.toString());
        }

        return newLookupService;
    }

    public static String getCountry(String ipAddress) {
        String country = null;

        if (MainController.IPLOOKUPSERVICE != null) {
            country = MainController.IPLOOKUPSERVICE.getCountry(ipAddress).getName();
        } else {
            MainController.writeDebugLogFile(1, "PilotGeoIP.getCountry: LookupService is null!");
        }

        return country;
    }

	public static void main(String[] args) {
	}
}
