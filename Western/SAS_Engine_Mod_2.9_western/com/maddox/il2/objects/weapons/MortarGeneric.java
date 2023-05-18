// Source File Name:   MortarGeneric.java from stock 4.13.4m

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.Atmosphere;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Gun, BulletParabolaGeneric, Bullet

public abstract class MortarGeneric extends Gun
    implements BulletAimer
{

    public MortarGeneric()
    {
        bulletTypeIdx = 0;
    }

    public final void setBulletType(int i)
    {
        bulletTypeIdx = i;
    }

    public int nextIndexBulletType()
    {
        return bulletTypeIdx;
    }

    public Bullet createNextBullet(Vector3d vector3d, int i, GunGeneric gungeneric, Loc loc, Vector3d vector3d1, long l)
    {
        return new BulletParabolaGeneric(vector3d, i, gungeneric, loc, vector3d1, l);
    }

    public static void autocomputeSplintersRadiuses(BulletProperties abulletproperties[])
    {
        for(int i = 0; i < abulletproperties.length; i++)
            if(abulletproperties[i].power > 0.0F && abulletproperties[i].powerType == 1)
            {
                float f = 110F;
                float f1 = 280F;
                float f2 = (abulletproperties[i].kalibr - 0.037F) / 0.045F;
                if(f2 <= 0.0F)
                    f2 = 0.0F;
                if(f2 >= 1.0F)
                    f2 = 1.0F;
                abulletproperties[i].powerRadius = f + f2 * (f1 - f);
            }

    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.weaponType = 4;
        gunproperties.bCannon = true;
        gunproperties.bUseHookAsRel = false;
        gunproperties.fireMesh = null;
        gunproperties.fire = null;
        gunproperties.sprite = null;
        gunproperties.smoke = "Effects/Smokes/MortarLight.eff";
        gunproperties.shells = null;
        gunproperties.emitColor = new Color3f(0.6F, 0.4F, 0.2F);
        gunproperties.emitI = 5F;
        gunproperties.emitR = 10F;
        gunproperties.emitTime = 0.1F;
        gunproperties.aimMinDist = 85F;
        gunproperties.aimMaxDist = 3040F;
        gunproperties.shotFreq = 999F;
        gunproperties.traceFreq = 0;
        gunproperties.bullets = -1;
        gunproperties.bulletsCluster = 1;
        gunproperties.sound = "weapon.Cannon45t";
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        for(int i = 0; i < gunproperties.bullet.length; i++)
        {
            gunproperties.bullet[i].massa = 0.001F;
            gunproperties.bullet[i].kalibr = 9F;
            gunproperties.bullet[i].speed = 10F;
            gunproperties.bullet[i].power = 0.0F;
            gunproperties.bullet[i].powerType = 1;
            gunproperties.bullet[i].powerRadius = 140F;
            gunproperties.bullet[i].timeLife = 10F;
            gunproperties.bullet[i].traceMesh = null;
            gunproperties.bullet[i].traceTrail = null;
            gunproperties.bullet[i].traceColor = 0;
        }

        float f = Specify(gunproperties);
        autocomputeSplintersRadiuses(gunproperties.bullet);
        for(int j = 0; j < gunproperties.bullet.length; j++)
        {
            float f1 = gunproperties.aimMaxDist / (gunproperties.bullet[j].speed * 0.707F);
            gunproperties.bullet[j].timeLife = f1 * 2.0F;
        }

        if(f > 0.0F)
        {
            if(f <= 20F)
                f = 20F;
            if(f >= 70F)
                f = 70F;
            f = (f - 20F) / 50F;
            gunproperties.maxDeltaAngle = 0.3F - f * 0.2F;
        } else
        {
            gunproperties.maxDeltaAngle = 0.2F;
        }
        return gunproperties;
    }

    protected abstract float Specify(GunProperties gunproperties);

    public float TravelTime(Point3d point3d, Point3d point3d1)
    {
        float f = (float)(point3d1.z - point3d.z);
        if(Math.abs(f) > prop.aimMaxDist + spotterModifier)
            return -1F;
        Vector3d vector3d = new Vector3d();
        getSpeed(vector3d);
        Vector3d vector3d1 = new Vector3d();
        float f1 = (float)(point3d1.x - point3d.x);
        float f2 = (float)(point3d1.y - point3d.y);
        float f3 = (float)Math.sqrt(f1 * f1 + f2 * f2);
        if(f3 > prop.aimMaxDist + spotterModifier)
            return -1F;
        if(f3 < prop.aimMinDist && Math.abs(f) < prop.aimMinDist)
            return -1F;
        float f4 = f3;
        float f5 = f;
        f1 = -Atmosphere.g() / 2.0F;
        float f6 = 0.0F;
        float f7 = -1F;
        f3 = f1 * f1;
        float f8 = 0.0F;
        for(int i = 0; i < prop.bullet.length; i++)
        {
            getSpeed(vector3d);
            vector3d1.set(f1, f2, 0.0D);
            vector3d1.normalize();
            f8 = prop.bullet[i].speed;
            vector3d1.scale(f8);
            vector3d1.add(vector3d);
            f6 = -(2.0F * f5 * f1 + f8 * f8);
            f7 = f6 * f6 - 4F * f3 * (f4 * f4 + f5 * f5);
            if(f7 < 0.0F)
                continue;
            setBulletType(i);
            break;
        }

        if(f7 < 0.0F)
            return -1F;
        f7 = (float)Math.sqrt(f7);
        f8 = (-f6 + f7) / (2.0F * f3);
        float f9 = (-f6 - f7) / (2.0F * f3);
        timeToFly = 0.0F;
        if(f8 >= 0.0F)
        {
            if(f9 >= 0.0F && f9 < f8)
                timeToFly = f8;
        } else
        if(f9 >= 0.0F)
            timeToFly = f9;
        else
            return -1F;
        timeToFly = (float)Math.sqrt(timeToFly);
        return timeToFly;
    }

    public boolean FireDirection(Point3d point3d, Point3d point3d1, Vector3d vector3d)
    {
        Vector3d vector3d1 = new Vector3d();
        Vector3d vector3d2 = new Vector3d();
        getSpeed(vector3d1);
        vector3d1.scale(timeToFly);
        point3d1.sub(vector3d1);
        float f = (float)(point3d1.x - point3d.x);
        float f1 = (float)(point3d1.y - point3d.y);
        float f2 = (float)Math.sqrt(f * f + f1 * f1);
        float f3 = (float)(point3d1.z - point3d.z);
        if((double)Math.abs(f2) < 0.01D)
        {
            vector3d.set(0.0D, 0.0D, 1.0D);
            if(f3 < 0.0F)
                vector3d.negate();
            return true;
        }
        vector3d.set(f, f1, 0.0D);
        if(timeToFly >= 0.0001F)
        {
            float f4 = f3 + (Atmosphere.g() / 2.0F) * timeToFly * timeToFly;
            vector3d.z = f4;
            vector3d.normalize();
        } else
        {
            return false;
        }
        return true;
    }

    private float timeToFly;
    protected int bulletTypeIdx;
}
