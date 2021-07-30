package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.MGunMK108k;
import com.maddox.rts.Property;

public abstract class BF_110 extends Scheme2a implements TypeFighter, TypeBNZFighter, TypeStormovik {

    public BF_110() {
        this.kangle0 = 0.0F;
        this.kangle1 = 0.0F;
        this.slpos = 0.0F;
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 30F * f, 0.0F);
    }

    public void update(float f) {
        if (this.FM.getSpeed() > 5F) {
            this.slpos = 0.7F * this.slpos + 0.13F * (this.FM.getAOA() <= 6.6F ? 0.0F : 0.07F);
            this.resetYPRmodifier();
            xyz[0] = this.slpos;
            this.hierMesh().chunkSetLocate("SlatL_D0", xyz, ypr);
            this.hierMesh().chunkSetLocate("SlatR_D0", xyz, ypr);
        }
        this.hierMesh().chunkSetAngles("WaterL_D0", 0.0F, 15F - 30F * this.kangle0, 0.0F);
        this.kangle0 = 0.95F * this.kangle0 + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        this.hierMesh().chunkSetAngles("WaterR_D0", 0.0F, 15F - 30F * this.kangle1, 0.0F);
        this.kangle1 = 0.95F * this.kangle1 + 0.05F * this.FM.EI.engines[1].getControlRadiator();
        super.update(f);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("p1")) {
                    if (Math.abs(point3d.y) > 0.231D) this.getEnergyPastArmor(8.585D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                    else this.getEnergyPastArmor(1.0F, shot);
                } else if (s.endsWith("p2")) this.getEnergyPastArmor(World.Rnd().nextFloat(1.96F, 3.4839F), shot);
                else if (s.endsWith("p3")) {
                    if (point3d.z < 0.08D) this.getEnergyPastArmor(8.585D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    else {
                        if (point3d.z < 0.09D) {
                            this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
                            return;
                        }
                        if (point3d.y > 0.175D && point3d.y < 0.287D && point3d.z < 0.177D) {
                            this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
                            return;
                        }
                        if (point3d.y > -0.334D && point3d.y < -0.177D && point3d.z < 0.204D) {
                            this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
                            return;
                        }
                        if (point3d.z > 0.288D && Math.abs(point3d.y) < 0.077D) this.getEnergyPastArmor(World.Rnd().nextFloat(8.5F, 12.46F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        else this.getEnergyPastArmor(10.51D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    }
                } else if (s.endsWith("p4")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    this.debuggunnery("Armor: Armor Glass: Hit..");
                    if (shot.power <= 0.0F) {
                        this.debuggunnery("Armor: Armor Glass: Bullet Stopped..");
                        if (World.Rnd().nextFloat() < 0.96F) this.doRicochetBack(shot);
                    }
                } else if (s.endsWith("p5")) this.getEnergyPastArmor(5.51D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("p6")) {
                    if (point3d.z > 0.448D) {
                        if (point3d.z > 0.609D && Math.abs(point3d.y) > 0.251D) {
                            this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
                            return;
                        }
                        this.getEnergyPastArmor(10.604999542236328D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    } else if (Math.abs(point3d.y) > 0.264D) {
                        if (point3d.z > 0.021D) this.getEnergyPastArmor(8.51D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        else {
                            this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
                            return;
                        }
                    } else {
                        if (point3d.z < -0.352D && Math.abs(point3d.y) < 0.04D) {
                            this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
                            return;
                        }
                        this.getEnergyPastArmor(8.06D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    }
                } else if (s.endsWith("p7")) this.getEnergyPastArmor(6.06D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("p8")) {
                    if (point3d.y > 0.112D && point3d.z < -0.319D || point3d.y < -0.065D && point3d.z > 0.038D && point3d.z < 0.204D) {
                        this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
                        return;
                    }
                    this.getEnergyPastArmor(8.06D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                } else if (s.endsWith("p9")) {
                    if (point3d.z > 0.611D && point3d.z < 0.674D && Math.abs(point3d.y) < 0.0415D) {
                        this.debuggunnery("Armor: Bullet Went Through a Pilofacturing Hole..");
                        return;
                    }
                    this.getEnergyPastArmor(8.06D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                if (World.Rnd().nextFloat() < 0.99F) return;
                int i = s.charAt(10) - 48;
                if (s.endsWith("10")) i = 10;
                if (s.endsWith("11")) i = 11;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 2:
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.02F, 2.351F), shot) > 0.0F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            this.debuggunnery("Controls: Throttle Quadrant: Hit, Engine Controls Disabled..");
                        }
                        break;

                    case 3:
                        if (this.getEnergyPastArmor(3.5F, shot) <= 0.0F) break;
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Controls: Aileron Controls: Fuselage Line Destroyed..");
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Controls: Elevator Controls: Fuselage Line Destroyed..");
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Controls: Rudder Controls: Fuselage Line Destroyed..");
                        }
                        break;

                    case 4:
                    case 5:
                        if (this.getEnergyPastArmor(0.002F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        }
                        break;

                    case 6:
                    case 7:
                    case 10:
                    case 11:
                        if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Controls: Aileron Controls: Disabled..");
                        }
                        break;

                    case 8:
                    case 9:
                        if (this.getEnergyPastArmor(6.8F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Controls: Aileron Controls: Crank Destroyed..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                    this.debuggunnery("Spar Construction: Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(13.2F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(13.2F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxwj")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(12.5F, 55.96F), shot) > 0.0F) if (s.endsWith("l")) {
                    this.debuggunnery("Spar Construction: WingL Console Lock Destroyed..");
                    this.nextDMGLevels(4, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                } else {
                    this.debuggunnery("Spar Construction: WingR Console Lock Destroyed..");
                    this.nextDMGLevels(4, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr")) {
                    int j = s.charAt(6) - 48;
                    if (this.getEnergyPastArmor(6.56F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) if (j < 3) {
                        this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                    } else {
                        this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                    }
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int k = s.charAt(5) - 49;
                this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Hit..");
                if (s.endsWith("prop")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 3);
                        this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Prop Governor Hit, Disabled..");
                    } else {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 4);
                        this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Prop Governor Hit, Damaged..");
                    }
                } else if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.EI.engines[k].setEngineStuck(shot.initiator);
                        this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Bullet Jams Reductor Gear..");
                    } else {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 3);
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 4);
                        this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Reductor Gear Damaged, Prop Governor Failed..");
                    }
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 0);
                        this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Supercharger Disabled..");
                    }
                } else if (s.endsWith("feed")) {
                    if (this.getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F && this.FM.EI.engines[k].getPowerOutput() > 0.7F) {
                        this.FM.AS.hitEngine(shot.initiator, k, 100);
                        this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Pressurized Fuel Line Pierced, Fuel Flamed..");
                    }
                } else if (s.endsWith("fuel")) {
                    if (this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
                        this.FM.EI.engines[k].setEngineStops(shot.initiator);
                        this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Fuel Line Stalled, Engine Stalled..");
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 175000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, k);
                            this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 50000F) {
                            this.FM.AS.hitEngine(shot.initiator, k, 2);
                            this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[k].getReadyness() + "..");
                        }
                        this.FM.EI.engines[k].setReadyness(shot.initiator, this.FM.EI.engines[k].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[k].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(22.5F, 33.6F), shot);
                } else if (s.endsWith("cyl1") || s.endsWith("cyl2")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[k].getCylindersRatio() * 1.75F) {
                        this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Cylinders Hit, " + this.FM.EI.engines[k].getCylindersOperable() + "/" + this.FM.EI.engines[k].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 24000F) {
                            this.FM.AS.hitEngine(shot.initiator, k, 3);
                            this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.01F) {
                            this.FM.AS.setEngineStuck(shot.initiator, k);
                            this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.endsWith("mag1") || s.endsWith("mag2")) {
                    int j2 = s.charAt(9) - 49;
                    this.FM.EI.engines[k].setMagnetoKnockOut(shot.initiator, j2);
                    this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Magneto " + j2 + " Destroyed..");
                } else if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, k);
                    this.debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 2.345F), shot) > 0.0F) {
                    int l = s.charAt(5) - 49;
                    this.FM.AS.hitOil(shot.initiator, l);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module (" + (l != 0 ? "Right" : "Left") + "): Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxw")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 0.75F), shot) > 0.0F) {
                    int i1 = s.charAt(3) - 49;
                    if (this.FM.AS.astateEngineStates[i1] == 0) {
                        this.debuggunnery("Engine Module (" + (i1 != 0 ? "Right" : "Left") + "): Water Radiator Pierced..");
                        this.FM.AS.hitEngine(shot.initiator, i1, 2);
                        this.FM.AS.doSetEngineState(shot.initiator, i1, 2);
                    }
                    this.getEnergyPastArmor(2.22F, shot);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j1 = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.23F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    if (this.FM.AS.astateTankStates[j1] == 0) {
                        this.debuggunnery("Fuel Tank " + (j1 + 1) + ": Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j1, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j1, 1);
                    } else if (this.FM.AS.astateTankStates[j1] == 1) {
                        this.debuggunnery("Fuel Tank " + (j1 + 1) + ": Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j1, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j1, 2);
                    }
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.hitTank(shot.initiator, 2, 2);
                        this.debuggunnery("Fuel Tank " + (j1 + 1) + ": Hit..");
                    }
                }
                return;
            }
            if (s.startsWith("xxhyd")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F) {
                    this.debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                if (s.endsWith("03")) {
                    this.debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 2);
                }
                if (s.endsWith("04")) {
                    this.debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 3);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 12.96F), shot);
            }
            if (s.startsWith("xxcannon")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Cowling Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Cowling Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if (s.startsWith("xxmgff")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Cowling Cannon (MGFF): Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Cowling Cannon (MGFF): Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if (s.startsWith("xxmk")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Cowling Cannon (Mk 108): Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Cowling Cannon (Mk 108): Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(24.5F, 96.87F), shot);
            }
            return;
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit") || s.startsWith("xnose")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (s.startsWith("xcockpit")) {
                if (point3d.x > 1.857D && point3d.z > 0.416D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            }
        } else if (s.startsWith("xengine")) {
            int k1 = s.charAt(7) - 48;
            if (this.chunkDamageVisible("Engine" + k1) < 2) this.hitChunk("Engine" + k1, shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            int l1 = s.charAt(5) - 48;
            if (this.chunkDamageVisible("Keel" + l1) < 3) this.hitChunk("Keel" + l1, shot);
        } else if (s.startsWith("xrudder")) {
            int i2 = s.charAt(7) - 48;
            if (this.chunkDamageVisible("Rudder" + i2) < 1) this.hitChunk("Rudder" + i2, shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 3) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 3) this.hitChunk("StabR", shot);
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
            if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.endsWith("2")) {
                if (World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(6.8F, 29.35F), shot) > 0.0F) {
                    this.debuggunnery("Undercarriage: Stuck..");
                    this.FM.AS.setInternalDamage(shot.initiator, 3);
                }
                String s1 = "" + s.charAt(5);
                this.hitChunk("Gear" + s1.toUpperCase() + "2", shot);
            }
        } else if (s.startsWith("xturret")) {
            if (this.getEnergyPastArmor(0.25F, shot) > 0.0F) {
                this.debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                this.FM.AS.setJamBullets(10, 0);
                this.FM.AS.setJamBullets(10, 1);
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k2;
            if (s.endsWith("a")) {
                byte0 = 1;
                k2 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k2 = s.charAt(6) - 49;
            } else k2 = s.charAt(5) - 49;
            this.hitFlesh(k2, shot, byte0);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 120F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, cvt(f, 0.0F, 0.1F, 0.0F, -50.5F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.0F, 0.1F, 0.0F, -50.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 120F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, cvt(f, 0.0F, 0.1F, 0.0F, -50.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.0F, 0.1F, 0.0F, -50.5F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                if (!this.FM.AS.bIsAboutToBailout) this.hierMesh().chunkVisible("Gore1_D0", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                if (!this.FM.AS.bIsAboutToBailout) this.hierMesh().chunkVisible("Gore2_D0", true);
                break;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 33:
                this.hitProp(0, j, actor);
                break;

            case 36:
                this.hitProp(1, j, actor);
                break;

            case 12:
            case 18:
            case 19:
                this.hierMesh().chunkVisible("Wire_D0", false);
                break;

            case 34:
                this.FM.AS.hitEngine(this, 0, 2);
                if (World.Rnd().nextFloat() < 0.66F) this.FM.AS.hitEngine(this, 0, 2);
                break;

            case 37:
                this.FM.AS.hitEngine(this, 1, 2);
                if (World.Rnd().nextFloat() < 0.66F) this.FM.AS.hitEngine(this, 1, 2);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.06F) this.FM.AS.hitTank(this, 0, 1);
            if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.06F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            if (this.FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
            if (this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.08F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.08F) this.FM.AS.hitTank(this, 0, 1);
            if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.08F) this.FM.AS.hitTank(this, 3, 1);
            if (this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.08F) this.FM.AS.hitTank(this, 2, 1);
        }
        for (int i = 1; i < 3; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -37F) {
                    f = -37F;
                    flag = false;
                }
                if (f > 37F) {
                    f = 37F;
                    flag = false;
                }
                if (f1 < -19F) {
                    f1 = -19F;
                    flag = false;
                }
                if (f1 > 27F) {
                    f1 = 27F;
                    flag = false;
                }
                if (Math.abs(f) > 17.8F && Math.abs(f) < 25F && f1 < -12F) flag = false;
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this instanceof BF_110G2) {
            Object aobj[] = this.pos.getBaseAttached();
            if (aobj == null) return;
            for (int i = 0; i < aobj.length; i++) {
                if (aobj[i] instanceof Bomb) this.hierMesh().chunkVisible("Rack_D0", true);
                if (aobj[i] instanceof MGunMK108k) {
                    this.hierMesh().chunkVisible("4xMG17", false);
                    this.hierMesh().chunkVisible("2xMK108", true);
                }
            }

        }
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap1_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap2_D0", 0.0F, f1, 0.0F);
    }

    private float kangle0;
    private float kangle1;
    private float slpos;

    static {
        Class class1 = BF_110.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
