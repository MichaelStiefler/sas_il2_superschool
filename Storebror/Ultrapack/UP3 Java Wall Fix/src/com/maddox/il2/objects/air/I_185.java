package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class I_185 extends Scheme1
    implements TypeFighter
{

    public I_185()
    {
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.07F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.14F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.14F)
                FM.AS.hitTank(this, 2, 1);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.07F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.14F)
                FM.AS.hitTank(this, 0, 1);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.07F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.14F)
                FM.AS.hitTank(this, 0, 1);
        }
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -65F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, cvt(f, 0.02F, 0.1F, 0.0F, -65F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f, 0.02F, 0.1F, 0.0F, -65F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 87F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.02F, 0.1F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 87F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f, 0.02F, 0.1F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 0.0F, -90F * f);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        xyz[1] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.15F, 0.0F, 0.15F);
        hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
        hierMesh().chunkSetAngles("GearL5_D0", 0.0F, cvt(xyz[1], 0.0F, 0.15F, 0.0F, -30F), 0.0F);
        hierMesh().chunkSetAngles("GearL4_D0", 0.0F, cvt(xyz[1], 0.0F, 0.15F, 0.0F, -60F), 0.0F);
        xyz[1] = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.15F, 0.0F, 0.15F);
        hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
        hierMesh().chunkSetAngles("GearR5_D0", 0.0F, cvt(xyz[1], 0.0F, 0.15F, 0.0F, -30F), 0.0F);
        hierMesh().chunkSetAngles("GearR4_D0", 0.0F, cvt(xyz[1], 0.0F, 0.15F, 0.0F, -60F), 0.0F);
    }

    public void update(float f)
    {
        if(FM.getSpeed() > 5F)
        {
            resetYPRmodifier();
            xyz[0] = cvt(FM.getAOA(), 6.8F, 11F, 0.0F, -0.025F);
            xyz[2] = cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 0.0125F);
            hierMesh().chunkSetLocate("SlatL_D0", xyz, ypr);
            xyz[0] = cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 0.025F);
            xyz[2] = cvt(FM.getAOA(), 6.8F, 11F, 0.0F, -0.0125F);
            hierMesh().chunkSetLocate("SlatR_D0", xyz, ypr);
        }
        super.update(f);
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

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                    getEnergyPastArmor(12.7F / (1E-005F + (float)Math.abs(v1.x)), shot);
                else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.05F / (1E-005F + (float)Math.abs(v1.x)), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(getEnergyPastArmor(3.276F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    if(World.Rnd().nextFloat() < 0.2F)
                        FM.AS.setControlsDamage(shot.initiator, 2);
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if((s.endsWith("li1") || s.endsWith("li2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(v1.x) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if((s.endsWith("sl1") || s.endsWith("sl2")) && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
                }
                if((s.endsWith("sr1") || s.endsWith("sr2")) && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                if(s.endsWith("al"))
                {
                    if(getEnergyPastArmor(0.35F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** AroneL Lock Damaged..");
                        nextDMGLevels(1, 2, "AroneL_D0", shot.initiator);
                    }
                } else
                if(s.endsWith("ar"))
                {
                    if(getEnergyPastArmor(0.35F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** AroneR Lock Damaged..");
                        nextDMGLevels(1, 2, "AroneR_D0", shot.initiator);
                    }
                } else
                if(s.endsWith("vl1") || s.endsWith("vl2"))
                {
                    if(getEnergyPastArmor(0.35F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** VatorL Lock Damaged..");
                        nextDMGLevels(1, 2, "VatorL_D0", shot.initiator);
                    }
                } else
                if(s.endsWith("vr1") || s.endsWith("vr2"))
                {
                    if(getEnergyPastArmor(0.35F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** VatorR Lock Damaged..");
                        nextDMGLevels(1, 2, "VatorR_D0", shot.initiator);
                    }
                } else
                if((s.endsWith("r1") || s.endsWith("r2")) && getEnergyPastArmor(0.35F, shot) > 0.0F)
                {
                    debugprintln(this, "*** Rudder1 Lock Damaged..");
                    nextDMGLevels(1, 2, "Rudder1_D0", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("prop") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if(s.endsWith("base"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 85000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
                        debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[0].setKillCompressor(shot.initiator);
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("eqpt"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(Pd.y > 0.0D && Pd.z < 0.18899999558925629D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                        if(Pd.y < 0.0D && Pd.z < 0.18899999558925629D && World.Rnd().nextFloat(0.0F, 16000F) < shot.power)
                            FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                FM.AS.hitOil(shot.initiator, 0);
                getEnergyPastArmor(0.1F, shot);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F && shot.powerType == 3)
                    FM.AS.hitTank(shot.initiator, i, 2);
                return;
            }
            if(s.startsWith("xxshvak"))
            {
                int j = s.charAt(7) - 49;
                FM.AS.setJamBullets(1, j);
                getEnergyPastArmor(12F, shot);
                return;
            }
            if(s.startsWith("xxpneu"))
            {
                FM.Gears.setHydroOperable(false);
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit") && Pd.z > 0.73299998044967651D)
            hitChunk("CF", shot);
        if(s.startsWith("xxcockpit"))
        {
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(Pd.y < 0.0D)
            {
                if(World.Rnd().nextFloat() < 0.25F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                if(World.Rnd().nextFloat() < 0.25F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
            } else
            {
                if(World.Rnd().nextFloat() < 0.25F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                if(World.Rnd().nextFloat() < 0.25F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            }
            if(Pd.z > 0.63899999856948853D && Pd.x < -1.0410000085830688D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
        } else
        if(s.startsWith("xeng"))
            hitChunk("Engine1", shot);
        else
        if(s.startsWith("xtail"))
            hitChunk("Tail1", shot);
        else
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
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int k;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else
            {
                k = s.charAt(5) - 49;
            }
            hitFlesh(k, shot, byte0);
        }
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
    }

    static 
    {
        Class class1 = I_185.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
