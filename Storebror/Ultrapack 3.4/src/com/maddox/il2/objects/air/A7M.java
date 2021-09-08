package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class A7M extends Scheme1 implements TypeFighter {

    public A7M() {
        this.flapps = 0.0F;
        this.curFlaps = 0.0F;
        this.mBar = 0.0F;
        this.desiredPosition = 0.0F;
        this.autoEng = false;
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

    private float flapsMovement(float f, float f1, float f2, float f3, float f4) {
        float f5 = (float) Math.exp(-f / f3);
        float f6 = f1 + ((f2 - f1) * f5);
        if (f6 < f1) {
            f6 += f4 * f;
            if (f6 > f1) {
                f6 = f1;
            }
        } else if (f6 > f1) {
            f6 -= f4 * f;
            if (f6 < f1) {
                f6 = f1;
            }
        }
        return f6;
    }

    public void update(float f) {
        super.update(f);
        float f7 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f7) > 0.01F) {
            this.flapps = f7;
            for (int i = 1; i < 9; i++) {
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * f7, 0.0F);
            }

        }
        if ((this.FM.CT.FlapsControl <= 0.32F) && (this.FM.CT.FlapsControl >= 0.05F)) {
            if ((this == World.getPlayerAircraft()) && !this.autoEng) {
                HUD.log("FlapsAuto");
                this.autoEng = true;
                this.curFlaps = this.FM.CT.getFlap();
            }
            float f2 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed());
            this.mBar = (f2 * f2) / (this.FM.getOverload() * 10F);
            if ((this.mBar < 315F) && (this.FM.getOverload() > 0.0F) && (f2 < 128F)) {
                this.desiredPosition = (315F - this.mBar) / 140F;
                if (this.desiredPosition > 1.0F) {
                    this.desiredPosition = 1.0F;
                }
                if (this.curFlaps < this.desiredPosition) {
                    this.curFlaps = this.flapsMovement(f, this.desiredPosition, this.curFlaps, 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.08F));
                } else {
                    this.curFlaps = this.flapsMovement(f, this.desiredPosition, this.curFlaps, 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.25F));
                }
                this.FM.CT.forceFlaps(this.curFlaps);
            } else {
                this.desiredPosition = 0.0F;
                this.curFlaps = this.flapsMovement(f, this.desiredPosition, this.curFlaps, 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.25F));
                this.FM.CT.forceFlaps(this.curFlaps);
            }
            if (Math.abs(this.desiredPosition - this.curFlaps) >= 0.02F) {
                this.sfxFlaps(true);
            } else {
                this.sfxFlaps(false);
            }
        } else {
            this.autoEng = false;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            for (int i = 0; i < 4; i++) {
                if ((this.FM.AS.astateTankStates[i] > 0) && (this.FM.AS.astateTankStates[i] < 5) && (World.Rnd().nextFloat() < 0.1F)) {
                    this.FM.AS.repairTank(i);
                }
            }

        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("OutAroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("OutAroneR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, -100F * f, 0.0F);
    }

    public void moveWingFold(float f) {
        this.moveWingFold(this.hierMesh(), f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -37F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.15F, 0.85F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.15F, 0.85F, 0.0F, -130F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.15F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.15F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.25F, 0.95F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.25F, 0.95F, 0.0F, -130F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.15F, 0.25F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.15F, 0.25F, 0.0F, 70F), 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.15F, 0.85F, 0.0F, 45F));
        float value = Aircraft.cvt(f, 0.15F, 0.85F, 0.0F, 0.3F);
        Aircraft.xyz[2] = value;
        hiermesh.chunkSetLocate("GearL8_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.25F, 0.95F, 0.0F, -45F));
        value = Aircraft.cvt(f, 0.25F, 0.95F, 0.0F, -0.3F);
        Aircraft.xyz[2] = value;
        hiermesh.chunkSetLocate("GearR8_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveGear(float f) {
        A7M.moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.14F, 0.0F, 0.1318F);
        Aircraft.xyz[1] = f;
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.14F, 0.0F, 0.1318F);
        Aircraft.xyz[1] = f;
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, -20F * f, 0.0F);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -30F * f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, -0.61F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxammo")) {
                if (s.endsWith("wl1")) {
                    if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                        this.FM.AS.setJamBullets(1, 0);
                    }
                    return;
                }
                if (s.endsWith("wl2")) {
                    if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                        this.FM.AS.setJamBullets(1, 1);
                    }
                    return;
                }
                if (s.endsWith("wr1")) {
                    if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                        this.FM.AS.setJamBullets(1, 3);
                    }
                    return;
                }
                if (s.endsWith("wr2")) {
                    if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) {
                        this.FM.AS.setJamBullets(1, 2);
                    }
                    return;
                } else {
                    this.getEnergyPastArmor(24F, shot);
                    return;
                }
            }
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("f1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(21F, 42F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) {
                        this.doRicochetBack(shot);
                    }
                } else if (s.endsWith("f2")) {
                    this.getEnergyPastArmor(12.52D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 2:
                        if ((this.getEnergyPastArmor(0.99F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.15F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Out..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                        if ((this.getEnergyPastArmor(0.99F, shot) <= 0.0F) || (World.Rnd().nextFloat() >= 0.675F)) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                        }
                        break;

                    case 4:
                        if ((this.getEnergyPastArmor(4.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.11F)) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        break;

                    case 5:
                        if ((this.getEnergyPastArmor(0.002F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.02F)) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
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
                if (s.endsWith("oil1")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxgun")) {
                if (this.getEnergyPastArmor(0.75F, shot) > 0.0F) {
                    if (s.endsWith("l1")) {
                        this.FM.AS.setJamBullets(1, 1);
                    }
                    if (s.endsWith("l2")) {
                        this.FM.AS.setJamBullets(1, 0);
                    }
                    if (s.endsWith("r1")) {
                        this.FM.AS.setJamBullets(1, 2);
                    }
                    if (s.endsWith("r2")) {
                        this.FM.AS.setJamBullets(1, 3);
                    }
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                return;
            }
            if (s.startsWith("xxlock")) {
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
                return;
            }
            if (s.startsWith("xxoil")) {
                if ((this.getEnergyPastArmor(0.25F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if (s.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F) {
                    this.debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if (s.startsWith("xxradio")) {
                this.getEnergyPastArmor(3.28F, shot);
                return;
            }
            if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(10.8F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                }
                if (s.startsWith("xxsparri") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(10.8F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(9.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(9.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                }
                if (s.startsWith("xxsparro") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                }
                if (s.startsWith("xxspark") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor((6.8F * World.Rnd().nextFloat(1.0F, 1.5F)) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.86D / Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.15F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.8F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.45F)) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, j, 2);
                        this.FM.AS.doSetTankState(shot.initiator, j, 2);
                    }
                    if ((World.Rnd().nextFloat() < 0.01F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.4F))) {
                        this.FM.AS.hitTank(shot.initiator, j, 4);
                        this.debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf") || s.startsWith("xblister")) {
            this.hitChunk("CF", shot);
            return;
        }
        if (s.startsWith("xeng")) {
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
            int k;
            if (s.endsWith("a")) {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else {
                k = s.charAt(5) - 49;
            }
            this.hitFlesh(k, shot, byte0);
        }
    }

    private float   flapps;
    private float   curFlaps;
    private float   mBar;
    private float   desiredPosition;
    private boolean autoEng;

    static {
        Class class1 = A7M.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
