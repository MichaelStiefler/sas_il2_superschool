package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F1M extends F1Mx {

    public F1M() {
    }

    static {
        Class class1 = F1M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F1M2");
        Property.set(class1, "meshName", "3DO/Plane/F1M/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/F1M.fmd:F1M_FM");
        Property.set(class1, "LOSElevation", 1.0728F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitF1M.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 0, 0, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
