package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Baron extends Scheme2 implements TypeScout, TypeTransport {

    public Baron() {
        this.bombBayDoorsRemoved = false;
        this.bChangedExts = false;
        Baron.bChangedPit = true;
        this.fSightSetForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 250F;
        this.fSightCurReadyness = 0.0F;
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

            case 19:
                this.killPilot(this, 2);
                this.killPilot(this, 1);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void onAircraftLoaded() {
        this.FM.AS.wantBeaconsNet(true);
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("default")) {
            this.hierMesh().chunkVisible("Kreslo2_D0", true);
            this.hierMesh().chunkVisible("Kreslo3_D0", true);
            this.hierMesh().chunkVisible("Bale1", false);
            this.hierMesh().chunkVisible("Bale2", false);
        }
        if (this.thisWeaponsName.equals("Cargo")) {
            this.hierMesh().chunkVisible("Kreslo2_D0", false);
            this.hierMesh().chunkVisible("Kreslo3_D0", false);
            this.hierMesh().chunkVisible("Bale1", true);
            this.hierMesh().chunkVisible("Bale2", true);
        }
        if (this.thisWeaponsName.equals("none")) {
            this.hierMesh().chunkVisible("Kreslo2_D0", true);
            this.hierMesh().chunkVisible("Kreslo3_D0", true);
            this.hierMesh().chunkVisible("Bale1", false);
            this.hierMesh().chunkVisible("Bale2", false);
        }
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.3333333F;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, -60F), 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.3F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.3F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 81F), 0.0F);
        hiermesh.chunkSetAngles("GearLH", 0.0F, Aircraft.cvt(f, 0.0F, 0.3F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(f, 0.7F, 0.99F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 81F), 0.0F);
        hiermesh.chunkSetAngles("GearRH", 0.0F, Aircraft.cvt(f, 0.0F, 0.3F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(f, 0.7F, 0.99F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 80F), 0.0F);
    }

    protected void moveGear(float f) {
        this.moveWheelSink();
        Baron.moveGear(this.hierMesh(), f);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        if (this.FM.CT.getGear() > 0.98F) {
            this.hierMesh().chunkSetAngles("GearC22_D0", -15F * f, 0.0F, 0.0F);
        }
        super.moveRudder(f);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxammo0")) {
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setJamBullets(0, World.Rnd().nextInt(0, 5));
                }
                this.getEnergyPastArmor(11.4F, shot);
                return;
            }
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(12.1D / Math.abs(Aircraft.v1.x), shot);
                }
                if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 60F) / Math.abs(Aircraft.v1.x), shot);
                    if (shot.power <= 0.0F) {
                        Aircraft.debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        this.doRicochetBack(shot);
                    }
                }
                if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(12.7F, shot);
                }
                if (s.endsWith("p4")) {
                    this.getEnergyPastArmor(12.699999809265137D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                }
                if (s.endsWith("p5")) {
                    this.getEnergyPastArmor(12.699999809265137D / (Math.abs(Aircraft.v1.z) + 9.9999999747524271E-007D), shot);
                }
                if (s.endsWith("p6")) {
                    this.getEnergyPastArmor(4.0999999046325684D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                }
                if (s.endsWith("p8")) {
                    this.getEnergyPastArmor(9.5299997329711914D / (Math.abs(Aircraft.v1.x) + 9.9999999747524271E-007D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                if (s.length() == 12) {
                    i = 10 + (s.charAt(11) - 48);
                }
                switch (i) {
                    case 8:
                    case 9:
                    default:
                        break;

                    case 1:
                    case 3:
                        if (this.getEnergyPastArmor(3F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Aileron Controls Out..");
                        }
                        break;

                    case 2:
                        this.getEnergyPastArmor(1.5F, shot);
                        break;

                    case 4:
                        if (this.getEnergyPastArmor(1.5F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.debuggunnery("*** Engine1 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            this.debuggunnery("*** Engine1 Prop Controls Out..");
                        }
                        break;

                    case 5:
                        if (this.getEnergyPastArmor(1.5F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            this.debuggunnery("*** Engine2 Throttle Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.15F) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            this.debuggunnery("*** Engine2 Prop Controls Out..");
                        }
                        break;

                    case 6:
                    case 7:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Ailerons Controls Out..");
                        }
                        break;

                    case 10:
                    case 11:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.12F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Evelator Controls Out..");
                        }
                        break;

                    case 12:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.12F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int j = 0;
                if (s.startsWith("xxeng2")) {
                    j = 1;
                }
                this.debuggunnery("Engine Module[" + j + "]: Hit..");
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 280000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 100000F)) {
                            this.debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(0.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[j].getCylindersRatio() * 0.66F))) {
                        this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        this.debuggunnery("Engine Module: Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 1000000F)) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            this.debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("eqpt") || (s.endsWith("cyls") && (World.Rnd().nextFloat() < 0.01F))) {
                    if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        }
                        if (World.Rnd().nextFloat(0.0F, 26700F) < shot.power) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                        }
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("mag1") || s.endsWith("mag2")) {
                    this.debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                    this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, j);
                }
                if (s.endsWith("oil1")) {
                    if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                        this.debuggunnery("Engine Module: Oil Radiator Hit..");
                    }
                    this.FM.AS.hitOil(shot.initiator, j);
                }
                if (s.endsWith("prop") && (this.getEnergyPastArmor(0.42F, shot) > 0.0F)) {
                    this.FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F)) {
                    this.debuggunnery("Engine Module: Turbine Disabled..");
                    this.FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if (s.startsWith("xxlockk1") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlocksl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlocksr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                int k = 0;
                if (s.endsWith("2")) {
                    k = 1;
                }
                if ((this.getEnergyPastArmor(0.21F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.2435F)) {
                    this.FM.AS.hitOil(shot.initiator, k);
                }
                Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Oil Tank Pierced..");
                return;
            }
            if (s.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F) {
                    this.debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if ((s.endsWith("e1") || s.endsWith("e2")) && (this.getEnergyPastArmor(28F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine1 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if ((s.endsWith("e3") || s.endsWith("e4")) && (this.getEnergyPastArmor(28F, shot) > 0.0F)) {
                    this.debuggunnery("*** Engine2 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                if (s.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 1) && (this.getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int i1 = s.charAt(6) - 48;
                switch (i1) {
                    case 1:
                        this.doHitMeATank(shot, 0);
                        break;

                    case 2:
                        this.doHitMeATank(shot, 1);
                        break;

                    case 3:
                        this.doHitMeATank(shot, 2);
                        break;

                    case 4:
                        this.doHitMeATank(shot, 3);
                        break;

                    case 5:
                        this.doHitMeATank(shot, 1);
                        this.doHitMeATank(shot, 2);
                        break;
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
            if (point3d.x > 1.471D) {
                if ((point3d.z > 0.552D) && (point3d.x > 2.37D)) {
                    if (point3d.y > 0.0D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                    } else {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                    }
                }
                if ((point3d.z > 0.0D) && (point3d.z < 0.539D)) {
                    if (point3d.y > 0.0D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                    } else {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                    }
                }
                if ((point3d.x < 2.407D) && (point3d.z > 0.552D)) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
                if ((point3d.x > 2.6D) && (point3d.z > 0.693D)) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                }
                if (World.Rnd().nextFloat() < 0.12F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
            }
        } else if (s.startsWith("xtail")) {
            this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 2) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) {
                this.hitChunk("StabL", shot);
            }
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 1) {
                this.hitChunk("VatorL", shot);
            }
        } else if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 1) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) {
                this.hitChunk("WingLIn", shot);
            }
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) {
                this.hitChunk("WingRIn", shot);
            }
        } else if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) {
                this.hitChunk("WingLMid", shot);
            }
        } else if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) {
                this.hitChunk("WingRMid", shot);
            }
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) {
                this.hitChunk("WingLOut", shot);
            }
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 1) {
                this.hitChunk("AroneL", shot);
            }
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 1) {
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
            if (World.Rnd().nextFloat() < 0.1F) {
                this.debuggunnery("*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
        } else if (s.startsWith("xturret")) {
            if (s.startsWith("xturret1b")) {
                this.FM.AS.setJamBullets(10, 0);
                this.FM.AS.setJamBullets(10, 1);
            }
            if (s.endsWith("2b")) {
                this.FM.AS.setJamBullets(11, 0);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j1;
            if (s.endsWith("a")) {
                byte0 = 1;
                j1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j1 = s.charAt(6) - 49;
            } else {
                j1 = s.charAt(5) - 49;
            }
            this.hitFlesh(j1, shot, byte0);
        }
    }

    private final void doHitMeATank(Shot shot, int i) {
        if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
            if (shot.power < 14100F) {
                if (this.FM.AS.astateTankStates[i] == 0) {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    this.FM.AS.doSetTankState(shot.initiator, i, 1);
                }
                if ((this.FM.AS.astateTankStates[i] > 0) && ((World.Rnd().nextFloat() < 0.02F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.25F)))) {
                    this.FM.AS.hitTank(shot.initiator, i, 2);
                }
            } else {
                this.FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int) (shot.power / 56000F)));
            }
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.AS.bNavLightsOn) {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            this.pos.getAbs(point3d, orient);
            Baron.l.set(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, this.findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            eff3dactor.draw.lightMap().put("light", lightpointactor);
        }
    }

    public void update(float f) {
        super.update(f);
    }

    protected boolean     bombBayDoorsRemoved;
    private static Loc    l           = new Loc();
    public boolean        bChangedExts;
    public static boolean bChangedPit = false;
    public float          fSightSetForwardAngle;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;

    static {
        Class class1 = Baron.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Baron");
        Property.set(class1, "meshName", "3DO/Plane/Baron/hier.him");
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1965.5F);
        Property.set(class1, "FlightModel", "FlightModels/Baron.fmd:Baron_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBaron.class, CockpitBaronCo.class });
        Property.set(class1, "LOSElevation", 0.92575F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 3, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_BombSpawn01", "_ExternalDev00", "_ExternalDev01", "_ExternalDev02" });
    }
}
