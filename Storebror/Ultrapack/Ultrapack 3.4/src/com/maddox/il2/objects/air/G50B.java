package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class G50B extends G50bis_xyz {
    static {
        Class var_class = G50B.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "G.50");
        Property.set(var_class, "meshName", "3DO/Plane/G-50B(Multi1)/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(var_class, "originCountry", PaintScheme.countryItaly);
        Property.set(var_class, "yearService", 1938F);
        Property.set(var_class, "yearExpired", 1944.8F);
        Property.set(var_class, "FlightModel", "FlightModels/G50B.fmd");
        Property.set(var_class, "cockpitClass", new Class[] { CockpitG50B.class, CockpitG50_R.class });
        Property.set(var_class, "LOSElevation", 0.98615F);
        Aircraft.weaponTriggersRegister(var_class, new int[2]);
        Aircraft.weaponHooksRegister(var_class, new String[] { "_MGUN01", "_MGUN02" });
    }
}
