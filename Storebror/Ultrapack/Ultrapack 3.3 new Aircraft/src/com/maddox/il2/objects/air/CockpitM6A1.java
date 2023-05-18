package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitM6A1 extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitM6A1.this.fm != null) {
				CockpitM6A1.this.setTmp = CockpitM6A1.this.setOld;
				CockpitM6A1.this.setOld = CockpitM6A1.this.setNew;
				CockpitM6A1.this.setNew = CockpitM6A1.this.setTmp;
				CockpitM6A1.this.setNew.flap = 0.88F * CockpitM6A1.this.setOld.flap + 0.12F * CockpitM6A1.this.fm.CT.FlapsControl;
				CockpitM6A1.this.setNew.tlock = 0.7F * CockpitM6A1.this.setOld.tlock + 0.3F * (CockpitM6A1.this.fm.Gears.bTailwheelLocked ? 1.0F : 0.0F);
				if (CockpitM6A1.this.cockpitDimControl) {
					if (CockpitM6A1.this.setNew.dimPosition > 0.0F) CockpitM6A1.this.setNew.dimPosition = CockpitM6A1.this.setOld.dimPosition - 0.05F;
				} else if (CockpitM6A1.this.setNew.dimPosition < 1.0F) CockpitM6A1.this.setNew.dimPosition = CockpitM6A1.this.setOld.dimPosition + 0.05F;
				if (CockpitM6A1.this.fm.CT.GearControl < 0.5F && CockpitM6A1.this.setNew.gear < 1.0F) CockpitM6A1.this.setNew.gear = CockpitM6A1.this.setOld.gear + 0.02F;
				if (CockpitM6A1.this.fm.CT.GearControl > 0.5F && CockpitM6A1.this.setNew.gear > 0.0F) CockpitM6A1.this.setNew.gear = CockpitM6A1.this.setOld.gear - 0.02F;
				CockpitM6A1.this.setNew.throttle = 0.9F * CockpitM6A1.this.setOld.throttle + 0.1F * CockpitM6A1.this.fm.CT.PowerControl;
				CockpitM6A1.this.setNew.manifold = 0.8F * CockpitM6A1.this.setOld.manifold + 0.2F * CockpitM6A1.this.fm.EI.engines[0].getManifoldPressure();
				CockpitM6A1.this.setNew.pitch = 0.9F * CockpitM6A1.this.setOld.pitch + 0.1F * CockpitM6A1.this.fm.EI.engines[0].getControlProp();
				CockpitM6A1.this.setNew.mix = 0.9F * CockpitM6A1.this.setOld.mix + 0.1F * CockpitM6A1.this.fm.EI.engines[0].getControlMix();
				CockpitM6A1.this.setNew.altimeter = CockpitM6A1.this.fm.getAltitude();
				CockpitM6A1.this.setNew.azimuth.setDeg(CockpitM6A1.this.setOld.azimuth.getDeg(1.0F), CockpitM6A1.this.fm.Or.azimut());
				CockpitM6A1.this.setNew.waypointAzimuth.setDeg(CockpitM6A1.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitM6A1.this.waypointAzimuth() - CockpitM6A1.this.setOld.azimuth.getDeg(1.0F));
				CockpitM6A1.this.setNew.vspeed = (199F * CockpitM6A1.this.setOld.vspeed + CockpitM6A1.this.fm.getVertSpeed()) / 200F;
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float      flap;
		float      throttle;
		float      pitch;
		float      mix;
		float      gear;
		float      tlock;
		float      altimeter;
		float      manifold;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float      vspeed;
		float      dimPosition;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
		}

	}

	public CockpitM6A1() {
		super("3DO/Cockpit/M6A1/hier.him", "bf109");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.pictGear = 0.0F;
		this.pictTE = 0.0F;
		this.pictTR = 0.0F;
		this.cockpitNightMats = new String[] { "Gauges_01", "Gauges_02", "Gauges_03", "Gauges_04", "Gauges_05", "Gauges_06", "Gauges_07", "DGauges_01", "DGauges_02", "DGauges_03", "DGauges_04", "DGauges_05", "DGauges_06" };
		this.setNightMats(false);
		this.cockpitDimControl = !this.cockpitDimControl;
		this.interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("CanopyOpenLvr", this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 0.1F, 0.0F, 63F), 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.61F);
		this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 0.1F, 0.0F, 0.0132F);
		this.mesh.chunkSetLocate("CanopyOpenRodL", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 0.1F, 0.0F, -0.0128F);
		this.mesh.chunkSetLocate("CanopyOpenRodR", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("GSDimmArm", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 64F, 0.0F), 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, -0.0594F, 0.0F);
		this.mesh.chunkSetLocate("GSDimmBase", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("IgnitionSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 113F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("GearHandle", this.cvt(this.pictGear = 0.9F * this.pictGear + 0.1F * this.fm.CT.GearControl, 0.0F, 1.0F, 0.0F, -31F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("GearHandleMan", 0.0F, 0.0F, 0.0F);
		if (this.fm.CT.FlapsControl != 0.19F) this.mesh.chunkSetAngles("FlapHandle", -48F * this.setNew.flap, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("TQHandle", 48F * this.setNew.throttle, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("TRigger", this.fm.CT.saveWeaponControl[1] ? -12F : 0.0F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("PropPitchLvr", 51F * this.setNew.pitch, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("MixLvr", 45F * this.setNew.mix, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("ChargerLvr", this.cvt(this.fm.EI.engines[0].getControlCompressor(), 0.0F, 2.0F, 0.0F, 49F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("ChargerAutoLvr", this.cvt(this.fm.EI.engines[0].getControlCompressor(), 0.0F, 2.0F, 0.0F, 49F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("CowlFlapLvr", -750F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("OilCoolerLvr", -450F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("PedalCrossBar", 25F * this.fm.CT.getRudder(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Pedal_L", 35F * this.fm.CT.getBrake(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Pedal_R", 35F * this.fm.CT.getBrake(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("FLCS", (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 20F, 0.0F, 20F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl));
		this.mesh.chunkSetAngles("FLCSRod", 0.0F, 0.0F, -20F * this.pictElev);
		this.mesh.chunkSetAngles("ElevTrim", 450F * (this.pictTE = 0.9F * this.pictTE + 0.1F * this.fm.CT.trimElevator), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("RuddrTrim", -450F * (this.pictTR = 0.9F * this.pictTR + 0.1F * this.fm.CT.trimRudder), 0.0F, 0.0F);
		if (this.cockpitLightControl) this.mesh.chunkSetAngles("SW_UVLight", 0.0F, 0.0F, 0.0F);
		else this.mesh.chunkSetAngles("SW_UVLight", 0.0F, 0.0F, -54F);
		this.resetYPRmodifier();
		float f1 = this.pictTE;
		if (f1 < 0.0F) Cockpit.xyz[1] = this.cvt(f1, -0.25F, 0.0F, -0.02305F, 0.0F);
		else Cockpit.xyz[1] = this.cvt(f1, 0.0F, 0.5F, 0.0F, 0.04985F);
		this.mesh.chunkSetLocate("NeedElevTrim", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("NeedRuddrTrim", this.cvt(this.pictTR, -0.5F, 0.5F, 90F, -90F), 0.0F, 0.0F);
		if ((this.fm.AS.astateCockpitState & 0x40) == 0 || (this.fm.AS.astateCockpitState & 0x10) == 0 || (this.fm.AS.astateCockpitState & 4) == 0) {
			this.mesh.chunkSetAngles("NeedAHCyl", 0.0F, -this.fm.Or.getKren(), 0.0F);
			this.mesh.chunkSetAngles("NeedAHBar", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 20F, -20F));
		}
		if ((this.fm.AS.astateCockpitState & 0x40) == 0 || (this.fm.AS.astateCockpitState & 8) == 0 || (this.fm.AS.astateCockpitState & 0x20) == 0) {
			this.mesh.chunkSetAngles("NeedCompass_A", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -20F, 20F, 20F, -20F));
			this.mesh.chunkSetAngles("NeedCompass_B", -this.setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
		}
		this.mesh.chunkSetAngles("NeedAlt_Km", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 1440F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedAlt_M", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 14400F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedBank", this.cvt(this.getBall(8D), -8F, 8F, 10F, -10F), 0.0F, 0.0F);
		this.w.set(this.fm.getW());
		this.fm.Or.transform(this.w);
		this.mesh.chunkSetAngles("NeedTurn", this.cvt(this.w.z, -0.23562F, 0.23562F, -25F, 25F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedClimb", this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedCylTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 360F, 0.0F, 90F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedExhTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 324F, 0.0F, 90F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedFuelA", this.cvt(this.fm.M.fuel, 171F, 600F, 0.0F, 290F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedFuelB", this.cvt(this.fm.M.fuel, 0.0F, 171F, 0.0F, 153F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedFuelPress", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.6F, 0.0F, -180F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedMin", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedManPress", this.cvt(this.setNew.manifold, 0.33339F, 1.66661F, -162.5F, 162.5F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedOilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 180F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedOilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 30F, 110F, 0.0F, 8F), oilTScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedRPM", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 7F), revolutionsScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 740.7998F, 0.0F, 20F), speedometerScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedVMPressA", this.cvt(this.fm.M.nitro, 0.0F, 3F, 0.0F, 180F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedVMPressB", this.cvt((float) Math.sqrt(this.fm.M.nitro), 0.0F, 8F, 0.0F, -180F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("NeedExternalT", this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 233.09F, 333.09F, 0.0F, 5F), frAirTempScale), 0.0F, 0.0F);
		if ((this.fm.AS.astateCockpitState & 0x10) == 0) this.mesh.chunkSetAngles("NeedDF", this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -90F, 90F, -33F, 33F), 0.0F, 0.0F);
		this.mesh.chunkVisible("FlareGearDn_L", this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
		this.mesh.chunkVisible("FlareGearDn_R", this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear);
		this.mesh.chunkVisible("FlareGearDn_C", this.fm.CT.getGear() > 0.99F);
		this.mesh.chunkVisible("FlareGearUp_L", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.lgear);
		this.mesh.chunkVisible("FlareGearUp_R", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.rgear);
		this.mesh.chunkVisible("FlareGearUp_C", this.fm.CT.getGear() < 0.01F);
		this.mesh.chunkVisible("FlareBoostRed", this.fm.M.fuel > 52.5F);
		this.mesh.chunkVisible("FlareBoostGreen", this.fm.M.fuel < 52.5F);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) {
			this.mesh.chunkVisible("DamageHull2", true);
			this.mesh.chunkVisible("Gages5_D0", false);
			this.mesh.chunkVisible("Gages5_D1", true);
			this.mesh.chunkVisible("NeedFuelA", false);
			this.mesh.chunkVisible("NeedFuelB", false);
		}
		if ((this.fm.AS.astateCockpitState & 8) != 0) {
			this.mesh.chunkVisible("DamageHull3", true);
			this.mesh.chunkVisible("Gages2_D0", false);
			this.mesh.chunkVisible("Gages2_D1", true);
			this.mesh.chunkVisible("NeedCylTemp", false);
			this.mesh.chunkVisible("NeedFuelPress", false);
			this.mesh.chunkVisible("NeedOilPress", false);
			this.mesh.chunkVisible("NeedOilTemp", false);
		}
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
			this.mesh.chunkVisible("DamageHull2", true);
			this.mesh.chunkVisible("Gages4_D0", false);
			this.mesh.chunkVisible("Gages4_D1", true);
			this.mesh.chunkVisible("NeedSpeed", false);
		}
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
			this.mesh.chunkVisible("DamageHull3", true);
			this.mesh.chunkVisible("Gages3_D0", false);
			this.mesh.chunkVisible("Gages3_D1", true);
			this.mesh.chunkVisible("NeedAlt_Km", false);
			this.mesh.chunkVisible("NeedAlt_M", false);
			this.mesh.chunkVisible("NeedClimb", false);
			this.mesh.chunkVisible("NeedVMPressA", false);
			this.mesh.chunkVisible("NeedVMPressB", false);
		}
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("OilSplats", true);
		if ((this.fm.AS.astateCockpitState & 2) != 0) {
			this.mesh.chunkVisible("GunSight_T3", false);
			this.mesh.chunkVisible("DGS_Lenz", true);
			this.mesh.chunkVisible("GSGlassMain", false);
			this.mesh.chunkVisible("GSDimmArm", false);
			this.mesh.chunkVisible("GSDimmBase", false);
			this.mesh.chunkVisible("GSGlassDimm", false);
			this.mesh.chunkVisible("DGunSight_T3", true);
			this.mesh.chunkVisible("DGS_Lenz", true);
			this.mesh.chunkVisible("Z_Z_RETICLE", false);
			this.mesh.chunkVisible("Z_Z_MASK", false);
			this.mesh.chunkVisible("DamageGlass1", true);
		}
		if ((this.fm.AS.astateCockpitState & 1) != 0) {
			this.mesh.chunkVisible("DamageGlass2", true);
			this.mesh.chunkVisible("DamageGlass3", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("DamageHull1", true);
			this.mesh.chunkVisible("Gages1_D0", false);
			this.mesh.chunkVisible("Gages1_D1", true);
			this.mesh.chunkVisible("NeedRPM", false);
			this.mesh.chunkVisible("NeedManPress", false);
			this.mesh.chunkVisible("NeedExhTemp", false);
			this.mesh.chunkVisible("NeedTurn", false);
			this.mesh.chunkVisible("NeedBank", false);
		}
		this.retoggleLight();
	}

	public void toggleDim() {
		this.cockpitDimControl = !this.cockpitDimControl;
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) this.setNightMats(true);
		else this.setNightMats(false);
	}

	private void retoggleLight() {
		if (this.cockpitLightControl) {
			this.setNightMats(false);
			this.setNightMats(true);
		} else {
			this.setNightMats(true);
			this.setNightMats(false);
		}
	}

	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;
	public Vector3f            w;
	private float              pictAiler;
	private float              pictElev;
	private float              pictGear;
	private float              pictTE;
	private float              pictTR;
	private static final float speedometerScale[] = { 0.0F, -2F, -7.5F, 22F, 65.5F, 109F, 153F, 197F, 242.5F, 290F, 338F, 374F, 407F, 439F, 471F, 503F, 536.5F, 570F, 606F, 643.5F, 676F };
	private static final float revolutionsScale[] = { 0.0F, 20F, 75F, 125F, 180F, 220F, 285F, 335F };
	private static final float oilTScale[]        = { 0.0F, 20F, 70.5F, 122.5F, 180F, 237.5F, 290.5F, 338F };
	private static final float frAirTempScale[]   = { 0.0F, 20.5F, 37F, 48.5F, 60.5F, 75.5F };
}
