/*Here only for obfuscation reasons.*/
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class B_24 extends Scheme4 implements TypeTransport {
    private float   fCSink;
    private float   fCSteer;
    private float[] flapps = { 0.0F, 0.0F, 0.0F, 0.0F };
    private float   fGunPos;
    private int     iGunPos;
    private long    btme;

    public B_24() {
        this.fCSink = 0.0F;
        this.fCSteer = 0.0F;
        this.fGunPos = 1.0F;
        this.iGunPos = 1;
        this.btme = -1L;
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30.0F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30.0F * f, 0.0F);
        this.fCSteer = 22.2F * f;
    }

    protected void moveFlap(float f) {
        float f_0_ = -35.5F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f_0_, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f_0_, 0.0F);
    }

    protected void hitBone(String string, Shot shot, Point3d point3d) {
        int i = 0;
        if (string.startsWith("xx")) {
            if (string.startsWith("xxammo")) {
                int i_1_ = string.charAt(6) - 48;
                if (string.length() > 7) i_1_ = 10;
                if (this.getEnergyPastArmor(6.87F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F) {
                    switch (i_1_) {
                        case 1:
                            i_1_ = 10;
                            i = 0;
                            break;
                        case 2:
                            i_1_ = 10;
                            i = 1;
                            break;
                        case 3:
                            i_1_ = 11;
                            i = 0;
                            break;
                        case 4:
                            i_1_ = 11;
                            i = 1;
                            break;
                        case 5:
                            i_1_ = 12;
                            i = 0;
                            break;
                        case 6:
                            i_1_ = 12;
                            i = 1;
                            break;
                        case 7:
                            i_1_ = 13;
                            i = 0;
                            break;
                        case 8:
                            i_1_ = 14;
                            i = 0;
                            break;
                        case 9:
                            i_1_ = 15;
                            i = 0;
                            break;
                        case 10:
                            i_1_ = 15;
                            i = 1;
                            break;
                    }
                    this.FM.AS.setJamBullets(i_1_, i);
                    return;
                }
            }
            if (string.startsWith("xxcontrols")) {
                int i_2_ = string.charAt(10) - 48;
                switch (i_2_) {
                    default:
                        break;
                    case 1:
                    case 2:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                        }
                        break;
                    case 3:
                        if (World.Rnd().nextFloat() < 0.125F && this.getEnergyPastArmor(5.2F, shot) > 0.0F) {
                            Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        this.getEnergyPastArmor(2.0F, shot);
                        break;
                    case 4:
                    case 5:
                    case 6:
                        if (World.Rnd().nextFloat() < 0.252F && this.getEnergyPastArmor(5.2F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.125F) this.FM.AS.setControlsDamage(shot.initiator, 2);
                            if (World.Rnd().nextFloat() < 0.125F) this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        this.getEnergyPastArmor(2.0F, shot);
                }
            } else if (string.startsWith("xxeng")) {
                int i_3_ = string.charAt(5) - 49;
                if (string.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 140000.0F) {
                            this.FM.AS.setEngineStuck(shot.initiator, i_3_);
                            Aircraft.debugprintln(this, "*** Engine (" + i_3_ + ") Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 85000.0F) {
                            this.FM.AS.hitEngine(shot.initiator, i_3_, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + i_3_ + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.0050F) this.FM.EI.engines[i_3_].setCyliderKnockOut(shot.initiator, 1);
                    else {
                        this.FM.EI.engines[i_3_].setReadyness(shot.initiator, this.FM.EI.engines[i_3_].getReadyness() - 8.2E-4F);
                        Aircraft.debugprintln(this, "*** Engine (" + i_3_ + ") Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[i_3_].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12.0F, shot);
                }
                if (string.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i_3_].getCylindersRatio() * 0.75F) {
                        this.FM.EI.engines[i_3_].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000.0F)));
                        Aircraft.debugprintln(this, "*** Engine (" + i_3_ + ") Cylinders Hit, " + this.FM.EI.engines[i_3_].getCylindersOperable() + "/" + this.FM.EI.engines[i_3_].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 18000.0F) {
                            this.FM.AS.hitEngine(shot.initiator, i_3_, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + i_3_ + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25.0F, shot);
                }
                if (string.endsWith("mag1")) {
                    this.FM.EI.engines[i_3_].setMagnetoKnockOut(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine (" + i_3_ + ") Module: Magneto #0 Destroyed..");
                    this.getEnergyPastArmor(25.0F, shot);
                }
                if (string.endsWith("mag2")) {
                    this.FM.EI.engines[i_3_].setMagnetoKnockOut(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + i_3_ + ") Module: Magneto #1 Destroyed..");
                    this.getEnergyPastArmor(25.0F, shot);
                }
                if (string.endsWith("oil1") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.setOilState(shot.initiator, i_3_, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + i_3_ + ") Module: Oil Filter Pierced..");
                }
            } else if (string.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (string.startsWith("xxlockr1") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (string.startsWith("xxlockr2") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if (string.startsWith("xxlockvl") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (string.startsWith("xxlockvr") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (string.startsWith("xxlockal") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (string.startsWith("xxlockar") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            } else if (string.startsWith("xxoil")) {
                int i_4_ = string.charAt(5) - 49;
                if (this.getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F) this.FM.AS.hitOil(shot.initiator, i_4_);
                Aircraft.debugprintln(this, "*** Engine (" + i_4_ + ") Module: Oil Tank Pierced..");
            } else if (string.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F) {
                    this.debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
            } else if (string.startsWith("xxradio")) this.getEnergyPastArmor(World.Rnd().nextFloat(5.0F, 25.0F), shot);
            else if (string.startsWith("xxspar")) {
                if (string.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (string.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (string.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (string.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (string.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (string.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (string.startsWith("xxspark1") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                if (string.startsWith("xxspark2") && this.chunkDamageVisible("Keel2") > 1 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Keel2 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel2_D" + this.chunkDamageVisible("Keel2"), shot.initiator);
                }
                if (string.startsWith("xxsparsl") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (string.startsWith("xxsparsr") && this.chunkDamageVisible("StabR") > 1 && this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
            } else if (string.startsWith("xxtank")) {
                int i_5_ = string.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.06F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[i_5_] == 0) {
                        this.FM.AS.hitTank(shot.initiator, i_5_, 1);
                        this.FM.AS.doSetTankState(shot.initiator, i_5_, 1);
                    }
                    if (shot.powerType == 3) {
                        if (shot.power < 16100.0F) {
                            if (this.FM.AS.astateTankStates[i_5_] < 4 && World.Rnd().nextFloat() < 0.21F) this.FM.AS.hitTank(shot.initiator, i_5_, 1);
                        } else this.FM.AS.hitTank(shot.initiator, i_5_, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100.0F)));
                    } else if (shot.power > 16100.0F) this.FM.AS.hitTank(shot.initiator, i_5_, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100.0F)));
                }
            }
        } else {
            if (string.startsWith("xcf")) {
                if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
                if (World.Rnd().nextFloat() < 0.0575F) if (point3d.y > 0.0) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x8);
                else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                if (point3d.x > 1.726) {
                    if (point3d.z > 0.444) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x1);
                    if (point3d.z > -0.281 && point3d.z < 0.444) if (point3d.y > 0.0) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x4);
                    else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                    if (point3d.x > 2.774 && point3d.x < 3.718 && point3d.z > 0.425) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x2);
                    if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
            } else if (string.startsWith("xtail")) {
                if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
                return;
            }
            if (string.startsWith("xkeel1")) {
                if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
            } else if (string.startsWith("xkeel2")) {
                if (this.chunkDamageVisible("Keel2") < 2) this.hitChunk("Keel2", shot);
            } else if (string.startsWith("xrudder1")) {
                if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
            } else if (string.startsWith("xrudder2")) {
                if (this.chunkDamageVisible("Rudder2") < 1) this.hitChunk("Rudder2", shot);
            } else if (string.startsWith("xstabl")) {
                if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
            } else if (string.startsWith("xstabr")) {
                if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
            } else if (string.startsWith("xwinglin")) {
                if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            } else if (string.startsWith("xwingrin")) {
                if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            } else if (string.startsWith("xwinglmid")) {
                if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            } else if (string.startsWith("xwingrmid")) {
                if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            } else if (string.startsWith("xwinglout")) {
                if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            } else if (string.startsWith("xwingrout")) {
                if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
            } else if (string.startsWith("xaronel")) {
                if (this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneL", shot);
            } else if (string.startsWith("xaroner")) {
                if (this.chunkDamageVisible("AroneR") < 1) this.hitChunk("AroneR", shot);
            } else if (string.startsWith("xengine1")) {
                if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
            } else if (string.startsWith("xengine2")) {
                if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
            } else if (string.startsWith("xengine3")) {
                if (this.chunkDamageVisible("Engine3") < 2) this.hitChunk("Engine3", shot);
            } else if (string.startsWith("xengine4")) {
                if (this.chunkDamageVisible("Engine4") < 2) this.hitChunk("Engine4", shot);
            } else if (string.startsWith("xgear")) {
                if (World.Rnd().nextFloat() < 0.05F) {
                    Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                    this.FM.Gears.setHydroOperable(false);
                }
            } else if (!string.startsWith("xturret")) if (string.startsWith("xmgun")) {
                int i_6_ = 10 * (string.charAt(5) - 48) + string.charAt(6) - 48;
                if (this.getEnergyPastArmor(6.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F) {
                    switch (i_6_) {
                        case 1:
                            i_6_ = 10;
                            i = 0;
                            break;
                        case 2:
                            i_6_ = 10;
                            i = 1;
                            break;
                        case 3:
                            i_6_ = 11;
                            i = 0;
                            break;
                        case 4:
                            i_6_ = 11;
                            i = 1;
                            break;
                        case 5:
                            i_6_ = 12;
                            i = 0;
                            break;
                        case 6:
                            i_6_ = 12;
                            i = 1;
                            break;
                        case 7:
                            i_6_ = 13;
                            i = 0;
                            break;
                        case 8:
                            i_6_ = 14;
                            i = 0;
                            break;
                        case 9:
                            i_6_ = 15;
                            i = 0;
                            break;
                        case 10:
                            i_6_ = 15;
                            i = 1;
                            break;
                    }
                    this.FM.AS.setJamBullets(i_6_, i);
                }
            } else if (string.startsWith("xpilot") || string.startsWith("xhead")) {
                int i_7_ = 0;
                int i_8_;
                if (string.endsWith("a")) {
                    i_7_ = 1;
                    i_8_ = string.charAt(6) - 49;
                } else if (string.endsWith("b")) {
                    i_7_ = 2;
                    i_8_ = string.charAt(6) - 49;
                } else i_8_ = string.charAt(5) - 49;
                this.hitFlesh(i_8_, shot, i_7_);
            }
        }
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if (explosion.chunkName == null || !(explosion.power > 0.0F) || !explosion.chunkName.equals("Tail1_D3") && !explosion.chunkName.equals("WingLIn_D3") && !explosion.chunkName.equals("WingRIn_D3") && !explosion.chunkName.equals("WingLMid_D3")
                && !explosion.chunkName.equals("WingRMid_D3") && !explosion.chunkName.equals("WingLOut_D3") && !explosion.chunkName.equals("WingROut_D3"))
            super.msgExplosion(explosion);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -55.0F), 0.0F);
        hiermesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -60.0F), 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -140.0F), 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -140.0F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -95.0F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -30.0F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, 180.0F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -98.0F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -20.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -95.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -30.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, 180.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -98.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -20.0F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        this.fCSink = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.5F, 0.0F, 0.5F);
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.456F, 0.0F, 0.2821F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.456F, 0.0F, 0.2821F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected boolean cutFM(int i, int i_9_, Actor actor) {
        switch (i) {
            case 33:
                this.hitProp(1, i_9_, actor);
                this.FM.EI.engines[1].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 9));
                /* fall through */
            case 34:
                this.hitProp(0, i_9_, actor);
                this.FM.EI.engines[0].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(2, 8));
                this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 5));
                /* fall through */
            case 35:
                this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(0, 4));
                break;
            case 36:
                this.hitProp(2, i_9_, actor);
                this.FM.EI.engines[2].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 9));
                /* fall through */
            case 37:
                this.hitProp(3, i_9_, actor);
                this.FM.EI.engines[3].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 5));
                this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(2, 8));
                /* fall through */
            case 38:
                this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(0, 4));
                break;
            case 25:
                this.FM.turret[0].bIsOperable = false;
                return false;
            case 26:
                this.FM.turret[1].bIsOperable = false;
                return false;
            case 27:
                this.FM.turret[2].bIsOperable = false;
                return false;
            case 28:
                this.FM.turret[3].bIsOperable = false;
                return false;
            case 29:
                this.FM.turret[4].bIsOperable = false;
                return false;
            case 30:
                this.FM.turret[5].bIsOperable = false;
                return false;
            case 19:
                this.killPilot(this, 5);
                this.killPilot(this, 6);
                this.killPilot(this, 7);
                this.killPilot(this, 8);
                this.cut("StabL");
                this.cut("StabR");
                break;
            case 13:
                this.killPilot(this, 0);
                this.killPilot(this, 1);
                this.killPilot(this, 2);
                this.killPilot(this, 3);
                break;
            case 17:
                this.cut("Keel1");
                this.hierMesh().chunkVisible("Keel1_CAP", false);
                break;
            case 18:
                this.cut("Keel2");
                this.hierMesh().chunkVisible("Keel2_CAP", false);
                break;
        }
        return super.cutFM(i, i_9_, actor);
    }

    protected void moveBayDoor(float f) {
        for (int i = 1; i < 10; i++)
            this.hierMesh().chunkSetAngles("Bay0" + i + "_D0", 0.0F, -30.0F * f, 0.0F);
        for (int i = 10; i < 13; i++)
            this.hierMesh().chunkSetAngles("Bay" + i + "_D0", 0.0F, -30.0F * f, 0.0F);
    }

    public void rareAction(float f, boolean bool) {
        super.rareAction(f, bool);
        if (bool) {
            if (this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            if (this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.04F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
            if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) for (int i = 0; i < this.FM.EI.getNum(); i++)
                if (this.FM.AS.astateEngineStates[i] > 3 && World.Rnd().nextFloat() < 0.2F) this.FM.EI.engines[i].setExtinguisherFire();
        }
        for (int i = 1; i < 10; i++)
            if (this.FM.getAltitude() < 3000.0F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2:
                this.FM.turret[0].setHealth(f);
                break;
            case 4:
                this.FM.turret[1].setHealth(f);
                break;
            case 5:
                this.FM.turret[2].setHealth(f);
                break;
            case 6:
                this.FM.turret[3].setHealth(f);
                break;
            case 7:
                this.FM.turret[4].setHealth(f);
                break;
            case 8:
                this.FM.turret[5].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
        this.hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 4; i++) {
            float f_10_ = this.FM.EI.engines[i].getControlRadiator();
            if (!(Math.abs(this.flapps[i] - f_10_) <= 0.01F)) {
                this.flapps[i] = f_10_;
                for (int i_11_ = 1; i_11_ < 9; i_11_++)
                    this.hierMesh().chunkSetAngles("Water" + (i * 8 + i_11_) + "_D0", 0.0F, -20.0F * f_10_, 0.0F);
            }
        }
        if (this.FM.CT.getGear() > 0.9F) {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = this.fCSink;
            Aircraft.ypr[1] = this.fCSteer;
            this.hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
        }
        do
            if (this.iGunPos == 0) {
                if (this.fGunPos > 0.0F) {
                    this.fGunPos -= 0.2F * f;
                    this.resetYPRmodifier();
                    Aircraft.xyz[1] = -0.635F + 0.635F * this.fGunPos;
                    this.hierMesh().chunkSetLocate("Turret3C_D0", Aircraft.xyz, Aircraft.ypr);
                }
            } else if (this.iGunPos == 1 && this.fGunPos < 1.0F) {
                this.fGunPos += 0.2F * f;
                this.resetYPRmodifier();
                Aircraft.xyz[1] = -0.635F + 0.635F * this.fGunPos;
                this.hierMesh().chunkSetLocate("Turret3C_D0", Aircraft.xyz, Aircraft.ypr);
                if (!(this.fGunPos > 0.8F) || this.fGunPos >= 0.9F) break;
            }
        while (false);
        if (this.FM.AS.isMaster()) {
            if (this.FM.turret[2].target != null && this.FM.AS.astatePilotStates[5] < 90) this.iGunPos = 1;
            if (Time.current() > this.btme) {
                this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if (this.FM.turret[2].target == null) this.iGunPos = 0;
            }
        }
    }

    static {
        Property.set(B_24.class, "originCountry", PaintScheme.countryUSA);
    }
}
