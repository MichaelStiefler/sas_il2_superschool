package utility;

import mainController.MainController;

public class IPAddress {

    public static boolean ipCompare(String firstIP, String compareIP) {
        boolean ipMatch = false;
        String[] firstIPSections;
        String[] compareIPSections;

        try {
            firstIPSections = firstIP.split("\\.");
            compareIPSections = compareIP.split("\\.");
            boolean sectionMatch = true;
            for (int i = 0; i < 4; i++) {
                int wildCardndx = compareIPSections[i].indexOf("*");
                if (wildCardndx < 0) {
                    if (!firstIPSections[i].equals(compareIPSections[i])) {
                        sectionMatch = false;
                        break;
                    }
                } else if (wildCardndx > 0) {
                    int checkToNdx = wildCardndx;
                    if (firstIPSections[i].length() < wildCardndx) {
                        sectionMatch = false;
                        break;
                    }
                    if (!firstIPSections[i].substring(0, checkToNdx).equals(compareIPSections[i].substring(0, checkToNdx))) {
                        sectionMatch = false;
                    }
                }
            }
            ipMatch = sectionMatch;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "IPAddress.ipCompare - Unhandled Exception firstIP(" + firstIP + ") compareIP(" + compareIP + ")");
        }
        return ipMatch;
    }

    public static boolean validateIPAddress(String ipAddress) {
        try {
            String[] parts = ipAddress.split("\\.");

            if (parts.length != 4) {
                // Must have 4 subsets (At this point)
                return false;
            }

            for (String s : parts) {
                if (!s.equals("*")) {
                    if (s.endsWith("*")) {
                        int wildCardndx = s.indexOf("*");
                        String xx = s.substring(0, wildCardndx);
                        int i = Integer.parseInt(xx);
                        if (wildCardndx == 2) {
                            if ((i < 0) || (i > 25)) {
                                return false;
                            }
                        } else {
                            if ((i < 0) || (i > 2)) {
                                return false;
                            }
                        }
                    } else {
                        int i = Integer.parseInt(s);
                        if ((i < 0) || (i > 255)) {
                            return false;
                        }
                    }
                }
            }

            return true;

        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "IPAddress.validateIPAddress - Unhandled Exception validating IP Address (" + ipAddress + ")");
            return false;
        }
    }

    public static void main(String[] args) {
        String ip1 = "192.168.1.133";
        String ipCompare = "192.2*.1.133";
        System.out.println("Match: " + IPAddress.ipCompare(ip1, ipCompare));

    }
}
