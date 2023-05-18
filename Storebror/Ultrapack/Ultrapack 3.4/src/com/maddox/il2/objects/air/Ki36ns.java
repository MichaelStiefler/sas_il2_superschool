package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Ki36ns extends Ki36X {
    static {
        Class class1 = Ki36ns.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ida");
        Property.set(class1, "meshName", "3DO/Plane/Ki36ns/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-32.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKi36.class });
        Property.set(class1, "LOSElevation", 0.87195F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16" });
    }
}
