package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class UT_2 extends UT2X {

    public UT_2() {
    }

    static {
        Class class1 = UT_2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "UT_2");
        Property.set(class1, "meshName", "3DO/Plane/UT-2(Multi1)/hierMV4.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/MagM14A.fmd:UT2FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitUT_2.class });
        Property.set(class1, "LOSElevation", 0.74185F);
        Aircraft.weaponTriggersRegister(class1, new int[0]);
        Aircraft.weaponHooksRegister(class1, new String[0]);
    }
}
