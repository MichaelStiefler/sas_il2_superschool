package mainController;

import java.util.ArrayList;
import java.util.Iterator;

import model.MissionFile;
import utility.Time;
import utility.UnicodeFormatter;

class ChatUserController {
    // Login (Reserved Name)
    public static void userCommandLogin(String name, String password) {
        String unicodeName = name;

        if (name.contains("\\u")) {
            unicodeName = UnicodeFormatter.convertAsciiStringToUnicode(name);
        }

        String unicodePassword = password;

        if (password.contains("\\u")) {
            unicodePassword = UnicodeFormatter.convertAsciiStringToUnicode(password);
        }

        // Check if Reserved Name
        if (MainController.RESERVEDNAMES.containsKey(unicodeName)) {
            // Check password
            if (MainController.RESERVEDNAMES.get(unicodeName).getPassword().equals(unicodePassword)) {
                MainController.PILOTS.get(unicodeName).setReservedName(true);
                ServerCommandController.serverCommandSend("chat Login Successful TO \"" + name + "\"");
                MainController.writeDebugLogFile(1, "ChatUserController.userCommandLogin: " + name + ": successfully logged in.");
                LogController.writeIPLogFile(name + " Successfully logged in with Reserved Name");

            }
            // Wrong password
            else {
                ServerCommandController.serverCommandSend("chat \"" + name + "\" incorrect password for Reserved Name! TO \"" + name + "\"");
                MainController.writeDebugLogFile(1, "ChatUserController.userCommandLogin: " + unicodeName + ": attempted Reserved Name login with wrong password.");
                LogController.writeIPLogFile(name + " Failed Reserved Name login - Wrong Password");
            }
        }
        // Not on Reserved Name list
        else {
            ServerCommandController.serverCommandSend("chat \"" + name + "\" is not on the Reserved Names list! TO \"" + name + "\"");
            MainController.writeDebugLogFile(1, "ChatUserController.userCommandLogin: " + unicodeName + ": attempted Reserved Name login but is not on list.");
            LogController.writeIPLogFile(name + " Failed Reserved Name login - Not a Reserved Name");
        }
    }

    // Timeleft
    public static void userCommandTimeleft(String name) {
        ServerCommandController.serverCommandSend("chat Mission TimeLeft: " + MissionController.getMissionTimeLeft() / 60000 + " Minutes TO \"" + name + "\"");
    }

