package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class PE_3BIS extends PE_2 implements TypeFighter {

    public PE_3BIS() {
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -110F) {
                    f = -110F;
                    flag = false;
                }
                if (f > 88F) {
                    f = 88F;
                    flag = false;
                }
                if (f1 < -1F) {
                    f1 = -1F;
                    flag = false;
                }
                if (f1 > 55F) {
                    f1 = 55F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -2F) {
                    f = -2F;
                    flag = false;
                }
                if (f > 2.0F) {
                    f = 2.0F;
                    flag = false;
                }
                if (f1 < -2F) {
                    f1 = -2F;
                    flag = false;
                }
                if (f1 > 2.0F) {
                    f1 = 2.0F;
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
            case 1:
                this.FM.turret[0].setHealth(f);
                this.FM.turret[1].setHealth(f);
                break;
        }
    }

    static {
        Class class1 = PE_3BIS.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Pe-3");
        Property.set(class1, "meshName", "3DO/Plane/Pe-3bis/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1941.5F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Pe-3bis.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPE3bis.class, CockpitPE3bis_TGunner.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 10, 11, 3, 3, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_CANNON03", "_CANNON04", "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_BombSpawn05", "_BombSpawn06" });
    }
}
