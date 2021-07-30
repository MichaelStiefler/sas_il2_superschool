package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class U_2NB extends U_2VSLNB_TD {
    static 
    {
        Class class1 = U_2NB.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "U-2");
        Property.set(class1, "meshName", "3do/plane/U_2VSLNB/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1967.8F);
        Property.set(class1, "FlightModel", "FlightModels/U-2VSLNB.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitU_2VSLNB.class, CockpitU_2VSLNB_Bombardier.class, CockpitU_2VSLNB_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 3, 3, 3, 3, 3, 3, 3, 3, 9, 
            9, 3, 3, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", 
            "_ExternalDev04", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBail02", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14"
        });
    }
}
