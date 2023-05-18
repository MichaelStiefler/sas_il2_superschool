package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_111H20_Bombardier extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			float f = ((HE_111xyz) CockpitHE_111H20_Bombardier.this.aircraft()).fSightCurForwardAngle;
			float f1 = ((HE_111xyz) CockpitHE_111H20_Bombardier.this.aircraft()).fSightCurSideslip;
			CockpitHE_111H20_Bombardier.this.mesh.chunkSetAngles("BlackBox", 0.0F, -f1, f);
			if (CockpitHE_111H20_Bombardier.this.bEntered) {
				HookPilot hookpilot = HookPilot.current;
				hookpilot.setInstantOrient(CockpitHE_111H20_Bombardier.this.aAim + f1, CockpitHE_111H20_Bombardier.this.tAim + f, 0.0F);
			}
			CockpitHE_111H20_Bombardier.this.mesh.chunkSetAngles("TurretA", 0.0F, CockpitHE_111H20_Bombardier.this.aircraft().FM.turret[0].tu[0], 0.0F);
			CockpitHE_111H20_Bombardier.this.mesh.chunkSetAngles("TurretB", 0.0F, CockpitHE_111H20_Bombardier.this.aircraft().FM.turret[0].tu[1], 0.0F);
			return true;
		}

		Interpolater() {
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			((HE_111xyz) ((Interpolate) this.fm).actor).bPitUnfocused = false;
			this.bTurrVisible = this.aircraft().hierMesh().isChunkVisible("Turret1C_D0");
			this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Head1_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
			this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
			this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret7B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Window_D0", false);
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			Point3d point3d = new Point3d();
			point3d.set(0.18D, 0.06D, -0.12D);
			hookpilot.setTubeSight(point3d);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		if (!this.isFocused()) return;
		else {
			((HE_111xyz) ((Interpolate) this.fm).actor).bPitUnfocused = false;
			this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.bTurrVisible);
			this.aircraft().hierMesh().chunkVisible("Cockpit_D0", this.aircraft().hierMesh().isChunkVisible("Nose_D0") || this.aircraft().hierMesh().isChunkVisible("Nose_D1") || this.aircraft().hierMesh().isChunkVisible("Nose_D2"));
			this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
			this.aircraft().hierMesh().chunkVisible("Turret7B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Window_D0", true);
			this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
			this.aircraft().hierMesh().chunkVisible("Head1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
			this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
			this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
			this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
			this.leave();
			super.doFocusLeave();
			return;
		}
	}

	private void prepareToEnter() {
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock()) hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleAimOrient(-5F, -33F, 0.0F);
		this.enteringAim = true;
	}

	private void enter() {
		this.saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov 23.913");
		Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock()) hookpilot.stopPadlock();
		hookpilot.doAim(true);
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
			hookpilot.setInstantOrient(-5F, -33F, 0.0F);
			hookpilot.doAim(false);
			hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			return;
		}
		if (!this.bEntered) return;
		else {
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
			CmdEnv.top().exec("fov " + this.saveFov);
			HookPilot hookpilot1 = HookPilot.current;
			hookpilot1.setInstantOrient(-5F, -33F, 0.0F);
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

	public CockpitHE_111H20_Bombardier() {
		super("3DO/Cockpit/He-111H-20-Bombardier/hier_H20.him", "he111");
		this.bEntered = false;
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
		this.cockpitNightMats = new String[] { "clocks1", "clocks2", "clocks4", "clocks5" };
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) this.setNightMats(true);
		else this.setNightMats(false);
	}

	public void reflectWorldToInstruments(float f) {
		if (this.enteringAim) {
			HookPilot hookpilot = HookPilot.current;
			if (hookpilot.isAimReached()) {
				this.enteringAim = false;
				this.enter();
			} else if (!hookpilot.isAim()) this.enteringAim = false;
		}
		if (this.bEntered) {
			this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((HE_111xyz) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
			boolean flag = ((HE_111xyz) this.aircraft()).fSightCurReadyness > 0.93F;
			this.mesh.chunkVisible("BlackBox", true);
			this.mesh.chunkVisible("zReticle", flag);
			this.mesh.chunkVisible("zAngleMark", flag);
		} else {
			this.mesh.chunkVisible("BlackBox", false);
			this.mesh.chunkVisible("zReticle", false);
			this.mesh.chunkVisible("zAngleMark", false);
		}
		this.mesh.chunkSetAngles("zAltWheel", 0.0F, this.cvt(((HE_111xyz) this.aircraft()).fSightCurAltitude, 0.0F, 10000F, 0.0F, 375.8333F), 0.0F);
		this.mesh.chunkSetAngles("zAnglePointer", 0.0F, ((HE_111xyz) this.aircraft()).fSightCurForwardAngle, 0.0F);
		this.mesh.chunkSetAngles("zAngleWheel", 0.0F, -10F * ((HE_111xyz) this.aircraft()).fSightCurForwardAngle, 0.0F);
		this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 16F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("zSpeedPointer", 0.0F, this.cvt(((HE_111xyz) this.aircraft()).fSightCurSpeed, 150F, 600F, 0.0F, 60F), 0.0F);
		this.mesh.chunkSetAngles("zSpeedWheel", 0.0F, 0.333F * ((HE_111xyz) this.aircraft()).fSightCurSpeed, 0.0F);
		this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
		this.mesh.chunkSetAngles("zAlt2", -this.cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
		this.mesh.chunkVisible("zRed1", this.fm.CT.BayDoorControl > 0.66F);
		this.mesh.chunkVisible("zYellow1", this.fm.CT.BayDoorControl < 0.33F);
	}

	private static final float angleScale[]       = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
	private static final float speedometerScale[] = { 0.0F, 0.1F, 19F, 37.25F, 63.5F, 91.5F, 112F, 135.5F, 159.5F, 186.5F, 213F, 238F, 264F, 289F, 314.5F, 339.5F, 359.5F, 360F, 360F, 360F };
	private boolean            bTurrVisible;
	private float              saveFov;
	private float              aAim;
	private float              tAim;
	private boolean            bEntered;
	private boolean            enteringAim;

	static {
		Property.set(CockpitHE_111H20_Bombardier.class, "astatePilotIndx", 1);
	}
}
