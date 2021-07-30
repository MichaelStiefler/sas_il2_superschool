package com.maddox.il2.engine;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.maddox.JGP.Vector2f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;

public class Sun {

    public Sun() {
        this.ToLight = new Vector3f();
        this.SunV = new Vector3f();
        this.ToSun = new Vector3f();
        this.ToMoon = new Vector3f();
        this.moonPhase = 0.5F;
        this.tod = 0.0F;
        this.darkness = 1.0F;
        this.sunMultiplier = 1.0F;
        this.tmpV3f = new Vector3f();
        this.tmpV2f = new Vector2f();
        this.aiLow = 1.0F;
        this.aiHigh = 1.0F;
        this.clLow = -1F;
        this.clHigh = -1F;
        this.SunV.set(0.0F, 0.0F, -1F);
        this.ToSun.set(this.SunV);
        this.ToSun.negate();
        this.ToMoon.set(this.ToSun);
        this.ToMoon.negate();
        this.ToLight.set(this.ToSun);
        this.Ambient = 0.5F;
        this.Diffuze = 0.5F;
        this.Specular = 1.0F;
        this.Red = 1.0F;
        this.Green = 1.0F;
        this.Blue = 1.0F;
    }

    public void activate() {
        if (Config.isUSE_RENDER()) setNative(this.ToLight.x, this.ToLight.y, this.ToLight.z, this.Ambient, this.Diffuze, this.Specular, this.Red, this.Green, this.Blue);
    }

    public void set(Vector3f vector3f) {
        this.SunV.set(vector3f);
        this.ToSun.set(this.SunV);
        this.ToSun.negate();
        this.ToLight.set(this.ToSun);
    }

    public void setLight(float f, float f1, float f2, float f3, float f4, float f5) {
        this.Ambient = f;
        this.Diffuze = f1;
        this.Specular = f2;
        this.Red = f3;
        this.Green = f4;
        this.Blue = f5;
    }

    private static float zLight(float f) {
        if (f > -0.104F) return 12.9364F - 1.43485F / (f + 0.2275954F);
        if (f > -0.31F) return 0.4315F + f * (-132.66F + f * (-1830.9F + f * (-6972.2F + f * -8738.5F)));
        else return -1E+009F;
    }

    public static float cvt(float inValue, float inMin, float inMax, float outMin, float outMax) {
        inValue = Math.min(Math.max(inValue, inMin), inMax);
        return outMin + (outMax - outMin) * (inValue - inMin) / (inMax - inMin);
    }

    public static float cvtsin1(float inValue, float inMin, float inMax, float outMin, float outMax) {
        inValue = Math.min(Math.max(inValue, inMin), inMax);
        float radians = (inValue - inMin) / (inMax - inMin) * (float) Math.PI / 2F;
        return outMin + (outMax - outMin) * (float) Math.sin(radians);
    }

    public void resetCalendar() {
        missionDate = null;
    }

