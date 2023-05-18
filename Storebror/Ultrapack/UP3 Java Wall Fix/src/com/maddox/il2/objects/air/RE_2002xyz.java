package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class RE_2002xyz extends Scheme1
    implements TypeFighter, TypeStormovik
{

    public RE_2002xyz()
    {
        maneuver = null;
        bRaiseTailWheel = false;
        counter = 0;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.Gears.computePlaneLandPose(FM);
        if(thisWeaponsName.startsWith("2x100kg") || thisWeaponsName.endsWith("2x100kg"))
        {
            hierMesh().chunkVisible("RackL_D0", true);
            hierMesh().chunkVisible("RackR_D0", true);
        }
        if(thisWeaponsName.endsWith("Torpedo"))
            bRaiseTailWheel = true;
        if(thisWeaponsName.equals("1x630kg") || thisWeaponsName.equals("1x500kg") || thisWeaponsName.startsWith("1x250kg"))
        {
            hierMesh().chunkVisible("LeverBase_d0", true);
            hierMesh().chunkVisible("BombLever_d0", true);
            hierMesh().chunkVisible("BombHook_d0", true);
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 19:
            FM.Gears.hitCentreGear();
            break;

        case 3:
            if(World.Rnd().nextInt(0, 99) < 1)
            {
                FM.AS.hitEngine(this, 0, 4);
                hitProp(0, j, actor);
                FM.EI.engines[0].setEngineStuck(actor);
                return cut("engine1");
            } else
            {
                FM.AS.setEngineDies(this, 0);
                return false;
            }
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F)
                FM.AS.hitTank(this, 0, 1);
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
        }
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.625F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 100F * f, 0.0F);
        if(f < 0.25F)
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, (-95F * f) / 0.25F, 0.0F);
        else
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -95F, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 100F * f, 0.0F);
        if(f < 0.25F)
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, (95F * f) / 0.25F, 0.0F);
        else
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 95F, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -100F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, f, 0.0F);
    }

    public void moveWheelSink()
    {
        float f = 95F * FM.CT.getGear();
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.2085F, 0.0F, -0.2F);
        Aircraft.ypr[1] = -f;
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.ypr[1] = f;
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.2085F, 0.0F, -0.2F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFlap(float f)
    {
        float f1 = -35F * f;
        hierMesh().chunkSetAngles("FlapL1_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("FlapR1_D0", 0.0F, f1, 0.0F);
        f1 = -50F * f;
        hierMesh().chunkSetAngles("FlapL2_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("FlapR2_D0", 0.0F, f1, 0.0F);
    }

    public void update(float f)
    {
        if(bRaiseTailWheel)
        {
            counter++;
            resetYPRmodifier();
            Aircraft.xyz[1] = (-0.2F * counter) / 50F;
            hierMesh().chunkSetLocate("Gearc2_D0", Aircraft.xyz, Aircraft.ypr);
            if(counter >= 50)
                bRaiseTailWheel = false;
        }
        for(int i = 1; i < 6; i++)
        {
            hierMesh().chunkSetAngles("RadiatorL" + i + "_D0", 0.0F, 30F * FM.EI.engines[0].getControlRadiator(), 0.0F);
            hierMesh().chunkSetAngles("RadiatorR" + i + "_D0", 0.0F, 30F * FM.EI.engines[0].getControlRadiator(), 0.0F);
        }

        super.update(f);
    }

    protected void setControlDamage(Shot shot, int i)
    {
        if(World.Rnd().nextFloat() < 0.002F && getEnergyPastArmor(4F, shot) > 0.0F)
            FM.AS.setControlsDamage(shot.initiator, i);
    }

    protected void moveAileron(float f)
    {
        float f1 = -(f * 23F);
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, f1, 0.0F);
        f1 = -(f * 23F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, f1, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -27F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        if(f < 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -20F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        boolean flag = false;
        String s1 = s.toLowerCase();
        if(s1.startsWith("xx"))
        {
            if(s1.startsWith("xxarmor"))
            {
                if(s1.startsWith("xxarmorp"))
                {
                    int i = s1.charAt(8) - 48;
                    switch(i)
                    {
                    case 1:
                        getEnergyPastArmor(22.76D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F)
                            doRicochetBack(shot);
                        break;

                    case 2:
                        getEnergyPastArmor(9.366F, shot);
                        break;

                    case 3:
                        getEnergyPastArmor(12.699999809265137D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                        break;
                    }
                }
            } else
            if(s1.startsWith("xxcontrols"))
            {
                int j = s1.charAt(10) - 48;
                switch(j)
                {
                case 1:
                case 2:
                    if(getEnergyPastArmor(0.25F / ((float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z) + 0.0001F), shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            mydebuggunnery("Controls: Elevator Wiring Hit, Elevator Controls Disabled..");
                            FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        if(World.Rnd().nextFloat() < 0.75F)
                        {
                            mydebuggunnery("Controls: Rudder Wiring Hit, Rudder Controls Disabled..");
                            FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                    }
                    break;

                case 3:
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F)
                    {
                        mydebuggunnery("*** Control Column: Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 4:
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        mydebuggunnery("Quadrant: Hit, Engine Controls Disabled..");
                    }
                    break;

                case 5:
                case 7:
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        mydebuggunnery("*** Aileron Controls: Control Crank Destroyed..");
                    }
                    break;

                case 6:
                case 8:
                    if(getEnergyPastArmor(4D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        mydebuggunnery("Controls: Aileron Wiring Hit, Aileron Controls Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;
                }
            } else
            if(s1.startsWith("xxspar"))
            {
                mydebuggunnery("Spar Construction: Hit..");
                if(s1.startsWith("xxspartli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    mydebuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s1.startsWith("xxspartri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    mydebuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s1.startsWith("xxspartlo") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    mydebuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s1.startsWith("xxspartro") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    mydebuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s1.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    mydebuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            } else
            {
                if(s1.startsWith("xxlock"))
                {
                    mydebuggunnery("Lock Construction: Hit..");
                    if(s1.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                        nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                    }
                    if(s1.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: VatorL Lock Shot Off..");
                        nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                    }
                    if(s1.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: VatorR Lock Shot Off..");
                        nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                    }
                    if(s1.startsWith("xxlockall") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: AroneL Lock Shot Off..");
                        nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                    }
                    if(s1.startsWith("xxlockalr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: AroneR Lock Shot Off..");
                        nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                    }
                }
                if(s1.startsWith("xxeng"))
                {
                    if((s1.endsWith("prop") || s1.endsWith("pipe")) && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                        FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                    if(s1.endsWith("case"))
                    {
                        if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                        {
                            if(World.Rnd().nextFloat() < shot.power / 140000F)
                            {
                                FM.AS.setEngineStuck(shot.initiator, 0);
                                mydebuggunnery("*** Engine Crank Case Hit - Engine Stucks..");
                            }
                            if(World.Rnd().nextFloat() < shot.power / 85000F)
                            {
                                FM.AS.hitEngine(shot.initiator, 0, 2);
                                mydebuggunnery("*** Engine Crank Case Hit - Engine Damaged..");
                            }
                        } else
                        if(World.Rnd().nextFloat() < 0.01F)
                        {
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                        } else
                        {
                            FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
                            mydebuggunnery("*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                        }
                        getEnergyPastArmor(12F, shot);
                    }
                    if(s1.endsWith("cyls"))
                    {
                        if(getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.75F)
                        {
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                            mydebuggunnery("*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                            if(World.Rnd().nextFloat() < shot.power / 48000F)
                            {
                                FM.AS.hitEngine(shot.initiator, 0, 2);
                                mydebuggunnery("*** Engine Cylinders Hit - Engine Fires..");
                            }
                        }
                        getEnergyPastArmor(25F, shot);
                    }
                    if(s1.endsWith("supc"))
                    {
                        if(getEnergyPastArmor(0.05F, shot) > 0.0F)
                            FM.EI.engines[0].setKillCompressor(shot.initiator);
                        getEnergyPastArmor(2.0F, shot);
                    }
                    if(s1.startsWith("xxeng1oil"))
                    {
                        FM.AS.hitOil(shot.initiator, 0);
                        mydebuggunnery("*** Engine Module: Oil Radiator Hit..");
                    }
                    mydebuggunnery("*** Engine state = " + FM.AS.astateEngineStates[0]);
                } else
                if(s1.startsWith("xxtank"))
                {
                    int k = s1.charAt(6) - 49;
                    if(getEnergyPastArmor(0.15F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        if(FM.AS.astateTankStates[k] == 0)
                        {
                            mydebuggunnery("Fuel System: Fuel Tank Pierced..");
                            FM.AS.hitTank(shot.initiator, k, 1);
                            FM.AS.doSetTankState(shot.initiator, k, 1);
                        } else
                        if(FM.AS.astateTankStates[k] == 1)
                        {
                            mydebuggunnery("Fuel System: Fuel Tank Pierced (2)..");
                            FM.AS.hitTank(shot.initiator, k, 1);
                            FM.AS.doSetTankState(shot.initiator, k, 2);
                        }
                        if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                        {
                            FM.AS.hitTank(shot.initiator, k, 2);
                            mydebuggunnery("Fuel System: Fuel Tank Pierced, State Shifted..");
                        }
                    }
                    mydebuggunnery("Tank State: " + FM.AS.astateTankStates[k]);
                } else
                if(s1.startsWith("xxmgun"))
                {
                    if(s1.endsWith("01"))
                    {
                        mydebuggunnery("Armament System: Left Machine Gun: Disabled..");
                        FM.AS.setJamBullets(0, 0);
                    }
                    if(s1.endsWith("02"))
                    {
                        mydebuggunnery("Armament System: Right Machine Gun: Disabled..");
                        FM.AS.setJamBullets(1, 0);
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
                }
            }
        } else
        if(s1.startsWith("xcf"))
        {
            setControlDamage(shot, 0);
            setControlDamage(shot, 1);
            setControlDamage(shot, 2);
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s1.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s1.startsWith("xtail"))
        {
            setControlDamage(shot, 1);
            setControlDamage(shot, 2);
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s1.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s1.startsWith("xrudder"))
        {
            setControlDamage(shot, 2);
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s1.startsWith("xstab"))
        {
            if(s1.startsWith("xstabl"))
                hitChunk("StabL", shot);
            if(s1.startsWith("xstabr"))
                hitChunk("StabR", shot);
        } else
        if(s1.startsWith("xvator"))
        {
            if(s1.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s1.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s1.startsWith("xwing"))
        {
            if(s1.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
            {
                setControlDamage(shot, 0);
                hitChunk("WingLIn", shot);
            }
            if(s1.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
            {
                setControlDamage(shot, 0);
                hitChunk("WingRIn", shot);
            }
            if(s1.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
            {
                setControlDamage(shot, 0);
                hitChunk("WingLMid", shot);
            }
            if(s1.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
            {
                setControlDamage(shot, 0);
                hitChunk("WingRMid", shot);
            }
            if(s1.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s1.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s1.startsWith("xarone"))
        {
            if(s1.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            if(s1.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
        } else
        if(s1.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                mydebuggunnery("Undercarriage: Stuck..");
                FM.Gears.setHydroOperable(false);
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s1.startsWith("xoil"))
        {
            if(World.Rnd().nextFloat() < 0.12F)
            {
                FM.AS.hitOil(shot.initiator, 0);
                mydebuggunnery("*** Engine Module: Oil Radiator Hit..");
            }
        } else
        if(!s1.startsWith("xblister") && (s1.startsWith("xpilot") || s1.startsWith("xhead")))
        {
            byte byte0 = 0;
            int l;
            if(s1.endsWith("a"))
            {
                byte0 = 1;
                l = s1.charAt(6) - 49;
            } else
            if(s1.endsWith("b"))
            {
                byte0 = 2;
                l = s1.charAt(6) - 49;
            } else
            {
                l = s1.charAt(5) - 49;
            }
            hitFlesh(l, shot, byte0);
        }
    }

    protected void mydebuggunnery(String s)
    {
    }

    private Maneuver maneuver;
    boolean bRaiseTailWheel;
    int counter;

    static 
    {
        Class class1 = RE_2002xyz.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "originCountry", PaintScheme.countryItaly);
    }
}
