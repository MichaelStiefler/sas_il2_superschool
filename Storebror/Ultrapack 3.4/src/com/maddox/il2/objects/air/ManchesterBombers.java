package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class ManchesterBombers extends Scheme2 implements TypeTransport, TypeBomber {

    public ManchesterBombers() {
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
        this.fSightCurReadyness = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 800F, -85F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 98F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 98F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -146F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -146F * f);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
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
        this.hierMesh().chunkSetAngles("Rudder2_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("FlapWL_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("FlapCL_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("FlapWR_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("FlapCR_D0", 0.0F, 0.0F, 40F * f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3) {
                if (World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 0, 1);
                if (World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 1, 1);
            }
            if (this.FM.AS.astateEngineStates[1] > 3) {
                if (World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 2, 1);
                if (World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 3, 1);
            }
            if (this.FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 0, 1);
            if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 2, 1);
            if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 3, 1);
            if (this.FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.02F) this.FM.AS.hitTank(this, 2, 1);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -70F) {
                    f = -70F;
                    flag = false;
                }
                if (f > 70F) {
                    f = 70F;
                    flag = false;
                }
                if (f1 < -20F) {
                    f1 = -20F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -70F) {
                    f = -70F;
                    flag = false;
                }
                if (f > 70F) {
                    f = 70F;
                    flag = false;
                }
                if (f1 < -20F) {
                    f1 = -20F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;

            case 2:
                if (f1 < 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                if (f1 > 88F) {
                    f1 = 88F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
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

            case 34:
                this.FM.AS.hitEngine(this, 0, 2);
                if (World.Rnd().nextInt(0, 99) < 66) this.FM.AS.hitEngine(this, 0, 2);
                break;

            case 37:
                this.FM.AS.hitEngine(this, 1, 2);
                if (World.Rnd().nextInt(0, 99) < 66) this.FM.AS.hitEngine(this, 1, 2);
                break;

            case 19:
                this.killPilot(actor, 6);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("BayL_D0", 0.0F, 85F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayR_D0", 0.0F, -85F * f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxcontrol")) {
                int i = s.charAt(9) - 48;
                switch (i) {
                    case 1:
                        if (this.getEnergyPastArmor(3F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setControlsDamage(shot.initiator, 2);
                            if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        break;

                    case 2:
                    case 3:
                        if (this.getEnergyPastArmor(3F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) this.FM.AS.setControlsDamage(shot.initiator, 0);
                        break;
                }
            } else if (s.startsWith("xxeng")) {
                int j = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(20000F, 140000F) < shot.power) {
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                            this.debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            this.debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.04F) this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                    else {
                        this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - 0.02F);
                        this.debuggunnery("*** Engine" + j + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 0.66F) {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("*** Engine" + j + " Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 1000000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            this.debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) this.FM.EI.engines[j].setKillCompressor(shot.initiator);
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        if (Aircraft.Pd.y > 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
                        if (Aircraft.Pd.y < 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("oil1") && this.getEnergyPastArmor(4.21F, shot) > 0.0F) {
                    this.FM.AS.hitOil(shot.initiator, j);
                    this.getEnergyPastArmor(0.42F, shot);
                }
            } else if (s.startsWith("xxfuel")) {
                int k = s.charAt(6) - 48;
                int i1;
                switch (k) {
                    default:
                        if (World.Rnd().nextFloat() < 0.1F) this.hitBone("xxfuel2", shot, point3d);
                        if (World.Rnd().nextFloat() < 0.1F) this.hitBone("xxfuel3", shot, point3d);
                        if (World.Rnd().nextFloat() < 0.1F) this.hitBone("xxfuel5", shot, point3d);
                        if (World.Rnd().nextFloat() < 0.1F) this.hitBone("xxfuel6", shot, point3d);
                        return;

                    case 2:
                        i1 = 1;
                        break;

                    case 3:
                        i1 = World.Rnd().nextInt(0, 1);
                        break;

                    case 4:
                        i1 = 0;
                        break;

                    case 5:
                        i1 = 2;
                        break;

                    case 6:
                        i1 = World.Rnd().nextInt(2, 3);
                        break;

                    case 7:
                        i1 = 3;
                        break;
                }
                if (this.getEnergyPastArmor(0.8F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2F) {
                    if (this.FM.AS.astateTankStates[i1] == 0) {
                        this.FM.AS.hitTank(shot.initiator, i1, 2);
                        this.FM.AS.doSetTankState(shot.initiator, i1, 2);
                    }
                    if (World.Rnd().nextFloat() < 0.15F) this.FM.AS.hitTank(shot.initiator, i1, 1);
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) this.FM.AS.hitTank(shot.initiator, i1, 2);
                }
            } else if (s.startsWith("xxgun")) {
                int l = s.charAt(5) - 49;
                if (World.Rnd().nextFloat() < 0.5F) this.FM.AS.setJamBullets(10 + l, 0);
                this.getEnergyPastArmor(22.7F, shot);
            } else if (s.startsWith("xxlock")) {
                if (s.startsWith("xxlockal")) {
                    if (this.getEnergyPastArmor(6.5F, shot) > 0.0F) {
                        this.debuggunnery("*** AroneL Lock Damaged..");
                        this.nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockar")) {
                    if (this.getEnergyPastArmor(6.5F, shot) > 0.0F) {
                        this.debuggunnery("*** AroneR Lock Damaged..");
                        this.nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlocksl")) {
                    if (this.getEnergyPastArmor(6.5F, shot) > 0.0F) {
                        this.debuggunnery("*** VatorL Lock Damaged..");
                        this.nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlocksr")) {
                    if (this.getEnergyPastArmor(6.5F, shot) > 0.0F) {
                        this.debuggunnery("*** VatorR Lock Damaged..");
                        this.nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockk") && this.getEnergyPastArmor(6.5F, shot) > 0.0F) {
                    this.debuggunnery("*** Rudder1 Lock Damaged..");
                    this.nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
                } else if (s.startsWith("xxlockk") && this.getEnergyPastArmor(6.5F, shot) > 0.0F) {
                    this.debuggunnery("*** Rudder2 Lock Damaged..");
                    this.nextDMGLevels(1, 2, "Rudder2_D0", shot.initiator);
                }
            } else if (s.startsWith("xxpar")) {
                if (s.startsWith("xxpark") && this.chunkDamageVisible("Keel1") > 2 && this.getEnergyPastArmor(10.7D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
                    this.debuggunnery("*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
                }
                if (s.startsWith("xxparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(12.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
                }
                if (s.startsWith("xxparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
                }
                if (s.startsWith("xxparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
                }
                if (s.startsWith("xxparrs") && this.chunkDamageVisible("StabL") > 2 && this.getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
                    this.debuggunnery("*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxparls") && this.chunkDamageVisible("StabR") > 2 && this.getEnergyPastArmor(8.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F) {
                    this.debuggunnery("*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
            }
        } else if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) this.hitChunk("CF", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 2) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) this.hitChunk("Keel2", shot);
        } else if (s.startsWith("xkeel3")) {
            if (this.chunkDamageVisible("Keel3") < 2) this.hitChunk("Keel3", shot);
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder2") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 2) this.hitChunk("WingLIn", shot);
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 2) this.hitChunk("WingRIn", shot);
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 2) this.hitChunk("WingLOut", shot);
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 2) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j1;
            if (s.endsWith("a") || s.endsWith("a2")) {
                byte0 = 1;
                j1 = s.charAt(6) - 49;
            } else if (s.endsWith("b") || s.endsWith("b2")) {
                byte0 = 2;
                j1 = s.charAt(6) - 49;
            } else j1 = s.charAt(5) - 49;
            this.hitFlesh(j1, shot, byte0);
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 3:
                this.FM.turret[1].bIsOperable = false;
                break;

            case 4:
                this.FM.turret[2].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                break;

            case 4:
                this.hierMesh().chunkVisible("Pilot5_D0", false);
                this.hierMesh().chunkVisible("Pilot5_D1", true);
                break;
        }
    }

    private static final float toMeters(float f) {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f) {
        return 0.4470401F * f;
    }

    public boolean typeBomberToggleAutomation() {
        this.bSightAutomation = !this.bSightAutomation;
        this.bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
        return this.bSightAutomation;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle++;
        if (this.fSightCurForwardAngle > 85F) this.fSightCurForwardAngle = 85F;
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) this.typeBomberToggleAutomation();
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle--;
        if (this.fSightCurForwardAngle < 0.0F) this.fSightCurForwardAngle = 0.0F;
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) this.typeBomberToggleAutomation();
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.1F;
        if (this.fSightCurSideslip > 3F) this.fSightCurSideslip = 3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.1F;
        if (this.fSightCurSideslip < -3F) this.fSightCurSideslip = -3F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 3000F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 50F;
        if (this.fSightCurAltitude > 50000F) this.fSightCurAltitude = 50000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 50F;
        if (this.fSightCurAltitude < 1000F) this.fSightCurAltitude = 1000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 200F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 10F;
        if (this.fSightCurSpeed > 450F) this.fSightCurSpeed = 450F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 10F;
        if (this.fSightCurSpeed < 100F) this.fSightCurSpeed = 100F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        double d = this.fSightCurSpeed / 3.6D * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
        d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
        this.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / this.fSightCurAltitude));
        if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
            this.fSightCurReadyness -= 0.0666666F * f;
            if (this.fSightCurReadyness < 0.0F) this.fSightCurReadyness = 0.0F;
        }
        if (this.fSightCurReadyness < 1.0F) this.fSightCurReadyness += 0.0333333F * f;
        else if (this.bSightAutomation) {
            this.fSightCurDistance -= toMetersPerSecond(this.fSightCurSpeed) * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / toMeters(this.fSightCurAltitude)));
            if (this.fSightCurDistance < toMetersPerSecond(this.fSightCurSpeed) * Math.sqrt(toMeters(this.fSightCurAltitude) * (2F / 9.81F))) this.bSightBombDump = true;
            if (this.bSightBombDump) if (this.FM.isTick(3, 0)) {
                if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
                    this.FM.CT.WeaponControl[3] = true;
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                }
            } else this.FM.CT.WeaponControl[3] = false;
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(this.fSightCurDistance);
        netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
        netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedByte();
        this.bSightAutomation = (i & 1) != 0;
        this.bSightBombDump = (i & 2) != 0;
        this.fSightCurDistance = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
        this.fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
        this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    public void update(float f) {
        if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
            for (int engineIndex = 0; engineIndex < 2; engineIndex++) {
                RealFlightModel realFlightModel = (RealFlightModel) this.FM;
                float rpm = this.FM.EI.engines[engineIndex].getRPM();
                if (rpm > 30) {
                    if (rpm < 300F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, (1500F - rpm) / 3000F);
                    } else if (rpm < 1000F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, (1500F - rpm) / 8000F);
                    } else if (rpm < 1500F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, 0.07F);
                    } else if (rpm < 2000F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, 0.05F);
                    } else if (rpm < 2300F) {
                        realFlightModel.producedShakeLevel = Math.max(realFlightModel.producedShakeLevel, 0.04F);
                    }
                }
            }
        }
        if (this.FM.getSpeedKMH() > 250F && this.FM.getVertSpeed() > 0.0F && this.FM.getAltitude() < 5000F) this.FM.producedAF.x += 20F * (250F - this.FM.getSpeedKMH());
        if (this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F) {
            if (this.FM.getSpeedKMH() > 300F && this.FM.getSpeedKMH() < 400F) {
                this.FM.SensPitch = 0.25F;
                this.FM.producedAM.y -= 300F * (200F - this.FM.getSpeedKMH());
            }
            if (this.FM.getSpeedKMH() >= 400F) {
                this.FM.SensPitch = 0.22F;
                this.FM.producedAM.y -= 150F * (200F - this.FM.getSpeedKMH());
            } else this.FM.SensPitch = 0.3F;
        }
        super.update(f);
    }

    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float   fSightCurDistance;
    public float    fSightCurForwardAngle;
    public float    fSightCurSideslip;
    public float    fSightCurAltitude;
    public float    fSightCurSpeed;
    public float    fSightCurReadyness;
    public float    fSightSetForwardAngle;

    static {
        Class class1 = ManchesterBombers.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
