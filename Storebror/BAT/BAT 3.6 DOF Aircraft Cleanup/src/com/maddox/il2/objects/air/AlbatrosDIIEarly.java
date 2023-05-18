package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class AlbatrosDIIEarly extends AlbatrosWD {

    public AlbatrosDIIEarly() {
    }

    static {
        Class class1 = AlbatrosDIIEarly.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AlbatrosDII");
        Property.set(class1, "meshName", "3DO/Plane/AlbatrosDIIEarly/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParWW1());
        Property.set(class1, "yearService", 1916F);
        Property.set(class1, "yearExpired", 1930F);
        Property.set(class1, "FlightModel", "FlightModels/AlbatrosD2.fmd:AlbatrosD2");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAlbatrosD2Early.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
