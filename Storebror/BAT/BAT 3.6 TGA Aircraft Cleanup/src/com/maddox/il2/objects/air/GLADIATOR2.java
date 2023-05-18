package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class GLADIATOR2 extends GLADIATOR {

    public GLADIATOR2() {
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
                    return "earlycontwar_";
                } else {
                    return "contwar_";
                }
            }
        }
        return "";
    }

    static {
        Class class1 = GLADIATOR2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Gladiator");
        Property.set(class1, "meshName", "3DO/Plane/GladiatorMkII(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/GladiatorMkII.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitGLADIATOR2.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
