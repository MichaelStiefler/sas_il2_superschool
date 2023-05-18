package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public abstract class Potezxyz extends Scheme2 {

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 92F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 92F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -70F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -70F * f);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, -89F * f);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, -89F * f);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("FlapWL_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("FlapWR_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("FlapCL_D0", 0.0F, 0.0F, 45F * f);
        this.hierMesh().chunkSetAngles("FlapCR_D0", 0.0F, 0.0F, 45F * f);
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void update(float f) {
        if (this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) {
            float f1 = this.FM.EI.engines[0].getRPM();
            if (f1 < 300F && f1 > 30F) ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f1) / 3000F;
            float f3 = this.FM.EI.engines[0].getRPM();
            if (f3 < 1000F && f3 > 301F) ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f3) / 8000F;
            float f6 = this.FM.EI.engines[0].getRPM();
            if (f6 > 1001F && f6 < 1500F) ((RealFlightModel) this.FM).producedShakeLevel = 0.07F;
            float f8 = this.FM.EI.engines[0].getRPM();
            if (f8 > 1501F && f8 < 2000F) ((RealFlightModel) this.FM).producedShakeLevel = 0.05F;
            float f10 = this.FM.EI.engines[0].getRPM();
            if (f10 > 2001F && f10 < 2500F) ((RealFlightModel) this.FM).producedShakeLevel = 0.04F;
            float f12 = this.FM.EI.engines[0].getRPM();
            if (f12 > 2501F) ((RealFlightModel) this.FM).producedShakeLevel = 0.03F;
        }
        if (this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode()) {
            float f2 = this.FM.EI.engines[1].getRPM();
            if (f2 < 300F && f2 > 30F) ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f2) / 3000F;
            float f4 = this.FM.EI.engines[1].getRPM();
            if (f4 < 1000F && f4 > 301F) ((RealFlightModel) this.FM).producedShakeLevel = (1500F - f4) / 8000F;
            float f7 = this.FM.EI.engines[1].getRPM();
            if (f7 > 1001F && f7 < 1500F) ((RealFlightModel) this.FM).producedShakeLevel = 0.07F;
            float f9 = this.FM.EI.engines[1].getRPM();
            if (f9 > 1501F && f9 < 2000F) ((RealFlightModel) this.FM).producedShakeLevel = 0.05F;
            float f11 = this.FM.EI.engines[1].getRPM();
            if (f11 > 2001F && f11 < 2500F) ((RealFlightModel) this.FM).producedShakeLevel = 0.04F;
            float f13 = this.FM.EI.engines[1].getRPM();
            if (f13 > 2501F) ((RealFlightModel) this.FM).producedShakeLevel = 0.03F;
        }
        if (this.FM.getSpeedKMH() > 250F && this.FM.getVertSpeed() > 0.0F && this.FM.getAltitude() < 5000F) this.FM.producedAF.x += 20F * (250F - this.FM.getSpeedKMH());
        if (this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F) {
            RealFlightModel realflightmodel = (RealFlightModel) this.FM;
            if (realflightmodel.RealMode && realflightmodel.indSpeed > 120F) {
                float f5 = 1.0F + 0.005F * (120F - realflightmodel.indSpeed);
                if (f5 < 0.0F) f5 = 0.0F;
                this.FM.SensPitch = 0.4F * f5;
                if (realflightmodel.indSpeed > 120F) this.FM.producedAM.y -= 1700F * (120F - realflightmodel.indSpeed);
            } else this.FM.SensPitch = 0.65F;
        }
        super.update(f);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("p1")) if (Math.abs(point3d.y) > 0.231D) this.getEnergyPastArmor(8.585D / (Math.abs(Aircraft.v1.y) + 9.9999997473787516E-005D), shot);
                else this.getEnergyPastArmor(1.0F, shot);
            } else if (s.startsWith("xxeng")) {
                int i = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(20000F, 140000F) < shot.power) this.FM.AS.setEngineStuck(shot.initiator, i);
                        if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) this.FM.AS.hitEngine(shot.initiator, i, 2);
                    } else if (World.Rnd().nextFloat() < 0.04F) this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    else this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.02F);
                    this.getEnergyPastArmor(12F, shot);
                } else if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 0.9878F) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        if (World.Rnd().nextFloat() < shot.power / 48000F) this.FM.AS.hitEngine(shot.initiator, 0, 2);
                    }
                    this.getEnergyPastArmor(25F, shot);
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("oil1") && World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.FM.AS.hitOil(shot.initiator, 0);
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.4F) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if (World.Rnd().nextFloat() < 0.003F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.2F) this.FM.AS.hitTank(shot.initiator, j, 4);
                }
            } else if (s.startsWith("xxcontrol")) {
                this.debuggunnery("Controls: Hit..");
                if (World.Rnd().nextFloat() >= 0.99F) {
                    int k = new Integer(s.substring(9)).intValue();
                    switch (k) {
                        case 2:
                        case 3:
                            if (this.getEnergyPastArmor(3.5F, shot) > 0.0F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                                this.debuggunnery("Controls: Rudder Controls: Fuselage Line Destroyed..");
                            }
                            break;

                        case 0:
                        case 1:
                            if (this.getEnergyPastArmor(0.002F, shot) > 0.0F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                                this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                            }
                            break;

                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            if (this.getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 0);
                                this.debuggunnery("Controls: Aileron Controls: Disabled..");
                            }
                            break;
                    }
                }
            } else if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr")) {
                    int l = s.charAt(6) - 48;
                    if (this.getEnergyPastArmor(6.56F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) if (l < 3) {
                        this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                    } else {
                        this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                    }
                } else if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                } else if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
            }
        } else if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) this.hitChunk("CF", shot);
        } else if (s.startsWith("xtail1")) {
            if (this.chunkDamageVisible("Tail1") < 2) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) this.hitChunk("Keel2", shot);
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 2) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 2) this.hitChunk("Rudder2", shot);
        } else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 2) this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 2) this.hitChunk("WingLIn", shot);
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 2) this.hitChunk("WingRIn", shot);
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 2) this.hitChunk("WingLOut", shot);
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 2) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 2) this.hitChunk("AroneL", shot);
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 2) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
        } else if (s.startsWith("xflapcl")) {
            if (this.chunkDamageVisible("FlapCL") < 2) this.hitChunk("FlapCL", shot);
        } else if (s.startsWith("xflapcr")) {
            if (this.chunkDamageVisible("FlapCR") < 2) this.hitChunk("FlapCR", shot);
        } else if (s.startsWith("xflapwl")) {
            if (this.chunkDamageVisible("FlapWL") < 2) this.hitChunk("FlapWL", shot);
        } else if (s.startsWith("xflapwr")) {
            if (this.chunkDamageVisible("FlapWR") < 2) this.hitChunk("FlapWR", shot);
        } else if (s.startsWith("xblisters")) {
            if (this.chunkDamageVisible("BlisterS") < 2) this.hitChunk("BlisterS", shot);
        } else if (s.startsWith("xgear")) {
            if (s.startsWith("xgear") && World.Rnd().nextFloat() < 0.1F && this.getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F) this.FM.AS.setInternalDamage(shot.initiator, 3);
        } else if (s.startsWith("xturret1b")) {
            this.FM.AS.setJamBullets(10, 0);
            this.FM.AS.setJamBullets(10, 1);
        } else if (s.startsWith("xturret1c")) {
            this.FM.AS.setJamBullets(11, 0);
            this.FM.AS.setJamBullets(11, 1);
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

    static {
        Class class1 = Potezxyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryFrance);
    }
}
