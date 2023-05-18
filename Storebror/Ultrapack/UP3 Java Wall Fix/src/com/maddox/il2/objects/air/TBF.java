package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class TBF extends Scheme1
    implements TypeStormovik, TypeBomber
{

    public TBF()
    {
        arrestor = 0.0F;
        flapps = 0.0F;
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -115F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -70F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay03_D0", 0.0F, -115F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay04_D0", 0.0F, -70F * f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("LLamp_D0", 0.0F, 90F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
        } else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
            hierMesh().chunkVisible("HMask3_D0", hierMesh().isChunkVisible("Pilot3_D0"));
        }
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1:
            FM.turret[0].setHealth(f);
            break;

        case 2:
            FM.turret[1].setHealth(f);
            break;
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

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.equals("xxarmorc1"))
                    getEnergyPastArmor(World.Rnd().nextFloat(10F, 15F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.startsWith("xxarmorrt"))
                {
                    if(s.endsWith("1"))
                        getEnergyPastArmor(World.Rnd().nextFloat(10F, 60F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    if(s.endsWith("2"))
                        getEnergyPastArmor(19.1D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                    if(s.endsWith("3"))
                        getEnergyPastArmor(12.699999809265137D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    if(s.endsWith("4") || s.endsWith("5"))
                        getEnergyPastArmor(9.5D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                } else
                if(s.startsWith("xxarmort"))
                {
                    if(s.endsWith("1"))
                        getEnergyPastArmor(9.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    if(s.endsWith("2"))
                        getEnergyPastArmor(19.1D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    if(s.endsWith("3"))
                        getEnergyPastArmor(World.Rnd().nextFloat(9.5F, 19.1F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                }
                return;
            }
            if(s.startsWith("xxammo"))
            {
                if(s.endsWith("rg") && getEnergyPastArmor(1.2F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < 0.12F)
                        FM.AS.setJamBullets(11, 0);
                    if(World.Rnd().nextFloat() < 0.12F)
                        FM.AS.explodeTank(shot.initiator, 3);
                }
                if(s.endsWith("wl") && getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    FM.AS.setJamBullets(0, 0);
                if(s.endsWith("wr") && getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    FM.AS.setJamBullets(0, 1);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

                case 1:
                case 2:
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3:
                    if(World.Rnd().nextFloat() < 0.95F && getEnergyPastArmor(4.253F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;

                case 4:
                case 5:
                    if(World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    break;
                }
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 280000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 100000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.66F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 32200F)));
                        debuggunnery("Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 1000000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.startsWith("xxeng1mag"))
                {
                    int j = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                }
                if(s.endsWith("oil1"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                        debuggunnery("Engine Module: Oil Radiator Hit..");
                    FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if(s.startsWith("xxgun"))
            {
                int k = 0;
                if(s.endsWith("r"))
                    k = 1;
                if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
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
            if(s.startsWith("xxpnm"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
                {
                    debuggunnery("Pneumo System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if(s.startsWith("xxradio"))
            {
                getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 26.98F), shot);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.5F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F)
                {
                    debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if((s.endsWith("sl1") || s.endsWith("sl2") || s.endsWith("sl3") || s.endsWith("sl4") || s.endsWith("sl5")) && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
                }
                if((s.endsWith("sr1") || s.endsWith("sr2") || s.endsWith("sr3") || s.endsWith("sr4") || s.endsWith("sr5")) && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
                }
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.1D && getEnergyPastArmor(3.4D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    FM.AS.hitTank(shot.initiator, l, 1);
                    if(World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F)
                        FM.AS.hitTank(shot.initiator, l, 2);
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xeng"))
            hitChunk("Engine1", shot);
        else
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
            if(s.startsWith("xWingLIn") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xWingRIn") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xWingLMid"))
            {
                if(chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if(World.Rnd().nextFloat() < shot.mass + 0.02F)
                    FM.AS.hitOil(shot.initiator, 0);
            }
            if(s.startsWith("xWingRMid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xWingLOut") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xWingROut") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner"))
                hitChunk("AroneR", shot);
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
            if(f < -120F)
            {
                f = -120F;
                flag = false;
            }
            if(f > 150F)
            {
                f = 150F;
                flag = false;
            }
            if(f1 < -2F)
            {
                f1 = -2F;
                flag = false;
            }
            if(f1 > 89F)
            {
                f1 = 89F;
                flag = false;
            }
            break;

        case 1:
            if(f < -45F)
            {
                f = -45F;
                flag = false;
            }
            if(f > 45F)
            {
                f = 45F;
                flag = false;
            }
            if(f1 < -40F)
            {
                f1 = -40F;
                flag = false;
            }
            if(f1 > 20F)
            {
                f1 = 20F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, -100F * f, 0.0F);
    }

    public void moveWingFold(float f)
    {
        if(f < 0.001F)
        {
            setGunPodsOn(true);
            hideWingWeapons(false);
        } else
        {
            setGunPodsOn(false);
            FM.CT.WeaponControl[0] = false;
            hideWingWeapons(true);
        }
        moveWingFold(hierMesh(), f);
    }

    public void moveArrestorHook(float f)
    {
        resetYPRmodifier();
        xyz[0] = -1.45F * f;
        ypr[1] = -arrestor;
        hierMesh().chunkSetLocate("Hook1_D0", xyz, ypr);
    }

    public void update(float f)
    {
        super.update(f);
        float f3 = FM.CT.getArrestor();
        float f4 = 45F * f3 * f3 * f3 * f3 * f3;
        if(f3 > 0.01F)
            if(FM.Gears.arrestorVAngle != 0.0F)
            {
                arrestor = cvt(FM.Gears.arrestorVAngle, -f4, f4, -f4, f4);
                moveArrestorHook(f3);
                if(FM.Gears.arrestorVAngle >= -35F);
            } else
            {
                float f1 = 40F * FM.Gears.arrestorVSink;
                if(f1 > 0.0F && FM.getSpeedKMH() > 60F)
                    Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                arrestor += f1;
                if(arrestor > f4)
                    arrestor = f4;
                if(arrestor < -f4)
                    arrestor = -f4;
                moveArrestorHook(f3);
            }
        float f2 = FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f2) > 0.01F)
        {
            flapps = f2;
            for(int i = 1; i < 5; i++)
                hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -20F * f2, 0.0F);

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

    private float arrestor;
    private float flapps;

    static 
    {
        Class class1 = TBF.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
