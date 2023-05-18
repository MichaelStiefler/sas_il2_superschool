package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F_86A5 extends F_86A
{

    public F_86A5()
    {
    }

    static 
    {
        Class localClass = F_86A5.class;
        new NetAircraft.SPAWN(localClass);
        Property.set(localClass, "iconFar_shortClassName", "F-86");
        Property.set(localClass, "meshName", "3DO/Plane/F-86A(Multi1)/hier.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(localClass, "meshName_us", "3DO/Plane/F-86A(USA)/hier.him");
        Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(localClass, "yearService", 1949.9F);
        Property.set(localClass, "yearExpired", 1960.3F);
        Property.set(localClass, "FlightModel", "FlightModels/F-86A.fmd");
        Property.set(localClass, "cockpitClass", new Class[] {
            CockpitF_86A.class
        });
        Property.set(localClass, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(localClass, new int[] {
            0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 
            3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 
            2, 2, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(localClass, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", 
            "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08"
        });
    }
}
