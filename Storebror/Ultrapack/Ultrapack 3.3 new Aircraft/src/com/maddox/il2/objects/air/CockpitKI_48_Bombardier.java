package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitKI_48_Bombardier extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitKI_48_Bombardier.this.bEntered) {
				float f = ((KI_48_II) CockpitKI_48_Bombardier.this.aircraft()).fSightCurForwardAngle;
				float f1 = -((KI_48_II) CockpitKI_48_Bombardier.this.aircraft()).fSightCurSideslip;
				CockpitKI_48_Bombardier.this.mesh.chunkSetAngles("BlackBox", f1, 0.0F, -f);
				HookPilot hookpilot = HookPilot.current;
				hookpilot.setInstantOrient(-f1, CockpitKI_48_Bombardier.this.tAim + f, 0.0F);
			}
			return true;
		}

		Interpolater() {
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			Point3d point3d = new Point3d();
			point3d.set(0.0D, 0.0D, 0.0D);
			hookpilot.setTubeSight(point3d);
			this.enter();
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

	private void enter() {
		this.saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov 24.0");
		Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock()) hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleUse(true);
		hookpilot.setSimpleAimOrient(this.aAim, this.tAim, 0.0F);
		hookpilot.setInstantOrient(this.aAim, this.tAim, 0.0F);
		this.doSetSimpleUse(true);
		HotKeyEnv.enable("PanView", false);
		HotKeyEnv.enable("SnapView", false);
		this.bEntered = true;
	}

	private void leave() {
		if (!this.bEntered) return;
		else {
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
			CmdEnv.top().exec("fov " + this.saveFov);
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
			hookpilot.setSimpleUse(false);
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
	}

	public CockpitKI_48_Bombardier() {
		super("3DO/Cockpit/D3A1-Bombardier/BombardierKi48.him", "he111");
		this.bEntered = false;
		try {
			Loc loc = new Loc();
			HookNamed hooknamed = new HookNamed(this.mesh, "CAMERA");
			hooknamed.computePos(this, this.pos.getAbs(), loc);
			this.aAim = loc.getOrient().getAzimut();
			this.tAim = loc.getOrient().getTangage();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		this.interpPut(new Interpolater(), null, Time.current(), null);
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("zMark1", ((KI_48_II) this.aircraft()).fSightCurForwardAngle * 3.675F, 0.0F, 0.0F);
		float f1 = this.cvt(((KI_48_II) this.aircraft()).fSightSetForwardAngle, -15F, 75F, -15F, 75F);
		this.mesh.chunkSetAngles("zMark2", f1 * 3.675F, 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[0] = this.cvt(this.fm.Or.getKren() * Math.abs(this.fm.Or.getKren()), -1225F, 1225F, -0.23F, 0.23F);
		Cockpit.xyz[1] = this.cvt((this.fm.Or.getTangage() - 1.0F) * Math.abs(this.fm.Or.getTangage() - 1.0F), -1225F, 1225F, 0.23F, -0.23F);
		Cockpit.ypr[0] = this.cvt(this.fm.Or.getKren(), -45F, 45F, -180F, 180F);
		this.mesh.chunkSetLocate("zBulb", Cockpit.xyz, Cockpit.ypr);
		this.resetYPRmodifier();
		Cockpit.xyz[0] = this.cvt(Cockpit.xyz[0], -0.23F, 0.23F, 0.0095F, -0.0095F);
		Cockpit.xyz[1] = this.cvt(Cockpit.xyz[1], -0.23F, 0.23F, 0.0095F, -0.0095F);
		this.mesh.chunkSetLocate("zRefraction", Cockpit.xyz, Cockpit.ypr);
	}

	private float   saveFov;
	private float   aAim;
	private float   tAim;
	private boolean bEntered;

	static {
		Property.set(CockpitKI_48_Bombardier.class, "astatePilotIndx", 0);
		Property.set(CockpitKI_48_Bombardier.class, "normZN", 0.5F);
		Property.set(CockpitKI_48_Bombardier.class, "gsZN", 0.5F);
	}
}
