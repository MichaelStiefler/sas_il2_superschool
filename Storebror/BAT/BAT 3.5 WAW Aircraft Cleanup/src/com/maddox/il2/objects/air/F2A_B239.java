package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class F2A_B239 extends F2A {

    public F2A_B239() {
    }

    public float getEyeLevelCorrection() {
        return 0.05F;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals("fi")) {
            int i = Mission.getMissionDate(true);
            if (i > 0) {
                if (i < 0x1282df2) {
                    return "winterwar_";
                }
                if (i < 0x1282fd5) {
                    return "early_";
                }
            }
        }
        return "";
    }

    static {
        Class class1 = F2A_B239.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-239");
        Property.set(class1, "meshNameDemo", "3DO/Plane/B-239/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/B-239/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "originCountry", PaintScheme.countryFinland);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F2A-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF2A1.class });
        Property.set(class1, "LOSElevation", 1.032F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
