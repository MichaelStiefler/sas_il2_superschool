package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Property;

public class H19D extends Scheme1
    implements TypeScout, TypeTransport, TypeStormovik
{

    public H19D()
    {
        suka = new Loc();
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        rotorrpm = 0;
        pictVBrake = 0.0F;
        pictAileron = 0.0F;
        pictVator = 0.0F;
        pictRudder = 0.0F;
        pictBlister = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(this.FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
    }

    protected void moveElevator(float f)
    {
    }

    protected void moveAileron(float f)
    {
    }

    protected void moveFlap(float f)
    {
    }

    protected void moveRudder(float f)
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -1F);
        hierMesh().chunkSetLocate("Bay01_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFan(float f)
    {
        rotorrpm = Math.abs((int)(FM.EI.engines[0].getw() * 0.025F + FM.Vwld.length() / 30D));
        if(rotorrpm >= 1)
            rotorrpm = 1;
        if(FM.EI.engines[0].getw() > 100F)
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", true);
        }
        if(FM.EI.engines[0].getw() < 100F)
        {
            hierMesh().chunkVisible("Prop1_D0", true);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop1_D1"))
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if(FM.EI.engines[0].getw() > 100F)
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", true);
        }
        if(FM.EI.engines[0].getw() < 100F)
        {
            hierMesh().chunkVisible("Prop2_D0", true);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop2_D1"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Tail1_CAP"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
            hierMesh().chunkVisible("Prop2_D1", false);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 100F) % 360F : (float)(dynamoOrient - rotorrpm * 25D) % 360F;
        hierMesh().chunkSetAngles("Prop1_D0", -dynamoOrient, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, dynamoOrient * -10F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -10F), 0.0F);
        hierMesh().chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, 10F), 0.0F);
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.42F, 0.0F, 0.3F);
        hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveArrestorHook(float f)
    {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if(s.endsWith("p1"))
                    getEnergyPastArmor(8.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-006D), shot);
            } else
            {
                if(s.startsWith("xxcontrols"))
                {
                    int i = s.charAt(10) - 48;
                    switch(i)
                    {
                    default:
                        break;

                    case 1:
                        if(getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.3F), shot) > 0.0F)
                        {
                            if(World.Rnd().nextFloat() < 0.25F)
                            {
                                FM.AS.setControlsDamage(shot.initiator, 2);
                                Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                            }
                            if(World.Rnd().nextFloat() < 0.25F)
                            {
                                FM.AS.setControlsDamage(shot.initiator, 1);
                                Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                            }
                        }
                        // fall through

                    case 2:
                    case 3:
                        if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                        {
                            FM.AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                        }
                        break;
                    }
                }
                if(s.startsWith("xxspar"))
                {
                    Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                    if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(9.5F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                        nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                    }
                    if(s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                        nextDMGLevels(1, 2, "WingLIn_D" + chunkDamageVisible("WingLIn"), shot.initiator);
                    }
                    if(s.startsWith("xxsparri") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                        nextDMGLevels(1, 2, "WingRIn_D" + chunkDamageVisible("WingRIn"), shot.initiator);
                    }
                    if(s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                        nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                    }
                    if(s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                        nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                    }
                    if(s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                        nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                    }
                    if(s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                        nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                    }
                    if(s.startsWith("xxstabl") && getEnergyPastArmor(16.2F, shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                        nextDMGLevels(1, 2, "StabL_D" + chunkDamageVisible("StabL"), shot.initiator);
                    }
                    if(s.startsWith("xxstabr") && getEnergyPastArmor(16.2F, shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                        nextDMGLevels(1, 2, "StabR_D" + chunkDamageVisible("StabR"), shot.initiator);
                    }
                }
                if(s.startsWith("xxlock"))
                {
                    Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                    if(s.startsWith("xxlockr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                        nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                    }
                    if(s.startsWith("xxlockvl") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                        nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                    }
                    if(s.startsWith("xxlockvr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                        nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                    }
                    if(s.startsWith("xxlockal") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                        nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                    }
                    if(s.startsWith("xxlockar") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                        nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                    }
                }
                if(s.startsWith("xxeng"))
                {
                    Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                    if(s.endsWith("prop"))
                    {
                        if(getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                        }
                    } else
                    if(s.endsWith("case"))
                    {
                        if(getEnergyPastArmor(2.1F, shot) > 0.0F)
                        {
                            if(World.Rnd().nextFloat() < shot.power / 175000F)
                            {
                                FM.AS.setEngineStuck(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                            }
                            if(World.Rnd().nextFloat() < shot.power / 50000F)
                            {
                                FM.AS.hitEngine(shot.initiator, 0, 2);
                                Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                            }
                            FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                        }
                        getEnergyPastArmor(12.7F, shot);
                    } else
                    if(s.startsWith("xxeng1cyls"))
                    {
                        if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.12F)
                        {
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                            if(World.Rnd().nextFloat() < shot.power / 48000F)
                            {
                                FM.AS.hitEngine(shot.initiator, 0, 3);
                                Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                            }
                            if(World.Rnd().nextFloat() < 0.005F)
                            {
                                FM.AS.setEngineStuck(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                            }
                            getEnergyPastArmor(22.5F, shot);
                        }
                    } else
                    if(s.endsWith("eqpt"))
                    {
                        if(getEnergyPastArmor(0.2721F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                        {
                            if(World.Rnd().nextFloat() < 0.1F)
                            {
                                FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                                Aircraft.debugprintln(this, "*** Engine Module: Magneto 0 Destroyed..");
                            }
                            if(World.Rnd().nextFloat() < 0.1F)
                            {
                                FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                                Aircraft.debugprintln(this, "*** Engine Module: Magneto 1 Destroyed..");
                            }
                            if(World.Rnd().nextFloat() < 0.1F)
                            {
                                FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                                Aircraft.debugprintln(this, "*** Engine Module: Prop Controls Cut..");
                            }
                            if(World.Rnd().nextFloat() < 0.1F)
                            {
                                FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                                Aircraft.debugprintln(this, "*** Engine Module: Throttle Controls Cut..");
                            }
                            if(World.Rnd().nextFloat() < 0.1F)
                            {
                                FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                                Aircraft.debugprintln(this, "*** Engine Module: Mix Controls Cut..");
                            }
                        }
                    } else
                    if(s.endsWith("oil1"))
                    {
                        FM.AS.hitOil(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                    }
                }
                if(s.startsWith("xxoil"))
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    getEnergyPastArmor(0.22F, shot);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
                }
                if(s.startsWith("xxtank1") && getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F)
                {
                    if(FM.AS.astateTankStates[0] == 0)
                    {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        FM.AS.hitTank(shot.initiator, 0, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.hitTank(shot.initiator, 0, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
                if(s.startsWith("xxmgun"))
                {
                    if(s.endsWith("01"))
                    {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        FM.AS.setJamBullets(0, 0);
                    }
                    if(s.endsWith("02"))
                    {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        FM.AS.setJamBullets(0, 1);
                    }
                    if(s.endsWith("03"))
                    {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        FM.AS.setJamBullets(1, 0);
                    }
                    if(s.endsWith("04"))
                    {
                        Aircraft.debugprintln(this, "*** Cowling Gun: Disabled..");
                        FM.AS.setJamBullets(1, 1);
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
                }
            }
        } else
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
        {
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
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
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
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int j;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else
            {
                j = s.charAt(5) - 49;
            }
            hitFlesh(j, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 34:
            return super.cutFM(35, j, actor);

        case 37:
            return super.cutFM(38, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("HMask1_D0", false);
            break;
        }
    }

    private void stability()
    {
        double d = 0.0D;
        if(FM.getSpeedKMH() > 180F)
        {
            d = (FM.getSpeedKMH() - FM.VmaxFLAPS) / 10F;
            if(d < 0.0D)
                d = 0.0D;
        }
        Point3d point3d = new Point3d(0.0D, 0.0D, 0.0D);
        point3d.x = 0.0D - (FM.Or.getTangage() / 10F - FM.CT.getElevator() * 2.5D);
        point3d.y = 0.0D - (FM.Or.getKren() / 10F - FM.CT.getAileron() * 2.5D) - d;
        point3d.z = 2D;
        FM.EI.engines[0].setPropPos(point3d);
        FM.producedAF.x += 7000D * (-FM.CT.getElevator() * FM.EI.engines[0].getPowerOutput());
        FM.producedAF.y += 6000D * (-FM.CT.getAileron() * FM.EI.engines[0].getPowerOutput());
    }

    public void update(float f)
    {
        tiltRotor(f);
        stability();
        boolean flag = false;
        super.update(f);
        float f1 = FM.CT.getAirBrake();
        f1 = FM.CT.getAileron();
        if(Math.abs(pictAileron - f1) > 0.01F)
            pictAileron = f1;
        f1 = FM.CT.getRudder();
        if(Math.abs(pictRudder - f1) > 0.01F)
            pictRudder = f1;
        f1 = FM.CT.getElevator();
        if(Math.abs(pictVator - f1) > 0.01F)
            pictVator = f1;
        float f2 = FM.EI.getPowerOutput() * Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 600F, 2.0F, 0.0F);
        if(FM.CT.getAirBrake() > 0.5F)
        {
            if(FM.Or.getTangage() > 5F)
            {
                FM.getW().scale(Aircraft.cvt(FM.Or.getTangage(), 45F, 90F, 1.0F, 0.1F));
                float f3 = FM.Or.getTangage();
                if(Math.abs(FM.Or.getKren()) > 90F)
                    f3 = 90F + (90F - f3);
                float f4 = f3 - 90F;
                FM.CT.trimElevator = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                f4 = FM.Or.getKren();
                if(Math.abs(f4) > 90F)
                    if(f4 > 0.0F)
                        f4 = 180F - f4;
                    else
                        f4 = -180F - f4;
                FM.CT.trimAileron = Aircraft.cvt(f4, -20F, 20F, 0.5F, -0.5F);
                FM.CT.trimRudder = Aircraft.cvt(f4, -15F, 15F, 0.04F, -0.04F);
            }
        } else
        {
            FM.CT.trimAileron = 0.0F;
            FM.CT.trimElevator = 0.0F;
            FM.CT.trimRudder = 0.0F;
        }
        FM.Or.increment(f2 * (FM.CT.getRudder() + FM.CT.getTrimRudderControl()), f2 * (FM.CT.getElevator() + FM.CT.getTrimElevatorControl()), f2 * (FM.CT.getAileron() + FM.CT.getTrimAileronControl()));
    }

    private void tiltRotor(float f)
    {
        hierMesh().chunkSetAngles("Vator_D0", -(FM.CT.getElevator() * 3F), 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Arone_D0", 0.0F, FM.CT.getAileron() * 3F, 0.0F);
    }

    public static boolean bChangedPit = false;
    public Loc suka;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int rotorrpm;
    private float pictVBrake;
    private float pictAileron;
    private float pictVator;
    private float pictRudder;
    private float pictBlister;
    private static final float fcA[] = {
        0.0F, 0.04F, 0.1F, 0.04F, 0.02F, -0.02F, -0.04F, -0.1F, -0.04F
    };
    private static final float fcE[] = {
        0.98F, 0.48F, 0.1F, -0.48F, -0.7F, -0.7F, -0.48F, 0.1F, 0.48F
    };
    private static final float fcR[] = {
        0.02F, 0.48F, 0.8F, 0.48F, 0.28F, -0.28F, -0.48F, -0.8F, -0.48F
    };
    private float deltaAzimuth;
    private float deltaTangage;
    private static final float gearL2[] = {
        0.0F, 1.0F, 2.0F, 2.9F, 3.2F, 3.35F
    };
    private static final float gearL4[] = {
        0.0F, 7.5F, 15F, 22F, 29F, 35.5F
    };
    private static final float gearL5[] = {
        0.0F, 1.5F, 4F, 7.5F, 10F, 11.5F
    };

    static
    {
        Class class1 = H19D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "H19D");
        Property.set(class1, "meshName", "3DO/Plane/H19D/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/Sikorsky-HO4S.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitH19D.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 1,
            1
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05",
            "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15",
            "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_CANNON01",
            "_CANNON02"
        });
    }
}
