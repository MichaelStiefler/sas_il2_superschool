package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Myrsky_II extends MyrskyX
    implements TypeFighter, TypeStormovik
{
    static 
    {
        Class class1 = Myrsky_II.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Myrsky-II");
        Property.set(class1, "meshName", "3DO/Plane/Myrsky_II/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Myrsky-II.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMyrsky_II.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 3, 3, 3, 3, 9, 9, 
            9, 9, 9, 9, 9, 9, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", 
            "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08"
        });
    }
}
