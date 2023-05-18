package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class XC_47X extends Swordfish implements TypeScout, TypeSailPlane, TypeBomber {

    public XC_47X() {
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    static {
        Class class1 = XC_47X.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "XC_47X");
        Property.set(class1, "meshName", "3DO/Plane/C-47X(USA)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1938.5F);
        Property.set(class1, "yearExpired", 1975.5F);
        Property.set(class1, "FlightModel", "FlightModels/C-47EX.fmd:C-47EX_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitC47.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 3, 3, 3, 3, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_BombSpawn01" });
    }
}
