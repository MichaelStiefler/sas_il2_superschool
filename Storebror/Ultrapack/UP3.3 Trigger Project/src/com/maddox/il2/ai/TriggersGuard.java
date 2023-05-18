// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;
import com.maddox.rts.MsgTimeOut;
import com.maddox.rts.Time;

public class TriggersGuard {

    public void checkTask() {
        if (!this.isActive) return;
        if (!Mission.isPlaying()) return;
        this.ticker.setTime(Time.current() + 1000L);
        this.ticker.post();
        long l = Time.current();
        int i = this.triggers.size();
        for (int j = 0; j < i; j++) {
            Trigger trigger = (Trigger) this.triggers.get(j);
            if (Actor.isValid(trigger) && (trigger.getTimeout() <= 0L || l >= trigger.getTimeout()) && trigger.checkPeriodic()) trigger.execute();
            if (l >= trigger.getTimeout()) trigger.setTimeout(-trigger.getTimeout());
        }

    }

    protected void addTrigger(Trigger trigger) {
        this.triggers.add(trigger);
    }

    public void activate() {
        if (this.isActive) return;
        this.isActive = true;
        if (this.ticker.busy()) this.ticker.remove();
        if (this.triggers.size() == 0) return;
        else {
            this.ticker.setTime(Time.current() + 1000L);
            this.ticker.post();
            return;
        }
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
        this.triggers.clear();
    }

    protected TriggersGuard() {
        this.setListTriggerAircraftSpawn(new ArrayList());
        this.setListTriggerStaticSpawn(new ArrayList());
        this.setListTriggerChiefSpawn(new ArrayList());
        this.setListTriggerAircraftAirSpawnRelativeAltitude(new ArrayList());
        this.setListTriggerAircraftActivate(new ArrayList());
        this.setListTriggerChiefActivate(new ArrayList());
        this.isActive = false;
        this.triggers = new ArrayList();
        this.ticker = new MsgTimeOut(null);
        this.ticker.setNotCleanAfterSend();
        this.ticker.setListener(this);
    }

    public List getTriggers() {
        if (this.triggers == null) this.triggers = new ArrayList();
        return this.triggers;
    }

    public Trigger getTriggerTarget(String target) {
        for (Iterator e = this.triggers.iterator(); e.hasNext();) {
            Trigger trigger = (Trigger) e.next();
            if (trigger instanceof TriNewAircraftAirLevel && trigger.isTriggered() && trigger.getTargetActorName().equals(target)) return trigger;
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

    private boolean    isActive;
    private MsgTimeOut ticker;
    private ArrayList  triggers;
    private ArrayList  listTriggerAircraftSpawn;
    private ArrayList  listTriggerStaticSpawn;
    private ArrayList  listTriggerChiefSpawn;
    private ArrayList  listTriggerAircraftAirSpawnRelativeAltitude;
    private ArrayList  listTriggerAircraftActivate;
    private ArrayList  listTriggerChiefActivate;
}