    public void setAstronomic(int declination, int month, int day, float timeOfDay, float altitude) {
        float declinationRadians = (90 - declination) * DEG2RAD;
        float declinationCosinus = (float) Math.cos(declinationRadians);
        float declinationSinus = (float) Math.sin(declinationRadians);
        float dayRadians = (month * 30 + day - 80) * DEG2RAD;
        float timeOfDayRadians = (float) Math.PI * timeOfDay / 12F;
        float timeOfDaySinus = (float) Math.sin(timeOfDayRadians);
        float timeOfDayCosinus = (float) Math.cos(timeOfDayRadians);
        float elevationRadians = (float) Math.sin((float) Math.PI / 8F * (float) Math.sin(dayRadians));
        this.ToSun.x = timeOfDaySinus;
        this.ToSun.y = timeOfDayCosinus * declinationCosinus + elevationRadians * declinationSinus;
        this.ToSun.z = elevationRadians * declinationCosinus - timeOfDayCosinus * declinationSinus;
        this.ToSun.normalize();
        this.SunV.x = -this.ToSun.x;
        this.SunV.y = -this.ToSun.y;
        this.SunV.z = -this.ToSun.z;
        int gregYear = Mission.curYear();
        int gregHour = (int) Math.floor(timeOfDay);
        int gregMinute = (int) ((timeOfDay - gregHour) * 60F);
        int gregSecond = 0;
        if (missionDate == null) missionDate = new GregorianCalendar(gregYear, month - 1, day, gregHour, gregMinute, gregSecond);
        else if (timeOfDay != this.tod) missionDate.roll(Calendar.SECOND, true);
        this.tod = timeOfDay;
        double moonPhaseArray[] = new double[10];
        moonPhaseArray = MoonPhase.phase(missionDate);
        this.moonPhase = (float) moonPhaseArray[0];
        dayRadians = (month * 30 + day - 80) * DEG2RAD + (float) Math.PI * 2F * this.moonPhase;
        timeOfDayRadians = (float) Math.PI * 2F * (-this.moonPhase + timeOfDay / 24F);
        timeOfDaySinus = (float) Math.sin(timeOfDayRadians);
        timeOfDayCosinus = (float) Math.cos(timeOfDayRadians);
        elevationRadians = (float) Math.sin((float) Math.PI / 8F * (float) Math.sin(dayRadians));
        this.ToMoon.x = timeOfDaySinus;
        this.ToMoon.y = timeOfDayCosinus * declinationCosinus + elevationRadians * declinationSinus;
        this.ToMoon.z = elevationRadians * declinationCosinus - timeOfDayCosinus * declinationSinus;
        this.ToMoon.normalize();
        float f14 = 0.0F;
        if (this.ToMoon.z > -0.31F) {
            float f11 = zLight(this.ToMoon.z) - 11.76757F - 1.066181F;
            float f15 = 1F - 2.0F * Math.abs(this.moonPhase - 0.5F);
            f11 += -0.01F / f15 - 5.225F + f15 * 5.235F;
            f14 = (float) Math.exp(f11);
        }
        float f16 = 0.0F;
        if (this.ToSun.z > -0.31F) {
            float f13 = zLight(this.ToSun.z);
            f16 = (float) Math.exp(f13);
        }
        float f17 = (float) Math.log(f14 + f16 + 0.001076F);
        float f18 = 0.0F;
        EffClouds effclouds = Main.cur().clouds;
        if (effclouds != null && effclouds.type() > 2) {
            float cloudsHeight = effclouds.height();
            float cloudsFactor1 = 0.0F;
            switch (effclouds.type()) {
                case 3:
                    this.clLow = cloudsHeight + 200F;
                    this.clHigh = cloudsHeight + 1000F;
                    cloudsFactor1 = 4.8F;
                    break;

                case 4:
                    this.clLow = cloudsHeight + 200F;
                    this.clHigh = cloudsHeight + 1600F;
                    cloudsFactor1 = 8F;
                    break;

                case 5:
                    this.clLow = cloudsHeight + 100F;
                    this.clHigh = cloudsHeight + 1900F;
                    cloudsFactor1 = 12.8F;
                    break;

                case 6:
                    this.clLow = cloudsHeight;
                    this.clHigh = cloudsHeight + 1200F;
                    cloudsFactor1 = 16F;
                    break;
            }
            f18 = cvt(altitude, this.clLow, this.clHigh, cloudsFactor1, 0.0F);
        }
        float f20 = f17 - f18;
        this.aiLow = (f20 >= 0.0F ? f20 : f20 / 6F) * 0.11F + 0.211F;
        this.aiLow = this.aiLow >= 0.0F ? this.aiLow <= 1.0F ? this.aiLow : 1.0F : 0.0F;
        this.aiHigh = (f17 >= 0.0F ? f17 : f17 / 6F) * 0.11F + 0.211F;
        this.aiHigh = this.aiHigh >= 0.0F ? this.aiHigh <= 1.0F ? this.aiHigh : 1.0F : 0.0F;
        this.h0 = altitude;
        f20 = this.aiLow;
        float f22 = f20;
        float f23 = 0.0F;
        this.Specular = f22;
        f23 += f22 * (f18 / 16F * 0.4F + 0.3F);
        f22 -= f23;
        this.Diffuze = f22 * 1.4285F;
        this.Ambient = f23 * 1.4285F;

        // TODO: +++ Moonlight Customization Mod by SAS~Storebror +++
        float DiffuzeMax = cvtsin1(this.ToSun.z, -0.2F, 0.0F, 0.3F, 0.9F);
        if (this.ToSun.z < 0F && this.Diffuze < DiffuzeMax) {
            float moonPhaseDiff = cvt(Math.abs(0.5F - this.moonPhase), 0.0F, 0.5F, 1.0F, 0.25F) * Config.cur.iDiffuse / Config.MAX_NIGHT_SETTINGS;
            this.Diffuze = cvt(moonPhaseDiff + this.Diffuze, 0.35F, 1.0F, 0.20F, DiffuzeMax);
        }
        // TODO: --- Moonlight Customization Mod by SAS~Storebror ---

        if (this.Ambient < 0.001F) this.Ambient = 0.001F;
        if (this.Specular < 0.01F) this.Specular = 0.01F;
        if (this.Diffuze < 0.001F) this.Diffuze = 0.001F;

        float f24 = f16 / (f14 + f16 + 0.001076F);
        float f25 = 1.0F - f24;
        this.tRed = this.tGreen = this.tBlue = 1.0F;
        float f26 = 1.0F - Math.abs(this.ToSun.z) * 4F;
        if (f26 > 0.0F) {
            this.tGreen = 1.0F - 0.6F * f26;
            this.tBlue = 1.0F - 0.7F * f26;
        }
        this.ToLight.set(this.ToSun);
        this.ToLight.scale(f16 + 0.001F);
        this.tmpV3f.set(this.ToMoon);
        this.tmpV3f.scale(f14 + 0.001F);
        this.ToLight.add(this.tmpV3f);
        this.ToLight.normalize();
        if (this.ToLight.z < 0.05F) {
            this.tmpV2f.x = this.ToLight.x;
            this.tmpV2f.y = this.ToLight.y;
            this.tmpV2f.normalize();
            this.tmpV2f.scale(0.998749F);
            this.ToLight.x = this.tmpV2f.x;
            this.ToLight.y = this.tmpV2f.y;
            this.ToLight.z = 0.05F;
        }
        this.Red = this.tRed * f24 + (1.0F - 0.3F * f25) * f25;
        this.Green = this.tGreen * f24 + f25;
        this.Blue = this.tBlue * f24 + f25;
        float f27 = 1.0F / Math.max(Math.max(this.Red, this.Green), this.Blue);
        f27 *= 1.0F - (1.0F - this.lightAtAlt(altitude)) * 0.8F;
        this.Red *= f27;
        this.Green *= f27;
        this.Blue *= f27;
        if (this.ToSun.z < 0.0F) this.ToSun.z *= 1.0F - this.ToSun.z * 10F;
        this.ToSun.normalize();
        this.activate();

        // TODO: Ultrapack backward compatibility!
        if (Config.isUSE_RENDER()) {
            float moonPhaseFactor = 1.0F - 2.0F * Math.abs(this.moonPhase - 0.5F);
            this.darkness = 0.095F + moonPhaseFactor * 0.666F;
            this.sunMultiplier = cvt(this.ToSun.z, -0.6F, 0.0F, this.darkness, 1.0F);
        }
    }

