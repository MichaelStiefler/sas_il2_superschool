package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111H5_LGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = false;
			this.aircraft().hierMesh().chunkVisible("Korzina_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret6C_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		if (this.isFocused()) {
			((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = true;
			this.aircraft().hierMesh().chunkVisible("Korzina_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret6C_D0", true);
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
		this.mesh.chunkSetAngles("TurretFA", 0.0F, this.fm.turret[5].tu[0], 0.0F);
		this.mesh.chunkSetAngles("TurretFB", 0.0F, this.fm.turret[5].tu[1], 0.0F);
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("TurretLA", 0.0F, -orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("TurretLB", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -55F) f = -55F;
		if (f > 23F) f = 23F;
		if (f1 > 30F) f1 = 30F;
		if (f1 < -45F) f1 = -45F;
		if (f1 < -55F - 0.6F * f) f1 = -55F - 0.6F * f;
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
			if (this.iCocking > 0) this.iCocking = 0;
			else this.iCocking = 1;
		} else this.iCocking = 0;
		this.resetYPRmodifier();
		Cockpit.xyz[0] = -0.07F * this.iCocking;
		this.mesh.chunkSetLocate("LeverL", Cockpit.xyz, Cockpit.ypr);
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	public CockpitHE_111H5_LGunner() {
		super("3DO/Cockpit/He-111P-4-LGun/hier-H5.him", "he111_gunner");
		this.hook1 = null;
		this.iCocking = 0;
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
	private int  iCocking;

	static {
		Property.set(CockpitHE_111H5_LGunner.class, "aiTuretNum", 3);
		Property.set(CockpitHE_111H5_LGunner.class, "weaponControlNum", 13);
		Property.set(CockpitHE_111H5_LGunner.class, "astatePilotIndx", 4);
	}
}
