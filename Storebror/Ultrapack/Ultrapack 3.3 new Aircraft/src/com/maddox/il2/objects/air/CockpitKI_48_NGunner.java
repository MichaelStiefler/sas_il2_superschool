package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitKI_48_NGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		this.mesh.chunkSetAngles("TurretA", 0.0F, f, 0.0F);
		this.mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
		if (f > 15F) f = 15F;
		if (f1 < -21F) f1 = -21F;
		this.mesh.chunkSetAngles("CameraRodA", 0.0F, f, 0.0F);
		this.mesh.chunkSetAngles("CameraRodB", 0.0F, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
		else {
			float f = orient.getYaw();
			float f1 = orient.getTangage();
			if (f < -25F) f = -25F;
			if (f > 25F) f = 25F;
			if (f1 > 20F) f1 = 20F;
			if (f1 < -40F) f1 = -40F;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
		}
	}

	protected void interpTick() {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public void doGunFire(boolean flag) {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			else this.bGunFire = flag;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public CockpitKI_48_NGunner() {
		super("3DO/Cockpit/Ki48-NGun/hier.him", "he111_gunner");
	}

	public void reflectWorldToInstruments(float f) {
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("ZHolesL_D1", true);
		if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("ZHolesL_D2", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("ZHolesR_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("ZHolesR_D2", true);
		if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("ZHolesF_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("zOil_D1", true);
	}

	static {
		Property.set(CockpitKI_48_NGunner.class, "aiTuretNum", 0);
		Property.set(CockpitKI_48_NGunner.class, "weaponControlNum", 10);
		Property.set(CockpitKI_48_NGunner.class, "astatePilotIndx", 1);
	}
}
