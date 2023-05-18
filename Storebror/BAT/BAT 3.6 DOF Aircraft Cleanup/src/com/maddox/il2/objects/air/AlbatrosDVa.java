package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class AlbatrosDVa extends Albatros {

    public AlbatrosDVa() {
    }

    static {
        Class class1 = AlbatrosDVa.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Albatros");
        Property.set(class1, "meshName", "3DO/Plane/AlbatrosDVa(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParWW1());
        Property.set(class1, "yearService", 1916F);
        Property.set(class1, "yearExpired", 1930F);
        Property.set(class1, "FlightModel", "FlightModels/AlbatrosDVa.fmd:AlbatrosDVa");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAlbatrosDV.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
