package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Do_26C extends Do_26xyz {

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (this.thisWeaponsName.startsWith("500kg_cargo")) {
			this.FM.M.massEmpty += 500F;
			return;
		} else return;
	}

	static {
		Class class1 = Do_26C.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Do-26C");
		Property.set(class1, "meshName", "3DO/Plane/Do-26/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "yearService", 1938F);
		Property.set(class1, "yearExpired", 1945F);
		Property.set(class1, "FlightModel", "FlightModels/Do26.fmd");
		Property.set(class1, "LOSElevation", 0.5265F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitDo_26.class });
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
	}
}
