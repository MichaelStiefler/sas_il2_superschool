package mainController;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import model.MissionFile;
import model.SortieEvent;
import utility.Time;
import utility.UnicodeFormatter;

class AdminCommandController {
    private enum AdminCommands {
        ADMINADD, ADMINLOG, ADMINREMOVE, BANADD, BANREMOVE, CHATLOG, CONSOLE, FBDJSTOP, KICK, MISSIONRESTART, MISSIONTIMELEFT, LOADMISSION, PASSWORDCHANGE, PINGKICK, RESERVEDNAMEADD, RESERVEDNAMEREMOVE, VETO
    }

    public static void adminCommandLogin(String name, String password, String ipAddress) throws UnknownHostException {

        try {

            String trimmedName = name.trim();
            String asciiName = MainController.PILOTS.get(trimmedName).getAsciiTextName();
            MainController.writeDebugLogFile(2, "AdminCommandLogin name: (" + name + ")");
            MainController.writeDebugLogFile(2, "AdminCommandLogin trimmedName: (" + trimmedName + ")");
            if (asciiName != null)
                MainController.writeDebugLogFile(2, "AdminCommandLogin asciiName: (" + asciiName + ")");

            // Check for admin in list
            if (MainController.ADMINS.containsKey(trimmedName)) {
                MainController.writeDebugLogFile(2, "AdminCommandLogin found name in Admin list");
                // Check command access
                if (MainController.ADMINS.get(trimmedName).isLogin()) {
                    // Check password
                    if (MainController.ADMINS.get(trimmedName).getPassword().equals(password)) {
                        MainController.PILOTS.get(trimmedName).setAdmin(true);
                        ServerCommandController.serverCommandSend("chat Logged in as Admin TO \"" + asciiName + "\"");
                        MainController.writeDebugLogFile(2, "AdminCommands.login: " + name + ": successfully logged in as admin.");
                    }
                    // Wrong password
                    else {
                        MainController.PILOTS.get(trimmedName).setBadLogin(MainController.PILOTS.get(trimmedName).getBadLogin() + 1);
                        ServerCommandController.serverCommandSend("chat Invalid Login Attempt TO \"" + asciiName + "\"");
                        MainController.writeDebugLogFile(2, "AdminCommands.login: " + name + ": entered the incorrect admin password. Attempt: " + MainController.PILOTS.get(trimmedName).getBadLogin());
                    }
                }
                // Cannot access this command
                else {
                    ServerCommandController.serverCommandSend("chat \"" + asciiName + "\" does not have access to this command!");
                    MainController.writeDebugLogFile(1, "AdminCommandController.login: " + trimmedName + ": does not have access to login command!");
                }
            }
            // Not in list
            else {
                MainController.writeDebugLogFile(2, "AdminCommandLogin ** NOT found ** name in Admin list");
                MainController.PILOTS.get(trimmedName).setBadLogin(MainController.PILOTS.get(trimmedName).getBadLogin() + 1);
                ServerCommandController.serverCommandSend("chat \"" + asciiName + "\" is not an Admin!");
                MainController.writeDebugLogFile(1, "AdminCommandController.login: " + name + ": is not an admin! Attempt: " + MainController.PILOTS.get(trimmedName).getBadLogin());
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AdminCommandController.login: Unhandled Exception during login of (" + name + "): " + ex);
        }
    }

    // MUST ALREADY BE ADMIN TO USE COMMANDS BELOW

    // Password Change
    public static void adminCommandPasswordChange(String name, String newPassword) {

        if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.PASSWORDCHANGE)) {
            AdminController.adminPasswordChange(name, newPassword);
            String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

            ServerCommandController.serverCommandSend("chat Password changed to (" + newPassword + ") TO \"" + asciiName + "\"");
            MainController.writeDebugLogFile(1, "AdminCommandController.passwordChange: " + name + ": Password changed to (" + newPassword + ")");
        } else {
            MainController.writeDebugLogFile(1, "AdminCommandController.passwordChange: " + name + ": failed admin validation!");
        }
    }

    public static void adminCommandPlayerList(String name) {
        ArrayList<String> players = null;
        String trimmedName = name.trim();
        if (AdminCommandController.adminCommandValidateAdmin(trimmedName, AdminCommands.PASSWORDCHANGE)) {
            players = PilotController.getPilotList();
            String asciiName = MainController.PILOTS.get(trimmedName).getAsciiTextName();

            for (String player : players) {
                ServerCommandController.serverCommandSend("chat " + player + " TO \"" + asciiName + "\"");
            }

        } else {
            MainController.writeDebugLogFile(1, "AdminCommandController.PlayerList: " + name + ": failed admin validation!");
        }
    }

    // Kick
    public static void adminCommandKick(String name, String kickName) {
        String pilotToKick = null;

        String unicodeKickName = kickName;
        if (kickName.contains("\\u")) {
            unicodeKickName = UnicodeFormatter.convertAsciiStringToUnicode(kickName);
        }
        // Make sure Admin has Kick privilages
        try {
            if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.KICK)) {
                String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

                // Check to see if Pilot to kick is inGame
                if (MainController.PILOTS.containsKey(unicodeKickName)) {
                    pilotToKick = unicodeKickName;
                } else {   // Didn't find pilot before so look to see if Admin entered a Socket # to kick
                    try {
                        int socket = Integer.valueOf(kickName);
                        // Loop through Pilots to get pilot name associated with socket #
                        Iterator<String> it = MainController.PILOTS.keySet().iterator();
                        while (it.hasNext()) {
                            // Key is actually name but this is for clarity
                            String key = it.next();
                            if (socket == MainController.PILOTS.get(key).getSocket()) {
                                pilotToKick = key;
                                break;
                            }
                        }
                    } catch (Exception ex) {
//						System.out.println("Entered value is name");
                    }
                }
                // Make sure pilot is still on server
                if (pilotToKick != null) {
                    // Make sure kicked pilot is not an admin
                    if (!MainController.PILOTS.get(pilotToKick).isAdmin()) {
                        PilotController.pilotKick(pilotToKick, SortieEvent.EventType.PILOTKICKED);

                        MainController.writeDebugLogFile(1, "AdminCommandController.kick: " + name + " kicked (" + pilotToKick + ")");
                    }
                    // Pilot being kicked is admin
                    else {
                        ServerCommandController.serverCommandSend("chat " + pilotToKick + " is an Admin and cannot be kicked! TO \"" + asciiName + "\"");
                        MainController.writeDebugLogFile(1, "AdminCommandController.kick: " + name + ": failed to kick (" + pilotToKick + ")! Player is an admin.");
                    }
                } else {
                    ServerCommandController.serverCommandSend("chat Cannot kick " + unicodeKickName + ", not Found in player list TO \"" + asciiName + "\"");
                }
            }
            // Failed validation
            else {
                MainController.writeDebugLogFile(1, "AdminCommandController.kick: " + name + ": failed admin validation!");
            }

        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AdminCommandController.kick - Error Unhandled Exception kicking Pilot (" + kickName + ") :" + ex);
        }

    }
    
  //--------------------------------------
    //TODO: skylla: slap:
    public static void adminCommandSlap(String name, String slapName) {
    	String pilotToSlap = null;
        String unicodeSlapName = slapName;
        String asciiName = MainController.PILOTS.get(name).getAsciiTextName();
        if (slapName.contains("\\u")) {
            unicodeSlapName = UnicodeFormatter.convertAsciiStringToUnicode(slapName);
        }
        try {
        	//You need kick privileges to slap someone:
            if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.KICK)) {
            	//String asciiName = MainController.PILOTS.get(name).getAsciiTextName();
                if (MainController.PILOTS.containsKey(unicodeSlapName)) {
                    pilotToSlap = unicodeSlapName;
                } else { 
                    try {
                        int socket = Integer.valueOf(slapName);
                        Iterator<String> it = MainController.PILOTS.keySet().iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            if (socket == MainController.PILOTS.get(key).getSocket()) {
                                pilotToSlap = key;
                                break;
                            }
                        }
                    } catch (Exception ex) {
                    	MainController.writeDebugLogFile(1, "AdminCommandController.slap: " + name + ": failed to slap (" + pilotToSlap + ")! Cannot Interpret Input!.");
                    }
                }
                if (pilotToSlap != null) {
                    if (!MainController.PILOTS.get(pilotToSlap).isAdmin() && !MainController.PILOTS.get(slapName).isAdmin() && !MainController.PILOTS.get(unicodeSlapName).isAdmin()) {
                        PilotController.pilotSlap(pilotToSlap);
                        MainController.writeDebugLogFile(1, "AdminCommandController.slap: " + name + " slapped (" + pilotToSlap + ")");
                    }
                    else {
                        ServerCommandController.serverCommandSend("chat " + pilotToSlap + " is an Admin and cannot be slapped! TO \"" + asciiName + "\"");
                        MainController.writeDebugLogFile(1, "AdminCommandController.slap: " + name + ": failed to slap (" + pilotToSlap + ")! Player is an admin.");
                    }
                } else {
                    ServerCommandController.serverCommandSend("chat Cannot slap " + unicodeSlapName + ", not Found in player list TO \"" + asciiName + "\"");
                }
            } else {
                    MainController.writeDebugLogFile(1, "AdminCommandController.slap: " + name + ": failed admin validation! Will now slap back!");
                    if (name.contains("\\u")) {
                        name = UnicodeFormatter.convertAsciiStringToUnicode(name);
                    }
                    if(name != null) {
                    	ServerCommandController.serverCommandSend("chat No Admin .. no slapping! You can only receive .. TO \"" + asciiName + "\"");
                    	PilotController.pilotSlap(name);
                    }          	
            }
        } catch(Exception ex) {
        	MainController.writeDebugLogFile(1, "AdminCommandController.slap - Error Unhandled Exception slapping Pilot (" + slapName + ") :" + ex);
        }
    }
    
    //TODO: skylla: implement multi-slap
    public static void adminCommandMultiSlap(String name, String slapName, String slapNumber) {
    	String asciiName = MainController.PILOTS.get(name).getAsciiTextName();
    	if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.KICK)) {
    		int num;
    		try {
    			num = Integer.parseInt(slapNumber);
    			if(num > 0 && num <= 60) {
    				for(int i = 0; i<num; i++) {
    					adminCommandSlap(name, slapName);
    				}
    			} else {
    				ServerCommandController.serverCommandSend("chat Slap Number out of range: Min 1, Max 60 TO \"" + asciiName + "\"");
        			MainController.writeDebugLogFile(1, "AdminCommandController.multiSlap - slap number too high!");
    			}
    		} catch (NumberFormatException e) {
    			ServerCommandController.serverCommandSend("chat cannot interpret parameter two TO \"" + asciiName + "\"");
    			MainController.writeDebugLogFile(1, "AdminCommandController.multiSlap - Cannot interpret slapNumber!");
    		}
    	} else {
    		ServerCommandController.serverCommandSend("chat No Admin .. no slapping! You can only receive .. TO \"" + asciiName + "\"");
        	PilotController.pilotSlap(name);
    	}
    }  
  //--------------------------------------

    // Ban Add
    public static void adminCommandBanAdd(String name, String banName, int banTime) {
        String pilotToBan = null;
        String unicodeBanName = banName;
        if (banName.contains("\\u")) {
            unicodeBanName = UnicodeFormatter.convertAsciiStringToUnicode(banName);
        }
        // Ban Duration is in Milliseconds so Multiple entered time (in hours) to get Milliseconds
        long banDuration = banTime * 3600000;

        if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.BANADD)) {
            String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

            // Make sure pilot is still on server
            if (MainController.PILOTS.containsKey(unicodeBanName)) {
                pilotToBan = unicodeBanName;
            } else {   // Didn't find pilot before so look to see if Admin entered a Socket # to kick
                try {
                    int socket = Integer.valueOf(banName);
                    // Loop through Pilots to get pilot name associated with socket #
                    Iterator<String> it = MainController.PILOTS.keySet().iterator();
                    while (it.hasNext()) {
                        // Key is actually name but this is for clarity
                        String key = it.next();
                        if (socket == MainController.PILOTS.get(key).getSocket()) {
                            pilotToBan = key;
                            break;
                        }
                    }
                } catch (Exception ex) {
//					System.out.println("Entered value is name");
                }
            }
            // Make sure pilot is still on server
            if (pilotToBan != null) {
                // Make sure banned pilot is not an admin
                if (!MainController.PILOTS.get(pilotToBan).isAdmin()) {
                    // Make sure banned pilot doesn't already exist
                    if (!MainController.BANNEDPILOTS.containsKey(MainController.PILOTS.get(pilotToBan).getIPAddress())) {
                        PilotBanController.pilotBanAdd(pilotToBan, MainController.PILOTS.get(pilotToBan).getIPAddress(), name, banDuration);
                        ServerCommandController.serverCommandSend("chat Ban Processed for ( " + pilotToBan + " ) TO \"" + asciiName + "\"");

                        MainController.writeDebugLogFile(1, "AdminCommandController.banAdd: " + name + ": banned " + pilotToBan);
                    }
                    // Banned pilot already exists
                    else {
                        ServerCommandController.serverCommandSend("chat Cannot ban " + pilotToBan + "! Ban already exists. TO \"" + asciiName + "\"");
                        MainController.writeDebugLogFile(1, "AdminCommandController.banAdd: " + name + ": failed to ban " + pilotToBan + "! Ban already exists.");
                    }
                }
                // Banned pilot is admin
                else {
                    ServerCommandController.serverCommandSend("chat Cannot ban " + banName + "! They are an admin. TO \"" + asciiName + "\"");
                    MainController.writeDebugLogFile(1, "AdminCommandController.banAdd: GUI: " + name + ": failed to ban (" + unicodeBanName + ")! Player ia an admin.");
                }
            } else {
                // Banned pilot not on server
                ServerCommandController.serverCommandSend("chat Cannot ban " + banName + "! Not found in Player List TO \"" + asciiName + "\"");
                MainController.writeDebugLogFile(1, "AdminCommandController.banAdd: " + name + ": failed to ban (" + unicodeBanName + ")! Player is not on the server.");
            }
        } else {
            MainController.writeDebugLogFile(1, "AdminCommandController.banAdd: " + name + ": failed admin validation!");
        }
    }

    // Ban Remove
    public static void adminCommandBanRemove(String name, String banName) {
        String unicodeBanName = banName;
        if (banName.contains("\\u")) {
            unicodeBanName = UnicodeFormatter.convertAsciiStringToUnicode(banName);
        }

        if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.BANREMOVE)) {
            String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

            PilotBanController.pilotBanRemove(unicodeBanName, null, null);

            ServerCommandController.serverCommandSend("chat Ban removed for ( " + banName + " ) TO \"" + asciiName + "\"");
            MainController.writeDebugLogFile(1, "AdminCommandController.banRemove: " + name + ": removed ban for " + unicodeBanName);
        } else {
            MainController.writeDebugLogFile(1, "AdminCommandController.banRemove: " + name + ": failed admin validation!");
        }
    }

    // Kick
    public static void adminCommandMarkingsWarning(String name, String playerName, boolean kick) {
        String pilotToWarn = null;

        String unicodePlayerName = playerName;
        if (playerName.contains("\\u")) {
            unicodePlayerName = UnicodeFormatter.convertAsciiStringToUnicode(playerName);
        }
        // Make sure Admin has Markings Warning privilages (Tied to Kick privilage
        try {
            if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.KICK)) {
                String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

                // Check to see if Pilot to send warning to is inGame
                if (MainController.PILOTS.containsKey(unicodePlayerName)) {
                    pilotToWarn = unicodePlayerName;
                } else {   // Didn't find pilot before so look to see if Admin entered a Socket # to kick
                    try {
                        int socket = Integer.valueOf(playerName);
                        // Loop through Pilots to get pilot name associated with socket #
                        Iterator<String> it = MainController.PILOTS.keySet().iterator();
                        while (it.hasNext()) {
                            // Key is actually name but this is for clarity
                            String key = it.next();
                            if (socket == MainController.PILOTS.get(key).getSocket()) {
                                pilotToWarn = key;
                                break;
                            }
                        }
                    } catch (Exception ex) {
//						MainController.writeDebugLogFile(1, "AdminCommandController.markingsWarning - Error Unhandled Exception finding Pilot for socket ("+playerName+") :"+ ex);
                    }
                }
                // Make sure pilot is still on server
                if (pilotToWarn != null) {
                    long sortieId = 0;
                    if (MainController.SORTIES.containsKey(pilotToWarn)) {
                        sortieId = MainController.SORTIES.get(pilotToWarn).getSortieStartTime();
                    }
                    if (sortieId != 0) {
                        PilotController.markingsWarning(pilotToWarn, sortieId, kick);
                        if (kick) {
                            ServerCommandController.serverCommandSend("chat Markings Warning Kick for (" + playerName + ") sent TO \"" + asciiName + "\"");
                            MainController.writeDebugLogFile(1, "AdminCommandController.markingsWarning: " + name + " Sent Markings Warning & Kick to (" + pilotToWarn + ")");
                        } else {
                            ServerCommandController.serverCommandSend("chat Markings Warning for (" + playerName + ") sent TO \"" + asciiName + "\"");
                            MainController.writeDebugLogFile(1, "AdminCommandController.markingsWarning: " + name + " Sent Markings Warning to (" + pilotToWarn + ")");
                        }
                    } else {
                        ServerCommandController.serverCommandSend("chat Error No Sortie Info Sending Markings Warning to (" + playerName + ") sent TO \"" + asciiName + "\"");
                        MainController.writeDebugLogFile(1, "AdminCommandController.markingsWarning: " + name + " No Sortie to Send Markings Warning to (" + pilotToWarn + ")");
                    }
                } else {
                    ServerCommandController.serverCommandSend("chat Cannot Send Markings Warning, (" + playerName + ") not Found TO \"" + asciiName + "\"");
                }
            }
            // Failed validation
            else {
                MainController.writeDebugLogFile(1, "AdminCommandController.markingsWarning: " + name + ": failed admin validation!");
            }

        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AdminCommandController.markingsWarning - Error Unhandled Exception Pilot (" + playerName + ") :" + ex);
        }

    }

    // Mission Extend
    public static void adminCommandMissionTimeleft(String name, int timeleftMinutes) {
        try {
            if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.MISSIONTIMELEFT)) {

                long newTimeLeft = (Time.getTime() - MainController.ACTIVEMISSION.getStartTime() + (timeleftMinutes * 60000));
                MainController.ACTIVEMISSION.getMissionParameters().setTimeLimit(newTimeLeft);
                //TODO: skylla: Mission extension:
                MissionController.setMissionExtended(true);

                ServerCommandController.serverCommandSend("chat Mission Time Remaining Reset to: " + timeleftMinutes + " Minutes TO ALL");

                MainController.writeDebugLogFile(1, "AdminCommandController.missionTimeleft (" + name + ") Reset Mission Time to ( " + timeleftMinutes + " )");
            } else {
                MainController.writeDebugLogFile(1, "AdminCommandController.missionTimeleft: " + name + ": failed admin validation!");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AdminCommandController.missionTimeLeft - Error unhandled exception resetting time Admin(" + name + ") new time(" + timeleftMinutes + ")");
        }
    }
    
    //--------------------------------------
    //TODO: skylla: Admin-Veto:
    
    public static boolean adminCommandVeto(String name) {
    	boolean b = true;
    	if(AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.VETO)) {
    		if(MainController.MISSIONCONTROL.getVoteCalled()) {
    			if(MissionController.getRequestedMissionFile() != null) {
    				MissionController.resetRequestedMissionFile();
    			}
    			MissionController.clearMissionVote();
    			ServerCommandController.serverCommandSend("chat " + name + " placed an Admin Veto! The current vote is stopped! TO ALL");
    			MainController.writeDebugLogFile(1, "AdminCommandController.adminCommandVeto: " + name + " placed a Veto!");
    			b = false;
    		}
    		if(b)
    			ServerCommandController.serverCommandSend("chat " + name + " placed an Admin Veto TO ALL");
			return true;
    	}
    	ServerCommandController.serverCommandSend("chat You have not the rights to Veto TO \"" + name + "\"");
    	return false;
    }
    
    public static void adminCommandVetoTime(String name, int timeOut) {
    	if(adminCommandVeto(name)) {
    		try {
    			MissionController.blockVote(timeOut);
    			if(timeOut != 0)
    				ServerCommandController.serverCommandSend("chat The Veto will block every Vote for the next " + timeOut + " Minutes TO ALL");
    			MainController.writeDebugLogFile(1, "AdminCommandController.adminCommandVetoTime: " + name + " added a " + timeOut + " Minute Vote-Timeout!");
    		} catch(IllegalInputException e) {
    			ServerCommandController.serverCommandSend("chat Vote-Timeout failed: " + e.getReason() + " TO \"" + name + "\"");
    			MainController.writeDebugLogFile(1, "AdminCommandController.adminCommandVetoTime: Adding a Vote-Timeout failed! Reason: " + e.getReason());
    		}
    	}
    }
    
    //--------------------------------------

    // Mission Restart
    public static void adminCommandMissionRestart(String name) {
        if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.MISSIONRESTART)) {
            MissionFile tempMission = MainController.MISSIONCONTROL.getCurrentMission();
            MainController.MISSIONCONTROL.setTempMission(tempMission);
            MainController.MISSIONCONTROL.setMissionOver(true);
            MainController.MISSIONCONTROL.setMissionOver(true);
            ServerCommandController.serverCommandSend("chat Current Mission To Be Restarted TO ALL");
        } else {
            MainController.writeDebugLogFile(1, name + " does not have access to that command.");
        }
    }

    // Load Mission
    public static void adminCommandLoadMission(String name, String newMission) {
        try {
            if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.LOADMISSION)) {
                String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

                // Check to see if the mission to be Loaded exists in the mission directory.
                // If it is set it as a temp mission and set the mission over flag to true.
                // This will stop the current mission and start the new mission.

                MissionFile tempMission = MissionController.getMission(newMission);
                if (tempMission == null) {
                    tempMission = new MissionFile(newMission);
                }
                Boolean validMission = MainController.validateMission(tempMission);
                if (validMission) {
                    MainController.MISSIONCONTROL.setTempMission(tempMission);
                    MainController.MISSIONCONTROL.setMissionOver(true);
                    ServerCommandController.serverCommandSend("chat Mission Will Change To ( " + newMission + ") TO ALL");
                } else {
                    ServerCommandController.serverCommandSend("chat Mission Not Found ( " + newMission + " ) TO \"" + asciiName + "\"");
                    MainController.writeDebugLogFile(1, "AdminCommandController.loadMission - Unable to open Mission ( " + newMission + " ) in path ( " + MainController.CONFIG.getServerDirectory() + "Missions/" + MainController.MISSIONPATH + " )");
                }
            } else {
                MainController.writeDebugLogFile(1, name + " does not have access to that command.");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AdminCommandController.loadMission - Unhandled Exception Mission( " + newMission + " ): " + ex);
        }
    }

    // Ping Kick
    public static void adminCommandPingKick(String name, int ping) {
        if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.PINGKICK)) {
            String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

            ServerCommandController.serverCommandSend("maxping " + ping);
            ServerCommandController.serverCommandSend("chat Ping Kick value changed to (" + ping + " ms) TO \"" + asciiName + "\"");
        } else {
            MainController.writeDebugLogFile(1, name + " does not have access to that command.");
        }
    }

    // ReservedName Add
    public static void adminCommandReservedNameAdd(String name, String reservedName, String reservedPassword) {
        String unicodeReservedName = reservedName;
        if (reservedName.contains("\\u")) {
            unicodeReservedName = UnicodeFormatter.convertAsciiStringToUnicode(reservedName);
        }

        String unicodeReservedPassword = reservedPassword;
        if (reservedName.contains("\\u")) {
            unicodeReservedPassword = UnicodeFormatter.convertAsciiStringToUnicode(reservedPassword);
        }

        if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.RESERVEDNAMEADD)) {
            String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

            ReservedNameController.reservedNameAdd(unicodeReservedName, unicodeReservedPassword);

            ServerCommandController.serverCommandSend("chat Reserved name added for " + reservedName + " TO \"" + asciiName + "\"");
            MainController.writeDebugLogFile(1, "AdminCommandController.reservedNameAdd: " + name + ": added reserved name for " + unicodeReservedName);
        } else {
            MainController.writeDebugLogFile(1, "AdminCommandController.reservedNameAdd: " + name + ": failed admin validation!");
        }
    }

    // ReservedName Remove
    public static void adminCommandReservedNameRemove(String name, String reservedName) {
        String unicodeReservedName = reservedName;
        if (reservedName.contains("\\u")) {
            unicodeReservedName = UnicodeFormatter.convertAsciiStringToUnicode(reservedName);
        }

        if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.RESERVEDNAMEREMOVE)) {

            String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

            ReservedNameController.reservedNameRemove(unicodeReservedName);

            // Remove reserved name access if on server
            if (MainController.PILOTS.containsKey(unicodeReservedName)) {
                MainController.PILOTS.get(unicodeReservedName).setReservedName(false);
            }

            ServerCommandController.serverCommandSend("chat Reserved name removed for " + reservedName + " TO \"" + asciiName + "\"");
            MainController.writeDebugLogFile(1, "AdminCommandController.reservedNameRemove: " + name + ": removed reserved name for " + unicodeReservedName);
        } else {
            MainController.writeDebugLogFile(1, "AdminCommandController.reservedNameRemove: " + name + ": failed admin validation!");
        }
    }

    // Console
    public static void adminCommandConsole(String name, String console) {
        if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.CONSOLE)) {
            String asciiName = MainController.PILOTS.get(name).getAsciiTextName();

            ServerCommandController.serverCommandSend(console);

            ServerCommandController.serverCommandSend("chat Console command sent. TO \"" + asciiName + "\"");
            MainController.writeDebugLogFile(1, "AdminCommandController.console: " + name + ": issues console command: " + console);
        } else {
            MainController.writeDebugLogFile(1, "AdminCommandController.console: " + name + ": failed admin validation!");
        }
    }

    // FBDj Stop
    public static void adminCommandFBDjStop(String name) {
        if (AdminCommandController.adminCommandValidateAdmin(name, AdminCommands.FBDJSTOP)) {

        } else {
            MainController.writeDebugLogFile(1, "AdminCommandController.FBDjStop: " + name + ": failed admin validation!");
        }
    }

    // Objective (Red||Blue)
    public static void adminCommandObjective(String name, String targetColor) {
        try {
            int army = 0;

            if (targetColor.equalsIgnoreCase("red")) {
                army = MainController.REDARMY;
            } else if (targetColor.equalsIgnoreCase("blue")) {
                army = MainController.BLUEARMY;
            }

            if (army > 0) {
                MainController.writeDebugLogFile(2, "here ( " + name + " ) Color(" + targetColor + ") ");
                MissionController.displayAdminObjectives(name, army);
            } else {
                ServerCommandController.serverCommandSend("chat Command Error: Must choose either RED or BLUE!" + " TO \"" + name + "\"");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ChatAdminController.AdminCommandObjective - Error ( " + name + " ) Color(" + targetColor + ") " + ex);
        }
    }

    public static void adminUpdateObjective(String name, String targetColor, String targetId, int newDestroyCount) {
        try {
            int army = 0;

            if (targetColor.equalsIgnoreCase("red")) {
                army = MainController.REDARMY;
            } else if (targetColor.equalsIgnoreCase("blue")) {
                army = MainController.BLUEARMY;
            }

            if (army > 0) {
                MainController.writeDebugLogFile(2, "AdminCommandController.AdminUpdateObjective Army (" + army + ") ID(" + targetId + ") count(" + newDestroyCount + ")");
                MissionController.updateAdminObjectives(name, army, targetId, newDestroyCount);
            } else {
                ServerCommandController.serverCommandSend("chat Command Error: Must choose either RED or BLUE!" + " TO \"" + name + "\"");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ChatAdminController.AdminUpdateObjective - Error ( " + name + " ) Color(" + targetColor + ") ID(" + targetId + ") Count(" + newDestroyCount + "): " + ex);
        }
    }

    
    
    // Admin Validation
    private static boolean adminCommandValidateAdmin(String name, AdminCommands adminCommand) {
        boolean validated = false;

        // Check list
        if (MainController.ADMINS.containsKey(name)) {
            // Check if GUI and not a player
            if (MainController.ADMINS.get(name).isGui() && !MainController.PILOTS.containsKey(name)) {
                validated = AdminCommandController.adminCommandValidateAdminCommand(name, adminCommand);
            }
            // Check if authenticated
            else if (MainController.PILOTS.get(name).isAdmin()) {
                validated = AdminCommandController.adminCommandValidateAdminCommand(name, adminCommand);
            }
            // Not authenticated
            else {
                MainController.writeDebugLogFile(1, "AdminCommandController.validateAdmin: " + name + ": not authenticated!");
            }
        }
        // Not in list
        else {
            MainController.writeDebugLogFile(1, "AdminCommandController.validateAdmin: " + name + ": not in list!");
        }

        return validated;
    }

    private static boolean adminCommandValidateAdminCommand(String name, AdminCommands adminCommand) {
        boolean validated = false;

        // Check access to command
        switch (adminCommand) {
            case RESERVEDNAMEADD:
                validated = MainController.ADMINS.get(name).isReservedNameAdd();
                break;
            case RESERVEDNAMEREMOVE:
                validated = MainController.ADMINS.get(name).isReservedNameRemove();
                break;
            case BANADD:
                validated = MainController.ADMINS.get(name).isBanAdd();
                break;
            case BANREMOVE:
                validated = MainController.ADMINS.get(name).isBanRemove();
                break;
            case CONSOLE:
                validated = MainController.ADMINS.get(name).isConsole();
                break;
            case FBDJSTOP:
                validated = MainController.ADMINS.get(name).isFBDjStop();
                break;
            case KICK:
                validated = MainController.ADMINS.get(name).isKick();
                break;
            case MISSIONRESTART:
                validated = MainController.ADMINS.get(name).isMissionRestart();
                break;
            case LOADMISSION:
                validated = MainController.ADMINS.get(name).isLoadMission();
                break;
            case MISSIONTIMELEFT:
                validated = MainController.ADMINS.get(name).isMissionExtend();
                break;
            case PASSWORDCHANGE:
                validated = MainController.ADMINS.get(name).isPasswordChange();
                break;
            case PINGKICK:
                validated = MainController.ADMINS.get(name).isPingKick();
                break;
            //------------------------------------  
            //TODO: skylla: Admin-Veto:
            case VETO:
            	validated = MainController.ADMINS.get(name).isVeto();
            	break;
            //------------------------------------
            default:

                ServerCommandController.serverCommandSend("chat You do not have privileges for Command ( " + adminCommand + " ) TO \"" + name + "\"");

                MainController.writeDebugLogFile(1, "AdminCommandController.validateAdminCommand: " + name + ": does not have access to that command!");
                break;
        }

        return validated;
    }
}
