package mainController;

import java.util.HashMap;
import java.util.Iterator;

import model.Pilot;
import model.PilotBan;
import model.SortieEvent;
import utility.Time;

public class PilotBanController {

    public static void pilotBanAdd(String name, String ipAddress, String reason, long banDuration) {
        String banKey = name + "(IP)" + ipAddress;
        if (MainController.BANNEDPILOTS.containsKey(banKey)) {
            MainController.writeDebugLogFile(1, "PilotBanController.pilotBanAdd: Ban for Name(" + name + ") IP(" + ipAddress + ") already exists");
        } else {
            // Create new ban
            long timestamp = Time.getTime();
            MainController.BANNEDPILOTS.put(banKey, new PilotBan(name, ipAddress, reason, timestamp, banDuration));
            MainController.writeDebugLogFile(1, "PilotBanController.pilotBanAdd: Ban Added for Name(" + name + ") IP(" + ipAddress + ")");

            // Write new ban list
            PilotBanController.pilotBansWrite();

            // Kick pilot off the server
            PilotController.pilotKick(name, SortieEvent.EventType.PILOTBANNED);
        }
    }

    public static void pilotBanRemove(String name, String ipAddress, String removeKey) {
        boolean found = false;

        if (removeKey != null) {
            if (MainController.BANNEDPILOTS.containsKey(removeKey))
                MainController.BANNEDPILOTS.remove(removeKey);
            found = true;
        }
        // We have an ip to work with
        else if (ipAddress != null) {
            String banKey = name + "(IP)" + ipAddress;
            // Remove banned player by ip key if exists
            if (MainController.BANNEDPILOTS.containsKey(banKey))
                MainController.BANNEDPILOTS.remove(banKey);
            found = true;
        }
        // Only got a name
        else {
            // Iterate through hash to find that name
            Iterator<String> it = MainController.BANNEDPILOTS.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                // If name found, remove it
                if (MainController.BANNEDPILOTS.get(key).getName().equals(name)) {
                    it.remove();
                    found = true;
                }
            }
        }

        // Write new ban list
        if (found) {
            PilotBanController.pilotBansWrite();
            MainController.writeDebugLogFile(1, "PilotBanController.pilotBanRemove: Name(" + name + ") IP(" + ipAddress + ") Removed from ban list.");
        } else {
            MainController.writeDebugLogFile(1, "PilotBanController.pilotBanRemove: Could not remove ban for Name(" + name + ") IP(" + ipAddress + ") ** Not found in list **");
        }
    }

    public static boolean pilotBanConnectBanCheck(Pilot pilot) {
        boolean isBanned = false;
        String returnKey = null;
        String banKey = pilot.getName() + "(IP)" + pilot.getIPAddress();

        // Check to see if the Pilot is banned by Name/IP or Both
        if (MainController.BANNEDPILOTS.containsKey(banKey) || (returnKey = validateBan(pilot)) != null) {
            if (!MainController.RESERVEDNAMES.containsKey(pilot.getName())) {
                if (returnKey != null) {
                    banKey = returnKey;
                }
                // Check permanent ban
                if (MainController.BANNEDPILOTS.get(banKey).getDuration() == 0) {
                    isBanned = true;
                    MainController.writeDebugLogFile(2, "PilotBanController.pilotBanConnectBanCheck: Name( " + pilot.getName() + " ) IP( " + pilot.getIPAddress() + " ) Permanently banned.");
                }
                // Check if ban is still in effect
                else {
                    // Ban is still effective
                    if (Time.getTime() < (MainController.BANNEDPILOTS.get(banKey).getTimestamp() + MainController.BANNEDPILOTS.get(banKey).getDuration())) {
                        isBanned = true;
                        MainController.writeDebugLogFile(2, "PilotBanController.pilotBanConnectBanCheck: Name( " + pilot.getName() + " ) IP( " + pilot.getIPAddress() + " ) Ban time is still in effect.");
                    }
                    // Ban is over
                    else {
                        PilotBanController.pilotBanRemove(pilot.getName(), pilot.getIPAddress(), banKey);
                        pilot.setConnectionComment("Ban Time is Up, Ban Removed");
                        MainController.writeDebugLogFile(2, "PilotBanController.pilotBanConnectBanCheck: Removing Ban for Name( " + pilot.getName() + " ) IP( " + pilot.getIPAddress() + " ). Ban time limit reached.");
                    }
                }
            } else {
                MainController.writeDebugLogFile(1, "PilotBanController.pilotBanConnectBanCheck: Name( " + pilot.getName() + " ) IP( " + pilot.getIPAddress() + " ) Would be banned but found in reserved Name List");
                pilot.setConnectionComment("Ban Override by Reserved Name");
            }
        }
        return isBanned;
    }

    public static String validateBan(Pilot pilot) {
        Iterator<String> it = MainController.BANNEDPILOTS.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String banIP = MainController.BANNEDPILOTS.get(key).getIpAddress();
            String banName = MainController.BANNEDPILOTS.get(key).getName();
            if (banName != null && banName.length() > 0) {
                if (pilot.getName().equals(banName)) {
                    MainController.writeDebugLogFile(2, "PilotBanController.validateBan: Pilot Banned by Name(" + pilot.getName() + ")");
                    pilot.setConnectionComment("Pilot Banned by Name");
                    return key;
                }
            }
            if (banIP != null && banIP.length() > 0) {
                if (utility.IPAddress.ipCompare(pilot.getIPAddress(), banIP)) {
                    MainController.writeDebugLogFile(2, "PilotBanController.validateBan: Pilot Banned by IP(" + pilot.getIPAddress() + ")");
                    pilot.setConnectionComment("Pilot Banned by IP Address");
                    return key;
                }

            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, PilotBan> pilotBansLoad() {
        HashMap<String, PilotBan> tempBanList;

        if ((tempBanList = (HashMap<String, PilotBan>) FileController.fileReadSerialized(MainController.CONFIG.getConfigDirectory() + MainController.PILOTBANFILENAME)) == null) {
            tempBanList = new HashMap<String, PilotBan>();
            MainController.writeDebugLogFile(1, "PilotBanController.pilotBansLoad: Could not find ban list. Creating new list.");
        } else {
            int banCount = tempBanList.size();
            MainController.writeDebugLogFile(1, "PilotBanController.pilotBansLoad: Loaded (" + banCount + ") Banned Names/IPAddress from file");
        }

        return tempBanList;
    }

    public static void pilotBansWrite() {
        FileController.fileWriteSerialized(MainController.CONFIG.getConfigDirectory(), MainController.PILOTBANFILENAME, MainController.BANNEDPILOTS);
        MainController.writeDebugLogFile(2, "PilotBanController.pilotBansWrite: Ban list written to file.");
    }
}
