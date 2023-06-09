package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Do17Z_2 extends Do17 {

	protected void moveBayDoor(float f) {
		if (f < 0.02F) {
			this.hierMesh().chunkVisible("Bay_D0", true);
			this.hierMesh().chunkVisible("BayL01_D0", false);
			this.hierMesh().chunkVisible("BayR01_D0", false);
		} else {
			this.hierMesh().chunkVisible("Bay_D0", false);
			this.hierMesh().chunkVisible("BayL01_D0", true);
			this.hierMesh().chunkVisible("BayR01_D0", true);
			this.hierMesh().chunkSetAngles("BayL01_D0", 0.0F, Aircraft.cvt(f, 0.04F, 0.7F, 0.0F, 120.5F), 0.0F);
			this.hierMesh().chunkSetAngles("BayR01_D0", 0.0F, Aircraft.cvt(f, 0.04F, 0.7F, 0.0F, -120.5F), 0.0F);
		}
	}

	public void doWoundPilot(int paramInt, float paramFloat) {
		switch (paramInt) {
			case 1:
				this.FM.turret[0].setHealth(paramFloat);
				break;

			case 2:
				this.FM.turret[1].setHealth(paramFloat);
				this.FM.turret[3].setHealth(paramFloat);
				this.FM.turret[4].setHealth(paramFloat);
				break;

			case 3:
				this.FM.turret[2].setHealth(paramFloat);
				// fall through

			case 4:
				this.FM.turret[5].setHealth(paramFloat);
				break;
		}
	}

	static {
		Class class1 = Do17Z_2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Do-17Z");
		Property.set(class1, "meshName", "3do/plane/Do-17_Z2/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1945F);
		Property.set(class1, "FlightModel", "FlightModels/Do17Z2.fmd");
		Property.set(class1, "cockpitClass",
				new Class[] { CockpitDo17.class, CockpitDo17_Bombardier.class, CockpitDo17_NGunner.class, CockpitDo17_TGunner.class, CockpitDo17_BGunner.class, CockpitDo17_LGunner.class, CockpitDo17_RGunner.class, CockpitDo17_NGunner2.class });
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 15, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09",
				"_BombSpawn10", "_BombSpawn05", "_MGUN06", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14" });
	}
}
