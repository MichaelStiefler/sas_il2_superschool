package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Curtiss_P6 extends Curtiss_P6x {

    public Curtiss_P6() {
    }

    static {
        Class class1 = Curtiss_P6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Curtiss_P6");
        Property.set(class1, "meshName", "3DO/Plane/Curtiss_P6(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1925F);
        Property.set(class1, "yearExpired", 1937F);
        Property.set(class1, "FlightModel", "FlightModels/F8c4.fmd:F8C4_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCurtiss_P6.class });
        Property.set(class1, "LOSElevation", 0.74185F);
        Aircraft.weaponTriggersRegister(class1, new int[2]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
