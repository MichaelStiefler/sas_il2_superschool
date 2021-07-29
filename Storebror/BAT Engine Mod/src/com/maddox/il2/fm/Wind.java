/*
 * Wind - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 * 4.12, enhanced by WxTech
 * disabled the land/water and time-of-day transition effect on pure lift; it resulted in the infamous 'shoreline crossing jump'
 * disabled the extra lift when temp > 24C
 * reduced amplitude of basic turbulence to 1/4
 * disabled gusts altogether (awful implementation, an occur with clockwork regularity)!
 * increased mechanical turbulence depth to 600m AGL; 1/2 strength at sfc, max strength at 200m, going to zero at 600m AGL
 * increased 10m deep zone at surface to 30m, and made wind drop to zero at surface so as to overcome awful weathercocking
 * added cloud zone turbulence intensity based on cloud/weather type; relative to max mechanical turbulence, varies from 0.3 to 1.4, from Good to Thunder
 * incorporated into the mechanical turbulence a sinusoidal diurnal variation; max at 1430hrs, 1/2 strength at 0230 hrs
 */

// Referenced classes of package com.maddox.il2.fm:
// FMMath, Atmosphere

package com.maddox.il2.fm;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.EffClouds;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.Main;					// NEW

public class Wind extends FMMath {
    Vector3f       steady        = new Vector3f();
    float          top;				//I *suspect* it's the cloud base height + some small addition (300m?). Replace with effclouds.height() ?
    float          turbulence;
    float          gust;
    float          velocityTop;
    float          velocityTrans = 0.0F;  //at TransitionAlt?
    float          wTransitionAlt;
    float          velocity10m;  //the lowest transition threshold AGL, in which wind speed drops at surface to 1/3 (now 30m deep and zero wind at surface)
    float          velocityH;	//slope in high zone (above top)? Or value at bottom of top zone?
    float          velocityM;	//slope in middle zone? Or value at bottom of middle zone?
    float          velocityL;	//slope in low zone? Or value at bottom of low zone?
    float          dir;
    public boolean noWind        = false;
    int            hdg;

    public native float SetWind(float f, float f_0_, float f_1_, float f_2_, float f_3_);

    public void set(float f, float f_4_, float f_5_, float f_6_, float f_7_) {
        setHDG(SetWind(f, f_4_, f_5_, f_6_, f_7_));
        steady.set((float) -Math.sin((double) dir), (float) -Math.cos((double) dir), 0.0F);	//sets vector opposite of wind's FROM direction?
        noWind = f_5_ == 0.0F;
    }

    public void setHDG(float f) {
        for (hdg = (int) f; hdg < 0; hdg += 360) {
            /* empty */
        }
        for (/**/; hdg >= 360; hdg -= 360) {
            /* empty */
        }
        hdg /= 30;
    }

    public int getHDG() {
        return hdg;
    }

    public int getSpeed() {
        return (int) velocityL;
    }

