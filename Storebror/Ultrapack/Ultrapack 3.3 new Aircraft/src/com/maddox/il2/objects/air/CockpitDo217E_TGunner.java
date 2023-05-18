package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitDo217E_TGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			((Do217) ((Interpolate) this.fm).actor).bPitUnfocused = false;
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
			this.aircraft().hierMesh().chunkVisible("Turret2A_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret4B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
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
			((Do217) ((Interpolate) this.fm).actor).bPitUnfocused = true;
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
			this.aircraft().hierMesh().chunkVisible("Turret2A_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret4B_D0", true);
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
			super.doFocusLeave();
		}
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
		this.mesh.chunkSetAngles("Z_turret1A", 0.0F, -this.fm.turret[0].tu[0], 0.0F);
		this.mesh.chunkSetAngles("Z_turret1B", 0.0F, this.fm.turret[0].tu[1], 0.0F);
		this.mesh.chunkSetAngles("Z_turret3A", 0.0F, -this.fm.turret[2].tu[0], 0.0F);
		this.mesh.chunkSetAngles("Z_turret3B", 0.0F, this.fm.turret[2].tu[1], 0.0F);
		this.mesh.chunkSetAngles("Z_turret4A", 0.0F, -this.fm.turret[3].tu[0], 0.0F);
		this.mesh.chunkSetAngles("Z_turret4B", 0.0F, this.fm.turret[3].tu[1], 0.0F);
		this.mesh.chunkSetAngles("Z_turret5A", 0.0F, -this.fm.turret[4].tu[0], 0.0F);
		this.mesh.chunkSetAngles("Z_turret5B", 0.0F, this.fm.turret[4].tu[1], 0.0F);
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("z_Turret2A", 0.0F, orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("z_Turret2B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f1 > 80F) f1 = 80F;
		if (f1 < -3F) f1 = -3F;
		if (f > 155F || f < -155F) {
			if (f1 < 40F) f1 = 40F;
		} else if (f > 135F) {
			if (f1 < (f - 135F) * 40F / 20F) f1 = (f - 135F) * 40F / 20F;
		} else if (f < -135F) {
			if (f1 < (-f - 135F) * 40F / 20F) f1 = (-f - 135F) * 40F / 20F;
		} else if ((f > 110F || f < -110F) && f1 < 0.0F) f1 = 0.0F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
			if (this.iCocking > 0) this.iCocking = 0;
			else this.iCocking = 1;
		} else this.iCocking = 0;
		if (this.emitter != null) {
			boolean flag = this.emitter.countBullets() % 2 == 0;
			this.mesh.chunkVisible("ZTurret2B-Bullet01", flag);
			this.mesh.chunkVisible("ZTurret2B-Bullet02", !flag);
		}
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	protected void reflectPlaneMats() {
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

	public CockpitDo217E_TGunner() {
		super("3DO/Cockpit/Do217E2/hierTGun.him", "he111_gunner");
		this.w = new Vector3f();
		this.bNeedSetUp = true;
		this.hook1 = null;
		this.iCocking = 0;
	}

	public Vector3f w;
	private boolean bNeedSetUp;
	private Hook    hook1;
	private int     iCocking;

	static {
		Property.set(CockpitDo217E_TGunner.class, "aiTuretNum", 1);
		Property.set(CockpitDo217E_TGunner.class, "weaponControlNum", 11);
		Property.set(CockpitDo217E_TGunner.class, "astatePilotIndx", 2);
	}
}
