package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class GLADIATOR extends Scheme1 implements TypeFighter, TypeTNBFighter {

    public GLADIATOR() {
        this.bChangedPit = true;
        this.hasSkis = false;
        this.suspension = 0.0F;
        this.skiAngleL = 0.0F;
        this.skiAngleR = 0.0F;
        this.spring = 0.15F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) this.bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) this.bChangedPit = true;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) this.getEnergyPastArmor(8.1D / (Math.abs(v1.x) + 9.9999997473787516E-006D), shot);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.3F), shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                                debugprintln(this, "*** Rudder Controls: Disabled..");
                            }
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                                debugprintln(this, "*** Elevator Controls: Disabled..");
                            }
                        }
                        // fall through

                    case 2:
                    case 3:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                        }
                        break;
                }
            }
            if (s.startsWith("xxspar")) {
                debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(9.5F / (float) Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F) {
                    debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if (s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                }
                if (s.startsWith("xxsparri") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                }
                if (s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                }
                if (s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                }
                if (s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                }
                if (s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                }
                if (s.startsWith("xxstabl") && this.getEnergyPastArmor(16.2F, shot) > 0.0F) {
                    debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D" + this.chunkDamageVisible("StabL"), shot.initiator);
                }
                if (s.startsWith("xxstabr") && this.getEnergyPastArmor(16.2F, shot) > 0.0F) {
                    debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D" + this.chunkDamageVisible("StabR"), shot.initiator);
                }
            }
            if (s.startsWith("xxlock")) {
                debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    debugprintln(this, "*** Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if (s.startsWith("xxeng")) {
                debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("prop")) {
                    if (this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 175000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 50000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12.7F, shot);
                } else if (s.startsWith("xxeng1cyls")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.12F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 48000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.005F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.endsWith("eqpt")) {
                    if (this.getEnergyPastArmor(0.2721F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                            debugprintln(this, "*** Engine Module: Magneto 0 Destroyed..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                            debugprintln(this, "*** Engine Module: Magneto 1 Destroyed..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            debugprintln(this, "*** Engine Module: Prop Controls Cut..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            debugprintln(this, "*** Engine Module: Throttle Controls Cut..");
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                            debugprintln(this, "*** Engine Module: Mix Controls Cut..");
                        }
                    }
                } else if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
            }
            if (s.startsWith("xxoil")) {
                this.FM.AS.hitOil(shot.initiator, 0);
                this.getEnergyPastArmor(0.22F, shot);
                debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
            }
            if (s.startsWith("xxtank1") && this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F) {
                if (this.FM.AS.astateTankStates[0] == 0) {
                    debugprintln(this, "*** Fuel Tank: Pierced..");
                    this.FM.AS.hitTank(shot.initiator, 0, 1);
                }
                if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.hitTank(shot.initiator, 0, 2);
                    debugprintln(this, "*** Fuel Tank: Hit..");
                }
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                if (s.endsWith("03")) {
                    debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.endsWith("04")) {
                    debugprintln(this, "*** Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            }
            return;
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
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
            if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 1) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
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

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if (explosion.chunkName != null && explosion.power > 0.0F && explosion.chunkName.startsWith("Tail1")) {
            if (World.Rnd().nextFloat(0.0F, 0.038F) < explosion.power) this.FM.AS.setControlsDamage(explosion.initiator, 1);
            if (World.Rnd().nextFloat(0.0F, 0.042F) < explosion.power) this.FM.AS.setControlsDamage(explosion.initiator, 2);
        }
        super.msgExplosion(explosion);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER() && World.cur().camouflage == 1) {
            this.hierMesh().chunkVisible("GearL1_D0", false);
            this.hierMesh().chunkVisible("GearL11_D0", true);
            this.hierMesh().chunkVisible("GearR1_D0", false);
            this.hierMesh().chunkVisible("GearR11_D0", true);
            this.hierMesh().chunkVisible("GearC1_D0", false);
            this.hierMesh().chunkVisible("GearC11_D0", true);
            this.FM.CT.bHasBrakeControl = false;
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = f * -0.6F;
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLrod1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLrod2_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLn_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRrod1_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRrod2_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, -30F * f, 0.0F);
    }

    public void moveWheelSink() {
        if (this.FM.Gears.onGround()) this.suspension = this.suspension + 0.008F;
        else this.suspension = this.suspension - 0.008F;
        if (this.suspension < 0.0F) {
            this.suspension = 0.0F;
            if (!this.FM.isPlayers()) this.FM.Gears.bTailwheelLocked = true;
        }
        if (this.suspension > 0.1F) this.suspension = 0.1F;
        Aircraft.xyz[0] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        float f = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 25F, 0.0F, 1.0F);
        float f1 = this.FM.Gears.gWheelSinking[0] * f + this.suspension;
        Aircraft.xyz[2] = Aircraft.cvt(f1, 0.0F, 0.24F, 0.0F, 0.24F);
        Aircraft.xyz[1] = Aircraft.cvt(f1, 0.0F, 0.24F, 0.0F, -0.12F);
        this.hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        f1 = this.FM.Gears.gWheelSinking[1] * f + this.suspension;
        Aircraft.xyz[2] = Aircraft.cvt(f1, 0.0F, 0.24F, 0.0F, 0.24F);
        Aircraft.xyz[1] = Aircraft.cvt(f1, 0.0F, 0.24F, 0.0F, 0.12F);
        this.hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            super.moveFan(f);
            if (World.cur().camouflage == 1) {
                float f1 = Aircraft.cvt(this.FM.getSpeed(), 30F, 75F, 1.0F, 0.0F);
                float f2 = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 30F, 0.0F, 0.5F);
                if (this.FM.Gears.gWheelSinking[0] > 0.0F) {
                    this.skiAngleL = 0.5F * this.skiAngleL + 0.5F * this.FM.Or.getTangage();
                    if (this.skiAngleL > 20F) this.skiAngleL = this.skiAngleL - this.spring;
                    this.hierMesh().chunkSetAngles("GearL11_D0", World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) + this.skiAngleL, World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(f2, f2));
                    if (this.FM.Gears.gWheelSinking[1] == 0.0F && this.FM.Or.getRoll() < 365F && this.FM.Or.getRoll() > 355F) {
                        this.skiAngleR = this.skiAngleL;
                        this.hierMesh().chunkSetAngles("GearR11_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleR);
                    }
                } else {
                    if (this.skiAngleL > f1 * -10F + 0.01D) this.skiAngleL = this.skiAngleL - this.spring;
                    else if (this.skiAngleL < f1 * -10F - 0.01D) this.skiAngleL = this.skiAngleL + this.spring;
                    this.hierMesh().chunkSetAngles("GearL11_D0", this.skiAngleL, 0.0F, 0.0F);
                }
                if (this.FM.Gears.gWheelSinking[1] > 0.0F) {
                    this.skiAngleR = 0.5F * this.skiAngleR + 0.5F * this.FM.Or.getTangage();
                    if (this.skiAngleR > 20F) this.skiAngleR = this.skiAngleR - this.spring;
                    this.hierMesh().chunkSetAngles("GearR11_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleR);
                } else {
                    if (this.skiAngleR > f1 * -10F + 0.01D) this.skiAngleR = this.skiAngleR - this.spring;
                    else if (this.skiAngleR < f1 * -10F - 0.01D) this.skiAngleR = this.skiAngleR + this.spring;
                    this.hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, -this.skiAngleR);
                }
                this.hierMesh().chunkSetAngles("GearC11_D0", (this.skiAngleL + this.skiAngleR) / 2.0F, 0.0F, 0.0F);
            }
        }
    }

    public void sfxWheels() {
        if (!this.hasSkis) super.sfxWheels();
    }

    public boolean cut(String s) {
        boolean flag = super.cut(s);
        if (s.equalsIgnoreCase("WingLIn")) this.hierMesh().chunkVisible("WingLMid_CAP", true);
        else if (s.equalsIgnoreCase("WingRIn")) this.hierMesh().chunkVisible("WingRMid_CAP", true);
        else if (s.equalsIgnoreCase("GearL2")) this.hierMesh().chunkVisible("GearL_Cap", true);
        else if (s.equalsIgnoreCase("GearR2")) this.hierMesh().chunkVisible("GearR_Cap", true);
        return flag;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 11:
                this.hierMesh().chunkVisible("PodkosLw_D0", false);
                this.hierMesh().chunkVisible("PodkosRw_D0", false);
                break;

            case 17:
                this.hierMesh().chunkVisible("PodkosLw_D0", false);
                break;

            case 18:
                this.hierMesh().chunkVisible("PodkosLw_D0", false);
                break;

            case 19:
                this.FM.Gears.hitCentreGear();
                this.hierMesh().chunkVisible("WireC_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean bChangedPit;
    public boolean hasSkis;
    private float  suspension;
    private float  skiAngleL;
    private float  skiAngleR;
    private float  spring;

    static {
        Class class1 = GLADIATOR.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
