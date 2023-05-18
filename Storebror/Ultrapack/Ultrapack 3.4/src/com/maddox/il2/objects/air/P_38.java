package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public abstract class P_38 extends Scheme2a implements TypeFighter, TypeBNZFighter, TypeStormovik {

    public P_38() {
        this.bChangedPit = true;
        this.steera = 0.0F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) this.bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) this.bChangedPit = true;
    }

    protected void moveFlap(float f) {
        this.resetYPRmodifier();
        xyz[1] = 0.3F * f;
        xyz[2] = -0.04835F * f;
        this.hierMesh().chunkSetLocate("Flap01_D0", xyz, ypr);
        float f1 = -25F * f;
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap05_D0", 0.0F, f1, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveAirBrake(float f) {
        this.FM.setGCenter(0.0F - 0.2F * f);
        this.hierMesh().chunkSetAngles("Brake1_D0", 0.0F, -40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake2_D0", 0.0F, -40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake3_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake4_D0", 0.0F, -90F * f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -102F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, -105F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, -140F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, cvt(f, 0.01F, 0.11F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, -40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, cvt(f, 0.01F, 0.11F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearL9_D0", 0.0F, cvt(f, 0.01F, 0.11F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, cvt(f, 0.01F, 0.11F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearR9_D0", 0.0F, cvt(f, 0.01F, 0.11F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearL10_D0", 0.0F, -90F * f, 0.0F);
    }

    protected void moveGear(float f) {
        this.steera = 0.0F;
        this.moveWheelSink();
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.steera = 0.9F * this.steera + 0.1F * cvt(f, -50F, 50F, 50F, -50F);
        this.moveWheelSink();
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.24F, 0.0F, 0.2406F);
        ypr[1] = this.steera;
        this.hierMesh().chunkSetLocate("GearC3_D0", xyz, ypr);
        this.hierMesh().chunkSetAngles("GearC4_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.24F, 0.0F, -50F), 0.0F);
        this.hierMesh().chunkSetAngles("GearC5_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.24F, 0.0F, -105F), 0.0F);
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -0.3206F);
        this.hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -60F), 0.0F);
        this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -117.5F), 0.0F);
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, 0.3206F);
        this.hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, -60F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, -117.5F), 0.0F);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Pilot1_D0", false);
                    this.hierMesh().chunkVisible("Head1_D0", false);
                    this.hierMesh().chunkVisible("Pilot1_D1", true);
                }
                break;
        }
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("Water1_D0", 0.0F, 18F - 36F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        this.hierMesh().chunkSetAngles("Water2_D0", 0.0F, 18F - 36F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        this.hierMesh().chunkSetAngles("Water3_D0", 0.0F, 18F - 36F * this.FM.EI.engines[1].getControlRadiator(), 0.0F);
        this.hierMesh().chunkSetAngles("Water4_D0", 0.0F, 18F - 36F * this.FM.EI.engines[1].getControlRadiator(), 0.0F);
        if (this.FM.CT.getFlap() < 0.25F * this.FM.CT.getAirBrake()) this.FM.setFlapsShift(0.25F * this.FM.CT.getAirBrake());
        super.update(f);
        if (this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F) {
            RealFlightModel realflightmodel = (RealFlightModel) this.FM;
            if (realflightmodel.RealMode && realflightmodel.indSpeed > 135F) {
                float f1 = 1.0F + 0.005F * (135F - realflightmodel.indSpeed);
                if (f1 < 0.0F) f1 = 0.0F;
                this.FM.SensPitch = 0.63F * f1;
                if (realflightmodel.indSpeed > 155F) this.FM.producedAM.y -= 6000F * (155F - realflightmodel.indSpeed);
            } else this.FM.SensPitch = 0.63F;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F), shot);
                    debugprintln(this, "*** Armor Glass: Hit..");
                    if (shot.power <= 0.0F) {
                        debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        if (World.Rnd().nextFloat() < 0.5F) this.doRicochetBack(shot);
                    }
                }
                if (s.endsWith("2")) this.getEnergyPastArmor(12.7F / (1E-005F + (float) Math.abs(v1.x)), shot);
                if (s.endsWith("3")) this.getEnergyPastArmor(12.7F / (1E-005F + (float) Math.abs(v1.x)), shot);
                if (s.endsWith("4")) this.getEnergyPastArmor(8.9F / (1E-005F + (float) Math.abs(v1.x)), shot);
                if (s.endsWith("5")) this.getEnergyPastArmor(8.9F / (1E-005F + (float) Math.abs(v1.z)), shot);
                return;
            }
            if (s.startsWith("xxarcon")) {
                if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                    debugprintln(this, "*** Ailerones Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxvatcon")) {
                if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                    debugprintln(this, "*** Elevators Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxrudcon")) {
                if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    debugprintln(this, "*** Rudder Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (this.getEnergyPastArmor(4.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.75F) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 6800F)));
                    debugprintln(this, "*** Engine 0 Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (this.FM.AS.astateEngineStates[0] < 1) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                    }
                    if (World.Rnd().nextFloat() < shot.power / 48000F) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        debugprintln(this, "*** Engine 0 Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                return;
            }
            if (s.startsWith("xxeng2")) {
                if (this.getEnergyPastArmor(4.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[1].getCylindersRatio() * 0.75F) {
                    this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 6800F)));
                    debugprintln(this, "*** Engine 1 Cylinders Hit, " + this.FM.EI.engines[1].getCylindersOperable() + "/" + this.FM.EI.engines[1].getCylinders() + " Left..");
                    if (this.FM.AS.astateEngineStates[1] < 1) {
                        this.FM.AS.hitEngine(shot.initiator, 1, 1);
                        this.FM.AS.doSetEngineState(shot.initiator, 1, 1);
                    }
                    if (World.Rnd().nextFloat() < shot.power / 48000F) {
                        this.FM.AS.hitEngine(shot.initiator, 1, 3);
                        debugprintln(this, "*** Engine 1 Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                return;
            }
            if (s.startsWith("xxoilradiat1")) {
                if (this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    debugprintln(this, "*** Engine Module 0: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoilradiat2")) {
                if (this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.hitOil(shot.initiator, 1);
                    debugprintln(this, "*** Engine Module 1: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoiltank1")) {
                if (this.getEnergyPastArmor(2.38F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    debugprintln(this, "*** Engine Module 0: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoiltank2")) {
                if (this.getEnergyPastArmor(2.38F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitOil(shot.initiator, 1);
                    debugprintln(this, "*** Engine Module 1: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxmagneto1")) {
                int i = World.Rnd().nextInt(0, 1);
                this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                debugprintln(this, "*** Engine Module 0: Magneto " + i + " Destroyed..");
                return;
            }
            if (s.startsWith("xxmagneto2")) {
                int j = World.Rnd().nextInt(0, 1);
                this.FM.EI.engines[1].setMagnetoKnockOut(shot.initiator, j);
                debugprintln(this, "*** Engine Module 1: Magneto " + j + " Destroyed..");
                return;
            }
            if (s.startsWith("xxturbo1")) {
                if (this.getEnergyPastArmor(1.23F, shot) > 0.0F) {
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                    debugprintln(this, "*** Engine Module 0: Supercharger Destroyed..");
                }
                return;
            }
            if (s.startsWith("xxturbo2")) {
                if (this.getEnergyPastArmor(1.23F, shot) > 0.0F) {
                    this.FM.EI.engines[1].setKillCompressor(shot.initiator);
                    debugprintln(this, "*** Engine Module 1: Supercharger Destroyed..");
                }
                return;
            }
            if (s.startsWith("xxradiat")) {
                int k = 0;
                if (s.endsWith("3") || s.endsWith("4")) k = 1;
                if (World.Rnd().nextFloat() < 0.25F) if (this.FM.AS.astateEngineStates[k] == 0) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, k, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, k, 1);
                } else if (this.FM.AS.astateEngineStates[k] == 1) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, k, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, k, 2);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                byte byte0 = 0;
                int j1 = s.charAt(6) - 48;
                switch (j1) {
                    case 1:
                        byte0 = 1;
                        break;

                    case 2:
                        byte0 = 1;
                        break;

                    case 3:
                        byte0 = 0;
                        break;

                    case 4:
                        byte0 = 2;
                        break;

                    case 5:
                        byte0 = 2;
                        break;

                    case 6:
                        byte0 = 3;
                        break;
                }
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitTank(shot.initiator, byte0, 1);
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F) this.FM.AS.hitTank(shot.initiator, byte0, 2);
                }
                return;
            }
            if (s.startsWith("xxgun")) {
                int l = s.charAt(5) - 49;
                this.FM.AS.setJamBullets(0, l);
                this.getEnergyPastArmor(23.5F, shot);
                return;
            }
            if (s.startsWith("xxcannon")) {
                this.FM.AS.setJamBullets(1, 0);
                this.getEnergyPastArmor(48.6F, shot);
                return;
            }
            if (s.startsWith("xxammogun")) {
                int i1 = World.Rnd().nextInt(0, 3);
                this.FM.AS.setJamBullets(0, i1);
                this.getEnergyPastArmor(23.5F, shot);
                return;
            }
            if (s.startsWith("xxammocan")) {
                this.FM.AS.setJamBullets(1, 0);
                this.getEnergyPastArmor(23.5F, shot);
                return;
            }
            if (s.startsWith("xxspar")) {
                debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxpark1") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxpark2") && this.chunkDamageVisible("Keel2") > 1 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Keel2 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel2_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Stab Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                debugprintln(this, "*** Lock Construction: Hit..");
                if ((s.startsWith("xxlockk1") || s.startsWith("xxlockk2")) && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if ((s.startsWith("xxlockk3") || s.startsWith("xxlockk4")) && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Rudder2 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if (s.startsWith("xxlocksl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Vator Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) this.hitChunk("CF", shot);
        if (!s.startsWith("xblister")) if (s.startsWith("xengine1")) this.hitChunk("Engine1", shot);
        else if (s.startsWith("xengine2")) this.hitChunk("Engine2", shot);
        else if (s.startsWith("xtail1")) this.hitChunk("Tail1", shot);
        else if (s.startsWith("xtail2")) this.hitChunk("Tail2", shot);
        else if (s.startsWith("xkeel1")) this.hitChunk("Keel1", shot);
        else if (s.startsWith("xkeel2")) this.hitChunk("Keel2", shot);
        else if (s.startsWith("xrudder1")) this.hitChunk("Rudder1", shot);
        else if (s.startsWith("xrudder2")) this.hitChunk("Rudder2", shot);
        else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
        else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte1 = 0;
            int k1;
            if (s.endsWith("a")) {
                byte1 = 1;
                k1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte1 = 2;
                k1 = s.charAt(6) - 49;
            } else k1 = s.charAt(5) - 49;
            this.hitFlesh(k1, shot, byte1);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        Vector3d vector3d = new Vector3d();
        switch (i) {
            case 11:
                this.hierMesh().chunkVisible("Wire_D0", false);
                break;

            case 12:
                this.hierMesh().chunkVisible("Wire_D0", false);
                break;

            case 17:
                this.FM.cut(17, j, actor);
                this.FM.cut(18, j, actor);
                break;

            case 31:
                this.FM.cut(31, j, actor);
                this.FM.cut(32, j, actor);
                break;

            case 33:
                Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Flap03_D0"));
                wreckage.collide(false);
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
                this.hierMesh().chunkVisible("Flap03_D0", false);
                this.FM.cut(19, j, actor);
                this.FM.cut(17, j, actor);
                this.FM.cut(18, j, actor);
                this.FM.cut(31, j, actor);
                this.FM.cut(32, j, actor);
                // fall through

            case 34:
                Wreckage wreckage1 = new Wreckage(this, this.hierMesh().chunkFind("Flap02_D0"));
                wreckage1.collide(false);
                vector3d.set(this.FM.Vwld);
                wreckage1.setSpeed(vector3d);
                this.hierMesh().chunkVisible("Flap02_D0", false);
                break;

            case 36:
                this.FM.cut(20, j, actor);
                Wreckage wreckage2 = new Wreckage(this, this.hierMesh().chunkFind("Flap05_D0"));
                wreckage2.collide(false);
                vector3d.set(this.FM.Vwld);
                wreckage2.setSpeed(vector3d);
                this.hierMesh().chunkVisible("Flap05_D0", false);
                this.FM.cut(17, j, actor);
                this.FM.cut(18, j, actor);
                this.FM.cut(31, j, actor);
                this.FM.cut(32, j, actor);
                // fall through

            case 37:
                Wreckage wreckage3 = new Wreckage(this, this.hierMesh().chunkFind("Flap04_D0"));
                wreckage3.collide(false);
                vector3d.set(this.FM.Vwld);
                wreckage3.setSpeed(vector3d);
                this.hierMesh().chunkVisible("Flap04_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean bChangedPit;
    private float  steera;

    static {
        Class class1 = P_38.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
