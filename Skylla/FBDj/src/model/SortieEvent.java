package model;

public class SortieEvent {

    public enum EventType {
        MISSIONWON, MISSIONBEGIN, MISSIONEND, PILOTCONNECT, PILOTDISCONNECT, PILOTREFLY, PILOTTAKEOFF, PILOTLANDED, PILOTBAILEDOUT, PILOTKILLEDAT, PILOTKILLEDBY, PILOTCHUTEKILLED, PILOTCHUTEDESTROYED, PILOTWOUNDED, PILOTHEAVILYWOUNDED, PILOTCAPTURED, SELECTPLANE, LOADEDWEAPONS, PLANEDAMAGEDBY, PLANEDAMAGEDONGROUND, PLANECRASHED, SHOTDOWN, DESTROYED, DESTROYEDBY, SHOTDOWNBY, KILLED, PLANEDAMAGED, KILLEDPILOTINCHUTE, KILLEDBAILINGOUT, PILOTBANNED, PILOTKICKED, ILLEGALLOADOUT, PLANELIMITREACHED, PILOTBANNEDFAIR, PILOTBANNEDFGROUND, CHANGEDPOSITION, PILOTKICKEDLANGUAGE, PILOTKICKEDMARKINGS, CRASHED, DAMAGED, DAMAGEDBY,
        // MDS RRR Events
        MDS_RRR_REARM_BOMBS, MDS_RRR_REARM_GUNS, MDS_RRR_REARM_ROCKETS, MDS_RRR_UNLOAD_BOMBS, MDS_RRR_UNLOAD_GUNS, MDS_RRR_UNLOAD_ROCKETS, MDS_RRR_FUEL_REFUEL, MDS_RRR_FUEL_UNLOAD, MDS_RRR_REPAIR_AIRCRAFT, MDS_RRR_REPAIR_ENGINE, MDS_RRR_CHANGE_LOADOUT
    }

    private int                        pilotId;
    private int                        sortieId;
    private EventType                  eventType;
    private long                       eventTime;
    private int                        opponentId;
    private String                     opponentName;
    private IL2StaticObject.ObjectType opponentObjectType = IL2StaticObject.ObjectType.MISC;
    private int                        opponentSortieId;
    private long                       opponentSortieStartTime;
    private int                        opponentArmy;
//	private String                      damage;
//	private double                      damageValue = 0.0;
    private double                     locationX;
    private double                     locationY;
    private String                     targetGrid;

    public SortieEvent(long eventTime, EventType eventType, int pilotId) {
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.pilotId = pilotId;
    }

    public int getPilotId() {
        return pilotId;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public int getSortieId() {
        return sortieId;
    }

    public void setSortieId(int sortieId) {
        this.sortieId = sortieId;
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

    public IL2StaticObject.ObjectType getOpponentObjectType() {
        return opponentObjectType;
    }

    public void setOpponentObjectType(IL2StaticObject.ObjectType objectType) {
        this.opponentObjectType = objectType;
    }

    public int getOpponentSortieId() {
        return opponentSortieId;
    }

    public void setOpponentSortieId(int opponentSortieId) {
        this.opponentSortieId = opponentSortieId;
    }

    public int getOpponentArmy() {
        return opponentArmy;
    }

    public void setOpponentArmy(int opponentArmy) {
        this.opponentArmy = opponentArmy;
    }

//	public String getDamage() {
//		return damage;
//	}
//
//	public void setDamage(String damage) {
//		this.damage = damage;
//	}
//
//	public double getDamageValue() {
//		return damageValue;
//	}
//
//	public void setDamageValue(double damageValue) {
//		this.damageValue = damageValue;
//	}

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

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public long getOpponentSortieStartTime() {
        return opponentSortieStartTime;
    }

    public void setOpponentSortieStartTime(long opponentSortieStartTime) {
        this.opponentSortieStartTime = opponentSortieStartTime;
    }

    public String getTargetGrid() {
        return targetGrid;
    }

    public void setTargetGrid(String targetGrid) {
        this.targetGrid = targetGrid;
    }

}
