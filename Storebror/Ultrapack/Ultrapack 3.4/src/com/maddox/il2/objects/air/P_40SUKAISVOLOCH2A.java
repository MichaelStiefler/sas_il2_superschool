package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_40SUKAISVOLOCH2A extends P_40SUKAISVOLOCH {

    public P_40SUKAISVOLOCH2A() {
    }

    static {
        Class class1 = P_40SUKAISVOLOCH2A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-40");
        Property.set(class1, "meshName", "3DO/Plane/TomahawkMkIIa(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_gb", "3DO/Plane/TomahawkMkIIa(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-40B.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_40B.class });
        Property.set(class1, "LOSElevation", 1.0728F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06" });
    }
}
