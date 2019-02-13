package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class LANCASTER extends Scheme4 implements TypeTransport, TypeBomber {

    public LANCASTER() {
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 800F, -50F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearC99_D0", 0.0F, 14F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 55F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 55F * f, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", -f, 0.0F, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 25: // '\031'
                super.FM.turret[0].bIsOperable = false;
                break;

            case 26: // '\032'
                super.FM.turret[1].bIsOperable = false;
                break;

            case 27: // '\033'
                super.FM.turret[2].bIsOperable = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, 65F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, 65F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        int i = 0;
        if (s.startsWith("xx")) {
            if (s.startsWith("xxammo")) {
                int j = s.charAt(6) - 48;
                if (s.length() > 7) {
                    j = 10;
                }
                if ((this.getEnergyPastArmor(6.87F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.05F)) {
                    switch (j) {
                        case 1: // '\001'
                            j = 10;
                            i = 0;
                            break;

                        case 2: // '\002'
                            j = 10;
                            i = 1;
                            break;

                        case 3: // '\003'
                            j = 11;
                            i = 0;
                            break;

                        case 4: // '\004'
                            j = 11;
                            i = 1;
                            break;

                        case 5: // '\005'
                            j = 12;
                            i = 0;
                            break;

                        case 6: // '\006'
                            j = 12;
                            i = 1;
                            break;

                        case 7: // '\007'
                            j = 13;
                            i = 0;
                            break;

                        case 8: // '\b'
                            j = 14;
                            i = 0;
                            break;

                        case 9: // '\t'
                            j = 15;
                            i = 0;
                            break;

                        case 10: // '\n'
                            j = 15;
                            i = 1;
                            break;
                    }
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(j, i);
                    return;
                }
            }
            if (s.startsWith("xxcontrols")) {
                int k = s.charAt(10) - 48;
                switch (k) {
                    default:
                        break;

                    case 1: // '\001'
                    case 2: // '\002'
                        if ((this.getEnergyPastArmor(1.0F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                        }
                        break;

                    case 3: // '\003'
                        if ((World.Rnd().nextFloat() < 0.125F) && (this.getEnergyPastArmor(5.2F, shot) > 0.0F)) {
                            Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                        }
                        this.getEnergyPastArmor(2.0F, shot);
                        break;

                    case 4: // '\004'
                    case 5: // '\005'
                    case 6: // '\006'
                        if ((World.Rnd().nextFloat() < 0.252F) && (this.getEnergyPastArmor(5.2F, shot) > 0.0F)) {
                            if (World.Rnd().nextFloat() < 0.125F) {
                                ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                            }
                            if (World.Rnd().nextFloat() < 0.125F) {
                                ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                            }
                        }
                        this.getEnergyPastArmor(2.0F, shot);
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int l = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 140000F)) {
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, l);
                            Aircraft.debugprintln(this, "*** Engine (" + l + ") Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 85000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, l, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + l + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.005F) {
                        ((FlightModelMain) (super.FM)).EI.engines[l].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        ((FlightModelMain) (super.FM)).EI.engines[l].setReadyness(shot.initiator, ((FlightModelMain) (super.FM)).EI.engines[l].getReadyness() - 0.00082F);
                        Aircraft.debugprintln(this, "*** Engine (" + l + ") Crank Case Hit - Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[l].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(5.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (((FlightModelMain) (super.FM)).EI.engines[l].getCylindersRatio() * 0.75F))) {
                        ((FlightModelMain) (super.FM)).EI.engines[l].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine (" + l + ") Cylinders Hit, " + ((FlightModelMain) (super.FM)).EI.engines[l].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[l].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 18000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, l, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + l + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("mag1")) {
                    ((FlightModelMain) (super.FM)).EI.engines[l].setMagnetoKnockOut(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Magneto #0 Destroyed..");
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("mag2")) {
                    ((FlightModelMain) (super.FM)).EI.engines[l].setMagnetoKnockOut(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Magneto #1 Destroyed..");
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("oil1") && (this.getEnergyPastArmor(0.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                    ((FlightModelMain) (super.FM)).AS.setOilState(shot.initiator, l, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Oil Filter Pierced..");
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr1") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockr2") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                int i1 = s.charAt(5) - 49;
                if ((this.getEnergyPastArmor(0.21F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.2435F)) {
                    ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, i1);
                }
                Aircraft.debugprintln(this, "*** Engine (" + i1 + ") Module: Oil Tank Pierced..");
                return;
            }
            if (s.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F) {
                    this.debuggunnery("Pneumo System: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if (s.startsWith("xxradio")) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 25F), shot);
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspark1") && (this.chunkDamageVisible("Keel1") > 1) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                if (s.startsWith("xxspark2") && (this.chunkDamageVisible("Keel2") > 1) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel2 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel2_D" + this.chunkDamageVisible("Keel2"), shot.initiator);
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
                int j1 = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.06F, shot) > 0.0F) {
                    if (((FlightModelMain) (super.FM)).AS.astateTankStates[j1] == 0) {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j1, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, j1, 1);
                    }
                    if (shot.powerType == 3) {
                        if (shot.power < 16100F) {
                            if ((((FlightModelMain) (super.FM)).AS.astateTankStates[j1] < 4) && (World.Rnd().nextFloat() < 0.21F)) {
                                ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j1, 1);
                            }
                        } else {
                            ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j1, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                        }
                    } else if (shot.power > 16100F) {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j1, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                    }
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
            return;
        }
        if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
            return;
        }
        if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
            return;
        }
        if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) {
                this.hitChunk("Keel2", shot);
            }
            return;
        }
        if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
            return;
        }
        if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 1) {
                this.hitChunk("Rudder2", shot);
            }
            return;
        }
        if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) {
                this.hitChunk("StabL", shot);
            }
            return;
        }
        if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) {
                this.hitChunk("StabR", shot);
            }
            return;
        }
        if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) {
                this.hitChunk("WingLIn", shot);
            }
            return;
        }
        if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) {
                this.hitChunk("WingRIn", shot);
            }
            return;
        }
        if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) {
                this.hitChunk("WingLMid", shot);
            }
            return;
        }
        if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) {
                this.hitChunk("WingRMid", shot);
            }
            return;
        }
        if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) {
                this.hitChunk("WingLOut", shot);
            }
            return;
        }
        if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) {
                this.hitChunk("WingROut", shot);
            }
            return;
        }
        if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 1) {
                this.hitChunk("AroneL", shot);
            }
            return;
        }
        if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 1) {
                this.hitChunk("AroneR", shot);
            }
            return;
        }
        if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
            return;
        }
        if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
            return;
        }
        if (s.startsWith("xengine3")) {
            if (this.chunkDamageVisible("Engine3") < 2) {
                this.hitChunk("Engine3", shot);
            }
            return;
        }
        if (s.startsWith("xengine4")) {
            if (this.chunkDamageVisible("Engine4") < 2) {
                this.hitChunk("Engine4", shot);
            }
            return;
        }
        if (s.startsWith("xgear")) {
            if (World.Rnd().nextFloat() < 0.05F) {
                Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                ((FlightModelMain) (super.FM)).Gears.setHydroOperable(false);
            }
            return;
        }
        if (s.startsWith("xturret")) {
            return;
        }
        if (s.startsWith("xmgun")) {
            int k1 = (10 * (s.charAt(5) - 48)) + (s.charAt(6) - 48);
            if ((this.getEnergyPastArmor(6.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.35F)) {
                switch (k1) {
                    case 1: // '\001'
                        k1 = 10;
                        i = 0;
                        break;

                    case 2: // '\002'
                        k1 = 10;
                        i = 1;
                        break;

                    case 3: // '\003'
                        k1 = 11;
                        i = 0;
                        break;

                    case 4: // '\004'
                        k1 = 11;
                        i = 1;
                        break;

                    case 5: // '\005'
                        k1 = 12;
                        i = 0;
                        break;

                    case 6: // '\006'
                        k1 = 12;
                        i = 1;
                        break;

                    case 7: // '\007'
                        k1 = 13;
                        i = 0;
                        break;

                    case 8: // '\b'
                        k1 = 14;
                        i = 0;
                        break;

                    case 9: // '\t'
                        k1 = 15;
                        i = 0;
                        break;

                    case 10: // '\n'
                        k1 = 15;
                        i = 1;
                        break;
                }
                ((FlightModelMain) (super.FM)).AS.setJamBullets(k1, i);
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l1;
            if (s.endsWith("a")) {
                byte0 = 1;
                l1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l1 = s.charAt(6) - 49;
            } else {
                l1 = s.charAt(5) - 49;
            }
            this.hitFlesh(l1, shot, byte0);
            return;
        } else {
            return;
        }
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if ((explosion.chunkName != null) && (explosion.power > 0.0F)) {
            if (explosion.chunkName.equals("Tail1_D3")) {
                return;
            }
            if (explosion.chunkName.equals("WingLIn_D3")) {
                return;
            }
            if (explosion.chunkName.equals("WingRIn_D3")) {
                return;
            }
            if (explosion.chunkName.equals("WingLMid_D3")) {
                return;
            }
            if (explosion.chunkName.equals("WingRMid_D3")) {
                return;
            }
            if (explosion.chunkName.equals("WingLOut_D3")) {
                return;
            }
            if (explosion.chunkName.equals("WingROut_D3")) {
                return;
            }
        }
        super.msgExplosion(explosion);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("Engine1") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("Engine2") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("Engine3") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 2, 1);
        }
        if (shot.chunkName.startsWith("Engine4") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 3, 1);
        }
        if (shot.chunkName.startsWith("CF")) {
            if ((((Tuple3d) (Aircraft.Pd)).x > 4.5500001907348633D) && (((Tuple3d) (Aircraft.Pd)).x < 7.1500000953674316D) && (((Tuple3d) (Aircraft.Pd)).z > 0.57999998331069946D)) {
                if (World.Rnd().nextFloat() < 0.233F) {
                    if (((Tuple3d) (Aircraft.Pd)).z > 1.2100000381469727D) {
                        this.killPilot(shot.initiator, 0);
                        if ((shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                            HUD.logCenter("H E A D S H O T");
                        }
                    } else {
                        ((FlightModelMain) (super.FM)).AS.hitPilot(shot.initiator, 0, (int) (shot.power * 0.004F));
                    }
                }
                if (World.Rnd().nextFloat() < 0.233F) {
                    if (((Tuple3d) (Aircraft.Pd)).z > 1.2100000381469727D) {
                        this.killPilot(shot.initiator, 1);
                        if ((shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                            HUD.logCenter("H E A D S H O T");
                        }
                    } else {
                        ((FlightModelMain) (super.FM)).AS.hitPilot(shot.initiator, 1, (int) (shot.power * 0.004F));
                    }
                }
            }
            if ((((Tuple3d) (Aircraft.Pd)).x > 9.5299997329711914D) && (((Tuple3d) (Aircraft.Pd)).z < 0.14000000059604645D) && (((Tuple3d) (Aircraft.Pd)).z > -0.62999999523162842D)) {
                ((FlightModelMain) (super.FM)).AS.hitPilot(shot.initiator, 2, (int) (shot.power * 0.002F));
            }
            if ((((Tuple3d) (Aircraft.Pd)).x > 2.4749999046325684D) && (((Tuple3d) (Aircraft.Pd)).x < 4.4899997711181641D) && (((Tuple3d) (Aircraft.Pd)).z > 0.61000001430511475D) && ((shot.power * Math.sqrt((((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y) + (((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z))) > 11900D) && (World.Rnd().nextFloat() < 0.45F)) {
                for (int i = 0; i < 4; i++) {
                    ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, i, 0);
                }

            }
        }
        if (shot.chunkName.startsWith("Turret1")) {
            if (((Tuple3d) (Aircraft.Pd)).z > 0.033449999988079071D) {
                this.killPilot(shot.initiator, 2);
                if ((shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                    HUD.logCenter("H E A D S H O T");
                }
            } else {
                ((FlightModelMain) (super.FM)).AS.hitPilot(shot.initiator, 2, (int) (shot.power * 0.004F));
            }
            shot.chunkName = "CF_D" + this.chunkDamageVisible("CF");
        }
        if (shot.chunkName.startsWith("Turret2")) {
            if (World.Rnd().nextBoolean()) {
                ((FlightModelMain) (super.FM)).AS.hitPilot(shot.initiator, 4, (int) (shot.power * 0.004F));
            } else {
                super.FM.turret[1].bIsOperable = false;
            }
        }
        if (shot.chunkName.startsWith("Turret3")) {
            if (((Tuple3d) (Aircraft.Pd)).z > 0.30445000529289246D) {
                this.killPilot(shot.initiator, 7);
                if ((shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                    HUD.logCenter("H E A D S H O T");
                }
            } else {
                ((FlightModelMain) (super.FM)).AS.hitPilot(shot.initiator, 7, (int) (shot.power * 0.002F));
            }
            shot.chunkName = "Tail1_D" + this.chunkDamageVisible("Tail1");
        }
        if (shot.chunkName.startsWith("Turret4")) {
            if (((Tuple3d) (Aircraft.Pd)).z > -0.99540001153945923D) {
                this.killPilot(shot.initiator, 5);
                if ((shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                    HUD.logCenter("H E A D S H O T");
                }
            } else {
                ((FlightModelMain) (super.FM)).AS.hitPilot(shot.initiator, 5, (int) (shot.power * 0.002F));
            }
        } else if (shot.chunkName.startsWith("Turret5")) {
            if (((Tuple3d) (Aircraft.Pd)).z > -0.99540001153945923D) {
                this.killPilot(shot.initiator, 6);
                if ((shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                    HUD.logCenter("H E A D S H O T");
                }
            } else {
                ((FlightModelMain) (super.FM)).AS.hitPilot(shot.initiator, 6, (int) (shot.power * 0.002F));
            }
        } else {
            super.msgShot(shot);
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 4: // '\004'
                super.FM.turret[0].bIsOperable = false;
                break;

            case 3: // '\003'
                super.FM.turret[1].bIsOperable = false;
                break;

            case 2: // '\002'
                super.FM.turret[2].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0: // '\0'
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                break;

            case 1: // '\001'
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("Head2_D0", false);
                break;

            case 2: // '\002'
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("Head3_D0", false);
                break;

            case 3: // '\003'
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                this.hierMesh().chunkVisible("Head4_D0", false);
                break;

            case 5: // '\005'
                this.hierMesh().chunkVisible("Pilot6_D0", false);
                this.hierMesh().chunkVisible("HMask6_D0", false);
                this.hierMesh().chunkVisible("Pilot6_D1", true);
                this.hierMesh().chunkVisible("Head5_D0", false);
                break;

            case 6: // '\006'
                this.hierMesh().chunkVisible("Pilot7_D0", false);
                this.hierMesh().chunkVisible("HMask7_D0", false);
                this.hierMesh().chunkVisible("Pilot7_D1", true);
                this.hierMesh().chunkVisible("Head6_D0", false);
                break;

            case 7: // '\007'
                this.hierMesh().chunkVisible("Pilot8_D0", false);
                this.hierMesh().chunkVisible("HMask8_D0", false);
                this.hierMesh().chunkVisible("Pilot8_D1", true);
                this.hierMesh().chunkVisible("Head7_D0", false);
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

            case 0: // '\0'
                if (f < -76F) {
                    f = -76F;
                    flag = false;
                }
                if (f > 76F) {
                    f = 76F;
                    flag = false;
                }
                if (f1 < -47F) {
                    f1 = -47F;
                    flag = false;
                }
                if (f1 > 47F) {
                    f1 = 47F;
                    flag = false;
                }
                break;

            case 1: // '\001'
                float f2 = Math.abs(f);
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                if (f2 < 1.0F) {
                    if (f1 < 17F) {
                        f1 = 17F;
                        flag = false;
                    }
                    break;
                }
                if (f2 < 4.5F) {
                    if (f1 < (0.71429F - (0.71429F * f2))) {
                        f1 = 0.71429F - (0.71429F * f2);
                        flag = false;
                    }
                    break;
                }
                if (f2 < 29.5F) {
                    if (f1 < -2.5F) {
                        f1 = -2.5F;
                        flag = false;
                    }
                    break;
                }
                if (f2 < 46F) {
                    if (f1 < (52.0303F - (1.84848F * f2))) {
                        f1 = 52.0303F - (1.84848F * f2);
                        flag = false;
                    }
                    break;
                }
                if (f2 < 89F) {
                    if (f1 < (-70.73518F + (0.80232F * f2))) {
                        f1 = -70.73518F + (0.80232F * f2);
                        flag = false;
                    }
                    break;
                }
                if (f2 < 147F) {
                    if (f1 < 1.5F) {
                        f1 = 1.5F;
                        flag = false;
                    }
                    break;
                }
                if (f2 < 162F) {
                    if (f1 < (-292.5F + (2.0F * f2))) {
                        f1 = -292.5F + (2.0F * f2);
                        flag = false;
                    }
                    break;
                }
                if (f1 < 31.5F) {
                    f1 = 31.5F;
                    flag = false;
                }
                break;

            case 2: // '\002'
                if (f < -87F) {
                    f = -87F;
                    flag = false;
                }
                if (f > 87F) {
                    f = 87F;
                    flag = false;
                }
                if (f1 < -78F) {
                    f1 = -78F;
                    flag = false;
                }
                if (f1 > 67F) {
                    f1 = 67F;
                    flag = false;
                }
                break;

            case 3: // '\003'
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 16F) {
                    f1 = 16F;
                    flag = false;
                }
                if (f < -60F) {
                    f = -60F;
                    flag = false;
                    if (f1 > -11.5F) {
                        f1 = -11.5F;
                    }
                    break;
                }
                if (f < -13.5F) {
                    if (f1 > (3.9836F + (0.25806F * f))) {
                        f1 = 3.9836F + (0.25806F * f);
                        flag = false;
                    }
                    break;
                }
                if (f < -10.5F) {
                    if (f1 > (16.25005F + (1.16667F * f))) {
                        f1 = 16.25005F + (1.16667F * f);
                        flag = false;
                    }
                    break;
                }
                if (f < 14F) {
                    if (f1 > 5F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 80F) {
                    if (f1 > 8F) {
                        flag = false;
                    }
                } else {
                    f = 80F;
                    flag = false;
                }
                break;

            case 4: // '\004'
                f = -f;
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 16F) {
                    f1 = 16F;
                    flag = false;
                }
                if (f < -60F) {
                    f = -60F;
                    flag = false;
                    if (f1 > -11.5F) {
                        f1 = -11.5F;
                    }
                } else if (f < -13.5F) {
                    if (f1 > (3.9836F + (0.25806F * f))) {
                        f1 = 3.9836F + (0.25806F * f);
                        flag = false;
                    }
                } else if (f < -10.5F) {
                    if (f1 > (16.25005F + (1.16667F * f))) {
                        f1 = 16.25005F + (1.16667F * f);
                        flag = false;
                    }
                } else if (f < 14F) {
                    if (f1 > 5F) {
                        flag = false;
                    }
                } else if (f < 80F) {
                    if (f1 > 8F) {
                        flag = false;
                    }
                } else {
                    f = 80F;
                    flag = false;
                }
                f = -f;
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
    }

    private static final float toMeters(float f) {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f) {
        return 0.4470401F * f;
    }

    public boolean typeBomberToggleAutomation() {
        this.bSightAutomation = !this.bSightAutomation;
        this.bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
        return this.bSightAutomation;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle++;
        if (this.fSightCurForwardAngle > 85F) {
            this.fSightCurForwardAngle = 85F;
        }
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle--;
        if (this.fSightCurForwardAngle < 0.0F) {
            this.fSightCurForwardAngle = 0.0F;
        }
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.05F;
        if (this.fSightCurSideslip > 3F) {
            this.fSightCurSideslip = 3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.05F;
        if (this.fSightCurSideslip < -3F) {
            this.fSightCurSideslip = -3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 3000F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 50F;
        if (this.fSightCurAltitude > 50000F) {
            this.fSightCurAltitude = 50000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 50F;
        if (this.fSightCurAltitude < 1000F) {
            this.fSightCurAltitude = 1000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 200F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 10F;
        if (this.fSightCurSpeed > 450F) {
            this.fSightCurSpeed = 450F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 10F;
        if (this.fSightCurSpeed < 100F) {
            this.fSightCurSpeed = 100F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        if (Math.abs(((FlightModelMain) (super.FM)).Or.getKren()) > 4.5D) {
            this.fSightCurReadyness -= 0.0666666F * f;
            if (this.fSightCurReadyness < 0.0F) {
                this.fSightCurReadyness = 0.0F;
            }
        }
        if (this.fSightCurReadyness < 1.0F) {
            this.fSightCurReadyness += 0.0333333F * f;
        } else if (this.bSightAutomation) {
            this.fSightCurDistance -= toMetersPerSecond(this.fSightCurSpeed) * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / toMeters(this.fSightCurAltitude)));
            if (this.fSightCurDistance < (toMetersPerSecond(this.fSightCurSpeed) * Math.sqrt(toMeters(this.fSightCurAltitude) * 0.2038736F))) {
                this.bSightBombDump = true;
            }
            if (this.bSightBombDump) {
                if (super.FM.isTick(3, 0)) {
                    if ((((FlightModelMain) (super.FM)).CT.Weapons[3] != null) && (((FlightModelMain) (super.FM)).CT.Weapons[3][((FlightModelMain) (super.FM)).CT.Weapons[3].length - 1] != null) && ((FlightModelMain) (super.FM)).CT.Weapons[3][((FlightModelMain) (super.FM)).CT.Weapons[3].length - 1].haveBullets()) {
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else {
                    ((FlightModelMain) (super.FM)).CT.WeaponControl[3] = false;
                }
            }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(this.fSightCurDistance);
        netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
        netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedByte();
        this.bSightAutomation = (i & 1) != 0;
        this.bSightBombDump = (i & 2) != 0;
        this.fSightCurDistance = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
        this.fSightCurSideslip = -3F + (netmsginput.readUnsignedByte() / 33.33333F);
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
        this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float   fSightCurDistance;
    public float    fSightCurForwardAngle;
    public float    fSightCurSideslip;
    public float    fSightCurAltitude;
    public float    fSightCurSpeed;
    public float    fSightCurReadyness;
    static {
        Class class1 = LANCASTER.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Lancaster");
        Property.set(class1, "meshName", "3DO/Plane/Lancaster/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Lancaster.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLanc.class, CockpitLanc_Bombardier.class, CockpitLanc_FGunner.class, CockpitLanc_TGunner.class, CockpitLanc_AGunner.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN09", "_MGUN10", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08" });
    }
}
