package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class Hurricane extends Scheme1
    implements TypeFighter, TypeTNBFighter
{

    public Hurricane()
    {
        kangle = 0.0F;
    }

    public void update(float f)
    {
        hierMesh().chunkSetAngles("Water1_D0", 0.0F, -22.5F * kangle, 0.0F);
        kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
        super.update(f);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.65F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.0F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, -26F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -152F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, -26F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -152F * f, 0.0F);
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
        resetYPRmodifier();
        Aircraft.xyz[2] = -Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.25F, 0.0F, 0.25F);
        hierMesh().chunkSetLocate("GearL10_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.25F, 0.0F, 0.25F);
        hierMesh().chunkSetLocate("GearR10_D0", Aircraft.xyz, Aircraft.ypr);
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
                if(s.endsWith("p1"))
                    getEnergyPastArmor(0.78F, shot);
                else
                if(s.endsWith("g1") || s.endsWith("g2"))
                    getEnergyPastArmor(8F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
            if(s.startsWith("xxcontrols"))
                if(s.endsWith("1"))
                {
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
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out..");
                    }
                } else
                if(s.endsWith("2"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                } else
                if(s.endsWith("3") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                }
            if(s.startsWith("xxspar"))
            {
                if((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.5F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if((s.endsWith("sl1") || s.endsWith("sl2") || s.endsWith("sl3") || s.endsWith("sl4") || s.endsWith("sl5")) && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
                }
                if((s.endsWith("sr1") || s.endsWith("sr2") || s.endsWith("sr3") || s.endsWith("sr4") || s.endsWith("sr5")) && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
                }
                if(s.endsWith("e1"))
                    getEnergyPastArmor(6F, shot);
            }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("prp") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if(s.endsWith("cas") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < shot.power / 200000F)
                    {
                        FM.AS.setEngineStuck(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 50000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 28000F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    }
                    FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                }
                if(s.endsWith("cyl") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(FM.AS.astateEngineStates[0] < 1)
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("sup") && getEnergyPastArmor(0.05F, shot) > 0.0F)
                    FM.EI.engines[0].setKillCompressor(shot.initiator);
            }
            if(s.startsWith("xxoil"))
                FM.AS.hitOil(shot.initiator, 0);
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F)
                        FM.AS.hitTank(shot.initiator, i, 2);
                }
            }
            if(s.startsWith("xxmgunl4"))
                FM.AS.setJamBullets(0, (World.Rnd().nextInt(0, 3) * 2 + 1) - 1);
            if(s.startsWith("xxmgunr4"))
                FM.AS.setJamBullets(0, World.Rnd().nextInt(1, 4) * 2 - 1);
            if(s.startsWith("xxmgunl2"))
                FM.AS.setJamBullets(0, (World.Rnd().nextInt(0, 1) * 2 + 9) - 1);
            if(s.startsWith("xxmgunr2"))
                FM.AS.setJamBullets(0, World.Rnd().nextInt(5, 6) * 2 - 1);
            if(s.startsWith("xxammol4") && shot.power > 27000F && World.Rnd().nextFloat() < 0.1F)
            {
                FM.AS.setJamBullets(0, 0);
                FM.AS.setJamBullets(0, 2);
                FM.AS.setJamBullets(0, 4);
                FM.AS.setJamBullets(0, 6);
            }
            if(s.startsWith("xxammor4") && shot.power > 27000F && World.Rnd().nextFloat() < 0.1F)
            {
                FM.AS.setJamBullets(0, 1);
                FM.AS.setJamBullets(0, 3);
                FM.AS.setJamBullets(0, 5);
                FM.AS.setJamBullets(0, 7);
            }
            if(s.startsWith("xxammol2") && shot.power > 27000F && World.Rnd().nextFloat() < 0.1F)
            {
                FM.AS.setJamBullets(0, 8);
                FM.AS.setJamBullets(0, 10);
            }
            if(s.startsWith("xxammor2") && shot.power > 27000F && World.Rnd().nextFloat() < 0.1F)
            {
                FM.AS.setJamBullets(0, 9);
                FM.AS.setJamBullets(0, 11);
            }
            if(s.startsWith("xxhispa1"))
                FM.AS.setJamBullets(0, 0);
            if(s.startsWith("xxhispa2"))
                FM.AS.setJamBullets(0, 1);
            if(s.startsWith("xxhispa3"))
                FM.AS.setJamBullets(0, 2);
            if(s.startsWith("xxhispa4"))
                FM.AS.setJamBullets(0, 3);
            if(s.startsWith("xxshvak1"))
                FM.AS.setJamBullets(1, 0);
            if(s.startsWith("xxshvak2"))
                FM.AS.setJamBullets(1, 1);
            if(s.startsWith("xxshkas1"))
                FM.AS.setJamBullets(0, 0);
            if(s.startsWith("xxshkas2"))
                FM.AS.setJamBullets(0, 1);
        } else
        if(s.startsWith("xcf") || s.startsWith("xoil"))
        {
            hitChunk("CF", shot);
            if(point3d.x > -2.2D)
            {
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                if(Aircraft.v1.x < -0.8D && World.Rnd().nextFloat() < 0.2F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                if(Aircraft.v1.x < -0.89999997615814209D && World.Rnd().nextFloat() < 0.2F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                if(Math.abs(Aircraft.v1.x) < 0.8D)
                    if(point3d.y > 0.0D)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                        if(World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                    } else
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                        if(World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
                    }
            }
        } else
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 3)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xrudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 3)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 3)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl"))
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr"))
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid"))
            {
                if(chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if(World.Rnd().nextFloat() < shot.mass + 0.02F)
                    FM.AS.hitOil(shot.initiator, 0);
            }
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

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 11:
            hierMesh().chunkVisible("Wire_D0", false);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    private float kangle;

    static 
    {
        Class class1 = Hurricane.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
