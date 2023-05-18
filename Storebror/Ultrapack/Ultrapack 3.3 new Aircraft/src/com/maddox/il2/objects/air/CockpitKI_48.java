package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitKI_48 extends CockpitPilot {
	private class Variables {

		float         throttle1;
		float         throttle2;
		float         prop1;
		float         prop2;
		float         mix1;
		float         mix2;
		private float manifold1;
		private float manifold2;
		float         altimeter;
		AnglesFork    azimuth;
		AnglesFork    waypointAzimuth;
		AnglesFork    waypointDirection;
		float         vspeed;
		float         inert;
		float         turn;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
			this.waypointDirection = new AnglesFork();
			this.manifold1 = 0.0F;
			this.manifold2 = 0.0F;
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitKI_48.this.fm != null) {
				CockpitKI_48.this.setTmp = CockpitKI_48.this.setOld;
				CockpitKI_48.this.setOld = CockpitKI_48.this.setNew;
				CockpitKI_48.this.setNew = CockpitKI_48.this.setTmp;
				CockpitKI_48.this.setNew.throttle1 = 0.9F * CockpitKI_48.this.setOld.throttle1 + 0.1F * CockpitKI_48.this.fm.EI.engines[0].getControlThrottle();
				CockpitKI_48.this.setNew.prop1 = 0.9F * CockpitKI_48.this.setOld.prop1 + 0.1F * CockpitKI_48.this.fm.EI.engines[0].getControlProp();
				CockpitKI_48.this.setNew.mix1 = 0.8F * CockpitKI_48.this.setOld.mix1 + 0.2F * CockpitKI_48.this.fm.EI.engines[0].getControlMix();
				CockpitKI_48.this.setNew.throttle2 = 0.9F * CockpitKI_48.this.setOld.throttle2 + 0.1F * CockpitKI_48.this.fm.EI.engines[1].getControlThrottle();
				CockpitKI_48.this.setNew.prop2 = 0.9F * CockpitKI_48.this.setOld.prop2 + 0.1F * CockpitKI_48.this.fm.EI.engines[1].getControlProp();
				CockpitKI_48.this.setNew.mix2 = 0.8F * CockpitKI_48.this.setOld.mix2 + 0.2F * CockpitKI_48.this.fm.EI.engines[1].getControlMix();
				CockpitKI_48.this.setNew.manifold1 = 0.8F * CockpitKI_48.this.setOld.manifold1 + 0.2F * CockpitKI_48.this.fm.EI.engines[0].getManifoldPressure();
				CockpitKI_48.this.setNew.manifold2 = 0.8F * CockpitKI_48.this.setOld.manifold2 + 0.2F * CockpitKI_48.this.fm.EI.engines[1].getManifoldPressure();
				CockpitKI_48.this.setNew.altimeter = CockpitKI_48.this.fm.getAltitude();
				CockpitKI_48.this.setNew.azimuth.setDeg(CockpitKI_48.this.setOld.azimuth.getDeg(1.0F), -CockpitKI_48.this.fm.Or.azimut() - 90F);
				CockpitKI_48.this.w.set(CockpitKI_48.this.fm.getW());
				CockpitKI_48.this.fm.Or.transform(CockpitKI_48.this.w);
				CockpitKI_48.this.setNew.turn = (12F * CockpitKI_48.this.setOld.turn + CockpitKI_48.this.w.z) / 13F;
				CockpitKI_48.this.setNew.vspeed = (199F * CockpitKI_48.this.setOld.vspeed + CockpitKI_48.this.fm.getVertSpeed()) / 200F;
				float f = CockpitKI_48.this.waypointAzimuth();
				CockpitKI_48.this.setNew.waypointAzimuth.setDeg(CockpitKI_48.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitKI_48.this.setOld.azimuth.getDeg(1.0F) + World.Rnd().nextFloat(-10F, 10F));
				CockpitKI_48.this.setNew.waypointDirection.setDeg(CockpitKI_48.this.setOld.waypointDirection.getDeg(1.0F), f);
				CockpitKI_48.this.setNew.inert = 0.999F * CockpitKI_48.this.setOld.inert + 0.001F * (CockpitKI_48.this.fm.EI.engines[0].getStage() == 6 ? 0.867F : 0.0F);
				CockpitKI_48.this.w.set(CockpitKI_48.this.fm.getW());
				CockpitKI_48.this.fm.Or.transform(CockpitKI_48.this.w);
				CockpitKI_48.this.setNew.turn = (33F * CockpitKI_48.this.setOld.turn + CockpitKI_48.this.w.z) / 34F;
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
		float f;
		for (f = (float) (57.295779513082323D * Math.atan2(-this.tmpV.y, this.tmpV.x)); f <= -180F; f += 180F)
			;
		for (; f > 180F; f -= 180F)
			;
		return f;
	}

	public CockpitKI_48() {
		super("3DO/Cockpit/Ki48-II/Cockpitki48.him", "he111");
		this.bNeedSetUp = true;
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.tmpP = new Point3d();
		this.tmpV = new Vector3d();
		this.cockpitNightMats = new String[] { "Pressure_Gauges", "Needs_Instr", "maininstrumentspanel4", "instructions", "instru_main_pan", "instru_main_pan2", "instru_main_pan3", "dmaininstrumentspanel4", "dinstru_main_pan", "dinstru_main_pan2",
				"dinstru_main_pan3", "GP1_d1", "GP1", "GP7", "GP9" };
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
		this.limits6DoF = new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.03F, -0.025F };
		if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
		AircraftLH.printCompassHeading = true;
		this.limits6DoF = new float[] { 0.7F, 0.055F, -0.07F, 0.08F, 0.12F, -0.11F, 0.04F, -0.03F };
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
		this.mesh.chunkSetAngles("Z_Gears", this.fm.CT.GearControl > 0.5F ? -60F : 0.0F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throttle_L", -31F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throttle_R", -31F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Propitch_L", -720F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Propitch_R", -720F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Mixture_L", 41.67F * this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Mixture_R", 41.67F * this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.03F, 0.03F);
		this.mesh.chunkSetAngles("Z_AH3", 0.0F, 0.0F, this.fm.Or.getKren());
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.03F, 0.03F);
		this.mesh.chunkSetLocate("Z_AH4", Cockpit.xyz, Cockpit.ypr);
		this.w.set(this.fm.getW());
		this.fm.Or.transform(this.w);
		float f1 = this.getBall(7D);
		this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(f1, -5F, 5F, 8.5F, -8.5F), 0.0F, 0.0F);
		this.mesh.chunkVisible("X_LGearDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear);
		this.mesh.chunkVisible("X_RGearDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear);
		this.mesh.chunkVisible("X_LGearUp", this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkVisible("X_RGearUp", this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkVisible("X_TailGearDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.cgear);
		this.mesh.chunkVisible("X_TailGearUp", this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkSetAngles("Z_CColumn_Elev", 0.0F, -(this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 10F, 0.0F);
		this.mesh.chunkSetAngles("Z_CColumn", 0.0F, (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 15F, 0.0F);
		this.resetYPRmodifier();
		if (this.fm.CT.saveWeaponControl[3]) Cockpit.xyz[1] = -0.002545F;
		this.mesh.chunkSetLocate("Z_Trig1", Cockpit.xyz, Cockpit.ypr);
		this.resetYPRmodifier();
		Cockpit.xyz[1] = 0.05F * this.fm.CT.getRudder();
		this.mesh.chunkSetLocate("Z_Rudder_L", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[1] = -0.05F * this.fm.CT.getRudder();
		this.mesh.chunkSetLocate("Z_Rudder_R", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("Z_Magn_L", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, -85F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Magn_R", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 85F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Need_ClockHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_ClockMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_Turn", 0.0F, this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 21F, -21F), 0.0F);
		float f2 = this.getBall(4D);
		this.mesh.chunkSetAngles("Z_Need_Bank", 0.0F, this.cvt(f2, -4F, 4F, -13F, 13F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_Climb", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.0F, 12F), variometerScale), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_AirSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 700F, 0.0F, 14F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_LTachio", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 38.8F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_RTachio", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3500F, 0.0F, 38.8F), 0.0F);
		int i = 0;
		if (this.fm.EI.getCurControl(1) && !this.fm.EI.getCurControl(0)) i = 1;
		this.mesh.chunkSetAngles("Z_cyltempsel", 0.0F, i == 0 ? 0.0F : 54F, 0.0F);
		this.mesh.chunkSetAngles("Z_Need_cyltemp", 0.0F, this.cvt(this.fm.EI.engines[i].tWaterOut, 0.0F, 360F, 0.0F, 56F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_alt_km", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_Compass", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_FuelQuant", 0.0F, this.cvt(this.fm.M.fuel / this.fm.M.maxFuel, 0.0F, 1.0F, 5F, 335F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_L_oilpress", 0.0F, this.cvt(60F + this.fm.EI.engines[0].tOilIn * 0.222F, 0.0F, 100F, 0.0F, 209F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_R_oilpress", 0.0F, this.cvt(60F + this.fm.EI.engines[1].tOilIn * 0.222F, 0.0F, 100F, 0.0F, -212F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_L_oiltemp", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 130F, 0.0F, 57F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_R_oiltemp", 0.0F, this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 130F, 0.0F, 57F), 0.0F);
		this.mesh.chunkSetAngles("Z_Supercharger_L", 0.0F, -40F * this.fm.EI.engines[0].getControlCompressor() + 20F, 0.0F);
		this.mesh.chunkSetAngles("Z_Supercharger_R", 0.0F, -40F * this.fm.EI.engines[1].getControlCompressor() + 20F, 0.0F);
		this.mesh.chunkSetAngles("Z_Need_L_Manifold", 0.0F, 180F + this.cvt(this.setNew.manifold1, 0.200068F, 1.799932F, -164F, 164F), 0.0F);
		this.mesh.chunkSetAngles("Z_Need_R_Manifold", 0.0F, 180F + this.cvt(this.setNew.manifold2, 0.200068F, 1.799932F, -164F, 164F), 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[1] = 0.05F * this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 324F, 0.0F, 1.0F);
		this.mesh.chunkSetLocate("Z_Need_ExhGasAnalys1", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[1] = 0.05F * this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 324F, 0.0F, 1.0F);
		this.mesh.chunkSetLocate("Z_Need_ExhGasAnalys2", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkVisible("X_LGearDown", this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
		this.mesh.chunkVisible("X_RGearDown", this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear);
		this.mesh.chunkVisible("X_LGearUp", this.fm.CT.getGear() < 0.01F && this.fm.Gears.lgear);
		this.mesh.chunkVisible("X_RGearUp", this.fm.CT.getGear() < 0.01F && this.fm.Gears.rgear);
		this.mesh.chunkVisible("X_TailGearDown", this.fm.CT.getGear() > 0.99F && this.fm.Gears.cgear);
		this.mesh.chunkVisible("X_TailGearUp", this.fm.CT.getGear() < 0.01F && this.fm.Gears.cgear);
		this.mesh.chunkSetAngles("Z_Need_L_fuelpress", 0.0F, this.fm.M.fuel > 1.0F ? 129F : 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Need_R_fuelpress", 0.0F, this.fm.M.fuel > 1.0F ? -129F : 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 1) != 0) {
			this.mesh.chunkVisible("XGlassDamage2", true);
			this.mesh.chunkVisible("XGlassDamage3", true);
			this.mesh.chunkVisible("XGlassDamage4", true);
			this.mesh.chunkVisible("XHullDamage3", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("nstr2", false);
			this.mesh.chunkVisible("DInstr2", true);
			this.mesh.chunkVisible("Z_Fuel1", false);
			this.mesh.chunkVisible("Z_Need_L_oiltemp", false);
			this.mesh.chunkVisible("Z_Need_alt_km", false);
			this.mesh.chunkVisible("Z_Need_L_Manifold", false);
			this.mesh.chunkVisible("XHullDamage3", true);
		}
		if ((this.fm.AS.astateCockpitState & 4) != 0) {
			this.mesh.chunkVisible("XGlassDamage3", true);
			this.mesh.chunkVisible("XHullDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 8) != 0) {
			this.mesh.chunkVisible("XGlassDamage1", true);
			this.mesh.chunkVisible("XGlassDamage5", true);
			this.mesh.chunkVisible("XHullDamage3", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
			this.mesh.chunkVisible("XGlassDamage4", true);
			this.mesh.chunkVisible("XHullDamage2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
			this.mesh.chunkVisible("XGlassDamage1", true);
			this.mesh.chunkVisible("XGlassDamage6", true);
			this.mesh.chunkVisible("XHullDamage3", true);
		}
		if ((this.fm.AS.astateCockpitState & 2) != 0 && (this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("Panel_D0", false);
			this.mesh.chunkVisible("Panel_D1", false);
			this.mesh.chunkVisible("Panel_D2", true);
			this.mesh.chunkVisible("Z_Altimeter1", false);
			this.mesh.chunkVisible("Z_Altimeter2", false);
			this.mesh.chunkVisible("Z_Speedometer1", false);
			this.mesh.chunkVisible("Z_Speedometer2", false);
			this.mesh.chunkVisible("Z_AirTemp", false);
			this.mesh.chunkVisible("Z_Pres2", false);
			this.mesh.chunkVisible("Z_RPM1", false);
			this.mesh.chunkVisible("Z_RPM2", false);
			this.mesh.chunkVisible("Z_InertGas", false);
			this.mesh.chunkVisible("Z_FuelPres2", false);
			this.mesh.chunkVisible("Z_Oilpres1", false);
			this.mesh.chunkVisible("Z_Oilpres2", false);
		}
		this.retoggleLight();
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) {
			this.light1.light.setEmit(1.0F, 0.5F);
			this.light2.light.setEmit(1.0F, 0.5F);
			this.light3.light.setEmit(1.0F, 0.5F);
			this.light4.light.setEmit(1.0F, 0.5F);
			this.setNightMats(true);
		} else {
			this.light1.light.setEmit(0.0F, 0.0F);
			this.light2.light.setEmit(0.0F, 0.0F);
			this.light3.light.setEmit(0.0F, 0.0F);
			this.light4.light.setEmit(0.0F, 0.0F);
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

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
	}

	private boolean            bNeedSetUp;
	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;
	private LightPointActor    light1;
	private LightPointActor    light2;
	private LightPointActor    light3;
	private LightPointActor    light4;
	public Vector3f            w;
	private float              pictAiler;
	private float              pictElev;
	private static final float speedometerScale[] = { 0.0F, 8.6F, 23.6F, 64.2F, 114.5F, 172.8F, 239.4F, 299F, 360F, 417F, 479F, 533F, 582F, 627F, 657F };
	private static final float variometerScale[]  = { -180F, -162F, -143F, -125F, -108F, -90F, 0.0F, 90F, 108F, 125F, 143F, 162F, 180F };
	private Point3d            tmpP;
	private Vector3d           tmpV;
}
