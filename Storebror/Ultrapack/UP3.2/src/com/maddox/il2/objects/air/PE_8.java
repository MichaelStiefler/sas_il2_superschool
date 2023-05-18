package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class PE_8 extends Scheme4 implements TypeTransport, TypeBomber {

	public PE_8() {
		this.fSightCurAltitude = 300F;
		this.fSightCurSpeed = 50F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightSetForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
	}

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
		moveGear(this.hierMesh(), f);
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

			case 28:
				this.FM.turret[3].bIsOperable = false;
				break;

			case 29:
				this.FM.turret[4].bIsOperable = false;
				break;
		}
		return super.cutFM(i, j, actor);
	}

	protected void moveBayDoor(float f) {
		this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, 65F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, 65F * f, 0.0F);
	}

	public void msgShot(Shot shot) {
		this.setShot(shot);
		if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) { this.FM.AS.hitEngine(shot.initiator, 0, 1); }
		if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) { this.FM.AS.hitEngine(shot.initiator, 1, 1); }
		if (shot.chunkName.startsWith("Engine3") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) { this.FM.AS.hitEngine(shot.initiator, 2, 1); }
		if (shot.chunkName.startsWith("Engine4") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) { this.FM.AS.hitEngine(shot.initiator, 3, 1); }
		if (shot.chunkName.startsWith("CF")) {
			if (Aircraft.Pd.x > 4.55D && Aircraft.Pd.x < 7.15D && Aircraft.Pd.z > 0.57999998331069946D) {
				if (World.Rnd().nextFloat() < 0.233F) {
					if (Aircraft.Pd.z > 1.21D) {
						this.killPilot(shot.initiator, 0);
						if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }
					} else {
						this.FM.AS.hitPilot(shot.initiator, 0, (int) (shot.power * 0.004F));
					}
				}
				if (World.Rnd().nextFloat() < 0.233F) {
					if (Aircraft.Pd.z > 1.21D) {
						this.killPilot(shot.initiator, 1);
						if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }
					} else {
						this.FM.AS.hitPilot(shot.initiator, 1, (int) (shot.power * 0.004F));
					}
				}
			}
			if (Aircraft.Pd.x > 9.5299997329711914D && Aircraft.Pd.z < 0.14D && Aircraft.Pd.z > -0.62999999523162842D) { this.FM.AS.hitPilot(shot.initiator, 2, (int) (shot.power * 0.002F)); }
			if (Aircraft.Pd.x > 2.4749999046325684D && Aircraft.Pd.x < 4.4899997711181641D && Aircraft.Pd.z > 0.61D && shot.power * Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z) > 11900D && World.Rnd().nextFloat() < 0.45F) {
				for (int i = 0; i < 4; i++) {
					this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 0);
				}

			}
		}
		if (shot.chunkName.startsWith("Turret1")) {
			if (Aircraft.Pd.z > 0.033449999988079071D) {
				this.killPilot(shot.initiator, 2);
				if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }
			} else {
				this.FM.AS.hitPilot(shot.initiator, 2, (int) (shot.power * 0.004F));
			}
			shot.chunkName = "CF_D" + this.chunkDamageVisible("CF");
		}
		if (shot.chunkName.startsWith("Turret2")) {
			if (World.Rnd().nextBoolean()) {
				this.FM.AS.hitPilot(shot.initiator, 4, (int) (shot.power * 0.004F));
			} else {
				this.FM.turret[1].bIsOperable = false;
			}
		}
		if (shot.chunkName.startsWith("Turret3")) {
			if (Aircraft.Pd.z > 0.30445000529289246D) {
				this.killPilot(shot.initiator, 7);
				if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }
			} else {
				this.FM.AS.hitPilot(shot.initiator, 7, (int) (shot.power * 0.002F));
			}
			shot.chunkName = "Tail1_D" + this.chunkDamageVisible("Tail1");
		}
		if (shot.chunkName.startsWith("Turret4")) {
			if (Aircraft.Pd.z > -0.99540001153945923D) {
				this.killPilot(shot.initiator, 5);
				if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }
			} else {
				this.FM.AS.hitPilot(shot.initiator, 5, (int) (shot.power * 0.002F));
			}
		} else if (shot.chunkName.startsWith("Turret5")) {
			if (Aircraft.Pd.z > -0.99540001153945923D) {
				this.killPilot(shot.initiator, 6);
				if (shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }
			} else {
				this.FM.AS.hitPilot(shot.initiator, 6, (int) (shot.power * 0.002F));
			}
		} else {
			super.msgShot(shot);
		}
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 2:
				this.FM.turret[0].bIsOperable = false;
				break;

			case 4:
				this.FM.turret[1].bIsOperable = false;
				break;

			case 5:
				this.FM.turret[3].bIsOperable = false;
				break;

			case 6:
				this.FM.turret[4].bIsOperable = false;
				break;

			case 7:
				this.FM.turret[2].bIsOperable = false;
				break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("Head1_D0", false);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				this.hierMesh().chunkVisible("Head2_D0", false);
				break;

			case 2:
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("HMask3_D0", false);
				this.hierMesh().chunkVisible("Pilot3_D1", true);
				this.hierMesh().chunkVisible("Head3_D0", false);
				break;

			case 3:
				this.hierMesh().chunkVisible("Pilot4_D0", false);
				this.hierMesh().chunkVisible("HMask4_D0", false);
				this.hierMesh().chunkVisible("Pilot4_D1", true);
				this.hierMesh().chunkVisible("Head4_D0", false);
				break;

			case 5:
				this.hierMesh().chunkVisible("Pilot6_D0", false);
				this.hierMesh().chunkVisible("HMask6_D0", false);
				this.hierMesh().chunkVisible("Pilot6_D1", true);
				this.hierMesh().chunkVisible("Head5_D0", false);
				break;

			case 6:
				this.hierMesh().chunkVisible("Pilot7_D0", false);
				this.hierMesh().chunkVisible("HMask7_D0", false);
				this.hierMesh().chunkVisible("Pilot7_D1", true);
				this.hierMesh().chunkVisible("Head6_D0", false);
				break;

			case 7:
				this.hierMesh().chunkVisible("Pilot8_D0", false);
				this.hierMesh().chunkVisible("HMask8_D0", false);
				this.hierMesh().chunkVisible("Pilot8_D1", true);
				this.hierMesh().chunkVisible("Head7_D0", false);
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
					if (f1 > -11.5F) { f1 = -11.5F; }
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
					if (f1 > 5F) { flag = false; }
					break;
				}
				if (f < 80F) {
					if (f1 > 8F) { flag = false; }
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
					if (f1 > -11.5F) { f1 = -11.5F; }
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
					if (f1 > 5F) { flag = false; }
				} else if (f < 80F) {
					if (f1 > 8F) { flag = false; }
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

	public boolean typeBomberToggleAutomation() {
		return false;
	}

	public void typeBomberAdjDistanceReset() {
		this.fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		this.fSightCurForwardAngle += 0.2F;
		if (this.fSightCurForwardAngle > 75F) { this.fSightCurForwardAngle = 75F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
	}

	public void typeBomberAdjDistanceMinus() {
		this.fSightCurForwardAngle -= 0.2F;
		if (this.fSightCurForwardAngle < -15F) { this.fSightCurForwardAngle = -15F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
	}

	public void typeBomberAdjSideslipReset() {
		this.fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		this.fSightCurSideslip++;
		if (this.fSightCurSideslip > 45F) { this.fSightCurSideslip = 45F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
	}

	public void typeBomberAdjSideslipMinus() {
		this.fSightCurSideslip--;
		if (this.fSightCurSideslip < -45F) { this.fSightCurSideslip = -45F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
	}

	public void typeBomberAdjAltitudeReset() {
		this.fSightCurAltitude = 300F;
	}

	public void typeBomberAdjAltitudePlus() {
		this.fSightCurAltitude += 10F;
		if (this.fSightCurAltitude > 6000F) { this.fSightCurAltitude = 6000F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
	}

	public void typeBomberAdjAltitudeMinus() {
		this.fSightCurAltitude -= 10F;
		if (this.fSightCurAltitude < 300F) { this.fSightCurAltitude = 300F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
	}

	public void typeBomberAdjSpeedReset() {
		this.fSightCurSpeed = 50F;
	}

	public void typeBomberAdjSpeedPlus() {
		this.fSightCurSpeed += 5F;
		if (this.fSightCurSpeed > 650F) { this.fSightCurSpeed = 650F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		this.fSightCurSpeed -= 5F;
		if (this.fSightCurSpeed < 50F) { this.fSightCurSpeed = 50F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		double d = this.fSightCurSpeed / 3.6D * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
		d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
		this.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / this.fSightCurAltitude));
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeFloat(this.fSightCurAltitude);
		netmsgguaranted.writeFloat(this.fSightCurSpeed);
		netmsgguaranted.writeFloat(this.fSightCurForwardAngle);
		netmsgguaranted.writeFloat(this.fSightCurSideslip);
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		this.fSightCurAltitude = netmsginput.readFloat();
		this.fSightCurSpeed = netmsginput.readFloat();
		this.fSightCurForwardAngle = netmsginput.readFloat();
		this.fSightCurSideslip = netmsginput.readFloat();
	}

	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurForwardAngle;
	public float fSightSetForwardAngle;
	public float fSightCurSideslip;

	static {
		Class class1 = PE_8.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Pe-8");
		Property.set(class1, "meshName", "3DO/Plane/Pe-8/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Pe-8.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitPE8.class, CockpitPE8_Bombardier.class, CockpitPE8_FGunner.class, CockpitPE8_TGunner.class, CockpitPE8_AGunner.class, CockpitPE8_RGunner.class, CockpitPE8_LGunner.class });
		Property.set(class1, "LOSElevation", 0.73425F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 12, 13, 14, 3, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 3, 9, 3,
				3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 9, 3, 3 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_ExternalDev01", "_ExternalBomb03", "_ExternalBomb04",
						"_ExternalBomb05", "_ExternalBomb06", "_ExternalDev02", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalDev03", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14",
						"_ExternalDev04", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalDev05", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_ExternalDev06", "_BombSpawn08", "_BombSpawn09",
						"_BombSpawn10", "_BombSpawn11", "_ExternalDev07", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_ExternalDev08", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_ExternalDev09", "_BombSpawn20",
						"_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_ExternalDev10", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", "_BombSpawn27", "_ExternalDev11", "_ExternalBomb19", "_ExternalBomb20", "_ExternalDev12", "_ExternalBomb21",
						"_ExternalBomb22", "_ExternalDev13", "_BombSpawn28", "_BombSpawn29", "_ExternalDev14", "_BombSpawn30", "_BombSpawn31", "_ExternalDev15", "_BombSpawn32", "_BombSpawn33", "_ExternalDev16", "_BombSpawn34", "_BombSpawn35" });
	}
}
