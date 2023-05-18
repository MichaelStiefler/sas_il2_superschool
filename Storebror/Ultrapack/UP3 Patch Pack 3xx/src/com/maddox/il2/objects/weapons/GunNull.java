package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.TypeGuidedMissileCarrier;

public abstract class GunNull extends Gun
{

    public GunNull()
    {
        hasBullets = true;
        super.flags = super.flags | 0x4004;
        super.pos = new ActorPosMove(this);
    }

    public void emptyGun()
    {
        hasBullets = false;
    }

    public BulletEmitter detach(HierMesh hiermesh, int i)
    {
        return this;
    }

    public void initRealisticGunnery(boolean flag1)
    {
    }

    public boolean isEnablePause()
    {
        return false;
    }

    public float bulletMassa()
    {
        return 0.0F;
    }

    public float bulletSpeed()
    {
        return 0.0F;
    }

    public int countBullets()
    {
        if(getOwner() != null && (getOwner() instanceof TypeGuidedMissileCarrier))
            return ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().hasMissiles() ? 0x7fffffff : 0;
        else
            return hasBullets ? 0x7fffffff : 0;
    }

    public boolean haveBullets()
    {
        if(getOwner() != null && (getOwner() instanceof TypeGuidedMissileCarrier))
            return ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().hasMissiles();
        else
            return hasBullets;
    }

    public void loadBullets()
    {
        EventLog.type("loadBullets");
        hasBullets = true;
    }

    public void loadBullets(int i)
    {
        EventLog.type("loadBullets " + i);
        hasBullets = true;
    }

    public Class bulletClass()
    {
        return null;
    }

    public void setBulletClass(Class class2)
    {
    }

    public boolean isShots()
    {
        return false;
    }

    public void shots(int j, float f1)
    {
    }

    public void shots(int j)
    {
    }

    public String getHookName()
    {
        return "Body";
    }

    public void set(Actor actor1, String s1, Loc loc1)
    {
    }

    public void set(Actor actor1, String s2, String s3)
    {
    }

    public void set(Actor actor1, String s1)
    {
    }

    private boolean hasBullets;
}
