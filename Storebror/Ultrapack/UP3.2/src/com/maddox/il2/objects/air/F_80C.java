package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F_80C extends X_80 {

	public F_80C() {
	}

	public static boolean bChangedPit = false;

	static {
		Class class1 = F_80C.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P-80C");
		Property.set(class1, "meshName", "3DO/Plane/P-80C/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1946.9F);
		Property.set(class1, "yearExpired", 1955.3F);
		Property.set(class1, "FlightModel", "FlightModels/P-80C.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitF_80C.class });
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06",
						"_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06",
						"_ExternalRock07", "_ExternalRock08" });
	}
}
