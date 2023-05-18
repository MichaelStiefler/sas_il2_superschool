package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_39Q15RECHKALOV extends P_39 implements TypeAcePlane {

	public P_39Q15RECHKALOV() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.Skill = 3;
	}

	static {
		Class class1 = P_39Q15RECHKALOV.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P39");
		Property.set(class1, "meshName", "3do/plane/P-39Q-15(ofRechkalov)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "FlightModel", "FlightModels/P-39Q-15(ofRechkalov).fmd");
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_ExternalBomb01" });
	}
}
