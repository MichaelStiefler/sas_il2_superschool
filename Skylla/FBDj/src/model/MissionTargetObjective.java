package model;

import java.io.Serializable;

/*
 * Stores specific targets, declared through the mission editor,
 * for the current running mission.
 */

public class MissionTargetObjective implements Serializable {
    private static final long serialVersionUID = 1L;

    private String            targetName;
    private String            targetDesc;
    private int               army;
    private double            locationX        = 0.0;
    private double            locationY        = 0.0;
    private double            targetRadius     = 0.0;
    private int               numberToDestroy  = 0;
    private int               totalTargets     = 0;
    private int               targetsLost      = 0;
    private boolean           objectiveMet     = false;
    private String            mapGridLocation;

    // Constructor requires all fields to create
    public MissionTargetObjective(int army, double locationX, double locationY, double targetRadius, int numberToDestroy) {
        this.army = army;
        this.locationX = locationX;
        this.locationY = locationY;
        this.targetRadius = targetRadius;
        this.numberToDestroy = numberToDestroy;
        this.targetName = "Circle";
        this.targetDesc = "Tgt Circle";
    }

    public int getArmy() {
        return army;
    }

    public double getLocationX() {
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public double getTargetRadius() {
        return targetRadius;
    }

    public int getTotalTargets() {
        return totalTargets;
    }

    public int getTargetsLost() {
        return targetsLost;
    }

    public int getNumberToDestroy() {
        return numberToDestroy;
    }

    public String getMapGridLocation() {
        return mapGridLocation;
    }

    public void setMapGridLocation(String mapGridLocation) {
        this.mapGridLocation = mapGridLocation;
    }

    public void setObjectiveMet(boolean objectiveMet) {
        this.objectiveMet = objectiveMet;
    }

    public boolean getObjectiveMet() {
        return objectiveMet;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public void setTotalTargets(int count) {
        this.totalTargets = count;
    }

    public void setTargetsLost(int count) {
        this.targetsLost = count;
    }

    public void setNumberToDestroy(int pct) {
        this.numberToDestroy = pct;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetDesc() {
        return targetDesc;
    }

    public void setTargetDesc(String targetDesc) {
        this.targetDesc = targetDesc;
    }

}
