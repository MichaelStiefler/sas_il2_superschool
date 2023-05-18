package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Do_24T extends Do_24X implements TypeSeaPlane, TypeTransport, TypeBomber {

    public Do_24T() {
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f > 95F) {
                    f = 95F;
                    flag = false;
                }
                if (f < -95F) {
                    f = -95F;
                    flag = false;
                }
                break;

            case 1:
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f > 95F) {
                    f = 95F;
                    flag = false;
                }
                if (f < -95F) {
                    f = -95F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -52F) {
                    f = -52F;
                    flag = false;
                }
                if (f > 52F) {
                    f = 52F;
                    flag = false;
                }
                if (f1 < -60F) {
                    f1 = -60F;
                    flag = false;
                }
                if (f1 > 90F) {
                    f1 = 90F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2:
                this.FM.turret[0].setHealth(f);
                break;

            case 3:
                this.FM.turret[1].setHealth(f);
                this.FM.turret[2].setHealth(f);
                break;

            case 4:
                this.FM.turret[3].setHealth(f);
                break;
        }
    }

    public void doKillPilot(int i) {
        if (i > 5) {
            return;
        } else {
            this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
            this.hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
            this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
            this.hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
            return;
        }
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

    public boolean bPitUnfocused;
    public float   fSightCurForwardAngle;
    public float   fSightSetForwardAngle;
    public int     fSightCurSideslip;

    static {
        Class class1 = Do_24T.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Do_24T");
        Property.set(class1, "meshName", "3DO/Plane/Do_24T/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00s());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/Do24T1.fmd:Do24T1_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDo_24T.class, CockpitDo_24T_NGunner.class, CockpitDo_24T_TGunner.class, CockpitDo_24T_RGunner.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12" });
    }
}
