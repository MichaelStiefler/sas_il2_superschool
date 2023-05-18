/* 4.10.1 class + TRACK IR mod */
package com.maddox.il2.engine.hotkey;

import java.io.BufferedReader;
import java.io.PrintWriter;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.HookRender;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.VisibilityChecker;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.MOSQUITO;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.effects.SpritesFog;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.Keyboard;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Time;
import com.maddox.rts.TrackIR;

public class HookPilot extends HookRender {
    private static final float[] af           = new float[7];
    private boolean              sight        = false;
    private boolean              usezoom;
    private boolean              usezoomc6;
    private boolean              mTypeBomber;
    private float                gfactor;
    private float                tfactor;
    private float                boh;
    private float                stepAzimut;
    private float                stepTangage;
    private float                maxAzimut;
    private float                maxTangage;
    private float                minTangage;
    // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
//    private float                Roll         = 0.0F;
//    private float                _Roll        = 0.0F;
    private float                maxRoll      = 45.0F;
    private float                minRoll      = -45.0F;
    // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---
    private float                Azimut;
    private float                Tangage;
    private float                _Azimut;
    private float                _Tangage;
    private long                 rprevTime;
    private float                Px;
    private float                Py;
    private float                Pz;
    private float                azimPadlock;
    private float                tangPadlock;
    private float                timeKoof;
    // TODO: Storebror: Implement Aircraft Control Surfaces and Pilot View Replication
//    private Orient               o;
    public Orient                o;
    // ---
    private Orient               op;
    private Loc                  le;
    private Point3d              pe;
    private Point3d              pEnemyAbs;
    private Vector3d             Ve;
    private Point3d              pAbs;
    private Orient               oAbs;
    private Actor                target;
    private Actor                target2;
    private Actor                enemy;
    private long                 stamp;
    private long                 prevTime;
    private long                 roolTime;
    private boolean              bUse;
    private boolean              bPadlock;
    private long                 tPadlockEnd;
    private long                 tPadlockEndLen;
    private boolean              bPadlockEnd;
    private boolean              bForward;
    private boolean              bAim;
    private boolean              bUp;
    private boolean              bVisibleEnemy;
    private boolean              bSimpleUse;
    private Point3d              pCenter;
    private Point3d              pAim;
    private Point3d              pUp;
    private static RangeRandom   rnd          = new RangeRandom();
    private static Point3d       P            = new Point3d();
    private static Vector3d      tmpA         = new Vector3d();
    private static Vector3d      tmpB         = new Vector3d();
    private static Vector3d      headShift    = new Vector3d();
    private static Vector3d      counterForce = new Vector3d();
    private static long          oldHeadTime  = 0L;
    private static double        oldWx        = 0.0;
    private static double        oldWy        = 0.0;
    private boolean              bUseMouse;
    private long                 timeViewSet;
    public static HookPilot      current;
    private static Orient        oTmp         = new Orient();
    private static Orient        oTmp2        = new Orient();
    private static float         shakeLVL;

    public void resetGame() {
        this.enemy = null;
        this.bUp = false;
    }

    private Point3d pCamera() {
        if (this.bAim)
            return this.pAim;
        if (this.bUp)
            return this.pUp;
        return this.pCenter;
    }

    public boolean isAim() {
        return this.bAim;
    }

    public void doAim(boolean bool) {
        this.sight = bool;
        if (this.bAim != bool)
            this.bAim = bool;
    }

    public void setSimpleUse(boolean bool) {
        this.bSimpleUse = bool;
    }

    public void setSimpleAimOrient(float f, float f_0_, float f_1_) {
        this.o.set(f, f_0_, f_1_);
        this.le.set(this.pCamera(), this.o);
    }

    public void setCenter(Point3d point3d) {
        this.pCenter.set(point3d);
        if (World.getPlayerAircraft() instanceof TypeBomber || World.getPlayerAircraft() instanceof MOSQUITO)
            this.mTypeBomber = true;
        else
            this.mTypeBomber = false;
    }

    public void setAim(Point3d point3d) {
        this.pAim.set(this.pCenter);
        if (point3d != null)
            this.pAim.set(point3d);
        this.setUp(point3d);
    }

    public void setUp(Point3d point3d) {
        this.pUp.set(this.pCenter);
        if (point3d != null)
            this.pUp.set(point3d);
    }

    public void setSteps(float f, float f_2_) {
        this.stepAzimut = f;
        this.stepTangage = f_2_;
    }

    public void setForward(boolean bool) {
        this.bForward = bool;
    }

    public void endPadlock() {
        this.bPadlockEnd = true;
    }

