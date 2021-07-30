package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_40SUKAISVOLOCHHAWKA2 extends P_40SUKAISVOLOCH {

    public P_40SUKAISVOLOCHHAWKA2() {
    }

    static {
        Class class1 = P_40SUKAISVOLOCHHAWKA2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-40");
        Property.set(class1, "meshName", "3DO/Plane/Hawk81A-2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_us", "3DO/Plane/Hawk81A-2(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-40C.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHAWK.class });
        Property.set(class1, "LOSElevation", 1.0728F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06" });
    }
}
