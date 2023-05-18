package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_51C extends P_51 {

    public P_51C() {
    }

    static {
        Class class1 = P_51C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-51");
        Property.set(class1, "meshNameDemo", "3DO/Plane/P-51C(USA)/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/P-51C(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/P-51C(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-51C.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_51C.class });
        Property.set(class1, "LOSElevation", 1.03F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
