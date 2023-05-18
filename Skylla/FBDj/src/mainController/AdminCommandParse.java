package mainController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.UnicodeFormatter;

/*
 * Parse admin commands from chat
 */

class AdminCommandParse {
    // Admin login/password
    private static final Pattern login               = Pattern.compile("^(?i)login\\s+(.*)\\\\n$");
    private static final Pattern passwordChange      = Pattern.compile("^(?i)password_change\\s+(.*)\\\\n$");
    private static final Pattern playerList          = Pattern.compile("^(?i)player_list|pl\\\\n$");
    // Kick
    private static final Pattern kick                = Pattern.compile("^(?i)kick\\s+(.*)\\\\n$");
    // Ban
    private static final Pattern banAdd              = Pattern.compile("^(?i)(?:ban_add|ba)\\s+(.*):(\\d+)\\\\n$");
    private static final Pattern banRemove           = Pattern.compile("^(?i)(?:ban_remove|br)\\s+(.*)\\\\n$");
    // Markings Warning
    private static final Pattern markingsWarning     = Pattern.compile("^(?i)(?:markings_warning|mw)\\s+(.*)\\\\n$");
    private static final Pattern markingsWarningKick = Pattern.compile("^(?i)(?:markings_warning_kick|mwk)\\s+(.*)\\\\n$");
    // Playerlist add/remove
    private static final Pattern reservednameAdd     = Pattern.compile("^(?i)(?:reservedname_add|rna)\\s+(.*):(\\S+)\\\\n$");
    private static final Pattern reservednameRemove  = Pattern.compile("^(?i)(?:reservedname_remove|rnr)\\s+(.*)\\\\n$");
    // Mission
    private static final Pattern missionTimeleft     = Pattern.compile("^(?i)(?:mission_timeleft|mtl)\\s+(\\d+)\\\\n$");
    private static final Pattern missionRestart      = Pattern.compile("^(?i)mission_restart\\\\n$");
    private static final Pattern loadMission         = Pattern.compile("^(?i)load_mission\\s+(.*)\\\\n$");
    // Ping-kick settings
    private static final Pattern pingKick            = Pattern.compile("^(?i)ping_kick\\s+(.*)\\\\n$");
    // Console
    private static final Pattern console             = Pattern.compile("^(?i)console\\s+(.*)\\\\n$");
    // FBDj stop
    private static final Pattern fbdjStop            = Pattern.compile("^(?i)fbdj_stop\\\\n$");
    // Show Objectives
    private static final Pattern objective           = Pattern.compile("^(?i)(?:objective|obj)\\<(red|blue)\\\\n$");
    // Update Objectives
    private static final Pattern updateObjective     = Pattern.compile("^(?i)(?:update_objective|updobj)\\<(red|blue)\\s+(.*):(\\d+)\\\\n$");
    // Update PlaneLimit
    private static final Pattern updatePlaneLimit    = Pattern.compile("^(?i)(?:update_plane|updplane)\\s+(.*)\\\\n$");

  //--------------------------------------
    //TODO: skylla: Admin-Veto
    private static final Pattern vetoTime			 = Pattern.compile("^(?i)veto\\s+(\\d+)\\\\n$");
    private static final Pattern veto				 = Pattern.compile("^(?i)veto\\\\n$");
    
    //TODO: skylla: slap
    private static final Pattern slap                = Pattern.compile("^(?i)(?:slap|s)\\s+(.*)\\\\n$");
    
    //TODO: skylla: destroy
    //private static final Pattern multiSlap         = Pattern.compile("^(?i)(?:slap|s)\\s+(.*)(:)\\s+(.*)\\\\n$");
    private static final Pattern multiSlap           = Pattern.compile("^(?i)(?:slap|s)\\s+(.*):(\\d+)\\\\n$");
    
  //--------------------------------------
    
