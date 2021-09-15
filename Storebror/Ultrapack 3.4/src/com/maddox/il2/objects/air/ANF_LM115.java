package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class ANF_LM115 extends ANF_LMxyz {
    static {
        Class class1 = ANF_LM115.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "ANF_LM115");
        Property.set(class1, "meshName", "3DO/Plane/ANF_LM113/hier115.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/ANFLM113.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitANF.class });
        Property.set(class1, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 10, 10, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_Cannon01", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
