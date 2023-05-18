package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGunAGM65L;
import com.maddox.il2.objects.weapons.RocketGunFlare;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class F_16A_B10 extends F_16fuelReceiver implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector {

    public F_16A_B10() {
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
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
        this.missilesList = new ArrayList();
        this.IR = false;
        this.tX4Prev = 0L;
        this.backfireList = new ArrayList();
    }

    public void launchMsl() {
        if (this.missilesList.isEmpty()) {
            return;
        } else {
            ((RocketGunAGM65L) this.missilesList.remove(0)).shots(1);
            return;
        }
    }

    public void launchbmb() {
        if (this.missilesList.isEmpty()) {
            return;
        } else {
            ((RocketBombGun) this.missilesList.remove(0)).shots(1);
            return;
        }
    }

    public void backFire() {
        if (this.backfireList.isEmpty()) {
            return;
        } else {
            ((RocketGunFlare) this.backfireList.remove(0)).shots(3);
            return;
        }
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
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        this.droptank();
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
    }

    private void droptank() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] != null) {
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j].haveBullets()) {
                        if (this.FM.CT.Weapons[i][j] instanceof RocketGunFlare) {
                            this.backfireList.add(this.FM.CT.Weapons[i][j]);
                        }
                    }
                }

            }
        }

    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        this.computePW_AB();
        super.update(f);
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteF16/mono.sim");
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
                this.removeChuteTimer = Time.current() + 250L;
            } else if ((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() < 20F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                }
                this.chute.pos.setRel(new Orient(0.0F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.removeChuteTimer = Time.current() + 10000L;
            }
        }
        if ((this.removeChuteTimer > 0L) && !this.FM.CT.bHasDragChuteControl && (Time.current() > this.removeChuteTimer)) {
            this.chute.destroy();
        }
        this.AIgroundattack();
    }

    public void AIgroundattack() {
        if ((!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver) && (((Maneuver) this.FM).get_task() == 7) && !this.FM.AP.way.isLanding()) {
            if (this.missilesList.isEmpty() && !((Maneuver) this.FM).hasBombs()) {
                Pilot pilot = (Pilot) this.FM;
                Vector3d vector3d = new Vector3d();
                this.getSpeed(vector3d);
                Point3d point3d1 = new Point3d();
                this.pos.getAbs(point3d1);
                float f = (float) (this.FM.getAltitude() - World.land().HQ(point3d1.x, point3d1.y));
                if ((f < 55F) && (vector3d.z < 0.0D)) {
                    vector3d.z = 0.0D;
                } else if ((pilot != null) && Actor.isAlive(((Maneuver) (pilot)).target_ground)) {
                    Point3d point3d2 = new Point3d();
                    ((Maneuver) (pilot)).target_ground.pos.getAbs(point3d2);
                    pilot.set_maneuver(43);
                    if (this.pos.getAbsPoint().distance(point3d2) < 2000D) {
                        point3d2.sub(this.FM.Loc);
                        this.FM.Or.transformInv(point3d2);
                        this.FM.CT.PowerControl = 0.55F;
                    }
                }
                this.setSpeed(vector3d);
            } else if (!this.missilesList.isEmpty() && (Time.current() > (this.tX4Prev + 500L + (this.IR ? 10000L : 0L)))) {
                Pilot pilot1 = (Pilot) this.FM;
                if ((pilot1.get_maneuver() == 43) && (((Maneuver) (pilot1)).target_ground != null)) {
                    Point3d point3d = new Point3d();
                    ((Maneuver) (pilot1)).target_ground.pos.getAbs(point3d);
                    point3d.sub(this.FM.Loc);
                    this.FM.Or.transformInv(point3d);
                    if ((point3d.x > 1000D) && (point3d.x < ((this.IR ? 2250D : 1250D) + (250D * this.FM.Skill)))) {
                        if (!this.IR) {
                            point3d.x /= 2 - (this.FM.Skill / 3);
                        }
                        if ((point3d.y < point3d.x) && (point3d.y > -point3d.x) && ((point3d.z * 1.5D) < point3d.x) && ((point3d.z * 1.5D) > -point3d.x)) {
                            this.launchMsl();
                            this.tX4Prev = Time.current();
                            Voice.speakAttackByRockets(this);
                        }
                    }
                }
            }
        }
    }

    public void computePW_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 34930D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 17.3D) {
                f1 = 33F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = (((0.0020439F * f4) - (0.048507F * f3)) + (0.353167F * f2)) - (0.267366F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public BulletEmitter       Weapons[][];
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
    public boolean             bToFire;
    private boolean            bHasDeployedDragChute;
    private Chute              chute;
    private long               removeChuteTimer;
    private ArrayList          backfireList;
    private long               tX4Prev;
    private boolean            IR;
    private ArrayList          missilesList;

    static {
        Class class1 = F_16A_B10.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Viper");
        Property.set(class1, "meshName", "3DO/Plane/F-16Viper(Mult10)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1973.3F);
        Property.set(class1, "yearExpired", 1999.3F);
        Property.set(class1, "FlightModel", "FlightModels/F-16A_B10.fmd:f16_fm");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_16A_B10.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 3, 3, 3, 3, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalDev09", "_Dev10", "_ExternalDev11", "_Dev12", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25",
                "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalBomb29", "_ExternalDev19", "_ExternalDev20" });
    }
}
