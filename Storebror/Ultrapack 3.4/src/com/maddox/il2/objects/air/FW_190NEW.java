package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.PylonETC501FW190;
import com.maddox.rts.Property;

public abstract class FW_190NEW extends Scheme1 implements TypeFighter, TypeBNZFighter {

    public FW_190NEW() {
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (World.cur().camouflage == 1) {
            this.hierMesh().chunkVisible("GearL5_D0", false);
            this.hierMesh().chunkVisible("GearR5_D0", false);
        }
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj == null) return;
        for (int i = 0; i < aobj.length; i++)
            if (aobj[i] instanceof PylonETC501FW190) {
                this.hierMesh().chunkVisible("GearL5_D0", false);
                this.hierMesh().chunkVisible("GearR5_D0", false);
                return;
            }

    }

    public void update(float f) {
        if (this.FM.AS.bIsAboutToBailout) this.hierMesh().chunkVisible("Wire_D0", false);
        super.update(f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 20F * f, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public boolean cut(String s) {
        if (s.startsWith("Tail1")) this.FM.AS.hitTank(this, 2, 4);
        return super.cut(s);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);

            case 34:
            case 35:
            default:
                return super.cutFM(i, j, actor);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(50F, 50F), shot);
                    if (World.Rnd().nextFloat() < 0.15F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    Aircraft.debugprintln(this, "*** Armor Glass: Hit..");
                    if (shot.power <= 0.0F) {
                        Aircraft.debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        if (World.Rnd().nextFloat() < 0.5F) this.doRicochetBack(shot);
                    }
                } else if (s.endsWith("p3")) {
                    if (point3d.z < -0.27D) this.getEnergyPastArmor(4.1D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-006D), shot);
                    else this.getEnergyPastArmor(8.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-006D), shot);
                } else if (s.endsWith("p6")) this.getEnergyPastArmor(8D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-006D), shot);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    case 7:
                    default:
                        break;

                    case 1:
                    case 4:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                        }
                        break;

                    case 2:
                    case 3:
                        if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls: Disabled..");
                        }
                        break;

                    case 5:
                        if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Elevator Controls: Disabled / Strings Broken..");
                        }
                        break;

                    case 6:
                        if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            Aircraft.debugprintln(this, "*** Rudder Controls: Disabled / Strings Broken..");
                        }
                        break;

                    case 8:
                        if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                            Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(2.4F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(23.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(23.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(23.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(23.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("pipe")) {
                    if (World.Rnd().nextFloat() < 0.1F && this.FM.EI.engines[0].getType() == 0 && this.FM.CT.Weapons[1] != null && this.FM.CT.Weapons[1].length != 2) {
                        this.FM.AS.setJamBullets(1, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Nose Nozzle Pipe Bent..");
                    }
                    this.getEnergyPastArmor(0.3F, shot);
                } else if (s.endsWith("prop")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.9F) if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                    } else {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Damaged..");
                    }
                } else if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(4.6F, shot) > 0.0F) if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Reductor Gear..");
                    } else {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        Aircraft.debugprintln(this, "*** Engine Module: Reductor Gear Damaged, Prop Governor Failed..");
                    }
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
                    }
                    this.getEnergyPastArmor(0.5F, shot);
                } else if (s.endsWith("feed")) {
                    if (this.getEnergyPastArmor(8.9F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F && this.FM.EI.engines[0].getPowerOutput() > 0.7F && this.FM.EI.engines[0].getType() == 0) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 100);
                        Aircraft.debugprintln(this, "*** Engine Module: Pressurized Fuel Line Pierced, Fuel Flamed..");
                    }
                    this.getEnergyPastArmor(1.0F, shot);
                } else if (s.endsWith("fuel")) {
                    if (this.getEnergyPastArmor(1.1F, shot) > 0.0F && this.FM.EI.engines[0].getType() == 0) {
                        this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine Module: Fuel Line Stalled, Engine Stalled..");
                    }
                    this.getEnergyPastArmor(1.0F, shot);
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(4.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 175000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 50000F && this.FM.EI.engines[0].getType() == 0) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        if (this.FM.EI.engines[0].getType() == 0) this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(27.5F, shot);
                } else if (s.startsWith("xxeng1cyl")) {
                    if (this.getEnergyPastArmor(2.4F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * (this.FM.EI.engines[0].getType() == 0 ? 1.75F : 0.5F)) {
                        if (this.FM.EI.engines[0].getType() == 0) this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        else this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19200F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 48000F && this.FM.EI.engines[0].getType() == 0) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 48000F && this.FM.EI.engines[0].getType() == 1) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 1);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.01F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(43.6F, shot);
                    }
                } else if (s.startsWith("xxeng1mag")) {
                    int j = s.charAt(9) - 49;
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                    Aircraft.debugprintln(this, "*** Engine Module: Magneto " + j + " Destroyed..");
                } else if (s.endsWith("sync")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) Aircraft.debugprintln(this, "*** Engine Module: Gun Synchronized Hit, Nose Guns Lose Authority..");
                } else if (s.endsWith("oil1") && this.getEnergyPastArmor(2.4F, shot) > 0.0F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 48;
                switch (k) {
                    default:
                        break;

                    case 1:
                        if (this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            if (this.FM.AS.astateTankStates[2] == 0) {
                                Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                                this.FM.AS.hitTank(shot.initiator, 2, 1);
                                this.FM.AS.doSetTankState(shot.initiator, 2, 1);
                            } else if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.9F || World.Rnd().nextFloat() < 0.03F) {
                                this.FM.AS.hitTank(shot.initiator, 2, 2);
                                Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                            }
                            if (shot.power > 200000F) {
                                this.FM.AS.hitTank(shot.initiator, 2, 99);
                                Aircraft.debugprintln(this, "*** Fuel Tank: Major Hit..");
                            }
                        }
                        break;

                    case 2:
                        if (this.getEnergyPastArmor(1.2F, shot) <= 0.0F || World.Rnd().nextFloat() >= 0.25F) break;
                        if (this.FM.AS.astateTankStates[1] == 0) {
                            Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                            this.FM.AS.hitTank(shot.initiator, 1, 1);
                            this.FM.AS.doSetTankState(shot.initiator, 1, 1);
                        } else if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F || World.Rnd().nextFloat() < 0.03F) {
                            this.FM.AS.hitTank(shot.initiator, 1, 2);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                        }
                        if (shot.power > 200000F) {
                            this.FM.AS.hitTank(shot.initiator, 1, 99);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Major Hit..");
                        }
                        break;

                    case 3:
                        if (this.getEnergyPastArmor(1.2F, shot) <= 0.0F || World.Rnd().nextFloat() >= 0.25F) break;
                        if (this.FM.AS.astateTankStates[0] == 0) {
                            Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                            this.FM.AS.hitTank(shot.initiator, 0, 1);
                            this.FM.AS.doSetTankState(shot.initiator, 0, 1);
                        } else if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F || World.Rnd().nextFloat() < 0.03F) {
                            this.FM.AS.hitTank(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                        }
                        if (shot.power > 200000F) {
                            this.FM.AS.hitTank(shot.initiator, 0, 99);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Major Hit..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxmw50")) {
                if (World.Rnd().nextFloat() < 0.05F) {
                    Aircraft.debugprintln(this, "*** MW50 Tank: Pierced..");
                    this.FM.AS.setInternalDamage(shot.initiator, 2);
                }
                return;
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) this.FM.AS.setJamBullets(1, 0);
                if (s.endsWith("02")) this.FM.AS.setJamBullets(1, 1);
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
                return;
            }
            if (s.startsWith("xxcannon")) {
                Aircraft.debugprintln(this, "*** Nose Cannon: Disabled..");
                this.FM.AS.setJamBullets(0, 0);
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
                return;
            }
            if (s.startsWith("xxradiat")) {
                this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, 0.05F));
                Aircraft.debugprintln(this, "*** Engine Module: Radiator Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
            }
            return;
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (s.startsWith("xcockpit")) {
                if (point3d.z > 0.4D) {
                    if (World.Rnd().nextFloat() < 0.2F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                    if (World.Rnd().nextFloat() < 0.2F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                } else if (point3d.y > 0.0D) {
                    if (World.Rnd().nextFloat() < 0.2F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                } else if (World.Rnd().nextFloat() < 0.2F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if (point3d.x > 0.2D && World.Rnd().nextFloat() < 0.2F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 1) this.hitChunk("StabR", shot);
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
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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
        if (s.startsWith("xxammo")) {
            Aircraft.debugprintln(this, "*** Ammo: Hit..");
            if (s.endsWith("05")) this.getEnergyPastArmor(World.Rnd().nextFloat(50F, 50F), shot);
            if (World.Rnd().nextFloat() < 0.03F) {
                this.debuggunnery("Armament: Cannon Gun (1) Payload Detonates..");
                this.FM.AS.hitTank(shot.initiator, 1, 99);
                this.nextDMGLevels(3, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
            }
        }
        if (s.endsWith("06")) this.getEnergyPastArmor(World.Rnd().nextFloat(50F, 50F), shot);
        if (World.Rnd().nextFloat() < 0.03F) this.nextDMGLevels(3, 2, "WingRMid_D", shot.initiator);
    }

    static {
        Class class1 = FW_190NEW.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
