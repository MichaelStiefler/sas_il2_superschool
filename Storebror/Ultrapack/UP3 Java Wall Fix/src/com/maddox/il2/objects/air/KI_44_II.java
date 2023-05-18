package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class KI_44_II extends Scheme1
    implements TypeFighter
{

    public KI_44_II()
    {
    }

    protected void moveFan(float f)
    {
        super.moveFan(f);
        hierMesh().chunkSetAngles(Aircraft.Props[0][0], 0.0F, propPos[0], 0.0F);
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

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.59F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.05F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean bool)
    {
        super.rareAction(f, bool);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        if(f < 0.01D)
            f = 0.0F;
        float f_0_ = Math.max(-f * 1500F, -40F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -55F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f_0_, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f_0_, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 91.807F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 135F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.06F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 4.42F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -91.807F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -135F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.06F, 0.0F, 85F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -4.42F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.24515F, 0.0F, 0.24515F);
        Aircraft.xyz[1] = f;
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.24515F, 0.0F, 0.24515F);
        Aircraft.xyz[1] = f;
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC5_D0", 0.0F, f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.4F * f;
        Aircraft.ypr[0] = -15F * f;
        Aircraft.ypr[1] = 16F * f;
        Aircraft.ypr[2] = -1.8F * f;
        hierMesh().chunkSetLocate("Flap01_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = 0.4F * f;
        Aircraft.ypr[0] = 15F * f;
        Aircraft.ypr[1] = -16F * f;
        Aircraft.ypr[2] = -1.8F * f;
        hierMesh().chunkSetLocate("Flap02_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected boolean cutFM(int i, int i_1_, Actor actor)
    {
        switch(i)
        {
        case 11:
        case 17:
        case 18:
            hierMesh().chunkVisible("Wire_D0", false);
            break;

        case 9:
            hierMesh().chunkVisible("GearL6_D0", false);
            hierMesh().chunkVisible("GearL4_D0", false);
            break;

        case 10:
            hierMesh().chunkVisible("GearR6_D0", false);
            hierMesh().chunkVisible("GearR4_D0", false);
            break;
        }
        return super.cutFM(i, i_1_, actor);
    }

    protected void hitBone(String string, Shot shot, Point3d point3d)
    {
        if(string.startsWith("xx"))
        {
            if(string.startsWith("xxarmor"))
            {
                if(string.endsWith("p1") || string.endsWith("p2"))
                    getEnergyPastArmor(13.13D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(string.endsWith("g1"))
                {
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(getEnergyPastArmor(World.Rnd().nextFloat(32.5F, 65F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) < 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(string.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = string.charAt(10) - 48;
                switch(i)
                {
                case 1:
                case 2:
                    if(getEnergyPastArmor(0.99F, shot) > 0.0F && World.Rnd().nextFloat() < 0.175F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3:
                    if(getEnergyPastArmor(0.99F, shot) > 0.0F && World.Rnd().nextFloat() < 0.675F)
                    {
                        if(World.Rnd().nextFloat() < 0.25F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        if(World.Rnd().nextFloat() < 0.25F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        if(World.Rnd().nextFloat() < 0.25F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                    }
                    break;

                case 4:
                    if(getEnergyPastArmor(0.22F, shot) > 0.0F && World.Rnd().nextFloat() < 0.02F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;

                case 5:
                    if(getEnergyPastArmor(4.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.11F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    break;
                }
            } else
            if(string.startsWith("xxeng1"))
            {
                if(string.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 85000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(string.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(string.endsWith("mag1"))
                {
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Magneto #0 Destroyed..");
                    getEnergyPastArmor(25F, shot);
                }
                if(string.endsWith("mag2"))
                {
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Engine Module: Magneto #1 Destroyed..");
                    getEnergyPastArmor(25F, shot);
                }
                if(string.endsWith("prop") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
            } else
            if(string.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(string.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(string.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(string.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(string.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(string.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                if(string.startsWith("xxlockf"))
                    getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 3F), shot);
            } else
            if(string.startsWith("xxmgun0"))
            {
                int i = string.charAt(7) - 49;
                if(getEnergyPastArmor(0.75F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + i + ") Disabled..");
                    FM.AS.setJamBullets(0, i);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(string.startsWith("xxcannon0"))
            {
                int i = string.charAt(9) - 49;
                if(getEnergyPastArmor(6.29F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Cannon (" + i + ") Disabled..");
                    FM.AS.setJamBullets(1, i);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(string.startsWith("xxammo"))
            {
                if(string.startsWith("xxammo01") && World.Rnd().nextFloat() < 0.01F)
                {
                    debuggunnery("Armament: Machine Gun (0) Chain Broken..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(string.startsWith("xxammo02") && World.Rnd().nextFloat() < 0.01F)
                {
                    debuggunnery("Armament: Machine Gun (1) Chain Broken..");
                    FM.AS.setJamBullets(0, 1);
                }
                if(string.startsWith("xxammowl"))
                {
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        debuggunnery("Armament: Cannon Gun (0) Chain Broken..");
                        FM.AS.setJamBullets(1, 0);
                    }
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        debuggunnery("Armament: Cannon Gun (0) Payload Detonates..");
                        FM.AS.hitTank(shot.initiator, 0, 99);
                        nextDMGLevels(3, 2, "WingLIn_D" + chunkDamageVisible("WingLIn"), shot.initiator);
                    }
                }
                if(string.startsWith("xxammowr"))
                {
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        debuggunnery("Armament: Cannon Gun (1) Chain Broken..");
                        FM.AS.setJamBullets(1, 1);
                    }
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        debuggunnery("Armament: Cannon Gun (1) Payload Detonates..");
                        FM.AS.hitTank(shot.initiator, 1, 99);
                        nextDMGLevels(3, 2, "WingRIn_D" + chunkDamageVisible("WingRIn"), shot.initiator);
                    }
                }
                getEnergyPastArmor(16F, shot);
            } else
            if(string.startsWith("xxoiltank"))
            {
                if(getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    getEnergyPastArmor(0.22F, shot);
                    debuggunnery("Engine Module: Oil Tank Pierced..");
                }
            } else
            if(string.startsWith("xxtank"))
            {
                int i = string.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.45F)
                {
                    if(FM.AS.astateTankStates[i] == 0)
                    {
                        debuggunnery("Fuel Tank (" + i + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, i, 1);
                        FM.AS.doSetTankState(shot.initiator, i, 1);
                    }
                    if(World.Rnd().nextFloat() < 0.01F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.4F)
                    {
                        FM.AS.hitTank(shot.initiator, i, 4);
                        debuggunnery("Fuel Tank (" + i + "): Hit..");
                    }
                }
            } else
            if(string.startsWith("xxspar"))
            {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if((string.endsWith("li1") || string.endsWith("li2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((string.endsWith("ri1") || string.endsWith("ri2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((string.endsWith("lm1") || string.endsWith("lm2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((string.endsWith("rm1") || string.endsWith("rm2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((string.endsWith("lo1") || string.endsWith("lo2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((string.endsWith("ro1") || string.endsWith("ro2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(string.startsWith("xxsparsl") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabL Spar Damaged..");
                    nextDMGLevels(1, 2, "StabL_D" + chunkDamageVisible("StabL"), shot.initiator);
                }
                if(string.startsWith("xxsparsr") && World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabR Spar Damaged..");
                    nextDMGLevels(1, 2, "StabR_D" + chunkDamageVisible("StabR"), shot.initiator);
                }
                if(string.startsWith("xxspark") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Keel Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D" + chunkDamageVisible("Keel1"), shot.initiator);
                }
                if(string.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            } else
            if(string.startsWith("xxradio"))
                getEnergyPastArmor(16F, shot);
            else
            if(string.startsWith("xxoxy"))
                getEnergyPastArmor(16F, shot);
        } else
        if(string.startsWith("xcf") || string.startsWith("xblister"))
        {
            hitChunk("CF", shot);
            if(string.startsWith("xblister"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(point3d.x > -0.605D && point3d.x < -0.295D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(point3d.x > -1.7050000000000001D && point3d.x < -0.492D && point3d.z > 0.082000000000000003D && World.Rnd().nextFloat() < 0.5F)
                if(World.Rnd().nextFloat() < 0.25F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                else
                if(World.Rnd().nextFloat() < 0.33F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                else
                if(World.Rnd().nextFloat() < 0.5F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                else
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
        } else
        if(string.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(string.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(string.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(string.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(string.startsWith("xstab"))
        {
            if(string.startsWith("xstabl"))
                hitChunk("StabL", shot);
            if(string.startsWith("xstabr"))
                hitChunk("StabR", shot);
        } else
        if(string.startsWith("xvator"))
        {
            if(string.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(string.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(string.startsWith("xwing"))
        {
            if(string.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(string.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(string.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(string.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(string.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(string.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(string.startsWith("xarone"))
        {
            if(string.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            if(string.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
        } else
        if(string.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(string.startsWith("xpilot") || string.startsWith("xhead"))
        {
            int i = 0;
            int i_2_;
            if(string.endsWith("a"))
            {
                i = 1;
                i_2_ = string.charAt(6) - 49;
            } else
            if(string.endsWith("b"))
            {
                i = 2;
                i_2_ = string.charAt(6) - 49;
            } else
            {
                i_2_ = string.charAt(5) - 49;
            }
            hitFlesh(i_2_, shot, i);
        }
    }

    static 
    {
        Class var_class = KI_44_II.class;
        Property.set(var_class, "originCountry", PaintScheme.countryJapan);
    }
}
