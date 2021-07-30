package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;

public class RocketSchlong500 extends Rocket {

    public RocketSchlong500() {
        this.tOrient = new Orient();
        this.flags &= 0xffffffdf;
    }

    public boolean interpolateStep() {
        if (this.tEStart > 0L) {
            if (Time.current() < this.tEStart) {
                Ballistics.update(this, this.M, 0.07068583F, 0.0F, true);
                this.pos.setAbs(this.tOrient);
                return false;
            }
            if (Time.current() < this.tEStart2) {
                this.setThrust(20000F);
                if (Config.isUSE_RENDER()) {
                    this.newSound(this.soundName, true);
                    Eff3DActor.setIntesity(this.smoke, 1.0F);
                    Eff3DActor.setIntesity(this.sprite, 1.0F);
                    this.flame.drawing(true);
                    this.light.light.setEmit(2.0F, 100F);
                }
                return super.interpolateStep();
            }
            if (Time.current() < this.tDestroy) {
                this.fishTail();
                return false;
            }
            this.doFireContaineds();
            return false;
        }
        return super.interpolateStep();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (Time.current() < this.tEStart2 + 1000L) return;
        super.msgCollision(actor, s, s1);
    }

    private void fishTail() {
        float f = Time.tickLenFs();
        float f1 = (float) this.getSpeed((Vector3d) null);
        f1 += (720F - f1) * 0.1F * f;
        this.pos.getAbs(this.p, this.or);
        this.v.set(1.0D, 0.0D, 0.0D);
        this.or.transform(this.v);
        this.v.scale(f1);
        this.setSpeed(this.v);
        this.p.x += this.v.x * f;
        this.p.y += this.v.y * f;
        this.p.z += this.v.z * f;
        if (this.isNet() && this.isNetMirror()) {
            this.pos.setAbs(this.p, this.or);
            return;
        }

        if (Time.current() > this.tFishTailDirectionChange) {
            this.tFishTailDirectionChange = Time.current() + TrueRandom.nextLong(200L, 600L);
            this.fishTailRight = !this.fishTailRight;
        }
        float pitch = (this.or.getPitch() + 2880.0F) % 360.0F;
        if (this.p.z < this.fLaunchAltitude + 1000F) {
            if (pitch < this.fFishTailClimb) this.or.increment(0F, TrueRandom.nextFloat(0.0F, 0.3F), 0F);
        } else if (pitch > 350F || pitch < 180F) this.or.increment(0F, -TrueRandom.nextFloat(0.0F, 0.3F), 0F);

        if (this.fishTailRight) this.or.increment(TrueRandom.nextFloat(0.0F, 3.0F), 0F, 0F);
        else this.or.increment(-TrueRandom.nextFloat(0.0F, 3.0F), 0F, 0F);
        this.or.setYPR(this.or.getYaw(), this.or.getPitch(), 0.0F);
        this.pos.setAbs(this.p, this.or);
    }

    private void doFireContaineds() {
        Actor actor = null;
        if (Actor.isValid(this.getOwner())) actor = this.getOwner();
        Point3d point3d = new Point3d(this.pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        for (int i = 0; i < 10; i++) {
            orient.set(this.or);
            orient.increment(World.Rnd().nextFloat(-80F, 80F), World.Rnd().nextFloat(-80F, 80F), 0F);
            this.getSpeed(vector3d);
            vector3d.scale(0.3D);
            RocketSchlort rocketSchlongLet = new RocketSchlort();
            rocketSchlongLet.start(-1F);
            rocketSchlongLet.pos.setUpdateEnable(true);
            rocketSchlongLet.pos.setAbs(point3d, orient);
            rocketSchlongLet.pos.reset();
            rocketSchlongLet.setOwner(actor, false, false, false);
            rocketSchlongLet.setSpeed(vector3d);
            rocketSchlongLet.setVictim(actor);
        }
        this.doExplosionAir();
        this.postDestroy();
        this.collide(false);
        this.drawing(false);
    }

    public void start(float f, int i) {
        super.start(-1F, i);
        FlightModel flightmodel = ((Aircraft) this.getOwner()).FM;
        this.tOrient.set(flightmodel.Or);
        this.speed.set(flightmodel.Vwld);
        this.noGDelay = -1L;
        this.tEStart = Time.current() + TrueRandom.nextLong(1500L, 2300L);
        this.tEStart2 = this.tEStart + TrueRandom.nextLong(2500L, 4000L);
        this.tDestroy = this.tEStart2 + TrueRandom.nextLong(9000L, 12000L);
        this.fFishTailClimb = TrueRandom.nextFloat(25F, 40F);
        if (TrueRandom.nextFloat() > 0.5F) this.fishTailRight = !this.fishTailRight;
        this.fLaunchAltitude = flightmodel.getAltitude();
        if (Config.isUSE_RENDER()) {
            this.breakSounds();
            Eff3DActor.setIntesity(this.smoke, 0.0F);
            Eff3DActor.setIntesity(this.sprite, 0.0F);
            this.flame.drawing(false);
            this.light.light.setEmit(0.0F, 0.0F);
        }
    }

    private long     tEStart;
    private long     tEStart2;
    private long     tDestroy;
    private Orient   tOrient;
    private boolean  fishTailRight            = false;
    private long     tFishTailDirectionChange = 0L;
    private float    fLaunchAltitude;
    private float    fFishTailClimb           = 35F;
    private Orient   or                       = new Orient();
    private Point3d  p                        = new Point3d();
    private Vector3d v                        = new Vector3d();

    static {
        Class class1 = RocketSchlong500.class;
        Property.set(class1, "mesh", "3DO/Arms/Schlong500/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "smokeStart", "3do/effects/rocket/rocketsmokewhitestart.eff");
        Property.set(class1, "smokeTile", "3do/effects/rocket/rocketsmokewhitetile.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 2.0F);
        Property.set(class1, "sound", "weapon.schlong");
        Property.set(class1, "radius", 0.1F);
        Property.set(class1, "timeLife", 1000000F);
        Property.set(class1, "timeFire", 33F);
        Property.set(class1, "force", 0.0F);
        Property.set(class1, "power", 0.01F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 500F);
        Property.set(class1, "massaEnd", 200F);
    }
}
