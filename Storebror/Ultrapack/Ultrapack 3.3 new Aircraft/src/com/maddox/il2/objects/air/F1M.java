package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F1M extends F1Mxyz implements TypeSailPlane, TypeScout, TypeFighter, TypeSeaPlane {

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			case 0:
				if (f < -33F) {
					f = -33F;
					flag = false;
				}
				if (f > 33F) {
					f = 33F;
					flag = false;
				}
				if (f1 < -3F) {
					f1 = -3F;
					flag = false;
				}
				if (f1 > 62F) {
					f1 = 62F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	static {
		Class class1 = F1M.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "F1M");
		Property.set(class1, "meshName", "3DO/Plane/F1M-Pete(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFCSPar05());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/F1M.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitF1M.class, CockpitF1M_Gunner.class });
		Property.set(class1, "LOSElevation", 1.01885F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 3, 3, 10 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_MGUN03" });
	}
}
