package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class HurricaneMkIIbMod extends Hurricane implements TypeFighter, TypeStormovik {

	public HurricaneMkIIbMod() {
	}

	static {
		Class class1 = HurricaneMkIIbMod.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Hurri");
		Property.set(class1, "meshName", "3DO/Plane/HurricaneMkIIbMod(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIIMod.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHURRII.class });
		Property.set(class1, "LOSElevation", 0.965F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 0, 0, 1, 1, 9, 2, 9, 2, 9, 2, 9, 2, 9, 2, 9, 2, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalRock01", "_ExternalDev02", "_ExternalRock02", "_ExternalDev03",
				"_ExternalRock03", "_ExternalDev04", "_ExternalRock04", "_ExternalDev05", "_ExternalRock05", "_ExternalDev06", "_ExternalRock06", "_ExternalRock03", "_ExternalRock04" });
	}
}
