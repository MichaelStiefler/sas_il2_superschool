package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class NC223_4 extends NC223_4xyz {
    static {
        Class class1 = NC223_4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "NC223");
        Property.set(class1, "meshName", "3DO/Plane/NC-223-4/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1941F);
        Property.set(class1, "FlightModel", "FlightModels/NC.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitNC223_4.class, CockpitNC223_4_Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08" });
    }
}
