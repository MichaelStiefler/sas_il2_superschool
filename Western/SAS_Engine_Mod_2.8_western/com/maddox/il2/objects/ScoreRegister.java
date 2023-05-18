/*Modified ScoreCounter class for the SAS Engine Mod*/

/*By western on 26th/Apr./2018, expanded for 8x Engines / Scheme8 aircrafts */
/*By western on 23rd/Jun./2018, expanded for 10x Engines / Scheme10 aircrafts */

package com.maddox.il2.objects;

import com.maddox.il2.ai.ScoreCounter;

public class ScoreRegister
{

    public ScoreRegister()
    {
    }

    public static void load()
    {
    }


    static 
    {
        ScoreCounter.register(com.maddox.il2.objects.air.Scheme1.class, 0, 100D, 300D);
        ScoreCounter.register(com.maddox.il2.objects.air.Scheme2.class, 0, 200D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.air.Scheme3.class, 0, 200D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.air.Scheme4.class, 0, 400D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.air.Scheme5.class, 0, 200D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.air.Scheme6.class, 0, 300D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.air.Scheme7.class, 0, 600D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.air.Scheme8.class, 0, 800D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.air.Scheme10.class, 0, 1000D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.air.BF_109G6HARTMANN.class, 0, 150D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.air.BF_109G6GRAF.class, 0, 150D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.air.I_16TYPE24SAFONOV.class, 0, 150D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.air.ME_262A1ANOWOTNY.class, 0, 150D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.air.MIG_3POKRYSHKIN.class, 0, 150D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.air.P_39NPOKRYSHKIN.class, 0, 150D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.air.P_39Q15RECHKALOV.class, 0, 150D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.air.YAK_9TALBERT.class, 0, 150D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.air.DXXI_SARJA3_SARVANTO.class, 0, 150D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.air.Aircraft.class, 0, 100D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.planes.PlaneGeneric.class, 8, 20D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.tanks.TankGeneric.class, 1, 40D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.tanks.Tank.Wirbelwind.class, 1, 130D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.tanks.Tank.T34.class, 1, 50D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.tanks.Tank.T34_85.class, 1, 50D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.tanks.Tank.KV1.class, 1, 55D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.tanks.Tank.IS2.class, 1, 55D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.tanks.Tank.PzVA.class, 1, 50D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.tanks.Tank.PzVIE.class, 1, 55D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.tanks.Tank.PzVIB.class, 1, 55D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.STank.class, 1, 40D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.Artillery.Wirbelwind.class, 1, 70D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.Artillery.T34.class, 1, 50D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.Artillery.T34_85.class, 1, 50D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.Artillery.KV1.class, 1, 55D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.Artillery.IS2.class, 1, 55D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.Artillery.PzVA.class, 1, 50D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.Artillery.PzVIE.class, 1, 55D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.Artillery.PzVIB.class, 1, 55D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.stationary.Stationary.Wirbelwind.class, 1, 30D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.stationary.Stationary.T34.class, 1, 30D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.stationary.Stationary.T34_85.class, 1, 30D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.stationary.Stationary.KV1.class, 1, 40D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.stationary.Stationary.IS2.class, 1, 40D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.stationary.Stationary.PzVA.class, 1, 30D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.stationary.Stationary.PzVIE.class, 1, 40D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.stationary.Stationary.PzVIB.class, 1, 40D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.AAA.class, 4, 60D, 150D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.SArtillery.class, 3, 30D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric.class, 3, 30D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.cars.CarGeneric.class, 2, 25D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.stationary.StationaryGeneric.class, 2, 25D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.artillery.SWagon.class, 6, 30D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.trains.Wagon.class, 6, 30D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.ships.ShipGeneric.class, 7, 100D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.BigshipGeneric.class, 7, 100D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Destroyer_USSR_Type7.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Destroyer_USSR_Type7_44.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Murgesku.class, 7, 250D, 600D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.MBoat.class, 7, 250D, 600D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Tral.class, 7, 200D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Aurora.class, 7, 500D, 1000D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Marat.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Kirov.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Tramp.class, 7, 200D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Niobe.class, 7, 600D, 1200D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.NiobeWithBeacon.class, 7, 600D, 1200D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Tanker.class, 7, 200D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Illmarinen.class, 7, 500D, 1000D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.Vainamoinen.class, 7, 500D, 1000D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSLexingtonCV2.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSSaratogaCV3.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSCVGeneric.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSBBGeneric.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSIndianapolisCA35.class, 7, 600D, 1200D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSGreenlingSS213_Srf.class, 7, 200D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSGreenlingSS213_Sub.class, 7, 200D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSGatoSS212_Srf.class, 7, 200D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSGatoSS212_Sub.class, 7, 200D, 500D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSWardDD139.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSDentDD116.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSFletcherDD445.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSOBannonDD450.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSKiddDD661.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSCasablancaCVE55.class, 7, 500D, 1000D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSKitkunBayCVE71.class, 7, 500D, 1000D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSShamrockBayCVE84.class, 7, 500D, 1000D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSEssexCV9.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.USSIntrepidCV11.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.LVT_2WAT.class, 7, 30D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.DUKW_WAT.class, 7, 30D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.LCVP.class, 7, 30D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.HMSIllustriousCV.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.HMSPoWBB.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.HMSKingGeorgeVBB.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.HMSDukeOfYorkBB.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNAkagiCV.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNShokakuCV.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNZuikakuCV.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNCVGeneric.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNBBGeneric.class, 7, 700D, 1400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNAkizukiDD42.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNAmatsukazeDD41.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNArashiDD41.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNKageroDD41.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNNowakiDD41.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNYukikazeDD41.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNAmatsukazeDD43.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNAmatsukazeDD43WithBeacon.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNNowakiDD43.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNYukikazeDD43.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNAmatsukazeDD45.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNYukikazeDD45.class, 7, 300D, 800D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNFishJunk.class, 7, 200D, 400D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.IJNFishJunkA.class, 7, 250D, 600D);
        ScoreCounter.register(com.maddox.il2.objects.ships.Ship.DaihatsuLC.class, 7, 30D, 100D);
        ScoreCounter.register(com.maddox.il2.objects.vehicles.radios.BeaconGeneric.class, 9, 35D, 200D);
        ScoreCounter.register(com.maddox.il2.objects.bridges.LongBridge.class, 5, 100D, 500D);
    }
}
