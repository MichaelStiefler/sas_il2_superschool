package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F_105F extends F_105fuelReceiver {

    public F_105F() {
    }

    static {
        Class class1 = F_105F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-105F");
        Property.set(class1, "meshName", "3DO/Plane/F-105F/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1959F);
        Property.set(class1, "yearExpired", 1980F);
        Property.set(class1, "FlightModel", "FlightModels/F-105F.fmd:F105");
        Property.set(class1, "LOSElevation", 0.725F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_105.class, CockpitF_105Bombardier.class, CockpitT_33i.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 9, 9, 3, 3, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_Rocket05", "_Rocket06", "_Rocket07", "_Rocket08", "_Rocket09", "_Rocket10", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_Dev15", "_Dev16", "_ExternalDev17", "_ExternalDev18", "_ExternalDev19", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20",
                "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_Dev20", "_ExternalDev21", "_ExternalRock11", "_ExternalRock11", "_ExternalRock12", "_ExternalRock12", "_ExternalRock13", "_ExternalRock13", "_ExternalRock14", "_ExternalRock14", "_ExternalRock15", "_ExternalRock15", "_ExternalRock16", "_ExternalRock16", "_ExternalRock17", "_ExternalRock17", "_ExternalRock18", "_ExternalRock18", "_ExternalBomb24", "_ExternalBomb25", "_ExternalDev22", "_ExternalDev23", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalDev24" });
    }
}