    private void _reset() {
        if (!AircraftHotKeys.bFirstHotCmd) {
            this._Azimut = this.Azimut = 0.0F;
            this._Tangage = this.Tangage = 0.0F;
            this.o.set(0.0F, 0.0F, 0.0F);
            this.le.set(this.pCamera(), this.o);
        }
        this.Px = this.Py = this.Pz = 0.0F;
        this.azimPadlock = 0.0F;
        this.tangPadlock = 0.0F;
        this.timeKoof = 1.0F;
        this.prevTime = -1L;
        this.roolTime = -1L;
        this.enemy = null;
        this.bPadlock = false;
        this.tPadlockEnd = -1L;
        this.tPadlockEndLen = 0L;
        this.bPadlockEnd = false;
        this.bForward = false;
        if (!Main3D.cur3D().isDemoPlaying())
            new MsgAction(64, 0.0) {
                public void doAction() {
                    HotKeyCmd.exec("misc", "target_");
                }
            };
        this.timeViewSet = -2000L;
        headShift.set(0.0, 0.0, 0.0);
        counterForce.set(0.0, 0.0, 0.0);
        oldHeadTime = -1L;
        oldWx = 0.0;
        oldWy = 0.0;
    }

    public void saveRecordedStates(PrintWriter printwriter) throws Exception {
        printwriter.println(this.Azimut);
        printwriter.println(this._Azimut);
        printwriter.println(this.Tangage);
        printwriter.println(this._Tangage);
        printwriter.println(this.o.azimut());
        printwriter.println(this.o.tangage());
    }

    public void loadRecordedStates(BufferedReader bufferedreader) throws Exception {
        this.Azimut = Float.parseFloat(bufferedreader.readLine());
        this._Azimut = Float.parseFloat(bufferedreader.readLine());
        this.Tangage = Float.parseFloat(bufferedreader.readLine());
        this._Tangage = Float.parseFloat(bufferedreader.readLine());
        this.o.set(Float.parseFloat(bufferedreader.readLine()), Float.parseFloat(bufferedreader.readLine()), 0.0F);
        this.le.set(this.pCamera(), this.o);
    }

    public void reset() {
        this.stamp = -1L;
        this._reset();
    }

    private void setTimeKoof() {
        long l = Time.current();
        if (this.prevTime == -1L)
            this.timeKoof = 1.0F;
        else
            this.timeKoof = (l - this.prevTime) / 30.0F;
        this.prevTime = l;
    }

