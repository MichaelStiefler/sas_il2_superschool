package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MIG_29 extends MIG_3 {

	public MIG_29() {
	}

	static {
		Class class1 = MIG_29.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "MIG");
		Property.set(class1, "meshName", "3do/plane/MIG-3U(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
		Property.set(class1, "FlightModel", "FlightModels/MiG-29.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_3.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
				"_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
	}
}
