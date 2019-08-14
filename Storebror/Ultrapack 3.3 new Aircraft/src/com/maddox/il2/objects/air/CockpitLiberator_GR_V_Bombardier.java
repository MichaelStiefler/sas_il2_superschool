package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitLiberator_GR_V_Bombardier extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			CockpitLiberator_GR_V_Bombardier.this.setTmp = CockpitLiberator_GR_V_Bombardier.this.setOld;
			CockpitLiberator_GR_V_Bombardier.this.setOld = CockpitLiberator_GR_V_Bombardier.this.setNew;
			CockpitLiberator_GR_V_Bombardier.this.setNew = CockpitLiberator_GR_V_Bombardier.this.setTmp;
			float f = ((Liberator_GR_V) CockpitLiberator_GR_V_Bombardier.this.aircraft()).fSightCurForwardAngle;
			float f1 = ((Liberator_GR_V) CockpitLiberator_GR_V_Bombardier.this.aircraft()).fSightCurSideslip;
			CockpitLiberator_GR_V_Bombardier.calibrAngle = 360F - CockpitLiberator_GR_V_Bombardier.this.fm.Or.getPitch();
			CockpitLiberator_GR_V_Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, CockpitLiberator_GR_V_Bombardier.calibrAngle + f);
			if (CockpitLiberator_GR_V_Bombardier.this.bEntered) {
				HookPilot hookpilot = HookPilot.current;
				hookpilot.setInstantOrient(CockpitLiberator_GR_V_Bombardier.this.aAim + 10F * f1, CockpitLiberator_GR_V_Bombardier.this.tAim + CockpitLiberator_GR_V_Bombardier.calibrAngle + f, 0.0F);
			}
			float f2 = CockpitLiberator_GR_V_Bombardier.this.waypointAzimuth();
			CockpitLiberator_GR_V_Bombardier.this.setNew.azimuth.setDeg(CockpitLiberator_GR_V_Bombardier.this.setOld.azimuth.getDeg(1.0F), CockpitLiberator_GR_V_Bombardier.this.fm.Or.azimut());
			if (CockpitLiberator_GR_V_Bombardier.this.useRealisticNavigationInstruments()) {
				CockpitLiberator_GR_V_Bombardier.this.setNew.waypointAzimuth.setDeg(f2 - 90F);
				CockpitLiberator_GR_V_Bombardier.this.setOld.waypointAzimuth.setDeg(f2 - 90F);
			} else CockpitLiberator_GR_V_Bombardier.this.setNew.waypointAzimuth.setDeg(CockpitLiberator_GR_V_Bombardier.this.setOld.waypointAzimuth.getDeg(0.1F), f2);
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		AnglesFork azimuth;
		AnglesFork waypointAzimuth;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
		}

		Variables(Variables variables) {
			this();
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			Point3d point3d = new Point3d();
			point3d.set(0.15D, 0.0D, -0.1D);
			hookpilot.setTubeSight(point3d);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		if (!this.isFocused()) return;
		else {
			this.leave();
			super.doFocusLeave();
			return;
		}
	}

	private void prepareToEnter() {
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock()) hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleAimOrient(0.0F, -33F, 0.0F);
		this.enteringAim = true;
	}

	private void enter() {
		this.saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov 23.913");
		Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
		HookPilot hookpilot = HookPilot.current;
		hookpilot.setInstantOrient(this.aAim, this.tAim, 0.0F);
		hookpilot.setSimpleUse(true);
		this.doSetSimpleUse(true);
		HotKeyEnv.enable("PanView", false);
		HotKeyEnv.enable("SnapView", false);
		this.bEntered = true;
	}

	private void leave() {
		if (this.enteringAim) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.setInstantOrient(0.0F, -33F, 0.0F);
			hookpilot.doAim(false);
			hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			return;
		}
		if (!this.bEntered) return;
		else {
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
			CmdEnv.top().exec("fov " + this.saveFov);
			HookPilot hookpilot1 = HookPilot.current;
			hookpilot1.setInstantOrient(0.0F, -33F, 0.0F);
			hookpilot1.doAim(false);
			hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			hookpilot1.setSimpleUse(false);
			this.doSetSimpleUse(false);
			boolean flag = HotKeyEnv.isEnabled("aircraftView");
			HotKeyEnv.enable("PanView", flag);
			HotKeyEnv.enable("SnapView", flag);
			this.bEntered = false;
			return;
		}
	}

	public void destroy() {
		super.destroy();
		this.leave();
	}

	public void doToggleAim(boolean flag) {
		if (!this.isFocused()) return;
		if (this.isToggleAim() == flag) return;
		if (flag) this.prepareToEnter();
		else this.leave();
	}

	public CockpitLiberator_GR_V_Bombardier() {
		super("3DO/Cockpit/B-25J-Bombardier/BombardierB24D.him", "bf109");
		this.enteringAim = false;
		this.bEntered = false;
		this.setOld = new Variables((Variables) null);
		this.setNew = new Variables((Variables) null);
		try {
			Loc loc = new Loc();
			HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
			hooknamed.computePos(this, this.pos.getAbs(), loc);
			this.aAim = loc.getOrient().getAzimut();
			this.tAim = loc.getOrient().getTangage();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		this.cockpitNightMats = new String[] { "textrbm9", "texture25" };
		this.setNightMats(false);
		this.interpPut(new Interpolater(), (Object) null, Time.current(), (Message) null);
		AircraftLH.printCompassHeading = true;
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) this.setNightMats(true);
		else this.setNightMats(false);
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("zSpeed1", 0.0F, this.floatindex(this.cvt(this.fm.getSpeedKMH(), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 10800F), 0.0F);
		this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 1080F), 0.0F);
		this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("zSecond", 0.0F, this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("zCompass1", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
		this.mesh.chunkSetAngles("zCompass2", 0.0F, this.setNew.waypointAzimuth.getDeg(0.1F), 0.0F);
		if (this.enteringAim) {
			HookPilot hookpilot = HookPilot.current;
			if (hookpilot.isAimReached()) {
				this.enteringAim = false;
				this.enter();
			} else if (!hookpilot.isAim()) this.enteringAim = false;
		}
		if (this.bEntered) {
			this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((Liberator_GR_V) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
			boolean flag = ((Liberator_GR_V) this.aircraft()).fSightCurReadyness > 0.93F;
			this.mesh.chunkVisible("BlackBox", true);
			this.mesh.chunkVisible("zReticle", flag);
			this.mesh.chunkVisible("zAngleMark", flag);
		} else {
			this.mesh.chunkVisible("BlackBox", false);
			this.mesh.chunkVisible("zReticle", false);
			this.mesh.chunkVisible("zAngleMark", false);
		}
	}

	private boolean            enteringAim;
	private static final float angleScale[]       = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
	private static final float speedometerScale[] = { 0.0F, 2.5F, 54F, 104F, 154.5F, 205.5F, 224F, 242F, 259.5F, 277.5F, 296.25F, 314F, 334F, 344.5F };
	private static float       calibrAngle        = 0.0F;
	private float              saveFov;
	private float              aAim;
	private float              tAim;
	private boolean            bEntered;
	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;

	static {
		Property.set(CockpitLiberator_GR_V_Bombardier.class, "astatePilotIndx", 0);
	}
}
