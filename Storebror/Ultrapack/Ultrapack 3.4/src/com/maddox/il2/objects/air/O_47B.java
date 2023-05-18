package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class O_47B extends O_47 {
    static {
        Class class1 = O_47B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "O-47");
        Property.set(class1, "meshName", "3DO/Plane/O-47/hierB.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/O47B.fmd");
        Property.set(class1, "LOSElevation", 1.0728F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitO_47B.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
