package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Bomb110Napalm extends Bomb {

    public void start() {
        super.start();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        this.doFireContaineds();
        Point3d point3d = new Point3d();
        this.pos.getTime(Time.current(), point3d);
        Class class1 = this.getClass();
        float f = Property.floatValue(class1, "power", 0.0F);
        int i = Property.intValue(class1, "powerType", 0);
        float f1 = Property.floatValue(class1, "radius", 1.0F);
        MsgExplosion.send(actor, s1, point3d, this.getOwner(), this.M, f, i, f1);
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds() {
        // System.out.println("doFireContaineds at " + Time.current());
        Actor actor = null;
        if (Actor.isValid(this.getOwner())) actor = this.getOwner();

        Point3d point3d = new Point3d();
        Orient orient = new Orient();
        Vector3d vector3d1 = new Vector3d();
        Vector3d vector3d2 = new Vector3d();
        Loc loc1 = new Loc();
        this.pos.getCurrent(loc1);
        this.getSpeed(vector3d2);
        vector3d2.z = 0.0D;

        for (int i = 0; i < 20; i++) {
            loc1.get(point3d, orient);
            point3d.z += 2.0D;
            orient.increment(World.Rnd().nextFloat(-135.0F, 135.0F), World.Rnd().nextFloat(30.0F, 70.0F), 0.0F);
            vector3d1.set(1.0D, 0.0D, 0.0D);
            orient.transform(vector3d1);
            vector3d1.scale(World.Rnd().nextDouble(10.0D, 20.0D));
            vector3d2.scale(World.Rnd().nextDouble(0.3D, 0.6D));
            vector3d1.add(vector3d2);

            if (i < 3) {
                BombletFireNapalm2 bombletfire2 = new BombletFireNapalm2();
                bombletfire2.pos.setUpdateEnable(true);
                bombletfire2.pos.setAbs(point3d, orient);
                bombletfire2.pos.reset();
                bombletfire2.setOwner(actor, false, false, false);
                bombletfire2.start();
                bombletfire2.setSpeed(vector3d1);
            } else {
                vector3d1.x *= 1.2D;
                vector3d1.y *= 1.2D;
                BombletFireNapalm bombletfire = new BombletFireNapalm();
                bombletfire.pos.setUpdateEnable(true);
                bombletfire.pos.setAbs(point3d, orient);
                bombletfire.pos.reset();
                bombletfire.setOwner(actor, false, false, false);
                bombletfire.start();
                bombletfire.setSpeed(vector3d1);
            }
        }

        this.postDestroy();
    }

    // private void doFireContaineds()
    // {
    // System.out.println("doFireContaineds at " + Time.current());
    // Actor actor = null;
    // if(Actor.isValid(getOwner()))
    // actor = getOwner();
    // Orient orient = new Orient();
    // Vector3d vector3d = new Vector3d();
    // Point3d point3d = new Point3d(pos.getAbsPoint());
    // point3d.z += 2.0D;
    // for(int i = 0; i < 16; i++)
    // {
    // // orient.set(World.Rnd().nextFloat(0.0F, 360F),
    // World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
    // orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(0F,
    // 90F), 0F);
    // this.getSpeed(vector3d);
    // vector3d.add(World.Rnd().nextFloat(50F, -50F), World.Rnd().nextFloat(50F,
    // -50F), 0.0D);
    // vector3d.z = 60.0D;
    // if(i < 3)
    // {
    // vector3d.x *= 0.55D;
    // vector3d.y *= 0.55D;
    // Bomb bombletfire2 = new BombletFireNapalm2();
    // bombletfire2.pos.setUpdateEnable(true);
    // bombletfire2.pos.setAbs(point3d, orient);
    // bombletfire2.pos.reset();
    // bombletfire2.setSpeed(vector3d);
    // bombletfire2.setOwner(actor, false, false, false);
    // bombletfire2.start();
    // } else
    // {
    // vector3d.x *= 0.6D;
    // vector3d.y *= 0.6D;
    // Bomb bombletfire = new BombletFireNapalm();
    // bombletfire.pos.setUpdateEnable(true);
    // bombletfire.pos.setAbs(point3d, orient);
    // bombletfire.pos.reset();
    // bombletfire.setSpeed(vector3d);
    // bombletfire.setOwner(actor, false, false, false);
    // bombletfire.start();
    // }
    // }
    //
    // postDestroy();
    // }

    static {
        Class class1 = Bomb110Napalm.class;
        Property.set(class1, "mesh", "3DO/Arms/Napalm110gal/mono.sim");
        // Property.set(class1, "radius", 0F);
        // Property.set(class1, "power", 0F);
        // Property.set(class1, "powerType", 0);
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 110F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 300.36F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
