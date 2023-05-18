
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import java.io.IOException;


public class A4TX extends Scheme1
    implements TypeFighter, TypeStormovik, TypeGSuit, TypeSupersonic, TypeFastJet
{

    public A4TX()
    {
        arrestor = 0.0F;
        critSpeed = 0.0F;
        lTimeNextEject = 0L;
        overrideBailout = false;
        ejectComplete = false;
        lTimeNextEject = 0L;
        SonicBoom = 0.0F;
        oldthrl = -1F;
        curthrl = -1F;
        APmode1 = false;
        APmode2 = false;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
    }

    public float getAirPressure(float f)
    {
        float f1 = 1.0F - (0.0065F * f) / 288.15F;
        float f2 = 5.255781F;
        return 101325F * (float)Math.pow(f1, f2);
    }

    public float getAirPressureFactor(float f)
    {
        return getAirPressure(f) / 101325F;
    }

    public float getAirDensity(float f)
    {
        return (getAirPressure(f) * 0.0289644F) / (8.31447F * (288.15F - 0.0065F * f));
    }

    public float getAirDensityFactor(float f)
    {
        return getAirDensity(f) / 1.225F;
    }

    public float getMachForAlt(float f)
    {
        f /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length - 1; i++)
            if(TypeSupersonic.fMachAltX[i] > f)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float getMpsFromKmh(float f)
    {
        return f / 3.6F;
    }

    public float calculateMach()
    {
        return FM.getSpeedKMH() / getMachForAlt(FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(FM.getAltitude()) - FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f1 = FM.getSpeedKMH() - getMachForAlt(FM.getAltitude());
        if(f1 < 0.5F)
            f1 = 0.5F;
        if((double)calculateMach() <= 1.0D)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if((double)calculateMach() >= 1.0D)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if(FM.VmaxAllowed > 1500F)
            FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log("Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if((double)calculateMach() > 1.01D || (double)calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(FM.AS.isMaster())
            if(curthrl == -1F)
            {
                curthrl = oldthrl = FM.EI.engines[0].getControlThrottle();
            } else
            {
                curthrl = FM.EI.engines[0].getControlThrottle();
                if(curthrl < 1.05F)
                {
                    if((curthrl - oldthrl) / f > 20F && FM.EI.engines[0].getRPM() < 3200F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                    {
                        if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                            HUD.log("Compressor Stall!");
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.01D * (double)(FM.EI.engines[0].getRPM() / 1000F);
                        FM.EI.engines[0].doSetReadyness(FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            FM.AS.hitEngine(this, 0, 100);
                        if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            FM.EI.engines[0].setEngineDies(this);
                    }
                    if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[0].getRPM() < 3200F && FM.EI.engines[0].getStage() == 6)
                    {
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.001D * (double)(FM.EI.engines[0].getRPM() / 1000F);
                        FM.EI.engines[0].doSetReadyness(FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                        {
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log("Engine Flameout!");
                            FM.EI.engines[0].setEngineStops(this);
                        } else
                        if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                            HUD.log("Compressor Stall!");
                    }
                }
                oldthrl = curthrl;
            }
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(1.5F, 1.5F, 1.0F, 2.0F, 2.0F, 2.0F);
    }

    public void missionStarting()
    {
        super.missionStarting();
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -117F);
        float f2 = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
        float f3 = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, -90F, 0.0F) : Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
        float f4 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -105F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -f4, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f2, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 55F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 55F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -55F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -55F * f, 0.0F);
    }

    protected void moveFan(float f)
    {
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 35F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -70F * f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(13.350000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.77F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch(i)
                {
                case 1: // '\001'
                case 2: // '\002'
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[j] == 0)
                    {
                        debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, j, 1);
                        FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        FM.AS.hitTank(shot.initiator, j, 2);
                        debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
            } else
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            } else
            if(s.startsWith("xxhyd"))
                FM.AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                FM.AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xcf"))
                hitChunk("CF", shot);
            else
            if(s.startsWith("xnose"))
                hitChunk("Nose", shot);
            else
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
            if(s.startsWith("xstab"))
            {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
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
                if(s.startsWith("xaronel"))
                    hitChunk("AroneL", shot);
                if(s.startsWith("xaroner"))
                    hitChunk("AroneR", shot);
            } else
            if(s.startsWith("xgear"))
            {
                if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    FM.AS.setInternalDamage(shot.initiator, 3);
                }
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
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            FM.EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 90F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 90F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
        hierMesh().chunkSetAngles("FuBrake01_D0", -45F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("FuBrake02_D0", 45F * f, 0.0F, 0.0F);
    }

    private void bailout()
    {
        if(overrideBailout)
            if(FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2)
            {
                if(FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.5F)
                    FM.AS.astateBailoutStep = 11;
                else
                    FM.AS.astateBailoutStep = 2;
            } else
            if(FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3)
            {
                switch(FM.AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(FM.CT.cockpitDoorControl < 0.5F)
                        doRemoveBlisters();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 1000L;
                    break;
                }
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if(FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            } else
            if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                byte byte0 = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                {
                    World.cur();
                    if(FM.AS.actor != World.getPlayerAircraft())
                        ((Maneuver)super.FM).set_maneuver(44);
                }
                if(FM.AS.astatePilotStates[byte0 - 11] < 99)
                {
                    if(byte0 == 11)
                    {
                        doRemoveBodyFromPlane(2);
                        lTimeNextEject = Time.current() + 1000L;
                    } else
                    if(byte0 == 12)
                    {
                        doRemoveBodyFromPlane(1);
                        doEjectCatapultInstructor();
                        FM.AS.astateBailoutStep = 51;
                        FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    FM.AS.astatePilotStates[byte0 - 11] = 99;
                } else
                {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + FM.AS.astatePilotStates[byte0 - 11]);
                }
            }
    }

    public void doEjectCatapultStudent()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat02");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat2_D0", false);
    }

    public void doEjectCatapultInstructor()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
    }

    private final void doRemoveBlisters()
    {
        for(int i = 1; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && FM.AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(FM.Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    public void update(float f)
    {
        computeCy();
        computeEngine();
        if(FM.CT.getArrestor() > 0.2F)
            calculateArrestor();
        super.update(f);
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
        if((double)calculateMach() < 0.78000000000000003D)
            critSpeed = 0.0F;
        if(FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.96999999999999997D)
            FM.Sq.dragParasiteCx += 0.001F;
        if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        soundbarier();
    }

    public void computeCy()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float f = calculateMach();
        if(calculateMach() < 0.0F);
        float f1 = 0.0F;
        if((double)f > 1.0D)
        {
            f1 = 0.6F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            f1 = ((9500F * f3 - 21285F * f2) + 8833F * f + 4752F) / 3600F;
        }
        polares.CyCritH_0 = f1;
    }

    public void computeEngine()
    {
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5)
            if(calculateMach() < 0.0F);
        if((double)f > 13.5D)
        {
            f1 = 11F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            f1 = ((134F * f3 - 2381F * f2) + 14946F * f) / 22680F;
        }
        FM.producedAF.x -= f1 * 1000F;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                FM.AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                FM.AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                FM.AP.setStabDirection(true);
                FM.CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                FM.AP.setStabDirection(false);
                FM.CT.bHasRudderControl = true;
            }
    }

    private void calculateArrestor()
    {
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f1;
            moveArrestorHook(arrestor);
        } else
        {
            float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
            if(f2 < 0.0F && super.FM.getSpeedKMH() > 60F)
                Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                f2 = 0.0F;
            if(f2 > 0.2F)
                f2 = 0.2F;
            if(f2 > 0.0F)
                arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
            else
                arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
            if(arrestor < 0.0F)
                arrestor = 0.0F;
            else
            if(arrestor > 1.0F)
                arrestor = 1.0F;
            moveArrestorHook(arrestor);
        }
    }

    private float arrestor;
    private float oldthrl;
    private float curthrl;
    private float engineSurgeDamage;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    private float critSpeed;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    public boolean APmode1;
    public boolean APmode2;
    public static boolean bChangedPit = false;

    static 
    {
        Class class1 = CLASS.THIS();
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}