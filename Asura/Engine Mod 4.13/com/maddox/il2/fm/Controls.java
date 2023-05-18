// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   Controls.java

package com.maddox.il2.fm;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.Time;

import java.io.PrintStream;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.fm:
//            RealFlightModel, FlightModelMain, EnginesInterface, Motor, 
//            FMMath, AircraftState, FlightModel, Mass, 
//            Autopilotage

public class Controls
{

    public void bombsightAutoPickle()
    {
        if((FM instanceof RealFlightModel) && !FM.actor.net.isMirror())
        {
            if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
            {
                FM.CT.WeaponControl[3] = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
            }
        } else
        {
            if(!((Aircraft)FM.actor).isNetPlayer())
                Main3D.cur3D().aircraftHotKeys.addBombardierAction(19);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
        }
    }

    public Controls(FlightModelMain flightmodelmain)
    {
        Sensitivity = 1.0F;
        PowerControl = 0.0F;
        afterburnerFControl = 0.0F;
        GearControl = 0.0F;
        wingControl = 0.0F;
        cockpitDoorControl = 0.0F;
        arrestorControl = 0.0F;
        FlapsControl = 0.0F;
        AileronControl = 0.0F;
        ElevatorControl = 0.0F;
        RudderControl = 0.0F;
        BrakeControl = 0.0F;
        BrakeControlL = 0.0F;
        BrakeControlR = 0.0F;
        StepControl = 1.0F;
        bStepControlAuto = true;
        MixControl = 1.0F;
        MagnetoControl = 3;
        CompressorControl = 0;
        BayDoorControl = 0.0F;
        AirBrakeControl = 0.0F;
        trimAileronControl = 0.0F;
        trimElevatorControl = 0.0F;
        trimRudderControl = 0.0F;
        trimAileron = 0.0F;
        trimElevator = 0.0F;
        trimRudder = 0.0F;
        RadiatorControl = 0.0F;
        bRadiatorControlAuto = true;
        StabilizerControl = false;
        courseAutopilotControl = false;
        WeaponControl = new boolean[21];
        bombReleaseMode = 3;
        rocketReleaseMode = 2;
        bombTrainDelay = 0;
        bombTrainAmount = 1;
        bombsReleased = 0;
        timeOfLastBombRelease = -1L;
        rocketsReleased = 0;
        timeOfLastRocketRelease = -1L;
        pdiLights = -1;
        copilotMode = 0;
        saveWeaponControl = new boolean[4];
        bHasGearControl = true;
        bHasDiffBrakes = false;
        bHasBayDoorControl = false;
        bHasWingControl = false;
        bHasCockpitDoorControl = false;
        bHasArrestorControl = false;
        bHasFlapsControl = true;
        bHasFlapsControlRed = false;
        bHasAileronControl = true;
        bHasElevatorControl = true;
        bHasRudderControl = true;
        bHasBrakeControl = true;
        bHasAirBrakeControl = true;
        bHasStabilizerControl = true;
        bHasLockGearControl = true;
        bHasAileronTrim = true;
        bHasRudderTrim = true;
        bHasElevatorTrim = true;
        Weapons = new BulletEmitter[21][];
        Step = 1.0F;
        AilThr = 100F;
        AilThr3 = 1000000F;
        RudThr = 100F;
        RudThr2 = 10000F;
        ElevThr = 112F;
        ElevThr2 = 12544F;
        dvGear = 0.2F;
        dvGearR = 0.19F;
        dvGearL = 0.21F;
        dvGearC = 0.24F;
        dvWing = 0.1F;
        dvCockpitDoor = 0.1F;
        dvAirbrake = 0.5F;
        electricPropDn = 0;
        PowerControlArr = new float[6];
        StepControlArr = new float[6];
        bDropWithPlayer = false;
        dropWithPlayer = null;
        bDropWithMe = false;
        FM = flightmodelmain;
        for(int i = 0; i < 6; i++)
            PowerControlArr[i] = 0.0F;

        for(int j = 0; j < 6; j++)
            StepControlArr[j] = 1.0F;
        //
        bHasBlownFlaps = false;
        BlownFlapsControl = 0.0F;
        dvBlownFlaps = 0.5F;
        bHasRefuelControl = false;
        RefuelControl = 0.0F;
        dvRefuel = 0.5F;
        bHasDragChuteControl = false;
        DragChuteControl = 0.0F;
        dvDragchute = 0.5F;
        bNoCarrierCanopyOpen = false;
        rocketHookSelected = 2;
        rocketNameSelected = null;
        bHasBayDoors = false;
        bHasFlapsControlSwitch = false;
        FlapsControlSwitch = 0;
        nFlapStages = -1;
        FlapStageText = null;
        FlapStage = null;
        FlapStageMax = -1F;
        bHasVarWingControl = false;
        bHasVarIncidence = false;
        VarWingControl = 0.0F;
        nVarWingStages = -1;
        VarWingStageMax = -1F;
        VarWingStage = null;
        dvVarWing = 0.5F;
        bHasSideDoor = false;
        SIDE_DOOR = 2;
        DiffBrakesType = 0;
    }

    public void set(Controls controls)
    {
        PowerControl = controls.PowerControl;
        GearControl = controls.GearControl;
        arrestorControl = controls.arrestorControl;
        FlapsControl = controls.FlapsControl;
        AileronControl = controls.AileronControl;
        ElevatorControl = controls.ElevatorControl;
        RudderControl = controls.RudderControl;
        BrakeControl = controls.BrakeControl;
        BrakeControlL = controls.BrakeControlL;
        BrakeControlR = controls.BrakeControlR;
        BayDoorControl = controls.BayDoorControl;
        AirBrakeControl = controls.AirBrakeControl;
        dvGear = controls.dvGear;
        dvWing = controls.dvWing;
        dvCockpitDoor = controls.dvCockpitDoor;
        dvAirbrake = controls.dvAirbrake;
        //
        dvBlownFlaps = controls.dvBlownFlaps;
        dvRefuel = controls.dvRefuel;
        dvDragchute = controls.dvDragchute;
        FlapsControlSwitch = controls.FlapsControlSwitch;
        dvVarWing = controls.dvVarWing;
    }

    public void CalcTresholds()
    {
        AilThr3 = AilThr * AilThr * AilThr;
        RudThr2 = RudThr * RudThr;
        ElevThr2 = ElevThr * ElevThr;
    }

    public void setLanded()
    {
        if(bHasGearControl)
            GearControl = Gear = gearC = gearR = gearL = 1.0F;
        else
            Gear = gearC = gearR = gearL = 1.0F;
        FlapsControl = Flaps = 0.0F;
        StepControl = 1.0F;
        bStepControlAuto = true;
        bRadiatorControlAuto = true;
        BayDoorControl = 0.0F;
        AirBrakeControl = 0.0F;
        //
        BlownFlapsControl = BlownFlaps = 0.0F;
        RefuelControl = 0.0F;
        DragChuteControl = 0.0F;
        FlapsControlSwitch = 0;
        VarWingControl = VarWing = 0.0F;
    }

