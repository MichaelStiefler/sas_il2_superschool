package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class GLADIATOR1 extends GLADIATOR
{

    public GLADIATOR1()
    {
    }

    static 
    {
        Class class1 = GLADIATOR1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Gladiator");
        Property.set(class1, "meshName", "3DO/Plane/GladiatorMkI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/GladiatorMkI.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitGLADIATOR1.class });
        Property.set(class1, "LOSElevation", 0.8472F);
        Property.set(class1, "originCountry", PaintScheme.countryFinland);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04"
        });
    }
}
