package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public class LAGG_3RD extends LAGG_3
{

    public LAGG_3RD()
    {
        oldctl = -1F;
        curctl = -1F;
    }

    public void update(float f)
    {
        if(FM.AS.isMaster())
            if(curctl == -1F)
            {
                curctl = oldctl = FM.EI.engines[0].getControlThrottle();
            } else
            {
                curctl = FM.EI.engines[0].getControlThrottle();
                if((curctl - oldctl) / f > 3F && FM.EI.engines[0].getRPM() < 2100F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.25F)
                    FM.AS.hitEngine(this, 0, 100);
                if((curctl - oldctl) / f < -3F && FM.EI.engines[0].getRPM() < 2100F && FM.EI.engines[0].getStage() == 6)
                {
                    if(World.Rnd().nextFloat() < 0.25F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                        FM.EI.engines[0].setEngineStops(this);
                    if(World.Rnd().nextFloat() < 0.75F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                        FM.EI.engines[0].setKillCompressor(this);
                }
                oldctl = curctl;
            }
        if(Config.isUSE_RENDER() && FM.AS.isMaster())
            if(FM.EI.engines[0].getPowerOutput() > 0.8F && FM.EI.engines[0].getStage() == 6)
            {
                if(FM.EI.engines[0].getPowerOutput() > 0.95F)
                    FM.AS.setSootState(this, 0, 3);
                else
                    FM.AS.setSootState(this, 0, 2);
            } else
            {
                FM.AS.setSootState(this, 0, 0);
            }
        if(FM.getSpeed() > 5F)
        {
            hierMesh().chunkSetAngles("SlatL_D0", 0.0F, cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 1.2F), 0.0F);
            hierMesh().chunkSetAngles("SlatR_D0", 0.0F, cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 1.2F), 0.0F);
        }
        super.update(f);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC99_D0", 0.0F, -75F * f, 0.0F);
        float f1 = Math.max(-f * 1200F, -80F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxammo"))
            {
                if(getEnergyPastArmor(4.81F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < 0.12F)
                        FM.AS.setJamBullets(1, 0);
                    if(World.Rnd().nextFloat() < 0.12F)
                        FM.AS.setJamBullets(1, 1);
                    if(World.Rnd().nextFloat() < 0.012F)
                        FM.AS.explodeTank(shot.initiator, 0);
                    getEnergyPastArmor(8.98F, shot);
                }
                return;
            }
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                    getEnergyPastArmor(12.7F / (1E-005F + (float)Math.abs(v1.x)), shot);
                else
                if(s.endsWith("g1"))
                {
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    getEnergyPastArmor(18.15F / (1E-005F + (float)Math.abs(v1.x)), shot);
                }
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(Pd.x > -0.35499998927116394D)
                {
                    if(World.Rnd().nextFloat() < 0.1F || getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        if(World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    }
                } else
                if(Pd.x > -1.2849999666213989D)
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    if(World.Rnd().nextFloat() < 0.2F)
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    if(World.Rnd().nextFloat() < 0.2F)
                        FM.AS.setControlsDamage(shot.initiator, 2);
                } else
                if(getEnergyPastArmor(0.31F, shot) > 0.0F)
                    FM.AS.setControlsDamage(shot.initiator, 1);
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
                if(point3d.x > 1.054D && point3d.x < 1.417D)
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6));
                if(World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass)
                    FM.AS.hitEngine(shot.initiator, 0, 2);
                getEnergyPastArmor(14.296F, shot);
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
        if((s.startsWith("xcf") || s.startsWith("xcockpit") && Pd.z > 0.73299998044967651D) && chunkDamageVisible("CF") < 3)
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

    private float oldctl;
    private float curctl;

    static 
    {
        Class class1 = LAGG_3RD.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "LaGG");
        Property.set(class1, "meshName", "3DO/Plane/LaGG-3RD(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/LaGG-3RD.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLAGG_3RD.class });
        Property.set(class1, "LOSElevation", 0.90695F);
        weaponTriggersRegister(class1, new int[] {
            1, 1
        });
        weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02"
        });
    }
}
