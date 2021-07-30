package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class G_55_ss0_Late extends G_55xyz {

    public G_55_ss0_Late() {
    }

    static {
        Class class1 = G_55_ss0_Late.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G.55");
        Property.set(class1, "meshName_it", "3DO/Plane/G-55_ss0(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "meshName", "3DO/Plane/G-55_ss0(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/G-55_ss0-late.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitG_55.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01" });
    }
}
