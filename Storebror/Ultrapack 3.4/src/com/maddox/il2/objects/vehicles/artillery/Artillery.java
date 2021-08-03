package com.maddox.il2.objects.vehicles.artillery;

import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.ai.ground.TgtInfantry;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.ai.ground.TgtVehicle;

public abstract class Artillery {
    public static class Battery88mm extends ArtilleryGeneric {}

    public static class Battery85mm extends ArtilleryGeneric {}

    public static class Battery75mm extends ArtilleryGeneric {}

    public static class WWIBattery75mm extends ArtilleryGeneric {}

    public static class Arty_Barrage extends ArtilleryGeneric {}

    public static class Zenit3K extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Zenit85mm_1939 extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Flak18_88mm extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Type88_75mm_JA extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Bofors_40mm_UK extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Bofors_40mm_US extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class StBofors_40mm_UK extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class StBofors_40mm_US extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Maxime extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Maxime4 extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Zenit25mm_1940 extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Zenit61K extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class _50calMG_water_US extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Flak30_20mm extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Flak38_20mm extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Flak18_37mm extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Type98_20mm_JA extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Twin25mm_JA extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class Trip25mm_JA extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class DShKAA extends ArtilleryGeneric implements TgtFlak, AAA {}

    public static class ML20 extends ArtilleryGeneric {}

    public static class M30_122mm extends ArtilleryGeneric {}

    public static class Howitzer_150mm extends ArtilleryGeneric {}

    public static class M2A1_105mm extends ArtilleryGeneric {}

    public static class Type91_105mm extends ArtilleryGeneric {}

    public static class ATG38_45mm extends ArtilleryGeneric {}

    public static class ZIS3 extends ArtilleryGeneric {}

    public static class M5_75mm extends ArtilleryGeneric {}

    public static class PaK35 extends ArtilleryGeneric {}

    public static class PaK38 extends ArtilleryGeneric {}

    public static class PaK40 extends ArtilleryGeneric {}

    public static class Type94_37mm_JA extends ArtilleryGeneric {}

    public static class Type38_75mm extends ArtilleryGeneric {}

    public static class BunkerA_gun extends ArtilleryGeneric {}

    public static class BunkerA2_gun extends ArtilleryGeneric {}

    public static class BunkerD_gun extends ArtilleryGeneric {}

    public static class BunkerS2_gun extends ArtilleryGeneric {}

    public static class MG42 extends ArtilleryGeneric {}

    public static class BunkerG1_gun extends ArtilleryGeneric {}

    public static class BunkerR1_gun extends ArtilleryGeneric {}

    public static class BunkerR2_gun extends ArtilleryGeneric {}

    public static class DShK extends ArtilleryGeneric {}

    public static class _50calMG_US_M2HB extends ArtilleryGeneric {}

    public static class HoRo_MG extends ArtilleryGeneric implements TgtVehicle {}

    public static class PzIIF extends ArtilleryGeneric implements TgtTank, STank {}

    public static class LT_35 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class LT_35Ro extends ArtilleryGeneric implements TgtTank, STank {}

    public static class LT_35Sl extends ArtilleryGeneric implements TgtTank, STank {}

    public static class LT_38 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class LT_38Sl extends ArtilleryGeneric implements TgtTank, STank {}

    public static class LT_40 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class LT_40MG extends ArtilleryGeneric implements TgtTank, STank {}

    public static class HaGo extends ArtilleryGeneric implements TgtTank, STank {}

    public static class HaGoRadio extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ChiHa extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ChiHa_alt extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzIIIG extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzIIIJ extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzIIIM extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzIIIN extends ArtilleryGeneric implements TgtTank, STank {}

    public static class TuranI extends ArtilleryGeneric implements TgtTank, STank {}

    public static class TuranII extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzIVE extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzIVF extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzIVF2 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzIVJ extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzVA extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzV_II extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzVIE extends ArtilleryGeneric implements TgtTank, STank {}

    public static class PzVIB extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Maus extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ZrinyiII extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Hetzer extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Jagdpanther extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Ferdinand extends ArtilleryGeneric implements TgtTank, STank {}

    public static class StuGIIIG extends ArtilleryGeneric implements TgtTank, STank {}

    public static class StuGIV extends ArtilleryGeneric implements TgtTank, STank {}

