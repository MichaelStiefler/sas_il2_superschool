package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class KI_49_II extends KI_49 implements TypeBomber {

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.06F, 0.0F, -70F), 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.06F, 0.0F, -70F), 0.0F);
		hiermesh.chunkSetAngles("GearL10_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -38F), 0.0F);
		hiermesh.chunkSetAngles("GearL11_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -45F));
		hiermesh.chunkSetAngles("GearL13_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -157F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.3F, 0.35F, 0.0F, -70F), 0.0F);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f1, 0.3F, 0.35F, 0.0F, -70F), 0.0F);
		hiermesh.chunkSetAngles("GearR10_D0", 0.0F, Aircraft.cvt(f1, 0.34F, 0.99F, 0.0F, -38F), 0.0F);
		hiermesh.chunkSetAngles("GearR11_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.05F, 0.75F, 0.0F, -45F));
		hiermesh.chunkSetAngles("GearR13_D0", 0.0F, Aircraft.cvt(f1, 0.34F, 0.99F, 0.0F, -157F), 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		KI_49_II.moveGear(this.hierMesh(), f, f1, f2);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f < -35F) {
					f = -35F;
					flag = false;
				}
				if (f > 35F) {
					f = 35F;
					flag = false;
				}
				if (f1 < -25F) {
					f1 = -25F;
					flag = false;
				}
				if (f1 > 30F) {
					f1 = 30F;
					flag = false;
				}
				break;

			case 1:
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -5F) {
					f1 = -5F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				break;

			case 2:
				if (f < -20F) {
					f = -20F;
					flag = false;
				}
				if (f > 20F) {
					f = 20F;
					flag = false;
				}
				if (f1 < -89F) {
					f1 = -89F;
					flag = false;
				}
				if (f1 > -12F) {
					f1 = -12F;
					flag = false;
				}
				break;

			case 3:
				if (f < -10F) {
					f = -10F;
					flag = false;
				}
				if (f > 10F) {
					f = 10F;
					flag = false;
				}
				if (f1 < -10F) {
					f1 = -10F;
					flag = false;
				}
				if (f1 > 10F) {
					f1 = 10F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.hierMesh().chunkSetAngles("Blister4_D0", 0.0F, -45F, 0.0F);
		this.hierMesh().chunkSetAngles("Blister5_D0", 0.0F, -45F, 0.0F);
		this.hierMesh().chunkSetAngles("Blister6_D0", 0.0F, -45F, 0.0F);
		this.hierMesh().chunkSetAngles("Turret3C_D0", -45F, 0.0F, 0.0F);
	}

	static {
		Class class1 = KI_49_II.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ki-21");
		Property.set(class1, "meshName", "3DO/Plane/Ki-49-II/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Ki-49-II.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitKI_49_II.class, CockpitKI_49_II_Bombardier.class, CockpitKI_49_II_NGunner.class, CockpitKI_49_II_AGunner.class, CockpitKI_49_II_TGunner.class, CockpitKI_49_II_BGunner.class });
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09" });
	}
}
