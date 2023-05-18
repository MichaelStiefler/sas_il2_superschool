package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombTorp45_36AV_A extends BombParaTorp {

    public BombTorp45_36AV_A() {
        this.chute = null;
        this.bOnChute = false;
    }

    public void start() {
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(0.5F, 1.75F);
        this.openHeight = 10000F;
    }

    public void destroy() {
        if (this.chute != null) this.chute.destroy();
        super.destroy();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (actor instanceof ActorLand && this.chute != null) this.bOnChute = false;
        this.ttcurTM = 100000F;
        if (this.chute != null) this.chute.landing();
        super.msgCollision(actor, s, s1);
    }

    public void interpolateTick() {
        this.curTm += Time.tickLenFs();
        super.interpolateTick();
        if (this.bOnChute) {
            this.getSpeed(v3d);
            v3d.scale(0.99D);
            if (v3d.z < -10D) v3d.z += 1.1F * Time.tickConstLenFs();
            this.setSpeed(v3d);
            this.pos.getAbs(P, Or);
        } else if (this.curTm > this.ttcurTM && P.z < this.openHeight) {
            this.bOnChute = true;
            // TODO: Bugfix by SAS~Storebror: Avoid Scaling Meshes with Shadows!
//          chute = new Chute(this);
            this.chute = new BombChute(this, 2.5F);
            this.chute.collide(false);
            // TODO: Bugfix by SAS~Storebror: Avoid Scaling Meshes with Shadows!
//          chute.mesh().setScale(2.5F);
            this.chute.pos.setRel(new Point3d(1.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        }
    }

    private Chute           chute;
    private boolean         bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;
    private float           openHeight;

    static {
        final Class class1 = BombTorp45_36AV_A.class;
        Property.set(class1, "mesh", "3do/arms/45-12/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 220F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 960F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 20F);
        Property.set(class1, "traveltime", 200F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 0.0F);
        Property.set(class1, "impactAngleMax", 90.5F);
        Property.set(class1, "impactSpeed", 115F);
        Property.set(class1, "armingTime", 3.5F);
    }
}