    private void headRoll(Aircraft aircraft) {
        if (Keyboard.adapter().isPressed(106))
            this.M_showMenu6DOF();
        long l = this.roolTime - this.stamp;
        if (l < 0L || l >= 50L) {
            this.roolTime = this.stamp;
            // TODO: Modified by |ZUTI|: figure out AI shake level!
            // --------------------------------------------------------
            try {
                shakeLVL = ((RealFlightModel) aircraft.FM).shakeLevel;
            } catch (Exception ex) {
                shakeLVL = 0.0F;
            }
            // --------------------------------------------------------
            TrackIR.adapter().getAngles(af);
//              DecimalFormat twoDigits = new DecimalFormat("#.##");
//              HUD.training("" + twoDigits.format(af[0])
//                        + "-" + twoDigits.format(af[1])
//                        + "-" + twoDigits.format(af[2])
//                        + "-" + twoDigits.format(af[3])
//                        + "-" + twoDigits.format(af[4])
//                        + "-" + twoDigits.format(af[5])
//                                );
//              for (int i=3; i<6; i++)
//                  af[i]*=360F;
            float f;
            float f_4_;
            float f_5_;
            if (!this.sight) {
                f_4_ = (!this.mTypeBomber ? af[3] * 11.0F * 0.0090F / 180.0F : af[3] * 19.0F * 0.0090F / 180.0F);
                if (af[4] < 0.0F)
                    f_5_ = af[4] * 12.0F * 0.0090F / 180.0F;
                else
                    f_5_ = af[4] * 7.0F * 0.0090F / 180.0F;
                if (af[5] < 0.0F) {
                    if (af[5] < -90.0F) {
                        f = 0.24299999F;
                        if (this.usezoom) {
                            if (!this.usezoomc6) {
                                if (Main3D.FOVX != 30.0F && !(af[0] < -120.0F) && !(af[0] > 120.0F)) {
                                    float f_6_ = (70.0F + 40.0F * (af[5] + 90.0F) / 90.0F);
                                    if (f_6_ <= 30.0F)
                                        f_6_ = 29.99999F;
                                    CmdEnv.top().exec("fov " + f_6_);
                                }
                            } else if (Main3D.FOVX != 30.0F) {
                                if (!(af[0] < -120.0F) && !(af[0] > 120.0F)) {
                                    float f_7_ = (70.0F + 40.0F * (af[5] + 90.0F) / 90.0F);
                                    if (f_7_ <= 30.0F)
                                        f_7_ = 29.99999F;
                                    CmdEnv.top().exec("fov " + f_7_);
                                } else {
                                    float f_8_ = (70.0F + 20.0F * (-af[5] - 90.0F) / 90.0F);
                                    if (f_8_ >= 90.0F)
                                        f_8_ = 89.99999F;
                                    CmdEnv.top().exec("fov " + f_8_);
                                }
                            }
                        }
                    } else
                        f = -(af[5] * 27.0F * 0.0090F) / 90.0F;
                } else if (af[5] > 90.0F) {
                    f = -0.053999998F;
                    if (Main3D.FOVX != 90.0F && this.usezoom) {
                        float f_9_ = 70.0F + 20.0F * (af[5] - 90.0F) / 90.0F;
                        if (f_9_ >= 90.0F)
                            f_9_ = 89.99999F;
                        CmdEnv.top().exec("fov " + f_9_);
                    }
                } else
                    f = -(af[5] * 6.0F * 0.0090F) / 90.0F;
            } else
                f = f_4_ = f_5_ = 0.0F;
            if (World.cur().diffCur.Head_Shake) {
                long l_10_ = Time.current();
                if (oldHeadTime == -1L) {
                    oldHeadTime = Time.current();
                    oldWx = aircraft.FM.getW().x;
                    oldWy = aircraft.FM.getW().y;
                }
                long l_11_ = l_10_ - oldHeadTime;
                oldHeadTime = l_10_;
                if (l_11_ > 200L)
                    l_11_ = 200L;
                double d = 0.0030 * l_11_;
                double d_12_ = aircraft.FM.getW().x - oldWx;
                double d_13_ = aircraft.FM.getW().y - oldWy;
                oldWx = aircraft.FM.getW().x;
                if (d < 0.0010)
                    d_12_ = 0.0;
                else
                    d_12_ /= d;
                oldWy = aircraft.FM.getW().y;
                if (d < 0.0010)
                    d_13_ = 0.0;
                else
                    d_13_ /= d;
                if (aircraft.FM.Gears.onGround()) {
                    tmpA.set(0.0, 0.0, 0.0);
                    headShift.scale(1.0 - d);
                    tmpA.scale(d);
                    headShift.add(tmpA);
                    f_4_ += (float) headShift.y * 0.0090F;
                    f += ((float) (headShift.x + 0.03F * shakeLVL * (0.5F - rnd.nextFloat())) * 0.0090F);
                    f_5_ += ((float) (headShift.z + 1.2F * shakeLVL * (0.5F - rnd.nextFloat())) * 0.0090F);
                } else {
                    tmpB.set(0.0, 0.0, 0.0);
                    tmpA.set(aircraft.FM.getAccel());
                    aircraft.FM.Or.transformInv(tmpA);
                    tmpA.scale(-0.6);
                    if (tmpA.z > 0.0)
                        tmpA.z *= 0.8;
                    tmpB.add(tmpA);
                    counterForce.scale(1.0 - 0.2 * d);
                    tmpA.scale(0.2 * d);
                    counterForce.add(tmpA);
                    tmpB.sub(counterForce);
                    counterForce.scale(1.0 - 0.05 * d);
                    if (counterForce.z > 0.0)
                        counterForce.z *= 1.0 - 0.08 * d;
                    tmpB.scale(0.08);
                    tmpA.set(-0.7 * d_13_, d_12_, 0.0);
                    tmpA.add(tmpB);
                    headShift.scale(1.0 - d);
                    tmpA.scale(d);
                    headShift.add(tmpA);
                    f_4_ += (float) headShift.y * 0.0090F * this.gfactor;
                    f += ((float) (headShift.x + 0.3F * shakeLVL * (0.5F - rnd.nextFloat())) * 0.0090F * this.gfactor);
                    f_5_ += ((float) (headShift.z + 0.4F * shakeLVL * (0.5F - rnd.nextFloat())) * 0.0090F * this.gfactor);
                }
            } else {
                f_4_ += 0.0F;
                f += 0.0F;
                f_5_ += 0.0F;
            }
            if (World.cur().diffCur.Wind_N_Turbulence) {
                float f_14_ = SpritesFog.dynamicFogAlpha;
                double d = aircraft.pos.getAbsPoint().z;
                if (f_14_ > 0.01F && d > 300.0 && d < 2500.0) {
                    float f_15_ = aircraft.FM.getSpeed();
                    if (f_15_ > 138.8889F)
                        f_15_ = 138.8889F;
                    f_15_ -= 55.55556F;
                    if (f_15_ < 0.0F)
                        f_15_ = 0.0F;
                    f_15_ /= 83.33334F;
                    f_4_ += (f_15_ * 0.05F * f_14_ * (0.5F - rnd.nextFloat()) * 0.0090F * this.tfactor);
                    f_5_ += (f_15_ * 0.3F * f_14_ * (0.5F - rnd.nextFloat()) * 0.0090F * this.tfactor);
                }
            }
            P.set(this.Px += f - this.Px, this.Py += f_4_ - this.Py, this.Pz += f_5_ - this.Pz);
            oTmp.set((float) P.y, (float) P.z, (float) P.z);
            oTmp.increment(0.31F * rnd.nextFloat(-shakeLVL, shakeLVL), 0.31F * rnd.nextFloat(-shakeLVL, shakeLVL), 0.54F * rnd.nextFloat(-shakeLVL, shakeLVL));
        }
    }

