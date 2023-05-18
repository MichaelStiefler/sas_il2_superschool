package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class MIG_11 extends MIG_7 {

    public MIG_11() {
    }

    static {
        Class class1 = MIG_11.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-11");
        Property.set(class1, "meshName", "3DO/Plane/MiG-11/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1953.5F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-11.fmd:MIG_7_11_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_7.class });
        Property.set(class1, "LOSElevation", 0.906F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
