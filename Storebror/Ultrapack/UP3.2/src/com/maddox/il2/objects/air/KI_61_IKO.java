package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class KI_61_IKO extends KI_61 {

	public KI_61_IKO() {
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f, 0.01F, 0.9F, 0.0F, 72F), 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, cvt(f, 0.01F, 0.21F, 0.0F, 57F), 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f, 0.01F, 0.21F, 0.0F, -57F), 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.1F, 0.82F, 0.0F, -82F), 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.1F, 0.82F, 0.0F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, cvt(f, 0.1F, 0.16F, 0.0F, 86F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.34F, 0.91F, 0.0F, -82F), 0.0F);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f, 0.34F, 0.91F, 0.0F, 90F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, cvt(f, 0.34F, 0.4F, 0.0F, -86F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC5_D0", 0.0F, -f, 0.0F);
	}

	static {
		Class class1 = KI_61_IKO.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ki-61");
		Property.set(class1, "meshName", "3DO/Plane/Ki-61-I(Ko)(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
		Property.set(class1, "meshName_ja", "3DO/Plane/Ki-61-I(Ko)(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1946F);
		Property.set(class1, "FlightModel", "FlightModels/Ki-61-IKo.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitKI_61.class });
		Property.set(class1, "LOSElevation", 0.81055F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
	}
}
