package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class FW_200 extends Scheme4 implements TypeBomber, TypeTransport {

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap05_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap06_D0", 0.0F, f1, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        int i = 0;
        if (s.startsWith("xx")) {
            if (s.startsWith("xxbomb")) {
                if (World.Rnd().nextFloat() < 0.001F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
                    Aircraft.debugprintln(this, "*** Bomb Payload Detonates..");
                    this.FM.AS.hitTank(shot.initiator, 0, 10);
                    this.FM.AS.hitTank(shot.initiator, 1, 10);
                    this.FM.AS.hitTank(shot.initiator, 2, 10);
                    this.FM.AS.hitTank(shot.initiator, 3, 10);
                    this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 2:
                    case 3:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) this.FM.AS.setControlsDamage(shot.initiator, 0);
                        break;

                    case 4:
                    case 5:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) this.FM.AS.setControlsDamage(shot.initiator, 1);
                        break;

                    case 6:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) this.FM.AS.setControlsDamage(shot.initiator, 2);
                        break;
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspark") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                if (s.startsWith("xxsparn") && this.chunkDamageVisible("Nose") > 1 && this.getEnergyPastArmor(37.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Nose Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Nose_D" + this.chunkDamageVisible("Nose"), shot.initiator);
                }
                if (s.startsWith("xxsparsl") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && this.getEnergyPastArmor(16.9F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabL Spar Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D" + this.chunkDamageVisible("StabL"), shot.initiator);
                }
                if (s.startsWith("xxsparsr") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && this.getEnergyPastArmor(16.9F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabR Spar Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D" + this.chunkDamageVisible("StabR"), shot.initiator);
                }
                if (s.startsWith("xxspart") && this.getEnergyPastArmor(46.6F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Tail Spar Damaged..");
                    this.nextDMGLevels(1, 2, "Tail1_D" + this.chunkDamageVisible("Tail1"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                i = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 140000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, i);
                            Aircraft.debugprintln(this, "*** Engine (" + i + ") Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 85000F) {
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + i + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.005F) this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    else {
                        this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.00082F);
                        Aircraft.debugprintln(this, "*** Engine (" + i + ") Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[i].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 0.75F) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine (" + i + ") Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 48000F) {
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + i + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("prop") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    if (World.Rnd().nextFloat() < 0.5F) this.FM.EI.engines[i].setKillPropAngleDevice(shot.initiator);
                    else this.FM.EI.engines[i].setKillPropAngleDeviceSpeeds(shot.initiator);
                    this.getEnergyPastArmor(15.1F, shot);
                    Aircraft.debugprintln(this, "*** Engine (" + i + ") Module: Prop Governor Fails..");
                }
                if (s.endsWith("oil1") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.setOilState(shot.initiator, i, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + i + ") Module: Oil Filter Pierced..");
                }
                if (s.endsWith("supc") && this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine (" + i + ") Module: Compressor Stops..");
                    this.getEnergyPastArmor(2.6F, shot);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 48;
                if (s.length() > 7) j = 10 + s.charAt(7) - 48;
                switch (j) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        i = World.Rnd().nextInt(1, 2);
                        break;

                    case 6:
                        i = 1;
                        break;

                    case 7:
                    case 8:
                    case 9:
                        i = 0;
                        break;

                    case 10:
                        i = 2;
                        break;

                    case 11:
                    case 12:
                    case 13:
                        i = 3;
                        break;
                }
                if (this.getEnergyPastArmor(0.03F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[i] == 0) {
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                        this.FM.AS.doSetTankState(shot.initiator, i, 2);
                    }
                    if (shot.powerType == 3) if (shot.power < 14100F) {
                        if (this.FM.AS.astateTankStates[i] < 4 && World.Rnd().nextFloat() < 0.125F) this.FM.AS.hitTank(shot.initiator, i, 1);
                    } else this.FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int) (shot.power / 28200F)));
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (World.Rnd().nextFloat() < 0.0575F) if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
            else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            if (point3d.x > 1.726D) {
                if (point3d.z > 0.444D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                if (point3d.z > -0.281D && point3d.z < 0.444D) if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if (point3d.x > 2.774D && point3d.x < 3.718D && point3d.z > 0.425D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
            return;
        }
        if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
            return;
        }
        if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
            return;
        }
        if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
            return;
        }
        if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
            return;
        }
        if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
            return;
        }
        if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
            return;
        }
        if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            return;
        }
        if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            return;
        }
        if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            return;
        }
        if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            return;
        }
        if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            return;
        }
        if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
            return;
        }
        if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
            return;
        }
        if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
            return;
        }
        if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
            return;
        }
        if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
            return;
        }
        if (s.startsWith("xengine3")) {
            if (this.chunkDamageVisible("Engine3") < 2) this.hitChunk("Engine3", shot);
            return;
        }
        if (s.startsWith("xengine4")) {
            if (this.chunkDamageVisible("Engine4") < 2) this.hitChunk("Engine4", shot);
            return;
        }
        if (s.startsWith("xgear")) {
            if (World.Rnd().nextFloat() < 0.05F) {
                Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
            return;
        }
        if (s.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose") < 2) this.hitChunk("Nose", shot);
            return;
        }
        if (s.startsWith("xoil")) {
            int k = s.charAt(4) - 49;
            if (this.getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                this.FM.AS.setOilState(shot.initiator, k, 1);
                Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Oil Filter Pierced (E)..");
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
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
            return;
        } else return;
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if (explosion.chunkName != null && explosion.power > 0.0F) {
            if (explosion.chunkName.equals("Tail1_D3")) return;
            if (explosion.chunkName.equals("WingLIn_D3")) return;
            if (explosion.chunkName.equals("WingRIn_D3")) return;
            if (explosion.chunkName.equals("WingLMid_D3")) return;
            if (explosion.chunkName.equals("WingRMid_D3")) return;
            if (explosion.chunkName.equals("WingLOut_D3")) return;
            if (explosion.chunkName.equals("WingROut_D3")) return;
        }
        super.msgExplosion(explosion);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -43F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -61F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 166F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, -floatindex(f * 10F, anglesL6));
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 0.0F, -floatindex(f * 10F, anglesL7));
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.07F, 0.0F, -80F));
        hiermesh.chunkSetAngles("GearL9_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.07F, 0.0F, 80F));
        hiermesh.chunkSetAngles("GearL10_D0", 0.0F, 0.0F, f < 0.5F ? Aircraft.cvt(f, 0.01F, 0.07F, 0.0F, -80F) : Aircraft.cvt(f, 0.93F, 0.99F, 80F, 0.0F));
        hiermesh.chunkSetAngles("GearL11_D0", 0.0F, 0.0F, f < 0.5F ? Aircraft.cvt(f, 0.01F, 0.07F, 0.0F, 80F) : Aircraft.cvt(f, 0.93F, 0.99F, -80F, 0.0F));
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -43F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -61F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 166F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, -floatindex(f * 10F, anglesL6));
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, 0.0F, -floatindex(f * 10F, anglesL7));
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.07F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearR9_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.07F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearR10_D0", 0.0F, f < 0.5F ? Aircraft.cvt(f, 0.01F, 0.07F, 0.0F, 80F) : Aircraft.cvt(f, 0.93F, 0.99F, 80F, 0.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR11_D0", 0.0F, f < 0.5F ? Aircraft.cvt(f, 0.01F, 0.07F, 0.0F, -80F) : Aircraft.cvt(f, 0.93F, 0.99F, -80F, 0.0F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.4615F, 0.0F, -26.5F);
        this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, f, 0.0F);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.4615F, 0.0F, 21.5F);
        this.hierMesh().chunkSetAngles("GearL12_D0", 0.0F, f, 0.0F);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.4615F, 0.0F, 3.5F);
        this.hierMesh().chunkSetAngles("GearL13_D0", 0.0F, f, 0.0F);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.4615F, 0.0F, -26.5F);
        this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, f, 0.0F);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.4615F, 0.0F, 21.5F);
        this.hierMesh().chunkSetAngles("GearR12_D0", 0.0F, f, 0.0F);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.4615F, 0.0F, 3.5F);
        this.hierMesh().chunkSetAngles("GearR13_D0", 0.0F, f, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                this.hitProp(1, j, actor);
                this.FM.EI.engines[1].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 9));
                // fall through

            case 34:
                this.hitProp(0, j, actor);
                this.FM.EI.engines[0].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(2, 8));
                this.FM.AS.hitTank(actor, 1, World.Rnd().nextInt(0, 5));
                // fall through

            case 35:
                this.FM.AS.hitTank(actor, 0, World.Rnd().nextInt(0, 4));
                break;

            case 36:
                this.hitProp(2, j, actor);
                this.FM.EI.engines[2].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 9));
                // fall through

            case 37:
                this.hitProp(3, j, actor);
                this.FM.EI.engines[3].setEngineStuck(actor);
                this.FM.AS.hitTank(actor, 2, World.Rnd().nextInt(0, 5));
                this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(2, 8));
                // fall through

            case 38:
                this.FM.AS.hitTank(actor, 3, World.Rnd().nextInt(0, 4));
                break;

            case 13:
                this.killPilot(this, 0);
                this.killPilot(this, 1);
                this.hierMesh().chunkVisible("Pilot1_D1", false);
                this.hierMesh().chunkVisible("Pilot2_D1", false);
                break;

            case 19:
                this.killPilot(this, 5);
                this.killPilot(this, 6);
                this.hierMesh().chunkVisible("Pilot6_D1", false);
                this.hierMesh().chunkVisible("Pilot7_D1", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, 0.0F, -95F * f);
        this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, 0.0F, 95F * f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 8; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2:
                this.FM.turret[0].setHealth(f);
                break;

            case 3:
                this.FM.turret[1].setHealth(f);
                break;

            case 4:
                this.FM.turret[5].setHealth(f);
                break;

            case 5:
                this.FM.turret[2].setHealth(f);
                break;

            case 6:
                this.FM.turret[3].setHealth(f);
                this.FM.turret[4].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        if (i > 6) return;
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
        if (i == 0) this.hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f1 < -3.3F) {
                    f1 = -3.3F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -40F) {
                    f = -40F;
                    flag = false;
                }
                if (f > 40F) {
                    f = 40F;
                    flag = false;
                }
                if (f1 < -60F) {
                    f1 = -60F;
                    flag = false;
                }
                if (f1 > 15F) {
                    f1 = 15F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -50F) {
                    f = -50F;
                    flag = false;
                }
                if (f > 50F) {
                    f = 50F;
                    flag = false;
                }
                if (f1 < -1F) {
                    f1 = -1F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;

            case 3:
                if (f < -35F) {
                    f = -35F;
                    flag = false;
                }
                if (f > 75F) {
                    f = 75F;
                    flag = false;
                }
                if (f1 < -40F) {
                    f1 = -40F;
                    flag = false;
                }
                if (f1 > 40F) {
                    f1 = 40F;
                    flag = false;
                }
                break;

            case 4:
                if (f < -75F) {
                    f = -75F;
                    flag = false;
                }
                if (f > 35F) {
                    f = 35F;
                    flag = false;
                }
                if (f1 < -40F) {
                    f1 = -40F;
                    flag = false;
                }
                if (f1 > 40F) {
                    f1 = 40F;
                    flag = false;
                }
                break;

            case 5:
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -50F) {
                    f1 = -50F;
                    flag = false;
                }
                if (f1 > 6F) {
                    f1 = 6F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public abstract void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException;

    public abstract void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException;

    public abstract void typeBomberUpdate(float f);

    public abstract void typeBomberAdjSpeedMinus();

    public abstract void typeBomberAdjSpeedPlus();

    public abstract void typeBomberAdjSpeedReset();

    public abstract void typeBomberAdjAltitudeMinus();

    public abstract void typeBomberAdjAltitudePlus();

    public abstract void typeBomberAdjAltitudeReset();

    public abstract void typeBomberAdjSideslipMinus();

    public abstract void typeBomberAdjSideslipPlus();

    public abstract void typeBomberAdjSideslipReset();

    public abstract void typeBomberAdjDistanceMinus();

    public abstract void typeBomberAdjDistancePlus();

    public abstract void typeBomberAdjDistanceReset();

    public abstract boolean typeBomberToggleAutomation();

    private static final float anglesL6[] = { 0.0F, 13.5F, 23F, 29.5F, 34.5F, 39F, 44F, 50F, 58.5F, 69.5F, 84F };
    private static final float anglesL7[] = { 0.0F, 3F, 5F, 6F, 7F, 8F, 10F, 12.5F, 15.5F, 18.5F, 22F };

    static {
        Class class1 = FW_200.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
