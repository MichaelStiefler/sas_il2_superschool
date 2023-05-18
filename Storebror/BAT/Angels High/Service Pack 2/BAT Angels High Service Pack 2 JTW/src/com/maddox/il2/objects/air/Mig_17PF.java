// Origin of this file: UNKNOWN!

package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Finger;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.util.HashMapInt;

public class Mig_17PF extends Mig_17 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeRadarGunsight {

    public Mig_17PF() {
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
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
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.smplSirena.setInfinite(true);
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
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

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (super.FM.isPlayers()) {
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.dvCockpitDoor = 0.5F;
        }
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(8, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        super.update(f);
        this.typeFighterAceMakerRangeFinder();
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            if ((this.FM.EI.engines[0].getThrustOutput() > 0.5F) && (this.FM.EI.engines[0].getStage() == 6)) {
                if (this.FM.EI.engines[0].getThrustOutput() > 1.001F) {
                    this.FM.AS.setSootState(this, 0, 5);
                } else {
                    this.FM.AS.setSootState(this, 0, 3);
                }
            } else {
                this.FM.AS.setSootState(this, 0, 0);
            }
        }
    }

    private GuidedMissileUtils guidedMissileUtils;
    private Sample             smplSirena;
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
    private static final float NEG_G_TOLERANCE_FACTOR = 1F;
    private static final float NEG_G_TIME_FACTOR      = 1F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 1.8F;
    private static final float POS_G_TIME_FACTOR      = 1.5F;
    private static final float POS_G_RECOVERY_FACTOR  = 1F;

    static {
        Class class1 = Mig_17PF.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-17PF");
        Property.set(class1, "meshName_ru", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar1956());
        Property.set(class1, "meshName_sk", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_ro", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_hu", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName", "3DO/Plane/MiG-17PF(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1952.11F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-17PF.fmd:JETERA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMig_17PF.class, CockpitMig_17Radar.class });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev07", "_ExternalDev08", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31",
                "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalDev17", "_ExternalDev13", "_ExternalDev14", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46", "_ExternalRock47", "_ExternalRock48", "_ExternalRock49", "_ExternalRock50", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalRock57", "_ExternalRock58", "_ExternalRock59", "_ExternalRock60", "_ExternalRock61", "_ExternalRock62", "_ExternalRock63", "_ExternalRock64", "_ExternalRock65", "_ExternalRock66", "_ExternalRock67", "_ExternalRock68", "_ExternalRock69", "_ExternalRock70" });
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 103;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunN37ki", 40);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDroptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunN37ki", 40);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FTGunL", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FTGunR", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xORO57";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunN37ki", 40);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xORO57+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunN37ki", 40);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FTGunL", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FTGunR", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xORO57";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunN37ki", 40);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xORO57+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunN37ki", 40);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FTGunL", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FTGunR", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonORO57", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMARS2";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunN37ki", 40);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "PylonMARS2", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "PylonMARS2", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMARS2+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunN37ki", 40);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNS23ki", 80);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FTGunL", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FTGunR", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "PylonMARS2", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "PylonMARS2", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS5", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xK5M";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xK5M+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FTGunL", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FTGunR", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xK5Mf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xK5Mf+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FTGunL", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FTGunR", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR55";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR55+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FTGunL", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FTGunR", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR55", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR55f";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunR55f", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR55f", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunR55f", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR55f", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR55f+2xDt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunNull", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FTGunL", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FTGunR", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunR55f", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunR55f", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunR55f", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunR55f", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
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
