// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;
import com.maddox.sas1946.il2.util.TrueRandom;

class TriggerSpawn extends Trigger {

    public TriggerSpawn(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, String targetActorName, int altitudeMin, int altitudeMax, int triggeredBy, boolean triggerOnExit, int noObjectsMin, int probability,
            String linkActorName, String displayMessage, int displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTargetActorName(targetActorName);
        this.setTriggerClass(TYPE_SPAWN);
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
        if (TrueRandom.nextFloat(100F) < this.getProbability()) this.doExecute();
        super.execute();
    }
    
    protected void doExecute() {
//        System.out.println("TriggerSpawn doExecute 1");
        if (this.isTriggered()) return;
        this.setTriggered(true);
        if (this.getTargetActorName() != null && Actor.getByName(this.getTargetActorName()) == null) {
//            System.out.println("TriggerSpawn doExecute 2");
            if (this.getTargetActorName().indexOf("Static") >= 0) {
//                System.out.println("TriggerSpawn doExecute 3");
                Mission.cur().loadNStationaryTrigger(this.getTargetActorName());
            }
            else if (this.getTargetActorName().indexOf("Rocket") >= 0) {
//                System.out.println("TriggerSpawn doExecute 4");
                Mission.cur().loadRocketryTrigger(this.getTargetActorName());
            }
            else if (this.getTargetActorName().indexOf("Chief") >= 0) {
//                System.out.println("TriggerSpawn doExecute 5");
                Mission.cur().loadChiefsTrigger(this.getTargetActorName());
            }
            else {
//                System.out.println("TriggerSpawn doExecute 6");
                Mission.cur().loadWingOnTrigger(this.getTargetActorName());
            }
        }
//        System.out.println("TriggerSpawn doExecute 7");
        Actor actor = Actor.getByName(this.getTargetActorName());
        if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
            if (actor != null) EventLog.onTriggerActivate(actor, this);
            this.doSendMsg(false);
        } else {
            if (actor != null) EventLog.onTriggerActivateLink(actor, this);
            this.doSendMsg(true);
        }
        super.doExecute();
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
