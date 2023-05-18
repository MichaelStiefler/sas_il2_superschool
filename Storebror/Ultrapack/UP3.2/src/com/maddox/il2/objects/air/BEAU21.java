package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BEAU21 extends BEAU implements TypeFighter, TypeStormovik {

	public BEAU21() {
	}

	static {
		Class class1 = BEAU21.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Beaufighter");
		Property.set(class1, "meshName", "3DO/Plane/BeaufighterMk21(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "meshName_gb", "3DO/Plane/BeaufighterMk21(AU)/hier.him");
		Property.set(class1, "PaintScheme_gb", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1965.5F);
		Property.set(class1, "FlightModel", "FlightModels/BeaufighterMk21.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBEAU21.class });
		Property.set(class1, "LOSElevation", 0.7394F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 9, 9, 3, 3, 9, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev01", "_ExternalBomb01",
				"_ExternalDev04", "_ExternalDev05", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
	}
}
