// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.Random;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;

class TriNewAircraftAir extends Trigger {

    public TriNewAircraftAir(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, String targetActorName, int altitudeMin, int altitudeMax, int triggeredBy, boolean hasTriggerActor, int noObjectsMin, int probability,
            String linkActorName, String displayMessage, int displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, hasTriggerActor, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTargetActorName(targetActorName);
        if (this.getTargetActorName() == "" || this.getTargetActorName() == null) this.destroy();
        if (this.getTargetActorName().indexOf("Static") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0) World.cur().triggersGuard.getListTriggerStaticSpawn().add(this.getTargetActorName());
        else if (this.getTargetActorName().indexOf("Chief") >= 0) World.cur().triggersGuard.getListTriggerChiefSpawn().add(this.getTargetActorName());
        else World.cur().triggersGuard.getListTriggerAircraftSpawn().add(this.getTargetActorName());
    }

    protected boolean checkPeriodic() {
        if (Actor.getByName(this.getTargetActorName()) == null) return super.checkPeriodic();
        else {
            this.destroy();
            return false;
        }
    }

    protected void execute() {
        Random r = new Random();
        float f = r.nextFloat() * 100F + 1.0F;
        if (f <= this.getProbability()) {
            this.setTriggered(true);
            if (this.getTargetActorName() != null && Actor.getByName(this.getTargetActorName()) == null) if (this.getTargetActorName().indexOf("Static") >= 0) Mission.cur().loadNStationaryTrigger(this.getTargetActorName());
            else if (this.getTargetActorName().indexOf("Rocket") >= 0) Mission.cur().loadRocketryTrigger(this.getTargetActorName());
            else if (this.getTargetActorName().indexOf("Chief") >= 0) Mission.cur().loadChiefsTrigger(this.getTargetActorName());
            else Mission.cur().loadWingOnTrigger(this.getTargetActorName());
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

    public void destroy() {
        super.destroy();
        if (this.getTargetActorName().indexOf("Static") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0)
            World.cur().triggersGuard.getListTriggerStaticSpawn().remove(World.cur().triggersGuard.getListTriggerStaticSpawn().indexOf(this.getTargetActorName()));
        else if (this.getTargetActorName().indexOf("Chief") >= 0) World.cur().triggersGuard.getListTriggerChiefSpawn().remove(World.cur().triggersGuard.getListTriggerChiefSpawn().indexOf(this.getTargetActorName()));
        else World.cur().triggersGuard.getListTriggerAircraftSpawn().remove(World.cur().triggersGuard.getListTriggerAircraftSpawn().indexOf(this.getTargetActorName()));
    }

    public String getTargetActorName() {
        return this.targetActorName;
    }

    public void setTargetActorName(String targetActorName) {
        this.targetActorName = targetActorName;
    }

    private String targetActorName;
}
