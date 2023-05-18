// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 14-Nov-12 1:34:33 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Sun.java

package com.maddox.il2.engine;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.CfgInt;
import java.util.GregorianCalendar;

// Referenced classes of package com.maddox.il2.engine:
//            Config, MoonPhase, EffClouds, Engine, 
//            Landscape, LandConf, RenderContext

public class Sun
{

    public Sun()
    {
        ToLight = new Vector3f();
        SunV = new Vector3f();
        ToSun = new Vector3f();
        ToMoon = new Vector3f();
        moonPhase = 0.5F;
        tod = 0.0F;
        darkness = 1.0F;
        sunMultiplier = 1.0F;
        SunV.set(0.0F, 0.0F, -1F);
        ToSun.set(SunV);
        ToSun.negate();
        ToMoon.set(ToSun);
        ToMoon.negate();
        ToLight.set(ToSun);
        Ambient = 0.5F;
        Diffuze = 0.5F;
        Specular = 1.0F;
        Red = 1.0F;
        Green = 1.0F;
        Blue = 1.0F;
    }

    public void activate()
    {
        if(Config.isUSE_RENDER())
            setNative(ToLight.x, ToLight.y, ToLight.z, Ambient, Diffuze, Specular, Red, Green, Blue);
    }

    public void set(Vector3f vector3f)
    {
        SunV.set(vector3f);
        ToSun.set(SunV);
        ToSun.negate();
        ToLight.set(ToSun);
    }

    public void setLight(float f, float f1, float f2, float f3, float f4, float f5)
    {
        Ambient = f;
        Diffuze = f1;
        Specular = f2;
        Red = f3;
        Green = f4;
        Blue = f5;
    }

    public void setAstronomic(int i, int j, int k, int l, int i1, int j1)
    {
        setAstronomic(i, j, k, (float)l + (float)i1 / 60F + (float)j1 / 3600F);
    }

    public void setAstronomic(int i, int j, int k, float f)
    {
        setAstronomic(i, j, k, f, 1.0F, 1.0F, 1.0F);
    }

    public void resetCalendar()
    {
        missionDate = null;
    }

    public void setAstronomic(int i, int j, int k, float f, float f1, float f2, float f3)
    {
        float f5 = (float)(90 - i) * DEG2RAD;
        float f6 = (float)Math.cos(f5);
        float f7 = (float)Math.sin(f5);
        float f4 = (float)((j * 30 + k) - 80) * DEG2RAD;
        float f8 = (6.283185F * f) / 24F;
        float f9 = (float)Math.sin(f8);
        float f10 = (float)Math.cos(f8);
        float f11 = (float)Math.sin(22.5F * DEG2RAD * (float)Math.sin(f4));
        ToSun.x = f9;
        ToSun.y = f10 * f6 + f11 * f7;
        ToSun.z = f11 * f6 - f10 * f7;
        ToSun.normalize();
        SunV.x = -ToSun.x;
        SunV.y = -ToSun.y;
        SunV.z = -ToSun.z;
        int l = Mission.curYear();
        int i1 = (int)Math.floor(f);
        int j1 = (int)((f - (float)i1) * 60F);
        int k1 = 0;
        if(missionDate == null)
            missionDate = new GregorianCalendar(l, j - 1, k, i1, j1, k1);
        else
        if(f != tod)
            missionDate.roll(13, true);
        tod = f;
        double ad[] = new double[10];
        ad = MoonPhase.phase(missionDate);
        moonPhase = (float)ad[0];
        f4 = (float)((j * 30 + k) - 80) * DEG2RAD;
        f8 = 6.283185F * (-moonPhase + f / 24F);
        f9 = (float)Math.sin(f8);
        f10 = (float)Math.cos(f8);
        f11 = (float)Math.sin(22.5F * DEG2RAD * (float)Math.sin(f4));
        ToMoon.x = f9;
        ToMoon.y = f10 * f6 + f11 * f7;
        ToMoon.z = f11 * f6 - f10 * f7;
        ToMoon.normalize();
        boolean flag = true;
        float f13 = 1.0F - 2.0F * Math.abs(moonPhase - 0.5F);
        float f14 = (ToMoon.z - 0.1F) * 7F;
        if(f14 < 0.0F)
            f14 = 0.0F;
        if(f14 > 1.0F)
            f14 = 1.0F;
        float f15 = -1F + 0.15F * (f13 - 0.2F) * f14;
        float f12 = (ToSun.z + 0.11F) * 4F;
        if(f12 >= 1.0F)
            f12 = 1.0F;
        else
        if(f12 <= f15)
        {
            if(ToMoon.z > 0.1F && f13 > 0.2F)
                flag = false;
            f12 = f15;
        }
        f12 = f12 * 0.5F + 0.5F;
        float f16 = 0.2F + f12 * 1.25F;
        Diffuze = 0.05F + f12 * 0.95F;
        Specular = 0.4F + f12 * 0.6F;
        Ambient = f16 - Diffuze;
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().clouds != null && Main3D.cur3D().clouds.type() > 4)
            {
                float f17 = 0.2F + f12 * 0.7F;
                Diffuze = 0.05F + f12 * 0.95F * 0.4F;
                Specular = 0.4F + f12 * 0.6F * 0.5F;
                Ambient = f17 - Diffuze;
            }
            if(Engine.land().config.camouflage.equalsIgnoreCase("WINTER") && RenderContext.cfgHardwareShaders.get() > 0)
            {
                float f18 = (ToSun.z - 0.1F) * 7F;
                if(f18 < 0.0F)
                    f18 = 0.0F;
                if(f18 > 1.0F)
                    f18 = 1.0F;
                Ambient *= 1.0F * (1.0F - f18) + 1.7F * f18;
                Diffuze *= 1.0F * (1.0F - f18) + 0.6F * f18;
            }
            float f19 = 1.0F;
            darkness = 0.095F + f13 * 0.666F;
            sunMultiplier = cvt(ToSun.z, -0.6F, 0.0F, darkness, 1.0F);
            f19 = cvt(sunMultiplier, darkness, 1.0F, 0.5F, 1.0F);
            Ambient *= f1 * sunMultiplier;
            Specular *= f2 * f19;
            Diffuze *= f3 * sunMultiplier;
        }
        if(flag)
        {
            Red = Green = Blue = 1.0F;
            float f20 = 1.0F - Math.abs(ToSun.z) * 4F;
            if(f20 > 0.0F)
            {
                Green = 1.0F - 0.6F * f20;
                Blue = 1.0F - 0.7F * f20;
            }
            ToLight.set(ToSun);
            if(ToSun.z < 0.0F)
            {
                ToLight.z = 0.0F;
                ToLight.normalize();
            }
        } else
        {
            Red = 0.7F * f13;
            Green = Blue = f13;
            ToLight.set(ToMoon);
        }
        activate();
    }

    private static float cvt(float f, float f1, float f2, float f3, float f4)
    {
        f = Math.min(Math.max(f, f1), f2);
        return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
    }

    private static native void setNative(float f, float f1, float f2, float f3, float f4, float f5, float f6, float f7, 
            float f8);

    public Vector3f ToLight;
    public Vector3f SunV;
    public Vector3f ToSun;
    public Vector3f ToMoon;
    public float Ambient;
    public float Diffuze;
    public float Specular;
    public float Red;
    public float Green;
    public float Blue;
    public float moonPhase;
    private static GregorianCalendar missionDate = null;
    private float tod;
    private static final float MAX_DARKNESS = 0.095F;
    public float darkness;
    public float sunMultiplier;
    private static float DEG2RAD = 0.01745329F;

}