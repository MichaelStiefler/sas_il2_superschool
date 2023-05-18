package com.maddox.il2.objects.air;

import java.security.SecureRandom;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.rts.Property;

public abstract class HE_1xx extends Scheme1 implements TypeFighter {

    public HE_1xx() {
        this.trimElevator = 0.0F;
        this.bHasElevatorControl = true;
        this.X = 1.0F;
        this.GlassState = 0;
        this.s17 = this.s18 = 0.15F;
        this.s31 = this.s32 = 0.35F;
        // Seed Pseudo-Random Generator with really random hash.
        SecureRandom secRandom = new SecureRandom();
        secRandom.setSeed(System.currentTimeMillis());
        RangeRandom rr = new RangeRandom(secRandom.nextLong());
        for (int i = 0; i < this.rndgear.length; i++) {
            this.rndgear[i] = rr.nextFloat(0.0F, 0.15F);
        }
    }

    public float getEyeLevelCorrection() {
        return 0.1F;
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.3F, 0.0F, 0.3F);
        this.hierMesh().chunkSetLocate("GearL99_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.3F, 0.0F, 0.3F);
        this.hierMesh().chunkSetLocate("GearR99_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void doWoundPilot(int j, float f1) {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                if (!this.FM.AS.bIsAboutToBailout && World.cur().isHighGore()) {
                    if (this.hierMesh().isChunkVisible("Blister1_D0")) {
                        this.hierMesh().chunkVisible("Gore1_D0", true);
                    }
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public boolean cut(String s) {
        if (s.startsWith("Tail1")) {
            this.FM.AS.hitTank(this, 2, 100);
        }
        return super.cut(s);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && (this.FM.AS.astateTankStates[0] > 5)) {
            this.FM.AS.repairTank(0);
        }
        if (super.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
        }
    }

    public void update(float f) {
        super.update(f);
        this.CombustionFlame();
        if (!this.getOp(31) || !this.getOp(32)) {
            this.FM.CT.trimAileron = (((this.FM.CT.ElevatorControl * (this.s32 - this.s31)) + (this.FM.CT.trimElevator * (this.s18 - this.s17))) * this.FM.SensPitch) / 3F;
        }
        if (!this.bHasElevatorControl) {
            this.FM.CT.ElevatorControl = 0.0F;
        }
        if (this.trimElevator != this.FM.CT.trimElevator) {
            this.trimElevator = this.FM.CT.trimElevator;
            this.hierMesh().chunkSetAngles("StabL_D0", 0.0F, 0.0F, -16F * this.trimElevator);
            this.hierMesh().chunkSetAngles("StabR_D0", 0.0F, 0.0F, -16F * this.trimElevator);
        }
        
//        HUD.training("W:" + df.format(this.FM.EI.engines[0].tWaterOut) + 
//                "/" + df.format(this.FM.EI.engines[0].tWaterCritMax) + 
//                " O:" + df.format(this.FM.EI.engines[0].tOilOut) + 
//                "/" + df.format(this.FM.EI.engines[0].tOilCritMax));
    }
//    DecimalFormat df = new DecimalFormat("0.##");

    protected void moveElevator(float f) {
        f -= this.trimElevator;
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    private void reflectGlassState(int i) {
        this.GlassState |= i;
        switch (this.GlassState & 3) {
            case 1:
                this.hierMesh().materialReplace("Glass2", "ZBulletsHoles");
                break;

            case 2:
                this.hierMesh().materialReplace("Glass2", "GlassOil");
                break;

            case 3:
                this.hierMesh().materialReplace("Glass2", "GlassOilHoles");
                break;
        }
        switch (this.GlassState & 0xc) {
            case 4:
                this.hierMesh().materialReplace("GlassW", "ZBulletsHoles");
                break;

            case 8:
                this.hierMesh().materialReplace("GlassW", "Wounded");
                this.hierMesh().chunkVisible("Gore2_D0", true);
                break;

            case 12:
                this.hierMesh().materialReplace("GlassW", "WoundedHoles");
                this.hierMesh().chunkVisible("Gore2_D0", true);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    this.reflectGlassState(5);
                    Aircraft.debugprintln(this, "*** Armor Glass: Hit..");
                    if (shot.power <= 0.0F) {
                        Aircraft.debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.doRicochetBack(shot);
                        }
                    }
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(0.5F, shot);
                } else if (s.endsWith("p3")) {
                    if (point3d.z < -0.27D) {
                        this.getEnergyPastArmor(4.1D / (Math.abs(Aircraft.v1.z) + 0.00001D), shot);
                    } else {
                        this.getEnergyPastArmor(8.1D / (Math.abs(Aircraft.v1.x) + 0.00001D), shot);
                    }
                } else if (s.endsWith("p4")) {
                    this.getEnergyPastArmor(9D / (Math.abs(Aircraft.v1.z) + 0.00001D), shot);
                } else if (s.endsWith("p5")) {
                    this.getEnergyPastArmor(9D / (Math.abs(Aircraft.v1.x) + 0.00001D), shot);
                } else if (s.endsWith("p6")) {
                    this.getEnergyPastArmor(9D / (Math.abs(Aircraft.v1.x) + 0.00001D), shot);
                } else if (s.endsWith("a1")) {
                    if (World.Rnd().nextFloat() < 0.5F) {
                        shot.powerType = 0;
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 3F), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 4:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                        }
                        break;

                    case 2:
                    case 3:
                        if ((this.getEnergyPastArmor(0.12F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.1F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls: Disabled..");
                        }
                        break;

                    case 5:
                    case 6:
                        if ((this.getEnergyPastArmor(0.002F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.1F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Elevator Controls: Disabled / Strings Broken..");
                        }
                        break;

                    case 7:
                        if ((this.getEnergyPastArmor(2.3F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.2F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                        }
                        break;

                    case 8:
                        if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                            Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 9:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                        }
                        break;
                }
            }
            if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.5F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            }
            if (s.startsWith("xxwj") && (this.getEnergyPastArmor(12.5F, shot) > 0.0F)) {
                if (s.endsWith("l")) {
                    Aircraft.debugprintln(this, "*** WingL Console Lock Destroyed..");
                    this.nextDMGLevels(4, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                } else {
                    Aircraft.debugprintln(this, "*** WingR Console Lock Destroyed..");
                    this.nextDMGLevels(4, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                }
            }
            if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
            }
            if (s.startsWith("xxeng")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("pipe")) {
                    if ((World.Rnd().nextFloat() < 0.1F) && (this.FM.CT.Weapons[1] != null) && (this.FM.CT.Weapons[1].length != 2)) {
                        this.FM.AS.setJamBullets(1, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Nose Nozzle Pipe Bent..");
                    }
                    this.getEnergyPastArmor(0.3F, shot);
                } else if (s.endsWith("prop")) {
                    if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.8F)) {
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                        } else {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Damaged..");
                        }
                    }
                } else if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Reductor Gear..");
                        } else {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                            Aircraft.debugprintln(this, "*** Engine Module: Reductor Gear Damaged, Prop Governor Failed..");
                        }
                    }
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
                    }
                } else if (s.endsWith("feed")) {
                    if ((this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F) && (this.FM.EI.engines[0].getPowerOutput() > 0.7F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 100);
                        Aircraft.debugprintln(this, "*** Engine Module: Pressurized Fuel Line Pierced, Fuel Flamed..");
                    }
                } else if (s.endsWith("fuel")) {
                    if (this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
                        this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine Module: Fuel Line Stalled, Engine Stalled..");
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(22.5F, shot);
                } else if (s.startsWith("xxeng1cyl")) {
                    if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.75F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.01F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.startsWith("xxeng1mag")) {
                    int j = s.charAt(9) - 49;
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                    Aircraft.debugprintln(this, "*** Engine Module: Magneto " + j + " Destroyed..");
                } else if (s.endsWith("sync")) {
                    if ((this.getEnergyPastArmor(2.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        Aircraft.debugprintln(this, "*** Engine Module: Gun Synchronized Hit, Nose Guns Lose Authority..");
                        this.FM.AS.setJamBullets(0, 0);
                        this.FM.AS.setJamBullets(0, 1);
                    }
                } else if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.reflectGlassState(2);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
            }
            if (s.startsWith("xxoil")) {
                this.FM.AS.hitOil(shot.initiator, 0);
                this.getEnergyPastArmor(0.22F, shot);
                this.reflectGlassState(2);
                Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 48;
                switch (k) {
                    default:
                        break;

                    case 1:
                    case 2:
                        if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            if (this.FM.AS.astateTankStates[2] == 0) {
                                Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                                this.FM.AS.hitTank(shot.initiator, 2, 1);
                                this.FM.AS.doSetTankState(shot.initiator, 2, 1);
                            }
                            if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
                                this.FM.AS.hitTank(shot.initiator, 2, 2);
                                Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                            }
                        }
                        break;

                    case 3:
                        if (World.Rnd().nextFloat() < 0.05F) {
                            Aircraft.debugprintln(this, "*** MW50 Tank: Pierced..");
                            this.FM.AS.setInternalDamage(shot.initiator, 2);
                        }
                        break;

                    case 4:
                        if ((this.getEnergyPastArmor(1.7F, shot) > 0.0F) && (((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) || (World.Rnd().nextFloat() < 0.25F))) {
                            Aircraft.debugprintln(this, "*** Nitrogen Oxyde Tank: Pierced, Nitros Flamed..");
                            this.FM.AS.hitTank(shot.initiator, 0, 100);
                            this.FM.AS.hitTank(shot.initiator, 1, 100);
                            this.FM.AS.hitTank(shot.initiator, 2, 100);
                        }
                        break;
                }
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                if (s.endsWith("l")) {
                    Aircraft.debugprintln(this, "*** Wing Gun (L): Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.endsWith("r")) {
                    Aircraft.debugprintln(this, "*** Wing Gun (L): Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 12.96F), shot);
            }
            if (s.startsWith("xxcannon")) {
                Aircraft.debugprintln(this, "*** Nose Cannon: Disabled..");
                this.FM.AS.setJamBullets(1, 0);
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if (s.startsWith("xxammo")) {
                if (World.Rnd().nextFloat(3800F, 30000F) < shot.power) {
                    if (s.endsWith("01")) {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Ammo Feed Chain Broken..");
                        this.FM.AS.setJamBullets(0, 0);
                    }
                    if (s.endsWith("02")) {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Ammo Feed Chain Broken..");
                        this.FM.AS.setJamBullets(0, 1);
                    }
                    if (s.endsWith("l")) {
                        Aircraft.debugprintln(this, "*** Wing Gun (L): Ammo Feed Drum Damaged..");
                        this.FM.AS.setJamBullets(1, 0);
                    }
                    if (s.endsWith("r")) {
                        Aircraft.debugprintln(this, "*** Wing Gun (R): Ammo Feed Drum Damaged..");
                        this.FM.AS.setJamBullets(1, 1);
                    }
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            }
            return;
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
            if (s.startsWith("xcockpit")) {
                if (point3d.z > 0.4D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                    this.reflectGlassState(5);
                    if (World.Rnd().nextFloat() < 0.1F) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                        this.reflectGlassState(5);
                    }
                } else if (point3d.y > 0.0D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                } else {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                    this.reflectGlassState(5);
                }
                if (point3d.x > 0.2D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 1)) {
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
            if (s.startsWith("xaronel")) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner")) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else {
                l = s.charAt(5) - 49;
            }
            this.hitFlesh(l, shot, byte0);
            if (this.FM.AS.getPilotHealth(0) < 1.0F) {
                this.reflectGlassState(8);
            }
        }
    }

    private void cutOp(int i) {
        super.FM.Operate &= ~(1L << i);
    }

    protected boolean getOp(int i) {
        return (this.FM.Operate & (1L << i)) != 0L;
    }

    private float Op(int i) {
        return this.getOp(i) ? 1.0F : 0.0F;
    }

    protected boolean cutFM(int i, int i_2_, Actor actor) {
        if (!this.getOp(i)) {
            return false;
        }
        switch (i) {
            case 17:
                this.cut("StabL");
                this.cutOp(17);
                super.FM.setCapableOfACM(false);
                if (World.Rnd().nextInt(-1, 8) < this.FM.Skill) {
                    super.FM.setReadyToReturn(true);
                }
                if (World.Rnd().nextInt(-1, 16) < this.FM.Skill) {
                    super.FM.setReadyToDie(true);
                }
                this.FM.Sq.liftStab *= (0.5F * this.Op(18)) + 0.1F;
                this.FM.Sq.liftWingLIn *= 1.1F;
                this.FM.Sq.liftWingRIn *= 0.9F;
                this.FM.Sq.dragProducedCx -= 0.06F;
                if (this.Op(18) == 0.0F) {
                    super.FM.SensPitch = 0.0F;
                    super.FM.setGCenter(0.2F);
                } else {
                    super.FM.setGCenter(0.1F);
                    this.s17 = 0.0F;
                    super.FM.SensPitch *= this.s17 + this.s18 + this.s31 + this.s32;
                    this.X = 1.0F / (this.s17 + this.s18 + this.s31 + this.s32);
                    this.s18 *= this.X;
                    this.s31 *= this.X;
                    this.s32 *= this.X;
                }
                // fall through

            case 31:
                if (this.Op(31) == 0.0F) {
                    return false;
                }
                this.cut("VatorL");
                this.cutOp(31);
                if (this.Op(32) == 0.0F) {
                    this.bHasElevatorControl = false;
                    super.FM.setCapableOfACM(false);
                    if (this.Op(18) == 0.0F) {
                        super.FM.setReadyToDie(true);
                    }
                }
                this.FM.Sq.squareElevators *= 0.5F * this.Op(32);
                this.FM.Sq.dragProducedCx += 0.06F;
                this.s31 = 0.0F;
                super.FM.SensPitch *= this.s17 + this.s18 + this.s31 + this.s32;
                this.X = 1.0F / (this.s17 + this.s18 + this.s31 + this.s32);
                this.s17 *= this.X;
                this.s18 *= this.X;
                this.s32 *= this.X;
                return false;

            case 18:
                this.cut("StabR");
                this.cutOp(18);
                super.FM.setCapableOfACM(false);
                if (World.Rnd().nextInt(-1, 8) < this.FM.Skill) {
                    super.FM.setReadyToReturn(true);
                }
                if (World.Rnd().nextInt(-1, 16) < this.FM.Skill) {
                    super.FM.setReadyToDie(true);
                }
                this.FM.Sq.liftStab *= (0.5F * this.Op(17)) + 0.1F;
                this.FM.Sq.liftWingLIn *= 0.9F;
                this.FM.Sq.liftWingRIn *= 1.1F;
                this.FM.Sq.dragProducedCx -= 0.06F;
                if (this.Op(17) == 0.0F) {
                    super.FM.SensPitch = 0.0F;
                    super.FM.setGCenter(0.2F);
                } else {
                    super.FM.setGCenter(0.1F);
                    this.s18 = 0.0F;
                    super.FM.SensPitch *= this.s17 + this.s18 + this.s31 + this.s32;
                    this.X = 1.0F / (this.s17 + this.s18 + this.s31 + this.s32);
                    this.s17 *= this.X;
                    this.s31 *= this.X;
                    this.s32 *= this.X;
                }
                // fall through

            case 32:
                if (this.Op(32) == 0.0F) {
                    return false;
                }
                this.cut("VatorR");
                this.cutOp(32);
                if (this.Op(31) == 0.0F) {
                    this.bHasElevatorControl = false;
                    super.FM.setCapableOfACM(false);
                    if (this.Op(17) == 0.0F) {
                        super.FM.setReadyToDie(true);
                    }
                }
                this.FM.Sq.squareElevators *= 0.5F * this.Op(31);
                this.FM.Sq.dragProducedCx += 0.06F;
                this.s32 = 0.0F;
                super.FM.SensPitch *= this.s17 + this.s18 + this.s31 + this.s32;
                this.X = 1.0F / (this.s17 + this.s18 + this.s31 + this.s32);
                this.s17 *= this.X;
                this.s18 *= this.X;
                this.s31 *= this.X;
                return false;

            default:
                return super.cutFM(i, i_2_, actor);
        }
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if ((explosion.chunkName != null) && (explosion.power > 0.0F) && explosion.chunkName.startsWith("Tail1")) {
            if (World.Rnd().nextFloat(0.0F, 0.038F) < explosion.power) {
                this.FM.AS.setControlsDamage(explosion.initiator, 1);
            }
            if (World.Rnd().nextFloat(0.0F, 0.042F) < explosion.power) {
                this.FM.AS.setControlsDamage(explosion.initiator, 2);
            }
        }
        super.msgExplosion(explosion);
    }

    private void CombustionFlame() {
        if ((this.FM.EI.engines[0].getStage() > 1) && (this.FM.EI.engines[0].getStage() < 3) && (super.FM.getSpeedKMH() < 10F)) {
            int i = World.Rnd().nextInt(1, 12);
            Eff3DActor.New(this, this.findHook("_Engine1EF_" + (i < 10 ? "0" : "") + i), null, 1.0F, "3DO/Effects/Aircraft/He1xxFT.eff", -1F);
        } else if ((this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[0].getPowerOutput() < 0.05F)) {
            int i = World.Rnd().nextInt(1, 80);
            if (i < 13) {
                Eff3DActor.New(this, this.findHook("_Engine1EF_" + (i < 10 ? "0" : "") + i), null, 1.0F, "3DO/Effects/Fireworks/He1xxFW.eff", -1F);
            }
        } else if ((this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[0].getPowerOutput() > 0.85F)) {
            int i = World.Rnd().nextInt(1, 12 + (int) cvt(this.FM.EI.engines[0].getPowerOutput(), 0.85F, 1.10F, 38F, 0F));
            if (i < 13) {
                Eff3DActor.New(this, this.findHook("_Engine1EF_" + (i < 10 ? "0" : "") + i), null, 1.0F, "3DO/Effects/Fireworks/He1xxFW.eff", -1F);
            }
        }
    }

    // Static Helper Method to initialize the static YPR and XYZ modifiers of the Aircraft class.
    // In IL-2 base game only instance method for this purpose exists, but this instance method cannot be called from static methods.
    static void myResetYPRmodifier() {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
        return outMin + ((outMax - outMin) * ((-0.5F * (float) Math.cos(((inputValue - inMin) / (inMax - inMin)) * Math.PI)) + 0.5F));
    }

    private float   trimElevator;
    private boolean bHasElevatorControl;
    private float   X;
    private float   s17;
    private float   s18;
    private float   s31;
    private float   s32;
    private int     GlassState;
    float[]         rndgear     = { 0.0F, 0.0F, 0.0F };
    static float[]  rndgearnull = { 0.0F, 0.0F, 0.0F }; // Used for Plane Land Pose calculation when Aircraft.setFM calls static gear methods

    static {
        Class var_class = HE_1xx.class;
        Property.set(var_class, "originCountry", PaintScheme.countryGermany);
    }
}
