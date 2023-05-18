package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Time;

public abstract class F84_Dockable extends F84 implements TypeDockable {
    
    public F84_Dockable() {
        this.queen_last = null;
        this.queen_time = 0L;
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.target_ = null;
        this.queen_ = null;
    }
    
    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && (this.FM instanceof Maneuver) && (this.FM.AP.way.curr().Action == 3) && this.typeDockableIsDocked() && (Math.abs(this.queen_.FM.Or.getKren()) < 3F)) {
            Maneuver maneuver = (Maneuver) this.FM;
            if (this.FM.isPlayers()) {
                if ((this.FM instanceof RealFlightModel) && !((RealFlightModel) this.FM).isRealMode()) {
                    this.typeDockableAttemptDetach();
                    maneuver.set_maneuver(22);
                    maneuver.setCheckStrike(false);
                    this.FM.Vwld.z -= 5D;
                    this.dtime = Time.current();
                }
            } else {
                this.typeDockableAttemptDetach();
                maneuver.set_maneuver(22);
                maneuver.setCheckStrike(false);
                this.FM.Vwld.z -= 5D;
                this.dtime = Time.current();
            }
        }
    }
    
    public void update(float f) {
        super.update(f);
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
        if (this.FM instanceof Maneuver) {
            this.receivingRefuel(f);
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
        if (Actor.isValid(this.target_) && ((this.target_ instanceof TypeTankerBoom) || (this.target_ instanceof KB_29P))) {
            this.queen_last = (Aircraft)this.target_;
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
            if ((aircraft instanceof TypeTankerBoom) || (aircraft instanceof KB_29P)) {
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
            if ((this.FM instanceof Maneuver) && (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode())) {
                Maneuver maneuver = (Maneuver) this.FM;
                maneuver.unblock();
                maneuver.set_maneuver(48);
                for (int j = 0; j < i; j++) {
                    maneuver.push(48);
                }

                if (this.FM.AP.way.curr().Action != 3) {
                    this.FM.AP.way.setCur(this.queen_.FM.AP.way.Cur());
                }
                if (this.FM instanceof Pilot) ((Pilot) this.FM).setDumbTime(3000L);
            }
            if ((this.queen_ instanceof TypeTankerBoom) && (this.FM.M.fuel < (this.FM.M.maxFuel) - 28F) )
                    this.FM.M.fuel += ((TypeTankerBoom) this.queen_).requestRefuel(this, 28F, f);
            else if (this.FM.M.fuel < (this.FM.M.maxFuel) - 20F * f)
                    this.FM.M.fuel += 20F * f;
            else
                this.typeDockableAttemptDetach();
        } else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
            if ((this.FM.CT.GearControl == 0.0F) && (this.FM.EI.engines[0].getStage() == 0)) {
                this.FM.EI.setEngineRunning();
            }
            if (this.FM instanceof Maneuver) {
                Maneuver maneuver = (Maneuver) this.FM;
                if ((this.dtime > 0L) && (maneuver.Group != null)) {
                    maneuver.Group.leaderGroup = null;
                    maneuver.set_maneuver(22);
                    if (this.FM instanceof Pilot) {
                        Pilot pilot = (Pilot) this.FM;
                        pilot.setDumbTime(3000L);
                        if (Time.current() > (this.dtime + 3000L)) {
                            this.dtime = -1L;
                            maneuver.clear_stack();
                            maneuver.set_maneuver(0);
                            pilot.setDumbTime(0L);
                        }
                    }
                } else if (this.FM.AP.way.curr().Action == 0) {
                    if ((maneuver.Group != null) && (maneuver.Group.airc[0] == this) && (maneuver.Group.clientGroup != null)) {
                        maneuver.Group.setGroupTask(2);
                    }
                }
            }
        }
    }

    private Aircraft queen_last;
    private long     queen_time;
    private boolean  bNeedSetup;
    private long     dtime;
    private Actor    target_;
    private Aircraft queen_;
    private int      dockport_;

}
