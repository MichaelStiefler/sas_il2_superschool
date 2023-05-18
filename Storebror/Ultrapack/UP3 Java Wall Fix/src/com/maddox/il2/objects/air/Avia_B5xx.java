package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public abstract class Avia_B5xx extends Scheme1
    implements TypeScout, TypeFighter, TypeTNBFighter, TypeStormovik
{

    public Avia_B5xx()
    {
        bChangedPit = true;
        pit = null;
        suspR = 0.0F;
        suspL = 0.0F;
    }

    protected void moveFlap(float f)
    {
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
    }

    public void registerPit(CockpitAVIA_B534 cockpitavia_b534)
    {
        pit = cockpitavia_b534;
    }

    public void missionStarting()
    {
        super.missionStarting();
        if(FM.CT.Weapons[0] == null && pit != null)
            pit.hideAllBullets();
        hierMesh().chunkVisible("pilotarm2_d0", true);
        hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    public void prepareCamouflage()
    {
        super.prepareCamouflage();
        hierMesh().chunkVisible("pilotarm2_d0", true);
        hierMesh().chunkVisible("pilotarm1_d0", true);
        if(FM.CT.Weapons[0] == null && pit != null)
            pit.hideAllBullets();
    }

    public void moveCockpitDoor(float f)
    {
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = f * 0.62F;
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, f, 0.0F);
    }

    public void moveWheelSink()
    {
        suspL = 0.9F * suspL + 0.1F * FM.Gears.gWheelSinking[0] * 23F;
        hierMesh().chunkSetAngles("GearL2_D0", 0.0F, suspL * 5F, 0.0F);
        hierMesh().chunkSetAngles("GearL3_D0", 0.0F, suspL * 3.67F, 0.0F);
        hierMesh().chunkSetAngles("GearL4_D0", -suspL * 1.3F, 0.0F, 0.0F);
        suspR = 0.9F * suspR + 0.1F * FM.Gears.gWheelSinking[1] * 23F;
        hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -suspR * 5F, 0.0F);
        hierMesh().chunkSetAngles("GearR3_D0", 0.0F, -suspR * 3.67F, 0.0F);
        hierMesh().chunkSetAngles("GearR4_D0", suspR * 1.3F, 0.0F, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 19:
            FM.Gears.hitCentreGear();
            break;

        case 9:
            if(hierMesh().chunkFindCheck("GearL3_D0") != -1)
            {
                hierMesh().hideSubTrees("GearL3_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("GearL3_D0"));
                wreckage.collide(true);
                FM.Gears.hitLeftGear();
            }
            break;

        case 10:
            if(hierMesh().chunkFindCheck("GearR3_D0") != -1)
            {
                hierMesh().hideSubTrees("GearR3_D0");
                Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("GearR3_D0"));
                wreckage1.collide(true);
                FM.Gears.hitRightGear();
            }
            break;

        case 3:
            if(World.Rnd().nextInt(0, 99) < 1)
            {
                FM.AS.hitEngine(this, 0, 4);
                hitProp(0, j, actor);
                FM.EI.engines[0].setEngineStuck(actor);
                return cut("engine1");
            } else
            {
                FM.AS.setEngineDies(this, 0);
                return false;
            }
        }
        return super.cutFM(i, j, actor);
    }

    public boolean cut(String s)
    {
        boolean flag = super.cut(s);
        if(s.equalsIgnoreCase("WingLIn"))
            hierMesh().chunkVisible("WingLMid_CAP", true);
        else
        if(s.equalsIgnoreCase("WingRIn"))
            hierMesh().chunkVisible("WingRMid_CAP", true);
        return flag;
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 20F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
        float f1 = FM.CT.getAileron();
        hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - cvt(f, -1F, 1.0F, -37F, 35F));
        hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f, -1F, 0.0F, -61F, 0.0F) + cvt(f, 0.0F, 1.0F, 0.0F, 43F));
        if(f < 0.0F)
            f /= 2.0F;
        hierMesh().chunkSetAngles("Stick_D0", 0.0F, 15F * f1, cvt(f, -1F, 1.0F, -16F, 16F));
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -20F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
        float f1 = FM.CT.getElevator();
        hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f, -1F, 1.0F, 6F, -8F) - cvt(f1, -1F, 1.0F, -37F, 35F));
        hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f, -1F, 1.0F, -16F, 14F) + cvt(f1, -1F, 0.0F, -61F, 0.0F) + cvt(f1, 0.0F, 1.0F, 0.0F, 43F));
        if(f1 < 0.0F)
            f1 /= 2.0F;
        hierMesh().chunkSetAngles("Stick_D0", 0.0F, 15F * f, cvt(f1, -1F, 1.0F, -16F, 20F));
    }

    public void update(float f)
    {
        float f1 = FM.EI.engines[0].getControlRadiator();
        f1 = (-48F * f1 - 42F) * f1 + 33F;
        kangle = 0.95F * kangle + 0.05F * f1;
        hierMesh().chunkSetAngles("radiator1_D0", 0.0F, kangle, 0.0F);
        hierMesh().chunkSetAngles("radiator2_D0", 0.0F, kangle, 0.0F);
        hierMesh().chunkSetAngles("radiator3_D0", 0.0F, kangle, 0.0F);
        hierMesh().chunkSetAngles("radiator4_D0", 0.0F, kangle, 0.0F);
        hierMesh().chunkSetAngles("radiator5_D0", 0.0F, kangle, 0.0F);
        hierMesh().chunkSetAngles("radiator6_D0", 0.0F, kangle, 0.0F);
        super.update(f);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag && FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.4F)
            FM.EI.engines[0].setExtinguisherFire();
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
            hierMesh().chunkVisible("Head1_D1", true);
            hierMesh().chunkVisible("pilotarm2_d0", false);
            hierMesh().chunkVisible("pilotarm1_d0", false);
            break;
        }
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        hierMesh().chunkVisible("pilotarm2_d0", false);
        hierMesh().chunkVisible("pilotarm1_d0", false);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1") || s.endsWith("p3"))
                    getEnergyPastArmor(9.96F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(s.endsWith("8"))
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#8)");
                    }
                } else
                if(s.endsWith("9"))
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#9)");
                    }
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out.. (#9)");
                    }
                } else
                if(s.endsWith("5"))
                {
                    if(World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#5)");
                    }
                } else
                if(s.endsWith("6"))
                {
                    if(World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#6)");
                    }
                } else
                if((s.endsWith("2") || s.endsWith("4")) && World.Rnd().nextFloat() < 0.5F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Arone Controls Out.. (#2/#4)");
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if(s.endsWith("prop"))
                    Aircraft.debugprintln(this, "*** Prop hit");
                else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.1F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                        }
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12.7F, shot);
                } else
                if(s.endsWith("cyl1") || s.endsWith("cyl2"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.12F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if(World.Rnd().nextFloat() < 0.005F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                } else
                if(s.endsWith("oil1"))
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                } else
                if(s.endsWith("supc"))
                {
                    Aircraft.debugprintln(this, "*** Engine Module: Supercharger Hit..");
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
                    }
                }
            } else
            if(s.endsWith("gear"))
            {
                Aircraft.debugprintln(this, "*** Engine Module: Gear Hit..");
                if(getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F)
                {
                    FM.AS.setEngineStuck(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: gear hit, engine stuck..");
                }
            } else
            if(s.startsWith("xxtank"))
            {
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F)
                {
                    if(FM.AS.astateTankStates[0] == 0)
                    {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        FM.AS.hitTank(shot.initiator, 0, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.hitTank(shot.initiator, 0, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            } else
            if(s.startsWith("xxlock"))
            {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                } else
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                } else
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
            } else
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #1: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #2: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                if(s.endsWith("03"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #3: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                    if(pit != null)
                        pit.jamLeftGun();
                }
                if(s.endsWith("04"))
                {
                    Aircraft.debugprintln(this, "*** Fixed Gun #4: Disabled..");
                    FM.AS.setJamBullets(1, 1);
                    if(pit != null)
                        pit.jamRightGun();
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            } else
            if(s.startsWith("xxspar"))
            {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(9.5F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                } else
                if(s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D" + chunkDamageVisible("WingLIn"), shot.initiator);
                } else
                if(s.startsWith("xxsparri") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D" + chunkDamageVisible("WingRIn"), shot.initiator);
                } else
                if(s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                } else
                if(s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                } else
                if(s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                } else
                if(s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                }
            }
            return;
        }
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else
            {
                i = s.charAt(5) - 49;
            }
            Aircraft.debugprintln(this, "*** hitFlesh..");
            hitFlesh(i, shot, byte0);
        } else
        if(s.startsWith("xcockpit"))
        {
            if(World.Rnd().nextFloat() < 0.2F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            else
            if(World.Rnd().nextFloat() < 0.4F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
            else
            if(World.Rnd().nextFloat() < 0.6F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            else
            if(World.Rnd().nextFloat() < 0.8F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
            else
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
        } else
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
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
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 2)
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
            Aircraft.debugprintln(this, "*** xWing: " + s);
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
        }
    }

    protected float kangle;
    public boolean bChangedPit;
    private CockpitAVIA_B534 pit;
    float suspR;
    float suspL;

    static 
    {
        Class class1 = Avia_B5xx.class;
        Property.set(class1, "originCountry", PaintScheme.countrySlovakia);
    }
}
