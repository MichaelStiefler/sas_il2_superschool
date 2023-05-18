package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class SB extends Scheme2 {

    public SB() {
        this.tme = 0L;
        this.curTakeem = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -175F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -155F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -175F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -155F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, 70F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
    }

    public void moveWheelSink() {
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F) this.FM.AS.hitTank(this, 0, 1);
            if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.39F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.08F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.08F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.08F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            if (this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.08F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
            if (this.FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.16F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.16F) this.FM.AS.hitTank(this, 1, 0);
            if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.16F) this.FM.AS.hitTank(this, 1, 3);
            if (this.FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.16F) this.FM.AS.hitTank(this, 1, 2);
        }
        for (int i = 1; i < 5; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("BayL_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayR_D0", 0.0F, 90F * f, 0.0F);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -43F) {
                    f = -43F;
                    flag = false;
                }
                if (f > 43F) {
                    f = 43F;
                    flag = false;
                }
                if (f1 < -15F) {
                    f1 = -15F;
                    flag = false;
                }
                if (f1 > 15F) {
                    f1 = 15F;
                    flag = false;
                }
                break;

            case 1:
                if (this instanceof SB_2M100A) {
                    if (f < -40F) {
                        f = -40F;
                        flag = false;
                    }
                    if (f > 40F) {
                        f = 40F;
                        flag = false;
                    }
                    if (f1 < -2F) {
                        f1 = -2F;
                        flag = false;
                    }
                    if (f1 > 50F) {
                        f1 = 50F;
                        flag = false;
                    }
                    break;
                }
                if (f1 > 48F) {
                    f1 = 48F;
                    flag = false;
                }
                float f2 = Math.abs(f);
                if (f2 < 6F) {
                    if (f1 < -4.5F) {
                        f1 = -4.5F;
                        flag = false;
                    }
                    break;
                }
                if (f2 < 18F) {
                    if (f1 < -1.291667F * f2 + 3.25F) {
                        f1 = -1.291667F * f2 + 3.25F;
                        flag = false;
                    }
                    break;
                }
                if (f2 < 42F) {
                    if (f1 < -1.666667F * f1 + 10F) {
                        f1 = -1.666667F * f1 + 10F;
                        flag = false;
                    }
                    break;
                }
                if (f2 < 100F) {
                    if (f1 < -60F) {
                        f1 = -60F;
                        flag = false;
                    }
                    break;
                }
                if (f2 < 138F) {
                    if (f1 < -23.5F) {
                        f1 = -23.5F;
                        flag = false;
                    }
                    break;
                }
                if (f2 < 165F) {
                    if (f1 < 1.518519F * f2 - 233.0556F) {
                        f1 = 1.518519F * f2 - 233.0556F;
                        flag = false;
                    }
                    break;
                }
                if (f1 < 1.5F * f2 - 230F) {
                    f1 = 1.5F * f2 - 230F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -25F) {
                    f = -25F;
                    flag = false;
                }
                if (f > 25F) {
                    f = 25F;
                    flag = false;
                }
                if (f1 < -35F) {
                    f1 = -35F;
                    flag = false;
                }
                if (f1 > 10F) {
                    f1 = 10F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                this.hitProp(0, j, actor);
                return super.cutFM(34, j, actor);

            case 36:
                this.hitProp(1, j, actor);
                return super.cutFM(37, j, actor);

            case 19:
                this.FM.AS.setJamBullets(12, 0);
                if (this.FM.turret.length > 0) this.FM.turret[2].bIsOperable = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) if (s.endsWith("p1")) this.getEnergyPastArmor(0.2F, shot);
            else if (s.endsWith("p2")) this.getEnergyPastArmor(0.2F, shot);
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if (World.Rnd().nextFloat() < 0.05F || shot.mass > 0.092F && World.Rnd().nextFloat() < 0.1F) {
                            if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            if (World.Rnd().nextFloat() < 0.5F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        }
                        break;

                    case 2:
                        if (World.Rnd().nextFloat() >= 0.05F && (shot.mass <= 0.092F || World.Rnd().nextFloat() >= 0.1F)) break;
                        if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                        if (World.Rnd().nextFloat() < 0.5F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                        break;

                    case 3:
                        if (this.getEnergyPastArmor(1.0F, shot) <= 0.0F) break;
                        if (World.Rnd().nextFloat() < 0.12F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.12F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                        }
                        break;
                }
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxspart") && World.Rnd().nextFloat() < 0.36F && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(6.8F, shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(14.8F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(14.8F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(12.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(12.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(9.1F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(9.1F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(5.2F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && this.chunkDamageVisible("StabR") > 1 && this.getEnergyPastArmor(5.2F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if (s.startsWith("xxspare1") && this.getEnergyPastArmor(28F, shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Engine1 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if (s.startsWith("xxspare2") && this.getEnergyPastArmor(28F, shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Engine2 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
            }
            if (s.startsWith("xxbmb") && World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
                Aircraft.debugprintln(this, "*** Bomb Payload Detonates..");
                this.FM.AS.hitTank(shot.initiator, 0, 10);
                this.FM.AS.hitTank(shot.initiator, 1, 10);
                this.FM.AS.hitTank(shot.initiator, 2, 10);
                this.FM.AS.hitTank(shot.initiator, 3, 10);
                this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if (s.startsWith("xxeng")) {
                int j = s.charAt(5) - 49;
                if (s.endsWith("prop") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F) {
                    this.FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Prop Governor Failed..");
                }
                if (s.endsWith("gear") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F) {
                    this.FM.EI.engines[j].setKillPropAngleDeviceSpeeds(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Prop Governor Damaged..");
                }
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 200000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 50000F) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Damaged..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 28000F) {
                            this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        }
                        if (World.Rnd().nextFloat() < 0.08F) {
                            this.FM.EI.engines[j].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                        }
                        this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[j].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Stalled..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.AS.hitEngine(shot.initiator, j, 10);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                    }
                    this.getEnergyPastArmor(6F, shot);
                }
                if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 1.72F) {
                    this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[j].setEngineStuck(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinder Case Broken - Engine Stuck..");
                    }
                    if (World.Rnd().nextFloat() < shot.power / 24000F) {
                        this.FM.AS.hitEngine(shot.initiator, j, 3);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if (s.endsWith("supc") && this.getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
                    this.FM.EI.engines[j].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Supercharger Out..");
                }
                if (s.endsWith("eqpt") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Magneto Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[j].setKillCompressor(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Compressor Feed Out..");
                    }
                }
            }
            if (s.startsWith("xxoil")) {
                int k = 0;
                if (s.endsWith("2")) k = 1;
                if (this.getEnergyPastArmor(0.21F, shot) > 0.0F) this.FM.AS.hitOil(shot.initiator, k);
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.03F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[l] == 0) {
                        this.FM.AS.hitTank(shot.initiator, l, 2);
                        this.FM.AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if (shot.powerType == 3) if (shot.power < 14100F) {
                        if (this.FM.AS.astateTankStates[l] < 4 && World.Rnd().nextFloat() < 0.1F) this.FM.AS.hitTank(shot.initiator, l, 1);
                    } else this.FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(0, (int) (shot.power / 28200F)));
                }
            }
            if (s.startsWith("xxhyd")) this.FM.AS.setInternalDamage(shot.initiator, 3);
            if (s.startsWith("xxpnm")) this.FM.AS.setInternalDamage(shot.initiator, 1);
        } else if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (shot.power > 33000F && point3d.x > 1.0D) {
                this.FM.AS.hitPilot(shot.initiator, 0, World.Rnd().nextInt(30, 192));
                this.FM.AS.hitPilot(shot.initiator, 1, World.Rnd().nextInt(30, 192));
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) this.hitChunk("Rudder1", shot);
        else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvatorl")) this.hitChunk("VatorL", shot);
        else if (s.startsWith("xvatorr")) this.hitChunk("VatorR", shot);
        else if (s.startsWith("xwinglin")) {
            if ((this.FM.AS.astateTankStates[0] > 1 || this.FM.AS.astateTankStates[1] > 1) && shot.powerType == 3 && this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.33F && World.Rnd().nextFloat() < this.curTakeem)
                this.FM.AS.hitTank(shot.initiator, World.Rnd().nextInt(0, 1), 3);
            if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
        } else if (s.startsWith("xwingrin")) {
            if ((this.FM.AS.astateTankStates[2] > 1 || this.FM.AS.astateTankStates[3] > 1) && shot.powerType == 3 && this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.33F && World.Rnd().nextFloat() < this.curTakeem)
                this.FM.AS.hitTank(shot.initiator, World.Rnd().nextInt(2, 3), 3);
            if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
        } else if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
        } else if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
        else if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
        else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
            this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 168000F));
            Aircraft.debugprintln(this, "*** Engine1 Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
            this.FM.EI.engines[1].setReadyness(shot.initiator, this.FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 168000F));
            Aircraft.debugprintln(this, "*** Engine2 Hit - Readyness Reduced to " + this.FM.EI.engines[1].getReadyness() + "..");
        } else if (s.startsWith("xgear")) {
            if (World.Rnd().nextFloat() < 0.1F) {
                Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
        } else if (s.startsWith("xturret")) {
            if (s.startsWith("xturret1")) {
                this.FM.AS.setJamBullets(10, 0);
                this.FM.AS.setJamBullets(10, 1);
            }
            if (s.startsWith("xturret2")) this.FM.AS.setJamBullets(11, 0);
            if (s.startsWith("xturret3")) this.FM.AS.setJamBullets(12, 0);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i1;
            if (s.endsWith("a")) {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else i1 = s.charAt(5) - 49;
            this.hitFlesh(i1, shot, byte0);
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 2:
                this.FM.turret[1].bIsOperable = false;
                this.FM.turret[2].bIsOperable = false;
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
                this.hierMesh().chunkVisible("Gore2_D0", true);
                this.hierMesh().chunkVisible("Gore1_D0", this.hierMesh().isChunkVisible("Blister1_D0"));
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Gore3_D0", true);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                break;
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        if (i >= 3) {
            this.doRemoveBodyChunkFromPlane("Pilot4");
            this.doRemoveBodyChunkFromPlane("Head4");
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void update(float f) {
        if (Time.current() > this.tme) {
            this.tme = Time.current() + World.Rnd().nextLong(1000L, 5000L);
            if (this.FM.turret.length != 0) {
                if (this.FM.turret[0].bIsOperable != (this.radist[1] == 0)) {
                    Actor actor = this.FM.turret[0].target;
                    if (actor != null) this.setRadist(1, 1);
                    else {
                        Actor actor1 = this.FM.turret[1].target;
                        if (actor1 == null) actor1 = this.FM.turret[2].target;
                        if (actor1 != null) {
                            this.setRadist(1, 1);
                            this.FM.turret[0].target = actor1;
                        } else this.setRadist(1, 0);
                    }
                }
                if (this.FM.turret[1].bIsOperable) {
                    Actor actor2 = this.FM.turret[1].target;
                    if (actor2 != null && Actor.isValid(actor2)) {
                        this.pos.getAbs(Aircraft.tmpLoc2);
                        actor2.pos.getAbs(Aircraft.tmpLoc3);
                        Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
                        if (Aircraft.tmpLoc3.getPoint().z < 0.0D) this.setRadist(2, 1);
                    }
                } else if (this.FM.turret[2].bIsOperable) {
                    Actor actor3 = this.FM.turret[2].target;
                    if (actor3 != null && Actor.isValid(actor3)) {
                        this.pos.getAbs(Aircraft.tmpLoc2);
                        actor3.pos.getAbs(Aircraft.tmpLoc3);
                        Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
                        if (Aircraft.tmpLoc3.getPoint().z > 0.0D) this.setRadist(2, 0);
                    }
                }
            }
        }
        super.update(f);
    }

    private void setRadist(int i, int j) {
        this.radist[i] = j;
        if (this.FM.AS.astatePilotStates[2] <= 90) {
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("HMask3_D0", false);
            this.hierMesh().chunkVisible("HMask4_D0", false);
            this.FM.turret[1].bIsOperable = false;
            this.FM.turret[2].bIsOperable = false;
            switch (j) {
                case 1:
                    this.hierMesh().chunkVisible("Pilot3_D0", true);
                    this.hierMesh().chunkVisible("HMask3_D0", this.FM.Loc.z > 3000D);
                    this.FM.turret[1].bIsOperable = true;
                    break;

                case 2:
                    this.hierMesh().chunkVisible("Pilot4_D0", true);
                    this.hierMesh().chunkVisible("HMask4_D0", this.FM.Loc.z > 3000D);
                    this.FM.turret[2].bIsOperable = true;
                    break;
            }
        }
    }

    private long  tme;
    private int   radist[] = { 0, 0, 0 };
    private float curTakeem;

    static {
        Class class1 = SB.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
