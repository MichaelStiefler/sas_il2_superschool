package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class BR_690 extends Scheme2 implements TypeStormovik {

    public BR_690() {
        this.bGunUp = false;
        this.btme = -1L;
        this.fGunPos = 0.0F;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 92F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 92F * f);
    }

    protected void moveGear(float f) {
        BR_690.moveGear(this.hierMesh(), f);
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

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("BayL_D0", 0.0F, 85F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayR_D0", 0.0F, -85F * f, 0.0F);
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
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

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    if (Aircraft.v1.z > 0.5D) {
                        this.getEnergyPastArmor(5D / Aircraft.v1.z, shot);
                    } else if (Aircraft.v1.x > 0.93969261646270752D) {
                        this.getEnergyPastArmor((10D / Aircraft.v1.x) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                    }
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(5D / Math.abs(Aircraft.v1.z), shot);
                }
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 2:
                        if (this.getEnergyPastArmor(1.0F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.12F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                                this.debuggunnery("Evelator Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.12F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                                this.debuggunnery("Rudder Controls Out..");
                            }
                        }
                        break;

                    case 3:
                    case 4:
                        if ((this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Ailerons Controls Out..");
                        }
                        break;

                    case 5:
                        if (this.getEnergyPastArmor(0.1F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.75F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.debuggunnery("*** Engine1 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.45F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            this.debuggunnery("*** Engine1 Prop Controls Out..");
                        }
                        break;

                    case 6:
                        if (this.getEnergyPastArmor(0.1F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.75F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            this.debuggunnery("*** Engine2 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.45F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            this.debuggunnery("*** Engine2 Prop Controls Out..");
                        }
                        break;
                }
            }
            if (s.startsWith("xxspar")) {
                if ((s.endsWith("cf1") || s.endsWith("cf2")) && (World.Rnd().nextFloat() < 0.1F) && (this.chunkDamageVisible("CF") > 2) && (this.getEnergyPastArmor(19.9F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                    this.debuggunnery("*** CF Spars Broken in Half..");
                    this.msgCollision(this, "WingLIn_D0", "WingLIn_D0");
                    this.msgCollision(this, "WingRIn_D0", "WingRIn_D0");
                }
                if ((s.endsWith("li1") || s.endsWith("li2")) && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
                }
                if ((s.endsWith("ri1") || s.endsWith("ri2")) && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(17.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
                }
                if ((s.endsWith("lo1") || s.endsWith("lo2")) && (World.Rnd().nextFloat() < (1.0D - (0.79D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
                }
                if ((s.endsWith("ro1") || s.endsWith("ro2")) && (World.Rnd().nextFloat() < (1.0D - (0.79D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
                }
                if (s.endsWith("e1") && ((point3d.y > 2.79D) || (point3d.y < 2.32D)) && (this.getEnergyPastArmor(18F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine1 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if (s.endsWith("e2") && ((point3d.y < -2.79D) || (point3d.y > -2.32D)) && (this.getEnergyPastArmor(18F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine2 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
            }
            if (s.startsWith("xxbomb") && (World.Rnd().nextFloat() < 0.01F) && (this.FM.CT.Weapons[3] != null) && this.FM.CT.Weapons[3][0].haveBullets()) {
                this.debuggunnery("*** Bomb Payload Detonates..");
                this.FM.AS.hitTank(shot.initiator, 0, 100);
                this.FM.AS.hitTank(shot.initiator, 1, 100);
                this.FM.AS.hitTank(shot.initiator, 2, 100);
                this.FM.AS.hitTank(shot.initiator, 3, 100);
                this.msgCollision(this, "CF_D0", "CF_D0");
            }
            if (s.startsWith("xxeng")) {
                int i = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(20000F, 140000F) < shot.power) {
                            this.FM.AS.setEngineStuck(shot.initiator, i);
                        }
                        if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) {
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                        }
                    } else if (World.Rnd().nextFloat() < 0.04F) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.02F);
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(0.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[i].getCylindersRatio() * 0.9878F))) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) {
                        this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("oil1") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(1.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.4F)) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if ((World.Rnd().nextFloat() < 0.003F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.2F))) {
                        this.FM.AS.hitTank(shot.initiator, j, 4);
                    }
                }
                return;
            }
        }
        if (s.startsWith("xmgun")) {
            if (s.endsWith("01")) {
                if ((this.getEnergyPastArmor(5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                    this.FM.AS.setJamBullets(0, 0);
                    this.getEnergyPastArmor(11.98F, shot);
                }
            } else if (s.endsWith("02")) {
                if ((this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                    this.FM.AS.setJamBullets(0, 1);
                    this.getEnergyPastArmor(11.98F, shot);
                }
            } else if (s.endsWith("03") && (this.getEnergyPastArmor(4.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.75F)) {
                this.FM.AS.setJamBullets(0, 2);
                this.getEnergyPastArmor(11.98F, shot);
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) {
                this.hitChunk("Keel2", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 2) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 2) {
                this.hitChunk("Rudder2", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (this.chunkDamageVisible("Stab") < 2) {
                this.hitChunk("Stab", shot);
            }
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 2) {
                this.hitChunk("VatorL", shot);
            }
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 2) {
                this.hitChunk("WingLIn", shot);
            }
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 2) {
                this.hitChunk("WingRIn", shot);
            }
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 2) {
                this.hitChunk("WingLOut", shot);
            }
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 2) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 2) {
                this.hitChunk("AroneL", shot);
            }
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 2) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xgear")) {
            if (s.startsWith("xgear") && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F)) {
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xflap1")) {
            if (this.chunkDamageVisible("FlapCL") < 2) {
                this.hitChunk("FlapCL", shot);
            }
        } else if (s.startsWith("xflap2")) {
            if (this.chunkDamageVisible("FlapCR") < 2) {
                this.hitChunk("FlapCR", shot);
            }
        } else if (s.startsWith("xflap3")) {
            if (this.chunkDamageVisible("FlapWL") < 2) {
                this.hitChunk("FlapWL", shot);
            }
        } else if (s.startsWith("xflap4")) {
            if (this.chunkDamageVisible("FlapWR") < 2) {
                this.hitChunk("FlapWR", shot);
            }
        } else if (s.startsWith("xblock1")) {
            if (this.chunkDamageVisible("Block1") < 2) {
                this.hitChunk("Block1", shot);
            }
        } else if (s.startsWith("xblock2")) {
            if (this.chunkDamageVisible("Block2") < 2) {
                this.hitChunk("Block2", shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k;
            if (s.endsWith("a") || s.endsWith("a2")) {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else if (s.endsWith("b") || s.endsWith("b2")) {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else {
                k = s.charAt(5) - 49;
            }
            this.hitFlesh(k, shot, byte0);
        }
    }

    public void update(float f) {
        if (!this.bGunUp) {
            if (this.fGunPos > 0.0F) {
                this.fGunPos -= 0.2F * f;
                this.FM.turret[0].bIsOperable = false;
                this.hierMesh().chunkVisible("Blistup_D0", false);
                this.hierMesh().chunkVisible("Blister2_D0", true);
            }
        } else if (this.fGunPos < 1.0F) {
            this.fGunPos += 0.2F * f;
            if ((this.fGunPos > 0.8F) && (this.fGunPos < 0.9F)) {
                this.FM.turret[0].bIsOperable = true;
                this.hierMesh().chunkVisible("Blistup_D0", true);
                this.hierMesh().chunkVisible("Blister2_D0", false);
            }
        }
        if (this.FM.turret[0].bIsAIControlled) {
            if ((this.FM.turret[0].target != null) && (this.FM.AS.astatePilotStates[2] < 90)) {
                this.bGunUp = true;
            }
            if (Time.current() > this.btme) {
                this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if ((this.FM.turret[0].target == null) && (this.FM.AS.astatePilotStates[2] < 90)) {
                    this.bGunUp = false;
                }
            }
        }
        if (this.FM.isPlayers() && (this.FM.Sq.squareElevators > 0.0F)) {
            if ((this.FM.getSpeedKMH() > 400F) && (this.FM.getSpeedKMH() < 500F)) {
                this.FM.SensPitch = 0.2F;
                this.FM.producedAM.y -= 400F * (300F - this.FM.getSpeedKMH());
            }
            if (this.FM.getSpeedKMH() >= 501F) {
                this.FM.SensPitch = 0.17F;
                this.FM.producedAM.y -= 250F * (300F - this.FM.getSpeedKMH());
            } else {
                this.FM.SensPitch = 0.59F;
            }
        }
        if (this.FM.CT.getGear() >= 0.99F) {
            this.hierMesh().chunkVisible("Antenna", true);
            this.hierMesh().chunkVisible("Antenna1", false);
        }
        if (this.FM.CT.getGear() < 0.01F) {
            this.hierMesh().chunkVisible("Antenna", false);
            this.hierMesh().chunkVisible("Antenna1", true);
        }
        if (this.FM.Gears.nOfGearsOnGr > 0) {
            this.hierMesh().chunkVisible("Antenna", true);
            this.hierMesh().chunkVisible("Antenna1", false);
        }
        super.update(f);
    }

    boolean      bGunUp;
    public long  btme;
    public float fGunPos;

    static {
        Class class1 = BR_690.class;
        Property.set(class1, "originCountry", PaintScheme.countryFrance);
    }
}
