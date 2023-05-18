package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public abstract class He115x extends Scheme2 implements TypeBomber, TypeStormovik, TypeSeaPlane {

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -120F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 120F * f, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
                this.hitProp(0, j, actor);
                this.cut("Engine1");
                break;

            case 36:
            case 37:
                this.hitProp(1, j, actor);
                this.cut("Engine2");
                break;

            case 19:
                this.killPilot(this, 2);
                this.killPilot(this, 1);
                this.hierMesh().chunkVisible("Blister1_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public static void moveGear(HierMesh hiermesh1, float f1) {
    }

    protected void moveGear(float f) {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxammo1")) {
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setJamBullets(0, World.Rnd().nextInt(0, 5));
                }
                this.getEnergyPastArmor(11.4F, shot);
                return;
            }
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(12.1D / Math.abs(Aircraft.v1.x), shot);
                }
                if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 60F) / Math.abs(Aircraft.v1.x), shot);
                    if (shot.power <= 0.0F) {
                        Aircraft.debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        this.doRicochetBack(shot);
                    }
                }
                if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(12.7F, shot);
                }
                if (s.endsWith("p4")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (s.endsWith("p5")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.z) + 0.000001D), shot);
                }
                if (s.endsWith("p6")) {
                    this.getEnergyPastArmor(4.1D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (s.endsWith("p8")) {
                    this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                if (s.length() == 12) {
                    i = 10 + (s.charAt(11) - 48);
                }
                switch (i) {
                    case 8:
                    case 9:
                    default:
                        break;

                    case 1:
                    case 3:
                        if (this.getEnergyPastArmor(3F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Aileron Controls Out..");
                        }
                        break;

                    case 2:
                        this.getEnergyPastArmor(1.5F, shot);
                        break;

                    case 4:
                        if (this.getEnergyPastArmor(1.5F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.debuggunnery("*** Engine1 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            this.debuggunnery("*** Engine1 Prop Controls Out..");
                        }
                        break;

                    case 5:
                        if (this.getEnergyPastArmor(1.5F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            this.debuggunnery("*** Engine2 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            this.debuggunnery("*** Engine2 Prop Controls Out..");
                        }
                        break;

                    case 6:
                    case 7:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Ailerons Controls Out..");
                        }
                        break;

                    case 10:
                    case 11:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.12F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        break;

                    case 12:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.12F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int j = 0;
                if (s.startsWith("xxeng2")) {
                    j = 1;
                }
                this.debuggunnery("Engine Module[" + j + "]: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 280000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 100000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(0.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[j].getCylindersRatio() * 0.66F))) {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 1000000F)) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("eqpt") || (s.endsWith("cyls") && (World.Rnd().nextFloat() < 0.01F))) {
                    if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                        }
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("mag1") || s.endsWith("mag2")) {
                    this.debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                    this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, j);
                }
                if (s.endsWith("oil1")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                        this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    }
                    this.FM.AS.hitOil(shot.initiator, j);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockk1") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlocksl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlocksr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                int k = 0;
                if (s.endsWith("2")) {
                    k = 1;
                }
                if ((this.getEnergyPastArmor(0.21F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.2435F)) {
                    this.FM.AS.hitOil(shot.initiator, k);
                }
                Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Oil Tank Pierced..");
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
                }
                if ((s.endsWith("e1") || s.endsWith("e2")) && (this.getEnergyPastArmor(28F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine1 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine1_D2", shot.initiator);
                }
                if ((s.endsWith("e3") || s.endsWith("e4")) && (this.getEnergyPastArmor(28F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine2 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine2_D2", shot.initiator);
                }
                if (s.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 2) && (this.getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && (this.chunkDamageVisible("StabL") > 2) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && (this.chunkDamageVisible("StabR") > 2) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 48;
                switch (l) {
                    case 1:
                        this.doHitMeATank(shot, 0);
                        break;

                    case 2:
                        this.doHitMeATank(shot, 1);
                        break;

                    case 3:
                        this.doHitMeATank(shot, 2);
                        break;

                    case 4:
                        this.doHitMeATank(shot, 3);
                        break;

                    case 5:
                        this.doHitMeATank(shot, 1);
                        this.doHitMeATank(shot, 2);
                        break;
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) {
                this.hitChunk("CF", shot);
            }
            if (point3d.x > 1.471D) {
                if ((point3d.z > 0.552D) && (point3d.x > 2.37D)) {
                    if (point3d.y > 0.0D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                    } else {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                    }
                }
                if ((point3d.z > 0.0D) && (point3d.z < 0.539D)) {
                    if (point3d.y > 0.0D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                    } else {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                    }
                }
                if ((point3d.x < 2.407D) && (point3d.z > 0.552D)) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
                if ((point3d.x > 2.6D) && (point3d.z > 0.693D)) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Taill1") < 2) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 2) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) {
                this.hitChunk("StabL", shot);
            }
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 2) {
                this.hitChunk("VatorL", shot);
            }
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 2) {
                this.hitChunk("WingLIn", shot);
            }
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 2) {
                this.hitChunk("WingRIn", shot);
            }
        } else if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 2) {
                this.hitChunk("WingLMid", shot);
            }
        } else if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 2) {
                this.hitChunk("WingRMid", shot);
            }
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 2) {
                this.hitChunk("WingLOut", shot);
            }
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 2) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 2) {
                this.hitChunk("AroneL", shot);
            }
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 2) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xbaydoorl")) {
            if (this.chunkDamageVisible("Bay1") < 2) {
                this.hitChunk("Bay1", shot);
            }
        } else if (s.startsWith("xbaydoorr")) {
            if (this.chunkDamageVisible("Bay2") < 2) {
                this.hitChunk("Bay2", shot);
            }
        } else if (s.startsWith("xstrutlout")) {
            if (this.chunkDamageVisible("StrutL") < 2) {
                this.hitChunk("StrutL", shot);
            }
        } else if (s.startsWith("xstrutlinf")) {
            if (this.chunkDamageVisible("StrutLIn") < 2) {
                this.hitChunk("StrutLIn", shot);
            }
        } else if (s.startsWith("xstrutlini")) {
            if (this.chunkDamageVisible("StrutLIn") < 2) {
                this.hitChunk("StrutLIn", shot);
            }
        } else if (s.startsWith("xstrutrout")) {
            if (this.chunkDamageVisible("StrutR") < 2) {
                this.hitChunk("StrutR", shot);
            }
        } else if (s.startsWith("xstrutrini")) {
            if (this.chunkDamageVisible("StrutRIn") < 2) {
                this.hitChunk("StrutRIn", shot);
            }
        } else if (s.startsWith("xgearl2")) {
            if (this.chunkDamageVisible("GearL2") < 2) {
                this.hitChunk("GearL2", shot);
            }
        } else if (s.startsWith("xgearr2")) {
            if (this.chunkDamageVisible("GearR2") < 2) {
                this.hitChunk("GearR2", shot);
            }
        } else if (s.startsWith("xcowl1")) {
            if (this.chunkDamageVisible("Eng1") < 2) {
                this.hitChunk("Eng1", shot);
            }
        } else if (s.startsWith("xcowl2")) {
            if (this.chunkDamageVisible("Eng2") < 2) {
                this.hitChunk("Eng2", shot);
            }
        } else if (s.startsWith("xflap1")) {
            if (this.chunkDamageVisible("Flap1") < 2) {
                this.hitChunk("Flap1", shot);
            }
        } else if (s.startsWith("xflapl2")) {
            if (this.chunkDamageVisible("FlapL02") < 2) {
                this.hitChunk("FlapL02", shot);
            }
        } else if (s.startsWith("xflapr2")) {
            if (this.chunkDamageVisible("FlapR02") < 2) {
                this.hitChunk("FlapR02", shot);
            }
        } else if (s.startsWith("xturret")) {
            if (s.endsWith("1b")) {
                this.FM.AS.setJamBullets(10, 0);
                this.FM.AS.setJamBullets(10, 1);
            }
            if (s.endsWith("2b")) {
                this.FM.AS.setJamBullets(11, 0);
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

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < 0.0F) {
                    f = 0.0F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 15F) {
                    f1 = 15F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 2:
                this.FM.turret[1].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                // fall through

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("Head2_D0", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("Head3_D0", false);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if ((this.FM.AS.astateEngineStates[0] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 0, 1);
            }
            if ((this.FM.AS.astateEngineStates[1] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 1, 1);
            }
            if ((this.FM.AS.astateEngineStates[2] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 2, 1);
            }
            if ((this.FM.AS.astateEngineStates[3] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 3, 1);
            }
        }
        for (int i = 1; i < 4; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    public void update(float f) {
        super.update(f);
        if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
            float f1 = this.FM.EI.engines[0].getRPM();
            if ((f1 < 300F) && (f1 > 30F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f1) / 3000F;
            }
            float f3 = this.FM.EI.engines[0].getRPM();
            if ((f3 < 1000F) && (f3 > 301F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f3) / 8000F;
            }
            float f5 = this.FM.EI.engines[0].getRPM();
            if ((f5 > 1001F) && (f5 < 1500F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.07F;
            }
            float f7 = this.FM.EI.engines[0].getRPM();
            if ((f7 > 1501F) && (f7 < 2000F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.05F;
            }
            float f9 = this.FM.EI.engines[0].getRPM();
            if ((f9 > 2001F) && (f9 < 2500F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.04F;
            }
            float f11 = this.FM.EI.engines[0].getRPM();
            if (f11 > 2501F) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.03F;
            }
        }
        if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
            float f2 = this.FM.EI.engines[1].getRPM();
            if ((f2 < 300F) && (f2 > 30F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f2) / 3000F;
            }
            float f4 = this.FM.EI.engines[1].getRPM();
            if ((f4 < 1000F) && (f4 > 301F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f4) / 8000F;
            }
            float f6 = this.FM.EI.engines[1].getRPM();
            if ((f6 > 1001F) && (f6 < 1500F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.07F;
            }
            float f8 = this.FM.EI.engines[1].getRPM();
            if ((f8 > 1501F) && (f8 < 2000F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.05F;
            }
            float f10 = this.FM.EI.engines[1].getRPM();
            if ((f10 > 2001F) && (f10 < 2500F)) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.04F;
            }
            float f12 = this.FM.EI.engines[1].getRPM();
            if (f12 > 2501F) {
                ((RealFlightModel) this.FM).producedShakeLevel = 0.03F;
            }
        }
        if ((this.FM.getSpeedKMH() > 250F) && (this.FM.getVertSpeed() > 0.0F) && (this.FM.getAltitude() < 5000F)) {
            this.FM.producedAF.x += 20F * (250F - this.FM.getSpeedKMH());
        }
        if (this.FM.isPlayers() && (this.FM.Sq.squareElevators > 0.0F)) {
            RealFlightModel realflightmodel = (RealFlightModel) this.FM;
            if (realflightmodel.RealMode && (realflightmodel.indSpeed > 120F)) {
                float f2 = 1.0F + (0.005F * (120F - realflightmodel.indSpeed));
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }
                this.FM.SensPitch = 0.45F * f2;
                if (realflightmodel.indSpeed > 120F) {
                    this.FM.producedAM.y -= 1720F * (120F - realflightmodel.indSpeed);
                }
            } else {
                this.FM.SensPitch = 0.56F;
            }
        }
    }

    static {
        Property.set(He115x.class, "originCountry", PaintScheme.countryGermany);
    }
}
