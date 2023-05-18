package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Z26 extends Zx26 {

    public Z26() {
    }

    static {
        Class class1 = Z26.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Z-26");
        Property.set(class1, "meshName", "3DO/Plane/Z26/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1946);
        Property.set(class1, "yearExpired", 2050);
        Property.set(class1, "cockpitClass", new Class[] { CockpitZ26per.class, CockpitZ26zad.class });
        Property.set(class1, "FlightModel", "FlightModels/Z26.fmd:Z26");
        Aircraft.weaponTriggersRegister(class1, new int[] { 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalBomb01" });
    }
}
