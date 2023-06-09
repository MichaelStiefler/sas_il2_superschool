package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BC18 extends Scheme2 implements TypeScout, TypeStormovik, TypeTransport {

    public BC18() {
        this.BC18hangedExts = false;
        BC18.BC18hangedPit = true;
        this.fSightSetForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 250F;
        this.fSightCurReadyness = 0.0F;
    }

    public float getWheelWidth(int i) {
        return i <= 1 ? 0.6F : 0.2F;
    }

    public float getEyeLevelCorrection() {
        return -0.025F;
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveBayDoor(float f) {
    }

    public void update(float f) {
        World.cur().diffCur.Torque_N_Gyro_Effects = false;
        super.update(f);
        this.FM.EI.engines[0].addVside *= 9.9999999999999995E-008D;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 70F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -70F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -100F * f, 0.0F);
    }

    protected void moveGear(float f) {
        BC18.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * f, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.25F, 0.0F, 0.23F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.25F, 0.0F, 0.2F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.25F, 0.0F, 0.23F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.25F, 0.0F, 0.2F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if ((this.FM.AS.astateEngineStates[0] > 3) && (World.Rnd().nextFloat() < 0.39F)) {
                this.FM.AS.hitTank(this, 0, 1);
            }
            if ((this.FM.AS.astateEngineStates[1] > 3) && (World.Rnd().nextFloat() < 0.39F)) {
                this.FM.AS.hitTank(this, 1, 1);
            }
            if ((this.FM.AS.astateTankStates[0] > 4) && (World.Rnd().nextFloat() < 0.035F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            }
            if ((this.FM.AS.astateTankStates[1] > 4) && (World.Rnd().nextFloat() < 0.035F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            }
            if ((this.FM.AS.astateTankStates[2] > 4) && (World.Rnd().nextFloat() < 0.035F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            }
            if ((this.FM.AS.astateTankStates[3] > 4) && (World.Rnd().nextFloat() < 0.035F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
            }
        }
        for (int i = 1; i < 2; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

        if (this.FM.AS.bNavLightsOn) {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            this.pos.getAbs(point3d, orient);
            BC18.l.set(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, this.findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            eff3dactor.draw.lightMap().put("light", lightpointactor);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                this.hitProp(0, j, actor);
                return super.cutFM(34, j, actor);

            case 36:
                this.hitProp(1, j, actor);
                return super.cutFM(37, j, actor);

            case 13:
                this.killPilot(this, 0);
                this.killPilot(this, 1);
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(5F, shot);
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(5F, shot);
                } else if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(5F, shot);
                } else if (s.endsWith("e1") || s.endsWith("e2")) {
                    this.getEnergyPastArmor(5F, shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if ((World.Rnd().nextFloat() < 0.1F) || (shot.mass > 0.092F)) {
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            }
                            if (World.Rnd().nextFloat() < 0.5F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            }
                        }
                        // fall through

                    case 2:
                        if ((World.Rnd().nextFloat() < 0.1F) || (shot.mass > 0.092F)) {
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            }
                            if (World.Rnd().nextFloat() < 0.5F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            }
                        }
                        // fall through

                    case 3:
                    case 4:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.12F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                                Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.12F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                                Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                            }
                        }
                        break;

                    case 5:
                    case 6:
                        if ((this.getEnergyPastArmor(0.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                        }
                        break;
                }
            }
            if (s.startsWith("xxspar")) {
                if ((s.endsWith("lm1") || s.endsWith("lm2")) && (World.Rnd().nextFloat() < Aircraft.cvt((float) Math.abs(Aircraft.v1.x), 0.0F, 1.0F, 1.0F, 0.08F)) && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if ((s.endsWith("rm1") || s.endsWith("rm2")) && (World.Rnd().nextFloat() < Aircraft.cvt((float) Math.abs(Aircraft.v1.x), 0.0F, 1.0F, 1.0F, 0.08F)) && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if ((s.endsWith("lo1") || s.endsWith("lo2")) && (World.Rnd().nextFloat() < Aircraft.cvt((float) Math.abs(Aircraft.v1.x), 0.0F, 1.0F, 1.0F, 0.33F)) && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if ((s.endsWith("ro1") || s.endsWith("ro2")) && (World.Rnd().nextFloat() < Aircraft.cvt((float) Math.abs(Aircraft.v1.x), 0.0F, 1.0F, 1.0F, 0.33F)) && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(12.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if ((s.endsWith("k1") || s.endsWith("k2")) && (World.Rnd().nextFloat() < Aircraft.cvt((float) Math.abs(Aircraft.v1.x), 0.0F, 1.0F, 1.0F, 0.26F)) && (this.chunkDamageVisible("Keel1") > 1) && (this.getEnergyPastArmor(8.6F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
            }
            if (s.startsWith("xxstruts")) {
                if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(26F, shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Engine1 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if (s.endsWith("2") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(26F, shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Engine2 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
            }
            if (s.startsWith("xxbomb") && (World.Rnd().nextFloat() < 0.01F) && (this.FM.CT.Weapons[3] != null) && this.FM.CT.Weapons[3][0].haveBullets()) {
                Aircraft.debugprintln(this, "*** Bomb Payload Detonates..");
                this.FM.AS.hitTank(shot.initiator, 0, 10);
                this.FM.AS.hitTank(shot.initiator, 1, 10);
                this.FM.AS.hitTank(shot.initiator, 2, 10);
                this.FM.AS.hitTank(shot.initiator, 3, 10);
                this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if (s.startsWith("xxprop")) {
                int j = 0;
                if (s.endsWith("2")) {
                    j = 1;
                }
                this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Governor Damaged..");
                if ((this.getEnergyPastArmor(2.0F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Governor Failed..");
                }
            }
            if (s.startsWith("xxengine")) {
                int k = 0;
                if (s.startsWith("xxengine2")) {
                    k = 1;
                }
                if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 4.3F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[k].getCylindersRatio() * 1.12F))) {
                    this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 9124F)));
                    Aircraft.debugprintln(this, "*** Engine" + (k + 1) + " Cylindres Damaged, " + this.FM.EI.engines[k].getCylindersOperable() + "/" + this.FM.EI.engines[k].getCylinders() + " left..");
                }
                if (World.Rnd().nextFloat(0.0F, 18000F) < shot.power) {
                    this.FM.AS.hitEngine(shot.initiator, k, 1);
                }
                this.FM.AS.hitOil(shot.initiator, k);
            }
            if (s.startsWith("xxoil")) {
                int i1 = 0;
                if (s.startsWith("xxoil2")) {
                    i1 = 1;
                }
                this.FM.AS.hitOil(shot.initiator, i1);
            }
            if (s.startsWith("xxtank")) {
                int j1 = s.charAt(6) - 49;
                if ((j1 < 4) && (this.getEnergyPastArmor(0.1F, shot) > 0.0F)) {
                    if (shot.power < 14100F) {
                        if (this.FM.AS.astateTankStates[j1] < 1) {
                            this.FM.AS.hitTank(shot.initiator, j1, 2);
                        }
                        if ((this.FM.AS.astateTankStates[j1] < 4) && (World.Rnd().nextFloat() < 0.1F)) {
                            this.FM.AS.hitTank(shot.initiator, j1, 1);
                        }
                        if ((shot.powerType == 3) && (this.FM.AS.astateTankStates[j1] > 2) && (World.Rnd().nextFloat() < 0.07F)) {
                            this.FM.AS.hitTank(shot.initiator, j1, 10);
                        }
                    } else {
                        this.FM.AS.hitTank(shot.initiator, j1, World.Rnd().nextInt(0, (int) (shot.power / 20000F)));
                    }
                }
                if ((j1 == 4) && (this.getEnergyPastArmor(1.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.06F)) {
                    for (int k1 = 0; k1 < 4; k1++) {
                        this.FM.AS.hitOil(shot.initiator, k1);
                    }

                }
            }
            if (s.startsWith("xxammo") && (World.Rnd().nextFloat() < 0.01F)) {
                this.FM.AS.hitTank(shot.initiator, 2, 10);
                this.FM.AS.explodeTank(shot.initiator, 2);
            }
            if (s.startsWith("xxmgun1")) {
                this.FM.AS.setJamBullets(0, 0);
            }
            return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (!s.startsWith("xnose")) {
            if (s.startsWith("xtail")) {
                if (this.chunkDamageVisible("Tail1") < 3) {
                    this.hitChunk("Tail1", shot);
                }
            } else if (s.startsWith("xkeel")) {
                if (this.chunkDamageVisible("Keel1") < 2) {
                    this.hitChunk("Keel1", shot);
                }
            } else if (s.startsWith("xrudder")) {
                this.hitChunk("Rudder1", shot);
            } else if (s.startsWith("xstabl")) {
                this.hitChunk("StabL", shot);
            } else if (s.startsWith("xstabr")) {
                this.hitChunk("StabR", shot);
            } else if (s.startsWith("xvatorl")) {
                this.hitChunk("VatorL", shot);
            } else if (s.startsWith("xvatorr")) {
                this.hitChunk("VatorR", shot);
            } else if (s.startsWith("xwinglmid")) {
                if (this.chunkDamageVisible("WingLMid") < 3) {
                    this.hitChunk("WingLMid", shot);
                }
            } else if (s.startsWith("xwingrmid")) {
                if (this.chunkDamageVisible("WingRMid") < 3) {
                    this.hitChunk("WingRMid", shot);
                }
            } else if (s.startsWith("xwinglout")) {
                if (this.chunkDamageVisible("WingLOut") < 3) {
                    this.hitChunk("WingLOut", shot);
                }
            } else if (s.startsWith("xwingrout")) {
                if (this.chunkDamageVisible("WingROut") < 3) {
                    this.hitChunk("WingROut", shot);
                }
            } else if (s.startsWith("xaronel")) {
                this.hitChunk("AroneL", shot);
            } else if (s.startsWith("xaroner")) {
                this.hitChunk("AroneR", shot);
            } else if (s.startsWith("xengine1")) {
                if (this.chunkDamageVisible("Engine1") < 2) {
                    this.hitChunk("Engine1", shot);
                }
                this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 96000F));
                Aircraft.debugprintln(this, "*** Engine1 Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
            } else if (s.startsWith("xengine2")) {
                if (this.chunkDamageVisible("Engine2") < 2) {
                    this.hitChunk("Engine2", shot);
                }
                this.FM.EI.engines[1].setReadyness(shot.initiator, this.FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 96000F));
                Aircraft.debugprintln(this, "*** Engine2 Hit - Readyness Reduced to " + this.FM.EI.engines[1].getReadyness() + "..");
            } else if (s.startsWith("xgear")) {
                if (World.Rnd().nextFloat() < 0.1F) {
                    Aircraft.debugprintln(this, "*** Gear Actuator Failed..");
                    this.FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else if (s.startsWith("xturret")) {
                if (s.startsWith("xturret1")) {
                    this.FM.AS.setJamBullets(10, 0);
                }
            } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
                byte byte0 = 0;
                int l1;
                if (s.endsWith("a")) {
                    byte0 = 1;
                    l1 = s.charAt(6) - 49;
                } else if (s.endsWith("b")) {
                    byte0 = 2;
                    l1 = s.charAt(6) - 49;
                } else {
                    l1 = s.charAt(5) - 49;
                }
                this.hitFlesh(l1, shot, byte0);
            }
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                break;
        }
    }

    private static Loc    l             = new Loc();
    public boolean        BC18hangedExts;
    public static boolean BC18hangedPit = false;
    public float          fSightSetForwardAngle;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;

    static {
        Class class1 = BC18.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Beechcraft");
        Property.set(class1, "meshName", "3DO/Plane/BC18(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/BeechC18.fmd:BeechC18_FM");
        Property.set(class1, "LOSElevation", 0.73425F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitBeechcraft.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn14", "_ExternalDev01", "_ExternalDev02" });
    }
}
