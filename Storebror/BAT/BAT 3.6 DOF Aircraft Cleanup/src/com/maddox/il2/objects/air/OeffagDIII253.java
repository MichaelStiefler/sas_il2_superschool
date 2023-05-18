package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class OeffagDIII253 extends Albatros {

    public OeffagDIII253() {
    }

    static {
        Class class1 = OeffagDIII253.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "OeffagDIII");
        Property.set(class1, "meshName", "3DO/Plane/OeffagDIII(Multi1)/hier253.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1916F);
        Property.set(class1, "yearExpired", 1930F);
        Property.set(class1, "FlightModel", "FlightModels/OeffagD3_253.fmd:OeffagDIII");
        Property.set(class1, "cockpitClass", new Class[] { CockpitOeffagDIII253.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
