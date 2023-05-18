package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class PE_3M extends PE_2 implements TypeFighter {

	public void update(float f) {
		this.hierMesh().chunkSetAngles("Turtle_D0", 0.0F, 0.0F, -2F);
		super.update(f);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -1F) {
					f1 = -1F;
					flag = false;
				}
				if (f1 > 45F) {
					f1 = 45F;
					flag = false;
				}
				break;

			case 1:
				if (f < -2F) {
					f = -2F;
					flag = false;
				}
				if (f > 2.0F) {
					f = 2.0F;
					flag = false;
				}
				if (f1 < -2F) {
					f1 = -2F;
					flag = false;
				}
				if (f1 > 2.0F) {
					f1 = 2.0F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 1:
				this.FM.turret[0].bIsOperable = false;
				this.FM.turret[1].bIsOperable = false;
				break;
		}
	}

	static {
		Class class1 = PE_3M.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Pe-3");
		Property.set(class1, "meshName", "3DO/Plane/Pe-3/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Pe-3M.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitPE3_1.class, CockpitPE2_84_TGunner.class });
		Property.set(class1, "LOSElevation", 0.76315F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1, 0, 10, 11, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_CANNON01", "_CANNON02", "_CANNON05", "_MGUN01", "_MGUN02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_ExternalDev01",
						"_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev15", "_ExternalDev16", "_ExternalRock01",
						"_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock15", "_ExternalRock16", "_ExternalRock15",
						"_ExternalRock16" });
	}
}
