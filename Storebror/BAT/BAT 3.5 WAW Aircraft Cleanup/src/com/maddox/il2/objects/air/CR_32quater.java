package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CR_32quater extends CR_32quaterx implements TypeFighter, TypeTNBFighter {

    public CR_32quater() {
    }

    public boolean bChangedPit;

    static {
        Class class1 = CR_32quater.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "CR.32");
        Property.set(class1, "meshName", "3DO/Plane/CR32/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1941F);
        Property.set(class1, "FlightModel", "FlightModels/CR32.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCR32quater.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
