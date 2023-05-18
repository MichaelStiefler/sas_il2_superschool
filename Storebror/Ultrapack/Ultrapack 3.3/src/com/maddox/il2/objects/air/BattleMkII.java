package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class BattleMkII extends FaireyBattle implements TypeBomber {

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
        return BombsightNorden.ToggleAutomation();
    }

    public void typeBomberAdjDistanceReset() {
        BombsightNorden.AdjDistanceReset();
    }

    public void typeBomberAdjDistancePlus() {
        BombsightNorden.AdjDistancePlus();
    }

    public void typeBomberAdjDistanceMinus() {
        BombsightNorden.AdjDistanceMinus();
    }

    public void typeBomberAdjSideslipReset() {
        BombsightNorden.AdjSideslipReset();
    }

    public void typeBomberAdjSideslipPlus() {
        BombsightNorden.AdjSideslipPlus();
    }

    public void typeBomberAdjSideslipMinus() {
        BombsightNorden.AdjSideslipMinus();
    }

    public void typeBomberAdjAltitudeReset() {
        BombsightNorden.AdjAltitudeReset();
    }

    public void typeBomberAdjAltitudePlus() {
        BombsightNorden.AdjAltitudePlus();
    }

    public void typeBomberAdjAltitudeMinus() {
        BombsightNorden.AdjAltitudeMinus();
    }

    public void typeBomberAdjSpeedReset() {
        BombsightNorden.AdjSpeedReset();
    }

    public void typeBomberAdjSpeedPlus() {
        BombsightNorden.AdjSpeedPlus();
    }

    public void typeBomberAdjSpeedMinus() {
        BombsightNorden.AdjSpeedMinus();
    }

    public void typeBomberUpdate(float f) {
        BombsightNorden.Update(f);
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            BombsightNorden.SetActiveBombNames(new String[] { "FAB-100" });
            BombsightNorden.ResetAll(1, this);
        }
    }

    static {
        Class class1 = BattleMkII.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FaireyBattle");
        Property.set(class1, "meshName", "3DO/Plane/FaireyBattle(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1941.5F);
        Property.set(class1, "FlightModel", "FlightModels/BattleMkII.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBattleMkII.class, Cockpit_BombsightNordenSimple.class, CockpitBattleMkII_TGunner.class });
        Property.set(class1, "LOSElevation", 0.7394F);
        weaponTriggersRegister(class1, new int[] { 0, 10, 9, 3, 9, 3, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalDev03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06",
                "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11" });
    }
}