    public static void parseAdminCommand(String name, String command) {
//		System.out.println("ChatAdmin.parseAdminCommand: Received data from " + name + ": " + command);

        Matcher m;
        try {

            // Login
            if ((m = login.matcher(command)).find()) {
                String password = m.group(1).trim();
                // String ipAddress = "1.1.1.1";
                String ipAddress = MainController.PILOTS.get(name).getIPAddress();
                AdminCommandController.adminCommandLogin(name, password, ipAddress);
            }

            // Password - change password
            else if ((m = passwordChange.matcher(command)).find()) {
                String newPassword = m.group(1).trim();
                AdminCommandController.adminCommandPasswordChange(name, newPassword);
            }

            // PlayerList
            else if ((m = playerList.matcher(command)).find()) {
                AdminCommandController.adminCommandPlayerList(name);
            }

            // Kick
            else if ((m = kick.matcher(command)).find()) {
                String kickName = m.group(1).trim();
                AdminCommandController.adminCommandKick(name, kickName);
            }
            
          //--------------------------------------
            //TODO: skylla: multi-slap:
            else if ((m = multiSlap.matcher(command)).find()) {
                String slapName = m.group(1).trim();
                String slapNumber = m.group(2).trim();
                AdminCommandController.adminCommandMultiSlap(name, slapName, slapNumber);
            }
            
            //TODO: skylla: slap
            else if ((m = slap.matcher(command)).find()) {
                String slapName = m.group(1).trim();
                AdminCommandController.adminCommandSlap(name, slapName);
            }
                        
          //--------------------------------------            

            // Ban Add
            else if ((m = banAdd.matcher(command)).find()) {
                String banName = m.group(1).trim();
                int banTime = Integer.parseInt(m.group(2).trim());
                AdminCommandController.adminCommandBanAdd(name, banName, banTime);
            }

            // Ban Remove
            else if ((m = banRemove.matcher(command)).find()) {
                String banName = m.group(1).trim();
                AdminCommandController.adminCommandBanRemove(name, banName);
            }

            // Markings Warning
            else if ((m = markingsWarning.matcher(command)).find()) {
                String playerName = m.group(1).trim();
                AdminCommandController.adminCommandMarkingsWarning(name, playerName, false);
            }

            // Markings Warning Kick
            else if ((m = markingsWarningKick.matcher(command)).find()) {
                String playerName = m.group(1).trim();
                AdminCommandController.adminCommandMarkingsWarning(name, playerName, true);
            }

            // ReservedName Add
            else if ((m = reservednameAdd.matcher(command)).find()) {
                String playerlistName = m.group(1).trim();
                String playerlistPassword = m.group(2).trim();
                AdminCommandController.adminCommandReservedNameAdd(name, playerlistName, playerlistPassword);
            }

            // ReservedName Remove
            else if ((m = reservednameRemove.matcher(command)).find()) {
                String playerlistName = m.group(1).trim();
                AdminCommandController.adminCommandReservedNameRemove(name, playerlistName);
            }

            // Mission Timeleft
            else if ((m = missionTimeleft.matcher(command)).find()) {
                int timeleftMinutes = Integer.parseInt(m.group(1).trim());
                AdminCommandController.adminCommandMissionTimeleft(name, timeleftMinutes);
            }
            
            //--------------------------------------
            //TODO: skylla: Admin-Veto
            else if((m = veto.matcher(command)).find()) {
            	AdminCommandController.adminCommandVeto(name);
            }
            
            else if((m = vetoTime.matcher(command)).find()) {
            	int timeOut = Integer.parseInt(m.group(1).trim());
            	AdminCommandController.adminCommandVetoTime(name, timeOut);
            }
            //--------------------------------------
            
            // Mission Restart
            else if ((m = missionRestart.matcher(command)).find()) {
                AdminCommandController.adminCommandMissionRestart(name);
            }

            // Load Mission
            else if ((m = loadMission.matcher(command)).find()) {
                String newMission = m.group(1).trim();
                AdminCommandController.adminCommandLoadMission(name, newMission);
            }

            // Ping Kick
            else if ((m = pingKick.matcher(command)).find()) {
                int ping = Integer.parseInt(m.group(1).trim());
                AdminCommandController.adminCommandPingKick(name, ping);
            }

            // Console
            else if ((m = console.matcher(command)).find()) {
                String console = m.group(1).trim();
                AdminCommandController.adminCommandConsole(name, console);
            }

            // FBDj Stop
            else if ((m = fbdjStop.matcher(command)).find()) {
                AdminCommandController.adminCommandFBDjStop(name);
            }
            // Targets
            else if ((m = objective.matcher(command)).find()) {
                String targetColor = m.group(1).trim();
                MainController.writeDebugLogFile(2, "in admincommandobjective targetcolor(" + targetColor + ")");
                AdminCommandController.adminCommandObjective(name, targetColor);
            } else if ((m = updateObjective.matcher(command)).find()) {
                String targetColor = m.group(1).trim();
                String targetId = m.group(2).trim();
                int newDestroyCount = Integer.parseInt(m.group(3).trim());
                MainController.writeDebugLogFile(2, "in admincommandobjective targetcolor(" + targetColor + ")");
                AdminCommandController.adminUpdateObjective(name, targetColor, targetId, newDestroyCount);
            } else if ((m = updatePlaneLimit.matcher(command)).find()) {
                String data = m.group(1).trim();
                System.out.println("Data: " + data);
                String Id = data.split("=")[0];
                System.out.println("ID: " + Id);
                int aerodromeId = Integer.getInteger(Id.split(":")[0]);
                System.out.println("aerodromeId: " + aerodromeId);
                String plane = Id.split(":")[1];
                System.out.println("plane: " + plane);

                int newLimit = Integer.getInteger(data.split("=")[1]);
                System.out.println("limit: " + newLimit);

                MainController.writeDebugLogFile(2, "in updatePlaneLimit AerodromeId(" + aerodromeId + ") Plane(" + plane + ") Limit(" + newLimit + ")");
            }

            // Unrecognized command
            else {
                // Send to game if not GUI
                if (MainController.ADMINS.containsKey(name)) {
                    if (!MainController.ADMINS.get(name).isGui()) {
                        String asciiName = UnicodeFormatter.convertUnicodeToString(name);
                        ServerCommandController.serverCommandSend("chat Admin Command ( " + command + " ) not recognized TO \"" + asciiName + "\"");
                    }
                }
//				MainController.writeDebugLogFile(1, "AdminCommandParse.parseAdminCommand: " + name + ": Command not recognized: " + command);
                return;
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AdminCommandParse.parseAdminCommand - Error on Command (" + command + ") error: " + ex);
        }

        // Log admin command
        String data = "Admin Command: " + name + ": " + command;
        LogController.writeAdminLogFile(data);
    }
}
