package com.maddox.il2.objects.ships;

import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.objects.vehicles.radios.TypeHasHayRake;
import com.maddox.rts.SectFile;

public abstract class ShipAsheUSS extends Ship {
    public static class USSEnterpriseCV6_44 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSEnterpriseCV6_44() {
        }

        public USSEnterpriseCV6_44(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSEnterpriseCV6_42 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSEnterpriseCV6_42() {
        }

        public USSEnterpriseCV6_42(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//    public static class USSLexingtonCV2_42 extends BigshipGeneric
//        implements TgtShip, TypeHasHayRake
//    {
//
//        public USSLexingtonCV2_42()
//        {
//        }
//
//        public USSLexingtonCV2_42(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//        {
//            super(s, i, sectfile, s1, sectfile1, s2);
//        }
//    }

    public static class USSYorktownCV5_42 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSYorktownCV5_42() {
        }

        public USSYorktownCV5_42(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSHornetCV8_42D extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSHornetCV8_42D() {
        }

        public USSHornetCV8_42D(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSHornetCV8_42 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSHornetCV8_42() {
        }

        public USSHornetCV8_42(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    static {
//        new BigshipGeneric.SPAWN(USSLexingtonCV2_42.class);
        new BigshipGeneric.SPAWN(USSYorktownCV5_42.class);
        new BigshipGeneric.SPAWN(USSHornetCV8_42.class);
        new BigshipGeneric.SPAWN(USSHornetCV8_42D.class);
        new BigshipGeneric.SPAWN(USSEnterpriseCV6_42.class);
        new BigshipGeneric.SPAWN(USSEnterpriseCV6_44.class);
    }
}
