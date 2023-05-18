// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.Random;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.Mission;

public class TriNewAircraftAirLevel extends Trigger {

    public TriNewAircraftAirLevel(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, String targetActorName, int altitudeMin, int altitudeMax, int triggeredBy, boolean hasTriggerActor, int noObjectsMin, int probability,
            int deltaAltitude, String linkActorName, String displayMessage, int displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, hasTriggerActor, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
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
            com.maddox.il2.engine.CollideEnv.ResultTrigger result = Engine.collideEnv().getEnemiesInCyl(this.getPosTigger(), this.getRadius(), this.getAltitudeMin(), this.getAltitudeMax(), this.getTriggeredByArmy(), this.getTriggeredBy(),
                    this.getNoObjectsMin());
            if (this.isHasTriggerActor()) {
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
        Random r = new Random();
        float f = r.nextFloat() * 100F + 1.0F;
        if (f <= this.getProbability()) {
            this.setTriggered(true);
            if (this.getTargetActorName() != null && Actor.getByName(this.getTargetActorName()) == null) Mission.cur().loadWingOnTrigger(this.getTargetActorName());
            if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
                EventLog.onTriggerActivate(Actor.getByName(this.getTargetActorName()), this);
                this.doSendMsg(false);
            } else {
                EventLog.onTriggerActivateLink(Actor.getByName(this.getTargetActorName()), this);
                this.doSendMsg(true);
            }
        }
        super.execute();
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
