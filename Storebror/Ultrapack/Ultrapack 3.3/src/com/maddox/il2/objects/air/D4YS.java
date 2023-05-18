package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class D4YS extends Scheme1 {

    public D4YS() {
        this.arrestor = 0.0F;
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -40F * f, 0.0F);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -65F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 65F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -10F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 10F * f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.95F, 0.0F, -85F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.4F, 0.75F, 0.0F, -0.205F);
        hiermesh.chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.75F, 0.94F, 0.0F, -0.05F);
        hiermesh.chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
        if (f > 0.75F) hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.75F, 0.95F, -45F, -65F), 0.0F);
        else hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.4F, 0.75F, 0.0F, -45F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.95F, 0.0F, -130F), 0.0F);
        if (f > 0.75F) hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.725F, 0.95F, -86F, -133F), 0.0F);
        else hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.725F, 0.0F, -86F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.05F, 0.95F, 0.0F, -0.1F);
        hiermesh.chunkSetLocate("GearL8_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL9_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.115F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL10_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.85F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.95F, 0.0F, -85F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.4F, 0.75F, 0.0F, -0.205F);
        hiermesh.chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.75F, 0.94F, 0.0F, -0.05F);
        hiermesh.chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
        if (f > 0.75F) hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.75F, 0.95F, -45F, -65F), 0.0F);
        else hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.4F, 0.75F, 0.0F, -45F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.95F, 0.0F, -130F), 0.0F);
        if (f > 0.75F) hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.725F, 0.95F, -86F, -133F), 0.0F);
        else hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.725F, 0.0F, -86F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.05F, 0.95F, 0.0F, -0.1F);
        hiermesh.chunkSetLocate("GearR8_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR9_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.115F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR10_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.85F, 0.0F, -85F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.252F, 0.0F, -0.231F);
        Aircraft.xyz[1] = f;
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.252F, 0.0F, -0.231F);
        Aircraft.xyz[1] = f;
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 60F * this.arrestor, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.64F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
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

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("t1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(21F, 42F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) this.doRicochetBack(shot);
                } else if (s.endsWith("t2")) this.getEnergyPastArmor(World.Rnd().nextFloat(12.2F, 12.9F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("t3")) this.getEnergyPastArmor(8.9F, shot);
                else if (s.endsWith("t4")) this.getEnergyPastArmor(World.Rnd().nextFloat(12.2F, 12.9F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("t5")) this.getEnergyPastArmor(World.Rnd().nextFloat(12.2F, 12.9F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("t6")) this.getEnergyPastArmor(World.Rnd().nextFloat(12.2F, 12.9F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                if ((s.endsWith("s1") || s.endsWith("s2")) && this.getEnergyPastArmor(4.8F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Elevator Cables: Hit, Controls Destroyed..");
                }
                if (s.endsWith("s3") && this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                }
                if (s.endsWith("s4") && this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                }
                if ((s.endsWith("s5") || s.endsWith("s6") || s.endsWith("s7") || s.endsWith("s8")) && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.35F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 280000F) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < shot.power / 100000F) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.66F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 1000000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.startsWith("xxeng1mag")) {
                    int i = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                }
                if (s.endsWith("oil1")) {
                    if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, 0);
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
                return;
            }
            if (s.startsWith("xxmgun1")) {
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                    this.FM.AS.setJamBullets(0, 0);
                    this.getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if (s.startsWith("xxmgun2")) {
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                    this.FM.AS.setJamBullets(0, 1);
                    this.getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if (s.startsWith("xxmgun3")) {
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                    this.FM.AS.setJamBullets(10, World.Rnd().nextInt(0, 1));
                    this.getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if (this.getEnergyPastArmor(2.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(6.75F, shot);
                    this.debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** Tail1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 10.25F) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if (World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.hitTank(shot.initiator, j, 2);
                        this.debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
                return;
            } else return;
        }
        if (s.startsWith("xCF")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
        } else if (!s.startsWith("xBlister")) if (s.startsWith("xEng")) this.hitChunk("Engine1", shot);
        else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xKeel")) this.hitChunk("Keel1", shot);
        else if (s.startsWith("xRudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xStab")) {
            if (s.startsWith("xStabL")) this.hitChunk("StabL", shot);
            if (s.startsWith("xStabR")) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
            if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xWing")) {
            if (s.startsWith("xWingLIn") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xWingRIn") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xWingLMid")) {
                if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
                if (World.Rnd().nextFloat() < shot.mass + 0.02F) this.FM.AS.hitOil(shot.initiator, 0);
            }
            if (s.startsWith("xWingRMid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xWingLOut") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xWingROut") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xArone")) {
            if (s.startsWith("xAroneL")) this.hitChunk("AroneL", shot);
            if (s.startsWith("xAroneR")) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xGearL")) this.hitChunk("GearL2", shot);
        else if (s.startsWith("xGearR")) this.hitChunk("GearR2", shot);
        else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k;
            if (s.endsWith("a")) {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else k = s.charAt(5) - 49;
            this.hitFlesh(k, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.FM.CT.bHasArrestorControl = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.CT.getArrestor() > 0.001F) if (this.FM.Gears.arrestorVAngle != 0.0F) {
            float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -73.5F, 3.5F, 1.0F, 0.0F);
            this.arrestor = 0.8F * this.arrestor + 0.2F * f1;
            this.moveArrestorHook(this.arrestor);
        } else {
            float f2 = -1.224F * this.FM.Gears.arrestorVSink;
            if (f2 < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if (f2 > 0.2F) f2 = 0.2F;
            if (f2 > 0.0F) this.arrestor = 0.7F * this.arrestor + 0.3F * (this.arrestor + f2);
            else this.arrestor = 0.3F * this.arrestor + 0.7F * (this.arrestor + f2);
            if (this.arrestor < 0.0F) this.arrestor = 0.0F;
            else if (this.arrestor > this.FM.CT.getArrestor()) this.arrestor = this.FM.CT.getArrestor();
            this.moveArrestorHook(this.arrestor);
        }
    }

    private float arrestor;

    static {
        Class class1 = D4YS.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
