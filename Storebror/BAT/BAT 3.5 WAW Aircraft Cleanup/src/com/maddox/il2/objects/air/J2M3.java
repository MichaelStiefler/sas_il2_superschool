package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class J2M3 extends J2M {

    public J2M3() {
    }

    static {
        Class class1 = J2M3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "J2M");
        Property.set(class1, "meshName", "3DO/Plane/J2M3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/J2M3(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/J2M3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJ2M3.class });
        Property.set(class1, "LOSElevation", 1.113F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
