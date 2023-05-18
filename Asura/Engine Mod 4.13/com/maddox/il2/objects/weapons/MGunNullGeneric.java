// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:18:21
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MGunNullGeneric.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            GunNull, Gun, BulletAircraftGeneric, Bullet

public class MGunNullGeneric extends GunNull
    implements BulletEmitter, BulletAimer
{

    public MGunNullGeneric()
    {
        _index = -1;
    }

    public String getDayProperties(String s)
    {
        return null;
    }

    public void createdProperties()
    {
        super.createdProperties();
    }

    public String prop_fireMesh()
    {
        return null;
    }

    public String prop_fire()
    {
        return null;
    }

    public String prop_sprite()
    {
        return null;
    }

    public void setConvDistance(float f2, float f3)
    {
    }

    public void init()
    {
        int i = super.prop.bullet.length;
        super.bulletAG = new float[i];
        super.bulletKV = new float[i];
        initRealisticGunnery();
    }

    public void initRealisticGunnery(boolean flag)
    {
        int i = super.prop.bullet.length;
        for(int j = 0; j < i; j++)
        {
            super.bulletAG[j] = 0.0F;
            super.bulletKV[j] = 0.0F;
        }

    }

    public Bullet createNextBullet(Vector3d vector3d, int i, GunGeneric gungeneric, Loc loc, Vector3d vector3d1, long l)
    {
        bullet = new BulletAircraftGeneric(vector3d, i, gungeneric, loc, vector3d1, l);
        if(!World.cur().diffCur.Realistic_Gunnery)
            if(isContainOwner(World.getPlayerAircraft()));
        return bullet;
    }

    public void loadBullets(int i)
    {
        super.loadBullets(i);
        resetGuard();
    }

    public void _loadBullets(int i)
    {
        super._loadBullets(i);
        resetGuard();
    }

    private void resetGuard()
    {
        guardBullet = null;
    }

    public int nextIndexBulletType()
    {
        _index++;
        if(_index == super.prop.bullet.length)
            _index = 0;
        return _index;
    }

    protected static final boolean DEBUG = false;
    protected BulletAircraftGeneric guardBullet;
    private static Bullet bullet;
    private int _index;
}