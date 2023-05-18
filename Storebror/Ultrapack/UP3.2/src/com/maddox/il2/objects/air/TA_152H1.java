package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class TA_152H1 extends TA_152_BASE {

	public void update(float f) {
		if (this.FM.Loc.z > 9000D) {
			if (!this.FM.EI.engines[0].getControlAfterburner()) { this.FM.EI.engines[0].setAfterburnerType(2); }
		} else if (!this.FM.EI.engines[0].getControlAfterburner()) { this.FM.EI.engines[0].setAfterburnerType(1); }
		super.update(f);
	}

	static {
		Class class1 = TA_152H1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ta.152");
		Property.set(class1, "meshName", "3DO/Plane/Ta-152H-1/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1944.6F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Ta-152H1 (Ultrapack).fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitTA_152.class });
		Property.set(class1, "LOSElevation", 0.764106F);
		weaponTriggersRegister(class1, new int[] { 0, 1, 1 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON03", "_CANNON04" });
	}
}
