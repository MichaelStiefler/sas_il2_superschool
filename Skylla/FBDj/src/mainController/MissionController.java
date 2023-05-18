package mainController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import model.DifficultySettings;
import model.IL2StaticObject;
import model.Mission;
import model.MissionCountObjective;
import model.MissionCycleEntry;
import model.MissionFile;
import model.MissionObject;
import model.MissionTargetObjective;
import model.PlaneLoadoutRestriction;
import model.UserMissionCycles;
import utility.Time;
import utility.UnicodeFormatter;
import viewController.MainWindowController;

public class MissionController {

	//TODO: skylla: request_mission command
	private static MissionFile requestedMissionFile;
	
	//TODO: skylla Admin-Veto timeout;
	private static boolean voteIsBlocked = false;
	
	//TODO: skylla: extending Mission always possible:
	private static boolean missionIsExtended = false;
	
    public static void setMissionObjectLost(MissionObject missionObject) {
        try {
            MissionCountObjective missionCountObjective = missionObject.getMissionCountObjective();
            if (missionCountObjective != null) {
                missionCountObjective.setMissionLostCount(missionCountObjective.getMissionLostCount() + 1);
            }
            MissionTargetObjective missionTargetObjective = missionObject.getMissionTargetObjective();
            if (missionTargetObjective != null) {
                missionTargetObjective.setTargetsLost(missionTargetObjective.getTargetsLost() + 1);
            }
            missionObject.setMissionObjectDestroyed();
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MainController.setMissionObjectLost - Error: " + ex);
        }
    }

    public static Boolean missionTimeLimitReached() {
        Boolean timeExpired = false;
        // Perform this check because sometimes the mission was just loaded and the mission start has not
        // been received from eventlog yet (Where start time is set)

        if (MainController.ACTIVEMISSION.getStartTime() != 0) {
            if (Time.getTime() > MainController.ACTIVEMISSION.getStartTime() + MainController.ACTIVEMISSION.getMissionParameters().getTimeLimit()) {
                timeExpired = true;
            }
        }
        return timeExpired;
    }

    public static Long getMissionTimeLeft() {
        Long timeLeft;
        // Time used in FBDj is in Milliseconds
        timeLeft = ((MainController.ACTIVEMISSION.getStartTime() + MainController.ACTIVEMISSION.getMissionParameters().getTimeLimit()) - Time.getTime());
        return timeLeft;
    }

    public static String missionObjectivesComplete(Mission mission) {
        int redCountObjectivesNeeded = mission.getMissionParameters().getRedCountObjectivesNeeded();
        int redCountObjectivesComplete = 0;
        int blueCountObjectivesNeeded = mission.getMissionParameters().getBlueCountObjectivesNeeded();
        int blueCountObjectivesComplete = 0;

        int redTargetObjectivesNeeded = mission.getMissionParameters().getRedTargetObjectivesNeeded();
        int redTargetObjectivesComplete = 0;
        int blueTargetObjectivesNeeded = mission.getMissionParameters().getBlueTargetObjectivesNeeded();
        int blueTargetObjectivesComplete = 0;
        for (MissionCountObjective countObjective : mission.getMissionParameters().getCountObjectives()) {
            if (countObjective.getArmy() == MainController.REDARMY) {

                if (MissionCountObjectivesController.isCountObjectiveComplete(countObjective)) {
                    redCountObjectivesComplete++;
                }
                // If ObjectType is Pilot or Plane and has been satisfied then return.
                // Note Red Pilot/Plane loss is Blue Win
                if ((countObjective.getObjectType() == IL2StaticObject.ObjectType.PILOT || countObjective.getObjectType() == IL2StaticObject.ObjectType.PLANE) && MissionCountObjectivesController.isCountObjectiveComplete(countObjective)) {
                    MainController.ACTIVEMISSION.setWinner(MainController.BLUEARMY);
                    return "Blue";
                }
            } else if (countObjective.getArmy() == MainController.BLUEARMY) {
                if (MissionCountObjectivesController.isCountObjectiveComplete(countObjective)) {
                    blueCountObjectivesComplete++;
                }
                if ((countObjective.getObjectType() == IL2StaticObject.ObjectType.PILOT || countObjective.getObjectType() == IL2StaticObject.ObjectType.PLANE) && MissionCountObjectivesController.isCountObjectiveComplete(countObjective)) {
                    MainController.ACTIVEMISSION.setWinner(MainController.REDARMY);
                    return "Red";
                }
            }
        }
        if (MainController.ACTIVEMISSION.getMissionParameters().getObjectiveType() != Mission.MissionObjectiveType.COUNT) {
            for (MissionTargetObjective targetObjective : MainController.ACTIVEMISSION.getMissionParameters().getTargetObjectives()) {
                if (targetObjective.getArmy() == MainController.REDARMY) {
                    if (MissionTargetObjectivesController.isTargetObjectiveComplete(targetObjective)) {
                        redTargetObjectivesComplete++;
                    }
                } else if (targetObjective.getArmy() == MainController.BLUEARMY) {
                    if (MissionTargetObjectivesController.isTargetObjectiveComplete(targetObjective)) {
                        blueTargetObjectivesComplete++;
                    }
                }
            }
        }

        Boolean redCountWin = false;
        Boolean blueCountWin = false;
        Boolean redTargetWin = false;
        Boolean blueTargetWin = false;

        if (redCountObjectivesNeeded <= redCountObjectivesComplete && redCountObjectivesNeeded > 0) {
            redCountWin = true;
        }
        if (blueCountObjectivesNeeded <= blueCountObjectivesComplete && blueCountObjectivesNeeded > 0) {
            blueCountWin = true;
        }
        if (redTargetObjectivesNeeded <= redTargetObjectivesComplete && redTargetObjectivesNeeded > 0) {
            redTargetWin = true;
        }
        if (blueTargetObjectivesNeeded <= blueTargetObjectivesComplete && blueTargetObjectivesNeeded > 0) {
            blueTargetWin = true;
        }

        String winner = "None";
        Mission.MissionObjectiveType missionObjectiveType = MainController.ACTIVEMISSION.getMissionParameters().getObjectiveType();
        if (missionObjectiveType == Mission.MissionObjectiveType.COUNT || missionObjectiveType == Mission.MissionObjectiveType.EITHER) {
            winner = whoWon(redCountWin, blueCountWin);
        }

        if (winner.equals("None") && (missionObjectiveType == Mission.MissionObjectiveType.TARGET || missionObjectiveType == Mission.MissionObjectiveType.EITHER)) {
            winner = whoWon(redTargetWin, blueTargetWin);
        }

        if (winner.equals("None")) {
            return whoWon((redTargetWin && redCountWin), (blueTargetWin && blueCountWin));
        }

        return winner;

    } // End MissionObjectivesComplete

