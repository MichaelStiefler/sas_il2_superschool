package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SPAD_MONO extends SPAD_X {

    public SPAD_MONO() {
    }

    static {
        Class class1 = SPAD_MONO.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SPAD");
        Property.set(class1, "meshName", "3DO/Plane/SPAD-MONO/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/SPAD_MONO.fmd:SPAD_MONO_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSPAD_MONO.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
