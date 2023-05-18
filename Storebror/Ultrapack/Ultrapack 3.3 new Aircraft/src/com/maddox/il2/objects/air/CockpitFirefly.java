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
import com.maddox.sound.ReverbFXRoom;

public class CockpitFirefly extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitFirefly.this.fm != null) {
				CockpitFirefly.this.setTmp = CockpitFirefly.this.setOld;
				CockpitFirefly.this.setOld = CockpitFirefly.this.setNew;
				CockpitFirefly.this.setNew = CockpitFirefly.this.setTmp;
				CockpitFirefly.this.setNew.throttle = 0.92F * CockpitFirefly.this.setOld.throttle + 0.08F * CockpitFirefly.this.fm.CT.PowerControl;
				CockpitFirefly.this.setNew.prop = 0.92F * CockpitFirefly.this.setOld.prop + 0.08F * CockpitFirefly.this.fm.EI.engines[0].getControlProp();
				CockpitFirefly.this.setNew.mix = 0.92F * CockpitFirefly.this.setOld.mix + 0.08F * CockpitFirefly.this.fm.EI.engines[0].getControlMix();
				CockpitFirefly.this.setNew.altimeter = CockpitFirefly.this.fm.getAltitude();
				if (Math.abs(CockpitFirefly.this.fm.Or.getKren()) < 30F) CockpitFirefly.this.setNew.azimuth = 0.97F * CockpitFirefly.this.setOld.azimuth + 0.03F * -CockpitFirefly.this.fm.Or.getYaw();
				if (CockpitFirefly.this.setOld.azimuth > 270F && CockpitFirefly.this.setNew.azimuth < 90F) CockpitFirefly.this.setOld.azimuth -= 360F;
				if (CockpitFirefly.this.setOld.azimuth < 90F && CockpitFirefly.this.setNew.azimuth > 270F) CockpitFirefly.this.setOld.azimuth += 360F;
				CockpitFirefly.this.setNew.waypointAzimuth = 0.91F * CockpitFirefly.this.setOld.waypointAzimuth + 0.09F * (CockpitFirefly.this.waypointAzimuth() - CockpitFirefly.this.setOld.azimuth) + World.Rnd().nextFloat(-10F, 10F);
				CockpitFirefly.this.setNew.vspeed = 0.99F * CockpitFirefly.this.setOld.vspeed + 0.01F * CockpitFirefly.this.fm.getVertSpeed();
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

