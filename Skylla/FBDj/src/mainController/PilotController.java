package mainController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import model.ConfigurationItem;
import model.GUICommand;
import model.Pilot;
import model.PilotBlueprint;
import model.PilotGeoIPInformation;
import model.PilotSortie;
import model.QueueObj;
import model.SortieEvent;
import utility.StringUtilities;
import utility.Time;
import utility.UnicodeFormatter;

class PilotController {

    public static void pilotAdd(String name, String ipAddress, int socket) {

        String unicodeName = name.trim();
        // Do not allow players with a "\" or "tab" imbedded in their name to connect
        if (name.contains("\\\\") || name.contains("\\t")) {
            MainController.writeDebugLogFile(1, "PilotController.pilotAdd - Pilot Kicked for having a '\\' or 'tab' in name: (" + name + ")");
            ServerCommandController.serverCommandSend("channel " + socket + " DESTROY");
        } else {
            try {
                if (name.contains("\\u")) {
                    unicodeName = UnicodeFormatter.convertAsciiStringToUnicode(name.trim());
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "PilotController.pilotAdd - Unhandled Exception processing Unicode in pilotName (" + name + "): " + ex);
            }
            // Instantiate pilot
            Pilot newPilot = new Pilot(unicodeName, ipAddress, socket, Time.getTime());
            newPilot.setAsciiTextName(name);
            pilotAddDataReceived(newPilot);
        }
    }

    public static void pilotDisconnect(int socket) {
        try {
            String pilotName = null;
            Iterator<String> it = MainController.PILOTS.keySet().iterator();
            while (it.hasNext()) {
                // Key is actually name but this is for clarity
                String key = it.next();
                if (socket == MainController.PILOTS.get(key).getSocket()) {
                    pilotName = key;
                    break;
                }
            }
            if (pilotName != null) {
                if (MainController.SORTIES.containsKey(pilotName)) {
                    PilotSortieController.sortieEnd(pilotName, SortieEvent.EventType.PILOTDISCONNECT, Time.getTime());
                } else {
                    pilotRemove(pilotName);
                }
            } else {
//				MainController.writeDebugLogFile(1, "PilotController.pilotDisconnect - Error Could not remove Pilot, not found for Socket("+socket+")");
            }

        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotController.pilotDisconnect - Error unhandled exception removing Socket (" + socket + "): " + ex);
        }
    }

    public static void pilotRemove(String name) {
        // Make sure the pilot exists
        if (MainController.PILOTS.containsKey(name)) {
            MainController.PILOTS.remove(name);
            MainController.writeDebugLogFile(2, "PilotController.removePilot: Pilot(" + name + ") removed.");
            PilotPanelController.removePilot(name);
        }
        // Pilot does not exist
        else {
            MainController.writeDebugLogFile(2, "PilotController.removePilot: Pilot(" + name + ") does not exist.");
        }
        // Since
        if (MainController.SORTIES.containsKey(name)) {
            MainController.SORTIES.remove(name);
        }
        MySQLConnectionController.userDisconnect(name, "Normal");

    }

    public static void pilotRemoveAll() {
        MainController.PILOTS.clear();
    }

