package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Property;

public class BombCargoC extends Bomb {

    public BombCargoC() {
        this.DropName = null;
    }

    public void start() {
        super.start();
        Actor actor = this.getOwner();
        if (Actor.isValid(actor)) {
            this.DropName = EventLog.name(actor);
            EventLog.DropCargo(actor);
        }
    }

    protected boolean haveSound() {
        return false;
    }

    public void interpolateTick() {
        super.interpolateTick();
        this.getSpeed(BombCargoC.v3d);
        this.currSpeed = BombCargoC.v3d.z;
    }

    public void msgCollision(Actor actor, String s, String s1) {
        Loc loc = new Loc();
        this.pos.getAbs(loc);
        loc.getPoint().z = Engine.land().HQ(loc.getPoint().x, loc.getPoint().y);
        if (this.currSpeed < -25D) {
            if (this.getOwner() instanceof Aircraft) {
                Aircraft aircraft = (Aircraft) this.getOwner();
                if (aircraft.FM.isPlayers()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "CargoContainerBroke");
                }
            }
            EventLog.FailCargo(this.DropName, (float) loc.getPoint().x, (float) loc.getPoint().y, (float) loc.getPoint().z);
            super.msgCollision(actor, s, s1);
        } else if (actor instanceof ActorLand) {
            if (!Engine.land().isWater(loc.getPoint().x, loc.getPoint().y)) {
                loc.getOrient().set(loc.getOrient().getAzimut(), 0.0F, 0.0F);
                ActorSimpleMesh actorsimplemesh = new ActorSimpleMesh("3DO/Arms/Cargo-TypeC/mono.sim", loc);
                actorsimplemesh.collide(false);
                actorsimplemesh.postDestroy(0x249f0L);
                EventLog.LandCargo(this.DropName, (float) loc.getPoint().x, (float) loc.getPoint().y, (float) loc.getPoint().z);
            } else {
                EventLog.WaterCargo(this.DropName, (float) loc.getPoint().x, (float) loc.getPoint().y, (float) loc.getPoint().z);
            }
        }
        this.DropName = null;
        this.destroy();
    }

    private static Vector3d v3d = new Vector3d();
    private double          currSpeed;
    String                  DropName;

    static {
        Class class1 = BombCargoC.class;
        Property.set(class1, "mesh", "3DO/Arms/Cargo-TypeC/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "sound", (String) null);
    }
}
