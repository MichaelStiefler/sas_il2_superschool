package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class KI_44 extends Scheme1 implements TypeFighter {
    protected void moveFan(float f) {
        super.moveFan(f);
        this.hierMesh().chunkSetAngles(Aircraft.Props[0][0], 0.0F, this.propPos[0], 0.0F);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.59F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.05F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        for (int i = 1; i < 13; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -32F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        }

        super.update(f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        if (f < 0.01D) {
            f = 0.0F;
        }
        float f1 = Math.max(-f * 1500F, -40F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -55F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 91.807F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 135F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.06F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 4.42F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -91.807F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -135F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.06F, 0.0F, 85F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -4.42F * f, 0.0F);
    }

    protected void moveGear(float f) {
        KI_44.moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.24515F, 0.0F, 0.24515F);
        Aircraft.xyz[1] = f;
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.24515F, 0.0F, 0.24515F);
        Aircraft.xyz[1] = f;
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC5_D0", 0.0F, f, 0.0F);
    }

    protected void moveFlap(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -0.4F * f;
        Aircraft.ypr[0] = -15F * f;
        Aircraft.ypr[1] = 16F * f;
        Aircraft.ypr[2] = -1.8F * f;
        this.hierMesh().chunkSetLocate("Flap01_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = 0.4F * f;
        Aircraft.ypr[0] = 15F * f;
        Aircraft.ypr[1] = -16F * f;
        Aircraft.ypr[2] = -1.8F * f;
        this.hierMesh().chunkSetLocate("Flap02_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 11:
            case 17:
            case 18:
                this.hierMesh().chunkVisible("Wire_D0", false);
                break;

            case 9:
                this.hierMesh().chunkVisible("GearL6_D0", false);
                this.hierMesh().chunkVisible("GearL4_D0", false);
                break;

            case 10:
                this.hierMesh().chunkVisible("GearR6_D0", false);
                this.hierMesh().chunkVisible("GearR4_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1") || s.endsWith("p2")) {
                    this.getEnergyPastArmor(13.13D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                } else if (s.endsWith("g1")) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(32.5F, 65F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) < 0.0F) {
                        this.doRicochetBack(shot);
                    }
                }
            } else if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch (i) {
                    case 1:
                    case 2:
                        if ((this.getEnergyPastArmor(0.99F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.175F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Out..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                        if ((this.getEnergyPastArmor(0.99F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.675F)) {
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            }
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            }
                            if (World.Rnd().nextFloat() < 0.25F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                            }
                        }
                        break;

                    case 4:
                        if ((this.getEnergyPastArmor(0.22F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.02F)) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;

                    case 5:
                        if ((this.getEnergyPastArmor(4.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.11F)) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        break;
                }
            } else if (s.startsWith("xxeng1")) {
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 140000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 85000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - 0.002F);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(5.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 0.75F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("mag1")) {
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Magneto #0 Destroyed..");
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("mag2")) {
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Engine Module: Magneto #1 Destroyed..");
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("prop") && (this.getEnergyPastArmor(0.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                    this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                }
            } else if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                if (s.startsWith("xxlockf")) {
                    this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 3F), shot);
                }
            } else if (s.startsWith("xxmgun0")) {
                int j = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(0.75F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
                    this.FM.AS.setJamBullets(0, j);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else if (s.startsWith("xxcannon0")) {
                int k = s.charAt(9) - 49;
                if (this.getEnergyPastArmor(6.29F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Cannon (" + k + ") Disabled..");
                    this.FM.AS.setJamBullets(1, k);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else if (s.startsWith("xxammo")) {
                if (s.startsWith("xxammo01") && (World.Rnd().nextFloat() < 0.01F)) {
                    this.debuggunnery("Armament: Machine Gun (0) Chain Broken..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.startsWith("xxammo02") && (World.Rnd().nextFloat() < 0.01F)) {
                    this.debuggunnery("Armament: Machine Gun (1) Chain Broken..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                if (s.startsWith("xxammow03")) {
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.debuggunnery("Armament: Cannon Gun (0) Chain Broken..");
                        this.FM.AS.setJamBullets(1, 0);
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.debuggunnery("Armament: Cannon Gun (0) Payload Detonates..");
                        this.FM.AS.hitTank(shot.initiator, 0, 99);
                        this.nextDMGLevels(3, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                    }
                }
                if (s.startsWith("xxammow04")) {
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.debuggunnery("Armament: Cannon Gun (1) Chain Broken..");
                        this.FM.AS.setJamBullets(1, 1);
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.debuggunnery("Armament: Cannon Gun (1) Payload Detonates..");
                        this.FM.AS.hitTank(shot.initiator, 1, 99);
                        this.nextDMGLevels(3, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                    }
                }
                this.getEnergyPastArmor(16F, shot);
            } else if (s.startsWith("xxoiltank")) {
                if ((this.getEnergyPastArmor(0.25F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Tank Pierced..");
                }
            } else if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.45F)) {
                    if (this.FM.AS.astateTankStates[l] == 0) {
                        this.debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, l, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if ((World.Rnd().nextFloat() < 0.01F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.4F))) {
                        this.FM.AS.hitTank(shot.initiator, l, 4);
                        this.debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            } else if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if ((s.endsWith("li1") || s.endsWith("li2")) && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if ((s.endsWith("ri1") || s.endsWith("ri2")) && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if ((s.endsWith("lm1") || s.endsWith("lm2")) && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if ((s.endsWith("rm1") || s.endsWith("rm2")) && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if ((s.endsWith("lo1") || s.endsWith("lo2")) && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if ((s.endsWith("ro1") || s.endsWith("ro2")) && (World.Rnd().nextFloat() < (1.0D - (0.92D * Math.abs(Aircraft.v1.x)))) && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabL Spar Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D" + this.chunkDamageVisible("StabL"), shot.initiator);
                }
                if (s.startsWith("xxsparsr") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** StabR Spar Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D" + this.chunkDamageVisible("StabR"), shot.initiator);
                }
                if (s.startsWith("xxspark") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor((6.8F * World.Rnd().nextFloat(1.0F, 1.5F)) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.86F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            } else if (s.startsWith("xxradio")) {
                this.getEnergyPastArmor(16F, shot);
            } else if (s.startsWith("xxoxy")) {
                this.getEnergyPastArmor(16F, shot);
            }
        } else if (s.startsWith("xcf") || s.startsWith("xblister")) {
            this.hitChunk("CF", shot);
            if (s.startsWith("xblister")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            }
            if ((point3d.x > -0.605D) && (point3d.x < -0.295D)) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            if ((point3d.x > -1.705D) && (point3d.x < -0.492D) && (point3d.z > 0.082D) && (World.Rnd().nextFloat() < 0.5F)) {
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                } else if (World.Rnd().nextFloat() < 0.33F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                } else if (World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                } else {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                }
            }
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
            if (s.startsWith("xstabl")) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr")) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.startsWith("xwingrmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xgear")) {
            if (World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if ((World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i1;
            if (s.endsWith("a")) {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else {
                i1 = s.charAt(5) - 49;
            }
            this.hitFlesh(i1, shot, byte0);
        }
    }

    static {
        Class class1 = KI_44.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
