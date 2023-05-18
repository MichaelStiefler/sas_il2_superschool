package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB24D_FRGunner extends CockpitB24D_FGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("Turret7A_Axis", 0.0F, -orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("Turret7B_D0", 0.0F, orient.getTangage(), 0.0F);
		float f = -this.cvt(orient.getYaw(), -40F, -7F, -40F, -7F) + this.cvt(orient.getTangage(), -45F, -28F, 15F, 0.0F);
		float f1 = this.cvt(orient.getTangage(), -28F, 4F, -28F, 4F);
		this.mesh.chunkSetAngles("TurretRight", f, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -45F) f = -45F;
		if (f > 0.0F) f = 0.0F;
		if (f1 > 15F) f1 = 15F;
		if (f1 < -45F) f1 = -45F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		super.interpTick();
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN11");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN11");
		}
	}

	public CockpitB24D_FRGunner() {
		super("3DO/Cockpit/B-24D-Bombardier/right_gun_hier.him");
		this.hook1 = null;
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("Turret8A_Axis", 0.0F, -40F, 0.0F);
		this.mesh.chunkSetAngles("Turret8B_D0", 0.0F, -40F, 0.0F);
		super.reflectWorldToInstruments(f);
	}

	private Hook hook1;

	static {
		Property.set(CockpitB24D_FRGunner.class, "aiTuretNum", 6);
		Property.set(CockpitB24D_FRGunner.class, "weaponControlNum", 16);
		Property.set(CockpitB24D_FRGunner.class, "astatePilotIndx", 6);
		Property.set(CockpitB24D_FRGunner.class, "normZN", 1.8F);
	}
}
