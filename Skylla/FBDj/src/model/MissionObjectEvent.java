package model;

import model.SortieEvent.EventType;

public class MissionObjectEvent {

    private String                     missionObjectName;
    private int                        IL2ObjectId;
    private String                     IL2ObjectName;
    private IL2StaticObject.ObjectType IL2ObjectType;
    private int                        army;
    private EventType                  eventType;
    private long                       eventTime;
    private int                        opponentId;
    private String                     opponentName;
    private IL2StaticObject.ObjectType opponentObjectType;
    private long                       opponentSortieStartTime;
    private int                        opponentArmy;
    private double                     locationX;
    private double                     locationY;
    private String                     targetGrid;

    public MissionObjectEvent(long eventTime, EventType eventType, int objectId) {
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.IL2ObjectId = objectId;
    }

    public String getIL2ObjectName() {
        return IL2ObjectName;
    }

    public void setIL2ObjectName(String objectName) {
        IL2ObjectName = objectName;
    }

    public IL2StaticObject.ObjectType getIL2ObjectType() {
        return IL2ObjectType;
    }

    public void setIL2ObjectType(IL2StaticObject.ObjectType objectType) {
        IL2ObjectType = objectType;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public int getIL2ObjectId() {
        return IL2ObjectId;
    }

    public void setIL2ObjectId(int objectId) {
        this.IL2ObjectId = objectId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public int getOpponentId() {
        return opponentId;
    }

    public void setOpponentId(int opponentId) {
        this.opponentId = opponentId;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public IL2StaticObject.ObjectType getOpponentObjectType() {
        return opponentObjectType;
    }

    public void setOpponentObjectType(IL2StaticObject.ObjectType opponentObjectType) {
        this.opponentObjectType = opponentObjectType;
    }

    public int getOpponentArmy() {
        return opponentArmy;
    }

    public void setOpponentArmy(int opponentArmy) {
        this.opponentArmy = opponentArmy;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    public long getOpponentSortieStartTime() {
        return opponentSortieStartTime;
    }

    public void setOpponentSortieStartTime(long opponentSortieStartTime) {
        this.opponentSortieStartTime = opponentSortieStartTime;
    }

    public String getMissionObjectName() {
        return missionObjectName;
    }

    public void setMissionObjectName(String missionObjectName) {
        this.missionObjectName = missionObjectName;
    }

    public String getTargetGrid() {
        return targetGrid;
    }

    public void setTargetGrid(String targetGrid) {
        this.targetGrid = targetGrid;
    }

}
