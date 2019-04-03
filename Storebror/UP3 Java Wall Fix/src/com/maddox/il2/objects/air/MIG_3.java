package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class MIG_3 extends Scheme1
    implements TypeFighter
{

    public MIG_3()
    {
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            if(!FM.AS.bIsAboutToBailout)
            {
                if(hierMesh().isChunkVisible("Blister1_D0"))
                    hierMesh().chunkVisible("Gore1_D0", true);
                hierMesh().chunkVisible("Gore2_D0", true);
            }
            break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debugprintln(this, "*** Armor: Hit..");
                if(s.endsWith("p1"))
                    getEnergyPastArmor(9.1D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(9.1D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor(5.6F, shot);
                else
                if(s.endsWith("t3"))
                    getEnergyPastArmor(6.1999998092651367D / (Math.abs(v1.z) + 9.9999997473787516E-006D), shot);
                else
                if(s.endsWith("t4"))
                    getEnergyPastArmor(6.1999998092651367D / (Math.abs(v1.z) + 9.9999997473787516E-006D), shot);
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
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 2:
                case 3:
                    if(getEnergyPastArmor(0.3F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;

                case 4:
                    if(getEnergyPastArmor(9.6F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Elevator Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    break;

                case 5:
                case 7:
                    if(getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        debugprintln(this, "*** Aileron Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 6:
                case 8:
                    if(getEnergyPastArmor(1.45F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Aileron Controls: Control Crank Link Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;
                }
            }
            if(s.startsWith("xxspar"))
            {
                debugprintln(this, "*** Spar Construction: Hit..");
                if(s.startsWith("xxsparlm") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLMid Spar Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                }
                if(s.startsWith("xxsparrm") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRMid Spar Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                }
                if(s.startsWith("xxsparlo") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLOut Spar Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                }
                if(s.startsWith("xxsparro") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingROut Spar Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                }
                if(s.startsWith("xxsparsl") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D" + chunkDamageVisible("StabL"), shot.initiator);
                }
                if(s.startsWith("xxsparsr") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D" + chunkDamageVisible("StabR"), shot.initiator);
                }
            }
            if(s.startsWith("xxlock"))
            {
                debugprintln(this, "*** Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** Rudder Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
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
            }
            if(s.startsWith("xxeng"))
            {
                debugprintln(this, "*** Engine Module: Hit..");
                if(s.endsWith("prop"))
                {
                    if(getEnergyPastArmor(3.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else
                        {
                            debugprintln(this, "*** Engine Module: Prop Governor Hit, Damaged..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                } else
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debugprintln(this, "*** Engine Module: Bullet Jams Reductor Gear..");
                        FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Engine Module: Supercharger Disabled..");
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else
                if(s.endsWith("fue1"))
                {
                    if(getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Engine Module: Fuel Line Pierced, Engine Fired..");
                        FM.AS.hitEngine(shot.initiator, 0, 100);
                    }
                } else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    getEnergyPastArmor(22.5F, shot);
                } else
                if(s.endsWith("cyl1"))
                {
                    if(getEnergyPastArmor(1.3F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        debugprintln(this, "*** Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                            FM.AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if(World.Rnd().nextFloat() < 0.01F)
                        {
                            debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                    if(Math.abs(point3d.y) < 0.1379999965429306D && getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debugprintln(this, "*** Engine Module: Feed Gear Hit, Engine Stalled..");
                            FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debugprintln(this, "*** Engine Module: Feed Gear Hit, Engine Jams..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debugprintln(this, "*** Engine Module: Feed Gear Hit, Cylinders Feed Cut..");
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.startsWith("xxeng1mag"))
                {
                    int j = s.charAt(9) - 49;
                    debugprintln(this, "*** Engine Module: Magneto " + j + " Destroyed..");
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                } else
                if(s.startsWith("xxeng1oil") && getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                    FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if(s.startsWith("xxoil") && getEnergyPastArmor(2.1F, shot) > 0.0F)
            {
                debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
                FM.AS.hitOil(shot.initiator, 0);
                getEnergyPastArmor(4.96F, shot);
            }
            if(s.startsWith("xxw1") && getEnergyPastArmor(0.1F, shot) > 0.0F)
            {
                debugprintln(this, "*** Engine Module: Water Filter Pierced..");
                FM.AS.hitOil(shot.initiator, 0);
                getEnergyPastArmor(4.96F, shot);
            }
            if(s.startsWith("xxtank"))
            {
                int k = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.32F)
                {
                    if(FM.AS.astateTankStates[k] == 0)
                    {
                        debugprintln(this, "*** Fuel Tank" + k + ": Pierced..");
                        FM.AS.hitTank(shot.initiator, k, 2);
                        FM.AS.doSetTankState(shot.initiator, k, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        debugprintln(this, "*** Fuel Tank" + k + ": Hit..");
                        FM.AS.hitTank(shot.initiator, k, 2);
                    }
                }
            }
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    debugprintln(this, "*** Cowling Gun: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    debugprintln(this, "*** Cowling Gun: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                if(s.endsWith("03"))
                {
                    debugprintln(this, "*** Cowling Gun: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 12.96F), shot);
            }
            if(s.startsWith("xxcannon"))
            {
                if(s.endsWith("01"))
                {
                    debugprintln(this, "*** Cowling Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.endsWith("02"))
                {
                    debugprintln(this, "*** Cowling Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if(s.startsWith("xxpnm") && getEnergyPastArmor(4.8F, shot) > 0.0F)
            {
                debugprintln(this, "*** Pneumo System: Off-Line..");
                FM.AS.setInternalDamage(shot.initiator, 1);
            }
            if(s.startsWith("xxhyd") && getEnergyPastArmor(4.8F, shot) > 0.0F)
            {
                debugprintln(this, "*** Hydro System: Off-Line..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.startsWith("xxins1"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            return;
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit") || s.startsWith("xwater"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(s.startsWith("xcockpit"))
                if(point3d.z > 0.6D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                else
                if(point3d.y > 0.0D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                else
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
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
            hitChunk("Keel1", shot);
        else
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
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLMid") < 2)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRMid") < 2)
                hitChunk("WingRMid", shot);
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
            if(s.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner"))
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debugprintln(this, "*** Hydro System (Wheel): Off-Line..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F)
            {
                debugprintln(this, "*** Gears: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
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
            hitFlesh(l, shot, byte0);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -78F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -78F * f, 0.0F);
        float f1 = Math.max(-f * 1500F, -90F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 70F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, -80F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void msgExplosion(Explosion explosion)
    {
        setExplosion(explosion);
        if(!(this instanceof MIG_3U) && explosion.chunkName != null)
        {
            if(explosion.chunkName.startsWith("CF"))
            {
                if(World.Rnd().nextFloat() < 0.005F)
                    FM.AS.setControlsDamage(explosion.initiator, 0);
                if(World.Rnd().nextFloat() < 0.005F)
                    FM.AS.setControlsDamage(explosion.initiator, 1);
                if(World.Rnd().nextFloat() < 0.005F)
                    FM.AS.setControlsDamage(explosion.initiator, 2);
                if(explosion.power > 0.0041F && World.Rnd().nextFloat() < 0.025F)
                    FM.AS.hitTank(explosion.initiator, 0, World.Rnd().nextInt(0, (int)(explosion.power * 808F)));
                if(explosion.power > 0.011F)
                {
                    FM.AS.hitTank(explosion.initiator, 0, World.Rnd().nextInt(0, (int)(1.0F + explosion.power * 808F)));
                    FM.AS.hitPilot(explosion.initiator, 0, (int)(explosion.power * 15333F * World.Rnd().nextFloat()));
                }
            }
            if(explosion.chunkName.startsWith("Engine"))
                if(explosion.power > 0.011F)
                {
                    FM.AS.hitEngine(explosion.initiator, 0, (int)(1.0F + explosion.power * 666F * World.Rnd().nextFloat(0.5F, 1.0F)));
                    FM.AS.hitOil(explosion.initiator, 0);
                } else
                {
                    FM.AS.hitEngine(explosion.initiator, 0, (int)(explosion.power * 450F * World.Rnd().nextFloat(0.5F, 1.0F)));
                }
            if(explosion.chunkName.startsWith("WingLMid"))
            {
                if(explosion.power > 0.0041F && World.Rnd().nextFloat() < 0.05F)
                    FM.AS.hitTank(explosion.initiator, 2, World.Rnd().nextInt(0, (int)(explosion.power * 909F)));
                if(explosion.power > 0.011F)
                    FM.AS.hitTank(explosion.initiator, 2, World.Rnd().nextInt(0, (int)(1.0F + explosion.power * 707F)));
            }
            if(explosion.chunkName.startsWith("WingRMid"))
            {
                if(explosion.power > 0.0041F && World.Rnd().nextFloat() < 0.05F)
                    FM.AS.hitTank(explosion.initiator, 3, World.Rnd().nextInt(0, (int)(explosion.power * 909F)));
                if(explosion.power > 0.011F)
                    FM.AS.hitTank(explosion.initiator, 3, World.Rnd().nextInt(0, (int)(1.0F + explosion.power * 707F)));
            }
            if(explosion.chunkName.startsWith("Tail"))
            {
                if(World.Rnd().nextFloat() < 0.01F)
                    FM.AS.setControlsDamage(explosion.initiator, 1);
                if(World.Rnd().nextFloat() < 0.11F)
                    FM.AS.setControlsDamage(explosion.initiator, 2);
            }
        }
        super.msgExplosion(explosion);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag && FM.AS.astateTankStates[0] > 5)
            FM.AS.repairTank(0);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
    }

    static 
    {
        Class class1 = MIG_3.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