    public void getVector(Point3d point3d, Vector3d vector3d) {
        float f = (float) Engine.cur.land.HQ(point3d.x, point3d.y);		//elevation of surface under actor
        float f_8_ = (float) (point3d.z - (double) f);					//height above surface (AGL)
//        float f_9_ = 1.0F - f_8_ / top;									//fractional height surface to top; 1 = sfc, 0 = top
        vector3d.set(steady);
        vector3d.scale((double) windVelocity(f_8_));
        EffClouds effclouds = Main.cur().clouds;  //NEW

/*	    if (gust > 0.0F) {  //added a new scaling factor of 0.1 below
		if (gust > 7.0F) {
		    float f_10_ = (float) Math.sin((double) (0.0050F * (float) Time.current() / 6.0F));
		    if (f_10_ > 0.75F)  //arcsin(rad) 0.75 = 0.6816; therefore if Time.current > 817.9 (0.8179s?)
			vector3d.scale((double) ((0.25F + f_10_) * 0.1F));	//1 or more
		}
		if (gust > 11.0F) {
		    float f_11_ = (float) Math.sin((double) (0.0050F * (float) Time.current() / 14.2F));
		    if (f_11_ > 0.16F)  //arcsin(rad) 0.16 = 0.1593; therefore if Time.current > 452.4 (0.4524s?)
			vector3d.scale((double) ((0.872F + f_11_ * 0.8F) * 0.1F));	//0.8256 or more
		}
		if (gust > 9.0F) {
		    float f_12_ = (float) Math.sin((double) (0.0050F * (float) Time.current() / 39.84F));
		    if (f_12_ > 0.86F)  //arcsin(rad) 0.86 = 0.7578; therefore if Time.current > 6038 (6.038s?)
			vector3d.scale((double) ((0.14F + f_12_) * 0.1F));	//1 or more
		}
		if (gust > 9.0F) {
		    float f_13_ = (float) Math.sin((double) (0.0050F * (float) Time.current() / 12.3341F));
		    if (f_13_ > 0.5F)  //arcsin(rad) 0.5 = 0.4794; therefore if Time.current > 1182.59 (1.183s?)
			vector3d.scale((double) ((1.0F + f_13_ * 0.5F) * 0.1F));	//0.75 or more
		}
}  */

/*	    if (Engine.land().isWater(point3d.x, point3d.y)) {  //this causes the annoying 'shoreline crossing jump'!
		Vector3d vector3d_14_ = vector3d;
		vector3d_14_.z = (vector3d_14_.z + (2.119999885559082 * (point3d.z > 250.0 ? 1.0 : point3d.z / 250.0)
			  * (double) (float) (Math.cos((double) (World.getTimeofDay() * 2.0F * 3.1415927F * 0.04166666F)))));
}	*/

/*	    if (Atmosphere.temperature(0.0F) > 297.0F)		//if > 24C...
		vector3d.z += (double) (1.0F * f_9_);			//...add 0 to 1 (presumably m/s)
vector3d.z *= (double) f_9_;					//always multiply by fractional height AGL (0 to 1)  */

/*	    if (f_8_ < 1000.0F && f > 999.0F) {		//if AGL < 1000m and terrain elevation > 999m, i.e., over mountains
		float f_15_ = Math.abs(f_8_ - 500.0F) * 0.0020F;  //f_15_ is 0 @500m AGL; ODD!!!
		f_15_ *= (float) (Math.sin((double) (0.0050F * (float) Time.current() / 13.899745F)) + Math.sin((double) (0.0050F * (float) Time.current() / 9.6F)) + Math.sin((double) (0.0050F * (float) Time.current() / 2.112F)));
		if (f_15_ > 0.0F)
		    vector3d.scale((double) (1.0F + f_15_));
}  */

        if (turbulence > 0F && f_8_ < 600.0F) {		//mechanical turbulence component; factor of 0.25 to make turb 1/4 the stock value
            float f_16_ = turbulence * 0.25F * (1F - (Math.abs(f_8_ - 200F)) + (600F - 200F)) / (600F - 200F) * ((float) (Math.cos((double) ((World.getTimeofDay() - 2.5F) * 2.0F * 3.1415927F * 0.04166666F))) * -0.25F + 0.75F);  //peak turbulence at 14:30
            if (Engine.land().isWater(point3d.x, point3d.y))
                f_16_ *= 0.67F;  //mech turb always 2/3 as strong over water as it is over land
            vector3d.add((double) World.Rnd().nextFloat(-f_16_, f_16_), (double) World.Rnd().nextFloat(-f_16_, f_16_), (double) World.Rnd().nextFloat(-f_16_, f_16_));
        }

        float f_161_ = (float) (effclouds.height() + (effclouds.type() - 1F) * 900F + 1500F);  //height cloud turbulence zone; cloud type 0-6: 600-6000m above base
        if (effclouds != null && effclouds.type() > 0 && point3d.z < f_161_) {
            float f_17_ = turbulence * 0.25F * ((1F - (Math.abs((float) point3d.z - f_161_ / 2F)) + f_161_ - f_161_ / 2F) / (f_161_ - f_161_ / 2F)) * ((effclouds.type() + 1F) / 4.5F - 0.15F);  // for CLD type 1 to 6: factor of ~0.3 to 1.4 (cld turb 30-140% as strong as peak mech turb)
            vector3d.add((double) World.Rnd().nextFloat(-f_17_, f_17_), (double) World.Rnd().nextFloat(-f_17_, f_17_), (double) World.Rnd().nextFloat(-f_17_, f_17_));
        }

    }

