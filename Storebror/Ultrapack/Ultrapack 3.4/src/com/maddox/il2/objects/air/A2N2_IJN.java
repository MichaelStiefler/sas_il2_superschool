package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class A2N2_IJN extends A2N_23 {
    static {
        Class class1 = A2N2_IJN.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bi-Plane");
        Property.set(class1, "meshName", "3DO/Plane/Nakajima-A2N2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Nakajima-A2N2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitNaka_A2N.class });
        Property.set(class1, "LOSElevation", 0.74185F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
