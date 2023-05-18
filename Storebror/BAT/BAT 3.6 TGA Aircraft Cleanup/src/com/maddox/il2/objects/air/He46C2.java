package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class He46C2 extends He46xyz {

    public He46C2() {
    }

    static {
        Class class1 = He46C2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He46C2");
        Property.set(class1, "meshName", "3DO/Plane/He46/hierC2.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/He46/hierC2.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/He46.fmd:He46_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHe46.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 3, 3, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02" });
    }
}
