package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class UTI_4 extends I_16 {

	public UTI_4() {
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
		Class class1 = UTI_4.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "I-16");
		Property.set(class1, "meshName", "3DO/Plane/UTI-4/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1938F);
		Property.set(class1, "yearExpired", 1946F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitUTI_4.class, CockpitUTI_4VP.class });
		Property.set(class1, "FlightModel", "FlightModels/I-16type15.fmd");
		Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02" });
	}
}
