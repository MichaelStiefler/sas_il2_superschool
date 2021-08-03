// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CollideEnv;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.Mission;
import com.maddox.sas1946.il2.util.TrueRandom;

public class TriggerSpawnAircraftRelativeAltitude extends Trigger {

    public TriggerSpawnAircraftRelativeAltitude(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, String targetActorName, int altitudeMin, int altitudeMax, int triggeredBy, boolean triggerOnExit, int noObjectsMin, int probability,
            int deltaAltitude, String linkActorName, String displayMessage, int displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTriggerClass(TYPE_SPAWN_AIRCRAFT_RELATIVE_ALTITUDE);
        this.setTargetActorName(targetActorName);
        this.setDeltaAltitude(deltaAltitude);
        if (this.getTargetActorName() == "" || this.getTargetActorName() == null) this.destroy();
        World.cur().triggersGuard.getListTriggerAircraftSpawn().add(this.getTargetActorName());
        World.cur().triggersGuard.getListTriggerAircraftAirSpawnRelativeAltitude().add(this.getTargetActorName());
    }

    protected boolean checkPeriodic() {
        if (Actor.getByName(this.getTargetActorName()) == null) {
            this.checkMove();
            if (this.getLinkActorName() != "" && this.getLinkActorName() != null && Actor.getByName(this.getLinkActorName()) == null) return false;
            CollideEnv.ResultTrigger result = Engine.collideEnv().getEnemiesInCyl(this.getPosTrigger(), this.getRadius(), this.getAltitudeMin(), this.getAltitudeMax(), this.getTriggeredByArmy(), this.getTriggeredBy(),
                    this.getNoObjectsMin());
            if (this.isTriggerOnExit()) {
                if (this.isEntered()) {
                    this.setMessageAltitude((int) result.altiSea);
                    return !result.result;
                }
                if (result.result) {
                    this.setEntered(true);
                    this.setAltitude(result.altiSea + this.getDeltaAltitude());
                }
            } else if (result.result) {
                this.setAltitude(result.altiSea + this.getDeltaAltitude());
                this.setMessageAltitude((int) result.altiSea);
                return true;
            }
            return false;
        } else {
            this.destroy();
            return false;
        }
    }

    protected void execute() {
        if (TrueRandom.nextFloat(100F) < this.getProbability()) this.doExecute();
        super.execute();
    }
    
    protected void doExecute() {
        if (this.isTriggered()) return;
        this.setTriggered(true);
        if (this.getTargetActorName() != null && Actor.getByName(this.getTargetActorName()) == null) Mission.cur().loadWingOnTrigger(this.getTargetActorName());
        if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
            EventLog.onTriggerActivate(Actor.getByName(this.getTargetActorName()), this);
            this.doSendMsg(false);
        } else {
            EventLog.onTriggerActivateLink(Actor.getByName(this.getTargetActorName()), this);
            this.doSendMsg(true);
        }
        super.doExecute();
    }

    public double getAltitude() {
        return this.altitude;
    }

    public String getTargetActorName() {
        return this.targetActorName;
    }

    public void destroy() {
        super.destroy();
        World.cur().triggersGuard.getListTriggerAircraftAirSpawnRelativeAltitude().remove(World.cur().triggersGuard.getListTriggerAircraftAirSpawnRelativeAltitude().indexOf(this.getTargetActorName()));
        World.cur().triggersGuard.getListTriggerAircraftSpawn().remove(World.cur().triggersGuard.getListTriggerAircraftSpawn().indexOf(this.getTargetActorName()));
    }

    public void setTargetActorName(String targetActorName) {
        this.targetActorName = targetActorName;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getDeltaAltitude() {
        return this.deltaAltitude;
    }

    public void setDeltaAltitude(int deltaAltitude) {
        this.deltaAltitude = deltaAltitude;
    }

    private String targetActorName;
    private double altitude;
    private int    deltaAltitude;
}
