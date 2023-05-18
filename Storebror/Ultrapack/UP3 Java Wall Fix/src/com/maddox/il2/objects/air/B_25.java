package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class B_25 extends Scheme2a
{

    public B_25()
    {
        kangle0 = 0.0F;
        kangle1 = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 109F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, floatindex(cvt(f, 0.0F, 1.0F, 0.0F, 10F), anglesc6), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, floatindex(cvt(f, 0.0F, 1.0F, 0.0F, 10F), anglesc7), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f, 0.0F, 0.1F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -89.5F * f);
        if(f < 0.5F)
        {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.01F, 0.1F, 0.0F, -45F), 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.01F, 0.1F, 0.0F, 45F), 0.0F);
        } else
        {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.9F, 0.99F, -45F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.9F, 0.99F, 45F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, cvt(f, 0.0F, 0.1F, 0.0F, 60F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -89.5F * f);
        if(f < 0.5F)
        {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.01F, 0.1F, 0.0F, -45F), 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f, 0.01F, 0.1F, 0.0F, 45F), 0.0F);
        } else
        {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.9F, 0.99F, -45F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f, 0.9F, 0.99F, 45F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, cvt(f, 0.0F, 0.1F, 0.0F, -60F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        xyz[2] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.33F, 0.0F, 0.33F);
        hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
        resetYPRmodifier();
        xyz[1] = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.33F, 0.0F, 0.33F);
        hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
        resetYPRmodifier();
        xyz[2] = cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.154F, 0.0F, 0.154F);
        hierMesh().chunkSetLocate("GearC3_D0", xyz, ypr);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("GearC31_D0", -20F * f, 0.0F, 0.0F);
        super.moveRudder(f);
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
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;

        case 3:
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("HMask4_D0", false);
            hierMesh().chunkVisible("Pilot4_D1", true);
            break;

        case 4:
            hierMesh().chunkVisible("Pilot5_D0", false);
            hierMesh().chunkVisible("HMask5_D0", false);
            hierMesh().chunkVisible("Pilot5_D1", true);
            break;

        case 5:
            hierMesh().chunkVisible("Pilot6_D0", false);
            hierMesh().chunkVisible("HMask6_D0", false);
            hierMesh().chunkVisible("Pilot6_D1", true);
            break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1") || s.endsWith("p2"))
                    if(Math.abs(v1.x) > 0.5D)
                        getEnergyPastArmor(7.94D / Math.abs(v1.x), shot);
                    else
                        getEnergyPastArmor(9.5299997329711914D / (1.0D - Math.abs(v1.x)), shot);
                if(s.endsWith("p3"))
                    getEnergyPastArmor(7.94D / (Math.abs(v1.z) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("p4"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(v1.x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("p5") || s.endsWith("p6"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(v1.y) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("p7"))
                    getEnergyPastArmor(0.5D / (Math.abs(v1.z) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("p8"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(v1.x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a1"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(v1.y) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a2"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(v1.y) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a3"))
                    getEnergyPastArmor(6.3499999046325684D / (Math.abs(v1.x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a4") || s.endsWith("a5"))
                    getEnergyPastArmor(12.699999809265137D / (Math.abs(v1.x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a6") || s.endsWith("a7"))
                    getEnergyPastArmor(6.3499999046325684D / (Math.abs(v1.x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("r1"))
                    getEnergyPastArmor(3.17D / (Math.abs(v1.x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("r2") || s.endsWith("r3"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(v1.x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("c1") || s.endsWith("c2"))
                    getEnergyPastArmor(8.7299995422363281D / (Math.abs(v1.x) + 9.9999999747524271E-007D), shot);
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
                    if(getEnergyPastArmor(3F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.12F)
                        {
                            FM.AS.setControlsDamage(shot.initiator, 1);
                            debuggunnery("Evelator Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.12F)
                        {
                            FM.AS.setControlsDamage(shot.initiator, 2);
                            debuggunnery("Rudder Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.12F)
                        {
                            FM.AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Aileron Controls Out..");
                        }
                    }
                    break;

                case 2:
                    if(getEnergyPastArmor(1.5F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.15F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        debuggunnery("*** Engine1 Throttle Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.15F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        debuggunnery("*** Engine1 Prop Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        debuggunnery("Ailerons Controls Out..");
                    }
                    break;

                case 3:
                    if(getEnergyPastArmor(1.5F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.15F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                        debuggunnery("*** Engine2 Throttle Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.15F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                        debuggunnery("*** Engine2 Prop Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        debuggunnery("Ailerons Controls Out..");
                    }
                    break;

                case 4:
                case 5:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        debuggunnery("Evelator Controls Out..");
                    }
                    break;

                case 6:
                case 7:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        debuggunnery("Rudder Controls Out..");
                    }
                    break;
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxspart") && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(19.9F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F)
                {
                    debuggunnery("*** Tail1 Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if((s.endsWith("li1") || s.endsWith("li2")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.endsWith("e1") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if(s.endsWith("e2") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                if(s.startsWith("xxspark1") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(s.startsWith("xxspark2") && chunkDamageVisible("Keel2") > 1 && getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** Keel2 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel2_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxbomb"))
            {
                if(World.Rnd().nextFloat() < 0.01F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
                {
                    debuggunnery("*** Bomb Payload Detonates..");
                    FM.AS.hitTank(shot.initiator, 0, 100);
                    FM.AS.hitTank(shot.initiator, 1, 100);
                    FM.AS.hitTank(shot.initiator, 2, 100);
                    FM.AS.hitTank(shot.initiator, 3, 100);
                    nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int j = 0;
                if(s.startsWith("xxeng2"))
                    j = 1;
                debuggunnery("Engine Module[" + j + "]: Hit..");
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 280000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            FM.AS.setEngineStuck(shot.initiator, j);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 100000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            FM.AS.hitEngine(shot.initiator, j, 2);
                        }
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[j].getCylindersRatio() * 0.66F)
                    {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 32200F)));
                        debuggunnery("Engine Module: Cylinders Hit, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 1000000F)
                        {
                            FM.AS.hitEngine(shot.initiator, j, 2);
                            debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("eqpt") || s.endsWith("cyls") && World.Rnd().nextFloat() < 0.01F)
                {
                    if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                    {
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
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                        FM.EI.engines[j].setEngineStuck(shot.initiator);
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                }
                if(s.endsWith("mag1") || s.endsWith("mag2"))
                {
                    debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                    FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, j);
                }
                if(s.endsWith("oil1"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                        debuggunnery("Engine Module: Oil Radiator Hit..");
                    FM.AS.hitOil(shot.initiator, j);
                }
                if(s.endsWith("prop") && getEnergyPastArmor(0.42F, shot) > 0.0F)
                    FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                if(s.endsWith("supc") && getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Turbine Disabled..");
                    FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int k = s.charAt(6) - 48;
                switch(k)
                {
                case 1:
                case 2:
                    doHitMeATank(shot, 1);
                    break;

                case 3:
                    doHitMeATank(shot, 0);
                    break;

                case 4:
                case 5:
                    doHitMeATank(shot, 2);
                    break;

                case 6:
                    doHitMeATank(shot, 3);
                    break;

                case 7:
                    doHitMeATank(shot, 0);
                    doHitMeATank(shot, 1);
                    doHitMeATank(shot, 2);
                    doHitMeATank(shot, 3);
                    break;
                }
                return;
            }
            if(s.startsWith("xxpnm"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F)
                {
                    debuggunnery("Pneumo System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if(s.startsWith("xxmgun02"))
            {
                FM.AS.setJamBullets(0, 0);
                getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxmgun07"))
            {
                FM.AS.setJamBullets(0, 0);
                getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxmgun08"))
            {
                FM.AS.setJamBullets(0, 1);
                getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxmgun09"))
            {
                FM.AS.setJamBullets(0, 2);
                getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxmgun10"))
            {
                FM.AS.setJamBullets(0, 3);
                getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxmgun13"))
            {
                FM.AS.setJamBullets(0, 4);
                getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxmgun14"))
            {
                FM.AS.setJamBullets(0, 5);
                getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxmgun15"))
            {
                FM.AS.setJamBullets(0, 6);
                getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxmgun16"))
            {
                FM.AS.setJamBullets(0, 7);
                getEnergyPastArmor(12.7F, shot);
                return;
            }
            if(s.startsWith("xxcannon"))
            {
                FM.AS.setJamBullets(1, 0);
                getEnergyPastArmor(44.7F, shot);
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debugprintln(this, "*** Lock Construction: Hit..");
                if(s.startsWith("xxlockr1") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockr2") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    debugprintln(this, "*** Rudder2 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder2_D" + chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxammo0"))
            {
                int l = s.charAt(7) - 48;
                byte byte1;
                byte byte2;
                switch(l)
                {
                case 1:
                default:
                    byte1 = 0;
                    byte2 = 0;
                    break;

                case 2:
                    byte1 = 10;
                    byte2 = 0;
                    break;

                case 3:
                    byte1 = 11;
                    byte2 = 0;
                    break;

                case 4:
                    byte1 = 11;
                    byte2 = 1;
                    break;

                case 5:
                    byte1 = 12;
                    byte2 = 0;
                    break;

                case 6:
                    byte1 = 12;
                    byte2 = 1;
                    break;

                case 7:
                    byte1 = 0;
                    byte2 = 0;
                    break;

                case 8:
                    byte1 = 0;
                    byte2 = 1;
                    break;

                case 9:
                    byte1 = 0;
                    byte2 = 2;
                    break;

                case 10:
                    byte1 = 0;
                    byte2 = 3;
                    break;
                }
                if(World.Rnd().nextFloat() < 0.125F)
                    FM.AS.setJamBullets(byte1, byte2);
                getEnergyPastArmor(4.7F, shot);
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
            if(World.Rnd().nextFloat() < 0.0575F)
                if(point3d.y > 0.0D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                else
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            if(point3d.x > 1.726D)
            {
                if(point3d.z > 0.444D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                if(point3d.z > -0.281D && point3d.z < 0.444D)
                    if(point3d.y > 0.0D)
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                    else
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                if(point3d.x > 2.774D && point3d.x < 3.718D && point3d.z > 0.425D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                if(World.Rnd().nextFloat() < 0.12F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            }
        } else
        if(s.startsWith("xnose"))
        {
            if(chunkDamageVisible("Nose1") < 2)
                hitChunk("Nose1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xkeel2"))
        {
            if(chunkDamageVisible("Keel2") < 2)
                hitChunk("Keel2", shot);
        } else
        if(s.startsWith("xrudder1"))
        {
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xrudder2"))
        {
            if(chunkDamageVisible("Rudder2") < 2)
                hitChunk("Rudder2", shot);
        } else
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
        {
            if(chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
        } else
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 1)
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
            if(chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
        } else
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 1)
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
            if(World.Rnd().nextFloat() < 0.1F)
            {
                debuggunnery("*** Gear Hydro Failed..");
                FM.Gears.setHydroOperable(false);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(s.startsWith("xturret1"))
                FM.AS.setJamBullets(10, 0);
            if(s.endsWith("2b1"))
                FM.AS.setJamBullets(11, 0);
            if(s.endsWith("2b2"))
                FM.AS.setJamBullets(11, 1);
            if(s.endsWith("3b1"))
                FM.AS.setJamBullets(12, 0);
            if(s.endsWith("3b2"))
                FM.AS.setJamBullets(12, 1);
            if(s.endsWith("4a"))
                FM.AS.setJamBullets(13, 1);
            if(s.endsWith("5a"))
                FM.AS.setJamBullets(14, 1);
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

    private final void doHitMeATank(Shot shot, int i)
    {
        if(getEnergyPastArmor(0.2F, shot) > 0.0F)
            if(shot.power < 14100F)
            {
                if(FM.AS.astateTankStates[i] == 0)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    FM.AS.doSetTankState(shot.initiator, i, 1);
                }
                if(shot.powerType == 3 && FM.AS.astateTankStates[i] > 0 && World.Rnd().nextFloat() < 0.25F)
                    FM.AS.hitTank(shot.initiator, i, 2);
            } else
            {
                FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int)(shot.power / 56000F)));
            }
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
        }
        for(int i = 1; i < 6; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 90F * f, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 13:
            killPilot(this, 2);
            return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        kangle0 = 0.95F * kangle0 + 0.05F * FM.EI.engines[0].getControlRadiator();
        if(kangle0 > 1.0F)
            kangle0 = 1.0F;
        for(int i = 1; i < 14; i++)
            hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * kangle0, 0.0F);

        kangle1 = 0.95F * kangle1 + 0.05F * FM.EI.engines[1].getControlRadiator();
        if(kangle1 > 1.0F)
            kangle1 = 1.0F;
        for(int j = 1; j < 14; j++)
            hierMesh().chunkSetAngles("Waterr" + j + "_D0", 0.0F, -20F * kangle1, 0.0F);

        super.update(f);
    }

    private static final float anglesc7[] = {
        0.0F, -6.5F, -13.5F, -24.5F, -32.5F, -39.75F, -47F, -54.75F, -62.5F, -69.75F, 
        -83.5F
    };
    private static final float anglesc6[] = {
        0.0F, -20.5F, -39.5F, -57.25F, -70F, -79.75F, -87.5F, -92.75F, -95F, -94F, 
        -85F
    };
    private float kangle0;
    private float kangle1;

    static 
    {
        Class class1 = B_25.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
