package com.maxmind.geoip;

/* CityLookupTest.java */

import java.io.IOException;

import utility.StringUtilities;

/* sample of how to use the GeoIP Java API with GeoIP City database */
/* Usage: java CityLookupTest 64.4.4.4 */

class CityLookupTest {

    public static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    public static void main(String[] args) {
        try {
            LookupService cl = new LookupService("GeoLiteCity.dat", LookupService.GEOIP_MEMORY_CACHE);
            Location l2 = cl.getLocation("98.117.44.105");
//            Location l2 = cl.getLocation(args[0]);
            System.out.println("countryCode: " + l2.countryCode + "\n countryName: " + l2.countryName + "\n region: " + l2.region + "\n regionName: " + regionName.regionNameByCode(l2.countryCode, l2.region) + "\n city: " + l2.city + "\n postalCode: "
                    + l2.postalCode + "\n latitude: " + l2.latitude + "\n longitude: " + l2.longitude +
//                               "\n distance: " + l2.distance(l1) +
//                               "\n distance: " + l1.distance(l2) + 
                    "\n metro code: " + l2.metro_code + "\n area code: " + l2.area_code + "\n timezone: " + timeZone.timeZoneByCountryAndRegion(l2.countryCode, l2.region));

            String welcomeMessage = "Welcome {pilotName} from {city}, {country}";
            System.out.println("Welcome:" + welcomeMessage);
            if (welcomeMessage.contains("{pilotName}")) {
                welcomeMessage = replace(welcomeMessage, "{pilotName}", "Willie");
            }
            if (welcomeMessage.contains("{countryCode}")) {
                welcomeMessage.replaceAll("countryCode", l2.countryCode);
            }
            if (welcomeMessage.contains("{country}")) {
                welcomeMessage.replaceAll("country", l2.countryName);
            }
            if (welcomeMessage.contains("{city}")) {
                welcomeMessage = StringUtilities.stringReplace(welcomeMessage, "{city}", l2.city);
            }
            if (welcomeMessage.contains("{regionName}")) {
                welcomeMessage.replaceAll("regionName", regionName.regionNameByCode(l2.countryCode, l2.region));
            }
            if (welcomeMessage.contains("{region}")) {
                welcomeMessage.replaceAll("region", l2.region);
            }
            System.out.println("Welcome:" + welcomeMessage);

            cl.close();
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

}
