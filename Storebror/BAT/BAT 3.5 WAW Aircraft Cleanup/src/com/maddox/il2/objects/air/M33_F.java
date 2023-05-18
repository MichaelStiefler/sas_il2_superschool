package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class M33_F extends Scheme1 implements TypeSailPlane {

    public M33_F() {
        this.haveSecondProp = -1;
    }

    public void onAircraftLoaded() {
        BulletEmitter be[] = new BulletEmitter[2];
        super.onAircraftLoaded();
        be[0] = this.getBulletEmitterByHookName("_MGUN01");
        be[1] = this.getBulletEmitterByHookName("_MGUN02");
        int gunCnt = 0;
        for (int i = 0; i < be.length; i++) {
            if (be[i] != GunEmpty.get()) {
                gunCnt++;
            }
        }

        if (gunCnt >= 2) {
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Fio_D0", false);
        } else {
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Fio_D0", true);
        }
    }

    protected void moveFan(float f) {
        if (this.haveSecondProp == -1) {
            try {
                this.hierMesh().chunkFind(Aircraft.Props[1][0]);
                this.haveSecondProp = 1;
            } catch (Exception e) {
                this.haveSecondProp = 0;
            }
        }
        int i = 0;
        for (int j = 0; j < (this.haveSecondProp != 1 ? 1 : 2); j++) {
            if (this.oldProp[j] < 2) {
                i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
                if (i >= 1) {
                    i = 1;
                }
                if ((i != this.oldProp[j]) && this.hierMesh().isChunkVisible(Aircraft.Props[j][this.oldProp[j]])) {
                    this.hierMesh().chunkVisible(Aircraft.Props[j][this.oldProp[j]], false);
                    this.oldProp[j] = i;
                    this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
                }
            }
            if (i == 0) {
                this.propPos[j] = (this.propPos[j] + (57.3F * this.FM.EI.engines[0].getw() * f)) % 360F;
            } else {
                float f1 = 57.3F * this.FM.EI.engines[0].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if (f1 <= 0.5F) {
                    f1 *= 2.0F;
                } else {
                    f1 = (f1 * 2.0F) - 2.0F;
                }
                f1 *= 1200F;
                this.propPos[j] = (this.propPos[j] + (f1 * f)) % 360F;
            }
            if (j == 0) {
                this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, -this.propPos[j], 0.0F);
                this.cockpitFanMoveFan(Aircraft.Props[j][i], 0.0F, -this.propPos[j], 0.0F);
            } else {
                this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, this.propPos[j], 0.0F);
                this.cockpitFanMoveFan(Aircraft.Props[j][i], 0.0F, this.propPos[j], 0.0F);
            }
        }

    }

    void cockpitFanMoveFan(String mesh, float x, float y, float z) {
        if (Config.isUSE_RENDER()) {
            try {
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null) && (mesh.equals("PropRot2_D0") || mesh.equals("Prop2_D0"))) {
                    Main3D.cur3D().cockpits[0].mesh.chunkSetAngles(mesh, x, y, z);
                }
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[1] != null)) {
                    Main3D.cur3D().cockpits[1].mesh.chunkSetAngles(mesh, x, y, z);
                }
            } catch (Exception exception) {
            }
        }
    }

    public static void moveGear(HierMesh hiermesh1, float f1) {
    }

    protected void moveGear(float f1) {
    }

    public void moveWheelSink() {
    }

    public void moveSteering(float f1) {
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++) {
            for (int i_0_ = 0; i_0_ < 2; i_0_++) {
                if (this.FM.Gears.clpGearEff[i][i_0_] != null) {
                    M33_F.tmpp.set(this.FM.Gears.clpGearEff[i][i_0_].pos.getAbsPoint());
                    M33_F.tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][i_0_].pos.setAbs(M33_F.tmpp);
                    this.FM.Gears.clpGearEff[i][i_0_].pos.reset();
                }
            }

        }

        if (this.FM.CT.BrakeControl > 0.4F) {
            this.FM.CT.BrakeControl = 0.4F;
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

    public void moveCockpitDoor(float f) {
        if (!this.hierMesh().isChunkVisible("Fio_D0")) {
            return;
        }
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.7F, 0.9F, 0.0F, 0.22F);
        this.hierMesh().chunkSetLocate("Pilot2_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("Hatch_D0", 0.0F, 60F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[1] != null)) {
                Main3D.cur3D().cockpits[1].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        super.hitBone(s, shot, point3d);
        if (s.startsWith("xgearc")) {
            this.hitChunk("GearC2", shot);
        }
        if (s.startsWith("xgearl")) {
            this.hitChunk("GearL2", shot);
        }
        if (s.startsWith("xgearr")) {
            this.hitChunk("GearR2", shot);
        }
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(32.5F, 65F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) < 0.0F) {
                        this.doRicochetBack(shot);
                    }
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(13.130000114440918D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                } else if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(8.7F, 9.81F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
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
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(6.9600000381469727D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.86F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
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

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -33F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearC11_D0", 0.0F, -30F * f, 0.0F);
    }

    protected int          haveSecondProp;
    private static Point3d tmpp = new Point3d();

    static {
        Class var_class = M33_F.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "M33");
        Property.set(var_class, "meshName", "3DO/Plane/M33_F(Multi1)/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(var_class, "meshName_ja", "3DO/Plane/M33_F(Multi1)/hier.him");
        Property.set(var_class, "PaintScheme_ja", new PaintSchemeFMPar01());
        Property.set(var_class, "yearService", 1932F);
        Property.set(var_class, "yearExpired", 1945.5F);
        int M33FMType = 0;
        try {
            if (FlightModelMain.sectFile("FlightModels/FlightModels/M33.fmd") != null) {
                M33FMType = 33;
            } else if (FlightModelMain.sectFile("FlightModels/A6M2N.fmd") != null) {
                M33FMType = 0;
            }
        } catch (Exception exception) {
        }
        switch (M33FMType) {
            case 33:
                Property.set(var_class, "FlightModel", "FlightModels/FlightModels/M33.fmd");
                System.out.println("M33 Loading FMD: FlightModels/FlightModels/M33.fmd");
                break;

            case 0:
            default:
                Property.set(var_class, "FlightModel", "FlightModels/A6M2N.fmd");
                System.out.println("M33 Loading FMD: FlightModels/A6M2N.fmd");
                break;
        }
        Property.set(var_class, "cockpitClass", new Class[] { CockpitM33_F.class, CockpitM33_F_Front.class });
        Property.set(var_class, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(var_class, new int[2]);
        Aircraft.weaponHooksRegister(var_class, new String[] { "_MGUN01", "_MGUN02" });
    }
}
