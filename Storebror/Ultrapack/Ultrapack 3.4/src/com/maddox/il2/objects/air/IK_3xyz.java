package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public abstract class IK_3xyz extends Scheme1
    implements TypeFighter, TypeTNBFighter
{

    public IK_3xyz()
    {
        kangle = 0.0F;
        suspension = 0.0F;
        emergencyPressure = 0.0F;
        flapps = 0.0F;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(this.FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
    }

    public boolean gearEmergencyUp()
    {
        return false;
    }

    public boolean gearEmergencyDown()
    {
        emergencyPressure += 0.1F;
        return emergencyPressure > 1.0F;
    }

    public void update(float f)
    {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f1) > 0.01F)
        {
            flapps = f1;
            f1 = Aircraft.cvt(f1, 0.0F, 1.0F, 34F, -25F);
            kangle = 0.95F * kangle + 0.05F * f1;
            for(int i = 1; i < 8; i++)
                hierMesh().chunkSetAngles("RadiatorFlaps" + i + "_D0", 0.0F, 0.0F, kangle);

        }
        if(this.FM.EI.engines[0].getStage() < 6)
            this.FM.Gears.setHydroOperable(false);
        else
            this.FM.Gears.setHydroOperable(true);
        emergencyPressure -= Aircraft.cvt(emergencyPressure, 0.0F, 1.2F, 0.1F * f, 0.5F * f);
        if(emergencyPressure < 0.0F)
            emergencyPressure = 0.0F;
        super.update(f);
    }

    protected void moveFlap(float f)
    {
        float f1 = -50F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 93.3F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 93.3F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL9_D0", 0.0F, 8F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR9_D0", 0.0F, -8F * f, 0.0F);
        float f3 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear5YScale);
        float f4 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear5XScale);
        hiermesh.chunkSetAngles("GearL5_D0", 5.5F * -f4, -29.3F * f3, 0.0F);
        f3 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear5YScale);
        f4 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear5XScale);
        hiermesh.chunkSetAngles("GearR5_D0", 5.5F * f4, -29.3F * f3, 0.0F);
        float f5 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear4Scale);
        Aircraft.xyz[0] = -f5 * 0.27F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        hiermesh.chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
        f5 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear4Scale);
        Aircraft.xyz[0] = -f5 * 0.27F;
        hiermesh.chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
        float f6 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear7YScale);
        float f7 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear7XScale);
        hiermesh.chunkSetAngles("GearL7_D0", 41F * f7, -33.5F * f6, 0.0F);
        f6 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear7YScale);
        f7 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear7XScale);
        hiermesh.chunkSetAngles("GearR7_D0", -41F * f7, -33.5F * f6, 0.0F);
        float f8 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear6Scale);
        Aircraft.xyz[0] = -f8 * 0.15F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        hiermesh.chunkSetLocate("GearL6_D0", Aircraft.xyz, Aircraft.ypr);
        f8 = floatindex(Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 4F), gear6Scale);
        Aircraft.xyz[0] = -f8 * 0.15F;
        hiermesh.chunkSetLocate("GearR6_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 95F * f);
        hiermesh.chunkSetAngles("GearCLDoor_D0", 0.0F, 120F * f, 0.0F);
        hiermesh.chunkSetAngles("GearCRDoor_D0", 0.0F, -120F * f, 0.0F);
    }

    public static float floatindex(float cvt, float gear5YScale2[])
    {
        return 0.0F;
    }

    public void moveWheelSink()
    {
        if(this.FM.Gears.onGround())
            suspension += 0.008F;
        else
            suspension -= 0.008F;
        if(suspension < 0.0F)
            suspension = 0.0F;
        if(suspension > 0.1F)
            suspension = 0.1F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        float f = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 25F, 0.0F, 1.0F);
        float f1 = this.FM.Gears.gWheelSinking[0] * f + suspension;
        f1 = Aircraft.cvt(f1, 0.0F, 0.25F, 0.0F, 0.2F);
        Aircraft.xyz[1] = -f1;
        hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearL8_D0", 0.0F, f1 * 160F, 0.0F);
        f1 = this.FM.Gears.gWheelSinking[1] * f + suspension;
        f1 = Aircraft.cvt(f1, 0.0F, 0.25F, 0.0F, 0.2F);
        Aircraft.xyz[1] = -f1;
        hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearR8_D0", 0.0F, f1 * 160F, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Head1_D1", true);
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
                    getEnergyPastArmor(0.78F, shot);
                else
                if(s.endsWith("g1") || s.endsWith("g2"))
                    getEnergyPastArmor(8F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(s.endsWith("1"))
                {
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out..");
                    }
                } else
                if(s.endsWith("2"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                } else
                if(s.endsWith("3") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                }
                return;
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
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("prp") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    this.FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if(s.endsWith("cas") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < shot.power / 200000F)
                    {
                        this.FM.AS.setEngineStuck(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 50000F)
                    {
                        this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 28000F)
                    {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    }
                    this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                }
                if(s.endsWith("cyl") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.75F)
                {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if(this.FM.AS.astateEngineStates[0] < 1)
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("sup") && getEnergyPastArmor(0.05F, shot) > 0.0F)
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                return;
            }
            if(s.startsWith("xxoil"))
                this.FM.AS.hitOil(shot.initiator, 0);
            debuggunnery("xxoil1 Tank Pierced..");
            if(s.startsWith("xxwater") && World.Rnd().nextFloat() < 0.2F)
                this.FM.AS.hitOil(shot.initiator, 0);
            debuggunnery("xxwater1 Radiator Pierced..");
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F)
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                }
            }
            if(s.startsWith("xxmgunl4"))
                this.FM.AS.setJamBullets(0, (World.Rnd().nextInt(0, 3) * 2 + 1) - 1);
            if(s.startsWith("xxmgunr4"))
                this.FM.AS.setJamBullets(0, World.Rnd().nextInt(1, 4) * 2 - 1);
            if(s.startsWith("xxmgunl2"))
                this.FM.AS.setJamBullets(0, (World.Rnd().nextInt(0, 1) * 2 + 9) - 1);
            if(s.startsWith("xxmgunr2"))
                this.FM.AS.setJamBullets(0, World.Rnd().nextInt(5, 6) * 2 - 1);
            if(s.startsWith("xxammol4") && shot.power > 27000F && World.Rnd().nextFloat() < 0.1F)
            {
                this.FM.AS.setJamBullets(0, 0);
                this.FM.AS.setJamBullets(0, 2);
                this.FM.AS.setJamBullets(0, 4);
                this.FM.AS.setJamBullets(0, 6);
            }
            if(s.startsWith("xxammor4") && shot.power > 27000F && World.Rnd().nextFloat() < 0.1F)
            {
                this.FM.AS.setJamBullets(0, 1);
                this.FM.AS.setJamBullets(0, 3);
                this.FM.AS.setJamBullets(0, 5);
                this.FM.AS.setJamBullets(0, 7);
            }
            if(s.startsWith("xxammol2") && shot.power > 27000F && World.Rnd().nextFloat() < 0.1F)
            {
                this.FM.AS.setJamBullets(0, 8);
                this.FM.AS.setJamBullets(0, 10);
            }
            if(s.startsWith("xxammor2") && shot.power > 27000F && World.Rnd().nextFloat() < 0.1F)
            {
                this.FM.AS.setJamBullets(0, 9);
                this.FM.AS.setJamBullets(0, 11);
            }
            if(s.startsWith("xxhispa1"))
                this.FM.AS.setJamBullets(0, 0);
        } else
        if(s.startsWith("xCF") || s.startsWith("xOil"))
        {
            hitChunk("CF", shot);
            if(point3d.x > -2.2D)
            {
                if(World.Rnd().nextFloat() < 0.1F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8 | 0x20);
                if(Aircraft.v1.x < -0.8D && World.Rnd().nextFloat() < 0.2F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                if(Aircraft.v1.x < -0.9D && World.Rnd().nextFloat() < 0.2F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                if(Math.abs(Aircraft.v1.x) < 0.8D)
                    if(point3d.y > 0.0D)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                        if(World.Rnd().nextFloat() < 0.1F)
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                    } else
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8 | 2);
                        if(World.Rnd().nextFloat() < 0.1F)
                            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10 | 4);
                    }
            }
        } else
        if(s.startsWith("xEng"))
        {
            if(chunkDamageVisible("Engine1") < 3)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xTail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xKeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xRudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xStab"))
        {
            if(s.startsWith("xStabL") && chunkDamageVisible("StabL") < 3)
                hitChunk("StabL", shot);
            if(s.startsWith("xStabR") && chunkDamageVisible("StabR") < 3)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xVator"))
        {
            if(s.startsWith("xVatorL"))
                hitChunk("VatorL", shot);
            if(s.startsWith("xVatorR"))
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xWing"))
        {
            if(s.startsWith("xWingLIn") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xWingRIn") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xWingLMid"))
            {
                if(chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if(World.Rnd().nextFloat() < shot.mass + 0.02F)
                    this.FM.AS.hitOil(shot.initiator, 0);
            }
            if(s.startsWith("xWingRMid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xWingLOut") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xWingROut") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xArone"))
        {
            if(s.startsWith("xAroneL"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xAroneR"))
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
        case 19:
            this.FM.Gears.hitCentreGear();
            hierMesh().chunkVisible("Wire_D0", false);
            break;

        case 10:
            doWreck("GearR3_D0");
            this.FM.Gears.hitRightGear();
            break;

        case 9:
            doWreck("GearL3_D0");
            this.FM.Gears.hitLeftGear();
            break;

        case 11:
            hierMesh().chunkVisible("Wire_D0", false);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void doWreck(String s)
    {
        if(hierMesh().chunkFindCheck(s) != -1)
        {
            hierMesh().hideSubTrees(s);
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind(s));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    private float kangle;
    private static final float gear5YScale[] = {
        0.0F, 0.35F, 0.75F, 0.95F, 1.0F
    };
    private static final float gear5XScale[] = {
        0.0F, 0.9F, 1.25F, 1.24F, 1.0F
    };
    private static final float gear4Scale[] = {
        -0.2F, -0.12F, 0.12F, 0.55F, 1.0F
    };
    private static final float gear7YScale[] = {
        -0.02F, 0.23F, 0.55F, 0.83F, 1.0F
    };
    private static final float gear7XScale[] = {
        0.0F, 0.15F, 0.3F, 0.64F, 1.0F
    };
    private static final float gear6Scale[] = {
        0.0F, 0.25F, 0.52F, 0.67F, 1.0F
    };
    private float suspension;
    protected float emergencyPressure;
    private float flapps;

    static 
    {
        Property.set(IK_3xyz.class, "originCountry", PaintScheme.countryBritain);
    }
}
