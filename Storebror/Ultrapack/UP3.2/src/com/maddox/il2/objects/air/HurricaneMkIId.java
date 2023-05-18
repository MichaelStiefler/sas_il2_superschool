package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class HurricaneMkIId extends Hurricane implements TypeStormovik, TypeStormovikArmored {

	public HurricaneMkIId() {
	}

	static {
		Class class1 = HurricaneMkIId.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Hurri");
		Property.set(class1, "meshName", "3DO/Plane/HurricaneMkIId(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIId.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHURRII.class });
		Property.set(class1, "LOSElevation", 0.66895F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
	}
}
