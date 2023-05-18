package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class OS2U extends OS2Uxyz implements TypeDiveBomber, TypeScout {

    public OS2U() {
        this.airBrakePos = false;
        this.bGunKilled = false;
        this.tGunKilled = false;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            OS2U.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            OS2U.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        super.doMurderPilot(i);
        if (this.FM.isPlayers()) {
            OS2U.bChangedPit = true;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -40F) {
                    f = -40F;
                    flag = false;
                }
                if (f > 40F) {
                    f = 40F;
                    flag = false;
                }
                if (f1 < -20F) {
                    f1 = -20F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public boolean        airBrakePos;
    boolean               bGunKilled;
    boolean               tGunKilled;
    public static boolean bChangedPit = false;

    static {
        Class class1 = OS2U.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "OS2U");
        Property.set(class1, "meshName", "3DO/Plane/OS2U/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1940.9F);
        Property.set(class1, "yearExpired", 1945.3F);
        Property.set(class1, "FlightModel", "FlightModels/OS2U.fmd:OS2U_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitOS2U.class });
        Property.set(class1, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02" });
    }
}
