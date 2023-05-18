package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class Martin_B10_xyz extends Scheme2
{

    public Martin_B10_xyz()
    {
        bChangedExts = false;
        bChangedPit = true;
        fSightSetForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 250F;
        fSightCurReadyness = 0.0F;
        tme = 0L;
        pilot2kill = false;
        blisTurOpen = false;
        hasAdditionalBlisterMeshes = true;
    }

    protected void moveFlap(float f)
    {
        this.doMoveFlap(-45F * f);
    }

    protected void doMoveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap05_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("Flap06_D0", 0.0F, f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.095F, 1.0F, 0.0F, 97F));
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.095F, 1.0F, 0.0F, 97F));
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.13F, 0.0F, -50F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.13F, 0.0F, -50F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.9F, 0.0F, 141F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.9F, 0.0F, 141F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
    }

    public void moveWheelSink()
    {
    }

    public void rareAction(float f, boolean flag)
    {
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        if(!pilot2kill && aircraft != null && !this.FM.AS.bIsAboutToBailout)
        {
            hierMesh().chunkVisible("Blister2open_D0", true);
            hierMesh().chunkVisible("Blister4open_D0", true);
            hierMesh().chunkVisible("Turret3B_D0", true);
            hierMesh().chunkVisible("Blister2closed_D0", false);
            hierMesh().chunkVisible("Blister4closed_D0", false);
            if (this.hasAdditionalBlisterMeshes) {
                hierMesh().chunkVisible("Blister2glassopen_D0", true);
                hierMesh().chunkVisible("Turret2B_D0", true);
                hierMesh().chunkVisible("Blister2glassclosed_D0", false);
                hierMesh().chunkVisible("Turret2Bclosed_D0", false);
                hierMesh().chunkVisible("RAIL_L_D0", false);
                hierMesh().chunkVisible("RAIL_R_D0", false);
            }
            blisTurOpen = true;
        }
        if(!pilot2kill && aircraft == null && !this.FM.AS.bIsAboutToBailout && blisTurOpen)
        {
            hierMesh().chunkVisible("Blister2open_D0", false);
            hierMesh().chunkVisible("Blister4open_D0", false);
            hierMesh().chunkVisible("Turret3B_D0", false);
            hierMesh().chunkVisible("Blister2closed_D0", true);
            hierMesh().chunkVisible("Blister4closed_D0", true);
            if (this.hasAdditionalBlisterMeshes) {
                hierMesh().chunkVisible("Blister2glassopen_D0", false);
                hierMesh().chunkVisible("Turret2B_D0", false);
                hierMesh().chunkVisible("Blister2glassclosed_D0", true);
                hierMesh().chunkVisible("Turret2Bclosed_D0", true);
                hierMesh().chunkVisible("RAIL_L_D0", true);
                hierMesh().chunkVisible("RAIL_R_D0", true);
            }
            blisTurOpen = false;
        }
        if(FM.getAltitude() < 3000F)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("HMask2a_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkVisible("HMask3a_D0", false);
        } else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
            hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
            hierMesh().chunkVisible("HMask2a_D0", hierMesh().isChunkVisible("Pilot2a_D0"));
            hierMesh().chunkVisible("HMask3_D0", hierMesh().isChunkVisible("Pilot3_D0"));
            hierMesh().chunkVisible("HMask3a_D0", hierMesh().isChunkVisible("Pilot3a_D0"));
        }
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3)
            {
                if(World.Rnd().nextFloat() < 0.03F)
                    FM.AS.hitTank(this, 0, 1);
                if(World.Rnd().nextFloat() < 0.03F)
                    FM.AS.hitTank(this, 1, 1);
            }
            if(FM.AS.astateEngineStates[1] > 3)
            {
                if(World.Rnd().nextFloat() < 0.03F)
                    FM.AS.hitTank(this, 2, 1);
                if(World.Rnd().nextFloat() < 0.03F)
                    FM.AS.hitTank(this, 3, 1);
            }
            if(FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.03F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.03F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F)
                FM.AS.hitTank(this, 2, 1);
            if(FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.03F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.03F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
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
            if(f1 < -40F)
            {
                f1 = -40F;
                flag = false;
            }
            if(f1 > 40F)
            {
                f1 = 40F;
                flag = false;
            }
            break;

        case 1:
            if(f < -33F)
            {
                f = -33F;
                flag = false;
            }
            if(f > 33F)
            {
                f = 33F;
                flag = false;
            }
            if(f1 < -3F)
            {
                f1 = -3F;
                flag = false;
            }
            if(f1 > 62F)
            {
                f1 = 62F;
                flag = false;
            }
            break;

        case 2:
            if(f < -25F)
            {
                f = -25F;
                flag = false;
            }
            if(f > 25F)
            {
                f = 25F;
                flag = false;
            }
            if(f1 < -90F)
            {
                f1 = -90F;
                flag = false;
            }
            if(f1 > 6F)
            {
                f1 = 6F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 35:
        default:
            break;

        case 33:
            hitProp(0, j, actor);
            break;

        case 36:
            hitProp(1, j, actor);
            break;

        case 34:
            FM.AS.hitEngine(this, 0, 2);
            if(World.Rnd().nextInt(0, 99) < 66)
                FM.AS.hitEngine(this, 0, 2);
            break;

        case 37:
            FM.AS.hitEngine(this, 1, 2);
            if(World.Rnd().nextInt(0, 99) < 66)
                FM.AS.hitEngine(this, 1, 2);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                    getEnergyPastArmor(World.Rnd().nextFloat(12.5F, 16F), shot);
                else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(1.01D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor(World.Rnd().nextFloat(12.5F, 16F), shot);
                else
                if(s.endsWith("p4"))
                    getEnergyPastArmor(World.Rnd().nextFloat(12.5F, 16F), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                if(s.endsWith("10"))
                    i = 10;
                else
                if(s.endsWith("11"))
                    i = 11;
                else
                if(s.endsWith("12"))
                    i = 12;
                switch(i)
                {
                default:
                    break;

                case 1:
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        debuggunnery("*** Control Column: Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 2:
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                    break;

                case 3:
                    if(getEnergyPastArmor(2.2F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    break;

                case 4:
                    if(getEnergyPastArmor(2.2F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                    break;

                case 5:
                    if(getEnergyPastArmor(2.2F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 7);
                    break;

                case 6:
                case 7:
                    if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        debuggunnery("*** Aileron Controls: Disabled..");
                    }
                    break;

                case 8:
                    if(getEnergyPastArmor(0.12F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.25F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    if(World.Rnd().nextFloat() < 0.25F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    if(World.Rnd().nextFloat() < 0.25F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                    break;

                case 9:
                    if(getEnergyPastArmor(0.12F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.25F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                    if(World.Rnd().nextFloat() < 0.25F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                    if(World.Rnd().nextFloat() < 0.25F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 7);
                    break;

                case 10:
                case 11:
                    if(getEnergyPastArmor(0.002F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        debuggunnery("*** Elevator Controls: Disabled / Strings Broken..");
                    }
                    break;

                case 12:
                    if(getEnergyPastArmor(2.3F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        debuggunnery("*** Rudder Controls: Disabled..");
                    }
                    break;
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(26.8D / (1.0001000165939331D - Math.abs(Aircraft.v1.y)), shot) > 0.0F)
                {
                    debuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if((s.endsWith("e1") || s.endsWith("e2")) && getEnergyPastArmor(32F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if((s.endsWith("e3") || s.endsWith("e4")) && getEnergyPastArmor(32F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                if(s.startsWith("xxlockal"))
                {
                    if(getEnergyPastArmor(4.35F, shot) > 0.0F)
                    {
                        debuggunnery("*** AroneL Lock Damaged..");
                        nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
                    }
                } else
                if(s.startsWith("xxlockar"))
                {
                    if(getEnergyPastArmor(4.35F, shot) > 0.0F)
                    {
                        debuggunnery("*** AroneR Lock Damaged..");
                        nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
                    }
                } else
                if(s.startsWith("xxlockvl"))
                {
                    if(getEnergyPastArmor(4.32F, shot) > 0.0F)
                    {
                        debuggunnery("*** VatorL Lock Damaged..");
                        nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
                    }
                } else
                if(s.startsWith("xxlockvr"))
                {
                    if(getEnergyPastArmor(4.32F, shot) > 0.0F)
                    {
                        debuggunnery("*** VatorR Lock Damaged..");
                        nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
                    }
                } else
                if(s.startsWith("xxlockr") && getEnergyPastArmor(4.32F, shot) > 0.0F)
                {
                    debuggunnery("*** Rudder1 Lock Damaged..");
                    nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxbomb"))
            {
                if(World.Rnd().nextFloat() < 0.01F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
                {
                    debuggunnery("*** Bomb Payload Detonates.. CF_D" + chunkDamageVisible("CF"));
                    FM.AS.hitTank(shot.initiator, 0, 100);
                    FM.AS.hitTank(shot.initiator, 1, 100);
                    FM.AS.hitTank(shot.initiator, 2, 100);
                    FM.AS.hitTank(shot.initiator, 3, 100);
                    nextDMGLevels(3, 2, "CF_D" + chunkDamageVisible("CF"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int j = s.charAt(5) - 49;
                if(s.endsWith("prop") && getEnergyPastArmor(1.2F, shot) > 0.0F)
                    FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(1.7F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, j);
                            debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            FM.AS.hitEngine(shot.initiator, j, 2);
                            debuggunnery("*** Engine" + j + " Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.04F)
                    {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[j].setReadyness(shot.initiator, FM.EI.engines[j].getReadyness() - 0.02F);
                        debuggunnery("*** Engine" + j + " Crank Case Hit - Readyness Reduced to " + FM.EI.engines[j].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[j].getCylindersRatio() * 0.9878F)
                    {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        debuggunnery("*** Engine" + j + " Cylinders Hit, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[j].setKillCompressor(shot.initiator);
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("eqpt"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(Aircraft.Pd.y > 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
                        if(Aircraft.Pd.y < 0.0D && Aircraft.Pd.z < 0.189D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 6);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("mag1"))
                {
                    FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 0);
                    debuggunnery("*** Engine" + j + " Magneto 1 Destroyed..");
                }
                if(s.endsWith("mag2"))
                {
                    FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, 1);
                    debuggunnery("*** Engine" + j + " Magneto 2 Destroyed..");
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                int k = s.charAt(5) - 49;
                if(getEnergyPastArmor(4.21F, shot) > 0.0F)
                {
                    FM.AS.hitOil(shot.initiator, k);
                    getEnergyPastArmor(0.42F, shot);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.6F, shot) > 0.0F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F)
                        FM.AS.hitTank(shot.initiator, l, 2);
                }
                return;
            }
            if(s.startsWith("xxammo"))
            {
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setJamBullets(10, 0);
                return;
            }
            if(s.startsWith("xxpnm"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
                {
                    debuggunnery("Pneumo System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            } else
            {
                return;
            }
        }
//        if(s.startsWith("xcockpit"));
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
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
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstabl"))
            hitChunk("StabL", shot);
        else
        if(s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
        } else
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
        } else
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
        } else
        if(s.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
        } else
        if(s.startsWith("xwingrmid"))
        {
            if(chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
        } else
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
        } else
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xaronel"))
        {
            if(chunkDamageVisible("AroneL") < 2)
                hitChunk("AroneL", shot);
        } else
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 2)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(s.startsWith("xturret1"))
            {
                debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                FM.AS.setJamBullets(10, 0);
                getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 66.35F), shot);
            }
            if(s.startsWith("xturret2"))
            {
                debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                FM.AS.setJamBullets(11, 0);
                getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 66.35F), shot);
            }
            if(s.startsWith("xturret3"))
            {
                debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                FM.AS.setJamBullets(12, 0);
                getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 66.35F), shot);
            }
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i1;
            if(s.endsWith("a") || s.endsWith("a2"))
            {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b") || s.endsWith("b2"))
            {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else
            {
                i1 = s.charAt(5) - 49;
            }
            hitFlesh(i1, shot, byte0);
        }
    }

    public void msgExplosion(Explosion explosion)
    {
        setExplosion(explosion);
        if(explosion.chunkName != null && explosion.power > 0.0F)
        {
            if(explosion.chunkName.equals("Tail1_D3"))
                return;
            if(explosion.chunkName.equals("WingLIn_D3"))
                return;
            if(explosion.chunkName.equals("WingRIn_D3"))
                return;
            if(explosion.chunkName.equals("WingLMid_D3"))
                return;
            if(explosion.chunkName.equals("WingRMid_D3"))
                return;
            if(explosion.chunkName.equals("WingLOut_D3"))
                return;
            if(explosion.chunkName.equals("WingROut_D3"))
                return;
            if(explosion.chunkName.equals("Engine1_D2"))
                return;
            if(explosion.chunkName.equals("Engine2_D2"))
                return;
        }
        super.msgExplosion(explosion);
    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 1:
            FM.turret[0].bIsOperable = false;
            break;

        case 2:
            FM.turret[1].bIsOperable = false;
            FM.turret[2].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        default:
            break;

        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1:
            if(hierMesh().isChunkVisible("Pilot2_D0"))
            {
                hierMesh().chunkVisible("Pilot2_D0", false);
                hierMesh().chunkVisible("Pilot2_D1", true);
            } else
            {
                hierMesh().chunkVisible("Pilot2a_D0", false);
                hierMesh().chunkVisible("Head2a_D0", false);
            }
            break;

        case 2:
            if(hierMesh().isChunkVisible("Pilot3_D0"))
            {
                hierMesh().chunkVisible("Pilot3_D0", false);
                hierMesh().chunkVisible("Pilot3_D1", true);
            } else
            {
                hierMesh().chunkVisible("Pilot3a_D0", false);
                hierMesh().chunkVisible("Pilot3a_D0", true);
            }
            break;
        }
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
//        setRadist(0, 0);
        setRadist(1, 0);
        setRadist(2, 0);
        hierMesh().chunkVisible("Turret3a_D0", false);
    }

    public void update(float f)
    {
        if(Time.current() > tme)
        {
            tme = Time.current() + World.Rnd().nextLong(1000L, 5000L);
            if(FM.turret.length != 0)
            {
//                if(FM.turret[0].bIsOperable != (radist[1] == 0))
//                {
//                    Actor actor = FM.turret[0].target;
//                    if(actor != null)
//                    {
//                        setRadist(1, 1);
//                    } else
//                    {
//                        Actor actor3 = FM.turret[1].target;
//                        if(actor3 == null)
//                            actor3 = FM.turret[2].target;
//                        if(actor3 != null)
//                        {
//                            setRadist(1, 1);
//                            FM.turret[0].target = actor3;
//                        } else
//                        {
//                            setRadist(1, 0);
//                        }
//                    }
//                }
//                if(FM.turret[1].bIsOperable)
//                {
//                    Actor actor1 = FM.turret[1].target;
//                    if(actor1 != null && Actor.isValid(actor1))
//                    {
//                        pos.getAbs(Aircraft.tmpLoc2);
//                        actor1.pos.getAbs(Aircraft.tmpLoc3);
//                        Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
//                        if(Aircraft.tmpLoc3.getPoint().z < 0.0D)
//                            setRadist(2, 1);
//                    }
//                } else
//                if(FM.turret[2].bIsOperable)
//                {
//                    Actor actor2 = FM.turret[2].target;
//                    if(actor2 != null && Actor.isValid(actor2))
//                    {
//                        pos.getAbs(Aircraft.tmpLoc2);
//                        actor2.pos.getAbs(Aircraft.tmpLoc3);
//                        Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
//                        if(Aircraft.tmpLoc3.getPoint().z > 0.0D)
//                            setRadist(2, 0);
//                    }
//                }
                
                
                
//                setRadist(1, FM.turret[0].target == null?0:1);
//                Actor actor = FM.turret[1].target;
//                boolean topTurretActive = false;
//                if(actor != null && Actor.isValid(actor))
//                {
//                    pos.getAbs(Aircraft.tmpLoc2);
//                    actor.pos.getAbs(Aircraft.tmpLoc3);
//                    Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
//                    if(Aircraft.tmpLoc3.getPoint().z < 0.0D)
//                        topTurretActive = true;
//                }
//                setRadist(2, topTurretActive?1:0);
//                if (!topTurretActive) {
//                    actor = FM.turret[2].target;
//                    if(actor != null && Actor.isValid(actor))
//                    {
//                        pos.getAbs(Aircraft.tmpLoc2);
//                        actor.pos.getAbs(Aircraft.tmpLoc3);
//                        Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
//                        if(Aircraft.tmpLoc3.getPoint().z > 0.0D)
//                            setRadist(2, 0);
//                    }
//                }
                
            }
        }
        super.update(f);
    }

    public void setRadist(int i, int j)
    {
//        System.out.println("setRadist(" + i + ", " + j + ")");
        
        hierMesh().chunkVisible("Pilot2_D0", false);
        hierMesh().chunkVisible("HMask2_D0", false);
        hierMesh().chunkVisible("Pilot2a_D0", true);
        hierMesh().chunkVisible("Head2a_D0", true);
        hierMesh().chunkVisible("HMask2a_D0", FM.Loc.z > 3000D);
        hierMesh().chunkVisible("Pilot3a_D0", false);
        hierMesh().chunkVisible("HMask3a_D0", false);
        hierMesh().chunkVisible("Pilot3_D0", true);
        hierMesh().chunkVisible("HMask3_D0", FM.Loc.z > 3000D);
        FM.turret[0].bIsOperable = true;
        FM.turret[1].bIsOperable = true;
        FM.turret[2].bIsOperable = true;

//        
//        
//        radist[i] = j;
//        if(FM.AS.astatePilotStates[i] > 90)
//            return;
//        switch(i)
//        {
//        default:
//            break;
//
//        case 1:
//            hierMesh().chunkVisible("Pilot2_D0", false);
//            hierMesh().chunkVisible("Pilot2a_D0", false);
//            hierMesh().chunkVisible("Head2a_D0", false);
//            hierMesh().chunkVisible("HMask2_D0", false);
//            hierMesh().chunkVisible("HMask2a_D0", false);
//            FM.turret[0].bIsOperable = false;
//            switch(j)
//            {
//            case 0:
//                hierMesh().chunkVisible("Pilot2_D0", true);
//                hierMesh().chunkVisible("HMask2_D0", FM.Loc.z > 3000D);
//                break;
//
//            case 1:
//                hierMesh().chunkVisible("Pilot2a_D0", true);
//                hierMesh().chunkVisible("Head2a_D0", true);
//                hierMesh().chunkVisible("HMask2a_D0", FM.Loc.z > 3000D);
//                FM.turret[0].bIsOperable = true;
//                break;
//            }
//            break;
//
//        case 2:
//            hierMesh().chunkVisible("Pilot3_D0", false);
//            hierMesh().chunkVisible("Pilot3a_D0", false);
//            hierMesh().chunkVisible("HMask3_D0", false);
//            hierMesh().chunkVisible("HMask3a_D0", false);
//            FM.turret[1].bIsOperable = false;
//            FM.turret[2].bIsOperable = false;
//            switch(j)
//            {
//            case 0:
//                hierMesh().chunkVisible("Pilot3_D0", true);
//                hierMesh().chunkVisible("HMask3_D0", FM.Loc.z > 3000D);
//                FM.turret[1].bIsOperable = true;
//                break;
//
//            case 1:
//                hierMesh().chunkVisible("Pilot3a_D0", true);
//                hierMesh().chunkVisible("HMask3a_D0", FM.Loc.z > 3000D);
//                FM.turret[2].bIsOperable = true;
//                break;
//            }
//            break;
//        }
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
        fSightCurForwardAngle++;
        if(fSightCurForwardAngle > 75F)
            fSightCurForwardAngle = 75F;
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle--;
        if(fSightCurForwardAngle < -0F)
            fSightCurForwardAngle = -0F;
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip += 0.1D;
        if(fSightCurSideslip > 3F)
            fSightCurSideslip = 3F;
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip -= 0.1F;
        if(fSightCurSideslip < -3F)
            fSightCurSideslip = -3F;
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 10F;
        if(fSightCurAltitude > 6000F)
            fSightCurAltitude = 6000F;
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 10F;
        if(fSightCurAltitude < 850F)
            fSightCurAltitude = 850F;
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 250F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 10F;
        if(fSightCurSpeed > 300F)
            fSightCurSpeed = 300F;
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 10F;
        if(fSightCurSpeed < 250F)
            fSightCurSpeed = 250F;
    }

    public void typeBomberUpdate(float f)
    {
        double d = ((double)fSightCurSpeed / 3.6D) * Math.sqrt((double)fSightCurAltitude * 0.203873598D);
        d -= (double)(fSightCurAltitude * fSightCurAltitude) * 1.419E-005D;
        fSightSetForwardAngle = (float)Math.atan(d / (double)fSightCurAltitude);
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

    private long tme;
//    private int radist[] = {
//        0, 0, 0
//    };
    private boolean pilot2kill;
    private boolean blisTurOpen;
    boolean hasAdditionalBlisterMeshes = false;
    public boolean bChangedExts;
    public static boolean bChangedPit = false;
    public float fSightSetForwardAngle;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;

    static 
    {
        Property.set(Martin_B10_xyz.class, "originCountry", PaintScheme.countryUSA);
    }
}