    public static void displayAdminObjectives(String toWho, int whichArmy) {
        String toAdmin = null;
        ArrayList<String> objectives = null;
        toAdmin = "\"" + UnicodeFormatter.convertUnicodeToString(toWho) + "\"";
        objectives = MissionCountObjectivesController.displayCountObjectives(MainController.ACTIVEMISSION, whichArmy);
        if (objectives != null) {
            for (int i = 0; i < objectives.size(); i++) {
                ServerCommandController.serverCommandSend("chat " + objectives.get(i) + " TO " + toAdmin);
            }
        }
        objectives = null;
        objectives = MissionTargetObjectivesController.displayTargetObjectives(MainController.ACTIVEMISSION, whichArmy);
        if (objectives != null) {
            for (int i = 0; i < objectives.size(); i++) {
                ServerCommandController.serverCommandSend("chat " + objectives.get(i) + " TO " + toAdmin);
            }
        }
    }

    public static void updateAdminObjectives(String toWho, int army, String objective, int newDestroyCount) {
        String displayText = "";
        String toAdmin = "\"" + UnicodeFormatter.convertUnicodeToString(toWho) + "\"";
        int targetId = Integer.valueOf(objective.substring(1));
        if (objective.startsWith("T") || objective.startsWith("t")) {
            displayText = MissionTargetObjectivesController.updateTargetObjective(MainController.ACTIVEMISSION, army, targetId, newDestroyCount);
            ServerCommandController.serverCommandSend("chat " + displayText + " TO " + toAdmin);
        } else if (objective.startsWith("C") || objective.startsWith("c")) {
            displayText = MissionCountObjectivesController.updateTargetObjective(MainController.ACTIVEMISSION, army, targetId, newDestroyCount);
            ServerCommandController.serverCommandSend("chat " + displayText + " TO " + toAdmin);
        } else {
            ServerCommandController.serverCommandSend("chat Obj(" + objective + ") needs to start with T for Target or C for count TO " + toAdmin);
            return;
        }
    }

    public static void displayRemainingObjectives(String toWho, int whichArmy) {
        String chatText;
        String toRedTeam = null;
        String toBlueTeam = null;
        int army;
        // Display targets to everyone if going to All
        if (toWho.equals("All")) {
            toRedTeam = "ALL";
            toBlueTeam = "ALL";
        } else
        // Otherwise we need to verify if the requestor has permission to view other
        // sides target info
        {
            try {
                army = MainController.PILOTS.get(toWho).getArmy();
            } catch (Exception ex) {
                army = 0;
            }

            if (army == 0) {
                if (whichArmy == MainController.REDARMY) {
                    toRedTeam = "\"" + UnicodeFormatter.convertUnicodeToString(toWho) + "\"";
                } else {
                    toBlueTeam = "\"" + UnicodeFormatter.convertUnicodeToString(toWho) + "\"";
                }
            } else if (army == whichArmy || (army != whichArmy && !MainController.CONFIG.isFogOfWar())) {
                if (whichArmy == MainController.REDARMY) {
                    toRedTeam = "\"" + UnicodeFormatter.convertUnicodeToString(toWho) + "\"";
                } else {
                    toBlueTeam = "\"" + UnicodeFormatter.convertUnicodeToString(toWho) + "\"";
                }
            }
        }

        if (toBlueTeam != null) {
            chatText = MissionCountObjectivesController.displayRemainingPilotPlaneObjectives(MainController.ACTIVEMISSION, MainController.BLUEARMY);
            ServerCommandController.serverCommandSend("chat " + chatText + " TO " + toBlueTeam);
            chatText = MissionCountObjectivesController.displayRemainingCountObjectives(MainController.ACTIVEMISSION, MainController.BLUEARMY);
            if (chatText != null) {
                ServerCommandController.serverCommandSend("chat " + chatText + " TO " + toBlueTeam);
            }
            ArrayList<String> tgtText = MissionTargetObjectivesController.displayRemainingTargetObjectives(MainController.ACTIVEMISSION, MainController.BLUEARMY);
            if (tgtText != null) {
                for (int i = 0; i < tgtText.size(); i++) {
                    ServerCommandController.serverCommandSend("chat " + tgtText.get(i) + " TO " + toBlueTeam);
                }
            }

        }
        if (toRedTeam != null) {
            chatText = MissionCountObjectivesController.displayRemainingPilotPlaneObjectives(MainController.ACTIVEMISSION, MainController.REDARMY);
            ServerCommandController.serverCommandSend("chat " + chatText + " TO " + toRedTeam);
            chatText = MissionCountObjectivesController.displayRemainingCountObjectives(MainController.ACTIVEMISSION, MainController.REDARMY);
            if (chatText != null) {
                ServerCommandController.serverCommandSend("chat " + chatText + " TO " + toRedTeam);
            }
            ArrayList<String> tgtText = MissionTargetObjectivesController.displayRemainingTargetObjectives(MainController.ACTIVEMISSION, MainController.REDARMY);
            if (tgtText != null) {
                for (int i = 0; i < tgtText.size(); i++) {
                    ServerCommandController.serverCommandSend("chat " + tgtText.get(i) + " TO " + toRedTeam);
                }
            }
        }
    } // displayRemainingObjectives

