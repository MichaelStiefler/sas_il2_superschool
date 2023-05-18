package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class AlbatrosDIIIEarly extends Albatros {

    public AlbatrosDIIIEarly() {
    }

    static {
        Class class1 = AlbatrosDIIIEarly.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AlbatrosDIII");
        Property.set(class1, "meshName", "3DO/Plane/AlbatrosDIIIEarly/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParWW1());
        Property.set(class1, "yearService", 1916F);
        Property.set(class1, "yearExpired", 1930F);
        Property.set(class1, "FlightModel", "FlightModels/AlbatrosD3.fmd:AlbatrosD3");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAlbatrosD3Early.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
