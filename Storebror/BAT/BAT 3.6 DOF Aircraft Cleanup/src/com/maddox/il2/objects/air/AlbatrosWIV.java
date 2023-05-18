package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class AlbatrosWIV extends AlbatrosWD {

    public AlbatrosWIV() {
    }

    static {
        Class class1 = AlbatrosWIV.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AlbatrosWD");
        Property.set(class1, "meshName", "3DO/Plane/AlbatrosWIV/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParWW1());
        Property.set(class1, "yearService", 1916F);
        Property.set(class1, "yearExpired", 1930F);
        Property.set(class1, "FlightModel", "FlightModels/AlbatrosW4.fmd:AlbatrosW4");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAlbatrosW4.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
