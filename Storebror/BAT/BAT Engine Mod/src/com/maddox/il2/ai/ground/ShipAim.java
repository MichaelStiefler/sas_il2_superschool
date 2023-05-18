package com.maddox.il2.ai.ground;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.AnglesForkExtended;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Time;

public class ShipAim extends Aim {

    private static final int SecsToTicks(float f) {
        int i = (int) (0.5F + (f / Time.tickLenFs()));
        return i >= 1 ? i : 1;
    }

    private static final float TicksToSecs(long l) {
        return l * Time.tickLenFs();
    }

    private static final float Rnd(float f, float f1) {
        return World.Rnd().nextFloat(f, f1);
    }

    private static final boolean RndB(float f) {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    public Actor getEnemy() {
        return this.enemy;
    }

    public float t() {
        return 1.0F - ((float) this.timeCur / (float) this.timeTot);
    }

    public boolean isInFiringMode() {
        return (this.curState == ShipAim.ST_TOFIREPOSITION) || (this.curState == ShipAim.ST_FIRING);
    }

    public boolean isInAimingMode() {
        return this.curState != ShipAim.ST_ENEMYSEARCH;
    }

    public ShipAim() {
    }

    public ShipAim(ShipHunterInterface hunterinterface, boolean flag) {
        this.aimingError = new Vector3d();
        this.passive = flag;
        this.bodyRotation = false;
        this.limitedRotation = false;
        this.hunter = hunterinterface;
        this.enemy = null;
        this.shotpoint_idx = -1;
        this.anglesYaw = new AnglesForkExtended(true, 0.0F, 0.0F);
        this.anglesPitch = new AnglesFork(0.0F);
        this.curState = this.S(ShipAim.ST_ENEMYSEARCH);
        this.waitforparkingTimer = 9999999;
        this.timeTot = 0L;
        this.passive_resetAutoParking();
        if (this.passive) {
            this.waitenemyTimer = ShipAim.SecsToTicks(9999F);
            this.reloadingTimer = 0;
        } else {
            this.waitenemyTimer = ShipAim.SecsToTicks(ShipAim.Rnd(ShipAim.TIME_FINDENEMY_MIN, ShipAim.TIME_FINDENEMY_MAX));
            this.reloadingTimer = ShipAim.SecsToTicks(ShipAim.Rnd(1.0F, 10F));
        }
    }

    public ShipAim(ShipHunterInterface hunterinterface, boolean flag, float f) {
        this(hunterinterface, flag);
        if (!this.passive) {
            this.reloadingTimer = ShipAim.SecsToTicks(ShipAim.Rnd(1.0F, 10F + f));
        }
    }

    public ShipAim(ShipHunterInterface hunterinterface, boolean flag, float f, boolean flag1) {
        this(hunterinterface, flag, f);
        this.bTrackingOnly = flag1;
    }

    private final int S(int i) {
        return i;
    }

    public void forgetAll() {
        this.forgetAiming();
        this.hunter = null;
    }

    public void forgetAiming() {
        if ((this.curState == ShipAim.ST_FIRING) && (this.hunter != null)) {
            this.hunter.stopFire(this);
        }
        this.forgetEnemy();
    }

    private void forgetEnemy() {
        this.enemy = null;
        this.shotpoint_idx = -1;
        this.curState = this.S(ShipAim.ST_ENEMYSEARCH);
        this.waitforparkingTimer = ShipAim.SecsToTicks(ShipAim.Rnd(ShipAim.TIME_PARKING_MIN, ShipAim.TIME_PARKING_MAX));
        this.timeTot = 0L;
        if (this.passive) {
            this.passive_resetAutoParking();
        }
    }

    private void passive_resetAutoParking() {
        this.passive_NumTrackingStepsBeforeAutoparking = ShipAim.PASSIVE_NUMTRACKINGSTEPSBEFOREAUTOPARKING;
    }

    public int tick_() {
        switch (this.curState) {
            case ST_ENEMYSEARCH:
                if (--this.reloadingTimer < 0) {
                    this.reloadingTimer = 0;
                }
                if (this.timeTot > 0L) {
                    this.timeCur--;
                    this.hunter.gunInMove(true, this);
                    if (this.timeCur <= 0L) {
                        this.timeTot = 0L;
                    }
                    this.waitforparkingTimer = 9999999;
                } else if (--this.waitforparkingTimer <= 0) {
                    this.hunter.gunStartParking(this);
                    this.waitforparkingTimer = 9999999;
                }
                if (this.enemy != null) {
                    System.out.println("*** Aim: ENEMY EXISTS!!!!!!!!!");
                    this.S(this.curState);
                    try {
                        byte byte0 = 6;
                        int k3 = 0;
                        int l3 = byte0 / k3;
                        System.out.println("****** c:" + l3);
                    } catch (Exception exception) {
                        System.out.println("Trace:" + exception);
                        exception.printStackTrace();
                    }
                    this.enemy = null;
                    this.shotpoint_idx = -1;
                    return 0;
                }
                if (--this.waitenemyTimer > 0) {
                    return 0;
                }
                if (this.passive) {
                    this.waitenemyTimer = ShipAim.SecsToTicks(9999F);
                    return 0;
                }
                this.waitenemyTimer = ShipAim.SecsToTicks(ShipAim.Rnd(ShipAim.TIME_FINDENEMY_MIN, ShipAim.TIME_FINDENEMY_MAX));
                this.shotpoint_idx = 0;
                this.enemy = this.hunter.findEnemy(this);
                if (this.enemy == null) {
                    this.shotpoint_idx = -1;
                    return 0;
                }
                this.waitenemyTimer /= 2;
                int i;
                if (!this.hunter.isMissile(this)) {
                    i = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TOSTARTFIRE_STEP, false);
                } else {
                    i = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TOSTARTFIRE_STEP, false);
                }

                if (i == ShipAim.ST_ENEMYSEARCH) {
                    this.forgetEnemy();
                } else if (this.reloadingTimer <= 0) {
                    if (i == ShipAim.ST_LOADINGTRACKING) {
                        this.curState = this.S(ShipAim.ST_TOFIREPOSITION);
                        if (!this.hunter.enterToFireMode(ShipAim.ST_LOADINGTRACKING, this.enemy, ShipAim.TicksToSecs(this.timeTot), this)) {
                            this.forgetEnemy();
                        }
                    } else {
                        this.curState = this.S(ShipAim.ST_TRACKING);
                        if (!this.hunter.enterToFireMode(ShipAim.ST_ENEMYSEARCH, this.enemy, 0.0F, this)) {
                            this.forgetEnemy();
                        }
                    }
                } else {
                    this.curState = this.S(ShipAim.ST_LOADINGTRACKING);
                    if (!this.hunter.enterToFireMode(ShipAim.ST_ENEMYSEARCH, this.enemy, 0.0F, this)) {
                        this.forgetEnemy();
                    }
                }
                return 0;

            case ST_LOADINGTRACKING:
                if (--this.reloadingTimer <= 0) {
                    int j;
                    if (!this.hunter.isMissile(this)) {
                        j = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TOSTARTFIRE_STEP, false);
                    } else {
                        j = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TOSTARTFIRE_STEP, false);
                    }

