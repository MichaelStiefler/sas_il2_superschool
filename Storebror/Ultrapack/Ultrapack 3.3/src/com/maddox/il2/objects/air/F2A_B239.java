package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F2A_B239 extends F2A {

    public F2A_B239() {
    }

    static {
        Class class1 = F2A_B239.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-239");
        Property.set(class1, "meshNameDemo", "3DO/Plane/B-239/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/B-239/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "originCountry", PaintScheme.countryFinland);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F2A-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF2A1.class });
        Property.set(class1, "LOSElevation", 1.032F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