    public static class MarderIII extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Karl_600mm extends ArtilleryGeneric implements TgtTank, STank {}

    public static class HoRo extends ArtilleryGeneric implements TgtTank, STank {}

    public static class SdKfz251 extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    public static class DemagFlak extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    public static class SdKfz6Flak37 extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    public static class OpelBlitzMaultierAA extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    public static class Nimrod extends ArtilleryGeneric implements TgtTank, TgtFlak, STank {}

    public static class Wirbelwind extends ArtilleryGeneric implements TgtTank, TgtFlak, STank {}

    public static class Coelian extends ArtilleryGeneric implements TgtTank, TgtFlak, STank {}

    public static class Tatra_OA30 extends ArtilleryGeneric implements TgtVehicle {}

    public static class OpelBlitzMaultierRocket extends ArtilleryGeneric implements TgtVehicle, STank {}

    public static class BA_64 extends ArtilleryGeneric implements TgtVehicle {}

    public static class BA_10 extends ArtilleryGeneric implements TgtVehicle {}

    public static class BA_6 extends ArtilleryGeneric implements TgtVehicle {}

    public static class FAIM extends ArtilleryGeneric implements TgtVehicle {}

    public static class LVT_2 extends ArtilleryGeneric implements TgtVehicle {}

    public static class M8_Greyhound extends ArtilleryGeneric implements TgtVehicle {}

    public static class BT7 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T40S extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T60 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class _7TP extends ArtilleryGeneric implements TgtTank, STank {}

    public static class M3A1Stuart extends ArtilleryGeneric implements TgtTank, STank {}

    public static class M5A1Stuart extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T70M extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T26_Early extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T26_Late extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ValentineII extends ArtilleryGeneric implements TgtTank, STank {}

    public static class MatildaMkII_UK extends ArtilleryGeneric implements TgtTank, STank {}

    public static class MatildaMkII_AU extends ArtilleryGeneric implements TgtTank, STank {}

    public static class M4A2 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class M4A2_US extends ArtilleryGeneric implements TgtTank, STank {}

    public static class M4A2_76W extends ArtilleryGeneric implements TgtTank, STank {}

    public static class M4A2_76W_US extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T34 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T44 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T34_85 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T28 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class KV1 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class IS2 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class IS3 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class T35 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ZSU37 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class SU76M extends ArtilleryGeneric implements TgtTank, STank {}

    public static class SU85 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class SU100 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ISU152 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Katyusha extends ArtilleryGeneric implements TgtVehicle {}

    public static class StudebeckerRocket extends ArtilleryGeneric implements TgtVehicle {}

    public static class ZIS5_AA extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    public static class MaximeGAZ extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    public static class M3A1_APC extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    public static class M16 extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    public static class M16_US extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    public static class GBBazooka extends ArtilleryGeneric implements TgtInfantry {}

    public static class USBazooka extends ArtilleryGeneric implements TgtInfantry {}

    public static class GBInfantry extends ArtilleryGeneric implements TgtInfantry {}

    public static class RusBazooka extends ArtilleryGeneric implements TgtInfantry {}

    public static class USInfantry extends ArtilleryGeneric implements TgtInfantry {}

    public static class AxisBazooka extends ArtilleryGeneric implements TgtInfantry {}

    public static class JapInfantry extends ArtilleryGeneric implements TgtInfantry {}

    public static class RusInfantry extends ArtilleryGeneric implements TgtInfantry {}

    public static class AxisInfantry extends ArtilleryGeneric implements TgtInfantry {}

    public static class CoastBunker_PTO extends ArtilleryGeneric {}

    public static class AxisPnzrInfantry extends ArtilleryGeneric implements TgtInfantry {}

    public static class CoastBunker_ETO1 extends ArtilleryGeneric {}

    public static class CoastBunker_ETO2 extends ArtilleryGeneric {}

    public static class M3Lee extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Crusader extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Fiat_M13 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Fiat_M15 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Fiat_M40 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Fiat_M42 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class SomuaS35 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class RenaultR35 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class VickersMKVI extends ArtilleryGeneric implements TgtTank, STank {}

    public static class HotchkissH35 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class Bishop extends ArtilleryGeneric implements TgtTank, STank {}

    public static class StuartM3a1 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ShermanM4 extends ArtilleryGeneric implements TgtTank, STank {}

