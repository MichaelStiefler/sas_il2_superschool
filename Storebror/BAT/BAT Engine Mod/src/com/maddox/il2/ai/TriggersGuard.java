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
        if (!bActive)
            return;
        if (!Mission.isPlaying())
            return;
        ticker.setTime(Time.current() + 1000L);
        ticker.post();
        long l = Time.current();
        int i = triggers.size();
        for (int j = 0; j < i; j++) {
            Trigger trigger = (Trigger) triggers.get(j);
            if (Actor.isValid(trigger) && (trigger.timeout <= 0L || l >= trigger.timeout) && trigger.checkPeriodic())
                trigger.execute();
            if (l >= trigger.timeout)
                trigger.timeout = -trigger.timeout;
        }

    }

    protected void addTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    public void activate() {
        if (bActive)
            return;
        bActive = true;
        if (ticker.busy())
            ticker.remove();
        if (triggers.size() == 0) {
            return;
        } else {
            ticker.setTime(Time.current() + 1000L);
            ticker.post();
            return;
        }
    }

    public void resetGame() {
        bActive = false;
        int i = triggers.size();
        for (int j = 0; j < i; j++) {
            Trigger trigger = (Trigger) triggers.get(j);
            if (Actor.isValid(trigger))
                trigger.destroy();
        }

        listTriggerAvionAppar.clear();
        listTriggerStaticAppar.clear();
        listTriggerChiefAppar.clear();
        listTriggerAvionAirLevel.clear();
        listTriggerChiefSol.clear();
        listTriggerAvionSol.clear();
        triggers.clear();
    }

    protected TriggersGuard() {
        listTriggerAvionAppar = new ArrayList();
        listTriggerStaticAppar = new ArrayList();
        listTriggerChiefAppar = new ArrayList();
        listTriggerAvionAirLevel = new ArrayList();
        listTriggerAvionSol = new ArrayList();
        listTriggerChiefSol = new ArrayList();
        bActive = false;
        triggers = new ArrayList();
        ticker = new MsgTimeOut(null);
        ticker.setNotCleanAfterSend();
        ticker.setListener(this);
    }

    public List getTriggers() {
        if (triggers == null)
            triggers = new ArrayList();
        return triggers;
    }

    public Trigger getTriggerCible(String cible) {
        for (Iterator e = triggers.iterator(); e.hasNext();) {
            Trigger trigger = (Trigger) e.next();
            if ((trigger instanceof TriNewAircraftAirLevel) && trigger.declanche && trigger.getTarget().equals(cible))
                return trigger;
        }

        return null;
    }

    private boolean    bActive;
    private MsgTimeOut ticker;
    private ArrayList  triggers;
    public ArrayList   listTriggerAvionAppar;
    public ArrayList   listTriggerStaticAppar;
    public ArrayList   listTriggerChiefAppar;
    public ArrayList   listTriggerAvionAirLevel;
    public ArrayList   listTriggerAvionSol;
    public ArrayList   listTriggerChiefSol;
}
