// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;
import com.maddox.sas1946.il2.util.TrueRandom;

class TriggerSpawn extends Trigger {

    public TriggerSpawn(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, String targetActorNames, int altitudeMin, int altitudeMax, int triggeredBy, boolean triggerOnExit, int noObjectsMin, int probability,
            String linkActorName, String displayMessage, float displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTargetActorNames(targetActorNames);
        this.setTriggerClass(TYPE_SPAWN);
        if (this.getTargetActorNames() == null || this.getTargetActorNames().size() < 1) this.destroy();
        for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
            String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
            if (targetActorName.indexOf("Static") >= 0 || targetActorName.indexOf("Rocket") >= 0) World.cur().triggersGuard.getListTriggerStaticSpawn().add(targetActorName);
            else if (targetActorName.indexOf("Chief") >= 0) World.cur().triggersGuard.getListTriggerChiefSpawn().add(targetActorName);
            else if (targetActorName.indexOf("Trigger") >= 0) World.cur().triggersGuard.getListTriggerTriggerActivate().add(targetActorName);
            else World.cur().triggersGuard.getListTriggerAircraftSpawn().add(targetActorName);
        }

//        this.setTargetActorName(targetActorName);
//        this.setTriggerClass(TYPE_SPAWN);
//        if (this.getTargetActorName() == "" || this.getTargetActorName() == null) this.destroy();
//        if (this.getTargetActorName().indexOf("Static") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0) World.cur().triggersGuard.getListTriggerStaticSpawn().add(this.getTargetActorName());
//        else if (this.getTargetActorName().indexOf("Chief") >= 0) World.cur().triggersGuard.getListTriggerChiefSpawn().add(this.getTargetActorName());
//        else World.cur().triggersGuard.getListTriggerAircraftSpawn().add(this.getTargetActorName());
    }

    protected boolean checkPeriodic() {
        boolean allTargetActorsSpawned = true;
        if (this.getTargetActorNames() != null && this.getTargetActorNames().size() > 0) {
            for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
                String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
                if (Actor.getByName(targetActorName) == null) {
                    allTargetActorsSpawned = false;
                    break;
                }
            }
        }
//        if (Actor.getByName(this.getTargetActorName()) == null) return super.checkPeriodic();
        if (!allTargetActorsSpawned) return super.checkPeriodic();
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
        if (this.isTriggered()) return;
        this.setTriggered(true);
        
        for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
            String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
            if (targetActorName != null && Actor.getByName(targetActorName) == null) {
                if (targetActorName.indexOf("Static") >= 0)
                    Mission.cur().loadNStationaryTrigger(targetActorName);
                else if (targetActorName.indexOf("Rocket") >= 0)
                    Mission.cur().loadRocketryTrigger(targetActorName);
                else if (targetActorName.indexOf("Chief") >= 0)
                    Mission.cur().loadChiefsTrigger(targetActorName);
                else if (targetActorName.indexOf("Trigger") > 0) {
                    Actor triggerTargetActor = World.cur().triggersGuard.getTrigger(targetActorName);
                    if (triggerTargetActor instanceof Trigger) {
                        ((Trigger) triggerTargetActor).setActivated(true);
                        // System.out.println("Trigger " + ((Trigger)triggerTargetActor).name() + " activated!");
                    }
                }
                else
                    Mission.cur().loadWingOnTrigger(targetActorName);
            }
            Actor actor = Actor.getByName(targetActorName);
            if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
                if (actor != null) EventLog.onTriggerActivate(actor, this);
                this.doSendMsg(false);
            } else {
                if (actor != null) EventLog.onTriggerActivateLink(actor, this);
                this.doSendMsg(true);
            }
        }
        
//        if (this.getTargetActorName() != null && Actor.getByName(this.getTargetActorName()) == null) {
//            if (this.getTargetActorName().indexOf("Static") >= 0) {
//                Mission.cur().loadNStationaryTrigger(this.getTargetActorName());
//            }
//            else if (this.getTargetActorName().indexOf("Rocket") >= 0) {
//                Mission.cur().loadRocketryTrigger(this.getTargetActorName());
//            }
//            else if (this.getTargetActorName().indexOf("Chief") >= 0) {
//                Mission.cur().loadChiefsTrigger(this.getTargetActorName());
//            }
//            else {
//                Mission.cur().loadWingOnTrigger(this.getTargetActorName());
//            }
//        }
//        Actor actor = Actor.getByName(this.getTargetActorName());
//        if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
//            if (actor != null) EventLog.onTriggerActivate(actor, this);
//            this.doSendMsg(false);
//        } else {
//            if (actor != null) EventLog.onTriggerActivateLink(actor, this);
//            this.doSendMsg(true);
//        }
        super.doExecute();
    }

    public void destroy() {
        super.destroy();
        for (int targetActorNameIndex = 0; targetActorNameIndex < this.getTargetActorNames().size(); targetActorNameIndex++) {
            String targetActorName = (String)this.getTargetActorNames().get(targetActorNameIndex);
            if (targetActorName.indexOf("Static") >= 0 || targetActorName.indexOf("Rocket") >= 0)
                World.cur().triggersGuard.getListTriggerStaticSpawn().remove(World.cur().triggersGuard.getListTriggerStaticSpawn().indexOf(targetActorName));
            else if (targetActorName.indexOf("Chief") >= 0)
                World.cur().triggersGuard.getListTriggerChiefSpawn().remove(World.cur().triggersGuard.getListTriggerChiefSpawn().indexOf(targetActorName));
            else if (targetActorName.indexOf("Trigger") >= 0)
                World.cur().triggersGuard.getListTriggerTriggerActivate().remove(World.cur().triggersGuard.getListTriggerTriggerActivate().indexOf(targetActorName));
            else World.cur().triggersGuard.getListTriggerAircraftSpawn().remove(World.cur().triggersGuard.getListTriggerAircraftSpawn().indexOf(targetActorName));
        }
//        if (this.getTargetActorName().indexOf("Static") >= 0 || this.getTargetActorName().indexOf("Rocket") >= 0)
//            World.cur().triggersGuard.getListTriggerStaticSpawn().remove(World.cur().triggersGuard.getListTriggerStaticSpawn().indexOf(this.getTargetActorName()));
//        else if (this.getTargetActorName().indexOf("Chief") >= 0) World.cur().triggersGuard.getListTriggerChiefSpawn().remove(World.cur().triggersGuard.getListTriggerChiefSpawn().indexOf(this.getTargetActorName()));
//        else World.cur().triggersGuard.getListTriggerAircraftSpawn().remove(World.cur().triggersGuard.getListTriggerAircraftSpawn().indexOf(this.getTargetActorName()));
    }

//    public String getTargetActorName() {
//        return this.targetActorName;
//    }
//
//    public void setTargetActorName(String targetActorName) {
//        this.targetActorName = targetActorName;
//    }
//
//    private String targetActorName;
    
    public ArrayList getTargetActorNames() {
        return this.targetActorNames;
    }

    private void setTargetActorNames(String targetActorNames) {
        this.targetActorNames = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(targetActorNames, "|");
        while (tokenizer.hasMoreTokens())
            this.targetActorNames.add(tokenizer.nextToken());
    }

    private ArrayList targetActorNames;// = new ArrayList();

}
