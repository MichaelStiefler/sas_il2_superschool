package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitFokker_G1_RGunner extends CockpitGunner {

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("TurretA", -orient.getYaw(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("TurretB", 0.0F, 0.0F, -orient.getTangage());
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -42F) f = -42F;
		if (f > 42F) f = 42F;
		if (f1 > 60F) f1 = 60F;
		if (f1 < -3F) f1 = -3F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN09");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN09");
		}
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		this.mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
		this.mesh.materialReplace("Gloss1D2o", mat);
	}

	public CockpitFokker_G1_RGunner() {
		super("3DO/Cockpit/Fokker-G1-RGun/RGunnerFG1.him", "he111_gunner");
		this.bNeedSetUp = true;
		this.hook1 = null;
	}

	private boolean bNeedSetUp;
	private Hook    hook1;

	static {
		Property.set(CockpitFokker_G1_RGunner.class, "aiTuretNum", 1);
		Property.set(CockpitFokker_G1_RGunner.class, "weaponControlNum", 10);
		Property.set(CockpitFokker_G1_RGunner.class, "astatePilotIndx", 2);
	}
}
