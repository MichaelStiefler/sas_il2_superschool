package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public class Sikorsky_SH34 extends Scheme1 implements TypeScout, TypeTransport {

    public Sikorsky_SH34() {
        this.suka = new Loc();
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.rotorrpm = 0;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.endsWith("ASW")) {
            this.hierMesh().chunkVisible("Winglets_D0", true);
            return;
        } else {
            return;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Head2_D0"));
        }
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
        Sikorsky_SH34.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -1F);
        this.hierMesh().chunkSetLocate("Bay01_D0", Aircraft.xyz, Aircraft.ypr);
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

    public void moveWheelSink() {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -10F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, 10F), 0.0F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.42F, 0.0F, 0.3F);
        this.hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, f, 0.0F);
        this.hierMesh().chunkVisible("FlotadoresIC_D0", this.FM.CT.getArrestor() > 0.1F);
        this.hierMesh().chunkVisible("FlotadoresIL_D0", this.FM.CT.getArrestor() > 0.1F);
        this.hierMesh().chunkVisible("FlotadoresIR_D0", this.FM.CT.getArrestor() > 0.1F);
        if (this.FM.CT.getArrestor() > 0.1F) {
            this.FM.CT.bHasArrestorControl = false;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(8.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-006D), shot);
                }
            } else {
                if (s.startsWith("xxcontrols")) {
                    int i = s.charAt(10) - 48;
                    switch (i) {
                        case 1:
                            if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.3F), shot) > 0.0F) {
                                if (World.Rnd().nextFloat() < 0.25F) {
                                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                                    Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                                }
                                if (World.Rnd().nextFloat() < 0.25F) {
                                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                                    Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                                }
                            }
                            // fall through

                        case 2:
                        case 3:
                            if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                            }
                            break;
                    }
                }
                if (s.startsWith("xxspar")) {
                    Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                    if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(9.5F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                        this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                    }
                    if (s.startsWith("xxsparli") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                    }
                    if (s.startsWith("xxsparri") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                    }
                    if (s.startsWith("xxsparlm") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                    }
                    if (s.startsWith("xxsparrm") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                    }
                    if (s.startsWith("xxsparlo") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                    }
                    if (s.startsWith("xxsparro") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                    }
                    if (s.startsWith("xxstabl") && (this.getEnergyPastArmor(16.2F, shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                        this.nextDMGLevels(1, 2, "StabL_D" + this.chunkDamageVisible("StabL"), shot.initiator);
                    }
                    if (s.startsWith("xxstabr") && (this.getEnergyPastArmor(16.2F, shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                        this.nextDMGLevels(1, 2, "StabR_D" + this.chunkDamageVisible("StabR"), shot.initiator);
                    }
                }
                if (s.startsWith("xxlock")) {
                    Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                    if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                    }
                    if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                    }
                    if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                    }
                    if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                    }
                    if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                        Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                    }
                }
                if (s.startsWith("xxeng")) {
                    Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                    if (s.endsWith("prop")) {
                        if ((this.getEnergyPastArmor(0.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
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
                        this.getEnergyPastArmor(12.7F, shot);
                    } else if (s.startsWith("xxeng1cyls")) {
                        if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.12F))) {
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                            if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
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
                        if ((this.getEnergyPastArmor(0.2721F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
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
                if (s.startsWith("xxtank1") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.99F)) {
                    if (this.FM.AS.astateTankStates[0] == 0) {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 0, 1);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
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
        } else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
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
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR", shot);
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

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 34:
                return super.cutFM(35, j, actor);

            case 37:
                return super.cutFM(38, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;
        }
    }

    public void update(float f) {
        this.tiltRotor(f);
        this.computeVerticalThrust();
        this.computeOrizzontalThrust();
        this.computeOrizzontalThrust2();
        this.computeHovering();
        this.computeEngine();
        super.update(f);
    }

    private void tiltRotor(float f) {
        this.hierMesh().chunkSetAngles("Vator_D0", -(this.FM.CT.getElevator() * 3F), 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Arone_D0", 0.0F, this.FM.CT.getAileron() * 3F, 0.0F);
    }

    public void computeVerticalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 210F, 1.0F, 0.2F);
        float f3 = Aircraft.cvt(this.FM.getAltitude(), 0.0F, 3500F, 1.0F, 0.5F);
        float f4 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 8F)) {
            f4 = 1.3F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 > 8F)) {
            f4 = 1.12F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && !flag) {
            f4 = 1.23F * f;
        }
        this.FM.producedAF.z += f4 * ((10F * this.FM.M.massEmpty) + (10F * this.FM.M.fuel)) * (1.0F * f2) * (1.0F * f3);
    }

    public void computeOrizzontalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 100F, 1.0F, 0.0F);
        float f3 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && !flag) {
            f3 = 0.16F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (f1 > 5F) && flag) {
            f3 = 0.07F * f;
        }
        this.FM.producedAF.x -= f3 * (10F * this.FM.M.mass) * (1.0F * f2);
    }

    public void computeOrizzontalThrust2() {
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.0F) && (this.FM.getSpeedKMH() < 0.0F)) {
            f1 = 0.35F * f;
        }
        this.FM.producedAF.x += f1 * (10F * this.FM.M.mass);
    }

    public void computeHovering() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 100F, 1.0F, 0.0F);
        float f3 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && !flag && (f1 < 35F) && (this.FM.getSpeedKMH() > 10F) && this.FM.CT.StabilizerControl) {
            f3 = 0.15F * f;
        }
        this.FM.producedAF.x -= f3 * (10F * this.FM.M.mass) * (1.0F * f2);
    }

    public void computeEngine() {
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if (this.FM.EI.engines[0].getStage() > 5) {
            if (this.FM.EI.getPowerOutput() <= 0.0F) {

            }
        }
        if (f > 6D) {
            f1 = 5F;
        } else {
            float f2 = f * f;
            float f3 = f2 * f;
            f1 = ((0.108538F * f3) - (0.532867F * f2)) + (2.02176F * f);
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public static boolean bChangedPit = false;
    public Loc            suka;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    private int           rotorrpm;
    static {
        Class class1 = Sikorsky_SH34.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Choctaw");
        Property.set(class1, "meshName", "3DO/Plane/Sikorsky-SH34(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/PrattWhitney_Sikorsky.fmd:Sikorsky_Wessex_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitUH_34.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 3, 0, 0, 9, 9, 9, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01", "_BombSpawn02", "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
