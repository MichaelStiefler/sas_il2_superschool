package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Hs126A_1_Spats extends Hs126 {

    static {
        Class localClass = Hs126A_1_Spats.class;
        new NetAircraft.SPAWN(localClass);
        Property.set(localClass, "iconFar_shortClassName", "Hs126A_1_Spats");
        Property.set(localClass, "meshName", "3DO/Plane/Hs126A_1_Spats/hierSpats.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(localClass, "meshName_de", "3DO/Plane/Hs126A_1_Spats/hierSpats.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(localClass, "yearService", 1939F);
        Property.set(localClass, "yearExpired", 1943F);
        Property.set(localClass, "FlightModel", "FlightModels/Hs126.fmd");
        Property.set(localClass, "cockpitClass", new Class[] { CockpitHS126.class });
        Property.set(localClass, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(localClass, new int[] { 0, 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(localClass,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10",
                        "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06",
                        "_ExternalDev07", "_ExternalDev08" });
    }
}
