package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Dragonfly34N extends Dragonfly34z implements TypeAmphibiousPlane, TypeFighter {

    public Dragonfly34N() {
    }

    static {
        Class class1 = Dragonfly34N.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dragonfly34N");
        Property.set(class1, "meshName", "3DO/Plane/Dragonfly34N/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1947F);
        Property.set(class1, "FlightModel", "FlightModels/Dragonfly34N.fmd:Dragonfly34_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDragonfly34.class });
        Property.set(class1, "LOSElevation", 0.81305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02" });
    }
}
