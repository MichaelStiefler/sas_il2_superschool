package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_177A5_BGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (!super.doFocusEnter()) return false;
		((HE_177A5) this.fm.actor).bPitUnfocused = false;
		this.aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
		this.aircraft().hierMesh().chunkVisible("Hmask3_D0", false);
		this.aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
		this.aircraft().hierMesh().chunkVisible("Turret3A_D0", false);
		this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
		this.isOppositeSideAiControlled = this.aircraft().FM.turret[1].bIsAIControlled;
		this.aircraft().FM.turret[1].bIsAIControlled = true;
		this.aircraft().FM.turret[2].bIsAIControlled = this.onAuto;
		((HE_177A5) this.aircraft()).setVentralGunnerDirection(HE_177_MOD.VENTRAL_GUNNER_AFT);
		return true;
	}

	protected void doFocusLeave() {
		if (!this.isFocused()) return;
		((HE_177A5) this.fm.actor).bPitUnfocused = true;
		if (!this.fm.AS.isPilotParatrooper(2)) {
			this.aircraft().hierMesh().chunkVisible("Pilot3_D0", !this.fm.AS.isPilotDead(2));
			this.aircraft().hierMesh().chunkVisible("Pilot3_D1", this.fm.AS.isPilotDead(2));
		}
		this.aircraft().hierMesh().chunkVisible("Turret3A_D0", true);
		this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
		this.onAuto = this.aircraft().FM.turret[2].bIsAIControlled;
		this.aircraft().FM.turret[1].bIsOperable = this.aircraft().FM.turret[1].health > 0.0F;
		this.aircraft().FM.turret[1].bIsAIControlled = this.isOppositeSideAiControlled;
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("z_Turret3A", -orient.getYaw(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("z_Turret3B", 0.0F, orient.getTangage() /* + 90.0F */, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage() + GUN_OFFSET_TANGAGE;
		if (this.aircraft().FM.CT.BayDoorControl > 0.0F) {
			f1 = -60F;
			f = 0F;
		} else {
			if (f1 > -3F) f1 = -3F;
			if (f1 < -80F) f1 = -80F;
			if (f > 50F) f = 50F;
			if (f < -40F) f = -40F;
		}
		orient.setYPR(f, f1 - GUN_OFFSET_TANGAGE, 0.0F);
		this.clipAnglesGunByOrdnance(orient);
		orient.wrap();
	}

	private void clipAnglesGunByOrdnance(Orient orient) {
		float y = orient.getYaw();
		float t = orient.getTangage();
		String weaponsName = this.aircraft().thisWeaponsName;

		if (weaponsName.equals("A51xSC2500")) {
			if (this.fm.CT.Weapons[3][0].haveBullets()) if (y > -25F && y < 25F && t > -40F) t = -40F;
		} else if (weaponsName.equals("A51xHs293")) {
			if (this.fm.CT.Weapons[3][0].haveBullets()) {
				if (y < 42F && t > -14F) t = -14F;
				if (y > -15F && y < 15F && t > -47.5F) t = -47.5F;
			}
		} else if (weaponsName.equals("A53xHs293")) {
			if (this.fm.CT.Weapons[3][4].haveBullets()) {
				if (y < 42F && t > -14F) t = -14F;
				if (y > -15F && y < 15F && t > -47.5F) t = -47.5F;
			}
		} else if (weaponsName.equals("A51xFritzX")) {
			if (this.fm.CT.Weapons[3][0].haveBullets()) if (y > -20F && y < 20F && t > -27.5F) t = -27.5F;
		} else if (weaponsName.equals("A53xFritzX")) {
			if (this.fm.CT.Weapons[3][4].haveBullets()) if (y > -20F && y < 20F && t > -27.5F) t = -27.5F;
		} else if (weaponsName.equals("A52xLT50")) {
			if (this.fm.CT.Weapons[3][0].haveBullets()) if (t > (-y + 1F) * 2F - 3F) t = (y - 1F) * -2F - 3F;
			if (this.fm.CT.Weapons[3][1].haveBullets()) if (t > (y + 1F) * 2F - 3F) t = (y + 1F) * 2F - 3F;
		} else if (weaponsName.equals("A52xLT50_spread")) {
			if (this.fm.CT.Weapons[3][0].haveBullets()) if (t > (-y + 1F) * 2F - 3F) t = (y - 1F) * -2F - 3F;
			if (this.fm.CT.Weapons[3][3].haveBullets()) if (t > (y + 1F) * 2F - 3F) t = (y + 1F) * 2F - 3F;
		} else if (weaponsName.equals("A54xLT50")) {
			if (this.fm.CT.Weapons[3][2].haveBullets()) if (t > (-y + 1F) * 2F - 3F) t = (y - 1F) * -2F - 3F;
			if (this.fm.CT.Weapons[3][3].haveBullets()) if (t > (y + 1F) * 2F - 3F) t = (y + 1F) * 2F - 3F;
		} else if (weaponsName.equals("A54xLT50_spread")) {
			if (this.fm.CT.Weapons[3][4].haveBullets()) if (t > (-y + 1F) * 2F - 3F) t = (y - 1F) * -2F - 3F;
			if (this.fm.CT.Weapons[3][7].haveBullets()) if (t > (y + 1F) * 2F - 3F) t = (y + 1F) * 2F - 3F;
		}
		orient.setYPR(y, t, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (this.fm.CT.BayDoorControl > 0.5F) {
			float gunTangage = this.hookGunner().getGunMove().getTangage();
			gunTangage = Math.min(gunTangage, GUN_OFFSET_TANGAGE * (((HE_177A5) this.aircraft()).getBombBayPos() - 1F));
			this.hookGunner().resetMove(0.0F, gunTangage);
		}
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
		}
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	public CockpitHE_177A5_BGunner() {
		super("3DO/Cockpit/He-177A-5/BGunner.him", "he111_gunner");
		this.hook1 = null;
		this.onAuto = true;
	}

	private Hook                 hook1;
	private boolean              onAuto;
	private boolean              isOppositeSideAiControlled;
	protected static final float GUN_OFFSET_TANGAGE = -60.0F;

	static {
		Class class1 = CockpitHE_177A5_BGunner.class;
		Property.set(class1, "aiTuretNum", 2);
		Property.set(class1, "weaponControlNum", 12);
		Property.set(class1, "astatePilotIndx", 2);
	}
}