    public void getVectorAI(Point3d point3d, Vector3d vector3d) {
        float f = (float) Engine.cur.land.HQ(point3d.x, point3d.y);
        float f_18_ = (float) (point3d.z - (double) f);							//height above surface
        vector3d.set(steady);
        vector3d.scale((double) windVelocity(f_18_));
        EffClouds effclouds = Main.cur().clouds;  //NEW
//ADDED NEW, to get turbulence for AI
        if (turbulence > 0F && f_18_ < 600.0F) {
            float f_16_ = turbulence * 0.25F * (1F - (Math.abs(f_18_ - 200F)) + (600F - 200F)) / (600F - 200F) * ((float) (Math.cos((double) ((World.getTimeofDay() - 2.5F) * 2.0F * 3.1415927F * 0.04166666F))) * -0.25F + 0.75F);
            if (Engine.land().isWater(point3d.x, point3d.y))
                f_16_ *= 0.67F;
            vector3d.add((double) World.Rnd().nextFloat(-f_16_, f_16_), (double) World.Rnd().nextFloat(-f_16_, f_16_), (double) World.Rnd().nextFloat(-f_16_, f_16_));
        }
        float f_161_ = (float) (effclouds.height() + (effclouds.type() - 1F) * 900F + 1500F);
        if (effclouds != null && effclouds.type() > 0 && point3d.z < f_161_) {
            float f_17_ = turbulence * 0.25F * ((1F - (Math.abs((float) point3d.z - f_161_ / 2F)) + f_161_ - f_161_ / 2F) / (f_161_ - f_161_ / 2F)) * ((effclouds.type() + 1F) / 4.5F - 0.15F);
            vector3d.add((double) World.Rnd().nextFloat(-f_17_, f_17_), (double) World.Rnd().nextFloat(-f_17_, f_17_), (double) World.Rnd().nextFloat(-f_17_, f_17_));
        }
    }

    public void getVectorWeapon(Point3d point3d, Vector3d vector3d) {
        float f = (float) Engine.cur.land.HQ(point3d.x, point3d.y);
        float f_19_ = (float) (point3d.z - (double) f);							//height above surface
        vector3d.set(steady);
        vector3d.scale((double) windVelocity(f_19_));
    }

    public float windVelocity(float f) {	//f = current altitude above ground?
        float f_20_ = 0.0F;
        if (f > top)
            f_20_ = velocityTop + (f - top) * velocityH;
        else if (f > wTransitionAlt)
            f_20_ = velocityTrans + (f - wTransitionAlt) * velocityM;
        else if (f > 30.0F)  //make formerly 10m deep zone 30m deep; more gradual transition
            f_20_ = velocity10m + velocityL * f;
        if (f <= 30.0F)
            f_20_ = velocity10m * f / 30F;  //originally (f + 5) / 15 = velocity on surface 1/3 that at 10m; now wind zero at surface (to nullify awful weathercocking!)
        return f_20_;
    }
    
    // TODO: Added by SAS~Storebror to make this class compatible at least with existing game code! +++
    
    private float curWindDirection;
    private float curWindVelocity;
    private float curGust;
    private float curTurbulence;

    public float curWindDirection()
    {
      return this.curWindDirection;
    }
    
    public float curWindVelocity()
    {
      return this.curWindVelocity;
    }
    
    public float curGust()
    {
      return this.curGust;
    }
    
    public float curTurbulence()
    {
      return this.curTurbulence;
    }
    
    public void setWindDirection(float f)
    {
      if (f < 0.0F) {
        f = 0.0F;
      }
      if (f > 359.98999F) {
        f = 0.0F;
      }
      this.curWindDirection = f;
    }
    
    public void setWindVelocity(float f)
    {
      if (f < 0.0F) {
        f = 0.0F;
      }
      if (f > 15.0F) {
        f = 15.0F;
      }
      this.curWindVelocity = f;
    }
    
    public void setGust(float f)
    {
      if (f < 0.0F) {
        f = 0.0F;
      }
      if (f > 12.0F) {
        f = 12.0F;
      }
      this.curGust = f;
    }
    
    public void setTurbulence(float f)
    {
      if (f < 0.0F) {
        f = 0.0F;
      }
      if (f > 6.0F) {
        f = 6.0F;
      }
      this.curTurbulence = f;
    }

    
    // ---
}
