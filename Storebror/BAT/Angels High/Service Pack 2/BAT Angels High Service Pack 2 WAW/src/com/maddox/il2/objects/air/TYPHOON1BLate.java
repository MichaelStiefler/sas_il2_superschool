package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class TYPHOON1BLate extends TEMPEST {

    public void onAircraftLoaded() {
        this.FM.EI.engines[0].doSetKillControlAfterburner();
        super.onAircraftLoaded();
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.625F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.06845F);
        Aircraft.ypr[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 1.0F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.13F);
        Aircraft.ypr[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -8F);
        this.hierMesh().chunkSetLocate("Pilot1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void rareAction(float paramFloat, boolean paramBoolean) {
        super.rareAction(paramFloat, paramBoolean);
        if ((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && (this.FM.CT.getCockpitDoor() == 1.1F)) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    static {
        Class class1 = TYPHOON1BLate.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Typhoon");
        Property.set(class1, "meshName", "3DO/Plane/TyphoonMkIBLate_(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_gb", "3DO/Plane/TyphoonMkIBLate_(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Typhoon1BLate.fmd:TYPHOON_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTemp5.class });
        Property.set(class1, "LOSElevation", 0.93655F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12" });
//        String as[] = new String[26];
//        as[0] = "MGunHispanoMKIIk 152";
//        as[1] = "MGunHispanoMKIIk 162";
//        as[2] = "MGunHispanoMKIIk 162";
//        as[3] = "MGunHispanoMKIIk 152";
//        as[8] = "PylonTEMPESTPLN1 1";
//        as[9] = "PylonTEMPESTPLN2 1";
//        Aircraft.weaponsRegister(class1, "default", as);
//        String as1[] = new String[26];
//        as1[0] = "MGunHispanoMKIIk 152";
//        as1[1] = "MGunHispanoMKIIk 162";
//        as1[2] = "MGunHispanoMKIIk 162";
//        as1[3] = "MGunHispanoMKIIk 152";
//        Aircraft.weaponsRegister(class1, "light", as1);
//        String as2[] = new String[26];
//        as2[0] = "MGunHispanoMKIIk 152";
//        as2[1] = "MGunHispanoMKIIk 162";
//        as2[2] = "MGunHispanoMKIIk 162";
//        as2[3] = "MGunHispanoMKIIk 152";
//        as2[4] = "FuelTankGun_Tank44gal 1";
//        as2[5] = "FuelTankGun_Tank44gal 1";
//        as2[6] = "PylonTEMPESTPLN1 1";
//        as2[7] = "PylonTEMPESTPLN2 1";
//        Aircraft.weaponsRegister(class1, "2xdt", as2);
//        String as3[] = new String[26];
//        as3[0] = "MGunHispanoMKIIk 152";
//        as3[1] = "MGunHispanoMKIIk 162";
//        as3[2] = "MGunHispanoMKIIk 162";
//        as3[3] = "MGunHispanoMKIIk 152";
//        as3[8] = "PylonTEMPESTPLN1 1";
//        as3[9] = "PylonTEMPESTPLN2 1";
//        as3[10] = "BombGun250lbsE 1";
//        as3[11] = "BombGun250lbsE 1";
//        Aircraft.weaponsRegister(class1, "(summer43)_2xGP250E", as3);
//        String as4[] = new String[26];
//        as4[0] = "MGunHispanoMKIIk 152";
//        as4[1] = "MGunHispanoMKIIk 162";
//        as4[2] = "MGunHispanoMKIIk 162";
//        as4[3] = "MGunHispanoMKIIk 152";
//        as4[8] = "PylonTEMPESTPLN1 1";
//        as4[9] = "PylonTEMPESTPLN2 1";
//        as4[10] = "BombGun500lbsEMC 1";
//        as4[11] = "BombGun500lbsEMC 1";
//        Aircraft.weaponsRegister(class1, "2x500", as4);
//        String as5[] = new String[26];
//        as5[0] = "MGunHispanoMKIIk 152";
//        as5[1] = "MGunHispanoMKIIk 162";
//        as5[2] = "MGunHispanoMKIIk 162";
//        as5[3] = "MGunHispanoMKIIk 152";
//        as5[12] = "PylonTYPHOONPLN3MkI";
//        as5[13] = "PylonTYPHOONPLN4MkI";
//        as5[14] = "RocketGunHVAR5BEAU 1";
//        as5[15] = "RocketGunHVAR5BEAU 1";
//        as5[16] = "RocketGunHVAR5BEAU 1";
//        as5[17] = "RocketGunHVAR5BEAU 1";
//        as5[18] = "RocketGunHVAR5BEAU 1";
//        as5[19] = "RocketGunHVAR5BEAU 1";
//        as5[22] = "RocketGunHVAR5BEAU 1";
//        as5[23] = "RocketGunHVAR5BEAU 1";
//        Aircraft.weaponsRegister(class1, "(oct43/may44)_8x60lbs_MkIrack", as5);
//        String as6[] = new String[26];
//        as6[0] = "MGunHispanoMKIIk 152";
//        as6[1] = "MGunHispanoMKIIk 162";
//        as6[2] = "MGunHispanoMKIIk 162";
//        as6[3] = "MGunHispanoMKIIk 152";
//        as6[12] = "PylonTYPHOONPLN3MkI";
//        as6[13] = "PylonTYPHOONPLN4MkI";
//        as6[14] = "RocketGunHVAR5BEAUSALVO 1";
//        as6[15] = "RocketGunHVAR5BEAUSALVO 1";
//        as6[16] = "RocketGunHVAR5BEAUSALVO 1";
//        as6[17] = "RocketGunHVAR5BEAUSALVO 1";
//        as6[18] = "RocketGunHVAR5BEAUSALVO 1";
//        as6[19] = "RocketGunHVAR5BEAUSALVO 1";
//        as6[22] = "RocketGunHVAR5BEAUSALVO 1";
//        as6[23] = "RocketGunHVAR5BEAUSALVO 1";
//        Aircraft.weaponsRegister(class1, "(oct43/may44)_8x60lbs_MkIrack_salvo", as6);
//        String as7[] = new String[26];
//        as7[0] = "MGunHispanoMKIIk 152";
//        as7[1] = "MGunHispanoMKIIk 162";
//        as7[2] = "MGunHispanoMKIIk 162";
//        as7[3] = "MGunHispanoMKIIk 152";
//        as7[8] = "PylonTEMPESTPLN1 1";
//        as7[9] = "PylonTEMPESTPLN2 1";
//        as7[10] = "BombGun1000lbsE_MC 1";
//        as7[11] = "BombGun1000lbsE_MC 1";
//        Aircraft.weaponsRegister(class1, "2x1000", as7);
//        Aircraft.weaponsRegister(class1, "(aug44)_12x60lbs_MkIrack", new String[] {
//            "MGunHispanoMKIIk 152", "MGunHispanoMKIIk 162", "MGunHispanoMKIIk 162", "MGunHispanoMKIIk 152", null, null, null, null, null, null,
//            null, null, "PylonTYPHOONPLN3MkI", "PylonTYPHOONPLN4MkI", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1",
//            "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1"
//        });
//        Aircraft.weaponsRegister(class1, "(aug44)_12x60lbs_MkIrack_salvo", new String[] {
//            "MGunHispanoMKIIk 152", "MGunHispanoMKIIk 162", "MGunHispanoMKIIk 162", "MGunHispanoMKIIk 152", null, null, null, null, null, null,
//            null, null, "PylonTYPHOONPLN3MkI", "PylonTYPHOONPLN4MkI", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1",
//            "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1"
//        });
//        String as8[] = new String[26];
//        as8[0] = "MGunHispanoMKIIk 152";
//        as8[1] = "MGunHispanoMKIIk 162";
//        as8[2] = "MGunHispanoMKIIk 162";
//        as8[3] = "MGunHispanoMKIIk 152";
//        as8[4] = "FuelTankGun_Tank44gal 1";
//        as8[5] = "FuelTankGun_Tank44gal 1";
//        as8[6] = "PylonTEMPESTPLN1 1";
//        as8[7] = "PylonTEMPESTPLN2 1";
//        as8[12] = "PylonTYPHOONPLN3MkIhalf";
//        as8[13] = "PylonTYPHOONPLN4MkIhalf";
//        as8[14] = "RocketGunHVAR5BEAU 1";
//        as8[15] = "RocketGunHVAR5BEAU 1";
//        as8[16] = "RocketGunHVAR5BEAU 1";
//        as8[17] = "RocketGunHVAR5BEAU 1";
//        Aircraft.weaponsRegister(class1, "(aug44)_DTK_4x60lbs_MkIrack", as8);
//        String as9[] = new String[26];
//        as9[0] = "MGunHispanoMKIIk 152";
//        as9[1] = "MGunHispanoMKIIk 162";
//        as9[2] = "MGunHispanoMKIIk 162";
//        as9[3] = "MGunHispanoMKIIk 152";
//        as9[12] = "PylonTYPHOONPLN3MkII";
//        as9[13] = "PylonTYPHOONPLN4MkII";
//        as9[14] = "RocketGunHVAR5BEAU 1";
//        as9[15] = "RocketGunHVAR5BEAU 1";
//        as9[16] = "RocketGunHVAR5BEAU 1";
//        as9[17] = "RocketGunHVAR5BEAU 1";
//        as9[18] = "RocketGunHVAR5BEAU 1";
//        as9[19] = "RocketGunHVAR5BEAU 1";
//        as9[22] = "RocketGunHVAR5BEAU 1";
//        as9[23] = "RocketGunHVAR5BEAU 1";
//        Aircraft.weaponsRegister(class1, "8x5", as9);
//        String as10[] = new String[26];
//        as10[0] = "MGunHispanoMKIIk 152";
//        as10[1] = "MGunHispanoMKIIk 162";
//        as10[2] = "MGunHispanoMKIIk 162";
//        as10[3] = "MGunHispanoMKIIk 152";
//        as10[12] = "PylonTYPHOONPLN3MkII";
//        as10[13] = "PylonTYPHOONPLN4MkII";
//        as10[14] = "RocketGunHVAR5BEAUSALVO 1";
//        as10[15] = "RocketGunHVAR5BEAUSALVO 1";
//        as10[16] = "RocketGunHVAR5BEAUSALVO 1";
//        as10[17] = "RocketGunHVAR5BEAUSALVO 1";
//        as10[18] = "RocketGunHVAR5BEAUSALVO 1";
//        as10[19] = "RocketGunHVAR5BEAUSALVO 1";
//        as10[22] = "RocketGunHVAR5BEAUSALVO 1";
//        as10[23] = "RocketGunHVAR5BEAUSALVO 1";
//        Aircraft.weaponsRegister(class1, "(dec44)_8x60lbs_MkIIrack_salvo", as10);
//        String as11[] = new String[26];
//        as11[0] = "MGunHispanoMKIIk 152";
//        as11[1] = "MGunHispanoMKIIk 162";
//        as11[2] = "MGunHispanoMKIIk 162";
//        as11[3] = "MGunHispanoMKIIk 152";
//        as11[12] = "PylonTYPHOONPLN3MkII";
//        as11[13] = "PylonTYPHOONPLN4MkII";
//        as11[14] = "RocketGunHVAR2 1";
//        as11[15] = "RocketGunHVAR2 1";
//        as11[16] = "RocketGunHVAR2 1";
//        as11[17] = "RocketGunHVAR2 1";
//        as11[18] = "RocketGunHVAR2 1";
//        as11[19] = "RocketGunHVAR2 1";
//        as11[22] = "RocketGunHVAR2 1";
//        as11[23] = "RocketGunHVAR2 1";
//        Aircraft.weaponsRegister(class1, "(dec44)_8x5thinAP_MkIIrack", as11);
//        String as12[] = new String[26];
//        as12[0] = "MGunHispanoMKIIk 152";
//        as12[1] = "MGunHispanoMKIIk 162";
//        as12[2] = "MGunHispanoMKIIk 162";
//        as12[3] = "MGunHispanoMKIIk 152";
//        as12[12] = "PylonTYPHOONPLN3MkII";
//        as12[13] = "PylonTYPHOONPLN4MkII";
//        as12[14] = "RocketGunHVAR5BEAU25AS 1";
//        as12[15] = "RocketGunHVAR5BEAU25AS 1";
//        as12[16] = "RocketGunHVAR5BEAU25AS 1";
//        as12[17] = "RocketGunHVAR5BEAU25AS 1";
//        as12[18] = "RocketGunHVAR5BEAU25AS 1";
//        as12[19] = "RocketGunHVAR5BEAU25AS 1";
//        as12[22] = "RocketGunHVAR5BEAU25AS 1";
//        as12[23] = "RocketGunHVAR5BEAU25AS 1";
//        Aircraft.weaponsRegister(class1, "(dec44)_8x25lbs(AntiShip)_MkIIrack", as12);
//        String as13[] = new String[26];
//        as13[0] = "MGunHispanoMKIIk 152";
//        as13[1] = "MGunHispanoMKIIk 162";
//        as13[2] = "MGunHispanoMKIIk 162";
//        as13[3] = "MGunHispanoMKIIk 152";
//        as13[12] = "PylonTYPHOONPLN3MkII";
//        as13[13] = "PylonTYPHOONPLN4MkII";
//        as13[14] = "RocketGunHVAR5BEAU 1";
//        as13[15] = "RocketGunHVAR5BEAU 1";
//        as13[16] = "RocketGunHVAR5BEAU25AP 1";
//        as13[17] = "RocketGunHVAR5BEAU25AP 1";
//        as13[18] = "RocketGunHVAR5BEAU 1";
//        as13[19] = "RocketGunHVAR5BEAU 1";
//        as13[22] = "RocketGunHVAR5BEAU25AP 1";
//        as13[23] = "RocketGunHVAR5BEAU25AP 1";
//        Aircraft.weaponsRegister(class1, "(dec44)_4x60lbs_4x25lbs(AntiPers)_MkIIrack", as13);
//        String as14[] = new String[26];
//        as14[0] = "MGunHispanoMKIIk 152";
//        as14[1] = "MGunHispanoMKIIk 162";
//        as14[2] = "MGunHispanoMKIIk 162";
//        as14[3] = "MGunHispanoMKIIk 152";
//        as14[12] = "PylonTYPHOONPLN3MkII";
//        as14[13] = "PylonTYPHOONPLN4MkII";
//        as14[14] = "RocketGunHVAR5BEAU25AP 1";
//        as14[15] = "RocketGunHVAR5BEAU25AP 1";
//        as14[16] = "RocketGunHVAR5BEAU 1";
//        as14[17] = "RocketGunHVAR5BEAU 1";
//        as14[18] = "RocketGunHVAR5BEAU25AS 1";
//        as14[19] = "RocketGunHVAR5BEAU25AS 1";
//        as14[22] = "RocketGunHVAR5BEAU25AS 1";
//        as14[23] = "RocketGunHVAR5BEAU25AS 1";
//        Aircraft.weaponsRegister(class1, "(dec44)_4x25lbs(AntiShip)_2x25lbs(AntiPers)_2x60lbs_MkIIrack", as14);
//        String as15[] = new String[26];
//        as15[0] = "MGunHispanoMKIIk 0";
//        as15[1] = "MGunHispanoMKIIk 0";
//        as15[2] = "MGunHispanoMKIIk 0";
//        as15[3] = "MGunHispanoMKIIk 0";
//        Aircraft.weaponsRegister(class1, "none", as15);
//        String as16[] = new String[26];
//        as16[0] = "MGunHispanoMKIIk 152";
//        as16[1] = "MGunHispanoMKIIk 162";
//        as16[2] = "MGunHispanoMKIIk 162";
//        as16[3] = "MGunHispanoMKIIk 152";
//        as16[8] = "PylonTEMPESTPLN1 1";
//        as16[9] = "PylonTEMPESTPLN2 1";
//        as16[10] = "BombGun500lbsE 1";
//        as16[11] = "BombGun500lbsE 1";
//        Aircraft.weaponsRegister(class1, "(late43)_2x500E", as16);
    }
}
