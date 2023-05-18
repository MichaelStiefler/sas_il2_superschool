package model;

import java.io.Serializable;

/*
 * Stores general targets (i.e. 5 tanks) for the current running mission.
 */

public class MissionCountObjective implements Serializable {
    private static final long          serialVersionUID  = 1L;

    private int                        army;
    private IL2StaticObject.ObjectType objectType;
    private int                        numberToDestroy;
    private int                        missionStartCount = 0;
    private int                        missionLostCount  = 0;
    private boolean                    objectiveMet      = false;

    public MissionCountObjective(int army, IL2StaticObject.ObjectType objType, int count) {
        this.army = army;
        this.objectType = objType;
        this.numberToDestroy = count;
    }

    public void setMissionLostCount(int count) {
        missionLostCount = count;
    }

    public void setMissionStartCount(int count) {
        missionStartCount = count;
    }

    public void setNumberToDestroy(int count) {
        numberToDestroy = count;
    }

    public void setObjectiveMet(boolean objectiveMet) {
        this.objectiveMet = objectiveMet;
    }

    public int getArmy() {
        return army;
    }

    public boolean getObjectiveMet() {
        return objectiveMet;
    }

    public IL2StaticObject.ObjectType getObjectType() {
        return objectType;
    }

    public int getMissionLostCount() {
        return missionLostCount;
    }

    public int getNumberToDestroy() {
        return numberToDestroy;
    }

    public int getMissionStartCount() {
        return missionStartCount;
    }

}
