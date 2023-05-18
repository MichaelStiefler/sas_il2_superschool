package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B5N2 extends B5N implements TypeBomber {

    public B5N2() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void doKillPilot(int i) {
        super.doKillPilot(i);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void doMurderPilot(int i) {
        super.doMurderPilot(i);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -31F) {
                    f = -31F;
                    flag = false;
                }
                if (f > 31F) {
                    f = 31F;
                    flag = false;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                    flag = false;
                }
                if (f1 > 52F) {
                    f1 = 52F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
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

    public static boolean bChangedPit = false;

    static {
        Class class1 = B5N2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B5N");
        Property.set(class1, "meshName", "3DO/Plane/B5N2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ja", "3DO/Plane/B5N2(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/B5N2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB5N2.class, Cockpit_BombsightOPB.class, CockpitB5N2_TGunner.class });
        Property.set(class1, "LOSElevation", 0.7394F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 9, 3, 9, 3, 9, 3, 3, 3, 9, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalDev03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev04",
                "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11" });
    }
}
