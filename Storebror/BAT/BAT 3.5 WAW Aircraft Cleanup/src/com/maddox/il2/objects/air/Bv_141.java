package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Bv_141 extends Bv_237X implements TypeScout, TypeFighter {

    public Bv_141() {
        Bv_141.prevWing = true;
        this.bToFire = false;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f1 < 40F) {
                    f1 = 40F;
                    flag = false;
                }
                if (f1 > 96F) {
                    f1 = 96F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -75F) {
                    f = -75F;
                    flag = false;
                }
                if (f > 85F) {
                    f = 85F;
                    flag = false;
                }
                if (f1 < 4F) {
                    f1 = 4F;
                    flag = false;
                }
                if (f1 > 80F) {
                    f1 = 80F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public boolean        bToFire;
    public static boolean bChangedPit = false;
    public static boolean prevWing    = false;
    static {
        Class class1 = Bv_141.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bv-141");
        Property.set(class1, "meshName", "3DO/Plane/Bv_141/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1955.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bv141.fmd:Bv141_FM");
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_Bv141.class });
        Property.set(class1, "LOSElevation", 0.7394F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
