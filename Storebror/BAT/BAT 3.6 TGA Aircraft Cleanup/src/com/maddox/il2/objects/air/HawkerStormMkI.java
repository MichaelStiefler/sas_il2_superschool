package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class HawkerStormMkI extends HawkerStorm {

    public HawkerStormMkI() {
    }

    static {
        Class class1 = HawkerStormMkI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Storm");
        Property.set(class1, "meshName", "3DO/Plane/HawkerStormMkI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1940.5F);
        Property.set(class1, "FlightModel", "FlightModels/HawkerStormMkI.fmd:HawkerStormMkI_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHawkerStormMkI.class });
        Aircraft.weaponTriggersRegister(class1, new int[8]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08" });
    }
}
