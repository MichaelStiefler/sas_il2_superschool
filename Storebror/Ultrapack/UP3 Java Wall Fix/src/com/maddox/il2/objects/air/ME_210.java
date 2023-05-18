package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class ME_210 extends Scheme2
{

    public ME_210()
    {
        kangle0 = 0.0F;
        kangle1 = 0.0F;
        slpos = 0.0F;
        llpos = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        hierMesh().chunkVisible("Oil1_D0", true);
        hierMesh().chunkVisible("Oil2_D0", true);
    }

    public void update(float f)
    {
        if(FM.getSpeed() > 5F)
        {
            slpos = 0.7F * slpos + 0.13F * (FM.getAOA() <= 6.6F ? 0.0F : 0.07F);
            resetYPRmodifier();
            xyz[0] = slpos;
            hierMesh().chunkSetLocate("SlatL_D0", xyz, ypr);
            xyz[0] = -slpos;
            hierMesh().chunkSetLocate("SlatR_D0", xyz, ypr);
        }
        hierMesh().chunkSetAngles("WaterL_D0", 0.0F, -17F * kangle0, 0.0F);
        hierMesh().chunkSetAngles("WaterL1_D0", 0.0F, -17F * kangle0, 0.0F);
        hierMesh().chunkSetAngles("OilL_D0", 0.0F, -30F * kangle0, 0.0F);
        kangle0 = 0.95F * kangle0 + 0.05F * FM.EI.engines[0].getControlRadiator();
        hierMesh().chunkSetAngles("WaterR_D0", 0.0F, -17F * kangle1, 0.0F);
        hierMesh().chunkSetAngles("WaterR1_D0", 0.0F, 17F * kangle1, 0.0F);
        hierMesh().chunkSetAngles("OilR_D0", 0.0F, -30F * kangle1, 0.0F);
        kangle1 = 0.95F * kangle1 + 0.05F * FM.EI.engines[1].getControlRadiator();
        FM.turret[1].target = FM.turret[0].target;
        if(FM.AS.bLandingLightOn)
        {
            if(llpos < 1.0F)
            {
                llpos += 0.5F * f;
                hierMesh().chunkSetAngles("LLamp_D0", 0.0F, 0.0F, 90F * llpos);
            }
        } else
        if(llpos > 0.0F)
        {
            llpos -= 0.5F * f;
            hierMesh().chunkSetAngles("LLamp_D0", 0.0F, 0.0F, 90F * llpos);
        }
        super.update(f);
    }

    protected void moveAirBrake(float f)
    {
        if(f > 0.05F)
        {
            for(int i = 1; i < 23; i++)
                hierMesh().chunkVisible("Brake" + (i >= 10 ? "" + i : "0" + i) + "_D0", true);

            hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake03_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake04_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake05_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake06_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake07_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake08_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake09_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake10_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake12_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake13_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake14_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake15_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake16_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake17_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake18_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake19_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake20_D0", 0.0F, 90F * f, 0.0F);
            hierMesh().chunkSetAngles("Brake21_D0", 0.0F, 90F * f, 0.0F);
        } else
        {
            for(int j = 1; j < 23; j++)
                hierMesh().chunkVisible("Brake" + (j >= 10 ? "" + j : "0" + j) + "_D0", false);

        }
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                    getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 45.2F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p2") || s.endsWith("p2"))
                {
                    getEnergyPastArmor(World.Rnd().nextFloat(8F, 12F), shot);
                    if(shot.power <= 0.0F)
                    {
                        debugprintln(this, "*** Armor: Nose Armor: Bullet Reflected..");
                        doRicochetBack(shot);
                    }
                } else
                if(s.endsWith("p4"))
                {
                    getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    debuggunnery("Armor: Armor Glass: Hit..");
                    if(shot.power <= 0.0F)
                    {
                        debuggunnery("Armor: Armor Glass: Bullet Stopped..");
                        doRicochetBack(shot);
                    }
                } else
                if(s.endsWith("p5"))
                    getEnergyPastArmor(10.1D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p6"))
                    getEnergyPastArmor(12D + World.Rnd().nextFloat(10F, 30F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p7"))
                    getEnergyPastArmor(8.0799999237060547D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p8"))
                {
                    getEnergyPastArmor(1.0099999904632568D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    if(shot.powerType == 3)
                        shot.powerType = 0;
                } else
                if(s.endsWith("p9"))
                    getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p10"))
                    getEnergyPastArmor(9.0900001525878906D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p11"))
                {
                    getEnergyPastArmor(64.639999389648438D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.startsWith("xxarmore"))
                    getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.startsWith("xxarmorw"))
                    getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

                case 1:
                    if(getEnergyPastArmor(2.45F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Barbette Controls: Disabled..");
                        FM.turret[0].bIsOperable = false;
                        if(FM.CT.Weapons[10] != null)
                        {
                            if(FM.CT.Weapons[10][0] != null)
                                FM.AS.setJamBullets(10, 0);
                            if(FM.CT.Weapons[10][1] != null)
                                FM.AS.setJamBullets(10, 1);
                        }
                    }
                    break;

                case 2:
                case 3:
                    if(getEnergyPastArmor(0.002F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                    }
                    break;

                case 4:
                    if(getEnergyPastArmor(World.Rnd().nextFloat(6.8F, 12F), shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        debuggunnery("Controls: Elevator Controls: Destroyed..");
                    }
                    break;

                case 5:
                case 6:
                case 7:
                case 8:
                    if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        debuggunnery("Controls: Aileron Controls: Disabled..");
                    }
                    break;
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(6.56F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int j = s.charAt(5) - 49;
                debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Hit..");
                if(s.endsWith("prop"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
                            debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Prop Governor Hit, Disabled..");
                        } else
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                            debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Prop Governor Hit, Damaged..");
                        }
                } else
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            FM.EI.engines[j].setEngineStuck(shot.initiator);
                            debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Bullet Jams Reductor Gear..");
                        } else
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 3);
                            FM.AS.setEngineSpecificDamage(shot.initiator, j, 4);
                            debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Reductor Gear Damaged, Prop Governor Failed..");
                        }
                } else
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                        debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Supercharger Disabled..");
                    }
                } else
                if(s.endsWith("feed"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F && FM.EI.engines[j].getPowerOutput() > 0.7F)
                    {
                        FM.AS.hitEngine(shot.initiator, j, 100);
                        debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Pressurized Fuel Line Pierced, Fuel Flamed..");
                    }
                } else
                if(s.endsWith("fuel"))
                {
                    if(getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        FM.EI.engines[j].setEngineStops(shot.initiator);
                        debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Fuel Line Stalled, Engine Stalled..");
                    }
                } else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.1F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, j);
                            debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Bullet Jams Crank Ball Bearing..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            FM.AS.hitEngine(shot.initiator, j, 2);
                            debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Crank Case Hit, Readyness Reduced to " + FM.EI.engines[j].getReadyness() + "..");
                        }
                        FM.EI.engines[j].setReadyness(shot.initiator, FM.EI.engines[j].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Crank Case Hit, Readyness Reduced to " + FM.EI.engines[j].getReadyness() + "..");
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(22.5F, 33.6F), shot);
                } else
                if(s.endsWith("cyl1") || s.endsWith("cyl2"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[j].getCylindersRatio() * 1.75F)
                    {
                        FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Cylinders Hit, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 24000F)
                        {
                            FM.AS.hitEngine(shot.initiator, j, 3);
                            debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Cylinders Hit, Engine Fires..");
                        }
                        if(World.Rnd().nextFloat() < 0.01F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, j);
                            debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Bullet Jams Piston Head..");
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                } else
                if(s.endsWith("mag1") || s.endsWith("mag2"))
                {
                    int l1 = s.charAt(9) - 49;
                    FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, l1);
                    debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Magneto " + l1 + " Destroyed..");
                } else
                if(s.endsWith("oil1"))
                {
                    FM.AS.hitOil(shot.initiator, j);
                    debuggunnery("Engine Module (" + (j != 0 ? "Right" : "Left") + "): Oil Radiator Hit..");
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 6.879F), shot) > 0.0F)
                {
                    int k = s.charAt(5) - 49;
                    FM.AS.hitOil(shot.initiator, k);
                    getEnergyPastArmor(0.22F, shot);
                    debuggunnery("Engine Module (" + (k != 0 ? "Right" : "Left") + "): Oil Tank Pierced..");
                }
                return;
            }
            if(s.startsWith("xxw"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 4.27F), shot) > 0.0F)
                {
                    int l = s.charAt(3) - 49;
                    if(FM.AS.astateEngineStates[l] == 0)
                    {
                        debuggunnery("Engine Module (" + (l != 0 ? "Right" : "Left") + "): Water Radiator Pierced..");
                        FM.AS.hitEngine(shot.initiator, l, 2);
                        FM.AS.doSetEngineState(shot.initiator, l, 2);
                    }
                    getEnergyPastArmor(2.22F, shot);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int i1 = s.charAt(6) - 48;
                switch(i1)
                {
                case 1:
                    i1 = 0;
                    break;

                case 2:
                case 3:
                    i1 = 1;
                    break;

                case 4:
                case 5:
                    i1 = 2;
                    break;

                case 6:
                    i1 = 3;
                    break;
                }
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.23F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[i1] == 0)
                    {
                        debuggunnery("Fuel Tank " + (i1 + 1) + ": Pierced..");
                        FM.AS.hitTank(shot.initiator, i1, 1);
                        FM.AS.doSetTankState(shot.initiator, i1, 1);
                    } else
                    if(FM.AS.astateTankStates[i1] == 1)
                    {
                        debuggunnery("Fuel Tank " + (i1 + 1) + ": Pierced..");
                        FM.AS.hitTank(shot.initiator, i1, 1);
                        FM.AS.doSetTankState(shot.initiator, i1, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.hitTank(shot.initiator, 2, 2);
                        debuggunnery("Fuel Tank " + (i1 + 1) + ": Hit..");
                    }
                }
                return;
            }
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    debuggunnery("Nose Gun: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    debuggunnery("Nose Gun: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 12.96F), shot);
            }
            if(s.startsWith("xxcannon"))
            {
                if(s.endsWith("01"))
                {
                    debuggunnery("Nose Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.endsWith("02"))
                {
                    debuggunnery("Nose Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if(s.startsWith("xxbarbette"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(2.58F, 28.37F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    FM.turret[0].bIsOperable = false;
                    if(FM.CT.Weapons[10] != null)
                    {
                        if(FM.CT.Weapons[10][0] != null)
                            FM.AS.setJamBullets(10, 0);
                        if(FM.CT.Weapons[10][1] != null)
                            FM.AS.setJamBullets(10, 1);
                    }
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
            {
                debuggunnery("Bomb Payload Detonates..");
                FM.AS.hitTank(shot.initiator, 0, 10);
                FM.AS.hitTank(shot.initiator, 1, 10);
                FM.AS.hitTank(shot.initiator, 2, 10);
                FM.AS.hitTank(shot.initiator, 3, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            return;
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit") || s.startsWith("xnose"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(!s.startsWith("xcockpit"));
        } else
        if(s.startsWith("xengine"))
        {
            int j1 = s.charAt(7) - 48;
            if(chunkDamageVisible("Engine" + j1) < 2)
                hitChunk("Engine" + j1, shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 3)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 3)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 3)
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
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.endsWith("2"))
            {
                if(World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(6.8F, 29.35F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    FM.AS.setInternalDamage(shot.initiator, 3);
                }
                String s1 = "" + s.charAt(5);
                hitChunk("Gear" + s1.toUpperCase() + "2", shot);
            }
        } else
        if(s.startsWith("xturret"))
        {
            int k1 = s.charAt(7) - 49;
            if(getEnergyPastArmor(0.25F, shot) > 0.0F)
            {
                debuggunnery("Armament System: Turret (" + (k1 + 1) + ") Machine Gun: Disabled..");
                FM.AS.setJamBullets(10, k1);
                getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i2;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i2 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i2 = s.charAt(6) - 49;
            } else
            {
                i2 = s.charAt(5) - 49;
            }
            hitFlesh(i2, shot, byte0);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = 10F * Math.min(f, 0.1F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, 80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 130F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 130F * f1, 0.0F);
        f1 = 10F * (f >= 0.5F ? Math.min(1.0F - f, 0.1F) : Math.min(f, 0.1F));
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL9_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -140F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -55F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -55F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR9_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -140F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -55F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -55F * f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.getGear() < 0.99F)
        {
            return;
        } else
        {
            hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
            return;
        }
    }

    public void moveWheelSink()
    {
        if(FM.CT.getGear() < 0.99F)
        {
            return;
        } else
        {
            resetYPRmodifier();
            xyz[1] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.35F, 0.0F, 0.35F);
            ypr[1] = -85F;
            hierMesh().chunkSetLocate("GearL9_D0", xyz, ypr);
            xyz[1] = -cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.35F, 0.0F, 0.35F);
            ypr[1] = -85F;
            hierMesh().chunkSetLocate("GearR9_D0", xyz, ypr);
            return;
        }
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1:
            FM.turret[0].setHealth(f);
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
            if(!FM.AS.bIsAboutToBailout)
                hierMesh().chunkVisible("Gore1_D0", true);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            if(!FM.AS.bIsAboutToBailout)
                hierMesh().chunkVisible("Gore2_D0", true);
            break;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 33:
            hitProp(0, j, actor);
            break;

        case 36:
            hitProp(1, j, actor);
            break;

        case 11:
        case 19:
            hierMesh().chunkVisible("Wire_D0", false);
            break;

        case 34:
            FM.AS.hitEngine(this, 0, 2);
            if(World.Rnd().nextFloat() < 0.66F)
                FM.AS.hitEngine(this, 0, 2);
            break;

        case 37:
            FM.AS.hitEngine(this, 1, 2);
            if(World.Rnd().nextFloat() < 0.66F)
                FM.AS.hitEngine(this, 1, 2);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.06F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.06F)
                FM.AS.hitTank(this, 2, 1);
            if(FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.02F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.02F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.08F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.08F)
                FM.AS.hitTank(this, 0, 1);
            if(FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.08F)
                FM.AS.hitTank(this, 3, 1);
            if(FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.08F)
                FM.AS.hitTank(this, 2, 1);
        }
        for(int i = 1; i < 3; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

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
            if(f < -65F)
            {
                f = -65F;
                flag = false;
            }
            if(f > 65F)
            {
                f = 65F;
                flag = false;
            }
            if(f1 < -10F)
            {
                f1 = -10F;
                flag = false;
            }
            if(f1 > 40F)
            {
                f1 = 40F;
                flag = false;
            }
            break;

        case 1:
            if(f < -65F)
            {
                f = -65F;
                flag = false;
            }
            if(f > 65F)
            {
                f = 65F;
                flag = false;
            }
            if(f1 < -40F)
            {
                f1 = -40F;
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

    protected void moveFlap(float f)
    {
        float f1 = -45F * f;
        hierMesh().chunkSetAngles("Flap1_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap2_D0", 0.0F, f1, 0.0F);
    }

    private float kangle0;
    private float kangle1;
    private float slpos;
    private float llpos;

    static 
    {
        Class class1 = ME_210.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
