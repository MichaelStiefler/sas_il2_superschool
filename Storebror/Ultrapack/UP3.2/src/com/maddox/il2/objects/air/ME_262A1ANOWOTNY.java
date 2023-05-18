package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class ME_262A1ANOWOTNY extends ME_262 implements TypeAcePlane {

	public ME_262A1ANOWOTNY() {
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.Skill = 3;
	}

	static {
		Class class1 = ME_262A1ANOWOTNY.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Me 262");
		Property.set(class1, "meshName", "3DO/Plane/Me-262A-1a(ofNowotny)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "FlightModel", "FlightModels/Me-262(ofNowotny).fmd");
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
		weaponHooksRegister(class1,
				new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07",
						"_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19",
						"_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24" });
	}
}
