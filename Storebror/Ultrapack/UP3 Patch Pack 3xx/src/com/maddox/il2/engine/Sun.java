package com.maddox.il2.engine;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.maddox.JGP.Vector2f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;

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
        tmpV3f = new Vector3f();
        tmpV2f = new Vector2f();
        aiLow = 1.0F;
        aiHigh = 1.0F;
        clLow = -1F;
        clHigh = -1F;
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

    private static float zLight(float f)
    {
        if(f > -0.104F)
            return 12.9364F - 1.43485F / (f + 0.2275954F);
        if(f > -0.31F)
            return 0.4315F + f * (-132.66F + f * (-1830.9F + f * (-6972.2F + f * -8738.5F)));
        else
            return -1E+009F;
    }

    public static float cvt(float inValue, float inMin, float inMax, float outMin, float outMax)
    {
        inValue = Math.min(Math.max(inValue, inMin), inMax);
        return outMin + ((outMax - outMin) * (inValue - inMin)) / (inMax - inMin);
    }

    public static float cvtsin1(float inValue, float inMin, float inMax, float outMin, float outMax)
    {
        inValue = Math.min(Math.max(inValue, inMin), inMax);
        float radians = (inValue-inMin) / (inMax-inMin) * (float)Math.PI / 2F;
        return outMin + (outMax - outMin) * (float)Math.sin(radians);
    }

    public void resetCalendar()
    {
        missionDate = null;
    }
    
    public void setAstronomic(int declination, int month, int day, float timeOfDay, float altitude)
    {
        float declinationRadians = (float)(90 - declination) * DEG2RAD;
        float declinationCosinus = (float)Math.cos(declinationRadians);
        float declinationSinus = (float)Math.sin(declinationRadians);
        float dayRadians = (float)(month * 30 + day - 80) * DEG2RAD;
        float timeOfDayRadians = (float)Math.PI * timeOfDay / 12F;
        float timeOfDaySinus = (float)Math.sin(timeOfDayRadians);
        float timeOfDayCosinus = (float)Math.cos(timeOfDayRadians);
        float elevationRadians = (float)Math.sin((float)Math.PI / 8F * (float)Math.sin(dayRadians));
        ToSun.x = timeOfDaySinus;
        ToSun.y = timeOfDayCosinus * declinationCosinus + elevationRadians * declinationSinus;
        ToSun.z = elevationRadians * declinationCosinus - timeOfDayCosinus * declinationSinus;
        ToSun.normalize();
        SunV.x = -ToSun.x;
        SunV.y = -ToSun.y;
        SunV.z = -ToSun.z;
        int gregYear = Mission.curYear();
        int gregHour = (int)Math.floor(timeOfDay);
        int gregMinute = (int)((timeOfDay - (float)gregHour) * 60F);
        int gregSecond = 0;
        if(missionDate == null)
            missionDate = new GregorianCalendar(gregYear, month - 1, day, gregHour, gregMinute, gregSecond);
        else
        if(timeOfDay != tod)
            missionDate.roll(Calendar.SECOND, true);
        tod = timeOfDay;
        double moonPhaseArray[] = new double[10];
        moonPhaseArray = MoonPhase.phase(missionDate);
        moonPhase = (float)moonPhaseArray[0];
        dayRadians = (float)(month * 30 + day - 80) * DEG2RAD + (float)Math.PI * 2F * moonPhase;
        timeOfDayRadians = (float)Math.PI * 2F * (-moonPhase + timeOfDay / 24F);
        timeOfDaySinus = (float)Math.sin(timeOfDayRadians);
        timeOfDayCosinus = (float)Math.cos(timeOfDayRadians);
        elevationRadians = (float)Math.sin((float)Math.PI / 8F * (float)Math.sin(dayRadians));
        ToMoon.x = timeOfDaySinus;
        ToMoon.y = timeOfDayCosinus * declinationCosinus + elevationRadians * declinationSinus;
        ToMoon.z = elevationRadians * declinationCosinus - timeOfDayCosinus * declinationSinus;
        ToMoon.normalize();
        float f14 = 0.0F;
        if(ToMoon.z > -0.31F)
        {
            float f11 = zLight(ToMoon.z) - 11.76757F - 1.066181F;
            float f15 = 1F - 2.0F * Math.abs(moonPhase - 0.5F);
            f11 += (-0.01F / f15 - 5.225F) + f15 * 5.235F;
            f14 = (float)Math.exp(f11);
        }
        float f16 = 0.0F;
        if(ToSun.z > -0.31F)
        {
            float f13 = zLight(ToSun.z);
            f16 = (float)Math.exp(f13);
        }
        float f17 = (float)Math.log(f14 + f16 + 0.001076F);
        float f18 = 0.0F;
        EffClouds effclouds = Main.cur().clouds;
        if(effclouds != null && effclouds.type() > 2)
        {
            float cloudsHeight = effclouds.height();
            float cloudsFactor1 = 0.0F;
            switch(effclouds.type())
            {
            case 3:
                clLow = cloudsHeight + 200F;
                clHigh = cloudsHeight + 1000F;
                cloudsFactor1 = 4.8F;
                break;

            case 4:
                clLow = cloudsHeight + 200F;
                clHigh = cloudsHeight + 1600F;
                cloudsFactor1 = 8F;
                break;

            case 5:
                clLow = cloudsHeight + 100F;
                clHigh = cloudsHeight + 1900F;
                cloudsFactor1 = 12.8F;
                break;

            case 6:
                clLow = cloudsHeight;
                clHigh = cloudsHeight + 1200F;
                cloudsFactor1 = 16F;
                break;
            }
            f18 = cvt(altitude, clLow, clHigh, cloudsFactor1, 0.0F);
        }
        float f20 = f17 - f18;
        aiLow = (f20 >= 0.0F ? f20 : f20 / 6F) * 0.11F + 0.211F;
        aiLow = aiLow >= 0.0F ? aiLow <= 1.0F ? aiLow : 1.0F : 0.0F;
        aiHigh = (f17 >= 0.0F ? f17 : f17 / 6F) * 0.11F + 0.211F;
        aiHigh = aiHigh >= 0.0F ? aiHigh <= 1.0F ? aiHigh : 1.0F : 0.0F;
        h0 = altitude;
        f20 = aiLow;
        float f22 = f20;
        float f23 = 0.0F;
        Specular = f22;
        f23 += f22 * ((f18 / 16F) * 0.4F + 0.3F);
        f22 -= f23;
        Diffuze = f22 * 1.4285F;
        Ambient = f23 * 1.4285F;
        
        // TODO: +++ Moonlight Customization Mod by SAS~Storebror +++
        float DiffuzeMax = cvtsin1(ToSun.z, -0.2F, 0.0F, 0.3F, 0.9F);
        if(ToSun.z < 0F && Diffuze < DiffuzeMax)
        {
            float moonPhaseDiff = cvt(Math.abs(0.5F - moonPhase), 0.0F, 0.5F, 1.0F, 0.25F) * (float)Config.cur.iDiffuse / (float)Config.MAX_NIGHT_SETTINGS;
            Diffuze = cvt(moonPhaseDiff + Diffuze, 0.35F, 1.0F, 0.20F, DiffuzeMax);
        }
        // TODO: --- Moonlight Customization Mod by SAS~Storebror ---
        
        if(Ambient < 0.001F)
            Ambient = 0.001F;
        if(Specular < 0.01F)
            Specular = 0.01F;
        if(Diffuze < 0.001F)
            Diffuze = 0.001F;
        
        float f24 = f16 / (f14 + f16 + 0.001076F);
        float f25 = 1.0F - f24;
        tRed = tGreen = tBlue = 1.0F;
        float f26 = 1.0F - Math.abs(ToSun.z) * 4F;
        if(f26 > 0.0F)
        {
            tGreen = 1.0F - 0.6F * f26;
            tBlue = 1.0F - 0.7F * f26;
        }
        ToLight.set(ToSun);
        ToLight.scale(f16 + 0.001F);
        tmpV3f.set(ToMoon);
        tmpV3f.scale(f14 + 0.001F);
        ToLight.add(tmpV3f);
        ToLight.normalize();
        if(ToLight.z < 0.05F)
        {
            tmpV2f.x = ToLight.x;
            tmpV2f.y = ToLight.y;
            tmpV2f.normalize();
            tmpV2f.scale(0.998749F);
            ToLight.x = tmpV2f.x;
            ToLight.y = tmpV2f.y;
            ToLight.z = 0.05F;
        }
        Red = tRed * f24 + (1.0F - 0.3F * f25) * f25;
        Green = tGreen * f24 + f25;
        Blue = tBlue * f24 + f25;
        float f27 = 1.0F / Math.max(Math.max(Red, Green), Blue);
        f27 *= 1.0F - (1.0F - lightAtAlt(altitude)) * 0.8F;
        Red *= f27;
        Green *= f27;
        Blue *= f27;
        if(ToSun.z < 0.0F)
            ToSun.z *= 1.0F - ToSun.z * 10F;
        ToSun.normalize();
        activate();
        
        // TODO: Ultrapack backward compatibility!
        if (Config.isUSE_RENDER()) {
            float moonPhaseFactor = 1.0F - 2.0F * Math.abs(moonPhase - 0.5F);
            darkness = 0.095F + moonPhaseFactor * 0.666F;
            sunMultiplier = cvt(ToSun.z, -0.6F, 0.0F, darkness, 1.0F);
        }
    }


    public void setAstronomicOld(int declination, int month, int day, float timeOfDay, float altitude)
    {
        float declinationRadians = (float)(90 - declination) * DEG2RAD;
        float declinationCosinus = (float)Math.cos(declinationRadians);
        float declinationSinus = (float)Math.sin(declinationRadians);
        float dayRadians = (float)(month * 30 + day - 80) * DEG2RAD;
        float timeOfDayRadians = (float)Math.PI * timeOfDay / 12F;
        float timeOfDaySinus = (float)Math.sin(timeOfDayRadians);
        float timeOfDayCosinus = (float)Math.cos(timeOfDayRadians);
        float elevationRadians = (float)Math.sin((float)Math.PI / 8F * (float)Math.sin(dayRadians));
        ToSun.x = timeOfDaySinus;
        ToSun.y = timeOfDayCosinus * declinationCosinus + elevationRadians * declinationSinus;
        ToSun.z = elevationRadians * declinationCosinus - timeOfDayCosinus * declinationSinus;
        ToSun.normalize();
        SunV.x = -ToSun.x;
        SunV.y = -ToSun.y;
        SunV.z = -ToSun.z;
        int gregYear = Mission.curYear();
        int gregHour = (int)Math.floor(timeOfDay);
        int gregMinute = (int)((timeOfDay - (float)gregHour) * 60F);
        int gregSecond = 0;
        if(missionDate == null)
            missionDate = new GregorianCalendar(gregYear, month - 1, day, gregHour, gregMinute, gregSecond);
        else
        if(timeOfDay != tod)
            missionDate.roll(Calendar.SECOND, true);
        tod = timeOfDay;
        double moonPhaseArray[] = new double[10];
        moonPhaseArray = MoonPhase.phase(missionDate);
        moonPhase = (float)moonPhaseArray[0];
        timeOfDayRadians = (float)Math.PI * 2F * (-moonPhase + timeOfDay / 24F);
        timeOfDaySinus = (float)Math.sin(timeOfDayRadians);
        timeOfDayCosinus = (float)Math.cos(timeOfDayRadians);
        ToMoon.x = timeOfDaySinus;
        ToMoon.y = timeOfDayCosinus * declinationCosinus + elevationRadians * declinationSinus;
        ToMoon.z = elevationRadians * declinationCosinus - timeOfDayCosinus * declinationSinus;
        ToMoon.normalize();
        float f14 = 0.0F;
        if(ToMoon.z > -0.31F)
        {
            float f11 = zLight(ToMoon.z) - 11.76757F - 1.066181F;
            float f15 = 1F - 2.0F * Math.abs(moonPhase - 0.5F);
            f11 += (-0.01F / f15 - 5.225F) + f15 * 5.235F;
            f14 = (float)Math.exp(f11);
        }
        float f16 = 0.0F;
        if(ToSun.z > -0.31F)
        {
            float f13 = zLight(ToSun.z);
            f16 = (float)Math.exp(f13);
        }
        float f17 = (float)Math.log(f14 + f16 + 0.001076F);
        float f18 = 0.0F;
        EffClouds effclouds = Main.cur().clouds;
        if(effclouds != null && effclouds.type() > 2)
        {
            float cloudsHeight = effclouds.height();
            float cloudsFactor1 = 0.0F;
            switch(effclouds.type())
            {
            case 3:
                clLow = cloudsHeight + 200F;
                clHigh = cloudsHeight + 1000F;
                cloudsFactor1 = 4.8F;
                break;

            case 4:
                clLow = cloudsHeight + 200F;
                clHigh = cloudsHeight + 1600F;
                cloudsFactor1 = 8F;
                break;

            case 5:
                clLow = cloudsHeight + 100F;
                clHigh = cloudsHeight + 1900F;
                cloudsFactor1 = 12.8F;
                break;

            case 6:
                clLow = cloudsHeight;
                clHigh = cloudsHeight + 1200F;
                cloudsFactor1 = 16F;
                break;
            }
            f18 = cvt(altitude, clLow, clHigh, cloudsFactor1, 0.0F);
        }
        float f20 = f17 - f18;
        aiLow = (f20 >= 0.0F ? f20 : f20 / 6F) * 0.11F + 0.211F;
        aiLow = aiLow >= 0.0F ? aiLow <= 1.0F ? aiLow : 1.0F : 0.0F;
        aiHigh = (f17 >= 0.0F ? f17 : f17 / 6F) * 0.11F + 0.211F;
        aiHigh = aiHigh >= 0.0F ? aiHigh <= 1.0F ? aiHigh : 1.0F : 0.0F;
        h0 = altitude;
        f20 = aiLow;
        float f22 = f20;
        float f23 = 0.0F;
        Specular = f22;
        f23 += f22 * ((f18 / 16F) * 0.4F + 0.3F);
        f22 -= f23;
        Diffuze = f22 * 1.4285F;
        Ambient = f23 * 1.4285F;
        
        // TODO: +++ Moonlight Customization Mod by SAS~Storebror +++
        float DiffuzeMax = cvtsin1(ToSun.z, -0.2F, 0.0F, 0.3F, 0.9F);
        if(ToSun.z < 0F && Diffuze < DiffuzeMax)
        {
            float moonPhaseDiff = cvt(Math.abs(0.5F - moonPhase), 0.0F, 0.5F, 1.0F, 0.25F) * (float)Config.cur.iDiffuse / (float)Config.MAX_NIGHT_SETTINGS;
            Diffuze = cvt(moonPhaseDiff + Diffuze, 0.35F, 1.0F, 0.20F, DiffuzeMax);
        }
        // TODO: --- Moonlight Customization Mod by SAS~Storebror ---
        
        if(Ambient < 0.001F)
            Ambient = 0.001F;
        if(Specular < 0.01F)
            Specular = 0.01F;
        if(Diffuze < 0.001F)
            Diffuze = 0.001F;
        
        float f24 = f16 / (f14 + f16 + 0.001076F);
        float f25 = 1.0F - f24;
        tRed = tGreen = tBlue = 1.0F;
        float f26 = 1.0F - Math.abs(ToSun.z) * 4F;
        if(f26 > 0.0F)
        {
            tGreen = 1.0F - 0.6F * f26;
            tBlue = 1.0F - 0.7F * f26;
        }
        ToLight.set(ToSun);
        ToLight.scale(f16 + 0.001F);
        tmpV3f.set(ToMoon);
        tmpV3f.scale(f14 + 0.001F);
        ToLight.add(tmpV3f);
        ToLight.normalize();
        if(ToLight.z < 0.05F)
        {
            tmpV2f.x = ToLight.x;
            tmpV2f.y = ToLight.y;
            tmpV2f.normalize();
            tmpV2f.scale(0.998749F);
            ToLight.x = tmpV2f.x;
            ToLight.y = tmpV2f.y;
            ToLight.z = 0.05F;
        }
        Red = tRed * f24 + (1.0F - 0.3F * f25) * f25;
        Green = tGreen * f24 + f25;
        Blue = tBlue * f24 + f25;
        float f27 = 1.0F / Math.max(Math.max(Red, Green), Blue);
        f27 *= 1.0F - (1.0F - lightAtAlt(altitude)) * 0.8F;
        Red *= f27;
        Green *= f27;
        Blue *= f27;
        if(ToSun.z < 0.0F)
            ToSun.z *= 1.0F - ToSun.z * 10F;
        ToSun.normalize();
        activate();
        
        // TODO: Ultrapack backward compatibility!
        if (Config.isUSE_RENDER()) {
            float moonPhaseFactor = 1.0F - 2.0F * Math.abs(moonPhase - 0.5F);
            darkness = 0.095F + moonPhaseFactor * 0.666F;
            sunMultiplier = cvt(ToSun.z, -0.6F, 0.0F, darkness, 1.0F);
        }
    }

    private static native void setNative(float f, float f1, float f2, float f3, float f4, float f5, float f6, float f7, 
            float f8);

    public float lightAtAlt(float f)
    {
        if(f > clHigh)
            return aiHigh;
        if(f < clLow)
            return aiLow;
        else
            return cvt(f, clLow, clHigh, aiLow, aiHigh);
    }

    public boolean needsCubeUpdate(float f)
    {
        if(f > clHigh && h0 > clHigh || f < clLow && h0 < clLow)
            return false;
//        return true;
        return f - h0 > 30F || f - h0 < -30F;
    }

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
    private float tRed;
    private float tGreen;
    private float tBlue;
    public float moonPhase;
    private static GregorianCalendar missionDate = null;
    private float tod;
    private static float DEG2RAD = 0.01745329F;
    private Vector3f tmpV3f;
    private Vector2f tmpV2f;
    float aiLow;
    float aiHigh;
    float clLow;
    float clHigh;
    private float h0;
    public float darkness;
    public float sunMultiplier;

}
