package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;

public class CockpitME_264_T1Gunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			this.bEntered = true;
			this.saveFov = Main3D.FOVX;
			if (Config.cur.windowsWideScreenFoV) {
				CmdEnv.top().exec("fov 35");
			} else {
				CmdEnv.top().exec("fov 30");
			}
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
			HotKeyEnv.enable("PanView", false);
			HotKeyEnv.enable("SnapView", false);
			HierMesh hiermesh = this.aircraft().hierMesh();
			hiermesh.chunkVisible("Turret1B_D0", false);
			return true;
		} else
			return false;
	}

	protected void doFocusLeave() {
		if (this.bEntered) {
			this.bEntered = false;
			Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
			CmdEnv.top().exec("fov " + this.saveFov);
			boolean flag = HotKeyEnv.isEnabled("aircraftView");
			HotKeyEnv.enable("PanView", flag);
			HotKeyEnv.enable("SnapView", flag);
			HierMesh hiermesh = this.aircraft().hierMesh();
			hiermesh.chunkVisible("Turret1B_D0", true);
		}
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("Z_TurretA", 0.0F, orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("Z_TurretB", 0.0F, -orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (this.isRealMode())
			if (!this.aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float yaw = orient.getYaw();
				float pitch = orient.getTangage();
				float pitchLimit = 1F;
				if (yaw < -3F && yaw > -7F)
				    pitchLimit = Math.max(pitchLimit, this.cvt(yaw, -7F, -3F, 8F, 1F));
				else if (yaw <= -7F && yaw > -17F)
                    pitchLimit = Math.max(pitchLimit, 8F);
                else if (yaw <= -17F)
                    pitchLimit = Math.max(pitchLimit, this.cvt(yaw, -22F, -17F, 1F, 8F));
                else if (yaw > 9F && yaw < 13F)
                    pitchLimit = Math.max(pitchLimit, this.cvt(yaw, 9F, 13F, 1F, 8F));
                else if (yaw >= 13F && yaw < 17F)
                    pitchLimit = Math.max(pitchLimit, 8F);
                else if (yaw >= 17F)
                    pitchLimit = Math.max(pitchLimit, this.cvt(yaw, 17F, 22F, 8F, 1F));
				if (yaw < -180F) {
					yaw = -180F;
				}
				if (yaw > 180F) {
					yaw = 180F;
				}
				if (pitch > 80F) {
					pitch = 80F;
				} else if (pitch < pitchLimit) {
					pitch = pitchLimit;
				}
				orient.setYPR(yaw, pitch, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
		if (this.bDontShot) {
			this.bGunFire = false;
		}
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
				this.bGunFire = false;
			}
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public void doGunFire(boolean flag) {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
				this.bGunFire = false;
			} else {
				this.bGunFire = flag;
			}
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
	}

	public void reflectCockpitState() {
	}

	public void reflectWorldToInstruments(float f) {
	}

	public CockpitME_264_T1Gunner() {
		super("3DO/Cockpit/Me-264-TGun/hier.him", "he111_gunner");
		this.bNeedSetUp = true;
		this.bEntered = false;
		this.bDontShot = false;
    		this.normZN = 0.5F;
    		this.gsZN = 0.5F;
	}

	private boolean bNeedSetUp;
	private boolean bEntered;
	private float saveFov;
	private boolean bDontShot;

	static {
		Class class1 = CockpitME_264_T1Gunner.class;
		Property.set(class1, "aiTuretNum", 0);
		Property.set(class1, "weaponControlNum", 10);
		Property.set(class1, "astatePilotIndx", 4);
		Property.set(class1, "normZN", 0.5F);
	}
}
