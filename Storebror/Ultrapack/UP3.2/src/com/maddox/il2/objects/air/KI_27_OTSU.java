package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_27_OTSU extends KI_27 {

	public KI_27_OTSU() {
	}

	static {
		Class class1 = KI_27_OTSU.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ki-27");
		Property.set(class1, "meshName", "3DO/Plane/Ki-27(Otsu)(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "meshName_ja", "3DO/Plane/Ki-27(Otsu)(ja)/hier.him");
		Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
		Property.set(class1, "yearService", 1938F);
		Property.set(class1, "yearExpired", 1946F);
		Property.set(class1, "FlightModel", "FlightModels/Ki-27.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitKI_27OTSU.class });
		Property.set(class1, "LOSElevation", 0.74185F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
	}
}
