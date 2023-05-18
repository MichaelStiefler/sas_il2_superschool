// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   Ballistics.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.AxisAngle4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class Ballistics
{

    public Ballistics()
    {
    }

    private static float KD(float f)
    {
        return 1.0F + f * (-9.59387E-005F + f * (3.53118E-009F + f * -5.83556E-014F));
    }

    private static float KF(float f)
    {
        return (608.5F + (-1.81327F + 0.00165114F * f) * f) * f;
    }

    public static void updateBomb(Bomb bomb, float f, float f1, float f2, float f3)
    {
        float f4 = Time.tickLenFs();
        float f5 = (float)bomb.getSpeed(v);
        bomb.pos.getAbs(pos, or);
        dirC.set(1.0D, 0.0D, 0.0D);
        or.transform(dirC);
        vA.set(v);
        if(f > 35F && World.cur().diffCur.Wind_N_Turbulence && World.cur().diffCur.Realistic_Gunnery)
        {
            World.wind().getVectorWeapon(pos, vWind);
            vA.sub(vWind);
            f5 = (float)vA.length();
        }
        if(f5 < 0.001F)
        {
            dirW.set(0.0D, 0.0D, -1D);
        } else
        {
            dirW.set(vA);
            dirW.scale(1.0F / f5);
        }
        float f6 = (float)dirC.dot(dirW);
        float f7;
        if(f5 > 330F)
            f7 = KF(f5) * KD((float)pos.z) * f1;
        else
            f7 = (0.06F * f1 * 1.225F * KD((float)pos.z) * f5 * f5) / 2.0F;
        float f8 = f7 / f;
        dir.scale(6F * f6, dirC);
        dir.x += -7D * dirW.x;
        dir.y += -7D * dirW.y;
        dir.z += -7D * dirW.z;
        dir.scale(f8 * f4);
        if(!World.cur().diffCur.Realistic_Gunnery)
            dir.set(0.0D, 0.0D, 0.0D);
        v.add(dir);
        v.z -= f4 * Atmosphere.g();
        bomb.setSpeed(v);
        pos.x += v.x * (double)f4;
        pos.y += v.y * (double)f4;
        pos.z += v.z * (double)f4;
        if(bomb.curTm > 0.35F)
        {
            float f9 = (float)Math.sqrt(1.0F - f6 * f6);
            if(f6 <= -0.996F)
                f9 = 0.08F;
            float f10 = f7 * 0.07F * f9;
            force.set(dirW);
            force.scale(-f10);
            dirW.set(dirC);
            dirW.scale(-f3);
            dirW.cross(dirW, force);
            dirW.scale(1.0F / f2);
            dirW.scale(f4);
            bomb.rotAxis.add(dirW);
            bomb.rotAxis.scale(0.95999999999999996D);
        }
        axAn.set(bomb.rotAxis);
        axAn.angle *= f4;
        axAn.rotateRightHanded(dirC);
        or.setAT0(dirC);
        bomb.pos.setAbs(pos, or);
    }

    public static void updateRocketBomb(Actor actor, float f, float f1, float f2, boolean flag)
    {
        float f3 = Time.tickLenFs();
        float f4 = (float)actor.getSpeed(v);
        actor.pos.getAbs(pos, or);
        dir.set(1.0D, 0.0D, 0.0D);
        or.transform(dir);
        float f5;
        if(f4 > 330F)
            f5 = KF(f4) * KD((float)pos.z) * f1;
        else
            f5 = (0.2F * f1 * 1.225F * KD((float)pos.z) * f4 * f4) / 2.0F;
        float f6 = (f2 - f5) / f;
        dir.scale(f6 * f3);
        v.add(dir);
        if(flag)
            v.z -= f3 * Atmosphere.g();
        actor.setSpeed(v);
        pos.x += v.x * (double)f3;
        pos.y += v.y * (double)f3;
        pos.z += v.z * (double)f3;
        if(f2 < 1000000F)
            or.setAT0(v);
        actor.pos.setAbs(pos, or);
    }

    public static void update(Actor actor, float f, float f1)
    {
        update(actor, f, f1, 0.0F, true);
    }

    public static void update(Actor actor, float f, float f1, float f2, boolean flag)
    {
        float f6 = Time.tickLenFs();
        float f3 = (float)actor.getSpeed(v);
        actor.pos.getAbs(pos, or);
        dir.set(1.0D, 0.0D, 0.0D);
        or.transform(dir);
        float f4;
        if(f3 > 330F)
            f4 = KF(f3) * KD((float)pos.z) * f1;
        else
            f4 = (0.2F * f1 * 1.225F * KD((float)pos.z) * f3 * f3) / 2.0F;
        float f5 = (f2 - f4) / f;
        dir.scale(f5 * f6);
        v.add(dir);
        if(flag)
            v.z -= f6 * Atmosphere.g();
        actor.setSpeed(v);
        pos.x += v.x * (double)f6;
        pos.y += v.y * (double)f6;
        pos.z += v.z * (double)f6;
        if(World.cur().diffCur.Wind_N_Turbulence && World.cur().diffCur.Realistic_Gunnery)
        {
            Vector3d vector3d = new Vector3d();
            World.wind().getVectorWeapon(pos, vector3d);
            pos.x += vector3d.x * (double)f6;
            pos.y += vector3d.y * (double)f6;
        }
        if(f2 < 1.0F)
            or.setAT0(v);
        actor.pos.setAbs(pos, or);
    }

    public static void updateSpinningRocket(Actor actor, float f, float f1, float f2, boolean flag, float spinFactor)
    {
        float f6 = Time.tickLenFs();
        float f3 = (float)actor.getSpeed(v);
        actor.pos.getAbs(pos, or);
        dir.set(1.0D, 0.0D, 0.0D);
        or.transform(dir);
        float f4;
        if(f3 > 330F)
            f4 = KF(f3) * KD((float)pos.z) * f1;
        else
            f4 = (0.2F * f1 * 1.225F * KD((float)pos.z) * f3 * f3) / 2.0F;
        float f5 = (f2 - f4) / f;
        dir.scale(f5 * f6);
        v.add(dir);
        if(flag)
            v.z -= f6 * Atmosphere.g() / (spinFactor + 1.0F);
        actor.setSpeed(v);
        pos.x += v.x * (double)f6;
        pos.y += v.y * (double)f6;
        pos.z += v.z * (double)f6;
        if(World.cur().diffCur.Wind_N_Turbulence && World.cur().diffCur.Realistic_Gunnery)
        {
            Vector3d vector3d = new Vector3d();
            World.wind().getVectorWeapon(pos, vector3d);
            pos.x += vector3d.x * (double)f6;
            pos.y += vector3d.y * (double)f6;
        }
        if(f2 < 1.0F)
            or.setAT0(v);
        actor.pos.setAbs(pos, or);
    }

    public static void update(Point3d point3d, Orient orient, Vector3d vector3d, float f, float f1, float f2, boolean flag, float f3)
    {
        float f4 = (float)vector3d.length();
        dir.set(1.0D, 0.0D, 0.0D);
        orient.transform(dir);
        float f5;
        if(f4 > 330F)
            f5 = KF(f4) * KD((float)point3d.z) * f1;
        else
            f5 = (0.2F * f1 * 1.225F * KD((float)point3d.z) * f4 * f4) / 2.0F;
        float f6 = (f2 - f5) / f;
        dir.scale(f6 * f3);
        vector3d.add(dir);
        if(flag)
            vector3d.z -= f3 * Atmosphere.g();
        point3d.x += vector3d.x * (double)f3;
        point3d.y += vector3d.y * (double)f3;
        point3d.z += vector3d.z * (double)f3;
        if(f2 < 1.0F)
            orient.setAT0(vector3d);
    }

    private static Point3d pos = new Point3d();
    private static Orient or = new Orient();
//    private static Orient orW = new Orient();
    private static Vector3d v = new Vector3d();
    private static Vector3d dir = new Vector3d();
    private static Vector3d dirC = new Vector3d();
    private static Vector3d dirW = new Vector3d();
    private static Vector3d force = new Vector3d();
    private static AxisAngle4d axAn = new AxisAngle4d();
    private static Vector3d vWind = new Vector3d();
    private static Vector3d vA = new Vector3d();

}
