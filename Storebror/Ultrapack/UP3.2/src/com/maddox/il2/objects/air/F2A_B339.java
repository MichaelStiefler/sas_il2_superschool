package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F2A_B339 extends F2A {

	public F2A_B339() {
	}

	static {
		Class class1 = F2A_B339.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Buffalo");
		Property.set(class1, "meshName", "3DO/Plane/BuffaloMkI(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "originCountry", PaintScheme.countryBritain);
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/F2A-1.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB339.class });
		Property.set(class1, "LOSElevation", 1.032F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02" });
	}
}
