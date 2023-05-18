package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F4U_4 extends F4U implements TypeFighter, TypeStormovik {

    public F4U_4() {
    }

    static {
        Class var_class = F4U_4.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "F4U4");
        Property.set(var_class, "meshName", "3DO/Plane/F4U4(Multi1)/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(var_class, "meshName_us", "3DO/Plane/F4U4(USA)/hier.him");
        Property.set(var_class, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(var_class, "yearService", 1945F);
        Property.set(var_class, "yearExpired", 1955.5F);
        Property.set(var_class, "FlightModel", "FlightModels/F4U-4.fmd:F4ULate_FM");
        Property.set(var_class, "cockpitClass", new Class[] { CockpitF4U1D.class });
        Property.set(var_class, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(var_class, new int[] { 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(var_class,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16" });
    }
}
