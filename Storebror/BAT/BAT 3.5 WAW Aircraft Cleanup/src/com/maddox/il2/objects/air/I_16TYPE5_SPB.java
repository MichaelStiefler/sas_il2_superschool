package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class I_16TYPE5_SPB extends I_16 implements MsgCollisionRequestListener, TypeTNBFighter, TypeStormovik, TypeDockable {

    public I_16TYPE5_SPB() {
        this.queen_last = null;
        this.queen_time = 0L;
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.target_ = null;
        this.queen_ = null;
        this.bailingOut = false;
        this.canopyForward = false;
        this.okToJump = false;
        this.flaperonAngle = 0.0F;
        this.aileronsAngle = 0.0F;
        this.oneTimeCheckDone = false;
        this.sideDoorOpened = false;
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if ((this.queen_last != null) && (this.queen_last == actor) && ((this.queen_time == 0L) || (Time.current() < (this.queen_time + 5000L)))) {
            aflag[0] = false;
        } else {
            aflag[0] = true;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.FM.Gears.hitCentreGear();
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xxtank1") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.3F)) {
            if (this.FM.AS.astateTankStates[0] == 0) {
                Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                this.FM.AS.hitTank(shot.initiator, 0, 2);
            }
            if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.hitTank(shot.initiator, 0, 2);
                Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
            }
        } else {
            super.hitBone(s, shot, point3d);
        }
    }

    public void moveCockpitDoor(float f) {
        if (this.bailingOut && (f >= 1.0F) && !this.canopyForward) {
            this.canopyForward = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        } else if (this.canopyForward) {
            this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
            if (f >= 1.0F) {
                this.okToJump = true;
                this.hitDaSilk();
            }
        } else {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[1] = f * 0.548F;
            this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void hitDaSilk() {
        if (this.okToJump) {
            super.hitDaSilk();
        } else if (this.FM.isPlayers() || this.isNetPlayer()) {
            if ((this.FM.CT.getCockpitDoor() == 1.0D) && !this.bailingOut) {
                this.bailingOut = true;
                this.okToJump = true;
                this.canopyForward = true;
                super.hitDaSilk();
            }
        } else if (!this.FM.AS.isPilotDead(0)) {
            if ((this.FM.CT.getCockpitDoor() < 1.0D) && !this.bailingOut) {
                this.bailingOut = true;
                this.FM.AS.setCockpitDoor(this, 1);
            } else if ((this.FM.CT.getCockpitDoor() == 1.0D) && !this.bailingOut) {
                this.bailingOut = true;
                this.okToJump = true;
                this.canopyForward = true;
                super.hitDaSilk();
            }
        }
        if (!this.sideDoorOpened && this.FM.AS.bIsAboutToBailout && !this.FM.AS.isPilotDead(0)) {
            this.sideDoorOpened = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        }
    }

    public void moveGear(float f, float f1, float f2) {
        super.moveGear(f, f1, f2);
        if (f > 0.5F) {
            this.hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.5F, 1.0F, -14.5F, 8F), Aircraft.cvt(f, 0.5F, 1.0F, -44F, -62.5F), 0.0F);
        } else if (f > 0.25F) {
            this.hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.25F, 0.5F, -33F, -14.5F), Aircraft.cvt(f, 0.25F, 0.5F, -38F, -44F), 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -33F), Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -38F), 0.0F);
        }
        if (f1 > 0.5F) {
            this.hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f1, 0.5F, 1.0F, 14.5F, -8F), Aircraft.cvt(f1, 0.5F, 1.0F, 44F, 62.5F), 0.0F);
        } else if (f1 > 0.25F) {
            this.hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f1, 0.25F, 0.5F, 33F, 14.5F), Aircraft.cvt(f1, 0.25F, 0.5F, 38F, 44F), 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f1, 0.0F, 0.25F, 0.0F, 33F), Aircraft.cvt(f1, 0.0F, 0.25F, 0.0F, 38F), 0.0F);
        }
        if (f > 0.5F) {
            this.hierMesh().chunkVisible("GearWireL2_D0", true);
        } else {
            this.hierMesh().chunkVisible("GearWireL2_D0", false);
        }
        if (f1 > 0.5F) {
            this.hierMesh().chunkVisible("GearWireR2_D0", true);
        } else {
            this.hierMesh().chunkVisible("GearWireR2_D0", false);
        }
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
                        ((Maneuver) this.FM).AP.way.setCur(((Aircraft) this.queen_).FM.AP.way.Cur());
                    }
                    ((Pilot) this.FM).setDumbTime(3000L);
                }
                if (this.FM.M.fuel < this.FM.M.maxFuel) {
                    this.FM.M.fuel += 0.06F * f;
                }
            } else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                if (this.FM.EI.engines[0].getStage() == 0) {
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
        super.update(f);
    }

    protected void moveAileron(float f) {
        this.aileronsAngle = f;
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, (-30F * f) - this.flaperonAngle, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, (-30F * f) + this.flaperonAngle, 0.0F);
    }

    protected void moveFlap(float f) {
        this.flaperonAngle = f * 17F;
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, (-30F * this.aileronsAngle) - this.flaperonAngle, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, (-30F * this.aileronsAngle) + this.flaperonAngle, 0.0F);
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            super.moveFan(f);
            float f1 = this.FM.CT.getAileron();
            float f2 = this.FM.CT.getElevator();
            this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 12F * f1, Aircraft.cvt(f2, -1F, 1.0F, -12F, 18F));
            this.hierMesh().chunkSetAngles("pilotarm2_d0", Aircraft.cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, Aircraft.cvt(f1, -1F, 1.0F, 6F, -8F) - (Aircraft.cvt(f2, -1F, 0.0F, -36F, 0.0F) + Aircraft.cvt(f2, 0.0F, 1.0F, 0.0F, 32F)));
            this.hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, Aircraft.cvt(f1, -1F, 1.0F, -16F, 14F) + Aircraft.cvt(f2, -1F, 0.0F, -62F, 0.0F) + Aircraft.cvt(f2, 0.0F, 1.0F, 0.0F, 44F));
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (!this.oneTimeCheckDone && !this.FM.isPlayers() && !this.isNetPlayer() && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.oneTimeCheckDone = true;
            if (World.cur().camouflage == 1) {
                if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.25F) {
                    this.FM.CT.cockpitDoorControl = 1.0F;
                    this.FM.AS.setCockpitDoor(this, 1);
                }
            } else if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F) {
                this.FM.CT.cockpitDoorControl = 1.0F;
                this.FM.AS.setCockpitDoor(this, 1);
            }
        }
        if (flag && (this.FM.AP.way.curr().Action == 3) && this.typeDockableIsDocked() && (Math.abs(((Aircraft) this.queen_).FM.Or.getKren()) < 3F)) {
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

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D1", true);
                this.hierMesh().chunkVisible("pilotarm2_d0", false);
                this.hierMesh().chunkVisible("pilotarm1_d0", false);
                break;
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        this.hierMesh().chunkVisible("pilotarm2_d0", false);
        this.hierMesh().chunkVisible("pilotarm1_d0", false);
    }

    public void missionStarting() {
        super.missionStarting();
        this.hierMesh().chunkVisible("pilotarm2_d0", true);
        this.hierMesh().chunkVisible("pilotarm1_d0", true);
        this.checkAsDrone();
    }

    public void prepareCamouflage() {
        super.prepareCamouflage();
        this.hierMesh().chunkVisible("pilotarm2_d0", true);
        this.hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.hierMesh().chunkVisible("GearWireR1_D0", true);
        this.hierMesh().chunkVisible("GearWireL1_D0", true);
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
        if (Actor.isValid(this.target_) && (this.target_ instanceof TB_3_4M_34R_SPB)) {
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
            if (aircraft instanceof TB_3_4M_34R_SPB) {
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
        this.queen_ = actor;
        this.dockport_ = i;
        this.queen_last = this.queen_;
        this.queen_time = 0L;
        this.FM.EI.setEngineRunning();
        this.FM.CT.setGearAirborne();
        this.moveGear(0.0F, 0.0F, 0.0F);
        FlightModel flightmodel = ((Aircraft) this.queen_).FM;
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

    private Actor   queen_last;
    private long    queen_time;
    private boolean bNeedSetup;
    private long    dtime;
    private Actor   target_;
    private Actor   queen_;
    private int     dockport_;
    private boolean bailingOut;
    private boolean canopyForward;
    private boolean okToJump;
    private float   flaperonAngle;
    private float   aileronsAngle;
    private boolean oneTimeCheckDone;
    private boolean sideDoorOpened;

    static {
        Class class1 = I_16TYPE5_SPB.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type5(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar07());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type5/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar07());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type5.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE5_SPB.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev07", "_ExternalDev08" });
    }
}