    public static class CromwellMkIV extends ArtilleryGeneric implements TgtTank, STank {}

    public static class CometMkI extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ChurchillMkVII extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ChurchillMkIVA extends ArtilleryGeneric implements TgtTank, STank {}

    public static class ChallengerMkI extends ArtilleryGeneric implements TgtTank, STank {}

    public static class CrusaderAAA extends ArtilleryGeneric implements TgtTank, TgtFlak, STank {}

    public static class M3_75mm_GMC extends ArtilleryGeneric implements TgtVehicle, TgtFlak, STank {}

    static {
        new ArtilleryGeneric.SPAWN(Battery88mm.class);
        new ArtilleryGeneric.SPAWN(Battery85mm.class);
        new ArtilleryGeneric.SPAWN(Battery75mm.class);
        new ArtilleryGeneric.SPAWN(WWIBattery75mm.class);
        new ArtilleryGeneric.SPAWN(Arty_Barrage.class);
        new ArtilleryGeneric.SPAWN(ChallengerMkI.class);
        new ArtilleryGeneric.SPAWN(ChurchillMkIVA.class);
        new ArtilleryGeneric.SPAWN(ChurchillMkVII.class);
        new ArtilleryGeneric.SPAWN(CometMkI.class);
        new ArtilleryGeneric.SPAWN(CromwellMkIV.class);
        new ArtilleryGeneric.SPAWN(ShermanM4.class);
        new ArtilleryGeneric.SPAWN(StuartM3a1.class);
        new ArtilleryGeneric.SPAWN(Bishop.class);
        new ArtilleryGeneric.SPAWN(CrusaderAAA.class);
        new ArtilleryGeneric.SPAWN(HotchkissH35.class);
        new ArtilleryGeneric.SPAWN(VickersMKVI.class);
        new ArtilleryGeneric.SPAWN(RenaultR35.class);
        new ArtilleryGeneric.SPAWN(SomuaS35.class);
        new ArtilleryGeneric.SPAWN(Fiat_M40.class);
        new ArtilleryGeneric.SPAWN(Fiat_M42.class);
        new ArtilleryGeneric.SPAWN(Fiat_M15.class);
        new ArtilleryGeneric.SPAWN(Fiat_M13.class);
        new ArtilleryGeneric.SPAWN(Crusader.class);
        new ArtilleryGeneric.SPAWN(M3Lee.class);
        new ArtilleryGeneric.SPAWN(CoastBunker_ETO2.class);
        new ArtilleryGeneric.SPAWN(CoastBunker_ETO1.class);
        new ArtilleryGeneric.SPAWN(AxisPnzrInfantry.class);
        new ArtilleryGeneric.SPAWN(CoastBunker_PTO.class);
        new ArtilleryGeneric.SPAWN(AxisInfantry.class);
        new ArtilleryGeneric.SPAWN(RusInfantry.class);
        new ArtilleryGeneric.SPAWN(JapInfantry.class);
        new ArtilleryGeneric.SPAWN(AxisBazooka.class);
        new ArtilleryGeneric.SPAWN(USInfantry.class);
        new ArtilleryGeneric.SPAWN(RusBazooka.class);
        new ArtilleryGeneric.SPAWN(GBInfantry.class);
        new ArtilleryGeneric.SPAWN(USBazooka.class);
        new ArtilleryGeneric.SPAWN(GBBazooka.class);
        new ArtilleryGeneric.SPAWN(Zenit3K.class);
        new ArtilleryGeneric.SPAWN(Zenit85mm_1939.class);
        new ArtilleryGeneric.SPAWN(Flak18_88mm.class);
        new ArtilleryGeneric.SPAWN(Type88_75mm_JA.class);
        new ArtilleryGeneric.SPAWN(Bofors_40mm_UK.class);
        new ArtilleryGeneric.SPAWN(Bofors_40mm_US.class);
        new ArtilleryGeneric.SPAWN(StBofors_40mm_UK.class);
        new ArtilleryGeneric.SPAWN(StBofors_40mm_US.class);
        new ArtilleryGeneric.SPAWN(Maxime.class);
        new ArtilleryGeneric.SPAWN(Maxime4.class);
        new ArtilleryGeneric.SPAWN(Zenit25mm_1940.class);
        new ArtilleryGeneric.SPAWN(Zenit61K.class);
        new ArtilleryGeneric.SPAWN(_50calMG_water_US.class);
        new ArtilleryGeneric.SPAWN(Flak30_20mm.class);
        new ArtilleryGeneric.SPAWN(Flak38_20mm.class);
        new ArtilleryGeneric.SPAWN(Flak18_37mm.class);
        new ArtilleryGeneric.SPAWN(Type98_20mm_JA.class);
        new ArtilleryGeneric.SPAWN(Twin25mm_JA.class);
        new ArtilleryGeneric.SPAWN(Trip25mm_JA.class);
        new ArtilleryGeneric.SPAWN(DShKAA.class);
        new ArtilleryGeneric.SPAWN(ML20.class);
        new ArtilleryGeneric.SPAWN(M30_122mm.class);
        new ArtilleryGeneric.SPAWN(Howitzer_150mm.class);
        new ArtilleryGeneric.SPAWN(M2A1_105mm.class);
        new ArtilleryGeneric.SPAWN(Type91_105mm.class);
        new ArtilleryGeneric.SPAWN(ATG38_45mm.class);
        new ArtilleryGeneric.SPAWN(ZIS3.class);
        new ArtilleryGeneric.SPAWN(M5_75mm.class);
        new ArtilleryGeneric.SPAWN(PaK35.class);
        new ArtilleryGeneric.SPAWN(PaK38.class);
        new ArtilleryGeneric.SPAWN(PaK40.class);
        new ArtilleryGeneric.SPAWN(Type94_37mm_JA.class);
        new ArtilleryGeneric.SPAWN(Type38_75mm.class);
        new ArtilleryGeneric.SPAWN(BunkerA_gun.class);
        new ArtilleryGeneric.SPAWN(BunkerA2_gun.class);
        new ArtilleryGeneric.SPAWN(BunkerD_gun.class);
        new ArtilleryGeneric.SPAWN(BunkerS2_gun.class);
        new ArtilleryGeneric.SPAWN(MG42.class);
        new ArtilleryGeneric.SPAWN(BunkerG1_gun.class);
        new ArtilleryGeneric.SPAWN(BunkerR1_gun.class);
        new ArtilleryGeneric.SPAWN(BunkerR2_gun.class);
        new ArtilleryGeneric.SPAWN(DShK.class);
        new ArtilleryGeneric.SPAWN(_50calMG_US_M2HB.class);
        new ArtilleryGeneric.SPAWN(HoRo_MG.class);
        new ArtilleryGeneric.SPAWN(PzIIF.class);
        new ArtilleryGeneric.SPAWN(LT_35.class);
        new ArtilleryGeneric.SPAWN(LT_35Ro.class);
        new ArtilleryGeneric.SPAWN(LT_35Sl.class);
        new ArtilleryGeneric.SPAWN(LT_38.class);
        new ArtilleryGeneric.SPAWN(LT_38Sl.class);
        new ArtilleryGeneric.SPAWN(LT_40.class);
        new ArtilleryGeneric.SPAWN(LT_40MG.class);
        new ArtilleryGeneric.SPAWN(HaGo.class);
        new ArtilleryGeneric.SPAWN(HaGoRadio.class);
        new ArtilleryGeneric.SPAWN(ChiHa.class);
        new ArtilleryGeneric.SPAWN(ChiHa_alt.class);
        new ArtilleryGeneric.SPAWN(PzIIIG.class);
        new ArtilleryGeneric.SPAWN(PzIIIJ.class);
        new ArtilleryGeneric.SPAWN(PzIIIM.class);
        new ArtilleryGeneric.SPAWN(PzIIIN.class);
        new ArtilleryGeneric.SPAWN(TuranI.class);
        new ArtilleryGeneric.SPAWN(TuranII.class);
        new ArtilleryGeneric.SPAWN(PzIVE.class);
        new ArtilleryGeneric.SPAWN(PzIVF.class);
        new ArtilleryGeneric.SPAWN(PzIVF2.class);
        new ArtilleryGeneric.SPAWN(PzIVJ.class);
        new ArtilleryGeneric.SPAWN(PzVA.class);
        new ArtilleryGeneric.SPAWN(PzV_II.class);
        new ArtilleryGeneric.SPAWN(PzVIE.class);
        new ArtilleryGeneric.SPAWN(PzVIB.class);
        new ArtilleryGeneric.SPAWN(Maus.class);
        new ArtilleryGeneric.SPAWN(ZrinyiII.class);
        new ArtilleryGeneric.SPAWN(Hetzer.class);
        new ArtilleryGeneric.SPAWN(Jagdpanther.class);
        new ArtilleryGeneric.SPAWN(Ferdinand.class);
        new ArtilleryGeneric.SPAWN(StuGIIIG.class);
        new ArtilleryGeneric.SPAWN(StuGIV.class);
        new ArtilleryGeneric.SPAWN(MarderIII.class);
        new ArtilleryGeneric.SPAWN(Karl_600mm.class);
        new ArtilleryGeneric.SPAWN(HoRo.class);
        new ArtilleryGeneric.SPAWN(SdKfz251.class);
        new ArtilleryGeneric.SPAWN(DemagFlak.class);
        new ArtilleryGeneric.SPAWN(SdKfz6Flak37.class);
        new ArtilleryGeneric.SPAWN(OpelBlitzMaultierAA.class);
        new ArtilleryGeneric.SPAWN(Nimrod.class);
        new ArtilleryGeneric.SPAWN(Wirbelwind.class);
        new ArtilleryGeneric.SPAWN(Coelian.class);
        new ArtilleryGeneric.SPAWN(Tatra_OA30.class);
        new ArtilleryGeneric.SPAWN(OpelBlitzMaultierRocket.class);
        new ArtilleryGeneric.SPAWN(BA_64.class);
        new ArtilleryGeneric.SPAWN(BA_10.class);
        new ArtilleryGeneric.SPAWN(BA_6.class);
        new ArtilleryGeneric.SPAWN(FAIM.class);
        new ArtilleryGeneric.SPAWN(LVT_2.class);
        new ArtilleryGeneric.SPAWN(M8_Greyhound.class);
        new ArtilleryGeneric.SPAWN(BT7.class);
        new ArtilleryGeneric.SPAWN(T40S.class);
        new ArtilleryGeneric.SPAWN(T60.class);
        new ArtilleryGeneric.SPAWN(_7TP.class);
        new ArtilleryGeneric.SPAWN(M3A1Stuart.class);
        new ArtilleryGeneric.SPAWN(M5A1Stuart.class);
        new ArtilleryGeneric.SPAWN(T70M.class);
        new ArtilleryGeneric.SPAWN(T26_Early.class);
        new ArtilleryGeneric.SPAWN(T26_Late.class);
        new ArtilleryGeneric.SPAWN(ValentineII.class);
        new ArtilleryGeneric.SPAWN(MatildaMkII_UK.class);
        new ArtilleryGeneric.SPAWN(MatildaMkII_AU.class);
        new ArtilleryGeneric.SPAWN(M4A2.class);
        new ArtilleryGeneric.SPAWN(M4A2_US.class);
        new ArtilleryGeneric.SPAWN(M4A2_76W.class);
        new ArtilleryGeneric.SPAWN(M4A2_76W_US.class);
        new ArtilleryGeneric.SPAWN(T34.class);
        new ArtilleryGeneric.SPAWN(T44.class);
        new ArtilleryGeneric.SPAWN(T34_85.class);
        new ArtilleryGeneric.SPAWN(T28.class);
        new ArtilleryGeneric.SPAWN(KV1.class);
        new ArtilleryGeneric.SPAWN(IS2.class);
        new ArtilleryGeneric.SPAWN(IS3.class);
        new ArtilleryGeneric.SPAWN(T35.class);
        new ArtilleryGeneric.SPAWN(ZSU37.class);
        new ArtilleryGeneric.SPAWN(SU76M.class);
        new ArtilleryGeneric.SPAWN(SU85.class);
        new ArtilleryGeneric.SPAWN(SU100.class);
        new ArtilleryGeneric.SPAWN(ISU152.class);
        new ArtilleryGeneric.SPAWN(Katyusha.class);
        new ArtilleryGeneric.SPAWN(StudebeckerRocket.class);
        new ArtilleryGeneric.SPAWN(ZIS5_AA.class);
        new ArtilleryGeneric.SPAWN(MaximeGAZ.class);
        new ArtilleryGeneric.SPAWN(M3A1_APC.class);
        new ArtilleryGeneric.SPAWN(M16.class);
        new ArtilleryGeneric.SPAWN(M16_US.class);
        new ArtilleryGeneric.SPAWN(M3_75mm_GMC.class);
    }
}
