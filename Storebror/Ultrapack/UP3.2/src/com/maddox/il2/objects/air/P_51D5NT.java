package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_51D5NT extends P_51 {

	public P_51D5NT() {
	}

	static {
		Class class1 = P_51D5NT.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P-51");
		Property.set(class1, "meshName", "3DO/Plane/P-51D-5NT(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "meshName_us", "3DO/Plane/P-51D-5NT(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1947.5F);
		Property.set(class1, "FlightModel", "FlightModels/P-51D-20.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_51D20N9.class });
		Property.set(class1, "LOSElevation", 1.1188F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 3, 3, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
	}
}
