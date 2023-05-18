package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Curtiss_37F_Cyclone extends Curtiss_37F_Cyclonex {

    public Curtiss_37F_Cyclone() {
    }

    static {
        Class class1 = Curtiss_37F_Cyclone.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Curtiss_37F_C");
        Property.set(class1, "meshName", "3DO/Plane/Curtiss_37F_Cyclone(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1925F);
        Property.set(class1, "yearExpired", 1937F);
        Property.set(class1, "FlightModel", "FlightModels/YokosukaE14Y.fmd:Yokosuka_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCurtiss_37F_Cyclone.class });
        Property.set(class1, "LOSElevation", 0.74185F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
