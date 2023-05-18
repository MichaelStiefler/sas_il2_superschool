package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class FW_190A2 extends FW_190NEW implements TypeFighter, TypeBNZFighter {

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	public void update(float f) {
		this.afterburnerhud();
		super.update(f);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (this.getGunByHookName("_MGUN01") instanceof GunEmpty) {
			this.hierMesh().chunkVisible("7mmC_D0", false);
			this.hierMesh().chunkVisible("7mmCowl_D0", true);
		}
		if (this.getGunByHookName("_CANNON03") instanceof GunEmpty) { this.hierMesh().chunkVisible("20mmL_D0", false); }
		if (this.getGunByHookName("_CANNON04") instanceof GunEmpty) { this.hierMesh().chunkVisible("20mmR_D0", false); }
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

	protected void afterburnerhud() {
		if (this.FM.isPlayers() && this.FM.EI.engines[0].getControlAfterburner()) { HUD.logRightBottom("Start- und Notleistung ENABLED!"); }
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		if (this.FM.CT.getGear() < 0.98F) {
			return;
		} else {
			this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
			return;
		}
	}

	public static boolean bChangedPit = false;

	static {
		Class class1 = FW_190A2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "FW190");
		Property.set(class1, "meshName", "3DO/Plane/Fw-190A-3(Beta)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1942.6F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Fw-190A-2 (Ultrapack).fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190A4.class });
		Property.set(class1, "LOSElevation", 0.764106F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 9, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01" });
	}
}
