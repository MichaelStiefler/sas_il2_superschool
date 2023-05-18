package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.BaseGameVersion;
import com.maddox.sas1946.il2.util.CommonTools;
import com.maddox.sas1946.il2.util.Reflection;

public abstract class JU_288 extends Scheme2a implements TypeBomber, TypeDiveBomber, TypeTransport {

    public JU_288() {
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
        this.fSightCurReadyness = 0.0F;
        this.fDiveRecoveryAlt = 1000.0F;
        this.fDiveVelocity = 400.0F;
        this.aiStartPropPitchSet = false;
        this.aiStartMixtureSet = false;
        this.steering = 0F;
        this.isAiBombDrop = false;
        this.closeAiBombBay = -1L;
        this.flapTarget = 0F;
        this.flapPos = new float[] { 0F, 0F, 0F, 0F };
        this.flapTargetPos = new float[] { 0F, 0F, 0F, 0F };
        this.Cy0_1 = 0F;
        this.diveMechStage = 0;
        this.bNDives = false;
        this.bDropsBombs = false;
        this.groundPulse = -1L;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (BaseGameVersion.is410orLater())
            this.FM.AS.wantBeaconsNet(true);
        if (BaseGameVersion.is411orLater())
            this.Cy0_1 = this.FM.getWing().Cy0_1;
        else
            this.Cy0_1 = ((Polares) Reflection.getValue(this.FM, "Wing")).Cy0_1;
    }
    
    public void setOnGround(Point3d p, Orient o, Vector3d v)
    {
        super.setOnGround(p, o, v);
        this.groundPulse = Time.current() + 1000L;
    }

    public void update(float f) {
        this.updateDiveBomber();
        this.checkAiBombDrop();
        super.update(f);
        this.setAiControls();
        this.updateFlap();
        
        // Apply a couple of virtual "gun pulses" so the plane's suspension can settle properly on ground spawn.
        if (this.groundPulse != -1L) {
            if (Time.current() < this.groundPulse)
                this.FM.gunPulse(new Vector3d(300.0D, 0.0D, 0.0D));
            else
                this.groundPulse = -1L;
        }
    }
    
