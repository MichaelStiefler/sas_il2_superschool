package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.GunEmpty;

public class FW_190A_BASE extends FW_190 {

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (!applyLoadoutVisibility) return;
		if (this.getGunByHookName("_MGUN01") instanceof GunEmpty) {
			this.hierMesh().chunkVisible("7mmC_D0", false);
			this.hierMesh().chunkVisible("7mmCowl_D0", true);
		}
		if (this.getGunByHookName("_CANNON03") instanceof GunEmpty) { this.hierMesh().chunkVisible("20mmL_D0", false); }
		if (this.getGunByHookName("_CANNON04") instanceof GunEmpty) { this.hierMesh().chunkVisible("20mmR_D0", false); }
		if (!(this.getGunByHookName("_ExternalDev05") instanceof GunEmpty)) {
			this.hierMesh().chunkVisible("Flap01_D0", false);
			this.hierMesh().chunkVisible("Flap01Holed_D0", true);
		}
		if (!(this.getGunByHookName("_ExternalDev06") instanceof GunEmpty)) {
			this.hierMesh().chunkVisible("Flap04_D0", false);
			this.hierMesh().chunkVisible("Flap04Holed_D0", true);
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 77F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 77F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 157F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 157F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC99_D0", 20F * f, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
		float f1 = Math.max(-f * 1500F, -94F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		if (this.FM.CT.getGear() < 0.98F) return;
		this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
	}

	public void update(float f) {
		if (this.FM.isPlayers() && this.FM.EI.engines[0].getControlAfterburner()) { HUD.logRightBottom("Start- und Notleistung ENABLED!"); }
		super.update(f);
	}

	boolean applyLoadoutVisibility = true;

}
