package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class MC_200xyz extends Scheme1 implements TypeFighter {

    public MC_200xyz() {
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
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.05F, 0.78F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.05F, 0.1376F, 0.0F, -80F), 0.0F);
        float f1 = cvt(f, 0.05F, 0.1376F, 0.0F, 1.0F);
        f1 = (float) Math.sqrt(f1);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -103F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL9_D0", 0.0F, cvt(f, 0.05F, 0.78F, 0.0F, -129F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.25F, 0.98F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.25F, 0.3376F, 0.0F, -80F), 0.0F);
        f1 = cvt(f, 0.25F, 0.3376F, 0.0F, 1.0F);
        f1 = (float) Math.sqrt(f1);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -103F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR9_D0", 0.0F, cvt(f, 0.25F, 0.98F, 0.0F, -129F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.2085F, 0.0F, 0.2085F);
        this.hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.2085F, 0.0F, 0.2085F);
        this.hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    public void update(float f) {
        float f1 = cvt(this.FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -32.2F);
        this.hierMesh().chunkSetAngles("Water1_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Water2_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Water3_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Water4_D0", 0.0F, f1, 0.0F);
        super.update(f);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.startsWith("xxarmorp")) {
                    int i = s.charAt(8) - 48;
                    switch (i) {
                        default:
                            break;

                        case 1:
                            this.getEnergyPastArmor(22.76D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                            if (shot.power <= 0.0F) this.doRicochetBack(shot);
                            break;

                        case 2:
                            this.getEnergyPastArmor(9.366F, shot);
                            break;

                        case 3:
                            this.getEnergyPastArmor(12.7D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                            break;
                    }
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                switch (j) {
                    default:
                        break;

                    case 1:
                    case 2:
                        if (this.getEnergyPastArmor(0.25F / ((float) Math.sqrt(v1.y * v1.y + v1.z * v1.z) + 0.0001F), shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.05F) {
                                this.debuggunnery("Controls: Elevator Wiring Hit, Elevator Controls Disabled..");
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                            }
                            if (World.Rnd().nextFloat() < 0.75F) {
                                this.debuggunnery("Controls: Rudder Wiring Hit, Rudder Controls Disabled..");
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                            }
                        }
                        break;

                    case 3:
                        if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                            debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 4:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                        }
                        break;

                    case 5:
                    case 7:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                        }
                        break;

                    case 6:
                    case 8:
                        if (this.getEnergyPastArmor(4D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.debuggunnery("Controls: Aileron Wiring Hit, Aileron Controls Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.86F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
            }
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("prop") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 140000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 85000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.01F) this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    else {
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
                        debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyl1")) {
                    if (this.getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.75F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 48000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.startsWith("xxeng1oil")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    if (this.FM.AS.astateTankStates[k] == 0) {
                        this.debuggunnery("Fuel System: Fuel Tank Pierced..");
                        this.FM.AS.hitTank(shot.initiator, k, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 1);
                    } else if (this.FM.AS.astateTankStates[k] == 1) {
                        this.debuggunnery("Fuel System: Fuel Tank Pierced (2)..");
                        this.FM.AS.hitTank(shot.initiator, k, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 2);
                    }
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.hitTank(shot.initiator, k, 2);
                        this.debuggunnery("Fuel System: Fuel Tank Pierced, State Shifted..");
                    }
                }
                return;
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) this.hitChunk("Keel1", shot);
        else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
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
        } else if (s.startsWith("xgear")) {
            if (World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xoil")) {
            if (World.Rnd().nextFloat() < 0.12F) {
                this.FM.AS.hitOil(shot.initiator, 0);
                debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
            }
        } else if (!s.startsWith("xblister") && (s.startsWith("xpilot") || s.startsWith("xhead"))) {
            byte byte0 = 0;
            int l;
            if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else l = s.charAt(5) - 49;
            this.hitFlesh(l, shot, byte0);
        }
    }

    static {
        Class class1 = MC_200xyz.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "originCountry", PaintScheme.countryItaly);
    }
}
