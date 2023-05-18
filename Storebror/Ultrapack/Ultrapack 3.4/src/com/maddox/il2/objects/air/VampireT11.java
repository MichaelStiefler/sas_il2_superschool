package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class VampireT11 extends Vampire {

    public void doMurderPilot(int i) {
        super.doMurderPilot(i);
        if (i == 1) {
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Head2_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("Pilot2_D1", true);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -150F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -91F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -87F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -91F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -87F), 0.0F);

//        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -90F), 0.0F);
//        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -110F), 0.0F);
//        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -90F), 0.0F);
//        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, 0.0F);
//        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -91F), 0.0F);
//        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -87F), 0.0F);
//        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -91F), 0.0F);
//        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -87F), 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (super.FM.getAltitude() < 500F) {
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    static {
        Class class1 = VampireT11.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Vampire");
        Property.set(class1, "meshName", "3DO/Plane/VampireT11(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/VampireT11.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitVampire.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
//        try
//        {
//            ArrayList arraylist = new ArrayList();
//            Property.set(class1, "weaponsList", arraylist);
//            HashMapInt hashmapint = new HashMapInt();
//            Property.set(class1, "weaponsMap", hashmapint);
//            byte byte0 = 16;
//            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
//            String s = "default";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[4] = null;
//            a_lweaponslot[5] = null;
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = null;
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = null;
//            a_lweaponslot[13] = null;
//            a_lweaponslot[14] = null;
//            a_lweaponslot[15] = null;
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "2xdpts";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Vamp2", 1);
//            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_Vamp2", 1);
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = null;
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = null;
//            a_lweaponslot[13] = null;
//            a_lweaponslot[14] = null;
//            a_lweaponslot[15] = null;
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "2xdpt_8xrkts";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Vamp2", 1);
//            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_Vamp2", 1);
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "8xrkts";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 150);
//            a_lweaponslot[4] = null;
//            a_lweaponslot[5] = null;
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5BEAU", 1);
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//            s = "none";
//            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
//            a_lweaponslot[0] = null;
//            a_lweaponslot[1] = null;
//            a_lweaponslot[2] = null;
//            a_lweaponslot[3] = null;
//            a_lweaponslot[4] = null;
//            a_lweaponslot[5] = null;
//            a_lweaponslot[6] = null;
//            a_lweaponslot[7] = null;
//            a_lweaponslot[8] = null;
//            a_lweaponslot[9] = null;
//            a_lweaponslot[10] = null;
//            a_lweaponslot[11] = null;
//            a_lweaponslot[12] = null;
//            a_lweaponslot[13] = null;
//            a_lweaponslot[14] = null;
//            a_lweaponslot[15] = null;
//            arraylist.add(s);
//            hashmapint.put(Finger.Int(s), a_lweaponslot);
//        }
//        catch(Exception exception) { }
    }
}
