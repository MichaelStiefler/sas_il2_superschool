package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class ME_262V3 extends ME_262 {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.getSquares();
    }

    public void moveSteering(float f) {
        if (f > 77.5F) {
            f = 77.5F;
            this.FM.Gears.steerAngle = f;
        }
        if (f < -77.5F) {
            f = -77.5F;
            this.FM.Gears.steerAngle = f;
        }
        this.hierMesh().chunkSetAngles("GearC2rear_D0", 0.0F, -f, 0.0F);
    }

    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        super.setOnGround(point3d, orient, vector3d);
        if (this.FM.isPlayers()) this.FM.CT.setTrimElevatorControl(0.5F);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.isPlayers() && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) {
            if (this.startModToStd != -1L) this.setSquaresStdSmoooth();
            if (this.FM.Gears.nOfGearsOnGr > 1 && this.FM.getAOA() > 15F) {
                if (!this.isOnGround) {
                    this.getSquares();
                    this.setSquaresMod();
                    this.startModToStd = -1L;
                    this.isOnGround = true;
                }
            } else if (this.isOnGround && this.FM.CT.BrakeControl > 0.5F && this.FM.getSpeedKMH() > 160F) {
                this.isOnGround = false;
                this.startModToStd = Time.current();
            }
            if (this.FM.getAltitude() - World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) > 10D && this.isOnGround) {
                this.isOnGround = false;
                this.startModToStd = Time.current();
            }
        }
    }

    public void engineSurge(float f) {
        if (DEBUG) return;
        if (!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
            super.engineSurge(f);
            return;
        }
        for (int i = 0; i < 2; i++) {
            this.FM.EI.engines[i].engineAcceleration = smoothCvt(this.FM.EI.engines[i].getRPM(), 2450F, 3400F, 0.03F, 0.15F); // limit engine acceleration below RPM_CRIT
            if (this.curthrl[i] == -1F) this.curthrl[i] = this.oldthrl[i] = this.FM.EI.engines[i].getControlThrottle();
            else {
//                // +++ Debugging Output only
//                if (DEBUG_SPOOL_UP && this.FM.brakeShoe) {
//                    if (i == 0 && this.oldthrl[i] < 0.01F)
//                        spoolUpStartTest = Time.current();
//                    float newThrottle = this.oldthrl[i] + CHECK_DIFF_UP_PER_TICK * f * THROTTLE_SMOOTHING_FACTOR / (1.0F - CHECK_DIFF_UP_PER_TICK * f);
//                    if (i == 0 && newThrottle > 1.0F && spoolUpTimeTest == -1L)
//                        spoolUpTimeTest = Time.current() - spoolUpStartTest;
//                    if (newThrottle > 1.0F)
//                        newThrottle = 1.0F;
//                    this.FM.EI.engines[i].setControlThrottle(newThrottle);
//                    if (i == 0 && this.FM.EI.engines[i].getRPM() > 3300F && spoolUpTimeTest2 == -1L)
//                        spoolUpTimeTest2 = Time.current() - spoolUpStartTest;
//                    if (i == 0) {
////                      HUD.training((spoolUpTimeTest > 0L ? "T1=" + spoolUpTimeTest + ", ":"") + (spoolUpTimeTest2 > 0L ? "T2=" + spoolUpTimeTest2 + ", ":"") + "gt=" + df.format(this.FM.EI.engines[i].getControlThrottle()) + ", ct=" + df.format(this.curthrl[i]) + ", cof=" + df.format((this.curthrl[i] - this.oldthrl[i]) / f));
////                        HUD.training((spoolUpTimeTest > 0L ? "T1=" + spoolUpTimeTest : "") + (spoolUpTimeTest2 > 0L ? "     T2=" + spoolUpTimeTest2 : ""));
//                    }
//                }
//                // ---
                this.curthrl[i] = (THROTTLE_SMOOTHING_FACTOR * this.oldthrl[i] + this.FM.EI.engines[i].getControlThrottle()) / (THROTTLE_SMOOTHING_FACTOR + 1.0F);
                if ((this.curthrl[i] - this.oldthrl[i]) / f > CHECK_DIFF_UP_PER_TICK && this.FM.EI.engines[i].getRPM() < RPM_CRIT && this.FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.5F) {
                    if (this.FM.actor == World.getPlayerAircraft()) HUD.log("Compressor Stall!");
                    this.playSound("weapon.MGunMk108s", true);
                    this.engineSurgeDamage[i] += 0.01D * (this.FM.EI.engines[i].getRPM() / 1000F);
                    this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - this.engineSurgeDamage[i]);
                    if (World.Rnd().nextFloat() < 0.2F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) this.FM.AS.hitEngine(this, i, 100);
                    if (World.Rnd().nextFloat() < 0.2F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) this.FM.EI.engines[i].setEngineDies(this);
                    this.curthrl[i] = this.oldthrl[i] = this.FM.EI.engines[i].getControlThrottle(); // Make sure to skip
                                                                                                    // further engine
                                                                                                    // damage for this
                                                                                                    // throttle change,
                                                                                                    // it's been handled
                                                                                                    // already!
                }
                if ((this.curthrl[i] - this.oldthrl[i]) / f < -CHECK_DIFF_DOWN_PER_TICK && (this.curthrl[i] - this.oldthrl[i]) / f > -100F && this.FM.EI.engines[i].getRPM() < RPM_CRIT && this.FM.EI.engines[i].getStage() == 6) {
                    this.playSound("weapon.MGunMk108s", true);
                    this.engineSurgeDamage[i] += 0.001D * (this.FM.EI.engines[i].getRPM() / 1000F);
                    this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - this.engineSurgeDamage[i]);
                    if (World.Rnd().nextFloat() < 0.5F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) {
                        if (this.FM.actor == World.getPlayerAircraft()) HUD.log("Engine Flameout!");
                        this.FM.EI.engines[i].setEngineStops(this);
                    } else {
                        if (this.FM.actor == World.getPlayerAircraft()) HUD.log("Compressor Stall!");
                        this.FM.EI.engines[i].setKillCompressor(this);
                    }
                    this.curthrl[i] = this.oldthrl[i] = this.FM.EI.engines[i].getControlThrottle(); // Make sure to skip
                                                                                                    // further engine
                                                                                                    // damage for this
                                                                                                    // throttle change,
                                                                                                    // it's been handled
                                                                                                    // already!
                }
                this.oldthrl[i] = this.curthrl[i];
            }
        }
    }

    private void getSquares() {
        this.squareWing = this.FM.Sq.squareWing;
        this.squareAilerons = this.FM.Sq.squareAilerons;
        this.squareElevators = this.FM.Sq.squareElevators;
        this.squareRudders = this.FM.Sq.squareRudders;
        this.squareFlaps = this.FM.Sq.squareFlaps;
        this.liftWingLIn = this.FM.Sq.liftWingLIn;
        this.liftWingLMid = this.FM.Sq.liftWingLMid;
        this.liftWingLOut = this.FM.Sq.liftWingLOut;
        this.liftWingRIn = this.FM.Sq.liftWingRIn;
        this.liftWingRMid = this.FM.Sq.liftWingRMid;
        this.liftWingROut = this.FM.Sq.liftWingROut;
        this.liftStab = this.FM.Sq.liftStab;
        this.liftKeel = this.FM.Sq.liftKeel;
        this.sensPitch = this.FM.SensPitch;
    }

    private void setSquaresStd() {
        this.FM.Sq.squareWing = this.squareWing;
        this.FM.Sq.squareAilerons = this.squareAilerons;
        this.FM.Sq.squareElevators = this.squareElevators;
        this.FM.Sq.squareRudders = this.squareRudders;
        this.FM.Sq.squareFlaps = this.squareFlaps;
        this.FM.Sq.liftWingLIn = this.liftWingLIn;
        this.FM.Sq.liftWingLMid = this.liftWingLMid;
        this.FM.Sq.liftWingLOut = this.liftWingLOut;
        this.FM.Sq.liftWingRIn = this.liftWingRIn;
        this.FM.Sq.liftWingRMid = this.liftWingRMid;
        this.FM.Sq.liftWingROut = this.liftWingROut;
        this.FM.Sq.liftStab = this.liftStab;
        this.FM.Sq.liftKeel = this.liftKeel;
        this.FM.SensPitch = this.sensPitch;
    }

    private void setSquaresStdSmoooth() {
        if (Time.current() > this.startModToStd + TIME_MOD_TO_STD) {
            this.startModToStd = -1L;
            this.setSquaresStd();
            return;
        }
        float fStepCur = Time.current() - this.startModToStd;
        float squareAileronsSmooth = smoothCvt(fStepCur, 0.0F, TIME_MOD_TO_STD, MOD_MULTIPLIER_SQUARE_AILERONS, 1.0F);
        float squareElevatorsSmooth = smoothCvt(fStepCur, 0.0F, TIME_MOD_TO_STD, MOD_MULTIPLIER_SQUARE_ELEVATORS, 1.0F);
        float squareFlapsSmooth = smoothCvt(fStepCur, 0.0F, TIME_MOD_TO_STD, MOD_MULTIPLIER_SQUARE_FLAPS, 1.0F);
        float liftWingSmooth = smoothCvt(fStepCur, 0.0F, TIME_MOD_TO_STD, MOD_MULTIPLIER_LIFT_WING, 1.0F);
        float liftStabSmooth = smoothCvt(fStepCur, 0.0F, TIME_MOD_TO_STD, MOD_MULTIPLIER_LIFT_STAB, 1.0F);
        float sensPitchSmooth = smoothCvt(fStepCur, 0.0F, TIME_MOD_TO_STD, MOD_MULTIPLIER_SENS_PITCH, 1.0F);
        this.FM.Sq.squareAilerons = this.squareAilerons * squareAileronsSmooth;
        this.FM.Sq.squareElevators = this.squareElevators * squareElevatorsSmooth;
        this.FM.Sq.squareFlaps = this.squareFlaps * squareFlapsSmooth;
        this.FM.Sq.liftWingLIn = this.liftWingLIn * liftWingSmooth;
        this.FM.Sq.liftWingLMid = this.liftWingLMid * liftWingSmooth;
        this.FM.Sq.liftWingLOut = this.liftWingLOut * liftWingSmooth;
        this.FM.Sq.liftWingRIn = this.liftWingRIn * liftWingSmooth;
        this.FM.Sq.liftWingRMid = this.liftWingRMid * liftWingSmooth;
        this.FM.Sq.liftWingROut = this.liftWingROut * liftWingSmooth;
        this.FM.Sq.liftStab = this.liftStab * liftStabSmooth;
        this.FM.SensPitch = this.sensPitch * sensPitchSmooth;
    }

    private void setSquaresMod() {
        this.FM.Sq.squareAilerons = this.squareAilerons * MOD_MULTIPLIER_SQUARE_AILERONS;
        this.FM.Sq.squareElevators = this.squareElevators * MOD_MULTIPLIER_SQUARE_ELEVATORS;
        this.FM.Sq.squareFlaps = this.squareFlaps * MOD_MULTIPLIER_SQUARE_FLAPS;
        this.FM.Sq.liftWingLIn = this.liftWingLIn * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingLMid = this.liftWingLMid * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingLOut = this.liftWingLOut * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingRIn = this.liftWingRIn * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingRMid = this.liftWingRMid * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftWingROut = this.liftWingROut * MOD_MULTIPLIER_LIFT_WING;
        this.FM.Sq.liftStab = this.liftStab * MOD_MULTIPLIER_LIFT_STAB;
        this.FM.SensPitch = this.sensPitch * MOD_MULTIPLIER_SENS_PITCH;
    }

    private static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
        return outMin + (outMax - outMin) * (-0.5F * (float) Math.cos((inputValue - inMin) / (inMax - inMin) * Math.PI) + 0.5F);
    }

    private float              oldthrl[]                       = { -1F, -1F };
    private float              curthrl[]                       = { -1F, -1F };
    private float              engineSurgeDamage[]             = { 0.0F, 0.0F };
    private boolean            isOnGround                      = false;
    private float              squareWing;
    private float              squareAilerons;
    private float              squareElevators;
    private float              squareRudders;
    private float              squareFlaps;
    private float              liftWingLIn;
    private float              liftWingLMid;
    private float              liftWingLOut;
    private float              liftWingRIn;
    private float              liftWingRMid;
    private float              liftWingROut;
    private float              liftStab;
    private float              liftKeel;
    private float              sensPitch;
    private long               startModToStd                   = -1L;
    private static final float RPM_CRIT                        = 2450F; // Equals 6000 RPM = limit for automatic fuel injector, above this the
                                                                        // throttle can be moved quickly
    private static final float MOD_MULTIPLIER_SQUARE_AILERONS  = 0.0F;
    private static final float MOD_MULTIPLIER_SQUARE_ELEVATORS = 0.0F;
    private static final float MOD_MULTIPLIER_SQUARE_FLAPS     = 0.0F;
    private static final float MOD_MULTIPLIER_LIFT_WING        = 0.5F;
    private static final float MOD_MULTIPLIER_LIFT_STAB        = -1.0F;
    private static final float MOD_MULTIPLIER_SENS_PITCH       = 0.0F;
    private static final long  TIME_MOD_TO_STD                 = 1500L;
    private static final float THROTTLE_SMOOTHING_FACTOR       = 50.0F; // Smoothing factor, smoothens "spikes" in throttle
                                                                        // movement and limits engine throttle response
    private static final float MAX_PERCENTAGE_UP_PER_TICK      = 10.0F; // Maximum Throttle percent per tick (0.03s) which is
                                                                        // allowed for throttle up below RPM_CRIT
    private static final float MAX_PERCENTAGE_DOWN_PER_TICK    = 20.0F; // Maximum Throttle percent per tick (0.03s) which
                                                                        // is allowed for throttle down below RPM_CRIT
    private static final float CHECK_DIFF_UP_PER_TICK          = MAX_PERCENTAGE_UP_PER_TICK / 100.0F / 0.03F / (THROTTLE_SMOOTHING_FACTOR + 1.0F); // Internal value for Throttle movement check
    private static final float CHECK_DIFF_DOWN_PER_TICK        = MAX_PERCENTAGE_DOWN_PER_TICK / 100.0F / 0.03F / (THROTTLE_SMOOTHING_FACTOR + 1.0F); // Internal value for Throttle movement check

    // Parameters below are for Debugging purpose only
    private static boolean DEBUG = false;
//    private static boolean     DEBUG_SPOOL_UP                  = false;
//    private static DecimalFormat       df                              = new DecimalFormat("#.##");
//    private long               spoolUpStartTest                = -1L;
//    private long               spoolUpTimeTest                 = -1L;
//    private long               spoolUpTimeTest2                = -1L;

    static {
        Class class1 = ME_262V3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me 262");
        Property.set(class1, "meshName", "3DO/Plane/Me-262V-3/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1943.1F);
        Property.set(class1, "yearExpired", 1943.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-262V3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262.class });
        Property.set(class1, "LOSElevation", 0.74615F);
        Aircraft.weaponTriggersRegister(class1, new int[2]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
    }
}