    private void setAiControls() {
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
            return;
        }
        if (!(this.FM instanceof Maneuver)) {
            return;
        }
        Maneuver maneuver = (Maneuver) this.FM;
        if ((maneuver.get_maneuver() == Maneuver.LANDING) && (maneuver.Alt < 60.0F)) {
            this.FM.CT.StabilizerControl = false;
            if (maneuver.Or.getTangage() > 6.5F) {
                maneuver.Or.increment(0.0F, -(maneuver.Or.getTangage() - 6.5F), 0.0F);
            }
        } else if ((maneuver.get_maneuver() == Maneuver.TAKEOFF) && (this.FM.getSpeedKMH() > 10F) && (this.FM.getSpeedKMH() < 230F) && (this.FM.Gears.onGround() || this.FM.Gears.nearGround())) {
            Reflection.setFloat(this.FM.CT, "Elevators", this.FM.CT.ElevatorControl = CommonTools.cvt(this.FM.getSpeedKMH(), 180F, 230F, 0F, this.FM.CT.ElevatorControl));
        }
        if (maneuver.get_maneuver() == Maneuver.TAKEOFF) {
            if ((this.FM.CT.getStepControl() < 1.0F) || !this.aiStartPropPitchSet) {
                this.aiStartPropPitchSet = true;
                this.FM.CT.setStepControl(1.0F);
            }
            if ((this.FM.CT.getMixControl() < 1.2F) || !this.aiStartMixtureSet) {
                this.aiStartMixtureSet = true;
                this.FM.CT.setMixControl(1.2F);
            }
            this.FM.CT.StabilizerControl = false;
        }
    }

    private void checkAiBombDrop() {
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
            return;
        }
        if (!(this.FM instanceof Maneuver)) {
            return;
        }
        // +++ Make sure AI always drops the full bomb load
        if (this.FM.CT.WeaponControl[3] || this.FM.CT.saveWeaponControl[3]) {
            this.isAiBombDrop = true;
        } else if (this.isAiBombDrop) {
            if (this.countBombs() == 0) {
                this.isAiBombDrop = false;
                this.closeAiBombBay = Time.current() + 3000L;
            } else {
                this.FM.CT.BayDoorControl = 1F;
                ((Maneuver) this.FM).bombsOut = true;
            }
        }
        if ((this.closeAiBombBay > 0) && (Time.current() > this.closeAiBombBay)) {
            this.FM.CT.BayDoorControl = 0F;
            this.closeAiBombBay = -1L;
        }
        // ---
    }

    void updateGears() {
        double l02 = 0.2415D;
        double alpha0 = 0.5759586531581287D; // Math.toRadians(33D);
        double s0 = 0.28795573521988127D; // l02/Math.cos(alpha0);
        
        float gearL, gearR, gearC;
        if (gearsSeparated) {
            gearL = this.FM.CT.getGearL();
            gearR = this.FM.CT.getGearR();
            gearC = this.FM.CT.getGearC();
        } else {
            gearL = gearR = gearC = this.FM.CT.getGear();
        }


        if (gearL > 0.98F) {
            this.resetYPRmodifier();
            // Calculate sink value for left gear.
            float lSink = Aircraft.xyz[0] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.48F, 0.0F, 0.48F);
            this.hierMesh().chunkSetLocate("GearL4b_D0", Aircraft.xyz, Aircraft.ypr);
            double l1 = l02 - (lSink / 2D);
            if (l1 < 0D) {
                l1 = 0D;
            }
            double alpha1 = Math.acos(l1 / s0) - alpha0;
            this.hierMesh().chunkSetAngles("GearL4c_D0", 0F, (float) Math.toDegrees(alpha1), 0F);
            this.hierMesh().chunkSetAngles("GearL4d_D0", 0F, -(float) Math.toDegrees(alpha1), 0F);
        }
        if (gearR > 0.98F) {
            this.resetYPRmodifier();
            // Calculate sink value for left gear.
            float rSink = Aircraft.xyz[0] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.48F, 0.0F, 0.48F);
            this.hierMesh().chunkSetLocate("GearR4b_D0", Aircraft.xyz, Aircraft.ypr);
            double l1 = l02 - (rSink / 2D);
            if (l1 < 0D) {
                l1 = 0D;
            }
            double alpha1 = Math.acos(l1 / s0) - alpha0;
            this.hierMesh().chunkSetAngles("GearR4c_D0", 0F, (float) Math.toDegrees(alpha1), 0F);
            this.hierMesh().chunkSetAngles("GearR4d_D0", 0F, -(float) Math.toDegrees(alpha1), 0F);
        }

        if (gearC > 0.98F) {
            this.resetYPRmodifier();
            // Calculate sink value for tail gear.
            Aircraft.xyz[0] = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.2F, 0.0F, 0.2F);
            Aircraft.ypr[2] = Aircraft.cvt(this.steering, -90F, 90F, 90F, -90F);
            this.hierMesh().chunkSetLocate("GearC2b_D0", Aircraft.xyz, Aircraft.ypr);
        } else {
            this.steering *= 0.99F;
            this.resetYPRmodifier();
            float fC = CommonTools.smoothCvt(gearC, 0F, 1F, 0F, 1F);
            Aircraft.xyz[0] = Aircraft.cvt(fC, 0.4F, 0.7F, 0.15F, 0.0F);
            Aircraft.ypr[2] = Aircraft.cvt(this.steering, -90F, 90F, 90F, -90F);
            this.hierMesh().chunkSetLocate("GearC2b_D0", Aircraft.xyz, Aircraft.ypr);
        }
    }

    public void moveSteering(float f) {
        float gearC;
        if (gearsSeparated) {
            gearC = this.FM.CT.getGearC();
        } else {
            gearC = this.FM.CT.getGear();
        }
        if (gearC < 0.98F) {
            return;
        } else {
            this.steering = f;
            this.updateGears();
        }
    }

    public void moveWheelSink() {
        this.updateGears();
    }

    public static void moveGear(HierMesh hiermesh, float leftGear, float rightGear, float tailGear) {
        float fL = CommonTools.smoothCvt(leftGear, 0F, 1F, 0F, 1F);

        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(fL, 0.1F, 0.99F, 0F, 90.5F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_Base", 0.0F, Aircraft.cvt(fL, 0.2F, 0.99F, 0F, 182.5F), 0.0F);
        hiermesh.chunkSetAngles("GL", 0.0F, 0.0F, Aircraft.cvt(fL, 0.25F, 0.99F, 0F, -50F));
        if (fL > 0.5F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(fL, 0.5F, 0.99F, -30F, -140F), 0.0F);
            hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(fL, 0.8F, 0.99F, -60F, 0F), 0.0F);
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, Aircraft.cvt(fL, 0.8F, 0.99F, 60F, 0F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(fL, 0.1F, 0.5F, 0F, -30F), 0.0F);
            hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(fL, 0.01F, 0.2F, 0F, -60F), 0.0F);
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, Aircraft.cvt(fL, 0.01F, 0.2F, 0F, 60F), 0.0F);
        }

        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.01F, 0.5F, 0F, 50F));
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.01F, 0.5F, 0F, -50F));
        hiermesh.chunkSetAngles("GearL7b_D0", 0.0F, Aircraft.cvt(fL, 0.8F, 0.99F, 0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearL8b_D0", 0.0F, Aircraft.cvt(fL, 0.8F, 0.99F, 0F, 75F), 0.0F);

        float fR = CommonTools.smoothCvt(rightGear, 0F, 1F, 0F, 1F);

        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(fR, 0.1F, 0.99F, 0F, 90.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_Base", 0.0F, Aircraft.cvt(fR, 0.2F, 0.99F, 0F, 182.5F), 0.0F);
        hiermesh.chunkSetAngles("GR", 0.0F, 0.0F, Aircraft.cvt(fR, 0.25F, 0.99F, 0F, -50F));
        if (fR > 0.5F) {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(fR, 0.5F, 0.99F, -30F, -140F), 0.0F);
            hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(fR, 0.8F, 0.99F, 60F, 0F), 0.0F);
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(fR, 0.8F, 0.99F, -60F, 0F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(fR, 0.1F, 0.5F, 0F, -30F), 0.0F);
            hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(fR, 0.01F, 0.2F, 0F, 60F), 0.0F);
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(fR, 0.01F, 0.2F, 0F, -60F), 0.0F);
        }

        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.01F, 0.5F, 0F, 50F));
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.01F, 0.5F, 0F, -50F));
        hiermesh.chunkSetAngles("GearR7b_D0", 0.0F, Aircraft.cvt(fR, 0.8F, 0.99F, 0F, 75F), 0.0F);
        hiermesh.chunkSetAngles("GearR8b_D0", 0.0F, Aircraft.cvt(fR, 0.8F, 0.99F, 0F, -75F), 0.0F);

        float fC = CommonTools.smoothCvt(tailGear, 0F, 1F, 0F, 1F);

        hiermesh.chunkSetAngles("GearC99_D0", 0.0F, Aircraft.cvt(fC, 0.2F, 0.99F, 0F, 85), 0.0F);
        Aircraft.xyz[0] = Aircraft.cvt(fC, 0.4F, 0.7F, 0.15F, 0.0F);
        Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0F;
        hiermesh.chunkSetLocate("GearC2b_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(fC, 0.01F, 0.3F, 0F, -125F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(fC, 0.01F, 0.3F, 0F, 125F), 0.0F);

    }

    protected void moveGear(float leftGearPos, float rightGearPos, float frontGearPos) {
        moveGear(this.hierMesh(), leftGearPos, rightGearPos, frontGearPos);
    }

    public static void moveGear(HierMesh hiermesh, float gearPos) {
        moveGear(hiermesh, gearPos, gearPos, gearPos);
    }

    protected void moveGear(float gearPos) {
        moveGear(this.hierMesh(), gearPos);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -85F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 85F * f, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3) {
                if ((this.FM.AS.astateTankStates[1] < 4) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.FM.AS.hitTank(this, 1, 1);
                }
                if ((this.FM.getSpeedKMH() > 200F) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.nextDMGLevel("Keel1_D0", 0, this);
                }
                if ((this.FM.getSpeedKMH() > 200F) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.nextDMGLevel("StabL_D0", 0, this);
                }
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.nextDMGLevel("WingLIn_D0", 0, this);
                }
            }
            if (this.FM.AS.astateEngineStates[1] > 3) {
                if ((this.FM.AS.astateTankStates[2] < 4) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.FM.AS.hitTank(this, 2, 1);
                }
                if ((this.FM.getSpeedKMH() > 200F) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.nextDMGLevel("Keel2_D0", 0, this);
                }
                if ((this.FM.getSpeedKMH() > 200F) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.nextDMGLevel("StabR_D0", 0, this);
                }
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.nextDMGLevel("WingRIn_D0", 0, this);
                }
            }
        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask3_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
            this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float turretYaw = -af[0];
        float turretPitch = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (turretYaw < -90F) {
                    turretYaw = -90F;
                    flag = false;
                }
                if (turretYaw > 90F) {
                    turretYaw = 90F;
                    flag = false;
                }
                if (turretPitch < 0F) {
                    turretPitch = 0F;
                    flag = false;
                }
                if (turretPitch > 86) {
                    turretPitch = 86;
                    flag = false;
                }

                if ((Math.abs(turretYaw) > 15F) && (Math.abs(turretYaw) < 20F) && (turretPitch < 6.5F)) {
                    flag = false;
                }
                if ((Math.abs(turretYaw) > 50F) && (turretPitch < 5F)) {
                    flag = false;
                }
                if ((Math.abs(turretYaw) > 70F) && (turretPitch < 15F)) {
                    flag = false;
                }

                break;

            case 1:
                if (turretYaw < -90F) {
                    turretYaw = -90F;
                    flag = false;
                }
                if (turretYaw > 90F) {
                    turretYaw = 90F;
                    flag = false;
                }
                if (turretPitch < -86F) {
                    turretPitch = -86F;
                    flag = false;
                }
                if (turretPitch > 0F) {
                    turretPitch = 0F;
                    flag = false;
                }

                float gearC;
                if (gearsSeparated) {
                    gearC = this.FM.CT.getGearC();
                } else {
                    gearC = this.FM.CT.getGear();
                }
                if ((gearC > 0.02F) && (Math.abs(turretYaw) < 10F) && (turretPitch > -20F)) {
                    flag = false;
                }

                break;
        }
        af[0] = -turretYaw;
        af[1] = turretPitch;
        return flag;
    }

    public void doWoundPilot(int i, float f) {
        if (i < 1 || i > 2) return;
        if (BaseGameVersion.is410orLater()) {
            this.FM.turret[i-1].setHealth(f);
        } else {
            if (f < 0.01F) this.FM.turret[i-1].bIsOperable = false;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Head1_D0", false);
            case 1:
            case 2:
                this.hierMesh().chunkVisible("Pilot" + (i+1) + "_D0", false);
                this.hierMesh().chunkVisible("HMask" + (i+1) + "_D0", false);
                this.hierMesh().chunkVisible("Pilot" + (i+1) + "_D1", true);
                break;
        }
    }

    protected void moveAileron(float f) {
        if (f >= 0.0F) {
            this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -20F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -27F * f, 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -27F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
        }
    }

    protected void moveElevator(float f) {
        if (f >= 0.0F) {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -31F * f, 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -31F * f, 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -18F * f, 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -18F * f, 0.0F);
        }
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -25F * f, 0.0F);
    }

    private boolean hasLeftDropTank() {
        if (!this.thisWeaponsName.endsWith("_ft")) {
            return false;
        }
        if (this.FM.CT.Weapons.length < 10) {
            return false;
        }
        if (this.FM.CT.Weapons[9].length < 4) {
            return false;
        }
        return this.FM.CT.Weapons[9][1].haveBullets();
    }

    private boolean hasRightDropTank() {
        if (!this.thisWeaponsName.endsWith("_ft")) {
            return false;
        }
        if (this.FM.CT.Weapons.length < 10) {
            return false;
        }
        if (this.FM.CT.Weapons[9].length < 4) {
            return false;
        }
        return this.FM.CT.Weapons[9][1].haveBullets();
    }

    private float clamp01(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        } else if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    protected void moveFlap(float f) {
        this.flapTarget = this.clamp01(f);
    }

    void updateFlap() {
        for (int i = 0; i < 4; i++) {
            this.flapTargetPos[i] = -50F * this.flapTarget;
        }

        float Cy0_1_factor = 1F;
        if (this.hasLeftDropTank() && (this.flapTargetPos[0] < -25F)) {
            Cy0_1_factor -= Aircraft.cvt(this.flapTargetPos[0], -25.01F, -49.99F, 0F, 0.125F);
            this.flapTargetPos[0] = -25;
        }
        if (this.hasRightDropTank() && (this.flapTargetPos[3] < -25F)) {
            Cy0_1_factor -= Aircraft.cvt(this.flapTargetPos[3], -25.01F, -49.99F, 0F, 0.125F);
            this.flapTargetPos[3] = -25;
        }

        if (this.hasLeftDropTank() && (this.FM.CT.FlapsControl > 0.6F)) {
            Cy0_1_factor -= 0.125F;
        }
        if (this.hasRightDropTank() && (this.FM.CT.FlapsControl > 0.6F)) {
            Cy0_1_factor -= 0.125F;
        }
        if (BaseGameVersion.is411orLater())
            this.FM.getWing().Cy0_1 = this.Cy0_1 * Cy0_1_factor;
        else
            this.Cy0_1 = ((Polares) Reflection.getValue(this.FM, "Wing")).Cy0_1 = this.Cy0_1 * Cy0_1_factor;

        float smoothFactor = 20F;
        for (int i = 0; i < 4; i++) {
            this.flapPos[i] = ((this.flapPos[i] * (smoothFactor - 1F)) + this.flapTargetPos[i]) / smoothFactor;
            this.hierMesh().chunkSetAngles("Flap0" + (i + 1) + "_D0", 0.0F, this.flapPos[i], 0.0F);
        }

    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith(".")) {
            s = s.substring(1); // remove leading dot "." in collision mesh names
        }
        if (s.startsWith("xx")) {
            if (s.startsWith("xxeng1")) {
                this.hitEngine(s, shot, point3d, 0);
                return;
            }
            if (s.startsWith("xxeng2")) {
                this.hitEngine(s, shot, point3d, 1);
                return;
            }
            if (s.startsWith("xxArmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                } else if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                } else if (s.endsWith("p4")) {
                    this.getEnergyPastArmor(9D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 3:
                        if ((this.getEnergyPastArmor(4.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        if ((this.getEnergyPastArmor(0.002F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.11F)) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;

                    case 4:
                    case 5:
                        if ((this.getEnergyPastArmor(0.99F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Out..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxbomb")) {
                if ((World.Rnd().nextFloat() < 0.01F) && (this.FM.CT.Weapons[3] != null) && this.FM.CT.Weapons[3][0].haveBullets()) {
                    this.debuggunnery("*** Bomb Payload Detonates..");
                    this.FM.AS.hitTank(shot.initiator, 0, 100);
                    this.FM.AS.hitTank(shot.initiator, 1, 100);
                    this.FM.AS.hitTank(shot.initiator, 2, 100);
                    this.FM.AS.hitTank(shot.initiator, 3, 100);
                    this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("prop") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F)) {
                    this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine1 Prop Governor Failed..");
                }
                if (s.endsWith("gear") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F)) {
                    this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine1 Prop Governor Damaged..");
                }
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 200000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Engine Damaged..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 28000F)) {
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        }
                        if (World.Rnd().nextFloat() < 0.08F) {
                            this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Engine Stalled..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 10);
                        Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                    }
                    this.getEnergyPastArmor(6F, shot);
                }
                if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.72F))) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine1 Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine1 Cylinder Case Broken - Engine Stuck..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine1 Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine1 Supercharger Out..");
                }
                if (s.endsWith("eqpt") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
                        Aircraft.debugprintln(this, "*** Engine1 Magneto Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine1 Compressor Feed Out..");
                    }
                }
                return;
            }
            if (s.startsWith("xxeng2")) {
                if (s.endsWith("prop") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F)) {
                    this.FM.EI.engines[1].setKillPropAngleDevice(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine2 Prop Governor Failed..");
                }
                if (s.endsWith("gear") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F)) {
                    this.FM.EI.engines[1].setKillPropAngleDeviceSpeeds(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine2 Prop Governor Damaged..");
                }
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 200000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 1, 2);
                            Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Engine Damaged..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 28000F)) {
                            this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[1].getCylindersOperable() + "/" + this.FM.EI.engines[1].getCylinders() + " Left..");
                        }
                        if (World.Rnd().nextFloat() < 0.08F) {
                            this.FM.EI.engines[1].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                        }
                        this.FM.EI.engines[1].setReadyness(shot.initiator, this.FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[1].getReadyness() + "..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[1].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Engine Stalled..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.AS.hitEngine(shot.initiator, 1, 10);
                        Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                    }
                    this.getEnergyPastArmor(6F, shot);
                }
                if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[1].getCylindersRatio() * 1.72F))) {
                    this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine2 Cylinders Hit, " + this.FM.EI.engines[1].getCylindersOperable() + "/" + this.FM.EI.engines[1].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[1].setEngineStuck(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine2 Cylinder Case Broken - Engine Stuck..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 1, 3);
                        Aircraft.debugprintln(this, "*** Engine2 Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    this.FM.EI.engines[1].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine2 Supercharger Out..");
                }
                if (s.endsWith("eqpt") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[1].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
                        Aircraft.debugprintln(this, "*** Engine2 Magneto Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[1].setKillCompressor(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine2 Compressor Feed Out..");
                    }
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr")) {
                    if ((s.startsWith("xxlockr1") || s.startsWith("xxlockr2")) && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                        this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                    }
                    if ((s.startsWith("xxlockr3") || s.startsWith("xxlockr4")) && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                        this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                    }
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvR") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxMgun0")) {
                int j = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
                    this.FM.AS.setJamBullets(0, j);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if (s.startsWith("xxoil1")) {
                if ((this.getEnergyPastArmor(0.25F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Radiator 1 Pierced..");
                }
                return;
            }
            if (s.startsWith("xxoil2")) {
                if ((this.getEnergyPastArmor(0.25F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitOil(shot.initiator, 1);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Radiator 2 Pierced..");
                }
                return;
            }
            if (s.startsWith("xxprib")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                this.getEnergyPastArmor(4.88F, shot);
                return;
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 48;
                switch (k) {
                    case 1:
                        this.doHitMeATank(shot, 1);
                        this.doHitMeATank(shot, 2);
                        break;

                    case 2:
                        this.doHitMeATank(shot, 1);
                        break;

                    case 3:
                        this.doHitMeATank(shot, 2);
                        break;

                    case 4:
                        this.doHitMeATank(shot, 0);
                        break;

                    case 5:
                        this.doHitMeATank(shot, 3);
                        break;
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(7.5F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.2F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            } else {
                if (s.startsWith("xxwater")) {

                }
                return;
            }
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            this.hitChunk("CF", shot);
            if (s.startsWith("xcockpit")) {
                if (point3d.x > 2.2D) {
                    if (World.Rnd().nextFloat() < 0.73F) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                    }
                } else if (point3d.y > 0.0D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                } else {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                }
                if (World.Rnd().nextFloat() < 0.125F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
                if (World.Rnd().nextFloat() < 0.125F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                }
            }
            return;
        }
        if (s.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose1") < 2) {
                this.hitChunk("Nose1", shot);
                if (World.Rnd().nextFloat() < 0.125F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                }
                if (World.Rnd().nextFloat() < 0.125F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                }
            }
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) {
                this.hitChunk("Keel2", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 1) {
                this.hitChunk("Rudder2", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr")) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.startsWith("xwingrmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.endsWith("2") && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.startsWith("xhead")) {
                l = s.charAt(5) - 49;
            } else if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else {
                byte0 = 2;
                l = s.charAt(6) - 49;
            }
            this.hitFlesh(l, shot, byte0);
        }
    }

    private final void doHitMeATank(Shot shot, int i) {
        if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
            if (shot.power < 14100F) {
                if (this.FM.AS.astateTankStates[i] == 0) {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    this.FM.AS.doSetTankState(shot.initiator, i, 1);
                }
                if ((this.FM.AS.astateTankStates[i] > 0) && ((World.Rnd().nextFloat() < 0.02F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.25F)))) {
                    this.FM.AS.hitTank(shot.initiator, i, 2);
                }
            } else {
                this.FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int) (shot.power / 56000F)));
            }
        }
    }

    protected void moveAirBrake(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.2F, 0F, -0.18F);
        this.hierMesh().chunkSetLocate("BL", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetLocate("BR", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.99F, 0F, 40F), 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.99F, 0F, 40F), 0.0F);
    }

    protected void moveFan(float f) {
        int i = 0;
        for (int j = 0; j < this.FM.EI.getNum(); j++) {
            if (this.oldProp[j] < 2) {
                i = Math.abs((int) (this.FM.EI.engines[j].getw() * 0.06F));
                if (i >= 1) {
                    i = 1;
                }
                if ((i != this.oldProp[j]) && (this.hierMesh().isChunkVisible(Aircraft.Props[j][this.oldProp[j]]))) {
                    this.hierMesh().chunkVisible(Aircraft.Props[j][this.oldProp[j]], false);
                    this.oldProp[j] = i;
                    this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
                }
            }
            if (i == 0) {
                this.propPos[j] = ((this.propPos[j] + (57.299999F * this.FM.EI.engines[j].getw() * f)) % 360.0F);
            } else {
                float posInc = 57.299999F * this.FM.EI.engines[j].getw();
                posInc %= 2880.0F;
                posInc /= 2880.0F;
                if (posInc <= 0.5F) {
                    posInc *= 2.0F;
                } else {
                    posInc = (posInc * 2.0F) - 2.0F;
                }
                posInc *= 1200.0F;
                this.propPos[j] = ((this.propPos[j] + (posInc * f)) % 360.0F);
            }
            this.hierMesh().chunkSetAngles(Aircraft.Props[j][0], 0.0F, this.propPos[j] * ((j % 2) == 0 ? 1F : -1F), 0.0F);
        }
    }

    protected void hitEngine(String s, Shot shot, Point3d point3d, int i) {
        if (s.endsWith("prop") && (this.getEnergyPastArmor(0.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
            this.FM.EI.engines[i].setKillPropAngleDevice(shot.initiator);
        }
        if (s.endsWith("base")) {
            if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                if (World.Rnd().nextFloat() < (shot.power / 140000F)) {
                    this.FM.AS.setEngineStuck(shot.initiator, i);
                    Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                }
                if (World.Rnd().nextFloat() < (shot.power / 85000F)) {
                    this.FM.AS.hitEngine(shot.initiator, i, 2);
                    Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                }
            } else if (World.Rnd().nextFloat() < 0.01F) {
                this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
            } else {
                this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.002F);
                Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[i].getReadyness() + "..");
            }
            this.getEnergyPastArmor(12F, shot);
        }
        if (s.endsWith("cyls")) {
            if ((this.getEnergyPastArmor(6.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[i].getCylindersRatio() * 0.75F))) {
                this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                    this.FM.AS.hitEngine(shot.initiator, i, 2);
                    Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                }
            }
            this.getEnergyPastArmor(25F, shot);
        }
        if (s.endsWith("supc")) {
            if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) {
                this.FM.EI.engines[i].setKillCompressor(shot.initiator);
            }
            this.getEnergyPastArmor(2.0F, shot);
        }
        if (s.endsWith("eqpt")) {
            if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                if ((Aircraft.Pd.y > 0.0D) && (Aircraft.Pd.z < 0.18899999558925629D) && (World.Rnd().nextFloat(0.0F, 16000F) < shot.power)) {
                    this.FM.EI.engines[i].setMagnetoKnockOut(shot.initiator, 0);
                }
                if ((Aircraft.Pd.y < 0.0D) && (Aircraft.Pd.z < 0.18899999558925629D) && (World.Rnd().nextFloat(0.0F, 16000F) < shot.power)) {
                    this.FM.EI.engines[i].setMagnetoKnockOut(shot.initiator, 1);
                }
                if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 4);
                }
                if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 0);
                }
                if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 6);
                }
                if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 1);
                }
            }
            this.getEnergyPastArmor(2.0F, shot);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 3:
                this.FM.AS.setEngineState(this, 0, 0);
                break;

            case 4:
                this.FM.AS.setEngineState(this, 1, 0);
                break;

            case 13:
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean typeBomberToggleAutomation() {
        this.bSightAutomation = (!this.bSightAutomation);
        this.bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
        return this.bSightAutomation;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 1.0F;
        if (this.fSightCurForwardAngle > 85.0F) {
            this.fSightCurForwardAngle = 85.0F;
        }
        this.fSightCurDistance = (this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle)));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 1.0F;
        if (this.fSightCurForwardAngle < 0.0F) {
            this.fSightCurForwardAngle = 0.0F;
        }
        this.fSightCurDistance = (this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle)));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.05F;
        if (this.fSightCurSideslip > 3.0F) {
            this.fSightCurSideslip = 3.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10.0F) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.05F;
        if (this.fSightCurSideslip < -3.0F) {
            this.fSightCurSideslip = -3.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10.0F) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 850.0F;
        this.typeDiveBomberAdjAltitudeReset();
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10.0F;
        if (this.fSightCurAltitude > 10000.0F) {
            this.fSightCurAltitude = 10000.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = (this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle)));
        this.typeDiveBomberAdjAltitudePlus();
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10.0F;
        if (this.fSightCurAltitude < 500.0F) {
            this.fSightCurAltitude = 500.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = (this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle)));
        this.typeDiveBomberAdjAltitudeMinus();
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 150.0F;
        this.typeDiveBomberAdjVelocityReset();
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 10.0F;
        if (this.fSightCurSpeed > 700.0F) {
            this.fSightCurSpeed = 700.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
        this.typeDiveBomberAdjVelocityPlus();
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 10.0F;
        if (this.fSightCurSpeed < 150.0F) {
            this.fSightCurSpeed = 150.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
        this.typeDiveBomberAdjVelocityMinus();
    }

    public void typeBomberUpdate(float paramFloat) {
//        this.checkAiBombDrop();
        if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
            this.fSightCurReadyness -= 0.0666666F * paramFloat;
            if (this.fSightCurReadyness < 0.0F) {
                this.fSightCurReadyness = 0.0F;
            }
        }
        if (this.fSightCurReadyness < 1.0F) {
            this.fSightCurReadyness += 0.0333333F * paramFloat;
        } else if (this.bSightAutomation) {
            this.fSightCurDistance -= (this.fSightCurSpeed / 3.6F) * paramFloat;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = ((float) Math.toDegrees(Math.atan(this.fSightCurDistance / this.fSightCurAltitude)));
            if (this.fSightCurDistance < ((this.fSightCurSpeed / 3.6F) * Math.sqrt(this.fSightCurAltitude * 0.203874F))) {
                this.bSightBombDump = true;
            }
            if (this.bSightBombDump) {
                if (this.FM.isTick(3, 0)) {
                    if ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][(this.FM.CT.Weapons[3].length - 1)] != null) && (this.FM.CT.Weapons[3][(this.FM.CT.Weapons[3].length - 1)].haveBullets())) {
                        this.FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else {
                    this.FM.CT.WeaponControl[3] = false;
                }
            }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted paramNetMsgGuaranted) throws IOException {
        paramNetMsgGuaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
        paramNetMsgGuaranted.writeFloat(this.fSightCurDistance);
        paramNetMsgGuaranted.writeByte((int) this.fSightCurForwardAngle);
        paramNetMsgGuaranted.writeByte((int) ((this.fSightCurSideslip + 3.0F) * 33.333328F));
        paramNetMsgGuaranted.writeFloat(this.fSightCurAltitude);
        paramNetMsgGuaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
        paramNetMsgGuaranted.writeByte((int) (this.fSightCurReadyness * 200.0F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput paramNetMsgInput) throws IOException {
        int i = paramNetMsgInput.readUnsignedByte();
        this.bSightAutomation = ((i & 0x1) != 0);
        this.bSightBombDump = ((i & 0x2) != 0);
        this.fSightCurDistance = paramNetMsgInput.readFloat();
        this.fSightCurForwardAngle = paramNetMsgInput.readUnsignedByte();
        this.fSightCurSideslip = (-3.0F + (paramNetMsgInput.readUnsignedByte() / 33.333328F));
        this.fSightCurAltitude = (this.fDiveRecoveryAlt = paramNetMsgInput.readFloat());
        this.fSightCurSpeed = (this.fDiveVelocity = paramNetMsgInput.readUnsignedByte() * 2.5F);
        this.fSightCurReadyness = (paramNetMsgInput.readUnsignedByte() / 200.0F);
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
        this.fDiveRecoveryAlt += 10.0F;
        if (this.fDiveRecoveryAlt > 10000.0F) {
            this.fDiveRecoveryAlt = 10000.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
    }

    public void typeDiveBomberAdjAltitudeMinus() {
        this.fDiveRecoveryAlt -= 10.0F;
        if (this.fDiveRecoveryAlt < 500.0F) {
            this.fDiveRecoveryAlt = 500.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
        this.fDiveVelocity += 10.0F;
        if (this.fDiveVelocity > 800.0F) {
            this.fDiveVelocity = 800.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fDiveVelocity) });
    }

    public void typeDiveBomberAdjVelocityMinus() {
        this.fDiveVelocity -= 10.0F;
        if (this.fDiveVelocity < 150.0F) {
            this.fDiveVelocity = 150.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fDiveVelocity) });
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netMsgGuaranted) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netMsgInput) throws IOException {
    }

    public void updateDiveBomber() {
        this.fDiveAngle = (-this.FM.Or.getTangage());
        if (this.fDiveAngle > 89.0F) {
            this.fDiveAngle = 89.0F;
        }
        if (this.fDiveAngle < 10.0F) {
            this.fDiveAngle = 10.0F;
        }
        if ((this == World.getPlayerAircraft()) && ((this.FM instanceof RealFlightModel))) {
            if (((RealFlightModel) this.FM).isRealMode()) {
                switch (this.diveMechStage) {
                    case 0:
                        if ((this.bNDives) && (this.FM.CT.AirBrakeControl == 1.0F) && (this.FM.Loc.z > this.fDiveRecoveryAlt)) {
                            this.diveMechStage += 1;
                            this.bNDives = false;
                        } else {
                            this.bNDives = (this.FM.CT.AirBrakeControl != 1.0F);
                        }
                        break;
                    case 1:
                        this.FM.CT.setTrimElevatorControl(-0.35F);
                        this.FM.CT.trimElevator = -0.35F;
                        if ((this.FM.CT.AirBrakeControl == 0.0F) || (this.FM.CT.saveWeaponControl[3]) || (this.countBombs() > 0) || ((this.FM.Loc.z < this.fDiveRecoveryAlt))) {
                            if (this.FM.CT.AirBrakeControl == 0.0F) {
                                this.diveMechStage += 1;
                            }
                            if (this.countBombs() < 1) {
                                this.diveMechStage += 1;
                            }
                            if (this.FM.Loc.z < this.fDiveRecoveryAlt) {
                                this.diveMechStage += 1;
                                if (World.cur().diffCur.Limited_Ammo) {
                                    this.bDropsBombs = true;
                                }
                            }
                        }
                        break;
                    case 2:
                        this.FM.CT.setTrimElevatorControl(0.45F);
                        this.FM.CT.trimElevator = 0.45F;
                        if ((this.FM.CT.AirBrakeControl == 0.0F) || (this.FM.Or.getTangage() > 0.0F)) {
                            this.diveMechStage += 1;
                        }
                        break;
                    case 3:
                        this.FM.CT.setTrimElevatorControl(0.0F);
                        this.FM.CT.trimElevator = 0.0F;
                        this.diveMechStage = 0;
                }
            } else {
                this.FM.CT.setTrimElevatorControl(0.0F);
                this.FM.CT.trimElevator = 0.0F;
            }
        }
        if (this.bDropsBombs && this.FM.isTick(3, 0) && (this.countBombs() > 0)) {
            this.FM.CT.BayDoorControl = 1F;
            this.FM.CT.WeaponControl[3] = true;
        }
    }

    private int countBombs() {
        int totalBombs = 0;
        if ((this.FM.CT != null) && (this.FM.CT.Weapons != null) && (this.FM.CT.Weapons[3] != null)) {
            for (int i = 0; i < this.FM.CT.Weapons[3].length; i++) {
                if ((this.FM.CT.Weapons[3][i] != null) && !(this.FM.CT.Weapons[3][i] instanceof BombGunNull) && this.FM.CT.Weapons[3][i].haveBullets()) {
                    totalBombs += this.FM.CT.Weapons[3][i].countBullets();
                }
            }
        }
        return totalBombs;
    }
    
