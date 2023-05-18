package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class IAR_80 extends IAR_8X implements TypeFighter {

    public IAR_80() {
    }

    static {
        Class class1 = IAR_80.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "IAR 80");
        Property.set(class1, "meshName", "3DO/Plane/IAR-80/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1939.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/IAR-80.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitIAR80.class });
        Property.set(class1, "LOSElevation", 0.8323F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
