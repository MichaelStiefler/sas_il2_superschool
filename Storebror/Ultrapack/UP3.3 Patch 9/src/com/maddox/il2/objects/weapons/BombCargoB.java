package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombCargoB extends Bomb {

    public BombCargoB() {
        this.chute = null;
        this.bOnChute = false;
        this.DropName = null;
    }

    protected boolean haveSound() {
        return false;
    }

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(0.5F, 1.75F);
        Actor actor = this.getOwner();
        if (Actor.isValid(actor)) {
            this.DropName = EventLog.name(actor);
            EventLog.DropCargo(actor);
        }
    }

    public void interpolateTick() {
        super.interpolateTick();
        this.getSpeed(BombCargoB.v3d);
        BombCargoB.or.setAT0(BombCargoB.v3d);
        this.pos.setAbs(BombCargoB.or);
        if (this.bOnChute) {
            BombCargoB.v3d.scale(0.99D);
            if (BombCargoB.v3d.z < -5D) {
                BombCargoB.v3d.z += 1.1F * Time.tickConstLenFs();
            }
            this.setSpeed(BombCargoB.v3d);
        } else if (this.curTm > this.ttcurTM) {
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.chute.mesh().setScale(1.5F);
            this.chute.pos.setRel(new Point3d(2D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand) {
            if (this.chute != null) {
                this.chute.landing();
            }
            Loc loc = new Loc();
            this.pos.getAbs(loc);
            loc.getPoint().z = Engine.land().HQ(loc.getPoint().x, loc.getPoint().y);
            if (!Engine.land().isWater(loc.getPoint().x, loc.getPoint().y)) {
                loc.getOrient().set(loc.getOrient().getAzimut(), 0.0F, 0.0F);
                ActorSimpleMesh actorsimplemesh = new ActorSimpleMesh("3DO/Arms/Cargo-TypeB/mono.sim", loc);
                actorsimplemesh.collide(false);
                actorsimplemesh.postDestroy(0x249f0L);
                EventLog.LandCargo(this.DropName, (float) loc.getPoint().x, (float) loc.getPoint().y, (float) loc.getPoint().z);
            } else {
                EventLog.WaterCargo(this.DropName, (float) loc.getPoint().x, (float) loc.getPoint().y, (float) loc.getPoint().z);
            }
        } else if (this.chute != null) {
            this.chute.destroy();
        }
        this.DropName = null;
        this.destroy();
    }

    private Chute           chute;
    private boolean         bOnChute;
    private static Orient   or  = new Orient();
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;
    String                  DropName;

    static {
        Class class1 = BombCargoB.class;
        Property.set(class1, "mesh", "3DO/Arms/Cargo-TypeB/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "sound", (String) null);
    }
}
