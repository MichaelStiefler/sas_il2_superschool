package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class Yak_7R extends YAK implements TypeTNBFighter {

	public static void moveGear(HierMesh hiermesh, float f) {
		final float f1 = Math.max(-f * 1500F, -60F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
	}

	public void update(float f) {
		this.hierMesh().chunkSetAngles("Water_luk", 0.0F, 12F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		this.hierMesh().chunkSetAngles("Oil_luk", 0.0F, 24F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		super.update(f);
	}

	static {
		final Class class1 = Yak_7R.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Yak");
		Property.set(class1, "meshName", "3DO/Plane/Yak-7R/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
		Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Yak-7R.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_7.class });
		Property.set(class1, "LOSElevation", 0.6116F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03",
						"_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02",
						"_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
	}
}