    public void setMinMax(float maxAzimut, float minTangage, float maxTangage) {
        this.maxAzimut = 150.0F;
        this.minTangage = minTangage;
        this.maxTangage = 180.0F;
    }

    private void M_showMenu6DOF() {
        if (Keyboard.adapter().isPressed(106)) {
            TextScr.setFont("arial");
            if (this.boh >= 0.0F) {
                TextScr.setColor(new Color4f(this.boh, 0.0F, 0.0F, 1.0F));
                if (this.boh < 0.99F)
                    this.boh += 0.01F;
                else
                    this.boh = -0.98F;
            } else {
                TextScr.setColor(new Color4f(-this.boh, 0.0F, 0.0F, 1.0F));
                if (this.boh > -0.99F)
                    this.boh += 0.01F;
                else
                    this.boh = 0.98F;
            }
            TextScr.output(75, 275, "6DOF");
            TextScr.setColor(new Color4f(1.0F, 0.0F, 0.0F, 1.0F));
            TextScr.output(75, 225, "KP 1. Inverse Zoom on Check 6");
            TextScr.output(75, 250, "KP 0. Zoom View");
            TextScr.output(75, 200, "KP 2. Increase   G Force Factor");
            TextScr.output(75, 175, "KP 3. Decrease G Force Factor");
            TextScr.output(75, 150, "KP 4. Increase   Turbulence Factor");
            TextScr.output(75, 125, "KP 5. Decrease Turbulence Factor");
            if (this.boh >= 0.0F)
                TextScr.setColor(new Color4f(1.0F - this.boh, 1.0F - this.boh, 1.0F, 1.0F));
            else
                TextScr.setColor(new Color4f(1.0F + this.boh, 1.0F + this.boh, 1.0F, 1.0F));
            TextScr.output(380, 250, this.usezoom ? "On" : "Off");
            TextScr.output(380, 225, this.usezoomc6 ? "On" : "Off");
            TextScr.output(380, 187, this.gfactor + " ");
            TextScr.output(380, 137, this.tfactor + " ");
            if (Keyboard.adapter().isPressed(96)) {
                this.usezoom = !this.usezoom;
                if (!this.usezoom)
                    this.usezoomc6 = false;
                Keyboard.adapter().setRelease(Time.currentReal(), 96);
            } else if (Keyboard.adapter().isPressed(97)) {
                if (this.usezoom) {
                    this.usezoomc6 = !this.usezoomc6;
                    Keyboard.adapter().setRelease(Time.currentReal(), 97);
                }
            } else if (Keyboard.adapter().isPressed(98)) {
                if (this.gfactor < 9.0F) {
                    this.gfactor += 0.1F;
                    this.gfactor *= 10.0F;
                    this.gfactor = Math.round(this.gfactor);
                    this.gfactor /= 10.0F;
                }
            } else if (Keyboard.adapter().isPressed(99)) {
                if (this.gfactor > 1.0F) {
                    this.gfactor -= 0.1F;
                    this.gfactor *= 10.0F;
                    this.gfactor = Math.round(this.gfactor);
                    this.gfactor /= 10.0F;
                }
            } else if (Keyboard.adapter().isPressed(100)) {
                if (this.tfactor < 9.0F) {
                    this.tfactor += 0.1F;
                    this.tfactor *= 10.0F;
                    this.tfactor = Math.round(this.tfactor);
                    this.tfactor /= 10.0F;
                }
            } else if (Keyboard.adapter().isPressed(101) && this.tfactor > 1.0F) {
                this.tfactor -= 0.1F;
                this.tfactor *= 10.0F;
                this.tfactor = Math.round(this.tfactor);
                this.tfactor /= 10.0F;
            }
        }
    }

