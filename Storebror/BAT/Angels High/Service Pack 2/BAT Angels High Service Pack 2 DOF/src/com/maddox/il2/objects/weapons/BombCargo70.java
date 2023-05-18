package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
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

public class BombCargo70 extends Bomb {

    public BombCargo70() {
        this.chute = null;
        this.bOnChute = false;
    }

    protected boolean haveSound() {
        return false;
    }

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(0.5F, 1.75F);
    }

    public void interpolateTick() {
        super.interpolateTick();
        this.getSpeed(v3d);
        or.setAT0(v3d);
        this.pos.setAbs(or);
        if (this.bOnChute) {
            v3d.scale(0.99D);
            if (v3d.z < -5D) {
                v3d.z += 1.1F * Time.tickConstLenFs();
            }
            this.setSpeed(v3d);
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
                loc.getOrient().set(loc.getOrient().getAzimut(), -90F, 0.0F);
                ActorSimpleMesh actorsimplemesh = new ActorSimpleMesh("3DO/Arms/Cargo-TypeA/mono.sim", loc);
                actorsimplemesh.collide(false);
                actorsimplemesh.postDestroy(0x249f0L);
            }
        } else if (this.chute != null) {
            this.chute.destroy();
        }
        this.destroy();
    }

    private Chute           chute;
    private boolean         bOnChute;
    private static Orient   or  = new Orient();
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;

    static {
        Class class1 = BombCargo70.class;
        Property.set(class1, "mesh", "3DO/Arms/Cargo-TypeA/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 6F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 1.0F);
        Property.set(class1, "massa", 70F);
        Property.set(class1, "sound", (String) null);
    }
}
