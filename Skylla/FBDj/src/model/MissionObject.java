package model;

import java.io.Serializable;

import model.IL2StaticObject.ObjectType;

/*
 * Stores the Static Ground Object information
 * for the current running mission.
 */

public class MissionObject implements Serializable {
    private static final long      serialVersionUID       = 1L;

    // Type of object (AAA, Artillery, Car, etc.)
    private ObjectType             objectType             = ObjectType.MISC;
    // Name to be stored in database and used for stats

    // Name assigned by the mission editor (i.e. 220_Static)
    private String                 missionObjectName      = "Unknown";
    // If part of a Multi Object Convoy, get MissionObjectName for Multi Objects usually xx_Chief
    private String                 missionMultiObjectName = "Unknown";
    // Multi Object Name
    private String                 multiObjectName        = "Unknown";
    // Which army the object was assigned to in the map
    private int                    army                   = 0;
    // Set to true when the object has been destroyed
    private boolean                destroyed              = false;
    // X coord of where object has been placed on the map
    private double                 locationX              = 0.0;
    // Y coord of where object has been placed on the map
    private double                 locationY              = 0.0;
    // Target Grid of where object has been placed on the map
    private String                 targetGrid             = null;
    // Mission Target Objective this object resides in
    private MissionTargetObjective missionTargetObjective = null;
    // Mission Count Objective this object resides in
    private MissionCountObjective  missionCountObjective  = null;
    // IL2 Static Ground Object
    private IL2StaticObject        groundObject           = null;

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void setMissionObjectDestroyed() {
        this.destroyed = true;
    }

    public String getMissionObjectName() {
        return missionObjectName;
    }

    public MissionCountObjective getMissionCountObjective() {
        return missionCountObjective;
    }

    public MissionTargetObjective getMissionTargetObjective() {
        return missionTargetObjective;
    }

    public int getArmy() {
        return army;
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public double getLocationX() {
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public IL2StaticObject getGroundObject() {
        return groundObject;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public void setGroundObject(IL2StaticObject il2Object) {
        this.groundObject = il2Object;
    }

    public void setMissionObjectName(String name) {
        this.missionObjectName = name;
    }

    public void setMissionCountObjective(MissionCountObjective missionCountObjective) {
        this.missionCountObjective = missionCountObjective;
    }

    public void setMissionTargetObjective(MissionTargetObjective missionTargetObjective) {
        this.missionTargetObjective = missionTargetObjective;
    }

    public String getTargetGrid() {
        return targetGrid;
    }

    public void setTargetGrid(String targetGrid) {
        this.targetGrid = targetGrid;
    }

    public String getMultiObjectName() {
        return multiObjectName;
    }

    public void setMultiObjectName(String multiObjectName) {
        this.multiObjectName = multiObjectName;
    }

    public String getMissionMultiObjectName() {
        return missionMultiObjectName;
    }

    public void setMissionMultiObjectName(String missionMultiObjectName) {
        this.missionMultiObjectName = missionMultiObjectName;
    }

}