    public boolean isPadlock() {
        return this.bPadlock;
    }

    public Actor getEnemy() {
        return this.enemy;
    }

    public void stopPadlock() {
        if (this.bPadlock) {
            this.stamp = -1L;
            this._reset();
        }
    }

    public boolean startPadlock(Actor actor) {
        if (!this.bUse || this.bSimpleUse)
            return false;
        if (!Actor.isValid(actor)) {
            this.bPadlock = false;
            return false;
        }
        Aircraft aircraft = World.getPlayerAircraft();
        if (!Actor.isValid(aircraft)) {
            this.bPadlock = false;
            return false;
        }
        this.enemy = actor;
        this.Azimut = this._Azimut;
        this.Tangage = this._Tangage;
        this.bPadlock = true;
        this.bPadlockEnd = false;
        this.bVisibleEnemy = true;
        aircraft.pos.getAbs(this.pAbs, this.oAbs);
        Camera3D camera3d = (Camera3D) this.target2;
        camera3d.pos.getAbs(this.o);
        this.o.sub(this.oAbs);
        this.azimPadlock = this.o.getAzimut();
        this.tangPadlock = this.o.getTangage();
        this.azimPadlock = (this.azimPadlock + 3600.0F) % 360.0F;
        if (this.azimPadlock > 180.0F)
            this.azimPadlock -= 360.0F;
        this.stamp = -1L;
        if (!Main3D.cur3D().isDemoPlaying())
            new MsgAction(64, 0.0) {
                public void doAction() {
                    HotKeyCmd.exec("misc", "target_");
                }
            };
        return true;
    }

    public boolean isUp() {
        return this.bUp;
    }

    public void doUp(boolean bool) {
        if (this.bUp != bool)
            this.bUp = bool;
    }

    private float bvalue(float f, float f_19_, long l) {
        float f_20_ = HookView.koofSpeed * l / 30.0F;
        if (f == f_19_)
            return f;
        if (f > f_19_) {
            if (f < f_19_ + f_20_)
                return f;
            return f_19_ + f_20_;
        }
        if (f > f_19_ - f_20_)
            return f;
        return f_19_ - f_20_;
    }

    public boolean computeRenderPos(Actor actor, Loc loc, Loc loc_21_) {
        if (this.bUse) {
            if (this.bPadlock) {
                Aircraft aircraft = World.getPlayerAircraft();
                if (!Actor.isValid(aircraft)) {
                    this.reset();
                    loc_21_.add(this.le, loc);
                    return true;
                }
                long l = Time.current();
                if (l != this.stamp && this.enemy.pos != null && aircraft.pos != null) {
                    this.stamp = l;
                    this.setTimeKoof();
                    this.enemy.pos.getRender(this.pe);
                    this.pEnemyAbs.set(this.pe);
                    aircraft.pos.getRender(this.pAbs, this.oAbs);
                    this.Ve.sub(this.pe, this.pAbs);
                    this.o.setAT0(this.Ve);
                    this.headRoll(aircraft);
                    this.pe.add(this.pCamera(), P);
                    this.le.set(this.pe);
                    this.o.sub(this.oAbs);
                    this.padlockSet(this.o);
                    this.op.set(this.o);
                    this.op.add(this.oAbs);
                }
                loc_21_.add(this.le, loc);
                loc_21_.set(this.op);
                return true;
            }
            long l = Time.currentReal();
            if (l != this.rprevTime && !this.bSimpleUse) {
                long l_22_ = l - this.rprevTime;
                this.rprevTime = l;
                if (this._Azimut != this.Azimut || this._Tangage != this.Tangage) {
                    this.Azimut = this.bvalue(this._Azimut, this.Azimut, l_22_);
                    this.Tangage = this.bvalue(this._Tangage, this.Tangage, l_22_);
                    this.o.set(this.Azimut, this.Tangage, 0.0F);
                }
            }
            Aircraft aircraft = World.getPlayerAircraft();
            if (Actor.isValid(aircraft)) {
                long l_23_ = Time.current();
                if (l_23_ != this.stamp) {
                    this.stamp = l_23_;
                    this.headRoll(aircraft);
                }
            }
            this.pe.add(this.pCamera(), P);
            oTmp2.set(this.o);
            oTmp2.increment(oTmp);
            this.le.set(this.pe, oTmp2);
            loc_21_.add(this.le, loc);
        } else
            loc_21_.set(loc);
        return true;
    }

