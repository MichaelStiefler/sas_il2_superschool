package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_177A5_Bombardier extends CockpitPilot {
	private class Variables {

		float elevTrim;
		float rudderTrim;
		float ailTrim;
		float cons;

		private Variables() {
		}

	}

	class Interpolater extends InterpolateRef {

		public boolean tick() {
			CockpitHE_177A5_Bombardier.this.setTmp = CockpitHE_177A5_Bombardier.this.setOld;
			CockpitHE_177A5_Bombardier.this.setOld = CockpitHE_177A5_Bombardier.this.setNew;
			CockpitHE_177A5_Bombardier.this.setNew = CockpitHE_177A5_Bombardier.this.setTmp;
			float f = ((HE_177A5) CockpitHE_177A5_Bombardier.this.aircraft()).fSightCurForwardAngle;
			float f1 = ((HE_177A5) CockpitHE_177A5_Bombardier.this.aircraft()).fSightCurSideslip;
			CockpitHE_177A5_Bombardier.calibrAngle = 360F - CockpitHE_177A5_Bombardier.this.fm.Or.getPitch();
			CockpitHE_177A5_Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, calibrAngle + f);
			if (CockpitHE_177A5_Bombardier.this.bEntered) if (!CockpitHE_177A5_Bombardier.this.hasToKG) HookPilot.current.setInstantOrient(CockpitHE_177A5_Bombardier.this.aAim + 10F * f1, CockpitHE_177A5_Bombardier.this.tAim + calibrAngle + f, 0.0F);
			CockpitHE_177A5_Bombardier.this.mesh.chunkSetAngles("Z_turret1A", 0.0F, CockpitHE_177A5_Bombardier.this.aircraft().FM.turret[0].tu[0], 0.0F);
			CockpitHE_177A5_Bombardier.this.mesh.chunkSetAngles("Z_turret1B", 0.0F, CockpitHE_177A5_Bombardier.this.aircraft().FM.turret[0].tu[1], 0.0F);
			CockpitHE_177A5_Bombardier.this.setNew.elevTrim = 0.85F * CockpitHE_177A5_Bombardier.this.setOld.elevTrim + CockpitHE_177A5_Bombardier.this.fm.CT.getTrimElevatorControl() * 0.15F;
			CockpitHE_177A5_Bombardier.this.setNew.rudderTrim = 0.85F * CockpitHE_177A5_Bombardier.this.setOld.rudderTrim + CockpitHE_177A5_Bombardier.this.fm.CT.getTrimRudderControl() * 0.15F;
			CockpitHE_177A5_Bombardier.this.setNew.ailTrim = 0.85F * CockpitHE_177A5_Bombardier.this.setOld.ailTrim + CockpitHE_177A5_Bombardier.this.fm.CT.getTrimAileronControl() * 0.15F;
			float f2 = CockpitHE_177A5_Bombardier.this.prevFuel - CockpitHE_177A5_Bombardier.this.fm.M.fuel;
			CockpitHE_177A5_Bombardier.this.prevFuel = CockpitHE_177A5_Bombardier.this.fm.M.fuel;
			f2 /= 0.72F;
			f2 /= Time.tickLenFs();
			f2 *= 3600F;
			CockpitHE_177A5_Bombardier.this.setNew.cons = 0.91F * CockpitHE_177A5_Bombardier.this.setOld.cons + 0.09F * f2;
			return true;
		}

		Interpolater() {
		}
	}

	protected boolean doFocusEnter() {
		if (!super.doFocusEnter()) return false;
//		if (CommonUtils.is411orLater()) {
		if (this.doToKGCheck) {
			this.doToKGCheck = false;
			if (this.aircraft() instanceof HE_177A5 && ((HE_177A5) this.aircraft()).hasToKG) {
				this.hasToKG = true;
				this.showToKG();
			}
		}
//		}
		((HE_177A5) this.fm.actor).bPitUnfocused = false;
		HookPilot hookpilot = HookPilot.current;
		hookpilot.doAim(false);
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
		this.mesh.chunkVisible("k2-Box", false);
		this.mesh.chunkVisible("k2-Cable", false);
		this.mesh.chunkVisible("k2-cushion", false);
		this.mesh.chunkVisible("k2-FuG203", false);
		this.mesh.chunkVisible("k2-gunsight", false);
		return true;
	}

	protected void doFocusLeave() {
		if (this.isFocused()) {
			((HE_177A5) this.fm.actor).bPitUnfocused = false;
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
			this.mesh.chunkVisible("k2-Box", true);
			this.mesh.chunkVisible("k2-Cable", true);
			this.mesh.chunkVisible("k2-cushion", true);
			this.mesh.chunkVisible("k2-FuG203", true);
			this.mesh.chunkVisible("k2-gunsight", true);
			this.leave();
			super.doFocusLeave();
		}
	}

	private void enter() {
		this.saveFov = Main3D.FOVX;
		CmdEnv.top().exec("fov 23.913");
		Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
		HookPilot hookpilot = HookPilot.current;
		if (hookpilot.isPadlock()) hookpilot.stopPadlock();
		hookpilot.doAim(true);
		hookpilot.setSimpleUse(true);
		hookpilot.setSimpleAimOrient(this.aAim, this.tAim, 0.0F);
		HotKeyEnv.enable("PanView", false);
		HotKeyEnv.enable("SnapView", false);
		this.bEntered = true;
	}

	private void leave() {
		if (!this.bEntered) return;
		Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
		CmdEnv.top().exec("fov " + this.saveFov);
		HookPilot hookpilot1 = HookPilot.current;
		hookpilot1.doAim(false);
		hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
		hookpilot1.setSimpleUse(false);
		boolean flag = HotKeyEnv.isEnabled("aircraftView");
		HotKeyEnv.enable("PanView", flag);
		HotKeyEnv.enable("SnapView", flag);
		this.bEntered = false;
	}

	public void destroy() {
		super.destroy();
		this.leave();
	}

	public void doToggleAim(boolean flag) {
		if (!this.isFocused()) return;
		if (this.isToggleAim() == flag) return;
		if (flag) this.enter();
		else this.leave();
	}

	public CockpitHE_177A5_Bombardier() {
		super("3DO/Cockpit/He-177A-5/Bombardier.him", "he111");
		this.doToKGCheck = true;
		this.hasToKG = false;
		this.bEntered = false;
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.prevFuel = 0.0F;
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

	private void showToKG() {
		System.out.println("CockpitHE_177_Bombardier showToKG()");
		this.mesh.chunkVisible("Lofte", false);
		this.mesh.chunkVisible("TorpedoBoxNew", true);
		this.mesh.chunkVisible("speedDial", true);
		this.mesh.chunkVisible("angleDial", true);
		this.mesh.chunkVisible("Ship", true);
		this.mesh.chunkVisible("Z_Angle", true);
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) this.setNightMats(true);
		else this.setNightMats(false);
	}

	public void reflectWorldToInstruments(float f) {
		boolean bShowBombsight = this.bEntered;
		if (bShowBombsight) {
			this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((HE_177A5) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
			boolean flag = ((HE_177A5) this.aircraft()).fSightCurReadyness > 0.93F;
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
			this.mesh.chunkSetAngles("z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F);
			this.mesh.chunkSetAngles("z_FuelPres2", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F);
			this.mesh.chunkSetAngles("Z_TempCylL", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
			this.mesh.chunkSetAngles("Z_TempCylR", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 75F), 0.0F);
			this.mesh.chunkSetAngles("z_OAT", 0.0F, this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -60F, 40F, 0.0F, 10F), OATscale), 0.0F);
			this.mesh.chunkSetAngles("Z_Turret1A", 0.0F, -this.fm.turret[0].tu[0], 0.0F);
			this.mesh.chunkSetAngles("Z_Turret1B", 0.0F, this.fm.turret[0].tu[1], 0.0F);
			this.mesh.chunkSetAngles("Z_Turret5A", 0.0F, -this.fm.turret[4].tu[0], 0.0F);
			this.mesh.chunkSetAngles("Z_Turret5B", 0.0F, this.fm.turret[4].tu[1], 0.0F);
			this.mesh.chunkSetAngles("z_Velx", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 800F, 0.0F, 15F), speedometerScale), 0.0F);
		}
