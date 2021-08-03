// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.HighPrecisionTimer;

public class TriggersGuard {

    public void checkTask() {
        if (!this.isActive) return;
        if (!Mission.isPlaying()) return;
//        if (Time.tickCounter() % 128 != 0) return; // TODO: Changed by SAS~Storebror: Moved here from War class
        if (Mission.isNet() && !Mission.isServer()) return; // TODO: Changed by SAS~Storebror: Only Server deals with Triggers online!
//        this.ticker.setTime(Time.current() + 1000L);
//        this.ticker.post();
        
        this.hpt.initTimer();
        
        long curTime = Time.current();
        int triggerSize = this.triggers.size();
        int triggersProcessed = 0;
        stopProcessing:
        while (triggersProcessed < triggerSize && this.hpt.getSecondsElapsed() < 0.001D) {
            for (int triggerIndex = this.nextTrigger; triggerIndex < triggerSize; triggerIndex++) {
                if (this.hpt.getSecondsElapsed() >= 0.001D) {
                    this.nextTrigger = triggerIndex;
                    break stopProcessing;
                }
                triggersProcessed++;
                Trigger trigger = (Trigger) this.triggers.get(triggerIndex);
                if (!trigger.isActivated()) continue;
                if (Actor.isValid(trigger) && (trigger.getTimeout() <= 0L || curTime >= trigger.getTimeout()) && trigger.checkPeriodic()) trigger.execute();
                if (curTime >= trigger.getTimeout()) trigger.setTimeout(-trigger.getTimeout());
            }
            this.nextTrigger = 0;
        }
        
//        this.secondsElapsed += this.hpt.getSecondsElapsed();
//        this.loops++;
//        
//        if (Time.tickCounter() % 128 == 0) {
//            System.out.println("Total time used: " + this.secondsElapsed + " secs, loops: " + this.loops + ", secs per loop: " + this.secondsElapsed / this.loops);
//        }

    }

    protected void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }

    public void activate() {
//        if (this.isActive) return;
        this.isActive = true;
//        if (this.ticker.busy()) this.ticker.remove();
//        if (this.triggers.size() == 0) return;
//        else {
//            this.ticker.setTime(Time.current() + 1000L);
//            this.ticker.post();
//            return;
//        }
    }

    public void resetGame() {
        this.isActive = false;
        int i = this.triggers.size();
        for (int j = 0; j < i; j++) {
            Trigger trigger = (Trigger) this.triggers.get(j);
            if (Actor.isValid(trigger)) trigger.destroy();
        }

        this.getListTriggerAircraftSpawn().clear();
        this.getListTriggerStaticSpawn().clear();
        this.getListTriggerChiefSpawn().clear();
        this.getListTriggerAircraftAirSpawnRelativeAltitude().clear();
        this.getListTriggerChiefActivate().clear();
        this.getListTriggerAircraftActivate().clear();
        this.getListTriggerTriggerActivate().clear();
        this.triggers.clear();
    }

    protected TriggersGuard() {
        this.setListTriggerAircraftSpawn(new ArrayList());
        this.setListTriggerStaticSpawn(new ArrayList());
        this.setListTriggerChiefSpawn(new ArrayList());
        this.setListTriggerAircraftAirSpawnRelativeAltitude(new ArrayList());
        this.setListTriggerAircraftActivate(new ArrayList());
        this.setListTriggerChiefActivate(new ArrayList());
        this.setListTriggerTriggerActivate(new ArrayList());
        this.isActive = false;
        this.triggers = new ArrayList();
//        this.ticker = new MsgTimeOut(null);
//        this.ticker.setNotCleanAfterSend();
//        this.ticker.setListener(this);
        this.hpt = new HighPrecisionTimer();
//        this.secondsElapsed = 0D;
//        this.loops = 0L;
        this.nextTrigger = 0;
    }

    public List getTriggers() {
        if (this.triggers == null) this.triggers = new ArrayList();
        return this.triggers;
    }

    public Trigger getTrigger(String triggerName) {
        for (Iterator e = this.triggers.iterator(); e.hasNext();) {
            Trigger trigger = (Trigger) e.next();
            if (trigger.getTriggerName().equalsIgnoreCase(triggerName)) return trigger;
//            System.out.println("getTrigger(" + triggerName + ") " + trigger.getTriggerName() + " doesn't match!");
        }
        return null;
    }

    public Trigger getTriggerTarget(String target) {
        for (Iterator e = this.triggers.iterator(); e.hasNext();) {
            Trigger trigger = (Trigger) e.next();
            if (trigger instanceof TriggerSpawnAircraftRelativeAltitude && trigger.isTriggered() && trigger.getTargetActorName().equals(target)) return trigger;
        }

        return null;
    }

    public ArrayList getListTriggerAircraftSpawn() {
        return this.listTriggerAircraftSpawn;
    }

    public void setListTriggerAircraftSpawn(ArrayList listTriggerAircraftSpawn) {
        this.listTriggerAircraftSpawn = listTriggerAircraftSpawn;
    }

    public ArrayList getListTriggerStaticSpawn() {
        return this.listTriggerStaticSpawn;
    }

    public void setListTriggerStaticSpawn(ArrayList listTriggerStaticSpawn) {
        this.listTriggerStaticSpawn = listTriggerStaticSpawn;
    }

    public ArrayList getListTriggerChiefSpawn() {
        return this.listTriggerChiefSpawn;
    }

    public void setListTriggerChiefSpawn(ArrayList listTriggerChiefSpawn) {
        this.listTriggerChiefSpawn = listTriggerChiefSpawn;
    }

    public ArrayList getListTriggerAircraftAirSpawnRelativeAltitude() {
        return this.listTriggerAircraftAirSpawnRelativeAltitude;
    }

    public void setListTriggerAircraftAirSpawnRelativeAltitude(ArrayList listTriggerAircraftAirSpawnRelativeAltitude) {
        this.listTriggerAircraftAirSpawnRelativeAltitude = listTriggerAircraftAirSpawnRelativeAltitude;
    }

    public ArrayList getListTriggerAircraftActivate() {
        return this.listTriggerAircraftActivate;
    }

    public void setListTriggerAircraftActivate(ArrayList listTriggerAircraftActivate) {
        this.listTriggerAircraftActivate = listTriggerAircraftActivate;
    }

    public ArrayList getListTriggerChiefActivate() {
        return this.listTriggerChiefActivate;
    }

    public void setListTriggerChiefActivate(ArrayList listTriggerChiefActivate) {
        this.listTriggerChiefActivate = listTriggerChiefActivate;
    }

    public ArrayList getListTriggerTriggerActivate() {
        return listTriggerTriggerActivate;
    }

    public void setListTriggerTriggerActivate(ArrayList listTriggerTriggerActivate) {
        this.listTriggerTriggerActivate = listTriggerTriggerActivate;
    }

    private boolean    isActive;
//    private MsgTimeOut ticker;
    private ArrayList  triggers;
    private ArrayList  listTriggerAircraftSpawn;
    private ArrayList  listTriggerStaticSpawn;
    private ArrayList  listTriggerChiefSpawn;
    private ArrayList  listTriggerAircraftAirSpawnRelativeAltitude;
    private ArrayList  listTriggerAircraftActivate;
    private ArrayList  listTriggerChiefActivate;
    private ArrayList  listTriggerTriggerActivate;
    
    private HighPrecisionTimer hpt;
//    private double secondsElapsed;
//    private long loops;
    private int nextTrigger;
}