	public CockpitFirefly() {
		super("3DO/Cockpit/Firefly/hier.him", "bf109");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.pictBrake = 0.0F;
		this.pictFlap = 0.0F;
		this.pictGear = 0.0F;
		this.pictManf = 1.0F;
		this.bNeedSetUp = true;
		this.tmpP = new Point3d();
		this.tmpV = new Vector3d();
		this.cockpitNightMats = new String[] { "COMPASS", "BORT2", "prib_five", "prib_five_damage", "prib_one", "prib_one_damage", "prib_three", "prib_three_damage", "prib_two", "prib_two_damage", "text13", "text15" };
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
		if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.2F);
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.reflectPlaneToModel();
			this.bNeedSetUp = false;
		}
		if (this.aircraft() instanceof Firefly) if (Firefly.bChangedPit) {
			this.reflectPlaneToModel();
			Firefly.bChangedPit = false;
		}
		this.resetYPRmodifier();
		Cockpit.xyz[1] = this.cvt(this.fm.CT.arrestorControl, 0.01F, 0.99F, 0.0F, 0.03F);
		this.mesh.chunkSetLocate("Arrester1", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkVisible("XLampArrest", this.fm.CT.getArrestor() > 0.9F);
		this.resetYPRmodifier();
		Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
		this.mesh.chunkSetLocate("Blister_D0", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkVisible("XLampGearUpL", this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkVisible("XLampGearDownL", this.fm.CT.getGear() == 1.0F);
		this.mesh.chunkVisible("XLampFuel", this.fm.M.fuel < 0.25F * this.fm.M.maxFuel);
		this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 8F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl), 0.0F);
		this.mesh.chunkSetAngles("Z_Column", -30F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Shlang01a", -5F * this.pictAiler, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Shlang01b", -9F * this.pictAiler, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Shlang01c", -12F * this.pictAiler, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Shlang02a", -5F * this.pictAiler, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Shlang02b", -7.5F * this.pictAiler, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Shlang02c", -15F * this.pictAiler, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Shlang03a", -5F * this.pictAiler, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Shlang03b", -8.5F * this.pictAiler, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Shlang03c", -18.5F * this.pictAiler, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Stick_Shtok01", 0.0F, 0.0F, 8F * this.pictElev);
		this.mesh.chunkSetAngles("Z_ColumnSwitch", -18F * (this.pictBrake = 0.89F * this.pictBrake + 0.11F * this.fm.CT.BrakeControl), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throtle1", -64.5454F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BasePedal", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = 0.0578F * this.fm.CT.getRudder();
		this.mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
		Cockpit.xyz[2] = -0.0578F * this.fm.CT.getRudder();
		this.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("Z_Gear1", -160F + 160F * (this.pictGear = 0.89F * this.pictGear + 0.11F * this.fm.CT.GearControl), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Flaps1", 160F * (this.pictFlap = 0.89F * this.pictFlap + 0.11F * this.fm.CT.FlapsControl), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Trim1", 1000F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Trim2", 1000F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Prop1", -90F * this.setNew.prop, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Mixture1", -60F * this.setNew.mix, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("COMPASS_M", -this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("SHKALA_DIRECTOR", this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("STREL_ALT_LONG", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
		this.mesh.chunkSetAngles("STREL_ALT_SHORT", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
		this.mesh.chunkSetAngles("STREL_ALT_SHRT1", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -108F));
		this.mesh.chunkSetAngles("STRELKA_BOOST", 0.0F, 0.0F, -this.cvt(this.pictManf = 0.91F * this.pictManf + 0.09F * this.fm.EI.engines[0].getManifoldPressure(), 0.5173668F, 2.72369F, -70F, 250F));
		this.mesh.chunkSetAngles("STRELKA_FUEL", 0.0F, 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 378.54F, 0.0F, 68F));
		this.mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 1000F, 5000F, 2.0F, 10F), rpmScale));
		this.mesh.chunkSetAngles("STRELK_TEMP_OIL", 0.0F, 0.0F, -this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 100F, 0.0F, 271F));
		this.mesh.chunkSetAngles("STRELK_TEMP_RAD", 0.0F, 0.0F, -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), radScale));
		this.mesh.chunkSetAngles("STR_OIL_LB", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, -37F), 0.0F);
		this.w.set(this.fm.getW());
		this.fm.Or.transform(this.w);
		this.mesh.chunkSetAngles("STREL_TURN_DOWN", 0.0F, 0.0F, -this.cvt(this.w.z, -0.23562F, 0.23562F, -48F, 48F));
		this.mesh.chunkSetAngles("STRELK_TURN_UP", 0.0F, 0.0F, -this.cvt(this.getBall(8D), -8F, 8F, 35F, -35F));
		this.mesh.chunkVisible("STRELK_V_SHORT", false);
		this.mesh.chunkSetAngles("STRELK_V_LONG", 0.0F, 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 257.2222F, 0.0F, 25F), speedometerScale));
		this.mesh.chunkSetAngles("STRELKA_VY", 0.0F, 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale));
		this.mesh.chunkSetAngles("STRELKA_GOR", 0.0F, 0.0F, this.fm.Or.getKren());
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.032F, -0.032F);
		this.mesh.chunkSetLocate("STRELKA_GOS", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("STRELKA_HOUR", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		this.mesh.chunkSetAngles("STRELKA_MINUTE", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("STRELKA_SECUND", 0.0F, this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("STR_CLIMB", 0.0F, 0.0F, this.cvt(this.fm.CT.trimElevator, -0.5F, 0.5F, -35.23F, 35.23F));
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("Z_OilSplats_D1", true);
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		this.mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
		this.mesh.materialReplace("Gloss1D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
		this.mesh.materialReplace("Gloss2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
		this.mesh.materialReplace("Gloss2D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
		this.mesh.materialReplace("Gloss2D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
		this.mesh.materialReplace("Matt1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
		this.mesh.materialReplace("Matt1D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
		this.mesh.materialReplace("Matt2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
		this.mesh.materialReplace("Matt2D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Overlay6"));
		this.mesh.materialReplace("Overlay6", mat);
		mat = hiermesh.material(hiermesh.materialFind("Overlay7"));
		this.mesh.materialReplace("Overlay7", mat);
		mat = hiermesh.material(hiermesh.materialFind("OverlayD1o"));
		this.mesh.materialReplace("OverlayD1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("OverlayD2o"));
		this.mesh.materialReplace("OverlayD2o", mat);
	}

	protected void reflectPlaneToModel() {
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) this.setNightMats(true);
		else this.setNightMats(false);
	}

	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;
	public Vector3f            w;
	private float              pictAiler;
	private float              pictElev;
	private float              pictBrake;
	private float              pictFlap;
	private float              pictGear;
	private float              pictManf;
	private boolean            bNeedSetUp;
	private static final float speedometerScale[] = { 0.0F, 7.5F, 17.5F, 37F, 63F, 88.5F, 114.5F, 143F, 171.5F, 202.5F, 228.5F, 255.5F, 282F, 309F, 336.5F, 366.5F, 394F, 421F, 448.5F, 474.5F, 500.5F, 530F, 557.5F, 584F, 609F, 629F };
	private static final float radScale[]         = { 0.0F, 3F, 7F, 13.5F, 21.5F, 27F, 34.5F, 50.5F, 71F, 94F, 125F, 161F, 202.5F, 253F, 315.5F };
	private static final float rpmScale[]         = { 0.0F, 0.0F, 0.0F, 22F, 58F, 103.5F, 152.5F, 193.5F, 245F, 281.5F, 311.5F };
	private static final float variometerScale[]  = { -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F };
	private Point3d            tmpP;
	private Vector3d           tmpV;
}
