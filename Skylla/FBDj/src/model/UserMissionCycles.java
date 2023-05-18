package model;

import java.io.Serializable;
import java.util.HashMap;

public class UserMissionCycles implements Serializable {
    private static final long                   serialVersionUID = 1L;
    private HashMap<String, MissionCycle>       missionCycleList;
    private HashMap<String, DifficultySettings> difficultySettings;
    private HashMap<String, MissionFile>        missionFiles;

    public UserMissionCycles() {
        this.missionCycleList = new HashMap<String, MissionCycle>();
        this.difficultySettings = new HashMap<String, DifficultySettings>();
        this.missionFiles = new HashMap<String, MissionFile>();
    }

    public HashMap<String, MissionCycle> getMissionCycleList() {
        return missionCycleList;
    }

    public void setMissionCycleList(HashMap<String, MissionCycle> missionCycleList) {
        this.missionCycleList = missionCycleList;
    }

    public HashMap<String, DifficultySettings> getDifficultySettings() {
        return difficultySettings;
    }

    public void setDifficultySettings(HashMap<String, DifficultySettings> difficultySettings) {
        this.difficultySettings = difficultySettings;
    }

    public HashMap<String, MissionFile> getMissionFiles() {
        return missionFiles;
    }

    public void setMissionFiles(HashMap<String, MissionFile> missionFiles) {
        this.missionFiles = missionFiles;
    }

}
