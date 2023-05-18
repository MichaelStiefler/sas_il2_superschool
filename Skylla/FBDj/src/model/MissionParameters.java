package model;

import java.io.Serializable;
import java.util.ArrayList;
import mainController.MissionController;

import mainController.MainController;

public class MissionParameters implements Serializable {
    private static final long                  serialVersionUID           = 1L;

    private String                             missionName;
    private String                             mapName;
    private long                               timeLimit;
    private Mission.MissionObjectiveType       objectiveType;
    private ArrayList<Aerodrome>               aerodrome;
    private ArrayList<PlaneLoadoutRestriction> planeLoadoutRestrictions;
    private ArrayList<MissionCountObjective>   countObjectives;
    private ArrayList<MissionTargetObjective>  targetObjectives;
    private ArrayList<MissionObject>           staticObjects;
    private int                                redCountObjectivesNeeded   = 0;
    private int                                blueCountObjectivesNeeded  = 0;
    private int                                redTargetObjectivesNeeded  = 0;
    private int                                blueTargetObjectivesNeeded = 0;

    public int getRedCountObjectivesNeeded() {
        return redCountObjectivesNeeded;
    }

    public void setRedCountObjectivesNeeded(int redCountObjectivesNeeded) {
        this.redCountObjectivesNeeded = redCountObjectivesNeeded;
    }

    public int getBlueCountObjectivesNeeded() {
        return blueCountObjectivesNeeded;
    }

    public void setBlueCountObjectivesNeeded(int blueCountObjectivesNeeded) {
        this.blueCountObjectivesNeeded = blueCountObjectivesNeeded;
    }

    public int getRedTargetObjectivesNeeded() {
        return redTargetObjectivesNeeded;
    }

    public void setRedTargetObjectivesNeeded(int redTargetObjectivesNeeded) {
        this.redTargetObjectivesNeeded = redTargetObjectivesNeeded;
    }

    public int getBlueTargetObjectivesNeeded() {
        return blueTargetObjectivesNeeded;
    }

    public void setBlueTargetObjectivesNeeded(int blueTargetObjectivesNeeded) {
        this.blueTargetObjectivesNeeded = blueTargetObjectivesNeeded;
    }

    public void setCountObjectivesNeeded(int army, int count) {
        if (army == MainController.REDARMY) {
            setRedCountObjectivesNeeded(getRedCountObjectivesNeeded() + 1);
        } else if (army == MainController.BLUEARMY) {
            setBlueCountObjectivesNeeded(getBlueCountObjectivesNeeded() + 1);
        }
    }

    public int getCountObjectivesNeeded(int army) {
        if (army == MainController.REDARMY) {
            return getRedCountObjectivesNeeded();
        } else if (army == MainController.BLUEARMY) {
            return getBlueCountObjectivesNeeded();
        } else
            return 0;
    }

    public void setTargetObjectivesNeeded(int army, int count) {
        if (army == MainController.REDARMY) {
            setRedTargetObjectivesNeeded(getRedTargetObjectivesNeeded() + 1);
        } else if (army == MainController.BLUEARMY) {
            setBlueTargetObjectivesNeeded(getBlueTargetObjectivesNeeded() + 1);
        }
    }

    public int getTargetObjectivesNeeded(int army) {
        if (army == MainController.REDARMY) {
            return getRedTargetObjectivesNeeded();
        } else if (army == MainController.BLUEARMY) {
            return getBlueTargetObjectivesNeeded();
        } else
            return 0;
    }

    public MissionParameters() {
        aerodrome = new ArrayList<Aerodrome>();
        planeLoadoutRestrictions = new ArrayList<PlaneLoadoutRestriction>();
        countObjectives = new ArrayList<MissionCountObjective>();
        targetObjectives = new ArrayList<MissionTargetObjective>();
        staticObjects = new ArrayList<MissionObject>();
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
        //TODO: skylla: Mission extension:
        MissionController.setMissionExtended(false);
    }

    public Mission.MissionObjectiveType getObjectiveType() {
        return objectiveType;
    }

    public void setObjectiveType(Mission.MissionObjectiveType objectiveType) {
        this.objectiveType = objectiveType;
    }

    public ArrayList<Aerodrome> getAerodromes() {
        return aerodrome;
    }

    public void setPlaneLimits(ArrayList<Aerodrome> planeLimits) {
        this.aerodrome = planeLimits;
    }

    public void addAerodrome(Aerodrome newAerodrome) {
        aerodrome.add(newAerodrome);
    }

    public void addPlane(String planeName, int homeBaseId) {
        aerodrome.get(homeBaseId).addPlane(planeName, 0, 0);
    }

    public ArrayList<PlaneLoadoutRestriction> getPlaneLoadoutRestrictions() {
        return planeLoadoutRestrictions;
    }

    public void setPlaneLoadoutRestrictions(ArrayList<PlaneLoadoutRestriction> planeLoadoutRestrictions) {
        this.planeLoadoutRestrictions = planeLoadoutRestrictions;
    }

