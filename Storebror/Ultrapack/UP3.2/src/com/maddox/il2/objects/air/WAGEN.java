package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class WAGEN extends CAR {

	public WAGEN() {
	}

	public void update(float f) {
		if (this.FM.AS.bIsAboutToBailout) { this.hierMesh().chunkVisible("Helm_D0", false); }
		this.FM.EI.engines[0].addVside *= 9.9999999999999995E-008D;
		super.update(f);
	}

	static {
		Class class1 = WAGEN.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Wagen");
		Property.set(class1, "meshName", "3do/plane/Wagen/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/Wagen.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitWagen.class });
		Property.set(class1, "LOSElevation", 0.8941F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
	}
}
