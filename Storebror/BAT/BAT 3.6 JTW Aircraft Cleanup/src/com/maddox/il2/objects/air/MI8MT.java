package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Property;

public class MI8MT extends Scheme2a implements TypeScout, TypeTransport, TypeStormovik {

    public MI8MT() {
        this.suka = new Loc();
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.rotorrpm = 0;
        this.obsLookTime = 0;
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
        if (!this.bObserverKilled) {
            if (this.obsLookTime == 0) {
                this.obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                this.obsMoveTot = 1.0F + (World.Rnd().nextFloat() * 1.5F);
                this.obsMove = 0.0F;
                this.obsAzimuthOld = this.obsAzimuth;
                this.obsElevationOld = this.obsElevation;
                if (World.Rnd().nextFloat() > 0.8D) {
                    this.obsAzimuth = 0.0F;
                    this.obsElevation = 0.0F;
                } else {
                    this.obsAzimuth = (World.Rnd().nextFloat() * 140F) - 70F;
                    this.obsElevation = (World.Rnd().nextFloat() * 50F) - 20F;
                }
            } else {
                this.obsLookTime--;
            }
        }
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -5F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 5F * f);
    }

    protected void moveFlap(float f) {
    }

    protected void moveRudder(float f) {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
    }

    protected void moveGear(float f) {
        MI8MT.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.8F);
        this.hierMesh().chunkSetLocate("Door1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFan(float f) {
        this.rotorrpm = Math.abs((int) ((this.FM.EI.engines[0].getw() * 0.025F) + (this.FM.Vwld.length() / 30D)));
        if (this.rotorrpm >= 1) {
            this.rotorrpm = 1;
        }
        if ((this.FM.EI.engines[0].getw() > 100F) && (this.FM.EI.engines[1].getw() > 100F)) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", true);
        }
        if ((this.FM.EI.engines[0].getw() < 100F) && (this.FM.EI.engines[1].getw() < 100F)) {
            this.hierMesh().chunkVisible("Prop1_D0", true);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Prop1_D1")) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if ((this.FM.EI.engines[0].getw() > 100F) && (this.FM.EI.engines[1].getw() > 100F)) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", true);
        }
        if ((this.FM.EI.engines[0].getw() < 100F) && (this.FM.EI.engines[1].getw() < 100F)) {
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
        this.hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, this.dynamoOrient);
    }

    public void moveWheelSink() {
        this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 1.0F, 0.0F, 40F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, 40F), 0.0F);
        this.hierMesh().chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 1.0F, 0.0F, 30F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, 30F), 0.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, 1.2F);
        this.hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1") || s.endsWith("p2")) {
                    this.getEnergyPastArmor(16.65F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                if (s.endsWith("1")) {
                    if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                } else if (s.endsWith("2")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < 0.33F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                            Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine 1 Controls Disabled..");
                        }
                        if (World.Rnd().nextFloat() < 0.33F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                            Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine 2 Controls Disabled..");
                        }
                    }
                } else if (s.endsWith("3") || s.endsWith("4")) {
                    if (World.Rnd().nextFloat() < 0.12F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                } else if (World.Rnd().nextFloat() < 0.12F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Arone Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.5F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if ((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if ((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if ((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if ((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if ((s.endsWith("lo1") || s.endsWith("lo2")) && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if ((s.endsWith("ro1") || s.endsWith("ro2")) && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int i = s.charAt(5) - 49;
                Aircraft.debugprintln(this, "*** Engine" + i + " Hit..");
                if (s.endsWith("case") && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                    if (World.Rnd().nextFloat() < (shot.power / 200000F)) {
                        this.FM.AS.setEngineStuck(shot.initiator, i);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                        this.FM.AS.hitEngine(shot.initiator, i, 2);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 28000F)) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                    }
                    this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[i].getReadyness() + "..");
                }
                if (s.endsWith("cyl1") && (this.getEnergyPastArmor(0.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[i].getCylindersRatio() * 1.75F))) {
                    this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                    if (this.FM.AS.astateEngineStates[i] < 1) {
                        this.FM.AS.hitEngine(shot.initiator, i, 1);
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                        this.FM.AS.hitEngine(shot.initiator, i, 3);
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F)) {
                    this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                }
                if (s.endsWith("prop") && (this.getEnergyPastArmor(0.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                    this.FM.EI.engines[i].setKillPropAngleDevice(shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 48;
                switch (j) {
                    case 1:
                    case 2:
                        this.doHitMeATank(shot, 0);
                        break;

                    case 3:
                        this.doHitMeATank(shot, 1);
                        break;

                    case 4:
                    case 5:
                        this.doHitMeATank(shot, 2);
                        break;
                }
                return;
            }
            if (s.startsWith("xxw1") || s.startsWith("xxoil1")) {
                if ((World.Rnd().nextFloat() < 0.12F) && (this.getEnergyPastArmor(2.25F, shot) > 0.0F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Oil Radiator L Hit..");
                }
                return;
            }
            if (s.startsWith("xxw2") || s.startsWith("xxoil1")) {
                if ((World.Rnd().nextFloat() < 0.12F) && (this.getEnergyPastArmor(2.25F, shot) > 0.0F)) {
                    this.FM.AS.hitOil(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Oil Radiator R Hit..");
                }
                return;
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
                return;
            }
            if (s.startsWith("xxmgun0")) {
                int k = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(0.75F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
                    this.FM.AS.setJamBullets(0, k);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if (s.startsWith("xxcannon0")) {
                int l = s.charAt(9) - 49;
                if (this.getEnergyPastArmor(6.29F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Cannon (" + l + ") Disabled..");
                    this.FM.AS.setJamBullets(1, l);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            this.hitChunk("CF", shot);
            if (point3d.x > 0.0D) {
                if (World.Rnd().nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
                if (point3d.z > 0.4D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
            }
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) {
            this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr")) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl")) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr")) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid")) {
                if (this.chunkDamageVisible("WingLMid") < 3) {
                    this.hitChunk("WingLMid", shot);
                }
                if (World.Rnd().nextFloat() < (shot.mass + 0.02F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
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
            if (s.endsWith("1")) {
                if (World.Rnd().nextFloat() < 0.05F) {
                    this.debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
            } else if ((World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
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

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                // fall through

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.bObserverKilled = true;
                // fall through

            default:
                return;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.endsWith("mm")) {
            this.hierMesh().chunkVisible("Turret1BB_D0", true);
            this.hierMesh().chunkVisible("Leather_Window", true);
        }
        if (this.thisWeaponsName.startsWith("2x")) {
            this.hierMesh().chunkVisible("PylonL_D0", true);
            this.hierMesh().chunkVisible("PylonR_D0", true);
        }
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
        this.computeVerticalThrust();
        this.computeOrizzontalThrust();
        this.computeOrizzontalThrust2();
        this.computeHovering();
        this.computeEngine();
        if (this.hierMesh().isChunkVisible("Keel1_CAP")) {

        }
        if ((this == World.getPlayerAircraft()) && (this.FM.turret.length > 0) && (this.FM.AS.astatePilotStates[1] < 90) && this.FM.turret[0].bIsAIControlled && ((this.FM.getOverload() > 3F) || (this.FM.getOverload() < -0.7F))) {
            Voice.speakRearGunShake();
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
        } else {
            return (f1 >= -3.1F) || (f1 <= -4.6F);
        }
    }

    public void computeVerticalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 210F, 1.0F, 0.2F);
        float f3 = Aircraft.cvt(this.FM.getAltitude(), 0.0F, 5000F, 1.0F, 0.5F);
        float f4 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 < 15F)) {
            f4 = 1.5F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && flag && (f1 > 15F)) {
            f4 = 1.12F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && !flag) {
            f4 = 1.4F * f;
        }
        this.FM.producedAF.z += f4 * (10F * this.FM.M.mass) * (1.0F * f2) * (1.0F * f3);
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
            f3 = 0.35F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (f1 > 5F) && flag) {
            f3 = 0.15F * f;
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
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && !flag && (f1 < 35F) && (this.FM.getSpeedKMH() > 12F) && this.FM.CT.StabilizerControl) {
            f3 = 0.4F * f;
        }
        this.FM.producedAF.x -= f3 * (10F * this.FM.M.mass) * (1.0F * f2);
    }

    public void computeEngine() {
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5)) {
            if (this.FM.EI.getPowerOutput() <= 0.0F) {

            }
        }
        if (f > 5D) {
            f1 = 18F;
        } else {
            float f2 = f * f;
            float f3 = f2 * f;
            f1 = ((0.139683F * f3) - (0.71746F * f2)) + (3.69524F * f);
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    private int           obsLookTime;
    private float         obsLookAzimuth;
    private float         obsLookElevation;
    private float         obsAzimuth;
    private float         obsElevation;
    private float         obsAzimuthOld;
    private float         obsElevationOld;
    private float         obsMove;
    private float         obsMoveTot;
    boolean               bObserverKilled;
    public static boolean bChangedPit = false;
    public Loc            suka;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    private int           rotorrpm;

    static {
        Class class1 = MI8MT.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mi-8");
        Property.set(class1, "meshName", "3DO/Plane/Mi-8/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-8.fmd:MI");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMi8.class, CockpitMi8_TGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9, 2, 2, 2, 2, 7, 7 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_BombSpawn01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Flare01", "_Flare02" });
    }
}
