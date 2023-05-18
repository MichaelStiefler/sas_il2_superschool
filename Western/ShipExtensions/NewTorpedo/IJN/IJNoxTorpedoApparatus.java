// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 2011/09/20 13:24:33
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TorpedoApparatus.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Gun, Bomb

public abstract class IJNoxTorpedoApparatus extends Gun
    implements BulletAimer
{

    public void loadBullets(int i)
    {
        bullets(i);
    }

    public void setBulletClass(Class class1)
    {
        bulletClass = class1;
    }

    public void doStartBullet(double d)
    {
        newBomb();
        if(bomb == null)
            return;
        bomb.pos.setUpdateEnable(true);
        bomb.pos.resetAsBase();
        if(bomb.pos.getAbsPoint().z > 0.0D)
        {
            Eff3DActor.New(getOwner(), hook, null, 1.0F, "3DO/Effects/Fireworks/20_SmokeBoiling.eff", -1F);
            Eff3DActor.New(getOwner(), hook, null, 1.0F, "3DO/Effects/Fireworks/20_SparksP.eff", -1F);
        }
        bomb.start();
    }

    public void shots(int i)
    {
        doStartBullet(0.0D);
    }

    public void doEffects(boolean flag)
    {
    }

    private void newBomb()
    {
        try
        {
            bomb = (Bomb)bulletClass.newInstance();
            bomb.pos.setBase(getOwner(), hook, false);
            bomb.pos.resetAsBase();
            bomb.pos.setUpdateEnable(false);
        }
        catch(Exception exception) { }
    }

    public void set(Actor actor, String s, Loc loc)
    {
        set(actor, s);
    }

    public void set(Actor actor, String s, String s1)
    {
        set(actor, s);
    }

    public void set(Actor actor, String s)
    {
        setOwner(actor);
        Class class1 = getClass();
        bulletClass = (Class)Property.value(class1, "bulletClass", null);
        setBulletClass(bulletClass);
        hook = (HookNamed)actor.findHook(s);
        actor.interpPut(interpolater, null, -1L, null);
    }

    public float TravelTime(Point3d point3d, Point3d point3d1)
    {
        float f = (float)point3d.distance(point3d1);
        Class class1 = getClass();
        Class class2 = (Class)Property.value(class1, "bulletClass", null);
        float f1 = Property.floatValue(class2, "velocity", 1.0F);
        float f2 = Property.floatValue(class2, "traveltime", 1.0F);
        if(f > f1 * f2)
            return -1F;
        else
            return f / f1;
    }

    public boolean FireDirection(Point3d point3d, Point3d point3d1, Vector3d vector3d)
    {
        float f = (float)point3d.distance(point3d1);
        Class class1 = getClass();
        Class class2 = (Class)Property.value(class1, "bulletClass", null);
        float f1 = Property.floatValue(class2, "velocity", 1.0F);
        float f2 = Property.floatValue(class2, "traveltime", 1.0F);
        if(f > f1 * f2)
        {
            return false;
        } else
        {
            vector3d.set(point3d1);
            vector3d.sub(point3d);
            vector3d.scale(1.0F / f);
            return true;
        }
    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = new GunProperties();
        gunproperties.weaponType = 16;
        return gunproperties;
    }

    public IJNoxTorpedoApparatus()
    {
        bulletClass = null;
    }

    protected Bomb bomb;
    protected HookNamed hook;
    protected Class bulletClass;
}