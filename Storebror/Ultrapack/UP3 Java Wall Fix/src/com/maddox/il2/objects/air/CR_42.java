package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CR_42 extends CR_42X
{

    public CR_42()
    {
    }

    static 
    {
        Class class1 = CR_42.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "CR.42");
        Property.set(class1, "meshName", "3DO/Plane/CR42(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/CR42(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/CR42.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCR42.class });
        Property.set(class1, "LOSElevation", 0.742F);
        weaponTriggersRegister(class1, new int[] {
            0, 0, 3, 3
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02"
        });
    }
}
