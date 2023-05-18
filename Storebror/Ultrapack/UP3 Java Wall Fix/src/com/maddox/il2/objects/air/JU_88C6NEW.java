package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class JU_88C6NEW extends Scheme2
{

    public JU_88C6NEW()
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 1600F, -80F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -90F * f, 0.0F);
        f1 = f < 0.5F ? Math.abs(Math.min(f, 0.1F)) : Math.abs(Math.min(1.0F - f, 0.1F));
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -450F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 450F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 1200F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -1200F * f1, 0.0F);
        if(f < 0.5F)
        {
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 900F * f1, 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -900F * f1, 0.0F);
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -900F * f1, 0.0F);
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 900F * f1, 0.0F);
        }
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 0.0F, 95F * f);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 0.0F, 95F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -130F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -130F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    protected void moveFlap(float f)
    {
        float f1 = -70F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
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
            if(f < -30F)
            {
                f = -30F;
                flag = false;
            }
            if(f > 35F)
            {
                f = 35F;
                flag = false;
            }
            if(f1 < -10F)
            {
                f1 = -10F;
                flag = false;
            }
            if(f1 > 35F)
            {
                f1 = 35F;
                flag = false;
            }
            break;

        case 1:
            f = 0.0F;
            f1 = 0.0F;
            flag = false;
            break;

        case 2:
            if(f < -45F)
            {
                f = -45F;
                flag = false;
            }
            if(f > 25F)
            {
                f = 25F;
                flag = false;
            }
            if(f1 < -10F)
            {
                f1 = -10F;
                flag = false;
            }
            if(f1 > 60F)
            {
                f1 = 60F;
                flag = false;
            }
            if(f < -2F)
            {
                if(f1 < Aircraft.cvt(f, -6.8F, -2F, -10F, -2.99F))
                    f1 = Aircraft.cvt(f, -6.8F, -2F, -10F, -2.99F);
                break;
            }
            if(f < 0.5F)
            {
                if(f1 < Aircraft.cvt(f, -2F, 0.5F, -2.99F, -2.3F))
                    f1 = Aircraft.cvt(f, -2F, 0.5F, -2.99F, -2.3F);
                break;
            }
            if(f < 5.3F)
            {
                if(f1 < Aircraft.cvt(f, 0.5F, 5.3F, -2.3F, -2.3F))
                    f1 = Aircraft.cvt(f, 0.5F, 5.3F, -2.3F, -2.3F);
                break;
            }
            if(f1 < Aircraft.cvt(f, 5.3F, 25F, -2.3F, -7.2F))
                f1 = Aircraft.cvt(f, 5.3F, 25F, -2.3F, -7.2F);
            break;

        case 3:
            if(f < -35F)
            {
                f = -35F;
                flag = false;
            }
            if(f > 35F)
            {
                f = 35F;
                flag = false;
            }
            if(f1 < -35F)
            {
                f1 = -35F;
                flag = false;
            }
            if(f1 > -0.48F)
            {
                f1 = -0.48F;
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
        case 13:
            killPilot(this, 0);
            killPilot(this, 1);
            killPilot(this, 2);
            killPilot(this, 3);
            return false;

        case 33:
            return super.cutFM(34, j, actor);

        case 36:
            return super.cutFM(37, j, actor);

        case 3:
            FM.AS.hitEngine(this, 0, 99);
            break;

        case 4:
            FM.AS.hitEngine(this, 1, 99);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void mydebuggunnery(String s)
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
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
        }
        for(int i = 1; i < 4; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
                if(s.endsWith("p1"))
                {
                    if(Aircraft.v1.z > 0.5D)
                        getEnergyPastArmor(5D / Aircraft.v1.z, shot);
                    else
                    if(Aircraft.v1.x > 0.93969261646270752D)
                        getEnergyPastArmor((10D / Aircraft.v1.x) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(5D / Math.abs(Aircraft.v1.z), shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor((8D / Math.abs(Aircraft.v1.x)) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                else
                if(s.endsWith("p4"))
                {
                    if(Aircraft.v1.x > 0.70710676908493042D)
                        getEnergyPastArmor((8D / Aircraft.v1.x) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                    else
                    if(Aircraft.v1.x > -0.70710676908493042D)
                        getEnergyPastArmor(6F, shot);
                } else
                if(s.endsWith("o1") || s.endsWith("o2"))
                    if(Aircraft.v1.x > 0.70710676908493042D)
                        getEnergyPastArmor((8D / Aircraft.v1.x) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                    else
                        getEnergyPastArmor(5F, shot);
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

                case 1:
                case 2:
                    if(getEnergyPastArmor(1.0F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.08F)
                        {
                            FM.AS.setControlsDamage(shot.initiator, 1);
                            mydebuggunnery("Evelator Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.08F)
                        {
                            FM.AS.setControlsDamage(shot.initiator, 2);
                            mydebuggunnery("Rudder Controls Out..");
                        }
                    }
                    break;

                case 3:
                case 4:
                    if(getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        mydebuggunnery("Ailerons Controls Out..");
                    }
                    break;

                case 5:
                    if(getEnergyPastArmor(0.1F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        mydebuggunnery("*** Engine1 Throttle Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.15F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        mydebuggunnery("*** Engine1 Prop Controls Out..");
                    }
                    break;

                case 6:
                    if(getEnergyPastArmor(0.1F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.15F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                        mydebuggunnery("*** Engine2 Throttle Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.15F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                        mydebuggunnery("*** Engine2 Prop Controls Out..");
                    }
                    break;
                }
            }
            if(s.startsWith("xxspar"))
            {
                getEnergyPastArmor(1.0F, shot);
                if((s.endsWith("cf1") || s.endsWith("cf2")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("CF") > 2 && getEnergyPastArmor(15.9F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    mydebuggunnery("*** CF Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                    msgCollision(this, "WingLIn_D0", "WingLIn_D0");
                    msgCollision(this, "WingRIn_D0", "WingRIn_D0");
                }
                if((s.endsWith("ta1") || s.endsWith("ta2")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(15.9F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    mydebuggunnery("*** Tail1 Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if((s.endsWith("li1") || s.endsWith("li2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2")) && World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2")) && World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.endsWith("e1") && (point3d.y > 2.79D || point3d.y < 2.32D) && getEnergyPastArmor(18F, shot) > 0.0F)
                {
                    mydebuggunnery("*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if(s.endsWith("e2") && (point3d.y < -2.79D || point3d.y > -2.32D) && getEnergyPastArmor(18F, shot) > 0.0F)
                {
                    mydebuggunnery("*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
            {
                mydebuggunnery("*** Bomb Payload Detonates..");
                FM.AS.hitTank(shot.initiator, 0, 100);
                FM.AS.hitTank(shot.initiator, 1, 100);
                FM.AS.hitTank(shot.initiator, 2, 100);
                FM.AS.hitTank(shot.initiator, 3, 100);
                msgCollision(this, "CF_D0", "CF_D0");
            }
            if(s.startsWith("xxprop"))
            {
                int j = 0;
                if(s.endsWith("2"))
                    j = 1;
                if(getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F)
                {
                    FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
                    mydebuggunnery("*** Engine" + (j + 1) + " Governor Failed..");
                }
            }
            if(s.startsWith("xxengine"))
            {
                int k = 0;
                if(s.startsWith("xxengine2"))
                    k = 1;
                mydebuggunnery("*** Engine " + k + " " + s + " hit");
                if(s.endsWith("base"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 120000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, k);
                            mydebuggunnery("*** Engine" + (k + 1) + " Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 30000F)
                        {
                            FM.AS.hitEngine(shot.initiator, k, 2);
                            mydebuggunnery("*** Engine" + (k + 1) + " Crank Case Hit - Engine Damaged..");
                        }
                    }
                } else
                if(s.endsWith("cyl"))
                {
                    mydebuggunnery("*** Engine " + k + " " + s + " hit");
                    if(getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[k].getCylindersRatio() * 1.8F)
                    {
                        FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        mydebuggunnery("*** Engine" + (k + 1) + " Cylinders Hit, " + FM.EI.engines[k].getCylindersOperable() + "/" + FM.EI.engines[k].getCylinders() + " Left..");
                        if(FM.AS.astateEngineStates[k] < 1)
                        {
                            FM.AS.hitEngine(shot.initiator, k, 1);
                            FM.AS.doSetEngineState(shot.initiator, k, 1);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 960000F)
                        {
                            FM.AS.hitEngine(shot.initiator, k, 3);
                            mydebuggunnery("*** Engine" + (k + 1) + " Cylinders Hit - Engine Fires..");
                        }
                        mydebuggunnery("*** Engine" + (k + 1) + " state " + FM.AS.astateEngineStates[k]);
                    }
                } else
                if(s.endsWith("sup") && getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F)
                {
                    FM.AS.setEngineSpecificDamage(shot.initiator, k, 0);
                    mydebuggunnery("*** Engine" + (k + 1) + " Supercharger Out..");
                }
                if(World.Rnd().nextFloat(0.0F, 18000F) < shot.power)
                    FM.AS.hitEngine(shot.initiator, k, 1);
                FM.AS.hitOil(shot.initiator, k);
            }
            if(s.startsWith("xxoil"))
            {
                int l = 0;
                if(s.endsWith("2"))
                    l = 1;
                if(getEnergyPastArmor(0.18F, shot) > 0.0F)
                {
                    FM.AS.hitOil(shot.initiator, l);
                    getEnergyPastArmor(0.42F, shot);
                }
            }
            if(s.startsWith("xxtank"))
            {
                int i1 = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                    if(shot.power < 14100F)
                    {
                        if(FM.AS.astateTankStates[i1] < 1)
                            FM.AS.hitTank(shot.initiator, i1, 1);
                        if(FM.AS.astateTankStates[i1] < 4 && World.Rnd().nextFloat() < 0.1F)
                            FM.AS.hitTank(shot.initiator, i1, 1);
                        if(shot.powerType == 3 && FM.AS.astateTankStates[i1] > 2 && World.Rnd().nextFloat() < 0.12F)
                            FM.AS.hitTank(shot.initiator, i1, 10);
                    } else
                    {
                        FM.AS.hitTank(shot.initiator, i1, World.Rnd().nextInt(0, (int)(shot.power / 20000F)));
                    }
                mydebuggunnery("*** Tank " + (i1 + 1) + " state = " + FM.AS.astateTankStates[i1]);
            }
        }
        if(s.startsWith("xoil"))
        {
            if(s.equals("xoil1"))
            {
                FM.AS.hitOil(shot.initiator, 0);
                s = "xengine1";
            }
            if(s.equals("xoil2"))
            {
                FM.AS.hitOil(shot.initiator, 1);
                s = "xengine2";
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xnose"))
        {
            if(chunkDamageVisible("Nose") < 2)
                hitChunk("Nose", shot);
            if(shot.power > 200000F)
            {
                FM.AS.hitPilot(shot.initiator, 0, World.Rnd().nextInt(3, 192));
                FM.AS.hitPilot(shot.initiator, 1, World.Rnd().nextInt(3, 192));
            }
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(point3d.x > 4.505000114440918D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            else
            if(point3d.y > 0.0D)
            {
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
            } else
            {
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
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
            hitChunk("StabL", shot);
        else
        if(s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("xvatorl"))
            hitChunk("VatorL", shot);
        else
        if(s.startsWith("xvatorr"))
            hitChunk("VatorR", shot);
        else
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
            hitChunk("AroneL", shot);
        else
        if(s.startsWith("xaroner"))
            hitChunk("AroneR", shot);
        else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            if(getEnergyPastArmor(0.2F, shot) > 800F)
            {
                FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 96000F));
                FM.AS.hitOil(shot.initiator, 0);
                mydebuggunnery("*** Engine1 Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + ".. State = " + FM.AS.astateEngineStates[0]);
                if(FM.EI.engines[0].getReadyness() < 0.85F && FM.AS.astateEngineStates[0] == 0)
                {
                    FM.AS.hitEngine(shot.initiator, 0, 1);
                    FM.AS.doSetEngineState(shot.initiator, 0, 1);
                }
            }
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            if(getEnergyPastArmor(0.2F, shot) > 800F)
            {
                FM.EI.engines[1].setReadyness(shot.initiator, FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 96000F));
                FM.AS.hitOil(shot.initiator, 1);
                mydebuggunnery("*** Engine2 Hit - Readyness Reduced to " + FM.EI.engines[1].getReadyness() + ".. State = " + FM.AS.astateEngineStates[1]);
                if(FM.EI.engines[1].getReadyness() < 0.85F && FM.AS.astateEngineStates[1] == 0)
                {
                    FM.AS.hitEngine(shot.initiator, 1, 1);
                    FM.AS.doSetEngineState(shot.initiator, 1, 1);
                }
            }
        } else
        if(s.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.1F)
            {
                mydebuggunnery("*** Gear Hydro Failed..");
                FM.Gears.setHydroOperable(false);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(s.startsWith("xturret1"))
                FM.AS.setJamBullets(10, 0);
            if(s.startsWith("xturret2"))
                FM.AS.setJamBullets(11, 0);
            if(s.startsWith("xturret3"))
                FM.AS.setJamBullets(12, 0);
            if(s.startsWith("xturret4"))
                FM.AS.setJamBullets(13, 0);
            if(s.startsWith("xturret5"))
                FM.AS.setJamBullets(14, 0);
        } else
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
        }
    }

    public boolean typeDiveBomberToggleAutomation()
    {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset()
    {
    }

    public void typeDiveBomberAdjAltitudePlus()
    {
    }

    public void typeDiveBomberAdjAltitudeMinus()
    {
    }

    public void typeDiveBomberAdjVelocityReset()
    {
    }

    public void typeDiveBomberAdjVelocityPlus()
    {
    }

    public void typeDiveBomberAdjVelocityMinus()
    {
    }

    public void typeDiveBomberAdjDiveAngleReset()
    {
    }

    public void typeDiveBomberAdjDiveAnglePlus()
    {
    }

    public void typeDiveBomberAdjDiveAngleMinus()
    {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    static 
    {
        Class class1 = JU_88C6NEW.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
