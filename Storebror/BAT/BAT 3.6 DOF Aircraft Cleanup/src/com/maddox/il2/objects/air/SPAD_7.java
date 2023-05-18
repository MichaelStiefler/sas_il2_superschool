package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SPAD_7 extends SPAD_X {

    public SPAD_7() {
    }

    static {
        Class class1 = SPAD_7.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SPAD");
        Property.set(class1, "meshName", "3DO/Plane/SPAD-7(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/SPAD7.fmd:SPAD7_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSPAD_7.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
