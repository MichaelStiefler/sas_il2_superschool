package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class B_17E extends B_17 implements TypeBomber {

    public B_17E() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 1:
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 20F) {
                    f = 20F;
                    flag = false;
                }
                if (f1 < -35F) {
                    f1 = -35F;
                    flag = false;
                }
                if (f1 > 20F) {
                    f1 = 20F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -50F) {
                    f = -50F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -35F) {
                    f1 = -35F;
                    flag = false;
                }
                if (f1 > 20F) {
                    f1 = 20F;
                    flag = false;
                }
                break;

            case 3:
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 66F) {
                    f1 = 66F;
                    flag = false;
                }
                break;

            case 4:
                if (f1 < -85F) {
                    f1 = -85F;
                    flag = false;
                }
                if (f1 > 6F) {
                    f1 = 6F;
                    flag = false;
                }
                break;

            case 5:
                if (f < -70F) {
                    f = -60F;
                    flag = false;
                }
                if (f > 50F) {
                    f = 50F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 41F) {
                    f1 = 41F;
                    flag = false;
                }
                break;

            case 6:
                if (f < -70F) {
                    f = -70F;
                    flag = false;
                }
                if (f > 25F) {
                    f = 25F;
                    flag = false;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;

            case 7:
                if (f < -25F) {
                    f = -25F;
                    flag = false;
                }
                if (f > 25F) {
                    f = 25F;
                    flag = false;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > 25F) {
                    f1 = 25F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    static {
        Class class1 = B_17E.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-17");
        Property.set(class1, "meshName", "3DO/Plane/B-17E(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/B-17E(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar03());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-17E.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB17.class, CockpitB17E_Bombardier.class, CockpitB17_TGunner.class, CockpitB17_LGunner.class, CockpitB17_RGunner.class, CockpitB17_AGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 11, 12, 13, 13, 14, 14, 15, 16, 17, 17, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN10", "_MGUN09", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02" });
    }
}
