package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_40E extends P_40 {

	public P_40E() {
	}

	static {
		Class class1 = P_40E.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P-40");
		Property.set(class1, "meshName", "3DO/Plane/P-40E(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
		Property.set(class1, "meshName_us", "3DO/Plane/P-40E(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar03());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/P-40E.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_40E.class });
		Property.set(class1, "LOSElevation", 1.06965F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 3, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb01" });
	}
}
