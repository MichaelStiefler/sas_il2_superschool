package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class HawkerStormMkIb_20 extends HawkerStorm {

    public HawkerStormMkIb_20() {
    }

    static {
        Class class1 = HawkerStormMkIb_20.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Storm");
        Property.set(class1, "meshName", "3DO/Plane/HawkerStormMkIb_20(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1940.5F);
        Property.set(class1, "FlightModel", "FlightModels/HawkerStormMkIb.fmd:HawkerStormMkIb_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHURRI.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
    }
}
