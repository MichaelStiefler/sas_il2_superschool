package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class BF_110E1 extends BF_110 {

	public BF_110E1() {
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	public static boolean bChangedPit = false;

	static {
		Class class1 = BF_110E1.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Bf-110");
		Property.set(class1, "meshName", "3DO/Plane/Bf-110E-1/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Bf-110Evroeg.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBF_110E1.class, CockpitBF_110E1_Gunner.class });
		Property.set(class1, "LOSElevation", 0.66895F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 10, 9, 9, 9, 3, 3, 3, 3, 3, 3, 0, 0, 1, 9, 9, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_CANNON01", "_CANNON02", "_CANNON01", "_CANNON02", "_MGUN07", "_MGUN08", "_MGUN05", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb01", "_ExternalBomb02",
						"_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock01", "_ExternalRock02",
						"_ExternalRock03", "_ExternalRock04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
	}
}
