package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class GeeBeeR1 extends GeeBeeX {

    public GeeBeeR1() {
    }

    static {
        Class class1 = GeeBeeR1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "GeeBee");
        Property.set(class1, "meshName", "3DO/Plane/GeeBeeR1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1935F);
        Property.set(class1, "FlightModel", "FlightModels/GeeBeeR1.fmd:GeeBee");
        Property.set(class1, "cockpitClass", new Class[] { CockpitGeeBeeR1.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { null });
    }
}
