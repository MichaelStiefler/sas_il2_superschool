package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F6F3Early extends F6F {

	public F6F3Early() {
		this.flapps = 0.0F;
	}

	public void update(float f) {
		super.update(f);
		float f1 = this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(this.flapps - f1) > 0.01F) {
			this.flapps = f1;
			for (int i = 1; i < 8; i++) {
				this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f1, 0.0F);
			}

		}
	}

	private float flapps;

	static {
		Class class1 = F6F3Early.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "F6F");
		Property.set(class1, "meshName", "3DO/Plane/F6F-3(Multi1)/hier_Early.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
		Property.set(class1, "meshName_us", "3DO/Plane/F6F-3(USA)/hier_Early.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar02());
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/F6F3Early.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitF6F3.class });
		Property.set(class1, "LOSElevation", 1.16055F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 3, 9, 3, 3, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev03", "_ExternalDev04",
				"_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
	}
}
