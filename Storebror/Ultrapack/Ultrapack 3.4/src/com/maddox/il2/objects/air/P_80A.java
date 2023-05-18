package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_80A extends P_80 {

    public P_80A() {
    }

    static {
        Class class1 = P_80A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "YP-80");
        Property.set(class1, "meshName", "3DO/Plane/P-80(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/P-80(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "meshNameDemo", "3DO/Plane/P-80(USA)/hier.him");
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/P-80A.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYP_80.class });
        Property.set(class1, "LOSElevation", 0.965F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06" });
    }
}
