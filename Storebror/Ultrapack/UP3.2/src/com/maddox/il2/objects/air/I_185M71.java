package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class I_185M71 extends I_185 {

	public I_185M71() {
	}

	public void update(float f) {
		this.hierMesh().chunkSetAngles("Water1_D0", 0.0F, -20F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		for (int i = 1; i < 9; i++) {
			this.hierMesh().chunkSetAngles("Oil" + i + "_D0", 0.0F, -15F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		}

		super.update(f);
	}

	static {
		Class class1 = I_185M71.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "I-185");
		Property.set(class1, "meshName", "3DO/Plane/I-185(M-71)(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1945F);
		Property.set(class1, "FlightModel", "FlightModels/I-185M-71.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitI_185M71.class });
		Property.set(class1, "LOSElevation", 0.89135F);
		weaponTriggersRegister(class1, new int[] { 1, 1, 1 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03" });
	}
}
