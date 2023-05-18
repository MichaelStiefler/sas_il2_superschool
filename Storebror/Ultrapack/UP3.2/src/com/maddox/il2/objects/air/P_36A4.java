package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_36A4 extends P_36 {

	public P_36A4() {
		this.kangle = 0.0F;
	}

	public void update(float f) {
		for (int i = 1; i < 9; i++) {
			this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -10F * this.kangle, 0.0F);
		}

		this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		super.update(f);
	}

	private float kangle;

	static {
		Class class1 = P_36A4.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P-36");
		Property.set(class1, "meshName", "3DO/Plane/Hawk75A-4(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "meshName_us", "3DO/Plane/Hawk75A-4(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar02());
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Hawk75A-4 (Ultrapack).fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_36.class });
		Property.set(class1, "LOSElevation", 1.06965F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06" });
	}
}
