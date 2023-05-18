package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class NC223_3 extends NC223_3xyz {
    static {
        Class class1 = NC223_3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "NC223");
        Property.set(class1, "meshName", "3DO/Plane/NC-223-3/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1941F);
        Property.set(class1, "FlightModel", "FlightModels/NC.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitNC223_3.class, CockpitNC223_3_Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24" });
    }
}
