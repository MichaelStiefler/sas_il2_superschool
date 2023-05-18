package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B6N2 extends B6N implements TypeBomber {

    public B6N2() {
    }

    public boolean typeBomberToggleAutomation() {
        if (this.FM.isPlayers()) {
            this.FM.CT.setTrimAileronControl(0.07F);
            this.FM.CT.setTrimElevatorControl(-0.23F);
            this.FM.CT.setTrimRudderControl(0.18F);
        }
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        BombsightOPB.AdjDistanceReset();
    }

    public void typeBomberAdjDistancePlus() {
        BombsightOPB.AdjDistancePlus();
    }

    public void typeBomberAdjDistanceMinus() {
        BombsightOPB.AdjDistanceMinus();
    }

    public void typeBomberAdjSideslipReset() {
        BombsightOPB.AdjSideslipReset();
    }

    public void typeBomberAdjSideslipPlus() {
        BombsightOPB.AdjSideslipPlus();
    }

    public void typeBomberAdjSideslipMinus() {
        BombsightOPB.AdjSideslipMinus();
    }

    public void typeBomberAdjAltitudeReset() {
        BombsightOPB.AdjAltitudeReset();
    }

    public void typeBomberAdjAltitudePlus() {
        BombsightOPB.AdjAltitudePlus();
    }

    public void typeBomberAdjAltitudeMinus() {
        BombsightOPB.AdjAltitudeMinus();
    }

    public void typeBomberAdjSpeedReset() {
        BombsightOPB.AdjSpeedReset();
    }

    public void typeBomberAdjSpeedPlus() {
        BombsightOPB.AdjSpeedPlus();
    }

    public void typeBomberAdjSpeedMinus() {
        BombsightOPB.AdjSpeedMinus();
    }

    public void typeBomberUpdate(float f) {
        BombsightOPB.Update(f);
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    static {
        Class class1 = B6N2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B6N");
        Property.set(class1, "meshName", "3DO/Plane/B6N2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ja", "3DO/Plane/B6N2(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/B6N2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB6N2.class, Cockpit_BombsightOPB.class, CockpitB6N2_TGunner.class });
        Property.set(class1, "LOSElevation", 0.7394F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 9, 3, 9, 3, 9, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalDev06", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev04", "_ExternalDev05",
                "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11" });
    }
}