    public static void displayLoadoutRestrictions(Mission mission, String toWho, int whichArmy) {
        String chatText;
        String chatTo = null;
        int army = 0;
        // Display targets to everyone if going to All
        if (toWho.equals("All")) {
            chatTo = "ALL";
        } else {
            chatTo = toWho;
        }

        try {
            int counter = 0;
            chatText = "Loadout Restrictions:";
            ServerCommandController.serverCommandSend("chat " + chatText + " TO " + chatTo);
            for (PlaneLoadoutRestriction loadoutRestriction : mission.getMissionParameters().getPlaneLoadoutRestrictions()) {
                army = loadoutRestriction.getArmy();
                String plane = loadoutRestriction.getPlane();
                String weapon = loadoutRestriction.getWeapon();
                if (army == whichArmy) {
                    chatText = plane + "-(" + weapon + ")";
                    ServerCommandController.serverCommandSend("chat " + chatText + " TO " + chatTo);
                    counter++;
                }
            }
            if (counter == 0) {
                chatText = "** No Loadout Restrictions **";
                ServerCommandController.serverCommandSend("chat " + chatText + " TO " + chatTo);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionController.displayLoadoutRestrictions - Error Unhandled Exception (" + toWho + ") Army (" + whichArmy + "): " + ex);
        }
    } // displayLoadoutRestrictions

    public static void loadMissionCycle() {
        // Set Default Mission Cycle from Configuration Setting
        String missionCycleName = MainController.CONFIG.getMissionCycle();

        // If a Scheduled Mission Cycle Change then set new Mission Cycle
        if (MainController.MISSIONCONTROL.getMissionCycleEvent() != null) {
            missionCycleName = MainController.MISSIONCONTROL.getMissionCycleEvent().getEventName();
        }

        // Read Mission Cycle File that contains all Mission Cycles
        String missionCycleFile = MainController.CONFIG.getConfigDirectory() + "MissionCycles.FBDj";
        UserMissionCycles userMissionCycles;
        if ((userMissionCycles = (UserMissionCycles) FileController.fileReadSerialized(missionCycleFile)) == null) {
            userMissionCycles = new UserMissionCycles();
            // We must have a mission Cycle for this to work so report error
            MainController.writeDebugLogFile(1, "MissionController.loadMissionCycle - Error Could not find MissionCycle file(" + missionCycleFile + ")! FBDj Disconnecting");
            // If there's no Mission Cycle List stop the whole works
            MainController.disconnect();
        } else {

            MainController.MISSIONCONTROL.setCurrentMissionFiles(userMissionCycles.getMissionFiles());
            MainController.MISSIONCONTROL.setDifficultySettings(userMissionCycles.getDifficultySettings());
            // Mission Cycles were read from File, find the correct Mission Cycle to load
            // If the Scheduled mission cycle is not found then revert to the configuration setting
            if (!userMissionCycles.getMissionCycleList().containsKey(missionCycleName) && MainController.MISSIONCONTROL.getMissionCycleEvent() != null) {
                MainController.writeDebugLogFile(1, "MissionController.loadMissionCycle - Error Could not find Mission Cycle ( " + MainController.MISSIONCONTROL.getMissionCycleEvent().getEventName()
                        + " ) in Mission Cycle list, reverting to default Mission Cycle ( " + MainController.CONFIG.getMissionCycle() + " )");
                missionCycleName = MainController.CONFIG.getMissionCycle();
            }

            if (userMissionCycles.getMissionCycleList().containsKey(missionCycleName)) {
                // Set the currently executing Mission cycle
                MainController.MISSIONCONTROL.setCurrentMissionCycle(userMissionCycles.getMissionCycleList().get(missionCycleName));
                MainController.writeDebugLogFile(1, "MissionController.loadMissionCycle: MissionCycle ( " + missionCycleName + " ) loaded from file.");

                // Mission Cycle has loaded correctly, check for a requested mission
                if (MainController.MISSIONCONTROL.getRequestedMissionPointer() == -1) {
                    // Mission Names from mission Cycle have been loaded
                    // If Configuration is set to resume mission cycle from where it left off then set the mission pointer.
                    int missionPointer = MissionController.getLastMissionRan(MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionFiles(), missionCycleName);
                    if (MainController.CONFIG.getResumeMissionCycle()) {
                        MainController.MISSIONCONTROL.getCurrentMissionCycle().setMissionPointer(missionPointer + 1);
                        if (MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionPointer() > (MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionFiles().size() - 1)) {
                            MainController.MISSIONCONTROL.getCurrentMissionCycle().setMissionPointer(0);
                        }
                    } else {
                        // TODO: Storebror: Random Mission Rotation ++++++++++++++++++++++++++++
                        //MainController.MISSIONCONTROL.getCurrentMissionCycle().setMissionPointer(0);
                        MainController.MISSIONCONTROL.getCurrentMissionCycle().setRandomMissionPointer();
                        // TODO: Storebror: Random Mission Rotation ----------------------------
                    }
                } else {
                    // A specific mission was requested so run that one.
                    MainController.MISSIONCONTROL.getCurrentMissionCycle().setMissionPointer(MainController.MISSIONCONTROL.getRequestedMissionPointer());
                }

                // Clear the ResetMissionCycle flag
                MainController.MISSIONCONTROL.setResetMissionCycle(false);
                // Clear the RequestedMissionPointer(Set to -1 to reset it)
                MainController.MISSIONCONTROL.setRequestedMissionPointer(-1);
            } else {
                MainController.writeDebugLogFile(1, "MissionController.loadMissionCycle:  Error Mission Cycle ( " + missionCycleName + " ) not Found.");
            }
        }
    }

    public static int getLastMissionRan(ArrayList<MissionCycleEntry> missions, String missionCycleName) {
        int missionPointer = 0;
        try {
            String lastMissionRan = MySQLConnectionController.getLastMissionPlayed(missionCycleName);
            for (int i = 0; i < missions.size(); i++) {
                if (missions.get(i).getMissionName().equals(lastMissionRan)) {
                    missionPointer = i;
                    break;
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionController.getLastMissionRan - Error Unhandled exception: " + ex);
        }
        return missionPointer;
    }

    public static boolean missionControllerLoadNext() {
        try {
            MissionFile missionToRun = null;
            Boolean missionValid = false;
            String missionParameterFileName = null;
            String missionDetailsFileName = null;
            String IL2BaseMissionDirectory = MainController.CONFIG.getServerDirectory() + "Missions" + File.separatorChar;

            String missionDirectory = null;

            // If a request has been made to reset mission cycle or the current mission cycle is empty, load the mission cycle
            if (MainController.MISSIONCONTROL.getResetMissionCycle() || MainController.MISSIONCONTROL.getCurrentMissionCycle() == null) {
                MissionController.loadMissionCycle();
            }

            // If there's a temporary mission that has been requested run it otherwise
            // get next one in the current mission cycle
            if (MainController.MISSIONCONTROL.getTempMission() != null) {
                missionToRun = MainController.MISSIONCONTROL.getTempMission();
                MainController.writeDebugLogFile(1, "Temporary Mission Inserted ( " + MainController.MISSIONCONTROL.getTempMission().getMissionName() + " )");

                // Make sure Temp Mission is available to run
                missionValid = validateMission(missionToRun);
                MainController.MISSIONCONTROL.setTempMission(null);
            } else {
                String missionFileNameToRun = null;
                int missionCounter = 0;
                // No Temporary Mission so lets get the next valid mission in the mission cycle list
                while (!missionValid) {
                    missionFileNameToRun = null;
                    if (MainController.ACTIVEMISSION != null) {
                        try {
                            if (MainController.ACTIVEMISSION.getWinner() == MainController.REDARMY && missionCounter < 1) {
                                missionFileNameToRun = MainController.MISSIONCONTROL.getCurrentMission().getRedWonMission();
                            } else if (MainController.ACTIVEMISSION.getWinner() == MainController.BLUEARMY && missionCounter < 1) {
                                missionFileNameToRun = MainController.MISSIONCONTROL.getCurrentMission().getBlueWonMission();
                            }
                        } catch (Exception ex) {
                            MainController.writeDebugLogFile(1, "MissionController.loadNextMission: Error Exception occured retrieving next mission.  Current Mission (" + MainController.ACTIVEMISSION.getMissionName() + "): " + ex);
                        }
                    }
                    if (missionFileNameToRun == null || missionFileNameToRun.equals("None")) {
                        if (MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionPointer() > MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionFiles().size()) {
                            MainController.writeDebugLogFile(1, "MissionController.loadNextMission: Error - MissionCycle Pointer is beyond number of Missions in cycle");
                        }
                        // Get Mission To run based on Mission Pointer
                        missionFileNameToRun = MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionFiles().get(MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionPointer()).getMissionName();

                        // TODO: Storebror: Random Mission Rotation ++++++++++++++++++++++++++++
//                        // Increment Mission Pointer
//                        MainController.MISSIONCONTROL.getCurrentMissionCycle().setMissionPointer(MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionPointer() + 1);
//                        // If Mission Pointer is at the end of the list, Reset Pointer to start at the Beginning of the List
//                        if (MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionPointer() > (MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionFiles().size() - 1)) {
//                            MainController.MISSIONCONTROL.getCurrentMissionCycle().setMissionPointer(0);
//                        }
                        MainController.MISSIONCONTROL.getCurrentMissionCycle().setRandomMissionPointer();
                        // TODO: Storebror: Random Mission Rotation ----------------------------
                        
                        // Identify Next Mission To run for the <nextmap user command
                        MainController.MISSIONCONTROL.setNextMission(MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionFiles().get(MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionPointer()).getMissionName());
                    }
                    missionToRun = MainController.MISSIONCONTROL.getCurrentMissionFiles().get(missionFileNameToRun);

                    // Set Directory where IL2 will look for Missions
                    IL2BaseMissionDirectory = MainController.CONFIG.getServerDirectory() + "Missions" + File.separatorChar;

                    // Make sure the Mission is available to run
                    missionValid = validateMission(missionToRun);
                    missionCounter++;
                }
            }
            if (missionValid) {
                MainWindowController.setStatusMessage("Starting Mission ( " + missionToRun.getMissionName() + " )");
                missionDirectory = missionToRun.getDirectory();
                missionParameterFileName = IL2BaseMissionDirectory + missionDirectory + missionToRun.getMissionName() + ".FBDj";
                missionDetailsFileName = IL2BaseMissionDirectory + missionDirectory + missionToRun.getMissionName() + ".mis";
                MainController.MISSIONCONTROL.setMissionEndReceived(false);
                MainController.MISSIONCONTROL.setMissionEndSent(false);
                MainController.MISSIONCONTROL.setMissionEndTimeLimitExpired(false);
                MainController.MISSIONCONTROL.setMissionOver(false);
                MainController.MISSIONCONTROL.setMissionOverMessageSent(false);
                MainController.writeDebugLogFile(2, "MissionController.missionControllerLoadNext - Mission To Run (" + missionToRun.getMissionName() + ")");
                MainController.ACTIVEMISSION = MissionLoadController.loadMission(missionDetailsFileName);
                MainController.ACTIVEMISSION.setMissionName(missionToRun.getMissionName());
//                Mission tempMission = MainController.ACTIVEMISSION;
                // Load Mission Parameters. Objectives to Win, Plane Limits, etc.
                MissionLoadParametersController.loadMissionParameters(missionToRun.getMissionName(), missionParameterFileName);
//				tempMission = MainController.ACTIVEMISSION;
                // Load all the Target info from the mission file.
                // Set Mission difficulty
                MainController.writeDebugLogFile(2, "MissionController.missionControllerLoadNext - Difficulty Setting for Mission: " + missionToRun.getDifficulty());
                try {
                    if (!MainController.MISSIONCONTROL.getCurrentDifficulty().equals(missionToRun.getDifficulty())) {
                        setMissionDifficulty(MainController.MISSIONCONTROL.getDifficultySettings().get(missionToRun.getDifficulty()));
                        MainController.MISSIONCONTROL.setCurrentDifficulty(missionToRun.getDifficulty());
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "MissionController.loadNext - Error loading Mission Difficulty settings for Mission(" + missionToRun.getMissionName() + "): " + ex);
                }
                // Switch the slash character from back to forward.
                missionDirectory = missionDirectory.replace("\\", "/");
                // Here we only use the Sub-Directory and mission file name because IL2 will start looking in it's missions directory
                String missionPathFileName = missionDirectory + missionToRun.getMissionName() + ".mis";
                ServerCommandController.serverCommandSend("mission DESTROY");
                ServerCommandController.serverCommandSend("mission LOAD " + missionPathFileName);
                ServerCommandController.serverCommandSend("mission BEGIN");
                MainController.writeDebugLogFile(1, "Mission( " + missionToRun.getMissionName() + " ) Loaded & Started");
                MainController.MISSIONCONTROL.setCurrentMission(missionToRun);
            } else {
                MainController.writeDebugLogFile(1, "MissionController.LoadNext - No Valid Missions found to run");
            }
            return missionValid;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionController.LoadNext - Error Unhandled Exception: " + ex);
            return false;
        }
    }

    public static void setMissionDifficulty(DifficultySettings difficulty) {
        Iterator<String> it = difficulty.getSettings().keySet().iterator();
        int settingValue = 1;
        // This might not be needed, but you cannot change settings unless there is no mission running
        ServerCommandController.serverCommandSend("mission END");

        while (it.hasNext()) {
            // Key is actually name but this is for clarity
            String key = it.next();
            if (difficulty.getSettings().get(key)) {
                settingValue = 1;
            } else {
                settingValue = 0;
            }
            ServerCommandController.serverCommandSend("difficulty " + key + " " + settingValue);

        }

    }

    public static boolean validateMission(MissionFile mission) {
        String IL2BaseMissionDirectory = MainController.CONFIG.getServerDirectory() + "Missions" + File.separatorChar;
        // Get the Sub-Directory for the current mission
        String missionDirectory = mission.getDirectory();
        // Set the Mission Parameters and Mission Details File (.FBDj & .MIS)
        String missionParameterFileName = IL2BaseMissionDirectory + missionDirectory + mission.getMissionName() + ".FBDj";
        String missionDetailsFileName = IL2BaseMissionDirectory + missionDirectory + mission.getMissionName() + ".mis";
        // Make sure both files are readable
        File parmFile = new File(missionParameterFileName);
        File detlFile = new File(missionDetailsFileName);
        if (parmFile.canRead() && detlFile.canRead()) {
            return true;
        } else {
            MainController.writeDebugLogFile(1, "MissionController.validateMission - Error Reading Mission Files for ( " + mission.getMissionName() + " )");
            MainController.writeDebugLogFile(1, "MissionController.validateMission - Mission File: ( " + missionDetailsFileName + " )");
            MainController.writeDebugLogFile(1, "MissionController.validateMission - Parameters File: ( " + missionParameterFileName + " )");
            return false;
        }
    }

    //TODO: skylla: request_mission command
    public static void missionOverCheck() {
        try {
            // See if One side or the Other won the Mission
            String whoWon = MissionController.missionObjectivesComplete(MainController.ACTIVEMISSION);
            if (whoWon.equals("None")) {
                // No One Won, so now lets check to see if the Time Limit has been reached.
                if (MissionController.missionTimeLimitReached() || missionVoteTally()) {
                	// skylla: new Method loadRequestedMission() is used here:
                	if(!loadRequestedMission()) {
                		MainController.MISSIONCONTROL.setMissionOver(true);
                	}
                }
            } else {
                MainController.MISSIONCONTROL.setMissionOver(true);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionController.missionOverCheck - Unhandled Exception: " + ex);
        }
    }

    public static void missionControllerOverNotify() {

        ArrayList<Integer> interval = MainController.CONFIG.getMissionEndTimerInterval();
        String notification = "";
        final Timer timer = new Timer();
        long waitTime = 1;

        class MissionEndNotification extends TimerTask {

            String             notification;
            int                counter;
            ArrayList<Integer> interval;
            

            public MissionEndNotification(String notification, int counter, ArrayList<Integer> interval) {
                this.notification = notification;
                this.counter = counter;
                this.interval = interval;
            }

            @Override
            public void run() {
            	//TODO: skylla: Mission extension:
                if(isMissionExtended() == true) {
            		setMissionExtended(false);
            		MainController.MISSIONCONTROL.setMissionOverMessageSent(false);
            		MainController.MISSIONCONTROL.setMissionEndSent(false);
            		MainController.MISSIONCONTROL.setMissionEndReceived(false);
            		MainController.MISSIONCONTROL.setMissionOver(false);
					timer.cancel();
            		return; 
            	}  
                try {

                    long waitTime = 1;
                    counter++;
                    long timerLength = 1;

                    if (interval.size() > counter) {
                        waitTime = interval.get(counter);
                        if (counter + 1 < interval.size())
                            timerLength = (interval.get(counter) - interval.get(counter + 1)) * 1000;
                        else
                            timerLength = (interval.get(counter)) * 1000;
                        ServerCommandController.serverCommandSend(notification);
                        ServerCommandController.serverCommandSend("chat Next Mission Load in " + waitTime + " Seconds TO ALL");
                        timer.schedule(new MissionEndNotification(notification, counter, interval), timerLength);
                    } else {
                        MainController.writeDebugLogFile(2, "Mission End Timer expired: " + Time.getTime());
                        MainController.MISSIONCONTROL.setMissionEndTimeLimitExpired(true);
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "MissionController.missionControllerOverNotify.MissionEndNotify - Error Unhandled Exception: " + ex);
                }
            }
        } // MissionEndNotification Class

        if (MainController.ACTIVEMISSION.getWinner() == 3) {
            MainController.writeDebugLogFile(1, "Mission Over Tie");
            notification = "chat Mission Over ** Tie ** TO ALL";
        } else if (MainController.ACTIVEMISSION.getWinner() == MainController.REDARMY) {
            MainController.writeDebugLogFile(1, "Mission Over Red Won");
            notification = "chat Mission Over ** Red Won ** TO ALL";
        } else if (MainController.ACTIVEMISSION.getWinner() == MainController.BLUEARMY) {
            MainController.writeDebugLogFile(1, "Mission Over Blue Won");
            notification = "chat Mission Over ** Blue Won ** TO ALL";
        } else if (MissionController.missionTimeLimitReached()) {
            MainController.writeDebugLogFile(1, "Mission TimeLimit ( " + MainController.ACTIVEMISSION.getMissionParameters().getTimeLimit() / 60000 + " ) Reached");
            notification = "chat Mission Over ** TimeLimit( " + MainController.ACTIVEMISSION.getMissionParameters().getTimeLimit() / 60000 + " minutes ) Reached ** TO ALL";
        } else {
            MainController.writeDebugLogFile(1, "Mission Stopped by Request");
            notification = "chat Mission Stopped by Admin TO ALL";
            interval = new ArrayList<Integer>();
            interval.add(5000);
        }
        MainController.MISSIONCONTROL.setMissionOverMessageSent(true);
        long timerLength = 1;
        if (interval.size() > 1) {
            waitTime = interval.get(0);
            timerLength = (interval.get(0) - interval.get(1)) * 1000;
        } else {
            waitTime = 5;
            timerLength = 5000;
        }
        ServerCommandController.serverCommandSend(notification);
        ServerCommandController.serverCommandSend("chat Next Mission Load in " + waitTime + " Seconds TO ALL");
        MainController.ACTIVEMISSION.getMissionParameters().setTimeLimit(waitTime * 1000 + 1000);
        MainController.writeDebugLogFile(2, "Mission notification sent waittime: " + waitTime + "(s) Current Time: " + Time.getTime());
        timer.schedule(new MissionEndNotification(notification, 0, interval), timerLength);
    }

    public static MissionFile getMission(String missionName) {
        MissionFile foundMission = null;
        HashMap<String, MissionFile> currentMissionFiles = new HashMap<String, MissionFile>();
        currentMissionFiles = MainController.MISSIONCONTROL.getCurrentMissionFiles();
//		Boolean validMission = false;
        Iterator<String> it = currentMissionFiles.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (missionName.equalsIgnoreCase(key)) {
                foundMission = currentMissionFiles.get(key);
                break;
            }
        }
        return foundMission;
    }

    private static String whoWon(boolean redWin, boolean blueWin) {

        if (redWin && blueWin) {
            MainController.ACTIVEMISSION.setWinner(3);
            return "Tie";
        } else if (redWin) {
            MainController.ACTIVEMISSION.setWinner(MainController.REDARMY);
            return "Red";
        } else if (blueWin) {
            MainController.ACTIVEMISSION.setWinner(MainController.BLUEARMY);
            return "Blue";
        } else {
            MainController.ACTIVEMISSION.setWinner(0);
            return "None";
        }
    }

    private static Boolean missionVoteTally() {
        try {
            // Users can also vote to change a mission. If more than 60% of the voters said Yes then change mission
            int votesFor = 0;
            int pilotCount = 0;
            long votesNeeded = 0;
            Iterator<String> it = MainController.PILOTS.keySet().iterator();
            while (it.hasNext()) {
                // Key is actually name but this is for clarity
                String key = it.next();
                if (MainController.PILOTS.get(key).isMissionVote()) {
                    votesFor++;
                }
                pilotCount++;
            }
            votesNeeded = (long) Math.ceil(Double.valueOf(pilotCount) * 0.60);
//    		MainController.writeDebugLogFile(2, "MissionController.missionVoteTally - Pilots("+pilotCount+") VotesFor("+votesFor+") Votes Needed("+votesNeeded+")");
            if (MainController.MISSIONCONTROL.getVoteCalled() && votesFor >= votesNeeded) {
                ServerCommandController.serverCommandSend("chat Players Voted to Change Mission ! TO ALL");
                String voteTally = "chat Vote Results (" + votesFor + ") For / (" + votesNeeded + ") Needed";
                ServerCommandController.serverCommandSend(voteTally);
                clearMissionVote();
                return true;
            } else {
//                MainController.writeDebugLogFile(2, "MissionController.missionVoteTally - voteEndTime("+MainController.MISSIONCONTROL.getVoteEndTime()+")");
//                MainController.writeDebugLogFile(2, "MissionController.missionVoteTally - CurrentTime("+Time.getTime()+")");
                if (MainController.MISSIONCONTROL.getVoteEndTime() > 0 && MainController.MISSIONCONTROL.getVoteEndTime() < Time.getTime()) {
                    // Time length for the vote expired so reset everything
                	//TODO: skylla: Admin-Veto: check if it was a "Veto-Vote"!
                	if(isVoteBlocked()) {
                		ServerCommandController.serverCommandSend("chat Veto time expired, voting is unlocked again TO ALL");
                	}
                	else {
                		ServerCommandController.serverCommandSend("chat Vote Time expired, mission not changed ! TO ALL");
                    	String voteTally = "chat Vote Results (" + votesFor + ") For / (" + votesNeeded + ") Needed";
                    	ServerCommandController.serverCommandSend(voteTally);
                	}
                    //---------------------------------------
                    //TODO: skylla: Requested Mission must not be loaded if the vote has a 'no'- majority
                    clearReqMissionVote();
                    //clearMissionVote();
                    //---------------------------------------
                }
                return false;
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionController.missionVoteTally - Error Unhandled Exception: " + ex);
            return false;
        }
    }

    public static void registerMissionVote(String name, String vote) {
        try {
            if (!MainController.MISSIONCONTROL.getVoteCalled()) {
                long voteEndTime = Time.getTime() + MainController.CONFIG.getVoteTime() * 60000;
                MainController.writeDebugLogFile(2, "MissionController.registerVoteTally - Time(" + Time.getTime() + ") Vote Duration (" + MainController.CONFIG.getVoteTime() + " minutes) EndTime(" + voteEndTime + ")");
                MainController.MISSIONCONTROL.setVoteCalled(true);
                MainController.MISSIONCONTROL.setVoteEndTime(voteEndTime);
                int voteTime = MainController.CONFIG.getVoteTime();
                String command = "chat A vote has been called to change the mission TO ALL";
                ServerCommandController.serverCommandSend(command);
                String command2 = "chat Please Vote Yes/No in the next " + voteTime + " minutes TO ALL";
                ServerCommandController.serverCommandSend(command2);
            }
            if (vote.equalsIgnoreCase("yes")) {
                MainController.PILOTS.get(name).setMissionVote(true);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionController.missionVote - Error Unhandled exception: " + ex);
        }
    }
    
    /*
     * @author skylla
     * @see: missionVoteTally()
     */
    public static void clearReqMissionVote() {
    	requestedMissionFile = null;
    	clearMissionVote();
    }
    
    //TODO: skylla: mission_request command
    /*
     * @author skylla
     * @see ChatUserController.userCommandRequest();
     */
    public static void registerMissionRequest(String name) {
    	try {
            if (!MainController.MISSIONCONTROL.getVoteCalled()) {
                long voteEndTime = Time.getTime() + MainController.CONFIG.getVoteTime() * 60000;
                MainController.writeDebugLogFile(2, "MissionController.registerVoteTally - Time(" + Time.getTime() + ") Vote Duration (" + MainController.CONFIG.getVoteTime() + " minutes) EndTime(" + voteEndTime + ")");
                MainController.MISSIONCONTROL.setVoteCalled(true);
                MainController.MISSIONCONTROL.setVoteEndTime(voteEndTime);
                int voteTime = MainController.CONFIG.getVoteTime();
                String command = "chat A vote has been called to change the mission TO ALL";
                ServerCommandController.serverCommandSend(command);
                String command2 = "chat Please Vote Yes/No in the next " + voteTime + " minutes TO ALL";
                ServerCommandController.serverCommandSend(command2);
                MainController.PILOTS.get(name).setMissionVote(true);
            }
            else {
            	ServerCommandController.serverCommandSend("chat A vote is already running! Request denied! TO \"" + name + "\"");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionController.registerMissionRequest - Error Unhandled exception: " + ex);
        }
    }
    
    //TODO: skylla request_mission command
    /*
     * @author skylla
     * @see ChatUserController.userCommandRequest();
     */
    public static void setRequestedMissionFile(String reqmis) throws IllegalMissionException{
    	MissionFile tempMission = MissionController.getMission(reqmis);
        if (tempMission == null) {
            tempMission = new MissionFile(reqmis);
        }
        Boolean validMission = MainController.validateMission(tempMission);
        if (validMission) {
        	requestedMissionFile = tempMission;
        }
        else {
        	throw new IllegalMissionException(reqmis);
        }
    }
    
    public static void resetRequestedMissionFile() {
    	requestedMissionFile = null;
    }
    /*
     * @author skylla
     * @see missionOverCheck()
     */
    public static boolean loadRequestedMission() {
    	if(getRequestedMissionFile() != null) {
    		MainController.MISSIONCONTROL.setTempMission(getRequestedMissionFile());
            MainController.MISSIONCONTROL.setMissionOver(true);
            ServerCommandController.serverCommandSend("chat Mission Will Change To ( " + getRequestedMissionFile().getMissionName() + ") TO ALL");
            requestedMissionFile = null;
            return true;
    	}
    	return false;
    }
    
  //TODO: skylla request_mission command    
    public static MissionFile getRequestedMissionFile() {
    	return requestedMissionFile;
    }

    public static void clearMissionVote() {
    	//TODO: skylla Admin-Veto Time
    	//Veto lifted:
    	setVoteBlocked(false);
    	//-----------------------------
        MainController.MISSIONCONTROL.setVoteCalled(false);
        MainController.MISSIONCONTROL.setVoteEndTime(0);
        Iterator<String> it = MainController.PILOTS.keySet().iterator();
        while (it.hasNext()) {
            // Key is actually name but this is for clarity
            String key = it.next();
            MainController.PILOTS.get(key).setMissionVote(false);
        }
    }
    
    //TODO: skylla: Admin-Veto: Timeout!
    /*
     * @author: skylla
     * @see: AdminCommandController.adminCommandVetoTime()
     */
    public static void blockVote(int timeOut) throws IllegalInputException {
    	if(MainController.MISSIONCONTROL.getVoteCalled()) {
    		clearMissionVote();
    	}
    	if(!(timeOut < 1 || timeOut >= 90)) {
    		MainController.MISSIONCONTROL.setVoteEndTime(Time.getTime() + timeOut*60000);
    		MainController.MISSIONCONTROL.setVoteCalled(false);
    		setVoteBlocked(true);
    	} else if(timeOut == 0) {
    		clearMissionVote();
    		ServerCommandController.serverCommandSend("chat Veto time expired, voting is unlocked again TO ALL"); 
    	} else {
    		throw new IllegalInputException("" + timeOut, "Timeout must be between 0 and 91 Minutes!");
    	}
    }
    
    public static void setVoteBlocked(boolean blocked) {
    	voteIsBlocked = blocked;
    }
    
    public static boolean isVoteBlocked() {
    	return voteIsBlocked;
    }
    
    public static boolean isMissionExtended() {
    	return missionIsExtended;
    }
    
    
    //TODO: skylla: Mission Extending always possible:
    public static void setMissionExtended(boolean extended) {
    	missionIsExtended = extended;
    }
}
