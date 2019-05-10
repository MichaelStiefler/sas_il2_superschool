package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class FW_190D15 extends FW_190 {

	public FW_190D15() {
		this.kangle = 0.0F;
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
		if (this.FM.CT.getGear() >= 0.98F) { this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F); }
	}

	public void update(float f) {
		for (int i = 1; i < 15; i++) {
			this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -10F * this.kangle, 0.0F);
		}

		this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		super.update(f);
	}

	private float kangle;

	static {
		Class class1 = FW_190D15.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "FW190");
		Property.set(class1, "meshName", "3DO/Plane/Fw-190D-15(Beta)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1944.6F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Fw-190D-15.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D15.class });
		Property.set(class1, "LOSElevation", 0.764106F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 9, 9, 1, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 9, 9, 1, 1, 9, 9, 2, 2, 9, 9, 1, 1, 9, 9 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalDev02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev05",
						"_ExternalDev06", "_CANNON09", "_CANNON10", "_ExternalDev07", "_ExternalDev08", "_CANNON11", "_CANNON12", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalDev11", "_ExternalDev12",
						"_ExternalDev13", "_ExternalDev14", "_CANNON13", "_CANNON14" });
	}
}
