package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class AT6 extends SBD
    implements TypeStormovik
{

    public AT6()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null)
            hierMesh().chunkVisible("Pilon_D0", true);
    }

    static 
    {
        Class class1 = AT6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AT6");
        Property.set(class1, "meshName", "3DO/Plane/AT6(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/AT6(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1975.5F);
        Property.set(class1, "FlightModel", "FlightModels/T-6Texan.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitAT6.class
        });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 
            9, 9, 3, 3, 2, 2, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", 
            "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", 
            "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12"
        });
    }
}
