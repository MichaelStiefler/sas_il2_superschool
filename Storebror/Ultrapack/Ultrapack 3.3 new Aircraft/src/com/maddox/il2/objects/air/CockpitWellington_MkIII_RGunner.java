package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitWellington_MkIII_RGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("TurretRA", 0.0F, -orient.getYaw() + 30F, 0.0F);
		this.mesh.chunkSetAngles("TurretRB", 25F, orient.getTangage(), 0.0F);
		this.mesh.chunkSetAngles("TurretRC", 0.0F, -orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
		else {
			float f = orient.getYaw();
			float f1 = orient.getTangage();
			if (f < -45F) f = -45F;
			if (f > 50F) f = 50F;
			if (f1 > 32F) f1 = 32F;
			if (f1 < -40F) f1 = -40F;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
		}
	}

	protected void interpTick() {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
			if (this.bGunFire) {
				if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN08");
				this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN08");
			}
		}
	}

	public void doGunFire(boolean flag) {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			else this.bGunFire = flag;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public CockpitWellington_MkIII_RGunner() {
		super("3DO/Cockpit/B-25J-RGun/RGunnerWellingtonMkIII.him", "he111_gunner");
		this.hook1 = null;
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("TurretLA", 0.0F, this.aircraft().FM.turret[2].tu[0], 0.0F);
		this.mesh.chunkSetAngles("TurretLB", 0.0F, this.aircraft().FM.turret[2].tu[1], 0.0F);
		this.mesh.chunkSetAngles("TurretLC", 0.0F, this.aircraft().FM.turret[2].tu[1], 0.0F);
		this.mesh.chunkVisible("TurretLC", false);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
		if ((this.fm.AS.astateCockpitState & 8) != 0) {
			this.mesh.chunkVisible("XGlassDamage1", true);
			this.mesh.chunkVisible("XHullDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
			this.mesh.chunkVisible("XGlassDamage2", true);
			this.mesh.chunkVisible("XHullDamage2", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
			this.mesh.chunkVisible("XGlassDamage2", true);
			this.mesh.chunkVisible("XHullDamage3", true);
		}
	}

	private Hook hook1;

	static {
		Property.set(CockpitWellington_MkIII_RGunner.class, "aiTuretNum", 3);
		Property.set(CockpitWellington_MkIII_RGunner.class, "weaponControlNum", 13);
		Property.set(CockpitWellington_MkIII_RGunner.class, "astatePilotIndx", 5);
	}
}
