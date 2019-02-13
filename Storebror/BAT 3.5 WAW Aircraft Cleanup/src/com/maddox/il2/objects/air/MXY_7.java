package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class MXY_7 extends Scheme2a implements MsgCollisionRequestListener, TypeDockable {

    public MXY_7() {
        this.flame = new Eff3DActor[3];
        this.dust = new Eff3DActor[3];
        this.trail = new Eff3DActor[3];
        this.sprite = new Eff3DActor[3];
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.queen_last = null;
        this.queen_time = 0L;
        this.target_ = null;
        this.queen_ = null;
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if ((this.queen_last != null) && (this.queen_last == actor) && ((this.queen_time == 0L) || (Time.current() < (this.queen_time + 5000L)))) {
            aflag[0] = false;
        } else {
            aflag[0] = true;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER()) {
            for (int i = 0; i < 3; i++) {
                this.flame[i] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
                this.dust[i] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100D.eff", -1F);
                this.trail[i] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100T.eff", -1F);
                this.sprite[i] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100S.eff", -1F);
                Eff3DActor.setIntesity(this.flame[i], 0.0F);
                Eff3DActor.setIntesity(this.dust[i], 0.0F);
                Eff3DActor.setIntesity(this.trail[i], 0.0F);
                Eff3DActor.setIntesity(this.sprite[i], 0.0F);
            }

        }
    }

    public void doMurderPilot(int i) {
        if (i != 0) {
            return;
        } else {
            this.hierMesh().chunkVisible("Pilot1_D0", false);
            this.hierMesh().chunkVisible("Head1_D0", false);
            this.hierMesh().chunkVisible("Pilot1_D1", true);
            this.hierMesh().chunkVisible("HMask1_D0", false);
            return;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xcf")) {
            this.hitChunk("CF", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 1) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel1")) {
            this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xkeel2")) {
            this.hitChunk("Keel2", shot);
        } else if (s.startsWith("xrudder1")) {
            this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xrudder2")) {
            this.hitChunk("Rudder2", shot);
        } else if (s.startsWith("xstabl")) {
            this.hitChunk("StabL", shot);
        } else if (s.startsWith("xvator")) {
            this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin")) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin")) {
                this.hitChunk("WingRIn", shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i;
            if (s.endsWith("a")) {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else {
                i = s.charAt(5) - 49;
            }
            this.hitFlesh(i, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 3: // '\003'
            case 19: // '\023'
                ((FlightModelMain) (super.FM)).AS.setEngineDies(this, 0);
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
    }

    public void msgEndAction(Object obj, int i) {
        super.msgEndAction(obj, i);
        switch (i) {
            case 2: // '\002'
                Actor actor = null;
                if (Actor.isValid(this.queen_last)) {
                    actor = this.queen_last;
                } else {
                    actor = Engine.cur.actorLand;
                }
                MsgExplosion.send(this, null, ((FlightModelMain) (super.FM)).Loc, actor, 0.0F, 600F, 0, 600F);
                break;
        }
    }

    protected void doExplosion() {
        super.doExplosion();
        if ((((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).z - 10D) < World.land().HQ_Air(((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).x, ((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).y)) {
            if (Engine.land().isWater(((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).x, ((Tuple3d) (((FlightModelMain) (super.FM)).Loc)).y)) {
                Explosions.BOMB250_Water(((FlightModelMain) (super.FM)).Loc, 1.0F, 1.0F);
            } else {
                Explosions.BOMB250_Land(((FlightModelMain) (super.FM)).Loc, 1.0F, 1.0F, true, false);
            }
        }
    }

    public void update(float f) {
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
        if (super.FM instanceof Maneuver) {
            if (this.typeDockableIsDocked()) {
                if (!(super.FM instanceof RealFlightModel) || !((RealFlightModel) super.FM).isRealMode()) {
                    ((Maneuver) super.FM).unblock();
                    ((Maneuver) super.FM).set_maneuver(48);
                    ((FlightModelMain) ((Maneuver) super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft) this.queen_)).FM)).AP.way.Cur());
                    ((Pilot) super.FM).setDumbTime(3000L);
                }
            } else if (!(super.FM instanceof RealFlightModel) || !((RealFlightModel) super.FM).isRealMode()) {
                if (((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0) {
                    ((FlightModelMain) (super.FM)).EI.setEngineRunning();
                }
                if (this.dtime > 0L) {
                    ((Maneuver) super.FM).setBusy(false);
                    ((Maneuver) super.FM).Group.leaderGroup = null;
                    ((Maneuver) super.FM).set_maneuver(22);
                    ((Pilot) super.FM).setDumbTime(3000L);
                    if (Time.current() > (this.dtime + 3000L)) {
                        this.dtime = -1L;
                        ((Maneuver) super.FM).clear_stack();
                        ((Maneuver) super.FM).pop();
                        ((Pilot) super.FM).setDumbTime(0L);
                    }
                }
            }
        }
        super.update(f);
        if (((FlightModelMain) (super.FM)).AS.isMaster()) {
            for (int i = 0; i < 3; i++) {
                if ((((FlightModelMain) (super.FM)).CT.PowerControl > 0.77F) && (((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 0) && (((FlightModelMain) (super.FM)).M.fuel > 0.0F) && !this.typeDockableIsDocked()) {
                    ((FlightModelMain) (super.FM)).EI.engines[i].setStage(this, 6);
                }
                if (((((FlightModelMain) (super.FM)).CT.PowerControl < 0.77F) && (((FlightModelMain) (super.FM)).EI.engines[i].getStage() > 0)) || (((FlightModelMain) (super.FM)).M.fuel == 0.0F)) {
                    ((FlightModelMain) (super.FM)).EI.engines[i].setEngineStops(this);
                }
            }

            if (Config.isUSE_RENDER()) {
                for (int j = 0; j < 3; j++) {
                    if ((((FlightModelMain) (super.FM)).EI.engines[j].getw() > 50F) && (((FlightModelMain) (super.FM)).EI.engines[j].getStage() == 6)) {
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, j, 1);
                    } else {
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, j, 0);
                    }
                }

            }
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && (((FlightModelMain) (super.FM)).AP.way.curr().Action == 3) && this.typeDockableIsDocked() && (Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft) this.queen_)).FM)).Or.getKren()) < 3F)) {
            if (super.FM.isPlayers()) {
                if ((super.FM instanceof RealFlightModel) && !((RealFlightModel) super.FM).isRealMode()) {
                    this.typeDockableAttemptDetach();
                    ((Maneuver) super.FM).set_maneuver(22);
                    ((Maneuver) super.FM).setCheckStrike(false);
                    ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
                    this.dtime = Time.current();
                }
            } else {
                this.typeDockableAttemptDetach();
                ((Maneuver) super.FM).set_maneuver(22);
                ((Maneuver) super.FM).setCheckStrike(false);
                ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
                this.dtime = Time.current();
            }
        }
    }

    public void missionStarting() {
        this.checkAsDrone();
    }

    private void checkAsDrone() {
        if (this.target_ == null) {
            if (((FlightModelMain) (super.FM)).AP.way.curr().getTarget() == null) {
                ((FlightModelMain) (super.FM)).AP.way.next();
            }
            this.target_ = ((FlightModelMain) (super.FM)).AP.way.curr().getTarget();
            if (Actor.isValid(this.target_) && (this.target_ instanceof Wing)) {
                Wing wing = (Wing) this.target_;
                int i = this.aircIndex();
                if (Actor.isValid(wing.airc[i])) {
                    this.target_ = wing.airc[i];
                } else {
                    this.target_ = null;
                }
            }
        }
        if (Actor.isValid(this.target_) && (this.target_ instanceof G4M2E)) {
            this.queen_last = this.target_;
            this.queen_time = Time.current();
            if (this.isNetMaster()) {
                ((TypeDockable) this.target_).typeDockableRequestAttach(this, 0, true);
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
        if (!((FlightModelMain) (super.FM)).AS.isMaster()) {
            return;
        }
        if (!this.typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
            if (aircraft instanceof G4M2E) {
                ((TypeDockable) aircraft).typeDockableRequestAttach(this);
            }
        }
    }

    public void typeDockableAttemptDetach() {
        if (((FlightModelMain) (super.FM)).AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_)) {
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
        this.queen_last = this.queen_;
        this.queen_time = 0L;
    }

    public void typeDockableDoDetachFromQueen(int i) {
        if (this.dockport_ != i) {
            return;
        } else {
            this.queen_last = this.queen_;
            this.queen_time = Time.current();
            this.queen_ = null;
            this.dockport_ = 0;
            return;
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

    public void doSetSootState(int i, int j) {
        switch (j) {
            case 0: // '\0'
                Eff3DActor.setIntesity(this.flame[i], 0.0F);
                Eff3DActor.setIntesity(this.dust[i], 0.0F);
                Eff3DActor.setIntesity(this.trail[i], 0.0F);
                Eff3DActor.setIntesity(this.sprite[i], 0.0F);
                break;

            case 1: // '\001'
                Eff3DActor.setIntesity(this.flame[i], 1.0F);
                Eff3DActor.setIntesity(this.dust[i], 0.5F);
                Eff3DActor.setIntesity(this.trail[i], 1.0F);
                Eff3DActor.setIntesity(this.sprite[i], 1.0F);
                break;
        }
    }

    private Eff3DActor flame[];
    private Eff3DActor dust[];
    private Eff3DActor trail[];
    private Eff3DActor sprite[];
    private boolean    bNeedSetup;
    private long       dtime;
    private Actor      queen_last;
    private long       queen_time;
    private Actor      target_;
    private Actor      queen_;
    private int        dockport_;

    static {
        Class class1 = MXY_7.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MXY");
        Property.set(class1, "meshName", "3DO/Plane/MXY-7-11(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/MXY-7-11.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMXY_7.class });
        Aircraft.weaponTriggersRegister(class1, new int[1]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_Clip00" });
    }
}
