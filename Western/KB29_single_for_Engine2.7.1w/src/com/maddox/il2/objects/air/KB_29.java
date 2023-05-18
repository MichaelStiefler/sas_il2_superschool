
package com.maddox.il2.objects.air;

import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme4, TypeTransport, Aircraft, PaintScheme

public abstract class KB_29 extends Scheme4
    implements TypeTransport
{

    public KB_29()
    {
    }

    protected void hitBone(java.lang.String s, com.maddox.il2.ai.Shot shot, com.maddox.JGP.Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxammo0"))
            {
                int i = s.charAt(7) - 48;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.15F)
                    switch(i)
                    {
                    case 1: // '\001'
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.347F)
                            FM.AS.setJamBullets(12, 0);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.347F)
                            FM.AS.setJamBullets(12, 1);
                        break;

                    case 2: // '\002'
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.347F)
                            FM.AS.setJamBullets(13, 0);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.347F)
                            FM.AS.setJamBullets(13, 1);
                        break;

                    case 4: // '\004'
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.223F)
                            FM.AS.setJamBullets(10, 0);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.223F)
                            FM.AS.setJamBullets(10, 1);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.223F)
                            FM.AS.setJamBullets(10, 2);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.223F)
                            FM.AS.setJamBullets(10, 3);
                        break;

                    case 5: // '\005'
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.347F)
                            FM.AS.setJamBullets(11, 0);
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.347F)
                            FM.AS.setJamBullets(11, 1);
                        break;
                    }
            } else
            if(s.startsWith("xxcontrols"))
            {
                int j = s.charAt(10) - 48;
                switch(j)
                {
                case 1: // '\001'
                case 2: // '\002'
                case 3: // '\003'
                    if(getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Aileron Controls Out..");
                    }
                    break;

                case 4: // '\004'
                    if(getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F)
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    break;

                case 5: // '\005'
                case 6: // '\006'
                    if(getEnergyPastArmor(1.0F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F)
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    break;
                }
            } else
            if(s.startsWith("xxeng"))
            {
                int k = s.charAt(5) - 49;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, k);
                            Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine (" + k + ") Crank Case Hit - Engine Stucks..");
                        }
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 85000F)
                        {
                            FM.AS.hitEngine(shot.initiator, k, 2);
                            Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine (" + k + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.005F)
                    {
                        FM.EI.engines[k].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[k].setReadyness(shot.initiator, FM.EI.engines[k].getReadyness() - 0.00082F);
                        Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine (" + k + ") Crank Case Hit - Readyness Reduced to " + FM.EI.engines[k].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(5.85F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < FM.EI.engines[k].getCylindersRatio() * 0.75F)
                    {
                        FM.EI.engines[k].setCyliderKnockOut(shot.initiator, com.maddox.il2.ai.World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine (" + k + ") Cylinders Hit, " + FM.EI.engines[k].getCylindersOperable() + "/" + FM.EI.engines[k].getCylinders() + " Left..");
                        if(com.maddox.il2.ai.World.Rnd().nextFloat() < shot.power / 18000F)
                        {
                            FM.AS.hitEngine(shot.initiator, k, 2);
                            Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine (" + k + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("mag1"))
                {
                    FM.EI.engines[k].setMagnetoKnockOut(shot.initiator, 0);
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine (" + k + ") Module: Magneto #0 Destroyed..");
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("prop") && getEnergyPastArmor(0.2F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F)
                {
                    if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F)
                        FM.EI.engines[k].setKillPropAngleDevice(shot.initiator);
                    else
                        FM.EI.engines[k].setKillPropAngleDeviceSpeeds(shot.initiator);
                    getEnergyPastArmor(15.1F, shot);
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine (" + k + ") Module: Prop Governor Fails..");
                }
                if(s.endsWith("oil") && getEnergyPastArmor(0.2F, shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.5F)
                {
                    FM.AS.setOilState(shot.initiator, k, 1);
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine (" + k + ") Module: Oil Filter Pierced..");
                }
            } else
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxspare1") && chunkDamageVisible("Engine1") > 2 && getEnergyPastArmor(19.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Engine1_D3", shot.initiator);
                }
                if(s.startsWith("xxspare2") && chunkDamageVisible("Engine2") > 2 && getEnergyPastArmor(19.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine2 Spars Damaged..");
                    nextDMGLevels(1, 2, "Engine2_D3", shot.initiator);
                }
                if(s.startsWith("xxspare3") && chunkDamageVisible("Engine3") > 2 && getEnergyPastArmor(19.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine3 Spars Damaged..");
                    nextDMGLevels(1, 2, "Engine3_D3", shot.initiator);
                }
                if(s.startsWith("xxspare4") && chunkDamageVisible("Engine4") > 2 && getEnergyPastArmor(19.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Engine4 Spars Damaged..");
                    nextDMGLevels(1, 2, "Engine4_D3", shot.initiator);
                }
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.8F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.8F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(16.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D" + chunkDamageVisible("Keel1"), shot.initiator);
                }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(16.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** StabL: Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D" + chunkDamageVisible("StabL"), shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(16.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F)
                {
                    Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** StabR: Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D" + chunkDamageVisible("StabR"), shot.initiator);
                }
                if(!s.startsWith("xxspart") || chunkDamageVisible("Tail1") <= 2 || getEnergyPastArmor(16.6F * com.maddox.il2.ai.World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F);
            } else
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                l /= 2;
                if(getEnergyPastArmor(0.06F, shot) > 0.0F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(shot.powerType == 3)
                    {
                        if(shot.power < 16100F)
                        {
                            if(FM.AS.astateTankStates[l] < 4 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.21F)
                                FM.AS.hitTank(shot.initiator, l, 1);
                        } else
                        {
                            FM.AS.hitTank(shot.initiator, l, com.maddox.il2.ai.World.Rnd().nextInt(1, 1 + (int)(shot.power / 16100F)));
                        }
                    } else
                    if(shot.power > 16100F)
                        FM.AS.hitTank(shot.initiator, l, com.maddox.il2.ai.World.Rnd().nextInt(1, 1 + (int)(shot.power / 16100F)));
                }
            }
        } else
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
        if(s.startsWith("xengine3"))
        {
            if(chunkDamageVisible("Engine3") < 2)
                hitChunk("Engine3", shot);
        } else
        if(s.startsWith("xengine4"))
        {
            if(chunkDamageVisible("Engine4") < 2)
                hitChunk("Engine4", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(com.maddox.il2.ai.World.Rnd().nextFloat() < 0.05F)
            {
                Aircraft.debugprintln(((com.maddox.il2.engine.Actor) (this)), "*** Gear Hydro Failed..");
                FM.Gears.setHydroOperable(false);
            }
        } else
        if(!s.startsWith("xturret") && (s.startsWith("xpilot") || s.startsWith("xhead")))
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
            hitFlesh(i1, shot, ((int) (byte0)));
        }
    }

    public void msgExplosion(com.maddox.il2.ai.Explosion explosion)
    {
        setExplosion(explosion);
        if(explosion.chunkName == null || explosion.power <= 0.0F || !explosion.chunkName.equals("Tail1_D3") && !explosion.chunkName.equals("WingLIn_D3") && !explosion.chunkName.equals("WingRIn_D3") && !explosion.chunkName.equals("WingLMid_D3") && !explosion.chunkName.equals("WingRMid_D3") && !explosion.chunkName.equals("WingLOut_D3") && !explosion.chunkName.equals("WingROut_D3"))
            super.msgExplosion(explosion);
    }

    public static void moveGear(com.maddox.il2.engine.HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 91F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.06F, 0.0F, 93F));
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.06F, 0.0F, 93F));
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 54F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 108F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, 59F * f, 0.0F);
//        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, 0.0F, -61.5F * f);
//        hiermesh.chunkSetAngles("GearC11_D0", 0.0F, -30F * f, 0.0F);
//        hiermesh.chunkSetAngles("GearC12_D0", 0.0F, 30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, 67F), 0.0F);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, -67F), 0.0F);
        hiermesh.chunkSetAngles("GearL9_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, 84F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, -67F), 0.0F);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, 67F), 0.0F);
        hiermesh.chunkSetAngles("GearR9_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, -84F), 0.0F);
    }

    protected void moveGear(float f)
    {
        KB_29.moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.325F, 0.0F, 0.325F);
        hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.325F, 0.0F, -0.325F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.325F, 0.0F, -0.325F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
        super.moveRudder(f);
        if(FM.CT.getGear() > 0.9F)
            hierMesh().chunkSetAngles("GearC33_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.4436F * f;
        Aircraft.xyz[2] = 0.063F * f;
        Aircraft.ypr[1] = 30F * f;
        hierMesh().chunkSetLocate("Flap01_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap02_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected boolean cutFM(int i, int j, com.maddox.il2.engine.Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
            hitProp(1, j, actor);
            FM.EI.engines[1].setEngineStuck(actor);
            FM.AS.hitTank(actor, 1, com.maddox.il2.ai.World.Rnd().nextInt(0, 9));
            // fall through

        case 34: // '"'
            hitProp(0, j, actor);
            FM.EI.engines[0].setEngineStuck(actor);
            FM.AS.hitTank(actor, 0, com.maddox.il2.ai.World.Rnd().nextInt(2, 8));
            FM.AS.hitTank(actor, 1, com.maddox.il2.ai.World.Rnd().nextInt(0, 5));
            // fall through

        case 35: // '#'
            FM.AS.hitTank(actor, 0, com.maddox.il2.ai.World.Rnd().nextInt(0, 4));
            break;

        case 36: // '$'
            hitProp(2, j, actor);
            FM.EI.engines[2].setEngineStuck(actor);
            FM.AS.hitTank(actor, 2, com.maddox.il2.ai.World.Rnd().nextInt(0, 9));
            // fall through

        case 37: // '%'
            hitProp(3, j, actor);
            FM.EI.engines[3].setEngineStuck(actor);
            FM.AS.hitTank(actor, 2, com.maddox.il2.ai.World.Rnd().nextInt(0, 5));
            FM.AS.hitTank(actor, 3, com.maddox.il2.ai.World.Rnd().nextInt(2, 8));
            // fall through

        case 38: // '&'
            FM.AS.hitTank(actor, 3, com.maddox.il2.ai.World.Rnd().nextInt(0, 4));
            break;

        case 25: // '\031'
            FM.turret[0].bIsOperable = false;
            return false;

        case 26: // '\032'
            FM.turret[1].bIsOperable = false;
            return false;

        case 27: // '\033'
            FM.turret[2].bIsOperable = false;
            return false;

        case 28: // '\034'
            FM.turret[3].bIsOperable = false;
            return false;

        case 29: // '\035'
            FM.turret[4].bIsOperable = false;
            return false;

        case 30: // '\036'
            FM.turret[5].bIsOperable = false;
            return false;

        case 19: // '\023'
            killPilot(((com.maddox.il2.engine.Actor) (this)), 5);
            killPilot(((com.maddox.il2.engine.Actor) (this)), 6);
            killPilot(((com.maddox.il2.engine.Actor) (this)), 7);
            killPilot(((com.maddox.il2.engine.Actor) (this)), 8);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay01_D0", 0.0F, 0.0F, -90F * f);
        hierMesh().chunkSetAngles("Bay02_D0", 0.0F, 0.0F, -90F * f);
        hierMesh().chunkSetAngles("Bay03_D0", 0.0F, 0.0F, -90F * f);
        hierMesh().chunkSetAngles("Bay04_D0", 0.0F, 0.0F, -90F * f);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.0023F)
                FM.AS.hitTank(((com.maddox.il2.engine.Actor) (this)), 0, 1);
            if(FM.AS.astateEngineStates[1] > 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.0023F)
                FM.AS.hitTank(((com.maddox.il2.engine.Actor) (this)), 1, 1);
            if(FM.AS.astateEngineStates[2] > 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.0023F)
                FM.AS.hitTank(((com.maddox.il2.engine.Actor) (this)), 2, 1);
            if(FM.AS.astateEngineStates[3] > 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.0023F)
                FM.AS.hitTank(((com.maddox.il2.engine.Actor) (this)), 3, 1);
            if(FM.AS.astateTankStates[0] > 4 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, ((com.maddox.il2.engine.Actor) (this)));
            if(FM.AS.astateTankStates[1] > 4 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, ((com.maddox.il2.engine.Actor) (this)));
            if(FM.AS.astateTankStates[2] > 4 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, ((com.maddox.il2.engine.Actor) (this)));
            if(FM.AS.astateTankStates[3] > 4 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, ((com.maddox.il2.engine.Actor) (this)));
            if(!(FM instanceof com.maddox.il2.fm.RealFlightModel) || !((com.maddox.il2.fm.RealFlightModel)FM).isRealMode())
            {
                for(int i = 0; i < FM.EI.getNum(); i++)
                    if(FM.AS.astateEngineStates[i] > 3 && com.maddox.il2.ai.World.Rnd().nextFloat() < 0.2F)
                        FM.EI.engines[i].setExtinguisherFire();

            }
        }
        for(int j = 1; j < 7; j++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + j + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + j + "_D0", hierMesh().isChunkVisible("Pilot" + j + "_D0"));

    }

    public void update(float f)
    {
        super.update(f);
        for(int i = 0; i < 4; i++)
        {
            float f1 = FM.EI.engines[i].getControlRadiator();
            if(java.lang.Math.abs(flapps[i] - f1) <= 0.01F)
                continue;
            flapps[i] = f1;
            hierMesh().chunkSetAngles("Water" + (i + 1) + "_D0", 0.0F, -10F * f1, 0.0F);
            for(int j = 0; j < 8; j++)
                hierMesh().chunkSetAngles("Water" + (i * 8 + j + 5) + "_D0", 0.0F, 0.0F, -20F * f1);

        }

    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 3: // '\003'
            FM.turret[0].bIsOperable = false;
            FM.turret[1].bIsOperable = false;
            break;

        case 4: // '\004'
            FM.turret[2].bIsOperable = false;
            FM.turret[3].bIsOperable = false;
            break;

        case 5: // '\005'
            FM.turret[4].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
        hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
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

        case 0: // '\0'
            if(f1 < -1F)
            {
                f1 = -1F;
                flag = false;
            }
            if(f1 > 80F)
            {
                f1 = 80F;
                flag = false;
            }
            break;

        case 1: // '\001'
            if(f1 < -80F)
            {
                f1 = -80F;
                flag = false;
            }
            if(f1 > 1.0F)
            {
                f1 = 1.0F;
                flag = false;
            }
            break;

        case 2: // '\002'
            if(f1 < -1F)
            {
                f1 = -1F;
                flag = false;
            }
            if(f1 > 80F)
            {
                f1 = 80F;
                flag = false;
            }
            break;

        case 3: // '\003'
            if(f1 < -80F)
            {
                f1 = -80F;
                flag = false;
            }
            if(f1 > 1.0F)
            {
                f1 = 1.0F;
                flag = false;
            }
            break;

        case 4: // '\004'
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
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    static java.lang.Class _mthclass$(java.lang.String s)
    {
        java.lang.Class class1;
        try
        {
            class1 = java.lang.Class.forName(s);
        }
        catch(java.lang.ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
        return class1;
    }

    private float flapps[] = {
        0.0F, 0.0F, 0.0F, 0.0F
    };

    static 
    {
        Class class1 = com.maddox.il2.objects.air.KB_29.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}