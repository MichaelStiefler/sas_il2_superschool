package com.maddox.il2.objects.air;

import java.security.SecureRandom;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public abstract class TA_154 extends Scheme2 {

    public TA_154() {
        // Seed Pseudo-Random Generator with really random hash.
        SecureRandom secRandom = new SecureRandom();
        secRandom.setSeed(System.currentTimeMillis());
        RangeRandom rr = new RangeRandom(secRandom.nextLong());
        for (int i = 0; i < this.rndgear.length; i++) {
            this.rndgear[i] = rr.nextFloat(0.0F, 0.2F);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
                this.hitProp(0, j, actor);
                this.cut("Engine1");
                break;

            case 36:
            case 37:
                this.hitProp(1, j, actor);
                this.cut("Engine2");
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float frontGearPos, float[] rnd) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, smoothCvt(frontGearPos, rnd[2] + 0.1F, rnd[2] + 0.69F, 0.0F, -105F), 0.0F);
        if (frontGearPos < 0.6F) {
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearC5_D0", 0.0F, smoothCvt(frontGearPos, rnd[2] + 0.05F, rnd[2] + 0.25F, 0.0F, -90F), 0.0F);
            hiermesh.chunkSetAngles("GearC6_D0", 0.0F, smoothCvt(frontGearPos, rnd[2] + 0.05F, rnd[2] + 0.25F, 0.0F, -90F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearC5_D0", 0.0F, smoothCvt(frontGearPos, rnd[2] + 0.63F, rnd[2] + 0.75F, -90F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearC6_D0", 0.0F, smoothCvt(frontGearPos, rnd[2] + 0.63F, rnd[2] + 0.75F, -90F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, smoothCvt(leftGearPos, rnd[0] + 0.01F, rnd[0] + 0.79F, 0.0F, 116.5F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, smoothCvt(leftGearPos, rnd[0] + 0.01F, rnd[0] + 0.1F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, smoothCvt(leftGearPos, rnd[0] + 0.01F, rnd[0] + 0.1F, 0.0F, -72.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, smoothCvt(rightGearPos, rnd[1] + 0.01F, rnd[1] + 0.79F, 0.0F, 116.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, smoothCvt(rightGearPos, rnd[1] + 0.01F, rnd[1] + 0.1F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, smoothCvt(rightGearPos, rnd[1] + 0.01F, rnd[1] + 0.1F, 0.0F, -72.5F), 0.0F);
        if (leftGearPos < 0.5F) {
            hiermesh.chunkSetAngles("GearL7_D0", 0.0F, smoothCvt(leftGearPos, rnd[0] + 0.01F, rnd[0] + 0.1F, 0.0F, -70F), 0.0F);
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, smoothCvt(leftGearPos, rnd[0] + 0.01F, rnd[0] + 0.1F, 0.0F, -72.5F), 0.0F);
            hiermesh.chunkSetAngles("GearR7_D0", 0.0F, smoothCvt(rightGearPos, rnd[1] + 0.01F, rnd[1] + 0.1F, 0.0F, -70F), 0.0F);
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, smoothCvt(rightGearPos, rnd[1] + 0.01F, rnd[1] + 0.1F, 0.0F, -72.5F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearL7_D0", 0.0F, smoothCvt(leftGearPos, rnd[0] + 0.69F, rnd[0] + 0.79F, -70F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, smoothCvt(leftGearPos, rnd[0] + 0.69F, rnd[0] + 0.79F, -72.5F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearR7_D0", 0.0F, smoothCvt(rightGearPos, rnd[1] + 0.69F, rnd[1] + 0.79F, -70F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, smoothCvt(rightGearPos, rnd[1] + 0.69F, rnd[1] + 0.79F, -72.5F, 0.0F), 0.0F);
        }
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float frontGearPos) {
        moveGear(hiermesh, leftGearPos, rightGearPos, frontGearPos, rndgearnull);
    }

    protected void moveGear(float leftGearPos, float rightGearPos, float frontGearPos) {
        moveGear(this.hierMesh(), leftGearPos, rightGearPos, frontGearPos, this.rndgear);
    }

    public static void moveGear(HierMesh hiermesh, float gearPos) {
        moveGear(hiermesh, gearPos, gearPos, gearPos, rndgearnull);
    }

    protected void moveGear(float gearPos) {
        moveGear(this.hierMesh(), gearPos);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.4F, 0.0F, 0.4F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.4F, 0.0F, 0.4F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.5F, 0.0F, 0.5F);
        Aircraft.ypr[1] = this.frontWheelSteeringPos;
        this.hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
        //HUD.training("0:" + this.FM.Gears.gWheelSinking[0] + " 1:" + this.FM.Gears.gWheelSinking[1] + " 2:" + this.FM.Gears.gWheelSinking[2]);
    }

//    protected void moveRudder(float f)
//    {
//        HUD.training("getGear():" + this.FM.CT.getGear() + ", f:" + f);
//        if(this.FM.CT.getGear() > 0.98F) {
//            frontWheelSteeringPos = 36F * f;
//            hierMesh().chunkSetAngles("GearC3_D0", 0.0F, frontWheelSteeringPos, 0.0F);
//        }
//        super.moveRudder(f);
//    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() > 0.98F) {
            this.frontWheelSteeringPos = -f * 2F;
            this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, this.frontWheelSteeringPos, 0.0F);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxammo0")) {
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setJamBullets(0, World.Rnd().nextInt(0, 5));
                }
                this.getEnergyPastArmor(11.4F, shot);
                return;
            }
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(12.1D / Math.abs(Aircraft.v1.x), shot);
                }
                if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(World.Rnd().nextDouble(20D, 60D) / Math.abs(Aircraft.v1.x), shot);
                    if (shot.power <= 0.0F) {
                        Aircraft.debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        this.doRicochetBack(shot);
                    }
                }
                if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(12.7F, shot);
                }
                if (s.endsWith("p4")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (s.endsWith("p5")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.z) + 0.000001D), shot);
                }
                if (s.endsWith("p6")) {
                    this.getEnergyPastArmor(4.1D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (s.endsWith("p8")) {
                    this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                if (s.length() == 12) {
                    i = 10 + (s.charAt(11) - 48);
                }
                switch (i) {
                    case 8:
                    case 9:
                    default:
                        break;

                    case 1:
                    case 3:
                        if (this.getEnergyPastArmor(3F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Aileron Controls Out..");
                        }
                        break;

                    case 2:
                        this.getEnergyPastArmor(1.5F, shot);
                        break;

                    case 4:
                        if (this.getEnergyPastArmor(1.5F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.debuggunnery("*** Engine1 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            this.debuggunnery("*** Engine1 Prop Controls Out..");
                        }
                        break;

                    case 5:
                        if (this.getEnergyPastArmor(1.5F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            this.debuggunnery("*** Engine2 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            this.debuggunnery("*** Engine2 Prop Controls Out..");
                        }
                        break;

                    case 6:
                    case 7:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Ailerons Controls Out..");
                        }
                        break;

                    case 10:
                    case 11:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.12F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        break;

                    case 12:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.12F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int j = 0;
                if (s.startsWith("xxeng2")) {
                    j = 1;
                }
                this.debuggunnery("Engine Module[" + j + "]: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 280000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 100000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(0.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[j].getCylindersRatio() * 0.66F))) {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 1000000F)) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("eqpt") || (s.endsWith("cyls") && (World.Rnd().nextFloat() < 0.01F))) {
                    if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                        }
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("mag1") || s.endsWith("mag2")) {
                    this.debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                    this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, j);
                }
                if (s.endsWith("oil1")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                        this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    }
                    this.FM.AS.hitOil(shot.initiator, j);
                }
                if (s.endsWith("prop") && (this.getEnergyPastArmor(0.42F, shot) > 0.0F)) {
                    this.FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F)) {
                    this.debuggunnery("Engine Module: Turbine Disabled..");
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockk1") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlocksl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlocksr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                int k = 0;
                if (s.endsWith("2")) {
                    k = 1;
                }
                if ((this.getEnergyPastArmor(0.21F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.2435F)) {
                    this.FM.AS.hitOil(shot.initiator, k);
                }
                Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Oil Tank Pierced..");
                return;
            }
            if (s.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F) {
                    this.debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
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
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if ((s.endsWith("e1") || s.endsWith("e2")) && (this.getEnergyPastArmor(28F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine1 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if ((s.endsWith("e3") || s.endsWith("e4")) && (this.getEnergyPastArmor(28F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine2 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                if (s.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 1) && (this.getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 48;
                switch (l) {
                    case 1:
                        this.doHitMeATank(shot, 0);
                        break;

                    case 2:
                        this.doHitMeATank(shot, 1);
                        break;

                    case 3:
                        this.doHitMeATank(shot, 2);
                        break;

                    case 4:
                        this.doHitMeATank(shot, 3);
                        break;

                    case 5:
                        this.doHitMeATank(shot, 1);
                        this.doHitMeATank(shot, 2);
                        break;
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
            if (((Tuple3d) (point3d)).x > 1.471D) {
                if ((((Tuple3d) (point3d)).z > 0.552D) && (((Tuple3d) (point3d)).x > 2.37D)) {
                    if (((Tuple3d) (point3d)).y > 0.0D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                    } else {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                    }
                }
                if ((((Tuple3d) (point3d)).z > 0.0D) && (((Tuple3d) (point3d)).z < 0.539D)) {
                    if (((Tuple3d) (point3d)).y > 0.0D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                    } else {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                    }
                }
                if ((((Tuple3d) (point3d)).x < 2.407D) && (((Tuple3d) (point3d)).z > 0.552D)) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
                if ((((Tuple3d) (point3d)).x > 2.6D) && (((Tuple3d) (point3d)).z > 0.693D)) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
            }
        } else if (s.startsWith("xtail")) {
            this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 2) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) {
                this.hitChunk("StabL", shot);
            }
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 1) {
                this.hitChunk("VatorL", shot);
            }
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 1) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) {
                this.hitChunk("WingLIn", shot);
            }
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) {
                this.hitChunk("WingRIn", shot);
            }
        } else if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) {
                this.hitChunk("WingLMid", shot);
            }
        } else if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) {
                this.hitChunk("WingRMid", shot);
            }
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) {
                this.hitChunk("WingLOut", shot);
            }
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 1) {
                this.hitChunk("AroneL", shot);
            }
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 1) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xgear")) {
            if (World.Rnd().nextFloat() < 0.1F) {
                this.debuggunnery("*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i1;
            if (s.endsWith("a")) {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else {
                i1 = s.charAt(5) - 49;
            }
            this.hitFlesh(i1, shot, byte0);
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

    public void update(float f) {
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
            Maneuver maneuver = (Maneuver) this.FM;
            if ((maneuver.get_maneuver() == 25) && (maneuver.Alt < 60.0F)) {
                if (maneuver.Or.getTangage() > 10F) { // Limit nose up attitude to 4 degrees on touchdown to avoid tail strike (only for AI)!
                    maneuver.Or.increment(0.0F, -(maneuver.Or.getTangage() - 10F), 0.0F);
                }
            }
        }

        super.update(f);
    }

    float          frontWheelSteeringPos = 0.0F;

    float[]        rndgear               = { 0.0F, 0.0F, 0.0F };
    static float[] rndgearnull           = { 0.0F, 0.0F, 0.0F }; // Used for Plane Land Pose calculation when Aircraft.setFM calls static gear methods

    // Static Helper Method to initialize the static YPR and XYZ modifiers of the Aircraft class.
    // In IL-2 base game only instance method for this purpose exists, but this instance method cannot be called from static methods.
    static void myResetYPRmodifier() {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
        return outMin + ((outMax - outMin) * ((-0.5F * (float) Math.cos(((inputValue - inMin) / (inMax - inMin)) * Math.PI)) + 0.5F));
    }

    static {
        Class class1 = TA_154.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
