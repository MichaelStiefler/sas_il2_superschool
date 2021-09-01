package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class FireflyMKIearly extends Fireflyxyz
    implements TypeStormovik
{

    static 
    {
        Class class1 = FireflyMKIearly.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Firefly Mk.I early");
        Property.set(class1, "meshName", "3DO/Plane/Fireflyearly(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1947F);
        Property.set(class1, "FlightModel", "FlightModels/FireflyMKIearly.fmd");
        Property.set(class1, "LOSElevation", 1.1058F);
        Property.set(class1, "cockpitClass", new Class[] {
                CockpitFireflyUpdate.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 1, 1, 3, 3, 9, 9, 2, 2, 
            2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 
            2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", 
            "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", 
            "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", 
            "_ExternalDev17", "_ExternalDev18", "_ExternalDev19", "_ExternalDev20", "_ExternalDev21", "_ExternalDev22", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", 
            "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16"
        });
    }
}
