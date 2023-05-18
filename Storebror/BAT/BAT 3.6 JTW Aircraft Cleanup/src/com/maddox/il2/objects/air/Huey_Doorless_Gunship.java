package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Huey_Doorless_Gunship extends HueyX implements TypeScout, TypeTransport {

    public Huey_Doorless_Gunship() {
    }

    static {
        Class class1 = Huey_Doorless_Gunship.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Huey");
        Property.set(class1, "meshName", "3DO/Plane/Huey_Doorless_Gunship/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1986.5F);
        Property.set(class1, "FlightModel", "FlightModels/Huey.fmd:Huey_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHuey1B.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 0, 0, 0, 0, 9, 9, 1, 1, 2, 2, 2, 2, 9, 9, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev10", "_ExternalDev11", "_MGUN07", "_MGUN08", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalDev12", "_ExternalDev13", "_MGUN09", "_MGUN10" });
    }
}
