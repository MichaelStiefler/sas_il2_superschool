package mainController;

import java.util.Iterator;

import model.Aerodrome;
import model.IL2StaticObject;
import model.Mission;
import model.MissionCountObjective;
import model.MissionParameters;
import model.MissionTargetObjective;
import model.PlaneLoadoutRestriction;

class MissionLoadParametersController {
    public static void loadMissionParameters(String missionName, String missionParameterFileName) {
        MissionParameters missionParameters = new MissionParameters();
        // Get Mission Parameters from File
        if ((missionParameters = (MissionParameters) FileController.fileReadSerialized(missionParameterFileName)) == null) {
            MainController.writeDebugLogFile(1, "MissionLoadParameters.loadMissionParameters - Error: Mission Load Parameters returned NULL from file: " + missionParameterFileName);
        } else {
            MainController.writeDebugLogFile(1, "Mission Load Parameters read from file: " + missionParameterFileName);
            Mission activeMission = MainController.ACTIVEMISSION;

            try {
                // Add Plane/Pilot Count Objectives to mission file version 1st before merge
                activeMission.getMissionParameters().addCountObjective(MainController.REDARMY, IL2StaticObject.ObjectType.PILOT, 0);
                activeMission.getMissionParameters().addCountObjective(MainController.REDARMY, IL2StaticObject.ObjectType.PLANE, 0);
                activeMission.getMissionParameters().addCountObjective(MainController.BLUEARMY, IL2StaticObject.ObjectType.PILOT, 0);
                activeMission.getMissionParameters().addCountObjective(MainController.BLUEARMY, IL2StaticObject.ObjectType.PLANE, 0);

                missionParameters = MissionLoadController.mergeMissionDetails(activeMission.getMissionParameters(), missionParameters, false);
                MainController.writeDebugLogFile(2, " Objective Type ( " + missionParameters.getObjectiveType() + " ) ");

                // Add Mission Count Objectives
                int blueObjectivesNeeded = 0;
                int redObjectivesNeeded = 0;
                for (MissionCountObjective countObjective : missionParameters.getCountObjectives()) {
                    if (countObjective.getNumberToDestroy() > 0 || countObjective.getObjectType() == IL2StaticObject.ObjectType.PLANE || countObjective.getObjectType() == IL2StaticObject.ObjectType.PILOT) {
                        MainController.writeDebugLogFile(2,
                                " Count Objective Army(" + countObjective.getArmy() + ") ObjectType(" + countObjective.getObjectType() + ") # Start(" + countObjective.getMissionStartCount() + ") # to Destroy(" + countObjective.getNumberToDestroy() + ")");
                        if (countObjective.getArmy() == MainController.BLUEARMY && countObjective.getObjectType() != IL2StaticObject.ObjectType.PILOT && countObjective.getObjectType() != IL2StaticObject.ObjectType.PLANE) {
                            blueObjectivesNeeded++;
                        } else if (countObjective.getArmy() == MainController.REDARMY && countObjective.getObjectType() != IL2StaticObject.ObjectType.PILOT && countObjective.getObjectType() != IL2StaticObject.ObjectType.PLANE) {
                            redObjectivesNeeded++;
                        }
                    }
                }

                if (missionParameters.getRedCountObjectivesNeeded() > redObjectivesNeeded) {
                    MainController.writeDebugLogFile(1, "MissionLoadParameters.loadMissionParameters - Error Red Count Objectives needed are too High (" + missionParameters.getRedCountObjectivesNeeded() + ") ...reset to (" + redObjectivesNeeded + ")");
                    missionParameters.setRedCountObjectivesNeeded(redObjectivesNeeded);
                }
                if (missionParameters.getBlueCountObjectivesNeeded() > blueObjectivesNeeded) {
                    MainController.writeDebugLogFile(1, "MissionLoadParameters.loadMissionParameters - Error Blue Count Objectives needed are too High (" + missionParameters.getBlueCountObjectivesNeeded() + ") ...reset to (" + blueObjectivesNeeded + ")");
                    missionParameters.setBlueCountObjectivesNeeded(blueObjectivesNeeded);
                }

                redObjectivesNeeded = 0;
                blueObjectivesNeeded = 0;
                // Add Mission Target Objectives
                for (MissionTargetObjective targetObjective : missionParameters.getTargetObjectives()) {
                    if (targetObjective.getNumberToDestroy() > 0) {
                        MainController.writeDebugLogFile(2,
                                " Target Objective Army(" + targetObjective.getArmy() + ") MapGrid(" + targetObjective.getMapGridLocation() + "/" + targetObjective.getTargetDesc() + ") # Start (" + targetObjective.getTotalTargets() + ") # To Destroy("
                                        + targetObjective.getNumberToDestroy() + ")");
                        if (targetObjective.getArmy() == MainController.BLUEARMY) {
                            blueObjectivesNeeded++;
                        } else if (targetObjective.getArmy() == MainController.REDARMY) {
                            redObjectivesNeeded++;
                        }
                    }
                }
                if (missionParameters.getRedTargetObjectivesNeeded() > redObjectivesNeeded) {
                    MainController.writeDebugLogFile(1, "MissionLoadParameters.loadMissionParameters - Error Red Target Objectives needed are too High...reset to (" + redObjectivesNeeded + ")");
                    missionParameters.setRedTargetObjectivesNeeded(redObjectivesNeeded);
                }
                if (missionParameters.getBlueTargetObjectivesNeeded() > blueObjectivesNeeded) {
                    MainController.writeDebugLogFile(1, "MissionLoadParameters.loadMissionParameters - Error Blue Target Objectives needed are too High...reset to (" + blueObjectivesNeeded + ")");
                    missionParameters.setBlueTargetObjectivesNeeded(blueObjectivesNeeded);
                }

                // Add Plane Limits
                for (Aerodrome aerodrome : missionParameters.getAerodromes()) {
                    MainController.writeDebugLogFile(1, " Aerodrome ( " + aerodrome.getAerodromeMapGrid() + " ) - ( Army=" + aerodrome.getArmy() + " Loc X=" + aerodrome.getAerodromeLocationX() + " Loc Y=" + aerodrome.getAerodromeLocationY() + " )");

                    Iterator<String> it = aerodrome.getPlanes().keySet().iterator();
                    while (it.hasNext()) {
                        // Key is actually name but this is for clarity
                        String key = it.next();
                        MainController.writeDebugLogFile(1, " -- Plane ( " + key + " ) ");
                    }
                    if (aerodrome.getOverrunBluePlanes() != null) {
                        Iterator<String> overrunKey = aerodrome.getOverrunBluePlanes().keySet().iterator();
                        while (overrunKey.hasNext()) {
                            // Key is actually name but this is for clarity
                            String key = overrunKey.next();
                            MainController.writeDebugLogFile(1, " -- Base Overrun Blue Plane ( " + key + " ) ");
                        }
                    }
                    if (aerodrome.getOverrunRedPlanes() != null) {
                        Iterator<String> overrunKey = aerodrome.getOverrunRedPlanes().keySet().iterator();
                        while (overrunKey.hasNext()) {
                            // Key is actually name but this is for clarity
                            String key = overrunKey.next();
                            MainController.writeDebugLogFile(1, " -- Base Overrun Red Plane ( " + key + " ) ");
                        }

                    }
                }

                // Add Plane Loadout Restrictions.
                for (PlaneLoadoutRestriction loadoutRestriction : missionParameters.getPlaneLoadoutRestrictions()) {
                    MainController.writeDebugLogFile(2, "New Loadout Restriction ( Army=" + loadoutRestriction.getArmy() + " Plane=" + loadoutRestriction.getPlane() + " Restricted Weapon=" + loadoutRestriction.getWeapon() + " )");
                }

                MainController.writeDebugLogFile(1, "AI Wings");
                Iterator<String> it = activeMission.getAiWings().keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    MainController.writeDebugLogFile(1, " -- Wing (" + activeMission.getAiWings().get(key).getName() + ") Army (" + activeMission.getAiWings().get(key).getArmy() + ") Plane (" + activeMission.getAiWings().get(key).getPlane() + ") Count ("
                            + activeMission.getAiWings().get(key).getPlaneCount() + ")");
                }

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MissionLoadParameters.loadParameters - Error Setting Mission Parameters: " + ex);
            }
            long timeLimit = MainController.ACTIVEMISSION.getMissionParameters().getTimeLimit();
            MainController.writeDebugLogFile(1, "Mission Parameters Loaded Timelimit: " + timeLimit + " ( " + timeLimit / 60000 + " Min )");
        }  // End else parameter file loaded
    } // End loadMissionParameters
}
