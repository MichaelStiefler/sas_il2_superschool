package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_100_IKO extends KI_100
{

    public KI_100_IKO()
    {
    }

    static 
    {
        Class class1 = KI_100_IKO.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-100");
        Property.set(class1, "meshName", "3DO/Plane/Ki-100-I(Ko)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-100-I(Ko)(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1947F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-100-I.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_100.class });
        Property.set(class1, "LOSElevation", 0.85935F);
        weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 3, 3, 9, 9
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02"
        });
    }
}
