package model;

public class Event {

    private SortieEvent.EventType eventType;
    private String                damage;
    private double                damageValue;
    private long                  eventTime;
    private String                attackerName;
    private Object                attackerObject;
    private String                victimName;
    private Object                victimObject;
    private Location              location;

    public Event(long eventTime, SortieEvent.EventType eventType) {

        this.eventTime = eventTime;
        this.eventType = eventType;

    }

    public SortieEvent.EventType getEventType() {
        return eventType;
    }

    public void setEventType(SortieEvent.EventType eventType) {
        this.eventType = eventType;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public String getAttackerName() {
        return attackerName;
    }

    public void setAttackerName(String attackerName) {
        this.attackerName = attackerName;
    }

    public Object getAttackerObject() {
        return attackerObject;
    }

    public void setAttackerObject(Object attackerObject) {
        this.attackerObject = attackerObject;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public Object getVictimObject() {
        return victimObject;
    }

    public void setVictimObject(Object victimObject) {
        this.victimObject = victimObject;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public double getDamageValue() {
        return damageValue;
    }

    public void setDamageValue(double damageValue) {
        this.damageValue = damageValue;
    }

}
