package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class Yak_36S extends Yak_36 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump {

    public Yak_36S() {
        this.fxSirena = this.newSound("aircraft.Sirena2", false);
        this.smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        this.guidedMissileUtils = null;
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
        this.removeChuteTimer = -1L;
        this.smplSirena.setInfinite(true);
    }

    public float getFlowRate() {
        return Yak_36S.FlowRate;
    }

    public float getFuelReserve() {
        return Yak_36S.FuelReserve;
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
        if (World.getPlayerAircraft() == null) {
            return false;
        }
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
            HUD.log(AircraftHotKeys.hudLogWeaponId, "AN/APR-36: Enemy at Six!");
        } else if (!flag && this.sirenaSoundPlaying) {
            this.fxSirena.cancel();
            this.sirenaSoundPlaying = false;
        }
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(Yak_36S.NEG_G_TOLERANCE_FACTOR, Yak_36S.NEG_G_TIME_FACTOR, Yak_36S.NEG_G_RECOVERY_FACTOR, Yak_36S.POS_G_TOLERANCE_FACTOR, Yak_36S.POS_G_TIME_FACTOR, Yak_36S.POS_G_RECOVERY_FACTOR);
//    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
//    private static final float NEG_G_TIME_FACTOR = 1.5F;
//    private static final float NEG_G_RECOVERY_FACTOR = 1.5F;
//    private static final float POS_G_TOLERANCE_FACTOR = 2.2F;
//    private static final float POS_G_TIME_FACTOR = 2.0F;
//    private static final float POS_G_RECOVERY_FACTOR = 1.5F;
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        this.sirenaWarning();
        if (this.FM.CT.getArrestor() > 0.2F) {
            this.calculateArrestor();
        }
        super.update(f);
        this.updateDragChute();
    }

    private void calculateArrestor() {
        if (this.FM.Gears.arrestorVAngle != 0.0F) {
            float f = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            this.arrestor = (0.8F * this.arrestor) + (0.2F * f);
            this.moveArrestorHook(this.arrestor);
        } else {
            float f1 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
            if ((f1 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            }
            if ((f1 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                f1 = 0.0F;
            }
            if (f1 > 0.2F) {
                f1 = 0.2F;
            }
            if (f1 > 0.0F) {
                this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f1));
            } else {
                this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f1));
            }
            if (this.arrestor < 0.0F) {
                this.arrestor = 0.0F;
            } else if (this.arrestor > 1.0F) {
                this.arrestor = 1.0F;
            }
            this.moveArrestorHook(this.arrestor);
        }
    }

    private void updateDragChute() {
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteF86/mono.sim");
            this.chute.collide(true);
            this.chute.mesh().setScale(0.5F);
            this.chute.pos.setRel(new Point3d(-5D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            this.bHasDeployedDragChute = true;
        } else if (this.bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl) {
            if (((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() > 600F)) || (this.FM.CT.DragChuteControl < 1.0F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                    this.chute.pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 250L;
            } else if ((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() < 20F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                }
                this.chute.pos.setRel(new Orient(0.0F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 10000L;
            }
        }
        if ((this.removeChuteTimer > 0L) && !this.FM.CT.bHasDragChuteControl && (Time.current() > this.removeChuteTimer)) {
            this.chute.destroy();
        }
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
    public static float        FlowRate               = 10F;
    public static float        FuelReserve            = 1500F;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR      = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR      = 2F;
    private static final float POS_G_RECOVERY_FACTOR  = 2F;
    public boolean             bToFire;
    private boolean            bHasDeployedDragChute;
    private Chute              chute;
    private long               removeChuteTimer;
    private float              arrestor;
    static {
        Class class1 = Yak_36S.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak-36");
        Property.set(class1, "meshName", "3DO/Plane/Yak-36/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-36.fmd:yak36");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYak_36.class });
        Property.set(class1, "LOSElevation", 0.965F);
        int ai[] = new int[57];
        ai[3] = 9;
        ai[4] = 9;
        ai[5] = 2;
        ai[6] = 2;
        ai[7] = 2;
        ai[8] = 2;
        ai[9] = 9;
        ai[10] = 9;
        ai[11] = 3;
        ai[12] = 3;
        ai[13] = 3;
        ai[14] = 3;
        ai[15] = 3;
        ai[16] = 3;
        ai[17] = 3;
        ai[18] = 3;
        ai[19] = 3;
        ai[20] = 3;
        ai[21] = 3;
        ai[22] = 3;
        ai[23] = 3;
        ai[24] = 3;
        ai[25] = 3;
        ai[26] = 3;
        ai[27] = 9;
        ai[28] = 9;
        ai[29] = 3;
        ai[30] = 3;
        ai[31] = 9;
        ai[32] = 9;
        ai[33] = 9;
        ai[34] = 9;
        ai[35] = 3;
        ai[36] = 3;
        ai[37] = 9;
        ai[38] = 9;
        ai[39] = 9;
        ai[40] = 9;
        ai[41] = 9;
        ai[42] = 9;
        ai[43] = 2;
        ai[44] = 2;
        ai[45] = 2;
        ai[46] = 2;
        ai[47] = 2;
        ai[48] = 2;
        ai[49] = 2;
        ai[50] = 2;
        ai[51] = 2;
        ai[52] = 2;
        ai[53] = 2;
        ai[54] = 2;
        Aircraft.weaponTriggersRegister(class1, ai);
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev03", "_ExternalDev04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev05", "_ExternalDev06", "_ExternalRock21", "_ExternalRock22", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31",
                "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_CANNON04", "_CANNON05" });
    }
}
