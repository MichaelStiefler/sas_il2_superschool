package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class HalifaxBMkIII extends Halifax implements TypeBomber {

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.killPilot(this, 4);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -23F) {
                    f = -23F;
                    flag = false;
                }
                if (f > 23F) {
                    f = 23F;
                    flag = false;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > 15F) {
                    f1 = 15F;
                    flag = false;
                }
                break;

            case 1:
                if (f1 < 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                if (f1 > 73F) {
                    f1 = 73F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -38F) {
                    f = -38F;
                    flag = false;
                }
                if (f > 38F) {
                    f = 38F;
                    flag = false;
                }
                if (f1 < -41F) {
                    f1 = -41F;
                    flag = false;
                }
                if (f1 > 38F) {
                    f1 = 38F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 3:
                this.FM.turret[1].bIsOperable = false;
                break;

            case 4:
                this.FM.turret[2].bIsOperable = false;
                break;
        }
    }

    static {
        Class class1 = HalifaxBMkIII.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Halifax");
        Property.set(class1, "meshName", "3DO/Plane/HALIFAX-B-Mk3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/HalifaxIII.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHalifaxBMkIII.class, CockpitHalifaxBMkIII_Bombardier.class, CockpitHalifaxBMkIII_FGunner.class, CockpitHalifaxBMkIII_TGunner.class, CockpitHalifaxBMkIII_AGunner.class });
        Aircraft.weaponTriggersRegister(class1,
                new int[] { 10, 11, 11, 11, 11, 12, 12, 12, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN08", "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN09", "_MGUN10", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn02", "_BombSpawn03", "_BombSpawn03",
                "_BombSpawn04", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_BombSpawn07", "_BombSpawn07", "_BombSpawn08", "_BombSpawn08", "_BombSpawn09", "_BombSpawn09", "_BombSpawn10", "_BombSpawn10", "_BombSpawn11",
                "_BombSpawn11", "_BombSpawn12", "_BombSpawn12", "_BombSpawn13", "_BombSpawn13", "_BombSpawn14", "_BombSpawn14", "_BombSpawn15", "_BombSpawn15", "_BombSpawn16", "_BombSpawn16", "_BombSpawn17", "_BombSpawn17", "_BombSpawn18", "_BombSpawn18",
                "_BombSpawn19", "_BombSpawn19", "_BombSpawn20", "_BombSpawn20", "_BombSpawn21", "_BombSpawn21", "_BombSpawn101", "_BombSpawn102", "_BombSpawn103", "_BombSpawn104", "_BombSpawn105", "_BombSpawn106", "_BombSpawn107", "_BombSpawn108",
                "_BombSpawn108", "_BombSpawn108", "_BombSpawn109", "_BombSpawn110", "_BombSpawn111", "_BombSpawn112", "_BombSpawn113", "_BombSpawn114", "_BombSpawn115", "_BombSpawn116", "_BombSpawn116", "_BombSpawn116", "_BombSpawn117", "_BombSpawn118",
                "_BombSpawn119", "_BombSpawn120", "_BombSpawn121", "_BombSpawn122", "_BombSpawn123", "_BombSpawn124", "_BombSpawn124", "_BombSpawn124", "_BombSpawn125", "_BombSpawn126", "_BombSpawn127", "_BombSpawn128", "_BombSpawn129", "_BombSpawn130",
                "_BombSpawn131", "_BombSpawn132", "_BombSpawn132", "_BombSpawn132", "_BombSpawn133", "_BombSpawn134", "_BombSpawn135", "_BombSpawn136", "_BombSpawn137", "_BombSpawn138", "_BombSpawn139", "_BombSpawn140", "_BombSpawn140", "_BombSpawn140",
                "_BombSpawn141", "_BombSpawn142", "_BombSpawn143", "_BombSpawn144", "_BombSpawn145", "_BombSpawn146", "_BombSpawn147", "_BombSpawn148", "_BombSpawn148", "_BombSpawn148", "_BombSpawn149", "_BombSpawn150", "_BombSpawn151", "_BombSpawn152",
                "_BombSpawn153", "_BombSpawn154", "_BombSpawn155", "_BombSpawn156", "_BombSpawn156", "_BombSpawn156", "_BombSpawn157", "_BombSpawn158", "_BombSpawn159", "_BombSpawn160", "_BombSpawn161", "_BombSpawn162", "_BombSpawn163", "_BombSpawn164",
                "_BombSpawn164", "_BombSpawn164", "_BombSpawn165", "_BombSpawn166", "_BombSpawn167", "_BombSpawn168", "_BombSpawn169", "_BombSpawn170", "_BombSpawn171", "_BombSpawn172", "_BombSpawn172", "_BombSpawn172", "_BombSpawn201", "_BombSpawn202",
                "_BombSpawn203", "_BombSpawn204", "_BombSpawn205", "_BombSpawn206", "_BombSpawn207", "_BombSpawn208", "_BombSpawn209", "_BombSpawn210", "_BombSpawn211", "_BombSpawn212", "_BombSpawn212", "_BombSpawn212", "_BombSpawn213", "_BombSpawn214",
                "_BombSpawn215", "_BombSpawn216", "_BombSpawn217", "_BombSpawn218", "_BombSpawn219", "_BombSpawn220", "_BombSpawn221", "_BombSpawn222", "_BombSpawn223", "_BombSpawn224", "_BombSpawn224", "_BombSpawn224", "_BombSpawn225", "_BombSpawn226",
                "_BombSpawn227", "_BombSpawn228", "_BombSpawn229", "_BombSpawn230", "_BombSpawn231", "_BombSpawn232", "_BombSpawn233", "_BombSpawn234", "_BombSpawn235", "_BombSpawn236", "_BombSpawn236", "_BombSpawn236", "_BombSpawn237", "_BombSpawn238",
                "_BombSpawn239", "_BombSpawn240", "_BombSpawn241", "_BombSpawn242", "_BombSpawn243", "_BombSpawn244", "_BombSpawn245", "_BombSpawn246", "_BombSpawn247", "_BombSpawn248", "_BombSpawn248", "_BombSpawn248", "_BombSpawn249", "_BombSpawn250",
                "_BombSpawn251", "_BombSpawn252", "_BombSpawn253", "_BombSpawn254", "_BombSpawn255", "_BombSpawn256", "_BombSpawn257", "_BombSpawn258", "_BombSpawn259", "_BombSpawn260", "_BombSpawn260", "_BombSpawn260", "_BombSpawn261", "_BombSpawn262",
                "_BombSpawn263", "_BombSpawn264", "_BombSpawn265", "_BombSpawn266", "_BombSpawn267", "_BombSpawn268", "_BombSpawn269", "_BombSpawn270", "_BombSpawn271", "_BombSpawn272", "_BombSpawn272", "_BombSpawn272", "_BombSpawn273", "_BombSpawn274",
                "_BombSpawn275", "_BombSpawn276", "_BombSpawn277", "_BombSpawn278", "_BombSpawn279", "_BombSpawn280", "_BombSpawn281", "_BombSpawn282", "_BombSpawn283", "_BombSpawn284", "_BombSpawn284", "_BombSpawn284", "_BombSpawn285", "_BombSpawn286",
                "_BombSpawn287", "_BombSpawn288", "_BombSpawn289", "_BombSpawn290", "_BombSpawn291", "_BombSpawn292", "_BombSpawn293", "_BombSpawn294", "_BombSpawn295", "_BombSpawn296", "_BombSpawn296", "_BombSpawn296", "_BombSpawn297", "_BombSpawn298",
                "_BombSpawn299", "_BombSpawn300", "_BombSpawn301", "_BombSpawn302", "_BombSpawn303", "_BombSpawn304", "_BombSpawn305", "_BombSpawn306", "_BombSpawn307", "_BombSpawn308", "_BombSpawn308", "_BombSpawn308", "_BombSpawn401", "_BombSpawn402",
                "_BombSpawn403", "_BombSpawn404", "_BombSpawn405", "_BombSpawn406", "_BombSpawn407", "_BombSpawn408", "_BombSpawn408", "_BombSpawn408", "_BombSpawn409", "_BombSpawn410", "_BombSpawn411", "_BombSpawn412", "_BombSpawn413", "_BombSpawn414",
                "_BombSpawn415", "_BombSpawn416", "_BombSpawn416", "_BombSpawn416", "_BombSpawn417", "_BombSpawn418", "_BombSpawn419", "_BombSpawn420", "_BombSpawn421", "_BombSpawn422", "_BombSpawn423", "_BombSpawn424", "_BombSpawn424", "_BombSpawn424",
                "_BombSpawn425", "_BombSpawn426", "_BombSpawn427", "_BombSpawn428", "_BombSpawn429", "_BombSpawn430", "_BombSpawn431", "_BombSpawn432", "_BombSpawn432", "_BombSpawn432", "_BombSpawn433", "_BombSpawn434", "_BombSpawn435", "_BombSpawn436",
                "_BombSpawn437", "_BombSpawn438", "_BombSpawn439", "_BombSpawn440", "_BombSpawn440", "_BombSpawn440", "_BombSpawn441", "_BombSpawn442", "_BombSpawn443", "_BombSpawn444", "_BombSpawn445", "_BombSpawn446", "_BombSpawn447", "_BombSpawn448",
                "_BombSpawn448", "_BombSpawn448", "_BombSpawn501", "_BombSpawn502", "_BombSpawn503", "_BombSpawn504", "_BombSpawn505", "_BombSpawn506", "_BombSpawn507", "_BombSpawn508", "_BombSpawn509", "_BombSpawn510", "_BombSpawn511", "_BombSpawn512",
                "_BombSpawn512", "_BombSpawn512", "_BombSpawn513", "_BombSpawn514", "_BombSpawn515", "_BombSpawn516", "_BombSpawn517", "_BombSpawn518", "_BombSpawn519", "_BombSpawn520", "_BombSpawn521", "_BombSpawn522", "_BombSpawn523", "_BombSpawn524",
                "_BombSpawn524", "_BombSpawn524", "_BombSpawn525", "_BombSpawn526", "_BombSpawn527", "_BombSpawn528", "_BombSpawn529", "_BombSpawn530", "_BombSpawn531", "_BombSpawn532", "_BombSpawn533", "_BombSpawn534", "_BombSpawn535", "_BombSpawn536",
                "_BombSpawn536", "_BombSpawn536", "_BombSpawn537", "_BombSpawn538", "_BombSpawn539", "_BombSpawn540", "_BombSpawn541", "_BombSpawn542", "_BombSpawn543", "_BombSpawn544", "_BombSpawn545", "_BombSpawn546", "_BombSpawn547", "_BombSpawn548",
                "_BombSpawn548", "_BombSpawn548", "_BombSpawn549", "_BombSpawn550", "_BombSpawn551", "_BombSpawn552", "_BombSpawn553", "_BombSpawn554", "_BombSpawn555", "_BombSpawn556", "_BombSpawn557", "_BombSpawn558", "_BombSpawn559", "_BombSpawn560",
                "_BombSpawn560", "_BombSpawn560", "_BombSpawn561", "_BombSpawn562", "_BombSpawn563", "_BombSpawn564", "_BombSpawn565", "_BombSpawn566", "_BombSpawn567", "_BombSpawn568", "_BombSpawn569", "_BombSpawn570", "_BombSpawn571", "_BombSpawn572",
                "_BombSpawn572", "_BombSpawn572", "_BombSpawn601", "_BombSpawn602", "_BombSpawn603", "_BombSpawn604", "_BombSpawn605", "_BombSpawn606", "_BombSpawn607", "_BombSpawn608", "_BombSpawn609", "_BombSpawn610", "_BombSpawn611", "_BombSpawn612",
                "_BombSpawn613", "_BombSpawn614", "_BombSpawn615", "_BombSpawn615", "_BombSpawn615", "_BombSpawn616", "_BombSpawn617", "_BombSpawn618", "_BombSpawn619", "_BombSpawn620", "_BombSpawn621", "_BombSpawn622", "_BombSpawn623", "_BombSpawn624",
                "_BombSpawn625", "_BombSpawn626", "_BombSpawn627", "_BombSpawn628", "_BombSpawn629", "_BombSpawn630", "_BombSpawn630", "_BombSpawn630", "_BombSpawn631", "_BombSpawn632", "_BombSpawn633", "_BombSpawn634", "_BombSpawn635", "_BombSpawn636",
                "_BombSpawn637", "_BombSpawn638", "_BombSpawn639", "_BombSpawn640", "_BombSpawn641", "_BombSpawn642", "_BombSpawn643", "_BombSpawn644", "_BombSpawn645", "_BombSpawn645", "_BombSpawn645", "_BombSpawn646", "_BombSpawn647", "_BombSpawn648",
                "_BombSpawn649", "_BombSpawn650", "_BombSpawn651", "_BombSpawn652", "_BombSpawn653", "_BombSpawn654", "_BombSpawn655", "_BombSpawn656", "_BombSpawn657", "_BombSpawn658", "_BombSpawn659", "_BombSpawn660", "_BombSpawn660", "_BombSpawn660",
                "_BombSpawn661", "_BombSpawn662", "_BombSpawn663", "_BombSpawn664", "_BombSpawn665", "_BombSpawn666", "_BombSpawn667", "_BombSpawn668", "_BombSpawn669", "_BombSpawn670", "_BombSpawn671", "_BombSpawn672", "_BombSpawn673", "_BombSpawn674",
                "_BombSpawn675", "_BombSpawn675", "_BombSpawn675", "_BombSpawn676", "_BombSpawn677", "_BombSpawn678", "_BombSpawn679", "_BombSpawn680", "_BombSpawn681", "_BombSpawn682", "_BombSpawn683", "_BombSpawn684", "_BombSpawn685", "_BombSpawn686",
                "_BombSpawn687", "_BombSpawn688", "_BombSpawn689", "_BombSpawn690", "_BombSpawn690", "_BombSpawn690", "_BombSpawn691", "_BombSpawn692", "_BombSpawn693", "_BombSpawn694", "_BombSpawn695", "_BombSpawn696", "_BombSpawn697", "_BombSpawn698",
                "_BombSpawn699", "_BombSpawn700", "_BombSpawn701", "_BombSpawn702", "_BombSpawn703", "_BombSpawn704", "_BombSpawn705", "_BombSpawn705", "_BombSpawn705", "_BombSpawn706", "_BombSpawn707", "_BombSpawn708", "_BombSpawn709", "_BombSpawn710",
                "_BombSpawn711", "_BombSpawn712", "_BombSpawn713", "_BombSpawn714", "_BombSpawn715", "_BombSpawn716", "_BombSpawn717", "_BombSpawn718", "_BombSpawn719", "_BombSpawn720", "_BombSpawn720", "_BombSpawn720", "_BombSpawn721", "_BombSpawn722",
                "_BombSpawn723", "_BombSpawn724", "_BombSpawn725", "_BombSpawn726", "_BombSpawn727", "_BombSpawn728", "_BombSpawn729", "_BombSpawn730", "_BombSpawn731", "_BombSpawn732", "_BombSpawn733", "_BombSpawn734", "_BombSpawn735", "_BombSpawn735",
                "_BombSpawn735", "_BombSpawn801", "_BombSpawn802", "_BombSpawn803", "_BombSpawn804", "_BombSpawn805", "_BombSpawn806", "_BombSpawn807", "_BombSpawn808", "_BombSpawn809", "_BombSpawn810", "_BombSpawn811", "_BombSpawn812", "_BombSpawn813",
                "_BombSpawn814", "_BombSpawn815", "_BombSpawn815", "_BombSpawn815", "_BombSpawn816", "_BombSpawn817", "_BombSpawn818", "_BombSpawn819", "_BombSpawn820", "_BombSpawn821", "_BombSpawn822", "_BombSpawn823", "_BombSpawn824", "_BombSpawn825",
                "_BombSpawn826", "_BombSpawn827", "_BombSpawn828", "_BombSpawn829", "_BombSpawn830", "_BombSpawn830", "_BombSpawn830", "_BombSpawn831", "_BombSpawn832", "_BombSpawn833", "_BombSpawn834", "_BombSpawn835", "_BombSpawn836", "_BombSpawn837",
                "_BombSpawn838", "_BombSpawn839", "_BombSpawn840", "_BombSpawn841", "_BombSpawn842", "_BombSpawn843", "_BombSpawn844", "_BombSpawn845", "_BombSpawn845", "_BombSpawn845", "_BombSpawn846", "_BombSpawn847", "_BombSpawn848", "_BombSpawn849",
                "_BombSpawn850", "_BombSpawn851", "_BombSpawn852", "_BombSpawn853", "_BombSpawn854", "_BombSpawn855", "_BombSpawn856", "_BombSpawn857", "_BombSpawn858", "_BombSpawn859", "_BombSpawn860", "_BombSpawn860", "_BombSpawn860", "_BombSpawn861",
                "_BombSpawn862", "_BombSpawn863", "_BombSpawn864", "_BombSpawn865", "_BombSpawn866", "_BombSpawn867", "_BombSpawn868", "_BombSpawn869", "_BombSpawn870", "_BombSpawn871", "_BombSpawn872", "_BombSpawn873", "_BombSpawn874", "_BombSpawn875",
                "_BombSpawn875", "_BombSpawn875", "_BombSpawn876", "_BombSpawn877", "_BombSpawn878", "_BombSpawn879", "_BombSpawn880", "_BombSpawn881", "_BombSpawn882", "_BombSpawn883", "_BombSpawn884", "_BombSpawn885", "_BombSpawn886", "_BombSpawn887",
                "_BombSpawn888", "_BombSpawn889", "_BombSpawn890", "_BombSpawn890", "_BombSpawn890" });
    }
}
