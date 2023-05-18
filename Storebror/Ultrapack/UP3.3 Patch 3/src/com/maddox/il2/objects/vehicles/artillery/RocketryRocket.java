/*4.10.1 class*/
package com.maddox.il2.objects.vehicles.artillery;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Message;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Time;

public class RocketryRocket extends ActorHMesh implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Prey {
    static final int                  DMG_LWING      = 1;
    static final int                  DMG_RWING      = 2;
    static final int                  DMG_LHYRO      = 4;
    static final int                  DMG_RHYRO      = 8;
    static final int                  DMG_ENGINE     = 16;
    static final int                  DMG_FUEL       = 32;
    static final int                  DMG_DEAD       = 64;
    static final int                  DMG_ANY_       = 127;
    static final int                  STA_HELL       = -2;
    static final int                  STA_WAIT       = -1;
    static final int                  STARMP_RAMP    = 0;
    static final int                  STARMP_GOUP    = 1;
    static final int                  STARMP_SPEEDUP = 2;
    static final int                  STARMP_TRAVEL  = 3;
    static final int                  STARMP_GODOWN  = 4;
    static final int                  STARMP_GROUND  = 5;
    static final int                  STAAIR_TRAVEL  = 0;
    static final int                  STAAIR_GODOWN  = 1;
    static final int                  STAAIR_GROUND  = 2;
    private int                       dmg            = 0;
    private int                       fallMode       = -1;
    private float                     fallVal        = 0.0F;
    private int                       sta            = -1;
    private RocketryGeneric.TrajSeg[] traj           = null;
    private RocketryGeneric           ramp           = null;
    private RocketryWagon             wagon          = null;
    int                               idR;
    int                               randseed;
    long                              timeOfStartMS;
    private long                      countdownTicks = 0L;
    private Eff3DActor                eng_trail      = null;
    private static Point3d            tmpP           = new Point3d();
    private static Vector3d           tmpV           = new Vector3d();
    private static Vector3d           tmpV3d0        = new Vector3d();
    private static Vector3d           tmpV3d1        = new Vector3d();
    private static Loc                tmpL           = new Loc();
    private static RangeRandom        rndSeed        = new RangeRandom();

    class Move extends Interpolate {
        private void disappear() {
            RocketryRocket.this.ramp.forgetRocket((RocketryRocket) this.actor);
            if (RocketryRocket.this.wagon != null) {
                RocketryRocket.this.wagon.forgetRocket();
                RocketryRocket.this.wagon = null;
            }
            RocketryRocket.this.collide(false);
            RocketryRocket.this.drawing(false);
            RocketryRocket.this.postDestroy();
        }

        public boolean tick() {
            if (RocketryRocket.this.Corpse()) {
                if (access$306(RocketryRocket.this) > 0L) return true;
                this.disappear();
                return false;
            }
            long l = Time.current();
            float f = (l - RocketryRocket.this.timeOfStartMS) * 0.0010F;
            int i = RocketryRocket.this.chooseTrajectorySegment(f);
            if (RocketryRocket.this.sta != i) {
                if (i == -2) {
                    this.disappear();
                    return false;
                }
                RocketryRocket.this.advanceState(RocketryRocket.this.sta, i);
            }
            RocketryRocket.this.computeCurLoc(RocketryRocket.this.sta, f, RocketryRocket.tmpL);
            if (RocketryRocket.this.fallMode > 0) {
                float f_0_ = RocketryRocket.tmpL.getOrient().getYaw();
                float f_1_ = RocketryRocket.tmpL.getOrient().getPitch();
                float f_2_ = RocketryRocket.tmpL.getOrient().getRoll();
                float f_3_ = 0.0F;
//                if (RocketryRocket.this.fallMode == 0) f_3_ = 0.0F;
                /*else*/ if (RocketryRocket.this.fallMode == 1) {
                    f_3_ = f * RocketryRocket.this.fallVal;
                    if (f_3_ >= 360.0F) f_3_ %= 360.0F;
                    else if (f_3_ < -360.0F) {
                        f_3_ = -f_3_;
                        f_3_ %= 360.0F;
                        f_3_ = -f_3_;
                    }
                } else if (f <= 0.0F) f_3_ = 0.0F;
                else if (f >= RocketryRocket.this.fallVal) f_3_ = 180.0F;
                else {
                    float f_4_ = Math.abs(RocketryRocket.this.fallVal);
                    f_3_ = Geom.sinDeg(f / f_4_ * 180.0F);
                    f_3_ = (float) (f_3_ * (RocketryRocket.this.fallVal < 0.0F ? 180.0 : -180.0));
                }
                RocketryRocket.tmpL.getOrient().setYPR(f_0_, f_1_, f_2_ + f_3_);
            }
            boolean bool = false;
            if ((RocketryRocket.this.dmg & 0x20) != 0 && access$306(RocketryRocket.this) <= 0L) bool = true;
            double d = Engine.land().HQ_Air(RocketryRocket.tmpL.getPoint().x, RocketryRocket.tmpL.getPoint().y);
            if (RocketryRocket.tmpL.getPoint().z <= d) {
                RocketryRocket.tmpL.getPoint().z = d;
                RocketryRocket.this.pos.setAbs(RocketryRocket.tmpL);
                bool = true;
            } else RocketryRocket.this.pos.setAbs(RocketryRocket.tmpL);
            if (bool) {
                RocketryRocket rocketryrocket = RocketryRocket.this;
                char c = 'X';
                if (RocketryRocket.this.ramp != null) {
                    /* empty */
                }
                int i_5_ = RocketryGeneric.RndI(0, 65535);
                if (Engine.cur != null) {
                    /* empty */
                }
                rocketryrocket.handleCommand(c, i_5_, Engine.actorLand());
            }
            return true;
        }
    }

