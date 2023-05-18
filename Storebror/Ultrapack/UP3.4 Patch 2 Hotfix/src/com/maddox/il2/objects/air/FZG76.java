package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class FZG76 extends Scheme1 implements TypeDockable, MsgCollisionRequestListener {

    public FZG76() {
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.queen_last = null;
        this.queen_time = 0L;
        this.target_ = null;
        this.queen_ = null;
        this.flame = null;
        this.trail = null;
        this.sprite = null;
        this.dust = null;
        this.bHasEngine = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.pk = 0;
        this.bStalled = true;
        this.soundfx = null;
        this.distance = 250F;
        this.posX = 0.0F;
        this.posY = 0.0F;
        this.bBombtargets = false;
        this.bBombdrop = false;
        this.trimmer = 0.0F;
    }

    public void destroy() {
        if (Actor.isValid(this.sprite)) {
            this.sprite.destroy();
        }
        if (Actor.isValid(this.flame)) {
            this.flame.destroy();
        }
        if (Actor.isValid(this.trail)) {
            this.trail.destroy();
        }
        if (Actor.isValid(this.dust)) {
            this.dust.destroy();
        }
        if (this.soundfx != null) {
            this.soundfx.cancel();
            this.soundfx = null;
        }
        super.destroy();
    }

    public void onAircraftLoaded() {
        this.FM.Gears.setHydroOperable(false);
        this.FM.Gears.setOperable(false);
        this.FM.CT.bHasBrakeControl = false;
        this.FM.setCapableOfTaxiing(false);
        this.FM.setCapableOfACM(false);
        this.FM.EI.setThrottle(1.0F);
        this.FM.EI.engines[0].doSetKillControlThrottle();
        this.FM.AS.bWingTipLExists = false;
        this.FM.AS.bWingTipRExists = false;
        this.FM.AS.bNavLightsOn = false;
        this.FM.AS.bLandingLightOn = false;
        this.FM.AS.bIsAboutToBailout = false;
        super.onAircraftLoaded();
        if ((this.soundfx = this.newSound("weapon.fau_2", true)) != null) {
            this.soundfx.setPosition(this.FM.EI.engines[0].getEnginePos());
            this.soundfx.setParent(this.sndRoot);
        }
        if (Config.isUSE_RENDER()) {
            this.sprite = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100S.eff", -1F);
            this.flame = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109F.eff", -1F);
            this.trail = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Tracers/ImpulseRocket/rocket.eff", -1F);
            this.dust = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100D.eff", -1F);
            Eff3DActor.setIntesity(this.sprite, 0.0F);
            Eff3DActor.setIntesity(this.flame, 0.0F);
            Eff3DActor.setIntesity(this.trail, 0.0F);
            Eff3DActor.setIntesity(this.dust, 0.0F);
        }
        if (this.FM instanceof Pilot) {
            ((Pilot) this.FM).kamikaze = true;
            ((Pilot) this.FM).silence = true;
        }
        if (this.thisWeaponsName.endsWith("_50km")) {
            this.distance = 49F;
        } else if (this.thisWeaponsName.endsWith("_55km")) {
            this.distance = 54F;
        } else if (this.thisWeaponsName.endsWith("_60km")) {
            this.distance = 59F;
        } else if (this.thisWeaponsName.endsWith("_65km")) {
            this.distance = 64F;
        } else if (this.thisWeaponsName.endsWith("_70km")) {
            this.distance = 69F;
        } else if (this.thisWeaponsName.endsWith("_75km")) {
            this.distance = 74F;
        } else if (this.thisWeaponsName.endsWith("_80km")) {
            this.distance = 79F;
        } else if (this.thisWeaponsName.endsWith("_85km")) {
            this.distance = 84F;
        } else if (this.thisWeaponsName.endsWith("_90km")) {
            this.distance = 89F;
        } else if (this.thisWeaponsName.endsWith("_95km")) {
            this.distance = 94F;
        } else if (this.thisWeaponsName.endsWith("_100km")) {
            this.distance = 99F;
        } else if (this.thisWeaponsName.endsWith("_125km")) {
            this.distance = 124F;
        } else if (this.thisWeaponsName.endsWith("_150km")) {
            this.distance = 149F;
        } else if (this.thisWeaponsName.endsWith("_175km")) {
            this.distance = 174F;
        } else if (this.thisWeaponsName.endsWith("_200km")) {
            this.distance = 199F;
        } else if (this.thisWeaponsName.endsWith("_225km")) {
            this.distance = 224F;
        } else if (this.thisWeaponsName.endsWith("_250km")) {
            this.distance = 249F;
        }
        this.distance += World.Rnd().nextFloat(0.0F, 2.0F);
    }

    protected void moveAileron(float f1) {
    }

    protected void moveElevator(float f1) {
    }

    protected void moveRudder(float f1) {
    }

    public void doMurderPilot(int j) {
    }

    public void doWoundPilot(int j, float f1) {
    }

    protected void moveFan(float f) {
        this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
        if (this.pk >= 1) {
            this.pk = 1;
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("Prop1_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("PropRot1_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("Prop1_D0", 0.0F, this.dynamoOrient, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (!this.typeDockableIsDocked()) {
            if (s.startsWith("xcf")) {
                if (this.chunkDamageVisible("CF") < 1) {
                    this.hitChunk("CF", shot);
                    if ((this.getEnergyPastArmor(2.1F, shot) > 0.0F) && (shot.powerType == 3) && (World.Rnd().nextFloat() < 0.1F)) {
                        this.FM.AS.hitTank(shot.initiator, 0, 2);
                        this.debuggunnery("Fuel Tank: Hit..");
                    }
                }
            } else if (s.startsWith("xtail")) {
                if (this.chunkDamageVisible("Tail1") < 1) {
                    this.hitChunk("Tail1", shot);
                    if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 5);
                    }
                }
            } else if (s.startsWith("xkeel")) {
                this.hitChunk("Engine1", shot);
            } else if (s.startsWith("xstabl")) {
                this.hitChunk("WingLIn", shot);
            } else if (s.startsWith("xstabr")) {
                this.hitChunk("WingRIn", shot);
            } else if (s.startsWith("xwinglin")) {
                if (this.chunkDamageVisible("WingLIn") < 1) {
                    this.hitChunk("WingLIn", shot);
                }
            } else if (s.startsWith("xwingrin")) {
                if (this.chunkDamageVisible("WingRIn") < 1) {
                    this.hitChunk("WingRIn", shot);
                }
            } else if (s.startsWith("xengine") && (this.chunkDamageVisible("Engine1") < 1)) {
                this.hitChunk("Engine1", shot);
            }
        } else if ((s.startsWith("xcf") || s.startsWith("xtail")) && (this.getEnergyPastArmor(15F, shot) > 0.0F) && (shot.powerType == 3) && (World.Rnd().nextFloat() < 0.025F)) {
            this.debuggunnery("*** FZG76 Detonates..");
            this.FM.AS.hitTank(shot.initiator, 0, 100);
            this.msgCollision(this, "CF_D0", "CF_D0");
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 33:
            case 36:
                this.FM.CT.bHasAileronControl = false;
                this.FM.CT.bHasElevatorControl = false;
                break;

            case 19:
                super.cutFM(3, j, actor);
                this.FM.cut(33, j, actor);
                this.FM.cut(36, j, actor);
                super.cutFM(2, j, actor);
                break;

            case 3:
                this.FM.EI.engines[0].setEngineDies(actor);
                if (this.soundfx != null) {
                    this.soundfx.cancel();
                    this.soundfx = null;
                }
                this.doRemoveEngine();
                this.bHasEngine = false;
                this.FM.M.massEmpty -= 153F;
                this.FM.Sq.dragEngineCx[0] = 0.0F;
                this.FM.Sq.liftKeel = 0.0F;
                this.FM.Sq.squareRudders = 0.0F;
                this.FM.SensYaw = 0.0F;
                this.FM.SensRoll = 0.0F;
                this.FM.CT.bHasAileronControl = false;
                this.FM.setReadyToDie(true);
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    private void doRemoveEngine() {
        Aircraft aircraft = (Aircraft) ((Interpolate) this.FM).actor;
        if (aircraft.hierMesh().chunkFindCheck("Engine1_D0") != -1) {
            aircraft.hierMesh().hideSubTrees("Engine1_D0");
            Wreckage wreckage = new Wreckage((ActorHMesh) ((Interpolate) this.FM).actor, aircraft.hierMesh().chunkFind("Engine1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(aircraft.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    protected void doExplosion() {
        super.doExplosion();
        if ((this.FM.Loc.z - 10D) < World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y)) {
            if (Engine.land().isWater(this.FM.Loc.x, this.FM.Loc.y)) {
                Explosions.BOMB1000a_Water(this.FM.Loc, 1.0F, 1.0F);
            } else {
                Explosions.BOMB1000a_Land(this.FM.Loc, 1.0F, 1.0F, false);
            }
        } else {
            Explosions.BOMB1000a_Object(this.FM.Loc, 1.0F, 1.0F);
        }
    }

    public void update(float f) {
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
        if (this.FM instanceof Maneuver) {
            if (this.typeDockableIsDocked()) {
                if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                    ((Maneuver) this.FM).unblock();
                    ((Maneuver) this.FM).set_maneuver(48);
                    ((Maneuver) this.FM).AP.way.setCur(((Aircraft) this.queen_).FM.AP.way.Cur());
                    ((Pilot) this.FM).setDumbTime(3000L);
                }
            } else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                if (this.FM.EI.engines[0].getStage() == 0) {
                    if (this.bStalled && ((this.FM.getSpeedKMH() <= 350F) || ((float) this.FM.Loc.z >= 4000F))) {
                        this.FM.CT.bHasAileronControl = false;
                        this.FM.CT.bHasRudderControl = false;
                        this.FM.CT.bHasElevatorControl = false;
                        this.FM.Sq.dragParasiteCx = 0.6F;
                        this.FM.Sq.dragFuselageCx = 0.6F;
                    } else if (this.bStalled && (this.FM.getSpeedKMH() > 350F) && ((float) this.FM.Loc.z < 4000F)) {
                        if (this.bHasEngine && (this.FM.M.fuel > 0.0F)) {
                            this.FM.EI.setEngineRunning();
                        }
                    } else if (!this.bStalled && ((float) this.FM.Loc.z >= 4000F)) {
                        this.FM.EI.engines[0].doSetEngineDies();
                        if (this.soundfx != null) {
                            this.soundfx.cancel();
                            this.soundfx = null;
                        }
                    } else if (!this.bStalled && ((float) this.FM.Loc.z < 4000F)) {
                        this.FM.EI.setEngineRunning();
                    }
                    this.FM.CT.StabilizerControl = true;
                    this.bStalled = false;
                }
                if (this.dtime > 0L) {
                    ((Maneuver) this.FM).setBusy(false);
                    ((Maneuver) this.FM).Group.leaderGroup = null;
                    ((Maneuver) this.FM).set_maneuver(22);
                    ((Pilot) this.FM).setDumbTime(3000L);
                    if (Time.current() > (this.dtime + 3000L)) {
                        this.dtime = -1L;
                        ((Maneuver) this.FM).clear_stack();
                        ((Maneuver) this.FM).pop();
                        ((Pilot) this.FM).setDumbTime(0L);
                    }
                }
            }
        }
        super.update(f);
        if (this.FM.AS.isMaster() && (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode())) {
            if (!this.bBombdrop && !this.typeDockableIsDocked()) {
                this.posX = (float) this.FM.Loc.x;
                this.posY = (float) this.FM.Loc.y;
                this.bBombdrop = true;
            }
            if (this.FM.isTick(44, 0) && !this.bBombtargets && this.bBombdrop) {
                float f1 = (float) (this.FM.Loc.x - this.posX);
                float f2 = (float) (this.FM.Loc.y - this.posY);
                float f3 = (float) Math.sqrt((f1 * f1) + (f2 * f2)) / 1000F;
                if (this.distance < f3) {
                    this.bBombtargets = true;
                }
            }
            if (this.bBombtargets) {
                this.FM.EI.setEngineStops();
                this.FM.CT.StabilizerControl = false;
                ((Maneuver) this.FM).kamikaze = true;
                ((Maneuver) this.FM).set_task(7);
                ((Maneuver) this.FM).clear_stack();
                ((Maneuver) this.FM).set_maneuver(46);
                if (this.FM.Or.getTangage() > -45F) {
                    this.FM.CT.setTrimElevatorControl(-0.99F);
                    this.FM.CT.trimElevator = -0.99F;
                    this.FM.CT.ElevatorControl = -0.99F;
                } else if (this.FM.Or.getTangage() <= -45.5F) {
                    this.FM.CT.setTrimElevatorControl(0.99F);
                    this.FM.CT.trimElevator = 0.99F;
                    this.FM.CT.ElevatorControl = 0.99F;
                }
            } else if ((this.FM.M.fuel == 0.0F) && !this.typeDockableIsDocked()) {
                this.FM.EI.setEngineStops();
            }
        }
        if (!this.isNetMirror() && !this.typeDockableIsDocked()) {
            if (this.FM.CT.bHasAileronControl) {
                if ((this.FM.Or.getKren() < -15F) || (this.FM.Or.getKren() > 15F)) {
                    this.FM.CT.bHasAileronControl = false;
                    this.FM.CT.bHasRudderControl = false;
                    this.FM.CT.bHasElevatorControl = false;
                } else {
                    this.FM.SensRoll = (90F - Math.abs(this.FM.Or.getKren())) / 2880F;
                }
            }
            if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                if ((this.FM.getSpeedKMH() > 200F) && !this.bBombtargets) {
                    if (this.FM.getVertSpeed() > 0.01F) {
                        if (this.trimmer > -0.98F) {
                            this.trimmer = this.FM.CT.getTrimElevatorControl() - 0.01F;
                        }
                        this.FM.CT.setTrimElevatorControl(this.trimmer);
                    } else if (this.FM.getVertSpeed() < -0.01F) {
                        if (this.trimmer < 0.98F) {
                            this.trimmer = this.FM.CT.getTrimElevatorControl() + 0.01F;
                        }
                        this.FM.CT.setTrimElevatorControl(this.trimmer);
                    }
                }
            }
            if (World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) >= (this.FM.Loc.z - 1.0D)) {
                this.doExplosion();
                MsgExplosion.send(this, null, this.FM.Loc, Engine.cur.actorLand, 0.0F, 2400F, 0, 600F);
                this.FM.AS.explodeTank(this, 0);
            }
        }
        if (Config.isUSE_RENDER()) {
            if (this.FM.EI.getThrustOutput() > 0.0F) {
                if (this.soundfx != null) {
                    this.soundfx.setVolume(1.0F);
                }
                Eff3DActor.setIntesity(this.sprite, 1.0F);
                Eff3DActor.setIntesity(this.flame, 1.0F);
                Eff3DActor.setIntesity(this.trail, 1.0F);
                Eff3DActor.setIntesity(this.dust, 1.0F);
            } else {
                if (this.soundfx != null) {
                    this.soundfx.setVolume(0.0F);
                }
                Eff3DActor.setIntesity(this.sprite, 0.0F);
                Eff3DActor.setIntesity(this.flame, 0.0F);
                Eff3DActor.setIntesity(this.trail, 0.0F);
                Eff3DActor.setIntesity(this.dust, 0.0F);
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

    public void msgEndAction(Object obj, int i) {
        super.msgEndAction(obj, i);
        switch (i) {
            case 2:
                Actor actor = null;
                if (Actor.isValid(this.queen_last)) {
                    actor = this.queen_last;
                } else {
                    actor = Engine.cur.actorLand;
                }
                MsgExplosion.send(this, null, this.FM.Loc, actor, 0.0F, 1400F, 0, 600F);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && (this.FM.AP.way.curr().Action == 3) && this.typeDockableIsDocked() && (Math.abs(((Aircraft) this.queen_).FM.Or.getKren()) < 5F) && (this.FM.AP.getWayPointDistance() < 1000D) && (this.FM.AP.getWayPointDistance() > 100D)) {
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

    public void missionStarting() {
        this.checkAsDrone();
        this.bStalled = this.typeDockableIsDocked();
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
                if (Actor.isValid(wing.airc[i])) {
                    this.target_ = wing.airc[i];
                } else {
                    this.target_ = null;
                }
            }
        }
        if (Actor.isValid(this.target_) && (this.target_ instanceof HE_111H22)) {
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
        if (this.FM.AS.isMaster() && !this.typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
            if (aircraft instanceof HE_111H22) {
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
        this.queen_last = this.queen_;
        this.queen_time = 0L;
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

    private boolean    bNeedSetup;
    private long       dtime;
    private Actor      queen_last;
    private long       queen_time;
    private Actor      target_;
    private Actor      queen_;
    private int        dockport_;
    private Eff3DActor flame;
    private Eff3DActor trail;
    private Eff3DActor sprite;
    private Eff3DActor dust;
    private boolean    bHasEngine;
    private float      dynamoOrient;
    private boolean    bDynamoRotary;
    private int        pk;
    private boolean    bStalled;
    private SoundFX    soundfx;
    private float      distance;
    private float      posX;
    private float      posY;
    private boolean    bBombtargets;
    private boolean    bBombdrop;
    private float      trimmer;

    static {
        Class class1 = FZG76.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FZG-76");
        Property.set(class1, "meshName", "3do/Plane/FZG-76/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/FZG-76.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMXY_7.class });
        Aircraft.weaponTriggersRegister(class1, new int[1]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_Clip00" });
    }
}
