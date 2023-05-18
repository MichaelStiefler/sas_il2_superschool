package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CA_133T extends CaproniXYZ {
    static {
        Class class1 = CA_133T.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Caproni");
        Property.set(class1, "meshName", "3do/plane/Caproni-133T(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-52_3mg4e.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCa133.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_BombSpawn01", "_BombSpawn02", "_BombSpawn02", "_BombSpawn03", "_BombSpawn03", "_BombSpawn04", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_BombSpawn07", "_BombSpawn07" });
    }
}
