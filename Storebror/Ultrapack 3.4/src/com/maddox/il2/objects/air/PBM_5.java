package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class PBM_5 extends PBM {
    static {
        Class class1 = PBM_5.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mariner");
        Property.set(class1, "meshName", "3DO/Plane/MARINER/hier_PBM5.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1965F);
        Property.set(class1, "FlightModel", "FlightModels/PBM5.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMariner.class, CockpitMariner_Bombardier.class, Cockpit_Mariner_Radar.class, CockpitMariner_TGunner.class, CockpitMariner_AGunner.class });
        Property.set(class1, "LOSElevation", 1.4078F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12" });
    }
}