    public void computePos(Actor actor, Loc loc, Loc loc_24_) {
        if (this.bUse) {
            if (Time.isPaused() && !this.bPadlock) {
                if (World.cur().diffCur.Head_Shake) {
                    this.pe.add(this.pCamera(), P);
                    this.le.set(this.pe, this.o);
                } else
                    this.le.set(this.pCamera(), this.o);
                loc_24_.add(this.le, loc);
            } else {
                loc_24_.add(this.le, loc);
                if (this.bPadlock)
                    loc_24_.set(this.op);
            }
        } else
            loc_24_.set(loc);
    }

    private float avalue(float f, float f_25_) {
        if (f >= 0.0F) {
            if (f <= f_25_)
                return 0.0F;
            return f - f_25_;
        }
        if (f >= -f_25_)
            return 0.0F;
        return f + f_25_;
    }

    private float bvalue(float f, float f_26_) {
        float f_27_ = HookView.koofSpeed * 4.0F / 6.0F * this.timeKoof;
        if (f > f_26_) {
            if (f < f_26_ + f_27_)
                return f;
            return f_26_ + f_27_;
        }
        if (f > f_26_ - f_27_)
            return f;
        return f_26_ - f_27_;
    }

    private void padlockSet(Orient orient) {
        float f = orient.getAzimut();
        float f_28_ = orient.getTangage();
        if (this.bPadlockEnd || this.bForward) {
            f = f_28_ = 0.0F;
            this.tPadlockEnd = -1L;
        } else {
            Camera3D camera3d = (Camera3D) this.target2;
            float f_29_ = camera3d.FOV() * 0.3F;
            float f_30_ = f_29_ / camera3d.aspect();
            f = (f + 3600.0F) % 360.0F;
            if (f > 180.0F)
                f -= 360.0F;
            f = this.avalue(f, f_29_);
            f_28_ = this.avalue(f_28_, f_30_);
            boolean bool = false;
            if (f < -this.maxAzimut) {
                f = -this.maxAzimut;
                bool = true;
            }
            if (f > this.maxAzimut) {
                f = this.maxAzimut;
                bool = true;
            }
            if (f_28_ < this.minTangage) {
                f_28_ = this.minTangage;
                bool = true;
            }
            if (bool || !this.bVisibleEnemy || !Actor.isAlive(this.enemy)) {
                if (this.tPadlockEnd != -1L)
                    this.tPadlockEndLen += Time.current() - this.tPadlockEnd;
                this.tPadlockEnd = Time.current();
                if (this.tPadlockEndLen > 4000L) {
                    this.bPadlockEnd = true;
                    this.tPadlockEnd = -1L;
                    this.tPadlockEndLen = 0L;
                }
            } else {
                this.tPadlockEnd = -1L;
                this.tPadlockEndLen = 0L;
            }
        }
        f = this.bvalue(f, this.azimPadlock);
        f_28_ = this.bvalue(f_28_, this.tangPadlock);
        orient.set(f, f_28_, 0.0F);
        this.azimPadlock = f;
        this.tangPadlock = f_28_;
        if (this.bPadlockEnd && -1.0F < this.azimPadlock && this.azimPadlock < 1.0F && -1.0F < this.tangPadlock && this.tangPadlock < 1.0F) {
            this.stamp = -1L;
            this._reset();
        }
    }

    public void checkPadlockState() {
        if (this.bPadlock && Actor.isAlive(this.enemy)) {
            VisibilityChecker.checkLandObstacle = true;
            VisibilityChecker.checkCabinObstacle = true;
            VisibilityChecker.checkPlaneObstacle = true;
            VisibilityChecker.checkObjObstacle = true;
            this.bVisibleEnemy = VisibilityChecker.computeVisibility(null, this.enemy) > 0.0F;
        }
    }

    public void setTarget(Actor actor) {
        this.target = actor;
    }

    public void setTarget2(Actor actor) {
        this.target2 = actor;
    }

    public boolean use(boolean bool) {
        boolean bool_31_ = this.bUse;
        this.bUse = bool;
        if (Actor.isValid(this.target))
            this.target.pos.inValidate(true);
        if (Actor.isValid(this.target2))
            this.target2.pos.inValidate(true);
        return bool_31_;
    }

    public boolean useMouse(boolean bool) {
        boolean bool_32_ = this.bUseMouse;
        this.bUseMouse = bool;
        return bool_32_;
    }

