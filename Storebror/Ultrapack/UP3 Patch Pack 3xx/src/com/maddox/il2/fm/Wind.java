/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.fm;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.rts.Time;

public class Wind extends FMMath {

    public Wind() {
        this.steady = new Vector3f();
        this.velocityTrans = 0.0F;
        this.noWind = false;
    }

    public native float SetWind(float f, float f1, float f2, float f3, float f4);

    public void set(float f, float f1, float f2, float f3, float f4) {
        this.setHDG(this.SetWind(f, f1, f2, f3, f4));
        this.steady.set((float) (-Math.sin(this.dir)), (float) (-Math.cos(this.dir)), 0.0F);
        this.noWind = f2 == 0.0F;
    }

    public void setHDG(float f) {
        for (this.hdg = (int) f; this.hdg < 0; this.hdg += 360)
            ;
        for (; this.hdg >= 360; this.hdg -= 360)
            ;
        this.hdg /= 30;
    }

    public int getHDG() {
        return this.hdg;
    }

    public int getSpeed() {
        return (int) this.velocityL;
    }

    public void getVector(Point3d point3d, Vector3d vector3d) {
        float f = (float) Engine.cur.land.HQ(point3d.x, point3d.y);
        float f1 = (float) (point3d.z - f);
        float f2 = 1.0F - f1 / this.top;
        vector3d.set(this.steady);
        vector3d.scale(this.windVelocity(f1));
        if (f1 > this.top)
            return;
        if (this.gust > 0.0F) {
            if (this.gust > 7F) {
                float f3 = (float) Math.sin((0.005F * Time.current()) / 6F);
                if (f3 > 0.75F)
                    vector3d.scale(0.25F + f3);
            }
            if (this.gust > 11F) {
                float f4 = (float) Math.sin((0.005F * Time.current()) / 14.2F);
                if (f4 > 0.16F)
                    vector3d.scale(0.872F + f4 * 0.8F);
            }
            if (this.gust > 9F) {
                float f5 = (float) Math.sin((0.005F * Time.current()) / 39.84F);
                if (f5 > 0.86F)
                    vector3d.scale(0.14F + f5);
            }
            if (this.gust > 9F) {
                float f6 = (float) Math.sin((0.005F * Time.current()) / 12.3341F);
                if (f6 > 0.5F)
                    vector3d.scale(1.0F + f6 * 0.5F);
            }
        }
        if (Engine.land().isWater(point3d.x, point3d.y))
            vector3d.z += 2.119999885559082D * (point3d.z <= 250D ? point3d.z / 250D : 1.0D) * (float) Math.cos(World.getTimeofDay() * 2.0F * 3.141593F * 0.04166666F);
        if (Atmosphere.temperature(0.0F) > 297F)
            vector3d.z += 1.0F * f2;
        vector3d.z *= f2;
        if (f1 < 1000F && f > 999F) {
            float f7 = Math.abs(f1 - 500F) * 0.002F;
            f7 *= (float) (Math.sin((0.005F * Time.current()) / 13.89974F) + Math.sin((0.005F * Time.current()) / 9.6F) + Math.sin((0.005F * Time.current()) / 2.112F));
            if (f7 > 0.0F)
                vector3d.scale(1.0F + f7);
        }
        if (this.turbulence > 2.0F && f1 < 300F) {
            float f8 = (this.turbulence * f1) / 300F;
            vector3d.add(World.Rnd().nextFloat(-f8, f8), World.Rnd().nextFloat(-f8, f8), World.Rnd().nextFloat(-f8, f8));
        }
        if (this.turbulence > 4F && point3d.z > this.top - 400F && point3d.z < this.top) {
            float f9 = Math.abs(this.top - 200F - (float) point3d.z) * 0.0051F * this.turbulence;
            vector3d.add(World.Rnd().nextFloat(-f9, f9), World.Rnd().nextFloat(-f9, f9), World.Rnd().nextFloat(-f9, f9));
        }
    }

    public void getVectorAI(Point3d point3d, Vector3d vector3d) {
        float f = (float) Engine.cur.land.HQ(point3d.x, point3d.y);
        float f1 = (float) (point3d.z - f);
        vector3d.set(this.steady);
        vector3d.scale(this.windVelocity(f1));
    }

    public void getVectorWeapon(Point3d point3d, Vector3d vector3d) {
        float f = (float) Engine.cur.land.HQ(point3d.x, point3d.y);
        float f1 = (float) (point3d.z - f);
        vector3d.set(this.steady);
        vector3d.scale(this.windVelocity(f1));
    }

    public float windVelocity(float f) {
        float f1 = 0.0F;
        if (f > this.top)
            f1 = this.velocityTop + (f - this.top) * this.velocityH;
        else if (f > this.wTransitionAlt)
            f1 = this.velocityTrans + (f - this.wTransitionAlt) * this.velocityM;
        else if (f > 10F)
            f1 = this.velocity10m + this.velocityL * f;
        if (f <= 10F)
            f1 = (this.velocity10m * (f + 5F)) / 15F;
        return f1;
    }

    Vector3f       steady;
    float          top;
    float          turbulence;
    float          gust;
    float          velocityTop;
    float          velocityTrans;
    float          wTransitionAlt;
    float          velocity10m;
    float          velocityH;
    float          velocityM;
    float          velocityL;
    float          dir;
    public boolean noWind;
    int            hdg;
}
