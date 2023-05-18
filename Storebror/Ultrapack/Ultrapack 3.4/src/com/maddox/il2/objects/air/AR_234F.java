package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;

public abstract class AR_234F extends Scheme2 implements TypeFighter, TypeBNZFighter {

    public AR_234F() {
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

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.65F, 0.0F, -92F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.65F, 0.0F, -62.5F), 0.0F);
        if (f < 0.525F) {
            hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.525F, 0.0F, -46F), 0.0F);
            hiermesh.chunkSetAngles("GearC7_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.525F, 0.0F, -0.25F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f, 0.525F, 0.65F, -46F, -73.5F), 0.0F);
            hiermesh.chunkSetAngles("GearC7_D0", 0.0F, Aircraft.cvt(f, 0.525F, 0.65F, -0.25F, -7.5F), 0.0F);
        }
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 0.65F, 0.0F, -0.2935F);
        hiermesh.chunkSetLocate("GearC8_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f, 0.07F, 0.32F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(f, 0.07F, 0.32F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.3F, 0.8F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, floatindex(Aircraft.cvt(f, 0.3F, 0.8F, 0.0F, 3F), gear6), 0.0F);
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.xyz[1] = floatindex(Aircraft.cvt(f, 0.3F, 0.8F, 0.0F, 3F), gear7);
        hiermesh.chunkSetLocate("GearL7_D0", Aircraft.xyz, Aircraft.ypr);
        if (f < 0.6F) {
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, Aircraft.cvt(f, 0.3F, 0.6F, 0.0F, -76.5F), 0.0F);
            hiermesh.chunkSetAngles("GearL9_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.3F, 0.0F, -44F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, Aircraft.cvt(f, 0.6F, 0.8F, -76.5F, -62F), 0.0F);
            hiermesh.chunkSetAngles("GearL9_D0", 0.0F, Aircraft.cvt(f, 0.6F, 0.8F, -44F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.3F, 0.8F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, floatindex(Aircraft.cvt(f, 0.3F, 0.8F, 0.0F, 3F), gear6), 0.0F);
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.xyz[1] = floatindex(Aircraft.cvt(f, 0.3F, 0.8F, 0.0F, 3F), gear7);
        hiermesh.chunkSetLocate("GearR7_D0", Aircraft.xyz, Aircraft.ypr);
        if (f < 0.6F) {
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(f, 0.3F, 0.6F, 0.0F, -76.5F), 0.0F);
            hiermesh.chunkSetAngles("GearR9_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.3F, 0.0F, -44F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(f, 0.6F, 0.8F, -76.5F, -62F), 0.0F);
            hiermesh.chunkSetAngles("GearR9_D0", 0.0F, Aircraft.cvt(f, 0.6F, 0.8F, -44F, 0.0F), 0.0F);
        }
    }

    protected void moveGear(float f) {
        AR_234.moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -0.3274F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -46F), 0.0F);
        this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -46F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, -0.3274F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, -46F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, -46F), 0.0F);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() > 0.8F) this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.startsWith("xxarmorp")) this.getEnergyPastArmor(15.15D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-006D), shot);
                return;
            }
            if (s.startsWith("xxcannon")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Armament System: Left Rear Cannon: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Armament System: Right Rear Cannon: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                if (s.length() == 12) i = 10 + s.charAt(11) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                            Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 2:
                        if (this.getEnergyPastArmor(0.1F, shot) <= 0.0F) break;
                        Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                        if (World.Rnd().nextFloat() < 0.5F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        else this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                        break;

                    case 3:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        break;

                    case 7:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        break;

                    case 4:
                    case 6:
                    case 8:
                    case 10:
                        if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                            Aircraft.debugprintln(this, "*** Aileron Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 5:
                    case 9:
                        if (this.getEnergyPastArmor(4.1F, shot) > 0.0F) {
                            Aircraft.debugprintln(this, "*** Aileron Controls Crank: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 11:
                    case 12:
                        if (this.getEnergyPastArmor(0.3F, shot) > 0.0F) {
                            Aircraft.debugprintln(this, "*** Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxEng")) {
                int j = s.charAt(5) - 49;
                if (point3d.x > 0.0D) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** Engine Module(s): Supercharger Disabled..");
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                    }
                } else {
                    if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                    if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass && this.FM.EI.engines[j].getStage() == 6) this.FM.AS.hitEngine(shot.initiator, j, 1);
                    this.getEnergyPastArmor(14.296F, shot);
                }
                return;
            }
            if (s.startsWith("xxLock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxLockR") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxLockVL") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxLockVR") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxLockAL") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxLockAR") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if (s.startsWith("xxSpar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxSparLI") && this.chunkDamageVisible("WingLIn") > 2 && World.Rnd().nextFloat() > Math.abs(Aircraft.v1.x) + 0.12D && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxSparrI") && this.chunkDamageVisible("WingRIn") > 2 && World.Rnd().nextFloat() > Math.abs(Aircraft.v1.x) + 0.12D && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxSparlm") && this.chunkDamageVisible("WingLMid") > 2 && World.Rnd().nextFloat() > Math.abs(Aircraft.v1.x) + 0.12D && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxSparrm") && this.chunkDamageVisible("WingRMid") > 2 && World.Rnd().nextFloat() > Math.abs(Aircraft.v1.x) + 0.12D && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxSparlo") && this.chunkDamageVisible("WingLOut") > 2 && World.Rnd().nextFloat() > Math.abs(Aircraft.v1.x) + 0.12D && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxSparro") && this.chunkDamageVisible("WingROut") > 2 && World.Rnd().nextFloat() > Math.abs(Aircraft.v1.x) + 0.12D && this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxSpart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.86F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if (s.startsWith("xxTank")) {
                int k = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F && shot.powerType == 3) this.FM.AS.hitTank(shot.initiator, k, 2);
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (point3d.x > 0.5D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            else if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            if (World.Rnd().nextFloat() < 0.05F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) this.hitChunk("Keel1", shot);
        else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
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
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
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
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && World.Rnd().nextFloat() < 0.04F) {
            if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.12F) {
                this.FM.AS.explodeEngine(this, 0);
                this.msgCollision(this, "WingLIn_D0", "WingLIn_D0");
                if (World.Rnd().nextBoolean()) this.FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
                else this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
            }
            if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.12F) {
                this.FM.AS.explodeEngine(this, 1);
                this.msgCollision(this, "WingRIn_D0", "WingRIn_D0");
                if (World.Rnd().nextBoolean()) this.FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
                else this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
            }
        }
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (!Actor.isValid(aircraft)) return;
                else {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(2, loc, vector3d, aircraft);
                    return;
                }
            }

        };
        this.hierMesh().chunkVisible("Seat_D0", false);
    }

    public void update(float f) {
        if (this.FM.AS.isMaster()) {
            for (int i = 0; i < 2; i++)
                if (this.curctl[i] == -1F) this.curctl[i] = this.oldctl[i] = this.FM.EI.engines[i].getControlThrottle();
                else {
                    this.curctl[i] = this.FM.EI.engines[i].getControlThrottle();
                    if ((this.curctl[i] - this.oldctl[i]) / f > 3F && this.FM.EI.engines[i].getRPM() < 2400F && this.FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.25F) this.FM.AS.hitEngine(this, i, 100);
                    if ((this.curctl[i] - this.oldctl[i]) / f < -3F && this.FM.EI.engines[i].getRPM() < 2400F && this.FM.EI.engines[i].getStage() == 6) {
                        if (World.Rnd().nextFloat() < 0.25F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) this.FM.EI.engines[i].setEngineStops(this);
                        if (World.Rnd().nextFloat() < 0.75F && this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) this.FM.EI.engines[i].setKillCompressor(this);
                    }
                    this.oldctl[i] = this.curctl[i];
                }

            if (Config.isUSE_RENDER()) {
                if (this.FM.EI.engines[0].getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6) {
                    if (this.FM.EI.engines[0].getPowerOutput() > 0.95F) this.FM.AS.setSootState(this, 0, 3);
                    else this.FM.AS.setSootState(this, 0, 2);
                } else this.FM.AS.setSootState(this, 0, 0);
                if (this.FM.EI.engines[1].getPowerOutput() > 0.8F && this.FM.EI.engines[1].getStage() == 6) {
                    if (this.FM.EI.engines[1].getPowerOutput() > 0.95F) this.FM.AS.setSootState(this, 1, 3);
                    else this.FM.AS.setSootState(this, 1, 2);
                } else this.FM.AS.setSootState(this, 1, 0);
            }
        }
        super.update(f);
    }

    private float              oldctl[] = { -1F, -1F };
    private float              curctl[] = { -1F, -1F };
    private static final float gear6[]  = { 0.0F, -3F, -3.5F, -1F, 7F };
    private static final float gear7[]  = { 0.0F, -0.09835F, -0.21265F, -0.3185F, -0.3917F };

    static {
        Class class1 = AR_234F.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
