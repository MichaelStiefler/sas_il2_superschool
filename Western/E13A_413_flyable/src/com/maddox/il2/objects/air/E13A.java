
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public abstract class E13A extends Scheme1BomberType90Mk1
    implements TypeSeaPlane, TypeScout, TypeStormovik
{

    public E13A()
    {
        bChangedPit = false;
        bGunUp = false;
        btme = -1L;
        fGunPos = 0.0F;
        flapps = 0.0F;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 9: // '\t'
        case 33: // '!'
            FM.Gears.bIsSail = false;
            break;

        case 10: // '\n'
        case 36: // '$'
            FM.Gears.bIsSail = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean canOpenBombBay()
    {
        if(FM.CT.Weapons[3] != null)
        {
            for(int i = 0; i < FM.CT.Weapons[3].length; i++)
                if(FM.CT.Weapons[3][i].haveBullets() && FM.CT.Weapons[3][i].getHookName().startsWith("_ExternalBomb"))
                    return false;

        }
        return true;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.65F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public void moveSteering(float f)
    {
    }

    public void moveWheelSink()
    {
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("GearR12_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("GearL12_D0", 0.0F, -30F * f, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 5; i++)
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
        float f2 = -30F;
        if(f < 0.0F)
        {
            if(f < -20F)
                f2 = cvt(f, -41F, -20F, -30F, -15F);
            else
                f2 = cvt(f, -20F, -10F, -15F, -8F);
        } else
        if(f > 20F)
            f2 = cvt(f, 20F, 41F, -15F, -30F);
        else
            f2 = cvt(f, 10F, 20F, -8F, -15F);
        switch(i)
        {
        case 0: // '\0'
            if(f < -54F)
            {
                f = -54F;
                flag = false;
            }
            if(f > 54F)
            {
                f = 54F;
                flag = false;
            }
            if(f1 < f2)
            {
                f1 = f2;
                flag = false;
            }
            if(f1 > 55F)
            {
                f1 = 55F;
                flag = false;
            }
            if(f > -0.9F && f < 0.9F && f1 < 15.5F)
                flag = false;
            if(f > -32F && f < 32F && f1 < -8F && f1 > -15F)
                flag = false;
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 2: // '\002'
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

        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("HMask2_D0", false);
            break;

        case 2: // '\002'
        case 3: // '\003'
            if(hierMesh().isChunkVisible("Pilot3_D0"))
            {
                hierMesh().chunkVisible("Pilot3_D0", false);
                hierMesh().chunkVisible("Pilot3_D1", true);
                hierMesh().chunkVisible("HMask3_D0", false);
            } else
            {
                hierMesh().chunkVisible("Pilot4_D0", false);
                hierMesh().chunkVisible("Pilot4_D1", true);
                hierMesh().chunkVisible("HMask4_D0", false);
            }
            break;
        }
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLFold_D0", 0.0F, -156F * f, 0.0F);
        hiermesh.chunkSetAngles("WingRFold_D0", 0.0F, -156F * f, 0.0F);
    }

    public void moveWingFold(float f)
    {
        moveWingFold(hierMesh(), f);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("BDoor1_D0", 0.0F, -86F * f, 0.0F);
        hierMesh().chunkSetAngles("BDoor2_D0", 0.0F, -86F * f, 0.0F);
    }

    public void update(float f)
    {
        super.update(f);
        float f1 = FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f1) > 0.01F)
        {
            flapps = f1;
            for(int i = 1; i < 14; i++)
            {
                String s = "Cowflap" + i + "_D0";
                hierMesh().chunkSetAngles(s, 0.0F, -30F * f1, 0.0F);
            }

        }
        if(!bGunUp)
        {
            if(fGunPos > 0.0F)
            {
                fGunPos -= 0.2F * f;
                FM.turret[0].bIsOperable = false;
                hierMesh().chunkVisible("Turret1A_D0", false);
                hierMesh().chunkVisible("Turret1B_D0", false);
                hierMesh().chunkVisible("Pilot3_D0", true);
                hierMesh().chunkVisible("Pilot4_D0", false);
            }
        } else
        if(fGunPos < 1.0F)
        {
            fGunPos += 0.2F * f;
            if(fGunPos > 0.8F && fGunPos < 0.9F)
            {
                FM.turret[0].bIsOperable = true;
                hierMesh().chunkVisible("Turret1A_D0", true);
                hierMesh().chunkVisible("Turret1B_D0", true);
                hierMesh().chunkVisible("Pilot3_D0", false);
                hierMesh().chunkVisible("Pilot4_D0", true);
            }
        }
        if(fGunPos < 0.333F)
            hierMesh().chunkSetAngles("Blister4_D0", 0.0F, -Aircraft.cvt(fGunPos, 0.0F, 0.333F, 0.0F, 41F), 0.0F);
        else
        if(fGunPos < 0.666F)
        {
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(fGunPos, 0.333F, 0.666F, 0.0F, -0.4F);
            hierMesh().chunkSetLocate("Blister3_D0", Aircraft.xyz, Aircraft.ypr);
        } else
        {
            hierMesh().chunkSetAngles("Blister4_D0", 0.0F, -Aircraft.cvt(fGunPos, 0.666F, 1.0F, 41F, 71F), 0.0F);
        }
        if(FM.turret[0].bIsAIControlled)
        {
            if(FM.turret[0].target != null && FM.AS.astatePilotStates[2] < 90)
                bGunUp = true;
            if(Time.current() > btme)
            {
                btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if(FM.turret[0].target == null && FM.AS.astatePilotStates[2] < 90)
                    bGunUp = false;
            }
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

                case 1: // '\001'
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    break;

                case 3: // '\003'
                    if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    break;
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(23F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F)
                {
                    debuggunnery("*** Tail1 Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2") || s.endsWith("lo3") || s.endsWith("lo4")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2") || s.endsWith("ro3") || s.endsWith("ro4")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(23F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                    } else
                    if(World.Rnd().nextFloat() < 0.04F)
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    else
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.02F);
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.startsWith("xxeng1mag"))
                {
                    int j = s.charAt(9) - 49;
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                }
                if(s.endsWith("oil1"))
                    FM.AS.hitOil(shot.initiator, 0);
                return;
            }
            if(s.startsWith("xxoil"))
            {
                FM.AS.hitOil(shot.initiator, 0);
                getEnergyPastArmor(0.22F, shot);
            }
            if(s.startsWith("xxtank"))
            {
                int k = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[k] == 0)
                    {
                        FM.AS.hitTank(shot.initiator, k, 1);
                        FM.AS.doSetTankState(shot.initiator, k, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                        FM.AS.hitTank(shot.initiator, k, 2);
                }
            }
            return;
        }
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
        if(s.startsWith("xengine"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xturret1a"))
        {
            FM.AS.setJamBullets(10, 0);
            getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
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

    public boolean bChangedPit;
    boolean bGunUp;
    public long btme;
    public float fGunPos;
    protected float flapps;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.E13A.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
