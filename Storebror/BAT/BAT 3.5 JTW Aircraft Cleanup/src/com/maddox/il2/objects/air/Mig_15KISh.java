package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class Mig_15KISh extends Mig_15K implements TypeStormovik, TypeGuidedMissileCarrier, TypeX4Carrier, TypeCountermeasure, TypeThreatDetector, TypeZBReceiver, TypeDockable, TypeRadarGunsight {

    public Mig_15KISh() {
        this.FlapAngle = 55F;
        this.hasDroptanks = false;
        this.hasMissiles = false;
        this.hasBombs = false;
        this.hasKAB = false;
        this.hasBoosters = false;
        this.ProbeOut = false;
        this.Ratos = 0;
        this.boosterFireOutTime = -1L;
        this.overrideBailout = false;
        this.ejectComplete = false;
        this.lTimeNextEject = 0L;
        this.guidedMissileUtils = null;
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
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
        this.fxKAB = this.newSound("weapon.K5.lock", false);
        this.smplKAB = new Sample("K5_lock.wav", 256, 65535);
        this.smplKAB.setInfinite(true);
        this.KABSoundPlaying = false;
        this.KABEngaged = false;
        this.KAB = 0;
        this.APmode5 = false;
        this.ExtFuel = 0.0F;
        this.TwoTanks = true;
        this.APmode6 = false;
        this.APmode7 = false;
    }

    public void typeFighterAceMakerRangeFinder() {
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 0.002F;
        this.k14WingspanType--;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -0.002F;
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -0.002F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
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

    public void moveRefuel(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, -0.8F);
        this.hierMesh().chunkSetLocate("ExtrasProbe", Aircraft.xyz, Aircraft.ypr);
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
        if (this.ProbeOut) {
            this.moveRefuel(-90F);
            this.ProbeOut = false;
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
            if (this.ProbeOut) {
                this.moveRefuel(-90F);
                this.ProbeOut = false;
            }
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

    private boolean KABscan() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i = 360 + i;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j = 360 + j;
        }
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft())) {
                this.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                Math.floor(actor.pos.getAbsPoint().z * 0.1D);
                Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D);
                double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float) Math.atan2(d8, -d7);
                int i1 = (int) (Math.floor((int) f) - 90D);
                if (i1 < 0) {
                    i1 = 360 + i1;
                }
                int j1 = i1 - i;
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.sqrt(d6 * d6);
                int k1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                int l1 = (int) (Math.floor((int) f1) - 90D);
                if (l1 < 0) {
                    l1 = 360 + l1;
                }
                k1 = (int) (k1 / 1000D);
                int j2 = (int) Math.ceil(k1);
                byte byte0 = 9;
                if (actor instanceof ShipGeneric) {
                    byte0 = 40;
                }
                if (actor instanceof BigshipGeneric) {
                    byte0 = 60;
                }
                if (j1 < 0) {
                    j1 = 360 + j1;
                }
                double d12 = this.FM.getAltitude() / j2;
                if ((k1 <= byte0) && (j2 <= 15D)) {
                    if ((j1 <= 20D) || (j1 >= 340D)) {
                        if (d12 >= 325D) {
                            this.KABEngaged = true;
                            this.playKAB(this.KABEngaged);
                        } else {
                            this.KABEngaged = false;
                            this.playKAB(this.KABEngaged);
                        }
                    } else {
                        this.KABEngaged = false;
                        this.playKAB(this.KABEngaged);
                    }
                } else {
                    this.KABEngaged = false;
                    this.playKAB(this.KABEngaged);
                }
            }
        }

        return true;
    }

    public void playKAB(boolean flag) {
        if (flag && !this.KABSoundPlaying) {
            this.KABSoundPlaying = true;
            this.fxKAB.play(this.smplKAB);
            this.KAB = this.KAB + 1;
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "KAB-500M engaged");
            }
        } else if (!flag && this.KABSoundPlaying) {
            this.KABSoundPlaying = false;
            this.fxKAB.cancel();
            if ((this.KAB > 1) && (((Interpolate) (this.FM)).actor == World.getPlayerAircraft())) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "KAB-500M disengaged");
            }
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("01")) {
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
            this.scopemode = 1;
        }
        if (this.thisWeaponsName.startsWith("02")) {
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hasKAB = true;
            this.hasDroptanks = true;
            this.TwoTanks = false;
            this.FlapAngle = 20F;
            if (this.FM.M.fuel > 324F) {
                this.hasBoosters = true;
            }
        }
        if (this.thisWeaponsName.startsWith("03")) {
            this.hierMesh().chunkVisible("S1AAM", true);
            this.hierMesh().chunkVisible("S1RailAAM", true);
            this.hierMesh().chunkVisible("S8AAM", true);
            this.hierMesh().chunkVisible("S8RailAAM", true);
            this.hasMissiles = true;
            this.FlapAngle = 20F;
            this.scopemode = 1;
        }
        if (this.thisWeaponsName.startsWith("04")) {
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
        }
        if (this.thisWeaponsName.startsWith("05")) {
            this.hierMesh().chunkVisible("S4RAT", true);
            this.hierMesh().chunkVisible("S5PTB", true);
            this.hasDroptanks = true;
            this.TwoTanks = false;
            if (this.FM.M.fuel > 299F) {
                this.hasBoosters = true;
            }
            this.ORDmode = 3;
        }
        if (this.thisWeaponsName.startsWith("06")) {
            this.scopemode = 1;
        }
        if (this.thisWeaponsName.startsWith("07")) {
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
        }
        if (this.thisWeaponsName.startsWith("08")) {
            this.hierMesh().chunkVisible("S1AAM", true);
            this.hierMesh().chunkVisible("S1RailAAM", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
            this.hierMesh().chunkVisible("S8AAM", true);
            this.hierMesh().chunkVisible("S8RailAAM", true);
            this.ORDmode = 5;
        }
        if (this.thisWeaponsName.startsWith("09")) {
            this.hierMesh().chunkVisible("S1FAB", true);
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S7FAB", true);
            this.hierMesh().chunkVisible("S8FAB", true);
            this.hasBombs = true;
            this.FlapAngle = 20F;
            if (this.FM.M.fuel > 508F) {
                this.hasBoosters = true;
            }
            this.ORDmode = 4;
        }
        if (this.thisWeaponsName.startsWith("10")) {
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S7FAB", true);
            this.hasBombs = true;
            this.FlapAngle = 20F;
            if (this.FM.M.fuel > 460F) {
                this.hasBoosters = true;
            }
            this.ORDmode = 2;
        }
        if (this.thisWeaponsName.startsWith("11")) {
            this.hierMesh().chunkVisible("S1FAB", true);
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S7FAB", true);
            this.hierMesh().chunkVisible("S8FAB", true);
            this.hasBombs = true;
            this.FlapAngle = 20F;
            if (this.FM.M.fuel > 616F) {
                this.hasBoosters = true;
            }
            this.ORDmode = 1;
        }
        if (this.thisWeaponsName.startsWith("12")) {
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hasDroptanks = true;
            this.FlapAngle = 20F;
            if (this.FM.M.fuel > 388F) {
                this.hasBoosters = true;
            }
        }
        if (this.thisWeaponsName.startsWith("13")) {
            this.hierMesh().chunkVisible("S1AAM", true);
            this.hierMesh().chunkVisible("S1RailAAM", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hierMesh().chunkVisible("S8AAM", true);
            this.hierMesh().chunkVisible("S8RailAAM", true);
            this.hasDroptanks = true;
            this.FlapAngle = 20F;
            if (this.FM.M.fuel > 284F) {
                this.hasBoosters = true;
            }
            this.scopemode = 1;
        }
        if (this.thisWeaponsName.startsWith("14")) {
            this.hierMesh().chunkVisible("S1AAM", true);
            this.hierMesh().chunkVisible("S1RailAAM", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hierMesh().chunkVisible("S8AAM", true);
            this.hierMesh().chunkVisible("S8RailAAM", true);
            this.hasMissiles = true;
            this.hasDroptanks = true;
            this.FlapAngle = 20F;
            if (this.FM.M.fuel > 74F) {
                this.hasBoosters = true;
            }
            this.scopemode = 1;
        }
        if (this.thisWeaponsName.startsWith("15")) {
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hasKAB = true;
            this.hasDroptanks = true;
            this.TwoTanks = false;
            this.FlapAngle = 20F;
            if (this.FM.M.fuel > 106F) {
                this.hasBoosters = true;
            }
        }
        if (this.thisWeaponsName.startsWith("16")) {
            this.hierMesh().chunkVisible("S1AAM", true);
            this.hierMesh().chunkVisible("S1RailASR", true);
            this.hierMesh().chunkVisible("S4RAT", true);
            this.hierMesh().chunkVisible("S5PTB", true);
            this.hierMesh().chunkVisible("S8AAM", true);
            this.hierMesh().chunkVisible("S8RailASR", true);
            this.hasDroptanks = true;
            this.TwoTanks = false;
            if (this.FM.M.fuel > 81F) {
                this.hasBoosters = true;
            }
            this.ORDmode = 3;
        }
        if (this.thisWeaponsName.startsWith("17")) {
            this.hierMesh().chunkVisible("S1FAB", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hierMesh().chunkVisible("S8AAM", true);
            this.hierMesh().chunkVisible("S8RailAAM", true);
            this.hasDroptanks = true;
            this.hasBoosters = true;
            this.FlapAngle = 20F;
        }
        if (this.thisWeaponsName.startsWith("18")) {
            this.hierMesh().chunkVisible("S1FAB", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hierMesh().chunkVisible("S8FAB", true);
            this.hasDroptanks = true;
            this.hasBombs = true;
            this.hasBoosters = true;
            this.FlapAngle = 20F;
            this.ORDmode = 4;
        }
        if (this.thisWeaponsName.startsWith("19")) {
            this.hierMesh().chunkVisible("S1FAB", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hierMesh().chunkVisible("S8FAB", true);
            this.hasDroptanks = true;
            this.hasBombs = true;
            this.hasBoosters = true;
            this.FlapAngle = 20F;
            this.ORDmode = 1;
        }
        if (this.thisWeaponsName.startsWith("20")) {
            this.hierMesh().chunkVisible("S1FAB", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hierMesh().chunkVisible("S8FAB", true);
            this.hasDroptanks = true;
            this.hasBombs = true;
            this.hasBoosters = true;
            this.FlapAngle = 20F;
            this.ORDmode = 4;
        }
        if (this.thisWeaponsName.startsWith("21")) {
            this.hierMesh().chunkVisible("S1FAB", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S7PTB", true);
            this.hierMesh().chunkVisible("S8FAB", true);
            this.hasDroptanks = true;
            this.hasBombs = true;
            this.hasBoosters = true;
            this.FlapAngle = 20F;
            this.ORDmode = 1;
        }
        if (this.thisWeaponsName.startsWith("22")) {
            this.hierMesh().chunkVisible("S1FAB", true);
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S3ASR", true);
            this.hierMesh().chunkVisible("S3RailASR", true);
            this.hierMesh().chunkVisible("S6ASR", true);
            this.hierMesh().chunkVisible("S6RailASR", true);
            this.hierMesh().chunkVisible("S7FAB", true);
            this.hierMesh().chunkVisible("S8FAB", true);
            if (this.FM.M.fuel > 274F) {
                this.hasBoosters = true;
            }
            this.hasBombs = true;
            this.FlapAngle = 20F;
            this.ORDmode = 4;
        }
    }

    public float checkExtFuel(int i) {
        FuelTank afueltank[] = this.FM.CT.getFuelTanks();
        if (afueltank.length == 0) {
            return 0.0F;
        }
        if (!this.TwoTanks) {
            //            ExtFuel = afueltank[0].Fuel * 1.102311F;
            this.ExtFuel = Reflection.getFloat(afueltank[0], "Fuel") * 1.102311F;
        }
        if (this.TwoTanks) {
            //            ExtFuel = afueltank[0].Fuel * 1.102311F + afueltank[1].Fuel * 1.102311F;
            this.ExtFuel = (Reflection.getFloat(afueltank[0], "Fuel") * 1.102311F) + (Reflection.getFloat(afueltank[1], "Fuel") * 1.102311F);
        }
        return this.ExtFuel;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.hasKAB) {
            this.KABscan();
        }
        if (this.hasDroptanks && !this.APmode6) {
            this.checkExtFuel(0);
        }
        if ((this.ExtFuel <= 0.0F) && this.APmode6) {
            this.FM.CT.dropFuelTanks();
            this.hasDroptanks = false;
            this.APmode6 = false;
        }
        if (((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) || !flag || !(this.FM instanceof Pilot)) {
            return;
        }
        if (flag && (this.FM.AP.way.curr().Action == 3) && this.typeDockableIsDocked() && (Math.abs(this.queen_.FM.Or.getKren()) < 3F)) {
            if (this.FM.isPlayers()) {
                if ((this.FM instanceof RealFlightModel) && !((RealFlightModel) this.FM).isRealMode()) {
                    this.typeDockableAttemptDetach();
                    ((Maneuver) this.FM).set_maneuver(22);
                    ((Maneuver) this.FM).setCheckStrike(false);
                    this.FM.Vwld.z -= 5D;
                    this.dtime = Time.current();
                }
            } else {
                this.typeDockableAttemptDetach();
                ((Maneuver) this.FM).set_maneuver(22);
                ((Maneuver) this.FM).setCheckStrike(false);
                this.FM.Vwld.z -= 5D;
                this.dtime = Time.current();
            }
        }
    }

    public void doFireBoosters() {
        this.RatoL = Eff3DActor.New(this, this.findHook("_RatoL"), null, 1.0F, "3do/Effects/P85/P85_Rato.eff", -1F);
        this.RatoR = Eff3DActor.New(this, this.findHook("_RatoR"), null, 1.0F, "3do/Effects/P85/P85_Rato.eff", -1F);
        this.RatoLsmk = Eff3DActor.New(this, this.findHook("_RatoLsmk"), null, 1.0F, "3do/Effects/P85/P85_RocketSmoke.eff", -1F);
        this.RatoRsmk = Eff3DActor.New(this, this.findHook("_RatoRsmk"), null, 1.0F, "3do/Effects/P85/P85_RocketSmoke.eff", -1F);
    }

    public void doCutBoosters() {
        Eff3DActor.finish(this.RatoL);
        Eff3DActor.finish(this.RatoR);
        Eff3DActor.finish(this.RatoLsmk);
        Eff3DActor.finish(this.RatoRsmk);
    }

    public void doDropBoosters() {
        this.hierMesh().chunkVisible("RatoR", false);
        this.doRemoveRatoR();
        this.hierMesh().chunkVisible("RatoL", false);
        this.doRemoveRatoL();
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.hasBoosters = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    private final void doRemoveRatoR() {
        Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("RatoR"));
        wreckage.collide(true);
        Vector3d vector3d = new Vector3d();
        this.getSpeed(vector3d);
        vector3d.z -= 10D;
        vector3d.set(vector3d);
        wreckage.setSpeed(vector3d);
    }

    private final void doRemoveRatoL() {
        Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("RatoL"));
        wreckage.collide(true);
        Vector3d vector3d = new Vector3d();
        this.getSpeed(vector3d);
        vector3d.z -= 10D;
        vector3d.set(vector3d);
        wreckage.setSpeed(vector3d);
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 23D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(12, loc, vector3d, aircraft);
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
        this.radarmode = 5;
    }

    private void bailout() {
        if (this.overrideBailout) {
            if ((this.FM.AS.astateBailoutStep >= 0) && (this.FM.AS.astateBailoutStep < 2)) {
                if ((this.FM.CT.cockpitDoorControl > 0.5F) && (this.FM.CT.getCockpitDoor() > 0.5F)) {
                    this.FM.AS.astateBailoutStep = 11;
                    this.doRemoveBlisters();
                } else {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 2) && (this.FM.AS.astateBailoutStep <= 3)) {
                switch (this.FM.AS.astateBailoutStep) {
                    case 2:
                        if (this.FM.CT.cockpitDoorControl < 0.5F) {
                            this.lTimeNextEject = Time.current() + 800L;
                            this.doRemoveBlister1();
                        }
                        break;

                    case 3:
                        this.doRemoveBlisters();
                        break;
                }
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                AircraftState aircraftstate = this.FM.AS;
                aircraftstate.astateBailoutStep = (byte) (aircraftstate.astateBailoutStep + 1);
                if (this.FM.AS.astateBailoutStep == 4) {
                    this.FM.AS.astateBailoutStep = 11;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 11) && (this.FM.AS.astateBailoutStep <= 19)) {
                byte byte0 = this.FM.AS.astateBailoutStep;
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                AircraftState aircraftstate1 = this.FM.AS;
                aircraftstate1.astateBailoutStep = (byte) (aircraftstate1.astateBailoutStep + 1);
                if (byte0 == 11) {
                    this.FM.setTakenMortalDamage(true, null);
                    if ((this.FM instanceof Maneuver) && (((Maneuver) this.FM).get_maneuver() != 44)) {
                        World.cur();
                        if (this.FM.AS.actor != World.getPlayerAircraft()) {
                            ((Maneuver) this.FM).set_maneuver(44);
                        }
                    }
                }
                if (this.FM.AS.astatePilotStates[byte0 - 11] < 99) {
                    this.doRemoveBodyFromPlane(byte0 - 10);
                    if (byte0 == 11) {
                        Eff3DActor.New(this, this.findHook("_EjectSmk"), null, 1.0F, "3do/Effects/P85/P85_RocketSmoke.eff", 0.5F);
                        this.doEjectCatapult();
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        this.overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        this.ejectComplete = true;
                    }
                }
            }
        }
    }

    private final void doRemoveBlister1() {
        if ((this.hierMesh().chunkFindCheck("Blister1_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemoveBlisters() {
        for (int i = 2; i < 10; i++) {
            if ((this.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1) && (this.FM.AS.getPilotHealth(i - 1) > 0.0F)) {
                this.hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
        }

    }

    protected void moveFlap(float f) {
        float f1 = -this.FlapAngle * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if ((i == 24) && !this.APmode5 && !this.hasBoosters && this.FM.Gears.onGround()) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Ground crew: SPRD's fitted");
            }
            this.hasBoosters = true;
            this.boosterFireOutTime = -1L;
            this.Ratos = 0;
            this.APmode5 = true;
        }
        if ((i == 25) && (this.ExtFuel > 0.0F) && !this.APmode6) {
            this.APmode6 = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Transferring fuel before tank jettison");
        }
        if ((i == 26) && !this.APmode7) {
            HUD.log("Ground proximity line ON");
            this.APmode7 = true;
        }
    }

    public void update(float f) {
        if ((this.FM.AS.bIsAboutToBailout || this.overrideBailout) && !this.ejectComplete && (this.FM.getSpeedKMH() > 15F)) {
            this.overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            if (Time.current() > this.lTimeNextEject) {
                this.bailout();
            }
        }
        if ((this.Ratos == 0) && this.hasBoosters && this.FM.Gears.onGround()) {
            this.hierMesh().chunkVisible("RatoMounts", true);
            this.hierMesh().chunkVisible("RatoL", true);
            this.hierMesh().chunkVisible("RatoR", true);
            this.Ratos = 1;
        }
        if ((this.FM instanceof Pilot) && this.hasBoosters) {
            if ((this.FM.getAltitude() > 500F) && (this.boosterFireOutTime == -1L) && (this.FM.Loc.z != 0.0D) && (World.Rnd().nextFloat() < 0.05F)) {
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.hasBoosters = false;
            }
            if (this.hasBoosters && (this.boosterFireOutTime == -1L) && this.FM.Gears.onGround() && (this.FM.EI.getPowerOutput() > 0.8F) && (this.FM.getSpeedKMH() > 80F)) {
                this.boosterFireOutTime = Time.current() + 6000L;
                this.doFireBoosters();
                this.FM.AS.setGliderBoostOn();
            }
            if (this.hasBoosters && (this.boosterFireOutTime > 0L)) {
                if (Time.current() < this.boosterFireOutTime) {
                    this.FM.producedAF.x += 35000D;
                }
                if (Time.current() > (this.boosterFireOutTime + 6000L)) {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                }
                if ((Time.current() > (this.boosterFireOutTime + 30000L)) && (this.Ratos == 1)) {
                    this.hierMesh().chunkVisible("RatoR", false);
                    this.doRemoveRatoR();
                    this.Ratos = 2;
                }
                if (Time.current() > (this.boosterFireOutTime + 31000L)) {
                    this.hierMesh().chunkVisible("RatoL", false);
                    this.doRemoveRatoL();
                    this.hasBoosters = false;
                    this.APmode5 = false;
                }
            }
        }
        super.update(f);
        if (this.APmode6 && (this.ExtFuel > 0.0F)) {
            this.ExtFuel = this.ExtFuel - (9F * f);
            this.FM.M.fuel += 9F * f;
            if (this.FM.M.fuel >= this.FM.M.maxFuel) {
                this.ExtFuel = 0.0F;
            }
        }
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
        this.guidedMissileUtils.update();
        int i = this.aircIndex();
        if (this.FM instanceof Maneuver) {
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
                if (this.FM.M.fuel > 1193F) {
                    HUD.logCenter("Receiver: Tanks full");
                    ((TypeDockable) ((Interpolate) (this.FM)).actor).typeDockableAttemptDetach();
                }
                if (this.FM.M.fuel < this.FM.M.maxFuel) {
                    this.FM.M.fuel += 15F * f;
                    if (!this.ProbeOut) {
                        this.moveRefuel(90F);
                        this.ProbeOut = true;
                    }
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
        if (this.hasDroptanks && !this.FM.CT.Weapons[9][0].haveBullets()) {
            this.ExtFuel = 0.0F;
            this.hasDroptanks = false;
            if (!this.hasMissiles && !this.hasBombs) {
                this.FlapAngle = 55F;
            }
        }
        if (!this.hasDroptanks && this.hasMissiles && !this.FM.CT.Weapons[2][2].haveBullets()) {
            this.FlapAngle = 55F;
            this.hasMissiles = false;
        }
        if (!this.hasDroptanks && this.hasBombs && !this.FM.CT.Weapons[3][2].haveBullets()) {
            this.FlapAngle = 55F;
            this.hasBombs = false;
        }
        if (this.hasKAB && !this.FM.CT.Weapons[3][0].haveBullets()) {
            this.hasKAB = false;
            this.fxKAB.cancel();
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "KAB-500M launched");
            }
        }
    }

    protected boolean          hasBoosters;
    private int                Ratos;
    protected long             boosterFireOutTime;
    private Eff3DActor         RatoL;
    private Eff3DActor         RatoR;
    private Eff3DActor         RatoLsmk;
    private Eff3DActor         RatoRsmk;
    private float              FlapAngle;
    private boolean            hasDroptanks;
    private boolean            hasMissiles;
    private boolean            hasBombs;
    private boolean            hasKAB;
    private boolean            ProbeOut;
    private boolean            overrideBailout;
    private boolean            ejectComplete;
    private long               lTimeNextEject;
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
    private Actor              queen_last;
    private long               queen_time;
    private boolean            bNeedSetup;
    private long               dtime;
    private Actor              target_;
    private Aircraft           queen_;
    private int                dockport_;
    private float              deltaAzimuth;
    private float              deltaTangage;
    public float               fSightCurDistance;
    public float               fSightCurForwardAngle;
    public float               fSightCurSideslip;
    public float               fSightCurAltitude;
    public float               fSightCurSpeed;
    public float               fSightCurReadyness;
    private SoundFX            fxKAB;
    private Sample             smplKAB;
    private boolean            KABSoundPlaying;
    private boolean            KABEngaged;
    private int                KAB;
    public boolean             APmode5;
    public float               ExtFuel;
    private boolean            TwoTanks;
    public boolean             APmode6;
    public boolean             APmode7;

    static {
        Class class1 = Mig_15KISh.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-15KISh");
        Property.set(class1, "meshName", "3DO/Plane/MiG-15KISh(Multi1)/Hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1949F);
        Property.set(class1, "yearExpired", 1969F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-15KISh.fmd:MiG-15KISh_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMig_15KISh.class, CockpitP85_RP5.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 0, 0, 0, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExtTank01", "_ExtTank02", "_ExtTank03", "_ExternalDev01", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalDev02", "_ExternalDev03", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb05", "_ExternalBomb05" });
    }
}