    public void setAstronomicOld(int declination, int month, int day, float timeOfDay, float altitude) {
        float declinationRadians = (90 - declination) * DEG2RAD;
        float declinationCosinus = (float) Math.cos(declinationRadians);
        float declinationSinus = (float) Math.sin(declinationRadians);
        float dayRadians = (month * 30 + day - 80) * DEG2RAD;
        float timeOfDayRadians = (float) Math.PI * timeOfDay / 12F;
        float timeOfDaySinus = (float) Math.sin(timeOfDayRadians);
        float timeOfDayCosinus = (float) Math.cos(timeOfDayRadians);
        float elevationRadians = (float) Math.sin((float) Math.PI / 8F * (float) Math.sin(dayRadians));
        this.ToSun.x = timeOfDaySinus;
        this.ToSun.y = timeOfDayCosinus * declinationCosinus + elevationRadians * declinationSinus;
        this.ToSun.z = elevationRadians * declinationCosinus - timeOfDayCosinus * declinationSinus;
        this.ToSun.normalize();
        this.SunV.x = -this.ToSun.x;
        this.SunV.y = -this.ToSun.y;
        this.SunV.z = -this.ToSun.z;
        int gregYear = Mission.curYear();
        int gregHour = (int) Math.floor(timeOfDay);
        int gregMinute = (int) ((timeOfDay - gregHour) * 60F);
        int gregSecond = 0;
        if (missionDate == null) missionDate = new GregorianCalendar(gregYear, month - 1, day, gregHour, gregMinute, gregSecond);
        else if (timeOfDay != this.tod) missionDate.roll(Calendar.SECOND, true);
        this.tod = timeOfDay;
        double moonPhaseArray[] = new double[10];
        moonPhaseArray = MoonPhase.phase(missionDate);
        this.moonPhase = (float) moonPhaseArray[0];
        timeOfDayRadians = (float) Math.PI * 2F * (-this.moonPhase + timeOfDay / 24F);
        timeOfDaySinus = (float) Math.sin(timeOfDayRadians);
        timeOfDayCosinus = (float) Math.cos(timeOfDayRadians);
        this.ToMoon.x = timeOfDaySinus;
        this.ToMoon.y = timeOfDayCosinus * declinationCosinus + elevationRadians * declinationSinus;
        this.ToMoon.z = elevationRadians * declinationCosinus - timeOfDayCosinus * declinationSinus;
        this.ToMoon.normalize();
        float f14 = 0.0F;
        if (this.ToMoon.z > -0.31F) {
            float f11 = zLight(this.ToMoon.z) - 11.76757F - 1.066181F;
            float f15 = 1F - 2.0F * Math.abs(this.moonPhase - 0.5F);
            f11 += -0.01F / f15 - 5.225F + f15 * 5.235F;
            f14 = (float) Math.exp(f11);
        }
        float f16 = 0.0F;
        if (this.ToSun.z > -0.31F) {
            float f13 = zLight(this.ToSun.z);
            f16 = (float) Math.exp(f13);
        }
        float f17 = (float) Math.log(f14 + f16 + 0.001076F);
        float f18 = 0.0F;
        EffClouds effclouds = Main.cur().clouds;
        if (effclouds != null && effclouds.type() > 2) {
            float cloudsHeight = effclouds.height();
            float cloudsFactor1 = 0.0F;
            switch (effclouds.type()) {
                case 3:
                    this.clLow = cloudsHeight + 200F;
                    this.clHigh = cloudsHeight + 1000F;
                    cloudsFactor1 = 4.8F;
                    break;

                case 4:
                    this.clLow = cloudsHeight + 200F;
                    this.clHigh = cloudsHeight + 1600F;
                    cloudsFactor1 = 8F;
                    break;

                case 5:
                    this.clLow = cloudsHeight + 100F;
                    this.clHigh = cloudsHeight + 1900F;
                    cloudsFactor1 = 12.8F;
                    break;

                case 6:
                    this.clLow = cloudsHeight;
                    this.clHigh = cloudsHeight + 1200F;
                    cloudsFactor1 = 16F;
                    break;
            }
            f18 = cvt(altitude, this.clLow, this.clHigh, cloudsFactor1, 0.0F);
        }
        float f20 = f17 - f18;
        this.aiLow = (f20 >= 0.0F ? f20 : f20 / 6F) * 0.11F + 0.211F;
        this.aiLow = this.aiLow >= 0.0F ? this.aiLow <= 1.0F ? this.aiLow : 1.0F : 0.0F;
        this.aiHigh = (f17 >= 0.0F ? f17 : f17 / 6F) * 0.11F + 0.211F;
        this.aiHigh = this.aiHigh >= 0.0F ? this.aiHigh <= 1.0F ? this.aiHigh : 1.0F : 0.0F;
        this.h0 = altitude;
        f20 = this.aiLow;
        float f22 = f20;
        float f23 = 0.0F;
        this.Specular = f22;
        f23 += f22 * (f18 / 16F * 0.4F + 0.3F);
        f22 -= f23;
        this.Diffuze = f22 * 1.4285F;
        this.Ambient = f23 * 1.4285F;

        // TODO: +++ Moonlight Customization Mod by SAS~Storebror +++
        float DiffuzeMax = cvtsin1(this.ToSun.z, -0.2F, 0.0F, 0.3F, 0.9F);
        if (this.ToSun.z < 0F && this.Diffuze < DiffuzeMax) {
            float moonPhaseDiff = cvt(Math.abs(0.5F - this.moonPhase), 0.0F, 0.5F, 1.0F, 0.25F) * Config.cur.iDiffuse / Config.MAX_NIGHT_SETTINGS;
            this.Diffuze = cvt(moonPhaseDiff + this.Diffuze, 0.35F, 1.0F, 0.20F, DiffuzeMax);
        }
        // TODO: --- Moonlight Customization Mod by SAS~Storebror ---

        if (this.Ambient < 0.001F) this.Ambient = 0.001F;
        if (this.Specular < 0.01F) this.Specular = 0.01F;
        if (this.Diffuze < 0.001F) this.Diffuze = 0.001F;

        float f24 = f16 / (f14 + f16 + 0.001076F);
        float f25 = 1.0F - f24;
        this.tRed = this.tGreen = this.tBlue = 1.0F;
        float f26 = 1.0F - Math.abs(this.ToSun.z) * 4F;
        if (f26 > 0.0F) {
            this.tGreen = 1.0F - 0.6F * f26;
            this.tBlue = 1.0F - 0.7F * f26;
        }
        this.ToLight.set(this.ToSun);
        this.ToLight.scale(f16 + 0.001F);
        this.tmpV3f.set(this.ToMoon);
        this.tmpV3f.scale(f14 + 0.001F);
        this.ToLight.add(this.tmpV3f);
        this.ToLight.normalize();
        if (this.ToLight.z < 0.05F) {
            this.tmpV2f.x = this.ToLight.x;
            this.tmpV2f.y = this.ToLight.y;
            this.tmpV2f.normalize();
            this.tmpV2f.scale(0.998749F);
            this.ToLight.x = this.tmpV2f.x;
            this.ToLight.y = this.tmpV2f.y;
            this.ToLight.z = 0.05F;
        }
        this.Red = this.tRed * f24 + (1.0F - 0.3F * f25) * f25;
        this.Green = this.tGreen * f24 + f25;
        this.Blue = this.tBlue * f24 + f25;
        float f27 = 1.0F / Math.max(Math.max(this.Red, this.Green), this.Blue);
        f27 *= 1.0F - (1.0F - this.lightAtAlt(altitude)) * 0.8F;
        this.Red *= f27;
        this.Green *= f27;
        this.Blue *= f27;
        if (this.ToSun.z < 0.0F) this.ToSun.z *= 1.0F - this.ToSun.z * 10F;
        this.ToSun.normalize();
        this.activate();

        // TODO: Ultrapack backward compatibility!
        if (Config.isUSE_RENDER()) {
            float moonPhaseFactor = 1.0F - 2.0F * Math.abs(this.moonPhase - 0.5F);
            this.darkness = 0.095F + moonPhaseFactor * 0.666F;
            this.sunMultiplier = cvt(this.ToSun.z, -0.6F, 0.0F, this.darkness, 1.0F);
        }
    }