    public static void pilotKick(String name, SortieEvent.EventType event) {
        try {
            // Make sure the pilot exists
            if (MainController.PILOTS.containsKey(name)) {
                MainController.PILOTS.get(name).setState(PilotBlueprint.ValidStates.KICK);
                // End sortie if exists
                if (MainController.SORTIES.containsKey(name)) {
                    PilotSortieController.sortieEnd(name, event, Time.getTime());
                } else {
                    ServerCommandController.serverCommandSend("channel " + MainController.PILOTS.get(name).getSocket() + " DESTROY");
                    MainController.writeDebugLogFile(1, "PilotController.kickPilot: " + MainController.PILOTS.get(name).getName() + " has been kicked!");
                    MainController.PILOTS.remove(name);
                    PilotPanelController.removePilot(name);
                    MySQLConnectionController.userDisconnect(name, "Kicked");
                }
            } else {
                // Pilot does not exist, but to make sure kick them from the server anyway
                MainController.writeDebugLogFile(2, "PilotController.kickPilot: Pilot does not exist ( " + name + " )");
                ServerCommandController.serverCommandSend("kick \"" + UnicodeFormatter.convertUnicodeToString(name) + "\"");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotController.kickPilot - Error kicking ( " + name + " ): " + ex);
        }
    }
    
  //---------------------------------------
    //TODO: skylla: slap    
    public static void pilotSlap(String name) {
    	 try {
             if (MainController.PILOTS.containsKey(name)) {
            	 // We don't need all that stats crap from the kick-method; we only want to slap someone ..
            	 ServerCommandController.serverCommandSend("slap \"" + UnicodeFormatter.convertUnicodeToString(name) + "\"");
             }
    	 } catch (Exception ex) {
             MainController.writeDebugLogFile(1, "PilotController.slapPilot - Error slapping ( " + name + " ): " + ex);
         }
    }
    
  //---------------------------------------

    public static boolean pilotValidateLogon(Pilot pilot) {
        // Check for ban
        if (PilotBanController.pilotBanConnectBanCheck(pilot)) {
            return true;
        }

        // Pilot is not on ban list
        return false;
    }

    // Validated that an admin or reserved name has logged in.
    // Once validated, they are removed from list,
    // or kicked if they fail validation after too many checks.
    public static void pilotValidateAuthentication() {
        try {
            LOOP: for (Iterator<String> it = MainController.AUTHENTICATIONQUEUE.iterator(); it.hasNext();) {
                String name = (String) it.next();

                boolean reservedNameAuthenticated = false;
                boolean adminAuthenticated = false;

                // Check if pilot still exists
                if (MainController.PILOTS.containsKey(name)) {

                    // Check if pilot is on reserved name list
                    if (MainController.RESERVEDNAMES.containsKey(name)) {
                        // Check if pilot is authenticated reserved name
                        if (MainController.PILOTS.get(name).isReservedName()) {
                            reservedNameAuthenticated = true;
                        }
                        // Not authenticated reserved name
                        else {
                            MainController.PILOTS.get(name).setReservedNameLoginCheck(MainController.PILOTS.get(name).getReservedNameLoginCheck() + 1);
                            // Check if too many checks
                            if (MainController.PILOTS.get(name).getReservedNameLoginCheck() > 10) {
                                // Kick
                                PilotController.pilotKick(name, SortieEvent.EventType.PILOTKICKED);
                                MainController.writeDebugLogFile(1, "PilotController.validateLogin: " + name + " kicked for not authenticating reserved name.");
                                it.remove();
                                continue LOOP;
                            }
                            // Send warning
                            else {
                                ServerCommandController.serverCommandSend("chat " + MainController.PILOTS.get(name).getAsciiTextName() + " using reserved name. Must login or will be kicked! TO \"" + MainController.PILOTS.get(name).getAsciiTextName()
                                        + "\"");
                                MainController.writeDebugLogFile(2, "PilotController.validateLogin: " + name + " has not authenticated reserved name. Check: " + MainController.PILOTS.get(name).getReservedNameLoginCheck());
                            }
                        }
                    }
                    // Pilot not a reserved name
                    else {
                        reservedNameAuthenticated = true;
                    }

                    // Check if pilot is on admin list
                    if (MainController.ADMINS.containsKey(name)) {
                        // Auto Login Check
                        if (MainController.ADMINS.get(name).isAdminHost(MainController.PILOTS.get(name).getIPAddress())) {
                            MainController.PILOTS.get(name).setAdmin(true);
                        }
                        // Check if pilot is authenticated admin
                        if (MainController.PILOTS.get(name).isAdmin()) {
                            adminAuthenticated = true;
                        }
                        // Not authenticated admin
                        else {
                            MainController.PILOTS.get(name).setAdminLoginCheck(MainController.PILOTS.get(name).getAdminLoginCheck() + 1);
                            // Check if too many checks
                            if (MainController.PILOTS.get(name).getAdminLoginCheck() > 5) {
                                // Kick
                                PilotController.pilotKick(name, SortieEvent.EventType.PILOTKICKED);
                                MainController.writeDebugLogFile(1, "PilotController.validateLogin: " + name + " kicked for not authenticating admin name.");
                                it.remove();
                                continue LOOP;
                            }
                            // Send warning
                            else {
                                ServerCommandController.serverCommandSend("chat " + MainController.PILOTS.get(name).getAsciiTextName() + " using admin name. Must login or will be kicked! TO \"" + MainController.PILOTS.get(name).getAsciiTextName() + "\"");
                                MainController.writeDebugLogFile(2, "PilotController.validateLogin: " + name + " has not authenticated admin name. Check: " + MainController.PILOTS.get(name).getAdminLoginCheck());
                            }
                        }
                    }
                    // Pilot not an admin
                    else {
                        adminAuthenticated = true;
                    }
                    // Check if authentication passed
                    if (reservedNameAuthenticated && adminAuthenticated) {
                        it.remove();
                        continue LOOP;
                    }
                }

                // Pilot is not on server
                else {
                    it.remove();
                    continue LOOP;
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotController.pilotValidateAuthentication - Error Unhandled exception: " + ex);
        }
    }

    public static void pilotFirstRun() {
        // This method is run when FBDj is started to load any pilots already connected to the Game.
        new CommandHost(null, CommandHost.HostActionType.FIRSTRUN);
    }

    public static void pilotAddDataReceived(Pilot pilot) {
        // This method process the data received for a pilot from a Host command issued to IL2
        try {
            // 1st make sure the pilot is not banned
            boolean invalid = PilotController.pilotValidateLogon(pilot);
            if (invalid) {
                MainController.writeIPAccessLogFile(pilot.getName() + " (Banned Being Kicked)", pilot.getIPAddress());
                pilot.setValidConnection(false);
                PilotController.pilotKick(pilot.getName(), SortieEvent.EventType.PILOTBANNED);
            } else {
                // OK Pilot can play, now get their Country based on the IP address
                MainController.writeIPAccessLogFile(pilot.getName(), pilot.getIPAddress());
                pilot.setValidConnection(true);

                pilotWelcomeMessage(pilot);

                // Check to see if Pilot is in the pilots List already, if not add
                if (!MainController.PILOTS.containsKey(pilot.getName())) {
                    MainController.PILOTS.put(pilot.getName(), pilot);
                    MainController.writeDebugLogFile(2, "PilotController.pilotAddDataReceived - Pilot (" + pilot.getName() + ") added to pilot list.");

                } else {
                    MainController.writeDebugLogFile(1, "PilotController.pilotAddDataReceived - Error Pilot to Add (" + pilot.getName() + ") already in Pilots List");
                }

                // Add Pilot to the GUI List
                PilotPanelController.addPilot(pilot.getName());

                // Check to see if Pilot must Authenticate him/herself
                MainController.AUTHENTICATIONQUEUE.add(pilot.getName());

                // Get the Pilots Stats ID from the Database
                pilot.setPilotId(MySQLConnectionController.getPilotId(pilot.getName()));

                // Check to see if their is a sortie for this pilot, if not create one
                if (MainController.SORTIES.containsKey(pilot.getName())) {
                    MainController.SORTIES.get(pilot.getName()).setPilotId(pilot.getPilotId());
                } else {
                    // Create a new Sortie for Pilot
                    PilotSortieController.newSortie(pilot.getName());
                    new CommandUserStat(pilot, CommandUserStat.UserStatActionType.NEWSORTIE, 0, 4000);
                }
            }
            MySQLConnectionController.userConnect(pilot);
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotController.pilotAddDataReceived - Error processing data: " + ex);
        }

    }

    public static void pilotGunStat(Pilot pilot) {
        try {
            String name = pilot.getAsciiTextName();
            long pctHit = 0;
            long pctHitAir = 0;
            int ead = pilot.getEAirConfirmed();
            int gud = pilot.getEGroundUnit() + pilot.getFGroundUnit();
            int bulletsFired = pilot.getFiBull();
            int bulletsHitGround = pilot.getHiBull();
            int bulletsHitAir = pilot.getHiABull();
            if (bulletsFired > 0) {
                pctHit = Math.round((Double.valueOf(bulletsHitGround) / Double.valueOf(bulletsFired) * 100));
                pctHitAir = Math.round((Double.valueOf(bulletsHitAir) / Double.valueOf((bulletsFired - (bulletsHitGround - bulletsHitAir))) * 100));
            } else {
                pctHit = 0;
                pctHitAir = 0;
            }
            ServerCommandController.serverCommandSend("chat EAD(" + ead + ") GUD(" + gud + ") Fired(" + bulletsFired + ") Hit(" + pctHit + "%) Hit-A(" + pctHitAir + "%) TO \"" + name + "\"");
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotController.pilotGunStat - Error Unhandled Exception pilot(" + pilot.getName() + "): " + ex);
        }
    }

    public static ArrayList<String> getPilotList() {
        String pilotString;
        ArrayList<String> pilots = new ArrayList<String>();
        Iterator<String> it = MainController.PILOTS.keySet().iterator();

        while (it.hasNext()) {
            // Key is actually name but this is for clarity
            String key = it.next();
            pilotString = "Pilot (" + key + ") IP(" + MainController.PILOTS.get(key).getIPAddress() + ") Socket(" + MainController.PILOTS.get(key).getSocket() + ")";
            pilots.add(pilotString);
        }

        return pilots;
    }

    public static void deathKick(Pilot pilot) {
        try {
            int deathKickLimit = MainController.CONFIG.getDeathKick();
            if (deathKickLimit != 0 && pilot.getDeathCount() >= deathKickLimit) {
                MainController.writeDebugLogFile(2, "PilotController.deathKick - Pilot Death Kick Reached for (" + pilot.getName() + ") # Deaths (" + pilot.getDeathCount() + ")");
                // Mark plane as SHOTDOWN if they are currently in one
                if (MainController.SORTIES.containsKey(pilot.getName())) {
                    MainController.SORTIES.get(pilot.getName()).setPlaneStatus(PilotSortie.PlaneStatus.SHOTDOWN);
                }
                if (MainController.CONFIG.getDeathKickTime() > 0) {
                    PilotBanController.pilotBanAdd(pilot.getName(), pilot.getIPAddress(), "Death Kick", MainController.CONFIG.getDeathKickTime() * 60000);
                } else {
                    pilotKick(pilot.getName(), SortieEvent.EventType.PILOTKICKED);
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotController.deathKick - Error Unhandled Exception pilot(" + pilot.getName() + "): " + ex);
        }
    }

    public static void markingsWarning(String pilotName, long sortieID, boolean kick) {
        final Timer timer = new Timer();

        class MarkingsWarning extends TimerTask {

            String  name;
            long    sortieID;
            boolean kick;
            int     counter;
            long    waitTime = MainController.CONFIG.getAutoKickTimer() * 1000;

            // Constructor passes in Pilot name and counter
            public MarkingsWarning(String name, int counter, long sortieID, boolean kick) {
                this.name = name;
                this.counter = counter;
                this.sortieID = sortieID;
                this.kick = kick;
            }

            public void run() {
                try {
                    // Make sure the Sortie is still there.
                    if (MainController.SORTIES.containsKey(name)) {
                        // Make sure we are working with the same sortie
                        if (MainController.SORTIES.get(name).getSortieStartTime() == sortieID) {
                            if (counter < 3) {
                                counter++;
                                String asciiName = MainController.PILOTS.get(name).getAsciiTextName();
                                if (kick) {
                                    ServerCommandController.serverCommandSend("chat CHANGE MARKINGS OR BE KICKED !!! TO \"" + asciiName + "\"");
//									ServerCommandController.serverCommandSend("chat Ð’ÐºÐ»ÑŽÑ‡Ð°Ð¹Ñ‚Ðµ Ð¿Ñ€Ð°Ð²Ð¸Ð»ÑŒÐ½ÑƒÑŽ Ð¾ÐºÑ€Ð°Ñ�ÐºÑƒ Ð¸ Ð¼Ð°Ñ€ÐºÐ¸Ñ€Ð¾Ð²ÐºÑƒ Ð¸Ð»Ð¸ Ð’Ð°Ñ� ÐºÐ¸ÐºÐ½ÑƒÑ‚  !!! TO \"" + name + "\"");
                                } else {
                                    ServerCommandController.serverCommandSend("chat Please Change Markings and use 'Default' skin!! TO \"" + asciiName + "\"");
                                }
                                timer.schedule(new MarkingsWarning(name, counter, sortieID, kick), waitTime);
                            } else {
                                if (kick) {
                                    String armyString = "RED";
                                    if (MainController.SORTIES.get(name).getArmy() == MainController.BLUEARMY) {
                                        armyString = "BLUE";
                                    }
                                    MainController.writeDebugLogFile(1, "Player (" + name + ") Kicked for wrong markings " + armyString + " ( " + MainController.SORTIES.get(name).getPlane() + "/" + MainController.SORTIES.get(name).getPlaneMarkings()
                                            + " )");
                                    GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.KICKPILOTMARKINGS);
                                    newGUICommand.setName(name);
                                    MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "PilotController.markingsWarning - Unhandled Exception for (" + name + ")");
                }
            }

        } // MarkingsWarning Class

        // When markingsWarning method is called 1st time put it on the timer
        timer.schedule(new MarkingsWarning(pilotName, 0, sortieID, kick), 1);

    } // End markingsWarning method

    public static void badLanguageCheck(String playerName) {
        if (MainController.PILOTS.containsKey(playerName)) {
            String asciiName = MainController.PILOTS.get(playerName).getAsciiTextName();
            ServerCommandController.serverCommandSend("chat WARNING - DO NOT USE FOUL LANGUAGE IN CHAT BAR TO \"" + asciiName + "\"");
            MainController.PILOTS.get(playerName).setBadWordCount(MainController.PILOTS.get(playerName).getBadWordCount() + 1);
            if (MainController.CONFIG.getBadLanguageKick() > 0 && MainController.PILOTS.get(playerName).getBadWordCount() >= MainController.CONFIG.getBadLanguageKick()) {
                // User will be kicked for foul language. Send a message to player then wait 8 seconds before
                // initiating the kick
                ServerCommandController.serverCommandSend("chat !! NOTICE !! - Being Kicked for Foul Language TO \"" + asciiName + "\"");
                ServerCommandController.serverCommandSend("chat !! NOTICE !! - Being Kicked for Foul Language TO \"" + asciiName + "\"");
                try {
                    Thread.sleep(8000);
                } catch (Exception ex) {}
                MainController.writeDebugLogFile(1, "Player (" + playerName + ") Kicked for Bad Language");
                GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.KICKPILOTLANGUAGE);
                newGUICommand.setName(playerName);
                MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
            }
        }
    }

    public static String convertUnicodeName(String name) {
//		String returnName = null;
        boolean finished = false;
        String newName = "";
        String tempName = name;
//		int startIndex = 0;
        try {
            while (!finished) {
                int unicodeIndex = tempName.indexOf("\\u");
                if (unicodeIndex > tempName.length() || unicodeIndex < 0) {
                    finished = true;
                    if (tempName.length() > 0) {
                        newName = newName + tempName;
                    }
                } else {
                    newName = newName + tempName.substring(0, unicodeIndex);
//					String asciiText = tempName.substring(unicodeIndex+2, unicodeIndex+6);

                    tempName = tempName.substring(unicodeIndex + 6);
                }
            }

            return newName;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotController.convertUnicodeName - Unhandled Exception processing Unicode in pilotName (" + name + "): " + ex);
            return name;
        }
    }

    public static void pilotWelcomeMessage(Pilot pilot) {
        try {
            String welcomeMessage = MainController.CONFIG.getWelcomeMessage();
            if (welcomeMessage.contains("{pilotName}")) {
                welcomeMessage = StringUtilities.stringReplace(welcomeMessage, "{pilotName}", pilot.getAsciiTextName());
            }
            
            PilotGeoIPInformation pilotGeoIPInformation = new PilotGeoIPInformation(pilot);
            
            boolean hasIpInfo = pilotGeoIPInformation.getGeoIpData().getStatus().equalsIgnoreCase("success");
            boolean needsIpInfo = false;
            
            String[] infoItems = { "continent",
                    "country",
                    "countryCode",
                    "region",
                    "city",
                    "org",
                    "ipType"
            };
            
            for (int infoIndex=0; infoIndex<infoItems.length; infoIndex++) {
                String infoItem = "{" + infoItems[infoIndex] + "}";
                if (welcomeMessage.contains(infoItem)) {
                    needsIpInfo = true;
                    if (hasIpInfo) {
                        if (infoItems[infoIndex].equalsIgnoreCase("ipType")) {
                            String infoText = pilotGeoIPInformation.getGeoIpData().getIpType();
                            if (!pilotGeoIPInformation.getGeoIpData().getIpType().equalsIgnoreCase("Residential")
                                && !pilotGeoIPInformation.getGeoIpData().getIpType().equalsIgnoreCase("N/A")) {
                                infoText += " (" + pilotGeoIPInformation.getGeoIpData().getBusinessName() + ")";
                            }
                            welcomeMessage = StringUtilities.stringReplace(welcomeMessage,
                                    infoItem,
                                    infoText);
                        } else {
                            welcomeMessage = StringUtilities.stringReplace(welcomeMessage,
                                    infoItem,
                                    pilotGeoIPInformation.getGeoIpData().getCustomFieldValue(infoItems[infoIndex]));
                        }
                    } else {
                        welcomeMessage = StringUtilities.stringReplace(welcomeMessage,
                                infoItem, "N/A");
                    }
                }
            }
            
            int trustLevel = pilotGeoIPInformation.trustLevelPercent();
            if (welcomeMessage.contains("{trustLevel}")) {
                welcomeMessage = StringUtilities.stringReplace(welcomeMessage, "{trustLevel}", "" + trustLevel);
            }
            if (needsIpInfo && !hasIpInfo) {
                welcomeMessage += " (Error: " + pilotGeoIPInformation.getGeoIpData().getMessage() + ")";
            }
                        
            String receipient = "";
            if (MainController.CONFIG.getSendWelcomeMessageTo() == ConfigurationItem.ChatReciepients.PILOT) {
                receipient = pilot.getAsciiTextName();
            } else if (MainController.CONFIG.getSendWelcomeMessageTo() == ConfigurationItem.ChatReciepients.ARMY) {
                receipient = "ARMY " + Integer.toString(pilot.getArmy());
            } else if (MainController.CONFIG.getSendWelcomeMessageTo() == ConfigurationItem.ChatReciepients.ALL) {
                receipient = "ALL";
            }

            if (welcomeMessage != null && receipient.length() > 0) {
                ServerCommandController.serverCommandSend("chat Pilot " + welcomeMessage + " TO \"" + receipient + "\"");
            }
            pilot.setCountry(pilotGeoIPInformation.getGeoIpData().getCountry());
            
            if (pilot.getAsciiTextName().length() < 3) {
                if (receipient.length() > 0) 
                    ServerCommandController.serverCommandSend("chat " + pilot.getAsciiTextName() + " gets kicked (name invalid!) TO \"" + receipient + "\"");
                PilotController.pilotKick(pilot.getName(), SortieEvent.EventType.PILOTKICKED);

                MainController.writeDebugLogFile(1, "AdminCommandController.kick: kicked (" + pilot.getAsciiTextName() + "), ASCII name has less than 3 characters!");
                
            }
            
            if (!pilotGeoIPInformation.isTrusted()) {
                if (receipient.length() > 0) 
                    ServerCommandController.serverCommandSend("chat " + pilot.getAsciiTextName() + " gets kicked (abuse) TO \"" + receipient + "\"");
                PilotController.pilotKick(pilot.getName(), SortieEvent.EventType.PILOTKICKED);

                MainController.writeDebugLogFile(1, "AdminCommandController.kick: kicked (" + pilot.getAsciiTextName() + "), AbuseIPDB returned trust level " + trustLevel + "%% !");
                
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotController.pilotWelcomeMessage - Error Unhandled Exception for Pilot (" + pilot.getName() + ") ex:" + ex);
        }
    }

}
