package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class LA_5FN extends LA_X
{

    public LA_5FN()
    {
    }

    static 
    {
        Class class1 = LA_5FN.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "La");
        Property.set(class1, "meshName", "3DO/Plane/La-5FN(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/La-5FN.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLA_5FN.class });
        Property.set(class1, "LOSElevation", 0.750618F);
        weaponTriggersRegister(class1, new int[] {
            1, 1, 3, 3, 9, 9
        });
        weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02"
        });
    }
}
