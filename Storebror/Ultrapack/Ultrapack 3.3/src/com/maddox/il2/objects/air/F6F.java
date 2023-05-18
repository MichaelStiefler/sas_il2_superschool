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

public abstract class F6F extends Scheme1 implements TypeFighter, TypeTNBFighter {

    public F6F() {
        this.arrestor2 = 0.0F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        xyz[1] = cvt(f, 0.01F, 0.99F, 0.0F, 0.625F);
        this.hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 54F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 97.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -90F * f, 0.0F);
        xyz[0] = xyz[1] = xyz[2] = ypr[0] = ypr[1] = ypr[2] = 0.0F;
        xyz[1] = -0.303F * f;
        hiermesh.chunkSetLocate("GearL11_D0", xyz, ypr);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, -30F * f);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 0.0F, 120F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 97.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 90F * f, 0.0F);
        xyz[0] = xyz[1] = xyz[2] = ypr[0] = ypr[1] = ypr[2] = 0.0F;
        xyz[1] = -0.303F * f;
        hiermesh.chunkSetLocate("GearR11_D0", xyz, ypr);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, -30F * f);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, 0.0F, 120F * f);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        float f = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.3575F, 0.0F, 0.3575F);
        xyz[1] = f;
        this.hierMesh().chunkSetLocate("GearL99_D0", xyz, ypr);
        f = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.3575F, 0.0F, -40F);
        this.hierMesh().chunkSetAngles("GearL77_D0", 0.0F, f, 0.0F);
        f = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.3575F, 0.0F, -80F);
        this.hierMesh().chunkSetAngles("GearL88_D0", 0.0F, f, 0.0F);
        f = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.3575F, 0.0F, 0.3575F);
        xyz[1] = f;
        this.hierMesh().chunkSetLocate("GearR99_D0", xyz, ypr);
        f = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.3575F, 0.0F, -40F);
        this.hierMesh().chunkSetAngles("GearR77_D0", 0.0F, f, 0.0F);
        f = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.3575F, 0.0F, -80F);
        this.hierMesh().chunkSetAngles("GearR88_D0", 0.0F, f, 0.0F);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, f, 0.0F);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 10.5F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, 0.0F, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.CT.getArrestor() > 0.9F) if (this.FM.Gears.arrestorVAngle != 0.0F) {
            this.arrestor2 = cvt(this.FM.Gears.arrestorVAngle, -65F, 3F, 45F, -23F);
            this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, this.arrestor2, 0.0F);
            if (this.FM.Gears.arrestorVAngle >= -35F);
        } else {
            float f1 = -41F * this.FM.Gears.arrestorVSink;
            if (f1 < 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if (f1 > 0.0F && this.FM.CT.getArrestor() < 0.9F) f1 = 0.0F;
            if (f1 > 6.2F) f1 = 6.2F;
            this.arrestor2 += f1;
            if (this.arrestor2 < -23F) this.arrestor2 = -23F;
            else if (this.arrestor2 > 45F) this.arrestor2 = 45F;
            this.hierMesh().chunkSetAngles("Hook2_D0", 0.0F, this.arrestor2, 0.0F);
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

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("f1")) this.getEnergyPastArmor(World.Rnd().nextFloat(8F, 12F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(16F, 36F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) this.doRicochetBack(shot);
                } else if (s.endsWith("p2")) this.getEnergyPastArmor(11D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("p3")) this.getEnergyPastArmor(11.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if (s.startsWith("xxcmglammo") && World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                int i = 0 + 2 * World.Rnd().nextInt(0, 2);
                this.FM.AS.setJamBullets(0, i);
            }
            if (s.startsWith("xxcmgrammo") && World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                int j = 1 + 2 * World.Rnd().nextInt(0, 2);
                this.FM.AS.setJamBullets(0, j);
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int k = s.charAt(10) - 48;
                switch (k) {
                    default:
                        break;

                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Ailerones Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 7:
                    case 8:
                        if (World.Rnd().nextFloat() < 0.08F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        break;

                    case 9:
                        if (World.Rnd().nextFloat() < 0.95F && this.getEnergyPastArmor(1.27F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;
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
                if (s.endsWith("eqpt")) {
                    if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                        this.debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                }
                if (s.startsWith("xxeng1mag")) {
                    int l = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + l + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, l);
                }
                if (s.endsWith("oil1")) {
                    if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                if (s.endsWith("prop") && this.getEnergyPastArmor(0.42F, shot) > 0.0F) this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if (s.startsWith("xxeng1typ") && this.getEnergyPastArmor(0.42F, shot) > 0.0F) this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                return;
            }
            if (s.startsWith("xxhyd")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F) {
                    this.debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
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
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxmgun0")) {
                int i1 = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + i1 + ") Disabled..");
                    this.FM.AS.setJamBullets(0, i1);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(14.8F * World.Rnd().nextFloat(0.99F, 1.8F), shot) > 0.0F) {
                    debugprintln(this, "*** Tail1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxsupc")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F) {
                    this.debuggunnery("Engine Module: Turbine Disabled..");
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j1 = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[j1] == 0) {
                        this.debuggunnery("Fuel Tank (" + j1 + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j1, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j1, 1);
                    }
                    if (World.Rnd().nextFloat() < 0.07F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F) {
                        this.FM.AS.hitTank(shot.initiator, j1, 2);
                        this.debuggunnery("Fuel Tank (" + j1 + "): Hit..");
                    }
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf") || s.startsWith("xblister")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if ((s.startsWith("xcf2") || s.startsWith("xblister")) && World.Rnd().nextFloat() < 0.25F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            if (s.startsWith("xcf3") && point3d.x > -0.645D && point3d.x < 0.406D && point3d.z > 0.3D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            return;
        }
        if (s.startsWith("xeng")) {
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
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k1;
            if (s.endsWith("a")) {
                byte0 = 1;
                k1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k1 = s.charAt(6) - 49;
            } else k1 = s.charAt(5) - 49;
            this.hitFlesh(k1, shot, byte0);
        }
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLMid_D0", 100F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", -100F * f, 0.0F, 0.0F);
    }

    public void moveWingFold(float f) {
        if (f < 0.001F) {
            this.setGunPodsOn(true);
            this.hideWingWeapons(false);
        } else {
            this.setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            this.hideWingWeapons(true);
        }
        this.moveWingFold(this.hierMesh(), f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    public static boolean bChangedPit = false;
    private float         arrestor2;

    static {
        Class class1 = F6F.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
