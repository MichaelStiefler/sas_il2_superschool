
package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitIL2_GunnerOpenFieldMod extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = -orient.getTangage();
		this.mesh.chunkSetAngles("Turret1A", f, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Turret1B", 0.0F, -f1, 0.0F);
		this.mesh.chunkSetAngles("Turret1C", 0.0F, f, 0.0F);
		float f2 = 0.01905F * (float) Math.sin(Math.toRadians(f));
		float f3 = 0.01905F * (float) Math.cos(Math.toRadians(f));
		f = (float) Math.toDegrees(Math.atan(f2 / (f3 + 0.3565F)));
		this.mesh.chunkSetAngles("Turret1D", 0.0F, -f, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (this.isRealMode()) {
			if (!this.aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f1 < -15F) {
					f1 = -15F;
				}
				if (f1 > 45F) {
					f1 = 45F;
				}
				if (f < -135F) {
					f = -135F;
				}
				if (f > 135F) {
					f = 135F;
				}
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
		}
	}

	protected void interpTick() {
		if (this.isRealMode()) {
			if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
				this.bGunFire = false;
			}
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
			if (this.bGunFire) {
				if (this.hook1 == null) {
					this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
				}
				this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
				if (this.hook2 == null) {
					this.hook2 = new HookNamed(this.aircraft(), "_MGUN04");
				}
				this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN04");
			}
		}
	}

	public void doGunFire(boolean flag) {
		if (this.isRealMode()) {
			if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
				this.bGunFire = false;
			} else {
				this.bGunFire = flag;
			}
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public CockpitIL2_GunnerOpenFieldMod() {
		super("3DO/Cockpit/Il-2-GunFieldMod/hier.him", "il2rear_open");
		this.bNeedSetUp = true;
		this.l = new Loc();
		this.hook1 = null;
		this.hook2 = null;
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
		this.mesh.materialReplace("Gloss1D1o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
		this.mesh.materialReplace("Gloss1D2o", mat);
		mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
		this.mesh.materialReplace("Gloss2D2o", mat);
	}

	protected void reflectPlaneToModel() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
		this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
		this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
		this.mesh.chunkVisible("CF_D3", hiermesh.isChunkVisible("CF_D3"));
		this.mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
		this.mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
		this.mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
		this.mesh.chunkVisible("Tail1_D3", hiermesh.isChunkVisible("Tail1_D3"));
	}

	private boolean bNeedSetUp;
	Loc l;
	private Hook hook1;
	private Hook hook2;

	static {
		Class clazz = CockpitIL2_GunnerOpenFieldMod.class;
		Property.set(clazz, "aiTuretNum", 0);
		Property.set(clazz, "weaponControlNum", 10);
		Property.set(clazz, "astatePilotIndx", 1);
	}
}
