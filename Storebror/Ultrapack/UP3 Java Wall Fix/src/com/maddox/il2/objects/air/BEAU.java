package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class BEAU extends Scheme2
{

    public BEAU()
    {
    }

    public void moveCockpitDoor(float f)
    {
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -120F), 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
        case 34:
            hitProp(0, j, actor);
            cut("Engine1");
            break;

        case 36:
        case 37:
            hitProp(1, j, actor);
            cut("Engine2");
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        case 0:
            if(f < -50F)
            {
                f = -50F;
                flag = false;
            }
            if(f > 50F)
            {
                f = 50F;
                flag = false;
            }
            if(f1 > Aircraft.cvt(Math.abs(f), 0.0F, 50F, 40F, 25F))
                f1 = Aircraft.cvt(Math.abs(f), 0.0F, 50F, 40F, 25F);
            if(f1 < Aircraft.cvt(Math.abs(f), 0.0F, 50F, -10F, -3.5F))
                f1 = Aircraft.cvt(Math.abs(f), 0.0F, 50F, -10F, -3.5F);
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -60F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.89F, 0.0F, -103F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.29F, 0.0F, -63F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.29F, 0.0F, -58F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.99F, 0.0F, -103F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.39F, 0.0F, -63F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.39F, 0.0F, -58F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.2F, 0.0F, 0.165F);
        hierMesh().chunkSetLocate("GearL25_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.2F, 0.0F, 0.165F);
        hierMesh().chunkSetLocate("GearR25_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("e1"))
                    getEnergyPastArmor(12.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("e2"))
                    getEnergyPastArmor(12.1D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("p1"))
                    getEnergyPastArmor(12.7F, shot);
                if(s.endsWith("p2"))
                    getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                switch(i)
                {
                case 1:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                    {
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
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                            debuggunnery("*** Engine1 Mix Controls Out..");
                        }
                    }
                    break;

                case 2:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                    {
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
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 1, 7);
                            debuggunnery("*** Engine2 Mix Controls Out..");
                        }
                    }
                    break;

                case 3:
                case 4:
                    if(getEnergyPastArmor(6F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            FM.AS.setControlsDamage(shot.initiator, 1);
                            debuggunnery("Evelator Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            FM.AS.setControlsDamage(shot.initiator, 2);
                            debuggunnery("Rudder Controls Out..");
                        }
                    }
                    break;

                case 5:
                case 6:
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        debuggunnery("Ailerons Controls Out..");
                    }
                    break;
                }
            } else
            if(s.startsWith("xxengine"))
            {
                int j = 0;
                if(s.startsWith("xxengine2"))
                    j = 1;
                debuggunnery("Engine Module[" + j + "]: Hit..");
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
            } else
            if(s.startsWith("xxmgun"))
            {
                int k = s.charAt(6) - 49;
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.startsWith("xxoil"))
            {
                int l = 0;
                if(s.endsWith("2"))
                    l = 1;
                if(getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F)
                    FM.AS.hitOil(shot.initiator, l);
                Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Oil Tank Pierced..");
            } else
            {
                if(s.startsWith("xxprop1") && getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                    if(World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine1 Module: Prop Governor Hit, Disabled..");
                    } else
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        Aircraft.debugprintln(this, "*** Engine1 Module: Prop Governor Hit, Damaged..");
                    }
                if(s.startsWith("xxprop2") && getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                    if(World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 3);
                        Aircraft.debugprintln(this, "*** Engine2 Module: Prop Governor Hit, Disabled..");
                    } else
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 4);
                        Aircraft.debugprintln(this, "*** Engine2 Module: Prop Governor Hit, Damaged..");
                    }
                if(s.startsWith("xxspar"))
                {
                    if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    {
                        debuggunnery("*** WingLIn Spars Damaged..");
                        nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                    }
                    if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    {
                        debuggunnery("*** WingRIn Spars Damaged..");
                        nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                    }
                    if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    {
                        debuggunnery("*** WingLMid Spars Damaged..");
                        nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                    if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    {
                        debuggunnery("*** WingRMid Spars Damaged..");
                        nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                    if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    {
                        debuggunnery("*** WingLOut Spars Damaged..");
                        nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                    if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    {
                        debuggunnery("*** WingROut Spars Damaged..");
                        nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                    if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                    {
                        debuggunnery("*** Keel1 Spars Damaged..");
                        nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                    }
                } else
                if(s.startsWith("xxstruts"))
                {
                    if(s.startsWith("xxstruts1") && chunkDamageVisible("Engine1") > 1 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 8F), shot) > 0.0F)
                    {
                        debuggunnery("*** Engine1 Spars Damaged..");
                        nextDMGLevels(1, 2, "Engine1_D2", shot.initiator);
                    }
                    if(s.startsWith("xxstruts2") && chunkDamageVisible("Engine2") > 1 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 8F), shot) > 0.0F)
                    {
                        debuggunnery("*** Engine2 Spars Damaged..");
                        nextDMGLevels(1, 2, "Engine2_D2", shot.initiator);
                    }
                } else
                if(s.startsWith("xxtank"))
                {
                    int i1 = s.charAt(6) - 49;
                    if(getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 10.25F)
                    {
                        if(FM.AS.astateTankStates[i1] == 0)
                        {
                            debuggunnery("Fuel Tank (" + i1 + "): Pierced..");
                            FM.AS.hitTank(shot.initiator, i1, 1);
                            FM.AS.doSetTankState(shot.initiator, i1, 1);
                        }
                        if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.1F)
                        {
                            FM.AS.hitTank(shot.initiator, i1, 2);
                            debuggunnery("Fuel Tank (" + i1 + "): Hit..");
                        }
                    }
                }
            }
        } else
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(point3d.x > 0.5D)
            {
                if(point3d.z > 0.913D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                if(point3d.z > 0.341D)
                {
                    if(point3d.x < 1.402D)
                    {
                        if(point3d.y > 0.0D)
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                        else
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                    } else
                    {
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    }
                } else
                if(point3d.y > 0.0D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                else
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
                if(point3d.x > 1.691D && point3d.x < 1.98D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            }
        } else
        if(s.startsWith("xtail"))
            hitChunk("Tail1", shot);
        else
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder1"))
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
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
        } else
        if(s.startsWith("xgearl"))
            hitChunk("GearL2", shot);
        else
        if(s.startsWith("xgearr"))
            hitChunk("GearR2", shot);
        else
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

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f)
    {
        super.update(f);
        for(int i = 0; i < 2; i++)
        {
            float f1 = FM.EI.engines[i].getControlRadiator();
            if(Math.abs(flapps[i] - f1) <= 0.01F)
                continue;
            flapps[i] = f1;
            for(int j = 1; j < 23; j++)
                hierMesh().chunkSetAngles("Water" + (j + 22 * i) + "_D0", 0.0F, -20F * f1, 0.0F);

        }

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
        }
    }

    private float flapps[] = {
        0.0F, 0.0F
    };

    static 
    {
        Class class1 = BEAU.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
