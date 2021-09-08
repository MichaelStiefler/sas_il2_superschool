package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SB2U_USN extends SB2U {
    static {
        Class class1 = SB2U_USN.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SB2U");
        Property.set(class1, "meshName", "3DO/Plane/SB2U/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/SB2U.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSB2U.class });
        Property.set(class1, "LOSElevation", 0.84305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
