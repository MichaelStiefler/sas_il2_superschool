package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.Property;

public class Ka_27AI extends Scheme1 implements TypeScout, TypeTransport, TypeStormovik {

    public Ka_27AI() {
        this.suka = new Loc();
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.rotorrpm = 0;
        this.setPictVBrake(0.0F);
        this.setPictAileron(0.0F);
        this.setPictVator(0.0F);
        this.setPictRudder(0.0F);
        this.setPictBlister(0.0F);
        this.setDeltaAzimuth(0.0F);
        this.setDeltaTangage(0.0F);
        this.setObsLookTime(0);
        this.obsLookAzimuth = 0.0F;
        this.obsLookElevation = 0.0F;
        this.obsAzimuth = 0.0F;
        this.obsElevation = 0.0F;
        this.obsAzimuthOld = 0.0F;
        this.obsElevationOld = 0.0F;
        this.obsMove = 0.0F;
        this.obsMoveTot = 0.0F;
        this.bObserverKilled = false;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
    }

    protected void moveElevator(float f) {
    }

    protected void moveAileron(float f) {
    }

    protected void moveFlap(float f) {
    }

    protected void moveRudder(float f) {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
    }

    protected void moveGear(float f) {
        Ka_27AI.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.9F);
        this.hierMesh().chunkSetLocate("Door1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFan(float f) {
        this.rotorrpm = Math.abs((int) ((this.FM.EI.engines[0].getw() * 0.025F) + (this.FM.Vwld.length() / 30D)));
        if (this.rotorrpm >= 1) {
            this.rotorrpm = 1;
        }
        if (this.FM.EI.engines[0].getw() > 100F) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", true);
        }
        if (this.FM.EI.engines[0].getw() < 100F) {
            this.hierMesh().chunkVisible("Prop1_D0", true);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Prop1_D1")) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if (this.FM.EI.engines[0].getw() > 100F) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", true);
        }
        if (this.FM.EI.engines[0].getw() < 100F) {
            this.hierMesh().chunkVisible("Prop2_D0", true);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Prop2_D1")) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Tail1_CAP")) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
            this.hierMesh().chunkVisible("Prop2_D1", false);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 100F) % 360F : (float) (this.dynamoOrient - (this.rotorrpm * 25D)) % 360F;
        this.hierMesh().chunkSetAngles("Prop1_D0", -this.dynamoOrient, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, this.dynamoOrient * -10F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        boolean flag1 = this instanceof Aircraft;
        boolean flag2 = !(this instanceof Aircraft);
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.startsWith("xxarmorp")) {
                    int i = s.charAt(8) - 48;
                    switch (i) {
                        default:
                            break;

                        case 1:
                            this.getEnergyPastArmor(7.070000171661377D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            break;

                        case 2:
                        case 3:
                            this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.86599999666213989D)) {
                                this.doRicochet(shot);
                            }
                            break;

                        case 4:
                            if (point3d.x > -1.35D) {
                                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
                                shot.powerType = 0;
                                if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.86599999666213989D)) {
                                    this.doRicochet(shot);
                                }
                            } else {
                                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                            }
                            break;

                        case 5:
                        case 6:
                            this.getEnergyPastArmor(20.2D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                            if (shot.power > 0.0F) {
                                break;
                            }
                            if (Math.abs(Aircraft.v1.x) > 0.86599999666213989D) {
                                this.doRicochet(shot);
                            } else {
                                this.doRicochetBack(shot);
                            }
                            break;

                        case 7:
                            this.getEnergyPastArmor(20.2D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                            if (shot.power <= 0.0F) {
                                this.doRicochetBack(shot);
                            }
                            break;
                    }
                }
                if (s.startsWith("xxarmorc1")) {
                    this.getEnergyPastArmor(7.070000171661377D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                }
                if (s.startsWith("xxarmort1")) {
                    this.getEnergyPastArmor(6.059999942779541D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                if (s.endsWith("10")) {
                    j = 10;
                }
                switch (j) {
                    default:
                        break;

                    case 1:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.33F) {
                                this.debuggunnery("Controls: Throttle Controls Disabled..");
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            }
                            if (World.Rnd().nextFloat() < 0.33F) {
                                this.debuggunnery("Controls: Prop Controls Disabled..");
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            }
                            if (World.Rnd().nextFloat() < 0.33F) {
                                this.debuggunnery("Controls: Mix Controls Disabled..");
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                            }
                        }
                        break;

                    case 2:
                        if (this.getEnergyPastArmor(1.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Control Column Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                        if (this.getEnergyPastArmor(0.25F / ((float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (Aircraft.v1.z * Aircraft.v1.z)) + 0.0001F), shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.debuggunnery("Controls: Elevator Wiring Hit, Elevator Controls Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        if (World.Rnd().nextFloat() < 0.75F) {
                            this.debuggunnery("Controls: Rudder Wiring Hit, Rudder Controls Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;

                    case 4:
                        if (this.getEnergyPastArmor(0.25F / ((float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (Aircraft.v1.z * Aircraft.v1.z)) + 0.0001F), shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.03F) {
                            this.debuggunnery("Controls: Elevator Wiring Hit, Elevator Controls Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.debuggunnery("Controls: Rudder Wiring Hit, Rudder Controls Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;

                    case 5:
                    case 6:
                    case 9:
                    case 10:
                        if ((this.getEnergyPastArmor(4D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            this.debuggunnery("Controls: Aileron Wiring Hit, Aileron Controls Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 7:
                    case 8:
                        if (this.getEnergyPastArmor(5.25F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Aileron Cranks Hit, Aileron Controls Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;
                }
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 1) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.1D)) && (this.getEnergyPastArmor(3.4D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxsparlm")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingLMid") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingLMid Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingLMid") > 2) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparrm")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingRMid") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingRMid Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingRMid") > 2) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparlo")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingLOut") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingLOut Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingLOut") > 2) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparro")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingROut") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingROut Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingROut") > 2) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparsl") && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(6.5D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(6.5D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.86F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
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
            }
            if (s.startsWith("xxeng")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("prop")) {
                    if ((this.getEnergyPastArmor(3.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.8F)) {
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.debuggunnery("Engine Module: Prop Governor Hit, Disabled..");
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else {
                            this.debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                    }
                } else if (s.endsWith("gear")) {
                    if ((this.getEnergyPastArmor(4.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.01F, shot) > 0.0F) {
                        this.debuggunnery("Engine Module: Supercharger Disabled..");
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else if (s.endsWith("feed")) {
                    if ((this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else if (s.endsWith("fue1")) {
                    if (this.getEnergyPastArmor(0.89F, shot) > 0.0F) {
                        this.debuggunnery("Engine Module: Fuel Feed Line Pierced, Engine Fires..");
                        this.FM.AS.hitEngine(shot.initiator, 0, 100);
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    this.debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    this.getEnergyPastArmor(22.5F, shot);
                } else if (s.endsWith("cyl1")) {
                    if ((this.getEnergyPastArmor(1.3F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.75F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        this.debuggunnery("Engine Module: Cylinders Assembly Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Operating..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if (World.Rnd().nextFloat() < 0.01F) {
                            this.debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                    if ((Math.abs(point3d.y) < 0.1379999965429306D) && (this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else if (s.startsWith("xxeng1mag")) {
                    int k = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + k + " Hit, Magneto " + k + " Disabled..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, k);
                } else if (s.startsWith("xxeng1oil") && (this.getEnergyPastArmor(0.5F, shot) > 0.0F)) {
                    this.debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if (s.startsWith("xxw1")) {
                if (this.FM.AS.astateEngineStates[0] == 0) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                } else if (this.FM.AS.astateEngineStates[0] == 1) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 2);
                }
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.12F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    if (this.FM.AS.astateTankStates[l] == 0) {
                        this.debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 1);
                    } else if (this.FM.AS.astateTankStates[l] == 1) {
                        this.debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
                        this.FM.AS.hitTank(shot.initiator, 2, 2);
                        this.debuggunnery("Fuel System: Fuel Tank " + l + " Pierced, State Shifted..");
                    }
                }
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if (s.startsWith("xxcannon")) {
                if (s.endsWith("01") && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.debuggunnery("Armament System: Left Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.endsWith("02") && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.debuggunnery("Armament System: Right Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if (s.startsWith("xxammo")) {
                if (s.startsWith("xxammol1") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Left Cannon: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.startsWith("xxammor1") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                if (s.startsWith("xxammol2") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.startsWith("xxammor2") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
            }
            if (s.startsWith("xxbomb") && (World.Rnd().nextFloat() < 0.00345F) && (this.FM.CT.Weapons[3] != null) && this.FM.CT.Weapons[3][0].haveBullets()) {
                this.debuggunnery("Armament System: Bomb Payload Detonated..");
                this.FM.AS.hitTank(shot.initiator, 0, 10);
                this.FM.AS.hitTank(shot.initiator, 1, 10);
                this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if (s.startsWith("xxpnm") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)) {
                this.debuggunnery("Pneumo System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            }
            if (s.startsWith("xxhyd") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.startsWith("xxins")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            return;
        }
        if (s.startsWith("xcockpit") || s.startsWith("xblister")) {
            if (point3d.z > 0.473D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            } else if (point3d.y > 0.0D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            } else {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            }
        }
        if (s.startsWith("xcf")) {
            if (point3d.x < -1.94D) {
                if (this.chunkDamageVisible("Tail1") < 3) {
                    this.hitChunk("Tail1", shot);
                }
            } else {
                if (point3d.x <= 1.342D) {
                    if ((point3d.z < -0.591D) || ((point3d.z > 0.40799999237060547D) && (point3d.x > 0.0D))) {
                        this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
                        if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.86599999666213989D)) {
                            this.doRicochet(shot);
                        }
                    } else {
                        this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                        if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.86599999666213989D)) {
                            this.doRicochet(shot);
                        }
                    }
                }
                if (this.chunkDamageVisible("CF") < 3) {
                    this.hitChunk("CF", shot);
                }
            }
        } else if (s.startsWith("xoil")) {
            if (point3d.z < -0.981D) {
                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
                if (shot.power <= 0.0F) {
                    this.doRicochet(shot);
                }
            } else if ((point3d.x > 0.537D) || (point3d.x < -0.1D)) {
                this.getEnergyPastArmor(0.2D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if (shot.power <= 0.0F) {
                    this.doRicochetBack(shot);
                }
            } else {
                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                if (shot.power <= 0.0F) {
                    this.doRicochet(shot);
                }
            }
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xeng")) {
            if (point3d.z > 0.159D) {
                this.getEnergyPastArmor((1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
            } else if ((point3d.x > 1.335D) && (point3d.x < 2.386D) && (point3d.z > -0.06D) && (point3d.z < 0.064000000000000001D)) {
                this.getEnergyPastArmor(0.5D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
            } else if ((point3d.x > 2.53D) && (point3d.x < 2.992D) && (point3d.z > -0.235D) && (point3d.z < 0.011D)) {
                this.getEnergyPastArmor(4.0399999618530273D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
            } else if ((point3d.x > 2.559D) && (point3d.z < -0.595D)) {
                this.getEnergyPastArmor(4.0399999618530273D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
            } else if ((point3d.x > 1.849D) && (point3d.x < 2.251D) && (point3d.z < -0.71D)) {
                this.getEnergyPastArmor(4.0399999618530273D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
            } else if (point3d.x > 3.0030000000000001D) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            } else if (point3d.z < -0.60600000619888306D) {
                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
            } else {
                this.getEnergyPastArmor(5.0500001907348633D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
            }
            if ((Math.abs(Aircraft.v1.x) > 0.86599999666213989D) && ((shot.power <= 0.0F) || (World.Rnd().nextFloat() < 0.1F))) {
                this.doRicochet(shot);
            }
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (flag2) {
                if (this.chunkDamageVisible("Tail1") < 3) {
                    this.hitChunk("Tail1", shot);
                }
            } else {
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
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if ((s.endsWith("2a") || s.endsWith("2b")) && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xturret")) {
            if (this.getEnergyPastArmor(0.25F, shot) > 0.0F) {
                this.debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                this.FM.AS.setJamBullets(10, 0);
                this.FM.AS.setJamBullets(10, 1);
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else if (s.startsWith("xhelm")) {
            this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
            if (shot.power <= 0.0F) {
                this.doRicochetBack(shot);
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

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 34:
                return super.cutFM(35, j, actor);

            case 37:
                return super.cutFM(38, j, actor);

            case 35:
            case 36:
            default:
                return super.cutFM(i, j, actor);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("4x")) {
            this.hierMesh().chunkVisible("PylonL_D0", true);
            this.hierMesh().chunkVisible("PylonR_D0", true);
        } else {
            return;
        }
    }

    public void update(float f) {
        if ((this.obsMove < this.obsMoveTot) && !this.bObserverKilled && !this.FM.AS.isPilotParatrooper(1)) {
            if ((this.obsMove < 0.2F) || (this.obsMove > (this.obsMoveTot - 0.2F))) {
                this.obsMove += 0.3D * f;
            } else if ((this.obsMove < 0.1F) || (this.obsMove > (this.obsMoveTot - 0.1F))) {
                this.obsMove += 0.15F;
            } else {
                this.obsMove += 1.2D * f;
            }
            this.obsLookAzimuth = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsAzimuthOld, this.obsAzimuth);
            this.obsLookElevation = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsElevationOld, this.obsElevation);
            this.hierMesh().chunkSetAngles("Head2_D0", 0.0F, this.obsLookAzimuth, this.obsLookElevation);
        }
        super.update(f);
        Pilot pilot = (Pilot) this.FM;
        if (pilot != null) {
            Actor actor = War.GetNearestEnemy(this, 1, 4000F);
            if ((pilot != null) && Actor.isAlive(actor) && !(actor instanceof BridgeSegment)) {
                Point3d point3d = new Point3d();
                actor.pos.getAbs(point3d);
                if (this.pos.getAbsPoint().distance(point3d) < 3000D) {
                    point3d.sub(this.FM.Loc);
                    this.FM.Or.transformInv(point3d);
                    if (point3d.y < 0.0D) {
                        this.FM.turret[0].target = actor;
                        this.FM.turret[0].tMode = 2;
                    }
                }
            } else if (actor != null) {
                for (int i = 0; i < this.FM.turret.length; i++) {
                    if ((this.FM.turret[i].target != null) && !(this.FM.turret[i].target instanceof Aircraft) && !Actor.isAlive(this.FM.turret[i].target)) {
                        this.FM.turret[i].target = null;
                    }
                }

            }
        }
        if ((this.FM.getAltitude() > 0.0F) && (this.FM.getSpeedKMH() >= 200D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1800D;
        }
        if ((this.FM.getAltitude() > 0.0F) && (this.FM.getSpeedKMH() >= 200D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 1800D;
        }
        if ((this.FM.getAltitude() > 0.0F) && (this.FM.getSpeedKMH() >= 220D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1700D;
        }
        if ((this.FM.getAltitude() > 0.0F) && (this.FM.getSpeedKMH() >= 220D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 1700D;
        }
        if ((this.FM.getAltitude() > 0.0F) && (this.FM.getSpeedKMH() >= 240D) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 4000D;
        }
        if ((this.FM.getAltitude() > 0.0F) && (this.FM.getSpeedKMH() >= 240D) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 4000D;
        }
        if ((this.FM.getAltitude() > 4000F) && (this.FM.EI.engines[0].getThrustOutput() > 0.8F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1000D;
        }
        if ((this.FM.getAltitude() > 4000F) && (this.FM.EI.engines[1].getThrustOutput() > 0.8F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 1000D;
        }
        if ((this.FM.getAltitude() > 4500F) && (this.FM.EI.engines[0].getThrustOutput() > 0.8F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x -= 1000D;
        }
        if ((this.FM.getAltitude() > 4500F) && (this.FM.EI.engines[1].getThrustOutput() > 0.8F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x -= 1000D;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -60F) {
            af[0] = -60F;
            flag = false;
        } else if (af[0] > 60F) {
            af[0] = 60F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (af[1] < -60F) {
            af[1] = -60F;
            flag = false;
        }
        if (af[1] > 60F) {
            af[1] = 60F;
            flag = false;
        }
        if (!flag) {
            return false;
        }
        float f1 = af[1];
        if ((f < 1.2F) && (f1 < 13.3F)) {
            return false;
        }
        return (f1 >= -3.1F) || (f1 <= -4.6F);
    }

    public int getObsLookTime() {
        return this.obsLookTime;
    }

    public void setObsLookTime(int obsLookTime) {
        this.obsLookTime = obsLookTime;
    }

    public float getPictVBrake() {
        return this.pictVBrake;
    }

    public void setPictVBrake(float pictVBrake) {
        this.pictVBrake = pictVBrake;
    }

    public float getPictAileron() {
        return this.pictAileron;
    }

    public void setPictAileron(float pictAileron) {
        this.pictAileron = pictAileron;
    }

    public float getPictVator() {
        return this.pictVator;
    }

    public void setPictVator(float pictVator) {
        this.pictVator = pictVator;
    }

    public float getPictRudder() {
        return this.pictRudder;
    }

    public void setPictRudder(float pictRudder) {
        this.pictRudder = pictRudder;
    }

    public float getPictBlister() {
        return this.pictBlister;
    }

    public void setPictBlister(float pictBlister) {
        this.pictBlister = pictBlister;
    }

    public static float[] getFca() {
        return Ka_27AI.fcA;
    }

    public static float[] getFce() {
        return Ka_27AI.fcE;
    }

    public static float[] getFcr() {
        return Ka_27AI.fcR;
    }

    public float getDeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public void setDeltaAzimuth(float deltaAzimuth) {
        this.deltaAzimuth = deltaAzimuth;
    }

    public float getDeltaTangage() {
        return this.deltaTangage;
    }

    public void setDeltaTangage(float deltaTangage) {
        this.deltaTangage = deltaTangage;
    }

    private int                obsLookTime;
    private float              obsLookAzimuth;
    private float              obsLookElevation;
    private float              obsAzimuth;
    private float              obsElevation;
    private float              obsAzimuthOld;
    private float              obsElevationOld;
    private float              obsMove;
    private float              obsMoveTot;
    boolean                    bObserverKilled;
    public static boolean      bChangedPit = false;
    public Loc                 suka;
    private float              dynamoOrient;
    private boolean            bDynamoRotary;
    private int                rotorrpm;
    private float              pictVBrake;
    private float              pictAileron;
    private float              pictVator;
    private float              pictRudder;
    private float              pictBlister;
    private static final float fcA[]       = { 0.0F, 0.04F, 0.1F, 0.04F, 0.02F, -0.02F, -0.04F, -0.1F, -0.04F };
    private static final float fcE[]       = { 0.98F, 0.48F, 0.1F, -0.48F, -0.7F, -0.7F, -0.48F, 0.1F, 0.48F };
    private static final float fcR[]       = { 0.02F, 0.48F, 0.8F, 0.48F, 0.28F, -0.28F, -0.48F, -0.8F, -0.48F };
    private float              deltaAzimuth;
    private float              deltaTangage;

    static {
        Class class1 = Ka_27AI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ka27");
        Property.set(class1, "meshName", "3DO/Plane/Ka-27/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/HRS3.fmd:MI_AI");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9, 2, 2, 2, 2, 7, 7 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_BombSpawn01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Flare01", "_Flare02" });
    }
}
