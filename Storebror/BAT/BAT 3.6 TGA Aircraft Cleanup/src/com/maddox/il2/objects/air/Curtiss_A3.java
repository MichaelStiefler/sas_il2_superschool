package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Curtiss_A3 extends Curtiss_A3x {

    public Curtiss_A3() {
    }

    static {
        Class class1 = Curtiss_A3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Curtiss_A3");
        Property.set(class1, "meshName", "3DO/Plane/Curtiss_A3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1925F);
        Property.set(class1, "yearExpired", 1937F);
        Property.set(class1, "FlightModel", "FlightModels/F8c4.fmd:F8C4_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCurtiss_A3.class });
        Property.set(class1, "LOSElevation", 0.74185F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 0, 0, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