    public void mouseMove(int i, int i_33_, int i_34_) {
        if (this.bUse && !this.bPadlock && !this.bSimpleUse) {
            if (this.bUseMouse && Time.real() > this.timeViewSet + 1000L) {
                float f = (this.o.azimut() + i * HookView.koofAzimut) % 360.0F;
                if (f > 180.0F)
                    f -= 360.0F;
                else if (f < -180.0F)
                    f += 360.0F;
                if (f < -this.maxAzimut) {
                    if (i <= 0)
                        f = -this.maxAzimut;
                    else
                        f = this.maxAzimut;
                } else if (f > this.maxAzimut) {
                    if (i >= 0)
                        f = this.maxAzimut;
                    else
                        f = -this.maxAzimut;
                }
                float f_35_ = ((this.o.tangage() + i_33_ * HookView.koofTangage) % 360.0F);
                if (f_35_ > 180.0F)
                    f_35_ -= 360.0F;
                else if (f_35_ < -180.0F)
                    f_35_ += 360.0F;
                if (f_35_ < this.minTangage) {
                    if (i_33_ <= 0)
                        f_35_ = this.minTangage;
                    else
                        f_35_ = this.maxTangage;
                } else if (f_35_ > this.maxTangage) {
                    if (i_33_ >= 0)
                        f_35_ = this.maxTangage;
                    else
                        f_35_ = this.minTangage;
                }
                this.o.set(f, f_35_, 0.0F);
                if (Actor.isValid(this.target))
                    this.target.pos.inValidate(true);
                if (Actor.isValid(this.target2))
                    this.target2.pos.inValidate(true);
                this.Azimut = this._Azimut;
                this.Tangage = this._Tangage;
            }
        }
    }

    public void viewSet(float yaw, float pitch) {
        if (!this.bUse || this.bPadlock || this.bSimpleUse)
            return;
        if (!this.bUseMouse)
            return;
        this.timeViewSet = Time.real();
        yaw %= 360.0F;
        if (yaw > 180.0F)
            yaw -= 360.0F;
        else if (yaw < -180.0F)
            yaw += 360.0F;
        pitch %= 360.0F;
        if (pitch > 180.0F)
            pitch -= 360.0F;
        else if (pitch < -180.0F)
            pitch += 360.0F;
        if (yaw < -this.maxAzimut)
            yaw = -this.maxAzimut;
        else if (yaw > this.maxAzimut)
            yaw = this.maxAzimut;
        if (pitch > this.maxTangage)
            pitch = this.maxTangage;
        else if (pitch < this.minTangage)
            pitch = this.minTangage;
        this._Azimut = this.Azimut = yaw;
        this._Tangage = this.Tangage = pitch;
        if (af[2] > 90.0)
            af[2] = 90.0F;
        else if (af[2] < -90.0F)
            af[2] = -90.0F;
        this.o.set(yaw, pitch, -af[2]);
        if (Actor.isValid(this.target))
            this.target.pos.inValidate(true);
        if (Actor.isValid(this.target2))
            this.target2.pos.inValidate(true);
    }

    // TODO: +++ 4.11+ TrackIR implementation by SAS~Storebror +++
    public void viewSet(float yaw, float pitch, float roll) {
        if (!this.bUse || this.bPadlock || this.bSimpleUse)
            return;
        if (!this.bUseMouse)
            return;
        this.timeViewSet = Time.real();
        yaw %= 360.0F;
        if (yaw > 180.0F)
            yaw -= 360.0F;
        else if (yaw < -180.0F)
            yaw += 360.0F;
        pitch %= 360.0F;
        if (pitch > 180.0F)
            pitch -= 360.0F;
        else if (pitch < -180.0F)
            pitch += 360.0F;
        roll %= 360.0F;
        if (roll > 180.0F)
            roll -= 360.0F;
        else if (roll < -180.0F)
            roll += 360.0F;
        if (yaw < -this.maxAzimut)
            yaw = -this.maxAzimut;
        else if (yaw > this.maxAzimut)
            yaw = this.maxAzimut;
        if (pitch > this.maxTangage)
            pitch = this.maxTangage;
        else if (pitch < this.minTangage)
            pitch = this.minTangage;
        if (roll > this.maxRoll)
            roll = this.maxRoll;
        else if (roll < this.minRoll)
            roll = this.minRoll;
        this._Azimut = this.Azimut = yaw;
        this._Tangage = this.Tangage = pitch;
//        this._Roll = (this.Roll = roll);
//        if (af[2] > 90.0)
//            af[2] = 90.0F;
//        else if (af[2] < -90.0F)
//            af[2] = -90.0F;
        this.o.set(yaw, pitch, roll);
        if (Actor.isValid(this.target))
            this.target.pos.inValidate(true);
        if (Actor.isValid(this.target2))
            this.target2.pos.inValidate(true);
    }
    // TODO: --- 4.11+ TrackIR implementation by SAS~Storebror ---

