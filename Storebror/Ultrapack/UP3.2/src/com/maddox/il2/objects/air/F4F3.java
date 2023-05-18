package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F4F3 extends F4F {

	public F4F3() {
	}

	protected void moveFlap(float f) {
		float f1 = -50F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	static {
		Class class1 = F4F3.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "F4F");
		Property.set(class1, "meshName", "3DO/Plane/F4F-3(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
		Property.set(class1, "meshName_us", "3DO/Plane/F4F-3(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar02());
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/F4F-3.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitF4F3.class });
		Property.set(class1, "LOSElevation", 1.28265F);
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02" });
	}
}
