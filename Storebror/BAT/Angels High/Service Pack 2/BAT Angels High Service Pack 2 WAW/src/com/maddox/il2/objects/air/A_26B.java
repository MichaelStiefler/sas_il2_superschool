package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.World;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class A_26B extends A_26 implements TypeStormovik, TypeStormovikArmored {

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        float fClip = 0.0F;
        switch (i) {
            default:
                break;

            case 0:
                fClip = (Math.abs(f) / 66F) * 2.5F;
                if (f1 > fClip) {
                    f1 = fClip;
                    flag = false;
                }
                if (f1 < -70F) {
                    f1 = -70F;
                    flag = false;
                }
                break;

            case 1:
                if (Math.abs(f) > 170F) {
                    fClip += Math.min(Math.abs(f) - 170F, 4F);
                }
                if (Math.abs(f) < 9F) {
                    fClip += ((9F - Math.abs(f)) / 9F) * 25F;
                }
                if (f1 < -70F) {
                    f1 = -70F;
                    flag = false;
                }
                if (f1 < fClip) {
                    f1 = fClip;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int paramInt, float paramFloat) {
        switch (paramInt) {
            case 1:
                super.FM.turret[0].setHealth(paramFloat);
                super.FM.turret[1].setHealth(paramFloat);
                break;
        }
    }

    public void rareAction(float paramFloat, boolean paramBoolean) {
        super.rareAction(paramFloat, paramBoolean);
        if (paramBoolean) {
            if ((this.FM.AS.astateEngineStates[0] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 0, 1);
            }
            if ((this.FM.AS.astateEngineStates[1] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 1, 1);
            }
            if ((this.FM.AS.astateEngineStates[2] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 2, 1);
            }
            if ((this.FM.AS.astateEngineStates[3] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 3, 1);
            }
        }
        for (int i = 1; i < 4; i++) {
            if (super.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(int i) {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
        try {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 400);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 450);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 450);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 450);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 450);
            for (int j = 18; j < 99; j++) {
                a_lweaponslot[j] = null;
            }

        } catch (Exception exception) {
        }
        return a_lweaponslot;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        if (super.thisWeaponsName.startsWith("PTO")) {
            this.FM.M.massEmpty -= 100F;
            this.FM.M.fuel += 260F;
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            super.FM.turret[0].setHealth(0.0F);
        } else {
            this.hierMesh().chunkVisible("Turret1A_D0", true);
            this.hierMesh().chunkVisible("Turret1B_D0", true);
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = A_26B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-26B");
        Property.set(class1, "meshName", "3DO/Plane/A26-Invader(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/A-26B.fmd:A26B_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA_26B.class, CockpitA26B_BGunner.class, CockpitA26B_TGunner.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 11, 11, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_MGUN17", "_MGUN18", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03",
                "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalDev17", "_ExternalDev18", "_ExternalR01", "_ExternalR02", "_ExternalR03", "_ExternalR04", "_ExternalR05", "_ExternalR06", "_ExternalR07", "_ExternalR08", "_ExternalR09", "_ExternalR10", "_ExternalR11", "_ExternalR12", "_ExternalR13", "_ExternalR14" });
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            int int1 = 99;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(int1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x1000lbs";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x500lbs";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16x100lbs";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk13 Torpedo";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunTorpMk13", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunTorpMk13", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "14xHVAR5";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xHVAR5+2x175Napalm";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGun175Napalm", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGun175Napalm", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xHVAR5+2x154DT";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank154gal", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank154gal", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x1000lbs+10xHVAR5";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x500lbs+10xHVAR5";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x500lbs+10xHVAR5+2x175Napalm";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGun175Napalm", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGun175Napalm", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16x100lbs+10xHVAR5";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16x100lbs+10xHVAR5+2x175Napalm";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGun175Napalm", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGun175Napalm", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x500lbsExternal+10xHVAR5";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x250lbsExternal+10xHVAR5";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x100lbsExternal+10xHVAR5";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x500lbs+4x500lbsExternal";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x500lbs+4x500lbsExternal+2x154DT";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank154gal", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank154gal", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x500lbs+4x500lbsExternal+2x175Napalm";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGun175Napalm", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGun175Napalm", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x154DT+4x1000lbs";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "FuelTankGun_Tank154gal", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "FuelTankGun_Tank154gal", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "PTO Default";
            a_lweaponslot = GenerateDefaultConfig(int1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "PTO 8xHVAR5";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "PTO 2x154DT+8xHVAR5";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "FuelTankGun_Tank154gal", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "FuelTankGun_Tank154gal", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "PTO 4x1000lbs";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "PTO 6x500lbs";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "PTO 16x100lbs";
            a_lweaponslot = GenerateDefaultConfig(int1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGun100lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[int1];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 0);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 0);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 0);
            for (int i = 18; i < int1; i++) {
                a_lweaponslot[i] = null;
            }

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
        }
    }
}
