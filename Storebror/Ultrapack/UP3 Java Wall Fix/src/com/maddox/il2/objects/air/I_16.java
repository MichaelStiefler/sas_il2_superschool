package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class I_16 extends Scheme1
    implements TypeFighter
{

    public I_16()
    {
        radiat = 0.0F;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debugprintln(this, "*** Armor: Hit..");
                if(s.endsWith("p1"))
                    getEnergyPastArmor(8.26F / (Math.abs((float)v1.x) + 1E-005F), shot);
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
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                    }
                    break;

                case 3:
                    if(getEnergyPastArmor(8.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        debugprintln(this, "*** Elevator Controls: Disabled..");
                    }
                    break;

                case 4:
                case 5:
                    if(getEnergyPastArmor(2.3F, shot) > 0.0F && World.Rnd().nextFloat() < 0.31F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        debugprintln(this, "*** Rudder Controls: Disabled..");
                    }
                    break;
                }
            }
            if(s.startsWith("xxspar"))
            {
                debugprintln(this, "*** Spar Construction: Hit..");
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.5F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F)
                {
                    debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if(s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D" + chunkDamageVisible("WingLIn"), shot.initiator);
                }
                if(s.startsWith("xxsparri") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D" + chunkDamageVisible("WingRIn"), shot.initiator);
                }
                if(s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                }
                if(s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                }
                if(s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                }
                if(s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                }
                if(s.startsWith("xxstabl") && getEnergyPastArmor(16.2F, shot) > 0.0F)
                {
                    debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D" + chunkDamageVisible("StabL"), shot.initiator);
                }
                if(s.startsWith("xxstabr") && getEnergyPastArmor(16.2F, shot) > 0.0F)
                {
                    debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D" + chunkDamageVisible("StabR"), shot.initiator);
                }
            }
            if(s.startsWith("xxlock"))
            {
                debugprintln(this, "*** Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
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
            }
            if(s.startsWith("xxeng"))
            {
                debugprintln(this, "*** Engine Module: Hit..");
                if(s.endsWith("prop"))
                {
                    if(getEnergyPastArmor(0.45F / (Math.abs((float)v1.x) + 0.0001F), shot) > 0.0F && World.Rnd().nextFloat() < 0.05F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                    }
                } else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(5.1F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                        }
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    }
                    getEnergyPastArmor(22.5F, shot);
                } else
                if(s.startsWith("xxeng1cyls"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(1.5F, 23.9F), shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.12F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        debugprintln(this, "*** Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 3);
                            debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if(World.Rnd().nextFloat() < 0.005F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                } else
                if(s.endsWith("eqpt"))
                {
                    if(getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                            debugprintln(this, "*** Engine Module: Magneto 0 Destroyed..");
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                            debugprintln(this, "*** Engine Module: Magneto 1 Destroyed..");
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            debugprintln(this, "*** Engine Module: Prop Controls Cut..");
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            debugprintln(this, "*** Engine Module: Throttle Controls Cut..");
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                            debugprintln(this, "*** Engine Module: Mix Controls Cut..");
                        }
                    }
                } else
                if(s.endsWith("oil1"))
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
            }
            if(s.startsWith("xxoil"))
            {
                FM.AS.hitOil(shot.initiator, 0);
                getEnergyPastArmor(0.22F, shot);
                debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
            }
            if(s.startsWith("xxtank1") && getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.15F)
            {
                if(FM.AS.astateTankStates[0] == 0)
                {
                    debugprintln(this, "*** Fuel Tank: Pierced..");
                    FM.AS.hitTank(shot.initiator, 0, 1);
                }
                if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                {
                    FM.AS.hitTank(shot.initiator, 0, 2);
                    debugprintln(this, "*** Fuel Tank: Hit..");
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
                if(s.endsWith("04"))
                {
                    debugprintln(this, "*** Cowling Gun: Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            }
            return;
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            if(World.Rnd().nextFloat() < 0.07F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
        } else
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
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
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int j;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else
            {
                j = s.charAt(5) - 49;
            }
            hitFlesh(j, shot, byte0);
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
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
    }

    public void msgExplosion(Explosion explosion)
    {
        setExplosion(explosion);
        if(explosion.chunkName != null)
        {
            if(explosion.chunkName.startsWith("CF"))
            {
                if(World.Rnd().nextFloat() < 0.01F)
                    FM.AS.setControlsDamage(explosion.initiator, 0);
                if(World.Rnd().nextFloat() < 0.01F)
                    FM.AS.setControlsDamage(explosion.initiator, 1);
                if(World.Rnd().nextFloat() < 0.01F)
                    FM.AS.setControlsDamage(explosion.initiator, 2);
                if(World.Rnd().nextFloat() < 0.01F)
                    FM.AS.hitPilot(explosion.initiator, 0, (int)(explosion.power * 8924F * World.Rnd().nextFloat()));
            }
            if(explosion.chunkName.startsWith("Tail"))
            {
                if(World.Rnd().nextFloat() < 0.01F)
                    FM.AS.setControlsDamage(explosion.initiator, 1);
                if(World.Rnd().nextFloat() < 0.01F)
                    FM.AS.setControlsDamage(explosion.initiator, 2);
            }
        }
        super.msgExplosion(explosion);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        ypr[0] = ypr[1] = ypr[2] = xyz[0] = xyz[1] = xyz[2] = 0.0F;
        float f1 = 1.0F * (float)Math.sin(f * 1.5707963267948966D);
        xyz[0] = -0.052F * f1;
        ypr[0] = 9F * f1;
        ypr[1] = -22F * f1;
        hiermesh.chunkSetLocate("GearL4_D0", xyz, ypr);
        xyz[0] = 0.052F * f1;
        ypr[0] = -9F * f1;
        ypr[1] = 22F * f1;
        hiermesh.chunkSetLocate("GearR4_D0", xyz, ypr);
        hiermesh.chunkSetAngles("GearL2X_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 33F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 6F * f1, -20F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR2X_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", -33F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", -6F * f1, 20F * f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -55F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -55F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, 30F * f, 0.0F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(this == World.getPlayerAircraft())
        {
            FM.Gears.setOperable(true);
            FM.Gears.setHydroOperable(false);
        }
    }

    public void update(float f)
    {
        hierMesh().chunkSetAngles("Engine1P_D0", 0.0F, 19F + 19F * FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
            return super.cutFM(34, j, actor);

        case 36:
            return super.cutFM(37, j, actor);

        case 17:
            FM.hit(17);
            FM.hit(17);
            FM.hit(17);
            return false;

        case 18:
            FM.hit(18);
            FM.hit(18);
            FM.hit(18);
            return false;

        case 19:
            FM.AS.hitTank(this, 0, 2);
            return false;
        }
        return super.cutFM(i, j, actor);
    }

    private float radiat;

    static 
    {
        Class class1 = I_16.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
