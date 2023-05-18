package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class HalifaxBMkII extends Halifax {

	public static void moveGear(HierMesh hiermesh, float f) {
		float f1 = Math.max(-f * 800F, -50F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -f1, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1, 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
		hiermesh.chunkSetAngles("GearC99_D0", 0.0F, 14F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 80F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 80F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 55F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 55F * f, 0.0F);
	}

	protected void moveGear(float f) {
		HalifaxBMkII.moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", -f, 0.0F, 0.0F);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 25:
				this.FM.turret[0].bIsOperable = false;
				break;

			case 26:
				this.FM.turret[1].bIsOperable = false;
				break;

			case 27:
				this.FM.turret[2].bIsOperable = false;
				break;
		}
		return super.cutFM(i, j, actor);
	}

	protected void moveBayDoor(float f) {
		this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, 65F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, 65F * f, 0.0F);
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 4:
				this.FM.turret[0].bIsOperable = false;
				break;

			case 3:
				this.FM.turret[1].bIsOperable = false;
				break;

			case 2:
				this.FM.turret[2].bIsOperable = false;
				break;
		}
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f < -76F) {
					f = -76F;
					flag = false;
				}
				if (f > 76F) {
					f = 76F;
					flag = false;
				}
				if (f1 < -47F) {
					f1 = -47F;
					flag = false;
				}
				if (f1 > 47F) {
					f1 = 47F;
					flag = false;
				}
				break;

			case 1:
				float f2 = Math.abs(f);
				if (f1 > 50F) {
					f1 = 50F;
					flag = false;
				}
				if (f2 < 1.0F) {
					if (f1 < 17F) {
						f1 = 17F;
						flag = false;
					}
					break;
				}
				if (f2 < 4.5F) {
					if (f1 < 0.71429F - 0.71429F * f2) {
						f1 = 0.71429F - 0.71429F * f2;
						flag = false;
					}
					break;
				}
				if (f2 < 29.5F) {
					if (f1 < -2.5F) {
						f1 = -2.5F;
						flag = false;
					}
					break;
				}
				if (f2 < 46F) {
					if (f1 < 52.0303F - 1.84848F * f2) {
						f1 = 52.0303F - 1.84848F * f2;
						flag = false;
					}
					break;
				}
				if (f2 < 89F) {
					if (f1 < -70.73518F + 0.80232F * f2) {
						f1 = -70.73518F + 0.80232F * f2;
						flag = false;
					}
					break;
				}
				if (f2 < 147F) {
					if (f1 < 1.5F) {
						f1 = 1.5F;
						flag = false;
					}
					break;
				}
				if (f2 < 162F) {
					if (f1 < -292.5F + 2.0F * f2) {
						f1 = -292.5F + 2.0F * f2;
						flag = false;
					}
					break;
				}
				if (f1 < 31.5F) {
					f1 = 31.5F;
					flag = false;
				}
				break;

			case 2:
				if (f < -87F) {
					f = -87F;
					flag = false;
				}
				if (f > 87F) {
					f = 87F;
					flag = false;
				}
				if (f1 < -78F) {
					f1 = -78F;
					flag = false;
				}
				if (f1 > 67F) {
					f1 = 67F;
					flag = false;
				}
				break;

			case 3:
				if (f1 < -45F) {
					f1 = -45F;
					flag = false;
				}
				if (f1 > 16F) {
					f1 = 16F;
					flag = false;
				}
				if (f < -60F) {
					f = -60F;
					flag = false;
					if (f1 > -11.5F) f1 = -11.5F;
					break;
				}
				if (f < -13.5F) {
					if (f1 > 3.9836F + 0.25806F * f) {
						f1 = 3.9836F + 0.25806F * f;
						flag = false;
					}
					break;
				}
				if (f < -10.5F) {
					if (f1 > 16.25005F + 1.16667F * f) {
						f1 = 16.25005F + 1.16667F * f;
						flag = false;
					}
					break;
				}
				if (f < 14F) {
					if (f1 > 5F) flag = false;
					break;
				}
				if (f < 80F) {
					if (f1 > 8F) flag = false;
				} else {
					f = 80F;
					flag = false;
				}
				break;

			case 4:
				f = -f;
				if (f1 < -45F) {
					f1 = -45F;
					flag = false;
				}
				if (f1 > 16F) {
					f1 = 16F;
					flag = false;
				}
				if (f < -60F) {
					f = -60F;
					flag = false;
					if (f1 > -11.5F) f1 = -11.5F;
				} else if (f < -13.5F) {
					if (f1 > 3.9836F + 0.25806F * f) {
						f1 = 3.9836F + 0.25806F * f;
						flag = false;
					}
				} else if (f < -10.5F) {
					if (f1 > 16.25005F + 1.16667F * f) {
						f1 = 16.25005F + 1.16667F * f;
						flag = false;
					}
				} else if (f < 14F) {
					if (f1 > 5F) flag = false;
				} else if (f < 80F) {
					if (f1 > 8F) flag = false;
				} else {
					f = 80F;
					flag = false;
				}
				f = -f;
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	static {
		Class class1 = HalifaxBMkII.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "HalifaxII");
		Property.set(class1, "meshName", "3DO/Plane/HalifaxBMkII/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/HalifaxII.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHalifaxBMkII.class, CockpitHalifaxBMkII_Bombardier.class, CockpitHalifaxBMkII_NGunner.class, CockpitHalifaxBMkII_TGunner.class, CockpitHalifaxBMkII_AGunner.class });
		Property.set(class1, "LOSElevation", 0.73425F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN09", "_MGUN10", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08" });
	}
}