    private static native void setNative(float f, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    public float lightAtAlt(float f) {
        if (f > this.clHigh) return this.aiHigh;
        if (f < this.clLow) return this.aiLow;
        else return cvt(f, this.clLow, this.clHigh, this.aiLow, this.aiHigh);
    }

    public boolean needsCubeUpdate(float f) {
        if (f > this.clHigh && this.h0 > this.clHigh || f < this.clLow && this.h0 < this.clLow) return false;
//        return true;
        return f - this.h0 > 30F || f - this.h0 < -30F;
    }

    public Vector3f                  ToLight;
    public Vector3f                  SunV;
    public Vector3f                  ToSun;
    public Vector3f                  ToMoon;
    public float                     Ambient;
    public float                     Diffuze;
    public float                     Specular;
    public float                     Red;
    public float                     Green;
    public float                     Blue;
    private float                    tRed;
    private float                    tGreen;
    private float                    tBlue;
    public float                     moonPhase;
    private static GregorianCalendar missionDate = null;
    private float                    tod;
    private static float             DEG2RAD     = 0.01745329F;
    private Vector3f                 tmpV3f;
    private Vector2f                 tmpV2f;
    float                            aiLow;
    float                            aiHigh;
    float                            clLow;
    float                            clHigh;
    private float                    h0;
    public float                     darkness;
    public float                     sunMultiplier;

}
