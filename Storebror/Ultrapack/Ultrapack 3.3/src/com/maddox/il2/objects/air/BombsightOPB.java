package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;

class BombsightOPB {

    public BombsightOPB() {
        ResetAll();
    }

    private static void SetCurrentBombIndex() {
        nCurrentBombIndex = 0;
        if (null == ActiveBombNames) return;
        if (nCurrentBombStringIndex >= ActiveBombNames.length) nCurrentBombStringIndex = 0;
        for (int i = 0; i < BombDescs.length; i++) {
            if (!BombDescs[i].sBombName.equals(ActiveBombNames[nCurrentBombStringIndex])) continue;
            nCurrentBombIndex = i;
            break;
        }

    }

    public static void ResetAll() {
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 400F;
        fSightCurForwardAngle = 0.0F;
        fSightSetForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        nCurrentBombIndex = 0;
        nCurrentBombStringIndex = 0;
        SetCurrentBombIndex();
        RecalculateAngle();
    }

    public static void SetActiveBombNames(String as[]) {
        ActiveBombNames = as;
    }

    public static boolean ToggleAutomation() {
        nCurrentBombStringIndex++;
        SetCurrentBombIndex();
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Bomb type: " + BombDescs[nCurrentBombIndex].sBombName);
        RecalculateAngle();
        return false;
    }

    public static void AdjDistanceReset() {
        fSightCurForwardAngle = 0.0F;
    }

    public static void AdjDistancePlus() {
        fSightCurForwardAngle += 0.2F;
        if (fSightCurForwardAngle > 75F) fSightCurForwardAngle = 75F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (fSightCurForwardAngle * 1.0F)) });
    }

    public static void AdjDistanceMinus() {
        fSightCurForwardAngle -= 0.2F;
        if (fSightCurForwardAngle < -15F) fSightCurForwardAngle = -15F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (fSightCurForwardAngle * 1.0F)) });
    }

    public static void AdjSideslipReset() {
        fSightCurSideslip = 0.0F;
    }

    public static void AdjSideslipPlus() {
        fSightCurSideslip++;
        if (fSightCurSideslip > 45F) fSightCurSideslip = 45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 1.0F)) });
    }

    public static void AdjSideslipMinus() {
        fSightCurSideslip--;
        if (fSightCurSideslip < -45F) fSightCurSideslip = -45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (fSightCurSideslip * 1.0F)) });
    }

    public static void AdjAltitudeReset() {
        fSightCurAltitude = 3000F;
        RecalculateAngle();
    }

    public static void AdjAltitudePlus() {
        fSightCurAltitude += 50F;
        if (fSightCurAltitude > 10000F) fSightCurAltitude = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
        RecalculateAngle();
    }

    public static void AdjAltitudeMinus() {
        fSightCurAltitude -= 50F;
        if (fSightCurAltitude < 500F) fSightCurAltitude = 500F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) fSightCurAltitude) });
        RecalculateAngle();
    }

    public static void AdjSpeedReset() {
        fSightCurSpeed = 400F;
        RecalculateAngle();
    }

    public static void AdjSpeedPlus() {
        fSightCurSpeed += 5F;
        if (fSightCurSpeed > 600F) fSightCurSpeed = 600F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
        RecalculateAngle();
    }

    public static void AdjSpeedMinus() {
        fSightCurSpeed -= 5F;
        if (fSightCurSpeed < 150F) fSightCurSpeed = 150F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) fSightCurSpeed) });
        RecalculateAngle();
    }

    public static void RecalculateAngle() {
        double d = fSightCurSpeed / 3.6D * Math.sqrt(fSightCurAltitude * 0.203873599D);
        double d1 = BombDescs[nCurrentBombIndex].GetCorrectionCoeff(fSightCurAltitude);
        d += d1 * fSightCurAltitude * fSightCurSpeed / 3.6D;
        fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / fSightCurAltitude));
    }

    public static void OnCCIP(float f, float f1) {
        fSightCurSpeed = f;
        fSightCurAltitude = f1;
        RecalculateAngle();
    }

    public static void Update(float f) {
    }

    public static void ReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeFloat(fSightCurSpeed);
        netmsgguaranted.writeFloat(fSightCurForwardAngle);
        netmsgguaranted.writeFloat(fSightCurSideslip);
    }

    public static void ReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readFloat();
        fSightCurSideslip = netmsginput.readFloat();
        RecalculateAngle();
    }

    public static float           fSightCurAltitude;
    public static float           fSightCurSpeed;
    public static float           fSightCurForwardAngle;
    public static float           fSightSetForwardAngle;
    public static float           fSightCurSideslip;
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
                    new double[] { 7.229E-006D, 2.155E-005D, 7.503E-005D, 0.0001036D, 8.952E-005D, 0.000123D, 0.0001678D, 0.0001858D, 0.0002024D, 0.0002145D, 0.0002389D, 0.0002708D, 0.0003072D, 0.000332D, 0.0003862D, 0.0004268D, 0.0004672D }),
            new BombDescriptor("FAB-5000",
                    new double[] { 7.229E-006D, 2.155E-005D, 8.365E-006D, 3.615E-006D, 4.952E-005D, 5.634E-005D, 8.22E-005D, 0.0001108D, 0.0001135D, 0.0001345D, 0.0001662D, 0.0001875D, 0.0002149D, 0.0002318D, 0.0003062D, 0.0003893D, 0.0004672D }) };
    private static int            nCurrentBombIndex;
    private static int            nCurrentBombStringIndex;
    private static String         ActiveBombNames[] = null;

}
