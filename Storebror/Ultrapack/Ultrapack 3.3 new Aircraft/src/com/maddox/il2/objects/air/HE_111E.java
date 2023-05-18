package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class HE_111E extends HE_111ABE implements TypeBomber, TypeTransport {

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f < -15F) {
					f = -15F;
					flag = false;
				}
				if (f > 15F) {
					f = 15F;
					flag = false;
				}
				if (f1 < -15F) {
					f1 = -15F;
					flag = false;
				}
				if (f1 > 15F) {
					f1 = 15F;
					flag = false;
				}
				break;

			case 1:
				if (f < -30F) {
					f = -30F;
					flag = false;
				}
				if (f > 30F) {
					f = 30F;
					flag = false;
				}
				if (f1 > 45F) {
					f1 = 45F;
					flag = false;
				}
				if (f1 < 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				break;

			case 2:
				if (f < -30F) {
					f = -30F;
					flag = false;
				}
				if (f > 30F) {
					f = 30F;
					flag = false;
				}
				if (f1 < -30F) {
					f1 = -30F;
					flag = false;
				}
				if (f1 > 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	static {
		Class class1 = HE_111E.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "He-111");
		Property.set(class1, "meshName", "3DO/Plane/He111ABE/hierE.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1937.8F);
		Property.set(class1, "yearExpired", 1943.5F);
		Property.set(class1, "FlightModel", "FlightModels/He111E.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHE_111ABE.class, CockpitHE_111ABE_Bombardier.class, CockpitHE_111ABE_NGunner.class, CockpitHE_111ABE_TGunner.class, CockpitHE_111ABE_BGunner.class });
		Property.set(class1, "LOSElevation", 0.76315F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn71", "_BombSpawn02", "_BombSpawn72", "_BombSpawn03", "_BombSpawn73", "_BombSpawn04", "_BombSpawn74", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08",
						"_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22",
						"_BombSpawn23", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", "_BombSpawn27", "_BombSpawn28", "_BombSpawn29", "_BombSpawn30", "_BombSpawn31", "_BombSpawn32", "_BombSpawn33", "_BombSpawn34", "_BombSpawn35", "_BombSpawn36",
						"_BombSpawn37", "_BombSpawn38", "_BombSpawn39", "_BombSpawn40", "_BombSpawn41", "_BombSpawn42", "_BombSpawn43", "_BombSpawn44", "_BombSpawn45", "_BombSpawn46", "_BombSpawn47", "_BombSpawn48", "_BombSpawn49", "_BombSpawn50",
						"_BombSpawn51", "_BombSpawn52", "_BombSpawn53", "_BombSpawn54" });
	}
}
