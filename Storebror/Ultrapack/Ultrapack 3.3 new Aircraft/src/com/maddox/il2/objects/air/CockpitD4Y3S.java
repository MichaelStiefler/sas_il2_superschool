package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitD4Y3S extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitD4Y3S.this.fm != null) {
				CockpitD4Y3S.this.setTmp = CockpitD4Y3S.this.setOld;
				CockpitD4Y3S.this.setOld = CockpitD4Y3S.this.setNew;
				CockpitD4Y3S.this.setNew = CockpitD4Y3S.this.setTmp;
				CockpitD4Y3S.this.setNew.gear = 0.7F * CockpitD4Y3S.this.setOld.gear + 0.3F * CockpitD4Y3S.this.fm.CT.GearControl;
				if (CockpitD4Y3S.this.cockpitDimControl) {
					if (CockpitD4Y3S.this.setNew.dimPosition > 0.0F) CockpitD4Y3S.this.setNew.dimPosition = CockpitD4Y3S.this.setOld.dimPosition - 0.05F;
				} else if (CockpitD4Y3S.this.setNew.dimPosition < 1.0F) CockpitD4Y3S.this.setNew.dimPosition = CockpitD4Y3S.this.setOld.dimPosition + 0.05F;
				if ((CockpitD4Y3S.this.fm.AS.astateCockpitState & 2) != 0) {
					if (CockpitD4Y3S.this.setNew.stbyPosition > 0.0F) CockpitD4Y3S.this.setNew.stbyPosition = CockpitD4Y3S.this.setOld.stbyPosition - 0.025F;
				} else if (CockpitD4Y3S.this.setNew.stbyPosition < 1.0F) CockpitD4Y3S.this.setNew.stbyPosition = CockpitD4Y3S.this.setOld.stbyPosition + 0.025F;
				CockpitD4Y3S.this.setNew.throttle = 0.8F * CockpitD4Y3S.this.setOld.throttle + 0.2F * CockpitD4Y3S.this.fm.CT.PowerControl;
				CockpitD4Y3S.this.setNew.prop = 0.8F * CockpitD4Y3S.this.setOld.prop + 0.2F * CockpitD4Y3S.this.fm.EI.engines[0].getControlProp();
				CockpitD4Y3S.this.setNew.mix = 0.8F * CockpitD4Y3S.this.setOld.mix + 0.2F * CockpitD4Y3S.this.fm.EI.engines[0].getControlMix();
				CockpitD4Y3S.this.setNew.man = 0.92F * CockpitD4Y3S.this.setOld.man + 0.08F * CockpitD4Y3S.this.fm.EI.engines[0].getManifoldPressure();
				CockpitD4Y3S.this.setNew.altimeter = CockpitD4Y3S.this.fm.getAltitude();
				float f = CockpitD4Y3S.this.waypointAzimuth();
				CockpitD4Y3S.this.setNew.azimuth.setDeg(CockpitD4Y3S.this.setOld.azimuth.getDeg(1.0F), CockpitD4Y3S.this.fm.Or.azimut());
				CockpitD4Y3S.this.setNew.waypointDeviation.setDeg(CockpitD4Y3S.this.setOld.waypointDeviation.getDeg(0.1F), f - CockpitD4Y3S.this.setOld.azimuth.getDeg(1.0F) + World.Rnd().nextFloat(-5F, 5F));
				CockpitD4Y3S.this.setNew.waypointAzimuth.setDeg(CockpitD4Y3S.this.setOld.waypointAzimuth.getDeg(1.0F), f);
				CockpitD4Y3S.this.setNew.vspeed = 0.5F * CockpitD4Y3S.this.setOld.vspeed + 0.5F * CockpitD4Y3S.this.fm.getVertSpeed();
				CockpitD4Y3S.this.mesh.chunkSetAngles("Turret1A_D0", 0.0F, -CockpitD4Y3S.this.aircraft().FM.turret[0].tu[0], 0.0F);
				CockpitD4Y3S.this.mesh.chunkSetAngles("Turret1B_D0", 0.0F, CockpitD4Y3S.this.aircraft().FM.turret[0].tu[1], 0.0F);
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float      dimPosition;
		float      stbyPosition;
		float      gear;
		float      throttle;
		float      prop;
		float      mix;
		float      altimeter;
		float      man;
		float      vspeed;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork waypointDeviation;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
			this.waypointDeviation = new AnglesFork();
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

	public CockpitD4Y3S() {
		super("3DO/Cockpit/D4Y3S/hier.him", "bf109");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.bNeedSetUp = true;
		this.tmpP = new Point3d();
		this.tmpV = new Vector3d();
		this.cockpitNightMats = new String[] { "gauges1_d1", "gauges1", "gauges2_d1", "gauges2", "gauges3_d1", "gauges3", "gauges4_d1", "gauges4", "gauges5", "gauges6", "gauges7", "turnbank_d1", "turnbank" };
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
		if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.64F);
		this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("sunOFF", 0.0F, this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -77F), 0.0F);
		this.mesh.chunkSetAngles("sight_rev", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition, this.setOld.stbyPosition, f), 0.0F, 1.0F, 0.0F, -115F), 0.0F);
		this.mesh.chunkSetAngles("Z_Throtle1", 64.5F * this.setNew.throttle, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Prop1", 58.25F * this.setNew.prop, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Mixture1", 48F * this.setNew.mix, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Pedal", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Columnbase", (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 8F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F);
		this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 21600F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 2160F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 340F, 0.0F, 17F), speedometerScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass1", this.cvt(-this.fm.Or.getKren(), -45F, 45F, -45F, 45F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass2", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass3", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass4", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass5", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Compass6", this.cvt(this.setNew.waypointDeviation.getDeg(f), -25F, 25F, 45F, -45F), 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.cvt(this.setNew.vspeed, -15F, 15F, -0.053F, 0.053F);
		this.mesh.chunkSetLocate("Z_Climb1", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 500F, 4500F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Manifold1", this.cvt(this.setNew.man, 0.400051F, 1.333305F, -202.5F, 112.5F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurnBank0", 0.0F, -this.fm.Or.getKren(), 0.0F);
		this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.fm.Or.getTangage(), -52F, 52F, 26F, -26F), 0.0F, 0.0F);
		this.w.set(this.fm.getW());
		this.fm.Or.transform(this.w);
		this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(this.getBall(6D), -6F, 6F, 14F, -14F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Temp1", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 7F), oilScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Oil1", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 5.5F, 0.0F, 180F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_fuelpress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 2.0F, 0.0F, 180F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel, 0.0F, 500F, 0.0F, 235F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Fuel2", this.cvt(this.fm.M.fuel, 0.0F, 160F, 0.0F, 256F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Oxypres1", 90F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Oxyquan1", 90F, 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
			this.mesh.chunkVisible("Z_OilSplats1_D1", true);
			this.mesh.chunkVisible("Z_OilSplats2_D1", true);
			this.mesh.chunkVisible("XHullDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 2) != 0) {
			this.mesh.chunkVisible("XGlassDamage4", true);
			this.mesh.chunkVisible("XHullDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 1) != 0) {
			this.mesh.chunkVisible("XGlassDamage1", true);
			this.mesh.chunkVisible("XGlassDamage3", true);
			this.mesh.chunkVisible("XHullDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("Panel_D0", false);
			this.mesh.chunkVisible("Panel_D1", true);
			this.mesh.chunkVisible("Z_Speedometer1", false);
			this.mesh.chunkVisible("Z_TurnBank3", false);
			this.mesh.chunkVisible("Z_Hour1", false);
			this.mesh.chunkVisible("Z_Minute1", false);
			this.mesh.chunkVisible("Z_Oil1", false);
			this.mesh.chunkVisible("Z_fuelpress1", false);
			this.mesh.chunkVisible("Z_Temp1", false);
			this.mesh.chunkVisible("Z_Manifold1", false);
			this.mesh.chunkVisible("Z_RPM1", false);
			this.mesh.chunkVisible("XHullDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 4) != 0) {
			this.mesh.chunkVisible("XHullDamage1", true);
			this.mesh.chunkVisible("XHullDamage3", true);
		}
		if ((this.fm.AS.astateCockpitState & 8) != 0) {
			this.mesh.chunkVisible("XGlassDamage2", true);
			this.mesh.chunkVisible("XHullDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
			this.mesh.chunkVisible("XHullDamage1", true);
			this.mesh.chunkVisible("XHullDamage2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
			this.mesh.chunkVisible("XGlassDamage2", true);
			this.mesh.chunkVisible("XHullDamage1", true);
		}
	}

	public void toggleDim() {
		this.cockpitDimControl = !this.cockpitDimControl;
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) this.setNightMats(true);
		else this.setNightMats(false);
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
		this.mesh.materialReplace("Matt1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
		this.mesh.materialReplace("Pilot2", mat);
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		this.mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
		this.mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
		this.mesh.chunkVisible("Turret1B_D0", hiermesh.isChunkVisible("Turret1B_D0"));
	}

	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;
	public Vector3f            w;
	private float              pictAiler;
	private float              pictElev;
	private boolean            bNeedSetUp;
	private static final float speedometerScale[] = { 0.0F, 13F, 28.5F, 62F, 105F, 157.5F, 213F, 273.5F, 332F, 388F, 445.7F, 499F, 549.5F, 591.5F, 633F, 671F, 688.5F, 698F };
	private static final float oilScale[]         = { 0.0F, -27.5F, 12F, 59.5F, 127F, 212.5F, 311.5F };
	private Point3d            tmpP;
	private Vector3d           tmpV;
}
