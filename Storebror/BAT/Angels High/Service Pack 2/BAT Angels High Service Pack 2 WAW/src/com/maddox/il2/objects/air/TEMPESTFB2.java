package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class TEMPESTFB2 extends TEMPEST {

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        this.hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    static {
        Class class1 = TEMPESTFB2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Tempest");
        Property.set(class1, "meshName", "3DO/Plane/TempestFBMkII(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_gb", "3DO/Plane/TempestFBMkII(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/TempestMkII.fmd:TYPHOON_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTemp5.class });
        Property.set(class1, "LOSElevation", 0.93655F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16" });
//        String as[] = new String[28];
//        as[0] = "MGunHispanoMKVk 200";
//        as[1] = "MGunHispanoMKVk 200";
//        as[2] = "MGunHispanoMKVk 200";
//        as[3] = "MGunHispanoMKVk 200";
//        Aircraft.weaponsRegister(class1, "default", as);
//        String as1[] = new String[28];
//        as1[0] = "MGunHispanoMKVk 200";
//        as1[1] = "MGunHispanoMKVk 200";
//        as1[2] = "MGunHispanoMKVk 200";
//        as1[3] = "MGunHispanoMKVk 200";
//        as1[4] = "FuelTankGun_TankTempest";
//        as1[5] = "FuelTankGun_TankTempest";
//        Aircraft.weaponsRegister(class1, "2xdt", as1);
//        String as2[] = new String[28];
//        as2[0] = "MGunHispanoMKVk 200";
//        as2[1] = "MGunHispanoMKVk 200";
//        as2[2] = "MGunHispanoMKVk 200";
//        as2[3] = "MGunHispanoMKVk 200";
//        as2[6] = "PylonTEMPESTPLN1";
//        as2[7] = "PylonTEMPESTPLN2";
//        as2[8] = "BombGun250lbsE 1";
//        as2[9] = "BombGun250lbsE 1";
//        Aircraft.weaponsRegister(class1, "2x250", as2);
//        String as3[] = new String[28];
//        as3[0] = "MGunHispanoMKVk 200";
//        as3[1] = "MGunHispanoMKVk 200";
//        as3[2] = "MGunHispanoMKVk 200";
//        as3[3] = "MGunHispanoMKVk 200";
//        as3[6] = "PylonTEMPESTPLN1";
//        as3[7] = "PylonTEMPESTPLN2";
//        as3[8] = "BombGun500lbsE 1";
//        as3[9] = "BombGun500lbsE 1";
//        Aircraft.weaponsRegister(class1, "2x500", as3);
//        String as4[] = new String[28];
//        as4[0] = "MGunHispanoMKVk 200";
//        as4[1] = "MGunHispanoMKVk 200";
//        as4[2] = "MGunHispanoMKVk 200";
//        as4[3] = "MGunHispanoMKVk 200";
//        as4[6] = "PylonTEMPESTPLN1";
//        as4[7] = "PylonTEMPESTPLN2";
//        as4[8] = "BombGun500lbsEMC 1";
//        as4[9] = "BombGun500lbsEMC 1";
//        Aircraft.weaponsRegister(class1, "2x500MC", as4);
//        String as5[] = new String[28];
//        as5[0] = "MGunHispanoMKVk 200";
//        as5[1] = "MGunHispanoMKVk 200";
//        as5[2] = "MGunHispanoMKVk 200";
//        as5[3] = "MGunHispanoMKVk 200";
//        as5[6] = "PylonTEMPESTPLN1";
//        as5[7] = "PylonTEMPESTPLN2";
//        as5[8] = "BombGun1000lbsE_MC 1";
//        as5[9] = "BombGun1000lbsE_MC 1";
//        Aircraft.weaponsRegister(class1, "2x1000", as5);
//        String as6[] = new String[28];
//        as6[0] = "MGunHispanoMKVk 200";
//        as6[1] = "MGunHispanoMKVk 200";
//        as6[2] = "MGunHispanoMKVk 200";
//        as6[3] = "MGunHispanoMKVk 200";
//        as6[6] = "PylonTEMPESTPLN1";
//        as6[7] = "PylonTEMPESTPLN2";
//        as6[8] = "BombGun1000lbs 1";
//        as6[9] = "BombGun1000lbs 1";
//        Aircraft.weaponsRegister(class1, "2x1000US_Bombs", as6);
//        String as7[] = new String[28];
//        as7[0] = "MGunHispanoMKVk 200";
//        as7[1] = "MGunHispanoMKVk 200";
//        as7[2] = "MGunHispanoMKVk 200";
//        as7[3] = "MGunHispanoMKVk 200";
//        as7[10] = "PylonTEMPESTPLN3";
//        as7[11] = "PylonTEMPESTPLN4";
//        as7[12] = "RocketGunHVAR5BEAU 1";
//        as7[13] = "RocketGunHVAR5BEAU 1";
//        as7[14] = "RocketGunHVAR5BEAU 1";
//        as7[15] = "RocketGunHVAR5BEAU 1";
//        as7[16] = "RocketGunHVAR5BEAU 1";
//        as7[17] = "RocketGunHVAR5BEAU 1";
//        as7[18] = "RocketGunHVAR5BEAU 1";
//        as7[19] = "RocketGunHVAR5BEAU 1";
//        Aircraft.weaponsRegister(class1, "8x5", as7);
//        String as8[] = new String[28];
//        as8[0] = "MGunHispanoMKVk 200";
//        as8[1] = "MGunHispanoMKVk 200";
//        as8[2] = "MGunHispanoMKVk 200";
//        as8[3] = "MGunHispanoMKVk 200";
//        as8[10] = "PylonTEMPESTPLN3";
//        as8[11] = "PylonTEMPESTPLN4";
//        as8[12] = "RocketGunHVAR5BEAUSALVO 1";
//        as8[13] = "RocketGunHVAR5BEAUSALVO 1";
//        as8[14] = "RocketGunHVAR5BEAUSALVO 1";
//        as8[15] = "RocketGunHVAR5BEAUSALVO 1";
//        as8[16] = "RocketGunHVAR5BEAUSALVO 1";
//        as8[17] = "RocketGunHVAR5BEAUSALVO 1";
//        as8[18] = "RocketGunHVAR5BEAUSALVO 1";
//        as8[19] = "RocketGunHVAR5BEAUSALVO 1";
//        Aircraft.weaponsRegister(class1, "(Postwar)_8x60lbs_MkIIrack_salvo", as8);
//        Aircraft.weaponsRegister(class1, "(Postwar)_8x60lbs_MkVIIIrack", new String[] {
//            "MGunHispanoMKVk 200", "MGunHispanoMKVk 200", "MGunHispanoMKVk 200", "MGunHispanoMKVk 200", null, null, null, null, null, null,
//            "PylonTempMkVIII_L", "PylonTempMkVIII_R", null, null, null, null, null, null, null, null,
//            "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1"
//        });
//        Aircraft.weaponsRegister(class1, "(Postwar)_8x60lbs_MkVIIIrack_salvo", new String[] {
//            "MGunHispanoMKVk 200", "MGunHispanoMKVk 200", "MGunHispanoMKVk 200", "MGunHispanoMKVk 200", null, null, null, null, null, null,
//            "PylonTempMkVIII_L", "PylonTempMkVIII_R", null, null, null, null, null, null, null, null,
//            "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1", "RocketGunHVAR5BEAUSALVO 1"
//        });
//        Aircraft.weaponsRegister(class1, "none", new String[28]);
    }
}
