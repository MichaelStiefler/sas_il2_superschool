package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class GeeBeeR2 extends GeeBeeX {

    public GeeBeeR2() {
    }

    static {
        Class class1 = GeeBeeR2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "GeeBee");
        Property.set(class1, "meshName", "3DO/Plane/GeeBeeR2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1935F);
        Property.set(class1, "FlightModel", "FlightModels/GeeBeeR2.fmd:GeeBee");
        Property.set(class1, "cockpitClass", new Class[] { CockpitGeeBeeR2.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { null });
    }
}
