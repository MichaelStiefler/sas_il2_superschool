package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class I_16TYPE24SAFONOV extends I_16 implements TypeAcePlane {

	public I_16TYPE24SAFONOV() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.Skill = 3;
	}

	static {
		Class class1 = I_16TYPE24SAFONOV.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "I-16");
		Property.set(class1, "meshName", "3DO/Plane/I-16type24(ofSafonov)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
		Property.set(class1, "FlightModel", "FlightModels/I-16type24(ofSafonov).fmd");
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
		weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02" });
	}
}
