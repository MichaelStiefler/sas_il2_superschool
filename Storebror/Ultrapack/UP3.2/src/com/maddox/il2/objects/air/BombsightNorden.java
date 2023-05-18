package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;

public class BombsightNorden {

	public BombsightNorden() {
		ResetAll(0, null);
	}

	private static final float toMeters(float f) {
		switch (TypeCurrent) {
			case 0:
				return f;

			case 1:
			case 2:
				return 0.3048F * f;
		}
		return 0.0F;
	}

	private static final float toMetersPerSecond(float f) {
		switch (TypeCurrent) {
			case 0:
				return f / 3.7F;

			case 1:
				return 0.4470401F * f;

			case 2:
				return 0.514F * f;
		}
		return 0.0F;
	}

	private static final float fromMeters(float f) {
		switch (TypeCurrent) {
			case 0:
				return f;

			case 1:
			case 2:
				return 3.2808F * f;
		}
		return 0.0F;
	}

	private static final float fromMetersPerSecond(float f) {
		switch (TypeCurrent) {
			case 0:
				return f * 3.6F;

			case 1:
				return f * 2.237F;

			case 2:
				return f * 1.946F;
		}
		return 0.0F;
	}

	private static void SetCurrentBombIndex() {
		nCurrentBombIndex = 0;
		if (null == ActiveBombNames) { return; }
		if (nCurrentBombStringIndex >= ActiveBombNames.length) { nCurrentBombStringIndex = 0; }
		for (int i = 0; i < BombDescs.length; i++) {
			if (!BombDescs[i].sBombName.equals(ActiveBombNames[nCurrentBombStringIndex])) { continue; }
			nCurrentBombIndex = i;
			break;
		}

	}

	public static void ResetAll(int i, Aircraft aircraft) {
		TypeCurrent = i;
		switch (TypeCurrent) {
			case 0:
				fSightCurAltitude = 3000F;
				fSightCurSpeed = 400F;
				break;

			case 1:
			case 2:
				fSightCurAltitude = 9000F;
				fSightCurSpeed = 250F;
				break;

			default:
				fSightCurAltitude = 0.0F;
				fSightCurSpeed = 0.0F;
				break;
		}
		fSightCurForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
		fSightCurDistance = 0.0F;
		fSightCurReadyness = 0.0F;
		bSightAutomation = false;
		bSightBombDump = false;
		currentCraft = aircraft;
		nNumBombsToRelease = 0;
		nNumBombsReleased = 0;
		fBombDropDelay = 0.25F;
		fDelayLeft = 0.0F;
		nCurrentBombIndex = 0;
		nCurrentBombStringIndex = 0;
		SetCurrentBombIndex();
		RecalculateDistance();
	}

	public static void SetActiveBombNames(String as[]) {
		ActiveBombNames = as;
	}

