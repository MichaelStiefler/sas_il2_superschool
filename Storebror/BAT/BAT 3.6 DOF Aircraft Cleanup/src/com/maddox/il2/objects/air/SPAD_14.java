package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SPAD_14 extends SPAD_G {

    public SPAD_14() {
    }

    static {
        Class class1 = SPAD_14.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SPAD_14");
        Property.set(class1, "meshName", "3DO/Plane/SPAD-14(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/SPAD14.fmd:SPAD14_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSPAD_14.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_CANNON01" });
    }
}
