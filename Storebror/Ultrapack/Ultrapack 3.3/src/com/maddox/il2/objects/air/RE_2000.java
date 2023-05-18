package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class RE_2000 extends RE_2000xyz {

    public RE_2000() {
    }

    static {
        Class class1 = RE_2000.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "RE.2000");
        Property.set(class1, "meshName_hu", "3DO/Plane/RE-2000(hu)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_it", "3DO/Plane/RE-2000(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "meshName", "3DO/Plane/RE-2000(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/RE-2000.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitRE_2000.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_BOMB100KG01", "_BOMB100KG02", "_BOMBCASSETTE01", "_BOMBCASSETTE02" });
    }
}
