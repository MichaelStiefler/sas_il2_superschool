/*
 * CockpitPilot - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitPilot extends Cockpit {
	private String[] hotKeyEnvs = { "pilot", "move" };
	protected float stepAzimut = 45.0F;
	protected float stepTangage = 30.0F;
	protected float minMaxAzimut = 145.0F;
	protected float maxTangage = 90.0F;
	protected float minTangage = -60.0F;
	protected Point3d cameraCenter = new Point3d();
	private Point3d cameraAim;
	private Point3d cameraUp;
	private boolean bBeaconKeysEnabled;
	private float saveZN;
	protected float normZN;
	protected float gsZN;
	private double pictBall = 0.0;
	private long oldBallTime = 0L;

	class Interpolater extends InterpolateRef {
		public boolean tick() {
			if (isPadlock())
				HookPilot.current.checkPadlockState();
			return true;
		}
	}

	public boolean isEnableHotKeysOnOutsideView() {
		return true;
	}

	public String[] getHotKeyEnvs() {
		return hotKeyEnvs;
	}

	private float ZNear(float f) {
		if (f < 0.0F)
			return -1.0F;
		Camera3D camera3d = (Camera3D) Actor.getByName("camera");
		float f_0_ = camera3d.ZNear;
		camera3d.ZNear = f;
		return f_0_;
	}

	protected boolean doFocusEnter() {
		HookPilot hookpilot = HookPilot.current;
		Aircraft aircraft = aircraft();
		Main3D main3d = Main3D.cur3D();
		hookpilot.setCenter(cameraCenter);
		hookpilot.setAim(cameraAim);
		hookpilot.setUp(cameraUp);
		if (!NetMissionTrack.isPlaying() || NetMissionTrack.playingOriginalVersion() > 101) {
			hookpilot.setSteps(stepAzimut, stepTangage);
			hookpilot.setMinMax(minMaxAzimut, minTangage, maxTangage);
		} else {
			hookpilot.setSteps(45.0F, 30.0F);
			hookpilot.setMinMax(135.0F, -60.0F, 90.0F);
		}
		hookpilot.reset();
		hookpilot.use(true);
		aircraft.setAcoustics(acoustics);
		if (acoustics != null) {
			aircraft.enableDoorSnd(true);
			if (acoustics.getEnvNum() == 2)
				aircraft.setDoorSnd(1.0F);
		}
		main3d.camera3D.pos.setRel(new Point3d(), new Orient());
		main3d.camera3D.pos.setBase(aircraft, hookpilot, false);
		main3d.camera3D.pos.resetAsBase();
		pos.resetAsBase();
		aircraft().setMotorPos(main3d.camera3D.pos.getAbsPoint());
		main3d.cameraCockpit.pos.setRel(new Point3d(), new Orient());
		main3d.cameraCockpit.pos.setBase(this, hookpilot, false);
		main3d.cameraCockpit.pos.resetAsBase();
		main3d.overLoad.setShow(true);
		main3d.renderCockpit.setShow(true);
		aircraft.drawing(!isNullShow());
		saveZN = ZNear(HookPilot.current.isAim() ? gsZN : normZN);
		bBeaconKeysEnabled = ((AircraftLH) aircraft()).bWantBeaconKeys;
		((AircraftLH) aircraft()).bWantBeaconKeys = true;
		return true;
	}

	protected void doFocusLeave() {
		saveZN = ZNear(saveZN);
		((AircraftLH) aircraft()).bWantBeaconKeys = bBeaconKeysEnabled;
		HookPilot hookpilot = HookPilot.current;
		Aircraft aircraft = aircraft();
		Main3D main3d = Main3D.cur3D();
		hookpilot.use(false);
		main3d.camera3D.pos.setRel(new Point3d(), new Orient());
		main3d.camera3D.pos.setBase(null, null, false);
		main3d.cameraCockpit.pos.setRel(new Point3d(), new Orient());
		main3d.cameraCockpit.pos.setBase(null, null, false);
		main3d.overLoad.setShow(false);
		main3d.renderCockpit.setShow(false);
		if (Actor.isValid(aircraft))
			aircraft.drawing(true);
		if (aircraft != null)
			aircraft.setAcoustics(null);
		aircraft.enableDoorSnd(false);
		aircraft().setMotorPos(null);
	}

	public boolean existPadlock() {
		return true;
	}

	public boolean isPadlock() {
		if (!isFocused())
			return false;
		HookPilot hookpilot = HookPilot.current;
		return hookpilot.isPadlock();
	}

	public Actor getPadlockEnemy() {
		if (!isFocused())
			return null;
		HookPilot hookpilot = HookPilot.current;
		return hookpilot.isPadlock() ? hookpilot.getEnemy() : null;
	}

	public boolean startPadlock(Actor actor) {
		if (!isFocused())
			return false;
		HookPilot hookpilot = HookPilot.current;
		return hookpilot.startPadlock(actor);
	}

	public void stopPadlock() {
		if (isFocused()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.stopPadlock();
		}
	}

	public void endPadlock() {
		if (isFocused()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.endPadlock();
		}
	}

	public void setPadlockForward(boolean bool) {
		if (isFocused()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.setForward(bool);
		}
	}

	public boolean isToggleAim() {
		if (!isFocused())
			return false;
		HookPilot hookpilot = HookPilot.current;
		return hookpilot.isAim();
	}

	public void doToggleAim(boolean bool) {
		if (isFocused()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(bool);
			if (bool)
				ZNear(gsZN);
			else
				ZNear(normZN);
		}
	}

	public boolean isToggleUp() {
		if (!isFocused())
			return false;
		HookPilot hookpilot = HookPilot.current;
		return hookpilot.isUp();
	}

	public void doToggleUp(boolean bool) {
		if (isFocused() && (!bool || cameraUp != null)) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doUp(bool);
		}
	}

	public CockpitPilot(String string, String string_1_) {
		super(string, string_1_);
		HookNamed hooknamed = new HookNamed(mesh, "CAMERA");
		Loc loc = new Loc();
		hooknamed.computePos(this, pos.getAbs(), loc);
		loc.get(cameraCenter);
		try {
			hooknamed = new HookNamed(mesh, "CAMERAAIM");
			loc.set(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
			hooknamed.computePos(this, pos.getAbs(), loc);
			cameraAim = new Point3d();
			loc.get(cameraAim);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		try {
			hooknamed = new HookNamed(mesh, "CAMERAUP");
			loc.set(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
			hooknamed.computePos(this, pos.getAbs(), loc);
			cameraUp = new Point3d();
			loc.get(cameraUp);
		} catch (Exception exception) {
			/* empty */
		}
		pos.setBase(aircraft(), new Cockpit.HookOnlyOrient(), false);
		interpPut(new Interpolater(), "CockpitPilot", Time.current(), null);
		if (HookPilot.current != null)
			HookPilot.current.doUp(false);
		normZN = Property.floatValue(this.getClass(), "normZN", -1.0F);
		gsZN = Property.floatValue(this.getClass(), "gsZN", -1.0F);

		// TODO: Added by |ZUTI|: reset multi crew bomber position
		// -----------------------------------------------------------
		if (string.indexOf("Bombardier") > -1) {
			pos.setAbs(new Point3d(0, 0, 0), new Orient(0, 0, 0));
			// System.out.println("BOMBARDIER COCKPIT POSITION RESET!");
		}
		// -----------------------------------------------------------
	}

	protected float getBall(double d) {
		double d_2_ = 0.0;
		long l = Time.current();
		long l_3_ = l - oldBallTime;
		oldBallTime = l;
		if (l_3_ > 200L)
			l_3_ = 200L;
		double d_4_ = 3.8E-4 * l_3_;
		if (-fm.getBallAccel().z > 0.0010) {
			d_2_ = Math.toDegrees(Math.atan2(fm.getBallAccel().y, -fm.getBallAccel().z));
			if (d_2_ > 20.0)
				d_2_ = 20.0;
			else if (d_2_ < -20.0)
				d_2_ = -20.0;
			pictBall = (1.0 - d_4_) * pictBall + d_4_ * d_2_;
		} else {
			if (pictBall > 0.0)
				d_2_ = 20.0;
			else
				d_2_ = -20.0;
			pictBall = (1.0 - d_4_) * pictBall + d_4_ * d_2_;
		}
		if (pictBall > d)
			pictBall = d;
		else if (pictBall < -d)
			pictBall = -d;
		return (float) pictBall;
	}

	static {
		Property.set(CockpitPilot.class, "astatePilotIndx", 0);
	}
}
