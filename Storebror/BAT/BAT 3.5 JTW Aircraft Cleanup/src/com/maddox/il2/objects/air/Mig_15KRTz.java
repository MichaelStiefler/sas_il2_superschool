package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class Mig_15KRTz extends Mig_15K implements TypeTransport, TypeTankerDrogue, TypeZBReceiver, TypeDockable, TypeStormovik {

    public Mig_15KRTz() {
        this.FlapAngle = 55F;
        this.hasDroptanks = false;
        this.hasBuddy = false;
        this.WingOn = true;
        this.ProbeOut = false;
        this.hasBoosters = false;
        this.Ratos = 0;
        this.boosterFireOutTime = -1L;
        this.FuelOn = false;
        this.FuelLow = false;
        this.FuelStop = false;
        this.WaveOff = false;
        this.DockSpeed = 10F;
        this.DockDistance = 5D;
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.drones = new Actor[2];
        this.overrideBailout = false;
        this.ejectComplete = false;
        this.lTimeNextEject = 0L;
        this.APmode5 = false;
        this.ExtFuel = 0.0F;
        this.APmode6 = false;
        this.APmode7 = false;
        this.PumpedFuel = 0.0F;
        this.DrogueCtrl = false;
    }

    public boolean typeDockableIsDocked() {
        if (!this.hasBuddy) {
            return Actor.isValid(this.queen_);
        } else {
            return true;
        }
    }

    public void typeDockableAttemptAttach() {
        if (!this.hasBuddy && this.FM.AS.isMaster() && !this.typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
            if (aircraft instanceof TypeTankerDrogue) {
                ((TypeDockable) aircraft).typeDockableRequestAttach(this);
            }
        }
    }

    public void typeDockableAttemptDetach() {
        if (this.hasBuddy && this.FM.AS.isMaster()) {
            for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i])) {
                    this.typeDockableRequestDetach(this.drones[i], i, true);
                }
            }

        }
        if (!this.hasBuddy) {
            if (this.FM.AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_)) {
                ((TypeDockable) this.queen_).typeDockableRequestDetach(this);
            }
            if (this.ProbeOut) {
                this.moveRefuel(-90F);
                this.ProbeOut = false;
            }
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
        this.checkAsDrone();
    }

    private void checkAsDrone() {
        if (!this.hasBuddy) {
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

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
        if (!this.hasBuddy) {
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

    public void moveRefuel(float f) {
        if (!this.hasBuddy) {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, -0.8F);
            this.hierMesh().chunkSetLocate("ExtrasProbe", Aircraft.xyz, Aircraft.ypr);
        }
        if (this.hasBuddy && (f > 0.0F) && (f < 1.0F)) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Standby...");
        }
        if (this.hasBuddy && this.ProbeOut && (f == 1.0F)) {
            this.DrogueCtrl = true;
            ((TypeDockable) ((Interpolate) (this.FM)).actor).typeDockableAttemptDetach();
            this.hierMesh().chunkVisible("UPAZDrogueOut", false);
            this.hierMesh().chunkVisible("UPAZDrogueIn", true);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Drogue retracted");
            this.ProbeOut = false;
            f = -1F;
        }
        if (this.hasBuddy && !this.ProbeOut && (f == 0.0F)) {
            this.DrogueCtrl = true;
            this.hierMesh().chunkVisible("UPAZDrogueOut", true);
            this.hierMesh().chunkVisible("UPAZDrogueIn", false);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Drogue deployed");
            this.ProbeOut = true;
            f = 2.0F;
        }
    }

    public void typeDockableRequestAttach(Actor actor) {
        if (this.hasBuddy && (actor instanceof Aircraft)) {
            Aircraft aircraft = (Aircraft) actor;
            if (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster() && (aircraft.FM.getSpeedKMH() > this.DockSpeed) && (this.FM.getSpeedKMH() > this.DockSpeed)) {
                for (int i = 0; i < 1; i++) {
                    if (Actor.isValid(this.drones[i])) {
                        continue;
                    }
                    HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    this.pos.getAbs(loc1);
                    hooknamed.computePos(this, loc1, loc);
                    actor.pos.getAbs(loc1);
                    if (loc.getPoint().distance(loc1.getPoint()) >= this.DockDistance) {
                        continue;
                    }
                    if (this.FM.AS.isMaster()) {
                        this.typeDockableRequestAttach(actor, i, true);
                    } else {
                        this.FM.AS.netToMaster(32, i, 0, actor);
                    }
                    break;
                }

            }
        }
    }

    public void typeDockableRequestDetach(Actor actor) {
        if (this.hasBuddy) {
            for (int i = 0; i < this.drones.length; i++) {
                if (actor == this.drones[i]) {
                    Aircraft aircraft = (Aircraft) actor;
                    if (aircraft.FM.AS.isMaster()) {
                        if (this.FM.AS.isMaster()) {
                            this.typeDockableRequestDetach(actor, i, true);
                        } else {
                            this.FM.AS.netToMaster(33, i, 1, actor);
                        }
                    }
                }
            }

        }
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
        if (this.hasBuddy && (i >= 0) && (i <= 4)) {
            if (flag) {
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(34, i, 1, actor);
                    this.typeDockableDoAttachToDrone(actor, i);
                } else {
                    this.FM.AS.netToMaster(34, i, 1, actor);
                }
            } else if (this.FM.AS.isMaster()) {
                if (!Actor.isValid(this.drones[i])) {
                    this.FM.AS.netToMirrors(34, i, 1, actor);
                    this.typeDockableDoAttachToDrone(actor, i);
                }
            } else {
                this.FM.AS.netToMaster(34, i, 0, actor);
            }
        }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
        if (this.hasBuddy && flag) {
            if (this.FM.AS.isMaster()) {
                this.FM.AS.netToMirrors(35, i, 1, actor);
                this.typeDockableDoDetachFromDrone(i);
            } else {
                this.FM.AS.netToMaster(35, i, 1, actor);
            }
        }
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
        if (this.hasBuddy && !Actor.isValid(this.drones[i]) && (i < 1)) {
            HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            this.pos.getAbs(loc1);
            hooknamed.computePos(this, loc1, loc);
            HookNamed hooknamed1 = new HookNamed((ActorMesh) actor, "_Probe");
            Loc loc2 = new Loc();
            hooknamed1.computePos(this, loc, loc2);
            actor.pos.setAbs(loc2);
            actor.pos.setBase(this, null, true);
            actor.pos.resetAsBase();
            this.drones[i] = actor;
            ((TypeDockable) this.drones[i]).typeDockableDoAttachToQueen(this, i);
        }
    }

    public void typeDockableDoDetachFromDrone(int i) {
        if (this.hasBuddy && Actor.isValid(this.drones[i])) {
            this.drones[i].pos.setBase(null, null, true);
            ((TypeDockable) this.drones[i]).typeDockableDoDetachFromQueen(i);
            this.drones[i] = null;
            Eff3DActor.finish(this.GreenSignal);
            Eff3DActor.finish(this.YellowSignal);
            Eff3DActor.finish(this.RedSignal);
            this.FuelOn = false;
            this.FuelLow = false;
            this.FuelStop = false;
            this.WaveOff = false;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (this.hasBuddy) {
            for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i])) {
                    netmsgguaranted.writeByte(1);
                    ActorNet actornet1 = this.drones[i].net;
                    if (actornet1.countNoMirrors() == 0) {
                        netmsgguaranted.writeNetObj(actornet1);
                    } else {
                        netmsgguaranted.writeNetObj(null);
                    }
                } else {
                    netmsgguaranted.writeByte(0);
                }
            }

        }
        if (!this.hasBuddy) {
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
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        if (this.hasBuddy) {
            for (int i = 0; i < this.drones.length; i++) {
                if (netmsginput.readByte() == 1) {
                    NetObj netobj1 = netmsginput.readNetObj();
                    if (netobj1 != null) {
                        this.typeDockableDoAttachToDrone((Actor) netobj1.superObj(), i);
                    }
                }
            }

        }
        if (!this.hasBuddy && (netmsginput.readByte() == 1)) {
            this.dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null) {
                Actor actor = (Actor) netobj.superObj();
                ((TypeDockable) actor).typeDockableDoAttachToDrone(this, this.dockport_);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (this.FM.AS.isMaster()) {
            switch (i) {
                default:
                    break;

                case 33:
                case 34:
                case 35:
                    if (this.hasBuddy) {
                        this.typeDockableRequestDetach(this.drones[0], 0, true);
                        break;
                    }
                    // fall through

                case 36:
                case 37:
                case 38:
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.hasBoosters = false;
                    this.typeDockableRequestDetach(this.drones[1], 1, true);
                    break;
            }
        }
        return super.cutFM(i, j, actor);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        World.getPlayerAircraft();
        if (this.thisWeaponsName.startsWith("01")) {
            this.hierMesh().chunkVisible("CamCoverAft", true);
            this.hierMesh().chunkVisible("CamCoverFwd", true);
            this.scopemode = 2;
        }
        if (this.thisWeaponsName.startsWith("02")) {
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S5PTB", true);
            this.hierMesh().chunkVisible("CamCoverAft", true);
            this.hierMesh().chunkVisible("CamCoverFwd", true);
            this.FlapAngle = 25F;
            this.hasDroptanks = true;
            if (this.FM.M.fuel > 355F) {
                this.hasBoosters = true;
            }
            this.scopemode = 2;
        }
        if (this.thisWeaponsName.startsWith("03")) {
            this.hierMesh().chunkVisible("S1FAB", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S5PTB", true);
            this.hierMesh().chunkVisible("S6FAB", true);
            this.hierMesh().chunkVisible("CamCoverAft", true);
            this.hierMesh().chunkVisible("CamCoverFwd", true);
            this.FlapAngle = 25F;
            this.hasDroptanks = true;
            if (this.FM.M.fuel > 259F) {
                this.hasBoosters = true;
            }
            this.scopemode = 2;
        }
        if (this.thisWeaponsName.startsWith("04")) {
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S3PTB", true);
            this.hierMesh().chunkVisible("S4PTB", true);
            this.hierMesh().chunkVisible("S5PTB", true);
            this.hierMesh().chunkVisible("UPAZ", true);
            this.hierMesh().chunkVisible("UPAZRATSt", true);
            this.FlapAngle = 25F;
            this.hasDroptanks = true;
            this.hasBuddy = true;
            this.hasBoosters = true;
            this.FM.M.maxFuel += 600F;
            this.FM.M.fuel += 600F;
        }
        if (this.thisWeaponsName.startsWith("05")) {
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S3PTB", true);
            this.hierMesh().chunkVisible("S4PTB", true);
            this.hierMesh().chunkVisible("S5PTB", true);
            this.hierMesh().chunkVisible("UPAZ", true);
            this.hierMesh().chunkVisible("UPAZRATSt", true);
            this.FlapAngle = 25F;
            this.hasDroptanks = true;
            this.hasBuddy = true;
            this.hasBoosters = true;
            this.DockSpeed = 30F;
            this.DockDistance = 15D;
            this.FM.M.maxFuel += 600F;
            this.FM.M.fuel += 600F;
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

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
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

    protected void moveFan(float f) {
        if (this.hasBuddy) {
            if (this.bDynamoOperational) {
                this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
                if (this.pk >= 1) {
                    this.pk = 1;
                }
            }
            if (this.bDynamoRotary != (this.pk == 1)) {
                this.bDynamoRotary = this.pk == 1;
                this.hierMesh().chunkVisible("UPAZRATSt", !this.bDynamoRotary);
                this.hierMesh().chunkVisible("UPAZRATMv", this.bDynamoRotary);
            }
            this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 120F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
            this.hierMesh().chunkSetAngles("UPAZRATSt", 0.0F, 0.0F, this.dynamoOrient);
            super.moveFan(f);
        }
    }

    protected void moveFlap(float f) {
        float f1 = -this.FlapAngle * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveGear(float f) {
        super.moveGear(f);
        if (this.scopemode == 2) {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.16F);
            Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0045F);
            this.hierMesh().chunkSetLocate("CamCoverFwd", Aircraft.xyz, Aircraft.ypr);
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.1727F);
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0115F);
            this.hierMesh().chunkSetLocate("CamCoverAft", Aircraft.xyz, Aircraft.ypr);
        }
    }

    public float checkExtFuel(int i) {
        FuelTank afueltank[] = this.FM.CT.getFuelTanks();
        if (afueltank.length == 0) {
            return 0.0F;
        } else {
//            ExtFuel = afueltank[0].Fuel * 1.102311F + afueltank[1].Fuel * 1.102311F;
            this.ExtFuel = (Reflection.getFloat(afueltank[0], "Fuel") * 1.102311F) + (Reflection.getFloat(afueltank[1], "Fuel") * 1.102311F);
            return this.ExtFuel;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.hasDroptanks && !this.APmode6) {
            this.checkExtFuel(0);
        }
        if ((this.ExtFuel <= 0.0F) && this.APmode6) {
            this.FM.CT.dropFuelTanks();
            this.hasDroptanks = false;
            this.APmode6 = false;
        }
        if (!this.DrogueCtrl && this.hasBuddy) {
            if ((this.FM.getAltitude() <= 1000F) || (this.FM.CT.getGear() > 0.0D)) {
                this.hierMesh().chunkVisible("UPAZDrogueOut", false);
                this.hierMesh().chunkVisible("UPAZDrogueIn", true);
                if (this.ProbeOut) {
                    this.moveRefuel(-90F);
                    this.ProbeOut = false;
                }
            } else {
                this.hierMesh().chunkVisible("UPAZDrogueOut", true);
                this.hierMesh().chunkVisible("UPAZDrogueIn", false);
                if (!this.ProbeOut) {
                    this.moveRefuel(90F);
                    this.ProbeOut = true;
                }
            }
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
        if (this.hasBuddy && this.WingOn && !this.FM.CT.bHasAileronControl && (this.FM.Gears.nOfGearsOnGr == 0)) {
            this.hierMesh().chunkVisible("UPAZDrogueOut", false);
            this.hierMesh().chunkVisible("UPAZDrogueIn", false);
            this.WingOn = false;
        }
        if (this.hasBuddy && ((TypeDockable) ((Interpolate) (this.FM)).actor).typeDockableIsDocked()) {
            for (int i = 0; i < this.drones.length; i++) {
                if (Actor.isValid(this.drones[i])) {
                    if (this.hasDroptanks) {
                        if (this.PumpedFuel <= 0.0F) {
                            this.checkExtFuel(0);
                        }
                        this.PumpedFuel += 15F * f;
                        if (this.PumpedFuel >= this.ExtFuel) {
                            this.FM.CT.dropFuelTanks();
                            this.hasDroptanks = false;
                        }
                    }
                    if (!this.hasDroptanks) {
                        this.FM.M.fuel -= 15F * f;
                    }
                    if (!this.FuelOn) {
                        this.GreenSignal = Eff3DActor.New(this, this.findHook("_GreenSignal"), null, 1.0F, "3do/Effects/P85/P85_SignalGreen.eff", -1F);
                        this.FuelOn = true;
                    }
                    if (!this.FuelLow && (this.FM.M.fuel < 600F)) {
                        Eff3DActor.finish(this.GreenSignal);
                        this.YellowSignal = Eff3DActor.New(this, this.findHook("_YellowSignal"), null, 1.0F, "3do/Effects/P85/P85_SignalYellow.eff", -1F);
                        this.FuelLow = true;
                    }
                    if (!this.WaveOff && (this.FM.M.fuel < 400F)) {
                        Eff3DActor.finish(this.YellowSignal);
                        this.RedSignal = Eff3DActor.New(this, this.findHook("_RedSignal"), null, 1.0F, "3do/Effects/P85/P85_SignalRed.eff", -1F);
                        this.WaveOff = true;
                    }
                    if (!this.FuelStop && (this.FM.M.fuel < 300F)) {
                        ((TypeDockable) ((Interpolate) (this.FM)).actor).typeDockableAttemptDetach();
                        Eff3DActor.finish(this.RedSignal);
                        this.FuelStop = true;
                    }
                    if (!this.DrogueCtrl && (this.FM.getAltitude() < 1000F)) {
                        ((TypeDockable) ((Interpolate) (this.FM)).actor).typeDockableAttemptDetach();
                        Eff3DActor.finish(this.GreenSignal);
                        Eff3DActor.finish(this.YellowSignal);
                        Eff3DActor.finish(this.RedSignal);
                    }
                }
            }

        }
        if (!this.hasBuddy) {
            if (this.bNeedSetup) {
                this.checkAsDrone();
            }
            int j = this.aircIndex();
            if (this.FM instanceof Maneuver) {
                if (this.typeDockableIsDocked()) {
                    if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                        ((Maneuver) this.FM).unblock();
                        ((Maneuver) this.FM).set_maneuver(48);
                        for (int k = 0; k < j; k++) {
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
        }
        if (this.hasDroptanks && !this.FM.CT.Weapons[9][1].haveBullets()) {
            this.ExtFuel = 0.0F;
            this.hasDroptanks = false;
            if (!this.hasBuddy) {
                this.FlapAngle = 55F;
            }
            if (this.hasBuddy) {
                this.FlapAngle = 40F;
            }
        }
    }

    private Actor         drones[];
    private float         FlapAngle;
    private boolean       hasDroptanks;
    private boolean       hasBuddy;
    private boolean       WingOn;
    protected boolean     hasBoosters;
    private int           Ratos;
    protected long        boosterFireOutTime;
    private Eff3DActor    RatoL;
    private Eff3DActor    RatoR;
    private Eff3DActor    RatoLsmk;
    private Eff3DActor    RatoRsmk;
    private boolean       FuelOn;
    private boolean       FuelLow;
    private boolean       WaveOff;
    private boolean       FuelStop;
    private float         DockSpeed;
    private double        DockDistance;
    private Eff3DActor    GreenSignal;
    private Eff3DActor    YellowSignal;
    private Eff3DActor    RedSignal;
    private Actor         queen_last;
    private long          queen_time;
    private boolean       bNeedSetup;
    private long          dtime;
    private Actor         target_;
    private Aircraft      queen_;
    private int           dockport_;
    private boolean       ProbeOut;
    private boolean       bDynamoOperational;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    private int           pk;
    public static boolean bChangedPit = false;
    private boolean       overrideBailout;
    private boolean       ejectComplete;
    private long          lTimeNextEject;
    public boolean        APmode5;
    public float          ExtFuel;
    public boolean        APmode6;
    public boolean        APmode7;
    public float          PumpedFuel;
    private boolean       DrogueCtrl;

    static {
        Class class1 = Mig_15KRTz.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-15KRTz");
        Property.set(class1, "meshName", "3DO/Plane/MiG-15KRTz(Multi1)/Hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1955F);
        Property.set(class1, "yearExpired", 1968.5F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-15KRTz.fmd:MiG-15KRTz_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMig_15KRTz.class, CockpitP85_PN2.class });
        Property.set(class1, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1, 9, 9, 9, 9, 9, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExtTank01", "_ExtTank02", "_ExtTank03", "_ExtTank04", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02" });
    }

    public float requestRefuel(Aircraft paramAircraft, float paramFloat1, float paramFloat2) {
        // TODO Auto-generated method stub
        return 0;

        // This is what that method looks like on a Skyhawk Tanker:
        /*
         * if(bDrogueExtended && FM.AS.isMaster())
         * {
         * for(int i = 0; i < drones.length; i++)
         * {
         * if(!Actor.isValid(drones[i]) || drones[i] != aircraft)
         * continue;
         * if(f > maxSendRefuel)
         * f = maxSendRefuel;
         * if(FM.M.requestFuel(f * f1))
         * return f * f1;
         * }
         *
         * }
         * return 0.0F;
         */

    }
}
