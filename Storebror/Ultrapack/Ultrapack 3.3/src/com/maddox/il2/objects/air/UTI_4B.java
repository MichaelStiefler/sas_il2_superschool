package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class UTI_4B extends I_16 implements TypeFighter, TypeTNBFighter {

    public UTI_4B() {
    }

    static {
        Class class1 = UTI_4B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/UTI-4B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type15.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitUTI_4B.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 3, 3, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01",
                "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
    }
}
