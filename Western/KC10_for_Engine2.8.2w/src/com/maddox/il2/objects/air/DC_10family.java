
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.SoundFX;
import java.io.IOException;
import java.lang.Math;
import java.lang.reflect.Field;
import java.util.*;


public abstract class DC_10family extends Scheme6
    implements TypeTransport, TypeSupersonic, TypeFastJet, TypeFuelDump
{

    public DC_10family()
    {
        lLightHook = new Hook[4];
        lightTime = 0.0F;
        SonicBoom = 0.0F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        antiColLight = new Eff3DActor[6];
        flashLight = new Eff3DActor[2];
        oldAntiColLight = false;
        isHydraulicAlive = false;
        isGeneratorAlive = false;
        lastUpdateTime = -1L;
        lastThrustReverser = false;
        bHasCenterGear = true;
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
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
        for(i = 0; i < TypeSupersonic.fMachAltX.length; i++)
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
        if(calculateMach() <= 1.0F)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if(calculateMach() >= 1.0F)
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
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if(calculateMach() > 1.01F || calculateMach() < 1.0F)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(FM.AS.isMaster())
        {
            for(int i = 0; i < 3; i++)
                if(curthrl[i] == -1.0F)
                {
                    curthrl[i] = oldthrl[i] = FM.EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl[i] = FM.EI.engines[i].getControlThrottle();
                    if(curthrl[i] < 1.05F)
                    {
                        if((curthrl[i] - oldthrl[i]) / f > 20F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage[i] += 0.01F * FM.EI.engines[i].getRPM() / 1000F;
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage[i]);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl[i] - oldthrl[i]) / f < -20F && (curthrl[i] - oldthrl[i]) / f > -100F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage[i] += 0.001F * FM.EI.engines[i].getRPM() / 1000F;
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage[i]);
                            if(World.Rnd().nextFloat() < 0.4F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                            {
                                if(FM.actor == World.getPlayerAircraft())
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                FM.EI.engines[i].setEngineStops(this);
                            } else
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        }
                    }
                    oldthrl[i] = curthrl[i];
                }

        }
    }

    private final void umn()
    {
        Vector3d vector3d = FM.getVflow();
        float f = (float)Math.sqrt(vector3d.lengthSquared());
        mn = f / Atmosphere.sonicSpeed((float)FM.Loc.z);
        if(mn >= lteb && mn < 1.1F)
            ts = true;
        else
            ts = false;
    }

    public boolean ist()
    {
        return ts;
    }

    public float gmnr()
    {
        return mn;
    }

    public boolean inr()
    {
        return ictl;
    }

    public void computeThrust()
    {
        if(FM.EI.engines[0].getThrustOutput() > 0.95F && calculateMach() < 0.32F && FM.EI.engines[0].getStage() > 5)
            FM.producedAF.x += 1000D;  // for fake FM, 1/4 of historical
        if(FM.EI.engines[1].getThrustOutput() > 0.95F && calculateMach() < 0.32F && FM.EI.engines[1].getStage() > 5)
            FM.producedAF.x += 1000D;  // for fake FM, 1/4 of historical
        if(FM.EI.engines[2].getThrustOutput() > 0.95F && calculateMach() < 0.32F && FM.EI.engines[2].getStage() > 5)
            FM.producedAF.x += 1000D;  // for fake FM, 1/4 of historical
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() == 6 && FM.EI.engines[1].getThrustOutput() < 1.001F && FM.EI.engines[1].getStage() == 6 && FM.EI.engines[2].getThrustOutput() < 1.001F && FM.EI.engines[2].getStage() == 6)
            if(f > 13.5F)
            {
                f1 = 11F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = ((358F * f3 - 7309F * f2) + 41826F * f) / 7560F;
            }
        FM.producedAF.x -= f1 * 300F;  // for fake FM, 1/4 of historical
    }

    public void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float f = calculateMach();
        if(calculateMach() < 0.0F);
        float f1 = 0.0F;
        if(f > 2.25F)
        {
            f1 = 0.12F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            float f5 = f4 * f;
            float f6 = f5 * f;
            float f7 = f6 * f;
            float f8 = f7 * f;
            float f9 = f8 * f;
            f1 = ((((((-0.0998429F * f8 + 1.02985F * f7) - 4.34775F * f6) + 9.5949F * f5) - 11.7055F * f4) + 7.70811F * f3) - 2.5701F * f2) + 0.398187F * f + 0.078F;
        }
        polares.lineCyCoeff = f1;
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        stockAilerons = FM.Sq.squareAilerons;
        stockThrustMax = FM.EI.engines[0].thrustMax;
        FM.CT.bHasAntiColLights = true;
        FM.CT.bHasDragChuteControl = false;
        FM.Sq.dragChuteCx = 0.0F;  // No Drag effect for Drag Chute , but used for Jet engines' thrust reversal.
    }

    public void missionStarting()
    {
        super.missionStarting();

        for(int i = 0; i < 3; i++)
        {
            oldthrl[i] = -1.0F;
            curthrl[i] = -1.0F;
            engineSurgeDamage[i] = 0.0F;
        }
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.EI.engines[1].getStage() < 6
           && FM.EI.engines[2].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            isGeneratorAlive = false;
            isHydraulicAlive = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasFlapsControl = false;
            FM.CT.bHasAirBrakeControl = false;
            FM.CT.AirBrakeControl = 0.0F;
        } else
        if(!isHydraulicAlive)
        {
            isGeneratorAlive = true;
            isHydraulicAlive = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasRudderControl = true;
            FM.CT.bHasFlapsControl = true;
            FM.CT.bHasAirBrakeControl = true;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            if(!(FM instanceof RealFlightModel))
                umn();
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
        {
            if(FM.AP.way.isLanding() && FM.getSpeed() > FM.Vmin && FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)
            {
                if(FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            }
            else if(((Maneuver) FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.getSpeed() < FM.VminFLAPS * 1.16F)
            {
                if(FM.getSpeed() > FM.VminFLAPS * 0.5F && FM.Gears.onGround())
                {
                    if(FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                }
                else if(FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            }
            else if(((Maneuver) FM).get_maneuver() == 66)
            {
                if(FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            }
            else if(((Maneuver) FM).get_maneuver() == 7)
            {
                if(FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            }
            else if(isHydraulicAlive && FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;

            if(((Maneuver) FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.Gears.nOfGearsOnGr == 3 && FM.getSpeedKMH() > 100F && FM.CT.bHasDragChuteControl)
                FM.CT.DragChuteControl = 1.0F;
            else
                FM.CT.DragChuteControl = 0.0F;
        }
        if(FM.CT.DragChuteControl == 0.0F && (FM.EI.engines[0].getThrustOutput() > 0.91F || FM.EI.engines[1].getThrustOutput() > 0.91F || FM.EI.engines[2].getThrustOutput() > 0.91F))
            FM.CT.AirBrakeControl = 0.0F;

        if(!FM.isPlayers())
            FM.CT.bAntiColLights = FM.AS.bNavLightsOn;
        anticollight();
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        default:
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        if(i > 3)
            return;
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
        if(i == 0)
            hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
    }

    public void update(float f)
    {
        if(lastUpdateTime != Time.current())
        {
            if(FM.getSpeedKMH() > 700F && FM.CT.bHasFlapsControl)
            {
                FM.CT.FlapsControl = 0.0F;
                FM.CT.bHasFlapsControl = false;
            } else
            {
                FM.CT.bHasFlapsControl = true;
            }
            if(FM.AS.isMaster() && Config.isUSE_RENDER())
            {
                for(int i = 0; i < 3; i++)
                {
                    if(FM.EI.engines[i].getThrustOutput() > 0.40F && FM.EI.engines[i].getStage() == 6)
                    {
                        if(FM.EI.engines[i].getThrustOutput() > 0.97F)
                            FM.AS.setSootState(this, i, 5);
                        else
                        if(FM.EI.engines[i].getThrustOutput() > 0.93F)
                            FM.AS.setSootState(this, i, 4);
                        else
                        if(FM.EI.engines[i].getThrustOutput() > 0.87F)
                            FM.AS.setSootState(this, i, 3);
                        else
                        if(FM.EI.engines[i].getThrustOutput() > 0.65F)
                            FM.AS.setSootState(this, i, 2);
                        else
                            FM.AS.setSootState(this, i, 1);
                    } else
                    {
                        FM.AS.setSootState(this, i, 0);
                    }
                }
                if(FM instanceof RealFlightModel)
                    umn();
            }
            checkHydraulicStatus();
            soundbarier();
        }
        engineSurge(f);
        super.update(f);
        rotWheels(f);

        if(lastUpdateTime != Time.current())
        {
            if(FM.Gears.onGround() && isGeneratorAlive && isHydraulicAlive)
                FM.CT.bHasDragChuteControl = true;
            else
            {
                if(FM.CT.DragChuteControl == 1.0F)
                    FM.CT.DragChuteControl = 0.0F;
                else if(FM.CT.getDragChute() == 0.0F)
                    FM.CT.bHasDragChuteControl = false;
            }
            if(FM.CT.bHasDragChuteControl && (FM.CT.getDragChute() == 0.0F || FM.CT.getDragChute() == 1.0F)
               && FM.EI.engines[0].getControlThrottle() > 0.04F && FM.EI.engines[1].getControlThrottle() > 0.04F && FM.EI.engines[2].getControlThrottle() > 0.04F)
                FM.CT.bHasDragChuteControl = false;
            if(FM.CT.bHasDragChuteControl)
                moveThrustReverser(FM.CT.getDragChute());
            if(FM.CT.getDragChute() > 0.99F && !lastThrustReverser)
            {
                Vector3f tmpvec3f = new Vector3f();
                tmpvec3f.x = -1.0F;
                tmpvec3f.y = 0.0F;
                tmpvec3f.z = 0.0F;
                for(int i = 0; i < 3; i++)
                {
                    FM.EI.engines[i].setVector(tmpvec3f);
                    FM.EI.engines[i].thrustMax = stockThrustMax * 0.4F;
                }
                lastThrustReverser = true;
            }
            if(lastThrustReverser && FM.CT.getDragChute() < 0.90F)
            {
                Vector3f tmpvec3f = new Vector3f();
                tmpvec3f.x = 1.0F;
                tmpvec3f.y = 0.0F;
                tmpvec3f.z = 0.0F;
                for(int i = 0; i < 3; i++)
                {
                    FM.EI.engines[i].setVector(tmpvec3f);
                    FM.EI.engines[i].thrustMax = stockThrustMax;
                }
                lastThrustReverser = false;
            }

            if(Config.isUSE_RENDER())
                if(this == World.getPlayerAircraft() && Main3D.cur3D().viewActor() == World.getPlayerAircraft() && !Main3D.cur3D().isViewOutside())
                    hierMesh().chunkVisible("WindSheild", false);
                else
                    hierMesh().chunkVisible("WindSheild", true);
        }
        lastUpdateTime = Time.current();
    }

    public void moveCockpitDoor(float f)
    {
        if(FM.CT.bMoveSideDoor)
        {
            hierMesh().chunkSetAngles("Door_Cargo", 0.0F, 0.0F, -90F * f);
        }
        else
        {
            resetYPRmodifier();
            if(f < 0.05F)
            {
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -0.1882F);
            } else
            {
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.06F, 0.99F, -0.1882F, -1.9761F);
                Aircraft.xyz[2] = Aircraft.cvt(f, 0.06F, 0.99F, 0.0F, 0.915593F);
                Aircraft.ypr[2] = Aircraft.cvt(f, 0.06F, 0.99F, 0.0F, -44.3F);
            }
            hierMesh().chunkSetLocate("DoorNL", Aircraft.xyz, Aircraft.ypr);
        }
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float fL, float fR, float fC)
    {
        hiermesh.chunkSetAngles("GearC_CoverL2", 0.0F, 0.0F, Aircraft.cvt(fC, 0.01F, 0.08F, 0.0F, -85F));
        hiermesh.chunkSetAngles("GearC_CoverR2", 0.0F, 0.0F, Aircraft.cvt(fC, 0.01F, 0.08F, 0.0F, 85F));
        if(fC < 0.10F)
        {
            hiermesh.chunkSetAngles("GearC_CoverL1", 0.0F, 0.0F, Aircraft.cvt(fC, 0.01F, 0.08F, 0.0F, -85F));
            hiermesh.chunkSetAngles("GearC_CoverR1", 0.0F, 0.0F, Aircraft.cvt(fC, 0.01F, 0.08F, 0.0F, 85F));
        }
        if(fC > 0.90F)
        {
            hiermesh.chunkSetAngles("GearC_CoverL1", 0.0F, 0.0F, Aircraft.cvt(fC, 0.91F, 0.98F, -85F, 0.0F));
            hiermesh.chunkSetAngles("GearC_CoverR1", 0.0F, 0.0F, Aircraft.cvt(fC, 0.91F, 0.98F, 85F, 0.0F));
        }

        if(fL < 0.10F)
            hiermesh.chunkSetAngles("GearL_CoverC", 0.0F, 0.0F, Aircraft.cvt(fL, 0.02F, 0.09F, 0.0F, 88F));
        if(fL > 0.90F)
            hiermesh.chunkSetAngles("GearL_CoverC", 0.0F, 0.0F, Aircraft.cvt(fL, 0.92F, 0.99F, 88F, 0.0F));
        hiermesh.chunkSetAngles("GearL_CoverW", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, -90F));

        if(fR < 0.10F)
            hiermesh.chunkSetAngles("GearR_CoverC", 0.0F, 0.0F, Aircraft.cvt(fR, 0.02F, 0.09F, 0.0F, -88F));
        if(fR > 0.90F)
            hiermesh.chunkSetAngles("GearR_CoverC", 0.0F, 0.0F, Aircraft.cvt(fR, 0.92F, 0.99F, -88F, 0.0F));
        hiermesh.chunkSetAngles("GearR_CoverW", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 90F));

        float fT = fL * 0.6F + fR * 0.4F;
        if(fT < 0.10F)
        {
            hiermesh.chunkSetAngles("GearT_CoverLF", 0.0F, 0.0F, Aircraft.cvt(fT, 0.02F, 0.09F, 0.0F, -88F));
            hiermesh.chunkSetAngles("GearT_CoverRF", 0.0F, 0.0F, Aircraft.cvt(fT, 0.02F, 0.09F, 0.0F, 88F));
        }
        if(fT > 0.90F)
        {
            hiermesh.chunkSetAngles("GearT_CoverLF", 0.0F, 0.0F, Aircraft.cvt(fT, 0.92F, 0.99F, -88F, 0.0F));
            hiermesh.chunkSetAngles("GearT_CoverRF", 0.0F, 0.0F, Aircraft.cvt(fT, 0.92F, 0.99F, 88F, 0.0F));
        }
        hiermesh.chunkSetAngles("GearT_CoverLR", 0.0F, 0.0F, Aircraft.cvt(fT, 0.10F, 0.75F, 0.0F, -60F));
        hiermesh.chunkSetAngles("GearT_CoverRR", 0.0F, 0.0F, Aircraft.cvt(fT, 0.10F, 0.75F, 0.0F, 60F));
        hiermesh.chunkSetAngles("GearT3_D0", 0.0F, Aircraft.cvt(fT, 0.16F, 0.83F, 0.0F, -85F), 0.0F);

        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(fC, 0.15F, 0.85F, 0.0F, -92F), 0.0F);

        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, -90F));
        hiermesh.chunkSetAngles("GearL41_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, -5.0F));
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, 0.514681F);
        hiermesh.chunkSetLocate("GearL42_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL43_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, -46.306F));
        hiermesh.chunkSetAngles("GearL44_D0", 0.0F, 0.0F, Aircraft.cvt(fL, 0.14F, 0.86F, 0.0F, 164.923F));

        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 90F));
        hiermesh.chunkSetAngles("GearR41_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 5.0F));
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 0.514681F);
        hiermesh.chunkSetLocate("GearR42_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR43_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, 46.306F));
        hiermesh.chunkSetAngles("GearR44_D0", 0.0F, 0.0F, Aircraft.cvt(fR, 0.14F, 0.86F, 0.0F, -164.923F));

        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(fL, 0.80F, 0.98F, 0.0F, -0.455245F);
        hiermesh.chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(fR, 0.80F, 0.98F, 0.0F, -0.455245F);
        hiermesh.chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(fT, 0.80F, 0.98F, 0.0F, -0.255F);
        hiermesh.chunkSetLocate("GearT2_D0", Aircraft.xyz, Aircraft.ypr);

    /*  // sample of Torque link working codes ---
        resetXYZYPR();
        Aircraft.xyz[0] = Aircraft.cvt(f2, 0.15F, 0.83F, 0.21F, 0.26F);
        hiermesh.chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
        float deg = (float)Math.toDegrees(Math.acos((double)((0.742F - Aircraft.xyz[0]) / 0.742F)));
        hiermesh.chunkSetAngles("GearC71_D0", 0.0F, -deg, 0.0F);
        hiermesh.chunkSetAngles("GearC72_D0", 0.0F, deg * 2F, 0.0F);

        resetXYZYPR();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.4F, 0.8F, 0.0F, 0.51F);
        hiermesh.chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
        deg = (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F)));
        deg -= (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F) / 0.96F)));
        hiermesh.chunkSetAngles("GearL41_D0", 0.0F, 0.0F, -deg);
        hiermesh.chunkSetAngles("GearL42_D0", 0.0F, 0.0F, deg * 2F);
    */
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public void moveWheelSink()
    {
        if(FM.CT.getGearC() > 0.999F)
        {
            float fC = FM.Gears.gWheelSinking[2];
            resetYPRmodifier();
            Aircraft.xyz[2] = Aircraft.cvt(fC, 0.0F, 0.16F, 0.0F, 0.16F);
            hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
        }
        float fL = FM.Gears.gWheelSinking[0];
        float fR = FM.Gears.gWheelSinking[1];
        if(FM.CT.getGearL() > 0.999F)
        {
            resetYPRmodifier();
            Aircraft.xyz[2] = Aircraft.cvt(fL, 0.0F, 0.40F, 0.0F, 0.40F) - 0.455245F;
            hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if(FM.CT.getGearR() > 0.999F)
        {
            resetYPRmodifier();
            Aircraft.xyz[2] = Aircraft.cvt(fR, 0.0F, 0.40F, 0.0F, 0.40F) - 0.455245F;
            hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if(bHasCenterGear && FM.CT.getGearL() > 0.999F && FM.CT.getGearR() > 0.999F)
        {
            resetYPRmodifier();
            // The center gear is 8 degrees tilted, +3.52 percent longer value is needed.
            Aircraft.xyz[2] = Aircraft.cvt(fL * 0.5176F + fR * 0.5176F, 0.0F, 0.41F, 0.0F, 0.41F) - 0.255F;
            hierMesh().chunkSetLocate("GearT2_D0", Aircraft.xyz, Aircraft.ypr);
        }
    }

    private void rotWheels(float f)
    {
        hierMesh().chunkSetAngles("GearL1f_D0", 0.0F, -FM.Gears.gWheelAngles[0], 0.0F);
        hierMesh().chunkSetAngles("GearR1f_D0", 0.0F, -FM.Gears.gWheelAngles[1], 0.0F);
        if(bHasCenterGear)
            hierMesh().chunkSetAngles("GearT1_D0", 0.0F, -FM.Gears.gWheelAngles[0], 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 20F * f, 0.0F, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.GearControl > 0.5F && FM.Gears.onGround())
            hierMesh().chunkSetAngles("GearC3_D0", 1.0F * f, 0.0F, 0.0F);
        if(FM.CT.GearControl < 0.5F)
            hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -24F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -24F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("ArlonL_inner_D0", 0.0F, 45F * f, 0.0F);
        hierMesh().chunkSetAngles("ArlonR_inner_D0", 0.0F, -45F * f, 0.0F);

        hierMesh().chunkSetAngles("ArlonCoverLI_D0", 0.0F, 45.5F * f - 10.5F * Math.abs(f), 0.0F);
        hierMesh().chunkSetAngles("ArlonCoverRI_D0", 0.0F, -39.5F * f - 9.5F * Math.abs(f), 0.0F);

        if(FM.getSpeedKMH() < 600F)
        {
            hierMesh().chunkSetAngles("ArlonL_D0", 0.0F, 40F * f, 0.0F);
            hierMesh().chunkSetAngles("ArlonR_D0", 0.0F, -40F * f, 0.0F);
            FM.Sq.squareAilerons = stockAilerons * 1.3F;
        }
        else
        {
            hierMesh().chunkSetAngles("ArlonL_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("ArlonR_D0", 0.0F, 0.0F, 0.0F);
            FM.Sq.squareAilerons = stockAilerons;
        }
    }

    protected void moveFlap(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.1882F * f;
        Aircraft.xyz[2] = 0.13174F * f;
        Aircraft.ypr[1] = 50F * f;
        hierMesh().chunkSetLocate("Flap02u_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap03u_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.49873F * f;
        Aircraft.xyz[2] = (float)((Math.cos(Math.toRadians(50.0D * (double)f)) - 1.0D) * 0.2370887D);
        Aircraft.ypr[1] = 50F * f;
        hierMesh().chunkSetLocate("Flap02_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap03_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.05646F * f;
        Aircraft.ypr[1] = 18F * f;
        hierMesh().chunkSetLocate("Flap01u_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap04u_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.40463F * f;
        Aircraft.xyz[2] = (float)((Math.cos(Math.toRadians(50.0D * (double)f)) - 1.0D) * 0.1320519D);
        Aircraft.ypr[1] = 50F * f;
        hierMesh().chunkSetLocate("Flap01_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap04_D0", Aircraft.xyz, Aircraft.ypr);

        hierMesh().chunkSetAngles("PylonLIn2", 0.0F, 50F * f, 0.0F);
        hierMesh().chunkSetAngles("PylonRIn2", 0.0F, 50F * f, 0.0F);
        hierMesh().chunkSetAngles("PylonL2i", 0.0F, 50F * f, 0.0F);
        hierMesh().chunkSetAngles("PylonL2o", 0.0F, 50F * f, 0.0F);
        hierMesh().chunkSetAngles("PylonR2i", 0.0F, 50F * f, 0.0F);
        hierMesh().chunkSetAngles("PylonR2o", 0.0F, 50F * f, 0.0F);

        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.cos(Math.toRadians(35.5D))) * 0.51755F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(-Math.sin(Math.toRadians(35.5D))) * 0.51755F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -0.11292F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -18.0F);
        hierMesh().chunkSetLocate("SlatLI_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.cos(Math.toRadians(34.0D))) * 0.47991F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(-Math.sin(Math.toRadians(34.0D))) * 0.47991F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -0.05646F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -18.0F);
        hierMesh().chunkSetLocate("SlatLO_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.cos(Math.toRadians(35.5D))) * 0.51755F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.sin(Math.toRadians(35.5D))) * 0.51755F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -0.11292F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -18.0F);
        hierMesh().chunkSetLocate("SlatRI_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.cos(Math.toRadians(34.0D))) * 0.47991F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.sin(Math.toRadians(34.0D))) * 0.47991F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -0.05646F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -18.0F);
        hierMesh().chunkSetLocate("SlatRO_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("SpoilerLI_D0", 0.0F, -60F * f, 0.0F);
        hierMesh().chunkSetAngles("SpoilerLO_D0", 0.0F, -60F * f, 0.0F);
        hierMesh().chunkSetAngles("SpoilerRI_D0", 0.0F, -60F * f, 0.0F);
        hierMesh().chunkSetAngles("SpoilerRO_D0", 0.0F, -60F * f, 0.0F);
    }

    protected void moveThrustReverser(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.03F, 0.97F, 0.0F, -0.61165F);
        hierMesh().chunkSetLocate("Engine1_Cowling2_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Engine2_Cowling2_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Engine3_Cowling2_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkVisible("Engine1_BlockDoors", f > 0.96F);
        hierMesh().chunkVisible("Engine2_BlockDoors", f > 0.96F);
        hierMesh().chunkVisible("Engine3_BlockDoors", f > 0.96F);
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(FM.AS.astateSootEffects[i][k]);
            FM.AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 0.8F, "3DO/Effects/Aircraft/TurboZippoCF6.eff", -1F);
            break;

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.2F, "3DO/Effects/Aircraft/TurboZippoCF6.eff", -1F);
            break;

        case 4: // '\004'
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.8F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            // fall through

        case 3: // '\003'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/TurboZippoCF6.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 4.5F, "3DO/Effects/Aircraft/TurboJRD1100CF6.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.2F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;
        }
    }

    public void updateLLights()
    {
        super.pos.getRender(Actor._tmpLoc);
        if(lLight == null)
        {
            if(Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = new LightPointWorld[4];
                for(int i = 0; i < 4; i++)
                {
                    lLight[i] = new LightPointWorld();
                    lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    lLight[i].setEmit(0.0F, 0.0F);
                    try
                    {
                        lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    }
                    catch(Exception exception) { }
                }

            }
        } else
        {
            for(int j = 0; j < 4; j++)
            {
                if(FM.AS.astateLandingLightEffects[j] != null)
                {
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    Engine.land();
                    if(Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL))
                    {
                        lLightPL.z++;
                        lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                        lLight[j].setPos(lLightP2);
                        float f = (float)lLightP1.distance(lLightPL);
                        float f1 = f * 0.5F + 60F;
                        float f2 = 0.7F - (0.8F * f * lightTime) / 2000F;
                        lLight[j].setEmit(f2, f1);
                    } else
                    {
                        lLight[j].setEmit(0.0F, 0.0F);
                    }
                    continue;
                }
                if(lLight[j].getR() != 0.0F)
                    lLight[j].setEmit(0.0F, 0.0F);
            }

        }
    }

    private final void UpdateLightIntensity()
    {
        if(World.getTimeofDay() >= 6F && World.getTimeofDay() < 7F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        else
        if(World.getTimeofDay() >= 18F && World.getTimeofDay() < 19F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        else
        if(World.getTimeofDay() >= 7F && World.getTimeofDay() < 18F)
            lightTime = 0.1F;
        else
            lightTime = 1.0F;
    }

    private void anticollight()
    {
        if(FM.CT.bAntiColLights && isGeneratorAlive && !oldAntiColLight)
        {
            char postfix = (char)('A' + World.Rnd().nextInt(0, 5));
            for(int i = 0; i < 6; i++)
            {
                if(antiColLight[i] == null)
                {
                    try
                    {
                        antiColLight[i] = Eff3DActor.New(this, findHook("_AntiColLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRedFlash_" + String.valueOf(postfix) + ".eff", -1.0F, false);
                    } catch(Exception exception) { }
                }
            }
            for(int i = 0; i < 2; i++)
            {
                if(flashLight[i] == null)
                {
                    try
                    {
                        flashLight[i] = Eff3DActor.New(this, findHook("_FlashLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareWhiteFlash2_" + String.valueOf(postfix) + ".eff", -1.0F, false);
                    } catch(Exception exception) { }
                }
            }
            oldAntiColLight = true;
        }
        else if((!FM.CT.bAntiColLights || !isGeneratorAlive) && oldAntiColLight)
        {
            for(int i = 0; i < 6; i++)
                if(antiColLight[i] != null)
                {
                    Eff3DActor.finish(antiColLight[i]);
                    antiColLight[i] = null;
                }
            for(int i = 0; i < 2; i++)
                if(flashLight[i] != null)
                {
                    Eff3DActor.finish(flashLight[i]);
                    flashLight[i] = null;
                }
            oldAntiColLight = false;
        }
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)
            FM.AS.hitEngine(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)
            FM.AS.hitEngine(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("Engine3") && World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)
            FM.AS.hitEngine(shot.initiator, 2, 1);
        if(shot.chunkName.startsWith("Tail1") && Pd.z > 0.5D && Pd.x > -6D && Pd.x < -4.9499998092651367D && World.Rnd().nextFloat() < 0.5F)
            FM.AS.hitPilot(shot.initiator, 2, (int)(shot.mass * 1000F * 0.5F));
        if(shot.chunkName.startsWith("CF") && v1.x < -0.20000000298023224D && Pd.x > 2.5999999046325684D && Pd.z > 0.73500001430511475D && World.Rnd().nextFloat() < 0.178F)
            FM.AS.hitPilot(shot.initiator, Pd.y <= 0.0D ? 1 : 0, (int)(shot.mass * 900F));
        if(shot.chunkName.startsWith("WingLIn") && Math.abs(Pd.y) < 2.0999999046325684D)
            FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(0, (int)(shot.mass * 30F)));
        if(shot.chunkName.startsWith("WingRIn") && Math.abs(Pd.y) < 2.0999999046325684D)
            FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, (int)(shot.mass * 30F)));
        super.msgShot(shot);
    }

    private static void resetXYZYPR()
    {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    private long lastUpdateTime;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[];
    private static Loc lLightLoc1 = new Loc();
    private static Point3d lLightP1 = new Point3d();
    private static Point3d lLightP2 = new Point3d();
    private static Point3d lLightPL = new Point3d();
    private boolean ictl;
    private static float mteb = 1.0F;
    private float mn;
    private static float uteb = 1.25F;
    private static float lteb = 0.92F;
    private float actl;
    private float rctl;
    private float ectl;
    private boolean ts;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    private float engineSurgeDamage[] = { 0.0F, 0.0F , 0.0F };
    private float oldthrl[] = { -1.0F, -1.0F , -1.0F };
    private float curthrl[] = { -1.0F, -1.0F , -1.0F };
    private Eff3DActor antiColLight[];
    private Eff3DActor flashLight[];
    private boolean oldAntiColLight;
    public boolean isHydraulicAlive;
    public boolean isGeneratorAlive;
    private float stockAilerons;
    private float stockThrustMax;
    private boolean lastThrustReverser;
    protected boolean bHasCenterGear;
    public static float FlowRate = 10F;  // fake value, 1/4 of historical
    public static float FuelReserve = 1500F;  // fake value, 1/4 of historical

    static
    {
        Class class1 = com.maddox.il2.objects.air.DC_10family.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
