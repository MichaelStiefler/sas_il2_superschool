package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class Potez_633 extends Potez_630 implements TypeBomber {

    public Potez_633() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.75F;
    }

    public boolean hasCourseWeaponBullets() {
        return (this.FM.CT.Weapons[0] != null) && (this.FM.CT.Weapons[0][0] != null) && (this.FM.CT.Weapons[0][0].countBullets() != 0);
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberUpdate(float f) {
    }

    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurReadyness;

    static {
        Class class1 = Potez_633.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P633");
        Property.set(class1, "meshName", "3do/plane/Potez-633(Multi1)/HEI_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_fr", "3do/plane/Potez-633(fr)/HEI_hier.him");
        Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
        Property.set(class1, "meshName_vi", "3do/plane/Potez-633(vi)/HEI_hier.him");
        Property.set(class1, "PaintScheme_vi", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Potez_630.fmd:Potez63x_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPotez_630.class, CockpitPotez_630_Gunner.class });
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_BombSpawn02", "_BombSpawn03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_MGUN01" });
    }
}
