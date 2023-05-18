package com.maddox.il2.objects.ships;

import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.rts.SectFile;

public abstract class ShipAshe extends Ship {
    public static class HMSWarspite extends BigshipGeneric implements TgtShip {

        public HMSWarspite() {
        }

        public HMSWarspite(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

// public static class HMSTartar extends BigshipGeneric
// implements TgtShip
// {
//
// public HMSTartar()
// {
// }
//
// public HMSTartar(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
// {
// super(s, i, sectfile, s1, sectfile1, s2);
// }
// }
//
// public static class HMSNubian extends BigshipGeneric
// implements TgtShip
// {
//
// public HMSNubian()
// {
// }
//
// public HMSNubian(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
// {
// super(s, i, sectfile, s1, sectfile1, s2);
// }
// }
//
// public static class HMSMatabele extends BigshipGeneric
// implements TgtShip
// {
//
// public HMSMatabele()
// {
// }
//
// public HMSMatabele(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
// {
// super(s, i, sectfile, s1, sectfile1, s2);
// }
// }
//
// public static class HMSJupiter extends BigshipGeneric
// implements TgtShip
// {
//
// public HMSJupiter()
// {
// }
//
// public HMSJupiter(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
// {
// super(s, i, sectfile, s1, sectfile1, s2);
// }
// }
//
// public static class HMSCossack extends BigshipGeneric
// implements TgtShip
// {
//
// public HMSCossack()
// {
// }
//
// public HMSCossack(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
// {
// super(s, i, sectfile, s1, sectfile1, s2);
// }
// }
//
    static {
// new BigshipGeneric.SPAWN(HMSCossack.class);
// new BigshipGeneric.SPAWN(HMSJupiter.class);
// new BigshipGeneric.SPAWN(HMSMatabele.class);
// new BigshipGeneric.SPAWN(HMSNubian.class);
// new BigshipGeneric.SPAWN(HMSTartar.class);
        new BigshipGeneric.SPAWN(HMSWarspite.class);
    }
}