//		if (CommonUtils.is411orLater()) {
		if (this.hasToKG) {
			this.mesh.chunkSetAngles("Ship", 0.0F, -((HE_177A5) this.aircraft()).fAOB, 0.0F);
			this.mesh.chunkSetAngles("speedDial", 0.0F, 6.4F * ((HE_177A5) this.aircraft()).fShipSpeed, 0.0F);
			this.mesh.chunkSetAngles("Z_Angle", 0.0F, ((HE_177A5) this.aircraft()).FM.AS.getGyroAngle(), 0.0F);
		}
//		}
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

	public float[] getBombSightFovs() {
		return defaultBSightFoVs;
	}

	private static final float defaultBSightFoVs[] = { 25F, 30F };

	private static final float angleScale[]        = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
	private static final float speedometerScale[]  = { 0.0F, 17F, 41F, 66F, 93F, 119F, 139F, 164F, 188F, 214F, 239F, 266F, 292F, 317F, 341F, 372F };
	private float              saveFov;
	private float              aAim;
	private float              tAim;
	private boolean            bEntered;
	private boolean            doToKGCheck;
	private boolean            hasToKG;
	private float              pictAiler;
	private float              pictElev;
	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;
	private LightPointActor    light1;
	private float              prevFuel;
	private static float       calibrAngle         = 3F;
	private static final float OATscale[]          = { 0.0F, 7F, 17F, 27F, 37F, 47F, 56F, 65F, 72F, 80F, 85F };

	static {
		Class class1 = CockpitHE_177A5_Bombardier.class;
		Property.set(class1, "astatePilotIndx", 0);
	}

}
