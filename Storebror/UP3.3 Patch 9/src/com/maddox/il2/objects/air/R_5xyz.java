package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class R_5xyz extends Scheme1
    implements TypeScout, TypeTransport
{

    public R_5xyz()
    {
        radPos = 0.0F;
        flapps = 0.0F;
        suspR = 0.0F;
        suspL = 0.0F;
        gunnerDead = false;
        gunnerEjected = false;
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        bChangedPit = false;
        CompassDelta = 0.0F;
        gunnerAnimation = 0.0F;
        tme0 = 0L;
        strafeWithGuns = false;
    }
    
//    public void onAircraftLoaded()
//    {
//        Mat mat = this.hierMesh().material((this.hierMesh().materialFind("Pilot1")));
//        String s = mat.Name();
//        mat.setLayer(0);
//        mat.get('\0');
//        mat = this.hierMesh().material((this.hierMesh().materialFind("Pilot1")));
//        this.hierMesh().materialReplace("Pilot2_FAKE", mat);
//        
//    }


    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public boolean cut(String s)
    {
        boolean flag = super.cut(s);
        if(s.equalsIgnoreCase("WingLIn"))
            hierMesh().chunkVisible("WingLMid_CAP", true);
        else
        if(s.equalsIgnoreCase("WingRIn"))
            hierMesh().chunkVisible("WingRMid_CAP", true);
        return flag;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 36:
            bDynamoOperational = false;
            pk = 1;
            hierMesh().chunkVisible("Prop2_d0", false);
            hierMesh().chunkVisible("PropRot2_d0", false);
            break;

        case 19:
            FM.Gears.hitCentreGear();
            break;

        case 9:
            if(hierMesh().chunkFindCheck("GearL0_D0") != -1)
            {
                hierMesh().hideSubTrees("GearL0_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("GearL0_D0"));
                wreckage.collide(true);
                FM.Gears.hitLeftGear();
            }
            break;

        case 10:
            if(hierMesh().chunkFindCheck("GearR0_D0") != -1)
            {
                hierMesh().hideSubTrees("GearR0_D0");
                Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("GearR0_D0"));
                wreckage1.collide(true);
                FM.Gears.hitRightGear();
            }
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveRudder(float f)
    {
        float f1 = -22F * f;
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Rudder1a_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Rudder1b_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("GearC2_D0", -f1, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        float f1 = 0.0F;
        float f2 = 0.0F;
        float f3 = 0.0F;
        if(f > 0.0F)
        {
            f1 = -20.06F * f;
            f2 = -18.96F * f;
            f3 = -20F;
        } else
        if(f < 0.0F)
        {
            f1 = -17.773F * f;
            f2 = -19.38F * f;
            f3 = -19F;
        }
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, f3 * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, f3 * f, 0.0F);
        hierMesh().chunkSetAngles("VatorLa_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("VatorLb_D0", 0.0F, f2, 0.0F);
        hierMesh().chunkSetAngles("VatorRa_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("VatorRb_D0", 0.0F, f2, 0.0F);
    }

    protected void moveAileron(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 10F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 32F * f, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 32F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 10F * f, 0.0F);
        }
    }

    public void moveWheelSink()
    {
        suspL = 0.9F * suspL + 0.1F * FM.Gears.gWheelSinking[0];
        suspR = 0.9F * suspR + 0.1F * FM.Gears.gWheelSinking[1];
        if(suspL > 0.035F)
            suspL = 0.035F;
        if(suspR > 0.035F)
            suspR = 0.035F;
        if(suspL < 0.0F)
            suspL = 0.0F;
        if(suspR < 0.0F)
            suspR = 0.0F;
        float f = 40F;
        hierMesh().chunkSetAngles("GearL2_D0", 0.0F, suspL * -12F * f, 0.0F);
        hierMesh().chunkSetAngles("GearL0_D0", 0.0F, suspL * -4F * f, 0.0F);
        hierMesh().chunkSetAngles("GearL3_D0", 0.0F, suspL * -10F * f, 0.0F);
        hierMesh().chunkSetAngles("GearR2_D0", 0.0F, suspR * -12F * f, 0.0F);
        hierMesh().chunkSetAngles("GearR0_D0", 0.0F, suspR * -4F * f, 0.0F);
        hierMesh().chunkSetAngles("GearR3_D0", 0.0F, suspR * -10F * f, 0.0F);
    }

    public void updateRadiator()
    {
        float f = FM.EI.engines[0].getControlRadiator();
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.xyz[2] = f * -0.45F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        if(Math.abs(flapps - Aircraft.xyz[2]) > 0.01F)
        {
            flapps = Aircraft.xyz[2];
            hierMesh().chunkSetLocate("Water_D0", Aircraft.xyz, Aircraft.ypr);
        }
    }

    protected void moveFan(float f)
    {
        if(bDynamoOperational)
        {
            pk = Math.abs((int)(FM.Vwld.length() / 14D));
            if(pk >= 1)
                pk = 1;
        }
        if(bDynamoRotary != (pk == 1))
        {
            bDynamoRotary = pk == 1;
            hierMesh().chunkVisible("Prop2_d0", !bDynamoRotary);
            hierMesh().chunkVisible("PropRot2_d0", bDynamoRotary);
        }
        if(bDynamoOperational)
        {
            dynamoOrient = bDynamoRotary ? (dynamoOrient - 17.987F) % 360F : (float)((double)dynamoOrient - FM.Vwld.length() * 1.5444015264511108D) % 360F;
            hierMesh().chunkSetAngles("Prop2_d0", 0.0F, dynamoOrient, 0.0F);
        }
        super.moveFan(f);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        if(hiermesh.chunkFindCheck("SkiR1_D0") != -1)
        {
            float f1 = 15F;
            hiermesh.chunkSetAngles("SkiL0_D0", 0.0F, -f1, 0.0F);
            hiermesh.chunkSetAngles("SkiR0_D0", 0.0F, -f1, 0.0F);
            hiermesh.chunkSetAngles("SkiC_D0", 0.0F, f1, 0.0F);
            float f2 = f1 / 20F;
            float f3 = f2 * -5F;
            float f4 = f2 * 10F;
            float f5 = f2 * 15F;
            float f6 = f2 * 20F;
            hiermesh.chunkSetAngles("SkiL1_D0", 0.0F, f2 * -2F + 0.00875F, f2 * 8.25F + 0.105F);
            hiermesh.chunkSetAngles("SkiL2_D0", 0.0F, f2 * -7F + 0.04375F, f2 * -6.25F + 0.09625F);
            hiermesh.chunkSetAngles("SkiL3_D0", 0.0F, f2 * 40F, f2 * 70F);
            hiermesh.chunkSetAngles("SkiL4_D0", 0.0F, -f3, -f6);
            hiermesh.chunkSetAngles("SkiL5_D0", 0.0F, -f4, -f6);
            hiermesh.chunkSetAngles("SkiL6_D0", 0.0F, -f5, -f6);
            hiermesh.chunkSetAngles("SkiL7_D0", 0.0F, -f4, -f6);
            hiermesh.chunkSetAngles("SkiL8_D0", 0.0F, -f4, -f6);
            hiermesh.chunkSetAngles("SkiR1_D0", 0.0F, f2 * 2.0F - 0.00875F, f2 * 8.25F + 0.105F);
            hiermesh.chunkSetAngles("SkiR2_D0", 0.0F, f2 * -7F + 0.04375F, f2 * -6.25F + 0.09625F);
            hiermesh.chunkSetAngles("SkiR3_D0", 0.0F, f2 * 40F, f2 * 70F);
            hiermesh.chunkSetAngles("SkiR4_D0", 0.0F, -f4, -f6);
            hiermesh.chunkSetAngles("SkiR5_D0", 0.0F, -f4, -f6);
            hiermesh.chunkSetAngles("SkiR6_D0", 0.0F, -f3, -f6);
            hiermesh.chunkSetAngles("SkiR7_D0", 0.0F, -f5, -f6);
            hiermesh.chunkSetAngles("SkiR8_D0", 0.0F, -f4, -f6);
        }
    }

    protected void moveFlap(float f)
    {
    }

    public void auxPlus(int i)
    {
        switch(i)
        {
        case 1:
            CompassDelta++;
            break;
        }
    }

    public void auxMinus(int i)
    {
        switch(i)
        {
        case 1:
            CompassDelta--;
            break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                    getEnergyPastArmor(9.96F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(s.endsWith("7"))
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#7)");
                    }
                } else
                if(s.endsWith("8"))
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#8)");
                    }
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out.. (#8)");
                    }
                } else
                if(s.endsWith("5"))
                {
                    if(World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#5)");
                    }
                } else
                if(s.endsWith("6"))
                {
                    if(World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#6)");
                    }
                } else
                if((s.endsWith("2") || s.endsWith("4")) && World.Rnd().nextFloat() < 0.5F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Arone Controls Out.. (#2/#4)");
                }
                return;
            }
            if(s.startsWith("xxeng") || s.startsWith("xxEng"))
            {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if(s.endsWith("prop"))
                    Aircraft.debugprintln(this, "*** Prop hit");
                else
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
                if(s.endsWith("Oil1"))
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                FM.AS.hitOil(shot.initiator, 0);
                getEnergyPastArmor(0.22F, shot);
                Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x80);
            } else
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.4F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F)
                {
                    if(FM.AS.astateTankStates[i] == 0)
                    {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        FM.AS.hitTank(shot.initiator, i, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.hitTank(shot.initiator, i, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            } else
            if(s.startsWith("xxlock"))
            {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                } else
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                } else
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
            } else
            if(s.startsWith("xMgun"))
            {
                if(s.endsWith("1"))
                {
                    Aircraft.debugprintln(this, "*** Rear Turret Gun #1: Disabled..");
                    FM.AS.setJamBullets(10, 0);
                }
                if(s.endsWith("2"))
                {
                    Aircraft.debugprintln(this, "*** Rear Turret Gun #2: Disabled..");
                    FM.AS.setJamBullets(10, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else
            if(s.startsWith("xxgun"))
            {
                if(s.endsWith("0"))
                {
                    Aircraft.debugprintln(this, "*** Fixed CF Gun #1: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                } else
                if(s.endsWith("3"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Wing Gun #1: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                } else
                if(s.endsWith("4"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Wing Gun #2: Disabled..");
                    FM.AS.setJamBullets(0, 2);
                } else
                if(s.endsWith("5"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Wing Gun #3: Disabled..");
                    FM.AS.setJamBullets(0, 3);
                } else
                if(s.endsWith("6"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Wing Gun #4: Disabled..");
                    FM.AS.setJamBullets(0, 4);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else
            if(s.startsWith("xxcccmgun"))
            {
                if(s.endsWith("1"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #1: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                } else
                if(s.endsWith("2"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #2: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                } else
                if(s.endsWith("3"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #3: Disabled..");
                    FM.AS.setJamBullets(0, 2);
                } else
                if(s.endsWith("4"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #4: Disabled..");
                    FM.AS.setJamBullets(0, 3);
                } else
                if(s.endsWith("5"))
                {
                    Aircraft.debugprintln(this, "*** Rear Turret Gun #1: Disabled..");
                    FM.AS.setJamBullets(10, 0);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else
            if(s.startsWith("xxspar"))
            {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(9.5F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                } else
                if(s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D" + chunkDamageVisible("WingLIn"), shot.initiator);
                } else
                if(s.startsWith("xxspar2i") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D" + chunkDamageVisible("WingRIn"), shot.initiator);
                } else
                if(s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                } else
                if(s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                } else
                if(s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                } else
                if(s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                }
            }
            return;
        }
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
            if(j == 2)
                j = 1;
            Aircraft.debugprintln(this, "*** hitFlesh..");
            hitFlesh(j, shot, byte0);
        } else
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(s.startsWith("xcf0") || s.startsWith("xcf1") || s.startsWith("xcf2"))
            {
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            }
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
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xWing"))
        {
            Aircraft.debugprintln(this, "*** xWing: " + s);
            if(s.startsWith("xWingLIn") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xWingRIn") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xWingLmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xWingRmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
        } else
        if(s.startsWith("xwing"))
        {
            Aircraft.debugprintln(this, "*** xwing: " + s);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL1", shot);
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR1", shot);
        }
    }

    public float getGunnerAnimation()
    {
        return gunnerAnimation;
    }

    public void setGunnerAnimation(float gunnerAnimation)
    {
        this.gunnerAnimation = gunnerAnimation;
    }

    public void gunnerTarget2()
    {
        if(gunnerDead || gunnerEjected)
        {
            FM.turret[0].bIsOperable = false;
            return;
        }
        if(!FM.turret[0].bIsAIControlled)
        {
            if(!bMultiFunction)
//            if(!FM.turret[0].bMultiFunction)
            {
                FM.turret[0].tu[0] = FM.turret[0].tu[1] = FM.turret[0].tu[2] = 0.0F;
                hierMesh().setCurChunk(FM.turret[0].indexA);
                hierMesh().chunkSetAngles(FM.turret[0].tu);
                hierMesh().setCurChunk(FM.turret[0].indexB);
                hierMesh().chunkSetAngles(FM.turret[0].tu);
                if((double)gunnerAnimation <= 0.0D)
                    FM.turret[0].bIsOperable = false;
            } else
            if(gunnerAnimation >= 1.0F)
                FM.turret[0].bIsOperable = true;
        } else
        if(Time.current() > tme0 && !bPlayerTurret)
        {
            tme0 = Time.current() + 2000L;
            if(FM.turret.length != 0)
            {
                Actor actor = War.GetNearestEnemy(this, 16, 7000F);
                Aircraft aircraft = War.getNearestEnemy(this, 6000F);
                if(actor != null && !(actor instanceof BridgeSegment) || aircraft != null)
                {
                    bMultiFunction = true;
//                    FM.turret[0].bMultiFunction = true;
                    if((double)gunnerAnimation >= 1.0D)
                        FM.turret[0].bIsOperable = true;
                    FM.turret[0].bIsAIControlled = true;
                } else
                {
                    bMultiFunction = false;
//                    FM.turret[0].bMultiFunction = false;
                    FM.turret[0].tu[0] = FM.turret[0].tu[1] = FM.turret[0].tu[2] = 0.0F;
                    hierMesh().setCurChunk(FM.turret[0].indexA);
                    hierMesh().chunkSetAngles(FM.turret[0].tu);
                    hierMesh().setCurChunk(FM.turret[0].indexB);
                    hierMesh().chunkSetAngles(FM.turret[0].tu);
                    if((double)gunnerAnimation <= 0.0D)
                        FM.turret[0].bIsOperable = false;
                    FM.turret[0].bIsAIControlled = true;
                }
            }
        }
    }

    public void gunnerAiming()
    {
//        System.out.println("gunnerAiming() bMultiFunction=" + bMultiFunction + ", gunnerAnimation=" + gunnerAnimation);
        if(bMultiFunction || bPlayerTurret || (Config.isUSE_RENDER() && Main3D.cur3D().cockpitCurIndx() == 3))
//          if(FM.turret[0].bMultiFunction)
        {
            if(gunnerAnimation < 1.0F)
            {
                gunnerAnimation += 0.0125F;
                moveGunner();
            }
        } else
        if(gunnerAnimation > 0.0F)
        {
            gunnerAnimation -= 0.0125F;
            moveGunner();
        }
    }

    public void CCCgunnerAiming()
    {
        if(bMultiFunction)
//        if(FM.turret[0].bMultiFunction)
        {
            if((double)gunnerAnimation < 1.0D)
            {
                gunnerAnimation += 0.0125F;
                CCCmoveGunner();
            }
        } else
        if((double)gunnerAnimation > 0.0D)
        {
            gunnerAnimation -= 0.0125F;
            CCCmoveGunner();
        }
    }

    public void moveGunner()
    {
        if(gunnerDead || gunnerEjected)
        {
            FM.turret[0].bIsOperable = false;
            return;
        }
//        System.out.println("moveGunner() gunnerAnimation=" + gunnerAnimation);
        if(gunnerAnimation < 1.0F)
        {
            FM.turret[0].bIsOperable = false;
            if (!bMultiFunction) {
                hierMesh().chunkVisible("Turret1A_D0", false);
                hierMesh().chunkVisible("Turret1B_D0", false);
                hierMesh().chunkVisible("Turret1C_D0", false);
                hierMesh().chunkVisible("Turret1A_FAKE", true);
                hierMesh().chunkVisible("Turret1B_FAKE", true);
            }
        } else
        {
            FM.turret[0].bIsOperable = true;
            if (!bMultiFunction) {
                hierMesh().chunkVisible("Turret1A_D0", true);
                hierMesh().chunkVisible("Turret1B_D0", true);
                hierMesh().chunkVisible("Turret1C_D0", true);
                hierMesh().chunkVisible("Turret1A_FAKE", false);
                hierMesh().chunkVisible("Turret1B_FAKE", false);
            }
        }
        if(gunnerAnimation > 0.5F)
        {
            hierMesh().chunkVisible("Pilot3_D0", false);
            if(gunnerAnimation < 1.0F)
            {
                hierMesh().chunkVisible("Pilot2_D0", false);
                hierMesh().chunkVisible("HMask2_D0", false);
                hierMesh().chunkVisible("Pilot2_FAKE", true);
//                System.out.println("Pilot2_D0 false");
//                System.out.println("Pilot2_FAKE true");
                if(FM.getAltitude() > 3000F)
                    hierMesh().chunkVisible("HMask2_FAKE", true);
                else
                    hierMesh().chunkVisible("HMask2_FAKE", false);
                hierMesh().chunkSetAngles("Pilot2_FAKE", 0.0F, 0.0F, 0.0F);
            } else
            {
                hierMesh().chunkVisible("Pilot2_D0", true);
                if(FM.getAltitude() > 3000F)
                    hierMesh().chunkVisible("HMask2_D0", true);
                else
                    hierMesh().chunkVisible("HMask2_D0", false);
                hierMesh().chunkVisible("Pilot2_FAKE", false);
                hierMesh().chunkVisible("HMask2_FAKE", false);
//                System.out.println("Pilot2_D0 true");
//                System.out.println("Pilot2_FAKE false");
            }
            float f = 120F * (gunnerAnimation - 0.5F) - 60F;
            hierMesh().chunkSetAngles("Turret1A_FAKE", 0.0F, f, 0.0F);
            hierMesh().chunkSetAngles("Turret1B_FAKE", -f, 0.0F, 0.0F);
        } else
        if(gunnerAnimation > 0.25F)
        {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = (gunnerAnimation - 0.5F) * 0.5F;
            Aircraft.ypr[0] = -120F + 480F * (gunnerAnimation - 0.25F);
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            hierMesh().chunkSetLocate("Pilot2_FAKE", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkVisible("Pilot2_FAKE", true);
            if(FM.getAltitude() > 3000F)
                hierMesh().chunkVisible("HMask2_FAKE", true);
            else
                hierMesh().chunkVisible("HMask2_FAKE", false);
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkSetAngles("Turret1A_FAKE", 0.0F, -60F, 0.0F);
            hierMesh().chunkSetAngles("Turret1B_FAKE", 60F, 0.0F, 0.0F);
//            System.out.println("Pilot3_D0 false");
//            System.out.println("Pilot2_FAKE true");
        } else
        {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = gunnerAnimation * 0.5F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_FAKE", false);
            hierMesh().chunkVisible("HMask2_FAKE", false);
            hierMesh().chunkVisible("Pilot3_D0", true);
            if(FM.getAltitude() > 3000F)
                hierMesh().chunkVisible("HMask3_D0", true);
            else
                hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkSetAngles("Turret1A_FAKE", 0.0F, -60F, 0.0F);
            hierMesh().chunkSetAngles("Turret1B_FAKE", 60F, 0.0F, 0.0F);
//            System.out.println("Pilot2_D0 false");
//            System.out.println("Pilot3_D0 true");
//            System.out.println("Pilot2_FAKE false");
        }
    }

    public void CCCmoveGunner()
    {
        if(gunnerDead || gunnerEjected)
        {
            FM.turret[0].bIsOperable = false;
            return;
        }
        if(gunnerAnimation < 1.0F)
        {
            FM.turret[0].bIsOperable = false;
            if (!bMultiFunction) {
                hierMesh().chunkVisible("Turret1B_D0", false);
                hierMesh().chunkVisible("Turret1C_D0", false);
                hierMesh().chunkVisible("Turret1D_D0", true);
            }
        } else
        {
            FM.turret[0].bIsOperable = true;
            if (!bMultiFunction) {
                hierMesh().chunkVisible("Turret1B_D0", true);
                hierMesh().chunkVisible("Turret1C_D0", true);
                hierMesh().chunkVisible("Turret1D_D0", false);
            }
        }
        if(gunnerAnimation > 0.75F)
        {
            hierMesh().chunkVisible("Pilot3_D0", false);
            if(gunnerAnimation < 1.0F)
            {
                Aircraft.xyz[0] = 0.0F;
                Aircraft.xyz[1] = 0.0F;
                Aircraft.xyz[2] = -0.03F;
                Aircraft.ypr[0] = 0.0F;
                Aircraft.ypr[1] = 0.0F;
                Aircraft.ypr[2] = 0.0F;
                hierMesh().chunkSetLocate("Pilot2_D0", Aircraft.xyz, Aircraft.ypr);
            }
            float f = Aircraft.cvt(gunnerAnimation, 0.75F, 1.0F, 0.0F, 70F);
            hierMesh().chunkSetAngles("Turret1D_D0", 0.0F, 0.0F, f);
        } else
        if(gunnerAnimation > 0.25F)
        {
            hierMesh().chunkVisible("Pilot2_D0", true);
            if(FM.getAltitude() > 3000F)
                hierMesh().chunkVisible("HMask2_D0", true);
            else
                hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = Aircraft.cvt(gunnerAnimation, 0.25F, 0.75F, -0.2F, -0.03F);
            Aircraft.ypr[0] = Aircraft.cvt(gunnerAnimation, 0.25F, 0.75F, -180F, 0.0F);
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            hierMesh().chunkSetLocate("Pilot2_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("Turret1D_D0", 0.0F, 0.0F, 0.0F);
        } else
        {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = Aircraft.cvt(gunnerAnimation, 0.0F, 0.25F, 0.0F, 0.11F);
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot3_D0", true);
            if(FM.getAltitude() > 3000F)
                hierMesh().chunkVisible("HMask3_D0", true);
            else
                hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkSetAngles("Turret1D_D0", 0.0F, 0.0F, 0.0F);
        }
    }
    
    float radPos;
    private float flapps;
    float suspR;
    float suspL;
    public boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;
    public boolean gunnerDead;
    public boolean gunnerEjected;
    public float CompassDelta;
    public static boolean bChangedPit;
    private float gunnerAnimation;
    private long tme0;
    public boolean strafeWithGuns;
    public boolean bMultiFunction;
    public boolean bPlayerTurret;

    static 
    {
        Class class1 = R_5xyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
