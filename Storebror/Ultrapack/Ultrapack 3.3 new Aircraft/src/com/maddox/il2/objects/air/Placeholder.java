package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Placeholder extends Scheme1 implements TypeScout {
	static {
		Class class1 = Placeholder.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "SAS");
		Property.set(class1, "meshName", "3DO/Plane/SASPlaceholder/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
		Property.set(class1, "yearService", 1910F);
		Property.set(class1, "yearExpired", 1970F);
		Property.set(class1, "FlightModel", "FlightModels/Generic.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109F2.class });
		Property.set(class1, "LOSElevation", 1.01885F);
		Aircraft.weaponTriggersRegister(class1, new int[] {});
		Aircraft.weaponHooksRegister(class1, new String[] {});
	}
}