    public void setFixedGear(boolean flag)
    {
        if(flag)
        {
            Gear = 1.0F;
            gearC = gearL = gearR = 1.0F;
            GearControl = 0.0F;
        }
    }

    public void setGearAirborne()
    {
        if(bHasGearControl)
            GearControl = Gear = gearC = gearL = gearR = 0.0F;
    }

    public void setGear(float f)
    {
        Gear = gearR = gearL = gearC = f;
    }

    public void setGearBraking()
    {
        Brake = 1.0F;
    }

    public void forceFlaps(float f)
    {
        Flaps = f;
    }
    
    public void forceVarWing(float f)
    {
        VarWing = f;
    }

    public void forceGear(float f)
    {
        if(bHasGearControl)
            Gear = GearControl = gearC = gearR = gearL = f;
        else
            setFixedGear(true);
    }

    public void forceWing(float f)
    {
        wing = f;
        FM.doRequestFMSFX(1, (int)(100F * f));
        ((Aircraft)FM.actor).moveWingFold(f);
    }

    public void forceArrestor(float f)
    {
        arrestor = f;
        ((Aircraft)FM.actor).moveArrestorHook(f);
    }

    public void setPowerControl(float f)
    {
        if(f < 0.0F)
            f = 0.0F;
        if(f > 1.1F)
            f = 1.1F;
        PowerControl = f;
        for(int i = 0; i < 8; i++)
            if(i < FM.EI.getNum() && FM.EI.bCurControl[i])
                PowerControlArr[i] = f;

    }

    public void setPowerControl(float f, int i)
    {
        if(f < 0.0F)
            f = 0.0F;
        if(f > 1.1F)
            f = 1.1F;
        PowerControlArr[i] = f;
        if(i == 0)
            PowerControl = f;
    }

    public float getPowerControl()
    {
        return PowerControl;
    }

    public float getPowerControl(int i)
    {
        return PowerControlArr[i];
    }

    public void setStepControl(float f)
    {
        if(!bUseElectricProp)
        {
            if(f > 1.0F)
                f = 1.0F;
            if(f < 0.0F)
                f = 0.0F;
            StepControl = f;
            for(int i = 0; i < 8; i++)
                if(i < FM.EI.getNum() && FM.EI.bCurControl[i])
                    StepControlArr[i] = f;

            HUD.log(AircraftHotKeys.hudLogPowerId, "PropPitch", new Object[] {
                new Integer(Math.round(getStepControl() * 100F))
            });
        }
    }

    public void setStepControl(float f, int i)
    {
        if(!bUseElectricProp)
        {
            if(f > 1.0F)
                f = 1.0F;
            if(f < 0.0F)
                f = 0.0F;
            StepControlArr[i] = f;
            if(!getStepControlAuto(i))
                HUD.log(AircraftHotKeys.hudLogPowerId, "PropPitch", new Object[] {
                    new Integer(Math.round(getStepControl(i) * 100F))
                });
        }
    }

    public void setStepControlAuto(boolean flag)
    {
        bStepControlAuto = flag;
    }

    public float getStepControl()
    {
        return StepControl;
    }

    public boolean getStepControlAuto()
    {
        return bStepControlAuto;
    }

    public boolean getStepControlAuto(int i)
    {
        if(i < FM.EI.getNum())
            return !FM.EI.engines[i].isHasControlProp() || FM.EI.engines[i].getControlPropAuto();
        else
            return true;
    }

    public float getStepControl(int i)
    {
        return StepControlArr[i];
    }

    public void setElectricPropUp(boolean flag)
    {
        if(bUseElectricProp)
            if(flag)
                electricPropUp = 1;
            else
                electricPropUp = 0;
    }

    public void setElectricPropDn(boolean flag)
    {
        if(bUseElectricProp)
            if(flag)
                electricPropDn = 1;
            else
                electricPropDn = 0;
    }

    public void setRadiatorControl(float f)
    {
        if(f > 1.0F)
            f = 1.0F;
        if(f < 0.0F)
            f = 0.0F;
        RadiatorControl = f;
    }

    public void setRadiatorControlAuto(boolean flag, EnginesInterface enginesinterface)
    {
        bRadiatorControlAuto = flag;
        if(enginesinterface.getFirstSelected() != null)
            radiator = enginesinterface.getFirstSelected().getControlRadiator();
    }

    public float getRadiatorControl()
    {
        return RadiatorControl;
    }

    public boolean getRadiatorControlAuto()
    {
        return bRadiatorControlAuto;
    }

    public float getRadiator()
    {
        return radiator;
    }

    public void setMixControl(float f)
    {
        if(f < 0.0F)
            f = 0.0F;
        if(f > 1.2F)
            f = 1.2F;
        MixControl = f;
    }

    public float getMixControl()
    {
        return MixControl;
    }

    public void setAfterburnerControl(boolean flag)
    {
        afterburnerControl = flag;
    }

    public boolean getAfterburnerControl()
    {
        return afterburnerControl;
    }

    public void setManualfterburnerControl(float f)
    {
        afterburnerFControl = f;
    }

    public float getManualAfterburnerControl()
    {
        return afterburnerFControl;
    }

    public void setMagnetoControl(int i)
    {
        if(i < 0)
            i = 0;
        if(i > 3)
            i = 3;
        MagnetoControl = i;
    }

    public int getMagnetoControl()
    {
        return MagnetoControl;
    }

    public void setCompressorControl(int i)
    {
        if(i < 0)
            i = 0;
        if(i > FM.EI.engines[0].compressorMaxStep)
            i = FM.EI.engines[0].compressorMaxStep;
        CompressorControl = i;
    }

    public int getCompressorControl()
    {
        return CompressorControl;
    }

    public void setTrimAileronControl(float f)
    {
        trimAileronControl = f;
    }

    public float getTrimAileronControl()
    {
        return trimAileronControl;
    }

    public void setTrimElevatorControl(float f)
    {
        trimElevatorControl = f;
    }

    public float getTrimElevatorControl()
    {
        return trimElevatorControl;
    }

    public void setTrimRudderControl(float f)
    {
        trimRudderControl = f;
    }

    public float getTrimRudderControl()
    {
        return trimRudderControl;
    }

    public void interpolate(Controls controls, float f)
    {
        PowerControl = FMMath.interpolate(PowerControl, controls.PowerControl, f);
        FlapsControl = FMMath.interpolate(FlapsControl, controls.FlapsControl, f);
        AileronControl = FMMath.interpolate(AileronControl, controls.AileronControl, f);
        ElevatorControl = FMMath.interpolate(ElevatorControl, controls.ElevatorControl, f);
        RudderControl = FMMath.interpolate(RudderControl, controls.RudderControl, f);
        BrakeControl = FMMath.interpolate(BrakeControl, controls.BrakeControl, f);
        BrakeControlL = FMMath.interpolate(BrakeControlL, controls.BrakeControlL, f);
        BrakeControlR = FMMath.interpolate(BrakeControlR, controls.BrakeControlR, f);        
    }

    public float getGearL()
    {
        return getGears(0, gearL);
    }

    public float getGearR()
    {
        return getGears(1, gearR);
    }

