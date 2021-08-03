package com.maddox.il2.ai;

import java.util.Random;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CollideEnv;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.Mission;

public class TriNewAircraftAirLevel extends Trigger {

    public TriNewAircraftAirLevel(String zname, int i, int j, int posx, int posy, int r, String s, int zmin, int zmax, int ziaHumans, boolean bSortie, int zAvionMin, int zProba, int zAltiDiff, String zsLink, String sTextDisplay, int zTextDuree) {
        super(zname, i, j, posx, posy, r, zmin, zmax, ziaHumans, bSortie, zAvionMin, zProba, zsLink, sTextDisplay, zTextDuree);
        this.nameTarget = s;
        this.altiDiff = zAltiDiff;
        if ((this.nameTarget == "") || (this.nameTarget == null)) {
            this.destroy();
        }
        World.cur().triggersGuard.listTriggerAvionAppar.add(this.nameTarget);
        World.cur().triggersGuard.listTriggerAvionAirLevel.add(this.nameTarget);
    }

    protected boolean checkPeriodic() {
        if (Actor.getByName(this.nameTarget) == null) {
            this.checkMove();
            if ((super.sLink != "") && (super.sLink != null) && (Actor.getByName(super.sLink) == null)) {
                return false;
            }
            CollideEnv.ResultTrigger result = Engine.collideEnv().getEnemiesInCyl(super.posTigger, super.rayon, super.altiMin, super.altiMax, super.army, super.iaHumans, super.avionMin);
            if (super.bTSortie) {
                if (super.bIsEnter) {
                    super.altiMsg = (int) result.altiSea;
                    return !result.result;
                }
                if (result.result) {
                    super.bIsEnter = true;
                    this.alti = result.altiSea + this.altiDiff;
                }
            } else if (result.result) {
                this.alti = result.altiSea + this.altiDiff;
                super.altiMsg = (int) result.altiSea;
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
        float f = (r.nextFloat() * 100F) + 1.0F;
        if (f <= super.proba) {
            super.declanche = true;
            if ((this.nameTarget != null) && (Actor.getByName(this.nameTarget) == null)) {
                Mission.cur().loadWingOnTrigger(this.nameTarget);
            }
            if ((super.sLink == "") || (super.sLink == null)) {
                EventLog.onTriggerActivate(Actor.getByName(this.nameTarget), this);
                this.doSendMsg(false);
            } else {
                EventLog.onTriggerActivateLink(Actor.getByName(this.nameTarget), this);
                this.doSendMsg(true);
            }
        }
        super.execute();
    }

    public double getAlti() {
        return this.alti;
    }

    public String getTarget() {
        return this.nameTarget;
    }

    public void destroy() {
        super.destroy();
        World.cur().triggersGuard.listTriggerAvionAirLevel.remove(World.cur().triggersGuard.listTriggerAvionAirLevel.indexOf(this.nameTarget));
        World.cur().triggersGuard.listTriggerAvionAppar.remove(World.cur().triggersGuard.listTriggerAvionAppar.indexOf(this.nameTarget));
    }

    String nameTarget;
    double alti;
    int    altiDiff;
}
