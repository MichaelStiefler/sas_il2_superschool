// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 02/11/2015 03:24:49 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombCBU24.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb, Bomblet2Kg

public class BombCBU24 extends Bomb
{

    public BombCBU24()
    {
    }

    public void start()
    {
        super.start();
        t1 = Time.current() + 1000L * (long)Math.max(delayExplosion, 3F) + World.Rnd().nextLong(-250L, 250L);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(t1 < Time.current())
            doFireContaineds();
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(t1 > Time.current())
            doFireContaineds();
        super.msgCollision(actor, s, s1);
    }

    private void doFireContaineds()
    {
        Explosions.AirFlak(pos.getAbsPoint(), 1);
        Actor actor = null;
        if(Actor.isValid(getOwner()))
            actor = getOwner();
        Point3d point3d = new Point3d(pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        for(int i = 0; i < 150; i++)
        {
            orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
            getSpeed(vector3d);
            vector3d.add(World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D));
            Bomblet2Kg bomblet2kg = new Bomblet2Kg();
            ((Bomb) (bomblet2kg)).pos.setUpdateEnable(true);
            ((Bomb) (bomblet2kg)).pos.setAbs(point3d, orient);
            ((Bomb) (bomblet2kg)).pos.reset();
            bomblet2kg.start();
            bomblet2kg.setOwner(actor, false, false, false);
            bomblet2kg.setSpeed(vector3d);
        }

        postDestroy();
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private long t1;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombCBU24.class;
        Property.set(class1, "mesh", "3DO/Arms/CBU24/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.18F);
        Property.set(class1, "massa", 362.336F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