    public float getGearC()
    {
        return getGears(2, gearC);
    }

    private float getGears(int i, float f)
    {
        float f1 = FM.AS.gearStates[i];
        if(f1 == 0.0F || f1 == -1F || f1 == 1.0F)
            return f;
        if(FM.AS.isMaster() && f1 < 0.0F && FM.AS.gearDamRecoveryStates[i] >= 3 && Gear > Math.abs(f1))
            FM.AS.hitGear(FM.actor, i, 11);
        return f;
    }

    public float getGear()
    {
        return Gear;
    }

    public float getWing()
    {
        return wing;
    }

    public float getCockpitDoor()
    {
        return cockpitDoor;
    }

    public void forceCockpitDoor(float f)
    {
        cockpitDoor = f;
    }

    public float getArrestor()
    {
        return arrestor;
    }

    public float getFlap()
    {
        return Flaps;
    }

    public float getAileron()
    {
        return Ailerons;
    }

    public float getElevator()
    {
        return Elevators;
    }

    public float getRudder()
    {
        return Rudder;
    }

    public float getBrake()
    {
        return Brake;
    }

    public float getBrakeL()
    {
        return BrakeL;
    }

    public float getBrakeR()
    {
        return BrakeR;
    }

    public float getPower()
    {
        return Power;
    }

    public float getStep()
    {
        return Step;
    }

    public float getBayDoor()
    {
        return BayDoorControl;
    }

    public float getAirBrake()
    {
        return airBrake;
    }

    //
    public float getBlownFlaps()
    {
        return BlownFlaps;
    }
    
    public float getVarWing()
    {
        return VarWing;
    }
    
    public float getRefuel()
    {
        return refuel;
    }
    
    public float getDragChute()
    {
        return dragChute;
    }
    //
    
    private float filter(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = (float)Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if(f6 < f1)
        {
            f6 += f4 * f;
            if(f6 > f1)
                f6 = f1;
        } else
        if(f6 > f1)
        {
            f6 -= f4 * f;
            if(f6 < f1)
                f6 = f1;
        }
        return f6;
    }

    private float clamp01(float f)
    {
        if(f < 0.0F)
            f = 0.0F;
        else
        if(f > 1.0F)
            f = 1.0F;
        return f;
    }

    private float clamp0115(float f)
    {
        if(f < 0.0F)
            f = 0.0F;
        else
        if(f > 1.1F)
            f = 1.1F;
        return f;
    }

    private float clamp11(float f)
    {
        if(f < -1F)
            f = -1F;
        else
        if(f > 1.0F)
            f = 1.0F;
        return f;
    }

    private float clampA(float f, float f1)
    {
        if(f < -f1)
            f = -f1;
        else
        if(f > f1)
            f = f1;
        return f;
    }

    public void setActiveDoor(int i)
    {
        if(i != SIDE_DOOR && bMoveSideDoor)
        {
            fSaveSideDoor = cockpitDoor;
            fSaveSideDoorControl = cockpitDoorControl;
            cockpitDoor = fSaveCockpitDoor;
            cockpitDoorControl = fSaveCockpitDoorControl;
            bMoveSideDoor = false;
        } else
        if(i == SIDE_DOOR && !bMoveSideDoor)
        {
            fSaveCockpitDoor = cockpitDoor;
            fSaveCockpitDoorControl = cockpitDoorControl;
            cockpitDoor = fSaveSideDoor;
            cockpitDoorControl = fSaveSideDoorControl;
            bMoveSideDoor = true;
        }
    }
    
    public void update(float f, float f1, EnginesInterface enginesinterface, boolean flag)
    {
        update(f, f1, enginesinterface, flag, false);
    }

    private float getDamMovSpd(int i, int j)
    {
        switch(i)
        {
        case 1: // '\001'
            return j != 0 ? dvGearR : dvGearL;

        case 5: // '\005'
            return j != 0 ? dvGearR * 15F : dvGearL * 15F;

        case 6: // '\006'
            return j != 0 ? dvGearR * 5F : dvGearL * 5F;

        case 8: // '\b'
            return j != 0 ? dvGearR : dvGearL;

        case 4: // '\004'
            if(j == 0)
                return dvGear * 0.4F;
            else
                return dvGear * 0.6F;

        case 7: // '\007'
            return j != 0 ? dvGearR : dvGearL;

        case 3: // '\003'
            return j != 0 ? dvGearR : dvGearL;

        case 2: // '\002'
            return j != 0 ? dvGearR : dvGearL;

        case 10: // '\n'
            return j != 0 ? dvGearR * 0.1F : dvGearL * 0.2F;

        case 9: // '\t'
        default:
            return dvGear;
        }
    }

