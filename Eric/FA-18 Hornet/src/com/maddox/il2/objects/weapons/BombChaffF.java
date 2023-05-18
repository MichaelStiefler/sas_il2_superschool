// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 7/28/2013 9:30:23 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombChaffF.java

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
//            Bomb, BombletChaffF

public class BombChaffF extends Bomb
{

    public BombChaffF()
    {
    }

    public void start()
    {
        super.start();
        t1 = Time.current() + 400L;
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
        Actor actor = null;
        if(Actor.isValid(getOwner()))
            actor = getOwner();
        Point3d point3d = new Point3d(pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        for(int i = 0; i < 150; i++)
        {
            orient.set(World.Rnd().nextFloat(90.0F, 270F), World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-270F, -180F));
            getSpeed(vector3d);
            vector3d.add(World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D), World.Rnd().nextDouble(-15D, 15D));
            BombletChaffF BombletChaffF = new BombletChaffF();
            ((Bomb) (BombletChaffF)).pos.setUpdateEnable(true);
            ((Bomb) (BombletChaffF)).pos.setAbs(point3d, orient);
            ((Bomb) (BombletChaffF)).pos.reset();
            BombletChaffF.start();
            BombletChaffF.setOwner(actor, false, false, false);
            BombletChaffF.setSpeed(vector3d);
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
        Class class1 = com.maddox.il2.objects.weapons.BombChaffF.class;
        Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 0.1);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}