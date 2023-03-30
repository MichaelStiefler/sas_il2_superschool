package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.VisibilityChecker;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class LN411xyz extends Scheme1 implements TypeStormovik, TypeDiveBomber, TypeHaveBombReleaseGear {

    public LN411xyz() {
        this.diveMechStage = 0;
        this.bNDives = false;
        this.bDropsBombs = false;
        this.fDiveRecoveryAlt = 750F;
        this.isReleased = false;
        this.isExtendedStart = false;
        this.isExtended = false;
        this.tickConstLenFs = 0.0F;
        this.armAngle = 0.0F;
        this.oldArmAngle = 0.0F;
        this.bombHookName = null;
        this.diveMechStage = 0;
        this.bNDives = false;
        this.bDropsBombs = false;
        this.prevGear = 0.0F;
        this.prevGear2 = 0.0F;
        this.cGearPos = 0.0F;
        this.cGear = 0.0F;
        this.bNeedSetup = true;
        this.kangle = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        try {
            this.bombHookName = "_ExternalBomb01";
            this.bombGun = (BombGun) this.getBulletEmitterByHookName(this.bombHookName);
        } catch (Exception exception) {
            this.bombGun = null;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, FlightModel flightmodel) {
        float f1 = 10F * f;
        if (flightmodel != null) {
            f = 10F * Math.max(f, flightmodel.CT.getAirBrake());
        } else {
            f = f1;
        }
        if (LN411xyz.bGearExtending) {
            if (flightmodel == null) {
                float f2 = Aircraft.cvt(f1, 0.0F, 1.0F, 0.0F, 0.7071068F);
                f2 = 2.0F * f2 * f2;
                hiermesh.chunkSetAngles("GearC22", 0.0F, 0.0F, -15F * f2);
                f2 = Aircraft.cvt(f1, 0.0F, 0.25F, 0.0F, 1.0F);
            }
            float f3;
            float f6;
            if (f < 4F) {
                f3 = f6 = Aircraft.cvt(f, 3F, 4F, 0.0F, 0.4F);
            } else {
                f3 = Aircraft.cvt(f, 4F, 8F, 0.75F, 2.0F);
                f3 = (float) Math.sqrt(f3);
                f3 = Aircraft.cvt(f3, (float) Math.sqrt(0.75D), (float) Math.sqrt(2D), 0.4F, 1.0F);
                f6 = Aircraft.cvt(f, 4F, 8.5F, 0.75F, 2.0F);
                f6 = (float) Math.sqrt(f6);
                f6 = Aircraft.cvt(f6, (float) Math.sqrt(0.75D), (float) Math.sqrt(2D), 0.4F, 1.0F);
            }
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 90F * f3);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 90F * f6);
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 27F * f3);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 27F * f6);
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, -153F * f3);
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, -153F * f6);
        } else {
            if (flightmodel == null) {
                float f4 = Aircraft.cvt(f1, 8.5F, 10F, 0.0F, 1.0F);
                hiermesh.chunkSetAngles("GearC22", 0.0F, 0.0F, -15F * f4);
                f4 = Aircraft.cvt(f1, 8.5F, 8.75F, 0.0F, 1.0F);
            }
            float f5;
            if (f > 7.5F) {
                f5 = Aircraft.cvt(f, 7.5F, 8.5F, 0.9F, 1.0F);
            } else {
                f5 = Aircraft.cvt(f, 3F, 7.5F, 0.0F, 0.9F);
            }
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 90F * f5);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 90F * f5);
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 27F * f5);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 27F * f5);
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, -153F * f5);
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, -153F * f5);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        LN411xyz.moveGear(hiermesh, f, null);
    }

    protected void moveGear(float f) {
        if (this.prevGear > f) {
            LN411xyz.bGearExtending = false;
        } else {
            LN411xyz.bGearExtending = true;
        }
        this.prevGear = f;
        LN411xyz.moveGear(this.hierMesh(), f, this.FM);
        f *= 10F;
        if (LN411xyz.bGearExtending) {
            float f1 = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.7071068F);
            this.cGearPos = 2.0F * f1 * f1;
        } else {
            this.cGearPos = Aircraft.cvt(f, 8.5F, 10F, 0.0F, 1.0F);
        }
    }

    protected void moveAirBrake(float f) {
        this.moveGear(this.FM.CT.getGear());
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("FlapL_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("FlapR_D0", 0.0F, 0.0F, 40F * f);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        if (this.FM.getSpeedKMH() > 780F) {
            VisibilityChecker.checkCabinObstacle = true;
        }
        if (this.FM.isPlayers() && (this.FM.Sq.squareElevators > 0.0F)) {
            RealFlightModel realflightmodel = (RealFlightModel) this.FM;
            if (realflightmodel.RealMode && (realflightmodel.indSpeed > 120F)) {
                float f2 = 1.0F + (0.005F * (120F - realflightmodel.indSpeed));
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }
                this.FM.SensPitch = 0.45F * f2;
                if (realflightmodel.indSpeed > 120F) {
                    this.FM.producedAM.y -= 1720F * (120F - realflightmodel.indSpeed);
                }
            } else {
                this.FM.SensPitch = 0.66F;
            }
        }
        if (this.FM.AS.isMaster() && (!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && this.FM.Gears.onGround()) {
            if (this.FM.getSpeedKMH() < 20.0F) {
              this.FM.CT.cockpitDoorControl = 1.0F;
            } else {
              this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
        this.moveArm();
        if ((this == World.getPlayerAircraft()) && (this.FM instanceof RealFlightModel)) {
            if (((RealFlightModel) this.FM).isRealMode()) {
                switch (this.diveMechStage) {
                    case 0:
                        if (this.bNDives && (this.FM.CT.AirBrakeControl == 1.0F) && (this.FM.CT.AirBrakeControl == 0.0F)) {
                            this.diveMechStage++;
                            this.bNDives = false;
                        } else {
                            this.bNDives = (this.FM.CT.AirBrakeControl != 1.0F) && (this.FM.CT.AirBrakeControl != 0.0F);
                        }
                        break;

                    case 1:
                        this.FM.CT.setTrimElevatorControl(-0.65F);
                        this.FM.CT.trimElevator = -0.65F;
                        if ((this.FM.CT.AirBrakeControl == 0.0F) || this.FM.CT.saveWeaponControl[3] || ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0) && !(this instanceof LN411))) {
                            if (this.FM.CT.AirBrakeControl == 0.0F) {
                                this.diveMechStage++;
                            }
                            if ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0)) {
                                this.diveMechStage++;
                            }
                        }
                        break;

                    case 2:
                        if (this.FM.isTick(41, 0)) {
                            this.FM.CT.setTrimElevatorControl(0.85F);
                            this.FM.CT.trimElevator = 0.85F;
                        }
                        if ((this.FM.CT.AirBrakeControl == 0.0F) || (this.FM.Or.getTangage() > 0.0F)) {
                            this.diveMechStage++;
                        }
                        break;

                    case 3:
                        this.FM.CT.setTrimElevatorControl(0.0F);
                        this.FM.CT.trimElevator = 0.0F;
                        this.diveMechStage = 0;
                        break;
                }
            } else {
                this.FM.CT.setTrimElevatorControl(0.0F);
                this.FM.CT.trimElevator = 0.0F;
            }
        }
        if (this.bDropsBombs && this.FM.isTick(3, 0) && (this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null) && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
            this.FM.CT.WeaponControl[3] = true;
        }
        if (this.bNeedSetup) {
            this.cGear = this.FM.CT.GearControl;
            this.bNeedSetup = false;
        }
        this.cGear = LN411xyz.filter(f, this.FM.CT.GearControl, this.cGear, 999.9F, this.FM.CT.dvGear);
        if (this.prevGear2 > this.cGear) {
            LN411xyz.bGearExtending2 = false;
        } else {
            LN411xyz.bGearExtending2 = true;
        }
        this.prevGear2 = this.cGear;
        float f2 = 10F * this.cGear;
        if (LN411xyz.bGearExtending2) {
            float f5 = Aircraft.cvt(f2, 0.0F, 1.0F, 0.0F, 0.7071068F);
            this.cGearPos = 2.0F * f5 * f5;
            this.hierMesh().chunkSetAngles("GearC22", 0.0F, 0.0F, -15F * this.cGearPos);
            f5 = Aircraft.cvt(f2, 0.0F, 0.25F, 0.0F, 1.0F);
        } else {
            this.cGearPos = Aircraft.cvt(f2, 8.5F, 10F, 0.0F, 1.0F);
            this.hierMesh().chunkSetAngles("GearC22", 0.0F, 0.0F, -15F * this.cGearPos);
        }
        this.hierMesh().chunkSetAngles("RadiaG_D0", 0.0F, -4.5F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("RadiaD_D0", 0.0F, -4.5F * this.kangle, 0.0F);
        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        if (this.kangle > 1.0F) {
            this.kangle = 1.0F;
        }
        if (this.FM.brakeShoe && (this.FM.Gears.nOfGearsOnGr == 3)) {
            this.hierMesh().chunkVisible("ShockL", true);
            this.hierMesh().chunkVisible("ShockR", true);
        } else {
            this.hierMesh().chunkVisible("ShockL", false);
            this.hierMesh().chunkVisible("ShockR", false);
        }
        super.update(f);
    }

    private static final float filter(float f, float f1, float f2, float f3, float f4) {
        float f5 = (float) Math.exp(-f / f3);
        float f6 = f1 + ((f2 - f1) * f5);
        if (f6 < f1) {
            f6 += f4 * f;
            if (f6 > f1) {
                f6 = f1;
            }
        } else if (f6 > f1) {
            f6 -= f4 * f;
            if (f6 < f1) {
                f6 = f1;
            }
        }
        return f6;
    }

    public boolean waitRelease() {
        if ((this.bombGun == null) || !this.bombGun.isShots()) {
            return true;
        }
        this.isExtendedStart = true;
        if (this.tickConstLenFs == 0.0F) {
            this.tickConstLenFs = Time.tickConstLenFs();
        }
        return this.isReleased;
    }

    private void moveArm() {
        if (!this.isExtendedStart) {
            return;
        }
        if (!this.isExtended) {
            this.armAngle += 90F * this.tickConstLenFs;
        }
        if (this.armAngle >= 90F) {
            this.isReleased = true;
            this.isExtended = true;
        } else if (this.FM.getOverload() > 1.0F) {
            this.isReleased = true;
        }
        if (this.isReleased && this.isExtended && (this.armAngle > 0.0F)) {
            this.armAngle -= 180F * this.tickConstLenFs;
        }
        if (this.oldArmAngle != this.armAngle) {
            try {
                this.hierMesh().chunkSetAngles("Arm1_D0", 0.0F, this.armAngle, 0.0F);
                this.hierMesh().chunkSetAngles("Arm2_D0", 0.0F, -this.armAngle, 0.0F);
                this.bombGun.updateHook(this.bombHookName);
            } catch (Exception exception) {
            }
            this.oldArmAngle = this.armAngle;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(14.2F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 140000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 85000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    } else if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(6.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 0.75F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                }
                if (s.endsWith("oil1") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    if ((World.Rnd().nextFloat() < 0.05F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.6F))) {
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                    }
                }
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 2) {
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
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 2)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 2)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 2)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 2)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 2)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 2)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 2)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 2)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 2)) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xradia")) {
            if (s.startsWith("xradiag") && (this.chunkDamageVisible("RadiaG") < 2)) {
                this.hitChunk("RadiaG", shot);
            }
            if (s.startsWith("xradiad") && (this.chunkDamageVisible("RadiaD") < 2)) {
                this.hitChunk("RadiaD", shot);
            }
        } else if (s.startsWith("xflap")) {
            if (s.startsWith("xflapl") && (this.chunkDamageVisible("FlapL") < 2)) {
                this.hitChunk("FlapL", shot);
            }
            if (s.startsWith("xflapr") && (this.chunkDamageVisible("FlapR") < 2)) {
                this.hitChunk("FlapR", shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else {
                j = s.charAt(5) - 49;
            }
            this.hitFlesh(j, shot, byte0);
        }
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
        this.fDiveRecoveryAlt += 25F;
        if (this.fDiveRecoveryAlt > 6000F) {
            this.fDiveRecoveryAlt = 6000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
    }

    public void typeDiveBomberAdjAltitudeMinus() {
        this.fDiveRecoveryAlt -= 25F;
        if (this.fDiveRecoveryAlt < 0.0F) {
            this.fDiveRecoveryAlt = 0.0F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    public float           fDiveRecoveryAlt;
    boolean                isReleased;
    boolean                isExtendedStart;
    boolean                isExtended;
    float                  tickConstLenFs;
    float                  armAngle;
    float                  oldArmAngle;
    BombGun                bombGun;
    String                 bombHookName;
    public int             diveMechStage;
    public boolean         bNDives;
    private boolean        bDropsBombs;
    private float          prevGear;
    private float          prevGear2;
    private static boolean bGearExtending  = false;
    private static boolean bGearExtending2 = false;
    private float          cGearPos;
    private float          cGear;
    private boolean        bNeedSetup;
    private float          kangle;

    static {
        Class class1 = LN411xyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryFrance);
    }
}
