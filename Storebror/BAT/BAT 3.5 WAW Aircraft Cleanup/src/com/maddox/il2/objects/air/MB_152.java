package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MB_152 extends MB_152xyz {

    public MB_152() {
    }

    static {
        Class class1 = MB_152.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MB-152");
        Property.set(class1, "meshName", "3DO/Plane/MB-152/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1947F);
        Property.set(class1, "FlightModel", "FlightModels/MB-152.fmd:MB152");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMB_152.class });
        Property.set(class1, "LOSElevation", 0.85935F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02" });
    }
}
