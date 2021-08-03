package com.maddox.il2.ai;

import java.util.Random;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;

class TriNewAircraftAir extends Trigger {

    public TriNewAircraftAir(String zname, int i, int j, int posx, int posy, int r, String s, int zmin, int zmax, int ziaHumans, boolean bSortie, int zAvionMin, int zProba, String zsLink, String sTextDisplay, int zTextDuree) {
        super(zname, i, j, posx, posy, r, zmin, zmax, ziaHumans, bSortie, zAvionMin, zProba, zsLink, sTextDisplay, zTextDuree);
        nameTarget = s;
        if (nameTarget == "" || nameTarget == null)
            destroy();
        if (nameTarget.indexOf("Static") >= 0 || nameTarget.indexOf("Rocket") >= 0)
            World.cur().triggersGuard.listTriggerStaticAppar.add(nameTarget);
        else if (nameTarget.indexOf("Chief") >= 0)
            World.cur().triggersGuard.listTriggerChiefAppar.add(nameTarget);
        else
            World.cur().triggersGuard.listTriggerAvionAppar.add(nameTarget);
    }

    protected boolean checkPeriodic() {
        if (Actor.getByName(nameTarget) == null) {
            return super.checkPeriodic();
        } else {
            destroy();
            return false;
        }
    }

    protected void execute() {
        Random r = new Random();
        float f = r.nextFloat() * 100F + 1.0F;
        if (f <= (float) super.proba) {
            super.declanche = true;
            if (nameTarget != null && Actor.getByName(nameTarget) == null)
                if (nameTarget.indexOf("Static") >= 0)
                    Mission.cur().loadNStationaryTrigger(nameTarget);
                else if (nameTarget.indexOf("Rocket") >= 0)
                    Mission.cur().loadRocketryTrigger(nameTarget);
                else if (nameTarget.indexOf("Chief") >= 0)
                    Mission.cur().loadChiefsTrigger(nameTarget);
                else
                    Mission.cur().loadWingOnTrigger(nameTarget);
            if (super.sLink == "" || super.sLink == null) {
                EventLog.onTriggerActivate(Actor.getByName(nameTarget), this);
                doSendMsg(false);
            } else {
                EventLog.onTriggerActivateLink(Actor.getByName(nameTarget), this);
                doSendMsg(true);
            }
        }
        super.execute();
    }

    public void destroy() {
        super.destroy();
        if (nameTarget.indexOf("Static") >= 0 || nameTarget.indexOf("Rocket") >= 0)
            World.cur().triggersGuard.listTriggerStaticAppar.remove(World.cur().triggersGuard.listTriggerStaticAppar.indexOf(nameTarget));
        else if (nameTarget.indexOf("Chief") >= 0)
            World.cur().triggersGuard.listTriggerChiefAppar.remove(World.cur().triggersGuard.listTriggerChiefAppar.indexOf(nameTarget));
        else
            World.cur().triggersGuard.listTriggerAvionAppar.remove(World.cur().triggersGuard.listTriggerAvionAppar.indexOf(nameTarget));
    }

    String nameTarget;
}
