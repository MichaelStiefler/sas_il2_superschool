package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class N1K1 extends Scheme1 implements TypeSeaPlane, TypeFighter {

    public N1K1() {
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

    public void update(float f) {
        super.update(f);
        if ((this.FM.CT.FlapsControl <= 0.32F) && (this.FM.CT.FlapsControl >= 0.05F)) {
            if ((this == World.getPlayerAircraft()) && !this.autoEng) {
                HUD.log("FlapsAuto");
                this.autoEng = true;
                this.curFlaps = this.FM.CT.getFlap();
            }
            float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed());
            this.mBar = (f1 * f1) / (this.FM.getOverload() * 10F);
            if ((this.mBar < 315F) && (this.FM.getOverload() > 0.0F) && (f1 < 128F)) {
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
        if (this.FM.CT.BrakeControl > 0.4F) {
            this.FM.CT.BrakeControl = 0.4F;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    N1K1.tmpp.set(((Actor) (this.FM.Gears.clpGearEff[i][j])).pos.getAbsPoint());
                    N1K1.tmpp.z = 0.01D;
                    ((Actor) (this.FM.Gears.clpGearEff[i][j])).pos.setAbs(N1K1.tmpp);
                    ((Actor) (this.FM.Gears.clpGearEff[i][j])).pos.reset();
                }
            }

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

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    protected void moveAileron(float f) {
        if (f > 0.0F) {
            this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -16F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -28.5F * f, 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -16F * f, 0.0F);
            this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -28.5F * f, 0.0F);
        }
    }

    protected void moveElevator(float f) {
        if (f > 0.0F) {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
        } else {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -24.5F * f, 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -24.5F * f, 0.0F);
        }
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -33F * f, 0.0F);
        this.hierMesh().chunkSetAngles("FloatC11_D0", 0.0F, -33F * f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, 0.61F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        super.hitBone(s, shot, point3d);
        if (s.startsWith("xgearc")) {
            this.hitChunk("FloatC2", shot);
        }
        if (s.startsWith("xgearl")) {
            this.hitChunk("FloatL2", shot);
        }
        if (s.startsWith("xgearr")) {
            this.hitChunk("FloatR2", shot);
        }
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(32.5F, 65F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) < 0.0F) {
                        this.doRicochetBack(shot);
                    }
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(13.130000114440918D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                } else if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(8.7F, 9.81F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                }
                return;
            }
            if (s.startsWith("xxcanon0")) {
                int i = s.charAt(8) - 49;
                if (this.getEnergyPastArmor(6.29F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Cannon (" + i + ") Disabled..");
                    this.FM.AS.setJamBullets(1, i);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                switch (j) {
                    default:
                        break;

                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        if ((this.getEnergyPastArmor(0.99F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.175F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Out..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 5:
                    case 6:
                        if ((this.getEnergyPastArmor(0.22F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.275F)) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;

                    case 7:
                        if ((this.getEnergyPastArmor(4.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.175F)) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        break;

                    case 8:
                        if (this.getEnergyPastArmor(3.2F, shot) > 0.0F) {
                            Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 9:
                        if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
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
                if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        }
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("prop") && (this.getEnergyPastArmor(0.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                    this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(2.5F, shot) > 0.0F)) {
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
            if (s.startsWith("xxmgun0")) {
                int k = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(0.75F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
                    this.FM.AS.setJamBullets(0, k);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
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
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.86F / (float) Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if (l > 3) {
                    return;
                }
                if ((this.getEnergyPastArmor(0.8F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.45F)) {
                    if (this.FM.AS.astateTankStates[l] == 0) {
                        this.debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, l, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if ((World.Rnd().nextFloat() < 0.008F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.6F))) {
                        this.FM.AS.hitTank(shot.initiator, l, 1);
                        this.debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf") || s.startsWith("xcock")) {
            this.hitChunk("CF", shot);
            return;
        }
        if (s.startsWith("xeng")) {
            if ((this.chunkDamageVisible("Engine1") < 2) && (this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power)) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            if ((this.getEnergyPastArmor(2.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 5000F) < shot.power)) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if ((this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power)) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if ((this.chunkDamageVisible("Rudder1") < 1) && (this.getEnergyPastArmor(2.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power) && (this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power)) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1) && (this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1) && (this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.getEnergyPastArmor(2.5F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 5000F) < shot.power)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.getEnergyPastArmor(2.5F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 5000F) < shot.power)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid") && (this.getEnergyPastArmor(2.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 4000F) < shot.power)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.startsWith("xwingrmid") && (this.getEnergyPastArmor(2.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 4000F) < shot.power)) {
                this.hitChunk("WingRMid", shot);
            }
            if (s.startsWith("xwinglout") && (this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat(0.0F, 3000F) < shot.power)) {
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

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.CT.Weapons[3] != null) {
            this.hierMesh().chunkVisible("RackL_D0", true);
            this.hierMesh().chunkVisible("RackR_D0", true);
        }
    }

    protected float        flapps;
    protected float        curFlaps;
    protected float        mBar;
    protected float        desiredPosition;
    private boolean        autoEng;
    private static Point3d tmpp = new Point3d();

    static {
        Class class1 = N1K1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Property.set(class1, "iconFar_shortClassName", "N1K");
        Property.set(class1, "meshName", "3DO/Plane/N1K1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/N1K1/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/N1K1.fmd:N1K_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitN1K2JA.class });
        Property.set(class1, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
