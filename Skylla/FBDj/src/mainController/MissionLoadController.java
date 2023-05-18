package mainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import model.AIWing;
import model.Aerodrome;
import model.IL2StaticObject;
import model.Mission;
import model.MissionCountObjective;
import model.MissionObject;
import model.MissionParameters;
import model.MissionTargetObjective;
import model.MultiVehicleObject;
import model.PlaneLimit;
import utility.Coordinates;

public class MissionLoadController {
    private static final Pattern homeBasePlanes = Pattern.compile("^\\[BornPlace(\\d+)");

    private enum MissionFileSectionTypes {
        MAIN, STATIC, CHIEFS, WINGLIST, WING, TARGET, ZUTIMDS, HOMEBASE, AIRCRAFT, NONE
    }

    private static void addMissionObject(Mission mission, String missionObjectName, String groundObjectName, String missionMultiObjectName, String multiObjectName, int army, double locationX, double locationY, Boolean AIPlane) {
        int objectiveArmy;
        IL2StaticObject.ObjectType staticPlaneType = IL2StaticObject.ObjectType.SPLANE;
        IL2StaticObject il2Object = null;
        MissionCountObjective countObjective = null;
        try {
            // Red Ground Objects get set to Blue Objectives, etc.
            if (army == MainController.REDARMY) {
                objectiveArmy = MainController.BLUEARMY;
            } else {
                objectiveArmy = MainController.REDARMY;
            }

            il2Object = MainController.IL2STATICOBJECTS.get(groundObjectName);

            if (il2Object != null) {
                // If Ground object is a plane set to Static plane Type
                if (!AIPlane && il2Object.getObjectType() == IL2StaticObject.ObjectType.PLANE) {
                    countObjective = mission.getMissionParameters().getCountObjective(objectiveArmy, staticPlaneType);
                    if (countObjective != null) {
                        countObjective.setMissionStartCount(countObjective.getMissionStartCount() + 1);
                    } else {
                        mission.getMissionParameters().addCountObjective(objectiveArmy, staticPlaneType, 1);
                        mission.getMissionParameters().setCountObjectivesNeeded(objectiveArmy, mission.getMissionParameters().getCountObjectivesNeeded(objectiveArmy) + 1);
                        countObjective = mission.getMissionParameters().getCountObjective(objectiveArmy, staticPlaneType);
                    }
                } else if (il2Object.getObjectType() == IL2StaticObject.ObjectType.MISC) {
                    // Do not load MISC objects.
                }

                else if (AIPlane) {
                    countObjective = mission.getMissionParameters().getCountObjective(objectiveArmy, IL2StaticObject.ObjectType.AIPLANE);
                    if (countObjective != null) {
                        countObjective.setMissionStartCount(countObjective.getMissionStartCount() + 1);
                    } else {
                        mission.getMissionParameters().addCountObjective(objectiveArmy, IL2StaticObject.ObjectType.AIPLANE, 1);
                        mission.getMissionParameters().setCountObjectivesNeeded(objectiveArmy, mission.getMissionParameters().getCountObjectivesNeeded(objectiveArmy) + 1);
                        countObjective = mission.getMissionParameters().getCountObjective(objectiveArmy, IL2StaticObject.ObjectType.AIPLANE);
                    }
                } else {
                    countObjective = mission.getMissionParameters().getCountObjective(objectiveArmy, il2Object.getObjectType());
                    if (countObjective != null) {
                        countObjective.setMissionStartCount(countObjective.getMissionStartCount() + 1);
                    } else {
                        mission.getMissionParameters().addCountObjective(objectiveArmy, il2Object.getObjectType(), 1);
                        mission.getMissionParameters().setCountObjectivesNeeded(objectiveArmy, mission.getMissionParameters().getCountObjectivesNeeded(objectiveArmy) + 1);
                        countObjective = mission.getMissionParameters().getCountObjective(objectiveArmy, il2Object.getObjectType());
                    }
                }
                MissionObject newMissionObject = new MissionObject();
                newMissionObject.setMissionObjectName(missionObjectName);
                newMissionObject.setMissionMultiObjectName(missionMultiObjectName);
                newMissionObject.setMultiObjectName(multiObjectName);
                newMissionObject.setArmy(army);
                newMissionObject.setLocationX(locationX);
                newMissionObject.setLocationY(locationY);
                if (locationX != 0.0 && locationY != 0.0) {
                    newMissionObject.setTargetGrid(Coordinates.getCoordinates(mission.getMapName(), mission.isBigMap(), locationX, locationY));
                } else {
                    newMissionObject.setTargetGrid("Moving");
                }
                newMissionObject.setGroundObject(il2Object);
                newMissionObject.setMissionCountObjective(countObjective);
                if (AIPlane)
                    newMissionObject.setObjectType(IL2StaticObject.ObjectType.AIPLANE);
                else
                    newMissionObject.setObjectType(il2Object.getObjectType());
                // Add new Static Ground Object to Mission
                mission.addMissionObject(missionObjectName, newMissionObject);

            } else {
                MainController.writeDebugLogFile(1, "MissionLoadController.addMissionObject - Error: Ground object in mission file ( " + groundObjectName + " ) not found in IL2 Object List");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionLoadController.addMissionObject - Error Unhandled Exception adding Ground Object(" + missionObjectName + "): " + ex);
        }
    }

    private static void addAIPlaneObjectives(Mission mission) {
        try {
            Iterator<String> it = mission.getAiWings().keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                AIWing wing = mission.getAiWings().get(key);
//				AIWingController.addAIPlaneObjective(mission, wing);

                for (int i = 0; i < wing.getPlaneCount(); i++) {
                    String missionObjectName = wing.getName() + Integer.toString(i);
                    addMissionObject(mission, missionObjectName, wing.getPlaneClass(), wing.getName(), "", wing.getArmy(), 0.0, 0.0, true);
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionLoadController.addAIPlaneObjectives - Error Unhandled Exception: " + ex);
        }
    }

    private static void addOverrunPlanes(ArrayList<Aerodrome> overrunAerodromes, ArrayList<Aerodrome> missionAerodromes) {
        for (Aerodrome aerodrome : missionAerodromes) {
            Aerodrome tempAerodrome = null;
            tempAerodrome = AerodromeController.getAerodrome(overrunAerodromes, aerodrome.getAerodromeLocationX(), aerodrome.getAerodromeLocationY());
            if (tempAerodrome != null) {
                aerodrome.setOverrunBluePlanes(tempAerodrome.getOverrunBluePlanes());
                aerodrome.setOverrunRedPlanes(tempAerodrome.getOverrunRedPlanes());
                aerodrome.setBaseOverrun(true);
            }
        }
    }

    private static void loadTargetAreaData(Mission mission) {
        // For each mission object check to see if it is in a target circle
        // If so assign it to that target circle.
        Iterator<String> it = mission.getMissionObjects().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            try {

                MissionObject missionObject = mission.getMissionObjects().get(key);
                MissionTargetObjective targetObjective;
                targetObjective = mission.getMissionParameters().getMissionTargetObjective(missionObject.getMissionMultiObjectName());
                if (targetObjective == null) {
                    targetObjective = mission.getMissionParameters().getMissionTargetObjective(missionObject.getLocationX(), missionObject.getLocationY());
                }
                if (targetObjective != null) {
                    missionObject.setMissionTargetObjective(targetObjective);
                    targetObjective.setTotalTargets(targetObjective.getTotalTargets() + 1);
                    if (!targetObjective.getTargetName().equals("Circle")) {
                        targetObjective.setTargetDesc(missionObject.getMultiObjectName());
                    }
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MissionLoadController.loadTargetAreaData - Error Unhandled Exception processing Mission Objects: " + ex);
                MainController.writeDebugLogFile(1, "**** Mission Object: " + key);
            }
        }
        // Loop through all Target Objectives and get the count of red/blue targets in each.
        for (MissionTargetObjective targetObjective : mission.getMissionParameters().getTargetObjectives()) {
            try {
                int redTargetCount = 0;
                int blueTargetCount = 0;
                it = mission.getMissionObjects().keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    MissionObject missionObject = mission.getMissionObjects().get(key);
                    if (missionObject.getMissionTargetObjective() == targetObjective) {
                        if (missionObject.getArmy() == MainController.REDARMY) {
                            redTargetCount++;
                        } else if (missionObject.getArmy() == MainController.BLUEARMY) {
                            blueTargetCount++;
                        }
                    }
                }
                // Set the Objectives Army based on the count of red or blue targets.
                // If more red targets in the circle then assume it's a blue objective, etc.
                double destroyNum = 0.0;
                double pctDestroy = targetObjective.getNumberToDestroy() / 100.0;
                if (redTargetCount < blueTargetCount) {
                    targetObjective.setArmy(MainController.REDARMY);
                    if (blueTargetCount > 1) {
                        destroyNum = blueTargetCount * pctDestroy;
                    }
                } else if (redTargetCount > blueTargetCount) {
                    targetObjective.setArmy(MainController.BLUEARMY);
                    if (redTargetCount > 1) {
                        destroyNum = redTargetCount * pctDestroy;
                    }
                }
                int numToDestroy = (int) Math.round(destroyNum);
                targetObjective.setNumberToDestroy(numToDestroy);
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MissionLoadController.loadTargetAreaData - Error Unhandled Exception processing Mission Objects: " + ex);
                MainController.writeDebugLogFile(1, "**** Target Grid/Desc: " + targetObjective.getMapGridLocation() + "/" + targetObjective.getTargetDesc());
            }
        }
    }

    private static boolean isWingName(HashMap<String, AIWing> wings, String line) {
        try {
            if (line.startsWith("[") && line.endsWith("]")) {
                String baseLine = line.substring(1, line.length() - 1);
                if (wings.containsKey(baseLine)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionLoadController.isWingName - Error Unhandled exception getting wing name: " + ex);
            MainController.writeDebugLogFile(1, "  *** Line: " + line);
        }
        return false;
    }

    public static Mission loadMission(String missionFileName) {
        Mission newMission = new Mission();
        int endIndex = 0;
        String missionMap = "";
        MissionFileSectionTypes fileSection = MissionFileSectionTypes.NONE;
        String missionObjectName = "Unknown";
        String rawObject;
        int objectNameEndIndex = 0;
        String objectName = "Unknown";
        int objectArmy;
        double objectLocationX;
        double objectLocationY;
        int homeBaseId = 0;
        Matcher m;
        String wingName;
        AIWing currentWing = null;
        String[] targetDetails;
        ArrayList<Aerodrome> aerodromeOverrunList = new ArrayList<Aerodrome>();
        Aerodrome overrunAerodrome = null;

        ArrayList<String> missionFile = FileController.fileRead(missionFileName);
        HashMap<String, AIWing> aiWings = new HashMap<String, AIWing>();

        // Loop through all lines of the mission file
        for (String line : missionFile) {
            try {
                line = line.trim();
                if (line.indexOf("[MAIN]") > -1) {
                    fileSection = MissionFileSectionTypes.MAIN;
                } else if (line.indexOf("[Chiefs]") > -1) {
                    fileSection = MissionFileSectionTypes.CHIEFS;
                } else if (line.indexOf("[Wing]") > -1) {
                    fileSection = MissionFileSectionTypes.WINGLIST;
                } else if (line.indexOf("[NStationary]") > -1) {
                    fileSection = MissionFileSectionTypes.STATIC;
                } else if (line.indexOf("[Target]") > -1) {
                    fileSection = MissionFileSectionTypes.TARGET;
                } else if (line.indexOf("[BornPlace]") > -1) {
                    fileSection = MissionFileSectionTypes.HOMEBASE;
                } else if (line.indexOf("[MDS]") > -1) {
                    fileSection = MissionFileSectionTypes.ZUTIMDS;
                }

                else if ((m = homeBasePlanes.matcher(line)).find()) {
                    fileSection = MissionFileSectionTypes.AIRCRAFT;
                    homeBaseId = Integer.valueOf(m.group(1));
                } else if (isWingName(aiWings, line)) {
                    fileSection = MissionFileSectionTypes.WING;
                    wingName = line.substring(1, line.length() - 1);
                    currentWing = aiWings.get(wingName);

                } else if (line.startsWith("[")) {
                    fileSection = MissionFileSectionTypes.NONE;
                }
                switch (fileSection) {
                    case MAIN:
                        if (line.indexOf("MAP") > -1) {
                            try {
                                endIndex = line.indexOf(".ini");
                                missionMap = line.substring(4, endIndex);
                                fileSection = MissionFileSectionTypes.NONE;
                                newMission.setMapName(missionMap);
                                newMission.getMissionParameters().setMapName(missionMap);
                                newMission.setBigMap(MainController.IL2MAPS.get(missionMap).isBigMap());
                            } catch (Exception ex) {
                                MainController.writeDebugLogFile(1, "Error Finding map name( " + missionMap + " ) in IL2Maps file; Error: " + ex);
                            }
                        }
                        break;
                    case HOMEBASE:
                        if (!line.startsWith("[")) {
                            try {
                                objectArmy = Integer.valueOf(line.split(" ")[0]);
                                double airportRadius = Double.valueOf(line.split(" ")[1]);
                                double airportLocationX = Double.valueOf(line.split(" ")[2]);
                                double airportLocationY = Double.valueOf(line.split(" ")[3]);
                                if (objectArmy > 0) {
                                    Aerodrome aerodrome = new Aerodrome(objectArmy, airportRadius, airportLocationX, airportLocationY);
                                    aerodrome.setAerodromeMapGrid(CoordinatesController.getCoordinates(missionMap, newMission.isBigMap(), airportLocationX, airportLocationY));
                                    newMission.getMissionParameters().addAerodrome(aerodrome);

                                }
                            } catch (Exception ex) {
                                MainController.writeDebugLogFile(1, "MissionLoadController.loadMission - Error Unhandled Exception processing Homebase Section: " + ex);
                                MainController.writeDebugLogFile(1, " *** Line: " + line);
                            }
                        }
                        break;
                    case AIRCRAFT:
                        if (!line.startsWith("[")) {
                            String plane = line.split(" ")[0];
                            String planeName = plane.trim();
                            int inUseLimit = 0;
                            int totalLimit = 0;
                            newMission.getMissionParameters().getAerodromes().get(homeBaseId).addPlane(planeName, inUseLimit, totalLimit);
                        }
                        break;
                    case ZUTIMDS:
                        if (!line.startsWith("[")) {
                            try {
                                line = line.trim();
                                int army = 0;
                                if (line.startsWith("ZutiHB")) {
                                    String[] baseOverrun = line.split(" ");
                                    double baseLocationX = Double.valueOf(baseOverrun[0].split("_")[1]);
                                    double baseLocationY = Double.valueOf(baseOverrun[0].split("_")[2]);
                                    String strArmy = baseOverrun[0].split("_")[3];
                                    if (strArmy.startsWith("R")) {
                                        army = MainController.REDARMY;
                                    } else if (strArmy.startsWith("B")) {
                                        army = MainController.BLUEARMY;
                                    }
                                    overrunAerodrome = AerodromeController.getAerodrome(aerodromeOverrunList, baseLocationX, baseLocationY);
                                    if (overrunAerodrome == null) {
                                        Aerodrome aerodrome = new Aerodrome(0, 0, baseLocationX, baseLocationY);
                                        aerodrome.setAerodromeMapGrid(CoordinatesController.getCoordinates(missionMap, newMission.isBigMap(), baseLocationX, baseLocationY));
                                        aerodromeOverrunList.add(aerodrome);
                                        overrunAerodrome = aerodrome;
                                    }
                                    for (int i = 1; i < baseOverrun.length; i++) {
                                        overrunAerodrome.setBaseOverrun(true);
                                        String planeName = baseOverrun[i];
                                        int inUseLimit = 0;
                                        int totalLimit = 0;
                                        overrunAerodrome.addBaseOverrunPlane(army, planeName, inUseLimit, totalLimit);
                                    }
                                }
                            } catch (Exception ex) {

                            }
                        }
                        break;
                    case CHIEFS:
                        if (line.indexOf("_Chief") > -1) {
                            try {
                                missionObjectName = line.split(" ")[0];
                                rawObject = line.split(" ")[1];
                                objectArmy = Integer.valueOf(line.split(" ")[2]);
                                objectLocationX = 0.0;
                                objectLocationY = 0.0;
                                objectNameEndIndex = rawObject.indexOf(".");
                                String groundObjectName = rawObject.substring(objectNameEndIndex + 1);
                                MultiVehicleObject multiVehicleObject = MainController.IL2MULTIOBJECTS.get(groundObjectName);

                                if (multiVehicleObject != null) {
                                    for (int i = 0; i < multiVehicleObject.getVehicleList().size(); i++) {
                                        String newObjectName;
                                        String name = multiVehicleObject.getVehicleList().get(i).getObjectName();
                                        if (i < 1 && multiVehicleObject.getVehicleList().get(i).getObjectType() == IL2StaticObject.ObjectType.SHIP) {
                                            // Chiefs that are ships do not have the number designation at the end of their name.
                                            newObjectName = missionObjectName;
                                        } else {
                                            newObjectName = missionObjectName + i;
                                        }
                                        addMissionObject(newMission, newObjectName, name, missionObjectName, groundObjectName, objectArmy, 0.0, 0.0, false);
                                    }
                                } else {
                                    MainController.writeDebugLogFile(1, "MissionLoadObjects.loadMission - Chief Object (" + groundObjectName + ") Not found");
                                }
                            } catch (Exception ex) {
                                MainController.writeDebugLogFile(1, "MissionLoadObjects.loadMission - Unhandled Exception loading Chief Object: " + ex);
                                MainController.writeDebugLogFile(1, "MissionLoadObjects.loadMission - ** Line: " + line);
                            }
                        }
                        break;
                    case WINGLIST:
                        if (!line.startsWith("[")) {
                            try {
                                String tempWingName = line.trim();
                                AIWing wing = new AIWing(tempWingName);
                                int army = MainController.IL2REGIMENTS.get(tempWingName.substring(0, tempWingName.length() - 2));
                                wing.setArmy(army);
                                aiWings.put(tempWingName, wing);
                            } catch (Exception ex) {
                                MainController.writeDebugLogFile(1, "MissionLoadController.loadMission - Error Unhandled Exception creating wing: " + ex);
                                MainController.writeDebugLogFile(1, " **** Line: " + line);
                            }
                        }
                        break;
                    case WING:
                        if (!line.startsWith("[")) {
                            try {
                                String parameter = line.split(" ")[0];
                                String value = line.split(" ")[1];
                                if (parameter.equals("Fuel")) {
                                    currentWing.setFuel(value);
                                } else if (parameter.equals("weapons")) {
                                    currentWing.setWeapons(value);
                                } else if (parameter.equals("Class")) {
                                    String plane = MainController.IL2AIRCLASSES.get(value);
                                    if (plane != null) {
                                        currentWing.setPlane(plane);
                                    }
                                    String planeClass = value.substring(4);
                                    if (planeClass != null) {
                                        currentWing.setPlaneClass(planeClass);
                                    } else {
                                        MainController.writeDebugLogFile(1, "MissionLoadController.loadMission - Error finding Plane name for class (" + value + ")");
                                    }
                                } else if (parameter.equals("Skill")) {
                                    currentWing.setSkillLevel(Integer.valueOf(value));
                                } else if (parameter.equals("Planes")) {
                                    currentWing.setPlaneCount(Integer.valueOf(value));
                                }
                            } catch (Exception ex) {
                                MainController.writeDebugLogFile(1, "MissionLoadController.loadMission - Error unhandled Exception with Wing Data: " + ex);
                                MainController.writeDebugLogFile(1, " *** Line: " + line);
                            }
                        }
                        break;
                    case STATIC:
                        if (line.indexOf("_Static") > -1) {
                            try {
                                missionObjectName = line.split(" ")[0];
                                rawObject = line.split(" ")[1];
                                objectArmy = Integer.valueOf(line.split(" ")[2]);
                                objectLocationX = Double.valueOf(line.split(" ")[3]);
                                objectLocationY = Double.valueOf(line.split(" ")[4]);
                                objectNameEndIndex = rawObject.indexOf("$");
                                objectName = rawObject.substring(objectNameEndIndex + 1);
                                addMissionObject(newMission, missionObjectName, objectName, missionObjectName, objectName, objectArmy, objectLocationX, objectLocationY, false);
                            } catch (Exception ex) {
                                MainController.writeDebugLogFile(1, "MissionLoadController.loadMission - Error Unhandled Exception processing Static Section: " + ex);
                                MainController.writeDebugLogFile(1, " *** Line: " + line);
                            }
                        }
                        break;
                    case TARGET:
                        if (!line.startsWith("[")) {
                            try {
                                targetDetails = line.split(" ");
                                objectArmy = Integer.valueOf(line.split(" ")[0]);
                                double locationX = Double.valueOf(line.split(" ")[5]);
                                double locationY = Double.valueOf(line.split(" ")[6]);
                                double targetRadius = Double.valueOf(line.split(" ")[7]);
                                int objectiveAmount = Integer.valueOf(targetDetails[4]) / 10;
                                missionObjectName = "Circle";
                                if (targetDetails.length > 9) { // If the length is greater than 8 then the target is a specific object

                                    if (targetDetails[9].equals("")) {  // For some reason Bridge targets have an extra space in the beginning which throws
                                                                       // off the pointer so I need to account for it.
                                        missionObjectName = " " + targetDetails[10];
                                        objectName = "Bridge";
                                        addMissionObject(newMission, missionObjectName, objectName, missionObjectName, objectName, objectArmy, locationX, locationY, false);
                                    } else {
                                        missionObjectName = targetDetails[9];
                                    }

                                }

                                String mapGrid = CoordinatesController.getCoordinates(missionMap, newMission.isBigMap(), locationX, locationY);
//							MissionTargetObjective targetObjective = newMission.getMissionParameters().addtargetObjective(missionObjectName, objectArmy, mapGrid, locationX, locationY, targetRadius, objectiveAmount);
                                newMission.getMissionParameters().addtargetObjective(missionObjectName, objectArmy, mapGrid, locationX, locationY, targetRadius, objectiveAmount);
                            } catch (Exception ex) {
                                MainController.writeDebugLogFile(1, "MissionLoadController.loadMission - Error Unhandled Exception processing Target Section: " + ex);
                                MainController.writeDebugLogFile(1, " *** Line: " + line);
                            }
                        }
                        break;
                    default:
                        break;
                } // Switch
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MissionLoadController.loadMission - Error Unhandled Exception processing mission file: " + ex);
                MainController.writeDebugLogFile(1, " *** Line: " + line);
            }
        } // For
        try {
            // Add landscape as a mission Object as this seems to come up a lot
            MissionObject newMissionObject = new MissionObject();
            newMissionObject.setMissionObjectName("landscape");
            newMissionObject.setArmy(0);
            newMissionObject.setLocationX(0.0);
            newMissionObject.setLocationY(0.0);
            IL2StaticObject il2Object = null;
            il2Object = MainController.IL2STATICOBJECTS.get("Landscape");
            newMissionObject.setGroundObject(il2Object);
            newMission.addMissionObject("landscape", newMissionObject);
            MainController.writeDebugLogFile(2, "landscape object added DB ID: " + il2Object.getIl2ObjectID());
            loadTargetAreaData(newMission);
            newMission.setAiWings(aiWings);
            addAIPlaneObjectives(newMission);
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionLoadObjects.loadMission - Error Unhandled Exception Adding landscape Object: " + ex);
        }
        addOverrunPlanes(aerodromeOverrunList, newMission.getMissionParameters().getAerodromes());
        MainController.writeDebugLogFile(1, "Mission File Loaded (" + missionFileName + ")");
        return newMission;
    }

    public static MissionParameters mergeMissionDetails(MissionParameters misFile, MissionParameters fbdjFile, boolean displayMessages) {
        ArrayList<String> msg = new ArrayList<String>();
        String differenceMessage = null;
        try {

            if (!misFile.getMapName().equals(fbdjFile.getMapName())) {
                differenceMessage = "Map Name Different MIS Map(" + misFile.getMissionName() + ") FBDj Map(" + fbdjFile.getMissionName() + ")\\n";
                msg.add(differenceMessage);
            }

            misFile.setTimeLimit(fbdjFile.getTimeLimit());
            misFile.setObjectiveType(fbdjFile.getObjectiveType());
            misFile.setBlueCountObjectivesNeeded(fbdjFile.getBlueCountObjectivesNeeded());
            misFile.setRedCountObjectivesNeeded(fbdjFile.getRedCountObjectivesNeeded());
            misFile.setBlueTargetObjectivesNeeded(fbdjFile.getBlueTargetObjectivesNeeded());
            misFile.setRedTargetObjectivesNeeded(fbdjFile.getRedTargetObjectivesNeeded());

            for (int i = 0; i < fbdjFile.getPlaneLoadoutRestrictions().size(); i++) {
                int army = fbdjFile.getPlaneLoadoutRestrictions().get(i).getArmy();
                String planeName = fbdjFile.getPlaneLoadoutRestrictions().get(i).getPlane();
                boolean planeFound = false;
                for (Aerodrome misAerodrome : misFile.getAerodromes()) {
                    if (misAerodrome.getPlanes().containsKey(planeName) && misAerodrome.getArmy() == army) {
                        misFile.addPlaneLoadoutRestriction(army, planeName, fbdjFile.getPlaneLoadoutRestrictions().get(i).getWeapon());
                        planeFound = true;
                        break;
                    }
                }
                if (!planeFound) {
                    differenceMessage = "Loadout Restriction for (" + planeName + ") failed to load.  Plane no longer available!\\n";
                    msg.add(differenceMessage);
                }
            }

            for (Aerodrome misAerodrome : misFile.getAerodromes()) {
                boolean aerodromeFound = false;
                for (Aerodrome fbdjAerodrome : fbdjFile.getAerodromes()) {
                    if (misAerodrome.getAerodromeLocationX() == fbdjAerodrome.getAerodromeLocationX() && misAerodrome.getAerodromeLocationY() == fbdjAerodrome.getAerodromeLocationY()) {
                        // Airports Match loop through and get any plane limits
                        Iterator<String> it = misAerodrome.getPlanes().keySet().iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            PlaneLimit fbdjPlane = null;
                            fbdjPlane = fbdjAerodrome.getPlanes().get(key);
                            if (fbdjPlane != null) {
                                misAerodrome.getPlanes().get(key).setPlanesInUseLimit(fbdjPlane.getPlanesInUseLimit());
                                misAerodrome.getPlanes().get(key).setPlaneTotalLimit(fbdjPlane.getPlaneTotalLimit());
                            } else {
                                differenceMessage = "New Plane in MIS file (" + key + ")\\n";
                                msg.add(differenceMessage);
                            }
                        }
                        aerodromeFound = true;
                        break;
                    }
                }
                if (!aerodromeFound) {
                    differenceMessage = "New Aerodrome in MIS file (" + misAerodrome.getAerodromeMapGrid() + ")\\n";
                    msg.add(differenceMessage);
                }
            }

            for (MissionCountObjective misCountObjective : misFile.getCountObjectives()) {
                boolean countObjectiveFound = false;
                for (MissionCountObjective fbdjCountObjective : fbdjFile.getCountObjectives()) {
                    if (misCountObjective.getArmy() == fbdjCountObjective.getArmy() && misCountObjective.getObjectType() == fbdjCountObjective.getObjectType()) {
                        misCountObjective.setNumberToDestroy(fbdjCountObjective.getNumberToDestroy());
                        countObjectiveFound = true;
                        break;
                    }
                }
                if (!countObjectiveFound) {
                    differenceMessage = "New Count Objective in MIS file Army(" + misCountObjective.getArmy() + ") Type(" + misCountObjective.getObjectType().toString() + ")\\n";
                    msg.add(differenceMessage);
                }
            }

            for (MissionTargetObjective misTargetObjective : misFile.getTargetObjectives()) {
                boolean targetObjectiveFound = false;
                for (MissionTargetObjective fbdjTargetObjective : fbdjFile.getTargetObjectives()) {
                    if (misTargetObjective.getLocationX() == fbdjTargetObjective.getLocationX() && misTargetObjective.getLocationY() == fbdjTargetObjective.getLocationY()) {
                        misTargetObjective.setNumberToDestroy(fbdjTargetObjective.getNumberToDestroy());
                        misTargetObjective.setArmy(fbdjTargetObjective.getArmy());
                        targetObjectiveFound = true;
                        break;
                    }
                }
                if (!targetObjectiveFound) {
                    differenceMessage = "New Target Objective in MIS file (" + misTargetObjective.getMapGridLocation() + ")";
                    msg.add(differenceMessage);
                }
            }
            if (displayMessages) {
                if (msg.size() > 0) {
                    JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                for (String lineOut : msg) {
                    MainController.writeDebugLogFile(1, "MissionLoadController.mergeMissionDetails Message: " + lineOut);
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionLoadController.mergeMissionDetails - Error Unhandled Exception processing merge: " + ex);
        }
        return misFile;
    }
}
