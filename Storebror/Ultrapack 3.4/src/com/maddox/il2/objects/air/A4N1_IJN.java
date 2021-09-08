package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class A4N1_IJN extends A4N_xyz {
    static {
        Class class1 = A4N1_IJN.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bi-Plane");
        Property.set(class1, "meshName", "3DO/Plane/Nakajima-A4N1(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Nakajima-A4N1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitNaka_A4N1.class });
        Property.set(class1, "LOSElevation", 0.74185F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
