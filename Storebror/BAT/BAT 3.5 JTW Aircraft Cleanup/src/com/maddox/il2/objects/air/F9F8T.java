package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class F9F8T extends F9F_Cougar implements TypeDockable {

    public F9F8T() {
        this.lTimeNextEject = 0L;
        this.overrideBailout = false;
        this.ejectComplete = false;
        this.lTimeNextEject = 0L;
        this.emergencyEject = false;
    }

    public void update(float f) {
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
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
                if (this.FM.M.fuel < this.FM.M.maxFuel) {
                    this.FM.M.fuel += 20F * f;
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
        if ((this.FM.AS.bIsAboutToBailout || this.overrideBailout) && !this.ejectComplete && !this.FM.Gears.onGround()) {
            this.overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            if (Time.current() > this.lTimeNextEject) {
                this.bailout();
            }
        }
        super.update(f);
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

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
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
        if ((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && (this.FM.CT.getCockpitDoor() == 1.0F)) {
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void doEjectCatapultStudent() {
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
                    new EjectionSeat(4, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat2_D0", false);
    }

    public void doEjectCatapultInstructor() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat02");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(4, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat1_D0", false);
    }

    private void bailout() {
        if (this.overrideBailout) {
            if ((this.FM.AS.astateBailoutStep >= 0) && (this.FM.AS.astateBailoutStep < 2)) {
                if ((this.FM.CT.cockpitDoorControl > 0.5F) && (this.FM.CT.getCockpitDoor() > 0.5F)) {
                    this.FM.AS.astateBailoutStep = 11;
                } else {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 2) && (this.FM.AS.astateBailoutStep <= 3)) {
                switch (this.FM.AS.astateBailoutStep) {
                    case 2:
                        if (this.FM.CT.cockpitDoorControl < 0.5F) {
                            if ((this.FM.AS.getPilotHealth(0) < 0.5F) || (this.FM.AS.getPilotHealth(1) < 0.5F) || (this.FM.EI.engines[0].getReadyness() < 0.7F) || !this.FM.CT.bHasCockpitDoorControl || (this.FM.getSpeedKMH() < 250F) || (World.Rnd().nextFloat() < 0.2F)) {
                                this.emergencyEject = true;
                                this.breakBlister();
                            } else {
                                this.doRemoveBlister1();
                            }
                        }
                        break;

                    case 3:
                        if (!this.emergencyEject) {
                            this.lTimeNextEject = Time.current() + 1000L;
                        }
                        break;
                }
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                this.FM.AS.astateBailoutStep = (byte) (this.FM.AS.astateBailoutStep + 1);
                if (this.FM.AS.astateBailoutStep == 4) {
                    this.FM.AS.astateBailoutStep = 11;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 11) && (this.FM.AS.astateBailoutStep <= 19)) {
                byte byte0 = this.FM.AS.astateBailoutStep;
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                this.FM.AS.astateBailoutStep = (byte) (this.FM.AS.astateBailoutStep + 1);
                if ((this.FM instanceof Maneuver) && (((Maneuver) this.FM).get_maneuver() != 44)) {
                    World.cur();
                    if (this.FM.AS.actor != World.getPlayerAircraft()) {
                        ((Maneuver) this.FM).set_maneuver(44);
                    }
                }
                if (this.FM.AS.astatePilotStates[byte0 - 11] < 99) {
                    if (byte0 == 11) {
                        this.doRemoveBodyFromPlane(2);
                        this.doEjectCatapultStudent();
                        this.lTimeNextEject = Time.current() + 1000L;
                    } else if (byte0 == 12) {
                        this.doRemoveBodyFromPlane(1);
                        this.doEjectCatapultInstructor();
                        this.FM.AS.astateBailoutStep = 51;
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        this.overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        this.ejectComplete = true;
                    }
                    this.FM.AS.astatePilotStates[byte0 - 11] = 99;
                } else {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + this.FM.AS.astatePilotStates[byte0 - 11]);
                }
            }
        }
    }

    private final void breakBlister() {
        if ((this.hierMesh().chunkFindCheck("Blister1_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
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

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);

            case 11:
                this.cutFM(17, j, actor);
                this.FM.cut(17, j, actor);
                this.cutFM(18, j, actor);
                this.FM.cut(18, j, actor);
                return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 2.3F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.15F, 0.99F, 0.0F, -0.25F);
        Aircraft.ypr[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 8F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.6F, 0.0F, 3.141593F));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        this.hierMesh().chunkSetAngles("Head1_D0", 14F * f1, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Pilot2_D0", 0.0F, 0.0F, 9F * f1);
        this.hierMesh().chunkSetAngles("Head2_D0", 14F * f1, 0.0F, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    private Actor         queen_last;
    private long          queen_time;
    private boolean       bNeedSetup;
    private long          dtime;
    private Actor         target_;
    private Aircraft      queen_;
    private int           dockport_;
    public static boolean bChangedPit = false;
    private boolean       overrideBailout;
    private boolean       ejectComplete;
    private long          lTimeNextEject;
    private boolean       emergencyEject;

    static {
        Class class1 = F9F8T.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F9F-8");
        Property.set(class1, "meshName", "3DO/Plane/F9F8T/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/F9F8T.fmd:F9F");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF9F_Student.class, CockpitT_33i.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalRock12", "_ExternalRock12", "_ExternalBomb07" });
    }
}
