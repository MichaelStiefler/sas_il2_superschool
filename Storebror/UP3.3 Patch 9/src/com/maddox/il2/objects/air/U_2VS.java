package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class U_2VS extends U_2VS_TD {
    static 
    {
        Class class1 = U_2VS.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "U-2");
        Property.set(class1, "meshName", "3do/plane/U_2VS/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1945.8F);
        Property.set(class1, "FlightModel", "FlightModels/U-2VS.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitU_2VS.class, CockpitU_2VS_Bombardier.class, CockpitU_2VS_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 3, 3, 3, 3, 3, 3, 9, 9, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN02", "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb05", 
            "_ExternalBomb06", "_ExternalBail02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08"
        });
    }
}
