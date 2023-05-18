package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class ThunderboltMkII extends P_47ModPack {

	public void update(float f) {
		super.update(f);
		this.bubbleTopTailSway();
	}

	static {
		Class class1 = ThunderboltMkII.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P-47");
		Property.set(class1, "meshName", "3DO/Plane/Thunderbolt_MkII(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1947.5F);
		Property.set(class1, "FlightModel", "FlightModels/P-47D-27.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D27.class });
		Property.set(class1, "LOSElevation", 1.1104F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 9, 9, 9, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev01",
				"_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03" });
	}
}
