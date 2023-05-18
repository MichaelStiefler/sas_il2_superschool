package com.maddox.il2.objects.ships;

import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.rts.SectFile;

public abstract class ShipYardSteam extends Ship {
    public static class Avizo2 extends BigshipGeneric implements TgtShip {

        public Avizo2() {
        }

        public Avizo2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Avizo1 extends BigshipGeneric implements TgtShip {

        public Avizo1() {
        }

        public Avizo1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class GunBoat2 extends BigshipGeneric implements TgtShip {

        public GunBoat2() {
        }

        public GunBoat2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class GunBoat1 extends BigshipGeneric implements TgtShip {

        public GunBoat1() {
        }

        public GunBoat1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class QMary extends BigshipGeneric implements TgtShip {

        public QMary() {
        }

        public QMary(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

// public static class Steamboat extends BigshipGeneric
// implements TgtShip
// {
//
// public Steamboat()
// {
// }
//
// public Steamboat(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
// {
// super(s, i, sectfile, s1, sectfile1, s2);
// }
// }
//
    static {
// new BigshipGeneric.SPAWN(Steamboat.class);
        new BigshipGeneric.SPAWN(QMary.class);
        new BigshipGeneric.SPAWN(GunBoat1.class);
        new BigshipGeneric.SPAWN(GunBoat2.class);
        new BigshipGeneric.SPAWN(Avizo1.class);
        new BigshipGeneric.SPAWN(Avizo2.class);
    }
}
