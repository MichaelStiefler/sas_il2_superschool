package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B4Y1 extends B4Yxyz {
    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, -35F * f, 0.0F);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            B4Y1.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            B4Y1.bChangedPit = true;
        }
    }

    public void doWoundPilot(int i, float f) {
        super.doWoundPilot(i, f);
        if (this.FM.isPlayers()) {
            B4Y1.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        super.doMurderPilot(i);
        if (this.FM.isPlayers()) {
            B4Y1.bChangedPit = true;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -33F) {
                    f = -33F;
                    flag = false;
                }
                if (f > 33F) {
                    f = 33F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 62F) {
                    f1 = 62F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
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

    public static boolean bChangedPit = false;

    static {
        Class class1 = B4Y1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Jean");
        Property.set(class1, "meshName", "3do/Plane/B4Y1-Jean(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/B4Y1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_B4Y.class, CockpitB4Y1_TGunner.class });
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17" });
    }
}