    public class MyMsgAction extends MsgAction {
        Point3d posi;

        public void doAction(Object object) {
            /* empty */
        }

        public MyMsgAction(double d, Object object, Point3d point3d) {
            super(d, object);
            this.posi = new Point3d(point3d);
        }
    }

    // TODO: Changed by |ZUTI|: made public
    public final boolean Corpse() {
        return (this.dmg & 0x40) != 0;
    }

    // TODO: Changed by |ZUTI|: made public
    public boolean isDamaged() {
        return this.dmg != 0;
    }

    boolean isOnRamp() {
        return !this.ramp.prop.air && this.sta <= 0;
    }

    void silentDeath() {
        if (this.wagon != null) {
            this.wagon.forgetRocket();
            this.wagon = null;
        }
        this.destroy();
    }

    void forgetWagon() {
        this.wagon = null;
    }

    public void sendRocketStateChange(char c, Actor actor) {
        this.ramp.sendRocketStateChange(this, c, actor);
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5 + f / Time.tickLenFs());
        return l < 1L ? 1L : l;
    }

    public void msgCollisionRequest(Actor actor, boolean[] bools) {
        if (this.Corpse()) bools[0] = false;
        else if (actor == this.ramp) bools[0] = false;
    }

    public void msgCollision(Actor actor, String string, String string_6_) {
        if (!this.Corpse() && Actor.isValid(actor) && string != null && (actor.net == null || !actor.net.isMirror()) && (!(actor instanceof Aircraft) || !string.startsWith("Wing"))) if (string.startsWith("WingLIn")) this.sendRocketStateChange('l', actor);
        else if (string.startsWith("WingRIn")) this.sendRocketStateChange('r', actor);
        else if (string.startsWith("Engine1")) this.sendRocketStateChange('e', actor);
        else this.sendRocketStateChange('x', actor);
    }

    public void msgShot(Shot shot) {
        shot.bodyMaterial = 2;
        if (!this.Corpse() && !(shot.power <= 0.0F) && shot.chunkName != null && !shot.chunkName.equals("") && !shot.chunkName.equals("Body")) {
            float f = 0.0F;
            float f_7_ = 0.0F;
            char c = ' ';
            if (shot.chunkName.equals("CF_D0")) {
                c = 'x';
                f = this.ramp.prop.DMG_WARHEAD_MM;
                f_7_ = this.ramp.prop.DMG_WARHEAD_PROB;
            } else if (shot.chunkName.equals("Tail1_D0")) {
                c = 'f';
                f = this.ramp.prop.DMG_FUEL_MM;
                f_7_ = this.ramp.prop.DMG_FUEL_PROB;
            } else if (shot.chunkName.equals("Engine1_D0")) {
                c = 'e';
                f = this.ramp.prop.DMG_ENGINE_MM;
                f_7_ = this.ramp.prop.DMG_ENGINE_PROB;
            } else if (shot.chunkName.equals("WingLIn_D0")) {
                c = 'l';
                f = this.ramp.prop.DMG_WING_MM;
                f_7_ = this.ramp.prop.DMG_WING_PROB;
            } else if (shot.chunkName.equals("WingRIn_D0")) {
                c = 'r';
                f = this.ramp.prop.DMG_WING_MM;
                f_7_ = this.ramp.prop.DMG_WING_PROB;
            }
            if (!(f_7_ <= 0.0F) && Aircraft.isArmorPenetrated(f, shot)) {
                if (this.ramp != null) {
                    /* empty */
                }
                if (!(RocketryGeneric.RndF(0.0F, 1.0F) > f_7_)) this.sendRocketStateChange(c, shot.initiator);
            }
        }
    }

    public void msgExplosion(Explosion explosion) {
        if (!this.Corpse() && (!this.ramp.prop.air || this.sta != -1)) {
            float f = explosion.receivedTNT_1meter(this);
            if (!(f <= 0.0F)) if (f >= this.ramp.prop.DMG_WARHEAD_TNT) this.sendRocketStateChange('x', explosion.initiator);
            else if (f >= this.ramp.prop.DMG_WING_TNT) {
                if (this.ramp != null) {
                    /* empty */
                }
                char c = RocketryGeneric.RndF(0.0F, 1.0F) > 0.5F ? 'l' : 'r';
                if (this.isCommandApplicable(c)) this.sendRocketStateChange(c, explosion.initiator);
            }
        }
    }

    public int HitbyMask() {
        return -1;
    }

    public int chooseBulletType(BulletProperties[] bulletpropertieses) {
        if (this.Corpse()) return -1;
        if (bulletpropertieses.length == 1) return 0;
        if (bulletpropertieses.length <= 0) return -1;
        if (bulletpropertieses[0].power <= 0.0F) return 1;
        if (bulletpropertieses[0].powerType == 1) return 0;
        if (bulletpropertieses[1].powerType == 1) return 1;
        if (bulletpropertieses[0].cumulativePower > 0.0F) return 1;
        if (bulletpropertieses[0].powerType == 2) return 1;
        return 0;
    }

    public int chooseShotpoint(BulletProperties bulletproperties) {
        if (this.Corpse()) return -1;
        return 0;
    }

    public boolean getShotpointOffset(int i, Point3d point3d) {
        if (this.Corpse()) return false;
        if (i != 0) return false;
        if (point3d != null) point3d.set(0.0, 0.0, 0.0);
        return true;
    }

    private boolean isCommandApplicable(char c) {
        if (this.Corpse()) return false;
        switch (c) {
            case 'X':
            case 'x':
                return (this.dmg & 0x40) == 0;
            case 'E':
            case 'e':
                return (this.dmg & 0x10) == 0;
            case 'F':
            case 'f':
                return (this.dmg & 0x20) == 0;
            case 'L':
            case 'l':
                return (this.dmg & 0x1) == 0;
            case 'R':
            case 'r':
                return (this.dmg & 0x2) == 0;
            case 'A':
            case 'a':
                return (this.dmg & 0x4) == 0;
            case 'B':
            case 'b':
                return (this.dmg & 0x8) == 0;
            default:
                return false;
        }
    }

    char handleCommand(char c, int i, Actor actor) {
        if (!this.isCommandApplicable(c)) return '\0';
        if (this.isOnRamp()) {
            c = 'X';
            if (this.wagon != null) {
                this.wagon.silentDeath();
                this.wagon = null;
            }
        }
        switch (c) {
            case 'X':
            case 120:
                this.dmg |= 0x40;
                this.hierMesh().chunkVisible("CF_D0", false);
                this.hierMesh().chunkVisible("CF_D1", true);
                tmpL.set(this.pos.getAbs());
                com.maddox.il2.objects.effects.Explosions.HydrogenBalloonExplosion(tmpL, null);
                new RocketryRocket.MyMsgAction(0.0D, actor, this.pos.getAbsPoint()) {

                    public void doAction(java.lang.Object obj) {
                        com.maddox.il2.ai.MsgExplosion.send(null, "Body", this.posi, (com.maddox.il2.engine.Actor) obj, 0.0F, RocketryRocket.this.ramp.prop.MASS_TNT, 0, RocketryRocket.this.ramp.prop.EXPLOSION_RADIUS);
                    }

                };
                this.ramp.forgetRocket(this);
                this.collide(false);
                this.drawing(false);
                this.countdownTicks = RocketryRocket.SecsToTicks(0.5F);
                this.breakSounds();
                return c;
            case 'E':
            case 'e':
                this.dmg |= 0x10;
                Eff3DActor.finish(this.eng_trail);
                this.eng_trail = null;
                this.hierMesh().chunkVisible("Engine1_D0", false);
                this.hierMesh().chunkVisible("Engine1_D1", true);
                Eff3DActor.New(this, this.findHook("_Engine1Burn"), null, 1.0F, "3DO/Effects/Aircraft/FireSPD.eff", -1.0F);
                Eff3DActor.New(this, this.findHook("_Engine1Burn"), null, 1.0F, "3DO/Effects/Aircraft/BlackHeavySPD.eff", -1.0F);
                Eff3DActor.New(this, this.findHook("_Engine1Burn"), null, 1.0F, "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", -1.0F);
                this.breakSounds();
                if (this.fallMode < 0) this.startFalling(i, 0, 0.0F);
                return c;
            case 'F':
            case 'f':
                this.dmg |= 0x20;
                this.hierMesh().chunkVisible("Tail1_D0", false);
                this.hierMesh().chunkVisible("Tail1_D1", true);
                Eff3DActor.New(this, this.findHook("_Tank1Burn"), null, 1.0F, "3DO/Effects/Aircraft/FireSPD.eff", -1.0F);
                Eff3DActor.New(this, this.findHook("_Tank1Burn"), null, 1.0F, "3DO/Effects/Aircraft/BlackHeavySPD.eff", -1.0F);
                Eff3DActor.New(this, this.findHook("_Tank1Burn"), null, 1.0F, "3DO/Effects/Aircraft/BlackHeavyTSPD.eff", -1.0F);
                this.countdownTicks = SecsToTicks(rndSeed.nextFloat(5.0F, 40.0F));
                return c;
            case 'L':
            case 'l':
                this.dmg |= 0x1;
                this.hierMesh().chunkVisible("WingLIn_D0", false);
                this.hierMesh().chunkVisible("WingLIn_D1", true);
                if (this.fallMode < 0) this.startFalling(i, 1, 90.0F);
                return c;
            case 'R':
            case 'r':
                this.dmg |= 0x2;
                this.hierMesh().chunkVisible("WingRIn_D0", false);
                this.hierMesh().chunkVisible("WingRIn_D1", true);
                if (this.fallMode < 0) this.startFalling(i, 1, -90.0F);
                return c;
            case 'A':
            case 'a':
                this.dmg |= 0x4;
                if (this.fallMode < 0) this.startFalling(i, 2, 5.0F);
                return c;
            case 'B':
            case 'b':
                this.dmg |= 0x8;
                if (this.fallMode < 0) this.startFalling(i, 2, -5.0F);
                return c;
            default:
                return '\0';
        }
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    private int chooseTrajectorySegment(float f) {
        int i;
        for (i = 0; i < this.traj.length; i++)
            if (f < this.traj[i].t0) return i - 1;
        if (f < this.traj[i - 1].t0 + this.traj[i - 1].t) return i - 1;
        return -2;
    }

    private void computeCurLoc(int i, float f, Loc loc) {
        if (i < 0) {
            loc.getPoint().set(this.traj[0].pos0);
            if (this.traj[0].v0.lengthSquared() > 0.0) loc.getOrient().setAT0(this.traj[0].v0);
            else loc.getOrient().setAT0(this.traj[0].a);
        } else {
            f -= this.traj[i].t0;
            tmpV3d0.scale(this.traj[i].v0, f);
            tmpV3d1.scale(this.traj[i].a, f * f * 0.5);
            tmpV3d0.add(tmpV3d1);
            tmpV3d0.add(this.traj[i].pos0);
            loc.getPoint().set(tmpV3d0);
            tmpV3d0.scale(this.traj[i].a, f);
            tmpV3d0.add(this.traj[i].v0);
            if (tmpV3d0.lengthSquared() <= 0.0) tmpV3d0.set(this.traj[i].a);
            loc.getOrient().setAT0(tmpV3d0);
        }
    }

    private void computeCurPhys(int i, float f, Point3d point3d, Vector3d vector3d) {
        if (i < 0) {
            point3d.set(this.traj[0].pos0);
            if (this.traj[0].v0.lengthSquared() > 0.0) vector3d.set(this.traj[0].v0);
            else {
                vector3d.set(this.traj[0].a);
                vector3d.normalize();
                vector3d.scale(0.0010);
            }
        } else {
            f -= this.traj[i].t0;
            tmpV3d0.scale(this.traj[i].v0, f);
            tmpV3d1.scale(this.traj[i].a, f * f * 0.5);
            tmpV3d0.add(tmpV3d1);
            tmpV3d0.add(this.traj[i].pos0);
            point3d.set(tmpV3d0);
            tmpV3d0.scale(this.traj[i].a, f);
            tmpV3d0.add(this.traj[i].v0);
            if (tmpV3d0.lengthSquared() <= 0.0) {
                vector3d.set(this.traj[i].a);
                vector3d.normalize();
                vector3d.scale(0.0010);
            } else vector3d.set(tmpV3d0);
        }
    }

    private void startFalling(int i, int i_9_, float f) {
        long l = Time.current();
        float f_10_ = (l - this.timeOfStartMS) * 0.0010F;
        int i_11_ = this.chooseTrajectorySegment(f_10_);
        if (i_11_ == -2) {
            this.ramp.forgetRocket(this);
            this.silentDeath();
        } else {
            this.computeCurPhys(i_11_, f_10_, tmpP, tmpV);
            this.traj = this.ramp._computeFallTrajectory_(i, tmpP, tmpV);
            this.fallMode = i_9_;
            this.fallVal = f;
            this.timeOfStartMS = l;
            this.sta = this.chooseTrajectorySegment(0.0F);
        }
    }

    private void advanceState(int i, int i_12_) {
        this.sta = i;
        while (this.sta < i_12_) {
            this.sta++;
            if (this.ramp.prop.air) switch (this.sta) {
                case 0:
                    this.collide(true);
                    this.drawing(true);
                    this.eng_trail = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Tracers/ImpulseRocket/rocket.eff", -1.0F);
                    this.newSound(this.ramp.prop.soundName, true);
                    break;
                case 1:
                    Eff3DActor.finish(this.eng_trail);
                    this.eng_trail = null;
                    this.breakSounds();
                    break;
            }
            else switch (this.sta) {
                case 0:
                    this.eng_trail = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Tracers/ImpulseRocket/rocket.eff", -1.0F);
                    this.newSound(this.ramp.prop.soundName, true);
                    if (this.wagon != null) {
                        this.wagon.forgetRocket();
                        this.wagon = null;
                    }
                    break;
                case 1:
                    break;
                case 4:
                    Eff3DActor.finish(this.eng_trail);
                    this.eng_trail = null;
                    this.breakSounds();
                    break;
            }
        }
        this.sta = i_12_;
    }

    void changeLaunchTimeIfCan(long l) {
        if (!this.Corpse()) if (this.sta == -1) {
            if (Time.current() < l) this.timeOfStartMS = l;
            else this.timeOfStartMS = Time.current();
            if (this.wagon != null) this.wagon.timeOfStartMS = this.timeOfStartMS;
        }
    }

    public RocketryRocket(RocketryGeneric rocketrygeneric, String string, int i, int i_13_, long l, long l_14_, RocketryGeneric.TrajSeg[] trajsegs) {
        super(string);
        this.ramp = rocketrygeneric;
        this.idR = i;
        this.randseed = i_13_;
        this.traj = trajsegs;
        this.timeOfStartMS = l;
        this.dmg = 0;
        this.setArmy(this.ramp.getArmy());
        this.sta = -1;
        float f = (l_14_ - this.timeOfStartMS) * 0.0010F;
        int i_15_ = this.chooseTrajectorySegment(f);
        if (i_15_ == -2 || this.ramp.prop.air && i_15_ >= 2 || !this.ramp.prop.air && i_15_ >= 5) {
            this.dmg = 64;
            this.collide(false);
            this.drawing(false);
        } else {
            this.wagon = null;
            if (!this.ramp.prop.air) {
                RocketryGeneric.TrajSeg[] trajsegs_16_ = this.ramp._computeWagonTrajectory_(this.randseed);
                if (trajsegs_16_ == null) {
                    this.dmg = 64;
                    this.collide(false);
                    this.drawing(false);
                    return;
                }
                this.wagon = new RocketryWagon(this, this.ramp.meshNames.wagon, this.timeOfStartMS, l_14_, trajsegs_16_);
                if (this.wagon == null || !this.wagon.isDrawing()) {
                    this.dmg = 64;
                    this.collide(false);
                    this.drawing(false);
                    return;
                }
            }
            if (this.ramp.prop.air) {
                this.collide(false);
                this.drawing(false);
            } else {
                this.collide(true);
                this.drawing(true);
            }
            this.setName(this.ramp.name() + "_" + i);
            if (this.sta != i_15_) this.advanceState(this.sta, i_15_);
            this.computeCurLoc(this.sta, f, tmpL);
            this.pos.setAbs(tmpL);
            this.pos.reset();
            this.dreamFire(true);
            if (!this.interpEnd("move")) this.interpPut(new Move(), "move", l_14_, null);
        }
    }

    static long access$306(RocketryRocket rocketryrocket) {
        return --rocketryrocket.countdownTicks;
    }
}
