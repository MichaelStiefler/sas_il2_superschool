package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class FW_190A8MSTL extends FW_190 implements TypeDockable {

    public FW_190A8MSTL() {
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.target_ = null;
        this.queen_ = null;
    }

    protected void moveFan(float f) {
        int i = 0;
        for (int j = 0; j < 1; j++) {
            if (this.oldProp[j] < 2) {
                i = Math.abs((int) (this.FM.EI.engines[j].getw() * 0.12F * 1.5F));
                if (i >= 1) {
                    i = 1;
                }
                if (i != this.oldProp[j]) {
                    this.hierMesh().chunkVisible(Aircraft.Props[j][this.oldProp[j]], false);
                    this.oldProp[j] = i;
                    this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
                }
            }
            if (i == 0) {
                this.propPos[j] = (this.propPos[j] + (57.3F * this.FM.EI.engines[j].getw() * f)) % 360F;
            } else {
                float f1 = 57.3F * this.FM.EI.engines[j].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if (f1 <= 0.5F) {
                    f1 *= 2.0F;
                } else {
                    f1 = (f1 * 2.0F) - 2.0F;
                }
                f1 *= 1200F;
                this.propPos[j] = (this.propPos[j] + (f1 * f)) % 360F;
            }
            this.hierMesh().chunkSetAngles(Aircraft.Props[j][0], 0.0F, -this.propPos[j], 0.0F);
        }

    }

    protected void moveGear(float f) {
        if (this.typeDockableIsDocked()) {
            FW_190.moveGear(this.hierMesh(), 0.0F);
        } else {
            FW_190.moveGear(this.hierMesh(), f);
        }
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void update(float f) {
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
        if (this.FM instanceof Maneuver) {
            if (this.typeDockableIsDocked()) {
                if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                    ((Maneuver) this.FM).set_maneuver(48);
                    ((FlightModelMain) ((Maneuver) this.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft) this.queen_)).FM)).AP.way.Cur());
                    ((Pilot) this.FM).setDumbTime(3000L);
                }
            } else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                if (this.dtime > 0L) {
                    ((Maneuver) this.FM).set_maneuver(22);
                    ((Pilot) this.FM).setDumbTime(3000L);
                    if (Time.current() > (this.dtime + 3000L)) {
                        this.dtime = -1L;
                        ((Maneuver) this.FM).clear_stack();
                        ((Maneuver) this.FM).pop();
                        ((Pilot) this.FM).setDumbTime(0L);
                    }
                } else if ((this.FM.AP.way.curr().Action == 3) && (((Maneuver) this.FM).get_maneuver() == 24)) {
                    ((Maneuver) this.FM).set_maneuver(21);
                    ((Pilot) this.FM).setDumbTime(30000L);
                }
            }
        }
        if (this.typeDockableIsDocked()) {
            Aircraft aircraft = (Aircraft) this.typeDockableGetQueen();
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.AileronControl = this.FM.CT.AileronControl;
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.ElevatorControl = this.FM.CT.ElevatorControl;
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.RudderControl = this.FM.CT.RudderControl;
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.GearControl = this.FM.CT.GearControl;
        }
        if (this.FM.CT.saveWeaponControl[3]) {
            this.typeDockableAttemptDetach();
        }
        super.update(f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {

        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        if (Mission.isCoop() && !Mission.isServer() && !this.isSpawnFromMission() && this.net.isMaster()) {
            new MsgAction(64, 0.0D, this) {

                public void doAction() {
                    FW_190A8MSTL.this.onCoopMasterSpawned();
                }

            };
        }
        if (this.getGunByHookName("_MGUN01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("7mmC_D0", false);
            this.hierMesh().chunkVisible("7mmCowl_D0", true);
        }
        if (this.getGunByHookName("_CANNON01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmL1_D0", false);
        }
        if (this.getGunByHookName("_CANNON02") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmR1_D0", false);
        }
    }

    private void onCoopMasterSpawned() {
        Actor actor = null;
        String s = null;
        if (this.FM.AP.way.curr().getTargetName() == null) {
            this.FM.AP.way.next();
        }
        s = this.FM.AP.way.curr().getTargetName();
        if (s != null) {
            actor = Actor.getByName(s);
        }
        if (Actor.isValid(actor) && (actor instanceof Wing) && (actor.getOwnerAttachedCount() > 0)) {
            actor = (Actor) actor.getOwnerAttached(0);
        }
        this.FM.AP.way.setCur(0);
        if (Actor.isValid(actor) && (actor instanceof JU_88MSTL)) {
            try {
                Aircraft aircraft = (Aircraft) actor;
                float f = 100F;
                if (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).M.maxFuel > 0.0F) {
                    f = (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).M.fuel / ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).M.maxFuel) * 100F;
                }
                String s1 = "spawn " + actor.getClass().getName() + " NAME net" + actor.name() + " FUEL " + f + " WEAPONS " + ((NetAircraft) (aircraft)).thisWeaponsName + (((NetAircraft) (aircraft)).bPaintShemeNumberOn ? "" : " NUMBEROFF") + " OVR";
                CmdEnv.top().exec(s1);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
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
            if (Actor.isValid(this.target_)) {
                this.target_ = this.FM.AP.way.curr().getTargetActorRandom();
            }
        }
        if (Actor.isValid(this.target_) && (this.target_ instanceof JU_88MSTL) && this.isNetMaster()) {
            ((TypeDockable) this.target_).typeDockableRequestAttach(this, 0, true);
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
        if (!this.FM.AS.isMaster()) {
            return;
        }
        if (!this.typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
            if (aircraft instanceof JU_88MSTL) {
                ((TypeDockable) aircraft).typeDockableRequestAttach(this);
            }
        }
    }

    public void typeDockableAttemptDetach() {
        if (this.FM.AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_)) {
            ((TypeDockable) this.queen_).typeDockableRequestDetach(this);
        }
    }

    public void typeDockableRequestAttach(Actor actor1) {
    }

    public void typeDockableRequestDetach(Actor actor1) {
    }

    public void typeDockableRequestAttach(Actor actor1, int j, boolean flag1) {
    }

    public void typeDockableRequestDetach(Actor actor1, int j, boolean flag1) {
    }

    public void typeDockableDoAttachToDrone(Actor actor1, int j) {
    }

    public void typeDockableDoDetachFromDrone(int j) {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
        this.queen_ = actor;
        this.dockport_ = i;
        if (this.FM.EI.getNum() == 1) {
            this.FM.Scheme = 2;
            Aircraft aircraft = (Aircraft) actor;
            this.FM.EI.setNum(3);
            Motor motor = this.FM.EI.engines[0];
            this.FM.EI.engines = new Motor[3];
            this.FM.EI.engines[0] = motor;
            this.FM.EI.engines[1] = ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[0];
            this.FM.EI.engines[2] = ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.engines[1];
            this.FM.EI.bCurControl = (new boolean[] { true, true, true });
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.bCurControl[0] = false;
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).EI.bCurControl[1] = false;
        }
        this.FM.EI.setEngineRunning();
        this.FM.CT.setGearAirborne();
        this.moveGear(0.0F);
        this.FM.CT.GearControl = ((FlightModelMain) (((SndAircraft) ((Aircraft) actor)).FM)).CT.GearControl;
        com.maddox.il2.fm.FlightModel flightmodel = ((SndAircraft) ((Aircraft) this.queen_)).FM;
        if ((this.FM instanceof Maneuver) && (flightmodel instanceof Maneuver)) {
            Maneuver maneuver = (Maneuver) flightmodel;
            Maneuver maneuver1 = (Maneuver) this.FM;
            if ((maneuver.Group != null) && (maneuver1.Group != null) && (maneuver1.Group.numInGroup(this) == (maneuver1.Group.nOfAirc - 1))) {
                AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i) {
        if (this.dockport_ != i) {
            return;
        }
        this.queen_ = null;
        this.dockport_ = 0;
        this.FM.CT.setTrimElevatorControl(0.51F);
        this.FM.CT.trimElevator = 0.51F;
        this.FM.CT.setGearAirborne();
        if (this.FM.EI.getNum() == 3) {
            this.FM.Scheme = 1;
            this.FM.EI.setNum(1);
            Motor motor = this.FM.EI.engines[0];
            this.FM.EI.engines = new Motor[1];
            this.FM.EI.engines[0] = motor;
            this.FM.EI.bCurControl = (new boolean[] { true });
            for (int j = 1; j < 3; j++) {
                if (this.FM.Gears.clpEngineEff[j][0] != null) {
                    Eff3DActor.finish(this.FM.Gears.clpEngineEff[j][0]);
                    this.FM.Gears.clpEngineEff[j][0] = null;
                }
                if (this.FM.Gears.clpEngineEff[j][1] != null) {
                    Eff3DActor.finish(this.FM.Gears.clpEngineEff[j][1]);
                    this.FM.Gears.clpEngineEff[j][1] = null;
                }
            }

        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (this.typeDockableIsDocked()) {
            netmsgguaranted.writeByte(1);
            com.maddox.il2.engine.ActorNet actornet = null;
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

    private boolean bNeedSetup;
    private long    dtime;
    private Actor   target_;
    private Actor   queen_;
    private int     dockport_;

    static {
        Class class1 = FW_190A8MSTL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190A-8(Beta)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190A-8.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190F8MSTL.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 9, 9, 1, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 9, 9, 1, 1, 9, 9, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalDev02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev05", "_ExternalDev06", "_CANNON09", "_CANNON10", "_ExternalDev07", "_ExternalDev08", "_CANNON11", "_CANNON12", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02" });
    }

}
