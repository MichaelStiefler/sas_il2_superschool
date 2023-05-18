package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public abstract class A_26 extends Scheme2a {

    public A_26() {
        this.kangle0 = 0.0F;
        this.kangle1 = 0.0F;
        A_26.kl = 1.0F;
        A_26.kr = 1.0F;
        A_26.kc = 1.0F;
    }

    protected void moveGear(float f) {
        A_26.moveGear(this.hierMesh(), f);
        if (this.FM.Gears.isHydroOperable()) {
            A_26.kl = 1.0F;
            A_26.kr = 1.0F;
            A_26.kc = 1.0F;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 109F * f * A_26.kc, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f * A_26.kc, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f * A_26.kc, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -89.5F * f * A_26.kl);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f * A_26.kl, 0.01F, 0.1F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f * A_26.kl, 0.01F, 0.1F, 0.0F, 75F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -89.5F * f * A_26.kr);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f * A_26.kr, 0.01F, 0.1F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f * A_26.kr, 0.01F, 0.1F, 0.0F, 75F), 0.0F);
    }

    public void moveWheelSink() {
    }

    protected void moveRudder(float f) {
        super.moveRudder(f);
    }

    protected void hitBone(String string, Shot shot, Point3d point3d) {
        if (string.startsWith("xx")) {
            if (string.startsWith("xxarmor")) {
                if (string.endsWith("p1") || string.endsWith("p2")) {
                    if (Math.abs(Aircraft.v1.x) > 0.5D) {
                        this.getEnergyPastArmor(7.94D / Math.abs(Aircraft.v1.x), shot);
                    } else {
                        this.getEnergyPastArmor(9.53D / (1.0D - Math.abs(Aircraft.v1.x)), shot);
                    }
                }
                if (string.endsWith("p3")) {
                    this.getEnergyPastArmor(7.94D / (Math.abs(Aircraft.v1.z) + 0.000001D), shot);
                }
                if (string.endsWith("p4")) {
                    this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (string.endsWith("p5") || string.endsWith("p6")) {
                    this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.y) + 0.000001D), shot);
                }
                if (string.endsWith("p7")) {
                    this.getEnergyPastArmor(0.5D / (Math.abs(Aircraft.v1.z) + 0.000001D), shot);
                }
                if (string.endsWith("p8")) {
                    this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (string.endsWith("a1")) {
                    this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.y) + 0.000001D), shot);
                }
                if (string.endsWith("a2")) {
                    this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.y) + 0.000001D), shot);
                }
                if (string.endsWith("a3")) {
                    this.getEnergyPastArmor(6.35D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (string.endsWith("a4") || string.endsWith("a5")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (string.endsWith("a6") || string.endsWith("a7")) {
                    this.getEnergyPastArmor(6.35D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (string.endsWith("r1")) {
                    this.getEnergyPastArmor(3.17D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (string.endsWith("r2") || string.endsWith("r3")) {
                    this.getEnergyPastArmor(9.53D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
                if (string.endsWith("c1") || string.endsWith("c2")) {
                    this.getEnergyPastArmor(8.73D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                }
            } else if (string.startsWith("xxcontrols")) {
                int i = string.charAt(10) - 48;
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
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.12F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        break;

                    case 6:
                    case 7:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.12F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;
                }
            } else if (string.startsWith("xxspar")) {
                if (string.startsWith("xxspart") && (World.Rnd().nextFloat() < 0.1F) && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(19.9F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F)) {
                    this.debuggunnery("*** Tail1 Spars Broken in Half..");
                    this.msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if ((string.endsWith("li1") || string.endsWith("li2")) && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if ((string.endsWith("ri1") || string.endsWith("ri2")) && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if ((string.endsWith("lm1") || string.endsWith("lm2")) && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if ((string.endsWith("rm1") || string.endsWith("rm2")) && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if ((string.endsWith("lo1") || string.endsWith("lo2")) && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if ((string.endsWith("ro1") || string.endsWith("ro2")) && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (string.endsWith("e1") && (this.getEnergyPastArmor(28F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine1 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if (string.endsWith("e2") && (this.getEnergyPastArmor(28F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine2 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                if (string.startsWith("xxspark1") && (this.chunkDamageVisible("Keel1") > 1) && (this.getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (string.startsWith("xxsparsl") && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (string.startsWith("xxsparsr") && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
            } else if (string.startsWith("xxbomb")) {
                if ((World.Rnd().nextFloat() < 0.01F) && (this.FM.CT.Weapons[3] != null) && this.FM.CT.Weapons[3][0].haveBullets()) {
                    this.debuggunnery("*** Bomb Payload Detonates..");
                    this.FM.AS.hitTank(shot.initiator, 0, 100);
                    this.FM.AS.hitTank(shot.initiator, 1, 100);
                    this.FM.AS.hitTank(shot.initiator, 2, 100);
                    this.FM.AS.hitTank(shot.initiator, 3, 100);
                    this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
            } else if (string.startsWith("xxeng")) {
                int i = 0;
                if (string.startsWith("xxeng2")) {
                    i = 1;
                }
                this.debuggunnery("Engine Module[" + i + "]: Hit..");
                if (string.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 280000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, i);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 100000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (string.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(0.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[i].getCylindersRatio() * 0.66F))) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 1000000F)) {
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                            this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (string.endsWith("eqpt") || (string.endsWith("cyls") && (World.Rnd().nextFloat() < 0.01F))) {
                    if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 4);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 0);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 6);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 1);
                        }
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (string.endsWith("gear")) {
                    if ((this.getEnergyPastArmor(4.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        this.debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                        this.FM.EI.engines[i].setEngineStuck(shot.initiator);
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                }
                if (string.endsWith("mag1") || string.endsWith("mag2")) {
                    this.debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    this.FM.EI.engines[i].setMagnetoKnockOut(shot.initiator, i);
                }
                if (string.endsWith("oil1")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                        this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    }
                    this.FM.AS.hitOil(shot.initiator, i);
                }
                if (string.endsWith("prop") && (this.getEnergyPastArmor(0.42F, shot) > 0.0F)) {
                    this.FM.EI.engines[i].setKillPropAngleDevice(shot.initiator);
                }
                if (string.endsWith("supc") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F)) {
                    this.debuggunnery("Engine Module: Turbine Disabled..");
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 0);
                }
            } else if (string.startsWith("xxtank")) {
                int i = string.charAt(6) - 48;
                switch (i) {
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
            } else if (string.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F) {
                    this.debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
            } else if (string.startsWith("xxmgun02")) {
                this.FM.AS.setJamBullets(0, 0);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (string.startsWith("xxmgun07")) {
                this.FM.AS.setJamBullets(0, 0);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (string.startsWith("xxmgun08")) {
                this.FM.AS.setJamBullets(0, 1);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (string.startsWith("xxmgun09")) {
                this.FM.AS.setJamBullets(0, 2);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (string.startsWith("xxmgun10")) {
                this.FM.AS.setJamBullets(0, 3);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (string.startsWith("xxmgun13")) {
                this.FM.AS.setJamBullets(0, 4);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (string.startsWith("xxmgun14")) {
                this.FM.AS.setJamBullets(0, 5);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (string.startsWith("xxmgun15")) {
                this.FM.AS.setJamBullets(0, 6);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (string.startsWith("xxmgun16")) {
                this.FM.AS.setJamBullets(0, 7);
                this.getEnergyPastArmor(12.7F, shot);
            } else if (string.startsWith("xxcannon")) {
                this.FM.AS.setJamBullets(1, 0);
                this.getEnergyPastArmor(44.7F, shot);
            } else if (string.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (string.startsWith("xxlockr1") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (string.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (string.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (string.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (string.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            } else if (string.startsWith("xxammo0")) {
                int i = string.charAt(7) - 48;
                int i_0_;
                int i_1_;
                switch (i) {
                    default:
                        i_0_ = 0;
                        i_1_ = 0;
                        break;

                    case 2:
                        i_0_ = 10;
                        i_1_ = 0;
                        break;

                    case 3:
                        i_0_ = 11;
                        i_1_ = 0;
                        break;

                    case 4:
                        i_0_ = 11;
                        i_1_ = 1;
                        break;

                    case 5:
                        i_0_ = 12;
                        i_1_ = 0;
                        break;

                    case 6:
                        i_0_ = 12;
                        i_1_ = 1;
                        break;

                    case 7:
                        i_0_ = 0;
                        i_1_ = 0;
                        break;

                    case 8:
                        i_0_ = 0;
                        i_1_ = 1;
                        break;

                    case 9:
                        i_0_ = 0;
                        i_1_ = 2;
                        break;

                    case 10:
                        i_0_ = 0;
                        i_1_ = 3;
                        break;
                }
                if (World.Rnd().nextFloat() < 0.125F) {
                    this.FM.AS.setJamBullets(i_0_, i_1_);
                }
                this.getEnergyPastArmor(4.7F, shot);
            }
        } else if (string.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
            if (World.Rnd().nextFloat() < 0.0575F) {
                if (point3d.y > 0.0D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                } else {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                }
            }
            if (point3d.x > 1.726D) {
                if (point3d.z > 0.444D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
                if ((point3d.z > -0.281D) && (point3d.z < 0.444D)) {
                    if (point3d.y > 0.0D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                    } else {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                    }
                }
                if ((point3d.x > 2.774D) && (point3d.x < 3.718D) && (point3d.z > 0.425D)) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
            }
        } else if (string.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose") < 2) {
                this.hitChunk("Nose", shot);
            }
        } else if (string.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (string.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (string.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 2) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (string.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) {
                this.hitChunk("StabL", shot);
            }
        } else if (string.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) {
                this.hitChunk("StabR", shot);
            }
        } else if (string.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 1) {
                this.hitChunk("VatorL", shot);
            }
        } else if (string.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 1) {
                this.hitChunk("VatorR", shot);
            }
        } else if (string.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) {
                this.hitChunk("WingLIn", shot);
            }
        } else if (string.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) {
                this.hitChunk("WingRIn", shot);
            }
        } else if (string.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) {
                this.hitChunk("WingLMid", shot);
            }
        } else if (string.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) {
                this.hitChunk("WingRMid", shot);
            }
        } else if (string.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) {
                this.hitChunk("WingLOut", shot);
            }
        } else if (string.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) {
                this.hitChunk("WingROut", shot);
            }
        } else if (string.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 1) {
                this.hitChunk("AroneL", shot);
            }
        } else if (string.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 1) {
                this.hitChunk("AroneR", shot);
            }
        } else if (string.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (string.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (string.startsWith("xgear")) {
            if ((World.Rnd().nextFloat() < 0.99F) && this.FM.Gears.isHydroOperable()) {
                this.debuggunnery("*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
                this.gearDamageFX(string);
            }
        } else if (string.startsWith("xturret")) {
            if (string.startsWith("xturret1")) {
                this.FM.AS.setJamBullets(10, 0);
            }
            if (string.endsWith("2b1")) {
                this.FM.AS.setJamBullets(11, 0);
            }
            if (string.endsWith("2b2")) {
                this.FM.AS.setJamBullets(11, 1);
            }
            if (string.endsWith("3b1")) {
                this.FM.AS.setJamBullets(12, 0);
            }
            if (string.endsWith("3b2")) {
                this.FM.AS.setJamBullets(12, 1);
            }
            if (string.endsWith("4a")) {
                this.FM.AS.setJamBullets(13, 1);
            }
            if (string.endsWith("5a")) {
                this.FM.AS.setJamBullets(14, 1);
            }
        } else if (string.startsWith("xpilot") || string.startsWith("xhead")) {
            int i = 0;
            int i_2_;
            if (string.endsWith("a")) {
                i = 1;
                i_2_ = string.charAt(6) - 49;
            } else if (string.endsWith("b")) {
                i = 2;
                i_2_ = string.charAt(6) - 49;
            } else {
                i_2_ = string.charAt(5) - 49;
            }
            this.hitFlesh(i_2_, shot, i);
        }
    }

    private final void doHitMeATank(Shot shot, int i) {
        if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
            if (shot.power < 14100F) {
                if (this.FM.AS.astateTankStates[i] == 0) {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    this.FM.AS.doSetTankState(shot.initiator, i, 1);
                }
                if ((shot.powerType == 3) && (this.FM.AS.astateTankStates[i] > 0) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitTank(shot.initiator, i, 2);
                }
            } else {
                this.FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int) (shot.power / 56000F)));
            }
        }
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -110F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 110F * f, 0.0F);
    }

    protected boolean cutFM(int i, int i_3_, Actor actor) {
        switch (i) {
            case 13:
                this.killPilot(this, 2);
                return false;
        }
        return super.cutFM(i, i_3_, actor);
    }

    public void update(float f) {
        this.kangle0 = (0.95F * this.kangle0) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        if (this.kangle0 > 1.0F) {
            this.kangle0 = 1.0F;
        }
        for (int i = 1; i < 14; i += 2) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * this.kangle0, 0.0F);
        }

        this.kangle1 = (0.95F * this.kangle1) + (0.05F * this.FM.EI.engines[1].getControlRadiator());
        if (this.kangle1 > 1.0F) {
            this.kangle1 = 1.0F;
        }
        for (int i = 1; i < 14; i += 2) {
            this.hierMesh().chunkSetAngles("Waterr" + i + "_D0", 0.0F, -20F * this.kangle1, 0.0F);
        }

        super.update(f);
    }

    private void gearDamageFX(String s) {
        if (s.startsWith("xgearl") || s.startsWith("GearL")) {
            if (this.FM.isPlayers()) {
                HUD.log("Left Gear:  Hydraulic system Failed");
            }
            A_26.kl = World.Rnd().nextFloat();
            A_26.kr = World.Rnd().nextFloat() * A_26.kl;
            A_26.kc = 0.1F;
            this.cutGearCovers("L");
        } else if (s.startsWith("xgearr") || s.startsWith("GearR")) {
            if (this.FM.isPlayers()) {
                HUD.log("Right Gear:  Hydraulic system Failed");
            }
            A_26.kr = World.Rnd().nextFloat();
            A_26.kl = World.Rnd().nextFloat() * A_26.kr;
            A_26.kc = 0.1F;
            this.cutGearCovers("R");
        } else {
            if (this.FM.isPlayers()) {
                HUD.log("Center Gear:  Hydraulic system Failed");
            }
            A_26.kc = World.Rnd().nextFloat();
            A_26.kl = World.Rnd().nextFloat() * A_26.kc;
            A_26.kr = World.Rnd().nextFloat() * A_26.kc;
            this.cutGearCovers("C");
        }
        this.FM.CT.GearControl = 1.0F;
        this.FM.Gears.setHydroOperable(false);
    }

    private void cutGearCovers(String s) {
        Vector3d vector3d = new Vector3d();
        if (World.Rnd().nextFloat() < 0.3F) {
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Gear" + s + 4 + "_D0"));
            wreckage.collide(true);
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            this.hierMesh().chunkVisible("Gear" + s + 4 + "_D0", false);
            Wreckage wreckage1 = new Wreckage(this, this.hierMesh().chunkFind("Gear" + s + 5 + "_D0"));
            wreckage1.collide(true);
            vector3d.set(this.FM.Vwld);
            wreckage1.setSpeed(vector3d);
            this.hierMesh().chunkVisible("Gear" + s + 5 + "_D0", false);
        } else if (World.Rnd().nextFloat() < 0.3F) {
            int i = World.Rnd().nextInt(2) + 4;
            Wreckage wreckage2 = new Wreckage(this, this.hierMesh().chunkFind("Gear" + s + i + "_D0"));
            wreckage2.collide(true);
            vector3d.set(this.FM.Vwld);
            wreckage2.setSpeed(vector3d);
            this.hierMesh().chunkVisible("Gear" + s + i + "_D0", false);
        }
    }

    private float        kangle0;
    private float        kangle1;
    private static float kl = 1.0F;
    private static float kr = 1.0F;
    private static float kc = 1.0F;

    static {
        Class localClass = A_26.class;
        Property.set(localClass, "originCountry", PaintScheme.countryUSA);
    }
}
