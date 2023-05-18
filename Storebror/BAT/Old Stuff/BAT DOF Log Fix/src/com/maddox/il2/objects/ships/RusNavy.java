package com.maddox.il2.objects.ships;

import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.rts.SectFile;

public abstract class RusNavy extends Ship {
    public static class Gavriil extends BigshipGeneric implements TgtShip {

        public Gavriil() {
        }

        public Gavriil(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Azard extends BigshipGeneric implements TgtShip {

        public Azard() {
        }

        public Azard(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Pallada extends BigshipGeneric implements TgtShip {

        public Pallada() {
        }

        public Pallada(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Diana extends BigshipGeneric implements TgtShip {

        public Diana() {
        }

        public Diana(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

// public static class Petropavlovsk extends BigshipGeneric
// implements TgtShip
// {
//
// public Petropavlovsk()
// {
// }
//
// public Petropavlovsk(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
// {
// super(s, i, sectfile, s1, sectfile1, s2);
// }
// }
//
    static {
// new BigshipGeneric.SPAWN(Petropavlovsk.class);
        new BigshipGeneric.SPAWN(Diana.class);
        new BigshipGeneric.SPAWN(Pallada.class);
        new BigshipGeneric.SPAWN(Azard.class);
        new BigshipGeneric.SPAWN(Gavriil.class);
    }
}
