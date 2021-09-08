package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombFR_10KgPx4 extends Bomb {
    public void start() {
        super.start();
        this.t1 = Time.current() + (1000L * (long) Math.max(this.delayExplosion, 1.21F)) + World.Rnd().nextLong(-250L, 250L);
        this.charge = 0;
        this.setName("qqq");
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.t1 < Time.current()) {
            this.doFireContaineds();
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if ((this.t1 > Time.current()) && this.isFuseArmed()) {
            this.doFireContaineds();
        }
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds() {
        this.charge++;
        if (this.charge < 2) {
            Actor actor = this.getOwner();
            if (!Actor.isValid(actor)) {
                return;
            }
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            Vector3d vector3d = new Vector3d();
            Vector3d vector3d1 = new Vector3d();
            Loc loc = new Loc();
            Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            this.pos.getCurrent(loc);
            this.findHook("_Spawn0" + this.charge).computePos(this, loc, loc1);
            this.getSpeed(vector3d1);
            for (int i = 0; i < 4; i++) {
                loc1.get(point3d, orient);
                orient.increment(World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(45F, 135F), World.Rnd().nextFloat(0.0F, 0.0F));
                vector3d.set(1.0D, 0.0D, 0.0D);
                orient.transform(vector3d);
                vector3d.scale(World.Rnd().nextDouble(0.001D, 5.D));
                vector3d.add(vector3d1);
                BombFR_10KgP bombfr_10kgp = new BombFR_10KgP();
                bombfr_10kgp.pos.setUpdateEnable(true);
                bombfr_10kgp.pos.setAbs(point3d, orient);
                bombfr_10kgp.pos.reset();
                bombfr_10kgp.start();
                bombfr_10kgp.setOwner(actor, false, false, false);
                bombfr_10kgp.setSpeed(vector3d);
            }

            this.t1 = Time.current() + 1000L;
        } else {
            this.postDestroy();
        }
    }

    private long t1;
    private int  charge;

    static {
        Class class1 = BombFR_10KgPx4.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr10KgPx4/mono.sim");
        Property.set(class1, "radius", 10F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.6404F);
        Property.set(class1, "massa", 40F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
