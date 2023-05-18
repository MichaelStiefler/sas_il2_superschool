package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_39D2 extends P_39 {

    public P_39D2() {
    }

    static {
        Class class1 = P_39D2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P39");
        Property.set(class1, "meshName", "3DO/Plane/P-39D-2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_us", "3DO/Plane/P-39D-2(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-39D.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_39N1.class });
        Property.set(class1, "LOSElevation", 0.8941F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 1, 9, 3, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_CANNON01", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02" });
    }
}