	public static boolean ToggleAutomation() {
		bSightAutomation = !bSightAutomation;
		bSightBombDump = false;
		nNumBombsReleased = 0;
		fDelayLeft = 0.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (bSightAutomation ? "ON" : "OFF"));
		return bSightAutomation;
	}

	public static void AdjDistanceReset() {
		fSightCurDistance = 0.0F;
		fSightCurForwardAngle = 0.0F;
	}

	public static void AdjDistancePlus() {
		fSightCurForwardAngle++;
		if (fSightCurForwardAngle > 85F) { fSightCurForwardAngle = 85F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		RecalculateDistance();
		if (bSightAutomation) { ToggleAutomation(); }
	}

	public static void AdjDistanceMinus() {
		fSightCurForwardAngle--;
		if (fSightCurForwardAngle < 0.0F) { fSightCurForwardAngle = 0.0F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) fSightCurForwardAngle) });
		RecalculateDistance();
		if (bSightAutomation) { ToggleAutomation(); }
	}

	public static void AdjSideslipReset() {
		fSightCurSideslip = 0.0F;
	}

	public static void AdjSideslipPlus() {
		nNumBombsToRelease++;
		if (nNumBombsToRelease > 10) { nNumBombsToRelease = 0; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "Number of bomb drops: " + nNumBombsToRelease);
	}

	public static void AdjSideslipMinus() {
		fBombDropDelay += 0.25F;
		if (fBombDropDelay > 5F) { fBombDropDelay = 0.25F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "Bomb drop delay (sec): " + fBombDropDelay);
	}

	public static void AdjAltitudeReset() {
		switch (TypeCurrent) {
			case 0:
				fSightCurAltitude = 3000F;
				break;

			case 1:
			case 2:
				fSightCurAltitude = 9000F;
				break;

			default:
				fSightCurAltitude = 0.0F;
				break;
		}
		RecalculateDistance();
	}

	public static void AdjAltitudePlus() {
		switch (TypeCurrent) {
			default:
				break;

			case 0:
				fSightCurAltitude += 50F;
				if (fSightCurAltitude > 10000F) { fSightCurAltitude = 10000F; }
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
				break;

			case 1:
			case 2:
				fSightCurAltitude += 50F;
				if (fSightCurAltitude > 30000F) { fSightCurAltitude = 30000F; }
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) fSightCurAltitude) });
				break;
		}
		RecalculateDistance();
	}

	public static void AdjAltitudeMinus() {
		switch (TypeCurrent) {
			default:
				break;

			case 0:
				fSightCurAltitude -= 50F;
				if (fSightCurAltitude < 500F) { fSightCurAltitude = 500F; }
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
				break;

			case 1:
			case 2:
				fSightCurAltitude -= 50F;
				if (fSightCurAltitude < 1500F) { fSightCurAltitude = 1500F; }
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) fSightCurAltitude) });
				break;
		}
		RecalculateDistance();
	}

	public static void AdjSpeedReset() {
		switch (TypeCurrent) {
			case 0:
				fSightCurSpeed = 400F;
				break;

			case 1:
			case 2:
				fSightCurSpeed = 250F;
				break;

			default:
				fSightCurSpeed = 0.0F;
				break;
		}
	}

	public static void AdjSpeedPlus() {
		switch (TypeCurrent) {
			default:
				break;

			case 0:
				fSightCurSpeed += 5F;
				if (fSightCurSpeed > 600F) { fSightCurSpeed = 600F; }
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
				break;

			case 1:
			case 2:
				fSightCurSpeed += 5F;
				if (fSightCurSpeed > 450F) { fSightCurSpeed = 450F; }
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) fSightCurSpeed) });
				break;
		}
	}

	public static void AdjSpeedMinus() {
		switch (TypeCurrent) {
			default:
				break;

			case 0:
				fSightCurSpeed -= 5F;
				if (fSightCurSpeed < 150F) { fSightCurSpeed = 150F; }
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
				break;

			case 1:
			case 2:
				fSightCurSpeed -= 5F;
				if (fSightCurSpeed < 100F) { fSightCurSpeed = 100F; }
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) fSightCurSpeed) });
				break;
		}
	}

	public static void RecalculateDistance() {
		fSightCurDistance = toMeters(fSightCurAltitude) * (float) Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public static void OnCCIP(float f, float f1) {
		fSightCurSpeed = fromMetersPerSecond(f);
		fSightCurAltitude = fromMeters(f1);
		RecalculateDistance();
	}

	public static void Update(float f) {
		if (null == currentCraft) { return; }
		if (Math.abs(currentCraft.FM.Or.getKren()) > 4.5D) {
			fSightCurReadyness -= 0.0666666F * f;
			if (fSightCurReadyness < 0.0F) { fSightCurReadyness = 0.0F; }
		}
		if (fSightCurReadyness < 1.0F) {
			fSightCurReadyness += 0.0333333F * f;
		} else if (bSightAutomation) {
			double d = toMetersPerSecond(fSightCurSpeed);
			double d1 = toMeters(fSightCurAltitude);
			fSightCurDistance = (float) (fSightCurDistance - d * f);
			if (fSightCurDistance < 0.0F) {
				fSightCurDistance = 0.0F;
				ToggleAutomation();
				return;
			}
			fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(fSightCurDistance / d1));
			double d2 = d * Math.sqrt(d1 * 0.20387359799999999D);
			double d3 = BombDescs[nCurrentBombIndex].GetCorrectionCoeff(d1);
			d2 += d3 * d1 * d;
			if (fSightCurDistance < d2) { bSightBombDump = true; }
			if (bSightBombDump) {
				if (currentCraft.FM.CT.Weapons[3] != null && currentCraft.FM.CT.Weapons[3][currentCraft.FM.CT.Weapons[3].length - 1] != null && currentCraft.FM.CT.Weapons[3][currentCraft.FM.CT.Weapons[3].length - 1].haveBullets()) {
					if (fDelayLeft <= 0.0F) {
						fDelayLeft = fBombDropDelay;
						if (nNumBombsReleased < nNumBombsToRelease || 0 == nNumBombsToRelease) {
							currentCraft.FM.CT.WeaponControl[3] = true;
							HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
							nNumBombsReleased++;
						} else {
							currentCraft.FM.CT.WeaponControl[3] = false;
							ToggleAutomation();
						}
					} else {
						fDelayLeft -= f;
						currentCraft.FM.CT.WeaponControl[3] = false;
					}
				} else {
					currentCraft.FM.CT.WeaponControl[3] = false;
					ToggleAutomation();
				}
			}
		}
	}

	public static void ReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(fSightCurDistance);
		netmsgguaranted.writeByte((int) fSightCurForwardAngle);
		netmsgguaranted.writeByte((int) ((fSightCurSideslip + 3F) * 33.33333F));
		netmsgguaranted.writeFloat(fSightCurAltitude);
		netmsgguaranted.writeByte((int) (fSightCurSpeed / 2.5F));
		netmsgguaranted.writeByte((int) (fSightCurReadyness * 200F));
	}

	public static void ReplicateFromNet(NetMsgInput netmsginput) throws IOException {
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

	public static int             TYPE_METRICK      = 0;
	public static int             TYPE_ENGLISH      = 1;
	public static int             TYPE_NAVAL        = 2;
	public static int             TypeCurrent       = 0;
	public static float           fSightCurAltitude;
	public static float           fSightCurSpeed;
	public static float           fSightCurForwardAngle;
	public static float           fSightCurSideslip;
	public static float           fSightCurReadyness;
	private static int            nNumBombsToRelease;
	private static int            nNumBombsReleased;
	private static float          fBombDropDelay;
	private static float          fDelayLeft;
	private static boolean        bSightAutomation;
	private static boolean        bSightBombDump;
	private static float          fSightCurDistance;
	private static Aircraft       currentCraft;
	private static BombDescriptor BombDescs[]       = {
			new BombDescriptor("FAB-100",
					new double[] { 0.0002072D, 0.0001216D, 0.0001417D, 0.0001036D, 0.0001295D, 8.967E-005D, 0.0001107D, 0.0001108D, 9.13E-005D, 9.449E-005D, 9.347E-005D, 0.0001042D, 9.184E-005D, 0.0001318D, 0.0001329D, 0.0001268D, 0.0001261D }),
			new BombDescriptor("FAB-250",
					new double[] { 0.0002072D, 0.0002216D, 0.0002084D, 0.0002036D, 0.0002095D, 0.0001897D, 0.0001964D, 0.0001858D, 0.0001802D, 0.0001945D, 0.0002026D, 0.0002042D, 0.0002149D, 0.0002033D, 0.0002262D, 0.0002518D, 0.0002672D }),
			new BombDescriptor("FAB-500",
					new double[] { 0.0002072D, 0.0003216D, 0.0004084D, 0.0004536D, 0.0004895D, 0.000523D, 0.0005107D, 0.0005108D, 0.0005135D, 0.0004945D, 0.0005117D, 0.0005375D, 0.0005534D, 0.0005747D, 0.0005996D, 0.0006393D, 0.0006555D }),
			new BombDescriptor("FAB-1000",
					new double[] { 0.0002072D, 0.0001216D, 0.0001417D, 0.0001536D, 0.0001695D, 0.0001897D, 0.0001964D, 0.0001858D, 0.0002024D, 0.0002145D, 0.0002389D, 0.0002542D, 0.0002765D, 0.000289D, 0.0003062D, 0.0003143D, 0.0003261D }),
			new BombDescriptor("FAB-2000",
					new double[] { 7.229E-006D, 2.155E-005D, 7.503E-005D, 0.0001036D, 8.952E-005D, 0.000123D, 0.0001678D, 0.0001858D, 0.0002024D, 0.0002145D, 0.0002389D, 0.0002708D, 0.0003072D, 0.0003318D, 0.0003862D, 0.0004268D, 0.0004672D }),
			new BombDescriptor("FAB-5000",
					new double[] { 7.229E-006D, 2.155E-005D, 8.365E-006D, 3.615E-006D, 4.952E-005D, 5.634E-005D, 8.214E-005D, 0.0001108D, 0.0001135D, 0.0001345D, 0.0001662D, 0.0001875D, 0.0002149D, 0.0002318D, 0.0003062D, 0.0003893D, 0.0004672D }) };
	private static int            nCurrentBombIndex;
	private static int            nCurrentBombStringIndex;
	private static String         ActiveBombNames[] = null;

}
