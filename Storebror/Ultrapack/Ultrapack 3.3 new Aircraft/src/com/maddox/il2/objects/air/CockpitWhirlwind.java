// This file is part of the SAS IL-2 Sturmovik 1946
// Westland Whirlwind Mod.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Mod Creator:          tfs101
// Original file source: SAS~S3
// Modified by:          SAS - Special Aircraft Services
//                       www.sas1946.com
//
// Last Edited by:       SAS~Storebror
// Last Edited at:       2013/10/22

package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitWhirlwind extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitWhirlwind.this.bNeedSetUp) {
				CockpitWhirlwind.this.reflectPlaneMats();
				CockpitWhirlwind.this.bNeedSetUp = false;
			}
			if (((Whirlwind) CockpitWhirlwind.this.aircraft()).bChangedPit) {
				CockpitWhirlwind.this.reflectPlaneToModel();
				((Whirlwind) CockpitWhirlwind.this.aircraft()).bChangedPit = false;
			}
			if (CockpitWhirlwind.this.fm != null) {
				CockpitWhirlwind.this.setTmp = CockpitWhirlwind.this.setOld;
				CockpitWhirlwind.this.setOld = CockpitWhirlwind.this.setNew;
				CockpitWhirlwind.this.setNew = CockpitWhirlwind.this.setTmp;
				CockpitWhirlwind.this.setNew.trim = 0.92F * CockpitWhirlwind.this.setOld.trim + 0.08F * CockpitWhirlwind.this.fm.CT.getTrimElevatorControl();
				CockpitWhirlwind.this.setNew.throttle1 = 0.85F * CockpitWhirlwind.this.setOld.throttle1 + CockpitWhirlwind.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
				CockpitWhirlwind.this.setNew.throttle2 = 0.85F * CockpitWhirlwind.this.setOld.throttle2 + CockpitWhirlwind.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
				CockpitWhirlwind.this.setNew.prop1 = 0.85F * CockpitWhirlwind.this.setOld.prop1 + CockpitWhirlwind.this.fm.EI.engines[0].getControlProp() * 0.15F;
				CockpitWhirlwind.this.setNew.prop2 = 0.85F * CockpitWhirlwind.this.setOld.prop2 + CockpitWhirlwind.this.fm.EI.engines[1].getControlProp() * 0.15F;
				CockpitWhirlwind.this.setNew.mix1 = 0.85F * CockpitWhirlwind.this.setOld.mix1 + CockpitWhirlwind.this.fm.EI.engines[0].getControlMix() * 0.15F;
				CockpitWhirlwind.this.setNew.mix2 = 0.85F * CockpitWhirlwind.this.setOld.mix2 + CockpitWhirlwind.this.fm.EI.engines[1].getControlMix() * 0.15F;
				CockpitWhirlwind.this.setNew.altimeter = CockpitWhirlwind.this.fm.getAltitude();
				float f = CockpitWhirlwind.this.waypointAzimuth();
				CockpitWhirlwind.this.setNew.azimuth.setDeg(CockpitWhirlwind.this.setOld.azimuth.getDeg(1.0F), CockpitWhirlwind.this.fm.Or.azimut());
				CockpitWhirlwind.this.setNew.waypointAzimuth.setDeg(CockpitWhirlwind.this.setOld.waypointAzimuth.getDeg(1.0F), f);
				if (CockpitWhirlwind.this.useRealisticNavigationInstruments()) {
					CockpitWhirlwind.this.setNew.waypointAzimuth.setDeg(f - 90F);
					CockpitWhirlwind.this.setOld.waypointAzimuth.setDeg(f - 90F);
				} else CockpitWhirlwind.this.setNew.waypointAzimuth.setDeg(CockpitWhirlwind.this.setOld.waypointAzimuth.getDeg(0.1F), f);
				CockpitWhirlwind.this.setNew.vspeed = (199F * CockpitWhirlwind.this.setOld.vspeed + CockpitWhirlwind.this.fm.getVertSpeed()) / 200F;

				CockpitWhirlwind.this.setNew.oilPress1 = 0.95F * CockpitWhirlwind.this.setOld.oilPress1 + 0.05F * CockpitWhirlwind.this.cvt(CockpitWhirlwind.this.fm.EI.engines[0].getRPM(), 0F, 750F, 0F, 0.6F)
						* CockpitWhirlwind.this.cvt(CockpitWhirlwind.this.fm.EI.engines[0].tOilOut, 0F, 15F, 1.7F, 1F) * CockpitWhirlwind.this.cvt(CockpitWhirlwind.this.fm.EI.engines[0].tOilOut, 50F, 80F, 1F, 0.85F)
						* CockpitWhirlwind.this.cvt(CockpitWhirlwind.this.fm.EI.engines[0].tOilOut, 80F, 100F, 1F, 0.9F) * CockpitWhirlwind.this.fm.EI.engines[0].getReadyness();
				CockpitWhirlwind.this.setNew.oilPress2 = 0.95F * CockpitWhirlwind.this.setOld.oilPress2 + 0.05F * CockpitWhirlwind.this.cvt(CockpitWhirlwind.this.fm.EI.engines[1].getRPM(), 0F, 750F, 0F, 0.6F)
						* CockpitWhirlwind.this.cvt(CockpitWhirlwind.this.fm.EI.engines[1].tOilOut, 0F, 15F, 1.7F, 1F) * CockpitWhirlwind.this.cvt(CockpitWhirlwind.this.fm.EI.engines[1].tOilOut, 50F, 80F, 1F, 0.85F)
						* CockpitWhirlwind.this.cvt(CockpitWhirlwind.this.fm.EI.engines[1].tOilOut, 80F, 100F, 1F, 0.9F) * CockpitWhirlwind.this.fm.EI.engines[1].getReadyness();
				if (CockpitWhirlwind.this.fm.EI.engines[0].getStage() > 6) CockpitWhirlwind.this.setNew.oilPress1 = 0.0F;
				if (CockpitWhirlwind.this.fm.EI.engines[0].getRPM() < 100F) CockpitWhirlwind.this.setNew.oilPress1 = 0.0F;
				if (CockpitWhirlwind.this.fm.EI.engines[1].getStage() > 6) CockpitWhirlwind.this.setNew.oilPress2 = 0.0F;
				if (CockpitWhirlwind.this.fm.EI.engines[1].getRPM() < 100F) CockpitWhirlwind.this.setNew.oilPress2 = 0.0F;
				CockpitWhirlwind.this.setNew.oilPress1 *= World.Rnd().nextFloat(0.99F, 1.01F);
				CockpitWhirlwind.this.setNew.oilPress2 *= World.Rnd().nextFloat(0.99F, 1.01F);
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float      throttle1;
		float      throttle2;
		float      prop1;
		float      prop2;
		float      mix1;
		float      mix2;
		float      altimeter;
		float      vspeed;
		float      oilPress1;
		float      oilPress2;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float      trim;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
		}
	}

	protected float waypointAzimuth() {
		return this.waypointAzimuthInvertMinus(10F);
	}

	public CockpitWhirlwind() {
		super("3DO/Cockpit/Whirlwind/hier.him", "bf109");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.pictFlap = 0.0F;
		this.pictGear = 0.0F;
		this.bNeedSetUp = true;
		this.cockpitNightMats = new String[] { "gauges1", "gauges1_dam", "gauges2", "gauges2_dam", "gauges3", "gauges3_dam", "gauges4", "swbox", "swbox2", "leftcontroles", "COMPASS" };
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
		if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
		AircraftLH.printCompassHeading = true;
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
			this.aircraft().hierMesh().chunkVisible("Radar_Set", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
		this.aircraft().hierMesh().chunkVisible("Radar_Set", true);
		super.doFocusLeave();
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
		if (((Whirlwind) this.aircraft()).bChangedPit) {
			this.reflectPlaneToModel();
			((Whirlwind) this.aircraft()).bChangedPit = false;
		}
		this.mesh.chunkSetAngles("Z_Trim1", -1722F * this.setNew.trim, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Flaps1", 60F * (this.pictFlap = 0.85F * this.pictFlap + 0.15F * this.fm.CT.FlapsControl), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Gear1", 90F * (this.pictGear = 0.85F * this.pictGear + 0.15F * this.fm.CT.GearControl), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throtle1", 93.1F * this.setNew.throttle1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throtle2", 93.1F * this.setNew.throttle2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Prop1", 95F * this.setNew.prop1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Prop2", 95F * this.setNew.prop2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Mixture1", 90F * this.setNew.mix1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Mixture2", 90F * this.setNew.mix2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, 16F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, 0.0F, 16F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -16F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 0.0F, -16F * this.fm.CT.getRudder());
		this.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, -(this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 70F);
		this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 10F);

		this.mesh.chunkVisible("RUS_GUN", !this.fm.CT.WeaponControl[0]);

		this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 648.2F, 0.0F, 35.0F), speedometerScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass3", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass2", 90F + this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8.0F), variometerScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurnBank1", -this.fm.Or.getKren(), 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.031F, -0.031F);
		this.mesh.chunkSetLocate("Z_TurnBank2", Cockpit.xyz, Cockpit.ypr);
		if (this.fm.Gears.onGround()) this.mesh.chunkSetAngles("Z_Fuel2", this.floatindex(this.cvt(this.fm.M.fuel / 3.2835820895522388059701492537313F, 0.0F, 135.0F, 0.0F, 15.0F), fuelScaleGround), 0.0F, 0.0F);
		else this.mesh.chunkSetAngles("Z_Fuel2", this.floatindex(this.cvt(this.fm.M.fuel / 3.2835820895522388059701492537313F, 0.0F, 135.0F, 0.0F, 27.0F), fuelScaleFlight), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.getBall(7D), -8.0F, 8.0F, -35.0F, 35.0F), 0.0F, 0.0F);
		this.w.set(this.fm.getW());
		this.fm.Or.transform(this.w);
		this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(this.w.z, -0.23562F, 0.23562F, 48.0F, -48.0F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		float rndBoost1 = Math.abs(this.fm.EI.engines[0].getManifoldPressure() - 1.0F) * 0.05F + 0.01F;
		if (this.fm.EI.engines[0].getRPM() < 100.0F) rndBoost1 = 0.0F;
		this.mesh.chunkSetAngles("Z_Manifold1", this.cvt(this.fm.EI.engines[0].getManifoldPressure() + World.Rnd().nextFloat(-rndBoost1, rndBoost1), 0.3F, 3.5F, -70.0F, 250F), 0.0F, 0.0F);
		float rndBoost2 = Math.abs(this.fm.EI.engines[1].getManifoldPressure() - 1.0F) * 0.05F + 0.01F;
		if (this.fm.EI.engines[1].getRPM() < 100.0F) rndBoost2 = 0.0F;
		this.mesh.chunkSetAngles("Z_Manifold2", this.cvt(this.fm.EI.engines[1].getManifoldPressure() + World.Rnd().nextFloat(-rndBoost2, rndBoost2), 0.3F, 3.5F, -70.0F, 250F), 0.0F, 0.0F);
		float zOil1 = this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 100F, 0F, 270F);
		float zOil2 = this.cvt(this.fm.EI.engines[1].tOilOut, 50F, 100F, 0F, 270F);
		if (this.fm.EI.engines[0].getStage() > 6) zOil1 = 0.0F;
		if (this.fm.EI.engines[1].getStage() > 6) zOil2 = 0.0F;
		this.mesh.chunkSetAngles("Z_Oil1", zOil1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Oil2", zOil2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELK_OIL_LB1", 0.0F, this.cvt(this.setNew.oilPress1, 0F, 1F, 0F, -37.5F), 0.0F);
		this.mesh.chunkSetAngles("STRELK_OIL_LB2", 0.0F, this.cvt(this.setNew.oilPress2, 0F, 1F, 0F, -37.5F), 0.0F);
		this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 1000.0F, 5000.0F, 2.0F, 10.0F), rpmScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_RPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 1000.0F, 5000.0F, 2.0F, 10.0F), rpmScale), 0.0F, 0.0F);
		this.mesh.chunkVisible("Z_GearGreen1", this.fm.CT.getGear() > 0.95F);
		this.mesh.chunkVisible("Z_GearRed1", this.fm.CT.getGear() < 0.05F || !this.fm.Gears.lgear || !this.fm.Gears.rgear);

		float fRadiator = this.fm.EI.engines[0].getControlRadiator();
		if (Math.abs(this.kangle1 - fRadiator) > 0.01F) {
			this.kangle1 = fRadiator;
			this.mesh.chunkSetAngles("WaterL_D0", 0.0F, -20.0F * fRadiator, 0.0F);
		}
		fRadiator = this.fm.EI.engines[1].getControlRadiator();
		if (Math.abs(this.kangle2 - fRadiator) > 0.01F) {
			this.kangle2 = fRadiator;
			this.mesh.chunkSetAngles("WaterR_D0", 0.0F, -20.0F * fRadiator, 0.0F);
		}

	}

	public void reflectCockpitState() {
		this.retoggleLight();
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

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		this.mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
		this.mesh.materialReplace("Gloss2D2o", mat);
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0") || hiermesh.isChunkVisible("WingLIn_D1") || hiermesh.isChunkVisible("WingLIn_D2") || hiermesh.isChunkVisible("WingLIn_D3"));
		this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0") || hiermesh.isChunkVisible("WingRIn_D1") || hiermesh.isChunkVisible("WingRIn_D2") || hiermesh.isChunkVisible("WingRIn_D3"));
	}

	private float                kangle1          = 0.0F;
	private float                kangle2          = 0.0F;

	private Variables            setOld;
	private Variables            setNew;
	private Variables            setTmp;
	public Vector3f              w;
	private float                pictAiler;
	private float                pictElev;
	private float                pictFlap;
	private float                pictGear;
	private boolean              bNeedSetUp;

	private static final float[] speedometerScale = { 0.0F, 0.0F, 0.0F, 10.0F, 22.0F, 36.0F, 47.0F, 64.0F, 78.0F, 94.0F, 115.0F, 133.0F, 152.0F, 171.0F, 186.0F, 201.0F, 220.5F, 239.0F, 257.0F, 274.0F, 295.0F, 314.0F, 337.0F, 359.0F, 377.0F, 395.0F,
			410.0F, 426.0F, 446.0F, 465.0F, 479.0F, 497.0F, 516.0F, 534.0F, 550.0F, 565.0F };

	private static final float[] variometerScale  = { -158.0F, -111.0F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111.0F, 158.0F };

	private static final float[] fuelScaleGround  = { 60.0F, 70.5F, 80.0F, 86.5F, 92.8F, 96.6F, 98.8F, 105.5F, 110.0F, 112.8F, 114.0F, 115.5F, 117.0F, 118.5F, 124.0F };

	private static final float[] fuelScaleFlight  = { 60.0F, 75.0F, 80.5F, 85.0F, 87.7F, 92.0F, 94.8F, 97.0F, 99.7F, 102.1F, 103.2F, 104.0F, 104.4F, 105.2F, 105.5F, 106.6F, 107.7F, 110.4F, 111.9F, 113.4F, 114.4F, 115.6F, 116.5F, 117.2F, 117.7F, 119.0F,
			120.0F, 121.2F };

	private static final float[] rpmScale         = { 5.0F, 5.0F, 7.0F, 32.0F, 68.0F, 113.5F, 162.5F, 203.5F, 255.0F, 291.5F, 321.5F };
}
