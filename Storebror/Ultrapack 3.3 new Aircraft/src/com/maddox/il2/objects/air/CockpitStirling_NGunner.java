package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitStirling_NGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
		this.mesh.chunkSetAngles("TurretE", -orient.getYaw(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("TurretF", 0.0F, orient.getTangage(), 0.0F);
		this.mesh.chunkSetAngles("TurretG", -this.cvt(orient.getYaw(), -33F, 33F, -33F, 33F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("TurretH", 0.0F, this.cvt(orient.getTangage(), -10F, 32F, -10F, 32F), 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[0] = this.cvt(Math.max(Math.abs(orient.getYaw()), Math.abs(orient.getTangage())), 0.0F, 20F, 0.0F, 0.3F);
		this.mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
	}

	public void clipAnglesGun(Orient orient) {
		if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
		else {
			float f = orient.getYaw();
			float f1 = orient.getTangage();
			if (f < -38F) f = -38F;
			if (f > 38F) f = 38F;
			if (f1 > 38F) f1 = 38F;
			if (f1 < -41F) f1 = -41F;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
		}
	}

	protected void interpTick() {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
			if (this.bGunFire) {
				if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
				this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
				if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN02");
				this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN02");
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

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
		super.doFocusLeave();
	}

	public CockpitStirling_NGunner() {
		super("3DO/Cockpit/Stirling-NGun/hier.him", "bf109");
		this.hook1 = null;
		this.hook2 = null;
	}

	private Hook hook1;
	private Hook hook2;

	static {
		Property.set(CockpitStirling_NGunner.class, "aiTuretNum", 0);
		Property.set(CockpitStirling_NGunner.class, "weaponControlNum", 10);
		Property.set(CockpitStirling_NGunner.class, "astatePilotIndx", 1);
	}
}
