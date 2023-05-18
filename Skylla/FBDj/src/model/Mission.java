package model;

/*
 * Stores current running mission general attributes,
 * and all the mission objects.
 */

import java.util.HashMap;

public class Mission {

    public enum MissionObjectiveType {
        TARGET, COUNT, BOTH, EITHER
    }

    private String                         missionName;
    private String                         mapName           = "Unknown";
    private boolean                        bigMap            = false;
    private long                           startTime         = 0;
    private long                           endTime           = 0;
    private int                            winner            = 0;
    private HashMap<String, MissionObject> missionObjects    = new HashMap<String, MissionObject>();
    private HashMap<String, AIWing>        aiWings;
    private MissionParameters              missionParameters = new MissionParameters();

    public HashMap<String, AIWing> getAiWings() {
        return aiWings;
    }

    public void setAiWings(HashMap<String, AIWing> aiWings) {
        this.aiWings = aiWings;
    }

    public MissionParameters getMissionParameters() {
        return missionParameters;
    }

    public void setMissionParameters(MissionParameters missionParameters) {
        this.missionParameters = missionParameters;
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

    public boolean isBigMap() {
        return bigMap;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public HashMap<String, MissionObject> getMissionObjects() {
        return missionObjects;
    }

    public int getWinner() {
        return winner;
    }

    // Now all the Sets

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setBigMap(boolean bigMap) {
        this.bigMap = bigMap;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public void addMissionObject(String objectName, MissionObject newMissionObject) {
        this.missionObjects.put(objectName, newMissionObject);
    }

} // End Class
