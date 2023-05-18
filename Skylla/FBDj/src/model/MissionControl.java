package model;

// Stores mission victory attributes, and next mission to load.

import java.util.HashMap;

public class MissionControl {
    private Boolean                             missionOver                = false;
    private Boolean                             missionOverMessageSent     = false;
    private Boolean                             missionEndTimeLimitExpired = false;
    private Boolean                             missionEndSent             = false;
    private Boolean                             resetMissionCycle          = false;
    private Boolean                             stop                       = false;
    private MissionFile                         tempMission                = null;
    private int                                 requestedMissionPointer    = -1;
    private Boolean                             missionEndReceived         = false;
    private HashMap<String, DifficultySettings> difficultySettings         = new HashMap<String, DifficultySettings>();
    private String                              nextMission                = null;
    private String                              currentDifficulty          = "";
    private ScheduledEvent                      missionCycleEvent          = null;
    private MissionCycle                        currentMissionCycle        = null;
    private HashMap<String, MissionFile>        currentMissionFiles        = null;
    private MissionFile                         currentMission             = null;
    private Boolean                             voteCalled                 = false;
    private long                                voteEndTime                = 0;

    public Boolean getVoteCalled() {
        return voteCalled;
    }

    public void setVoteCalled(Boolean voteCalled) {
        this.voteCalled = voteCalled;
    }

    public long getVoteEndTime() {
        return voteEndTime;
    }

    public void setVoteEndTime(long voteTime) {
        this.voteEndTime = voteTime;
    }

    public MissionCycle getCurrentMissionCycle() {
        return currentMissionCycle;
    }

    public void setCurrentMissionCycle(MissionCycle currentMissionCycle) {
        this.currentMissionCycle = currentMissionCycle;
    }

    public ScheduledEvent getMissionCycleEvent() {
        return missionCycleEvent;
    }

    public void setMissionCycleEvent(ScheduledEvent missionCycleEvent) {
        this.missionCycleEvent = missionCycleEvent;
    }

    public String getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setCurrentDifficulty(String currentDifficulty) {
        this.currentDifficulty = currentDifficulty;
    }

    public void setMissionOver(Boolean missionOver) {
        this.missionOver = missionOver;
    }

    public void setMissionOverMessageSent(Boolean messageSent) {
        this.missionOverMessageSent = messageSent;
    }

    public boolean getMissionOverMessageSent() {
        return missionOverMessageSent;
    }

    public void setMissionEndSent(Boolean missionEndSent) {
        this.missionEndSent = missionEndSent;
    }

    public boolean getMissionEndSent() {
        return missionEndSent;
    }

    public void setNextMission(String nextMission) {
        this.nextMission = nextMission;
    }

    public String getNextMission() {
        return nextMission;
    }

    public void setMissionEndTimeLimitExpired(Boolean expired) {
        this.missionEndTimeLimitExpired = expired;
    }

    public boolean getMissionEndTimeLimitExpired() {
        return missionEndTimeLimitExpired;
    }

    public void setResetMissionCycle(Boolean resetCycle) {
        this.resetMissionCycle = resetCycle;
    }

    public void setTempMission(MissionFile tempMission) {
        this.tempMission = tempMission;
    }

    public void setMissionEndReceived(boolean missionEndReceived) {
        this.missionEndReceived = missionEndReceived;
    }

    public boolean getStop() {
        return stop;
    }

    public MissionFile getTempMission() {
        return tempMission;
    }

    public boolean getMissionOver() {
        return missionOver;
    }

    public boolean getResetMissionCycle() {
        return resetMissionCycle;
    }

    public boolean getMissionEndReceived() {
        return missionEndReceived;
    }

    public HashMap<String, DifficultySettings> getDifficultySettings() {
        return difficultySettings;
    }

    public void setDifficultySettings(HashMap<String, DifficultySettings> difficultySettings) {
        this.difficultySettings = difficultySettings;
    }

    public int getRequestedMissionPointer() {
        return requestedMissionPointer;
    }

    public void setRequestedMissionPointer(int requestedMissionPointer) {
        this.requestedMissionPointer = requestedMissionPointer;
    }

    public HashMap<String, MissionFile> getCurrentMissionFiles() {
        return currentMissionFiles;
    }

    public void setCurrentMissionFiles(HashMap<String, MissionFile> currentMissionFiles) {
        this.currentMissionFiles = currentMissionFiles;
    }

    public MissionFile getCurrentMission() {
        return currentMission;
    }

    public void setCurrentMission(MissionFile currentMission) {
        this.currentMission = currentMission;
    }

} // End of Class
