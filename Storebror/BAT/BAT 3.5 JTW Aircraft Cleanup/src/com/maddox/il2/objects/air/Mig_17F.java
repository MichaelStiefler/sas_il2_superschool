package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class Mig_17F extends Mig_17 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeRadarGunsight {

    public Mig_17F() {
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.fxSirena = this.newSound("aircraft.Sirena2", false);
        this.smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        this.sirenaSoundPlaying = false;
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
        gfactors.setGFactors(Mig_17F.NEG_G_TOLERANCE_FACTOR, Mig_17F.NEG_G_TIME_FACTOR, Mig_17F.NEG_G_RECOVERY_FACTOR, Mig_17F.POS_G_TOLERANCE_FACTOR, Mig_17F.POS_G_TIME_FACTOR, Mig_17F.POS_G_RECOVERY_FACTOR);
//    private static final float NEG_G_TOLERANCE_FACTOR = 1.0F;
//    private static final float NEG_G_TIME_FACTOR = 1.0F;
//    private static final float NEG_G_RECOVERY_FACTOR = 1.0F;
//    private static final float POS_G_TOLERANCE_FACTOR = 4.5F;
//    private static final float POS_G_TIME_FACTOR = 1.5F;
//    private static final float POS_G_RECOVERY_FACTOR = 1.0F;
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

    private boolean sirenaWarning() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j += 360;
        }
        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
        if ((aircraft1 instanceof Aircraft) && (aircraft1.getArmy() != World.getPlayerArmy()) && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && (aircraft1 != World.getPlayerAircraft()) && (aircraft1.getSpeed(vector3d) > 20D)) {
            this.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().z;
            new String();
            new String();
            double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float) Math.atan2(d8, -d7);
            int k = (int) (Math.floor((int) f) - 90D);
            if (k < 0) {
                k += 360;
            }
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int) (Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)) / 10D) * 10D);
            float f1 = 57.32484F * (float) Math.atan2(i1, d11);
            int j1 = (int) (Math.floor((int) f1) - 90D);
            if (j1 < 0) {
                j1 += 360;
            }
            int k1 = j1 - j;
            int l1 = (int) (Math.ceil((i1 * 3.2808399000000001D) / 100D) * 100D);
            if (l1 >= 5280) {
                l1 = (int) Math.floor(l1 / 5280);
            }
            this.bRadarWarning = (i1 <= 3000D) && (i1 >= 50D) && (k1 >= 195) && (k1 <= 345) && (Math.sqrt(l * l) >= 120D);
            this.playSirenaWarning(this.bRadarWarning);
        } else {
            this.bRadarWarning = false;
            this.playSirenaWarning(this.bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag) {
        if (flag && !this.sirenaSoundPlaying) {
            this.fxSirena.play(this.smplSirena);
            this.sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
        } else if (!flag && this.sirenaSoundPlaying) {
            this.fxSirena.cancel();
            this.sirenaSoundPlaying = false;
        }
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

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
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
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        super.update(f);
        this.computeVk1();
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
        if ((World.getPlayerRegiment().country().equalsIgnoreCase("ru") || World.getPlayerRegiment().country().equalsIgnoreCase("nv")) && (Mission.curYear() >= 1952)) {
            this.sirenaWarning();
        }
    }

    public void computeVk1() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            this.FM.producedAF.x += 5400D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 10D) {
                f1 = 5.4F;
            } else {
                f1 = 0.54F * f;
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    private GuidedMissileUtils guidedMissileUtils;
    private SoundFX            fxSirena;
    private Sample             smplSirena;
    private boolean            sirenaSoundPlaying;
    private boolean            bRadarWarning;
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
    private static final float POS_G_TOLERANCE_FACTOR = 4.5F;
    private static final float POS_G_TIME_FACTOR      = 1.5F;
    private static final float POS_G_RECOVERY_FACTOR  = 1F;

    static {
        Class class1 = Mig_17F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-17AS");
        Property.set(class1, "meshName_ru", "3DO/Plane/MiG-17F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar1956());
        Property.set(class1, "meshName_sk", "3DO/Plane/MiG-17F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_ro", "3DO/Plane/MiG-17F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_hu", "3DO/Plane/MiG-17F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName", "3DO/Plane/MiG-17F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1952.11F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-17F.fmd:JETERA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMig_17.class });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 0, 0, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev07", "_ExternalDev08", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31",
                "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalDev13", "_ExternalDev14", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46", "_ExternalRock47", "_ExternalRock48", "_ExternalRock49", "_ExternalRock50", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalRock57", "_ExternalRock58", "_ExternalRock59", "_ExternalRock60", "_ExternalRock61", "_ExternalRock62", "_ExternalRock63", "_ExternalRock64", "_ExternalRock65", "_ExternalRock66", "_ExternalRock67", "_ExternalRock68", "_ExternalRock69", "_ExternalRock70", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04" });
    }
}
