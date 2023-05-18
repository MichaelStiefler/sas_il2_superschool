package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public abstract class G_55xyz extends Scheme1
    implements TypeFighter
{

    public G_55xyz()
    {
        canopyF = 0.0F;
        tiltCanopyOpened = false;
        slideCanopyOpened = false;
        blisterRemoved = false;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.Gears.computePlaneLandPose(FM);
        mydebuggunnery("H = " + FM.Gears.H);
        mydebuggunnery("Pitch = " + FM.Gears.Pitch);
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
        if(i == 19)
            FM.Gears.hitCentreGear();
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("hmask1_D0", false);
        else
            hierMesh().chunkVisible("hmask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if(tiltCanopyOpened && !blisterRemoved && FM.getSpeed() > 75F)
            doRemoveBlister1();
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

    private final void doRemoveBlister1()
    {
        blisterRemoved = true;
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public void moveCockpitDoor(float f)
    {
        if(f > canopyF)
        {
            if((FM.Gears.onGround() && FM.getSpeed() < 5F || tiltCanopyOpened) && (FM.isPlayers() || isNetPlayer()))
            {
                tiltCanopyOpened = true;
                hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            } else
            {
                slideCanopyOpened = true;
                resetYPRmodifier();
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.28F);
                hierMesh().chunkSetLocate("Blister4L_D0", Aircraft.xyz, Aircraft.ypr);
            }
        } else
        if(FM.Gears.onGround() && FM.getSpeed() < 5F && !slideCanopyOpened || tiltCanopyOpened)
        {
            hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if(FM.getSpeed() > 70F && f < 0.6F && !blisterRemoved)
                doRemoveBlister1();
            if(f == 0.0F)
                tiltCanopyOpened = false;
        } else
        {
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.3F);
            hierMesh().chunkSetLocate("Blister4L_D0", Aircraft.xyz, Aircraft.ypr);
            if(f == 0.0F)
                slideCanopyOpened = false;
        }
        canopyF = f;
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.18F, 0.99F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.2F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("Gearl6_D0", 0.0F, Aircraft.cvt(f, 0.18F, 0.99F, 0.0F, -30F), 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.3F, 0.99F, 0.0F, 0.45F);
        hiermesh.chunkSetLocate("Gearl9_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.18F, 0.99F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.2F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.18F, 0.99F, 0.0F, 30F), 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.3F, 0.99F, 0.0F, 0.45F);
        hiermesh.chunkSetLocate("GearR9_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.67F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearC3L_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.15F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC3R_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.15F, 0.0F, -80F), 0.0F);
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
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.2085F, 0.0F, 0.2085F);
        hierMesh().chunkSetLocate("GearL8_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.2085F, 0.0F, 0.2085F);
        hierMesh().chunkSetLocate("GearR8_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFlap(float f)
    {
        float f1 = -35F * f;
        hierMesh().chunkSetAngles("FlapInL_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("FlapInR_D0", 0.0F, f1, 0.0F);
        f1 = -35F * f;
        hierMesh().chunkSetAngles("FlapOutL_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("FlapOutR_D0", 0.0F, f1, 0.0F);
    }

    public void update(float f)
    {
        hierMesh().chunkSetAngles("Filter_D0", 0.0F, -20F * FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    protected void setControlDamage(Shot shot, int i)
    {
        if(World.Rnd().nextFloat() < 0.002F && getEnergyPastArmor(4F, shot) > 0.0F)
        {
            FM.AS.setControlsDamage(shot.initiator, i);
            mydebuggunnery(i + " Controls Out... //0 = AILERON, 1 = ELEVATOR, 2 = RUDDER");
        }
    }

    protected void moveAileron(float f)
    {
        float f1 = -(f * 30F);
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, f1, 0.0F);
        f1 = -(f * 30F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, f1, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -31F * f, 0.0F);
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
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                mydebuggunnery("Armor: Hit..");
                if(s.startsWith("xxarmorp"))
                {
                    int i = s.charAt(8) - 48;
                    switch(i)
                    {
                    case 1:
                        getEnergyPastArmor(22.76D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F)
                            doRicochetBack(shot);
                        break;

                    case 3:
                        getEnergyPastArmor(9.366F, shot);
                        break;

                    case 5:
                        getEnergyPastArmor(12.699999809265137D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                        break;
                    }
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                mydebuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
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
            if(s.startsWith("xxspar"))
            {
                mydebuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    mydebuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    mydebuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    mydebuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    mydebuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    mydebuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            } else
            {
                if(s.startsWith("xxlock"))
                {
                    mydebuggunnery("Lock Construction: Hit..");
                    if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                        nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                    }
                    if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: VatorL Lock Shot Off..");
                        nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                    }
                    if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: VatorR Lock Shot Off..");
                        nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                    }
                    if(s.startsWith("xxlockalL") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: AroneL Lock Shot Off..");
                        nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                    }
                    if(s.startsWith("xxlockalR") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                    {
                        mydebuggunnery("Lock Construction: AroneR Lock Shot Off..");
                        nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                    }
                }
                if(s.startsWith("xxeng"))
                {
                    if((s.endsWith("prop") || s.endsWith("pipe")) && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                        FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                    if(s.endsWith("case") || s.endsWith("gear"))
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
                        if(World.Rnd().nextFloat() < 0.02F)
                        {
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                        } else
                        {
                            FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                            mydebuggunnery("*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                        }
                        getEnergyPastArmor(12F, shot);
                    }
                    if(s.endsWith("cyl1") || s.endsWith("cyl2"))
                    {
                        if(getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 4F), shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.75F)
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
                    if(s.endsWith("supc"))
                    {
                        if(getEnergyPastArmor(0.05F, shot) > 0.0F)
                            FM.EI.engines[0].setKillCompressor(shot.initiator);
                        getEnergyPastArmor(2.0F, shot);
                    }
                    if(s.startsWith("xxeng1oil"))
                    {
                        FM.AS.hitOil(shot.initiator, 0);
                        mydebuggunnery("*** Engine Module: Oil Radiator Hit..");
                    }
                    mydebuggunnery("*** Engine state = " + FM.AS.astateEngineStates[0]);
                } else
                if(s.startsWith("xxtank"))
                {
                    int k = s.charAt(6) - 49;
                    if(getEnergyPastArmor(0.19F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
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
                if(s.startsWith("xxmgun"))
                {
                    if(s.endsWith("01"))
                    {
                        mydebuggunnery("Armament System: Left Machine Gun: Disabled..");
                        FM.AS.setJamBullets(0, 0);
                    }
                    if(s.endsWith("02"))
                    {
                        mydebuggunnery("Armament System: Right Machine Gun: Disabled..");
                        FM.AS.setJamBullets(1, 0);
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
                } else
                if(s.startsWith("xxcannon"))
                {
                    if(s.endsWith("02"))
                    {
                        mydebuggunnery("Armament System: Left Cannon: Disabled..");
                        FM.AS.setJamBullets(3, 0);
                    }
                    if(s.endsWith("03"))
                    {
                        mydebuggunnery("Armament System: Right Cannon: Disabled..");
                        FM.AS.setJamBullets(4, 0);
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
                }
            }
        } else
        if(s.startsWith("xcf"))
        {
            setControlDamage(shot, 0);
            setControlDamage(shot, 1);
            setControlDamage(shot, 2);
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            setControlDamage(shot, 1);
            setControlDamage(shot, 2);
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xrudder"))
        {
            setControlDamage(shot, 2);
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl"))
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr"))
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
            {
                setControlDamage(shot, 0);
                hitChunk("WingLIn", shot);
            }
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
            {
                setControlDamage(shot, 0);
                hitChunk("WingRIn", shot);
            }
            if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
            {
                setControlDamage(shot, 0);
                hitChunk("WingLMid", shot);
            }
            if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
            {
                setControlDamage(shot, 0);
                hitChunk("WingRMid", shot);
            }
            if(s.startsWith("xwinglout1") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout1") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                mydebuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xoil"))
        {
            if(World.Rnd().nextFloat() < 0.12F)
            {
                FM.AS.hitOil(shot.initiator, 0);
                mydebuggunnery("*** Engine Module: Oil Radiator Hit..");
            }
        } else
        if(!s.startsWith("xblister") && (s.startsWith("xpilot") || s.startsWith("xhead")))
        {
            byte byte0 = 0;
            int l;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else
            {
                l = s.charAt(5) - 49;
            }
            hitFlesh(l, shot, byte0);
        }
    }

    protected void mydebuggunnery(String s)
    {
    }

    private static final float gear8scale[] = {
        0.0F, 3.75F, 13.75F, 27.68F, 42.75F, 57.61F, 70.93F, 82.25F, 91F, 96.7F, 
        98.8F
    };
    public float canopyF;
    private boolean tiltCanopyOpened;
    private boolean slideCanopyOpened;
    private boolean blisterRemoved;

    static 
    {
        Class class1 = G_55xyz.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "originCountry", PaintScheme.countryItaly);
    }
}
