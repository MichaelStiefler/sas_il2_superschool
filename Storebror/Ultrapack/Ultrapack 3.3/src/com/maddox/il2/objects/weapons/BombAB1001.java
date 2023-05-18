package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombAB1001 extends Bomb {

    public void start() {
        super.start();
        this.t1 = Time.current() + 1000L * (long) Math.max(this.delayExplosion, 3F) + World.Rnd().nextLong(-250L, 250L);
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.t1 < Time.current()) this.doFireContaineds();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (this.t1 > Time.current()) this.doFireContaineds();
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds() {
        Explosions.AirFlak(this.pos.getAbsPoint(), 1);
        Actor actor = null;
        if (Actor.isValid(this.getOwner())) actor = this.getOwner();
        Point3d point3d = new Point3d(this.pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        for (int i = 0; i < 320; i++) {
            orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
            this.getSpeed(vector3d);
            vector3d.add(World.Rnd().nextDouble(-20D, 20D), World.Rnd().nextDouble(-20D, 20D), World.Rnd().nextDouble(-20D, 20D));
            BombSD2A bombsd2a = new BombSD2A();
            bombsd2a.pos.setUpdateEnable(true);
            bombsd2a.pos.setAbs(point3d, orient);
            bombsd2a.pos.reset();
            bombsd2a.start();
            bombsd2a.setOwner(actor, false, false, false);
            bombsd2a.setSpeed(vector3d);
        }

        this.postDestroy();
    }

    private long t1;

    static {
        Class class1 = BombAB1001.class;
        Property.set(class1, "mesh", "3DO/Arms/AB-1000/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.6604F);
        Property.set(class1, "massa", 684.088F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
