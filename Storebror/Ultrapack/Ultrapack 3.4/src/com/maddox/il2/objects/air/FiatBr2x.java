package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class FiatBr2x extends Scheme2 implements TypeBomber, TypeTransport {

    public FiatBr2x() {
        this.airBrakePos = false;
        this.bGunKilled = false;
        this.tGunKilled = false;
        this.noenemy = 0;
        this.bPitUnfocused = true;
        this.bSightAutomation = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
        this.fSightCurReadyness = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
    }

    public void onAircraftLoaded() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 95F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -120F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 95F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -120F * f, 0.0F);
        hiermesh.chunkSetAngles("LandingLights_D0", 0.0F, 90F * f, 0.0F);
    }

    protected void moveGear(float f) {
        FiatBr2x.moveGear(this.hierMesh(), f);
    }

    protected void moveFlap(float f) {
        float f1 = -40F * f;
        this.hierMesh().chunkSetAngles("FlapL_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("FlapR_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("FlapLn_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("FlapRn_D0", 0.0F, f1, 0.0F);
    }

    protected void moveRudder(float f) {
        super.moveRudder(f);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, 0.2F);
        this.hierMesh().chunkSetAngles("TurretBody2A_D0", 0.0F, -30F * f, 0.0F);
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("TurretBase2B_D0", 0.0F, 45F * f, 0.0F);
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("TurretAxle2B_D0", 0.0F, -45F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
            if (this.pk >= 1) {
                this.pk = 1;
            }
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("Prop3_d0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("PropRot3_d0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("Prop3_d0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot1H_D0", false);
                this.hierMesh().chunkVisible("Head1H_D0", false);
                this.hierMesh().chunkVisible("HMask1H_D0", false);
                this.hierMesh().chunkVisible("Pilot1H_D1", true);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.FM.turret[0].bIsOperable = false;
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.FM.turret[2].bIsOperable = false;
                this.tGunKilled = true;
                break;

            case 4:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                this.FM.turret[1].bIsOperable = false;
                this.bGunKilled = true;
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxArmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                } else if (s.endsWith("p3")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                } else if (s.endsWith("p4")) {
                    this.getEnergyPastArmor(9D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 3:
                        if ((this.getEnergyPastArmor(4.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        if ((this.getEnergyPastArmor(0.002F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.11F)) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;

                    case 4:
                    case 5:
                        if ((this.getEnergyPastArmor(0.99F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Out..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxbomb")) {
                if ((World.Rnd().nextFloat() < 0.01F) && (this.FM.CT.Weapons[3] != null) && this.FM.CT.Weapons[3][0].haveBullets()) {
                    this.debuggunnery("*** Bomb Payload Detonates..");
                    this.FM.AS.hitTank(shot.initiator, 0, 100);
                    this.FM.AS.hitTank(shot.initiator, 1, 100);
                    this.FM.AS.hitTank(shot.initiator, 2, 100);
                    this.FM.AS.hitTank(shot.initiator, 3, 100);
                    this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("prop") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F)) {
                    this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine1 Prop Governor Failed..");
                }
                if (s.endsWith("gear") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F)) {
                    this.FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine1 Prop Governor Damaged..");
                }
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 200000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Engine Damaged..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 28000F)) {
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        }
                        if (World.Rnd().nextFloat() < 0.08F) {
                            this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                        }
                        this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Engine Stalled..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 10);
                        Aircraft.debugprintln(this, "*** Engine1 Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                    }
                    this.getEnergyPastArmor(6F, shot);
                }
                if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.72F))) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine1 Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine1 Cylinder Case Broken - Engine Stuck..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine1 Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine1 Supercharger Out..");
                }
                if (s.endsWith("eqpt") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
                        Aircraft.debugprintln(this, "*** Engine1 Magneto Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine1 Compressor Feed Out..");
                    }
                }
                return;
            }
            if (s.startsWith("xxeng2")) {
                if (s.endsWith("prop") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F)) {
                    this.FM.EI.engines[1].setKillPropAngleDevice(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine2 Prop Governor Failed..");
                }
                if (s.endsWith("gear") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F)) {
                    this.FM.EI.engines[1].setKillPropAngleDeviceSpeeds(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine2 Prop Governor Damaged..");
                }
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 200000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 1, 2);
                            Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Engine Damaged..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 28000F)) {
                            this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[1].getCylindersOperable() + "/" + this.FM.EI.engines[1].getCylinders() + " Left..");
                        }
                        if (World.Rnd().nextFloat() < 0.08F) {
                            this.FM.EI.engines[1].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                        }
                        this.FM.EI.engines[1].setReadyness(shot.initiator, this.FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[1].getReadyness() + "..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[1].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Engine Stalled..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.AS.hitEngine(shot.initiator, 1, 10);
                        Aircraft.debugprintln(this, "*** Engine2 Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                    }
                    this.getEnergyPastArmor(6F, shot);
                }
                if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[1].getCylindersRatio() * 1.72F))) {
                    this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine2 Cylinders Hit, " + this.FM.EI.engines[1].getCylindersOperable() + "/" + this.FM.EI.engines[1].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[1].setEngineStuck(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine2 Cylinder Case Broken - Engine Stuck..");
                    }
                    if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 1, 3);
                        Aircraft.debugprintln(this, "*** Engine2 Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if (s.endsWith("supc") && (this.getEnergyPastArmor(0.05F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    this.FM.EI.engines[1].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine2 Supercharger Out..");
                }
                if (s.endsWith("eqpt") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.89F)) {
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[1].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
                        Aircraft.debugprintln(this, "*** Engine2 Magneto Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[1].setKillCompressor(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine2 Compressor Feed Out..");
                    }
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr")) {
                    if ((s.startsWith("xxlockr1") || s.startsWith("xxlockr2")) && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                        this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                    }
                    if ((s.startsWith("xxlockr3") || s.startsWith("xxlockr4")) && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                        this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
                        this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                    }
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvR") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
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
            if (s.startsWith("xxMgun0")) {
                int j = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
                    this.FM.AS.setJamBullets(0, j);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if (s.startsWith("xxoil1")) {
                if ((this.getEnergyPastArmor(0.25F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Radiator 1 Pierced..");
                }
                return;
            }
            if (s.startsWith("xxoil2")) {
                if ((this.getEnergyPastArmor(0.25F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitOil(shot.initiator, 1);
                    this.getEnergyPastArmor(0.22F, shot);
                    this.debuggunnery("Engine Module: Oil Radiator 2 Pierced..");
                }
                return;
            }
            if (s.startsWith("xxprib")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                this.getEnergyPastArmor(4.88F, shot);
                return;
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 48;
                switch (k) {
                    case 1:
                        this.doHitMeATank(shot, 1);
                        this.doHitMeATank(shot, 2);
                        break;

                    case 2:
                        this.doHitMeATank(shot, 1);
                        break;

                    case 3:
                        this.doHitMeATank(shot, 2);
                        break;

                    case 4:
                        this.doHitMeATank(shot, 0);
                        break;

                    case 5:
                        this.doHitMeATank(shot, 3);
                        break;
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
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(7.5F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.2F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            } else {
                if (s.startsWith("xxwater")) {
                    ;
                }
                return;
            }
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            this.hitChunk("CF", shot);
            if (s.startsWith("xcockpit")) {
                if (point3d.x > 2.2D) {
                    if (World.Rnd().nextFloat() < 0.73F) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                    }
                } else if (point3d.y > 0.0D) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                } else {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                }
                if (World.Rnd().nextFloat() < 0.125F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
                if (World.Rnd().nextFloat() < 0.125F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                }
            }
            return;
        }
        if (s.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose") < 2) {
                this.hitChunk("Nose", shot);
                if (World.Rnd().nextFloat() < 0.125F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                }
                if (World.Rnd().nextFloat() < 0.125F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                }
            }
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) {
                this.hitChunk("Keel2", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 1) {
                this.hitChunk("Rudder2", shot);
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
            if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.endsWith("2") && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.startsWith("xhead")) {
                l = s.charAt(5) - 49;
            } else if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else {
                byte0 = 2;
                l = s.charAt(6) - 49;
            }
            this.hitFlesh(l, shot, byte0);
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
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3) {
                if ((this.FM.AS.astateTankStates[1] < 4) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.FM.AS.hitTank(this, 1, 1);
                }
                if ((this.FM.getSpeedKMH() > 200F) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.nextDMGLevel("Keel1_D0", 0, this);
                }
                if ((this.FM.getSpeedKMH() > 200F) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.nextDMGLevel("StabL_D0", 0, this);
                }
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.nextDMGLevel("WingLIn_D0", 0, this);
                }
            }
            if (this.FM.AS.astateEngineStates[1] > 3) {
                if ((this.FM.AS.astateTankStates[2] < 4) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.FM.AS.hitTank(this, 2, 1);
                }
                if ((this.FM.getSpeedKMH() > 200F) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.nextDMGLevel("Keel2_D0", 0, this);
                }
                if ((this.FM.getSpeedKMH() > 200F) && (World.Rnd().nextFloat() < 0.025F)) {
                    this.nextDMGLevel("StabR_D0", 0, this);
                }
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.nextDMGLevel("WingRIn_D0", 0, this);
                }
            }
        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask1H_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask1H_D0", this.hierMesh().isChunkVisible("Pilot1H_D0"));
        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask3_D0", false);
            this.hierMesh().chunkVisible("HMask4_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
            this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
            this.hierMesh().chunkVisible("HMask4_D0", this.hierMesh().isChunkVisible("Pilot4_D0"));
        }
        if (this.hierMesh().chunkFindCheck("HMask3_D0") > 0) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask3_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
            }
        }
        if (this.hierMesh().chunkFindCheck("HMask2_D0") > 0) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask2_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
            }
        }
        if (this.hierMesh().chunkFindCheck("HMask4_D0") > 0) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask4_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask4_D0", this.hierMesh().isChunkVisible("Pilot4_D0"));
            }
        }
        if (!this.bGunKilled && (this.FM.getAltitude() < 250F)) {
            Actor actor = War.GetNearestEnemy(this, 16, 6000F);
            Aircraft aircraft = War.getNearestEnemy(this, 5000F);
            if (((actor != null) && !(actor instanceof BridgeSegment)) || (aircraft != null)) {
                this.noenemy = 0;
                if (this.FM.CT.getCockpitDoor() < 0.01F) {
                    this.wait = World.Rnd().nextInt(0, 30);
                    this.FM.AS.setCockpitDoor(this, 1);
                }
            } else {
                this.noenemy++;
                if ((this.noenemy > (30 + this.wait)) && (this.FM.CT.getCockpitDoor() > 0.99F)) {
                    this.FM.AS.setCockpitDoor(this, 0);
                }
            }
        }
        if (!this.bGunKilled && (this.FM.getAltitude() > 250F)) {
            Actor actor = War.GetNearestEnemy(this, 16, 6000F);
            Aircraft aircraft = War.getNearestEnemy(this, 5000F);
            if (((actor != null) && !(actor instanceof BridgeSegment)) || (aircraft != null)) {
                this.noenemy = 0;
                if (this.FM.CT.AirBrakeControl < 0.01F) {
                    this.wait = World.Rnd().nextInt(0, 30);
                    this.FM.CT.AirBrakeControl = 1.0F;
                }
                if (this.FM.CT.getCockpitDoor() < 0.01F) {
                    this.wait = World.Rnd().nextInt(0, 30);
                    this.FM.AS.setCockpitDoor(this, 1);
                }
            } else {
                this.noenemy++;
                if ((this.noenemy > (30 + this.wait)) && (this.FM.CT.AirBrakeControl > 0.99F)) {
                    this.FM.CT.AirBrakeControl = 0.0F;
                }
                if ((this.noenemy > (30 + this.wait)) && (this.FM.CT.getCockpitDoor() > 0.99F)) {
                    this.FM.AS.setCockpitDoor(this, 0);
                }
            }
        }
        if (!this.tGunKilled && (this.FM.getAltitude() > 250F)) {
            Actor actor = War.GetNearestEnemy(this, 16, 6000F);
            Aircraft aircraft = War.getNearestEnemy(this, 5000F);
            if (((actor != null) && !(actor instanceof BridgeSegment)) || (aircraft != null)) {
                this.noenemy = 0;
                if (this.FM.CT.AirBrakeControl < 0.01F) {
                    this.wait = World.Rnd().nextInt(0, 30);
                    this.FM.CT.AirBrakeControl = 1.0F;
                }
                if (this.FM.CT.getCockpitDoor() < 0.01F) {
                    this.wait = World.Rnd().nextInt(0, 30);
                    this.FM.AS.setCockpitDoor(this, 1);
                }
            } else {
                this.noenemy++;
                if ((this.noenemy > (30 + this.wait)) && (this.FM.CT.AirBrakeControl > 0.99F)) {
                    this.FM.CT.AirBrakeControl = 0.0F;
                }
                if ((this.noenemy > (30 + this.wait)) && (this.FM.CT.getCockpitDoor() > 0.99F)) {
                    this.FM.AS.setCockpitDoor(this, 0);
                }
            }
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        if (i >= 3) {
            this.doRemoveBodyChunkFromPlane("Pilot4");
        }
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("BayR_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayL_D0", 0.0F, 90F * f, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);

            case 3:
                this.FM.AS.setEngineState(this, 0, 0);
                break;

            case 4:
                this.FM.AS.setEngineState(this, 1, 0);
                break;

            case 13:
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        if (this.FM.AS.bIsAboutToBailout) {
            if (this.hierMesh().isChunkVisible("Blister2B_D0")) {
                this.hierMesh().chunkVisible("Blister2B_D0", false);
                this.hierMesh().chunkVisible("Blister2_D0", true);
            }
            if (this.hierMesh().isChunkVisible("Pilot2H_D0")) {
                this.hierMesh().chunkVisible("Pilot2H_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D0", true);
            }
            if (this.hierMesh().isChunkVisible("Pilot1H_D0")) {
                this.hierMesh().chunkVisible("Pilot1H_D0", false);
                this.hierMesh().chunkVisible("Head1H_D0", false);
                this.hierMesh().chunkVisible("HMask1H_D0", false);
            }
        }
        super.update(f);
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.4F;
        if (this.fSightCurForwardAngle > 75F) {
            this.fSightCurForwardAngle = 75F;
        }
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.4F;
        if (this.fSightCurForwardAngle < -15F) {
            this.fSightCurForwardAngle = -15F;
        }
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.5D;
        if (this.thisWeaponsName.startsWith("1x")) {
            if (this.fSightCurSideslip > 40F) {
                this.fSightCurSideslip = 40F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + this.fSightCurSideslip);
        } else {
            if (this.fSightCurSideslip > 10F) {
                this.fSightCurSideslip = 10F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + this.fSightCurSideslip);
        }
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.5D;
        if (this.thisWeaponsName.startsWith("1x")) {
            if (this.fSightCurSideslip < -40F) {
                this.fSightCurSideslip = -40F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + this.fSightCurSideslip);
        } else {
            if (this.fSightCurSideslip < -10F) {
                this.fSightCurSideslip = -10F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + this.fSightCurSideslip);
        }
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 6000F) {
            this.fSightCurAltitude = 6000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 300F) {
            this.fSightCurAltitude = 300F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5F;
        if (this.fSightCurSpeed > 650F) {
            this.fSightCurSpeed = 650F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5F;
        if (this.fSightCurSpeed < 50F) {
            this.fSightCurSpeed = 50F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        double d = (this.fSightCurSpeed / 3.6D) * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
        d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
        this.fSightSetForwardAngle = (float) Math.atan(d / this.fSightCurAltitude);
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
        netmsgguaranted.writeFloat(this.fSightCurForwardAngle);
        netmsgguaranted.writeFloat(this.fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readFloat();
        this.fSightCurSideslip = netmsginput.readFloat();
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        float fTurrMax = (this instanceof FiatBr20M) || (this instanceof FiatBr20ML) ? 20F : 0F;
        switch (i) {
            default:
                break;

            case 0:
                if (f < -35F) {
                    f = -35F;
                    flag = false;
                }
                if (f > 35F) {
                    f = 35F;
                    flag = false;
                }
                if (f1 < -15F) {
                    f1 = -15F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -15F) {
                    f1 = -15F;
                    flag = false;
                }
                if (f1 > fTurrMax) {
                    f1 = fTurrMax;
                    flag = false;
                }
                break;

            case 2:
                if (f < -90F) {
                    f = -90F;
                    flag = false;
                }
                if (f > 90F) {
                    f = 90F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                if (f1 < -2F) {
                    f1 = -2F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public boolean  bPitUnfocused;
    public boolean  bSightAutomation;
    private boolean bDynamoOperational;
    private int     pk;
    private float   dynamoOrient;
    private boolean bDynamoRotary;
    public float    fSightCurDistance;
    public float    fSightCurForwardAngle;
    public float    fSightCurSideslip;
    public float    fSightCurAltitude;
    public float    fSightCurSpeed;
    public float    fSightCurReadyness;
    public float    fSightSetForwardAngle;
    public boolean  airBrakePos;
    private int     wait;
    private int     noenemy;
    boolean         bGunKilled;
    boolean         tGunKilled;

    static {
        Class class1 = FiatBr2x.class;
        Property.set(class1, "originCountry", PaintScheme.countryItaly);
    }
}
