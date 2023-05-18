package model;

import java.io.Serializable;

public class MissionCycleEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    private String            missionName;

    public MissionCycleEntry(String mission) {
        this.missionName = mission;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

}
