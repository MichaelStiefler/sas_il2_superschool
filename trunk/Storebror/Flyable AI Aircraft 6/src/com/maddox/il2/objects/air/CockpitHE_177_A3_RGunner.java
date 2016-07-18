package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_177_A3_RGunner extends CockpitGunner {

	public void reflectWorldToInstruments(float f) {
		if (bNeedSetUp) {
			reflectPlaneMats();
			bNeedSetUp = false;
		}
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = aircraft().hierMesh();
		Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
		this.mesh.materialReplace("Gloss2D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
		this.mesh.materialReplace("Gloss2D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
		this.mesh.materialReplace("Matt1D0o", mat);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 8) != 0)
			this.mesh.chunkVisible("XGlassDamage1", true);
		if ((this.fm.AS.astateCockpitState & 0x20) != 0)
			this.mesh.chunkVisible("XGlassDamage2", true);
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
		this.mesh.chunkSetAngles("TurretC", 0.0F, cvt(orient.getYaw(), -38F, 38F, -15F, 15F), 0.0F);
		this.mesh.chunkSetAngles("TurretD", 0.0F, cvt(orient.getTangage(), -43F, 43F, -10F, 10F), 0.0F);
		this.mesh.chunkSetAngles("TurretE", -orient.getYaw(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("TurretF", 0.0F, orient.getTangage(), 0.0F);
		this.mesh.chunkSetAngles("TurretG", -cvt(orient.getYaw(), -33F, 33F, -33F, 33F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("TurretH", 0.0F, cvt(orient.getTangage(), -10F, 32F, -10F, 32F), 0.0F);
		resetYPRmodifier();
		Cockpit.xyz[0] = cvt(Math.max(Math.abs(orient.getYaw()), Math.abs(orient.getTangage())), 0.0F, 20F, 0.0F, 0.3F);
		this.mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -22.5F)
					f = -22.5F;
				if (f > 38F)
					f = 38F;
				if (f1 > 40F)
					f1 = 40F;
				if (f1 < -20F)
					f1 = -20F;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
				this.bGunFire = false;
			this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
		}
	}

	public void doGunFire(boolean flag) {
		if (isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
				this.bGunFire = false;
			else
				this.bGunFire = flag;
			this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
		}
	}

	public CockpitHE_177_A3_RGunner() {
		super("3DO/Cockpit/He-177-RGun/hier.him", "bf109");
		bNeedSetUp = true;
	}

	private boolean bNeedSetUp;

	static {
		Class class1 = CockpitHE_177_A3_RGunner.class;
		Property.set(class1, "aiTuretNum", 5);
		Property.set(class1, "weaponControlNum", 15);
		Property.set(class1, "astatePilotIndx", 5);
	}
}