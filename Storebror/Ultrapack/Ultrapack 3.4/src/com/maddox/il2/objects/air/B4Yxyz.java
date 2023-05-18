package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class B4Yxyz extends Scheme1 implements TypeScout, TypeBomber {

    public B4Yxyz() {
        this.arrestor = 0.0F;
        this.kangle = 0.0F;
        this.bGunnerKilled = false;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                break;
        }
    }

    public boolean cut(String s) {
        boolean flag = super.cut(s);
        if (s.equalsIgnoreCase("WingLIn")) {
            this.hierMesh().chunkVisible("WingLMid_CAP", true);
        } else if (s.equalsIgnoreCase("WingRIn")) {
            this.hierMesh().chunkVisible("WingRMid_CAP", true);
        }
        return flag;
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLMid_D0", 90F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", -90F * f, 0.0F, 0.0F);
    }

    public void moveWingFold(float f) {
        this.moveWingFold(this.hierMesh(), f);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -40F * f, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 19:
                this.FM.Gears.hitCentreGear();
                break;

            case 3:
                if (World.Rnd().nextInt(0, 99) < 1) {
                    this.FM.AS.hitEngine(this, 0, 4);
                    this.hitProp(0, j, actor);
                    this.FM.EI.engines[0].setEngineStuck(actor);
                    return this.cut("engine1");
                } else {
                    this.FM.AS.setEngineDies(this, 0);
                    return false;
                }
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
    }

    protected void moveFlap(float f) {
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -29F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
        float f1 = this.FM.CT.getAileron();
        this.hierMesh().chunkSetAngles("pilot1_arm2_d0", Aircraft.cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, Aircraft.cvt(f1, -1F, 1.0F, 6F, -8F) - Aircraft.cvt(f, -1F, 1.0F, -37F, 35F));
        this.hierMesh().chunkSetAngles("pilot1_arm1_d0", 0.0F, 0.0F, Aircraft.cvt(f1, -1F, 1.0F, -16F, 14F) + Aircraft.cvt(f, -1F, 0.0F, -61F, 0.0F) + Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 43F));
        if (f < 0.0F) {
            f /= 2.0F;
        }
        this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 15F * f1, Aircraft.cvt(f, -1F, 1.0F, -16F, 20F));
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneL3_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR3_D0", 0.0F, -35F * f, 0.0F);
        float f1 = this.FM.CT.getElevator();
        this.hierMesh().chunkSetAngles("pilot1_arm2_d0", Aircraft.cvt(f, -1F, 1.0F, 14F, -16F), 0.0F, Aircraft.cvt(f, -1F, 1.0F, 6F, -8F) - Aircraft.cvt(f1, -1F, 1.0F, -37F, 35F));
        this.hierMesh().chunkSetAngles("pilot1_arm1_d0", 0.0F, 0.0F, Aircraft.cvt(f, -1F, 1.0F, -16F, 14F) + Aircraft.cvt(f1, -1F, 0.0F, -61F, 0.0F) + Aircraft.cvt(f1, 0.0F, 1.0F, 0.0F, 43F));
        if (f1 < 0.0F) {
            f1 /= 2.0F;
        }
        this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 15F * f, Aircraft.cvt(f1, -1F, 1.0F, -16F, 20F));
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        this.hierMesh().chunkVisible("pilot1_arm2_d0", false);
        this.hierMesh().chunkVisible("pilot1_arm1_d0", false);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("pilot1_arm2_d0", false);
                this.hierMesh().chunkVisible("pilot1_arm1_d0", false);
                break;

            case 1:
                this.bGunnerKilled = true;
                this.FM.turret[0].bIsOperable = false;
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public void update(float f) {
        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        if (this.kangle > 1.0F) {
            this.kangle = 1.0F;
        }
        this.hierMesh().chunkSetAngles("radiator1_D0", 0.0F, -55F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("radiator2_D0", 0.0F, -40F * this.kangle, 0.0F);
        super.update(f);
        if (this.FM.Gears.arrestorVAngle != 0.0F) {
            float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -44F, 9F, 1.0F, 0.0F);
            this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
        } else {
            float f2 = (-28F * this.FM.Gears.arrestorVSink) / 53F;
            if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            }
            if (f2 > 0.15F) {
                f2 = 0.15F;
            }
            this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
            if (this.arrestor < 0.0F) {
                this.arrestor = 0.0F;
            }
        }
        if (this.arrestor > this.FM.CT.getArrestor()) {
            this.arrestor = this.FM.CT.getArrestor();
        }
        this.moveArrestorHook(this.arrestor);
    }

    public void missionStarting() {
        super.missionStarting();
        this.hierMesh().chunkVisible("pilot1_arm2_d0", true);
        this.hierMesh().chunkVisible("pilot1_arm1_d0", true);
    }

    public void prepareCamouflage() {
        super.prepareCamouflage();
        this.hierMesh().chunkVisible("pilot1_arm2_d0", true);
        this.hierMesh().chunkVisible("pilot1_arm1_d0", true);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(9.96F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                if (s.endsWith("7")) {
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#7)");
                    }
                } else if (s.endsWith("8")) {
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#8)");
                    }
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out.. (#8)");
                    }
                } else if (s.endsWith("5")) {
                    if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#5)");
                    }
                } else if (s.endsWith("6")) {
                    if (World.Rnd().nextFloat() < 0.5F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#6)");
                    }
                } else if ((s.endsWith("2") || s.endsWith("4")) && (World.Rnd().nextFloat() < 0.5F)) {
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Arone Controls Out.. (#2/#4)");
                }
                return;
            }
            if (s.startsWith("xxeng") || s.startsWith("xxEng")) {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if (s.endsWith("prop")) {
                    Aircraft.debugprintln(this, "*** Prop hit");
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12.7F, shot);
                } else if (s.startsWith("xxeng1cyls")) {
                    if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.12F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if (World.Rnd().nextFloat() < 0.005F) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                } else if (s.endsWith("Oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                this.FM.AS.hitOil(shot.initiator, 0);
                this.getEnergyPastArmor(0.22F, shot);
                Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
            } else if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.4F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.99F)) {
                    if (this.FM.AS.astateTankStates[i] == 0) {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            } else if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                } else if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                } else if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
            } else if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    Aircraft.debugprintln(this, "*** Fixed Gun #1: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    Aircraft.debugprintln(this, "*** Fixed Gun #2: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(9.5F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                } else if (s.startsWith("xxsparli") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                } else if (s.startsWith("xxspar2i") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                } else if (s.startsWith("xxsparlm") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                } else if (s.startsWith("xxsparrm") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                } else if (s.startsWith("xxsparlo") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                } else if (s.startsWith("xxsparro") && (World.Rnd().nextFloat() < 0.25F) && (this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                }
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else {
                j = s.charAt(5) - 49;
            }
            Aircraft.debugprintln(this, "*** hitFlesh..");
            this.hitFlesh(j, shot, byte0);
        } else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xturret1b")) {
            Aircraft.debugprintln(this, "*** Turret Gun: Disabled.. (xturret1b)");
            this.FM.AS.setJamBullets(10, 0);
            this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 2)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xWing")) {
            Aircraft.debugprintln(this, "*** xWing: " + s);
            if (s.startsWith("xWingLIn") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xWingRIn") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xWingLmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.startsWith("xWingRmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
        } else if (s.startsWith("xwing")) {
            Aircraft.debugprintln(this, "*** xwing: " + s);
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL1", shot);
                this.hitChunk("AroneL2", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR1", shot);
                this.hitChunk("AroneR2", shot);
            }
        }
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

    protected float kangle;
    private float   arrestor;
    boolean         bGunnerKilled;

    static {
        Class class1 = B4Yxyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
