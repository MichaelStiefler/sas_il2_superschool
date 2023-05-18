package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SeaFuryMkI extends SeaFury
    implements TypeFighter, TypeStormovik
{

    public SeaFuryMkI()
    {
    }

    static 
    {
        Class class1 = SeaFuryMkI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SeaFury");
        Property.set(class1, "meshName", "3DO/Plane/SeaFuryMkI/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1955.5F);
        Property.set(class1, "FlightModel", "FlightModels/SeaFuryMkI.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSFURY.class
        });
        Property.set(class1, "LOSElevation", 0.7394F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", 
            "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08"
        });
    }
}
