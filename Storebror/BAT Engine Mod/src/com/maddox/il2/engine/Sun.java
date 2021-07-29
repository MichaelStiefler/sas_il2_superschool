package com.maddox.il2.engine;

import com.maddox.JGP.Vector2f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import java.util.GregorianCalendar;

// -----------------------------------------------------------
// WxTech's 4.12 Sun and Wind classfiles
// https://www.sas1946.com/main/index.php/topic,63935.0.html
// -----------------------------------------------------------

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
/*		MoonPhase = 0.5F;		//NEW - to get proper graphic <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  */
        
        // TODO: By SAS~Storebror: Darker Nights Backport +++
        darkness = 1F;
        sunMultiplier = 1F;
        // TODO: By SAS~Storebror: Darker Nights Backport ---
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
        if(f > -0.104F)																				// -6 degrees = civil twilight
            return 12.9364F - 1.43485F / (f + 0.2275954F);											// > -6 deg: 1.327 to 11.767; 0 deg is 6.632 
        if(f > -0.31F)																				// -18 degrees = astronomical twilight
            return 0.4315F + f * (-132.66F + f * (-1830.9F + f * (-6972.2F + f * -8738.5F)));		// > -18 deg: -7.386 to 1.245
/*	    return (1.4F + f * (-132.66F + f * (-1830.9F + f * (-7000F + f * -15000F))));  //makes darkness lighting decrease more  */
/*	    return (3.0F + f * (-132.66F + f * (-1830.9F + f * (-7000F + f * -25000F))));  //makes darkness lighting decrease even more still  */
        else
            return -1E+009F;																		/* could be -7.387 ??? */
    }

    public static float cvt(float f, float f1, float f2, float f3, float f4)
    {
        f = Math.min(Math.max(f, f1), f2);					//take largest of f and f1, then take smallest of that and f2
        return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
    }

    public void resetCalendar()
    {
        missionDate = null;
    }

    public void setAstronomic(int i, int j, int k, float f, float f1)		// i=decl(latitude), j=month, k=day, f=hour(decimal), f1=cloud height???
    {
        float f3 = (float)(90 - i) * DEG2RAD;								// zenith angle (rad) of NCP
        float f4 = (float)Math.cos(f3);
        float f5 = (float)Math.sin(f3);
        float f2 = (float)((j * 30 + k) - 80) * DEG2RAD;					// Sun long. (rad) relative to vernal equinox
        float f6 = (6.283185F * f) / 24F;									// Sun hour angle (rad) w.r.t midnight
        float f7 = (float)Math.sin(f6);
        float f8 = (float)Math.cos(f6);
        float f9 = (float)Math.sin(23.5F * DEG2RAD * (float)Math.sin(f2)); 	// Sun lat., original obliquity 22.5
        ToSun.x = f7;
        ToSun.y = f8 * f4 + f9 * f5;
        ToSun.z = f9 * f4 - f8 * f5;
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
/*        double ad[] = new double[10];   								// 7 variables (0-6) actually defined in MoonPhase.class */
        double ad[] = new double[7];
        ad = com.maddox.il2.engine.MoonPhase.phase(missionDate);
        moonPhase = (float)ad[0];										// New to Full to New: 0 to 0.5 to 1
/*		MoonPhase = moonPhase;											// NEW - to get proper graphic<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  */
        f2 = (float)((j * 30 + k) - 80) * DEG2RAD;						// Sun long. (rad) relative to vernal equinox
		f2 = f2 + 6.2831855F * moonPhase;								// NEW - actual Moon long (rad), so as to get the actual declination
        f6 = 6.283185F * (-moonPhase + f / 24F);						// hour angle (rad) of Moon w.r.t midnight
        f7 = (float)Math.sin(f6);
        f8 = (float)Math.cos(f6);
        f9 = (float)Math.sin(23.5F * DEG2RAD * (float)Math.sin(f2));	// Moon lat., original obliquity 22.5  */
        ToMoon.x = f7;
        ToMoon.y = f8 * f4 + f9 * f5;
        ToMoon.z = f9 * f4 - f8 * f5;
        ToMoon.normalize();
//        float f10 = -1E+009F;
//        float f12 = f10;
        float f14 = 0.0F;
        if(ToMoon.z > -0.31F)											// if Moon higher than -18 degrees
        {
            float f11 = zLight(ToMoon.z) - 11.76757F - 1.066181F;		// moonlight by angular height factor; zLight (-18d to +90d; -7.386 to 11.767, else -1E+009)
            float f15 = 1F - 2.0F * Math.abs(moonPhase - 0.5F);			// intrinsic moon lighting (New to Full, 0-1)
            f11 += (-0.01F / f15 - 5.225F) + f15 * 5.235F;				// add moon phase contribution
            f14 = (float)Math.exp(f11);									// 2.71828^f11
        }

        float f16 = 0.0F;
        if(ToSun.z > -0.31F)											// if Sun higher than -18 degrees
        {
            float f13 = zLight(ToSun.z);								// zLight = f13 = (-18d to +90d; -7.386 to 11.767, else -1E+009)
            f16 = (float)Math.exp(f13);									// 2.71828^f13
        }
        float f17 = (float)Math.log(f14 + f16 + 0.001076F);	 			// natural LOG, or ln(f14 + f16 + 0.001076F)
        float f18 = 0.0F;
        EffClouds effclouds = Main.cur().clouds;
		
		
        if(effclouds != null && effclouds.type() > 2)
        {
            float f19 = effclouds.height();
            float f21 = 0.0F;											//0.0 for clouds type 0, 1 and 2 (clear, good and hazy)
            switch(effclouds.type())
            {
            case 3: // '\003'
                clLow = f19 + 400F;  //was 200
                clHigh = f19 + 1000F;
                f21 = 1.2F;  //was 4.8F;
                break;

            case 4: // '\004'
                clLow = f19 + 500F;  //was 200
                clHigh = f19 + 1600F;
                f21 = 3F;  //was 8F;
                break;

            case 5: // '\005'
                clLow = f19 + 900F;  //was 100
                clHigh = f19 + 2100F;  //was 1900
                f21 = 6F;  //was 12.8F;
                break;

            case 6: // '\006'
                clLow = f19 + 1200F;  //was = f19
                clHigh = f19 + 3000F;  //was 1200
                f21 = 7.5F;  //was 16F;
                break;
            }
            f18 = cvt(f1, clLow, clHigh, f21, 0.0F);  //cvt method outlined below
			/* f = Math.min(Math.max(f1, c1Low), c1High);
			   f18 = f21 + ((0.0F - f21) * (f - c1Low)) / (c1High - c1Low);  */

        }
        float f20 = f17 - f18;
        aiLow = (f20 >= 0.0F ? f20 : f20 / 6F) * 0.11F + 0.211F;
        aiLow = aiLow >= 0.0F ? aiLow <= 1.0F ? aiLow : 1.0F : 0.0F;
        aiHigh = (f17 >= 0.0F ? f17 : f17 / 6F) * 0.11F + 0.211F;
        aiHigh = aiHigh >= 0.0F ? aiHigh <= 1.0F ? aiHigh : 1.0F : 0.0F;
        h0 = f1;
        f20 = aiLow;
        float f22 = f20;
        float f23 = 0.0F;
        Specular = f22;
/*        f23 += f22 * ((f18 / 16F) * 0.4F + 0.3F);  */
        f23 += f22 * ((f18 / 7.5F) * 0.4F + 0.3F);
        f22 -= f23;
        Diffuze = f22 * 1.4285F;
        Ambient = f23 * 1.4285F;
        if(Ambient < 0.001F)
            Ambient = 0.001F;
        if(Specular < 0.01F)
            Specular = 0.01F;
        if(Diffuze < 0.001F)
            Diffuze = 0.001F;
        
        // TODO: By SAS~Storebror: Darker Nights Backport +++
        if(Config.isUSE_RENDER()) {
            darkness = 0.095F + f17 * 0.666F;
            sunMultiplier = cvt(ToSun.z, -0.6F, 0.0F, darkness, 1.0F);
        }
        // TODO: By SAS~Storebror: Darker Nights Backport ---
        
        float f24 = f16 / (f14 + f16 + 0.001076F);
        float f25 = 1.0F - f24;
        tRed = tGreen = tBlue = 1.0F;
        float f26 = 1.0F - Math.abs(ToSun.z + 0.02F) * 4F;		// added this 0.02: peak color when Sun at -1.15 degrees, instead of on horizon
        if(f26 > 0.0F)
        {
/*            tGreen = 1.0F - 0.6F * f26;
            tBlue = 1.0F - 0.7F * f26;		*/
            tGreen = 1.0F - 0.4F * f26;
            tBlue = 1.0F - 0.8F * f26;
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
        f27 *= 1.0F - (1.0F - lightAtAlt(f1)) * 0.8F;
        Red *= f27;
        Green *= f27;
        Blue *= f27;
        if(ToSun.z < 0.0F)
            ToSun.z *= 1.0F - ToSun.z * 10F;  //to make lighting fall off more rapidly, increase factor from 10 to 15
        ToSun.normalize();
        activate();
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
/*	public float MoonPhase;		// NEW - to get proper graphic <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  */

    // TODO: By SAS~Storebror, make this compatible with the "darker nights" modification once implemented in EngineMod back in the good old 4.10 days
    public float darkness;
    public float sunMultiplier;
}
