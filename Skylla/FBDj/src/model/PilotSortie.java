package model;

import java.util.HashMap;

/*
 * Creates a sortie object for the pilot's current sortie.
 * Essentially this copies the pilots attributes, and specific sortie attributes at time of takeoff.
 */

public class PilotSortie extends PilotBlueprint {
    public enum PlaneStatus {
        OK, INFLIGHT, LANDED, SHOTDOWN, DAMAGED, CRASHED
    };

    public enum PilotStatus {
        OK, KIA, BAILEDOUT, WOUNDED, CAPTURED, KIACHUTEKILLED, KIABAILINGOUT, MIA
    };

    // Sortie start time and its ID
    private long                       missionStartTime;
    private long                       sortieStartTime;
    private PilotStatus                pilotStatus;
    private PlaneStatus                planeStatus;
    private long                       bonusPoints;
    private boolean                    bailedOut;
    private double                     spawnPointX;
    private double                     spawnPointY;

    // Sortie events (destroyed controls, etc) as <Time, Event>
    private HashMap<Long, SortieEvent> sortieEvents = new HashMap<Long, SortieEvent>();

    public PilotSortie(Pilot pilot, long sortieStartTime, long missionStartTime) {
        this.missionStartTime = missionStartTime;
        this.name = pilot.getName();
        this.pilotId = pilot.getPilotId();
        this.score = pilot.getScore();
        this.army = pilot.getArmy();
        this.weapons = "Unknown";
        this.fuel = "Unknown";
        this.plane = "Unknown";
        this.planeMarkings = "Unknown";
        this.eAir = pilot.getEAir();
        this.fAir = pilot.getFAir();
        this.fiBull = pilot.getFiBull();
        this.hiBull = pilot.getHiBull();
        this.hiABull = pilot.getHiABull();
        this.fiRock = pilot.getFiRock();
        this.hiRock = pilot.getHiRock();
        this.fiBomb = pilot.getFiBomb();
        this.hiBomb = pilot.getHiBomb();
        this.eAirConfirmed = pilot.getEAirConfirmed();
        this.sortieStartTime = sortieStartTime;
        this.pilotStatus = PilotStatus.OK;
        this.planeStatus = PlaneStatus.OK;
        this.bailedOut = false;
        this.pilotId = pilot.getPilotId();
        this.spawnPointX = 0.0;
        this.spawnPointY = 0.0;

    }

    public void setSortieEvents(long time, SortieEvent event) {
        this.sortieEvents.put(time, event);
    }

    public HashMap<Long, SortieEvent> getSortieEvents() {
        return this.sortieEvents;
    }

    public long getSortieStartTime() {
        return this.sortieStartTime;
    }

    public void setSortieStartTime(long time) {
        this.sortieStartTime = time;
    }

    public PilotStatus getPilotStatus() {
        return pilotStatus;
    }

    public void setPilotStatus(PilotStatus pilotStatus) {
        this.pilotStatus = pilotStatus;
    }

    public PlaneStatus getPlaneStatus() {
        return planeStatus;
    }

    public void setPlaneStatus(PlaneStatus planeStatus) {
        this.planeStatus = planeStatus;
    }

    public boolean isBailedOut() {
        return bailedOut;
    }

    public void setBailedOut(boolean bailedOut) {
        this.bailedOut = bailedOut;
    }

    public long getMissionStartTime() {
        return missionStartTime;
    }

    public void setMissionStartTime(long missionStartTime) {
        this.missionStartTime = missionStartTime;
    }

    public long getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(long bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public double getSpawnPointX() {
        return spawnPointX;
    }

    public void setSpawnPointX(double spawnPointX) {
        this.spawnPointX = spawnPointX;
    }

    public double getSpawnPointY() {
        return spawnPointY;
    }

    public void setSpawnPointY(double spawnPointY) {
        this.spawnPointY = spawnPointY;
    }
}