    public ArrayList<MissionCountObjective> getCountObjectives() {
        return countObjectives;
    }

    public void setCountObjectives(ArrayList<MissionCountObjective> missionCountObjectives) {
        this.countObjectives = missionCountObjectives;
    }

    public ArrayList<MissionTargetObjective> getTargetObjectives() {
        return targetObjectives;
    }

    public ArrayList<MissionObject> getStaticObjects() {
        return staticObjects;
    }

    public void addCountObjective(int army, IL2StaticObject.ObjectType objectType, int count) {
        // Add new CountObjective to arraylist. Since this will be called when we get an object we need start count
        // to be set to count comming in.
        MissionCountObjective countObjective = new MissionCountObjective(army, objectType, 0);
        countObjective.setMissionStartCount(count);
        this.countObjectives.add(countObjective);
    }

    public MissionTargetObjective addtargetObjective(String targetName, int army, String mapGrid, double locationX, double locationY, double targetRadius, int percentRequired) {
        // Add new TargetObjective to arraylist.
        MissionTargetObjective targetObjective = new MissionTargetObjective(army, locationX, locationY, targetRadius, percentRequired);
        targetObjective.setTargetName(targetName);
        targetObjective.setMapGridLocation(mapGrid);
        targetObjectives.add(targetObjective);
        return targetObjective;
    }

    public void addStaticObject(MissionObject staticObject) {
        staticObjects.add(staticObject);
    }

    public void addPlaneLoadoutRestriction(int army, String plane, String weapon) {
        PlaneLoadoutRestriction newLoadoutRestriction = new PlaneLoadoutRestriction(army, plane, weapon);
        planeLoadoutRestrictions.add(newLoadoutRestriction);
    }

    public PlaneLoadoutRestriction getPlaneLoadoutRestriction(int army, String plane, String weapon) {
        PlaneLoadoutRestriction returnLoadout = null;
        for (PlaneLoadoutRestriction restrictedLoadout : planeLoadoutRestrictions) {
            if (restrictedLoadout.getArmy() == army && restrictedLoadout.getPlane().equals(plane) && restrictedLoadout.getWeapon().equals(weapon)) {
                returnLoadout = restrictedLoadout;
                break;
            }
        }
        return returnLoadout;
    }

    public void removeLoadoutRestrictionsForPlane(int army, String plane) {
        boolean planeFound = true;
        while (planeFound) {
            planeFound = false;
            for (int i = 0; i < planeLoadoutRestrictions.size(); i++) {
                if (planeLoadoutRestrictions.get(i).getArmy() == army && planeLoadoutRestrictions.get(i).getPlane().equals(plane)) {
                    planeLoadoutRestrictions.remove(i);
                    planeFound = true;
                    break;
                }
            }
        }
    }

    public MissionCountObjective getCountObjective(int army, IL2StaticObject.ObjectType objectType) {
        MissionCountObjective returnObjective = null;
        for (MissionCountObjective objective : countObjectives) {
            if (objective.getArmy() == army && objective.getObjectType() == objectType) {
                returnObjective = objective;
                break;
            }
        }
        return returnObjective;
    }

    // This Method returns the Target Objective if the X,Y coordinates passed are
    // within a Target Objective radius
    public MissionTargetObjective getMissionTargetObjective(double locationX, double locationY) {
        MissionTargetObjective returnObjective = null;
        for (MissionTargetObjective tempObjective : targetObjectives) {
            if ((java.lang.Math.pow(java.lang.Math.abs(locationX - tempObjective.getLocationX()), 2) + java.lang.Math.pow(java.lang.Math.abs(locationY - tempObjective.getLocationY()), 2)) <= java.lang.Math.pow(tempObjective.getTargetRadius(), 2)) { // Object
                                                                                                                                                                                                                                                         // is
                                                                                                                                                                                                                                                         // within
                                                                                                                                                                                                                                                         // the
                                                                                                                                                                                                                                                         // target
                                                                                                                                                                                                                                                         // area
                                                                                                                                                                                                                                                         // and
                                                                                                                                                                                                                                                         // is
                                                                                                                                                                                                                                                         // a
                                                                                                                                                                                                                                                         // valid
                                                                                                                                                                                                                                                         // target.
                returnObjective = tempObjective;
                break;
            }
        }
        return returnObjective;
    }

    // This Method returns the Target Objective if the missionObjectName matches the targetObjective Name
    public MissionTargetObjective getMissionTargetObjective(String missionObjectName) {
        MissionTargetObjective returnObjective = null;
        for (MissionTargetObjective tempObjective : targetObjectives) {
            if (tempObjective.getTargetName().equals(missionObjectName)) {
                returnObjective = tempObjective;
                break;
            }
        }
        return returnObjective;
    }

    public void setTargetObjectives(ArrayList<MissionTargetObjective> missionTargetObjectives) {
        this.targetObjectives = missionTargetObjectives;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

}
