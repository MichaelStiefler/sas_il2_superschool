package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class GLADIATOR1J8A extends GLADIATOR {

    static {
        Class class1 = GLADIATOR1J8A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Gladiator");
        Property.set(class1, "meshName", "3DO/Plane/GladiatorJ8A(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/GladiatorMkI (Ultrapack).fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJ8A.class });
        Property.set(class1, "LOSElevation", 0.8472F);
        Property.set(class1, "originCountry", PaintScheme.countryFinland);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