    public void update(float f, float f1, EnginesInterface enginesinterface, boolean flag, boolean flag1)
    {
    	if(bHasBayDoors)
            bHasBayDoorControl = true;
    	
    	float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 1.0F;
        float f5 = f1 * f1;
        if(f1 > AilThr)
            f2 = Math.max(0.2F, AilThr3 / (f5 * f1));
        if(f5 > RudThr2)
            f3 = Math.max(0.2F, RudThr2 / f5);
        if(f5 > ElevThr2)
            f4 = Math.max(0.4F, ElevThr2 / f5);
        f2 *= Sensitivity;
        f3 *= Sensitivity;
        f4 *= Sensitivity;
        if(!flag1)
            if(FM instanceof RealFlightModel)
            {
                float f6 = 0.0F;
                for(int j1 = 0; j1 < enginesinterface.getNum(); j1++)
                {
                    PowerControlArr[j1] = clamp0115(PowerControlArr[j1]);
                    enginesinterface.engines[j1].setControlThrottle(PowerControlArr[j1]);
                    if(PowerControlArr[j1] > f6)
                        f6 = PowerControlArr[j1];
                }

                if(flag)
                {
                    Power = f6;
                } else
                {
                    Power = filter(f, f6, Power, 5F, 0.01F * f);
                    enginesinterface.setThrottle(Power);
                }
            } else
            {
                PowerControl = clamp0115(PowerControl);
                if(flag)
                    Power = PowerControl;
                else
                    Power = filter(f, PowerControl, Power, 5F, 0.01F * f);
                enginesinterface.setThrottle(Power);
            }
        if(!flag1)
        {
            enginesinterface.setAfterburnerControl(afterburnerControl);
            enginesinterface.setManualAfterburnerControl(afterburnerFControl);
        }
        if(!flag1)
        {
            StepControl = clamp01(StepControl);
            if(bUseElectricProp && (FM instanceof RealFlightModel))
            {
                enginesinterface.setPropAuto(bStepControlAuto);
                int i = electricPropUp - electricPropDn;
                if(i < 0)
                    HUD.log(AircraftHotKeys.hudLogPowerId, "elPropDn");
                else
                if(i > 0)
                    HUD.log(AircraftHotKeys.hudLogPowerId, "elPropUp");
                enginesinterface.setPropDelta(i);
            }
            if(enginesinterface.getFirstSelected() != null)
                if(bStepControlAuto)
                {
                    if(enginesinterface.isSelectionAllowsAutoProp())
                    {
                        enginesinterface.setPropAuto(true);
                    } else
                    {
                        enginesinterface.setPropAuto(false);
                        bStepControlAuto = false;
                    }
                } else
                if(FM instanceof RealFlightModel)
                {
                    if(!bUseElectricProp)
                    {
                        for(int j = 0; j < enginesinterface.getNum(); j++)
                        {
                            StepControlArr[j] = clamp01(StepControlArr[j]);
                            enginesinterface.engines[j].setControlPropAuto(false);
                            enginesinterface.engines[j].setControlProp(StepControlArr[j]);
                        }

                    }
                } else
                {
                    Step = filter(f, StepControl, Step, 0.2F, 0.02F);
                    enginesinterface.setPropAuto(false);
                    enginesinterface.setProp(Step);
                }
        }
        RadiatorControl = clamp01(RadiatorControl);
        radiator = filter(f, RadiatorControl, radiator, 999.9F, 0.2F);
        if(bRadiatorControlAuto && enginesinterface.getFirstSelected() != null)
        {
            if(enginesinterface.isSelectionAllowsAutoRadiator())
            {
                enginesinterface.updateRadiator(f);
            } else
            {
                enginesinterface.setRadiator(radiator);
                bRadiatorControlAuto = false;
            }
        } else
        {
            enginesinterface.setRadiator(radiator);
        }
        if(!flag1)
            enginesinterface.setMagnetos(MagnetoControl);
        if(!flag1 && flag && World.cur().diffCur.ComplexEManagement)
            enginesinterface.setCompressorStep(CompressorControl);
        if(!flag1)
            enginesinterface.setMix(MixControl);
        if(bHasGearControl || flag1)
        {
            GearControl = clamp01(GearControl);
            Gear = filter(f, GearControl, Gear, 999.9F, dvGear);
            float f7 = FM.AS.gearStates[0];
            float f9 = FM.AS.gearStates[1];
            float f11 = FM.AS.gearStates[2];
            if(f7 > 0.0F)
                gearL = filter(f, f7, gearL, 999.9F, getDamMovSpd(FM.AS.gearDamType[0], 0));
            else
            if(f7 < 0.0F)
                gearL = filter(f, Math.min(GearControl, Math.abs(f7)), gearL, 999.9F, getDamMovSpd(FM.AS.gearDamType[0], 0));
            else
                gearL = filter(f, GearControl, gearL, 999.9F, dvGearL);
            if(f9 > 0.0F)
                gearR = filter(f, f9, gearR, 999.9F, getDamMovSpd(FM.AS.gearDamType[1], 1));
            else
            if(f9 < 0.0F)
                gearR = filter(f, Math.min(GearControl, Math.abs(f9)), gearR, 999.9F, getDamMovSpd(FM.AS.gearDamType[1], 1));
            else
                gearR = filter(f, GearControl, gearR, 999.9F, dvGearR);
            if(f11 > 0.0F)
                gearC = filter(f, f11, gearC, 999.9F, getDamMovSpd(FM.AS.gearDamType[2], 2));
            else
            if(f11 < 0.0F)
                gearC = filter(f, Math.min(GearControl, Math.abs(f11)), gearC, 999.9F, getDamMovSpd(FM.AS.gearDamType[2], 2));
            else
                gearC = filter(f, GearControl, gearC, 999.9F, dvGearC);
        }
        if(bHasAirBrakeControl || flag1)
            airBrake = filter(f, AirBrakeControl, airBrake, 999.9F, dvAirbrake);
        //
        if(bHasBlownFlaps || flag1)
        {
            BlownFlapsControl = clamp01(BlownFlapsControl);
            BlownFlaps = filter(f, BlownFlapsControl, BlownFlaps, 999.9F, dvBlownFlaps);
        }
        
        if(bHasRefuelControl || flag1)
            refuel = filter(f, RefuelControl, refuel, 999.9F, dvRefuel);
        
        if(bHasVarWingControl || flag1)
        {
            VarWingControl = clamp01(VarWingControl);
            VarWing = filter(f, VarWingControl, VarWing, 999.9F, dvVarWing);
        }
        
        if(bHasDragChuteControl || flag1)
            dragChute = filter(f, DragChuteControl, dragChute, 999.9F, dvDragchute);
        //
        if(bHasWingControl)
        {
            wing = filter(f, wingControl, wing, 999.9F, dvWing);
            if(wing > 0.01F && wing < 0.99F)
                FM.doRequestFMSFX(1, (int)(100F * wing));
        }
        if(bHasCockpitDoorControl)
            cockpitDoor = filter(f, cockpitDoorControl, cockpitDoor, 999.9F, dvCockpitDoor);
        if((bHasArrestorControl || flag1) && (arrestorControl == 0.0F || arrestorControl == 1.0F))
            arrestor = filter(f, arrestorControl, arrestor, 999.9F, 0.2F);
        if(bHasFlapsControl || flag1)
        {
            FlapsControl = clamp01(FlapsControl);
            if(Flaps > FlapsControl)
                Flaps = filter(f, FlapsControl, Flaps, 999F, Aircraft.cvt(FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.25F));
            else
                Flaps = filter(f, FlapsControl, Flaps, 999F, Aircraft.cvt(FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.02F));
        }
        if(StabilizerControl || courseAutopilotControl)
        {
            float f8 = FM.Or.getKren();
            float f10 = 0.0F;
            if(courseAutopilotControl && (FM instanceof RealFlightModel) && AircraftHotKeys.isCockpitRealMode(0))
                if(FM.actor instanceof TypeBomberAPBombSight)
                    f10 = ((TypeBomberAPBombSight)FM.actor).getBombSightPDI();
                else
                if(FM.actor instanceof TypePatinAutopilot)
                    f10 = ((TypePatinAutopilot)FM.actor).getPDI();
            if(f10 == 0.0F)
            {
                AileronControl = -0.1F * f8 - 2.0F * (float)FM.getW().x;
                RudderControl = -0.1F * FM.AOS + 20F * (float)FM.getW().z;
            } else
            {
                float f12 = 16F;
                float f14 = 4F;
                if(FM.actor instanceof TypeFighter)
                {
                    f12 = 25F;
                    f14 = 12F;
                }
                float f15 = Math.abs(f10);
                if(f15 < 0.2F)
                {
                    RudderControl += 0.5F * f15 * f10;
                    AileronControl = -0.02F * f8 - 2.0F * (float)FM.getW().x;
                } else
                {
                    RudderControl += 0.3F * f15 * f10;
                    float f16 = f8;
                    float f17 = Math.max(f12 * f15, f14);
                    if(f10 > 0.0F)
                        f16 -= f17;
                    else
                        f16 -= 360F - f17;
                    if(f16 < -180F)
                        f16 += 360F;
                    f16 = -0.01F * f16;
                    AileronControl = f16;
                }
                if(RudderControl < -0.33F)
                    RudderControl = -0.33F;
                if(RudderControl > 0.33F)
                    RudderControl = 0.33F;
            }
            tmpV3d.set(FM.Vwld);
            tmpV3d.normalize();
            float f13 = (float)(-500D * (tmpV3d.z - 0.001D));
            if(f13 > 0.8F)
                f13 = 0.8F;
            if(f13 < -0.8F)
                f13 = -0.8F;
            ElevatorControl = (f13 - 0.2F * FM.Or.getTangage() - 0.05F * FM.AOA) + 25F * (float)FM.getW().y;
        }
        if(bHasAileronControl || flag1)
        {
            trimAileron = filter(f, trimAileronControl, trimAileron, 999.9F, 0.25F);
            AileronControl = clamp11(AileronControl);
            tmpF = clampA(AileronControl, f2);
            Ailerons = filter(f, (1.0F + (trimAileron * tmpF <= 0.0F ? 1.0F : -1F) * Math.abs(trimAileron)) * tmpF + trimAileron, Ailerons, 0.2F * (1.0F + 0.3F * Math.abs(AileronControl)), 0.025F);
        }
        if(bHasElevatorControl || flag1)
        {
            trimElevator = filter(f, trimElevatorControl, trimElevator, 999.9F, 0.25F);
            ElevatorControl = clamp11(ElevatorControl);
            tmpF = clampA(ElevatorControl, f4);
            Ev = filter(f, (1.0F + (trimElevator * tmpF <= 0.0F ? 1.0F : -1F) * Math.abs(trimElevator)) * tmpF + trimElevator, Ev, 0.3F * (1.0F + 0.3F * Math.abs(ElevatorControl)), 0.022F);
            if(FM.actor instanceof SU_26M2)
                Elevators = clamp11(Ev);
            else
                Elevators = clamp11(Ev - 0.25F * (1.0F - f4));
        }
        if(bHasRudderControl || flag1)
        {
            trimRudder = filter(f, trimRudderControl, trimRudder, 999.9F, 0.25F);
            RudderControl = clamp11(RudderControl);
            tmpF = clampA(RudderControl, f3);
            Rudder = filter(f, (1.0F + (trimRudder * tmpF <= 0.0F ? 1.0F : -1F) * Math.abs(trimRudder)) * tmpF + trimRudder, Rudder, 0.35F * (1.0F + 0.3F * Math.abs(RudderControl)), 0.025F);
        }
        BrakeControl = clamp01(BrakeControl);
        BrakeControlL = clamp01(BrakeControlL);
        BrakeControlR = clamp01(BrakeControlR);
        if(bHasBrakeControl || flag1)
        {
            if(BrakeControl > Brake)
                Brake = Brake + 0.3F * f;
            else
                Brake = BrakeControl;
            if(bHasDiffBrakes)
            {
                if(BrakeControlL > BrakeL)
                    BrakeL = BrakeL + 1.0F * f;
                else
                    BrakeL = BrakeControlL;
                if(BrakeControlR > BrakeR)
                    BrakeR = BrakeR + 1.0F * f;
                else
                    BrakeR = BrakeControlR;
                if(BrakeR > 0.0F || BrakeL > 0.0F)
                    Brake = Math.max(BrakeR, BrakeL);
            }
        } else
        {
            Brake = 0.0F;
            BrakeL = 0.0F;
            BrakeR = 0.0F;
        }
        
        if(tick == Time.tickCounter())
            return;
        tick = Time.tickCounter();
        CTL = (byte)((GearControl <= 0.5F ? 0 : 1) | (FlapsControl >= 0.25F ? ((byte)(FlapsControl >= 0.66F ? 6 : 4)) : FlapsControl <= 0.1F ? 0 : 2) | (BrakeControl <= 0.2F ? 0 : 8) | (RadiatorControl <= 0.5F ? 0 : 0x10) | (BayDoorControl <= 0.5F ? 0 : 0x20) | (AirBrakeControl <= 0.5F ? 0 : 0x40));
        WCT &= 0xfc;
        TWCT &= 0xfc;
        int k = 0;
        for(int l1 = 1; k < WeaponControl.length && l1 < 256; l1 <<= 1)
        {
            if(WeaponControl[k])
            {
                WCT |= l1;
                TWCT |= l1;
            }
            k++;
        }

        for(int l = 0; l < 4; l++)
            saveWeaponControl[l] = WeaponControl[l];

        for(int i1 = 0; i1 < Weapons.length; i1++)
        {
            if(Weapons[i1] == null)
                continue;
            switch(i1)
            {
            case 2: // '\002'
            case 4:
            case 5:
            case 6:            	            	
                boolean flag2 = WeaponControl[i1];
                if(flag2)
                    doRocketRelease((byte)i1);
                break;

            case 3: // '\003'
                boolean flag3 = WeaponControl[i1];
                if(!flag3)
                    break;
                if(bDropWithPlayer)
                {
                    bDropWithMe = true;
                    bDropWithPlayer = false;
                }
                doBombRelease();
                break;

            default:             	
                boolean flag4 = false;
                for(int k1 = 0; k1 < Weapons[i1].length; k1++)
                {                	
                    Weapons[i1][k1].shots(WeaponControl[i1] ? -1 : 0);
                    flag4 = flag4 || Weapons[i1][k1].haveBullets();
                }

                if(WeaponControl[i1] && !flag4 && FM.isPlayers())
                    ForceFeedback.fxTriggerShake(i1, false);
                break;
            }
        }

    }

