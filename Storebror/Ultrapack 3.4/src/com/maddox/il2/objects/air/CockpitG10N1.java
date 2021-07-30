package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitG10N1 extends CockpitPilot {
	private class Variables {

		float      throttle[];
		float      prop[];
		float      altimeter;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float      vspeed;
		AnglesFork waypointDeviation;

		private Variables() {
			this.throttle = new float[6];
			this.prop = new float[6];
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
			this.waypointDeviation = new AnglesFork();
		}
	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitG10N1.this.fm != null) {
				CockpitG10N1.this.setTmp = CockpitG10N1.this.setOld;
				CockpitG10N1.this.setOld = CockpitG10N1.this.setNew;
				CockpitG10N1.this.setNew = CockpitG10N1.this.setTmp;
				for (int i = 0; i < 6; i++) {
					CockpitG10N1.this.setNew.throttle[i] = 0.85F * CockpitG10N1.this.setOld.throttle[i] + CockpitG10N1.this.fm.EI.engines[i].getControlThrottle() * 0.15F;
					CockpitG10N1.this.setNew.prop[i] = 0.85F * CockpitG10N1.this.setOld.prop[i] + CockpitG10N1.this.fm.EI.engines[i].getControlProp() * 0.15F;
				}

				CockpitG10N1.this.setNew.altimeter = CockpitG10N1.this.fm.getAltitude();
				float f = CockpitG10N1.this.waypointAzimuth();
				CockpitG10N1.this.setNew.azimuth.setDeg(CockpitG10N1.this.setOld.azimuth.getDeg(1.0F), ((FlightModelMain) CockpitG10N1.this.fm).Or.azimut());
				CockpitG10N1.this.setNew.waypointAzimuth.setDeg(CockpitG10N1.this.setOld.waypointAzimuth.getDeg(1.0F), f);
				CockpitG10N1.this.setNew.waypointDeviation.setDeg(CockpitG10N1.this.setOld.waypointDeviation.getDeg(0.1F), f - CockpitG10N1.this.setOld.azimuth.getDeg(1.0F) + World.Rnd().nextFloat(-10F, 10F));
				CockpitG10N1.this.setNew.vspeed = (199F * CockpitG10N1.this.setOld.vspeed + CockpitG10N1.this.fm.getVertSpeed()) / 200F;
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected float waypointAzimuth() {
		WayPoint waypoint = this.fm.AP.way.curr();
		if (waypoint == null) return 0.0F;
		waypoint.getP(this.tmpP);
		this.tmpV.sub(this.tmpP, this.fm.Loc);
		float f = (float) Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x));
		while (f <= -180F)
			f += 180F;
		while (f > 180F)
			f -= 180F;
		return f;
	}

	public CockpitG10N1() {
		super("3DO/Cockpit/G10N1-P/hier.him", "bf109");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.pictFlap = 0.0F;
		this.tmpP = new Point3d();
		this.tmpV = new Vector3d();
		super.cockpitNightMats = new String[] { "texture01_dmg", "texture01", "texture02_dmg", "texture02", "texture03_dmg", "texture03", "texture04_dmg", "texture04", "texture05_dmg", "texture05", "texture06_dmg", "texture06", "texture21_dmg",
				"texture21", "texture25" };
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        Cockpit.xyz[0] = (this.pictElev = 0.99F * this.pictElev + 0.01F * this.fm.CT.ElevatorControl) * 0.1F;
        Cockpit.xyz[2] = (this.pictElev = 0.99F * this.pictElev + 0.01F * this.fm.CT.ElevatorControl) * 0.012F;
        this.mesh.chunkSetLocate("Z_Column", Cockpit.xyz, Cockpit.ypr);
        
		this.mesh.chunkSetAngles("Z_AroneL", 0.0F, -(this.pictAiler = 0.99F * this.pictAiler + 0.01F * this.fm.CT.AileronControl) * 68F, 0.0F);
		this.mesh.chunkSetAngles("Z_AroneR", 0.0F, -(this.pictAiler = 0.99F * this.pictAiler + 0.01F * this.fm.CT.AileronControl) * 68F, 0.0F);
		this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
		this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
		this.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
		this.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
		this.mesh.chunkSetAngles("zFlaps1", 0.0F, 38F * (this.pictFlap = 0.75F * this.pictFlap + 0.25F * this.fm.CT.FlapsControl), 0.0F);
		for (int i = 0; i < 6; i++) {
			tmpstr = "Z_PropPitch" + (i * 2 + 1);
			this.mesh.chunkSetAngles(tmpstr, 0.0F, -54F * this.interp(this.setNew.prop[i], this.setOld.prop[i], f), 0.0F);
		}

		for (int j = 0; j < 6; j++) {
			tmpstr = "zThrottle" + (j + 1);
			this.mesh.chunkSetAngles(tmpstr, 0.0F, -49F * this.interp(this.setNew.throttle[j], this.setOld.throttle[j], f), 0.0F);
		}

		if (this.fm.Gears.lgear) {
			this.resetYPRmodifier();
			Cockpit.xyz[1] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 0.015F);
			this.mesh.chunkSetLocate("Z_GearL1", Cockpit.xyz, Cockpit.ypr);
		}
		if (this.fm.Gears.rgear) {
			this.resetYPRmodifier();
			Cockpit.xyz[1] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 0.015F);
			this.mesh.chunkSetLocate("Z_GearR1", Cockpit.xyz, Cockpit.ypr);
		}
		this.mesh.chunkSetAngles("zFlaps2", 0.0F, 90F * this.fm.CT.getFlap(), 0.0F);
		this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("zSecond", 0.0F, this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("zAH1", 0.0F, -this.fm.Or.getKren(), 0.0F);
		this.mesh.chunkSetAngles("zAH1a", 0.0F, -this.fm.Or.getKren(), 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
		this.mesh.chunkSetLocate("zAH2", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetLocate("zAH2a", Cockpit.xyz, Cockpit.ypr);
		if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
			this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
			this.mesh.chunkSetAngles("Z_Climb2", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
		}
		this.w.set(super.fm.getW());
		this.fm.Or.transform(this.w);
		this.mesh.chunkSetAngles("zTurnBank", 0.0F, this.cvt(((Tuple3f) this.w).z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
		this.mesh.chunkSetAngles("zTurnBank3", 0.0F, this.cvt(((Tuple3f) this.w).z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
		this.mesh.chunkSetAngles("zBall", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
		this.mesh.chunkSetAngles("zBall2", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
		this.mesh.chunkSetAngles("zBall3", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
		this.mesh.chunkSetAngles("zPDI", 0.0F, this.cvt(this.setNew.waypointDeviation.getDeg(f), -90F, 90F, -46.5F, 46.5F), 0.0F);
		this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, super.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("zSpeed2", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, super.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
		this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
		this.mesh.chunkSetAngles("zAlt3", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
		this.mesh.chunkSetAngles("zAlt4", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
		this.mesh.chunkSetAngles("zCompass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
		this.mesh.chunkSetAngles("zCompass2", 0.0F, -0.5F * this.setNew.azimuth.getDeg(f), 0.0F);
		this.mesh.chunkSetAngles("zCompass3", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
		if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
			this.mesh.chunkSetAngles("zMagnetic", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
			this.mesh.chunkSetAngles("zNavP", 0.0F, this.setNew.waypointAzimuth.getDeg(f), 0.0F);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) == 0) for (int k = 0; k < 6; k++) {
			tmpstr = "zRPM" + (k + 1);
			this.mesh.chunkSetAngles(tmpstr, 0.0F, this.cvt(this.fm.EI.engines[k].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
			tmpstr = "Z_Pres" + (k + 1);
			this.mesh.chunkSetAngles(tmpstr, 0.0F, this.pictManf[k] = 0.9F * this.pictManf[k] + 0.1F * this.cvt(this.fm.EI.engines[k].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
		}
		this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[4].tOilOut * this.fm.EI.engines[1].getReadyness(), 0.0F, 13.49F, 0.0F, 90F), 0.0F);
		this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[4].getReadyness(), 0.0F, 13.49F, 0.0F, 90F), 0.0F);
		if ((this.fm.AS.astateCockpitState & 0x40) == 0) this.mesh.chunkSetAngles("zFreeAir", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -70F, 150F, -26.6F, 57F), 0.0F);
		this.mesh.chunkSetAngles("zHydPres", 0.0F, this.fm.Gears.bIsHydroOperable ? 165.5F : 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 0x80) == 0);
		if ((this.fm.AS.astateCockpitState & 2) != 0) {
			this.mesh.chunkVisible("Pricel_D0", false);
			this.mesh.chunkVisible("Pricel_D1", true);
			this.mesh.chunkVisible("Z_Z_RETICLE", false);
			this.mesh.chunkVisible("Z_Z_MASK", false);
		}
		if ((this.fm.AS.astateCockpitState & 1) != 0) {
			this.mesh.chunkVisible("XGlassDamage", true);
			this.mesh.chunkVisible("XGlassDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("Panel_D0", false);
			this.mesh.chunkVisible("Panel_D1", true);
			this.mesh.chunkVisible("zCompass3", false);
			this.mesh.chunkVisible("Z_Oilpres1", false);
			this.mesh.chunkVisible("Z_Oilpres2", false);
			this.mesh.chunkVisible("zHydPres", false);
		}
		if ((this.fm.AS.astateCockpitState & 4) != 0) {
			this.mesh.chunkVisible("XGlassDamage", true);
			this.mesh.chunkVisible("XHullDamage2", true);
		}
		if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XHullDamage1", true);
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
		this.retoggleLight();
	}

	public void toggleLight() {
		super.cockpitLightControl = !super.cockpitLightControl;
		if (super.cockpitLightControl) this.setNightMats(true);
		else this.setNightMats(false);
	}

	private void retoggleLight() {
		if (super.cockpitLightControl) {
			this.setNightMats(false);
			this.setNightMats(true);
		} else {
			this.setNightMats(true);
			this.setNightMats(false);
		}
	}
	
    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            aircraft().hierMesh().chunkVisible("nose", false);
            aircraft().hierMesh().chunkVisible("nose1", false);
            aircraft().hierMesh().chunkVisible("nose2", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!isFocused()) {
            return;
        } else {
            aircraft().hierMesh().chunkVisible("nose", aircraft().isChunkAnyDamageVisible("CF_D"));
            aircraft().hierMesh().chunkVisible("nose1", aircraft().isChunkAnyDamageVisible("CF_D"));
            aircraft().hierMesh().chunkVisible("nose2", aircraft().isChunkAnyDamageVisible("CF_D"));
            super.doFocusLeave();
            return;
        }
    }



	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;
	public Vector3f            w;
	private float              pictAiler;
	private float              pictElev;
	private float              pictFlap;
	private float              pictManf[]         = { 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F };
	private static final float speedometerScale[] = { 0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 296.5F, 308.5F, 324F, 338.5F };
	private static final float variometerScale[]  = { -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 130F, 157F, 180F };
	private Point3d            tmpP;
	private Vector3d           tmpV;
	private static String      tmpstr             = null;
	
    static 
    {
        Class class1 = CockpitG10N1.class;
        Property.set(class1, "normZN", 2.0F);
        Property.set(class1, "gsZN", 2.0F);
    }
}
