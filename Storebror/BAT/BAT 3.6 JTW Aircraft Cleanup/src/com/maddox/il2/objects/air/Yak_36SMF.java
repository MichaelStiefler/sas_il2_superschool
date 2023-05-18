package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.FuelTankGun_Tankyak;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGunR13MS;
import com.maddox.il2.objects.weapons.RocketGunR3RL;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class Yak_36SMF extends Yak_36MF implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeDockable {

    public Yak_36SMF() {
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
        this.hasIR = false;
        this.hasPRHM = false;
    }

    public float getFlowRate() {
        return Yak_36SMF.FlowRate;
    }

    public float getFuelReserve() {
        return Yak_36SMF.FuelReserve;
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
        gfactors.setGFactors(Yak_36SMF.NEG_G_TOLERANCE_FACTOR, Yak_36SMF.NEG_G_TIME_FACTOR, Yak_36SMF.NEG_G_RECOVERY_FACTOR, Yak_36SMF.POS_G_TOLERANCE_FACTOR, Yak_36SMF.POS_G_TIME_FACTOR, Yak_36SMF.POS_G_RECOVERY_FACTOR);
//    private static final float NEG_G_TOLERANCE_FACTOR = 3.5F;
//    private static final float NEG_G_TIME_FACTOR = 1.5F;
//    private static final float NEG_G_RECOVERY_FACTOR = 2.5F;
//    private static final float POS_G_TOLERANCE_FACTOR = 8.2F;
//    private static final float POS_G_TIME_FACTOR = 3F;
//    private static final float POS_G_RECOVERY_FACTOR = 3.5F;
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

    private final void doRemoveTankL() {
        if (this.hierMesh().chunkFindCheck("TankL") != -1) {
            this.hierMesh().hideSubTrees("TankL");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("TankL"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemoveTankR() {
        if (this.hierMesh().chunkFindCheck("TankR") != -1) {
            this.hierMesh().hideSubTrees("TankR");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("TankR"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private void droptank() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] == null) {
                continue;
            }
            for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                if (!this.FM.CT.Weapons[i][j].haveBullets()) {
                    continue;
                }
                if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_Tankyak) {
                    this.havedroptank = true;
                    this.hierMesh().chunkVisible("TankL", true);
                    this.hierMesh().chunkVisible("TankR", true);
                }
                if (this.FM.CT.Weapons[i][j] instanceof RocketGunR13MS) {
                    this.hasIR = true;
                }
                if (this.FM.CT.Weapons[i][j] instanceof RocketGunR3RL) {
                    this.hasPRHM = true;
                }
            }

        }

    }

    private void AIswitchmissile() {
        if (((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) || !(this.FM instanceof Pilot)) {
            return;
        }
        Pilot pilot = (Pilot) this.FM;
        if (((pilot.get_maneuver() == 63) || (((Maneuver) (pilot)).target != null)) && this.hasIR) {
            Class class1 = com.maddox.il2.objects.weapons.MissileR3RL.class;
            this.guidedMissileUtils.changeMissileClass(class1);
        }
        if (((pilot.get_maneuver() != 63) || (((Maneuver) (pilot)).target == null)) && this.hasPRHM) {
            Class class2 = com.maddox.il2.objects.weapons.MissileR13MS.class;
            this.guidedMissileUtils.changeMissileClass(class2);
        }
    }

    public void update(float f) {
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
        this.guidedMissileUtils.update();
        this.AIswitchmissile();
        if (this.FM instanceof Maneuver) {
            this.receivingRefuel(f);
        }
        this.sirenaWarning();
        super.update(f);
        if (this.havedroptank && !this.FM.CT.Weapons[9][1].haveBullets()) {
            this.havedroptank = false;
            this.doRemoveTankL();
            this.doRemoveTankR();
        }
        this.updateDragChute();
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

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if ((this.queen_last != null) && (this.queen_last == actor) && ((this.queen_time == 0L) || (Time.current() < (this.queen_time + 5000L)))) {
            aflag[0] = false;
        } else {
            aflag[0] = true;
        }
    }

    public void missionStarting() {
        super.missionStarting();
        this.checkAsDrone();
    }

    private void checkAsDrone() {
        if (this.target_ == null) {
            if (this.FM.AP.way.curr().getTarget() == null) {
                this.FM.AP.way.next();
            }
            this.target_ = this.FM.AP.way.curr().getTarget();
            if (Actor.isValid(this.target_) && (this.target_ instanceof Wing)) {
                Wing wing = (Wing) this.target_;
                int i = this.aircIndex();
                if (Actor.isValid(wing.airc[i / 2])) {
                    this.target_ = wing.airc[i / 2];
                } else {
                    this.target_ = null;
                }
            }
        }
        if (Actor.isValid(this.target_) && (this.target_ instanceof TypeTankerDrogue)) {
            this.queen_last = this.target_;
            this.queen_time = Time.current();
            if (this.isNetMaster()) {
                ((TypeDockable) this.target_).typeDockableRequestAttach(this, this.aircIndex() % 2, true);
            }
        }
        this.bNeedSetup = false;
        this.target_ = null;
    }

    public int typeDockableGetDockport() {
        if (this.typeDockableIsDocked()) {
            return this.dockport_;
        } else {
            return -1;
        }
    }

    public Actor typeDockableGetQueen() {
        return this.queen_;
    }

    public boolean typeDockableIsDocked() {
        return Actor.isValid(this.queen_);
    }

    public void typeDockableAttemptAttach() {
        if (this.FM.AS.isMaster() && !this.typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
            if (aircraft instanceof TypeTankerDrogue) {
                ((TypeDockable) aircraft).typeDockableRequestAttach(this);
            }
        }
    }

    public void typeDockableAttemptDetach() {
        if (this.FM.AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_)) {
            ((TypeDockable) this.queen_).typeDockableRequestDetach(this);
        }
    }

    public void typeDockableRequestAttach(Actor actor) {
    }

    public void typeDockableRequestDetach(Actor actor) {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
    }

    public void typeDockableDoDetachFromDrone(int i) {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
        this.queen_ = (Aircraft) actor;
        this.dockport_ = i;
        this.queen_last = this.queen_;
        this.queen_time = 0L;
        this.FM.EI.setEngineRunning();
        this.FM.CT.setGearAirborne();
        this.moveGear(0.0F);
        FlightModel flightmodel = this.queen_.FM;
        if ((this.aircIndex() == 0) && (this.FM instanceof Maneuver) && (flightmodel instanceof Maneuver)) {
            Maneuver maneuver = (Maneuver) flightmodel;
            Maneuver maneuver1 = (Maneuver) this.FM;
            if ((maneuver.Group != null) && (maneuver1.Group != null) && (maneuver1.Group.numInGroup(this) == (maneuver1.Group.nOfAirc - 1))) {
                AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
                airgroup.leaderGroup = null;
                airgroup.clientGroup = maneuver.Group;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i) {
        if (this.dockport_ == i) {
            this.queen_last = this.queen_;
            this.queen_time = Time.current();
            this.queen_ = null;
            this.dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (this.typeDockableIsDocked()) {
            netmsgguaranted.writeByte(1);
            ActorNet actornet = null;
            if (Actor.isValid(this.queen_)) {
                actornet = this.queen_.net;
                if (actornet.countNoMirrors() > 0) {
                    actornet = null;
                }
            }
            netmsgguaranted.writeByte(this.dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        if (netmsginput.readByte() == 1) {
            this.dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null) {
                Actor actor = (Actor) netobj.superObj();
                ((TypeDockable) actor).typeDockableDoAttachToDrone(this, this.dockport_);
            }
        }
    }

    private void receivingRefuel(float f) {
        int i = this.aircIndex();
        if (this.typeDockableIsDocked()) {
            if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                ((Maneuver) this.FM).unblock();
                ((Maneuver) this.FM).set_maneuver(48);
                for (int j = 0; j < i; j++) {
                    ((Maneuver) this.FM).push(48);
                }

                if (this.FM.AP.way.curr().Action != 3) {
                    ((FlightModelMain) ((Maneuver) this.FM)).AP.way.setCur(this.queen_.FM.AP.way.Cur());
                }
                ((Pilot) this.FM).setDumbTime(3000L);
            }
            FuelTank afueltank[] = this.FM.CT.getFuelTanks();
            if (this.FM.M.fuel < (this.FM.M.maxFuel - 12F)) {
                float f1 = ((TypeTankerDrogue) this.queen_).requestRefuel(this, 11.101F, f);
                this.FM.M.fuel += f1;
            } else if ((afueltank.length > 0) && (afueltank[0] != null) && !this.FM.M.bFuelTanksDropped) {
                float f2 = ((TypeTankerDrogue) this.queen_).requestRefuel(this, 11.101F, f);
                float f3 = 0.0F;
                for (int k = 0; k < afueltank.length; k++) {
                    f3 += afueltank[k].checkFreeTankSpace();
                }

                if (f3 < 12F) {
                    this.typeDockableAttemptDetach();
                }
                for (int l = 0; l < afueltank.length; l++) {
                    afueltank[l].doRefuel(f2 * (afueltank[l].checkFreeTankSpace() / f3));
                }

            } else {
                this.typeDockableAttemptDetach();
            }
        } else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
            if ((this.FM.CT.GearControl == 0.0F) && (this.FM.EI.engines[0].getStage() == 0)) {
                this.FM.EI.setEngineRunning();
            }
            if ((this.dtime > 0L) && (((Maneuver) this.FM).Group != null)) {
                ((Maneuver) this.FM).Group.leaderGroup = null;
                ((Maneuver) this.FM).set_maneuver(22);
                ((Pilot) this.FM).setDumbTime(3000L);
                if (Time.current() > (this.dtime + 3000L)) {
                    this.dtime = -1L;
                    ((Maneuver) this.FM).clear_stack();
                    ((Maneuver) this.FM).set_maneuver(0);
                    ((Pilot) this.FM).setDumbTime(0L);
                }
            } else if (this.FM.AP.way.curr().Action == 0) {
                Maneuver maneuver = (Maneuver) this.FM;
                if ((maneuver.Group != null) && (maneuver.Group.airc[0] == this) && (maneuver.Group.clientGroup != null)) {
                    maneuver.Group.setGroupTask(2);
                }
            }
        }
    }

    public BulletEmitter       Weapons[][];
    private boolean            hasIR;
    private boolean            hasPRHM;
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
    private boolean            havedroptank;
    private Actor              queen_last;
    private long               queen_time;
    private boolean            bNeedSetup;
    private long               dtime;
    private Actor              target_;
    private Aircraft           queen_;
    private int                dockport_;

    static {
        Class class1 = Yak_36SMF.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak-36MF");
        Property.set(class1, "meshName", "3DO/Plane/Yak-36MF/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-36MF.fmd:yak36");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYak36MF.class });
        Property.set(class1, "LOSElevation", 0.965F);
        int ai[] = new int[61];
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
                "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_CANNON09" });
    }
}
