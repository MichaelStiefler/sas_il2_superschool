package model;

public class ScheduledEvent {

    public enum EventType {
        MissionToRun, MissionCycleChange
    };

    private int       eventId        = 0;
    private EventType eventType;
    private String    eventName      = null;
    private String    lastMissionRan = null;

    public ScheduledEvent(int eventId, EventType eventType, String eventName) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.eventName = eventName;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLastMissionRan() {
        return lastMissionRan;
    }

    public void setLastMissionRan(String lastMissionRan) {
        this.lastMissionRan = lastMissionRan;
    }

}
