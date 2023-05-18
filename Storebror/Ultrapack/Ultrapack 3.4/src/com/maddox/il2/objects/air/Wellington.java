package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class Wellington extends Scheme2 {

    public Wellington() {
        this.wheel1 = 0.0F;
        this.wheel2 = 0.0F;
    }

    public void moveCockpitDoor(float f) {
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -120F), 0.0F);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    protected void moveBayDoor(float f) {
        if (this.thisWeaponsName.startsWith("1x4000")) {
            if (f < 0.02F) {
                this.hierMesh().chunkVisible("Bay_Mod423_D0", true);
                this.hierMesh().chunkVisible("Bay1_D0", false);
                this.hierMesh().chunkVisible("Bay5_D0", false);
                this.hierMesh().chunkVisible("Bay6_D0", false);
                this.hierMesh().chunkVisible("Bay10_D0", false);
                this.hierMesh().chunkVisible("Bay11_D0", false);
                this.hierMesh().chunkVisible("Bay15_D0", false);
            } else {
                this.hierMesh().chunkVisible("Bay_Mod423_D0", false);
                this.hierMesh().chunkVisible("Bay1_D0", true);
                this.hierMesh().chunkVisible("Bay5_D0", true);
                this.hierMesh().chunkVisible("Bay6_D0", true);
                this.hierMesh().chunkVisible("Bay10_D0", true);
                this.hierMesh().chunkVisible("Bay11_D0", true);
                this.hierMesh().chunkVisible("Bay15_D0", true);
                this.hierMesh().chunkSetAngles("Bay1_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("Bay6_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("Bay11_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("Bay5_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -89F), 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("Bay10_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -89F), 0.0F, 0.0F);
                this.hierMesh().chunkSetAngles("Bay15_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -89F), 0.0F, 0.0F);
            }
        } else if (f < 0.02F) {
            this.hierMesh().chunkVisible("Bay_D0", true);
            for (int i = 1; i <= 15; i++) {
                this.hierMesh().chunkVisible("Bay" + i + "_D0", false);
                this.hierMesh().chunkVisible("Bay" + i + "_D0", false);
            }

        } else {
            this.hierMesh().chunkVisible("Bay_D0", false);
            for (int j = 1; j <= 15; j++) {
                this.hierMesh().chunkVisible("Bay" + j + "_D0", true);
                this.hierMesh().chunkVisible("Bay" + j + "_D0", true);
            }

            this.hierMesh().chunkSetAngles("Bay1_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay3_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay4_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay6_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay8_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay9_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay11_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay13_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay14_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay2_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay5_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay7_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay10_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay15_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -89F), 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("Bay12_D0", Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -89F), 0.0F, 0.0F);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
                this.hitProp(0, j, actor);
                this.cut("Engine1");
                break;

            case 36:
            case 37:
                this.hitProp(1, j, actor);
                this.cut("Engine2");
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("hmask1_d0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("hmask2_d0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("hmask3_d0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("hmask4_d0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f > 95F) {
                    f = 95F;
                    flag = false;
                }
                if (f < -95F) {
                    f = -95F;
                    flag = false;
                }
                break;

            case 1:
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f > 95F) {
                    f = 95F;
                    flag = false;
                }
                if (f < -95F) {
                    f = -95F;
                    flag = false;
                }
                break;

            case 2:
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                break;

            case 3:
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.1F, 0.75F, 0.0F, 90F));
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 98F));
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, -43F));
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.42F, 0.99F, -15F, -7F));
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 32F));
        hiermesh.chunkSetAngles("GearL8_D0", Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, 80F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL9_D0", Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, -80F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL10_D0", Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, -80F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL11_D0", Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, 80F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 98F));
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, -43F));
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.42F, 0.99F, -15F, -7F));
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 32F));
        hiermesh.chunkSetAngles("GearR8_D0", Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, -80F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR9_D0", Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, 80F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR10_D0", Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, 80F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR11_D0", Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, 80F), 0.0F, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        this.wheel1 = 0.75F * this.wheel1 + 0.25F * this.FM.Gears.gWheelSinking[0];
        this.wheel2 = 0.75F * this.wheel2 + 0.25F * this.FM.Gears.gWheelSinking[1];
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.wheel1, 0.0F, 0.2F, 0.0F, 0.7F);
        this.hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.wheel2, 0.0F, 0.2F, 0.0F, 0.7F);
        this.hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("e1")) this.getEnergyPastArmor(12.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if (s.endsWith("e2")) this.getEnergyPastArmor(12.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if (s.endsWith("p1")) this.getEnergyPastArmor(12.7F, shot);
                if (s.endsWith("p2")) this.getEnergyPastArmor(12.7F, shot);
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < 0.15F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                                this.debuggunnery("*** Engine1 Throttle Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.15F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                                this.debuggunnery("*** Engine1 Prop Controls Out..");
                            }
                            if (World.Rnd().nextFloat() < 0.15F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                                this.debuggunnery("*** Engine1 Mix Controls Out..");
                            }
                        }
                        break;

                    case 2:
                        if (this.getEnergyPastArmor(1.5F, shot) <= 0.0F) break;
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            this.debuggunnery("*** Engine2 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            this.debuggunnery("*** Engine2 Prop Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 7);
                            this.debuggunnery("*** Engine2 Mix Controls Out..");
                        }
                        break;

                    case 3:
                    case 4:
                        if (this.getEnergyPastArmor(6F, shot) <= 0.0F) break;
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;

                    case 5:
                    case 6:
                        if (this.getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Ailerons Controls Out..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int i1 = 0;
                if (s.startsWith("xxeng2")) i1 = 1;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 220000F) this.FM.AS.setEngineStuck(shot.initiator, i1);
                        if (World.Rnd().nextFloat() < shot.power / 54000F) this.FM.AS.hitEngine(shot.initiator, i1, 2);
                    }
                } else if (s.endsWith("cyls1") || s.endsWith("cyls2")) {
                    if (this.getEnergyPastArmor(0.7F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i1].getCylindersRatio() * 0.95F) {
                        this.FM.EI.engines[i1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 2400F)));
                        if (this.FM.AS.astateEngineStates[i1] < 1) {
                            this.FM.AS.hitEngine(shot.initiator, i1, 1);
                            this.FM.AS.doSetEngineState(shot.initiator, i1, 1);
                        }
                        if (World.Rnd().nextFloat() < shot.power / 990000F) this.FM.AS.hitEngine(shot.initiator, i1, 3);
                        this.getEnergyPastArmor(25F, shot);
                    }
                } else if (s.endsWith("supc") && this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) this.FM.AS.setEngineSpecificDamage(shot.initiator, i1, 0);
                if (s.endsWith("oil1") || s.endsWith("oil2") || s.endsWith("oil3")) {
                    if (this.getEnergyPastArmor(0.63F, shot) > 0.0F) this.FM.AS.hitOil(shot.initiator, i1);
                    this.getEnergyPastArmor(0.45F, shot);
                }
            }
            if (s.startsWith("xxmgun")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
                    this.FM.AS.setJamBullets(0, j);
                    this.getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                int k = 0;
                if (s.endsWith("2")) k = 1;
                if (this.getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F) this.FM.AS.hitOil(shot.initiator, k);
                return;
            }
            if (s.startsWith("xxprop1") && this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) if (World.Rnd().nextFloat() < 0.5F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
            else this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
            if (s.startsWith("xxprop2") && this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F) if (World.Rnd().nextFloat() < 0.5F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 3);
            else this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 4);
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                if (s.startsWith("xxspark") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(7.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                this.getEnergyPastArmor(4F, shot);
                return;
            }
            if (s.startsWith("xxstruts")) {
                if (s.startsWith("xxstruts1") && this.chunkDamageVisible("Engine1") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 8F), shot) > 0.0F) this.nextDMGLevels(1, 2, "Engine1_D2", shot.initiator);
                if (s.startsWith("xxstruts2") && this.chunkDamageVisible("Engine2") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 8F), shot) > 0.0F) this.nextDMGLevels(1, 2, "Engine2_D2", shot.initiator);
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.85F) if (shot.power < 14100F) {
                    if (this.FM.AS.astateTankStates[l] == 0) {
                        this.FM.AS.hitTank(shot.initiator, l, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if (this.FM.AS.astateTankStates[l] < 4 && World.Rnd().nextFloat() < 0.11F) this.FM.AS.hitTank(shot.initiator, l, 1);
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.15F) this.FM.AS.hitTank(shot.initiator, l, 10);
                } else this.FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(0, (int) (shot.power / 40000F)));
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (point3d.x > 0.5D) {
                if (point3d.z > 0.913D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                if (point3d.z > 0.341D) {
                    if (point3d.x < 1.402D) {
                        if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                        else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                    } else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                } else if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                if (point3d.x > 1.691D && point3d.x < 1.98D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
        } else if (s.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose") < 3) this.hitChunk("Nose", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 3) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) this.hitChunk("Rudder1", shot);
        else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
        else if (s.startsWith("xstabr")) this.hitChunk("StabR", shot);
        else if (s.startsWith("xvatorl")) this.hitChunk("VatorL", shot);
        else if (s.startsWith("xvatorr")) this.hitChunk("VatorR", shot);
        else if (s.startsWith("xwinglin")) {
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
        } else if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
        else if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
        else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
            this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 45000F));
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
            this.FM.EI.engines[1].setReadyness(shot.initiator, this.FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 45000F));
        } else if (s.startsWith("xgearl")) {
            this.hitChunk("GearL2", shot);
            if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setInternalDamage(shot.initiator, 3);
        } else if (s.startsWith("xgearr")) {
            this.hitChunk("GearR2", shot);
            if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setInternalDamage(shot.initiator, 3);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j1;
            if (s.endsWith("a")) {
                byte0 = 1;
                j1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j1 = s.charAt(6) - 49;
            } else j1 = s.charAt(5) - 49;
            this.hitFlesh(j1, shot, byte0);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f) {
        super.update(f);
        int i = 0;
        float f1 = this.FM.EI.engines[i].getControlRadiator();
        if (Math.abs(this.flapps[i] - f1) > 0.01F) {
            this.flapps[i] = f1;
            for (int j = 1; j < 17; j++)
                this.hierMesh().chunkSetAngles("Radiator" + (j + 16 * i) + "_D0", 0.0F, 0.0F, -30F * f1);

        }
        i = 1;
        if (Math.abs(this.flapps[i] - f1) > 0.01F) {
            this.flapps[i] = f1;
            for (int k = 1; k < 17; k++)
                this.hierMesh().chunkSetAngles("Radiator" + (k + 16 * i) + "_D0", 0.0F, 0.0F, 30F * f1);

        }
    }

    private float wheel1;
    private float wheel2;
    private float flapps[] = { 0.0F, 0.0F };

    static {
        Class class1 = Wellington.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
