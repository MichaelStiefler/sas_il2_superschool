package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BV138MS extends BV138abc implements TypeScout, TypeSeaPlane {

    public BV138MS() {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                break;

            case 4:
                this.hierMesh().chunkVisible("Pilot5_D0", false);
                this.hierMesh().chunkVisible("Pilot5_D1", true);
                this.hierMesh().chunkVisible("HMask5_D0", false);
                break;

            case 5:
                this.hierMesh().chunkVisible("Pilot6_D0", false);
                this.hierMesh().chunkVisible("Pilot6_D1", true);
                this.hierMesh().chunkVisible("HMask6_D0", false);
                break;
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 3:
                this.FM.turret[1].bIsOperable = false;
                break;

            case 5:
                this.FM.turret[2].bIsOperable = false;
                break;
        }
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
                if (f > 110F) {
                    f = 110F;
                    flag = false;
                }
                if (f1 < 3F) {
                    f1 = 3F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -90F) {
                    f = -90F;
                    flag = false;
                }
                if (f > 90F) {
                    f = 90F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -90F) {
                    f = -90F;
                    flag = false;
                }
                if (f > 90F) {
                    f = 90F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    static {
        Class class1 = BV138MS.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BV138MS");
        Property.set(class1, "meshName", "3DO/Plane/BV138B/BV138MS.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/BV138M.fmd:BV138s_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBV138B.class, CockpitBV138_FGunner.class, CockpitBV138_TGunner.class, CockpitBV138_RGunner.class });
        Property.set(class1, "LOSElevation", 0.5265F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 9, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_MGUN02", "_CANNON02", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
