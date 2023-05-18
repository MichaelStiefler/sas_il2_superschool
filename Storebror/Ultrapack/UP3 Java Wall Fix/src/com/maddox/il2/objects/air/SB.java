package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class SB extends Scheme2
{

    public SB()
    {
        tme = 0L;
        topGunnerPosition = 0.0F;
        curTopGunnerPosition = 0.0F;
        radPosNum = 1;
        curTakeem = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -175F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -155F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -175F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -155F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, 70F), 0.0F);
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
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F)
                FM.AS.hitTank(this, 0, 1);
            if(FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.39F)
                FM.AS.hitTank(this, 1, 1);
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

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("BayL_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("BayR_D0", 0.0F, 90F * f, 0.0F);
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
            if(f < -43F)
            {
                f = -43F;
                flag = false;
            }
            if(f > 43F)
            {
                f = 43F;
                flag = false;
            }
            if(f1 < -15F)
            {
                f1 = -15F;
                flag = false;
            }
            if(f1 > 15F)
            {
                f1 = 15F;
                flag = false;
            }
            break;

        case 1:
            if(this instanceof SB_2M100A)
            {
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
                if(f1 < -2F)
                {
                    f1 = -2F;
                    flag = false;
                }
                if(f1 > 50F)
                {
                    f1 = 50F;
                    flag = false;
                }
                break;
            }
            if(f1 > 48F)
            {
                f1 = 48F;
                flag = false;
            }
            float f2 = Math.abs(f);
            if(f2 < 6F)
            {
                if(f1 < -4.5F)
                {
                    f1 = -4.5F;
                    flag = false;
                }
                break;
            }
            if(f2 < 18F)
            {
                if(f1 < -1.291667F * f2 + 3.25F)
                {
                    f1 = -1.291667F * f2 + 3.25F;
                    flag = false;
                }
                break;
            }
            if(f2 < 42F)
            {
                if(f1 < -1.666667F * f1 + 10F)
                {
                    f1 = -1.666667F * f1 + 10F;
                    flag = false;
                }
                break;
            }
            if(f2 < 100F)
            {
                if(f1 < -60F)
                {
                    f1 = -60F;
                    flag = false;
                }
                break;
            }
            if(f2 < 138F)
            {
                if(f1 < -23.5F)
                {
                    f1 = -23.5F;
                    flag = false;
                }
                break;
            }
            if(f2 < 165F)
            {
                if(f1 < 1.518519F * f2 - 233.0556F)
                {
                    f1 = 1.518519F * f2 - 233.0556F;
                    flag = false;
                }
                break;
            }
            if(f1 < 1.5F * f2 - 230F)
            {
                f1 = 1.5F * f2 - 230F;
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
            if(f1 < -35F)
            {
                f1 = -35F;
                flag = false;
            }
            if(f1 > 10F)
            {
                f1 = 10F;
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
        case 33:
            hitProp(0, j, actor);
            return super.cutFM(34, j, actor);

        case 36:
            hitProp(1, j, actor);
            return super.cutFM(37, j, actor);

        case 19:
            FM.AS.setJamBullets(12, 0);
            if(FM.turret.length > 0)
                FM.turret[2].bIsOperable = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
                if(s.endsWith("p1"))
                    getEnergyPastArmor(0.2F, shot);
                else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(0.2F, shot);
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

                case 1:
                    if(World.Rnd().nextFloat() < 0.05F || shot.mass > 0.092F && World.Rnd().nextFloat() < 0.1F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        if(World.Rnd().nextFloat() < 0.5F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    }
                    break;

                case 2:
                    if(World.Rnd().nextFloat() >= 0.05F && (shot.mass <= 0.092F || World.Rnd().nextFloat() >= 0.1F))
                        break;
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                    if(World.Rnd().nextFloat() < 0.5F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                    break;

                case 3:
                    if(getEnergyPastArmor(1.0F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                    break;
                }
            }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxspart") && World.Rnd().nextFloat() < 0.36F && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(6.8F, shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(14.8F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(14.8F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(12.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(12.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(9.1F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(9.1F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(5.2F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(5.2F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if(s.startsWith("xxspare1") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if(s.startsWith("xxspare2") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
            }
            if(s.startsWith("xxbmb") && World.Rnd().nextFloat() < 0.01F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
            {
                Aircraft.debugprintln(this, "*** Bomb Payload Detonates..");
                FM.AS.hitTank(shot.initiator, 0, 10);
                FM.AS.hitTank(shot.initiator, 1, 10);
                FM.AS.hitTank(shot.initiator, 2, 10);
                FM.AS.hitTank(shot.initiator, 3, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if(s.startsWith("xxeng"))
            {
                int j = s.charAt(5) - 49;
                if(s.endsWith("prop") && getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F)
                {
                    FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Prop Governor Failed..");
                }
                if(s.endsWith("gear") && getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F)
                {
                    FM.EI.engines[j].setKillPropAngleDeviceSpeeds(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Prop Governor Damaged..");
                }
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 200000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, j);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            FM.AS.hitEngine(shot.initiator, j, 2);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Damaged..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 28000F)
                        {
                            FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                        }
                        if(World.Rnd().nextFloat() < 0.08F)
                        {
                            FM.EI.engines[j].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                        }
                        FM.EI.engines[j].setReadyness(shot.initiator, FM.EI.engines[j].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Readyness Reduced to " + FM.EI.engines[j].getReadyness() + "..");
                    }
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.EI.engines[j].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Stalled..");
                    }
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.AS.hitEngine(shot.initiator, j, 10);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                    }
                    getEnergyPastArmor(6F, shot);
                }
                if((s.endsWith("cyl1") || s.endsWith("cyl2")) && getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[j].getCylindersRatio() * 1.72F)
                {
                    FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinders Hit, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.EI.engines[j].setEngineStuck(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinder Case Broken - Engine Stuck..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, j, 3);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinders Hit - Engine Fires..");
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if(s.endsWith("supc") && getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F)
                {
                    FM.EI.engines[j].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Supercharger Out..");
                }
                if(s.endsWith("eqpt") && getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F && World.Rnd().nextFloat() < 0.89F)
                {
                    if(World.Rnd().nextFloat() < 0.11F)
                    {
                        FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Magneto Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.11F)
                    {
                        FM.EI.engines[j].setKillCompressor(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Compressor Feed Out..");
                    }
                }
            }
            if(s.startsWith("xxoil"))
            {
                int k = 0;
                if(s.endsWith("2"))
                    k = 1;
                if(getEnergyPastArmor(0.21F, shot) > 0.0F)
                    FM.AS.hitOil(shot.initiator, k);
            }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.03F, shot) > 0.0F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        FM.AS.hitTank(shot.initiator, l, 2);
                        FM.AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if(shot.powerType == 3)
                        if(shot.power < 14100F)
                        {
                            if(FM.AS.astateTankStates[l] < 4 && World.Rnd().nextFloat() < 0.1F)
                                FM.AS.hitTank(shot.initiator, l, 1);
                        } else
                        {
                            FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(0, (int)(shot.power / 28200F)));
                        }
                }
            }
            if(s.startsWith("xxhyd"))
                FM.AS.setInternalDamage(shot.initiator, 3);
            if(s.startsWith("xxpnm"))
                FM.AS.setInternalDamage(shot.initiator, 1);
        } else
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(shot.power > 33000F && point3d.x > 1.0D)
            {
                FM.AS.hitPilot(shot.initiator, 0, World.Rnd().nextInt(30, 192));
                FM.AS.hitPilot(shot.initiator, 1, World.Rnd().nextInt(30, 192));
            }
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
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
        } else
        if(s.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvatorl"))
            hitChunk("VatorL", shot);
        else
        if(s.startsWith("xvatorr"))
            hitChunk("VatorR", shot);
        else
        if(s.startsWith("xwinglin"))
        {
            if((FM.AS.astateTankStates[0] > 1 || FM.AS.astateTankStates[1] > 1) && shot.powerType == 3 && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.33F && World.Rnd().nextFloat() < curTakeem)
                FM.AS.hitTank(shot.initiator, World.Rnd().nextInt(0, 1), 3);
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
        } else
        if(s.startsWith("xwingrin"))
        {
            if((FM.AS.astateTankStates[2] > 1 || FM.AS.astateTankStates[3] > 1) && shot.powerType == 3 && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.33F && World.Rnd().nextFloat() < curTakeem)
                FM.AS.hitTank(shot.initiator, World.Rnd().nextInt(2, 3), 3);
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
            hitChunk("AroneL", shot);
        else
        if(s.startsWith("xaroner"))
            hitChunk("AroneR", shot);
        else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 168000F));
            Aircraft.debugprintln(this, "*** Engine1 Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            FM.EI.engines[1].setReadyness(shot.initiator, FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 168000F));
            Aircraft.debugprintln(this, "*** Engine2 Hit - Readyness Reduced to " + FM.EI.engines[1].getReadyness() + "..");
        } else
        if(s.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.1F)
            {
                Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                FM.Gears.setHydroOperable(false);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(s.startsWith("xturret1"))
            {
                FM.AS.setJamBullets(10, 0);
                FM.AS.setJamBullets(10, 1);
            }
            if(s.startsWith("xturret2"))
                FM.AS.setJamBullets(11, 0);
            if(s.startsWith("xturret3"))
                FM.AS.setJamBullets(12, 0);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
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
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Gore2_D0", true);
            hierMesh().chunkVisible("Gore1_D0", hierMesh().isChunkVisible("Blister1_D0"));
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Gore3_D0", true);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            hierMesh().chunkVisible("HMask3_D0", false);
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

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public void update(float f)
    {
        if(Time.current() > tme)
        {
            tme = Time.current() + World.Rnd().nextLong(1000L, 5000L);
            if(FM.turret.length != 0)
            {
                Object obj = null;
                if(FM.turret[0].bIsOperable != (radist[1] == 0))
                {
                    Actor actor = FM.turret[0].target;
                    if(actor != null)
                    {
                        setRadist(1, 1);
                    } else
                    {
                        Actor actor1 = FM.turret[1].target;
                        if(actor1 == null)
                            actor1 = FM.turret[2].target;
                        if(actor1 != null)
                        {
                            setRadist(1, 1);
                            FM.turret[0].target = actor1;
                        } else
                        {
                            setRadist(1, 0);
                        }
                    }
                }
                if(FM.turret[1].bIsOperable)
                {
                    Actor actor2 = FM.turret[1].target;
                    if(actor2 != null && Actor.isValid(actor2))
                    {
                        pos.getAbs(Aircraft.tmpLoc2);
                        actor2.pos.getAbs(Aircraft.tmpLoc3);
                        Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
                        if(Aircraft.tmpLoc3.getPoint().z < 0.0D)
                            setRadist(2, 1);
                    }
                } else
                if(FM.turret[2].bIsOperable)
                {
                    Actor actor3 = FM.turret[2].target;
                    if(actor3 != null && Actor.isValid(actor3))
                    {
                        pos.getAbs(Aircraft.tmpLoc2);
                        actor3.pos.getAbs(Aircraft.tmpLoc3);
                        Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
                        if(Aircraft.tmpLoc3.getPoint().z > 0.0D)
                            setRadist(2, 0);
                    }
                }
            }
        }
        super.update(f);
    }

    private void setRadist(int i, int j)
    {
        radist[i] = j;
        if(FM.AS.astatePilotStates[2] <= 90)
        {
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkVisible("HMask4_D0", false);
            FM.turret[1].bIsOperable = false;
            FM.turret[2].bIsOperable = false;
            switch(j)
            {
            case 1:
                hierMesh().chunkVisible("Pilot3_D0", true);
                hierMesh().chunkVisible("HMask3_D0", FM.Loc.z > 3000D);
                FM.turret[1].bIsOperable = true;
                topGunnerPosition = 1.0F;
                break;

            case 2:
                hierMesh().chunkVisible("Pilot4_D0", true);
                hierMesh().chunkVisible("HMask4_D0", FM.Loc.z > 3000D);
                FM.turret[2].bIsOperable = true;
                topGunnerPosition = 0.0F;
                break;
            }
        }
    }

    private long tme;
    private float topGunnerPosition;
    private float curTopGunnerPosition;
    private int radPosNum;
    private Gun gun3;
    private Gun gun4;
    private int radist[] = {
        0, 0, 0
    };
    private float curTakeem;

    static 
    {
        Class class1 = SB.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
