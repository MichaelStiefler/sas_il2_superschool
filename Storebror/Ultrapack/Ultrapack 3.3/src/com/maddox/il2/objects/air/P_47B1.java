package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_47B1 extends P_47ModPack {

    static {
        Class class1 = P_47B1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-47");
        Property.set(class1, "meshName", "3DO/Plane/P-47B1(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/P-47B1(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-47B1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D10.class });
        Property.set(class1, "LOSElevation", 0.9879F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06" });
    }
}
