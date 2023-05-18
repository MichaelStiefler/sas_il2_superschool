
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


public abstract class KC_10 extends Scheme3
    implements TypeTransport, TypeSupersonic, TypeFastJet, TypeFuelDump
{

    public KC_10()
    {
        lLightHook = new Hook[4];
        lightTime = 0.0F;
        SonicBoom = 0.0F;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        engineSurgeDamage = 0.0F;
        hasHydraulicPressure = true;
        antiColLight = new Eff3DActor[6];
        flashLight = new Eff3DActor[2];
        bAntiColLight = false;
        bFL = false;
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
            for(int i = 0; i < 1; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = FM.EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = FM.EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 20F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.001D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
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
                    oldthrl = curthrl;
                }

        }
    }

    private final void umn()
    {
        Vector3d vector3d = FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        KC_10 kc_10 = this;
        float f = mn;
        World.cur().getClass();
        kc_10.mn = f / Atmosphere.sonicSpeed((float)FM.Loc.z);
        if(mn >= lteb)
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
        if(FM.EI.engines[0].getThrustOutput() > 0.95F && (double)calculateMach() < 0.32000000000000001D && FM.EI.engines[0].getStage() > 5)
            FM.producedAF.x += 1000D;  // for fake FM, 1/4 of historical
        if(FM.EI.engines[1].getThrustOutput() > 0.95F && (double)calculateMach() < 0.32000000000000001D && FM.EI.engines[1].getStage() > 5)
            FM.producedAF.x += 1000D;  // for fake FM, 1/4 of historical
        if(FM.EI.engines[2].getThrustOutput() > 0.95F && (double)calculateMach() < 0.32000000000000001D && FM.EI.engines[2].getStage() > 5)
            FM.producedAF.x += 1000D;  // for fake FM, 1/4 of historical
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() == 6 && FM.EI.engines[1].getThrustOutput() < 1.001F && FM.EI.engines[1].getStage() == 6 && FM.EI.engines[2].getThrustOutput() < 1.001F && FM.EI.engines[2].getStage() == 6)
            if((double)f > 13.5D)
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
        if((double)f > 2.25D)
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
        FM.CT.bHasSideDoor = true;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 28)
            if(!bFL)
            {
                bFL = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL ON");
            } else
            {
                bFL = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL OFF");
            }
        if(i == 29)
            if(!bAntiColLight)
            {
                bAntiColLight = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Anti-Col Lights ON");
            } else
            {
                bAntiColLight = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Anti-Col Lights OFF");
            }
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.EI.engines[1].getStage() < 6
           && FM.EI.engines[2].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            hasHydraulicPressure = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasFlapsControl = false;
            FM.CT.AirBrakeControl = 0.0F;
        } else
        if(!hasHydraulicPressure)
        {
            hasHydraulicPressure = true;
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
        boolean bSideDoorOccupy = false;
        if(FM.CT.bHasSideDoor && FM.CT.bMoveSideDoor)
        {
            if(FM.CT.getCockpitDoor() > 0.0F && FM.CT.getCockpitDoor() < 1.0F)
                bSideDoorOccupy = true;
        }
        if(!bSideDoorOccupy)
            FM.CT.setActiveDoor( 1 );     // setActiveDoor <== not SIDE_DOOR
        if (FM.Gears.onGround() && !bSideDoorOccupy)
        {
            if(FM.brakeShoe && FM.CT.getCockpitDoor() > 0.9F)
                hierMesh().chunkVisible("Stair_D0", true);
            else
                hierMesh().chunkVisible("Stair_D0", false);
        }
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            KC_10 kc_10 = this;
            float f1 = mn;
            World.cur().getClass();
            kc_10.mn = f1 / Atmosphere.sonicSpeed((float)((Tuple3d) (FM.Loc)).z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
            if (FM.AP.way.isLanding() && FM.getSpeed() > FM.VmaxFLAPS && FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)
            {
                if (FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            }
            else if (((Maneuver) FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.getSpeed() < FM.VmaxFLAPS * 1.16F)
            {
                if (FM.getSpeed() > FM.VminFLAPS * 0.5F && FM.Gears.onGround())
                {
                    if (FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                }
                else if (FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            }
            else if (((Maneuver) FM).get_maneuver() == 66)
            {
                if (FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            }
            else if (((Maneuver) FM).get_maneuver() == 7)
            {
                if (FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            }
            else if (hasHydraulicPressure && FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 0.91F || FM.EI.engines[1].getThrustOutput() > 0.91F || FM.EI.engines[2].getThrustOutput() > 0.91F)
            FM.CT.AirBrakeControl = 0.0F;

        formationlights();
        if(!FM.isPlayers())
            bAntiColLight = FM.AS.bNavLightsOn;
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
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head3_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;

        case 2: // '\002'
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Head3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;
        }
    }

    public void update(float f)
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
                    if(FM.EI.engines[i].getThrustOutput() > 0.91F && FM.EI.engines[i].getThrustOutput() < 0.971F)
                        FM.AS.setSootState(this, i, 4);
                    else
                    if(FM.EI.engines[i].getThrustOutput() > 0.84F && FM.EI.engines[i].getThrustOutput() < 0.911F)
                        FM.AS.setSootState(this, i, 3);
                    else
                    if(FM.EI.engines[i].getThrustOutput() > 0.58F && FM.EI.engines[i].getThrustOutput() < 0.841F)
                        FM.AS.setSootState(this, i, 2);
                    else
                        FM.AS.setSootState(this, i, 1);
                } else
                {
                    FM.AS.setSootState(this, i, 0);
                }
                setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[i].getThrustOutput(), 0.86F, 1.04F, 0.0F, 12F)), i);
            }
            if(FM instanceof RealFlightModel)
                umn();
        }
        engineSurge(f);
        checkHydraulicStatus();
        soundbarier();
        super.update(f);
        rotWheels(f);

        anticollight();

        if(Config.isUSE_RENDER())
            if(this == World.getPlayerAircraft() && Main3D.cur3D().viewActor() == World.getPlayerAircraft() && !Main3D.cur3D().isViewOutside())
                hierMesh().chunkVisible("Cockpit_WindowClmn", false);
            else
                hierMesh().chunkVisible("Cockpit_WindowClmn", true);
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
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -0.2F);
            } else
            {
                Aircraft.xyz[1] = Aircraft.cvt(f, 0.06F, 0.99F, -0.2F, -2.1F);
                Aircraft.xyz[2] = Aircraft.cvt(f, 0.06F, 0.99F, 0.0F, 0.973F);
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

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC_CoverL2", 0.0F, 0.0F, Aircraft.cvt(f2, 0.01F, 0.08F, 0.0F, -85F));
        hiermesh.chunkSetAngles("GearC_CoverR2", 0.0F, 0.0F, Aircraft.cvt(f2, 0.01F, 0.08F, 0.0F, 85F));
        if(f2 < 0.10F)
        {
            hiermesh.chunkSetAngles("GearC_CoverL1", 0.0F, 0.0F, Aircraft.cvt(f2, 0.01F, 0.08F, 0.0F, -85F));
            hiermesh.chunkSetAngles("GearC_CoverR1", 0.0F, 0.0F, Aircraft.cvt(f2, 0.01F, 0.08F, 0.0F, 85F));
        }
        if(f2 > 0.90F)
        {
            hiermesh.chunkSetAngles("GearC_CoverL1", 0.0F, 0.0F, Aircraft.cvt(f2, 0.91F, 0.98F, -85F, 0.0F));
            hiermesh.chunkSetAngles("GearC_CoverR1", 0.0F, 0.0F, Aircraft.cvt(f2, 0.91F, 0.98F, 85F, 0.0F));
        }

        if(f < 0.10F)
            hiermesh.chunkSetAngles("GearL_CoverC", 0.0F, 0.0F, Aircraft.cvt(f, 0.02F, 0.09F, 0.0F, 88F));
        if(f > 0.90F)
            hiermesh.chunkSetAngles("GearL_CoverC", 0.0F, 0.0F, Aircraft.cvt(f, 0.92F, 0.99F, 88F, 0.0F));
        hiermesh.chunkSetAngles("GearL_CoverW", 0.0F, 0.0F, Aircraft.cvt(f, 0.13F, 0.85F, 0.0F, -100F));

        if(f1 < 0.10F)
            hiermesh.chunkSetAngles("GearR_CoverC", 0.0F, 0.0F, Aircraft.cvt(f1, 0.02F, 0.09F, 0.0F, -88F));
        if(f1 > 0.90F)
            hiermesh.chunkSetAngles("GearR_CoverC", 0.0F, 0.0F, Aircraft.cvt(f1, 0.92F, 0.99F, -88F, 0.0F));
        hiermesh.chunkSetAngles("GearR_CoverW", 0.0F, 0.0F, Aircraft.cvt(f1, 0.13F, 0.85F, 0.0F, 100F));

        if(f < 0.10F)
        {
            hiermesh.chunkSetAngles("GearT_CoverLF", 0.0F, 0.0F, Aircraft.cvt(f, 0.02F, 0.09F, 0.0F, -88F));
            hiermesh.chunkSetAngles("GearT_CoverLR", 0.0F, 0.0F, Aircraft.cvt(f, 0.02F, 0.09F, 0.0F, -88F));
            hiermesh.chunkSetAngles("GearT_CoverRF", 0.0F, 0.0F, Aircraft.cvt(f, 0.02F, 0.09F, 0.0F, 88F));
            hiermesh.chunkSetAngles("GearT_CoverRR", 0.0F, 0.0F, Aircraft.cvt(f, 0.02F, 0.09F, 0.0F, 88F));
        }
        if(f > 0.90F)
        {
            hiermesh.chunkSetAngles("GearT_CoverLF", 0.0F, 0.0F, Aircraft.cvt(f, 0.92F, 0.99F, -88F, 0.0F));
            hiermesh.chunkSetAngles("GearT_CoverLR", 0.0F, 0.0F, Aircraft.cvt(f, 0.92F, 0.99F, -88F, -65F));
            hiermesh.chunkSetAngles("GearT_CoverRF", 0.0F, 0.0F, Aircraft.cvt(f, 0.92F, 0.99F, 88F, 0.0F));
            hiermesh.chunkSetAngles("GearT_CoverRR", 0.0F, 0.0F, Aircraft.cvt(f, 0.92F, 0.99F, 88F, 65F));
        }

        hiermesh.chunkSetAngles("GearT3_D0", 0.0F, Aircraft.cvt(f, 0.16F, 0.83F, 0.0F, -98F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.70F, 0.81F, 0.0F, -0.46F) * f;
        hiermesh.chunkSetLocate("GearT2_D0", Aircraft.xyz, Aircraft.ypr);

        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.15F, 0.85F, 0.0F, -92F), 0.0F);

        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.14F, 0.86F, 0.0F, -90F));
        hiermesh.chunkSetAngles("GearL41_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.14F, 0.86F, 0.0F, 107F));
        resetXYZYPR();
        Aircraft.xyz[2] = 0.70F * f;
        hiermesh.chunkSetLocate("GearL42_D0", Aircraft.xyz, Aircraft.ypr);

        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.14F, 0.86F, 0.0F, 90F));
        hiermesh.chunkSetAngles("GearR41_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.14F, 0.86F, 0.0F, -107F));
        resetXYZYPR();
        Aircraft.xyz[2] = -0.70F * f1;
        hiermesh.chunkSetLocate("GearR42_D0", Aircraft.xyz, Aircraft.ypr);

        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.88F, 0.95F, 0.0F, -0.27F) * f;
        hiermesh.chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);

        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f1, 0.88F, 0.95F, 0.0F, -0.27F) * f;
        hiermesh.chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);


      return;
      /*
        if(f2 < 0.31F)
            hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f2, 0.11F, 0.31F, 0.0F, -50F), 0.0F);
        else if(f2 < 0.69F)
            hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f2, 0.31F, 0.69F, -50F, -179F), 0.0F);
        else
            hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.85F, -179F, -140F), 0.0F);
        if(f2 < 0.31F)
            hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.11F, 0.31F, 0.0F, 17.3F), 0.0F);
        else if(f2 < 0.69F)
            hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.31F, 0.69F, 17.3F, 85F), 0.0F);
        else if(f2 < 0.75F)
            hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.69F, 0.75F, 85F, 71F), 0.0F);
        else
            hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.85F, 71F, -2F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[0] = Aircraft.cvt(f2, 0.15F, 0.83F, 0.21F, 0.26F);
        hiermesh.chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
        float deg = (float)Math.toDegrees(Math.acos((double)((0.742F - Aircraft.xyz[0]) / 0.742F)));
        hiermesh.chunkSetAngles("GearC71_D0", 0.0F, -deg, 0.0F);
        hiermesh.chunkSetAngles("GearC72_D0", 0.0F, deg * 2F, 0.0F);
        hiermesh.chunkSetAngles("GearC51_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.90F, 0.0F, -25F), 0.0F);

        resetXYZYPR();
        if(f < 0.5F)
        {
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.10F, 0.20F, 0.0F, 0.15F);
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.10F, 0.5F, 0.0F, -50F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.05F, 0.14F, 0.0F, -8F);
        }
        else if(f < 0.75F)
        {
            Aircraft.xyz[1] = 0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.5F, 0.75F, -50F, -85F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.5F, 0.75F, -8F, -2.8F);
        }
        else
        {
            Aircraft.xyz[1] = 0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.75F, 1F, -85F, -105F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.75F, 1F, -2.8F, -0.8F);
        }
        hiermesh.chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(f, 0.10F, 0.20F, 0.0F, 98F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.65F, 1F, 0F, 16.32F));
        if(f < 0.6F)
            hiermesh.chunkSetAngles("GearL51_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.4F, 0.6F, 0F, -77.4F));
        else
            hiermesh.chunkSetAngles("GearL51_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.6F, 1.0F, -77.4F, -167.627F));
        hiermesh.chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, 4.25F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, -0.222F);
        hiermesh.chunkSetLocate("GearL23_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.4F, 0.8F, 0.0F, 0.51F);
        hiermesh.chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
        deg = (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F)));
        deg -= (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F) / 0.96F)));
        hiermesh.chunkSetAngles("GearL41_D0", 0.0F, 0.0F, -deg);
        hiermesh.chunkSetAngles("GearL42_D0", 0.0F, 0.0F, deg * 2F);
        hiermesh.chunkSetAngles("GearL15_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.4F, 0.6F, 0F, 70F));

        hiermesh.chunkSetAngles("GearL11_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 30F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -90F));
        hiermesh.chunkSetAngles("GearL61_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.03F, 0.1F, 0.0F, 90F));
      
        resetXYZYPR();
        if(f1 < 0.5F)
        {
            Aircraft.xyz[1] = Aircraft.cvt(f1, 0.10F, 0.20F, 0.0F, -0.15F);
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.10F, 0.5F, 0.0F, -50F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.05F, 0.14F, 0.0F, 8F);
        }
        else if(f1 < 0.75F)
        {
            Aircraft.xyz[1] = -0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.5F, 0.75F, -50F, -85F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.5F, 0.75F, 8F, 2.8F);
        }
        else
        {
            Aircraft.xyz[1] = -0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.75F, 1F, -85F, -105F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.75F, 1F, 2.8F, 0.8F);
        }
        hiermesh.chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR3_D0", Aircraft.cvt(f1, 0.10F, 0.20F, 0.0F, -98F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.65F, 1F, 0F, 16.32F));
        if(f1 < 0.6F)
            hiermesh.chunkSetAngles("GearR51_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.4F, 0.6F, 0F, -77.4F));
        else
            hiermesh.chunkSetAngles("GearR51_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.6F, 1.0F, -77.4F, -167.627F));
        hiermesh.chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(f1, 0.1F, 1.0F, 0.0F, 4.25F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f1, 0.1F, 1.0F, 0.0F, -0.222F);
        hiermesh.chunkSetLocate("GearR23_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[1] = Aircraft.cvt(f1, 0.4F, 0.8F, 0.0F, 0.51F);
        hiermesh.chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
        deg = (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F)));
        deg -= (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F) / 0.96F)));
        hiermesh.chunkSetAngles("GearR41_D0", 0.0F, 0.0F, -deg);
        hiermesh.chunkSetAngles("GearR42_D0", 0.0F, 0.0F, deg * 2F);
        hiermesh.chunkSetAngles("GearR15_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.4F, 0.6F, 0F, -70F));

        hiermesh.chunkSetAngles("GearR11_D0", 0.0F, Aircraft.cvt(f1, 0.03F, 0.15F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.01F, 0.1F, 0.0F, 90F));
        hiermesh.chunkSetAngles("GearR61_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.03F, 0.1F, 0.0F, -90F));
  */
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don't indepently move their gears
    public static void moveGear(HierMesh hiermesh, float f)
    {
        moveGear(hiermesh, f, f, f); // re-route old style function calls to new code
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
    }

    private void rotWheels(float f)
    {
        for (int i1 = 0; i1 < 3; i1++)
            fWheelAngles[i1] = (fWheelAngles[i1] + (float) Math.toDegrees(Math.atan((FM.Gears.gVelocity[i1] * (double) f) / dWheelsRadius[i1]))) % 360F;
        fWheelAngles[3] = (fWheelAngles[3] + (float) Math.toDegrees(Math.atan((Math.max(FM.Gears.gVelocity[0],FM.Gears.gVelocity[1]) * (double) f) / dWheelsRadius[3]))) % 360F;

        hierMesh().chunkSetAngles("GearL1f_D0", 0.0F, -fWheelAngles[0], 0.0F);
        hierMesh().chunkSetAngles("GearL1r_D0", 0.0F, -fWheelAngles[0], 0.0F);
        hierMesh().chunkSetAngles("GearR1f_D0", 0.0F, -fWheelAngles[1], 0.0F);
        hierMesh().chunkSetAngles("GearR1r_D0", 0.0F, -fWheelAngles[1], 0.0F);
        hierMesh().chunkSetAngles("GearC1_D0", 0.0F, -fWheelAngles[2], 0.0F);
        hierMesh().chunkSetAngles("GearT1_D0", 0.0F, -fWheelAngles[3], 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 35F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 35F * f, 0.0F, 0.0F);
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
        Aircraft.xyz[0] = -0.60F * f;
        Aircraft.xyz[2] = 0.24F * f;
        Aircraft.ypr[1] = 42F * f;
        Aircraft.ypr[2] = 1.4F * f;
        hierMesh().chunkSetLocate("FlapLIn1_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.45F * f;
        Aircraft.xyz[2] = 0.10F * f;
        Aircraft.ypr[1] = 8F * f;
        hierMesh().chunkSetLocate("FlapLIn2_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.60F * f;
        Aircraft.xyz[2] = 0.24F * f;
        Aircraft.ypr[1] = 42F * f;
        Aircraft.ypr[2] = -1.4F * f;
        hierMesh().chunkSetLocate("FlapRIn1_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.45F * f;
        Aircraft.xyz[2] = 0.10F * f;
        Aircraft.ypr[1] = 8F * f;
        hierMesh().chunkSetLocate("FlapRIn2_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.78F * f;
        Aircraft.ypr[1] = 30F * f;  // 20F
        hierMesh().chunkSetLocate("FlapLOut_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.78F * f;
        Aircraft.ypr[1] = 30F * f;  // 20F
        hierMesh().chunkSetLocate("FlapROut_D0", Aircraft.xyz, Aircraft.ypr);

        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.cos(Math.toRadians(35.5D))) * 0.55F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(-Math.sin(Math.toRadians(35.5D))) * 0.55F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -0.12F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -18.0F);
        hierMesh().chunkSetLocate("SlatLI_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.cos(Math.toRadians(34.0D))) * 0.51F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(-Math.sin(Math.toRadians(34.0D))) * 0.51F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -0.06F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -18.0F);
        hierMesh().chunkSetLocate("SlatLO_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.cos(Math.toRadians(35.5D))) * 0.55F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.sin(Math.toRadians(35.5D))) * 0.55F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -0.12F);
        Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -18.0F);
        hierMesh().chunkSetLocate("SlatRI_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.cos(Math.toRadians(34.0D))) * 0.51F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, (float)(Math.sin(Math.toRadians(34.0D))) * 0.51F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -0.06F);
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
        if(bAntiColLight)
        {
            for(int i = 0; i < 6; i++)
            {
                if(antiColLight[i] == null)
                {
                    try
                    {
                        antiColLight[i] = Eff3DActor.New(this, findHook("_AntiColLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRedFlash.eff", -1.0F, false);
                    } catch(Exception exception) { }
                }
            }
            for(int i = 0; i < 2; i++)
            {
                if(flashLight[i] == null)
                {
                    try
                    {
                        flashLight[i] = Eff3DActor.New(this, findHook("_FlashLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareWhiteFlash2.eff", -1.0F, false);
                    } catch(Exception exception) { }
                }
            }
        }
        else
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
        }
    }

    private void formationlights()
    {
        int ws = Mission.cur().curCloudsType();
        float we = Mission.cur().curCloudsHeight() + 500F;
        if((World.getTimeofDay() <= 6.5F || World.getTimeofDay() > 18F || (ws > 4 && FM.getAltitude()<we)) && !FM.isPlayers())
        {
            bFL = true;
        }
        if(((World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && ws <= 4) || (World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && FM.getAltitude()>we)) && !FM.isPlayers())
        {
            bFL = false;
        }
        hierMesh().chunkVisible("SSlightCf", bFL);
        hierMesh().chunkVisible("SSlightNs", bFL);
        hierMesh().chunkVisible("SSlightKl", bFL);
        hierMesh().chunkVisible("SSlightWgl", bFL);
        hierMesh().chunkVisible("SSlightWgr", bFL);
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

    public void setExhaustFlame(int i, int j)
    {

        return;
/*
        if(j == 0)
            switch(i)
            {
            case 0: // '\0'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                // fall through

            case 4: // '\004'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", true);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", true);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", true);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", true);
                break;

            default:
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;
            }
        if(j == 1)
            switch(i)
            {
            case 0: // '\0'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                // fall through

            case 4: // '\004'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", true);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", true);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", true);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", true);
                break;

            default:
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;
            }
  */
    }

    private static void resetXYZYPR()
    {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    private float oldctl;
    private float curctl;
    private float oldthrl;
    private float curthrl;
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
    private float engineSurgeDamage;
    public boolean hasHydraulicPressure;
    private Eff3DActor antiColLight[];
    private Eff3DActor flashLight[];
    private boolean bAntiColLight;
    private boolean bFL;
    private float stockAilerons;
    private float fWheelAngles[] = {0.0F, 0.0F, 0.0F, 0.0F};
    private double dWheelsRadius[] = {0.660D, 0.660D, 0.508D, 0.660D};
    public static float FlowRate = 10F;  // fake value, 1/4 of historical
    public static float FuelReserve = 1500F;  // fake value, 1/4 of historical

    static 
    {
        Class class1 = com.maddox.il2.objects.air.KC_10.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
