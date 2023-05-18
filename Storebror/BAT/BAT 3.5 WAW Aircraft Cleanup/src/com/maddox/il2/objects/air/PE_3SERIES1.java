package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class PE_3SERIES1 extends PE_2 implements TypeFighter {

    public void update(float f) {
        super.update(f);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -1F) {
                    f1 = -1F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
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

    public PE_3SERIES1() {
        this.hierMesh().chunkSetAngles("Turtle_D0", 0.0F, 0.0F, -2F);
    }

    static {
        Class class1 = PE_3SERIES1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Pe-3");
        Property.set(class1, "meshName", "3DO/Plane/Pe-3/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Pe-3series1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPE3_1.class, CockpitPE3_1_TGunner.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 11, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_BombSpawn05", "_BombSpawn06" });
    }
}
