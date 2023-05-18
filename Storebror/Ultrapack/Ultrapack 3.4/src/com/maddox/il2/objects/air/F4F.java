package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.AircraftTools;

public abstract class F4F extends Scheme1 implements TypeFighter, TypeTNBFighter {

    public F4F() {
        this.arrestor = 0.0F;
        this.flapps = 0.0F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        xyz[1] = cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
        xyz[2] = cvt(f, 0.01F, 0.99F, 0.0F, -0.05735F);
        this.hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        moveGear(hiermesh, cvt(f, 0.01F, 0.91F, 0.0F, 1.0F), 0, true);
        moveGear(hiermesh, cvt(f, 0.09F, 0.98F, 0.0F, 1.0F), 1, true);
        hiermesh.chunkSetAngles("LampGear_D0", 0.0F, cvt(f, 0.01F, 0.99F, 0.0F, -90F), 0.0F);
    }

    protected static final void moveGear(HierMesh hiermesh, float f, int i, boolean flag) {
        String s = i <= 0 ? "L" : "R";
        hiermesh.chunkSetAngles("Gear" + s + "2_D0", 0.0F, -87F * f, 0.0F);
        hiermesh.chunkSetAngles("Gear" + s + "3_D0", 0.0F, -115.5F * f, 0.0F);
        hiermesh.chunkSetAngles("Gear" + s + "4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("Gear" + s + "5_D0", 0.0F, -88.5F * f, 0.0F);
        if (flag) {
            hiermesh.chunkSetAngles("Gear" + s + "2X_D0", 0.0F, -87F * f, 0.0F);
            hiermesh.chunkSetAngles("Gear" + s + "3X_D0", 0.0F, -115.5F * f, 0.0F);
        }
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveWheelSink() {
        if (this.FM.CT.getGear() > 0.99F) {
            moveGear(this.hierMesh(), cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 1.0F, 0.57F), 0, false);
            moveGear(this.hierMesh(), cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 1.0F, 0.57F), 1, false);
        }
    }

    public void moveArrestorHook(float f) {
        this.resetYPRmodifier();
        xyz[0] = -1.045F * f;
        ypr[1] = -this.arrestor;
        this.hierMesh().chunkSetLocate("Hook1_D0", xyz, ypr);
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, -110F * f, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, -110F * f, 0.0F);
    }

    public void moveWingFold(float f) {
//        if (f < 0.001F) {
//            this.setGunPodsOn(true);
//            this.hideWingWeapons(false);
//        } else {
//            this.setGunPodsOn(false);
//            this.FM.CT.WeaponControl[0] = false;
//            this.hideWingWeapons(true);
//        }
        this.moveWingFold(this.hierMesh(), f);
        AircraftTools.updateExternalWeaponHooks(this);
    }

    public void update(float f) {
        super.update(f);
        float f3 = this.FM.CT.getArrestor();
        float f4 = 81F * f3 * f3 * f3 * f3 * f3 * f3 * f3;
        if (f3 > 0.01F) if (this.FM.Gears.arrestorVAngle != 0.0F) {
            this.arrestor = cvt(this.FM.Gears.arrestorVAngle, -f4, f4, -f4, f4);
            this.moveArrestorHook(f3);
            if (this.FM.Gears.arrestorVAngle >= -81F);
        } else {
            float f1 = 58F * this.FM.Gears.arrestorVSink;
            if (f1 > 0.0F && this.FM.getSpeedKMH() > 60F) Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            this.arrestor += f1;
            if (this.arrestor > f4) this.arrestor = f4;
            if (this.arrestor < -f4) this.arrestor = -f4;
            this.moveArrestorHook(f3);
        }
        float f2 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f2) > 0.01F) {
            this.flapps = f2;
            for (int i = 1; i < 9; i++)
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * f2, 0.0F);

        }
        if (Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) > 72F && this.FM.CT.getFlap() > 0.01D && this.FM.CT.FlapsControl != 0.0F) {
            this.FM.CT.FlapsControl = 0.0F;
            World.cur();
            if (this.FM.actor == World.getPlayerAircraft()) HUD.log("FlapsRaised");
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxammo")) {
                if (s.endsWith("wl1")) {
                    if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) this.FM.AS.setJamBullets(0, 0);
                    return;
                }
                if (s.endsWith("wl3")) {
                    if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) this.FM.AS.setJamBullets(0, 1);
                    return;
                }
                if (s.endsWith("wr4")) {
                    if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) this.FM.AS.setJamBullets(0, 2);
                    return;
                }
                if (s.endsWith("wr2")) {
                    if (World.Rnd().nextFloat(0.0F, 20000F) < shot.power) this.FM.AS.setJamBullets(0, 3);
                    return;
                } else return;
            }
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("f1")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(8F, 12F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F && point3d.y > -0.442D && point3d.y < 0.442D && point3d.z > 0.544D)
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                } else if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(16F, 36F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) this.doRicochetBack(shot);
                } else if (s.endsWith("p2")) this.getEnergyPastArmor(11D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else if (s.endsWith("p3")) this.getEnergyPastArmor(11.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
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
                        if (World.Rnd().nextFloat() < 0.7F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Ailerones Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                        if (this.getEnergyPastArmor(0.99F, shot) <= 0.0F || World.Rnd().nextFloat() >= 0.675F) break;
                        if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                        break;

                    case 4:
                        if (World.Rnd().nextFloat() < 0.95F && this.getEnergyPastArmor(1.27F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;

                    case 5:
                        if (World.Rnd().nextFloat() < 0.08F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 280000F) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < shot.power / 100000F) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (s.endsWith("cyls")) {
                    if (this.getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.66F) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < shot.power / 1000000F) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.startsWith("xxeng1mag")) {
                    int j = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                }
                if (s.endsWith("oil1")) {
                    if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxmgun0")) {
                int k = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
                    this.FM.AS.setJamBullets(0, k);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                if (this.getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
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
            if (s.startsWith("xxradio")) return;
            if (s.startsWith("xxspar")) {
                debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspark") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(6.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(6.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && this.chunkDamageVisible("StabR") > 1 && this.getEnergyPastArmor(6.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    this.debuggunnery("*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.8F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[l] == 0) {
                        this.debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, l, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if (World.Rnd().nextFloat() < 0.07F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F) {
                        this.FM.AS.hitTank(shot.initiator, l, 2);
                        this.debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            this.hitChunk("CF", shot);
            if (s.startsWith("xcf2")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            if (point3d.x > -1.431D && point3d.x < -0.009D) {
                if (point3d.y > 0.0D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            } else this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            return;
        }
        if (s.startsWith("xblister")) {
            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            return;
        }
        if (s.startsWith("xeng")) {
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
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && World.Rnd().nextFloat() < 0.05F) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
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
            } else i1 = s.charAt(5) - 49;
            this.hitFlesh(i1, shot, byte0);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this == World.getPlayerAircraft()) {
            this.FM.Gears.setOperable(true);
            this.FM.Gears.setHydroOperable(false);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    private float         arrestor;
    public static boolean bChangedPit = false;
    private float         flapps;

    static {
        Class class1 = F4F.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
