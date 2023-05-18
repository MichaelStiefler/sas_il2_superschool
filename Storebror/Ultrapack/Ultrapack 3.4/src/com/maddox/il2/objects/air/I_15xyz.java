package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;

public class I_15xyz extends Scheme1 implements TypeFighter, TypeTNBFighter {

    public I_15xyz() {
        this.suspension = 0.0F;
        this.suspR = 0.0F;
        this.suspL = 0.0F;
        this.blisterRemoved = false;
        this.sideDoorOpened = false;
    }

    public void moveCockpitDoor(float f) {
        if (f > 0.01F) this.sideDoorOpened = true;
        else this.sideDoorOpened = false;
        this.hierMesh().chunkSetAngles("blister4_D0", 0.0F, -f * 177.7F, 0.0F);
        this.hierMesh().chunkSetAngles("blister3_D0", 0.0F, -f * 15.6F, 0.0F);
        if (this.FM.getSpeedKMH() > 314F && f < 0.6F && !this.blisterRemoved) this.doRemoveBlisters();
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void hitDaSilk() {
        super.hitDaSilk();
        if (!this.sideDoorOpened && this.FM.AS.bIsAboutToBailout && !this.FM.AS.isPilotDead(0)) {
            this.sideDoorOpened = true;
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        }
    }

    private final void doRemoveBlisters() {
        this.blisterRemoved = true;
        this.FM.CT.bHasCockpitDoorControl = false;
        bChangedPit = true;
        if (this.hierMesh().chunkFindCheck("blister4_D0") != -1) {
            this.hierMesh().hideSubTrees("blister4_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("blister4_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
        if (this.hierMesh().chunkFindCheck("blister3_D0") != -1) {
            this.hierMesh().hideSubTrees("blister3_D0");
            Wreckage wreckage1 = new Wreckage(this, this.hierMesh().chunkFind("blister3_D0"));
            wreckage1.collide(true);
            Vector3d vector3d1 = new Vector3d();
            vector3d1.set(this.FM.Vwld);
            wreckage1.setSpeed(vector3d1);
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public boolean cut(String s) {
        boolean flag = super.cut(s);
        if (s.equalsIgnoreCase("WingLIn")) this.hierMesh().chunkVisible("WingLMid_CAP", true);
        else if (s.equalsIgnoreCase("WingRIn")) this.hierMesh().chunkVisible("WingRMid_CAP", true);
        return flag;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (i == 19) this.FM.Gears.hitCentreGear();
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        this.FM.CT.FlapsControl = 0.0F;
        float f1 = Aircraft.cvt(this.FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, 10F);
        this.hierMesh().chunkSetAngles("Water_D0", 0.0F, f1, 0.0F);
        super.update(f);
    }

    protected void moveFlap(float f) {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        if (hiermesh.chunkFindCheck("SkiR1_D0") != -1) {
            float f1 = 15F;
            float f2 = (float) (Math.random() * 2D) - 1.0F;
            float f3 = (float) (Math.random() * 2D) - 1.0F;
            float f4 = (float) (Math.random() * 2D) - 1.0F;
            float f5 = (float) (Math.random() * 2D) - 1.0F;
            float f6 = f1 / 20F;
            hiermesh.chunkSetAngles("SkiR1_D0", 0.0F, -f1, 0.0F);
            hiermesh.chunkSetAngles("SkiL1_D0", 0.0F, -f1, 0.0F);
            hiermesh.chunkSetAngles("SkiC_D0", 0.0F, f1, 0.0F);
            hiermesh.chunkSetAngles("LSkiFrontDownWire1_d0", 0.0F, -f6 * 4F, f6 * 12.4F);
            hiermesh.chunkSetAngles("LSkiFrontDownWire2_d0", 0.0F, -f6 * 4F, f6 * 12.4F);
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[0] = -0.16F * f6;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            hiermesh.chunkSetLocate("LSkiFrontUpWire_d0", Aircraft.xyz, Aircraft.ypr);
            hiermesh.chunkSetAngles("LWire1_d0", 0.0F, 6.5F * f6 + f6 * -20F * f4, f6 * 60F);
            hiermesh.chunkSetAngles("LWire12_d0", 0.0F, 6.5F * f6 + f6 * 20F * f5, f6 * 70F);
            float f7 = f6 * -5F;
            float f8 = f6 * -10F;
            float f9 = f6 * -15F;
            float f10 = f6 * 5F * f4;
            float f11 = f6 * 10F * f4;
            float f12 = f6 * -5F * f5;
            float f13 = f6 * 5F * f2;
            float f14 = f6 * 10F * f2;
            float f15 = f6 * -5F * f3;
            hiermesh.chunkSetAngles("LWire2_d0", 0.0F, f11, f7);
            hiermesh.chunkSetAngles("LWire3_d0", 0.0F, f10, f8);
            hiermesh.chunkSetAngles("LWire4_d0", 0.0F, f11, f8);
            hiermesh.chunkSetAngles("LWire5_d0", 0.0F, f10, f8);
            hiermesh.chunkSetAngles("LWire6_d0", 0.0F, f11, f9);
            hiermesh.chunkSetAngles("LWire7_d0", 0.0F, f10, f8);
            hiermesh.chunkSetAngles("LWire8_d0", 0.0F, f11, f9);
            hiermesh.chunkSetAngles("LWire9_d0", 0.0F, f10, f7);
            hiermesh.chunkSetAngles("LWire10_d0", 0.0F, f11, f7);
            hiermesh.chunkSetAngles("LWire11_d0", 0.0F, f10, f7);
            hiermesh.chunkSetAngles("LWire13_d0", 0.0F, f12, f8);
            hiermesh.chunkSetAngles("LWire14_d0", 0.0F, f12, f9);
            hiermesh.chunkSetAngles("LWire15_d0", 0.0F, f12, f8);
            hiermesh.chunkSetAngles("LWire16_d0", 0.0F, f12, f9);
            hiermesh.chunkSetAngles("LWire17_d0", 0.0F, 0.0F, f8);
            hiermesh.chunkSetAngles("LWire18_d0", 0.0F, f12, f8);
            hiermesh.chunkSetAngles("LWire19_d0", 0.0F, f12, f8);
            hiermesh.chunkSetAngles("LWire20_d0", 0.0F, f12, f8);
            hiermesh.chunkSetAngles("LWire21_d0", 0.0F, f12, f8);
            hiermesh.chunkSetAngles("LWire22_d0", 0.0F, f12, f8);
            hiermesh.chunkSetAngles("RSkiFrontDownWire1_d0", 0.0F, f6 * 4F, f6 * 12.4F);
            hiermesh.chunkSetAngles("RSkiFrontDownWire2_d0", 0.0F, f6 * 4F, f6 * 12.4F);
            hiermesh.chunkSetLocate("RSkiFrontUpWire_d0", Aircraft.xyz, Aircraft.ypr);
            hiermesh.chunkSetAngles("RWire1_d0", 0.0F, -6.5F * f6 + f6 * -20F * f2, f6 * 60F);
            hiermesh.chunkSetAngles("RWire12_d0", 0.0F, -6.5F * f6 + f6 * 20F * f3, f6 * 70F);
            hiermesh.chunkSetAngles("RWire2_d0", 0.0F, f14, f7);
            hiermesh.chunkSetAngles("RWire3_d0", 0.0F, f13, f8);
            hiermesh.chunkSetAngles("RWire4_d0", 0.0F, f14, f8);
            hiermesh.chunkSetAngles("RWire5_d0", 0.0F, f13, f9);
            hiermesh.chunkSetAngles("RWire6_d0", 0.0F, f14, f8);
            hiermesh.chunkSetAngles("RWire7_d0", 0.0F, f13, f8);
            hiermesh.chunkSetAngles("RWire8_d0", 0.0F, f14, f8);
            hiermesh.chunkSetAngles("RWire9_d0", 0.0F, f13, f7);
            hiermesh.chunkSetAngles("RWire10_d0", 0.0F, f14, f7);
            hiermesh.chunkSetAngles("RWire11_d0", 0.0F, f13, f7);
            hiermesh.chunkSetAngles("RWire13_d0", 0.0F, f15, f8);
            hiermesh.chunkSetAngles("RWire14_d0", 0.0F, f15, f8);
            hiermesh.chunkSetAngles("RWire15_d0", 0.0F, f15, f9);
            hiermesh.chunkSetAngles("RWire16_d0", 0.0F, f15, f8);
            hiermesh.chunkSetAngles("RWire17_d0", 0.0F, 0.0F, f9);
            hiermesh.chunkSetAngles("RWire18_d0", 0.0F, f15, f8);
            hiermesh.chunkSetAngles("RWire19_d0", 0.0F, f15, f8);
            hiermesh.chunkSetAngles("RWire20_d0", 0.0F, f15, f8);
            hiermesh.chunkSetAngles("RWire21_d0", 0.0F, f15, f8);
            hiermesh.chunkSetAngles("RWire22_d0", 0.0F, f15, f8);
        }
    }

    protected void moveGear(float f) {
    }

    public void moveWheelSink() {
        if (this.FM.Gears.onGround()) this.suspension = this.suspension + 0.008F;
        else this.suspension = this.suspension - 0.008F;
        if (this.suspension < 0.0F) {
            this.suspension = 0.0F;
            if (!this.FM.isPlayers()) this.FM.Gears.bTailwheelLocked = true;
        }
        if (this.suspension > 0.1F) this.suspension = 0.1F;
        Aircraft.xyz[0] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        float f = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 25F, 0.0F, 1.0F);
        this.suspL = this.FM.Gears.gWheelSinking[0] * f + this.suspension;
        Aircraft.xyz[1] = -Aircraft.cvt(this.suspL, 0.0F, 0.24F, 0.0F, 0.24F);
        this.hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        this.suspR = this.FM.Gears.gWheelSinking[1] * f + this.suspension;
        Aircraft.xyz[1] = -Aircraft.cvt(this.suspR, 0.0F, 0.24F, 0.0F, 0.24F);
        this.hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if (s.endsWith("p1")) this.getEnergyPastArmor(8.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-006D), shot);
            } else {
                if (s.startsWith("xxcontrols")) {
                    int i = s.charAt(10) - 48;
                    switch (i) {
                        default:
                            break;

                        case 1:
                            if (this.getEnergyPastArmor(2.3F, shot) > 0.0F) {
                                if (World.Rnd().nextFloat() < 0.25F) {
                                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                                    Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                                }
                                if (World.Rnd().nextFloat() < 0.25F) {
                                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                                    Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                                }
                            }
                            // fall through

                        case 2:
                        case 3:
                            if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                                this.FM.AS.setControlsDamage(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                            }
                            break;
                    }
                }
                if (s.startsWith("xxspar")) {
                    Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                    if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(3.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                        this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                    }
                    if (s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                    }
                    if (s.startsWith("xxsparri") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                    }
                    if (s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                    }
                    if (s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                    }
                    if (s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                    }
                    if (s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                        this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                    }
                    if (s.startsWith("xxstabl") && this.getEnergyPastArmor(16.2F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                        this.nextDMGLevels(1, 2, "StabL_D" + this.chunkDamageVisible("StabL"), shot.initiator);
                    }
                    if (s.startsWith("xxstabr") && this.getEnergyPastArmor(16.2F, shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                        this.nextDMGLevels(1, 2, "StabR_D" + this.chunkDamageVisible("StabR"), shot.initiator);
                    }
                }
                if (s.startsWith("xxlock")) {
                    Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                    if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                        Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
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
                }
                if (s.startsWith("xxeng")) {
                    Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                    if (s.endsWith("prop")) {
                        if (this.getEnergyPastArmor(0.45D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F && World.Rnd().nextFloat() < 0.05F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                        }
                    } else if (s.endsWith("case")) {
                        if (this.getEnergyPastArmor(5.1F, shot) > 0.0F) {
                            if (World.Rnd().nextFloat() < shot.power / 175000F) {
                                this.FM.AS.setEngineStuck(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                            }
                            if (World.Rnd().nextFloat() < shot.power / 50000F) {
                                this.FM.AS.hitEngine(shot.initiator, 0, 2);
                                Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                            }
                            this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    } else if (s.startsWith("xxeng1cyls")) {
                        if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.5F, 23.9F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.12F) {
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                            if (World.Rnd().nextFloat() < shot.power / 48000F) {
                                this.FM.AS.hitEngine(shot.initiator, 0, 3);
                                Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                            }
                            if (World.Rnd().nextFloat() < 0.005F) {
                                this.FM.AS.setEngineStuck(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                            }
                            this.getEnergyPastArmor(22.5F, shot);
                        }
                    } else if (s.endsWith("eqpt")) {
                        if (this.getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Engine Module: Magneto 0 Destroyed..");
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                                Aircraft.debugprintln(this, "*** Engine Module: Magneto 1 Destroyed..");
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                                Aircraft.debugprintln(this, "*** Engine Module: Prop Controls Cut..");
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                                Aircraft.debugprintln(this, "*** Engine Module: Throttle Controls Cut..");
                            }
                            if (World.Rnd().nextFloat() < 0.1F) {
                                this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                                Aircraft.debugprintln(this, "*** Engine Module: Mix Controls Cut..");
                            }
                        }
                    } else if (s.endsWith("oil1")) {
                        this.FM.AS.hitOil(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                    }
                }
                if (s.startsWith("xxoil")) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x80);
                }
                if (s.startsWith("xxtank1") && this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    if (this.FM.AS.astateTankStates[0] == 0) {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 0, 2);
                    }
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.999F) {
                        this.FM.AS.hitTank(shot.initiator, 0, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
                if (s.startsWith("xxmgun")) {
                    if (s.endsWith("01")) {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        this.FM.AS.setJamBullets(0, 0);
                    }
                    if (s.endsWith("02")) {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        this.FM.AS.setJamBullets(0, 1);
                    }
                    if (s.endsWith("03")) {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        this.FM.AS.setJamBullets(1, 0);
                    }
                    if (s.endsWith("04")) {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        this.FM.AS.setJamBullets(1, 1);
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
                } else if (s.startsWith("xxeqpt")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
        } else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (s.startsWith("xcf1")) {
                if (point3d.x > -1.147D && point3d.x < -0.869D && point3d.z > 0.653D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                if (World.Rnd().nextFloat() < 0.012F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                if (point3d.x > -1.195D && point3d.x < -0.904D && point3d.z > 0.203D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
            if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 1) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
            if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) this.hitChunk("VatorR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 1) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 1) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else j = s.charAt(5) - 49;
            this.hitFlesh(j, shot, byte0);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
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

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if (explosion.chunkName != null && explosion.power > 0.0F && explosion.chunkName.startsWith("Tail1")) {
            if (World.Rnd().nextFloat(0.0F, 0.038F) < explosion.power) this.FM.AS.setControlsDamage(explosion.initiator, 1);
            if (World.Rnd().nextFloat(0.0F, 0.042F) < explosion.power) this.FM.AS.setControlsDamage(explosion.initiator, 2);
        }
        super.msgExplosion(explosion);
    }

    public static boolean bChangedPit = false;
    private float         suspension;
    public float          suspR;
    public float          suspL;
    public boolean        blisterRemoved;
    private boolean       sideDoorOpened;

}
