package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitPZL42_TGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("Turret1A", 0.0F, orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -130F) f = -130F;
		if (f > 130F) f = 130F;
		if (f1 > 50F) f1 = 50F;
		if (f1 < -15F) f1 = -15F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
			if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN04");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN04");
		}
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	public CockpitPZL42_TGunner() {
		super("3DO/Cockpit/PZL42_TGun/hier.him", "he111_gunner");
		this.hook1 = null;
		this.bNeedSetUp = true;
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
	}

	protected void reflectPlaneMats() {
		this.mesh.materialReplace("Gloss1D0o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss1D0o")));
		this.mesh.materialReplace("Gloss2D0o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss2D0o")));
	}

	private Hook    hook1;
	private Hook    hook2;
	private boolean bNeedSetUp;

	static {
		Property.set(CockpitPZL42_TGunner.class, "aiTuretNum", 0);
		Property.set(CockpitPZL42_TGunner.class, "weaponControlNum", 10);
		Property.set(CockpitPZL42_TGunner.class, "astatePilotIndx", 1);
	}
}
