/*Modified Sun class for the SAS Engine Mod*/
package com.maddox.il2.engine;

import java.util.GregorianCalendar;

import com.maddox.JGP.Vector2f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;

public class Sun {
	// TODO: Default Parameters
	// --------------------------------------------------------------------------
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
	// --------------------------------------------------------------------------

	// TODO: Parameters imported from modded 4.101m
	// --------------------------------------------------------------------------
	public float darkness;
	public float sunMultiplier;

	// --------------------------------------------------------------------------

	public Sun() {
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
	}

	public void activate() {
		if (Config.isUSE_RENDER()) setNative(ToLight.x, ToLight.y, ToLight.z, Ambient, Diffuze, Specular, Red, Green, Blue);
	}

	public void set(Vector3f vector3f) {
		SunV.set(vector3f);
		ToSun.set(SunV);
		ToSun.negate();
		ToLight.set(ToSun);
	}

	public void setLight(float f, float f1, float f2, float f3, float f4, float f5) {
		Ambient = f;
		Diffuze = f1;
		Specular = f2;
		Red = f3;
		Green = f4;
		Blue = f5;
	}

	private static float zLight(float f) {
		if (f > -0.104F) return 12.9364F - 1.43485F / (f + 0.2275954F);
		if (f > -0.31F) return 0.4315F + f * (-132.66F + f * (-1830.9F + f * (-6972.2F + f * -8738.5F)));
		else return -1E+009F;
	}

	public static float cvt(float f, float f1, float f2, float f3, float f4) {
		f = Math.min(Math.max(f, f1), f2);
		return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
	}

	public void resetCalendar() {
		missionDate = null;
	}

	// TODO: Includes some minor edits from the 4.101m modded version
	public void setAstronomic(int i, int j, int k, float f, float f1) {
		float f3 = (float) (90 - i) * DEG2RAD;
		float f4 = (float) Math.cos(f3);
		float f5 = (float) Math.sin(f3);
		float f2 = (float) ((j * 30 + k) - 80) * DEG2RAD;
		float f6 = (6.283185F * f) / 24F;
		float f7 = (float) Math.sin(f6);
		float f8 = (float) Math.cos(f6);
		float f9 = (float) Math.sin(22.5F * DEG2RAD * (float) Math.sin(f2));
		ToSun.x = f7;
		ToSun.y = f8 * f4 + f9 * f5;
		ToSun.z = f9 * f4 - f8 * f5;
		ToSun.normalize();
		SunV.x = -ToSun.x;
		SunV.y = -ToSun.y;
		SunV.z = -ToSun.z;
		int l = Mission.curYear();
		int i1 = (int) Math.floor(f);
		int j1 = (int) ((f - (float) i1) * 60F);
		int k1 = 0;
		if (missionDate == null) missionDate = new GregorianCalendar(l, j - 1, k, i1, j1, k1);
		else if (f != tod) missionDate.roll(13, true);
		tod = f;
		double ad[] = new double[10];
		ad = MoonPhase.phase(missionDate);
		moonPhase = (float) ad[0];
		f2 = (float) ((j * 30 + k) - 80) * DEG2RAD;
		f6 = 6.283185F * (-moonPhase + f / 24F);
		f7 = (float) Math.sin(f6);
		f8 = (float) Math.cos(f6);
		f9 = (float) Math.sin(22.5F * DEG2RAD * (float) Math.sin(f2));
		ToMoon.x = f7;
		ToMoon.y = f8 * f4 + f9 * f5;
		ToMoon.z = f9 * f4 - f8 * f5;
		ToMoon.normalize();
		float f14 = 0.0F;
		// TODO:
		float f15 = 1F - 2.0F * Math.abs(moonPhase - 0.5F);
		if (ToMoon.z > -0.31F) {
			float f11 = zLight(ToMoon.z) - 11.76757F - 1.066181F;
			f15 = 1F - 2.0F * Math.abs(moonPhase - 0.5F);
			f11 += (-0.01F / f15 - 5.225F) + f15 * 5.235F;
			f14 = (float) Math.exp(f11);
		}
		float f16 = 0.0F;
		if (ToSun.z > -0.31F) {
			float f13 = zLight(ToSun.z);
			f16 = (float) Math.exp(f13);
		}
		float f17 = (float) Math.log(f14 + f16 + 0.001076F);
		float f18 = 0.0F;
		EffClouds effclouds = Main.cur().clouds;
		if (effclouds != null && effclouds.type() > 2) {
			float f19 = effclouds.height();
			float f21 = 0.0F;
			switch (effclouds.type()) {
			case 3: // '\003'
				clLow = f19 + 200F;
				clHigh = f19 + 1000F;
				f21 = 4.8F;
				break;

			case 4: // '\004'
				clLow = f19 + 200F;
				clHigh = f19 + 1600F;
				f21 = 8F;
				break;

			case 5: // '\005'
				clLow = f19 + 100F;
				clHigh = f19 + 1900F;
				f21 = 12.8F;
				break;

			case 6: // '\006'
				clLow = f19;
				clHigh = f19 + 1200F;
				f21 = 16F;
				break;
			}
			f18 = cvt(f1, clLow, clHigh, f21, 0.0F);
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
		// TODO: From 4.101m version
		// --------------------------------------------------------------------------
		darkness = 0.095F + f15 * 0.666F;
		sunMultiplier = cvt(ToSun.z, -0.6F, 0.0F, darkness, 1.0F);
		// --------------------------------------------------------------------------
		Specular = f22;
		f23 += f22 * ((f18 / 16F) * 0.4F + 0.3F);
		f22 -= f23;
		Diffuze = f22 * 1.4285F;
		Ambient = f23 * 1.4285F;
		if (Ambient < 0.001F) Ambient = 0.001F;
		if (Specular < 0.01F) Specular = 0.01F;
		if (Diffuze < 0.001F) Diffuze = 0.001F;
		float f24 = f16 / (f14 + f16 + 0.001076F);
		float f25 = 1.0F - f24;
		tRed = tGreen = tBlue = 1.0F;
		float f26 = 1.0F - Math.abs(ToSun.z) * 4F;
		if (f26 > 0.0F) {
			tGreen = 1.0F - 0.6F * f26;
			tBlue = 1.0F - 0.7F * f26;
		}
		ToLight.set(ToSun);
		ToLight.scale(f16 + 0.001F);
		tmpV3f.set(ToMoon);
		tmpV3f.scale(f14 + 0.001F);
		ToLight.add(tmpV3f);
		ToLight.normalize();
		if (ToLight.z < 0.05F) {
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
		if (ToSun.z < 0.0F) ToSun.z *= 1.0F - ToSun.z * 10F;
		ToSun.normalize();
		activate();
	}

	private static native void setNative(float f, float f1, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

	public float lightAtAlt(float f) {
		if (f > clHigh) return aiHigh;
		if (f < clLow) return aiLow;
		else return cvt(f, clLow, clHigh, aiLow, aiHigh);
	}

	public boolean needsCubeUpdate(float f) {
		if (f > clHigh && h0 > clHigh || f < clLow && h0 < clLow) return false;
		return f - h0 > 30F || f - h0 < -30F;
	}
}
