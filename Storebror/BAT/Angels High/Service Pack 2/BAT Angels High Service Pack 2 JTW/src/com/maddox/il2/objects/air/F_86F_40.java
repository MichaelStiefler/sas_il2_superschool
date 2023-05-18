// Origin of this file: UNKNOWN!

package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;

public class F_86F_40 extends F_86F implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit {

    public F_86F_40() {
        this.guidedMissileUtils = new GuidedMissileUtils(this);
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

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.dvCockpitDoor = 0.5F;
        }
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        super.update(f);
        if (this.FM.getSpeed() > 5F) {
            this.moveSlats(f);
            this.bSlatsOff = false;
        } else {
            this.slatsOff();
        }
    }

    protected void moveSlats(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.15F);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 0.1F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.065F);
        this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        this.hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.1F);
        this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        this.hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void slatsOff() {
        if (!this.bSlatsOff) {
            this.resetYPRmodifier();
            Aircraft.xyz[0] = -0.15F;
            Aircraft.xyz[1] = 0.1F;
            Aircraft.xyz[2] = -0.065F;
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 8.5F, 0.0F);
            this.hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[1] = -0.1F;
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 8.5F, 0.0F);
            this.hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
            this.bSlatsOff = true;
        }
    }

    private GuidedMissileUtils guidedMissileUtils;
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
    public boolean             bToFire;

    static {
        Class class1 = F_86F_40.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-86");
        Property.set(class1, "meshName", "3DO/Plane/F-86F(Multi1)/hierF40.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "meshName_us", "3DO/Plane/F-86F(USA)/hierF40.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1949.9F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/F-86F-40.fmd:JETERA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_86Flate.class });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 9, 2, 2, 9, 2, 2, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04", "_ExternalBomb04", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalBomb07" });
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 49;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x120dt+2x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x75nap";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x75nap+2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x75nap+2x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82+2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82+2x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82+2xM117";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM117";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM117+2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM117+2x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+2x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16xHVAR";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xHVAR+2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xHVAR+2x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9B";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9B";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9B+2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9B+2x207dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunBrowningM3", 270);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunBrowningM3b", 270);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowningM3c", 270);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galL", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank207galR", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "RocketGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
        }
    }
}
