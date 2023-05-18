package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F4UCORSAIR2 extends F4U
{

    public F4UCORSAIR2()
    {
    }

    static 
    {
        Class class1 = F4UCORSAIR2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F4U");
        Property.set(class1, "meshName", "3DO/Plane/CorsairMkII(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_gb", "3DO/Plane/CorsairMkII(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F4U-1Aclipped.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF4U1A.class });
        Property.set(class1, "LOSElevation", 1.0585F);
        weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 
            3, 3, 3, 9, 9
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb02", "_ExternalBomb03"
        });
    }
}