    public void snapSet(float f, float f_37_) {
        if (this.bUse && !this.bPadlock && !this.bSimpleUse) {
            this._Azimut = 45.0F * f;
            this._Tangage = 44.0F * f_37_;
            this.Azimut = this.o.azimut() % 360.0F;
            if (this.Azimut > 180.0F)
                this.Azimut -= 360.0F;
            else if (this.Azimut < -180.0F)
                this.Azimut += 360.0F;
            this.Tangage = this.o.tangage() % 360.0F;
            if (this.Tangage > 180.0F)
                this.Tangage -= 360.0F;
            else if (this.Tangage < -180.0F)
                this.Tangage += 360.0F;
            if (Actor.isValid(this.target))
                this.target.pos.inValidate(true);
            if (Actor.isValid(this.target2))
                this.target2.pos.inValidate(true);
        }
    }

    public void panSet(int i, int i_38_) {
        if (this.bUse && !this.bPadlock && !this.bSimpleUse) {
            if (i == 0 && i_38_ == 0) {
                this._Azimut = 0.0F;
                this._Tangage = 0.0F;
            }
            if (this._Azimut == -this.maxAzimut) {
                int i_39_ = (int) (this._Azimut / this.stepAzimut);
                if (-this._Azimut % this.stepAzimut > 0.01F * this.stepAzimut)
                    i_39_--;
                this._Azimut = i_39_ * this.stepAzimut;
            } else if (this._Azimut == this.maxAzimut) {
                int i_40_ = (int) (this._Azimut / this.stepAzimut);
                if (this._Azimut % this.stepAzimut > 0.01F * this.stepAzimut)
                    i_40_++;
                this._Azimut = i_40_ * this.stepAzimut;
            }
            this._Azimut = i * this.stepAzimut + this._Azimut;
            if (this._Azimut < -this.maxAzimut)
                this._Azimut = -this.maxAzimut;
            if (this._Azimut > this.maxAzimut)
                this._Azimut = this.maxAzimut;
            this._Tangage = i_38_ * this.stepTangage + this._Tangage;
            if (this._Tangage < this.minTangage)
                this._Tangage = this.minTangage;
            if (this._Tangage > this.maxTangage)
                this._Tangage = this.maxTangage;
            this.Azimut = this.o.azimut() % 360.0F;
            if (this.Azimut > 180.0F)
                this.Azimut -= 360.0F;
            else if (this.Azimut < -180.0F)
                this.Azimut += 360.0F;
            this.Tangage = this.o.tangage() % 360.0F;
            if (this.Tangage > 180.0F)
                this.Tangage -= 360.0F;
            else if (this.Tangage < -180.0F)
                this.Tangage += 360.0F;
            if (Actor.isValid(this.target))
                this.target.pos.inValidate(true);
            if (Actor.isValid(this.target2))
                this.target2.pos.inValidate(true);
        }
    }

    private HookPilot() {
        this.usezoom = this.usezoomc6 = true;
        this.gfactor = this.tfactor = this.boh = 1.0F;
        this.stepAzimut = 45.0F;
        this.stepTangage = 30.0F;
        this.maxAzimut = 155.0F;
        this.maxTangage = 89.0F;
        this.minTangage = -60.0F;
        this.Azimut = 0.0F;
        this.Tangage = 0.0F;
        this._Azimut = 0.0F;
        this._Tangage = 0.0F;
        this.rprevTime = 0L;
        this.timeKoof = 1.0F;
        this.o = new Orient();
        this.op = new Orient();
        this.le = new Loc();
        this.pe = new Point3d();
        this.pEnemyAbs = new Point3d();
        this.Ve = new Vector3d();
        this.pAbs = new Point3d();
        this.oAbs = new Orient();
        this.target = null;
        this.target2 = null;
        this.enemy = null;
        this.stamp = -1L;
        this.prevTime = -1L;
        this.roolTime = -1L;
        this.bUse = false;
        this.bPadlock = true;
        this.tPadlockEnd = -1L;
        this.tPadlockEndLen = 0L;
        this.bPadlockEnd = false;
        this.bForward = false;
        this.bAim = false;
        this.bUp = false;
        this.bVisibleEnemy = true;
        this.bSimpleUse = false;
        this.pCenter = new Point3d();
        this.pAim = new Point3d();
        this.pUp = new Point3d();
        this.bUseMouse = true;
        this.timeViewSet = -2000L;
    }

    public static HookPilot New() {
        if (current == null)
            current = new HookPilot();
        return current;
    }

    public static HookPilot cur() {
        return New();
    }
}