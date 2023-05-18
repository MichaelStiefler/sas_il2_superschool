/*Modified Explosion class for the SAS Engine Mod*/
//TODO: Modified to allow for nuclear weapon explosions

package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;

public class Explosion
{

    public Explosion()
    {
        p = new Point3d();
    }

    void computeSplinterParams(float f)
    {
        float f1 = f * 0.9F;
        nSplinters = f1 / 0.015F;
        if(nSplinters < 0.5F)
        {
            nSplinters = 0.0F;
            return;
        } else
        {
            return;
        }
    }

    public float computeSplinterSpeed(float f)
    {
        if(f <= 0.01F)
            return 650F;
        if(f >= radius)
        {
            return 150F;
        } else
        {
            float f1 = f / radius;
            return 650F * (1.0F - f1) + 150F * f1;
        }
    }

    public void computeSplintersHit(Point3d point3d, float f, float f1, float af[])
    {
        float f2 = (float)point3d.distance(p) - f;
        if(f2 <= 0.001F)
        {
            af[0] = nSplinters * 0.5F;
            af[1] = computeSplinterSpeed(f2);
        }
        float f3 = 3.141593F * f * f;
        float f4 = 12.56637F * f2 * f2;
        float f5 = (nSplinters * f3) / f4;
        if(f5 >= nSplinters * 0.5F)
            f5 = nSplinters * 0.5F;
        af[0] = f5;
        af[1] = computeSplinterSpeed(f2);
    }

    public boolean isMirage()
    {
        if(!Actor.isValid(initiator))
            return true;
        else
            return initiator.isNetMirror();
    }

    public float receivedPower(ActorMesh actormesh)
    {
        float f = actormesh.collisionR();
        float f1 = (float)actormesh.pos.getAbsPoint().distance(p);
        f1 -= f;
        if(f1 >= radius)
            return 0.0F;
        float f2 = 1.0F - f1 / radius;
        f2 *= f2;
        if(f2 >= 1.0F)
            return power;
        else
            return f2 * power;
    }

    public float receivedTNT_1meter(float f)
    {
        if(f >= radius)
            return 0.0F;
        if(f < 1.0F || bNuke)
            return power;
        else
            return power / (f * f);
    }

    public float receivedTNT_1meter(Point3d point3d, float f)
    {
        float f1 = (float)point3d.distance(p) - f;
        return receivedTNT_1meter(f1);
    }

    public float receivedTNT_1meter(ActorMesh actormesh)
    {
        float f = (float)actormesh.pos.getAbsPoint().distance(p) - actormesh.collisionR();
        return receivedTNT_1meter(f);
    }

    public float receivedTNTpower(ActorMesh actormesh)
    {
        float f = actormesh.collisionR();
        float f1 = (float)actormesh.pos.getAbsPoint().distance(p) - f;
        if(f1 <= 0.0F)
            return 0.5F * power;
        float f2 = 1.0F / (float)Math.pow(f1, 1.2000000476837158D);
        if(f2 <= 0.0F)
            return 0.0F;
        if(f2 >= 0.5F)
            f2 = 0.5F;
        return f2 * power;
    }

    public static boolean killable(ActorMesh actormesh, float f, float f1, float f2, float f3)
    {
        float f4 = f;
        if(f4 <= f1)
            return false;
        if(f4 >= f2)
        {
            return true;
        } else
        {
            float f5 = (f4 - f1) / (f2 - f1);
            f3 += (1.0F - f3) * f5;
            return World.Rnd().nextFloat(0.0F, 1.0F) < f3;
        }
    }

    public static final int POWER_SPLASH = 0;
    public static final int POWER_SPLINTERS = 1;
    public static final int POWER_NAPALM = 2;
    public static final float SPLINTER_MASS = 0.015F;
    public String chunkName;
    public Point3d p;
    public float radius;
    public Actor initiator;
    public float power;
    public int powerType;
    private float nSplinters;
    public boolean bNuke;
}