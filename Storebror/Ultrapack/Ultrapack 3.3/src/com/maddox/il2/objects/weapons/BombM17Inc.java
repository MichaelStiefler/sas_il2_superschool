package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

public class BombM17Inc extends Bomb {

    private long          t1;
    private static Loc    l = new Loc();
    private static Orient o = new Orient();
    private int           charge;

    public void start() {
        super.start();
        if (this.delayExplosion == 0.0F) this.t1 = Time.current() + 200L;
        else this.t1 = Time.current() + (long) (1000.0F * this.delayExplosion);
        this.charge = 0;
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.t1 < Time.current()) this.doFireContaineds();
    }

    public void msgCollision(Actor paramActor, String paramString1, String paramString2) {
        super.msgCollision(paramActor, paramString1, paramString2);

        if (this.t1 > Time.current()) this.doFireContaineds();
        else this.destroy();
    }

    private void ExplodeCharge(Point3d paramPoint3d) {
        if (!Config.isUSE_RENDER()) return;
        o.set(0.0F, 0.0F, 0.0F);
        l.set(paramPoint3d, o);

        String str = "effects/Explodes/Air/Zenitka/US_Frag/";

        this.FragChargeSound(paramPoint3d);
        float f = -1.0F;

        Eff3DActor.New(l, 1.0F, str + "Sparks.eff", f);
        Eff3DActor.New(l, 1.0F, str + "SparksP.eff", f);
    }

    private void FragChargeSound(Point3d paramPoint3d) {
        SoundPreset localSoundPreset = new SoundPreset("explode.bullet");
        SoundFX localSoundFX = new SoundFX(localSoundPreset);
        localSoundFX.setPosition(paramPoint3d);
        localSoundFX.setUsrFlag(1);
        localSoundFX.play();
    }

    private void doFireContaineds() {
        this.charge++;
        if (this.charge < 6) {
            // Explosions.airbustABcontainer(this.pos.getAbsPoint(), 3);
            this.ExplodeCharge(this.pos.getAbsPoint());
            Actor actor = this.getOwner();
            if (!Actor.isValid(actor)) return;

            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            Vector3d vector3d1 = new Vector3d();
            Vector3d vector3d2 = new Vector3d();
            Loc loc1 = new Loc();
            // Loc loc2 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            this.pos.getCurrent(loc1);
            // findHook("_Spawn0" + this.charge).computePos(this, loc1, loc2);
            this.getSpeed(vector3d2);
            for (int i = 0; i < (this.charge % 3 == 0 ? 19 : 18); i++) {
                // loc2.get(point3d, orient);
                loc1.get(point3d, orient);
                orient.increment(World.Rnd().nextFloat(-135.0F, 135.0F), World.Rnd().nextFloat(-17.5F, 17.5F), World.Rnd().nextFloat(-0.5F, 0.5F));
                vector3d1.set(1.0D, 0.0D, 0.0D);
                orient.transform(vector3d1);
                // vector3d1.scale(World.Rnd().nextDouble(5.0D,
                // 38.2D));
                vector3d1.scale(World.Rnd().nextDouble(1.0D, 10.0D));
                vector3d1.add(vector3d2);
                Bomb bomblet = null;
                if (i == 0) bomblet = new BombM50XA3Inc();
                else bomblet = new BombM50TA2Inc();
                bomblet.pos.setUpdateEnable(true);
                bomblet.pos.setAbs(point3d, orient);
                bomblet.pos.reset();
                bomblet.start();
                bomblet.setOwner(actor, false, false, false);
                bomblet.setSpeed(vector3d1);

                if (i % 4 == 0) Eff3DActor.New(bomblet, null, null, 3.0F, "effects/Smokes/SmokeBlack_BuletteTrail.eff", 30.0F);
            }
            this.t1 = Time.current() + 200L;
        } else // Explosions.AirFlak(this.pos.getAbsPoint(), 1);
            this.postDestroy();
    }

    static {
        Class class1 = BombM17Inc.class;
        Property.set(class1, "mesh", "3DO/Arms/M17Incendiary/mono.sim");
        Property.set(class1, "radius", 35F);
        Property.set(class1, "power", 50F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 260F);
        Property.set(class1, "sound", "weapon.bomb_phball");
    }
}
