package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_15_I extends KI_15xyz {

    static {
        Class class1 = KI_15_I.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-15");
        Property.set(class1, "meshName", "3DO/Plane/Ki-15/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-15-I.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_15.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05",
                "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev06", "_ExternalDev07" });
    }
}
