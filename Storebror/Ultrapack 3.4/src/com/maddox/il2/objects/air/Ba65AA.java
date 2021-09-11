package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Ba65AA extends Ba65B {
    static {
        Class class1 = Ba65AA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ba.65");
        Property.set(class1, "meshName", "3DO/Plane/Ba65Fiat/hierAA.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/Ba65AA.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBa65A.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 10, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
