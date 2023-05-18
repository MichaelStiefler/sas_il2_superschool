package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitDo217E_Bombardier extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			CockpitDo217E_Bombardier.this.setTmp = CockpitDo217E_Bombardier.this.setOld;
			CockpitDo217E_Bombardier.this.setOld = CockpitDo217E_Bombardier.this.setNew;
			CockpitDo217E_Bombardier.this.setNew = CockpitDo217E_Bombardier.this.setTmp;
			float f = ((Do217) (Do217_E2) CockpitDo217E_Bombardier.this.aircraft()).fSightCurForwardAngle;
			float f1 = ((Do217) (Do217_E2) CockpitDo217E_Bombardier.this.aircraft()).fSightCurSideslip;
			CockpitDo217E_Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
			if (CockpitDo217E_Bombardier.this.bEntered) {
				HookPilot hookpilot = HookPilot.current;
				hookpilot.setInstantOrient(CockpitDo217E_Bombardier.this.aAim + 10F * f1, CockpitDo217E_Bombardier.this.tAim + f, 0.0F);
			}
			CockpitDo217E_Bombardier.this.mesh.chunkSetAngles("TurretA", 0.0F, CockpitDo217E_Bombardier.this.aircraft().FM.turret[0].tu[0], 0.0F);
			CockpitDo217E_Bombardier.this.mesh.chunkSetAngles("TurretB", 0.0F, CockpitDo217E_Bombardier.this.aircraft().FM.turret[0].tu[1], 0.0F);
			float f2 = CockpitDo217E_Bombardier.this.waypointAzimuth();
			CockpitDo217E_Bombardier.this.setNew.azimuth.setDeg(CockpitDo217E_Bombardier.this.setOld.azimuth.getDeg(1.0F), CockpitDo217E_Bombardier.this.fm.Or.azimut());
			CockpitDo217E_Bombardier.this.setNew.waypointAzimuth.setDeg(CockpitDo217E_Bombardier.this.setOld.waypointAzimuth.getDeg(0.1F), f2);
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float      cons;
		float      elevTrim;
		float      rudderTrim;
		float      ailTrim;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
			Point3d point3d = new Point3d();
			point3d.set(0.18D, 0.06D, -0.12D);
			hookpilot.setTubeSight(point3d);
			this.aircraft().hierMesh().chunkVisible("Interior1_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
			this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
			this.aircraft().hierMesh().chunkVisible("Hmask1_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
			this.aircraft().hierMesh().chunkVisible("Hmask2_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
			this.aircraft().hierMesh().chunkVisible("Hmask3_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot4_D0", false);
			this.aircraft().hierMesh().chunkVisible("Hmask4_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
			this.aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
			this.aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
			this.aircraft().hierMesh().chunkVisible("Pilot4_D1", false);
			this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
			if (this.aircraft() instanceof Do217_E2) {
				this.mesh.chunkVisible("k2-Box", false);
				this.mesh.chunkVisible("k2-Cable", false);
				this.mesh.chunkVisible("k2-cushion", false);
				this.mesh.chunkVisible("k2-FuG203", false);
				this.mesh.chunkVisible("k2-gunsight", false);
			} else {
				this.mesh.chunkVisible("StuviArm", false);
				this.mesh.chunkVisible("StuviPlate", false);
				this.mesh.chunkVisible("Revi_D0", false);
				this.mesh.chunkVisible("StuviHandle", false);
				this.mesh.chunkVisible("StuviLock", false);
			}
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		if (this.isFocused()) {
			((Do217) ((Interpolate) this.fm).actor).bPitUnfocused = false;
			this.aircraft().hierMesh().chunkVisible("Interior1_D0", true);
			if (!this.fm.AS.isPilotParatrooper(0)) {
				this.aircraft().hierMesh().chunkVisible("Pilot1_D0", !this.fm.AS.isPilotDead(0));
				this.aircraft().hierMesh().chunkVisible("Head1_D0", !this.fm.AS.isPilotDead(0));
				this.aircraft().hierMesh().chunkVisible("Pilot1_D1", this.fm.AS.isPilotDead(0));
			}
			if (!this.fm.AS.isPilotParatrooper(1)) {
				this.aircraft().hierMesh().chunkVisible("Pilot2_D0", !this.fm.AS.isPilotDead(1));
				this.aircraft().hierMesh().chunkVisible("Pilot2_D1", this.fm.AS.isPilotDead(1));
			}
			if (!this.fm.AS.isPilotParatrooper(2)) {
				this.aircraft().hierMesh().chunkVisible("Pilot3_D0", !this.fm.AS.isPilotDead(2));
				this.aircraft().hierMesh().chunkVisible("Pilot3_D1", this.fm.AS.isPilotDead(2));
			}
			if (!this.fm.AS.isPilotParatrooper(3)) {
				this.aircraft().hierMesh().chunkVisible("Pilot4_D0", !this.fm.AS.isPilotDead(3));
				this.aircraft().hierMesh().chunkVisible("Pilot4_D1", this.fm.AS.isPilotDead(3));
			}
			this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
			if (this.aircraft() instanceof Do217_E2) {
				this.mesh.chunkVisible("k2-Box", true);
				this.mesh.chunkVisible("k2-Cable", true);
				this.mesh.chunkVisible("k2-cushion", true);
				this.mesh.chunkVisible("k2-FuG203", true);
				this.mesh.chunkVisible("k2-gunsight", true);
			} else {
				this.mesh.chunkVisible("StuviArm", true);
				this.mesh.chunkVisible("StuviPlate", true);
				this.mesh.chunkVisible("Revi_D0", true);
				this.mesh.chunkVisible("StuviHandle", true);
				this.mesh.chunkVisible("StuviLock", true);
			}
			this.leave();
			super.doFocusLeave();
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

	public CockpitDo217E_Bombardier() {
		super("3DO/Cockpit/Do217E2/hierBombardier.him", "he111");
		this.bEntered = false;
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.setOld = new Variables();
		this.setNew = new Variables();
		try {
			Loc loc = new Loc();
			HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
			hooknamed.computePos(this, this.pos.getAbs(), loc);
			this.aAim = loc.getOrient().getAzimut();
			this.tAim = loc.getOrient().getTangage();
			HookNamed hooknamed1 = new HookNamed(this.mesh, "LAMPHOOK1");
			Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
			hooknamed1.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
			this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
			this.light1.light.setColor(218F, 143F, 128F);
			this.light1.light.setEmit(0.0F, 0.0F);
			this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		this.cockpitNightMats = new String[] { "Peil1", "Peil2", "Instrument1", "Instrument2", "Instrument4", "Instrument5", "Instrument6", "Instrument7", "Instrument8", "Instrument9", "Needles" };
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
			this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((Do217) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
			boolean flag = ((Do217) this.aircraft()).fSightCurReadyness > 0.93F;
			this.mesh.chunkVisible("BlackBox", true);
			this.mesh.chunkVisible("zReticle", flag);
			this.mesh.chunkVisible("zAngleMark", flag);
		} else {
			this.mesh.chunkVisible("BlackBox", false);
			this.mesh.chunkVisible("zReticle", false);
			this.mesh.chunkVisible("zAngleMark", false);
			this.mesh.chunkSetAngles("ZWheel", 0.0F, (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 40F, 0.0F);
			this.mesh.chunkSetAngles("ZColumn", 0.0F, -(this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 10F, 0.0F);
			this.mesh.chunkSetAngles("PedalR", 0.0F, -this.fm.CT.getRudder() * 10F, 0.0F);
			this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 10F, 0.0F);
			this.resetYPRmodifier();
			Cockpit.xyz[1] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, 0.08F, -0.08F);
			this.mesh.chunkSetLocate("PedalRbar", Cockpit.xyz, Cockpit.ypr);
			Cockpit.xyz[1] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -0.08F, 0.08F);
			this.mesh.chunkSetLocate("PedalLbar", Cockpit.xyz, Cockpit.ypr);
			this.mesh.chunkSetAngles("z_ElevTrim", 0.0F, -this.cvt(this.interp(this.setNew.elevTrim, this.setOld.elevTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
			this.mesh.chunkSetAngles("z_RudderTrim", 0.0F, this.cvt(this.interp(this.setNew.rudderTrim, this.setOld.rudderTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
			this.mesh.chunkSetAngles("z_AileronTrim", 0.0F, this.cvt(this.interp(this.setNew.ailTrim, this.setOld.ailTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
			float f1 = this.setNew.cons;
			float f2 = this.fm.EI.engines[0].getRPM();
			float f3 = this.fm.EI.engines[1].getRPM();
			float f4 = f1 * f2 / (f2 + f3);
			float f5 = f1 * f3 / (f2 + f3);
			this.mesh.chunkSetAngles("z_FuelCons1", 0.0F, this.cvt(f4, 0.0F, 500F, 0.0F, 300F), 0.0F);
			this.mesh.chunkSetAngles("z_FuelCons2", 0.0F, this.cvt(f5, 0.0F, 500F, 0.0F, 300F), 0.0F);
			float f6 = this.fm.M.fuel / 0.72F;
			this.mesh.chunkSetAngles("z_Fuel3", 0.0F, this.cvt(f6, 0.0F, 1100F, 0.0F, 69F), 0.0F);
			this.mesh.chunkSetAngles("z_Fuel1", 0.0F, this.cvt(f6, 1100F, 2670F, 0.0F, 84F), 0.0F);
			this.mesh.chunkSetAngles("z_Fuel2", 0.0F, this.cvt(f6, 1100F, 2670F, 0.0F, 84F), 0.0F);
			this.mesh.chunkSetAngles("z_OilPres1", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 15F, 0.0F, -135F), 0.0F);
			this.mesh.chunkSetAngles("z_OilPres2", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness(), 0.0F, 15F, 0.0F, -135F), 0.0F);
			this.mesh.chunkSetAngles("z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3F, 0.0F, 135F), 0.0F);
			this.mesh.chunkSetAngles("z_FuelPres2", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3F, 0.0F, 135F), 0.0F);
			this.mesh.chunkSetAngles("Z_TempCylL", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
			this.mesh.chunkSetAngles("Z_TempCylR", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
			this.mesh.chunkSetAngles("z_OAT", 0.0F, this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -60F, 40F, 0.0F, 10F), OATscale), 0.0F);
			this.mesh.chunkSetAngles("Z_Turret1A", 0.0F, -this.fm.turret[0].tu[0], 0.0F);
			this.mesh.chunkSetAngles("Z_Turret1B", 0.0F, this.fm.turret[0].tu[1], 0.0F);
			this.mesh.chunkSetAngles("Z_Turret5A", 0.0F, -this.fm.turret[4].tu[0], 0.0F);
			this.mesh.chunkSetAngles("Z_Turret5B", 0.0F, this.fm.turret[4].tu[1], 0.0F);
		}
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) {
			this.mesh.chunkVisible("XGlassHoles1", true);
			this.mesh.chunkVisible("XGlassHoles3", true);
		}
		if ((this.fm.AS.astateCockpitState & 8) != 0) {
			this.mesh.chunkVisible("XGlassHoles7", true);
			this.mesh.chunkVisible("XGlassHoles4", true);
			this.mesh.chunkVisible("XGlassHoles2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
			this.mesh.chunkVisible("XGlassHoles5", true);
			this.mesh.chunkVisible("XGlassHoles3", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XGlassHoles1", true);
		this.mesh.chunkVisible("XGlassHoles6", true);
		this.mesh.chunkVisible("XGlassHoles4", true);
		if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("XGlassHoles6", true);
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("XGlassHoles7", true);
			this.mesh.chunkVisible("XGlassHoles2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
			this.mesh.chunkVisible("XGlassHoles5", true);
			this.mesh.chunkVisible("XGlassHoles2", true);
		}
	}

	protected void mydebugcockpit(String s) {
		System.out.println(s);
	}

	private static final float angleScale[] = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
	private float              saveFov;
	private float              aAim;
	private float              tAim;
	private boolean            bEntered;
	private boolean            enteringAim;
	private float              pictAiler;
	private float              pictElev;
	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;
	private LightPointActor    light1;
	private static final float OATscale[]   = { 0.0F, 7F, 17F, 27F, 37F, 47F, 56F, 65F, 72F, 80F, 85F };

	static {
		Property.set(CockpitDo217E_Bombardier.class, "astatePilotIndx", 0);
	}
}