    private boolean isPlayers(Aircraft aircraft)
    {
        if(aircraft.FM.isPlayers() && (aircraft.FM instanceof RealFlightModel) && ((RealFlightModel)aircraft.FM).isRealMode())
            return true;
        return aircraft.isNetPlayer();
    }

    public float getWeaponMass()
    {
        int i = Weapons.length;
        float f = 0.0F;
        for(int k = 0; k < i; k++)
        {
            if(Weapons[k] == null)
                continue;
            int l = Weapons[k].length;
            for(int j = 0; j < l; j++)
            {
                BulletEmitter bulletemitter = Weapons[k][j];
                if(bulletemitter == null || (bulletemitter instanceof FuelTankGun))
                    continue;
                int i1 = bulletemitter.countBullets();
                if(i1 < 0)
                {
                    i1 = 1;
                    if((bulletemitter instanceof BombGun) && ((BombGun)bulletemitter).isCassette())
                        i1 = 10;
                }
                f += bulletemitter.bulletMassa() * (float)i1;
            }

        }

        return f;
    }

    public int getWeaponCount(int i)
    {
        if(i >= Weapons.length || Weapons[i] == null)
            return 0;
        int l = Weapons[i].length;
        int k;
        int j = k = 0;
        for(; k < l; k++)
        {
            BulletEmitter bulletemitter = Weapons[i][k];
            if(bulletemitter != null && !(bulletemitter instanceof FuelTankGun) && !(bulletemitter instanceof BombGunNull))
                j += bulletemitter.countBullets();
        }

        return j;
    }

    public boolean dropFuelTanks()
    {
        boolean flag = false;
        for(int i = 0; i < Weapons.length; i++)
        {
            if(Weapons[i] == null)
                continue;
            for(int j = 0; j < Weapons[i].length; j++)
                if((Weapons[i][j] instanceof FuelTankGun) && Weapons[i][j].haveBullets())
                {
                    Weapons[i][j].shots(1);
                    flag = true;
                }

        }

        if(flag)
        {
            ((Aircraft)FM.actor).replicateDropFuelTanks();
            FM.M.onFuelTanksChanged();
        }
        return flag;
    }

