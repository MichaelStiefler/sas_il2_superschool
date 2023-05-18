package model;

import java.io.File;
import java.io.Serializable;

public class MissionFile implements Serializable {
    private static final long serialVersionUID = 1L;
    private String            directory;
    private String            missionName;
    private String            difficulty;
    private String            redWonMission;
    private String            blueWonMission;

    public MissionFile(String missionName) {
        this.missionName = missionName;
        this.directory = "FBDj" + File.separatorChar;
        this.difficulty = "Default";
        this.redWonMission = "None";
        this.blueWonMission = "None";
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getRedWonMission() {
        return redWonMission;
    }

    public void setRedWonMission(String redWonMission) {
        this.redWonMission = redWonMission;
    }

    public String getBlueWonMission() {
        return blueWonMission;
    }

    public void setBlueWonMission(String blueWonMission) {
        this.blueWonMission = blueWonMission;
    }
}