package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class MissionCycle implements Serializable {
    private static final long            serialVersionUID = 1L;
    private ArrayList<MissionCycleEntry> missionCycleEntries;
    private String                       missionCycleName;
    private int                          missionPointer   = -1;

    public MissionCycle(String name) {
        missionCycleName = name;
        missionCycleEntries = new ArrayList<MissionCycleEntry>();
    }

    public String getMissionCycleName() {
        return missionCycleName;
    }

    public void setMissionCycleName(String missionCycleName) {
        this.missionCycleName = missionCycleName;
    }

    public int getMissionPointer() {
        return missionPointer;
    }

    public void setMissionPointer(int missionPointer) {
        this.missionPointer = missionPointer;
    }

    public MissionCycle() {
        this.missionCycleEntries = new ArrayList<MissionCycleEntry>();
    }

    public ArrayList<MissionCycleEntry> getMissionFiles() {
        return missionCycleEntries;
    }

    public void setMissionFiles(ArrayList<MissionCycleEntry> missionFiles) {
        this.missionCycleEntries = missionFiles;
    }
    
    // TODO: Storebror: Random Mission Rotation ++++++++++++++++++++++++++++
    public int setRandomMissionPointer() {
        int retVal = 0;
        Random rnd = new Random();
        do { retVal = rnd.nextInt(missionCycleEntries.size()); } while ( retVal == missionPointer );
        missionPointer = retVal;
        return retVal;
    }
    // TODO: Storebror: Random Mission Rotation ----------------------------
   
}  // End MissionCycle

