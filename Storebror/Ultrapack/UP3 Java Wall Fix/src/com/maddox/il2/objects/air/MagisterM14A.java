package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MagisterM14A extends Magister
{

    public MagisterM14A()
    {
    }

    static 
    {
        Class class1 = MagisterM14A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Milesmag");
        Property.set(class1, "meshName", "3DO/Plane/Magister(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/MagM14A.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMagisterM14A.class
        });
        Property.set(class1, "LOSElevation", 0.74185F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01"
        });
    }
}
