package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

public class I_16TYPE24DRONE extends I_16 implements MsgCollisionRequestListener, TypeTNBFighter, TypeStormovik, TypeDockable, TypeDockableAutoDock {

    public I_16TYPE24DRONE() {
        this.queen_last = null;
        this.queen_time = 0L;
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.target_ = null;
        this.queen_ = null;
        // TODO: +++ Auto Docking Mode
        this.attemptDocking = false;
        this.presetDockBeep = null;
        this.fxDockBeep = null;
        this.lastDockTone = -2;
        this.lastHudRefresh = Long.MIN_VALUE;
        // ---
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if (this.queen_last != null && this.queen_last == actor && (this.queen_time == 0L || Time.current() < this.queen_time + 5000L)) aflag[0] = false;
        else aflag[0] = true;
    }

    public void update(float f) {
        if (this.bNeedSetup) this.checkAsDrone();
        int i = this.aircIndex();
        if (this.FM instanceof Maneuver) if (this.typeDockableIsDocked()) {
            if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                ((Maneuver) this.FM).unblock();
                ((Maneuver) this.FM).set_maneuver(48);
                for (int j = 0; j < i; j++)
                    ((Maneuver) this.FM).push(48);

                if (this.FM.AP.way.curr().Action != 3) ((Maneuver) this.FM).AP.way.setCur(((Aircraft) this.queen_).FM.AP.way.Cur());
                ((Pilot) this.FM).setDumbTime(3000L);
            }
            // TODO: +++ Patch Pack 107, limit refueling to previously selected fuel level
            // if (FM.M.fuel < FM.M.maxFuel)
            if (this.FM.M.fuel < this.initialFuel) // ---
                this.FM.M.fuel += 0.06F * f;
        } else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
            if (this.FM.EI.engines[0].getStage() == 0) this.FM.EI.setEngineRunning();
            if (this.dtime > 0L && ((Maneuver) this.FM).Group != null) {
                ((Maneuver) this.FM).Group.leaderGroup = null;
                ((Maneuver) this.FM).set_maneuver(22);
                ((Pilot) this.FM).setDumbTime(3000L);
                if (Time.current() > this.dtime + 3000L) {
                    this.dtime = -1L;
                    ((Maneuver) this.FM).clear_stack();
                    ((Maneuver) this.FM).set_maneuver(0);
                    ((Pilot) this.FM).setDumbTime(0L);
                }
            } else if (this.FM.AP.way.curr().Action == 0) {
                Maneuver maneuver = (Maneuver) this.FM;
                if (maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null) maneuver.Group.setGroupTask(2);
            }
        }

        // TODO: +++ Auto Docking Mode
        if (this == World.getPlayerAircraft() && this.attemptDocking) if (this.typeDockableIsDocked()) {
            this.attemptDocking = false;
            HUD.logCenter("");
            HUD.log("DOCK MODE OFF");
            this.showDockDistance(-2);
        } else this.typeDockableDoAttemptAttach();

        super.update(f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && this.FM.AP.way.curr().Action == 3 && this.typeDockableIsDocked() && Math.abs(((Aircraft) this.queen_).FM.Or.getKren()) < 3F) if (this.FM.isPlayers()) {
            if (this.FM instanceof RealFlightModel && !((RealFlightModel) this.FM).isRealMode()) {
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

    public void missionStarting() {
        this.checkAsDrone();
    }

    // TODO: +++ Patch Pack 107, limit refueling to previously selected fuel level
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.initialFuel = this.FM.M.fuel;
    }
    // ---

    private void checkAsDrone() {
        if (this.target_ == null) {
            if (this.FM.AP.way.curr().getTarget() == null) this.FM.AP.way.next();
            this.target_ = this.FM.AP.way.curr().getTarget();
            if (Actor.isValid(this.target_) && this.target_ instanceof Wing) {
                Wing wing = (Wing) this.target_;
                int i = this.aircIndex();
                if (Actor.isValid(wing.airc[i / 2])) this.target_ = wing.airc[i / 2];
                else this.target_ = null;
            }
        }
        if (Actor.isValid(this.target_) && this.target_ instanceof TB_3_4M_34R_SPB) {
            this.queen_last = this.target_;
            this.queen_time = Time.current();
            if (this.isNetMaster()) ((TypeDockable) this.target_).typeDockableRequestAttach(this, this.aircIndex() % 2, true);
        }
        this.bNeedSetup = false;
        this.target_ = null;
    }

    public int typeDockableGetDockport() {
        DebugLog("typeDockableAttemptAttach() = " + (this.typeDockableIsDocked() ? this.dockport_ : -1), 1);
        if (this.typeDockableIsDocked()) return this.dockport_;
        else return -1;
    }

    public Actor typeDockableGetQueen() {
        DebugLog("typeDockableAttemptAttach() = " + (this.queen_ == null ? "null" : this.queen_.getClass().getName()), 1);
        return this.queen_;
    }

    public boolean typeDockableIsDocked() {
        if (Actor.isValid(this.queen_) == this.last_docked) return this.last_docked;
        this.last_docked = Actor.isValid(this.queen_);
        DebugLog("typeDockableIsDocked() = " + this.last_docked, 1);
        return Actor.isValid(this.queen_);
    }

    // TODO: +++ Auto Docking Mode
    public void typeDockableAttemptAttach() {
        if (this == World.getPlayerAircraft()) {
            if (this.attemptDocking) {
                this.attemptDocking = false;
                HUD.logCenter("");
                HUD.log("DOCK MODE OFF");
                this.showDockDistance(-2);
            } else {
                this.attemptDocking = true;
                HUD.log("DOCK MODE ON");
                this.typeDockableDoAttemptAttach();
            }
        } else this.typeDockableDoAttemptAttach();
    }

    // public void typeDockableAttemptAttach() {
    public void typeDockableDoAttemptAttach() {
        // ---
        DebugLog("typeDockableAttemptAttach()", 1);
        if (this.FM.AS.isMaster() && !this.typeDockableIsDocked()) {
            // Aircraft aircraft = War.getNearestFriend(this);
            // if (aircraft instanceof TB_3_4M_34R_SPB)
            Aircraft aircraft = TB_3_4M_34R_SPB.GetNearestFriendlyInstance(this, 10000F);
            if (aircraft != null) ((TypeDockable) aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach() {
        DebugLog("typeDockableAttemptDetach() FM.AS.isMaster()=" + this.FM.AS.isMaster() + ", typeDockableIsDocked()=" + this.typeDockableIsDocked() + ", Actor.isValid(queen_)=" + Actor.isValid(this.queen_), 1);
        if (this.FM.AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_)) {
            DebugLog("Requesting " + this.queen_.getClass().getName() + " to detach us!", 1);
            ((TypeDockable) this.queen_).typeDockableRequestDetach(this);
        }
    }

    public void typeDockableRequestAttach(Actor actor) {
        DebugLog("typeDockableRequestAttach(" + (actor == null ? "null" : actor.getClass().getName()) + ")", 1);
    }

    public void typeDockableRequestDetach(Actor actor) {
        DebugLog("typeDockableRequestDetach(" + (actor == null ? "null" : actor.getClass().getName()) + ")", 1);
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
        DebugLog("typeDockableRequestAttach(" + (actor == null ? "null" : actor.getClass().getName()) + ", " + i + ", " + flag + ")", 1);
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
        DebugLog("typeDockableRequestDetach(" + (actor == null ? "null" : actor.getClass().getName()) + ", " + i + ", " + flag + ")", 1);
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
        DebugLog("typeDockableDoAttachToDrone(" + (actor == null ? "null" : actor.getClass().getName()) + ", " + i + ")", 1);
    }

    public void typeDockableDoDetachFromDrone(int i) {
        DebugLog("typeDockableDoDetachFromDrone(" + i + ")", 1);
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
        DebugLog("typeDockableDoAttachToQueen(" + (actor == null ? "null" : actor.getClass().getName()) + ", " + i + ")", 1);
        this.queen_ = actor;
        this.dockport_ = i;
        this.queen_last = this.queen_;
        this.queen_time = 0L;
        this.FM.EI.setEngineRunning();
        this.FM.CT.setGearAirborne();
        this.moveGear(0.0F);
        FlightModel flightmodel = ((Aircraft) this.queen_).FM;
        if (this.aircIndex() == 0 && this.FM instanceof Maneuver && flightmodel instanceof Maneuver) {
            Maneuver maneuver = (Maneuver) flightmodel;
            Maneuver maneuver1 = (Maneuver) this.FM;
            if (maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1) {
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
        DebugLog("typeDockableDoDetachFromQueen(" + i + "), dockport_=" + this.dockport_, 1);

        if (this.FM.getAltitude() - (float) Engine.land().HQ(this.FM.Loc.x, this.FM.Loc.y) < 10F) {
            DebugLog("We're close to the ground, extend landing gear.", 1);
            this.FM.CT.forceGear(1.0F);
            this.moveGear(0.0F);
        }

        if (this.dockport_ == i) {
            DebugLog("dockport_ == i", 1);
            this.queen_last = this.queen_;
            this.queen_time = Time.current();
            this.queen_ = null;
            this.dockport_ = -1; // was 0 before, which would be "attached to left port"
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        DebugLog("typeDockableReplicateToNet(" + netmsgguaranted.dataLength() + ")", 1);
        if (this.typeDockableIsDocked()) {
            DebugLog("typeDockableIsDocked() = true", 1);
            netmsgguaranted.writeByte(1);
            ActorNet actornet = null;
            if (Actor.isValid(this.queen_)) {
                actornet = this.queen_.net;
                if (actornet.countNoMirrors() > 0) actornet = null;
            }
            DebugLog("actornet=" + (actornet == null ? "null" : actornet.getClass().getName()) + ", dockport_=" + this.dockport_, 1);
            netmsgguaranted.writeByte(this.dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else {
            DebugLog("typeDockableIsDocked() = false", 1);
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        DebugLog("typeDockableReplicateFromNet(" + netmsginput.available() + ")", 1);
        if (netmsginput.readByte() == 1) {
            this.dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            DebugLog("netmsginput.readByte()=1, dockport_=" + this.dockport_ + ", netobj=" + (netobj == null ? "null" : netobj.getClass().getName()), 1);
            if (netobj != null) {
                Actor actor = (Actor) netobj.superObj();
                DebugLog("actor=" + (actor == null ? "null" : actor.getClass().getName()), 1);
                ((TypeDockable) actor).typeDockableDoAttachToDrone(this, this.dockport_);
            }
        }
    }

    // TODO: +++ Auto Docking Mode
    public boolean TypeDockableAutoDockIsDockModeActive() {
        return this.attemptDocking;
    }

    private void initSounds() {
        if (this.presetDockBeep == null) {
            this.presetDockBeep = new SoundPreset("sound.dock");
            if (this.presetDockBeep == null) return;
        }
        if (this.fxDockBeep == null) {
            this.fxDockBeep = new SoundFX[this.NUM_DOCK_SOUNDS];
            for (int i = 0; i < this.NUM_DOCK_SOUNDS; i++) {
                this.fxDockBeep[i] = World.getPlayerAircraft().newSound(this.presetDockBeep, false, false);
                this.fxDockBeep[i].setParent(World.getPlayerAircraft().getRootFX());
                this.fxDockBeep[i].setUsrFlag(i);
            }
        }
    }

    public void showDockDistance(int distance) {
        if (!Config.isUSE_RENDER() || this != World.getPlayerAircraft()) return;
        boolean newLockTone = this.lastDockTone != distance;
        if (newLockTone) {
            this.initSounds();
            if (distance < 0) {
                if (this.lastDockTone >= 0) this.fxDockBeep[this.lastDockTone].stop();
                this.lastDockTone = -1;
            } else {
                if (this.lastDockTone >= 0) this.fxDockBeep[this.lastDockTone].stop();
                this.lastDockTone = distance;
                this.fxDockBeep[this.lastDockTone].setVolume(1.0F);
                this.fxDockBeep[this.lastDockTone].setPlay(true);
            }
        }
        if (newLockTone || Time.current() > this.lastHudRefresh + HUD.logCenterTimeLife) {
            this.lastHudRefresh = Time.current();
            if (distance < 0) {
                if (distance < -1) HUD.logCenter("");
                else HUD.logCenter(
                        "\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA\u25CA");
            } else {
                StringBuffer logString = new StringBuffer(
                        "\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CF\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB\u25CB");
                for (int i = 0; i < distance; i++) {
                    logString.setCharAt(19 - i, '\u25CF');
                    logString.setCharAt(21 + i, '\u25CF');
                }
                HUD.logCenter(logString.toString());
            }
        }
    }
    // ---

    private static void DebugLog(String logLine, int minLogLevel) {
        if (DEBUG >= minLogLevel) System.out.println("[I_16TYPE24DRONE] " + logLine);
    }

    public static int DEBUG = 0;

    // TODO: +++ Auto Docking Mode
    private boolean     attemptDocking;
    private SoundPreset presetDockBeep;
    private SoundFX     fxDockBeep[];
    private final int   NUM_DOCK_SOUNDS = 21;
    private int         lastDockTone;
    private long        lastHudRefresh;
    // ---

    private boolean last_docked = false;
    private Actor   queen_last;
    private long    queen_time;
    private boolean bNeedSetup;
    private long    dtime;
    private Actor   target_;
    private Actor   queen_;
    private int     dockport_;
    // TODO: +++ Patch Pack 107, limit refueling to previously selected fuel level
    private float   initialFuel;
    // ---

    static {
        Class class1 = I_16TYPE24DRONE.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type24(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type24/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type24.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE24_SPB.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb01", "_ExternalBomb02",
                "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
    }
}