    // Teams
    public static void userCommandTeams(String name) {
        try {
            int redPlayers = 0;
            int bluePlayers = 0;
            int undecidedPlayers = 0;
            Iterator<String> it = MainController.PILOTS.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (MainController.PILOTS.get(key).getArmy() == MainController.REDARMY) {
                    redPlayers++;
                } else if (MainController.PILOTS.get(key).getArmy() == MainController.BLUEARMY) {
                    bluePlayers++;
                } else {
                    undecidedPlayers++;
                }
            }
            ServerCommandController.serverCommandSend("chat Teams (Red: " + redPlayers + " Blue: " + bluePlayers + " Undecided: " + undecidedPlayers + ") TO \"" + name + "\"");

        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ChatUserController.userCommandTeams - Error Unhandled Exception: " + ex);
        }
    }

    // Help
    public static void userCommandHelp(String name) {
        ServerCommandController.serverCommandSend("chat ( <help ) Display Help TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <login password ) Login for Restricted UserNames TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <nextmap ) Display Next Mission TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <planes ) Display Restricted Plane Availability TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <timeleft ) Display Time Remaining in Mission TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <obj<red ) Display Red Objectives TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <obj<blue ) Display Blue Objectives TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <load<red ) Display Red Loadout Restrictions TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <load<blue ) Display Blue Loadout Restrictions TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <gunstat ) Display User Gunnery Statistics TO \"" + name + "\"");
        ServerCommandController.serverCommandSend("chat ( <vote yes or <vote no ) Vote to change map if enabled TO \"" + name + "\"");

    }

    // Next Map
    public static void userCommandNextMap(String name) {
        MissionFile missionToRun = null;
        missionToRun = MainController.MISSIONCONTROL.getTempMission();
        String nextMission = null;
        String redWonMission = MainController.MISSIONCONTROL.getCurrentMission().getRedWonMission();
        String blueWonMission = MainController.MISSIONCONTROL.getCurrentMission().getBlueWonMission();
        if (missionToRun == null) {
            if (!redWonMission.equals("None")) {
                nextMission = "Red Win (" + redWonMission + ")";
            }
            if (!blueWonMission.equals("None")) {
                if (nextMission == null) {
                    nextMission = "Blue Win (" + blueWonMission + ")";
                } else {
                    nextMission += " Blue Win (" + blueWonMission + ")";
                }
            }
            if (nextMission == null) {
                nextMission = MainController.MISSIONCONTROL.getNextMission();
            } else {
                nextMission += " No Winner (" + MainController.MISSIONCONTROL.getNextMission() + ")";
            }
        } else {
            nextMission = missionToRun.getMissionName();
        }
        ServerCommandController.serverCommandSend("chat Next Mission: " + nextMission + " TO \"" + name + "\"");
    }

    public static void userCommandPlanes(String name) {
        String unicodeName = name;

        if (name.contains("\\u")) {
            unicodeName = UnicodeFormatter.convertAsciiStringToUnicode(name);
        }

        ArrayList<String> redPlaneLimits = AerodromeController.planeLimitGetPlanesAvailable(MainController.ACTIVEMISSION, MainController.REDARMY);
        ArrayList<String> bluePlaneLimits = AerodromeController.planeLimitGetPlanesAvailable(MainController.ACTIVEMISSION, MainController.BLUEARMY);

        try {
            for (String planeLimit : redPlaneLimits) {
                ServerCommandController.serverCommandSend("chat " + planeLimit + " TO \"" + name + "\"");
            }
            for (String planeLimit : bluePlaneLimits) {
                ServerCommandController.serverCommandSend("chat " + planeLimit + " TO \"" + name + "\"");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ChatUserController.userCommandPlanes - Error ( " + unicodeName + " ) " + ex);
        }
    }

    // Objective (Red||Blue)
    public static void userCommandObjective(String name, String targetColor) {
        try {
            int army = 0;

            if (targetColor.equalsIgnoreCase("red")) {
                army = MainController.REDARMY;
            } else if (targetColor.equalsIgnoreCase("blue")) {
                army = MainController.BLUEARMY;
            }

            if (army > 0) {
                MissionController.displayRemainingObjectives(name, army);
            } else {
                ServerCommandController.serverCommandSend("chat Command Error: Must choose either RED or BLUE!" + " TO \"" + name + "\"");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ChatUserController.userCommandObjective - Error ( " + name + " ) Color(" + targetColor + ") " + ex);
        }
    }

    public static void userCommandRestriction(String name, String targetColor) {
        try {
            int army = 0;

            if (targetColor.equalsIgnoreCase("red")) {
                army = MainController.REDARMY;
            } else if (targetColor.equalsIgnoreCase("blue")) {
                army = MainController.BLUEARMY;
            }

            if (army > 0) {
                MissionController.displayLoadoutRestrictions(MainController.ACTIVEMISSION, name, army);
            } else {
                ServerCommandController.serverCommandSend("chat Command Error: Must choose either RED or BLUE!" + " TO \"" + name + "\"");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ChatUserController.userCommandObjective - Error ( " + name + " ) Color(" + targetColor + ") " + ex);
        }
    }

    public static void userCommandGunStat(String name) {
        String unicodeName = name;

        if (name.contains("\\u")) {
            unicodeName = UnicodeFormatter.convertAsciiStringToUnicode(name);
        }

        try {
            new CommandUserStat(MainController.PILOTS.get(unicodeName), CommandUserStat.UserStatActionType.GUNSTAT, 0, 4000);
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ChatUserController.userCommandGunStat (" + unicodeName + ") Error:" + ex);
        }
    }
    
    //TODO: skylla: userCommandRequest(String name, String new Mission)
    /*
     * @author skylla
     * @see ChatUserParse, ChatUserController.userCommandVote(), AdminCommandController.adminCommandLoadMission(), ChatUserController.userCommandRequest();
     */
    public static void userCommandRequest(String name, String newMission) {
    	if(MissionController.isVoteBlocked()) {
    		ServerCommandController.serverCommandSend("chat Requests are Blocked by Veto TO \"" + name + "\"");
    		ServerCommandController.serverCommandSend("chat Time until Veto gets lifted: " +  (MainController.MISSIONCONTROL.getVoteEndTime() - Time.getTime()) / 60000  + " Minutes TO \"" + name + "\"");
    		return;
    	}
    	//first: check if Mission exists!
    	 try {
    		 MissionController.setRequestedMissionFile(newMission);
        	 //second: initiate vote
        	 
        	 ServerCommandController.serverCommandSend("chat You have voted yes to change the Mission TO \"" + name + "\"");

             String unicodeName = name;
             if (name.contains("\\u")) {
                 unicodeName = UnicodeFormatter.convertAsciiStringToUnicode(name);
             }
             MissionController.registerMissionRequest(unicodeName);
        	 
        	 /*
             MainController.MISSIONCONTROL.setTempMission(tempMission);
             MainController.MISSIONCONTROL.setMissionOver(true);
             ServerCommandController.serverCommandSend("chat Mission Will Change To ( " + newMission + ") TO ALL");
             */
         } catch(IllegalMissionException ime) {
             ServerCommandController.serverCommandSend("chat Mission Not Found ( " + ime.getWrongMissionName() + " ) TO \"" + name + "\"");
             MainController.writeDebugLogFile(1, "UserCommandController.userCommandRequest - Unable to open Mission ( " + ime.getWrongMissionName() + " ) in path ( " + MainController.CONFIG.getServerDirectory() + "Missions/" + MainController.MISSIONPATH + " )");
         }
    }

    public static void userCommandVote(String name, String vote) {
    	if(MissionController.isVoteBlocked()) {
    		ServerCommandController.serverCommandSend("chat Vote is Blocked by Veto TO \"" + name + "\"");
    		ServerCommandController.serverCommandSend("chat Time until Veto gets lifted: " +  (MainController.MISSIONCONTROL.getVoteEndTime() - Time.getTime()) / 60000 + " Minutes TO \"" + name + "\"");
    		return;
    	}
        try {
            if (MainController.CONFIG.getVoteTime() > 0) {
                if (vote.equalsIgnoreCase("yes") || (vote.equalsIgnoreCase("no"))) {
                    ServerCommandController.serverCommandSend("chat You have voted " + vote + " to change the Mission TO \"" + name + "\"");
                    
                    String unicodeName = name;
                    if (name.contains("\\u")) {
                        unicodeName = UnicodeFormatter.convertAsciiStringToUnicode(name);
                    }
                    MissionController.registerMissionVote(unicodeName, vote);
                } else {
                    ServerCommandController.serverCommandSend("chat Command Error: Must vote either YES or NO" + " TO \"" + name + "\"");
                }
            } else {
                ServerCommandController.serverCommandSend("chat Vote Function not enabled on this server" + " TO \"" + name + "\"");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ChatUserController.userCommandVote - Error Unhandled Exception Name(" + name + ") vote(" + vote + ") ex:" + ex);
        }
    }

}
