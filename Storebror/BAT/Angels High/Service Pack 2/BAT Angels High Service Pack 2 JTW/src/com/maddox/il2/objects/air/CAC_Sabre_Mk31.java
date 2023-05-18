// Origin of this file: UNKNOWN!

package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;

public class CAC_Sabre_Mk31 extends F_86F implements TypeCountermeasure, TypeThreatDetector, TypeGSuit {

    public CAC_Sabre_Mk31() {
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
    }

    public long getChaffDeployed() {
        if (this.hasChaff) {
            return this.lastChaffDeployed;
        } else {
            return 0L;
        }
    }

    public long getFlareDeployed() {
        if (this.hasFlare) {
            return this.lastFlareDeployed;
        } else {
            return 0L;
        }
    }

    public void setCommonThreatActive() {
        long l = Time.current();
        if ((l - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = l;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long l = Time.current();
        if ((l - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = l;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long l = Time.current();
        if ((l - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = l;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (super.FM.isPlayers()) {
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.dvCockpitDoor = 0.5F;
        }
    }

    public void update(float f) {
        super.update(f);
        this.engineSurge(f);
    }

    private boolean            hasChaff;
    private boolean            hasFlare;
    private long               lastChaffDeployed;
    private long               lastFlareDeployed;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR      = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR      = 2F;
    private static final float POS_G_RECOVERY_FACTOR  = 2F;

    static {
        Class class1 = CAC_Sabre_Mk31.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "CAC_Sabre");
        Property.set(class1, "meshName_gb", "3DO/Plane/CAC_Sabre_Mk32(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName", "3DO/Plane/CAC_Sabre_Mk32(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1949.9F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/CAC_Sabre_Mk31.fmd:JETERA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_86Flate.class });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 9, 2, 2, 9, 2, 2, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04", "_ExternalBomb04", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26",
                "_ExternalDev17", "_ExternalDev18", "_ExternalDev19", "_ExternalDev20", "_ExternalDev21", "_ExternalDev22", "_ExternalDev23", "_ExternalDev24", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalDev25", "_ExternalDev26", "_ExternalDev27", "_ExternalDev28", "_ExternalDev29", "_ExternalDev30", "_ExternalDev31", "_ExternalDev32" });
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 84;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xNap";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGun75Napalm", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "BombGun75Napalm", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGun500lbsMC", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "BombGun500lbsMC", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x1000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "BombGun1000lbsMC", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "BombGun1000lbsMC", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16xRP3Mk5";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16xRP3Mk5-25";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xRP3Mk5+2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xRP3Mk5-25+2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xSURA_D_HE";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xSURA_D_AP";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunADEN30ki", 150);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception1) {
        }
    }
}
