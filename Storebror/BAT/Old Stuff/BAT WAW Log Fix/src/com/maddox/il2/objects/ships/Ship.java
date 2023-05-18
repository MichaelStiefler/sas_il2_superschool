package com.maddox.il2.objects.ships;

import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.ai.ground.TypeRadar;
import com.maddox.il2.objects.vehicles.radios.TypeHasBeacon;
import com.maddox.il2.objects.vehicles.radios.TypeHasHayRake;
import com.maddox.rts.SectFile;

public abstract class Ship {
//     public static class I400_Sub extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public I400_Sub()
//         {
//         }
//
//         public I400_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class I400_Srf extends BigshipGeneric implements TgtShip {

        public I400_Srf() {
        }

        public I400_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class Apanui extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Apanui()
//         {
//         }
//
//         public Apanui(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Awanui extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Awanui()
//         {
//         }
//
//         public Awanui(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class FortGeorge extends BigshipGeneric implements TgtShip {

        public FortGeorge() {
        }

        public FortGeorge(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class germantrawlerA extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public germantrawlerA()
//         {
//         }
//
//         public germantrawlerA(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class HMSColossusCV extends BigshipGeneric
//         implements TgtShip, TypeHasHayRake
//     {
//
//         public HMSColossusCV()
//         {
//         }
//
//         public HMSColossusCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class HokokuMaru extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public HokokuMaru()
//         {
//         }
//
//         public HokokuMaru(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Holmlea extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Holmlea()
//         {
//         }
//
//         public Holmlea(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IIbfinland extends ShipGeneric
//         implements TgtShip
//     {
//
//         public IIbfinland()
//         {
//         }
//
//         public IIbfinland(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IIbfinlandP extends ShipGeneric
//         implements TgtShip
//     {
//
//         public IIbfinlandP()
//         {
//         }
//
//         public IIbfinlandP(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IICU3 extends ShipGeneric
//         implements TgtShip
//     {
//
//         public IICU3()
//         {
//         }
//
//         public IICU3(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IICU3P extends ShipGeneric
//         implements TgtShip
//     {
//
//         public IICU3P()
//         {
//         }
//
//         public IICU3P(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IICU12 extends ShipGeneric
//         implements TgtShip
//     {
//
//         public IICU12()
//         {
//         }
//
//         public IICU12(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IICU12P extends ShipGeneric
//         implements TgtShip
//     {
//
//         public IICU12P()
//         {
//         }
//
//         public IICU12P(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IICU57 extends ShipGeneric
//         implements TgtShip
//     {
//
//         public IICU57()
//         {
//         }
//
//         public IICU57(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IICU57P extends ShipGeneric
//         implements TgtShip
//     {
//
//         public IICU57P()
//         {
//         }
//
//         public IICU57P(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Janssens extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Janssens()
//         {
//         }
//
//         public Janssens(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class KMHansa extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public KMHansa()
//         {
//         }
//
//         public KMHansa(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class LSHuddell extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public LSHuddell()
//         {
//         }
//
//         public LSHuddell(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class LSSimonBenson extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public LSSimonBenson()
//         {
//         }
//
//         public LSSimonBenson(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class MalaysianQueen extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public MalaysianQueen()
//         {
//         }
//
//         public MalaysianQueen(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Mapito extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Mapito()
//         {
//         }
//
//         public Mapito(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Mararoa extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Mararoa()
//         {
//         }
//
//         public Mararoa(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class MSAtenfels extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public MSAtenfels()
//         {
//         }
//
//         public MSAtenfels(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class RedCanyon extends BigshipGeneric implements TgtShip {

        public RedCanyon() {
        }

        public RedCanyon(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class SeawayPrincess extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public SeawayPrincess()
//         {
//         }
//
//         public SeawayPrincess(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class SetsuMaru extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public SetsuMaru()
//         {
//         }
//
//         public SetsuMaru(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Speedwell extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Speedwell()
//         {
//         }
//
//         public Speedwell(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Sumatra extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Sumatra()
//         {
//         }
//
//         public Sumatra(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class TakatsuMaru extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public TakatsuMaru()
//         {
//         }
//
//         public TakatsuMaru(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSBesugo_Srf extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public USSBesugo_Srf()
//         {
//         }
//
//         public USSBesugo_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSBesugo_Sub extends ShipGeneric
//         implements TgtShip
//     {
//
//         public USSBesugo_Sub()
//         {
//         }
//
//         public USSBesugo_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSNautilus_Srf extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public USSNautilus_Srf()
//         {
//         }
//
//         public USSNautilus_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSNautilus_Sub extends ShipGeneric
//         implements TgtShip
//     {
//
//         public USSNautilus_Sub()
//         {
//         }
//
//         public USSNautilus_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSPermit_Srf extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public USSPermit_Srf()
//         {
//         }
//
//         public USSPermit_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSPermit_Sub extends ShipGeneric
//         implements TgtShip
//     {
//
//         public USSPermit_Sub()
//         {
//         }
//
//         public USSPermit_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSSpot_Srf extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public USSSpot_Srf()
//         {
//         }
//
//         public USSSpot_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSSpot_Sub extends ShipGeneric
//         implements TgtShip
//     {
//
//         public USSSpot_Sub()
//         {
//         }
//
//         public USSSpot_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICRO500_Srf extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public VIICRO500_Srf()
//         {
//         }
//
//         public VIICRO500_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICRO500_Sub extends ShipGeneric
//         implements TgtShip
//     {
//
//         public VIICRO500_Sub()
//         {
//         }
//
//         public VIICRO500_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICswordfish_Srf extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public VIICswordfish_Srf()
//         {
//         }
//
//         public VIICswordfish_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICswordfish_Sub extends ShipGeneric
//         implements TgtShip
//     {
//
//         public VIICswordfish_Sub()
//         {
//         }
//
//         public VIICswordfish_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICU124_Srf extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public VIICU124_Srf()
//         {
//         }
//
//         public VIICU124_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICU124_Sub extends ShipGeneric
//         implements TgtShip
//     {
//
//         public VIICU124_Sub()
//         {
//         }
//
//         public VIICU124_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICU564_Srf extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public VIICU564_Srf()
//         {
//         }
//
//         public VIICU564_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICU564_Sub extends ShipGeneric
//         implements TgtShip
//     {
//
//         public VIICU564_Sub()
//         {
//         }
//
//         public VIICU564_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICwinter_Srf extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public VIICwinter_Srf()
//         {
//         }
//
//         public VIICwinter_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class VIICwinter_Sub extends ShipGeneric
//         implements TgtShip
//     {
//
//         public VIICwinter_Sub()
//         {
//         }
//
//         public VIICwinter_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class WhiteRiver extends BigshipGeneric implements TgtShip {

        public WhiteRiver() {
        }

        public WhiteRiver(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class Zephyr extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Zephyr()
//         {
//         }
//
//         public Zephyr(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class USSYorktownCV10_1944 extends USSEssexCV9 implements TgtShip {

        public USSYorktownCV10_1944() {
        }

        public USSYorktownCV10_1944(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSWaspCV18_1944 extends USSEssexCV9 implements TgtShip {

        public USSWaspCV18_1944() {
        }

        public USSWaspCV18_1944(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSTiconderogaCV14_1944 extends USSEssexCV9 implements TgtShip {

        public USSTiconderogaCV14_1944() {
        }

        public USSTiconderogaCV14_1944(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSLexingtonCV16 extends USSEssexCV9 implements TgtShip {

        public USSLexingtonCV16() {
        }

        public USSLexingtonCV16(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSIntrepidCV11_1944 extends USSEssexCV9 implements TgtShip {

        public USSIntrepidCV11_1944() {
        }

        public USSIntrepidCV11_1944(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSHornetCV12_1944 extends USSEssexCV9 implements TgtShip {

        public USSHornetCV12_1944() {
        }

        public USSHornetCV12_1944(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSHancockCV19_1944 extends USSEssexCV9 implements TgtShip {

        public USSHancockCV19_1944() {
        }

        public USSHancockCV19_1944(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSGenericCV9ClassMS21 extends USSEssexCV9 implements TgtShip {

        public USSGenericCV9ClassMS21() {
        }

        public USSGenericCV9ClassMS21(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSFranklinCV13_1944 extends USSEssexCV9 implements TgtShip {

        public USSFranklinCV13_1944() {
        }

        public USSFranklinCV13_1944(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSFranklinCV13_1943 extends USSEssexCV9 implements TgtShip {

        public USSFranklinCV13_1943() {
        }

        public USSFranklinCV13_1943(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSBunkerHillCV17_1944 extends USSEssexCV9 implements TgtShip {

        public USSBunkerHillCV17_1944() {
        }

        public USSBunkerHillCV17_1944(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Littorio extends BigshipGeneric implements TgtShip {

        public Littorio() {
        }

        public Littorio(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSTartar extends BigshipGeneric implements TgtShip {

        public HMSTartar() {
        }

        public HMSTartar(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
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
    public static class Sub_Coldwar extends BigshipGeneric implements TgtShip {

        public Sub_Coldwar() {
        }

        public Sub_Coldwar(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class GrafZeppelin extends BigshipGeneric implements TgtShip {

        public GrafZeppelin() {
        }

        public GrafZeppelin(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class PeterStrasser extends BigshipGeneric implements TgtShip {

        public PeterStrasser() {
        }

        public PeterStrasser(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Aquila extends BigshipGeneric implements TgtShip {

        public Aquila() {
        }

        public Aquila(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSPortlandCA33 extends BigshipGeneric implements TgtShip {

        public USSPortlandCA33() {
        }

        public USSPortlandCA33(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSChesterCA27 extends BigshipGeneric implements TgtShip {

        public USSChesterCA27() {
        }

        public USSChesterCA27(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSHoustonCA30 extends BigshipGeneric implements TgtShip {

        public USSHoustonCA30() {
        }

        public USSHoustonCA30(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSYorktownCV5_42 extends BigshipGeneric implements TgtShip {

        public USSYorktownCV5_42() {
        }

        public USSYorktownCV5_42(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSIowaBB61 extends BigshipGeneric implements TgtShip {

        public USSIowaBB61() {
        }

        public USSIowaBB61(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSEnterpriseCV6_42 extends BigshipGeneric implements TgtShip {

        public USSEnterpriseCV6_42() {
        }

        public USSEnterpriseCV6_42(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class USSBoxerCV21_51 extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public USSBoxerCV21_51()
//         {
//         }
//
//         public USSBoxerCV21_51(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSBoxerCV21 extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public USSBoxerCV21()
//         {
//         }
//
//         public USSBoxerCV21(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSValleyForgeCV45 extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public USSValleyForgeCV45()
//         {
//         }
//
//         public USSValleyForgeCV45(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IJNRyujoCV extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public IJNRyujoCV()
//         {
//         }
//
//         public IJNRyujoCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Factory1 extends BigshipGeneric
//         implements TgtShip, TgtFactory
//     {
//
//         public Factory1()
//         {
//         }
//
//         public Factory1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Factory1Big extends BigshipGeneric
//         implements TgtShip, TgtFactory
//     {
//
//         public Factory1Big()
//         {
//         }
//
//         public Factory1Big(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Factory2Big extends BigshipGeneric
//         implements TgtShip, TgtFactory
//     {
//
//         public Factory2Big()
//         {
//         }
//
//         public Factory2Big(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Factory3Big extends BigshipGeneric
//         implements TgtShip, TgtFactory
//     {
//
//         public Factory3Big()
//         {
//         }
//
//         public Factory3Big(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class SeaPlaneTender extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public SeaPlaneTender()
//         {
//         }
//
//         public SeaPlaneTender(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class USSNoaDD343 extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public USSNoaDD343()
//         {
//         }
//
//         public USSNoaDD343(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class IJNYamato extends BigshipGeneric
//         implements TgtShip, TypeRadar
//     {
//
//         public float getRange()
//         {
//             return Range;
//         }
//
//         public int getType()
//         {
//             return Type;
//         }
//
//         private float Range;
//         private int Type;
//
//         public IJNYamato()
//         {
//             Range = 25000F;
//             Type = 1;
//         }
//
//         public IJNYamato(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//             Range = 25000F;
//             Type = 1;
//         }
//     }
//
    public static class IJNCVLGeneric extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasBeacon {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public IJNCVLGeneric() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public IJNCVLGeneric(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

//     public static class IJNChitoseCVL extends BigshipGeneric
//         implements TgtShip, TypeRadar, TypeHasBeacon
//     {
//
//         public float getRange()
//         {
//             return Range;
//         }
//
//         public int getType()
//         {
//             return Type;
//         }
//
//         private float Range;
//         private int Type;
//
//         public IJNChitoseCVL()
//         {
//             Range = 25000F;
//             Type = 1;
//         }
//
//         public IJNChitoseCVL(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//             Range = 25000F;
//             Type = 1;
//         }
//     }
//
    public static class IJNKumaCL extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasBeacon {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public IJNKumaCL() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public IJNKumaCL(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

//     public static class IJNZuihoCVL extends BigshipGeneric
//         implements TgtShip, TypeRadar, TypeHasBeacon
//     {
//
//         public float getRange()
//         {
//             return Range;
//         }
//
//         public int getType()
//         {
//             return Type;
//         }
//
//         private float Range;
//         private int Type;
//
//         public IJNZuihoCVL()
//         {
//             Range = 25000F;
//             Type = 1;
//         }
//
//         public IJNZuihoCVL(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//             Range = 25000F;
//             Type = 1;
//         }
//     }
//
//     public static class IJNUnryuCV extends BigshipGeneric
//         implements TgtShip, TypeRadar, TypeHasBeacon
//     {
//
//         public float getRange()
//         {
//             return Range;
//         }
//
//         public int getType()
//         {
//             return Type;
//         }
//
//         private float Range;
//         private int Type;
//
//         public IJNUnryuCV()
//         {
//             Range = 100000F;
//             Type = 0;
//         }
//
//         public IJNUnryuCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//             Range = 100000F;
//             Type = 0;
//         }
//     }
//
    public static class IJNHiryuCV extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasBeacon {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public IJNHiryuCV() {
            this.Range = 100000F;
            this.Type = 0;
        }

        public IJNHiryuCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 100000F;
            this.Type = 0;
        }
    }

    public static class IJNKagaCV extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasBeacon {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public IJNKagaCV() {
            this.Range = 100000F;
            this.Type = 0;
        }

        public IJNKagaCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 100000F;
            this.Type = 0;
        }
    }

    public static class IJNSoryuCV extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasBeacon {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public IJNSoryuCV() {
            this.Range = 100000F;
            this.Type = 0;
        }

        public IJNSoryuCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 100000F;
            this.Type = 0;
        }
    }

    public static class Bismarck extends BigshipGeneric implements TgtShip, TypeRadar {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public Bismarck() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public Bismarck(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

    public static class HMSWarspite extends BigshipGeneric implements TgtShip, TypeRadar {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public HMSWarspite() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public HMSWarspite(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

    public static class USSColoradoBB45_41 extends BigshipGeneric implements TgtShip, TypeRadar {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public USSColoradoBB45_41() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public USSColoradoBB45_41(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

    public static class USSTennesseeBB43_41 extends BigshipGeneric implements TgtShip, TypeRadar {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public USSTennesseeBB43_41() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public USSTennesseeBB43_41(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

//     public static class USSLexingtonCV2_42 extends BigshipGeneric
//         implements TgtShip, TypeRadar, TypeHasHayRake
//     {
//
//         public float getRange()
//         {
//             return Range;
//         }
//
//         public int getType()
//         {
//             return Type;
//         }
//
//         private float Range;
//         private int Type;
//
//         public USSLexingtonCV2_42()
//         {
//             Range = 100000F;
//             Type = 0;
//         }
//
//         public USSLexingtonCV2_42(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//             Range = 100000F;
//             Type = 0;
//         }
//     }
//
    public static class USSPrincetonCVL23 extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasHayRake {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public USSPrincetonCVL23() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public USSPrincetonCVL23(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

    public static class USSBelleauWoodCVL24 extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasHayRake {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public USSBelleauWoodCVL24() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public USSBelleauWoodCVL24(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

    public static class USSSanJacintoCVL30 extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasHayRake {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public USSSanJacintoCVL30() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public USSSanJacintoCVL30(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

    public static class HMSNubian extends BigshipGeneric implements TgtShip {

        public HMSNubian() {
        }

        public HMSNubian(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSCossack extends BigshipGeneric implements TgtShip {

        public HMSCossack() {
        }

        public HMSCossack(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class HMSTribal extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public HMSTribal()
//         {
//         }
//
//         public HMSTribal(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
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
//     public static class DestroyerRN extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public DestroyerRN()
//         {
//         }
//
//         public DestroyerRN(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class HMSFiji extends BigshipGeneric implements TgtShip, TypeRadar {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public HMSFiji() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public HMSFiji(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

//     public static class RMCaioDuilioBB extends BigshipGeneric
//         implements TgtShip, TypeRadar
//     {
//
//         public float getRange()
//         {
//             return Range;
//         }
//
//         public int getType()
//         {
//             return Type;
//         }
//
//         private float Range;
//         private int Type;
//
//         public RMCaioDuilioBB()
//         {
//             Range = 25000F;
//             Type = 1;
//         }
//
//         public RMCaioDuilioBB(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//             Range = 25000F;
//             Type = 1;
//         }
//     }
//
    public static class Soldati extends BigshipGeneric implements TgtShip {

        public Soldati() {
        }

        public Soldati(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Italia0 extends BigshipGeneric implements TgtShip, TypeRadar {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public Italia0() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public Italia0(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

    public static class Italia1 extends BigshipGeneric implements TgtShip, TypeRadar {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public Italia1() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public Italia1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

    public static class TroopTrans0 extends BigshipGeneric implements TgtShip {

        public TroopTrans0() {
        }

        public TroopTrans0(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class TroopTrans1 extends BigshipGeneric implements TgtShip {

        public TroopTrans1() {
        }

        public TroopTrans1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class TroopTrans2 extends BigshipGeneric implements TgtShip {

        public TroopTrans2() {
        }

        public TroopTrans2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class TroopTrans3 extends BigshipGeneric implements TgtShip {

        public TroopTrans3() {
        }

        public TroopTrans3(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Transport4 extends BigshipGeneric implements TgtShip {

        public Transport4() {
        }

        public Transport4(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Transport5 extends BigshipGeneric implements TgtShip {

        public Transport5() {
        }

        public Transport5(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Transport6 extends BigshipGeneric implements TgtShip {

        public Transport6() {
        }

        public Transport6(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSMcKean extends BigshipGeneric implements TgtShip {

        public USSMcKean() {
        }

        public USSMcKean(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MFPT extends BigshipGeneric implements TgtShip {

        public MFPT() {
        }

        public MFPT(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MFPIT extends BigshipGeneric implements TgtShip {

        public MFPIT() {
        }

        public MFPIT(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Boat1 extends ShipGeneric implements TgtShip {

        public Boat1() {
        }

        public Boat1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Tanker0 extends BigshipGeneric implements TgtShip {

        public Tanker0() {
        }

        public Tanker0(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Tanker1 extends BigshipGeneric implements TgtShip {

        public Tanker1() {
        }

        public Tanker1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Tanker2 extends BigshipGeneric implements TgtShip {

        public Tanker2() {
        }

        public Tanker2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class TankerDmg extends BigshipGeneric implements TgtShip {

        public TankerDmg() {
        }

        public TankerDmg(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Destroyer0 extends BigshipGeneric implements TgtShip {

        public Destroyer0() {
        }

        public Destroyer0(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Destroyer1 extends BigshipGeneric implements TgtShip {

        public Destroyer1() {
        }

        public Destroyer1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Destroyer2 extends BigshipGeneric implements TgtShip {

        public Destroyer2() {
        }

        public Destroyer2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class DestroyerDmg extends BigshipGeneric implements TgtShip {

        public DestroyerDmg() {
        }

        public DestroyerDmg(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class DestroyerWreck extends BigshipGeneric implements TgtShip {

        public DestroyerWreck() {
        }

        public DestroyerWreck(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LCVPWreck extends ShipGeneric implements TgtShip {

        public LCVPWreck() {
        }

        public LCVPWreck(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class DLCWreck extends ShipGeneric implements TgtShip {

        public DLCWreck() {
        }

        public DLCWreck(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Boat extends ShipGeneric implements TgtShip {

        public Boat() {
        }

        public Boat(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSFormidableCV extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasHayRake {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public HMSFormidableCV() {
            this.Range = 100000F;
            this.Type = 0;
        }

        public HMSFormidableCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 100000F;
            this.Type = 0;
        }
    }

    public static class HMSIndomitableCV extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasHayRake {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public HMSIndomitableCV() {
            this.Range = 100000F;
            this.Type = 0;
        }

        public HMSIndomitableCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 100000F;
            this.Type = 0;
        }
    }

    public static class HMSEagle extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasHayRake {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public HMSEagle() {
            this.Range = 100000F;
            this.Type = 0;
        }

        public HMSEagle(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 100000F;
            this.Type = 0;
        }
    }

//     public static class Carrier0 extends BigshipGeneric
//         implements TgtShip, TypeRadar, TypeHasBeacon
//     {
//
//         public float getRange()
//         {
//             return Range;
//         }
//
//         public int getType()
//         {
//             return Type;
//         }
//
//         private float Range;
//         private int Type;
//
//         public Carrier0()
//         {
//             Range = 100000F;
//             Type = 1;
//         }
//
//         public Carrier0(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//             Range = 100000F;
//             Type = 1;
//         }
//     }
//
    public static class Carrier1 extends BigshipGeneric implements TgtShip, TypeRadar, TypeHasHayRake {

        public float getRange() {
            return this.Range;
        }

        public int getType() {
            return this.Type;
        }

        private float Range;
        private int   Type;

        public Carrier1() {
            this.Range = 25000F;
            this.Type = 1;
        }

        public Carrier1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
            this.Range = 25000F;
            this.Type = 1;
        }
    }

    public static class Fisherman2 extends ShipGeneric implements TgtShip {

        public Fisherman2() {
        }

        public Fisherman2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Fisherman1 extends ShipGeneric implements TgtShip {

        public Fisherman1() {
        }

        public Fisherman1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Fisherman extends ShipGeneric implements TgtShip {

        public Fisherman() {
        }

        public Fisherman(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Barge1 extends ShipGeneric implements TgtShip {

        public Barge1() {
        }

        public Barge1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Barge0 extends ShipGeneric implements TgtShip {

        public Barge0() {
        }

        public Barge0(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HospitalShip extends BigshipGeneric implements TgtShip {

        public HospitalShip() {
        }

        public HospitalShip(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Transport1 extends BigshipGeneric implements TgtShip {

        public Transport1() {
        }

        public Transport1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Transport2 extends BigshipGeneric implements TgtShip {

        public Transport2() {
        }

        public Transport2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Transport3 extends BigshipGeneric implements TgtShip {

        public Transport3() {
        }

        public Transport3(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Transport7 extends BigshipGeneric implements TgtShip {

        public Transport7() {
        }

        public Transport7(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class TransWreck extends BigshipGeneric implements TgtShip {

        public TransWreck() {
        }

        public TransWreck(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class TransDmg extends BigshipGeneric implements TgtShip {

        public TransDmg() {
        }

        public TransDmg(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class SmallWreck extends ShipGeneric implements TgtShip {

        public SmallWreck() {
        }

        public SmallWreck(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class PaddleSteamer extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public PaddleSteamer()
//         {
//         }
//
//         public PaddleSteamer(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Gunboat2 extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Gunboat2()
//         {
//         }
//
//         public Gunboat2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class Gunboat1 extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public Gunboat1()
//         {
//         }
//
//         public Gunboat1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class MAS501JP extends BigshipGeneric implements TgtShip {

        public MAS501JP() {
        }

        public MAS501JP(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MAS501UNE extends BigshipGeneric implements TgtShip {

        public MAS501UNE() {
        }

        public MAS501UNE(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MAS501UNP extends BigshipGeneric implements TgtShip {

        public MAS501UNP() {
        }

        public MAS501UNP(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MAS501RN extends BigshipGeneric implements TgtShip {

        public MAS501RN() {
        }

        public MAS501RN(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class DestroyerZ88 extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public DestroyerZ88()
//         {
//         }
//
//         public DestroyerZ88(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
//     public static class DestroyerKM extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public DestroyerKM()
//         {
//         }
//
//         public DestroyerKM(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class Icebreaker_1 extends BigshipGeneric implements TgtShip {

        public Icebreaker_1() {
        }

        public Icebreaker_1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Hospital_2 extends BigshipGeneric implements TgtShip {

        public Hospital_2() {
        }

        public Hospital_2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LSD_1 extends BigshipGeneric implements TgtShip {

        public LSD_1() {
        }

        public LSD_1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LST_3 extends BigshipGeneric implements TgtShip {

        public LST_3() {
        }

        public LST_3(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LST_1 extends BigshipGeneric implements TgtShip {

        public LST_1() {
        }

        public LST_1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LCVP_2 extends ShipGeneric implements TgtShip {

        public LCVP_2() {
        }

        public LCVP_2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LCVP_1 extends ShipGeneric implements TgtShip {

        public LCVP_1() {
        }

        public LCVP_1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LCM_3 extends ShipGeneric implements TgtShip {

        public LCM_3() {
        }

        public LCM_3(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LCM_2 extends ShipGeneric implements TgtShip {

        public LCM_2() {
        }

        public LCM_2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LCM_1 extends ShipGeneric implements TgtShip {

        public LCM_1() {
        }

        public LCM_1(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class HMSFlowerCorvette extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public HMSFlowerCorvette()
//         {
//         }
//
//         public HMSFlowerCorvette(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class ItalGabbianoCorvette extends BigshipGeneric implements TgtShip {

        public ItalGabbianoCorvette() {
        }

        public ItalGabbianoCorvette(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class ItalTuffetoCorvetteKM extends BigshipGeneric implements TgtShip {

        public ItalTuffetoCorvetteKM() {
        }

        public ItalTuffetoCorvetteKM(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNShigaEscort extends BigshipGeneric implements TgtShip {

        public IJNShigaEscort() {
        }

        public IJNShigaEscort(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNCDD extends BigshipGeneric implements TgtShip {

        public IJNCDD() {
        }

        public IJNCDD(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class G5 extends BigshipGeneric implements TgtShip {

        public G5() {
        }

        public G5(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MO4 extends BigshipGeneric implements TgtShip {

        public MO4() {
        }

        public MO4(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class BBK_1942 extends BigshipGeneric implements TgtShip {

        public BBK_1942() {
        }

        public BBK_1942(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class BBK1124_1943 extends BigshipGeneric implements TgtShip {

        public BBK1124_1943() {
        }

        public BBK1124_1943(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Destroyer_USSR_Type7 extends BigshipGeneric implements TgtShip {

        public Destroyer_USSR_Type7() {
        }

        public Destroyer_USSR_Type7(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Destroyer_USSR_Type7_44 extends BigshipGeneric implements TgtShip {

        public Destroyer_USSR_Type7_44() {
        }

        public Destroyer_USSR_Type7_44(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Tral extends BigshipGeneric implements TgtShip {

        public Tral() {
        }

        public Tral(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Shuka extends ShipGeneric implements TgtShip {

        public Shuka() {
        }

        public Shuka(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class ShukaP extends BigshipGeneric implements TgtShip {

        public ShukaP() {
        }

        public ShukaP(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Aurora extends BigshipGeneric implements TgtShip {

        public Aurora() {
        }

        public Aurora(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Marat extends BigshipGeneric implements TgtShip {

        public Marat() {
        }

        public Marat(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Kirov extends BigshipGeneric implements TgtShip {

        public Kirov() {
        }

        public Kirov(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Tashkent extends BigshipGeneric implements TgtShip {

        public Tashkent() {
        }

        public Tashkent(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Tramp extends BigshipGeneric implements TgtShip {

        public Tramp() {
        }

        public Tramp(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Tanker extends BigshipGeneric implements TgtShip, TankerType {

        public Tanker() {
        }

        public Tanker(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSLexingtonCV2 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSLexingtonCV2() {
        }

        public USSLexingtonCV2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSSaratogaCV3 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSSaratogaCV3() {
        }

        public USSSaratogaCV3(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSCVGeneric extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSCVGeneric() {
        }

        public USSCVGeneric(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

//     public static class USSBBGeneric extends BigshipGeneric
//         implements TgtShip
//     {
//
//         public USSBBGeneric()
//         {
//         }
//
//         public USSBBGeneric(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2)
//         {
//             super(s, i, sectfile, s1, sectfile1, s2);
//         }
//     }
//
    public static class USSIndianapolisCA35 extends BigshipGeneric implements TgtShip {

        public USSIndianapolisCA35() {
        }

        public USSIndianapolisCA35(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LVT_2WAT extends ShipGeneric implements TgtShip {

        public LVT_2WAT() {
        }

        public LVT_2WAT(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class DUKW_WAT extends ShipGeneric implements TgtShip {

        public DUKW_WAT() {
        }

        public DUKW_WAT(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class LCVP extends ShipGeneric implements TgtShip {

        public LCVP() {
        }

        public LCVP(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSGreenlingSS213_Srf extends BigshipGeneric implements TgtShip {

        public USSGreenlingSS213_Srf() {
        }

        public USSGreenlingSS213_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSGreenlingSS213_Sub extends BigshipGeneric implements TgtShip {

        public USSGreenlingSS213_Sub() {
        }

        public USSGreenlingSS213_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSGatoSS212_Srf extends BigshipGeneric implements TgtShip {

        public USSGatoSS212_Srf() {
        }

        public USSGatoSS212_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSGatoSS212_Sub extends BigshipGeneric implements TgtShip {

        public USSGatoSS212_Sub() {
        }

        public USSGatoSS212_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSWardDD139 extends BigshipGeneric implements TgtShip {

        public USSWardDD139() {
        }

        public USSWardDD139(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSDentDD116 extends BigshipGeneric implements TgtShip {

        public USSDentDD116() {
        }

        public USSDentDD116(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSFletcherDD445 extends BigshipGeneric implements TgtShip {

        public USSFletcherDD445() {
        }

        public USSFletcherDD445(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSOBannonDD450 extends BigshipGeneric implements TgtShip {

        public USSOBannonDD450() {
        }

        public USSOBannonDD450(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSKiddDD661 extends BigshipGeneric implements TgtShip {

        public USSKiddDD661() {
        }

        public USSKiddDD661(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSCasablancaCVE55 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSCasablancaCVE55() {
        }

        public USSCasablancaCVE55(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSKitkunBayCVE71 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSKitkunBayCVE71() {
        }

        public USSKitkunBayCVE71(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSShamrockBayCVE84 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSShamrockBayCVE84() {
        }

        public USSShamrockBayCVE84(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSEssexCV9 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSEssexCV9() {
        }

        public USSEssexCV9(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class USSIntrepidCV11 extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public USSIntrepidCV11() {
        }

        public USSIntrepidCV11(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class PilotWater_US extends ShipGeneric implements WeakBody {

        public PilotWater_US() {
        }

        public PilotWater_US(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class PilotBoatWater_US extends ShipGeneric implements WeakBody {

        public PilotBoatWater_US() {
        }

        public PilotBoatWater_US(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Norw_Ship_Stord extends BigshipGeneric implements TgtShip {

        public Norw_Ship_Stord() {
        }

        public Norw_Ship_Stord(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSIllustriousCV extends BigshipGeneric implements TgtShip, TypeHasHayRake {

        public HMSIllustriousCV() {
        }

        public HMSIllustriousCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSPoWBB extends BigshipGeneric implements TgtShip {

        public HMSPoWBB() {
        }

        public HMSPoWBB(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSKingGeorgeVBB extends BigshipGeneric implements TgtShip {

        public HMSKingGeorgeVBB() {
        }

        public HMSKingGeorgeVBB(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class HMSDukeOfYorkBB extends BigshipGeneric implements TgtShip {

        public HMSDukeOfYorkBB() {
        }

        public HMSDukeOfYorkBB(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class S80 extends BigshipGeneric implements TgtShip {

        public S80() {
        }

        public S80(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MFP extends BigshipGeneric implements TgtShip {

        public MFP() {
        }

        public MFP(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MFP2 extends BigshipGeneric implements TgtShip {

        public MFP2() {
        }

        public MFP2(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MAS501 extends BigshipGeneric implements TgtShip {

        public MAS501() {
        }

        public MAS501(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Murgesku extends BigshipGeneric implements TgtShip {

        public Murgesku() {
        }

        public Murgesku(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class MBoat extends BigshipGeneric implements TgtShip {

        public MBoat() {
        }

        public MBoat(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Submarine extends ShipGeneric implements TgtShip {

        public Submarine() {
        }

        public Submarine(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class SubmarineP extends BigshipGeneric implements TgtShip {

        public SubmarineP() {
        }

        public SubmarineP(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Niobe extends BigshipGeneric implements TgtShip {

        public Niobe() {
        }

        public Niobe(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class NiobeWithBeacon extends BigshipGeneric implements TgtShip, TypeHasBeacon {

        public NiobeWithBeacon() {
        }

        public NiobeWithBeacon(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class SubTypeVIIC_Srf extends BigshipGeneric implements TgtShip {

        public SubTypeVIIC_Srf() {
        }

        public SubTypeVIIC_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class SubTypeVIIC_SrfWithBeacon extends BigshipGeneric implements TgtShip, TypeHasBeacon {

        public SubTypeVIIC_SrfWithBeacon() {
        }

        public SubTypeVIIC_SrfWithBeacon(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class SubTypeVIIC_Sub extends BigshipGeneric implements TgtShip {

        public SubTypeVIIC_Sub() {
        }

        public SubTypeVIIC_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class SubTypeVIIB_Srf extends BigshipGeneric implements TgtShip {

        public SubTypeVIIB_Srf() {
        }

        public SubTypeVIIB_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class SubTypeVIIB_Sub extends BigshipGeneric implements TgtShip {

        public SubTypeVIIB_Sub() {
        }

        public SubTypeVIIB_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class SubTypeIXB_Srf extends BigshipGeneric implements TgtShip {

        public SubTypeIXB_Srf() {
        }

        public SubTypeIXB_Srf(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class SubTypeIXB_Sub extends BigshipGeneric implements TgtShip {

        public SubTypeIXB_Sub() {
        }

        public SubTypeIXB_Sub(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Illmarinen extends BigshipGeneric implements TgtShip {

        public Illmarinen() {
        }

        public Illmarinen(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Vainamoinen extends BigshipGeneric implements TgtShip {

        public Vainamoinen() {
        }

        public Vainamoinen(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class Tirpitz extends BigshipGeneric implements TgtShip {

        public Tirpitz() {
        }

        public Tirpitz(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class PAM extends BigshipGeneric implements TgtShip {

        public PAM() {
        }

        public PAM(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNAkagiCV extends BigshipGeneric implements TgtShip, TypeHasBeacon {

        public IJNAkagiCV() {
        }

        public IJNAkagiCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNShokakuCV extends BigshipGeneric implements TgtShip, TypeHasBeacon {

        public IJNShokakuCV() {
        }

        public IJNShokakuCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNZuikakuCV extends BigshipGeneric implements TgtShip, TypeHasBeacon {

        public IJNZuikakuCV() {
        }

        public IJNZuikakuCV(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNCVGeneric extends BigshipGeneric implements TgtShip, TypeHasBeacon {

        public IJNCVGeneric() {
        }

        public IJNCVGeneric(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNBBGeneric extends BigshipGeneric implements TgtShip {

        public IJNBBGeneric() {
        }

        public IJNBBGeneric(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNAkizukiDD42 extends BigshipGeneric implements TgtShip {

        public IJNAkizukiDD42() {
        }

        public IJNAkizukiDD42(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNAmatsukazeDD41 extends BigshipGeneric implements TgtShip {

        public IJNAmatsukazeDD41() {
        }

        public IJNAmatsukazeDD41(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNArashiDD41 extends BigshipGeneric implements TgtShip {

        public IJNArashiDD41() {
        }

        public IJNArashiDD41(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNKageroDD41 extends BigshipGeneric implements TgtShip {

        public IJNKageroDD41() {
        }

        public IJNKageroDD41(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNNowakiDD41 extends BigshipGeneric implements TgtShip {

        public IJNNowakiDD41() {
        }

        public IJNNowakiDD41(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNYukikazeDD41 extends BigshipGeneric implements TgtShip {

        public IJNYukikazeDD41() {
        }

        public IJNYukikazeDD41(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNAmatsukazeDD43 extends BigshipGeneric implements TgtShip {

        public IJNAmatsukazeDD43() {
        }

        public IJNAmatsukazeDD43(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNAmatsukazeDD43WithBeacon extends BigshipGeneric implements TgtShip, TypeHasBeacon {

        public IJNAmatsukazeDD43WithBeacon() {
        }

        public IJNAmatsukazeDD43WithBeacon(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNNowakiDD43 extends BigshipGeneric implements TgtShip {

        public IJNNowakiDD43() {
        }

        public IJNNowakiDD43(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNYukikazeDD43 extends BigshipGeneric implements TgtShip {

        public IJNYukikazeDD43() {
        }

        public IJNYukikazeDD43(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNAmatsukazeDD45 extends BigshipGeneric implements TgtShip {

        public IJNAmatsukazeDD45() {
        }

        public IJNAmatsukazeDD45(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNYukikazeDD45 extends BigshipGeneric implements TgtShip {

        public IJNYukikazeDD45() {
        }

        public IJNYukikazeDD45(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNFishJunk extends BigshipGeneric implements TgtShip {

        public IJNFishJunk() {
        }

        public IJNFishJunk(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class IJNFishJunkA extends BigshipGeneric implements TgtShip {

        public IJNFishJunkA() {
        }

        public IJNFishJunkA(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class DaihatsuLC extends ShipGeneric implements TgtShip {

        public DaihatsuLC() {
        }

        public DaihatsuLC(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class PilotWater_JA extends ShipGeneric implements WeakBody {

        public PilotWater_JA() {
        }

        public PilotWater_JA(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class RwyCon extends BigshipGeneric implements TgtShip, TestRunway {

        public RwyCon() {
        }

        public RwyCon(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class RwySteel extends BigshipGeneric implements TgtShip, TestRunway {

        public RwySteel() {
        }

        public RwySteel(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class RwySteelLow extends BigshipGeneric implements TgtShip, TestRunway {

        public RwySteelLow() {
        }

        public RwySteelLow(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class RwyTransp extends BigshipGeneric implements TgtShip, TestRunway, TransparentTestRunway {

        public RwyTransp() {
        }

        public RwyTransp(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class RwyTranspWide extends BigshipGeneric implements TgtShip, TestRunway, TransparentTestRunway {

        public RwyTranspWide() {
        }

        public RwyTranspWide(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static class RwyTranspSqr extends BigshipGeneric implements TgtShip, TestRunway, TransparentTestRunway {

        public RwyTranspSqr() {
        }

        public RwyTranspSqr(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
            super(s, i, sectfile, s1, sectfile1, s2);
        }
    }

    public static final int AIR_WARNING  = 0;
    public static final int FIRE_CONTROL = 1;

    static {
        new BigshipGeneric.SPAWN(G5.class);
        new BigshipGeneric.SPAWN(MO4.class);
        new BigshipGeneric.SPAWN(BBK_1942.class);
        new BigshipGeneric.SPAWN(BBK1124_1943.class);
        new BigshipGeneric.SPAWN(Destroyer_USSR_Type7.class);
        new BigshipGeneric.SPAWN(Destroyer_USSR_Type7_44.class);
        new BigshipGeneric.SPAWN(Tral.class);
        new ShipGeneric.SPAWN(Shuka.class);
        new BigshipGeneric.SPAWN(ShukaP.class);
        new BigshipGeneric.SPAWN(Aurora.class);
        new BigshipGeneric.SPAWN(Marat.class);
        new BigshipGeneric.SPAWN(Kirov.class);
        new BigshipGeneric.SPAWN(Tashkent.class);
        new BigshipGeneric.SPAWN(Tramp.class);
        new BigshipGeneric.SPAWN(Tanker.class);
        new BigshipGeneric.SPAWN(USSLexingtonCV2.class);
        new BigshipGeneric.SPAWN(USSSaratogaCV3.class);
        new BigshipGeneric.SPAWN(USSCVGeneric.class);
//        new BigshipGeneric.SPAWN(USSBBGeneric.class);
        new BigshipGeneric.SPAWN(USSIndianapolisCA35.class);
        new ShipGeneric.SPAWN(LVT_2WAT.class);
        new ShipGeneric.SPAWN(DUKW_WAT.class);
        new ShipGeneric.SPAWN(LCVP.class);
        new BigshipGeneric.SPAWN(USSGreenlingSS213_Srf.class);
        new BigshipGeneric.SPAWN(USSGreenlingSS213_Sub.class);
        new BigshipGeneric.SPAWN(USSGatoSS212_Srf.class);
        new BigshipGeneric.SPAWN(USSGatoSS212_Sub.class);
        new BigshipGeneric.SPAWN(USSWardDD139.class);
        new BigshipGeneric.SPAWN(USSDentDD116.class);
        new BigshipGeneric.SPAWN(USSFletcherDD445.class);
        new BigshipGeneric.SPAWN(USSOBannonDD450.class);
        new BigshipGeneric.SPAWN(USSKiddDD661.class);
        new BigshipGeneric.SPAWN(USSCasablancaCVE55.class);
        new BigshipGeneric.SPAWN(USSKitkunBayCVE71.class);
        new BigshipGeneric.SPAWN(USSShamrockBayCVE84.class);
        new BigshipGeneric.SPAWN(USSEssexCV9.class);
        new BigshipGeneric.SPAWN(USSIntrepidCV11.class);
        new ShipGeneric.SPAWN(PilotWater_US.class);
        new ShipGeneric.SPAWN(PilotBoatWater_US.class);
        new BigshipGeneric.SPAWN(Norw_Ship_Stord.class);
        new BigshipGeneric.SPAWN(HMSIllustriousCV.class);
        new BigshipGeneric.SPAWN(HMSPoWBB.class);
        new BigshipGeneric.SPAWN(HMSKingGeorgeVBB.class);
        new BigshipGeneric.SPAWN(HMSDukeOfYorkBB.class);
        new BigshipGeneric.SPAWN(S80.class);
        new BigshipGeneric.SPAWN(MFP.class);
        new BigshipGeneric.SPAWN(MFP2.class);
        new BigshipGeneric.SPAWN(MAS501.class);
        new BigshipGeneric.SPAWN(Murgesku.class);
        new BigshipGeneric.SPAWN(MBoat.class);
        new ShipGeneric.SPAWN(Submarine.class);
        new BigshipGeneric.SPAWN(SubmarineP.class);
        new BigshipGeneric.SPAWN(Niobe.class);
        new BigshipGeneric.SPAWN(NiobeWithBeacon.class);
        new BigshipGeneric.SPAWN(SubTypeVIIC_Srf.class);
        new BigshipGeneric.SPAWN(SubTypeVIIC_SrfWithBeacon.class);
        new BigshipGeneric.SPAWN(SubTypeVIIC_Sub.class);
        new BigshipGeneric.SPAWN(SubTypeVIIB_Srf.class);
        new BigshipGeneric.SPAWN(SubTypeVIIB_Sub.class);
        new BigshipGeneric.SPAWN(SubTypeIXB_Srf.class);
        new BigshipGeneric.SPAWN(SubTypeIXB_Sub.class);
        new BigshipGeneric.SPAWN(Illmarinen.class);
        new BigshipGeneric.SPAWN(Vainamoinen.class);
        new BigshipGeneric.SPAWN(Tirpitz.class);
        new BigshipGeneric.SPAWN(PAM.class);
        new BigshipGeneric.SPAWN(IJNAkagiCV.class);
        new BigshipGeneric.SPAWN(IJNShokakuCV.class);
        new BigshipGeneric.SPAWN(IJNZuikakuCV.class);
        new BigshipGeneric.SPAWN(IJNCVGeneric.class);
        new BigshipGeneric.SPAWN(IJNBBGeneric.class);
        new BigshipGeneric.SPAWN(IJNAkizukiDD42.class);
        new BigshipGeneric.SPAWN(IJNAmatsukazeDD41.class);
        new BigshipGeneric.SPAWN(IJNArashiDD41.class);
        new BigshipGeneric.SPAWN(IJNKageroDD41.class);
        new BigshipGeneric.SPAWN(IJNNowakiDD41.class);
        new BigshipGeneric.SPAWN(IJNYukikazeDD41.class);
        new BigshipGeneric.SPAWN(IJNAmatsukazeDD43.class);
        new BigshipGeneric.SPAWN(IJNAmatsukazeDD43WithBeacon.class);
        new BigshipGeneric.SPAWN(IJNNowakiDD43.class);
        new BigshipGeneric.SPAWN(IJNYukikazeDD43.class);
        new BigshipGeneric.SPAWN(IJNAmatsukazeDD45.class);
        new BigshipGeneric.SPAWN(IJNYukikazeDD45.class);
        new BigshipGeneric.SPAWN(IJNFishJunk.class);
        new BigshipGeneric.SPAWN(IJNFishJunkA.class);
        new ShipGeneric.SPAWN(DaihatsuLC.class);
        new ShipGeneric.SPAWN(PilotWater_JA.class);
        new BigshipGeneric.SPAWN(RwyCon.class);
        new BigshipGeneric.SPAWN(RwySteel.class);
        new BigshipGeneric.SPAWN(RwySteelLow.class);
        new BigshipGeneric.SPAWN(RwyTransp.class);
        new BigshipGeneric.SPAWN(RwyTranspWide.class);
        new BigshipGeneric.SPAWN(RwyTranspSqr.class);
//        new BigshipGeneric.SPAWN(Apanui.class);
        new BigshipGeneric.SPAWN(Aquila.class);
//        new BigshipGeneric.SPAWN(Awanui.class);
        new BigshipGeneric.SPAWN(Bismarck.class);
//        new BigshipGeneric.SPAWN(Carrier0.class);
        new BigshipGeneric.SPAWN(Carrier1.class);
        new BigshipGeneric.SPAWN(Destroyer0.class);
        new BigshipGeneric.SPAWN(Destroyer1.class);
        new BigshipGeneric.SPAWN(Destroyer2.class);
        new BigshipGeneric.SPAWN(DestroyerDmg.class);
//        new BigshipGeneric.SPAWN(DestroyerKM.class);
//        new BigshipGeneric.SPAWN(DestroyerRN.class);
        new BigshipGeneric.SPAWN(DestroyerWreck.class);
//        new BigshipGeneric.SPAWN(DestroyerZ88.class);
//        new BigshipGeneric.SPAWN(Factory1.class);
//        new BigshipGeneric.SPAWN(Factory1Big.class);
//        new BigshipGeneric.SPAWN(Factory2Big.class);
//        new BigshipGeneric.SPAWN(Factory3Big.class);
        new BigshipGeneric.SPAWN(FortGeorge.class);
//        new BigshipGeneric.SPAWN(germantrawlerA.class);
        new BigshipGeneric.SPAWN(GrafZeppelin.class);
//        new BigshipGeneric.SPAWN(Gunboat1.class);
//        new BigshipGeneric.SPAWN(Gunboat2.class);
//        new BigshipGeneric.SPAWN(HMASNapier.class);
//        new BigshipGeneric.SPAWN(HMSColossusCV.class);
        new BigshipGeneric.SPAWN(HMSCossack.class);
        new BigshipGeneric.SPAWN(HMSEagle.class);
        new BigshipGeneric.SPAWN(HMSFiji.class);
//        new BigshipGeneric.SPAWN(HMSFlowerCorvette.class);
        new BigshipGeneric.SPAWN(HMSFormidableCV.class);
        new BigshipGeneric.SPAWN(HMSIndomitableCV.class);
//        new BigshipGeneric.SPAWN(HMSJavelin.class);
//        new BigshipGeneric.SPAWN(HMSJupiter.class);
//        new BigshipGeneric.SPAWN(HMSKipling.class);
        new BigshipGeneric.SPAWN(HMSMatabele.class);
        new BigshipGeneric.SPAWN(HMSNubian.class);
        new BigshipGeneric.SPAWN(HMSTartar.class);
//        new BigshipGeneric.SPAWN(HMSTribal.class);
        new BigshipGeneric.SPAWN(HMSWarspite.class);
//        new BigshipGeneric.SPAWN(HokokuMaru.class);
//        new BigshipGeneric.SPAWN(Holmlea.class);
        new BigshipGeneric.SPAWN(Hospital_2.class);
        new BigshipGeneric.SPAWN(HospitalShip.class);
        new BigshipGeneric.SPAWN(Icebreaker_1.class);
        new BigshipGeneric.SPAWN(IJNCDD.class);
//        new BigshipGeneric.SPAWN(IJNChitoseCVL.class);
//        new BigshipGeneric.SPAWN(IJNChitoseCVL.class);
//        new BigshipGeneric.SPAWN(IJNCVLGeneric.class);
        new BigshipGeneric.SPAWN(IJNCVLGeneric.class);
//        new BigshipGeneric.SPAWN(IJNHiryuCV.class);
        new BigshipGeneric.SPAWN(IJNHiryuCV.class);
//        new BigshipGeneric.SPAWN(IJNKagaCV.class);
        new BigshipGeneric.SPAWN(IJNKagaCV.class);
        new BigshipGeneric.SPAWN(IJNKumaCL.class);
//        new BigshipGeneric.SPAWN(IJNRyujoCV.class);
        new BigshipGeneric.SPAWN(IJNShigaEscort.class);
//        new BigshipGeneric.SPAWN(IJNSoryuCV.class);
        new BigshipGeneric.SPAWN(IJNSoryuCV.class);
//        new BigshipGeneric.SPAWN(IJNUnryuCV.class);
//        new BigshipGeneric.SPAWN(IJNYamato.class);
//        new BigshipGeneric.SPAWN(IJNZuihoCVL.class);
        new BigshipGeneric.SPAWN(ItalGabbianoCorvette.class);
        new BigshipGeneric.SPAWN(Italia0.class);
        new BigshipGeneric.SPAWN(Italia1.class);
        new BigshipGeneric.SPAWN(ItalTuffetoCorvetteKM.class);
//        new BigshipGeneric.SPAWN(Janssens.class);
//        new BigshipGeneric.SPAWN(KMHansa.class);
        new BigshipGeneric.SPAWN(Littorio.class);
        new BigshipGeneric.SPAWN(LSD_1.class);
//        new BigshipGeneric.SPAWN(LSHuddell.class);
//        new BigshipGeneric.SPAWN(LSSimonBenson.class);
        new BigshipGeneric.SPAWN(LST_1.class);
        new BigshipGeneric.SPAWN(LST_3.class);
//        new BigshipGeneric.SPAWN(MalaysianQueen.class);
//        new BigshipGeneric.SPAWN(Mapito.class);
//        new BigshipGeneric.SPAWN(Mararoa.class);
//        new BigshipGeneric.SPAWN(MAS501JP.class);
//        new BigshipGeneric.SPAWN(MAS501RN.class);
//        new BigshipGeneric.SPAWN(MAS501UNE.class);
//        new BigshipGeneric.SPAWN(MAS501UNP.class);
//        new BigshipGeneric.SPAWN(MFPIT.class);
//        new BigshipGeneric.SPAWN(MFPT.class);
//        new BigshipGeneric.SPAWN(MSAtenfels.class);
//        new BigshipGeneric.SPAWN(PaddleSteamer.class);
        new BigshipGeneric.SPAWN(PeterStrasser.class);
        new BigshipGeneric.SPAWN(RedCanyon.class);
//        new BigshipGeneric.SPAWN(RMCaioDuilioBB.class);
//        new BigshipGeneric.SPAWN(SeaPlaneTender.class);
//        new BigshipGeneric.SPAWN(SeawayPrincess.class);
//        new BigshipGeneric.SPAWN(SetsuMaru.class);
        new BigshipGeneric.SPAWN(Soldati.class);
//        new BigshipGeneric.SPAWN(Speedwell.class);
        new BigshipGeneric.SPAWN(Sub_Coldwar.class);
//        new BigshipGeneric.SPAWN(Sumatra.class);
//        new BigshipGeneric.SPAWN(TakatsuMaru.class);
        new BigshipGeneric.SPAWN(Tanker0.class);
        new BigshipGeneric.SPAWN(Tanker1.class);
        new BigshipGeneric.SPAWN(Tanker2.class);
        new BigshipGeneric.SPAWN(TankerDmg.class);
        new BigshipGeneric.SPAWN(TransDmg.class);
        new BigshipGeneric.SPAWN(Transport1.class);
        new BigshipGeneric.SPAWN(Transport2.class);
        new BigshipGeneric.SPAWN(Transport3.class);
        new BigshipGeneric.SPAWN(Transport4.class);
        new BigshipGeneric.SPAWN(Transport5.class);
        new BigshipGeneric.SPAWN(Transport6.class);
        new BigshipGeneric.SPAWN(Transport7.class);
        new BigshipGeneric.SPAWN(TransWreck.class);
        new BigshipGeneric.SPAWN(TroopTrans0.class);
        new BigshipGeneric.SPAWN(TroopTrans1.class);
        new BigshipGeneric.SPAWN(TroopTrans2.class);
        new BigshipGeneric.SPAWN(TroopTrans3.class);
        new BigshipGeneric.SPAWN(USSBelleauWoodCVL24.class);
//        new BigshipGeneric.SPAWN(USSBesugo_Srf.class);
//        new BigshipGeneric.SPAWN(USSBoxerCV21.class);
        new BigshipGeneric.SPAWN(USSBunkerHillCV17_1944.class);
        new BigshipGeneric.SPAWN(USSChesterCA27.class);
//        new BigshipGeneric.SPAWN(USSColoradoBB45_41.class);
        new BigshipGeneric.SPAWN(USSColoradoBB45_41.class);
        new BigshipGeneric.SPAWN(USSFranklinCV13_1943.class);
        new BigshipGeneric.SPAWN(USSFranklinCV13_1944.class);
        new BigshipGeneric.SPAWN(USSGenericCV9ClassMS21.class);
        new BigshipGeneric.SPAWN(USSHancockCV19_1944.class);
        new BigshipGeneric.SPAWN(USSHornetCV12_1944.class);
        new BigshipGeneric.SPAWN(USSHoustonCA30.class);
        new BigshipGeneric.SPAWN(USSIntrepidCV11_1944.class);
        new BigshipGeneric.SPAWN(USSLexingtonCV16.class);
//        new BigshipGeneric.SPAWN(USSLexingtonCV2_42.class);
//        new BigshipGeneric.SPAWN(USSLexingtonCV2_42.class);
        new BigshipGeneric.SPAWN(USSMcKean.class);
//        new BigshipGeneric.SPAWN(USSNautilus_Srf.class);
//        new BigshipGeneric.SPAWN(USSNoaDD343.class);
//        new BigshipGeneric.SPAWN(USSPermit_Srf.class);
        new BigshipGeneric.SPAWN(USSPortlandCA33.class);
        new BigshipGeneric.SPAWN(USSPrincetonCVL23.class);
        new BigshipGeneric.SPAWN(USSSanJacintoCVL30.class);
//        new BigshipGeneric.SPAWN(USSSpot_Srf.class);
//        new BigshipGeneric.SPAWN(USSTennesseeBB43_41.class);
        new BigshipGeneric.SPAWN(USSTennesseeBB43_41.class);
        new BigshipGeneric.SPAWN(USSTiconderogaCV14_1944.class);
//        new BigshipGeneric.SPAWN(USSValleyForgeCV45.class);
        new BigshipGeneric.SPAWN(USSWaspCV18_1944.class);
        new BigshipGeneric.SPAWN(USSYorktownCV10_1944.class);
//        new BigshipGeneric.SPAWN(VIICRO500_Srf.class);
//        new BigshipGeneric.SPAWN(VIICswordfish_Srf.class);
//        new BigshipGeneric.SPAWN(VIICU124_Srf.class);
//        new BigshipGeneric.SPAWN(VIICU564_Srf.class);
//        new BigshipGeneric.SPAWN(VIICwinter_Srf.class);
        new BigshipGeneric.SPAWN(WhiteRiver.class);
//        new BigshipGeneric.SPAWN(Zephyr.class);
        new ShipGeneric.SPAWN(Barge0.class);
        new ShipGeneric.SPAWN(Barge1.class);
        new ShipGeneric.SPAWN(Boat.class);
        new ShipGeneric.SPAWN(Boat1.class);
        new ShipGeneric.SPAWN(DLCWreck.class);
        new ShipGeneric.SPAWN(Fisherman.class);
        new ShipGeneric.SPAWN(Fisherman1.class);
        new ShipGeneric.SPAWN(Fisherman2.class);
//        new ShipGeneric.SPAWN(IIbfinland.class);
//        new ShipGeneric.SPAWN(IIbfinlandP.class);
//        new ShipGeneric.SPAWN(IICU12.class);
//        new ShipGeneric.SPAWN(IICU12P.class);
//        new ShipGeneric.SPAWN(IICU3.class);
//        new ShipGeneric.SPAWN(IICU3P.class);
//        new ShipGeneric.SPAWN(IICU57.class);
//        new ShipGeneric.SPAWN(IICU57P.class);
        new ShipGeneric.SPAWN(LCM_1.class);
        new ShipGeneric.SPAWN(LCM_2.class);
        new ShipGeneric.SPAWN(LCM_3.class);
        new ShipGeneric.SPAWN(LCVP_1.class);
        new ShipGeneric.SPAWN(LCVP_2.class);
        new ShipGeneric.SPAWN(LCVPWreck.class);
        new ShipGeneric.SPAWN(MAS501JP.class);
        new ShipGeneric.SPAWN(MAS501RN.class);
        new ShipGeneric.SPAWN(MAS501UNE.class);
        new ShipGeneric.SPAWN(MAS501UNP.class);
        new ShipGeneric.SPAWN(MFPIT.class);
        new ShipGeneric.SPAWN(MFPT.class);
        new ShipGeneric.SPAWN(SmallWreck.class);
//        new ShipGeneric.SPAWN(USSBesugo_Sub.class);
//        new ShipGeneric.SPAWN(USSNautilus_Sub.class);
//        new ShipGeneric.SPAWN(USSPermit_Sub.class);
//        new ShipGeneric.SPAWN(USSSpot_Sub.class);
//        new ShipGeneric.SPAWN(VIICRO500_Sub.class);
//        new ShipGeneric.SPAWN(VIICswordfish_Sub.class);
//        new ShipGeneric.SPAWN(VIICU124_Sub.class);
//        new ShipGeneric.SPAWN(VIICU564_Sub.class);
//        new ShipGeneric.SPAWN(VIICwinter_Sub.class);
        new BigshipGeneric.SPAWN(I400_Srf.class);
//        new BigshipGeneric.SPAWN(I400_Sub.class);
//        new BigshipGeneric.SPAWN(USSBoxerCV21_51.class);
        new BigshipGeneric.SPAWN(USSEnterpriseCV6_42.class);
        new BigshipGeneric.SPAWN(USSIowaBB61.class);
        new BigshipGeneric.SPAWN(USSYorktownCV5_42.class);
    }
}
