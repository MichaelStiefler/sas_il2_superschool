package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Hawk_3 extends Hawk_3xyz {

    public Hawk_3() {
    }

    static {
        Class class1 = Hawk_3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hawk-III");
        Property.set(class1, "meshName", "3DO/Plane/Hawk_3/Hawk3_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar08());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/Hawk3.fmd:Hawk3_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHawk_3.class });
        Property.set(class1, "LOSElevation", 0.84305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb05" });
    }
}
