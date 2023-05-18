package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111H16_LGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			if (this.fm.actor instanceof HE_111) ((HE_111) this.fm.actor).bPitUnfocused = false;
			else if (this.fm.actor instanceof HE_111xyz) ((HE_111xyz) this.fm.actor).bPitUnfocused = false;
			this.aircraft().hierMesh().chunkVisible("Korzina_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		if (this.isFocused()) {
			if (this.fm.actor instanceof HE_111) ((HE_111) this.fm.actor).bPitUnfocused = true;
			else if (this.fm.actor instanceof HE_111xyz) ((HE_111xyz) this.fm.actor).bPitUnfocused = true;
			this.aircraft().hierMesh().chunkVisible("Korzina_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
			this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
			super.doFocusLeave();
		}
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("TurretBA", 0.0F, this.fm.turret[2].tu[0], 0.0F);
		this.mesh.chunkSetAngles("TurretBB", 0.0F, this.fm.turret[2].tu[1], 0.0F);
		this.mesh.chunkSetAngles("TurretRA", 0.0F, this.fm.turret[4].tu[0], 0.0F);
		this.mesh.chunkSetAngles("TurretRB", 0.0F, this.fm.turret[4].tu[1], 0.0F);
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		this.mesh.chunkSetAngles("TurretLA", 0.0F, f, 0.0F);
		this.mesh.chunkSetAngles("TurretLB", 0.0F, f1, 0.0F);
		this.mesh.chunkSetAngles("CameraRodA", 0.0F, f, 0.0F);
		this.mesh.chunkSetAngles("CameraRodB", 0.0F, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -40F) f = -40F;
		if (f > 30F) f = 30F;
		if (f1 > 30F) f1 = 30F;
		if (f1 < -40F) f1 = -40F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN04");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN04");
			if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN07");
			this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN07");
		}
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	public CockpitHE_111H16_LGunner() {
		super("3DO/Cockpit/He-111P-4-LGun/hier-H16.him", "he111_gunner");
		this.hook1 = null;
		this.hook2 = null;
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) {
			this.mesh.chunkVisible("Flare", true);
			this.setNightMats(true);
		} else {
			this.mesh.chunkVisible("Flare", false);
			this.setNightMats(false);
		}
	}

	public void reflectCockpitState() {
		if (this.fm.AS.astateCockpitState != 0) this.mesh.chunkVisible("Holes_D1", true);
	}

	private Hook hook1;
	private Hook hook2;

	static {
		Property.set(CockpitHE_111H16_LGunner.class, "aiTuretNum", 3);
		Property.set(CockpitHE_111H16_LGunner.class, "weaponControlNum", 13);
		Property.set(CockpitHE_111H16_LGunner.class, "astatePilotIndx", 4);
	}
}
