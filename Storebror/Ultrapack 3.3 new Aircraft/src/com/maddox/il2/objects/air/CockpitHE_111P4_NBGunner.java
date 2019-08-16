package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111P4_NBGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = false;
			this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Head1_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
			this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
			this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Window_D0", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = true;
		this.aircraft().hierMesh().chunkVisible("Cockpit_D0", this.aircraft().hierMesh().isChunkVisible("Nose_D0") || this.aircraft().hierMesh().isChunkVisible("Nose_D1") || this.aircraft().hierMesh().isChunkVisible("Nose_D2"));
		this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
		this.aircraft().hierMesh().chunkVisible("Head1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
		this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
		this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
		this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
		this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
		this.aircraft().hierMesh().chunkVisible("Window_D0", true);
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		this.mesh.chunkSetAngles("TurretNA", 0.0F, f, 0.0F);
		this.mesh.chunkSetAngles("TurretNB", 0.0F, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -20F) f = -20F;
		if (f > 50F) f = 50F;
		if (f1 > 30F) f1 = 30F;
		if (f1 < -30F) f1 = -30F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN07");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN07");
			if (this.iCocking > 0) this.iCocking = 0;
			else this.iCocking = 1;
		} else this.iCocking = 0;
		this.resetYPRmodifier();
		Cockpit.xyz[0] = -0.07F * this.iCocking;
		Cockpit.ypr[1] = 0.0F;
		this.mesh.chunkSetLocate("LeverNB", Cockpit.xyz, Cockpit.ypr);
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	public CockpitHE_111P4_NBGunner() {
		super("3DO/Cockpit/He-111P-4-NGun/hier-NBGunner.him", "he111_gunner");
		this.iCocking = 0;
		this.hook1 = null;
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * this.fm.CT.ElevatorControl);
		this.mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * this.fm.CT.AileronControl);
		this.mesh.chunkSetAngles("TurretA", 0.0F, this.fm.turret[0].tu[0], 0.0F);
		this.mesh.chunkSetAngles("TurretB", 0.0F, this.fm.turret[0].tu[1], 0.0F);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("ZHolesL_D1", true);
		if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("ZHolesL_D2", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("ZHolesR_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("ZHolesR_D2", true);
		if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("ZHolesF_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("zOil_D1", true);
	}

	private int  iCocking;
	private Hook hook1;

	static {
		Property.set(CockpitHE_111P4_NBGunner.class, "aiTuretNum", 6);
		Property.set(CockpitHE_111P4_NBGunner.class, "weaponControlNum", 16);
		Property.set(CockpitHE_111P4_NBGunner.class, "astatePilotIndx", 1);
	}
}
