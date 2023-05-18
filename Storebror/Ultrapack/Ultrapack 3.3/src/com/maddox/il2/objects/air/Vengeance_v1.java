package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class Vengeance_v1 extends Scheme1 implements TypeStormovik, TypeDiveBomber {

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -115F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -70F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay03_D0", 0.0F, -115F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay04_D0", 0.0F, -70F * f, 0.0F);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake03_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake04_D0", 0.0F, -90F * f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -65F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4b_D0", 0.0F, -65F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -65F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4b_D0", 0.0F, -65F * f, 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = 0.303F * f;
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 25F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -90F * f, 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = -0.303F * f;
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 90F * f, 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = -0.303F * f;
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void update(float f) {
        World.cur().diffCur.Torque_N_Gyro_Effects = false;
        super.update(f);
        this.FM.EI.engines[0].addVside *= 9.9999999999999995E-008D;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -33F) {
                    f = -33F;
                    flag = false;
                }
                if (f > 33F) {
                    f = 33F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 62F) {
                    f1 = 62F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.equals("xxarmorc1")) this.getEnergyPastArmor(World.Rnd().nextFloat(10F, 15F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else if (s.startsWith("xxarmorrt")) {
                    if (s.endsWith("1")) this.getEnergyPastArmor(World.Rnd().nextFloat(10F, 60F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    if (s.endsWith("2")) this.getEnergyPastArmor(19.1D / (Math.abs(Aircraft.v1.z) + 9.9999997473787516E-005D), shot);
                    if (s.endsWith("3")) this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    if (s.endsWith("4") || s.endsWith("5")) this.getEnergyPastArmor(9.5D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                } else if (s.startsWith("xxarmort")) {
                    if (s.endsWith("1")) this.getEnergyPastArmor(9.5D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    if (s.endsWith("2")) this.getEnergyPastArmor(19.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    if (s.endsWith("3")) this.getEnergyPastArmor(World.Rnd().nextFloat(9.5F, 19.1F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                }
                return;
            }
            if (s.startsWith("xxammo")) {
                if (s.endsWith("rg") && this.getEnergyPastArmor(1.2F, shot) > 0.0F) {
                    if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.setJamBullets(11, 0);
                    if (World.Rnd().nextFloat() < 0.12F) this.FM.AS.explodeTank(shot.initiator, 3);
                }
                if (s.endsWith("wl") && this.getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) this.FM.AS.setJamBullets(0, 0);
                if (s.endsWith("wr") && this.getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) this.FM.AS.setJamBullets(0, 1);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 2:
                        if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Ailerones Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                        if (World.Rnd().nextFloat() < 0.95F && this.getEnergyPastArmor(4.253F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;

                    case 4:
                    case 5:
                        if (World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
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
                if (s.startsWith("xxeng1mag")) {
                    int j = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                }
                if (s.endsWith("oil1")) {
                    if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxgun")) {
                int k = 0;
                if (s.endsWith("r")) k = 1;
                if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
                    this.FM.AS.setJamBullets(0, k);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
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
            if (s.startsWith("xxoil")) {
                if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F) {
                    this.debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if (s.startsWith("xxradio")) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 26.98F), shot);
                return;
            }
            if (s.startsWith("xxspar")) {
                if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && this.chunkDamageVisible("Tail1") > 2
                        && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if ((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if ((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if ((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if ((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if ((s.endsWith("lo1") || s.endsWith("lo2")) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if ((s.endsWith("ro1") || s.endsWith("ro2")) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if ((s.endsWith("sl1") || s.endsWith("sl2") || s.endsWith("sl3") || s.endsWith("sl4") || s.endsWith("sl5")) && this.chunkDamageVisible("StabL") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
                }
                if ((s.endsWith("sr1") || s.endsWith("sr2") || s.endsWith("sr3") || s.endsWith("sr4") || s.endsWith("sr5")) && this.chunkDamageVisible("StabR") > 2 && this.getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
                }
                if (s.startsWith("xxspark") && this.chunkDamageVisible("Keel1") > 1 && World.Rnd().nextFloat() > Math.abs(Aircraft.v1.x) + 0.1D && this.getEnergyPastArmor(3.4D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F) {
                    this.debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.hitTank(shot.initiator, l, 1);
                    if (World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F) this.FM.AS.hitTank(shot.initiator, l, 2);
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
        } else if (s.startsWith("xeng")) this.hitChunk("Engine1", shot);
        else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
            if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xWingLIn") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xWingRIn") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xWingLMid")) {
                if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
                if (World.Rnd().nextFloat() < shot.mass + 0.02F) this.FM.AS.hitOil(shot.initiator, 0);
            }
            if (s.startsWith("xWingRMid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xWingLOut") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xWingROut") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
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

    public abstract void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException;

    public abstract void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException;

    public abstract void typeDiveBomberAdjDiveAngleMinus();

    public abstract void typeDiveBomberAdjDiveAnglePlus();

    public abstract void typeDiveBomberAdjDiveAngleReset();

    public abstract void typeDiveBomberAdjVelocityMinus();

    public abstract void typeDiveBomberAdjVelocityPlus();

    public abstract void typeDiveBomberAdjVelocityReset();

    public abstract void typeDiveBomberAdjAltitudeMinus();

    public abstract void typeDiveBomberAdjAltitudePlus();

    public abstract void typeDiveBomberAdjAltitudeReset();

    public abstract boolean typeDiveBomberToggleAutomation();

    static {
        Class class1 = Vengeance_v1.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
