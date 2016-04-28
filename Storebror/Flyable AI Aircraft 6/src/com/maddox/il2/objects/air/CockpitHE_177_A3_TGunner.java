package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_177_A3_TGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (!super.doFocusEnter())
			return false;
		((HE_177A3) fm.actor).bPitUnfocused = false;
		aircraft().hierMesh().chunkVisible("Pilot5_D0", false);
		aircraft().hierMesh().chunkVisible("Hmask5_D0", false);
		aircraft().hierMesh().chunkVisible("Pilot5_D1", false);
		aircraft().hierMesh().chunkVisible("Turret5A_D0", false);
		aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
		return true;
	}

	protected void doFocusLeave() {
		if (isFocused()) {
			((HE_177A3) fm.actor).bPitUnfocused = true;
			if (!this.fm.AS.isPilotParatrooper(4)) {
				aircraft().hierMesh().chunkVisible("Pilot5_D0", !this.fm.AS.isPilotDead(4));
				aircraft().hierMesh().chunkVisible("Pilot5_D1", this.fm.AS.isPilotDead(4));
			}
			aircraft().hierMesh().chunkVisible("Turret5A_D0", true);
			aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
			super.doFocusLeave();
		}
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("z_Turret2A", 0.0F, orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("z_Turret2B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!isRealMode())
			return;
		if (!aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 27.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage() + GUN_OFFSET_TANGAGE;
		float minf1 = -10F;
		float maxf1 = 80F;
		if (f1 < minf1) {
			f1 = minf1;
		} else if (f1 > maxf1) {
			f1 = maxf1;
		}
		float f2 = Math.abs(f);
		if (f2 < 10F)
			minf1 = Math.max(minf1, 10F * ((float) Math.cos(Math.PI * f2 / 100F) - 1F) + 27F * (float) Math.cos(Math.PI * f2 / 20F));
		else if (f2 < 50F)
			minf1 = Math.max(minf1, 10F * ((float) Math.cos(Math.PI * f2 / 100F) - 1F));
		else if (f2 > 90F && f2 < 98F)
			minf1 = Math.max(minf1, 5F + 15F * ((float) Math.cos(Math.PI * (98F - f2) / 16F) - 1F));
		else if (f2 >= 98F && f2 < 130F)
			minf1 = Math.max(minf1, 5F + 5F * ((float) Math.cos(Math.PI * (98F - f2) / 64F) - 1F));
		else if (f2 >= 130F)
			minf1 = Math.max(minf1, 10F + 10F * ((float) Math.cos(Math.PI * (180F - f2) / 100F) - 1F));
		if (f1 < minf1) {
			this.tangageOffset += minf1 - f1;
			f1 = minf1;
		} else {
			if (this.tangageOffset < f1 - minf1) { // Offset is smaller than difference between current f1 and minf1
				f1 -= this.tangageOffset;
				this.tangageOffset = 0F;
			} else { // Offset is larger than difference between current f1 and minf1
				this.tangageOffset -= f1 - minf1;
				f1 = minf1;
			}
		}

		orient.setYPR(f, f1 - GUN_OFFSET_TANGAGE, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!isRealMode())
			return;
		if (this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
			this.bGunFire = false;
		this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (hook1 == null)
				hook1 = new HookNamed(aircraft(), "_MGUN06");
			doHitMasterAircraft(aircraft(), hook1, "_MGUN06");
		}
		if (this.emitter != null) {
			boolean flag = this.emitter.countBullets() % 2 == 0;
			this.mesh.chunkVisible("ZTurret2B-Bullet01", flag);
			this.mesh.chunkVisible("ZTurret2B-Bullet02", !flag);
		}
	}

	public void doGunFire(boolean flag) {
		if (!isRealMode())
			return;
		if (this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
			this.bGunFire = false;
		else
			this.bGunFire = flag;
		this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
	}

	public CockpitHE_177_A3_TGunner() {
		super("3DO/Cockpit/He-177A-3/TGunner.him", "he111_gunner");
		this.hook1 = null;
		this.tangageOffset = 0F;
	}

	private Hook hook1;
	private float tangageOffset;
	protected static final float GUN_OFFSET_TANGAGE = 27.0F;

	static {
		Class class1 = CockpitHE_177_A3_TGunner.class;
		Property.set(class1, "aiTuretNum", 4);
		Property.set(class1, "weaponControlNum", 14);
		Property.set(class1, "astatePilotIndx", 4);
	}
}