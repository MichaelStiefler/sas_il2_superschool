package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Ansaldo_SVA10E extends Ansaldo {

    public Ansaldo_SVA10E() {
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

    public void typeBomberUpdate(float f1) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    static {
        Class class1 = Ansaldo_SVA10E.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SVA10E");
        Property.set(class1, "meshName", "3DO/Plane/Ansaldo_SVA10E/hierSVA10E.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1914F);
        Property.set(class1, "yearExpired", 1918F);
        Property.set(class1, "FlightModel", "FlightModels/Ansaldo_SVA10.fmd:Ansaldo_SVA10_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAnsaldo_SVA10E.class, CockpitAnsaldo_SVA10E_TGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12" });
    }
}
