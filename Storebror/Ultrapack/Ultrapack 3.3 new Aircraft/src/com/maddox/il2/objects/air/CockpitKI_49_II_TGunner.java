package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitKI_49_II_TGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		if (!this.isFocused()) return;
		else {
			this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
			super.doFocusLeave();
			return;
		}
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage() - 9F;
		this.mesh.chunkSetAngles("Turret3A", 0.0F, -f, 0.0F);
		this.mesh.chunkSetAngles("Turret3B", 0.0F, f1, 0.0F);
		if (f < -33F) f = -33F;
		if (f > 33F) f = 33F;
		this.mesh.chunkSetAngles("Turret3D", 0.0F, -f, 0.0F);
		this.mesh.chunkSetAngles("Turret3E", 0.0F, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -50F) f = -50F;
		if (f > 50F) f = 50F;
		if (f1 > this.cvt(Math.abs(f), 0.0F, 50F, 40F, 25F)) f1 = this.cvt(Math.abs(f), 0.0F, 50F, 40F, 25F);
		if (f1 < this.cvt(Math.abs(f), 0.0F, 50F, -10F, -3.5F)) f1 = this.cvt(Math.abs(f), 0.0F, 50F, -10F, -3.5F);
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.iCocking > 0) this.iCocking = 0;
			else this.iCocking = 1;
		} else this.iCocking = 0;
		this.resetYPRmodifier();
		Cockpit.xyz[1] = -0.07F * this.iCocking;
		Cockpit.ypr[1] = 0.0F;
		this.mesh.chunkSetLocate("Turret3C", Cockpit.xyz, Cockpit.ypr);
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	public CockpitKI_49_II_TGunner() {
		super("3DO/Cockpit/Ki49-TGun/hier.him", "he111_gunner");
		this.iCocking = 0;
	}

	private int iCocking;

	static {
		Property.set(CockpitKI_49_II_TGunner.class, "aiTuretNum", 1);
		Property.set(CockpitKI_49_II_TGunner.class, "weaponControlNum", 11);
		Property.set(CockpitKI_49_II_TGunner.class, "astatePilotIndx", 3);
	}
}
