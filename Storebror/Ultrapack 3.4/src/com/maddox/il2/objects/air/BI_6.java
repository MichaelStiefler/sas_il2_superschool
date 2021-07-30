package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class BI_6 extends Scheme6 implements TypeFighter {

    public BI_6() {
        this.bHasEngine = true;
        this.flame = null;
        this.dust = null;
        this.trail = null;
        this.sprite = null;
    }

    public void destroy() {
        if (Actor.isValid(this.flame)) this.flame.destroy();
        if (Actor.isValid(this.dust)) this.dust.destroy();
        if (Actor.isValid(this.trail)) this.trail.destroy();
        if (Actor.isValid(this.sprite)) this.sprite.destroy();
        super.destroy();
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER()) {
            this.flame = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            this.dust = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100D.eff", -1F);
            this.trail = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100T.eff", -1F);
            this.sprite = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100S.eff", -1F);
            Eff3DActor.setIntesity(this.flame, 0.0F);
            Eff3DActor.setIntesity(this.dust, 0.0F);
            Eff3DActor.setIntesity(this.trail, 0.0F);
            Eff3DActor.setIntesity(this.sprite, 0.0F);
            if (World.cur().camouflage == 1) {
                this.hierMesh().chunkVisible("GearL1_D0", false);
                this.hierMesh().chunkVisible("GearR1_D0", false);
                this.hierMesh().chunkVisible("GearL4_D0", false);
                this.hierMesh().chunkVisible("GearR4_D0", false);
                this.hierMesh().chunkVisible("GearL5_D0", false);
                this.hierMesh().chunkVisible("GearR5_D0", false);
                this.hierMesh().chunkVisible("GearL6_D0", true);
                this.hierMesh().chunkVisible("GearR6_D0", true);
                this.hierMesh().chunkVisible("GearC3_D0", true);
                this.moveGear(0.0F);
                this.FM.CT.bHasBrakeControl = false;
            }
        }
        this.FM.Gears.bTailwheelLocked = true;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    public void doMurderPilot(int i) {
        if (i != 0) return;
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("Pilot1_D1", true);
        this.hierMesh().chunkVisible("HMask1_D0", false);
        if (!this.FM.AS.bIsAboutToBailout) {
            if (this.hierMesh().isChunkVisible("Blister1_D0")) this.hierMesh().chunkVisible("Gore1_D0", true);
            this.hierMesh().chunkVisible("Gore2_D0", true);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 1500F, -80F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
        if (World.cur().camouflage == 1) {
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 5F + 75F * f, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -5F - 75F * f, 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 80F * f, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -80F * f, 0.0F);
        }
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 75F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -75F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -75F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 75F * f, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    protected void moveFlap(float f) {
        float f1 = -50F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    protected void moveFan(float f) {
        if (!Config.isUSE_RENDER()) return;
        if (this.isNetMirror()) {
            if (this.FM.EI.engines[0].getStage() == 6) {
                Eff3DActor.setIntesity(this.flame, 1.0F);
                Eff3DActor.setIntesity(this.dust, 1.0F);
                Eff3DActor.setIntesity(this.trail, 1.0F);
                Eff3DActor.setIntesity(this.sprite, 1.0F);
            } else {
                Eff3DActor.setIntesity(this.flame, 0.0F);
                Eff3DActor.setIntesity(this.dust, 0.0F);
                Eff3DActor.setIntesity(this.trail, 0.0F);
                Eff3DActor.setIntesity(this.sprite, 0.0F);
            }
        } else if (this.bHasEngine && this.FM.CT.getPower() > 0.0F && this.FM.EI.engines[0].getStage() == 6) {
            Eff3DActor.setIntesity(this.flame, 1.0F);
            Eff3DActor.setIntesity(this.dust, 1.0F);
            Eff3DActor.setIntesity(this.trail, 1.0F);
            Eff3DActor.setIntesity(this.sprite, 1.0F);
        } else {
            Eff3DActor.setIntesity(this.flame, 0.0F);
            Eff3DActor.setIntesity(this.dust, 0.0F);
            Eff3DActor.setIntesity(this.trail, 0.0F);
            Eff3DActor.setIntesity(this.sprite, 0.0F);
        }
        if (this.FM.Gears.onGround() && this.FM.CT.getGear() > 0.9F && this.FM.getSpeed() > 5F) {
            if (this.FM.Gears.lgear) this.hierMesh().chunkSetAngles("GearL6_D0", World.Rnd().nextFloat(-3F, 3F), -75F + World.Rnd().nextFloat(-3F, 3F), World.Rnd().nextFloat(-3F, 3F));
            if (this.FM.Gears.rgear) this.hierMesh().chunkSetAngles("GearR6_D0", World.Rnd().nextFloat(-3F, 3F), 75F + World.Rnd().nextFloat(-3F, 3F), World.Rnd().nextFloat(-3F, 3F));
        }
        if (this.FM.EI.engines[1].getThrustOutput() > 0.4F && this.FM.EI.engines[1].getStage() == 6) {
            if (this.FM.EI.engines[1].getThrustOutput() > 0.65F) this.FM.AS.setSootState(this, 1, 5);
            else this.FM.AS.setSootState(this, 1, 4);
        } else this.FM.AS.setSootState(this, 1, 0);
        if (this.FM.EI.engines[2].getThrustOutput() > 0.4F && this.FM.EI.engines[2].getStage() == 6) {
            if (this.FM.EI.engines[2].getThrustOutput() > 0.65F) this.FM.AS.setSootState(this, 2, 5);
            else this.FM.AS.setSootState(this, 2, 4);
        } else this.FM.AS.setSootState(this, 2, 0);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) this.getEnergyPastArmor(12.71F, shot);
                if (s.endsWith("p2")) this.getEnergyPastArmor(12.7D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if (s.startsWith("xxCANNON01")) {
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                    this.FM.AS.setJamBullets(0, 0);
                    this.getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if (s.startsWith("xxCANNON02")) {
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                    this.FM.AS.setJamBullets(0, 1);
                    this.getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if (s.startsWith("xxeng2cas")) {
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) this.FM.AS.hitEngine(shot.initiator, 1, 5);
                return;
            }
            if (s.startsWith("xxeng3cas")) {
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) this.FM.AS.hitEngine(shot.initiator, 2, 5);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 4:
                        if (this.getEnergyPastArmor(4.5F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Ailerons Controls Out..");
                        }
                        break;

                    case 2:
                    case 3:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Ailerons Controls Out..");
                        }
                        break;

                    case 5:
                        if (this.getEnergyPastArmor(1.5F, shot) <= 0.0F) break;
                        if (World.Rnd().nextFloat() < 0.45F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.debuggunnery("*** Engine1 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.45F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            this.debuggunnery("*** Engine2 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.45F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 2, 1);
                            this.debuggunnery("*** Engine3 Throttle Controls Out..");
                        }
                        break;

                    case 6:
                        if (this.getEnergyPastArmor(4F, shot) <= 0.0F) break;
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;

                    case 7:
                        if (this.getEnergyPastArmor(1.0F, shot) <= 0.0F) break;
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;
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
            if (s.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) this.FM.AS.setInternalDamage(shot.initiator, 1);
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** Tail1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Tail1_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F) {
                        this.FM.AS.hitTank(shot.initiator, 0, 2);
                        this.debuggunnery("Fuel Tank: Hit..");
                    }
                    if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass) this.FM.AS.hitEngine(shot.initiator, 0, 5);
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (point3d.x > 0.4D && point3d.x < 1.672D && point3d.z <= 0.399D) if (point3d.x > 1.387D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            else if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
        } else if (s.startsWith("xcockpit")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
        else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 2) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel1")) this.hitChunk("Keel1", shot);
        else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
        else if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
        else if (s.startsWith("xVatorL")) {
            if (this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xVatorR")) {
            if (this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
        } else if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
        } else if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xAroneL")) this.hitChunk("AroneL", shot);
        else if (s.startsWith("xAroneR")) this.hitChunk("AroneR", shot);
        else if (!s.startsWith("xengine1")) if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
        } else if (s.startsWith("xengine3")) {
            if (this.chunkDamageVisible("Engine3") < 2) this.hitChunk("Engine3", shot);
        } else if (s.startsWith("xgearl")) this.hitChunk("GearL2", shot);
        else if (s.startsWith("xgearr")) this.hitChunk("GearR2", shot);
        else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else j = s.charAt(5) - 49;
            this.hitFlesh(j, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 3:
            case 19:
                this.bHasEngine = false;
                this.FM.AS.setEngineDies(this, 0);
                return this.cut(partNames()[i]);

            case 4:
            case 33:
            case 34:
            case 35:
                this.FM.AS.setEngineDies(this, 1);
                return this.cut(partNames()[i]);

            case 5:
            case 36:
            case 37:
            case 38:
                this.FM.AS.setEngineDies(this, 2);
                return this.cut(partNames()[i]);

            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 20:
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
            default:
                return super.cutFM(i, j, actor);
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.isNetMirror()) return;
        this.FM.setCapableOfBMP(true, this);
        this.FM.setCapableOfACM(true);
        this.bPowR = this == World.getPlayerAircraft();
        if (this.FM.getAltitude() - Engine.land().HQ(this.FM.Loc.x, this.FM.Loc.y) > 5D && this.FM.M.fuel > 0.0F) {
            if (this.FM.EI.engines[0].getControlThrottle() > (this.bPowR ? powR : powA) && this.FM.EI.engines[0].getStage() == 0) {
                this.FM.EI.engines[0].setStage(this, 6);
                if (this.bPowR) HUD.log("EngineI" + (this.FM.EI.engines[0].getStage() != 6 ? 48 : '1'));
            }
            if (this.FM.CT.PowerControl < (this.bPowR ? powR : powA) && this.FM.EI.engines[0].getStage() > 0) {
                this.FM.EI.engines[0].setEngineStops(this);
                if (this.bPowR) HUD.log("EngineI" + (this.FM.EI.engines[0].getStage() != 6 ? 48 : '1'));
            }
        }
        if (this.FM.EI.engines[1].getControlThrottle() > 0.8F && this.FM.EI.engines[1].getStage() == 0 && this.FM.M.nitro > 0.0F) this.FM.EI.engines[1].setStage(this, 6);
        if (this.FM.EI.engines[1].getControlThrottle() < 0.8F && this.FM.EI.engines[1].getStage() == 6) this.FM.EI.engines[1].setStage(this, 0);
        if (this.FM.EI.engines[2].getControlThrottle() > 0.8F && this.FM.EI.engines[2].getStage() == 0 && this.FM.M.nitro > 0.0F) this.FM.EI.engines[2].setStage(this, 6);
        if (this.FM.EI.engines[2].getControlThrottle() < 0.8F && this.FM.EI.engines[2].getStage() == 6) this.FM.EI.engines[2].setStage(this, 0);
        if (this.FM.isPlayers() && Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH()) > 750F) {
            v.x = v.z = 0.0D;
            v.y = cvt(Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH()), 750F, 950F, 0.0F, 400000F);
            ((RealFlightModel) this.FM).gunMomentum(v, false);
        }
    }

    private static final float powR = 0.4120879F;
    private static final float powA = 0.77F;
    private boolean            bHasEngine;
    private Eff3DActor         flame;
    private Eff3DActor         dust;
    private Eff3DActor         trail;
    private Eff3DActor         sprite;
    private boolean            bPowR;
    private static Vector3d    v    = new Vector3d();

    static {
        Class class1 = BI_6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BI-6");
        Property.set(class1, "meshName", "3DO/Plane/BI-6/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/BI-6.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBI_6.class });
        Property.set(class1, "LOSElevation", 0.87325F);
        weaponTriggersRegister(class1, new int[] { 0, 0 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
    }
}
