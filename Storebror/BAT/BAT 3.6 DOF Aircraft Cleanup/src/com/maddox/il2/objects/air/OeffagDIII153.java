package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class OeffagDIII153 extends Albatros {

    public OeffagDIII153() {
    }

    static {
        Class class1 = OeffagDIII153.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "OeffagDIII");
        Property.set(class1, "meshName", "3DO/Plane/OeffagDIII(Multi1)/hier153.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1916F);
        Property.set(class1, "yearExpired", 1930F);
        Property.set(class1, "FlightModel", "FlightModels/OeffagD3_153.fmd:OeffagDIII");
        Property.set(class1, "cockpitClass", new Class[] { CockpitOeffagDIII153.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
