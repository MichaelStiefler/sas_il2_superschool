package com.maddox.il2.objects.ships;

import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.objects.vehicles.radios.TypeHasHayRake;
import com.maddox.rts.SectFile;

public abstract class ShipAshe extends Ship {
    public static class HMSFurious extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public HMSFurious() {
        }

        public HMSFurious(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSArkRoyal extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public HMSArkRoyal() {
        }

        public HMSArkRoyal(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSEagle extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public HMSEagle() {
        }

        public HMSEagle(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class RMCaioDuilio extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public RMCaioDuilio()
//         {
//         }
//
//         public RMCaioDuilio(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class HMSFiji extends BigshipGeneric implements TgtShip {

        public HMSFiji() {
        }

        public HMSFiji(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSWarspite extends BigshipGeneric implements TgtShip {

        public HMSWarspite() {
        }

        public HMSWarspite(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class HMSNelson extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public HMSNelson()
//         {
//         }
//
//         public HMSNelson(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class HMSTartar extends BigshipGeneric implements TgtShip {

        public HMSTartar() {
        }

        public HMSTartar(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSNubian extends BigshipGeneric implements TgtShip {

        public HMSNubian() {
        }

        public HMSNubian(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSMatabele extends BigshipGeneric implements TgtShip {

        public HMSMatabele() {
        }

        public HMSMatabele(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class HMSKipling extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public HMSKipling()
//         {
//         }
//
//         public HMSKipling(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class HMASNapier extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public HMASNapier()
//         {
//         }
//
//         public HMASNapier(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class HMSJavelin extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public HMSJavelin()
//         {
//         }
//
//         public HMSJavelin(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class HMSJupiter extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public HMSJupiter()
//         {
//         }
//
//         public HMSJupiter(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class HMSCossack extends BigshipGeneric implements TgtShip {

        public HMSCossack() {
        }

        public HMSCossack(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    static {
        new BigshipGeneric.SPAWN(HMSCossack.class);
//        new BigshipGeneric.SPAWN(HMSJupiter.class);
//        new BigshipGeneric.SPAWN(HMSJavelin.class);
//        new BigshipGeneric.SPAWN(HMASNapier.class);
//        new BigshipGeneric.SPAWN(HMSKipling.class);
        new BigshipGeneric.SPAWN(HMSMatabele.class);
        new BigshipGeneric.SPAWN(HMSNubian.class);
        new BigshipGeneric.SPAWN(HMSTartar.class);
        new BigshipGeneric.SPAWN(HMSWarspite.class);
//        new BigshipGeneric.SPAWN(HMSNelson.class);
        new BigshipGeneric.SPAWN(HMSFiji.class);
//        new BigshipGeneric.SPAWN(RMCaioDuilio.class);
        new BigshipGeneric.SPAWN(HMSEagle.class);
        new BigshipGeneric.SPAWN(HMSArkRoyal.class);
        new BigshipGeneric.SPAWN(HMSFurious.class);
    }
}