    public boolean dropExternalStores(boolean flag)
    {
        boolean flag1 = ((Aircraft)FM.actor).dropExternalStores(flag);
        if(flag1)
        {
            FM.AS.externalStoresDropped = true;
            ((Aircraft)FM.actor).replicateDropExternalStores();
        }
        return flag1;
    }

    public FuelTank[] getFuelTanks()
    {
        int i1 = 0;
        for(int i = 0; i < Weapons.length; i++)
        {
            if(Weapons[i] == null)
                continue;
            for(int k = 0; k < Weapons[i].length; k++)
                if(Weapons[i][k] instanceof FuelTankGun)
                    i1++;

        }

        FuelTank afueltank[] = new FuelTank[i1];
        int j1;
        for(int j = j1 = 0; j < Weapons.length; j++)
        {
            if(Weapons[j] == null)
                continue;
            for(int l = 0; l < Weapons[j].length; l++)
                if(Weapons[j][l] instanceof FuelTankGun)
                    afueltank[j1++] = ((FuelTankGun)Weapons[j][l]).getFuelTank();

        }

        return afueltank;
    }

    public void resetControl(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            AileronControl = 0.0F;
            Ailerons = 0.0F;
            trimAileron = 0.0F;
            break;

        case 1: // '\001'
            ElevatorControl = 0.0F;
            Elevators = 0.0F;
            trimElevator = 0.0F;
            break;

        case 2: // '\002'
            RudderControl = 0.0F;
            Rudder = 0.0F;
            trimRudder = 0.0F;
            break;
        }
    }

    public boolean isShooting()
    {
        for(int i = 0; i < WeaponControl.length; i++)
            if(i != 3 && i != 20 && WeaponControl[i])
                return true;

        return false;
    }

    public void toggleRocketReleaseMode()
    {  	
     	
    	if(getWeaponCount(2) <= 1)
            return;
        int i = rocketReleaseMode + 1;
        if(i > 2)
            i = 0;
        if(i == 0)
        {
            rocketsReleased = 0;
            timeOfLastRocketRelease = -1L;
        }
        rocketReleaseMode = i;
        FM.AS.replicateRocketModeStatesToNet();
        switch(rocketReleaseMode)
        {
        case 0: // '\0'
            HUD.log(AircraftHotKeys.hudLogWeaponId, "RocketReleaseSalvo");
            return;

        case 2: // '\002'
            HUD.log(AircraftHotKeys.hudLogWeaponId, "RocketReleasePairs");
            return;

        case 1: // '\001'
            HUD.log(AircraftHotKeys.hudLogWeaponId, "RocketReleaseSingle");
            return;
        }
    }

    public void toggleBombReleaseMode()
    {
        if(isTrainStillDropping() || getWeaponCount(3) <= 1)
            return;
        boolean flag = hasWingBombs();
        boolean flag1 = hasFuselageBombs();
        int i = bombReleaseMode + 1; 
                        
        if((i == 2 || i == 4) && flag1 != flag)
            i++;
        if(i > 5 || i == 5 && !((Aircraft)FM.actor).hasIntervalometer())
            i = 0;
        if(i == 5)
        {
            bombsReleased = 0;
            timeOfLastBombRelease = -1L;
        }
        bombReleaseMode = i;
        FM.AS.replicateBombModeStatesToNet();
        switch(bombReleaseMode)
        {
        case 0: // '\0'
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSalvo");
            return;

        case 3: // '\003'
            if(flag && flag1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleasePairsWF");
            else
                HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleasePairs");
            return;

        case 4: // '\004'
            if(flag && flag1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleasePairsFF");
            else
                HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleasePairs");
            return;

        case 1: // '\001'
            if(flag && flag1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSingleWF");
            else
                HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSingle");
            return;

        case 2: // '\002'
            if(flag && flag1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSingleFF");
            else
                HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSingle");
            return;

        case 5: // '\005'
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrain");
            return;
        }
    }

    private void autoSwitchToTrain()
    {
        if(bombReleaseMode != 5)
        {
            bombReleaseMode = 5;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrain");
        }
    }

    public void toggleBombTrainAmount()
    {
        if(isTrainStillDropping() || getWeaponCount(3) <= 1)
            return;
        autoSwitchToTrain();
        int i = getWeaponCount(3);
        bombTrainAmount++;
        if(bombTrainAmount > i || bombTrainAmount > ((Aircraft)FM.actor).getBombTrainMaxAmount())
            bombTrainAmount = 1;
        if(bombTrainAmount == i)
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrainAmountAll", new Object[] {
                new Integer(bombTrainAmount)
            });
        else
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrainAmount", new Object[] {
                new Integer(bombTrainAmount)
            });
    }

    public void toggleBombTrainDelay()
    {
        if(isTrainStillDropping() || getWeaponCount(3) <= 1)
            return;
        autoSwitchToTrain();
        bombTrainDelay++;
        if(bombTrainDelay > ((Aircraft)FM.actor).getBombTrainDelayArray().length - 1)
            bombTrainDelay = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrainDelay", new Object[] {
            new Float(getBombTrainDelay())
        });
    }

    public float getBombTrainDelay()
    {
        return (float)((Aircraft)FM.actor).getBombTrainDelayArray()[bombTrainDelay];
    }

    private void doRocketRelease(byte j)
    {
        byte byte0 = 2;
        
        if (j != 2)
        {
        	rocketReleaseMode = 2;
        	byte0 = j;
        }
                      
        if(rocketReleaseMode == 0 && rocketsReleased > 0 && Time.current() < timeOfLastRocketRelease + 100L)
            return;
        int i = rocketsReleased;
        if(rocketReleaseMode == 2 || rocketReleaseMode == 0)
        {
            weaponReleaseCycle(byte0, rocketReleaseMode, 0, 2, false, false);
            weaponReleaseCycle(byte0, rocketReleaseMode, 1, 2, false, false);
        } else
        {
            weaponReleaseCycle(byte0, rocketReleaseMode, 0, 1, false, false);
        }
        if(rocketReleaseMode != 0 || i == rocketsReleased)
        {
            WeaponControl[byte0] = false;
            System.out.println("Stop rocket fire");
            rocketsReleased = 0;
            timeOfLastRocketRelease = -1L;
        }
    }

    public boolean isTrainStillDropping()
    {
        return bombReleaseMode == 5 && bombsReleased > 0;
    }

    private void doBombRelease()
    {
        byte byte0 = 3;
        if(isTrainStillDropping() && (float)Time.current() < (float)timeOfLastBombRelease + getBombTrainDelay())
            return;
        int i = bombsReleased;
        if(bombReleaseMode == 4 || bombReleaseMode == 3)
        {
            boolean flag = hasWingBombs();
            boolean flag2 = hasFuselageBombs();
            weaponReleaseCycle(byte0, bombReleaseMode, 0, 2, flag, flag2);
            weaponReleaseCycle(byte0, bombReleaseMode, 1, 2, flag, flag2);
        } else
        if(bombReleaseMode == 2 || bombReleaseMode == 1)
        {
            boolean flag1 = hasWingBombs();
            boolean flag3 = hasFuselageBombs();
            weaponReleaseCycle(byte0, bombReleaseMode, 0, 1, flag1, flag3);
        } else
        {
            weaponReleaseCycle(byte0, bombReleaseMode, 0, 1, false, false);
        }
        if(bombReleaseMode != 5)
        {
            WeaponControl[byte0] = false;
            bombsReleased = 0;
            timeOfLastBombRelease = -1L;
        } else
        if(bombsReleased >= bombTrainAmount || i == bombsReleased)
        {
            WeaponControl[byte0] = false;
            bombsReleased = 0;
            timeOfLastBombRelease = -1L;
        }
    }

    private void weaponReleaseCycle(int i, int j, int k, int l, boolean flag, boolean flag1)
    {
        for(int i1 = k; i1 < Weapons[i].length; i1 += l)
        {
            if(Weapons[i][i1] instanceof FuelTankGun)
                continue;
            if((Weapons[i][i1] instanceof BombGunNull) && i == 3 && j != 4 && j != 3)
            {
                Weapons[i][i1].shots(1);
                continue;
            }
            if((Weapons[i][i1] instanceof RocketGunNull) && i == 2)
            {
                Weapons[i][i1].shots(1);
                continue;
            }
            if(!Weapons[i][i1].haveBullets())
                continue;
            
            if(Weapons[i][i1].getHookName().startsWith("_InternalRock"))
                if(bHasBayDoorControl && isPlayers((Aircraft)FM.actor))
                {
                    if(BayDoorControl < 0.9F)
                    	continue;
                } else
                {
                    BayDoorControl = 1.0F;
                }
            
            if(Weapons[i][i1].getHookName().startsWith("_BombSpawn") && ((Aircraft)FM.actor).needsOpenBombBay())
                if(bHasBayDoorControl && isPlayers((Aircraft)FM.actor) && World.cur().diffCur.RealisticBombSights)
                {
                    if(BayDoorControl < 0.9F)
                        continue;
                } else
                {
                    BayDoorControl = 1.0F;
                }
            
            if(j == 0 || j == 0)
            {
                Weapons[i][i1].shots(Weapons[i][i1].countBullets());
                if(i == 2)
                {
                    rocketsReleased++;
                    timeOfLastRocketRelease = Time.current();
                }
            } else
            {
                if(i == 3 && j != 0 && j != 5 && (flag1 && (j == 4 || j == 2) && isWingBomb(Weapons[i][i1]) || flag && (j == 3 || j == 1) && isFuselageBomb(Weapons[i][i1])))
                    continue;
                Weapons[i][i1].shots(1);
                if(i == 3)
                {
                    bombsReleased++;
                    timeOfLastBombRelease = Time.current();
                }
            }
            if((Weapons[i][i1] instanceof BombGun) && !((BombGun)Weapons[i][i1]).isCassette() && j != 0)
                return;
            if((Weapons[i][i1] instanceof RocketGun) && !((RocketGun)Weapons[i][i1]).isCassette())
                return;
            if((Weapons[i][i1] instanceof RocketBombGun) && !((RocketBombGun)Weapons[i][i1]).isCassette())
                return;
        }

    }

    public void setRocketModeDefaults()
    {
        BulletEmitter abulletemitter[][] = Weapons;
        if(abulletemitter[2] == null)
            return;               
        
        for(int i = 0; i < abulletemitter[2].length; i++)
            if(abulletemitter[2][i].haveBullets() && (abulletemitter[2][i] instanceof RocketGunX4))
            {            	
                FM.CT.rocketReleaseMode = 1;
                return;
            }
        
    }

    public void setBombModeDefaults()
    {
        BulletEmitter abulletemitter[][] = Weapons;
        if(abulletemitter[3] != null)
        {
            for(int i = 0; i < abulletemitter[3].length; i++)
            {
                if(!abulletemitter[3][i].haveBullets())
                    continue;
                if((abulletemitter[3][i] instanceof RocketGunHS_293) || (abulletemitter[3][i] instanceof RocketGunFritzX) || (abulletemitter[3][i] instanceof RocketGunBat))
                {
                    bombReleaseMode = 1;
                    return;
                }
                if(abulletemitter[3][i] instanceof FlareBombGun)
                {
                    bombReleaseMode = 1;
                    return;
                }
            }

        }
        if((FM.actor instanceof TypeBomber) && ((Aircraft)FM.actor).hasIntervalometer())
            bombReleaseMode = 5;
        int j = 0;
        for(int k = 0; k < FM.AP.way.size(); k++)
        {
            WayPoint waypoint = FM.AP.way.look_at_point(k);
            if(waypoint.Action == 3)
                j++;
        }

        bombTrainAmount = getWeaponCount(3) / Math.max(1, j);
        if(bombTrainAmount == 0)
            bombTrainAmount = 1;
        int l = ((Aircraft)FM.actor).getBombTrainDelayArray().length;
        int i1 = 0;
        do
        {
            if(i1 >= l)
                break;
            if(((Aircraft)FM.actor).getBombTrainDelayArray()[i1] >= 200)
            {
                bombTrainDelay = i1;
                break;
            }
            i1++;
        } while(true);
    }

    public boolean isWingBomb(BulletEmitter bulletemitter)
    {
        String s = bulletemitter.getHookName();
        Hook hook = FM.actor.findHook(s);
        String s1 = hook.chunkName();
        return s1.toLowerCase().startsWith("wing");
    }

    private boolean isFuselageBomb(BulletEmitter bulletemitter)
    {
        String s = bulletemitter.getHookName();
        Hook hook = FM.actor.findHook(s);
        String s1 = hook.chunkName();
        return s1.toLowerCase().startsWith("cf");
    }

    private boolean hasWingBombs()
    {
        for(int i = 0; i < Weapons[3].length; i++)
            if(!(Weapons[3][i] instanceof FuelTankGun) && !(Weapons[3][i] instanceof BombGunNull) && Weapons[3][i].haveBullets() && isWingBomb(Weapons[3][i]))
                return true;

        return false;
    }

    private boolean hasFuselageBombs()
    {
        for(int i = 0; i < Weapons[3].length; i++)
            if(!(Weapons[3][i] instanceof FuelTankGun) && !(Weapons[3][i] instanceof BombGunNull) && Weapons[3][i].haveBullets() && isFuselageBomb(Weapons[3][i]))
                return true;

        return false;
    }
    
    public boolean doSetRocketHook(int i)
    {
        rocketHookSelected = i;
        if(Weapons[rocketHookSelected] == null)
            return false;
        if(Weapons[rocketHookSelected][0] instanceof MissileGun)
        {
            GuidedMissileUtils guidedmissileutils = ((TypeGuidedMissileCarrier)FM.actor).getGuidedMissileUtils();
            Class class2 = ((RocketGun)Weapons[rocketHookSelected][0]).bulletClass();
            guidedmissileutils.changeMissileClass(class2);
            rocketNameSelected = guidedmissileutils.getMissileName();
        } else
        {
            Class class1 = ((RocketGun)Weapons[rocketHookSelected][0]).bulletClass();
            rocketNameSelected = Property.stringValue(class1, "friendlyName", "Rocket");
        }
        return true;
    }
    
    public boolean toggleRocketHook()
    {    	    	
    	if(rocketHookSelected == 2)
        {
            if(Weapons[4] != null)
                return doSetRocketHook(4);
            if(Weapons[5] != null)
                return doSetRocketHook(5);
            if(Weapons[6] != null)
                return doSetRocketHook(6);
        } else
        if(rocketHookSelected == 4)
        {
            if(Weapons[5] != null)
                return doSetRocketHook(5);
            if(Weapons[6] != null)
                return doSetRocketHook(6);
            if(Weapons[2] != null)
                return doSetRocketHook(2);
        } else
        if(rocketHookSelected == 5)
        {
            if(Weapons[6] != null)
                return doSetRocketHook(6);
            if(Weapons[2] != null)
                return doSetRocketHook(2);
            if(Weapons[4] != null)
                return doSetRocketHook(4);
        } else
        if(rocketHookSelected == 6)
        {
            if(Weapons[2] != null)
                return doSetRocketHook(2);
            if(Weapons[4] != null)
                return doSetRocketHook(4);
            if(Weapons[5] != null)
                return doSetRocketHook(5);
        }
        return false;
    }
    
    public float Sensitivity;
    public float PowerControl;
    public boolean afterburnerControl;
    public float afterburnerFControl;
    public float GearControl;
    public float wingControl;
    public float cockpitDoorControl;
    public float arrestorControl;
    public float FlapsControl;
    public float AileronControl;
    public float ElevatorControl;
    public float RudderControl;
    public float BrakeControl;
    public float BrakeControlL;
    public float BrakeControlR;
    private float StepControl;
    private boolean bStepControlAuto;
    private float MixControl;
    private int MagnetoControl;
    private int CompressorControl;
    public float BayDoorControl;
    public float AirBrakeControl;
    private float trimAileronControl;
    private float trimElevatorControl;
    private float trimRudderControl;
    public float trimAileron;
    public float trimElevator;
    public float trimRudder;
    private float RadiatorControl;
    private boolean bRadiatorControlAuto;
    public boolean StabilizerControl;
    public boolean courseAutopilotControl;
    public boolean WeaponControl[];
    private static final int R_RELEASE_SALVO = 0;
    private static final int R_RELEASE_SINGLE = 1;
    private static final int R_RELEASE_PAIRS = 2;
    public static final int B_RELEASE_SALVO = 0;
    public static final int B_RELEASE_SINGLE_WINGS_FIRST = 1;
    private static final int B_RELEASE_SINGLE_FC_FIRST = 2;
    private static final int B_RELEASE_PAIRS_WINGS_FIRST = 3;
    private static final int B_RELEASE_PAIRS_FC_FIRST = 4;
    public static final int B_RELEASE_TRAIN = 5;
    public int bombReleaseMode;
    public int rocketReleaseMode;
    public int bombTrainDelay;
    public int bombTrainAmount;
    public static final int bombTrainDelays[] = {
        50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 
        1000
    };
    private int bombsReleased;
    private long timeOfLastBombRelease;
    private int rocketsReleased;
    private long timeOfLastRocketRelease;
    public byte pdiLights;
    public int copilotMode;
    public static final int COPILOT_MODE_DUAL = 0;
    public static final int COPILOT_MODE_FULL = 1;
    public static final int COPILOT_MODE_NO_AXIS = 2;
    public static final int COPILOT_MODE_BLOCKED = 3;
    public boolean saveWeaponControl[];
    public boolean bHasGearControl;
    public boolean bHasDiffBrakes;
    public boolean bHasBayDoorControl;
    public boolean bHasWingControl;
    public boolean bHasCockpitDoorControl;
    public boolean bHasArrestorControl;
    public boolean bHasFlapsControl;
    public boolean bHasFlapsControlRed;
    public boolean bHasAileronControl;
    public boolean bHasElevatorControl;
    public boolean bHasRudderControl;
    public boolean bHasBrakeControl;
    public boolean bHasAirBrakeControl;
    public boolean bHasStabilizerControl;
    public boolean bHasLockGearControl;
    public boolean bHasAileronTrim;
    public boolean bHasRudderTrim;
    public boolean bHasElevatorTrim;
    public BulletEmitter Weapons[][];
    public byte CTL;
    public byte WCT;
    public int TWCT;
    private float Power;
    private float Gear;
    private float gearR;
    private float gearL;
    private float gearC;
    private float wing;
    private float cockpitDoor;
    private float arrestor;
    private float Flaps;
    public float Ailerons;
    public float Elevators;
    public float Rudder;
    private float Brake;
    private float BrakeL;
    private float BrakeR;
    private float Step;
    private float radiator;
    private float airBrake;
    private float Ev;
    private int tick;
    public float AilThr;
    public float AilThr3;
    public float RudThr;
    public float RudThr2;
    public float ElevThr;
    public float ElevThr2;
    public float dvGear;
    public float dvGearR;
    public float dvGearL;
    public float dvGearC;
    public float dvWing;
    public float dvCockpitDoor;
    public float dvAirbrake;
    public int electricPropUp;
    public int electricPropDn;
    public boolean bUseElectricProp;
    private final int POWERCONTROLNUM = 6;
    public float PowerControlArr[];
    public float StepControlArr[];
    private FlightModelMain FM;
    private static float tmpF;
    public boolean bDropWithPlayer;
    public Aircraft dropWithPlayer;
    boolean bDropWithMe;
    private static Vector3d tmpV3d = new Vector3d();
    //
    public boolean bHasBlownFlaps;
    public float BlownFlapsControl;
    private float BlownFlaps;
    public float dvBlownFlaps;
    public String BlownFlapsType;
    public boolean bHasRefuelControl;
    private float refuel;
    public float RefuelControl;
    public float dvRefuel;
    public boolean bHasDragChuteControl;
    private float dragChute;
    public float DragChuteControl;
    public float dvDragchute;
    public boolean bNoCarrierCanopyOpen;
    public int rocketHookSelected;
    public String rocketNameSelected;
    public boolean bHasBayDoors;
    public boolean bHasFlapsControlSwitch;
    public int FlapsControlSwitch;
    public int nFlapStages;
    public String FlapStageText[];
    public float FlapStage[];
    public float FlapStageMax;
    public float VarWingControl;
    private float VarWing;
    public int nVarWingStages;
    public float VarWingStageMax;
    public float VarWingStage[];
    public float dvVarWing;
    public boolean bHasVarWingControl;
    public boolean bHasVarIncidence;
    private float fSaveSideDoor;
    private float fSaveSideDoorControl;
    private float fSaveCockpitDoor;
    private float fSaveCockpitDoorControl;
    public boolean bMoveSideDoor;
    private int SIDE_DOOR;
    public boolean bHasSideDoor;
    public int DiffBrakesType;
}
