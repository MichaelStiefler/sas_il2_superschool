package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F_86F_30 extends F_86F {

	public F_86F_30() {
	}

	static {
		Class localClass = F_86F_30.class;
		new NetAircraft.SPAWN(localClass);
		Property.set(localClass, "iconFar_shortClassName", "F-86");
		Property.set(localClass, "meshName", "3DO/Plane/F-86F(Multi1)/hier.him");
		Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(localClass, "meshName_us", "3DO/Plane/F-86F(USA)/hier.him");
		Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(localClass, "meshName_de", "3DO/Plane/F-86F(Multi1)/hier.him");
		Property.set(localClass, "PaintScheme_de", new PaintSchemeFMPar1956());
		Property.set(localClass, "yearService", 1949.9F);
		Property.set(localClass, "yearExpired", 1960.3F);
		Property.set(localClass, "FlightModel", "FlightModels/F-86F-30.fmd");
		Property.set(localClass, "cockpitClass", new Class[] { CockpitF_86Flate.class });
		Property.set(localClass, "LOSElevation", 0.725F);
		Aircraft.weaponTriggersRegister(localClass, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 9, 2, 2, 9, 2, 2, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3 });
		Aircraft.weaponHooksRegister(localClass,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06",
						"_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12",
						"_ExternalDev13", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04", "_ExternalBomb04", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08",
						"_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalBomb07", "_ExternalBomb08" });
	}
}
