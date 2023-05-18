package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class BF_109Z extends Scheme2 implements TypeFighter {

    public BF_109Z() {
        this.kangle = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = cvt(f, 0.05F, 0.49F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", -33.5F * f1, 0.0F, 0.0F);
        f1 = cvt(f, 0.12F, 0.95F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 77.5F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 33.5F * f1, 0.0F, 0.0F);
        f1 = cvt(f, 0.3F, 0.82F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 33.5F * f1, 0.0F, 0.0F);
        f1 = cvt(f, 0.34F, 0.78F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -77.5F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", -33.5F * f1, 0.0F, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("GearL6_D0", 0.0F, -this.FM.Gears.gWheelAngles[0], 0.0F);
        this.hierMesh().chunkSetAngles("GearR6_D0", 0.0F, -this.FM.Gears.gWheelAngles[1], 0.0F);
        this.hierMesh().chunkSetAngles("GearC4_D0", 0.0F, -this.FM.Gears.gWheelAngles[2], 0.0F);
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
        }
        this.hierMesh().chunkSetAngles("Flap01L_D0", 0.0F, -20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap01U_D0", 0.0F, 20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02L_D0", 0.0F, -20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02U_D0", 0.0F, 20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap01L2_D0", 0.0F, -20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap01U2_D0", 0.0F, 20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02L2_D0", 0.0F, -20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02U2_D0", 0.0F, 20F * this.kangle, 0.0F);
        this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        if (this.kangle > 1.0F) this.kangle = 1.0F;
        super.update(f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateTankStates[2] > 5) this.FM.AS.repairTank(2);
            if (this.FM.AS.astateTankStates[3] > 5) this.FM.AS.repairTank(3);
        }
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 12:
            case 14:
            case 15:
            case 16:
            case 17:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            default:
                break;

            case 3:
            case 4:
                return false;

            case 18:
                if (World.Rnd().nextFloat() >= 0.5F) break;
                this.cut("Keel2");
                if (World.Rnd().nextFloat() < 0.5F) this.cut("Tail2");
                break;

            case 13:
                this.cut("WingRIn");
                this.cut("Engine2");
                this.cut("Tail2");
                this.FM.cut(36, j, actor);
                this.FM.cut(4, j, actor);
                this.FM.cut(20, j, actor);
                if (World.Rnd().nextFloat() < 0.5F) {
                    this.cut("StabR");
                    this.FM.cut(18, j, actor);
                    this.FM.cut(17, j, actor);
                }
                break;

            case 19:
                this.cut("StabR");
                this.FM.cut(18, j, actor);
                this.FM.cut(17, j, actor);
                break;

            case 20:
                this.cut("StabR");
                this.FM.cut(18, j, actor);
                this.FM.cut(17, j, actor);
                break;

            case 36:
                this.cut("Engine2");
                this.cut("Nose");
                this.cut("Tail2");
                this.FM.cut(4, j, actor);
                this.FM.cut(13, j, actor);
                this.FM.cut(20, j, actor);
                break;

            case 9:
                this.cut("GearL4");
                break;

            case 10:
                this.cut("GearR4");
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    debugprintln(this, "*** Armor Glass: Hit..");
                    if (shot.power <= 0.0F) {
                        debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        if (World.Rnd().nextFloat() < 0.5F) this.doRicochetBack(shot);
                    }
                } else if (s.endsWith("p2")) this.getEnergyPastArmor(0.5F, shot);
                else if (s.endsWith("p3")) {
                    if (point3d.z < -0.27D) this.getEnergyPastArmor(4.1D / (Math.abs(v1.z) + 9.9999997473787516E-006D), shot);
                    else this.getEnergyPastArmor(8.1D / (Math.abs(v1.x) + 9.9999997473787516E-006D), shot);
                } else if (s.endsWith("p4")) this.getEnergyPastArmor(10.1D / (Math.abs(v1.z) + 9.9999997473787516E-006D), shot);
                else if (s.endsWith("p5")) this.getEnergyPastArmor(10.1D / (Math.abs(v1.x) + 9.9999997473787516E-006D), shot);
                else if (s.endsWith("p6")) this.getEnergyPastArmor(12.1D / (Math.abs(v1.x) + 9.9999997473787516E-006D), shot);
                else if (s.endsWith("a1")) {
                    if (World.Rnd().nextFloat() < 0.5F) shot.powerType = 0;
                    this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 7F), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                if (s.length() > 11) i = 10 + s.charAt(11) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 4:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                        }
                        break;

                    case 2:
                    case 3:
                    case 8:
                        if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            debugprintln(this, "*** Aileron Controls: Disabled..");
                        }
                        break;

                    case 5:
                    case 6:
                    case 10:
                    case 11:
                        if (this.getEnergyPastArmor(0.002F, shot) > 0.0F && World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            debugprintln(this, "*** Elevator Controls: Disabled / Strings Broken..");
                        }
                        break;

                    case 7:
                    case 9:
                        if (this.getEnergyPastArmor(2.3F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            debugprintln(this, "*** Rudder Controls: Disabled..");
                        }
                        break;

                    case 12:
                        if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                            debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 13:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart1") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F) {
                    debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if (s.startsWith("xxspart2") && this.chunkDamageVisible("Tail2") > 2 && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F) {
                    debugprintln(this, "*** Tail2 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail2_D3", shot.initiator);
                }
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxwj")) {
                if (this.getEnergyPastArmor(12.5F, shot) > 0.0F) {
                    if (s.endsWith("l1")) {
                        debugprintln(this, "*** WingL Console Lock Destroyed..");
                        this.nextDMGLevels(4, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                    }
                    if (s.endsWith("l2") || s.endsWith("r2")) {
                        debugprintln(this, "*** WingR Console Lock Destroyed..");
                        this.nextDMGLevels(4, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                    }
                    if (s.endsWith("r1")) {
                        debugprintln(this, "*** WingR Outer Console Lock Destroyed..");
                        this.nextDMGLevels(4, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                    }
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr1") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockr2") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Rudder2 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int j = s.charAt(5) - 49;
                debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("pipe")) {
                    if (World.Rnd().nextFloat() < 0.1F && this.FM.CT.Weapons[0] != null) {
                        this.FM.AS.setJamBullets(0, j);
                        debugprintln(this, "*** Engine" + j + ": Nose Nozzle Pipe Bent..");
                    }
                    this.getEnergyPastArmor(0.3F, shot);
                } else if (s.endsWith("prop")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
                        debugprintln(this, "*** Engine" + j + ": Prop Governor Hit, Disabled..");
                    } else {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        debugprintln(this, "*** Engine" + j + ": Prop Governor Hit, Damaged..");
                    }
                } else if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.EI.engines[j].setEngineStuck(shot.initiator);
                        debugprintln(this, "*** Engine" + j + ": Bullet Jams Reductor Gear..");
                    } else {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        debugprintln(this, "*** Engine" + j + ": Reductor Gear Damaged, Prop Governor Failed..");
                    }
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        debugprintln(this, "*** Engine" + j + ": Supercharger Disabled..");
                    }
                } else if (s.endsWith("feed")) {
                    if (this.getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F && this.FM.EI.engines[j].getPowerOutput() > 0.7F) {
                        this.FM.AS.hitEngine(shot.initiator, j, 100);
                        debugprintln(this, "*** Engine" + j + ": Pressurized Fuel Line Pierced, Fuel Flamed..");
                    }
                } else if (s.endsWith("fuel")) {
                    if (this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
                        this.FM.EI.engines[j].setEngineStops(shot.initiator);
                        debugprintln(this, "*** Engine" + j + ": Fuel Line Stalled, Engine Stalled..");
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 175000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                            debugprintln(this, "*** Engine" + j + ": Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 50000F) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            debugprintln(this, "*** Engine" + j + ": Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
                        }
                        this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        debugprintln(this, "*** Engine" + j + ": Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(22.5F, shot);
                } else if (s.endsWith("cyl1") || s.endsWith("cyl2")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 1.75F) {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        debugprintln(this, "*** Engine" + j + ": Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 24000F) {
                            this.FM.AS.hitEngine(shot.initiator, j, 3);
                            debugprintln(this, "*** Engine" + j + ": Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.01F) {
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                            debugprintln(this, "*** Engine" + j + ": Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.endsWith("mag1") || s.endsWith("mag2")) {
                    int j1 = s.charAt(9) - 49;
                    this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, j1);
                    debugprintln(this, "*** Engine" + j + ": Magneto " + j1 + " Destroyed..");
                } else if (s.endsWith("sync")) {
                    if (this.getEnergyPastArmor(2.1F, shot) <= 0.0F || World.Rnd().nextFloat() >= 0.5F);
                } else if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, j);
                    debugprintln(this, "*** Engine" + j + ": Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                    int k = s.charAt(5) - 49;
                    if (k == 7) k = 0;
                    if (k == 8) k = 1;
                    this.FM.AS.hitOil(shot.initiator, k);
                    this.getEnergyPastArmor(0.22F, shot);
                    debugprintln(this, "*** Engine" + k + ": Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 48;
                switch (l) {
                    default:
                        break;

                    case 1:
                    case 2:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                            if (this.FM.AS.astateTankStates[2] == 0) {
                                debugprintln(this, "*** Fuel Tank: Pierced..");
                                this.FM.AS.hitTank(shot.initiator, 2, 1);
                                this.FM.AS.doSetTankState(shot.initiator, 2, 1);
                            }
                            if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) {
                                this.FM.AS.hitTank(shot.initiator, 2, 2);
                                debugprintln(this, "*** Fuel Tank: Hit..");
                            }
                        }
                        break;

                    case 3:
                    case 4:
                        if (this.getEnergyPastArmor(0.1F, shot) <= 0.0F || World.Rnd().nextFloat() >= 0.25F) break;
                        if (this.FM.AS.astateTankStates[3] == 0) {
                            debugprintln(this, "*** Fuel Tank: Pierced..");
                            this.FM.AS.hitTank(shot.initiator, 3, 1);
                            this.FM.AS.doSetTankState(shot.initiator, 3, 1);
                        }
                        if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.hitTank(shot.initiator, 3, 2);
                            debugprintln(this, "*** Fuel Tank: Hit..");
                        }
                        break;

                    case 5:
                        if (this.getEnergyPastArmor(0.1F, shot) <= 0.0F || World.Rnd().nextFloat() >= 0.25F) break;
                        if (this.FM.AS.astateTankStates[1] == 0) {
                            debugprintln(this, "*** Fuel Tank: Pierced..");
                            this.FM.AS.hitTank(shot.initiator, 1, 1);
                            this.FM.AS.doSetTankState(shot.initiator, 1, 1);
                        }
                        if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.hitTank(shot.initiator, 1, 2);
                            debugprintln(this, "*** Fuel Tank: Hit..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxcannon")) {
                if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) {
                    int i1 = s.charAt(9) - 49;
                    debugprintln(this, "*** Cannon(" + i1 + "): Disabled..");
                    this.FM.AS.setJamBullets(0, i1);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
                }
                return;
            }
            if (s.startsWith("xxammo")) return;
            else return;
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (s.startsWith("xcockpit")) {
                if (point3d.z > 0.4D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                    if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                } else if (point3d.y > 1.765D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if (point3d.x > 0.2D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xtail1")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xtail2")) {
            if (this.chunkDamageVisible("Tail2") < 3) this.hitChunk("Tail2", shot);
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) this.hitChunk("Keel2", shot);
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 1) this.hitChunk("Rudder2", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 1) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k1;
            if (s.endsWith("a")) {
                byte0 = 1;
                k1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k1 = s.charAt(6) - 49;
            } else k1 = s.charAt(5) - 49;
            this.hitFlesh(k1, shot, byte0);
        }
    }

    public void doWoundPilot(int i, float f) {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                if (!this.FM.AS.bIsAboutToBailout) {
                    if (this.hierMesh().isChunkVisible("Blister1_D0")) this.hierMesh().chunkVisible("Gore1_D0", true);
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    private float kangle;

    static {
        Class class1 = BF_109Z.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109Z/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109Z.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109Z.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 9, 9, 9, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON05", "_CANNON04", "_ExternalDev01", "_ExternalDev03", "_ExternalDev02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
