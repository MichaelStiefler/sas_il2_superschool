package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Ki_32 extends Ki_32xyz {
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
        Class class1 = Ki_32.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki32");
        Property.set(class1, "meshName", "3DO/Plane/Ki32(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-32.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKi_32.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05" });
    }
}
