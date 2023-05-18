package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class U_2UT extends U_2_TD {
    static 
    {
        Class class1 = U_2UT.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "U-2");
        Property.set(class1, "meshName", "3do/plane/U_2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1945.8F);
        Property.set(class1, "FlightModel", "FlightModels/U-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitU_2.class, CockpitU_2_Bombardier.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            3, 3, 3, 3, 9, 9, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBail02"
        });
    }
}
