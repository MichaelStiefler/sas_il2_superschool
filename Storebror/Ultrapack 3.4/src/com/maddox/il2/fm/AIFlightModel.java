/*Here only because of obfuscation problems*/
package com.maddox.il2.fm;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Time;

public class AIFlightModel extends FlightModel {
    private long       w;
    public float       Density;
    public float       Kq;
    protected boolean  callSuperUpdate = true;
    protected boolean  dataDrawn       = false;
    Vector3d           Cw              = new Vector3d();
    Vector3d           Fw              = new Vector3d();
    protected Vector3d Wtrue           = new Vector3d();
    private Point3d    TmpP            = new Point3d();
    private Vector3d   Vn              = new Vector3d();
    private Vector3d   TmpV            = new Vector3d();
//	private Vector3d	TmpVd			= new Vector3d();
//	private Loc			L				= new Loc();

    public AIFlightModel(String string) {
        super(string);
        this.w = Time.current();
    }

    private void flutter() {
        ((Aircraft) this.actor).msgCollision(this.actor, "CF_D0", "CF_D0");
    }

    public Vector3d getW() {
        return this.Wtrue;
    }

    public void update(float f) {
        this.CT.update(f, (this.getSpeed() + 50.0F) * 0.5F, this.EI, false);
        this.Wing.setFlaps(this.CT.getFlap());
        this.EI.update(f);
        super.update(f);
        this.Gravity = this.M.getFullMass() * Atmosphere.g();
        this.M.computeFullJ(this.J, this.J0);
        if (this.isTick(44, 0)) {
            this.AS.update(f * 44.0F);
            ((Aircraft) this.actor).rareAction(f * 44.0F, true);
            this.M.computeParasiteMass(this.CT.Weapons);
            this.Sq.computeParasiteDrag(this.CT, this.CT.Weapons);
            if (((Pilot) ((Aircraft) this.actor).FM).get_task() == 7 || ((Maneuver) ((Aircraft) this.actor).FM).get_maneuver() == 43) this.putScareShpere();
            if (World.Rnd().nextInt(0, 99) < 25) {
                Aircraft aircraft = (Aircraft) this.actor;
                if (aircraft.aircIndex() == 0 && !(aircraft instanceof TypeFighter)) {
                    int i = this.actor.getArmy() - 1 & 0x1;
                    int i_0_ = (int) (Time.current() / 60000L);
                    if (i_0_ > Voice.cur().SpeakBombersUnderAttack[i] || i_0_ > Voice.cur().SpeakBombersUnderAttack1[i] || i_0_ > Voice.cur().SpeakBombersUnderAttack2[i]) {
                        Aircraft aircraft_1_ = War.getNearestEnemy(aircraft, 6000.0F);
                        if (aircraft_1_ != null && (aircraft_1_ instanceof TypeFighter || aircraft_1_ instanceof TypeStormovik)) Voice.speakBombersUnderAttack(aircraft, false);
                    }
                    if (i_0_ > Voice.cur().SpeakNearBombers[i]) {
                        Aircraft aircraft_2_ = War.getNearestFriendlyFighter(aircraft, 4000.0F);
                        if (aircraft_2_ != null) Voice.speakNearBombers(aircraft);
                    }
                }
            }
        }
        if (World.cur().diffCur.Wind_N_Turbulence && !this.Gears.onGround() && Time.current() > this.w + 50L) World.wind().getVectorAI(this.Loc, this.Vwind);
        else this.Vwind.set(0.0, 0.0, 0.0);
        this.Vair.sub(this.Vwld, this.Vwind);
        this.Or.transformInv(this.Vair, this.Vflow);
        this.Density = Atmosphere.density((float) this.Loc.z);
        this.AOA = RAD2DEG(-(float) Math.atan2(this.Vflow.z, this.Vflow.x));
        this.AOS = RAD2DEG((float) Math.atan2(this.Vflow.y, this.Vflow.x));
        this.V2 = (float) this.Vflow.lengthSquared();
        this.V = (float) Math.sqrt(this.V2);
        this.Mach = this.V / Atmosphere.sonicSpeed((float) this.Loc.z);
        if (this.Mach > 0.8F) this.Mach = 0.8F;
        this.Kq = 1.0F / (float) Math.sqrt(1.0F - this.Mach * this.Mach);
        this.q_ = this.Density * this.V2 * 0.5F;
        if (this.callSuperUpdate) {
            double d = this.Loc.z - this.Gears.screenHQ;
            if (d < 0.0) d = 0.0;
            this.Cw.x = -this.q_ * (this.Wing.new_Cx(this.AOA) + 2.0F * this.GearCX * this.CT.getGear() + 2.0F * this.Sq.dragAirbrakeCx * this.CT.getAirBrake());
            this.Cw.z = this.q_ * this.Wing.new_Cy(this.AOA) * this.Kq;
            if (this.fmsfxCurrentType != 0 && this.fmsfxCurrentType == 1) this.Cw.z *= Aircraft.cvt(this.fmsfxPrevValue, 0.0030F, 0.8F, 1.0F, 0.0F);
            if (d < 0.4 * this.Length) {
                double d_3_ = 1.0 - d / (0.4 * this.Length);
                double d_4_ = 1.0 + 0.3 * d_3_;
                double d_5_ = 1.0 + 0.3 * d_3_;
                this.Cw.z *= d_4_;
                this.Cw.x *= d_5_;
            }
            this.Cw.y = -this.q_ * this.Wing.new_Cz(this.AOS);
            for (int i = 0; i < this.EI.getNum(); i++)
                this.Cw.x += -this.q_ * (0.8F * this.Sq.dragEngineCx[i]);
            this.Fw.scale(this.Sq.liftWingLIn + this.Sq.liftWingLMid + this.Sq.liftWingLOut + this.Sq.liftWingRIn + this.Sq.liftWingRMid + this.Sq.liftWingROut, this.Cw);
            this.Fw.x -= this.q_ * (this.Sq.dragParasiteCx + this.Sq.dragProducedCx);
            this.AF.set(this.Fw);
            this.AF.y -= this.AOS * this.q_ * 0.1F;
            if (isNAN(this.AF)) {
                this.AF.set(0.0, 0.0, 0.0);
                this.flutter();
            } else if (this.AF.length() > this.Gravity * 50.0F) {
                this.AF.normalize();
                this.AF.scale(this.Gravity * 50.0F);
                this.flutter();
            }
            this.AM.set(0.0, 0.0, 0.0);
            this.Wtrue.set(0.0, 0.0, 0.0);
            this.AM.x = (this.Sq.liftWingLIn * this.Arms.WING_ROOT + this.Sq.liftWingLMid * this.Arms.WING_MIDDLE + this.Sq.liftWingLOut * this.Arms.WING_END - this.Sq.liftWingRIn * this.Arms.WING_ROOT - this.Sq.liftWingRMid * this.Arms.WING_MIDDLE
                    - this.Sq.liftWingROut * this.Arms.WING_END) * this.Cw.z;
            if (this.fmsfxCurrentType != 0) {
                if (this.fmsfxCurrentType == 2) {
                    this.AM.x = (-this.Sq.liftWingRIn * this.Arms.WING_ROOT - this.Sq.liftWingRMid * this.Arms.WING_MIDDLE - this.Sq.liftWingROut * this.Arms.WING_END) * this.Cw.z;
                    if (Time.current() >= this.fmsfxTimeDisable) this.doRequestFMSFX(0, 0);
                }
                if (this.fmsfxCurrentType == 3) {
                    this.AM.x = (this.Sq.liftWingLIn * this.Arms.WING_ROOT + this.Sq.liftWingLMid * this.Arms.WING_MIDDLE + this.Sq.liftWingLOut * this.Arms.WING_END) * this.Cw.z;
                    if (Time.current() >= this.fmsfxTimeDisable) this.doRequestFMSFX(0, 0);
                }
            }
            if (Math.abs(this.AOA) > 33.0F) this.AM.y = 1.0F * (this.Sq.liftStab * this.Arms.HOR_STAB) * (this.q_ * this.Tail.new_Cy(this.AOA) * this.Kq);
            if (Math.abs(this.AOS) > 33.0F) this.AM.z = 1.0F * ((0.2F + this.Sq.liftKeel) * this.Arms.VER_STAB) * (this.q_ * this.Tail.new_Cy(this.AOS) * this.Kq);
            float f_6_ = this.Sq.liftWingLIn + this.Sq.liftWingLMid + this.Sq.liftWingLOut;
            float f_7_ = this.Sq.liftWingRIn + this.Sq.liftWingRMid + this.Sq.liftWingROut;
            float f_8_ = (float) this.Vflow.lengthSquared() - 120.0F;
            if (f_8_ < 0.0F) f_8_ = 0.0F;
            if (this.Vflow.x < 0.0) f_8_ = 0.0F;
            if (f_8_ > 15000.0F) f_8_ = 15000.0F;
            if (((Maneuver) ((Aircraft) this.actor).FM).get_maneuver() != 20) {
                float f_9_ = f_6_ - f_7_;
                if (!this.getOp(19) && d > 20.0 && f_8_ > 10.0F) {
                    this.AM.y += 5.0F * this.Sq.squareWing * this.Vflow.x;
                    this.AM.z += 80.0F * this.Sq.squareWing * this.EI.getPropDirSign();
                    if (this.AOA > 20.0F || this.AOA < -20.0F) {
                        float f_10_ = 1.0F;
                        if (this.W.z < 0.0) f_10_ = -1.0F;
                        this.AM.z += 30.0F * f_10_ * this.Sq.squareWing * (3.0 * this.Vflow.z + this.Vflow.x);
                        this.AM.x -= 50.0F * f_10_ * this.Sq.squareWing * (this.Vflow.z + 3.0 * this.Vflow.x);
                    }
                } else {
                    if (!this.Gears.onGround()) {
                        float f_11_ = this.AOA * 3.0F;
                        if (f_11_ > 25.0F) f_11_ = 25.0F;
                        if (f_11_ < -25.0F) f_11_ = -25.0F;
                        if (!this.getOp(34)) this.AM.x -= f_11_ * f_7_ * f_8_;
                        else if (!this.getOp(37)) this.AM.x += f_11_ * f_6_ * f_8_;
                        else if (((Maneuver) ((Aircraft) this.actor).FM).get_maneuver() == 44 && (this.AOA > 15.0F || this.AOA < -12.0F)) {
                            if (f_9_ > 0.0F && this.W.z > 0.0) this.W.z = -9.999999747378752E-5;
                            if (f_9_ < 0.0F && this.W.z < 0.0) this.W.z = 9.999999747378752E-5;
                            if (f_8_ > 1000.0F) f_8_ = 1000.0F;
                            if (this.W.z < 0.0) {
                                this.AM.z -= 3.0F * this.Sq.squareWing * f_8_;
                                if (this.AOA > 0.0F) this.AM.x += 40.0F * this.Sq.squareWing * f_8_;
                                else this.AM.x -= 40.0F * this.Sq.squareWing * f_8_;
                            } else {
                                this.AM.z += 3.0F * this.Sq.squareWing * f_8_;
                                if (this.AOA > 0.0F) this.AM.x -= 40.0F * this.Sq.squareWing * f_8_;
                                else this.AM.x += 40.0F * this.Sq.squareWing * f_8_;
                            }
                        }
                    }
                    if (this.Sq.liftKeel > 0.1F) this.AM.z += this.AOS * this.q_ * 0.5F;
                    else this.AM.x += this.AOS * this.q_ * 1.0F;
                    double d_12_ = 1.0;
                    if (d < 1.5 * this.Length) {
                        d_12_ += (d - 1.5 * this.Length) / this.Length;
                        if (d_12_ < 0.0) d_12_ = 0.0;
                    }
                    if (this.Vflow.x < 20.0 && Math.abs(this.AOS) < 33.0F) this.AM.y += d_12_ * this.AF.z;
                    float f_13_ = (float) this.Vflow.x;
                    if (f_13_ > 150.0F) f_13_ = 150.0F;
                    float f_14_ = this.SensYaw;
                    if (f_14_ > 0.2F) f_14_ = 0.2F;
                    float f_15_ = this.SensRoll;
                    if (f_15_ > 4.0F) f_15_ = 4.0F;
                    double d_16_ = 20.0 - Math.abs(this.AOA);
                    if (d_16_ < this.minElevCoeff) d_16_ = this.minElevCoeff;
                    double d_17_ = this.AOA - this.CT.getElevator() * d_16_;
                    double d_18_ = 0.017 * Math.abs(this.AOA);
                    if (d_18_ < 1.0) d_18_ = 1.0;
                    if (d_17_ > 90.0) d_17_ = -(180.0 - d_17_);
                    if (d_17_ < -90.0) d_17_ = 180.0 - d_17_;
                    d_17_ *= d_18_;
                    double d_19_ = 12.0 - Math.abs(this.AOS);
                    if (d_19_ < 0.0) d_19_ = 0.0;
                    double d_20_ = this.AOS - this.CT.getRudder() * d_19_;
                    double d_21_ = 0.01 * Math.abs(this.AOS);
                    if (d_21_ < 1.0) d_21_ = 1.0;
                    if (d_20_ > 90.0) d_20_ = -(180.0 - d_20_);
                    if (d_20_ < -90.0) d_20_ = 180.0 - d_20_;
                    d_20_ *= d_21_;
                    float f_22_ = this.Sq.squareWing;
                    if (f_22_ < 1.0F) f_22_ = 1.0F;
                    f_22_ = 1.0F / f_22_;
                    this.Wtrue.x = this.CT.getAileron() * f_13_ * f_15_ * this.Sq.squareAilerons * f_22_ * 1.0F;
                    this.Wtrue.y = d_17_ * f_13_ * this.SensPitch * this.Sq.squareElevators * f_22_ * 0.012000000104308128;
                    this.Wtrue.z = d_20_ * f_13_ * f_14_ * this.Sq.squareRudders * f_22_ * 0.15000000596046448;
                }
            } else {
                float f_23_ = 1.0F;
                if (this.W.z < 0.0) f_23_ = -1.0F;
                this.AM.z += 30.0F * f_23_ * this.Sq.squareWing * (3.0 * this.Vflow.z + this.Vflow.x);
                this.AM.x -= 50.0F * f_23_ * this.Sq.squareWing * (this.Vflow.z + 3.0 * this.Vflow.x);
            }
            if (this.Sq.squareElevators < 0.1F && d > 20.0 && f_8_ > 10.0F) this.AM.y += this.Gravity * 0.4F;
            this.AM.add(this.producedAM);
            this.AF.add(this.producedAF);
            this.producedAM.set(0.0, 0.0, 0.0);
            this.producedAF.set(0.0, 0.0, 0.0);
            this.AF.add(this.EI.producedF);
            if (this.W.lengthSquared() > 36.0) this.W.scale(6.0 / this.W.length());
            double d_24_ = 0.1 + this.Sq.squareWing;
            if (d_24_ < 1.0) d_24_ = 1.0;
            d_24_ = 1.0 / d_24_;
            this.W.x *= 1.0 - 0.12 * (0.2 + f_6_ + f_7_) * d_24_;
            this.W.y *= 1.0 - 0.5 * (0.2 + this.Sq.liftStab) * d_24_;
            this.W.z *= 1.0 - 0.5 * (0.2 + this.Sq.liftKeel) * d_24_;
            this.GF.set(0.0, 0.0, 0.0);
            this.GM.set(0.0, 0.0, 0.0);
            this.Gears.roughness = 0.5;
            this.Gears.ground(this, true);
            this.GM.x *= 0.1;
            this.GM.y *= 0.4;
            this.GM.z *= 0.8;
            int i = 2;
            if (this.GF.lengthSquared() == 0.0 && this.GM.lengthSquared() == 0.0 || this.brakeShoe) i = 1;
            this.SummF.add(this.AF, this.GF);
            this.ACmeter.set(this.SummF);
            this.ACmeter.scale(1.0F / this.Gravity);
            this.TmpV.set(0.0, 0.0, -this.Gravity);
            this.Or.transformInv(this.TmpV);
            this.GF.add(this.TmpV);
            this.SummF.add(this.AF, this.GF);
            this.SummM.add(this.AM, this.GM);
            double d_25_ = 1.0 / this.M.mass;
            this.LocalAccel.scale(d_25_, this.SummF);
            if (isNAN(this.AM)) {
                this.AM.set(0.0, 0.0, 0.0);
                this.flutter();
            } else if (this.AM.length() > this.Gravity * 100.0F) {
                this.AM.normalize();
                this.AM.scale(this.Gravity * 100.0F);
                this.flutter();
            }
            this.dryFriction -= 0.01;
            if (this.Gears.gearsChanged) this.dryFriction = 1.0F;
            if (this.Gears.nOfPoiOnGr > 0) this.dryFriction += 0.02F;
            if (this.dryFriction < 1.0F) this.dryFriction = 1.0F;
            if (this.dryFriction > 32.0F) this.dryFriction = 32.0F;
            float f_26_ = 4.0F * (0.25F - this.EI.getPowerOutput());
            if (f_26_ < 0.0F) f_26_ = 0.0F;
            f_26_ *= f_26_;
            f_26_ *= this.dryFriction;
            float f_27_ = f_26_ * this.M.mass * this.M.mass;
            if (!this.brakeShoe && (this.Gears.nOfPoiOnGr == 0 && this.Gears.nOfGearsOnGr < 3 || f_26_ == 0.0F || this.SummM.lengthSquared() > 2.0F * f_27_ || this.SummF.lengthSquared() > 80.0F * f_27_ || this.W.lengthSquared() > 1.4E-4F * f_26_
                    || this.Vwld.lengthSquared() > 0.09F * f_26_)) {
                double d_28_ = 1.0 / i;
                for (int i_29_ = 0; i_29_ < i; i_29_++) {
                    this.SummF.add(this.AF, this.GF);
                    this.SummM.add(this.AM, this.GM);
                    this.AW.x = ((this.J.y - this.J.z) * this.W.y * this.W.z + this.SummM.x) / this.J.x;
                    this.AW.y = ((this.J.z - this.J.x) * this.W.z * this.W.x + this.SummM.y) / this.J.y;
                    this.AW.z = ((this.J.x - this.J.y) * this.W.x * this.W.y + this.SummM.z) / this.J.z;
                    this.TmpV.scale(d_28_ * f, this.AW);
                    this.W.add(this.TmpV);
                    this.Or.transform(this.W, this.Vn);
                    this.Wtrue.add(this.W);
                    this.TmpV.scale(d_28_ * f, this.Wtrue);
                    this.Or.increment((float) -RAD2DEG(this.TmpV.z), (float) -RAD2DEG(this.TmpV.y), (float) RAD2DEG(this.TmpV.x));
                    this.Or.transformInv(this.Vn, this.W);
                    this.TmpV.scale(d_25_, this.SummF);
                    this.Or.transform(this.TmpV);
                    this.Accel.set(this.TmpV);
                    this.TmpV.scale(d_28_ * f);
                    this.Vwld.add(this.TmpV);
                    this.TmpV.scale(d_28_ * f, this.Vwld);
                    this.TmpP.set(this.TmpV);
                    this.Loc.add(this.TmpP);
                    if (isNAN(this.Loc)) {
//						boolean bool = false;
                    }
                    this.GF.set(0.0, 0.0, 0.0);
                    this.GM.set(0.0, 0.0, 0.0);
                    if (i_29_ < i - 1) {
                        this.Gears.ground(this, true);
                        this.GM.x *= 0.1;
                        this.GM.y *= 0.4;
                        this.GM.z *= 0.8;
                        this.TmpV.set(0.0, 0.0, -this.Gravity);
                        this.Or.transformInv(this.TmpV);
                        this.GF.add(this.TmpV);
                    }
                }
                for (int i_30_ = 0; i_30_ < 3; i_30_++) {
                    this.Gears.gWheelAngles[i_30_] = (this.Gears.gWheelAngles[i_30_] + (float) Math.toDegrees(Math.atan(this.Gears.gVelocity[i_30_] * f / 0.375))) % 360.0F;
                    this.Gears.gVelocity[i_30_] *= 0.949999988079071;
                }
                this.HM.chunkSetAngles("GearL1_D0", 0.0F, -this.Gears.gWheelAngles[0], 0.0F);
                this.HM.chunkSetAngles("GearR1_D0", 0.0F, -this.Gears.gWheelAngles[1], 0.0F);
                this.HM.chunkSetAngles("GearC1_D0", 0.0F, -this.Gears.gWheelAngles[2], 0.0F);
            }
        }
    }
}
