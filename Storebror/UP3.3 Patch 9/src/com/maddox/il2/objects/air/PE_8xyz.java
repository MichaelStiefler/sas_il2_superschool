package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;

public class PE_8xyz extends Scheme4 //Scheme4BomberOPB2
    implements TypeTransport, TypeBomber
{

    public PE_8xyz()
    {
        suspension = 0.0F;
        suspR = 0.0F;
        suspL = 0.0F;
        fSightCurAltitude = 300F;
        fSightCurSpeed = 50F;
        fSightCurForwardAngle = 0.0F;
        fSightSetForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
    }

    public float getEyeLevelCorrection()
    {
        return 0.15F;
    }

    public int[] getBombTrainDelayArray()
    {
        return (new int[] {
            100, 200, 300, 400, 500, 600, 700, 800, 900, 1000
        });
    }

    public int getBombTrainMaxAmount()
    {
        return 20;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
    }

    public void moveWheelSink()
    {
        if(FM.Gears.onGround())
            suspension = suspension + 0.008F;
        else
            suspension = suspension - 0.008F;
        if(suspension < 0.0F)
        {
            suspension = 0.0F;
            if(!FM.isPlayers())
                FM.Gears.bTailwheelLocked = true;
        }
        if(suspension > 0.1F)
            suspension = 0.1F;
        Aircraft.xyz[0] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        float f = Aircraft.cvt(FM.getSpeed(), 0.0F, 25F, 0.0F, 1.0F);
        suspL = FM.Gears.gWheelSinking[0] * f + suspension;
        Aircraft.xyz[1] = -Aircraft.cvt(suspL, 0.0F, 0.25F, 0.0F, 0.25F);
        hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        suspR = FM.Gears.gWheelSinking[1] * f + suspension;
        Aircraft.xyz[1] = -Aircraft.cvt(suspR, 0.0F, 0.25F, 0.0F, 0.25F);
        hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = -55F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 25:
            FM.turret[0].bIsOperable = false;
            break;

        case 26:
            FM.turret[1].bIsOperable = false;
            break;

        case 27:
            FM.turret[2].bIsOperable = false;
            break;

        case 28:
            FM.turret[3].bIsOperable = false;
            break;

        case 29:
            FM.turret[4].bIsOperable = false;
            break;

        case 19:
            FM.Gears.hitCentreGear();
            hierMesh().chunkVisible("Antenna2_D0", false);
            killPilot(this, 7);
            break;

        case 11:
            hierMesh().chunkVisible("Antenna2_D0", false);
            break;

        case 13:
            killPilot(this, 2);
            killPilot(this, 3);
            break;

        case 36:
            killPilot(this, 6);
            break;

        case 33:
            killPilot(this, 5);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -65F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -65F * f, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.0023F)
                FM.AS.hitTank(this, 0, 1);
            if(FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.0023F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.0023F)
                FM.AS.hitTank(this, 2, 1);
            if(FM.AS.astateEngineStates[3] > 3 && World.Rnd().nextFloat() < 0.0023F)
                FM.AS.hitTank(this, 3, 1);
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
            if(!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode())
            {
                for(int i = 0; i < FM.EI.getNum(); i++)
                    if(FM.AS.astateEngineStates[i] > 3 && World.Rnd().nextFloat() < 0.2F)
                        FM.EI.engines[i].setExtinguisherFire();

            }
        }
        for(int j = 1; j <= 8; j++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + j + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + j + "_D0", hierMesh().isChunkVisible("Pilot" + j + "_D0"));

    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i1 = 0;
        s = s.toLowerCase();
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                getEnergyPastArmor(2.4F, shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                if(s.length() == 12)
                    i = 10 + (s.charAt(11) - 48);
                switch(i)
                {
                case 2:
                default:
                    break;

                case 1:
                    if(World.Rnd().nextFloat() < 0.125F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            Aircraft.debugprintln(this, "*** tail control wires hit, rudder destroyed.");
                            FM.AS.setControlsDamage(shot.initiator, 2);
                        } else
                        {
                            Aircraft.debugprintln(this, "*** tail control wires hit, elevators destroyed.");
                            FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                    getEnergyPastArmor(2.0F, shot);
                    break;

                case 3:
                    if(World.Rnd().nextFloat() < 0.125F)
                    {
                        Aircraft.debugprintln(this, "*** left wing control wires hit, ailerons destroyed.");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    getEnergyPastArmor(2.0F, shot);
                    break;

                case 4:
                    if(World.Rnd().nextFloat() < 0.125F)
                    {
                        Aircraft.debugprintln(this, "*** right wing control wires hit, ailerons destroyed.");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    getEnergyPastArmor(2.0F, shot);
                    break;
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** Tail Spars Damaged..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D3", shot.initiator);
                }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxbomb"))
            {
                if(World.Rnd().nextFloat() < 0.001F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
                {
                    Aircraft.debugprintln(this, "*** Bomb Payload Detonates..");
                    FM.AS.hitTank(shot.initiator, 0, 10);
                    FM.AS.hitTank(shot.initiator, 1, 10);
                    FM.AS.hitTank(shot.initiator, 2, 10);
                    FM.AS.hitTank(shot.initiator, 3, 10);
                    nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int j = s.charAt(5) - 49;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, j);
                            Aircraft.debugprintln(this, "*** Engine (" + j + ") Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 85000F)
                        {
                            FM.AS.hitEngine(shot.initiator, j, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + j + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.005F)
                    {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[j].setReadyness(shot.initiator, FM.EI.engines[j].getReadyness() - 0.00082F);
                        Aircraft.debugprintln(this, "*** Engine (" + j + ") Crank Case Hit - Readyness Reduced to " + FM.EI.engines[j].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyl1") || s.endsWith("cyl2"))
                {
                    if(getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[j].getCylindersRatio() * 0.75F)
                    {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine (" + j + ") Cylinders Hit, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 18000F)
                        {
                            FM.AS.hitEngine(shot.initiator, j, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + j + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("prop") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                {
                    if(World.Rnd().nextFloat() < 0.5F)
                        FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                    else
                        FM.EI.engines[j].setKillPropAngleDeviceSpeeds(shot.initiator);
                    getEnergyPastArmor(15.1F, shot);
                    Aircraft.debugprintln(this, "*** Engine (" + j + ") Module: Prop Governor Fails..");
                }
                if(s.endsWith("oil") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                {
                    FM.AS.setOilState(shot.initiator, j, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + j + ") Module: Oil Filter Pierced..");
                }
                if(s.endsWith("gear"))
                {
                    if(World.Rnd().nextFloat() < 0.5F)
                    {
                        if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                        {
                            debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                            FM.EI.engines[j].setEngineStuck(shot.initiator);
                        }
                    } else
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.EI.engines[j].setKillCompressor(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine (" + j + ") Module: Compressor Stops..");
                        getEnergyPastArmor(2.6F, shot);
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int k = s.charAt(6) - 49;
                if(k >= 4)
                    return;
                if(getEnergyPastArmor(1.0F, shot) > 0.0F)
                {
                    if(FM.AS.astateTankStates[k] == 0)
                    {
                        FM.AS.hitTank(shot.initiator, k, 1);
                        FM.AS.doSetTankState(shot.initiator, k, 1);
                    }
                    if(shot.powerType == 3)
                    {
                        if(shot.power < 16100F)
                        {
                            if(FM.AS.astateTankStates[k] < 4 && World.Rnd().nextFloat() < 0.21F)
                                FM.AS.hitTank(shot.initiator, k, 1);
                        } else
                        {
                            FM.AS.hitTank(shot.initiator, k, World.Rnd().nextInt(1, 1 + (int)(shot.power / 16100F)));
                        }
                    } else
                    if(shot.power > 16100F)
                        FM.AS.hitTank(shot.initiator, k, World.Rnd().nextInt(1, 1 + (int)(shot.power / 16100F)));
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(s.startsWith("xcf1"))
                if(point3d.x < 8D)
                {
                    if(chunkDamageVisible("CF") < 3)
                        hitChunk("CF", shot);
                } else
                if(chunkDamageVisible("Nose") < 3)
                    hitChunk("Nose", shot);
            if(s.startsWith("xcf2") && point3d.x > 4.2D)
                if(point3d.x > 6D && World.Rnd().nextFloat() < 0.1F)
                {
                    float f = World.Rnd().nextFloat();
                    if(f < 0.3F)
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                    else
                    if(f < 0.6F)
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    else
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x80);
                } else
                if(point3d.x > 6.5D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                else
                if(point3d.y > 0.0D)
                {
                    if(World.Rnd().nextFloat() < 0.5F)
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                    else
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                } else
                if(World.Rnd().nextFloat() < 0.5F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
                else
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
            return;
        }
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
            return;
        }
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
            return;
        }
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
            return;
        }
        if(s.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            return;
        }
        if(s.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
            return;
        }
        if(s.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
            return;
        }
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
            return;
        }
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            return;
        }
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            return;
        }
        if(s.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            return;
        }
        if(s.startsWith("xwingrmid"))
        {
            if(chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            return;
        }
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            return;
        }
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
            return;
        }
        if(s.startsWith("xaronel"))
        {
            if(chunkDamageVisible("AroneL") < 2)
                hitChunk("AroneL", shot);
            return;
        }
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 2)
                hitChunk("AroneR", shot);
            return;
        }
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            return;
        }
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            return;
        }
        if(s.startsWith("xengine3"))
        {
            if(chunkDamageVisible("Engine3") < 2)
                hitChunk("Engine3", shot);
            return;
        }
        if(s.startsWith("xengine4"))
        {
            if(chunkDamageVisible("Engine4") < 2)
                hitChunk("Engine4", shot);
            return;
        }
        if(s.startsWith("xgearl"))
            hitChunk("GearL2", shot);
        else
        if(s.startsWith("xgearr"))
        {
            hitChunk("GearR2", shot);
        } else
        {
            if(s.startsWith("xturret") && s.endsWith("b"))
            {
                int l = s.charAt(7) - 48;
                if(getEnergyPastArmor(1.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F)
                {
                    switch(l)
                    {
                    case 1:
                        l = 10;
                        i1 = 0;
                        break;

                    case 2:
                        l = 11;
                        i1 = 0;
                        break;

                    case 3:
                        l = 12;
                        i1 = 0;
                        break;

                    case 4:
                        l = 13;
                        i1 = 0;
                        break;

                    case 5:
                        l = 14;
                        i1 = 0;
                        break;
                    }
                    FM.AS.setJamBullets(l, i1);
                }
                return;
            }
            if(s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int j1;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    j1 = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    j1 = s.charAt(6) - 49;
                } else
                {
                    j1 = s.charAt(5) - 49;
                }
                hitFlesh(j1, shot, byte0);
                return;
            }
        }
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 2:
            FM.turret[0].setHealth(f);
            break;

        case 4:
            FM.turret[1].setHealth(f);
            break;

        case 5:
            FM.turret[3].setHealth(f);
            break;

        case 6:
            FM.turret[4].setHealth(f);
            break;

        case 7:
            FM.turret[2].setHealth(f);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("Head2_D0", false);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            hierMesh().chunkVisible("Head3_D0", false);
            break;

        case 3:
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("HMask4_D0", false);
            hierMesh().chunkVisible("Pilot4_D1", true);
            hierMesh().chunkVisible("Head4_D0", false);
            break;

        case 4:
            hierMesh().chunkVisible("Pilot5_D0", false);
            hierMesh().chunkVisible("HMask5_D0", false);
            hierMesh().chunkVisible("Pilot5_D1", true);
            hierMesh().chunkVisible("Head5_D0", false);
            break;

        case 5:
            hierMesh().chunkVisible("Pilot6_D0", false);
            hierMesh().chunkVisible("HMask6_D0", false);
            hierMesh().chunkVisible("Pilot6_D1", true);
            hierMesh().chunkVisible("Head6_D0", false);
            break;

        case 6:
            hierMesh().chunkVisible("Pilot7_D0", false);
            hierMesh().chunkVisible("HMask7_D0", false);
            hierMesh().chunkVisible("Pilot7_D1", true);
            hierMesh().chunkVisible("Head7_D0", false);
            break;

        case 7:
            hierMesh().chunkVisible("Pilot8_D0", false);
            hierMesh().chunkVisible("HMask8_D0", false);
            hierMesh().chunkVisible("Pilot8_D1", true);
            hierMesh().chunkVisible("Head8_D0", false);
            break;
        }
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0:
            if(f < -75F)
            {
                f = -75F;
                flag = false;
            }
            float f2 = Aircraft.cvt(f1, 0.0F, 46F, 75F, 60F);
            if(f > f2)
            {
                f = f2;
                flag = false;
            }
            if(f1 < -53F)
            {
                f1 = -53F;
                flag = false;
            }
            if(f1 > 46F)
            {
                f1 = 46F;
                flag = false;
            }
            break;

        case 1:
            float f3 = Math.abs(f);
            if(f1 > 50F)
            {
                f1 = 50F;
                flag = false;
            }
            if(f3 < 1.0F)
            {
                if(f1 < 17F)
                {
                    f1 = 17F;
                    flag = false;
                }
                break;
            }
            if(f3 < 4.5F)
            {
                if(f1 < 0.71429F - 0.71429F * f3)
                {
                    f1 = 0.71429F - 0.71429F * f3;
                    flag = false;
                }
                break;
            }
            if(f3 < 29.5F)
            {
                if(f1 < -2.5F)
                {
                    f1 = -2.5F;
                    flag = false;
                }
                break;
            }
            if(f3 < 46F)
            {
                if(f1 < 52.0303F - 1.84848F * f3)
                {
                    f1 = 52.0303F - 1.84848F * f3;
                    flag = false;
                }
                break;
            }
            if(f3 < 89F)
            {
                if(f1 < -70.73518F + 0.80232F * f3)
                {
                    f1 = -70.73518F + 0.80232F * f3;
                    flag = false;
                }
                break;
            }
            if(f3 < 147F)
            {
                if(f1 < 1.5F)
                {
                    f1 = 1.5F;
                    flag = false;
                }
                break;
            }
            if(f3 < 162F)
            {
                if(f1 < -292.5F + 2.0F * f3)
                {
                    f1 = -292.5F + 2.0F * f3;
                    flag = false;
                }
                break;
            }
            if(f1 < 31.5F)
            {
                f1 = 31.5F;
                flag = false;
            }
            break;

        case 2:
            float f4 = FM.CT.getElevator() * 25F;
            float f7 = 0.0F;
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 69F)
            {
                f1 = 69F;
                flag = false;
            }
            if(f < -110F)
            {
                f = -110F;
                flag = false;
            } else
            if(f < -90F)
            {
                if(f1 > -11F)
                {
                    f7 = Aircraft.cvt(f, -110F, -90F, 1.0F, -1.4F) + f4;
                    if(f1 < f7)
                    {
                        f1 = f7;
                        flag = false;
                    }
                } else
                {
                    f7 = Aircraft.cvt(f, -110F, -90F, -36F, -26F) + f4;
                    if(f1 > f7)
                    {
                        f1 = f7;
                        flag = false;
                    }
                }
            } else
            if(f < -70F)
                if(f1 > -11F)
                {
                    f7 = Aircraft.cvt(f, -90F, -70F, -1.4F, -11F) + f4;
                    if(f1 < f7)
                    {
                        f1 = f7;
                        flag = false;
                    }
                } else
                {
                    f7 = Aircraft.cvt(f, -90F, -70F, -26F, -11F) + f4;
                    if(f1 > f7)
                    {
                        f1 = f7;
                        flag = false;
                    }
                }
            if(f > 110F)
            {
                f = 110F;
                flag = false;
                break;
            }
            if(f > 90F)
            {
                if(f1 > -11F)
                {
                    f7 = Aircraft.cvt(f, 90F, 110F, -1.4F, 1.0F) + f4;
                    if(f1 < f7)
                    {
                        f1 = f7;
                        flag = false;
                    }
                    break;
                }
                f7 = Aircraft.cvt(f, 90F, 110F, -16F, -27F) + f4;
                if(f1 > f7)
                {
                    f1 = f7;
                    flag = false;
                }
                break;
            }
            if(f <= 70F)
                break;
            if(f1 > -11F)
            {
                f7 = Aircraft.cvt(f, 70F, 90F, -11F, -1.4F) + f4;
                if(f1 < f7)
                {
                    f1 = f7;
                    flag = false;
                }
                break;
            }
            f7 = Aircraft.cvt(f, 70F, 90F, -11F, -16F) + f4;
            if(f1 > f7)
            {
                f1 = f7;
                flag = false;
            }
            break;

        case 3:
            if(f1 < -60F)
            {
                f1 = -60F;
                flag = false;
            }
            if(f1 > 8F)
            {
                f1 = 8F;
                flag = false;
            }
            if(f1 > 6F && f > 65F)
            {
                f1 = 6F;
                flag = false;
            }
            if(f1 > 3.3F && f < 14F)
            {
                f1 = 3.3F;
                flag = false;
            }
            if(f1 > -15F && f < -90F)
            {
                f1 = -15F;
                flag = false;
            }
            if(f <= -11F && f > -12.5F && f1 >= -50F)
            {
                f = -11F;
                flag = false;
            }
            if(f >= -88F && f < -10F)
            {
                float f5 = Aircraft.cvt(f, -90F, -10F, -60F, -50F);
                if(f1 > f5)
                {
                    f1 = f5;
                    flag = false;
                }
            }
            if(f >= -91.5F && f < -90.5F && f1 >= -50F)
            {
                f = -91.5F;
                flag = false;
            }
            if(f < -98.5F)
            {
                f = -98.5F;
                flag = false;
            }
            if(f > 102F)
            {
                f = 102F;
                flag = false;
            }
            break;

        case 4:
            f = -f;
            if(f1 < -60F)
            {
                f1 = -60F;
                flag = false;
            }
            if(f1 > 8F)
            {
                f1 = 8F;
                flag = false;
            }
            if(f1 > 6F && f < -65F)
            {
                f1 = 6F;
                flag = false;
            }
            if(f1 > 3.3F && f > -14F)
            {
                f1 = 3.3F;
                flag = false;
            }
            if(f1 > -15F && f > 90F)
            {
                f1 = -15F;
                flag = false;
            }
            if(f >= 17F && f < 18F && f1 >= -50F)
            {
                f = 17F;
                flag = false;
            }
            if(f > 20F && f < 91F)
            {
                float f6 = Aircraft.cvt(f, 18F, 93F, -50F, -60F);
                if(f1 > f6)
                {
                    f1 = f6;
                    flag = false;
                }
            }
            if(f > 93F && f <= 94F && f1 >= -50F)
            {
                f = 94F;
                flag = false;
            }
            if(f > 102F)
            {
                f = 102F;
                flag = false;
            }
            if(f < -98F)
            {
                f = -98F;
                flag = false;
            }
            f = -f;
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }
    
    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
        fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle += 0.2F;
        if(fSightCurForwardAngle > 75F)
            fSightCurForwardAngle = 75F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)(fSightCurForwardAngle * 1.0F))
        });
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle -= 0.2F;
        if(fSightCurForwardAngle < -15F)
            fSightCurForwardAngle = -15F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)(fSightCurForwardAngle * 1.0F))
        });
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip++;
        if(fSightCurSideslip > 45F)
            fSightCurSideslip = 45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 1.0F))
        });
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip--;
        if(fSightCurSideslip < -45F)
            fSightCurSideslip = -45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 1.0F))
        });
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 10F;
        if(fSightCurAltitude > 6000F)
            fSightCurAltitude = 6000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 10F;
        if(fSightCurAltitude < 300F)
            fSightCurAltitude = 300F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 5F;
        if(fSightCurSpeed > 650F)
            fSightCurSpeed = 650F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 5F;
        if(fSightCurSpeed < 50F)
            fSightCurSpeed = 50F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberUpdate(float f)
    {
        double d = ((double)fSightCurSpeed / 3.6D) * Math.sqrt((double)fSightCurAltitude * 0.203873598D);
        d -= (double)(fSightCurAltitude * fSightCurAltitude) * 0.00001419D;
        fSightSetForwardAngle = (float)Math.toDegrees(Math.atan(d / (double)fSightCurAltitude));
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeFloat(fSightCurSpeed);
        netmsgguaranted.writeFloat(fSightCurForwardAngle);
        netmsgguaranted.writeFloat(fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readFloat();
        fSightCurSideslip = netmsginput.readFloat();
    }
    
    protected void moveGear(float f, float f1, float f2)
    {
    }

    protected void moveGear(float f)
    {
        moveGear(f, f, f);
    }
    
    private float suspension;
    public float suspR;
    public float suspL;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurForwardAngle;
    public float fSightSetForwardAngle;
    public float fSightCurSideslip;
}