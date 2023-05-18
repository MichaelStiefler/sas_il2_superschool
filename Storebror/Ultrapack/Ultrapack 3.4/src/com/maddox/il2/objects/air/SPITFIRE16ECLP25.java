package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SPITFIRE16ECLP25 extends SPITFIRE16E {

    public SPITFIRE16ECLP25() {
    }

    static {
        Class class1 = SPITFIRE16ECLP25.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/SpitfireMkXVIeCLP(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_gb", "3DO/Plane/SpitfireMkXVIeCLP(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/SpitfireXVICLP25.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSpit16E.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 1, 9, 9, 9, 3, 3, 9, 3, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev08", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev01",
                "_ExternalBomb01", "_ExternalRock01", "_ExternalRock02" });
    }
}
