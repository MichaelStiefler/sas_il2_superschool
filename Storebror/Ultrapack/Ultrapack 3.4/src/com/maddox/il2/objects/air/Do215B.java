package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Do215B extends Do215 {
    static {
        Class class1 = Do215B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Do-215B");
        Property.set(class1, "meshName", "3do/plane/Do-215/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "meshName_hu", "3do/plane/Do-215(hu)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Do17Z0.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDo17.class, CockpitDo17_Bombardier.class, CockpitDo17_NGunner.class, CockpitDo17_TGunner.class, CockpitDo17_BGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn05" });
    }
}
