package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class B6N extends Scheme1
{

    public B6N()
    {
        arrestor = 0.0F;
        tme = 0L;
        topGunnerPosition = 1.0F;
        curTopGunnerPosition = 1.0F;
        radPosNum = 1;
        flapps = 0.0F;
    }

    protected void moveFlap(float f)
    {
        float f1 = -25F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.08F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.08F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.08F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.08F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
            if(FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.16F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.16F)
                FM.AS.hitTank(this, 1, 0);
            if(FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.16F)
                FM.AS.hitTank(this, 1, 3);
            if(FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.16F)
                FM.AS.hitTank(this, 1, 2);
        }
        for(int i = 1; i < 5; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    private void setRadist(int i)
    {
        radPosNum = i;
        if(FM.AS.astatePilotStates[2] > 90)
            return;
        hierMesh().chunkVisible("HMask3_D0", false);
        hierMesh().chunkVisible("HMask4_D0", false);
        switch(i)
        {
        case 0:
            topGunnerPosition = 0.0F;
            break;

        case 1:
            topGunnerPosition = 1.0F;
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
            if(f < -48F)
            {
                f = -48F;
                flag = false;
            }
            if(f > 48F)
            {
                f = 48F;
                flag = false;
            }
            if(f1 < -7F)
            {
                f1 = -7F;
                flag = false;
            }
            if(f1 > 65F)
            {
                f1 = 65F;
                flag = false;
            }
            break;

        case 1:
            if(f < -40F)
            {
                f = -40F;
                flag = false;
            }
            if(f > 40F)
            {
                f = 40F;
                flag = false;
            }
            if(f1 < -73F)
            {
                f1 = -73F;
                flag = false;
            }
            if(f1 > 4.5F)
            {
                f1 = 4.5F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 2:
        case 3:
            FM.turret[0].setHealth(f);
            FM.turret[1].setHealth(f);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 3:
        default:
            break;

        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Gore3_D0", true);
            break;

        case 2:
            if(hierMesh().isChunkVisible("Pilot3_D0"))
            {
                hierMesh().chunkVisible("Pilot3_D0", false);
                hierMesh().chunkVisible("Pilot3_D1", true);
                hierMesh().chunkVisible("HMask3_D0", false);
            } else
            {
                hierMesh().chunkVisible("Pilot4_D0", false);
                hierMesh().chunkVisible("Pilot4_D1", true);
                hierMesh().chunkVisible("HMask4_D0", false);
            }
            break;
        }
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        if(i >= 3)
        {
            doRemoveBodyChunkFromPlane("Pilot4");
            doRemoveBodyChunkFromPlane("Head4");
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxcontrols"))
            {
                if((s.endsWith("1") || s.endsWith("2")) && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.35F, shot) > 0.0F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 0);
                    debugprintln(this, "*** Aileron Controls Out..");
                }
                if(s.endsWith("3") && getEnergyPastArmor(0.2F, shot) > 0.0F)
                {
                    FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                }
                if(s.endsWith("4") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 1);
                    debugprintln(this, "*** Evelator Controls Out..");
                }
                if(s.endsWith("5") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(2.2F, shot) > 0.0F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 2);
                    debugprintln(this, "*** Rudder Controls Out..");
                }
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 85000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
                        debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.startsWith("xxeng1mag"))
                {
                    int i = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                }
                if(s.endsWith("oil1"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                        debuggunnery("Engine Module: Oil Radiator Hit..");
                    FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                if(getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    getEnergyPastArmor(0.22F, shot);
                    debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if(s.endsWith("li1") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.endsWith("ri1") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.endsWith("lm1") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.endsWith("rm1") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.endsWith("lo1") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.endsWith("ro1") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if((s.endsWith("sl1") || s.endsWith("sl2")) && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
                }
                if((s.endsWith("sr1") || s.endsWith("sr2")) && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
                }
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 48;
                int k = 0;
                switch(j)
                {
                case 1:
                    k = World.Rnd().nextInt(0, 0);
                    break;

                case 2:
                    k = World.Rnd().nextInt(0, 1);
                    break;

                case 3:
                    k = World.Rnd().nextInt(1, 1);
                    break;

                case 4:
                    k = World.Rnd().nextInt(2, 2);
                    break;

                case 5:
                    k = World.Rnd().nextInt(2, 3);
                    break;

                case 6:
                    k = World.Rnd().nextInt(2, 3);
                    break;
                }
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    FM.AS.hitTank(shot.initiator, k, 1);
                    if(World.Rnd().nextFloat() < 0.05F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F)
                        FM.AS.hitTank(shot.initiator, k, 2);
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf"))
            hitChunk("CF", shot);
        else
        if(!s.startsWith("xblister"))
            if(s.startsWith("xeng"))
                hitChunk("Engine1", shot);
            else
            if(s.startsWith("xtail"))
                hitChunk("Tail1", shot);
            else
            if(s.startsWith("xkeel"))
            {
                if(chunkDamageVisible("Keel1") < 2)
                    hitChunk("Keel1", shot);
            } else
            if(s.startsWith("xrudder"))
            {
                if(chunkDamageVisible("Rudder1") < 2)
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
                if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 2)
                    hitChunk("VatorL", shot);
                if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 2)
                    hitChunk("VatorR", shot);
            } else
            if(s.startsWith("xwing"))
            {
                if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                    hitChunk("WingLIn", shot);
                if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                    hitChunk("WingRIn", shot);
                if(s.startsWith("xwinglmid"))
                {
                    if(chunkDamageVisible("WingLMid") < 3)
                        hitChunk("WingLMid", shot);
                    if(World.Rnd().nextFloat() < shot.mass + 0.02F)
                        FM.AS.hitOil(shot.initiator, 0);
                }
                if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                    hitChunk("WingRMid", shot);
                if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                    hitChunk("WingLOut", shot);
                if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                    hitChunk("WingROut", shot);
            } else
            if(s.startsWith("xarone"))
            {
                if(s.startsWith("xaronel"))
                    hitChunk("AroneL", shot);
                if(s.startsWith("xaroner"))
                    hitChunk("AroneR", shot);
            } else
            {
                if(s.startsWith("xArr"))
                    return;
                if(s.startsWith("xgearc"))
                    hitChunk("GearC2", shot);
                else
                if(s.startsWith("xgearl"))
                    hitChunk("GearL2", shot);
                else
                if(s.startsWith("xgearr"))
                    hitChunk("GearR2", shot);
                else
                if(s.startsWith("xturret1"))
                {
                    if(getEnergyPastArmor(0.25F, shot) > 0.0F)
                    {
                        debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                        FM.AS.setJamBullets(10, 0);
                        getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
                    }
                } else
                if(s.startsWith("xturret1"))
                {
                    if(getEnergyPastArmor(0.25F, shot) > 0.0F)
                    {
                        debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                        FM.AS.setJamBullets(11, 0);
                        getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
                    }
                } else
                if(s.startsWith("xpilot") || s.startsWith("xhead"))
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
                    if(l == 3)
                        l = 2;
                    hitFlesh(l, shot, byte0);
                }
            }
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 0.01F, 0.99F, 0.0F, -135F), 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 0.01F, 0.99F, 0.0F, -135F), 0.0F);
    }

    public void moveWingFold(float f)
    {
        moveWingFold(hierMesh(), f);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19:
            FM.CT.bHasArrestorControl = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.1F, 0.7F, 0.0F, -83.5F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.1F, 0.15F, 0.0F, -88F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.1F, 0.7F, 0.0F, -56F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, cvt(f, 0.1F, 0.7F, 0.0F, -180F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.3F, 0.9F, 0.0F, -83.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.3F, 0.35F, 0.0F, -88F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f, 0.3F, 0.9F, 0.0F, -56F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, cvt(f, 0.3F, 0.9F, 0.0F, -180F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -56F * f, 0.0F);
    }

    public void moveWheelSink()
    {
    }

    public void update(float f)
    {
        super.update(f);
        if(Time.current() > tme)
        {
            tme = Time.current() + World.Rnd().nextLong(1000L, 5000L);
            if(FM.turret.length != 0)
            {
                FM.AS.astatePilotStates[2] = FM.AS.astatePilotStates[3] = (byte)Math.max(FM.AS.astatePilotStates[2], FM.AS.astatePilotStates[3]);
                Actor actor = null;
                for(int j = 0; j < 2; j++)
                    if(FM.turret[j].bIsOperable)
                        actor = FM.turret[j].target;

                for(int k = 1; k < 2; k++)
                    FM.turret[k].target = actor;

                if(actor != null && Actor.isValid(actor))
                {
                    pos.getAbs(tmpLoc2);
                    actor.pos.getAbs(tmpLoc3);
                    tmpLoc2.transformInv(tmpLoc3.getPoint());
                    if(tmpLoc3.getPoint().z > 0.0D)
                        setRadist(1);
                    else
                        setRadist(0);
                }
            }
        }
        if(FM.AS.astatePilotStates[2] < 90)
        {
            if(topGunnerPosition > 0.5F)
            {
                curTopGunnerPosition += 0.1F * f;
                if(curTopGunnerPosition > 1.0F)
                    curTopGunnerPosition = 1.0F;
            } else
            {
                curTopGunnerPosition -= 0.1F * f;
                if(curTopGunnerPosition < 0.0F)
                    curTopGunnerPosition = 0.0F;
            }
            if(curTopGunnerPosition <= 0.1F);
            if(curTopGunnerPosition >= 0.9F);
            FM.turret[0].bIsOperable = true;
            FM.turret[1].bIsOperable = true;
        }
        hierMesh().chunkVisible("Turret2B_D0", curTopGunnerPosition <= 0.01F);
        hierMesh().chunkVisible("Gun_D0", curTopGunnerPosition > 0.01F);
        resetYPRmodifier();
        xyz[0] = cvt(curTopGunnerPosition, 0.0F, 0.15F, 0.43105F, 0.0F);
        xyz[1] = cvt(curTopGunnerPosition, 0.0F, 0.15F, 0.396F, 0.0F);
        xyz[2] = cvt(curTopGunnerPosition, 0.0F, 0.15F, -0.7906F, 0.0F);
        hierMesh().chunkSetLocate("Gun_D0", xyz, ypr);
        hierMesh().chunkSetAngles("Blister4_D0", 0.0F, cvt(curTopGunnerPosition, 0.25F, 0.35F, -30F, 0.0F), 0.0F);
        hierMesh().chunkSetAngles("Blister5_D0", 0.0F, cvt(curTopGunnerPosition, 0.25F, 0.35F, -180F, 0.0F), 0.0F);
        hierMesh().chunkSetAngles("Blister3_D0", 0.0F, cvt(curTopGunnerPosition, 0.4F, 0.5F, -32F, 0.0F), 0.0F);
        hierMesh().chunkSetAngles("Blister6_D0", 0.0F, cvt(curTopGunnerPosition, 0.4F, 0.5F, -40F, 0.0F), 0.0F);
        if(FM.AS.astatePilotStates[2] < 90)
        {
            hierMesh().chunkVisible("Pilot4_D0", curTopGunnerPosition <= 0.75F);
            resetYPRmodifier();
            xyz[1] = cvt(curTopGunnerPosition, 0.5F, 0.75F, 0.0F, -0.45715F);
            xyz[2] = cvt(curTopGunnerPosition, 0.5F, 0.75F, 0.0F, 0.239F);
            ypr[2] = cvt(curTopGunnerPosition, 0.5F, 0.75F, 0.0F, -40F);
            hierMesh().chunkSetLocate("Pilot4_D0", xyz, ypr);
        }
        if(FM.AS.astatePilotStates[2] < 90)
        {
            hierMesh().chunkVisible("Pilot3_D0", curTopGunnerPosition > 0.75F);
            resetYPRmodifier();
            xyz[1] = cvt(curTopGunnerPosition, 0.75F, 1.0F, 0.0443F, 0.0F);
            xyz[2] = cvt(curTopGunnerPosition, 0.75F, 1.0F, -0.1485F, 0.0F);
            ypr[2] = cvt(curTopGunnerPosition, 0.75F, 1.0F, -45F, 0.0F);
            hierMesh().chunkSetLocate("Pilot3_D0", xyz, ypr);
        }
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f1 = cvt(FM.Gears.arrestorVAngle, -51F, 5F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f1;
        } else
        {
            float f2 = (-51F * FM.Gears.arrestorVSink) / 56F;
            if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
                Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if(f2 > 0.15F)
                f2 = 0.15F;
            arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
            if(arrestor < 0.0F)
                arrestor = 0.0F;
        }
        if(arrestor > FM.CT.getArrestor())
            arrestor = FM.CT.getArrestor();
        moveArrestorHook(arrestor);
        float f3 = FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f3) > 0.02F)
        {
            flapps = f3;
            for(int i = 1; i < 11; i++)
                hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -22F * f3, 0.0F);

        }
        if(FM.AS.astateBailoutStep > 1)
            hierMesh().chunkVisible("Turret2B_D0", hierMesh().isChunkVisible("Blister5_D0"));
    }

    private float arrestor;
    private long tme;
    private float topGunnerPosition;
    private float curTopGunnerPosition;
    private int radPosNum;
    private float flapps;

    static 
    {
        Class class1 = B6N.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
