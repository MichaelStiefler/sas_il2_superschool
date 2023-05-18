package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class PWS_10 extends PWS_10x {

    public PWS_10() {
    }

    static {
        Class class1 = PWS_10.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "PWS_10");
        Property.set(class1, "meshName", "3DO/Plane/PWS-10(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_pl", "3DO/Plane/PWS-10/hier.him");
        Property.set(class1, "PaintScheme_pl", new PaintSchemeFCSPar01());
        Property.set(class1, "meshName_sp", "3DO/Plane/PWS-10(Spain)/hier.him");
        Property.set(class1, "PaintScheme_sp", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1939F);
        Property.set(class1, "FlightModel", "FlightModels/PWS-10.fmd:PWS10_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPWS_10.class });
        Property.set(class1, "LOSElevation", 0.87195F);
        Aircraft.weaponTriggersRegister(class1, new int[2]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
