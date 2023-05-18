package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitD520 extends CockpitPilot {
	private class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitD520.this.fm != null) {
				CockpitD520.this.setTmp = CockpitD520.this.setOld;
				CockpitD520.this.setOld = CockpitD520.this.setNew;
				CockpitD520.this.setNew = CockpitD520.this.setTmp;
				CockpitD520.this.setNew.throttle = 0.92F * CockpitD520.this.setOld.throttle + 0.08F * CockpitD520.this.fm.CT.PowerControl;
				CockpitD520.this.setNew.prop = 0.92F * CockpitD520.this.setOld.prop + 0.08F * CockpitD520.this.fm.EI.engines[0].getControlProp();
				CockpitD520.this.setNew.mix = 0.92F * CockpitD520.this.setOld.mix + 0.08F * CockpitD520.this.fm.EI.engines[0].getControlMix();
				CockpitD520.this.setNew.altimeter = CockpitD520.this.fm.getAltitude();
				CockpitD520.this.setNew.azimuth = 0.97F * CockpitD520.this.setOld.azimuth + 0.03F * -CockpitD520.this.fm.Or.getYaw();
				if (CockpitD520.this.setOld.azimuth > 270F && CockpitD520.this.setNew.azimuth < 90F) CockpitD520.this.setOld.azimuth -= 360F;
				if (CockpitD520.this.setOld.azimuth < 90F && CockpitD520.this.setNew.azimuth > 270F) CockpitD520.this.setOld.azimuth += 360F;
				CockpitD520.this.setNew.waypointAzimuth = 0.91F * CockpitD520.this.setOld.waypointAzimuth + 0.09F * (CockpitD520.this.waypointAzimuth() - CockpitD520.this.setOld.azimuth) + World.Rnd().nextFloat(-10F, 10F);
				CockpitD520.this.setNew.vspeed = 0.99F * CockpitD520.this.setOld.vspeed + 0.01F * CockpitD520.this.fm.getVertSpeed();
				if (CockpitD520.this.fm.CT.GearControl < 0.5F) {
					if (CockpitD520.this.setNew.gearPhi > 0.0F) CockpitD520.this.setNew.gearPhi = CockpitD520.this.setOld.gearPhi - 0.021F;
				} else if (CockpitD520.this.setNew.gearPhi < 1.0F) CockpitD520.this.setNew.gearPhi = CockpitD520.this.setOld.gearPhi + 0.021F;
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float throttle;
		float prop;
		float mix;
		float altimeter;
		float azimuth;
		float vspeed;
		float gearPhi;
		float waypointAzimuth;

		private Variables() {
		}

	}

	protected float waypointAzimuth() {
		WayPoint waypoint = this.fm.AP.way.curr();
		if (waypoint == null) return 0.0F;
		else {
			waypoint.getP(this.tmpP);
			this.tmpV.sub(this.tmpP, this.fm.Loc);
			return (float) (57.295779513082323D * Math.atan2(-this.tmpV.y, this.tmpV.x));
		}
	}

	public CockpitD520() {
		super("3DO/Cockpit/D520/hier.him", "bf109");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.pictFlap = 0.0F;
		this.pictManf = 1.0F;
		this.bNeedSetUp = true;
		this.tmpP = new Point3d();
		this.tmpV = new Vector3d();
		this.cockpitNightMats = new String[] { "TEMPPIT5-op", "TEMPPIT6-op", "TEMPPIT14-op", "TEMPPIT18-op", "TEMPPIT22-op", "TEMPPIT28-op", "TEMPPIT38-op", "TEMPPIT1-tr", "TEMPPIT2-tr", "TEMPPIT3-tr", "TEMPPIT4-tr", "TEMPPIT5-tr", "TEMPPIT6-tr",
				"TEMPPIT14-tr", "TEMPPIT18-tr", "TEMPPIT22-tr", "TEMPPIT28-tr", "TEMPPIT38-tr", "TEMPPIT1_damage", "TEMPPIT3_damage", "gauge3" };
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
//        Cockpit.xyz[1] = cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.55F);
//        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 1.0F, 0.0F, 0.627F);
		this.mesh.chunkSetLocate("Top", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkVisible("XLampGearUpR", this.fm.CT.getGear() > 0.01F && this.fm.CT.getGear() < 0.99F || !this.fm.Gears.rgear);
		this.mesh.chunkVisible("XLampGearUpL", this.fm.CT.getGear() > 0.01F && this.fm.CT.getGear() < 0.99F || !this.fm.Gears.lgear);
		this.mesh.chunkVisible("XLampGearDownR", this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear);
		this.mesh.chunkVisible("XLampGearDownL", this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
		this.mesh.chunkSetAngles("Z_Columnbase", 16F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Column", 25F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 0.0F, 0.0F);
		this.resetYPRmodifier();
		this.mesh.chunkSetAngles("Z_Throtle1", 65.45F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BasePedal", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
		float f1;
		if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
			if (this.fm.CT.FlapsControl - this.fm.CT.getFlap() > 0.0F) f1 = 24F;
			else f1 = -24F;
		} else f1 = 0.0F;
		this.mesh.chunkSetAngles("Z_Flaps1", this.pictFlap = 0.8F * this.pictFlap + 0.2F * f1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Prop1", 72.5F * this.setNew.prop, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELK_V_LONG", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 700F, 0.0F, 35F), speedometerScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STREL_ALT_LONG", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STREL_ALT_SHORT", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("SHKALA_DIRECTOR", this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELKA_VY", -this.floatindex(this.cvt(this.setNew.vspeed, -12F, 20F, 0.0F, 17F), variometerScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELKA_BOOST", this.cvt(this.pictManf = 0.91F * this.pictManf + 0.09F * this.fm.EI.engines[0].getManifoldPressure(), 0.4F, 1.6F, 60F, -240F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELKA_FUEL1", this.cvt(this.fm.M.fuel, 0.0F, 300F, 0.0F, -295F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELKA_RPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3200F, 0.0F, 16F), rpmScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELK_TEMP_OIL", -this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 94F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELK_TEMP_RAD", -this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 94F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STR_OIL_LB", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, -36F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELK_TURN_UP", -this.cvt(this.getBall(8D), -8F, 8F, 12F, -12F), 0.0F, 0.0F);
		this.w.set(this.fm.getW());
		this.fm.Or.transform(this.w);
		this.mesh.chunkSetAngles("STREL_TURN_DOWN", -this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.022F, -0.022F);
		this.mesh.chunkSetAngles("STRELKA_HOUR", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELKA_MINUTE", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STRELKA_SECUND", -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("Z_OilSplats_D1", true);
		if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage4", true);
		if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("Panel_D0", false);
			this.mesh.chunkVisible("Panel_D1", true);
			this.mesh.chunkVisible("STRELK_V_LONG", false);
			this.mesh.chunkVisible("STREL_ALT_LONG", false);
			this.mesh.chunkVisible("STREL_ALT_SHORT", false);
			this.mesh.chunkVisible("STRELKA_VY", false);
			this.mesh.chunkVisible("STRELKA_RPM", false);
			this.mesh.chunkVisible("STRELK_TEMP_RAD", false);
			this.mesh.chunkVisible("STRELK_TEMP_OIL", false);
		}
		if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
		if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
		this.retoggleLight();
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
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
	private float              pictFlap;
	private float              pictManf;
	private boolean            bNeedSetUp;
	private static final float speedometerScale[] = { 0.0F, 5F, 10F, 15F, 20F, 23F, 33F, 43F, 56F, 69F, 84F, 100F, 117F, 136F, 155F, 174F, 195F, 217F, 240F, 262F, 286F, 310F, 335F, 361F, 388F, 415F, 442F, 470F, 498F, 526F, 553F, 581F, 608F, 635F, 662F,
			690F };
	private static final float rpmScale[]         = { 0.0F, 2.0F, 18F, 42F, 66F, 90F, 114F, 138F, 162F, 186F, 210F, 234F, 258F, 282F, 306F, 330F, 354F };
	private static final float variometerScale[]  = { -108F, -90F, -72F, -45F, -39F, -18F, 0.0F, 18F, 39F, 39F, 45F, 72F, 90F, 108F, 126F, 144F, 162F, 180F };
	private Point3d            tmpP;
	private Vector3d           tmpV;
}
