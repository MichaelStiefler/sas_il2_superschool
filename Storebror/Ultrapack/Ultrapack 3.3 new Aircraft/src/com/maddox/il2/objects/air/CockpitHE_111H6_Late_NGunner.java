package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111H6_Late_NGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = false;
			this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Head1_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
			this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
			this.aircraft().hierMesh().chunkVisible("Window_D0", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		((HE_111) ((Interpolate) this.fm).actor).bPitUnfocused = true;
		this.aircraft().hierMesh().chunkVisible("Cockpit_D0", this.aircraft().hierMesh().isChunkVisible("Nose_D0") || this.aircraft().hierMesh().isChunkVisible("Nose_D1") || this.aircraft().hierMesh().isChunkVisible("Nose_D2"));
		this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
		this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
		this.aircraft().hierMesh().chunkVisible("Head1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
		this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
		this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
		this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
		this.aircraft().hierMesh().chunkVisible("Window_D0", true);
		super.doFocusLeave();
	}

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
		if (!this.isRealMode()) return;
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -25F) f = -25F;
		if (f > 15F) f = 15F;
		if (f1 > 0.0F) f1 = 0.0F;
		if (f1 < -40F) f1 = -40F;
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) this.mesh.chunkSetAngles("Butona", 0.15F, 0.0F, 0.0F);
		else this.mesh.chunkSetAngles("Butona", 0.0F, 0.0F, 0.0F);
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	public CockpitHE_111H6_Late_NGunner() {
		super("3DO/Cockpit/He-111P-4-NGun/hier-H6-Late.him", "he111_gunner");
		this.way = 0.0F;
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * this.fm.CT.ElevatorControl);
		this.mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * this.fm.CT.AileronControl);
		float f1 = this.waypointAzimuthInvertMinus(30F);
		if (this.useRealisticNavigationInstruments()) {
			this.mesh.chunkSetAngles("Z_Compass3", this.fm.Or.azimut() - f1 + 90F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_Compass4", -f1, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_Compass5", this.radioCompassAzimuthInvertMinus() - this.fm.Or.azimut(), 0.0F, 0.0F);
		} else {
			this.mesh.chunkSetAngles("Z_Compass4", -this.fm.Or.azimut() - 90F, 0.0F, 0.0F);
			this.way = 0.95F * this.way + 0.05F * (f1 - this.fm.Or.azimut());
			this.mesh.chunkSetAngles("Z_Compass3", this.way, 0.0F, 0.0F);
		}
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("ZHolesL_D1", true);
		if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("ZHolesL_D2", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("ZHolesR_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("ZHolesR_D2", true);
		if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("ZHolesF_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("zOil_D1", true);
	}

	float way;

	static {
		Property.set(CockpitHE_111H6_Late_NGunner.class, "aiTuretNum", 0);
		Property.set(CockpitHE_111H6_Late_NGunner.class, "weaponControlNum", 10);
		Property.set(CockpitHE_111H6_Late_NGunner.class, "astatePilotIndx", 1);
	}
}
