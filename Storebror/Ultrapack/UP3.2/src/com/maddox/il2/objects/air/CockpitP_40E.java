package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitP_40E extends CockpitPilot {
	private class Variables {

		float throttle;
		float prop;
		float altimeter;
		float azimuth;
		float vspeed;
		float waypointAzimuth;

		// TODO: +++ New Oil and Fuel Pressure Calculation and Animation by
		// SAS~Storebror +++
		float oilPressure;
		float fuelPressure;
		// TODO: --- New Oil and Fuel Pressure Calculation and Animation by
		// SAS~Storebror ---
	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitP_40E.this.fm != null) {
				CockpitP_40E.this.setTmp = CockpitP_40E.this.setOld;
				CockpitP_40E.this.setOld = CockpitP_40E.this.setNew;
				CockpitP_40E.this.setNew = CockpitP_40E.this.setTmp;
				CockpitP_40E.this.setNew.throttle = (10F * CockpitP_40E.this.setOld.throttle + CockpitP_40E.this.fm.CT.PowerControl) / 11F;
				CockpitP_40E.this.setNew.prop = (8F * CockpitP_40E.this.setOld.prop + CockpitP_40E.this.fm.CT.getStepControl()) / 9F;
				CockpitP_40E.this.setNew.altimeter = CockpitP_40E.this.fm.getAltitude();
				if (Math.abs(CockpitP_40E.this.fm.Or.getKren()) < 45F) { CockpitP_40E.this.setNew.azimuth = (35F * CockpitP_40E.this.setOld.azimuth + -CockpitP_40E.this.fm.Or.getYaw()) / 36F; }
				if (CockpitP_40E.this.setOld.azimuth > 270F && CockpitP_40E.this.setNew.azimuth < 90F) { CockpitP_40E.this.setOld.azimuth -= 360F; }
				if (CockpitP_40E.this.setOld.azimuth < 90F && CockpitP_40E.this.setNew.azimuth > 270F) { CockpitP_40E.this.setOld.azimuth += 360F; }
				CockpitP_40E.this.setNew.waypointAzimuth = (10F * CockpitP_40E.this.setOld.waypointAzimuth + (CockpitP_40E.this.waypointAzimuth() - CockpitP_40E.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
				CockpitP_40E.this.setNew.vspeed = (199F * CockpitP_40E.this.setOld.vspeed + CockpitP_40E.this.fm.getVertSpeed()) / 200F;

				// TODO: +++ New Oil and Fuel Pressure Calculation and Animation by
				// SAS~Storebror +++
				float newOilPressure = CockpitP_40E.this.cvt(CockpitP_40E.this.fm.EI.engines[0].getRPM(), 0F, 300F, 0F, 1F) * CockpitP_40E.this.fm.EI.engines[0].getReadyness()
						* CockpitP_40E.this.cvt(CockpitP_40E.this.fm.EI.engines[0].tOilOut, 70F, 80F, 70F, 60F) * CockpitP_40E.this.cvt(CockpitP_40E.this.fm.EI.engines[0].tOilOut, 60F, 70F, 85F / 70F, 1F)
						* CockpitP_40E.this.cvt(CockpitP_40E.this.fm.EI.engines[0].tOilOut, 80F, 100F, 1F, 0.7F);
				if (newOilPressure > CockpitP_40E.this.setOld.oilPressure) {
					CockpitP_40E.this.setNew.oilPressure = ((OIL_PRESSURE_SMOOTHING_UP - 1F) * CockpitP_40E.this.setOld.oilPressure + newOilPressure) / OIL_PRESSURE_SMOOTHING_UP;
				} else {
					CockpitP_40E.this.setNew.oilPressure = ((OIL_PRESSURE_SMOOTHING_DOWN - 1F) * CockpitP_40E.this.setOld.oilPressure + newOilPressure) / OIL_PRESSURE_SMOOTHING_DOWN;
				}

				float newFuelPressure = CockpitP_40E.this.cvt(CockpitP_40E.this.fm.EI.engines[0].getRPM(), 0F, 50F, 0F, 18F) * CockpitP_40E.this.cvt(CockpitP_40E.this.fm.EI.engines[0].getReadyness(), 0.5F, 1F, 0.9F, 1F)
						* CockpitP_40E.this.cvt(CockpitP_40E.this.fm.EI.engines[0].getReadyness(), 0.0F, 0.5F, 0F, 1F);
				if (newFuelPressure > CockpitP_40E.this.setOld.fuelPressure) {
					CockpitP_40E.this.setNew.fuelPressure = ((FUEL_PRESSURE_SMOOTHING_UP - 1F) * CockpitP_40E.this.setOld.fuelPressure + newFuelPressure) / FUEL_PRESSURE_SMOOTHING_UP;
				} else {
					CockpitP_40E.this.setNew.fuelPressure = ((FUEL_PRESSURE_SMOOTHING_DOWN - 1F) * CockpitP_40E.this.setOld.fuelPressure + newFuelPressure) / FUEL_PRESSURE_SMOOTHING_DOWN;
					// TODO: --- New Oil and Fuel Pressure Calculation and Animation by
					// SAS~Storebror ---
				}
			}
			return true;
		}

	}

	protected float waypointAzimuth() {
		WayPoint waypoint = this.fm.AP.way.curr();
		if (waypoint == null) {
			return 0.0F;
		} else {
			waypoint.getP(this.tmpP);
			this.tmpV.sub(this.tmpP, this.fm.Loc);
			return (float) (57.295779513082323D * Math.atan2(-((Tuple3d) this.tmpV).y, ((Tuple3d) this.tmpV).x));
		}
	}

	public CockpitP_40E() {
		super("3DO/Cockpit/P-40E/hier.him", "bf109");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.tmpP = new Point3d();
		this.tmpV = new Vector3d();
		this.cockpitNightMats = new String[] { "Textur1", "Textur2", "Textur3", "Textur4", "Textur6", "Textur7", "Textur9", "Textur20", "Textur22", "Textur25" };
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("Z_Throttle1", -66.81F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
		if (this.fm.CT.getStepControlAuto()) {
			this.mesh.chunkSetAngles("Z_Pitch1", -70F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
		} else {
			this.mesh.chunkSetAngles("Z_Pitch1", -70F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
		}
		this.mesh.chunkSetAngles("Z_Mixt1", -70.8F * this.fm.EI.engines[0].getControlMix(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Columnbase", (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 20F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Column", (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 16F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -20F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_LeftPedal2", 0.0F, 0.0F, 23F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 0.0F, -20F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, 20F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_RightPedal2", 0.0F, 0.0F, -23F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, 0.0F, 20F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_SW_LandLights", this.fm.AS.bLandingLightOn ? 60F : 0.0F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_SW_UVLights", this.cockpitLightControl ? 60F : 0.0F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_SW_NavLights", this.fm.AS.bNavLightsOn ? 60F : 0.0F, 0.0F, 0.0F);
		this.mesh.chunkVisible("XLampCoolant1", this.fm.EI.engines[0].tOilOut > this.fm.EI.engines[0].tOilCritMax);
		this.mesh.chunkVisible("XLampFuel1", this.fm.M.fuel < 40.1F);
		this.mesh.chunkVisible("XLampGear1", this.fm.Gears.isAnyDamaged() || !this.fm.Gears.isOperable());
		this.mesh.chunkSetAngles("Z_Altimeter1", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Altimeter2", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F), 0.0F, 0.0F);
		this.w.set(this.fm.getW());
		this.fm.Or.transform(this.w);
		this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(((Tuple3f) this.w).z, -0.23562F, 0.23562F, 25F, -25F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurnBank2", -this.fm.Or.getKren(), 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0321F, -0.0321F);
		this.mesh.chunkSetLocate("Z_TurnBank2a", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("Z_TurnBank3", -this.cvt(this.getBall(7D), -7F, 7F, -14F, 14F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Carbair1", this.cvt(Atmosphere.temperature((float) ((Tuple3d) this.fm.Loc).z) - 273.15F, -50F, 50F, -60F, 60F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass1", -this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Flaps1", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 80F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Gearc1", this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 80F), 0.0F, 0.0F);
		if (this.fm.Gears.lgear) { this.mesh.chunkSetAngles("Z_GearL1", this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 80F), 0.0F, 0.0F); }
		if (this.fm.Gears.rgear) { this.mesh.chunkSetAngles("Z_GearR1", this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 80F), 0.0F, 0.0F); }
		this.mesh.chunkSetAngles("Z_Coolant1", this.cvt(this.fm.EI.engines[0].tWaterOut, 40F, 160F, 0.0F, 130F), 0.0F, 0.0F);
		// TODO: +++ New Oil and Fuel Pressure Calculation and Animation by
		// SAS~Storebror +++
		this.mesh.chunkSetAngles("Z_Oilpres1", this.setNew.oilPressure * 1.5F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Oil1", this.setNew.oilPressure * 0.9F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Fuel1", -this.setNew.fuelPressure * 36F / 7F, 0.0F, 0.0F);
		// TODO: --- New Oil and Fuel Pressure Calculation and Animation by
		// SAS~Storebror ---
		this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 180F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Fuelqty", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 5F), fuelScale), 0.0F);
		if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
			this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 343.5F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 280F), 0.0F, 0.0F);
		}
		this.mesh.chunkSetAngles("Z_Heading1", this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) ((Tuple3d) this.fm.Loc).z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 2) != 0) { this.mesh.chunkVisible("XGlassDamage3", true); }
		if ((this.fm.AS.astateCockpitState & 1) != 0) {
			this.mesh.chunkVisible("HullDamage4", true);
			this.mesh.chunkVisible("XGlassDamage1", true);
			this.mesh.chunkVisible("XGlassDamage2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("Gauge", false);
			this.mesh.chunkVisible("Gauge_D1", true);
			this.mesh.chunkVisible("Z_Speedometer1", false);
			this.mesh.chunkVisible("Z_Altimeter1", false);
			this.mesh.chunkVisible("Z_Altimeter2", false);
			this.mesh.chunkVisible("Z_Climb1", false);
			this.mesh.chunkVisible("Z_Carbair1", false);
			this.mesh.chunkVisible("Z_Coolant1", false);
			this.mesh.chunkVisible("Z_Oilpres1", false);
			this.mesh.chunkVisible("HullDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 4) != 0) {
			this.mesh.chunkVisible("HullDamage1", true);
			this.mesh.chunkVisible("XGlassDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 8) != 0) { this.mesh.chunkVisible("HullDamage3", true); }
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) { this.mesh.chunkVisible("Z_OilSplats_D1", true); }
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
			this.mesh.chunkVisible("HullDamage2", true);
			this.mesh.chunkVisible("XGlassDamage2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
			this.mesh.chunkVisible("HullDamage2", true);
			this.mesh.chunkVisible("HullDamage4", true);
		}
		this.retoggleLight();
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) {
			this.setNightMats(true);
		} else {
			this.setNightMats(false);
		}
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

	// TODO: +++ New Oil and Fuel Pressure Calculation and Animation by
	// SAS~Storebror +++
	private static final float OIL_PRESSURE_SMOOTHING_UP    = 10F;
	private static final float OIL_PRESSURE_SMOOTHING_DOWN  = 50F;
	private static final float FUEL_PRESSURE_SMOOTHING_UP   = 20F;
	private static final float FUEL_PRESSURE_SMOOTHING_DOWN = 100F;
	// TODO: --- New Oil and Fuel Pressure Calculation and Animation by
	// SAS~Storebror ---

	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;
	public Vector3f            w;
	private float              pictAiler;
	private float              pictElev;
	private static final float speedometerScale[] = { 0.0F, 17F, 56.5F, 107.5F, 157F, 204F, 220.5F, 238.5F, 256.5F, 274.5F, 293F, 311F, 330F, 342F };
	private static final float variometerScale[]  = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
	private static final float fuelScale[]        = { 0.0F, 85F, 165F, 225F, 245F, 265F };
	private Point3d            tmpP;
	private Vector3d           tmpV;

}
