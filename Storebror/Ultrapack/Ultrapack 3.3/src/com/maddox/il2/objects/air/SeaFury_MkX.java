package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SeaFury_MkX extends SeaFury implements TypeFighter, TypeStormovik {

    public SeaFury_MkX() {
    }

    static {
        Class class1 = SeaFury_MkX.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SeaFury");
        Property.set(class1, "meshName", "3DO/Plane/SeaFury_MkX/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1955.5F);
        Property.set(class1, "FlightModel", "FlightModels/SeaFuryMkI.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSFURY.class });
        Property.set(class1, "LOSElevation", 0.7394F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03",
                        "_ExternalBomb04", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09",
                        "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalDev09", "_ExternalDev10" });
    }
}
