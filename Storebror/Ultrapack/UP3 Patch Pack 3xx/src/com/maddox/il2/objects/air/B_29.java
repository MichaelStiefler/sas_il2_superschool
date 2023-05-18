package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B_29 extends B_29X implements TypeBomber, TypeX4Carrier, TypeGuidedBombCarrier {

	public B_29() {
		calibDistance = 0.0F;
		bToFire = false;
		deltaAzimuth = 0.0F;
		deltaTangage = 0.0F;
		isGuidingBomb = false;
		bSightAutomation = false;
		bSightBombDump = false;
		fSightCurDistance = 0.0F;
		fSightCurForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
		fSightCurAltitude = 3000F;
		fSightCurSpeed = 200F;
		fSightCurReadyness = 0.0F;
	}

	public boolean typeGuidedBombCisMasterAlive() {
		return isMasterAlive;
	}

	public void typeGuidedBombCsetMasterAlive(boolean flag) {
		isMasterAlive = flag;
	}

	public boolean typeGuidedBombCgetIsGuiding() {
		return isGuidingBomb;
	}

	public void typeGuidedBombCsetIsGuiding(boolean flag) {
		isGuidingBomb = flag;
	}

	public void typeX4CAdjSidePlus() {
		deltaAzimuth = 0.002F;
	}

	public void typeX4CAdjSideMinus() {
		deltaAzimuth = -0.002F;
	}

	public void typeX4CAdjAttitudePlus() {
		deltaTangage = 0.002F;
	}

	public void typeX4CAdjAttitudeMinus() {
		deltaTangage = -0.002F;
	}

	public void typeX4CResetControls() {
		deltaAzimuth = deltaTangage = 0.0F;
	}

	public float typeX4CgetdeltaAzimuth() {
		return deltaAzimuth;
	}

	public float typeX4CgetdeltaTangage() {
		return deltaTangage;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 19: // '\023'
			killPilot(this, 4);
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		FM.AS.wantBeaconsNet(true);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		for (int i = 1; i < 7; i++)
			if (FM.getAltitude() < 3000F)
				hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -23F) {
				f = -23F;
				flag = false;
			}
			if (f > 23F) {
				f = 23F;
				flag = false;
			}
			if (f1 < -25F) {
				f1 = -25F;
				flag = false;
			}
			if (f1 > 15F) {
				f1 = 15F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f1 < 0.0F) {
				f1 = 0.0F;
				flag = false;
			}
			if (f1 > 73F) {
				f1 = 73F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f < -38F) {
				f = -38F;
				flag = false;
			}
			if (f > 38F) {
				f = 38F;
				flag = false;
			}
			if (f1 < -41F) {
				f1 = -41F;
				flag = false;
			}
			if (f1 > 43F) {
				f1 = 43F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f < -85F) {
				f = -85F;
				flag = false;
			}
			if (f > 22F) {
				f = 22F;
				flag = false;
			}
			if (f1 < -40F) {
				f1 = -40F;
				flag = false;
			}
			if (f1 > 32F) {
				f1 = 32F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f < -34F) {
				f = -34F;
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
			if (f1 > 32F) {
				f1 = 32F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
		case 2: // '\002'
			FM.turret[0].setHealth(f);
			break;

		case 3: // '\003'
			FM.turret[1].setHealth(f);
			break;

		case 4: // '\004'
			FM.turret[2].setHealth(f);
			break;

		case 5: // '\005'
			FM.turret[3].setHealth(f);
			FM.turret[4].setHealth(f);
			break;
		}
	}

	private static final float toMeters(float f) {
		return 0.3048F * f;
	}

	private static final float toMetersPerSecond(float f) {
		return 0.4470401F * f;
	}

	public boolean typeBomberToggleAutomation() {
		bSightAutomation = !bSightAutomation;
		bSightBombDump = false;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (bSightAutomation ? "ON" : "OFF"));
		return bSightAutomation;
	}

	public void typeBomberAdjDistanceReset() {
		fSightCurDistance = 0.0F;
		fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		fSightCurForwardAngle++;
		if (fSightCurForwardAngle > 85F)
			fSightCurForwardAngle = 85F;
		fSightCurDistance = B_29.toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation",
				new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus() {
		fSightCurForwardAngle--;
		if (fSightCurForwardAngle < 0.0F)
			fSightCurForwardAngle = 0.0F;
		fSightCurDistance = B_29.toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation",
				new Object[] { new Integer((int) fSightCurForwardAngle) });
		if (bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjSideslipReset() {
		fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		fSightCurSideslip += 0.05F;
		if (fSightCurSideslip > 3F)
			fSightCurSideslip = 3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjSideslipMinus() {
		fSightCurSideslip -= 0.05F;
		if (fSightCurSideslip < -3F)
			fSightCurSideslip = -3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjAltitudeReset() {
		fSightCurAltitude = 3000F;
	}

	public void typeBomberAdjAltitudePlus() {
		fSightCurAltitude += 50F;
		if (fSightCurAltitude > 50000F)
			fSightCurAltitude = 50000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft",
				new Object[] { new Integer((int) fSightCurAltitude) });
		fSightCurDistance = B_29.toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		fSightCurAltitude -= 50F;
		if (fSightCurAltitude < 1000F)
			fSightCurAltitude = 1000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft",
				new Object[] { new Integer((int) fSightCurAltitude) });
		fSightCurDistance = B_29.toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset() {
		fSightCurSpeed = 200F;
	}

	public void typeBomberAdjSpeedPlus() {
		fSightCurSpeed += 10F;
		if (fSightCurSpeed > 450F)
			fSightCurSpeed = 450F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH",
				new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		fSightCurSpeed -= 10F;
		if (fSightCurSpeed < 100F)
			fSightCurSpeed = 100F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH",
				new Object[] { new Integer((int) fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		if (Math.abs(FM.Or.getKren()) > 4.5D) {
			fSightCurReadyness -= 0.0666666F * f;
			if (fSightCurReadyness < 0.0F)
				fSightCurReadyness = 0.0F;
		}
		if (fSightCurReadyness < 1.0F)
			fSightCurReadyness += 0.0333333F * f;
		else if (bSightAutomation) {
			fSightCurDistance -= B_29.toMetersPerSecond(fSightCurSpeed) * f;
			if (fSightCurDistance < 0.0F) {
				fSightCurDistance = 0.0F;
				typeBomberToggleAutomation();
			}
			fSightCurForwardAngle = (float) Math
					.toDegrees(Math.atan(fSightCurDistance / B_29.toMeters(fSightCurAltitude)));
			calibDistance = B_29.toMetersPerSecond(fSightCurSpeed) * floatindex(
					Aircraft.cvt(B_29.toMeters(fSightCurAltitude), 0.0F, 7000F, 0.0F, 7F), calibrationScale);
			if (fSightCurDistance < calibDistance
					+ B_29.toMetersPerSecond(fSightCurSpeed) * Math.sqrt(B_29.toMeters(fSightCurAltitude) * 0.2038736F))
				bSightBombDump = true;
			if (bSightBombDump)
				if (FM.isTick(3, 0)) {
					if (FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null
							&& FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets()) {
						FM.CT.WeaponControl[3] = true;
						HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
					}
				} else {
					FM.CT.WeaponControl[3] = false;
				}
		}
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws java.io.IOException {
		netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(fSightCurDistance);
		netmsgguaranted.writeByte((int) fSightCurForwardAngle);
		netmsgguaranted.writeByte((int) ((fSightCurSideslip + 3F) * 33.33333F));
		netmsgguaranted.writeFloat(fSightCurAltitude);
		netmsgguaranted.writeByte((int) (fSightCurSpeed / 2.5F));
		netmsgguaranted.writeByte((int) (fSightCurReadyness * 200F));
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws java.io.IOException {
		int i = netmsginput.readUnsignedByte();
		bSightAutomation = (i & 1) != 0;
		bSightBombDump = (i & 2) != 0;
		fSightCurDistance = netmsginput.readFloat();
		fSightCurForwardAngle = netmsginput.readUnsignedByte();
		fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
		fSightCurAltitude = netmsginput.readFloat();
		fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
		fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
	}

	public boolean bToFire;
	private float deltaAzimuth;
	private float deltaTangage;
	private boolean isGuidingBomb;
	private boolean isMasterAlive;
	public static boolean bChangedPit = false;
	private float calibDistance;
	private boolean bSightAutomation;
	private boolean bSightBombDump;
	private float fSightCurDistance;
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;
	static final float calibrationScale[] = { 0.0F, 0.2F, 0.4F, 0.66F, 0.86F, 1.05F, 1.2F, 1.6F };

	static {
		Class class1 = B_29.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-29");
		Property.set(class1, "meshName", "3DO/Plane/B-29(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "meshName_us", "3DO/Plane/B-29(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1943.5F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-29.fmd");
		Property.set(class1, "cockpitClass",
				new Class[] { CockpitB29.class, CockpitB29_Bombardier.class, CockpitB29_TGunner.class,
						CockpitB29_T2Gunner.class, CockpitB29_FGunner.class, CockpitB29_RGunner.class,
						CockpitB29_AGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 10, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 3, 3, 3, 3 });
		weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08",
						"_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03",
						"_BombSpawn04" });
	}
}
