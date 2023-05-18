package viewController;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import mainController.MainController;
import mainController.MissionLoadController;
import model.Aerodrome;
import model.IL2StaticObject;
import model.Mission;
import model.MissionCountObjective;
import model.MissionParameters;
import model.MissionTargetObjective;
import model.PlaneLimit;
import model.PlaneLoadoutRestriction;
import model.Weapon;
import utility.FileRead;
import utility.FileWrite;
import view.MissionBuilderPanel;

public class MissionBuilderController {

    public static MissionParameters readMission(String missionFileName) {
        MissionParameters missionDetails = new MissionParameters();
        if ((missionDetails = (MissionParameters) FileRead.getFileSerialized(missionFileName)) == null) {
            return null;
        } else {
            return missionDetails;
        }

    }

    public static Mission loadMission(String missionFileName) {
        Mission mission = MissionLoadController.loadMission(missionFileName);
        if (mission != null) {
            mission.getMissionParameters().addCountObjective(MainController.REDARMY, IL2StaticObject.ObjectType.PILOT, 0);
            mission.getMissionParameters().addCountObjective(MainController.REDARMY, IL2StaticObject.ObjectType.PLANE, 0);
            mission.getMissionParameters().addCountObjective(MainController.BLUEARMY, IL2StaticObject.ObjectType.PILOT, 0);
            mission.getMissionParameters().addCountObjective(MainController.BLUEARMY, IL2StaticObject.ObjectType.PLANE, 0);
//			mission.getMissionParameters().setBigMap(mission.isBigMap());
            mission.getMissionParameters().setMapName(mission.getMapName());
            mission.getMissionParameters().setMissionName(mission.getMissionName());
//			loadTargetAreaData(missionDetails);
            MainController.writeDebugLogFile(1, "MissionBuilderController.loadMission - Mission File Loaded");
        } else {
            MainController.writeDebugLogFile(1, "MissionBuilderController.loadMission - Error !! Mission File Failed to Load !!");
        }
        return mission;
    }

    public static void missionParametersWrite(Object o, String directory, String fileName) {
        FileWrite.writeFileSerialized(directory, fileName, o);
        MainController.writeDebugLogFile(1, "MissionParametersController.missionParametersWrite: Mission Parameters written to file.");
    }

    public static void removeLoadoutRestriction(int army, String plane) {
        MissionBuilderPanel.getMissionDetails().removeLoadoutRestrictionsForPlane(army, plane);
    }

    public static void removePlaneLimit(PlaneLimit planeLimit) {
        planeLimit.setPlanesInUseLimit(0);
        planeLimit.setPlaneTotalLimit(0);
    }

    public static void updatePlaneLimit(Aerodrome aerodrome, String plane, int inUseLimit, int totalLimit) {
        aerodrome.getPlanes().get(plane).setPlanesInUseLimit(inUseLimit);
        aerodrome.getPlanes().get(plane).setPlaneTotalLimit(totalLimit);
    }

    public static void updateLoadoutRestriction(String weapon, PlaneLoadoutRestriction planeLoadoutId) {
        planeLoadoutId.setWeapon(weapon);
    }

    public static ArrayList<Weapon> getPlaneWeapons(String planeName) {
        ArrayList<Weapon> weapons = null;
        weapons = MainController.IL2PLANELOADOUTS.get(planeName);
        return weapons;
    }

    public static MissionParameters mergeMissionDetails(MissionParameters misFile, MissionParameters fbdjFile) {
        MissionLoadController.mergeMissionDetails(misFile, fbdjFile, true);
        return misFile;
    }

    public static MissionParameters oldMergeMissionDetails(MissionParameters misFile, MissionParameters fbdjFile) {
        ArrayList<String> msg = new ArrayList<String>();
        String differenceMessage = null;
        if (!misFile.getMapName().equals(fbdjFile.getMapName())) {
            differenceMessage = "Map Name Different MIS Map(" + misFile.getMissionName() + ") FBDj Map(" + fbdjFile.getMissionName() + ")";
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
                differenceMessage = "Loadout Restriction for (" + planeName + ") failed to load.  Plane no longer available!";
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
                            differenceMessage = "New Plane in MIS file (" + key + ")";
                            msg.add(differenceMessage);
                        }
                    }
                    aerodromeFound = true;
                    break;
                }
            }
            if (!aerodromeFound) {
                differenceMessage = "New Aerodrome in MIS file (" + misAerodrome.getAerodromeMapGrid() + ")";
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
                differenceMessage = "New Count Objective in MIS file Army(" + misCountObjective.getArmy() + ") Type(" + misCountObjective.getObjectType().toString() + ")";
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
                differenceMessage = "New Target Objective in MIS file Army(" + misTargetObjective.getMapGridLocation() + ")";
                msg.add(differenceMessage);
            }
        }

        if (msg.size() > 0) {
            JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        return misFile;
    }
}
