package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BR_691 extends BR_690 {
    static {
        Class class1 = BR_691.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Br-691");
        Property.set(class1, "meshName", "3DO/Plane/Breguet-691/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/BR691.fmd");
        Property.set(class1, "LOSElevation", 1.0728F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitBreguet_691.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 10, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn02", "_BombSpawn03", "_BombSpawn03", "_BombSpawn04", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_BombSpawn07", "_BombSpawn07", "_BombSpawn08" });
    }
}
