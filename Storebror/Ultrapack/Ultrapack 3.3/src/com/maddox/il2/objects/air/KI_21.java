package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class KI_21 extends Scheme2 {

    public KI_21() {
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink() {
        if (this.FM.CT.getGear() < 0.999F) return;
        else {
            this.resetYPRmodifier();
            this.hierMesh().chunkSetAngles("GearL10_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.35F, -38F, -27.5F), 0.0F);
            this.hierMesh().chunkSetAngles("GearL11_D0", 0.0F, 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.35F, -45F, -48.25F));
            xyz[2] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.35F, 0.0F, -0.325F);
            this.hierMesh().chunkSetLocate("GearL12_D0", xyz, ypr);
            this.hierMesh().chunkSetAngles("GearR10_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.35F, -38F, -27.5F), 0.0F);
            this.hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.35F, -45F, -48.25F));
            xyz[2] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.35F, 0.0F, -0.325F);
            this.hierMesh().chunkSetLocate("GearR12_D0", xyz, ypr);
            return;
        }
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) for (int i = 1; i < 7; i++)
            this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
        else for (int j = 1; j < 7; j++)
            this.hierMesh().chunkVisible("HMask" + j + "_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 33:
                this.hitProp(0, j, actor);
                break;

            case 36:
                this.hitProp(1, j, actor);
                break;

            case 34:
                this.FM.AS.hitEngine(this, 0, 2);
                if (World.Rnd().nextInt(0, 99) < 66) this.FM.AS.hitEngine(this, 0, 2);
                break;

            case 37:
                this.FM.AS.hitEngine(this, 1, 2);
                if (World.Rnd().nextInt(0, 99) < 66) this.FM.AS.hitEngine(this, 1, 2);
                break;

            case 19:
                this.killPilot(actor, 5);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxbomb")) {
                if (World.Rnd().nextFloat() < 0.001F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
                    debugprintln(this, "*** Bomb Payload Detonates..");
                    this.FM.AS.hitTank(shot.initiator, 0, 10);
                    this.FM.AS.hitTank(shot.initiator, 1, 10);
                    this.FM.AS.hitTank(shot.initiator, 2, 10);
                    this.FM.AS.hitTank(shot.initiator, 3, 10);
                    this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxcontrol")) {
                int i = s.charAt(9) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 2:
                        if (this.getEnergyPastArmor(2.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.1F) this.FM.AS.setControlsDamage(shot.initiator, 2);
                        break;

                    case 3:
                        if (this.getEnergyPastArmor(2.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2F) this.FM.AS.setControlsDamage(shot.initiator, 1);
                        break;

                    case 4:
                    case 5:
                        if (this.getEnergyPastArmor(2.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) this.FM.AS.setControlsDamage(shot.initiator, 0);
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int j = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(20000F, 140000F) < shot.power) {
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                            this.debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            this.debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.04F) this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                    else {
                        this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - 0.02F);
                        this.debuggunnery("*** Engine" + j + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 0.9878F) {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        this.debuggunnery("*** Engine" + j + " Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 48000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            this.debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) this.FM.EI.engines[j].setKillCompressor(shot.initiator);
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("prop")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        if (Pd.y > 0.0D && Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
                        if (Pd.y < 0.0D && Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power) this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("oil1") && this.getEnergyPastArmor(4.21F, shot) > 0.0F) {
                    this.FM.AS.hitOil(shot.initiator, j);
                    this.getEnergyPastArmor(0.42F, shot);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                if (s.startsWith("xxlockal")) {
                    if (this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
                        this.debuggunnery("*** AroneL Lock Damaged..");
                        this.nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockar")) {
                    if (this.getEnergyPastArmor(4.35F, shot) > 0.0F) {
                        this.debuggunnery("*** AroneR Lock Damaged..");
                        this.nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockvl")) {
                    if (this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
                        this.debuggunnery("*** VatorL Lock Damaged..");
                        this.nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockvr")) {
                    if (this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
                        this.debuggunnery("*** VatorR Lock Damaged..");
                        this.nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
                    }
                } else if (s.startsWith("xxlockr") && this.getEnergyPastArmor(4.32F, shot) > 0.0F) {
                    this.debuggunnery("*** Rudder1 Lock Damaged..");
                    this.nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if ((s.endsWith("li1") || s.endsWith("li2")) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if ((s.endsWith("ri1") || s.endsWith("ri2")) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if ((s.endsWith("lm1") || s.endsWith("lm2")) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if ((s.endsWith("rm1") || s.endsWith("rm2")) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if ((s.endsWith("lo1") || s.endsWith("lo2")) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if ((s.endsWith("ro1") || s.endsWith("ro2")) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.endsWith("e1") && this.getEnergyPastArmor(28F, shot) > 0.0F) {
                    this.debuggunnery("*** Engine1 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if (s.endsWith("e3") && this.getEnergyPastArmor(28F, shot) > 0.0F) {
                    this.debuggunnery("*** Engine2 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                int k = s.charAt(5) - 49;
                if (this.getEnergyPastArmor(4.21F, shot) > 0.0F) {
                    this.FM.AS.hitOil(shot.initiator, k);
                    this.getEnergyPastArmor(0.42F, shot);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.06F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[l] == 0) {
                        this.FM.AS.hitTank(shot.initiator, l, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if (shot.powerType == 3) {
                        if (shot.power < 16100F) {
                            if (this.FM.AS.astateTankStates[l] < 4 && World.Rnd().nextFloat() < 0.21F) this.FM.AS.hitTank(shot.initiator, l, 1);
                        } else this.FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                    } else if (shot.power > 16100F) this.FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
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
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
        } else {
            if (s.startsWith("xturret")) {
                if (s.startsWith("xturret1")) this.FM.AS.setJamBullets(10, 0);
                if (s.startsWith("xturret2")) this.FM.AS.setJamBullets(11, 0);
                if (s.startsWith("xturret3")) this.FM.AS.setJamBullets(12, 0);
                if (s.startsWith("xturret4")) this.FM.AS.setJamBullets(13, 0);
                return;
            }
            if (s.startsWith("xgear")) {
                if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                    this.debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if (s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F) {
                    this.debuggunnery("Undercarriage: Stuck..");
                    this.FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
                byte byte0 = 0;
                int i1;
                if (s.endsWith("a") || s.endsWith("a2")) {
                    byte0 = 1;
                    i1 = s.charAt(6) - 49;
                } else if (s.endsWith("b") || s.endsWith("b2")) {
                    byte0 = 2;
                    i1 = s.charAt(6) - 49;
                } else i1 = s.charAt(5) - 49;
                this.hitFlesh(i1, shot, byte0);
            }
        }
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
        }
    }

    public void doMurderPilot(int i) {
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        if (i == 0) this.hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        this.hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
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

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -100F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -100F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, -100F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -100F * f, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 2; i++) {
            float f1 = this.FM.EI.engines[i].getControlRadiator();
            if (Math.abs(this.flapps[i] - f1) <= 0.01F) continue;
            this.flapps[i] = f1;
            for (int j = 1; j < 10; j++) {
                String s = "RAD" + (i != 0 ? "R" : "L") + "0" + j + "_D0";
                this.hierMesh().chunkSetAngles(s, 0.0F, -20F * f1, 0.0F);
            }

            for (int k = 10; k < 13; k++) {
                String s1 = "RAD" + (i != 0 ? "R" : "L") + "" + k + "_D0";
                this.hierMesh().chunkSetAngles(s1, 0.0F, -20F * f1, 0.0F);
            }

        }

    }

    private float flapps[] = { 0.0F, 0.0F };

    static {
        Class class1 = KI_21.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