                    if (j == ShipAim.ST_LOADINGTRACKING) {
                        this.curState = this.S(ShipAim.ST_TOFIREPOSITION);
                        if (!this.hunter.enterToFireMode(ShipAim.ST_LOADINGTRACKING, this.enemy, ShipAim.TicksToSecs(this.timeTot), this)) {
                            this.forgetEnemy();
                        }
                    } else if (j == ShipAim.ST_TRACKING) {
                        int k;
                        if (!this.hunter.isMissile(this)) {
                            k = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                        } else {
                            k = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                        }

                        if (k == ShipAim.ST_LOADINGTRACKING) {
                            this.curState = this.S(ShipAim.ST_TOFIREPOSITION);
                            if (!this.hunter.enterToFireMode(ShipAim.ST_LOADINGTRACKING, this.enemy, ShipAim.TicksToSecs(this.timeTot), this)) {
                                this.forgetEnemy();
                            }
                        } else if (k == ShipAim.ST_TRACKING) {
                            this.curState = this.S(ShipAim.ST_TRACKING);
                        } else {
                            this.forgetEnemy();
                        }
                    } else {
                        this.forgetEnemy();
                    }
                    return 0;
                }
                if (--this.timeCur <= 0L) {
                    this.hunter.gunInMove(false, this);
                    int l;
                    if (!this.hunter.isMissile(this)) {
                        l = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                    } else {
                        l = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                    }

                    if (l == ShipAim.ST_ENEMYSEARCH) {
                        this.forgetEnemy();
                    }
                } else {
                    this.hunter.gunInMove(false, this);
                }
                return 0;

            case ST_TRACKING:
                if (--this.timeCur <= 0L) {
                    this.hunter.gunInMove(false, this);
                    int i1, j1;
                    if (this.passive) {
                        i1 = ShipAim.ST_TRACKING;
                        if (--this.passive_NumTrackingStepsBeforeAutoparking <= 0) {
                            this.forgetEnemy();
                            return 0;
                        }
                    } else {
                        if (!this.hunter.isMissile(this)) {
                            i1 = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TOSTARTFIRE_STEP, false);
                        } else {
                            i1 = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TOSTARTFIRE_STEP, false);
                        }
                    }
                    if (i1 == ShipAim.ST_LOADINGTRACKING) {
                        this.curState = this.S(ShipAim.ST_TOFIREPOSITION);
                        if (!this.hunter.enterToFireMode(ShipAim.ST_LOADINGTRACKING, this.enemy, ShipAim.TicksToSecs(this.timeTot), this)) {
                            this.forgetEnemy();
                        }
                    } else if (i1 == ShipAim.ST_TRACKING) {
                        if (!this.hunter.isMissile(this)) {
                            j1 = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                        } else {
                            j1 = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                        }
                        if (this.passive && (j1 == ShipAim.ST_LOADINGTRACKING)) {
                            j1 = ShipAim.ST_TRACKING;
                        }
                        if (j1 == ShipAim.ST_LOADINGTRACKING) {
                            this.curState = this.S(ShipAim.ST_TOFIREPOSITION);
                            if (!this.hunter.enterToFireMode(ShipAim.ST_LOADINGTRACKING, this.enemy, ShipAim.TicksToSecs(this.timeTot), this)) {
                                this.forgetEnemy();
                            }
                        } else if (j1 != ShipAim.ST_TRACKING) {
                            this.forgetEnemy();
                        }
                    } else {
                        this.forgetEnemy();
                    }
                } else {
                    this.hunter.gunInMove(false, this);
                }
                return 0;

            case ST_TOFIREPOSITION:
                if (--this.timeCur > 0L) {
                    this.hunter.gunInMove(false, this);
                    return 0;
                }
                this.anglesYaw.makeSrcSameAsDst();
                this.anglesPitch.makeSrcSameAsDst();
                this.hunter.gunInMove(false, this);
                this.fireTimer = (int) (0.5F + (this.hunter.chainFireTime(this) / ShipAim.TIME_CHAINFIRE_STEP));
                if (this.fireTimer > 0) {
                    int k1;
                    if (!this.hunter.isMissile(this)) {
                        k1 = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_CHAINFIRE_STEP, false);
                    } else {
                        k1 = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_CHAINFIRE_STEP, false);
                    }

                    if (k1 == ShipAim.ST_ENEMYSEARCH) {
                        this.timeTot = (int) (ShipAim.SecsToTicks(ShipAim.TIME_CHAINFIRE_STEP) * ShipAim.Rnd(ShipAim.TIME_TRACKING_STEP, 1.2F));
                        this.timeCur = 0L;
                        this.fireTimer = 1;
                    }
                    this.hunter.startFire(this);
                    if ((this.enemy instanceof Aircraft) && !this.bTrackingOnly && !NetMissionTrack.isPlaying()) {
                        try {
                            ((Maneuver) ((Aircraft) this.enemy).FM).Group.setUnderAAA(true, this);
                        } catch (Exception exception1) {
                        }
                    }
                    this.curState = this.S(ShipAim.ST_FIRING);
                } else {
                    this.hunter.singleShot(this);
                    if ((this.enemy instanceof Aircraft) && !this.bTrackingOnly && !NetMissionTrack.isPlaying()) {
                        try {
                            ((Maneuver) ((Aircraft) this.enemy).FM).Group.setUnderAAA(true, this);
                        } catch (Exception exception2) {
                        }
                    }
                    if (this.passive) {
                        this.curState = this.S(ShipAim.ST_TRACKING);
                        int l1;
                        if (!this.hunter.isMissile(this)) {
                            l1 = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                        } else {
                            l1 = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                        }
                        if (l1 == ShipAim.ST_ENEMYSEARCH) {
                            this.forgetEnemy();
                        } else {
                            this.passive_resetAutoParking();
                        }
                    } else {
                        this.reloadingTimer = ShipAim.SecsToTicks(this.hunter.getReloadingTime(this));
                        if (ShipAim.RndB(this.hunter.probabKeepSameEnemy(this.enemy))) {
                            this.curState = this.S(ShipAim.ST_LOADINGTRACKING);
                            int i2;
                            if (!this.hunter.isMissile(this)) {
                                i2 = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                            } else {
                                i2 = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                            }
                            if (i2 == ShipAim.ST_ENEMYSEARCH) {
                                this.forgetEnemy();
                            }
                        } else {
                            int i3 = ShipAim.SecsToTicks(this.hunter.minTimeRelaxAfterFight());
                            if (this.waitenemyTimer < i3) {
                                this.waitenemyTimer = (int) (i3 * ShipAim.Rnd(1.0F, 1.2F));
                            }
                            this.forgetEnemy();
                        }
                    }
                }
                return 0;

            case ST_FIRING:
                if (--this.timeCur > 0L) {
                    this.hunter.gunInMove(false, this);
                    return 0;
                }
                this.anglesYaw.makeSrcSameAsDst();
                this.anglesPitch.makeSrcSameAsDst();
                this.hunter.gunInMove(false, this);
                if (--this.fireTimer > 0) {
                    int j2;
                    if (!this.hunter.isMissile(this)) {
                        j2 = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_CHAINFIRE_STEP, false);
                    } else {
                        j2 = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_CHAINFIRE_STEP, false);
                    }
                    if (j2 == ShipAim.ST_ENEMYSEARCH) {
                        this.hunter.stopFire(this);
                        this.timeTot = (int) (ShipAim.SecsToTicks(ShipAim.TIME_CHAINFIRE_STEP) * ShipAim.Rnd(ShipAim.TIME_TRACKING_STEP, 1.2F));
                        this.timeCur = 0L;
                        this.fireTimer = 1;
                    } else {
                        this.hunter.continueFire(this);
                    }
                    return 0;
                }
                this.hunter.stopFire(this);
                if (this.passive) {
                    this.curState = this.S(ShipAim.ST_TRACKING);
                    int k2;
                    if (!this.hunter.isMissile(this)) {
                        k2 = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                    } else {
                        k2 = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                    }
                    if (k2 == ShipAim.ST_ENEMYSEARCH) {
                        this.forgetEnemy();
                    } else {
                        this.passive_resetAutoParking();
                    }
                } else {
                    this.reloadingTimer = ShipAim.SecsToTicks(this.hunter.getReloadingTime(this));
                    if (ShipAim.RndB(this.hunter.probabKeepSameEnemy(this.enemy))) {
                        this.curState = this.S(ShipAim.ST_LOADINGTRACKING);
                        int l2;
                        if (!this.hunter.isMissile(this)) {
                            l2 = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                        } else {
                            l2 = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
                        }
                        if (l2 == ShipAim.ST_ENEMYSEARCH) {
                            this.forgetEnemy();
                        }
                    } else {
                        int j3 = ShipAim.SecsToTicks(this.hunter.minTimeRelaxAfterFight());
                        if (this.waitenemyTimer < j3) {
                            this.waitenemyTimer = (int) (j3 * ShipAim.Rnd(1.0F, 1.2F));
                        }
                        this.forgetEnemy();
                    }
                }
                return 0;
        }
        System.out.println("Error in ShipAim: unexpected state");
        return 0;
    }

    public void passive_StartFiring(int i, Actor actor, int j, float f) {
        this.passive_resetAutoParking();
        int k;
        if (i == ShipAim.ST_ENEMYSEARCH) {
            if ((this.curState == ShipAim.ST_TOFIREPOSITION) || (this.curState == ShipAim.ST_FIRING)) {
                return;
            }
            if ((actor == this.enemy) && (this.curState == ShipAim.ST_TRACKING)) {
                this.shotpoint_idx = j;
                return;
            }
            this.forgetAiming();
            this.enemy = actor;
            this.shotpoint_idx = j;
            this.curState = this.S(ShipAim.ST_TRACKING);
            if (!this.hunter.isMissile(this)) {
                k = this.hunter.targetGun(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
            } else {
                k = this.hunter.targetMissile(this, this.enemy, ShipAim.TIME_TRACKING_STEP, false);
            }
        } else {
            i = ShipAim.ST_LOADINGTRACKING;
            if ((this.curState == ShipAim.ST_TOFIREPOSITION) || (this.curState == ShipAim.ST_FIRING)) {
                return;
            }
            this.forgetAiming();
            this.enemy = actor;
            this.shotpoint_idx = j;
            this.curState = this.S(ShipAim.ST_TOFIREPOSITION);
            if (!this.hunter.isMissile(this)) {
                k = this.hunter.targetGun(this, this.enemy, f, false);
            } else {
                k = this.hunter.targetMissile(this, this.enemy, f, false);
            }
        }
        if (k == ShipAim.ST_ENEMYSEARCH) {
            this.forgetEnemy();
        } else if (!this.hunter.enterToFireMode(i, this.enemy, ShipAim.TicksToSecs(this.timeTot), this)) {
            this.forgetEnemy();
        }
    }

    public int setRotationForTargeting(Actor actor, Orient orient, Point3d point3d, float f, float f1, Vector3d vector3d, float f2, float f3, AnglesRange anglesrange, AnglesRange anglesrange1, boolean nfflag, float f4, float f5, float f6, float f7, float f8) {
        if (f3 <= 0.0F) {
            f3 = 0.0F;
        }
        if (f2 > 0.0F) {
            Vector3d vector3d1 = new Vector3d();
            vector3d1.x = World.Rnd().nextDouble(-1D, 1.0D);
            vector3d1.y = World.Rnd().nextDouble(-1D, 1.0D);
            vector3d1.z = World.Rnd().nextDouble(-1D, 1.0D);
            if (vector3d1.length() > 0.0001D) {
                float f10 = Geom.tanDeg(f2);
                vector3d1.scale(f10 / vector3d1.length());
                Vector3d vector3d2 = new Vector3d();
                vector3d2.set(vector3d);
                vector3d2.add(vector3d1);
                if (vector3d2.length() > 0.01D) {
                    vector3d2.normalize();
                    vector3d.set(vector3d2);
                }
            }
        }
        Object obj = new Vector3d();
        orient.transformInv(vector3d, ((com.maddox.JGP.Tuple3d) (obj)));
        Orient orient1 = new Orient();
        orient1.setAT0(((Vector3d) (obj)));
        float f9 = anglesrange.transformIntoRangeSpace(orient1.getYaw());
        float f221 = anglesrange1.transformIntoRangeSpace(orient1.getYaw());
        float f11 = AnglesFork.signedAngleDeg(orient1.getPitch());
        if (f5 >= 90F) {
            f5 = 90F;
            if (!anglesrange.fullcircle()) {
                System.out.println("*** ShipAim: zenith without full circle!");
                return 0;
            }
            if ((f7 <= 0.001F) || (f6 <= 0.001F)) {
                System.out.println("*** ShipAim: zenith speed(s)!");
                return 0;
            }
            if (f8 > 0.0F) {
                System.out.println("*** ShipAim: zenith with body rotation!");
                return 0;
            }
            if (f11 < f4) {
                f11 = f4;
                obj = new Orient();
                ((Orient) (obj)).setYPR(f9, f11, 0.0F);
                vector3d.set(1.0D, 0.0D, 0.0D);
                ((Orient) (obj)).transform(vector3d);
                orient.transform(vector3d);
            }
        } else if ((f11 < f4) || (f11 > f5)) {
            return 0;
        }
        ShipAim.checkBegPoint.set(point3d);
        ShipAim.checkEndPoint.set(vector3d);
        ShipAim.checkEndPoint.scale(250D);
        ShipAim.checkEndPoint.add(ShipAim.checkBegPoint);
        obj = Engine.collideEnv().getLine(ShipAim.checkBegPoint, ShipAim.checkEndPoint, false, actor, null);
        if ((obj != null) && ((((Actor) (obj)).getArmy() == 0) || (((Actor) (obj)).getArmy() == actor.getArmy()) || !((Actor) (obj)).isAlive())) {
            return 0;
        }
        if (anglesrange.fullcircle()) {
            this.bodyRotation = false;
            this.limitedRotation = false;
        } else if (anglesrange.isInside(f9) && (!anglesrange1.isInside(f221) || !nfflag)) {
            this.bodyRotation = false;
            this.limitedRotation = false;
        } else {
            if (f8 <= 0.0F) {
                return 0;
            }
            this.bodyRotation = true;
            this.limitedRotation = false;
            f6 = f8;
        }
        this.anglesYaw.setDeg(anglesrange.fullcircle(), f, f9);
        this.anglesPitch.setDeg(f1, f11);
        boolean flag = false;
        float f12 = this.anglesYaw.getAbsDiffDeg();
        float f14 = this.anglesPitch.getAbsDiffDeg();
        if (f5 >= 90F) {
            float f16 = f12 / f6;
            float f17 = f14 / f7;
            this.anglesYaw.setDeg(f, f9 + 180F);
            this.anglesPitch.setDeg(f1, 180F - f11);
            float f18 = this.anglesYaw.getAbsDiffDeg();
            float f19 = this.anglesPitch.getAbsDiffDeg();
            float f20 = f18 / f6;
            float f21 = f19 / f7;
            if (Math.max(f16, f17) < Math.max(f20, f21)) {
                this.anglesYaw.setDeg(f, f9);
                this.anglesPitch.setDeg(f1, f11);
            } else {
                f12 = f18;
                f14 = f19;
            }
        }
        if ((f12 > 0.2F) && ((f3 < 0.0001F) || ((f12 / f3) > f6))) {
            float f13 = f6 * f3;
            if (this.anglesYaw.getDiffDeg() <= 0.0F) {
                f13 = -f13;
            }
            this.anglesYaw.setDeg(f, f + f13);
            flag = true;
        }
        if ((f14 > 0.2F) && ((f3 < 0.0001F) || ((f14 / f3) > f7))) {
            float f15 = f7 * f3;
            if (this.anglesPitch.getDiffDeg() <= 0.0F) {
                f15 = -f15;
            }
            this.anglesPitch.setDeg(f1, f1 + f15);
            flag = true;
        }
        if (this.bodyRotation) {
            this.anglesYaw.setDeg(0.0F, this.anglesYaw.getDiffDeg());
        }
        this.timeTot = this.timeCur = ShipAim.SecsToTicks(f3);
        return flag ? 2 : 1;
    }

    public int setRotationForTargetingMissile(Actor actor, Orient orient, Point3d point3d, float headYawNow, float gunPitchNow, Vector3d vector3d, float f2, float f3, AnglesRange anglesrange, AnglesRange anglesrange1, boolean nfflag, float gunMinPitch, float gunMaxPitch, float speedMaxYaw, float speedMaxPitch, float f8, float YAW_MIN, float YAW_MAX, float NOFIRE_MIN, float NOFIRE_MAX) {
        if (f3 <= 0.0F) {
            f3 = 0.0F;
        }
        if (f2 > 0.0F) {
            Vector3d vector3d1 = new Vector3d();
            vector3d1.x = World.Rnd().nextDouble(-1D, 1.0D);
            vector3d1.y = World.Rnd().nextDouble(-1D, 1.0D);
            vector3d1.z = World.Rnd().nextDouble(-1D, 1.0D);
            if (vector3d1.length() > 0.0001D) {
                float f10 = Geom.tanDeg(f2);
                vector3d1.scale(f10 / vector3d1.length());
                Vector3d vector3d2 = new Vector3d();
                vector3d2.set(vector3d);
                vector3d2.add(vector3d1);
                if (vector3d2.length() > 0.01D) {
                    vector3d2.normalize();
                    vector3d.set(vector3d2);
                }
            }
        }
        Vector3d v3dtemp = new Vector3d();
        orient.transformInv(vector3d, v3dtemp);
        Orient orient1 = new Orient();
        orient1.setAT0(v3dtemp);
        float f9 = anglesrange.transformIntoRangeSpace(orient1.getYaw());
        float f221 = anglesrange1.transformIntoRangeSpace(orient1.getYaw());
        float f11 = AnglesFork.signedAngleDeg(orient1.getPitch());
        if (gunMaxPitch >= 90F) {
            gunMaxPitch = 90F;
            if (!anglesrange.fullcircle()) {
                System.out.println("*** ShipAim: zenith without full circle!");
                return 0;
            }
            if ((speedMaxPitch <= 0.001F) || (speedMaxYaw <= 0.001F)) {
                System.out.println("*** ShipAim: zenith speed(s)!");
                return 0;
            }
            if (f8 > 0.0F) {
                System.out.println("*** ShipAim: zenith with body rotation!");
                return 0;
            }
            if (f11 < gunMinPitch) {
                f11 = gunMinPitch;
                Orient otemp = new Orient();
                otemp.setYPR(f9, f11, 0.0F);
                vector3d.set(1.0D, 0.0D, 0.0D);
                otemp.transform(vector3d);
                orient.transform(vector3d);
            }
        } else if (f11 < gunMinPitch) {
            f11 = gunMinPitch;
        } else if (f11 > gunMaxPitch) {
            f11 = gunMaxPitch;
        }
        ShipAim.checkBegPoint.set(point3d);
        ShipAim.checkEndPoint.set(vector3d);
        ShipAim.checkEndPoint.scale(250D);
        ShipAim.checkEndPoint.add(ShipAim.checkBegPoint);
        Actor collideActor = Engine.collideEnv().getLine(ShipAim.checkBegPoint, ShipAim.checkEndPoint, false, actor, null);
        if ((collideActor != null) && ((collideActor.getArmy() == 0) || (collideActor.getArmy() == actor.getArmy()) || !collideActor.isAlive())) {
            return 0;
        }
        if (anglesrange.fullcircle()) {
            this.bodyRotation = false;
            this.limitedRotation = false;
        } else if (anglesrange.isInside(f9) && (!anglesrange1.isInside(f221) || !nfflag)) {
            this.bodyRotation = false;
            this.limitedRotation = false;
        } else {
            if (f8 < 0.0F) {
                return 0;
            } else {
                this.bodyRotation = false;
                this.limitedRotation = true;
            }
        }
        boolean flag = false;
        if (this.limitedRotation) {
            float yawdiff[] = new float[4];
            int index = 0;
            if (nfflag) {
                yawdiff[0] = Math.abs(f9 - YAW_MIN);
                yawdiff[1] = Math.abs(f9 - YAW_MAX);
                yawdiff[2] = Math.abs(f9 - NOFIRE_MIN);
                yawdiff[3] = Math.abs(f9 - NOFIRE_MAX);
                for (int i = 1; i < 4; i++) {
                    if (yawdiff[index] > yawdiff[i]) {
                        index = i;
                    }
                }
            } else {
                yawdiff[0] = Math.abs(f9 - YAW_MIN);
                yawdiff[1] = Math.abs(f9 - YAW_MAX);
                if (yawdiff[0] > yawdiff[1]) {
                    index = 1;
                }
            }
            switch (index) {
                case 0:
                    f9 = YAW_MIN;
                    break;
                case 1:
                    f9 = YAW_MAX;
                    break;
                case 2:
                    f9 = NOFIRE_MIN;
                    break;
                case 3:
                    f9 = NOFIRE_MAX;
                    break;
                default:
                    f9 = 0F;
                    break;
            }
        }

        this.anglesYaw.setDeg(anglesrange.fullcircle(), headYawNow, f9);
        this.anglesPitch.setDeg(gunPitchNow, f11);
        float f12 = this.anglesYaw.getAbsDiffDeg();
        float f14 = this.anglesPitch.getAbsDiffDeg();
        if (gunMaxPitch >= 90F) {
            float f16 = f12 / speedMaxYaw;
            float f17 = f14 / speedMaxPitch;
            this.anglesYaw.setDeg(headYawNow, f9 + 180F);
            this.anglesPitch.setDeg(gunPitchNow, 180F - f11);
            float f18 = this.anglesYaw.getAbsDiffDeg();
            float f19 = this.anglesPitch.getAbsDiffDeg();
            float f20 = f18 / speedMaxYaw;
            float f21 = f19 / speedMaxPitch;
            if (Math.max(f16, f17) < Math.max(f20, f21)) {
                this.anglesYaw.setDeg(headYawNow, f9);
                this.anglesPitch.setDeg(gunPitchNow, f11);
            } else {
                f12 = f18;
                f14 = f19;
            }
        }
        if ((f12 > 0.2F) && ((f3 < 0.0001F) || ((f12 / f3) > speedMaxYaw))) {
            float f13 = speedMaxYaw * f3;
            if (this.anglesYaw.getDiffDeg() <= 0.0F) {
                f13 = -f13;
            }
            this.anglesYaw.setDeg(headYawNow, headYawNow + f13);
            flag = true;
        }
        if ((f14 > 0.2F) && ((f3 < 0.0001F) || ((f14 / f3) > speedMaxPitch))) {
            float f15 = speedMaxPitch * f3;
            if (this.anglesPitch.getDiffDeg() <= 0.0F) {
                f15 = -f15;
            }
            this.anglesPitch.setDeg(gunPitchNow, gunPitchNow + f15);
            flag = true;
        }
        if (this.bodyRotation) {
            this.anglesYaw.setDeg(0.0F, this.anglesYaw.getDiffDeg());
        }
        this.timeTot = this.timeCur = ShipAim.SecsToTicks(f3);
        return flag ? 2 : 1;
    }

    public boolean setRotationForParking(float f, float f1, float f2, float f3, AnglesRange anglesrange, float f4, float f5) {
        this.bodyRotation = false;
        this.limitedRotation = false;
        this.anglesYaw.setDeg(anglesrange.fullcircle(), f, f2);
        this.anglesPitch.setDeg(f1, f3);
        float f6 = this.anglesYaw.getAbsDiffDeg() / f4;
        float f7 = this.anglesPitch.getAbsDiffDeg() / f5;
        this.timeTot = this.timeCur = ShipAim.SecsToTicks(Math.max(f6, f7));
        return this.timeTot > 1L;
    }

    public Vector3d getAimingError() {
        return this.aimingError;
    }

    public void setAimingError(float f, float f1, float f2) {
        this.aimingError.set(f, f1, f2);
    }

    public void scaleAimingError(float f) {
        this.aimingError.scale(f);
    }

    public static final int     RES_FAILED                                = 0;
    public static final int     RES_OK                                    = 1;
    public static final int     RES_NOT_ENOUGH_TIME                       = 2;
    private static final float  TIME_FINDENEMY_MIN                        = 1.7F;
    private static final float  TIME_FINDENEMY_MAX                        = 3.6F;
    private static final float  TIME_PARKING_MIN                          = 17F;
    private static final float  TIME_PARKING_MAX                          = 25F;
    private static final float  TIME_TRACKING_STEP                        = 0.8F;
    private static final int    PASSIVE_NUMTRACKINGSTEPSBEFOREAUTOPARKING = 75;
    private static final float  TIME_TOSTARTFIRE_STEP                     = 0.22F;
    private static final float  TIME_CHAINFIRE_STEP                       = 0.75F;
    public static final float   DISTANCE_WAKEUP                           = 2000F;
    public static final float   AngleErrorKoefForSkill[]                  = { 2.3F, 1.5F, 1.0F, 0.5F };
    private boolean             limitedRotation;
    private boolean             passive;
    private int                 curState;
    private static final int    ST_ENEMYSEARCH                            = 0;
    private static final int    ST_LOADINGTRACKING                        = 1;
    private static final int    ST_TRACKING                               = 2;
    private static final int    ST_TOFIREPOSITION                         = 3;
    private static final int    ST_FIRING                                 = 4;
    private ShipHunterInterface hunter;
    private Actor               enemy;
    private int                 waitenemyTimer;
    private int                 waitforparkingTimer;
    private int                 passive_NumTrackingStepsBeforeAutoparking;
    private int                 reloadingTimer;
    private int                 fireTimer;
    private static Point3d      checkBegPoint                             = new Point3d();
    private static Point3d      checkEndPoint                             = new Point3d();
    private Vector3d            aimingError;
}
