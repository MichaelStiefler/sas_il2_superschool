package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class YAK_9M extends YAK_9TX {

	public YAK_9M() {
	}

	static {
		Class class1 = YAK_9M.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Yak");
		Property.set(class1, "meshName", "3DO/Plane/Yak-9M(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
		Property.set(class1, "meshName_fr", "3DO/Plane/Yak-9M(Multi1)/hier.him");
		Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1952.8F);
		Property.set(class1, "FlightModel", "FlightModels/Yak-9M.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_9T.class });
		Property.set(class1, "LOSElevation", 0.661F);
		weaponTriggersRegister(class1, new int[] { 0, 1, 1, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01", "_CANNON02", "_ExternalDev01" });
	}
}
