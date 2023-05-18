package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_39Q1 extends P_39 {

    public P_39Q1() {
    }

    static {
        Class class1 = P_39Q1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P39");
        Property.set(class1, "meshName", "3do/plane/P-39Q-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-39Q-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_39Q1.class });
        Property.set(class1, "LOSElevation", 0.8941F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_ExternalBomb01" });
    }
}
