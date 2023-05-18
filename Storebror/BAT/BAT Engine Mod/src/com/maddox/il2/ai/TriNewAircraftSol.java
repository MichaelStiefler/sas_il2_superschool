package com.maddox.il2.ai;

import java.util.Random;

import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Main;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.trains.Train;
import com.maddox.il2.objects.vehicles.artillery.RocketryGeneric;
import com.maddox.rts.NetEnv;

public class TriNewAircraftSol extends Trigger {

    public TriNewAircraftSol(String zname, int i, int j, int posx, int posy, int r, String s, int zmin, int zmax, int ziaHumans, boolean bSortie, int zAvionMin, int zProba, String zsLink, String sTextDisplay, int zTextDuree) {
        super(zname, i, j, posx, posy, r, zmin, zmax, ziaHumans, bSortie, zAvionMin, zProba, zsLink, sTextDisplay, zTextDuree);
        this.nameTarget = s;
        if ((this.nameTarget == "") || (this.nameTarget == null)) {
            this.destroy();
        }
        if ((this.nameTarget.indexOf("Chief") >= 0) || (this.nameTarget.indexOf("Rocket") >= 0)) {
            World.cur().triggersGuard.listTriggerChiefSol.add(this.nameTarget);
        } else {
            World.cur().triggersGuard.listTriggerAvionSol.add(this.nameTarget);
        }
    }

    protected void execute() {
        Random r = new Random();
        float f = (r.nextFloat() * 100F) + 1.0F;
        if (f <= super.proba) {
            super.declanche = true;
            if (this.nameTarget != null) {
                if ((this.nameTarget.indexOf("Chief") >= 0) || (this.nameTarget.indexOf("Rocket") >= 0)) {
                    if (Main.cur().netServerParams.isMaster()) {
                        ((NetUser) NetEnv.host()).replicateTriggerStartGround(this.nameTarget);
                    }
                    TriNewAircraftSol.startGround(this.nameTarget);
                } else {
                    Wing wing = (Wing) Actor.getByName(this.nameTarget);
                    for (int i = 0; i < 4; i++) {
                        if (wing.airc[i] != null) {
                            Aircraft aircraft = wing.airc[i];
                            if (((SndAircraft) (aircraft)).FM instanceof Maneuver) {
                                ((Maneuver) ((SndAircraft) (aircraft)).FM).triggerTakeOff = true;
                            }
                        }
                    }

                }
                if ((super.sLink == "") || (super.sLink == null)) {
                    EventLog.onTriggerActivate(Actor.getByName(this.nameTarget), this);
                    this.doSendMsg(false);
                } else {
                    EventLog.onTriggerActivateLink(Actor.getByName(this.nameTarget), this);
                    this.doSendMsg(true);
                }
            }
        }
        super.execute();
    }

    public void destroy() {
        super.destroy();
        if ((this.nameTarget.indexOf("Chief") >= 0) || (this.nameTarget.indexOf("Rocket") >= 0)) {
            World.cur().triggersGuard.listTriggerChiefSol.remove(World.cur().triggersGuard.listTriggerChiefSol.indexOf(this.nameTarget));
        } else {
            World.cur().triggersGuard.listTriggerAvionSol.remove(World.cur().triggersGuard.listTriggerAvionSol.indexOf(this.nameTarget));
        }
    }

    public static void startGround(String nameUnit) {
        Actor actor = Actor.getByName(nameUnit);
        if (actor instanceof RocketryGeneric) {
            ((RocketryGeneric) actor).startMove();
        } else if (actor instanceof Train) {
            ((Train) actor).startMove();
        } else if (actor instanceof BigshipGeneric) {
            ((BigshipGeneric) actor).startMove();
        } else if (actor instanceof ShipGeneric) {
            ((ShipGeneric) actor).startMove();
        } else if (actor instanceof ChiefGround) {
            ((ChiefGround) actor).startMove();
        }
    }

    String nameTarget;
}
