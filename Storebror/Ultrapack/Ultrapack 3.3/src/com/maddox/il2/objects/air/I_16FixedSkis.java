package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class I_16FixedSkis extends Scheme1 implements TypeFighter {

    public I_16FixedSkis() {
        this.skiAngleL = 0.0F;
        this.skiAngleR = 0.0F;
        this.spring = 0.15F;
        this.wireRandomizer1 = 1.0F;
        this.wireRandomizer2 = 1.0F;
        this.wireRandomizer3 = 1.0F;
        this.wireRandomizer4 = 1.0F;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) this.getEnergyPastArmor(8.26F / (Math.abs((float) Aircraft.v1.x) + 1E-005F), shot);
            } else {
                if (s.startsWith("xxcontrols")) {
                    int i = s.charAt(10) - 48;
                    switch (i) {
                        default:
                            break;

                        case 1:
                        case 2:
                            if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                            }
                            break;

                        case 3:
                            if (this.getEnergyPastArmor(8.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                                Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                            }
                            break;

                        case 4:
                        case 5:
                            if (this.getEnergyPastArmor(2.3F, shot) > 0.0F && World.Rnd().nextFloat() < 0.31F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                                Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                            }
                            break;
                    }
                }
                if (s.startsWith("xxspar")) {
                    Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                    if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                        this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                    }
                    if (s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                    }
                    if (s.startsWith("xxsparri") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                    }
                    if (s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                    }
                    if (s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                    }
                    if (s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                    }
                    if (s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                    }
                    if (s.startsWith("xxstabl") && this.getEnergyPastArmor(16.2F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                        this.nextDMGLevels(1, 2, "StabL_D" + this.chunkDamageVisible("StabL"), shot.initiator);
                    }
                    if (s.startsWith("xxstabr") && this.getEnergyPastArmor(16.2F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                        this.nextDMGLevels(1, 2, "StabR_D" + this.chunkDamageVisible("StabR"), shot.initiator);
                    }
                }
                if (s.startsWith("xxlock")) {
                    Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                    if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                    }
                    if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                    }
                    if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                    }
                    if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                    }
                    if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                    }
                }
                if (s.startsWith("xxeng")) {
                    Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                    if (s.endsWith("prop")) {
                        if (this.getEnergyPastArmor(0.45F / (Math.abs((float) Aircraft.v1.x) + 0.0001F), shot) > 0.0F && World.Rnd().nextFloat() < 0.05F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                        }
                    } else if (s.endsWith("case")) {
                        if (this.getEnergyPastArmor(5.1F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < shot.power / 175000F) {
                                this.FM.AS.setEngineStuck(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                            }
                            if (World.Rnd().nextFloat() < shot.power / 50000F) {
                                this.FM.AS.hitEngine(shot.initiator, 0, 2);
                                Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                            }
                            this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    } else if (s.startsWith("xxeng1cyls")) {
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.5F, 23.9F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.12F) {
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                            if (World.Rnd().nextFloat() < shot.power / 48000F) {
                                this.FM.AS.hitEngine(shot.initiator, 0, 3);
                                Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                            }
                            if (World.Rnd().nextFloat() < 0.005F) {
                                this.FM.AS.setEngineStuck(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                            }
                            this.getEnergyPastArmor(22.5F, shot);
                        }
                    } else if (s.endsWith("eqpt")) {
                        if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Engine Module: Magneto 0 Destroyed..");
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                                Aircraft.debugprintln(this, "*** Engine Module: Magneto 1 Destroyed..");
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                                Aircraft.debugprintln(this, "*** Engine Module: Prop Controls Cut..");
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                                Aircraft.debugprintln(this, "*** Engine Module: Throttle Controls Cut..");
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                                Aircraft.debugprintln(this, "*** Engine Module: Mix Controls Cut..");
                            }
                        }
                    } else if (s.endsWith("oil1")) {
                        this.FM.AS.hitOil(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                    }
                }
                if (s.startsWith("xxoil")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
                }
                if (s.startsWith("xxtank1") && this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.3F) {
                    if (this.FM.AS.astateTankStates[0] == 0) {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 0, 2);
                    }
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F) {
                        this.FM.AS.hitTank(shot.initiator, 0, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
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
                    if (s.endsWith("03")) {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        this.FM.AS.setJamBullets(1, 0);
                    }
                    if (s.endsWith("04")) {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        this.FM.AS.setJamBullets(1, 1);
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
                }
            }
        } else if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 1) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
            if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 1) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else j = s.charAt(5) - 49;
            this.hitFlesh(j, shot, byte0);
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

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if (explosion.chunkName != null) {
            if (explosion.chunkName.startsWith("CF")) {
                if (World.Rnd().nextFloat() < 0.01F) this.FM.AS.setControlsDamage(explosion.initiator, 0);
                if (World.Rnd().nextFloat() < 0.01F) this.FM.AS.setControlsDamage(explosion.initiator, 1);
                if (World.Rnd().nextFloat() < 0.01F) this.FM.AS.setControlsDamage(explosion.initiator, 2);
                if (World.Rnd().nextFloat() < 0.01F) this.FM.AS.hitPilot(explosion.initiator, 0, (int) (explosion.power * 8924F * World.Rnd().nextFloat()));
            }
            if (explosion.chunkName.startsWith("Tail")) {
                if (World.Rnd().nextFloat() < 0.01F) this.FM.AS.setControlsDamage(explosion.initiator, 1);
                if (World.Rnd().nextFloat() < 0.01F) this.FM.AS.setControlsDamage(explosion.initiator, 2);
            }
        }
        super.msgExplosion(explosion);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = 16.6F;
        float f2 = (float) (Math.random() * 2D) - 1.0F;
        float f3 = (float) (Math.random() * 2D) - 1.0F;
        float f4 = (float) (Math.random() * 2D) - 1.0F;
        float f5 = (float) (Math.random() * 2D) - 1.0F;
        float f6 = f1 / 20F;
        hiermesh.chunkSetAngles("SkiR1_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("SkiL1_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("SkiC1_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("SkiFrontDownWireL1_d0", 0.0F, -f6 * 10.5F, 0.0F);
        hiermesh.chunkSetAngles("SkiFrontDownWireL2_d0", 0.0F, -f6 * 11.2F, 0.0F);
        hiermesh.chunkSetAngles("SkiFrontUpWireL_d0", 0.0F, -f6 * -8.1F, 0.0F);
        hiermesh.chunkSetAngles("WireL1_d0", 0.0F, -f6 * 56.5F, f6 * -37F * f4);
        hiermesh.chunkSetAngles("WireL12_d0", 0.0F, -f6 * 77.3F, f6 * 28F * f5);
        float f7 = f6 * -5F;
        float f8 = f6 * 10F;
        float f9 = f6 * 15F;
        float f10 = f6 * 20F;
        float f11 = f6 * 5F * f2;
        float f12 = f6 * 5F * f4;
        float f13 = f6 * 10F * f2;
        float f14 = f6 * 10F * f4;
        float f15 = f6 * -5F * f3;
        float f16 = f6 * -5F * f5;
        hiermesh.chunkSetAngles("WireL2_d0", 0.0F, f7, f14);
        hiermesh.chunkSetAngles("WireL3_d0", 0.0F, f8, f12);
        hiermesh.chunkSetAngles("WireL4_d0", 0.0F, f9, f14);
        hiermesh.chunkSetAngles("WireL5_d0", 0.0F, f9, f12);
        hiermesh.chunkSetAngles("WireL6_d0", 0.0F, f9, f14);
        hiermesh.chunkSetAngles("WireL7_d0", 0.0F, f8, f12);
        hiermesh.chunkSetAngles("WireL8_d0", 0.0F, f9, f14);
        hiermesh.chunkSetAngles("WireL9_d0", 0.0F, f9, f12);
        hiermesh.chunkSetAngles("WireL10_d0", 0.0F, f8, f14);
        hiermesh.chunkSetAngles("WireL11_d0", 0.0F, f7, f12);
        hiermesh.chunkSetAngles("WireL13_d0", 0.0F, f8, f16);
        hiermesh.chunkSetAngles("WireL14_d0", 0.0F, f9, f16);
        hiermesh.chunkSetAngles("WireL15_d0", 0.0F, f9, f16);
        hiermesh.chunkSetAngles("WireL16_d0", 0.0F, f10, f16);
        hiermesh.chunkSetAngles("WireL17_d0", 0.0F, f8, 0.0F);
        hiermesh.chunkSetAngles("WireL18_d0", 0.0F, f8, f16);
        hiermesh.chunkSetAngles("WireL19_d0", 0.0F, f9, f16);
        hiermesh.chunkSetAngles("WireL20_d0", 0.0F, f8, f16);
        hiermesh.chunkSetAngles("WireL21_d0", 0.0F, f8, f16);
        hiermesh.chunkSetAngles("WireL22_d0", 0.0F, f9, f16);
        hiermesh.chunkSetAngles("SkiFrontDownWireR1_d0", 0.0F, -f6 * 10.5F, 0.0F);
        hiermesh.chunkSetAngles("SkiFrontDownWireR2_d0", 0.0F, -f6 * 11.2F, 0.0F);
        hiermesh.chunkSetAngles("SkiFrontUpWireR_d0", 0.0F, -f6 * -8.1F, 0.0F);
        hiermesh.chunkSetAngles("WireR1_d0", 0.0F, -f6 * 56.5F, f6 * -37F * f2);
        hiermesh.chunkSetAngles("WireR12_d0", 0.0F, -f6 * 77.3F, f6 * 28F * f3);
        hiermesh.chunkSetAngles("WireR2_d0", 0.0F, f7, f13);
        hiermesh.chunkSetAngles("WireR3_d0", 0.0F, f8, f11);
        hiermesh.chunkSetAngles("WireR4_d0", 0.0F, f8, f13);
        hiermesh.chunkSetAngles("WireR5_d0", 0.0F, f9, f11);
        hiermesh.chunkSetAngles("WireR6_d0", 0.0F, f9, f13);
        hiermesh.chunkSetAngles("WireR7_d0", 0.0F, f9, f11);
        hiermesh.chunkSetAngles("WireR8_d0", 0.0F, f8, f13);
        hiermesh.chunkSetAngles("WireR9_d0", 0.0F, f9, f11);
        hiermesh.chunkSetAngles("WireR10_d0", 0.0F, f9, f13);
        hiermesh.chunkSetAngles("WireR11_d0", 0.0F, f7, f11);
        hiermesh.chunkSetAngles("WireR13_d0", 0.0F, f8, f15);
        hiermesh.chunkSetAngles("WireR14_d0", 0.0F, f8, f15);
        hiermesh.chunkSetAngles("WireR15_d0", 0.0F, f9, f15);
        hiermesh.chunkSetAngles("WireR16_d0", 0.0F, f10, f15);
        hiermesh.chunkSetAngles("WireR17_d0", 0.0F, f9, 0.0F);
        hiermesh.chunkSetAngles("WireR18_d0", 0.0F, f8, f15);
        hiermesh.chunkSetAngles("WireR19_d0", 0.0F, f8, f15);
        hiermesh.chunkSetAngles("WireR20_d0", 0.0F, f9, f15);
        hiermesh.chunkSetAngles("WireR21_d0", 0.0F, f8, f15);
        hiermesh.chunkSetAngles("WireR22_d0", 0.0F, f9, f15);
    }

    protected void moveGear(float f) {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasBrakeControl = false;
        this.wireRandomizer1 = (float) (Math.random() * 2D) - 1.0F;
        this.wireRandomizer2 = (float) (Math.random() * 2D) - 1.0F;
        this.wireRandomizer3 = (float) (Math.random() * 2D) - 1.0F;
        this.wireRandomizer4 = (float) (Math.random() * 2D) - 1.0F;
    }

    public void sfxWheels() {
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("Engine1P_D0", 0.0F, 19F + 19F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);

            case 17:
                this.FM.hit(17);
                this.FM.hit(17);
                this.FM.hit(17);
                return false;

            case 18:
                this.FM.hit(18);
                this.FM.hit(18);
                this.FM.hit(18);
                return false;

            case 19:
                this.FM.Gears.hitCentreGear();
                this.FM.AS.hitTank(this, 0, 2);
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            boolean flag = false;
            float f1 = this.FM.CT.getAileron();
            float f2 = this.FM.CT.getElevator();
            this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 12F * f1, cvt(f2, -1F, 1.0F, -12F, 18F));
            this.hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - (cvt(f2, -1F, 0.0F, -36F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 32F)));
            this.hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f2, -1F, 0.0F, -62F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 44F));
            float f3 = Aircraft.cvt(this.FM.getSpeed(), 30F, 80F, 1.0F, 0.0F);
            float f4 = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 30F, 0.0F, 0.5F);
            if (this.FM.Gears.gWheelSinking[0] > 0.0F) {
                flag = true;
                this.skiAngleL = 0.5F * this.skiAngleL + 0.5F * this.FM.Or.getTangage();
                if (this.skiAngleL > 20F) this.skiAngleL = this.skiAngleL - this.spring;
                this.hierMesh().chunkSetAngles("SkiL1_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) - this.skiAngleL, World.Rnd().nextFloat(f4, f4));
            } else {
                if (this.skiAngleL > f3 * -10F + 0.01D) {
                    this.skiAngleL = this.skiAngleL - this.spring;
                    flag = true;
                } else if (this.skiAngleL < f3 * -10F - 0.01D) {
                    this.skiAngleL = this.skiAngleL + this.spring;
                    flag = true;
                }
                this.hierMesh().chunkSetAngles("SkiL1_D0", 0.0F, -this.skiAngleL, 0.0F);
            }
            if (this.FM.Gears.gWheelSinking[1] > 0.0F) {
                flag = true;
                this.skiAngleR = 0.5F * this.skiAngleR + 0.5F * this.FM.Or.getTangage();
                if (this.skiAngleR > 20F) this.skiAngleR = this.skiAngleR - this.spring;
                this.hierMesh().chunkSetAngles("SkiR1_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + this.skiAngleR, World.Rnd().nextFloat(f4, f4));
                if (this.FM.Gears.gWheelSinking[0] == 0.0F && this.FM.Or.getRoll() < 365F && this.FM.Or.getRoll() > 355F) {
                    this.skiAngleL = this.skiAngleR;
                    this.hierMesh().chunkSetAngles("SkiL1_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) - this.skiAngleL, World.Rnd().nextFloat(f4, f4));
                }
            } else {
                if (this.skiAngleR > f3 * -10F + 0.01D) {
                    this.skiAngleR = this.skiAngleR - this.spring;
                    flag = true;
                } else if (this.skiAngleR < f3 * -10F - 0.01D) {
                    this.skiAngleR = this.skiAngleR + this.spring;
                    flag = true;
                }
                this.hierMesh().chunkSetAngles("SkiR1_D0", 0.0F, this.skiAngleR, 0.0F);
            }
            if (!flag && f3 == 0.0F) {
                super.moveFan(f);
                return;
            }
            this.hierMesh().chunkSetAngles("SkiC1_D0", 0.0F, -((this.skiAngleL + this.skiAngleR) / 2.0F), 0.0F);
            float f5 = this.skiAngleL / 20F;
            this.hierMesh().chunkSetAngles("SkiFrontDownWireL1_d0", 0.0F, -f5 * 10.5F, 0.0F);
            this.hierMesh().chunkSetAngles("SkiFrontDownWireL2_d0", 0.0F, -f5 * 11.2F, 0.0F);
            this.hierMesh().chunkSetAngles("SkiFrontUpWireL_d0", 0.0F, -f5 * -8.1F, 0.0F);
            if (this.skiAngleL < 0.0F) {
                this.hierMesh().chunkSetAngles("WireL1_d0", 0.0F, f5 * -15F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL12_d0", 0.0F, f5 * -15F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL2_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL3_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL4_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL5_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL6_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL7_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL8_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL9_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL10_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL11_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL13_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL14_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL15_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL16_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL17_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL18_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL19_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL20_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL21_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireL22_d0", 0.0F, 0.0F, 0.0F);
            } else {
                float f6 = 1.0F;
                if (f5 < 0.5F) f6 = Aircraft.cvt(f5, 0.0F, 0.5F, 1.0F, 0.95F);
                else f6 = Aircraft.cvt(f5, 0.5F, 1.0F, 0.95F, 1.0F);
                this.hierMesh().chunkSetAngles("WireL1_d0", 0.0F, -f5 * (56.5F * f6), f5 * (-37F * f3) * this.wireRandomizer3);
                this.hierMesh().chunkSetAngles("WireL12_d0", 0.0F, -f5 * (77.3F * f6), f5 * (28F * f3) * this.wireRandomizer4);
                float f8 = f5 * -5F;
                float f10 = f5 * 10F;
                float f12 = f5 * 15F;
                float f14 = f5 * 20F;
                float f16 = f5 * (5F * f3) * this.wireRandomizer3;
                float f18 = f5 * (10F * f3) * this.wireRandomizer3;
                float f20 = f5 * (-5F * f3) * this.wireRandomizer4;
                this.hierMesh().chunkSetAngles("WireL2_d0", 0.0F, f8, f18);
                this.hierMesh().chunkSetAngles("WireL3_d0", 0.0F, f10, f16);
                this.hierMesh().chunkSetAngles("WireL4_d0", 0.0F, f12, f18);
                this.hierMesh().chunkSetAngles("WireL5_d0", 0.0F, f12, f16);
                this.hierMesh().chunkSetAngles("WireL6_d0", 0.0F, f12, f18);
                this.hierMesh().chunkSetAngles("WireL7_d0", 0.0F, f10, f16);
                this.hierMesh().chunkSetAngles("WireL8_d0", 0.0F, f12, f18);
                this.hierMesh().chunkSetAngles("WireL9_d0", 0.0F, f12, f16);
                this.hierMesh().chunkSetAngles("WireL10_d0", 0.0F, f10, f18);
                this.hierMesh().chunkSetAngles("WireL11_d0", 0.0F, f8, f16);
                this.hierMesh().chunkSetAngles("WireL13_d0", 0.0F, f10, f20);
                this.hierMesh().chunkSetAngles("WireL14_d0", 0.0F, f12, f20);
                this.hierMesh().chunkSetAngles("WireL15_d0", 0.0F, f12, f20);
                this.hierMesh().chunkSetAngles("WireL16_d0", 0.0F, f14, f20);
                this.hierMesh().chunkSetAngles("WireL17_d0", 0.0F, f10, 0.0F);
                this.hierMesh().chunkSetAngles("WireL18_d0", 0.0F, f10, f20);
                this.hierMesh().chunkSetAngles("WireL19_d0", 0.0F, f12, f20);
                this.hierMesh().chunkSetAngles("WireL20_d0", 0.0F, f10, f20);
                this.hierMesh().chunkSetAngles("WireL21_d0", 0.0F, f10, f20);
                this.hierMesh().chunkSetAngles("WireL22_d0", 0.0F, f12, f20);
            }
            f5 = this.skiAngleR / 20F;
            this.hierMesh().chunkSetAngles("SkiFrontDownWireR1_d0", 0.0F, -f5 * 10.5F, 0.0F);
            this.hierMesh().chunkSetAngles("SkiFrontDownWireR2_d0", 0.0F, -f5 * 11.2F, 0.0F);
            this.hierMesh().chunkSetAngles("SkiFrontUpWireR_d0", 0.0F, -f5 * -8.1F, 0.0F);
            if (this.skiAngleR < 0.0F) {
                this.hierMesh().chunkSetAngles("WireR1_d0", 0.0F, f5 * -15F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR12_d0", 0.0F, f5 * -15F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR2_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR3_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR4_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR5_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR6_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR7_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR8_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR9_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR10_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR11_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR13_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR14_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR15_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR16_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR17_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR18_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR19_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR20_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR21_d0", 0.0F, 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("WireR22_d0", 0.0F, 0.0F, 0.0F);
            } else {
                float f7 = 1.0F;
                if (f5 < 0.5F) f7 = Aircraft.cvt(f5, 0.0F, 0.5F, 1.0F, 0.95F);
                else f7 = Aircraft.cvt(f5, 0.5F, 1.0F, 0.95F, 1.0F);
                this.hierMesh().chunkSetAngles("WireR1_d0", 0.0F, -f5 * (56.5F * f7), f5 * (-37F * f3) * this.wireRandomizer1);
                this.hierMesh().chunkSetAngles("WireR12_d0", 0.0F, -f5 * (77.3F * f7), f5 * (28F * f3) * this.wireRandomizer2);
                float f9 = f5 * -5F;
                float f11 = f5 * 10F;
                float f13 = f5 * 15F;
                float f15 = f5 * 20F;
                float f17 = f5 * (5F * f3) * this.wireRandomizer1;
                float f19 = f5 * (10F * f3) * this.wireRandomizer1;
                float f21 = f5 * (-5F * f3) * this.wireRandomizer2;
                this.hierMesh().chunkSetAngles("WireR2_d0", 0.0F, f9, f19);
                this.hierMesh().chunkSetAngles("WireR3_d0", 0.0F, f11, f17);
                this.hierMesh().chunkSetAngles("WireR4_d0", 0.0F, f11, f19);
                this.hierMesh().chunkSetAngles("WireR5_d0", 0.0F, f13, f17);
                this.hierMesh().chunkSetAngles("WireR6_d0", 0.0F, f13, f19);
                this.hierMesh().chunkSetAngles("WireR7_d0", 0.0F, f13, f17);
                this.hierMesh().chunkSetAngles("WireR8_d0", 0.0F, f11, f19);
                this.hierMesh().chunkSetAngles("WireR9_d0", 0.0F, f13, f17);
                this.hierMesh().chunkSetAngles("WireR10_d0", 0.0F, f13, f19);
                this.hierMesh().chunkSetAngles("WireR11_d0", 0.0F, f9, f17);
                this.hierMesh().chunkSetAngles("WireR13_d0", 0.0F, f11, f21);
                this.hierMesh().chunkSetAngles("WireR14_d0", 0.0F, f11, f21);
                this.hierMesh().chunkSetAngles("WireR15_d0", 0.0F, f13, f21);
                this.hierMesh().chunkSetAngles("WireR16_d0", 0.0F, f15, f21);
                this.hierMesh().chunkSetAngles("WireR17_d0", 0.0F, f13, 0.0F);
                this.hierMesh().chunkSetAngles("WireR18_d0", 0.0F, f11, f21);
                this.hierMesh().chunkSetAngles("WireR19_d0", 0.0F, f11, f21);
                this.hierMesh().chunkSetAngles("WireR20_d0", 0.0F, f13, f21);
                this.hierMesh().chunkSetAngles("WireR21_d0", 0.0F, f11, f21);
                this.hierMesh().chunkSetAngles("WireR22_d0", 0.0F, f13, f21);
            }
        }
        super.moveFan(f);
    }

    private float skiAngleL;
    private float skiAngleR;
    private float spring;
    private float wireRandomizer1;
    private float wireRandomizer2;
    private float wireRandomizer3;
    private float wireRandomizer4;

    static {
        Class class1 = I_16FixedSkis.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
