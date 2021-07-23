package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class JU_288_A2 extends JU_288 {

    static {
        Class class1 = JU_288_A2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-288");
        Property.set(class1, "meshName", "3DO/Plane/Ju-288-A2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/JU_288_A2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_288_A2.class, CockpitJU_288_Bombardier.class, CockpitJU_288_TGunner.class, CockpitJU_288_VGunner.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        int weaponTriggers[] = new int[254];
        weaponTriggers[0] = weaponTriggers[1] = 0;
        weaponTriggers[2] = weaponTriggers[3] = 10;
        weaponTriggers[4] = weaponTriggers[5] = 11;
        for (int i = 6; i < 10; i++) {
            weaponTriggers[i] = 9;
        }
        for (int i = 10; i < 254; i++) {
            weaponTriggers[i] = 3;
        }
        Aircraft.weaponTriggersRegister(class1, weaponTriggers);

        String weaponHooks[] = new String[254];
        for (int i = 0; i < 6; i++) {
            weaponHooks[i] = "_MGUN0" + (i + 1);
        }
        weaponHooks[6] = "_ExternalDev01";
        weaponHooks[7] = "_ExternalDev03";
        weaponHooks[8] = "_ExternalDev02";
        weaponHooks[9] = "_ExternalDev04";
        for (int i = 10; i < 254; i++) {
            int spawn = (i / 2) - 4;
            weaponHooks[i] = "_BombSpawn" + (spawn < 10 ? "0" : "") + spawn;
        }
        Aircraft.weaponHooksRegister(class1, weaponHooks);
    }

}
