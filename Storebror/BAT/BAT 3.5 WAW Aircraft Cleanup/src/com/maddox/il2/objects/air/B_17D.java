package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class B_17D extends B_17 implements TypeBomber {

    public B_17D() {
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -85F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -85F * f, 0.0F);
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2:
                this.FM.turret[0].setHealth(f);
                this.FM.turret[1].setHealth(f);
                this.FM.turret[2].setHealth(f);
                break;

            case 6:
                this.FM.turret[3].setHealth(f);
                break;

            case 7:
                this.FM.turret[4].setHealth(f);
                break;

            case 8:
                this.FM.turret[5].setHealth(f);
                break;
        }
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

            case 0:
                if (f < -11F) {
                    f = -11F;
                    flag = false;
                }
                if (f > 11F) {
                    f = 11F;
                    flag = false;
                }
                if (f1 < -14F) {
                    f1 = -14F;
                    flag = false;
                }
                if (f1 > 14F) {
                    f1 = 14F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -26F) {
                    f = -26F;
                    flag = false;
                }
                if (f > 0.0F) {
                    f = 0.0F;
                    flag = false;
                }
                if (f1 < -14F) {
                    f1 = -14F;
                    flag = false;
                }
                if (f1 > 14F) {
                    f1 = 14F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -11F) {
                    f = -11F;
                    flag = false;
                }
                if (f > 11F) {
                    f = 11F;
                    flag = false;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                break;

            case 3:
                if (f < -12F) {
                    f = -12F;
                    flag = false;
                }
                if (f > 12F) {
                    f = 12F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 2.0F) {
                    f1 = 2.0F;
                    flag = false;
                }
                break;

            case 4:
                if (f < -41F) {
                    f = -41F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;

            case 5:
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 53F) {
                    f = 53F;
                    flag = false;
                }
                if (f1 < -10F) {
                    f1 = -10F;
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

    static {
        Class class1 = B_17D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-17");
        Property.set(class1, "meshName", "3DO/Plane/B-17D(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "meshName_us", "3DO/Plane/B-17D(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-17D.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB17.class, CockpitB17D_Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 14, 15, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_BombSpawn01", "_BombSpawn02" });
    }
}
