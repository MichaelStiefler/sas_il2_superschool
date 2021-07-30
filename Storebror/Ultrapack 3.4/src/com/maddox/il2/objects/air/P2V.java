package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class P2V extends Scheme2a {

    public P2V() {
        this.kangle0 = 0.0F;
        this.kangle1 = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 109F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 10F), anglesc6), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 10F), anglesc7), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -89.5F * f);
        if (f < 0.5F) {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -45F), 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 45F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, -45F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, 45F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, 60F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -89.5F * f);
        if (f < 0.5F) {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -45F), 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 45F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, -45F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.9F, 0.99F, 45F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, -60F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.33F, 0.0F, 0.33F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.33F, 0.0F, 0.33F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.154F, 0.0F, 0.154F);
        this.hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("GearC31_D0", -20F * f, 0.0F, 0.0F);
        super.moveRudder(f);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                break;

            case 4:
                this.hierMesh().chunkVisible("Pilot5_D0", false);
                this.hierMesh().chunkVisible("HMask5_D0", false);
                this.hierMesh().chunkVisible("Pilot5_D1", true);
                break;

            case 5:
                this.hierMesh().chunkVisible("Pilot6_D0", false);
                this.hierMesh().chunkVisible("HMask6_D0", false);
                this.hierMesh().chunkVisible("Pilot6_D1", true);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1") || s.endsWith("p2")) if (Math.abs(Aircraft.v1.x) > 0.5D) this.getEnergyPastArmor(7.94D / Math.abs(Aircraft.v1.x), shot);
                else this.getEnergyPastArmor(9.53D / (1.0D - Math.abs(Aircraft.v1.x)), shot);
                if (s.endsWith("p3")) this.getEnergyPastArmor(7.94D / (Math.abs(Aircraft.v1.z) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("p4")) this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("p5") || s.endsWith("p6")) this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.y) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("p7")) this.getEnergyPastArmor(0.5D / (Math.abs(Aircraft.v1.z) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("p8")) this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("a1")) this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.y) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("a2")) this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.y) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("a3")) this.getEnergyPastArmor(6.35D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("a4") || s.endsWith("a5")) this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("a6") || s.endsWith("a7")) this.getEnergyPastArmor(6.35D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("r1")) this.getEnergyPastArmor(3.17D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("r2") || s.endsWith("r3")) this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                if (s.endsWith("c1") || s.endsWith("c2")) this.getEnergyPastArmor(8.73D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
            } else if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    case 1:
                        if (this.getEnergyPastArmor(3F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.12F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 1);
                                this.debuggunnery("Evelator Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.12F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 2);
                                this.debuggunnery("Rudder Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.12F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 0);
                                this.debuggunnery("Aileron Controls Out..");
                            }
                        }
                        break;

                    case 2:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.15F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                                this.debuggunnery("*** Engine1 Throttle Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.15F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                                this.debuggunnery("*** Engine1 Prop Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 0);
                                this.debuggunnery("Ailerons Controls Out..");
                            }
                        }
                        break;

                    case 3:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.15F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                                this.debuggunnery("*** Engine2 Throttle Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.15F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                                this.debuggunnery("*** Engine2 Prop Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 0);
                                this.debuggunnery("Ailerons Controls Out..");
                            }
                        }
                        break;

                    case 4:
                    case 5:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        break;

                    case 6:
                    case 7:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;
                }
            } else if (s.startsWith("xxspar")) {
                if (s.startsWith("xxspart") && World.Rnd().nextFloat() < 0.1F && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(19.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
                    this.debuggunnery("*** Tail1 Spars Broken in Half..");
                    this.msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
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
                if (s.endsWith("e2") && this.getEnergyPastArmor(28F, shot) > 0.0F) {
                    this.debuggunnery("*** Engine2 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                if (s.startsWith("xxspark1") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxspark2") && this.chunkDamageVisible("Keel2") > 1 && this.getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** Keel2 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel2_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && this.chunkDamageVisible("StabR") > 1 && this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
            } else if (s.startsWith("xxbomb")) {
                if (World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
                    this.debuggunnery("*** Bomb Payload Detonates..");
                    this.FM.AS.hitTank(shot.initiator, 0, 100);
                    this.FM.AS.hitTank(shot.initiator, 1, 100);
                    this.FM.AS.hitTank(shot.initiator, 2, 100);
                    this.FM.AS.hitTank(shot.initiator, 3, 100);
                    this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
            } else if (s.startsWith("xxeng")) {
                int j = 0;
                if (s.startsWith("xxeng2")) j = 1;
                this.debuggunnery("Engine Module[" + j + "]: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 280000F) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                        }
                        if (World.Rnd().nextFloat() < shot.power / 100000F) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 0.66F) {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 1000000F) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("eqpt") || s.endsWith("cyls") && World.Rnd().nextFloat() < 0.01F) {
                    if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                        this.debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                        this.FM.EI.engines[j].setEngineStuck(shot.initiator);
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                }
                if (s.endsWith("mag1") || s.endsWith("mag2")) {
                    this.debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                    this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, j);
                }
                if (s.endsWith("oil1")) {
                    if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, j);
                }
                if (s.endsWith("prop") && this.getEnergyPastArmor(0.42F, shot) > 0.0F) this.FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                if (s.endsWith("supc") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F) {
                    this.debuggunnery("Engine Module: Turbine Disabled..");
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                }
            } else if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 48;
                switch (k) {
                    case 1:
                    case 2:
                        this.doHitMeATank(shot, 1);
                        break;

                    case 3:
                        this.doHitMeATank(shot, 0);
                        break;

                    case 4:
                    case 5:
                        this.doHitMeATank(shot, 2);
                        break;

                    case 6:
                        this.doHitMeATank(shot, 3);
                        break;

                    case 7:
                        this.doHitMeATank(shot, 0);
                        this.doHitMeATank(shot, 1);
                        this.doHitMeATank(shot, 2);
                        this.doHitMeATank(shot, 3);
                        break;
                }
            } else if (s.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F) {
                    this.debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
            } else if (s.startsWith("xxmgun02")) {
                this.FM.AS.setJamBullets(0, 0);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (s.startsWith("xxmgun07")) {
                this.FM.AS.setJamBullets(0, 0);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (s.startsWith("xxmgun08")) {
                this.FM.AS.setJamBullets(0, 1);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (s.startsWith("xxmgun09")) {
                this.FM.AS.setJamBullets(0, 2);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (s.startsWith("xxmgun10")) {
                this.FM.AS.setJamBullets(0, 3);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (s.startsWith("xxmgun13")) {
                this.FM.AS.setJamBullets(0, 4);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (s.startsWith("xxmgun14")) {
                this.FM.AS.setJamBullets(0, 5);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (s.startsWith("xxmgun15")) {
                this.FM.AS.setJamBullets(0, 6);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (s.startsWith("xxmgun16")) {
                this.FM.AS.setJamBullets(0, 7);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (s.startsWith("xxcannon")) {
                this.FM.AS.setJamBullets(1, 0);
                this.getEnergyPastArmor(44.7F, shot);
            } else if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockr1") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockr2") && this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Rudder2 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            } else if (s.startsWith("xxammo0")) {
                int l = s.charAt(7) - 48;
                byte byte1;
                byte byte2;
                switch (l) {
                    default:
                        byte1 = 0;
                        byte2 = 0;
                        break;

                    case 2:
                        byte1 = 10;
                        byte2 = 0;
                        break;

                    case 3:
                        byte1 = 11;
                        byte2 = 0;
                        break;

                    case 4:
                        byte1 = 11;
                        byte2 = 1;
                        break;

                    case 5:
                        byte1 = 12;
                        byte2 = 0;
                        break;

                    case 6:
                        byte1 = 12;
                        byte2 = 1;
                        break;

                    case 7:
                        byte1 = 0;
                        byte2 = 0;
                        break;

                    case 8:
                        byte1 = 0;
                        byte2 = 1;
                        break;

                    case 9:
                        byte1 = 0;
                        byte2 = 2;
                        break;

                    case 10:
                        byte1 = 0;
                        byte2 = 3;
                        break;
                }
                if (World.Rnd().nextFloat() < 0.125F) this.FM.AS.setJamBullets(byte1, byte2);
                this.getEnergyPastArmor(4.7F, shot);
            }
        } else if (s.startsWith("xcf")) {
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
        } else if (s.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose1") < 2) this.hitChunk("Nose1", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
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
            if (this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
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
            if (this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneL", shot);
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 1) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
        } else if (s.startsWith("xgear")) {
            if (World.Rnd().nextFloat() < 0.1F) {
                this.debuggunnery("*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
        } else if (s.startsWith("xturret")) {
            if (s.startsWith("xturret1")) this.FM.AS.setJamBullets(10, 0);
            if (s.endsWith("2b1")) this.FM.AS.setJamBullets(11, 0);
            if (s.endsWith("2b2")) this.FM.AS.setJamBullets(11, 1);
            if (s.endsWith("3b1")) this.FM.AS.setJamBullets(12, 0);
            if (s.endsWith("3b2")) this.FM.AS.setJamBullets(12, 1);
            if (s.endsWith("4a")) this.FM.AS.setJamBullets(13, 1);
            if (s.endsWith("5a")) this.FM.AS.setJamBullets(14, 1);
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

    private final void doHitMeATank(Shot shot, int i) {
        if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) if (shot.power < 14100F) {
            if (this.FM.AS.astateTankStates[i] == 0) {
                this.FM.AS.hitTank(shot.initiator, i, 1);
                this.FM.AS.doSetTankState(shot.initiator, i, 1);
            }
            if (shot.powerType == 3 && this.FM.AS.astateTankStates[i] > 0 && World.Rnd().nextFloat() < 0.25F) this.FM.AS.hitTank(shot.initiator, i, 2);
        } else this.FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int) (shot.power / 56000F)));
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 0, 1);
            if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 2, 1);
            if (this.FM.AS.astateEngineStates[3] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 3, 1);
        }
        for (int i = 1; i < 6; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 90F * f, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 13:
                this.killPilot(this, 2);
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        this.kangle0 = 0.95F * this.kangle0 + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        if (this.kangle0 > 1.0F) this.kangle0 = 1.0F;
        for (int i = 1; i < 14; i++)
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * this.kangle0, 0.0F);

        this.kangle1 = 0.95F * this.kangle1 + 0.05F * this.FM.EI.engines[1].getControlRadiator();
        if (this.kangle1 > 1.0F) this.kangle1 = 1.0F;
        for (int j = 1; j < 14; j++)
            this.hierMesh().chunkSetAngles("Waterr" + j + "_D0", 0.0F, -20F * this.kangle1, 0.0F);

        super.update(f);
    }

    private static final float anglesc7[] = { 0.0F, -6.5F, -13.5F, -24.5F, -32.5F, -39.75F, -47F, -54.75F, -62.5F, -69.75F, -83.5F };
    private static final float anglesc6[] = { 0.0F, -20.5F, -39.5F, -57.25F, -70F, -79.75F, -87.5F, -92.75F, -95F, -94F, -85F };
    private float              kangle0;
    private float              kangle1;

    static {
        Class class1 = P2V.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
