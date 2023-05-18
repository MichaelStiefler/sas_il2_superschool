package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MC_200_1 extends MC_200xyz
{

    public MC_200_1()
    {
    }

    static 
    {
        Class class1 = MC_200_1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "M.C.200");
        Property.set(class1, "meshName_it", "3DO/Plane/MC-200_I(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeFCSPar02());
        Property.set(class1, "meshName", "3DO/Plane/MC-200_I(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/MC-200.fmd");
        weaponTriggersRegister(class1, new int[] {
            0, 0
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02"
        });
    }
}