//    public static boolean hasGearsSeparated(FlightModel fm) {
//        if (BaseGameVersion.is412orLater()) return true;
//        if (gearsSeparated < 0) {
//            if (fm == null) return false;
//            if (fm.CT == null) return false;
//            try {
//                Method gearC = fm.CT.getClass().get
//            }
//        }
//    }

    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float   fSightCurDistance;
    public float    fSightCurForwardAngle;
    public float    fSightCurSideslip;
    public float    fSightCurAltitude;
    public float    fSightCurSpeed;
    public float    fSightCurReadyness;
    private float   steering;
    public float    fDiveRecoveryAlt;
    public float    fDiveVelocity;
    public float    fDiveAngle;
    private boolean isAiBombDrop;
    private long    closeAiBombBay;
    private float   flapTarget;
    private float   flapPos[];
    private float   flapTargetPos[];
    private float   Cy0_1;
    private boolean aiStartPropPitchSet;
    private boolean aiStartMixtureSet;
    public int      diveMechStage;
    public boolean  bNDives;
    private boolean bDropsBombs;
    float           rndgear[];
    static float    rndgearnull[];
    public static boolean gearsSeparated;
    private long    groundPulse;

    static {
        if (BaseGameVersion.is412orLater()) {
            gearsSeparated = true;
        } else {
            try {
                Controls.class.getMethod("getGearC", null);
                gearsSeparated = true;
            } catch (Exception e) {
                gearsSeparated = false;
            }
        }
        Property.set(JU_288.class, "originCountry", PaintScheme.countryGermany);
    }
}
