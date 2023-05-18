// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 28.04.2015 16:37:30
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Controls.java

package com.maddox.il2.fm;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.fm:
//            RealFlightModel, FlightModelMain, EnginesInterface, Motor, 
//            FMMath, AircraftState, FlightModel, Mass

public class Controls
{

    public Controls(FlightModelMain flightmodelmain)
    {
        bHasBayDoors = false;
        bMoveSideDoor = false;
        SIDE_DOOR = 2;
        LEFT = 1;
        RIGHT = 2;
        iToggleRocketSide = LEFT;
        iToggleBombSide = LEFT;
        lWeaponTime = System.currentTimeMillis();
        weaponFireMode = 2;
        weaponReleaseDelay = 33L;
        rocketHookSelected = 2;
        rocketNameSelected = null;
        FlapStage = null;
        VarWingStage = null;
        FlapStageText = null;
        Sensitivity = 1.0F;
        PowerControl = 0.0F;
        GearControl = 0.0F;
        wingControl = 0.0F;
        cockpitDoorControl = 0.0F;
        arrestorControl = 0.0F;
        FlapsControl = 0.0F;
        AileronControl = 0.0F;
        ElevatorControl = 0.0F;
        RudderControl = 0.0F;
        BrakeControl = 0.0F;
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
        WeaponControl = new boolean[21];
        saveWeaponControl = new boolean[4];
        bHasGearControl = true;
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
        PowerControlArr = new float[8];
        StepControlArr = new float[8];
        FM = flightmodelmain;
        for(int i = 0; i < 8; i++)
            PowerControlArr[i] = 0.0F;

        for(int j = 0; j < 8; j++)
            StepControlArr[j] = 1.0F;

        bHasDragChuteControl = false;
        bHasRefuelControl = false;
        DiffBrakesType = 0;
        BrakeRightControl = 0.0F;
        BrakeLeftControl = 0.0F;
        nFlapStages = -1;
        FlapStageMax = -1F;
        FlapStage = null;
        bHasVarWingControl = false;
        bHasVarIncidence = false;
        VarWingControl = 0.0F;
        nVarWingStages = -1;
        VarWingStageMax = -1F;
        VarWingStage = null;
        bHasStabilizerControl = false;
        DragChuteControl = 0.0F;
        RefuelControl = 0.0F;
        dvDragchute = 0.5F;
        dvRefuel = 0.5F;
        dvVarWing = 0.5F;
        bHasBlownFlaps = false;
        BlownFlapsControl = 0.0F;
        dvBlownFlaps = 0.5F;
        bNoCarrierCanopyOpen = false;
        bHasSideDoor = false;
        FlapsControlSwitch = 0;
        bHasFlapsControlSwitch = false;
        FlapStageText = null;
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
        BayDoorControl = controls.BayDoorControl;
        AirBrakeControl = controls.AirBrakeControl;
        dvGear = controls.dvGear;
        dvWing = controls.dvWing;
        dvCockpitDoor = controls.dvCockpitDoor;
        dvAirbrake = controls.dvAirbrake;
        dvDragchute = controls.dvDragchute;
        dvRefuel = controls.dvRefuel;
        dvVarWing = controls.dvVarWing;
        dvBlownFlaps = controls.dvBlownFlaps;
        FlapsControlSwitch = controls.FlapsControlSwitch;
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
        DragChuteControl = 0.0F;
        RefuelControl = 0.0F;
        VarWingControl = VarWing = 0.0F;
        BlownFlapsControl = BlownFlaps = 0.0F;
        FlapsControlSwitch = 0;
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
        BrakeRightControl = FMMath.interpolate(BrakeRightControl, controls.BrakeRightControl, f);
        BrakeLeftControl = FMMath.interpolate(BrakeLeftControl, controls.BrakeLeftControl, f);
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

    public float getDragChute()
    {
        return dragChute;
    }

    public float getRefuel()
    {
        return refuel;
    }

    public float getVarWing()
    {
        return VarWing;
    }

    public float getBlownFlaps()
    {
        return BlownFlaps;
    }

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
            return j == 0 ? dvGearL : dvGearR;

        case 5: // '\005'
            return j == 0 ? dvGearL * 15F : dvGearR * 15F;

        case 6: // '\006'
            return j == 0 ? dvGearL * 5F : dvGearR * 5F;

        case 8: // '\b'
            return j == 0 ? dvGearL : dvGearR;

        case 4: // '\004'
            if(j == 0)
                return dvGear * 0.4F;
            else
                return dvGear * 0.6F;

        case 7: // '\007'
            return j == 0 ? dvGearL : dvGearR;

        case 3: // '\003'
            return j == 0 ? dvGearL : dvGearR;

        case 2: // '\002'
            return j == 0 ? dvGearL : dvGearR;

        case 10: // '\n'
            return j == 0 ? dvGearL * 0.2F : dvGearR * 0.1F;

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
                for(int l = 0; l < enginesinterface.getNum(); l++)
                {
                    PowerControlArr[l] = clamp0115(PowerControlArr[l]);
                    enginesinterface.engines[l].setControlThrottle(PowerControlArr[l]);
                    if(PowerControlArr[l] > f6)
                        f6 = PowerControlArr[l];
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
            enginesinterface.setAfterburnerControl(afterburnerControl);
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
            float f10 = FM.AS.gearStates[2];
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
            if(f10 > 0.0F)
                gearC = filter(f, f10, gearC, 999.9F, getDamMovSpd(FM.AS.gearDamType[2], 2));
            else
            if(f10 < 0.0F)
                gearC = filter(f, Math.min(GearControl, Math.abs(f10)), gearC, 999.9F, getDamMovSpd(FM.AS.gearDamType[2], 2));
            else
                gearC = filter(f, GearControl, gearC, 999.9F, dvGearC);
        }
        if(bHasAirBrakeControl || flag1)
            airBrake = filter(f, AirBrakeControl, airBrake, 999.9F, dvAirbrake);
        if(bHasDragChuteControl || flag1)
            dragChute = filter(f, DragChuteControl, dragChute, 999.9F, dvDragchute);
        if(bHasRefuelControl || flag1)
            refuel = filter(f, RefuelControl, refuel, 999.9F, dvRefuel);
        if(bHasVarWingControl || flag1)
        {
            VarWingControl = clamp01(VarWingControl);
            VarWing = filter(f, VarWingControl, VarWing, 999.9F, dvVarWing);
        }
        if(bHasBlownFlaps || flag1)
        {
            BlownFlapsControl = clamp01(BlownFlapsControl);
            BlownFlaps = filter(f, BlownFlapsControl, BlownFlaps, 999.9F, dvBlownFlaps);
        }
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
        if(StabilizerControl)
        {
            AileronControl = -0.2F * FM.Or.getKren() - 2.0F * (float)FM.getW().x;
            tmpV3d.set(FM.Vwld);
            tmpV3d.normalize();
            float f8 = (float)(-500D * (tmpV3d.z - 0.001D));
            if(f8 > 0.8F)
                f8 = 0.8F;
            if(f8 < -0.8F)
                f8 = -0.8F;
            ElevatorControl = (f8 - 0.2F * FM.Or.getTangage() - 0.05F * FM.AOA) + 25F * (float)FM.getW().y;
            RudderControl = -0.2F * FM.AOS + 20F * (float)FM.getW().z;
        }
        if(bHasAileronControl || flag1)
        {
            trimAileron = filter(f, trimAileronControl, trimAileron, 999.9F, 0.25F);
            AileronControl = clamp11(AileronControl);
            tmpF = clampA(AileronControl, f2);
            Ailerons = filter(f, (1.0F + (trimAileron * tmpF > 0.0F ? -1F : 1.0F) * Math.abs(trimAileron)) * tmpF + trimAileron, Ailerons, 0.2F * (1.0F + 0.3F * Math.abs(AileronControl)), 0.025F);
        }
        if(bHasElevatorControl || flag1)
        {
            trimElevator = filter(f, trimElevatorControl, trimElevator, 999.9F, 0.25F);
            ElevatorControl = clamp11(ElevatorControl);
            tmpF = clampA(ElevatorControl, f4);
            Ev = filter(f, (1.0F + (trimElevator * tmpF > 0.0F ? -1F : 1.0F) * Math.abs(trimElevator)) * tmpF + trimElevator, Ev, 0.3F * (1.0F + 0.3F * Math.abs(ElevatorControl)), 0.022F);
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
            Rudder = filter(f, (1.0F + (trimRudder * tmpF > 0.0F ? -1F : 1.0F) * Math.abs(trimRudder)) * tmpF + trimRudder, Rudder, 0.35F * (1.0F + 0.3F * Math.abs(RudderControl)), 0.025F);
        }
        BrakeControl = clamp01(BrakeControl);
        if(bHasBrakeControl || flag1)
        {
            if(BrakeControl > Brake)
                Brake = Brake + 0.3F * f;
            else
                Brake = BrakeControl;
        } else
        {
            Brake = 0.0F;
        }
        if(bHasBrakeControl || flag1)
        {
            if(BrakeRightControl > BrakeRight)
                BrakeRight = BrakeRight + 0.3F * f;
            else
                BrakeRight = BrakeRightControl;
        } else
        {
            BrakeRight = 0.0F;
        }
        if(bHasBrakeControl || flag1)
        {
            if(BrakeLeftControl > BrakeLeft)
                BrakeLeft = BrakeLeft + 0.3F * f;
            else
                BrakeLeft = BrakeLeftControl;
        } else
        {
            BrakeLeft = 0.0F;
        }
        if(tick == Time.tickCounter())
            return;
        tick = Time.tickCounter();
        CTL = (byte)((((byte)(GearControl > 0.5F ? 1 : 0))) | (FlapsControl < 0.25F ? ((byte)(FlapsControl > 0.1F ? 2 : 0)) : (byte)(FlapsControl < 0.66F ? 4 : 6)) | (BrakeControl > 0.2F ? 8 : 0) | (RadiatorControl > 0.5F ? 0x10 : 0) | (BayDoorControl > 0.5F ? 0x20 : 0) | (AirBrakeControl > 0.5F ? 0x40 : 0));
        WCT &= 0xfc;
        TWCT &= 0xfc;
        int k = 0;
        for(int i1 = 1; k < WeaponControl.length && i1 < 256; i1 <<= 1)
        {
            if(WeaponControl[k])
            {
                WCT |= i1;
                TWCT |= i1;
            }
            k++;
        }

        for(int j1 = 0; j1 < 4; j1++)
            saveWeaponControl[j1] = WeaponControl[j1];

        long l1 = weaponReleaseDelay;
        if((double)Time.speed() == 0.5D)
            l1 *= 2L;
        else
        if((double)Time.speed() == 0.25D)
            l1 *= 4L;
        else
            l1 /= (long)Time.speed();
        for(int k1 = 0; k1 < Weapons.length; k1++)
        {
            if(Weapons[k1] == null)
                continue;
            switch(k1)
            {
            case 2: // '\002'
            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
                int i2 = WeaponControl[k1] ? 1 : 0;
                if(i2 == 0)
                    continue;
                if(bDropWithPlayer)
                {
                    bDropWithMe = true;
                    bDropWithPlayer = false;
                }
                if(System.currentTimeMillis() <= lWeaponTime + l1)
                    continue;
                int j2 = -1;
                for(int l2 = 0; l2 < Weapons[k1].length; l2 += 2)
                {
                    if((Weapons[k1][l2] instanceof FuelTankGun) || !Weapons[k1][l2].haveBullets())
                        continue;
                    if(Weapons[k1][l2].getHookName().startsWith("_BombSpawn") && ((Aircraft)FM.actor).needsOpenBombBay())
                    {
                        if(bHasBayDoorControl && isPlayers((Aircraft)FM.actor))
                        {
                            if(BayDoorControl < 0.9F)
                                break;
                        } else
                        {
                            BayDoorControl = 1.0F;
                        }
                        Weapons[k1][l2].shots(i2);
                    } else
                    {
                        if(Weapons[k1][l2].getHookName().startsWith("_InternalRock"))
                            if(bHasBayDoorControl && isPlayers((Aircraft)FM.actor))
                            {
                                if(BayDoorControl < 0.9F)
                                    break;
                            } else
                            {
                                BayDoorControl = 1.0F;
                            }
                        if(weaponFireMode == 2 || (Weapons[k1][l2] instanceof RocketGun4andHalfInch) || (Weapons[k1][l2] instanceof RocketGun) && iToggleRocketSide == LEFT || (Weapons[k1][l2] instanceof RocketBombGun) && iToggleRocketSide == LEFT || (Weapons[k1][l2] instanceof BombGun) && iToggleBombSide == LEFT)
                            Weapons[k1][l2].shots(i2);
                        if(Weapons[k1][l2].getHookName().startsWith("_BombSpawn") && ((Aircraft)FM.actor).needsOpenBombBay())
                            if(bHasBayDoorControl && isPlayers((Aircraft)FM.actor))
                            {
                                if(BayDoorControl < 0.9F)
                                    break;
                            } else
                            {
                                BayDoorControl = 1.0F;
                            }
                    }
                    if((Weapons[k1][l2] instanceof BombGun) && weaponFireMode == 0 || (Weapons[k1][l2] instanceof RocketGun) && !(Weapons[k1][l2] instanceof MissileGun) && weaponFireMode == 0)
                    {
                        Weapons[k1][l2].shots(i2);
                        continue;
                    }
                    if((!(Weapons[k1][l2] instanceof BombGun) || ((BombGun)Weapons[k1][l2]).isCassette() || weaponFireMode == 0) && (!(Weapons[k1][l2] instanceof RocketGun) || ((RocketGun)Weapons[k1][l2]).isCassette() || weaponFireMode == 0))
                        continue;
                    j2 = l2;
                    lWeaponTime = System.currentTimeMillis();
                    break;
                }

                for(int i3 = 1; i3 < Weapons[k1].length; i3 += 2)
                {
                    if((Weapons[k1][i3] instanceof FuelTankGun) || !Weapons[k1][i3].haveBullets())
                        continue;
                    if(Weapons[k1][i3].getHookName().startsWith("_BombSpawn") && ((Aircraft)FM.actor).needsOpenBombBay())
                    {
                        if(bHasBayDoorControl && isPlayers((Aircraft)FM.actor))
                        {
                            if(BayDoorControl < 0.9F)
                                break;
                        } else
                        {
                            BayDoorControl = 1.0F;
                        }
                        Weapons[k1][i3].shots(i2);
                    } else
                    {
                        if(Weapons[k1][i3].getHookName().startsWith("_InternalRock"))
                            if(bHasBayDoorControl && isPlayers((Aircraft)FM.actor))
                            {
                                if(BayDoorControl < 0.9F)
                                    break;
                            } else
                            {
                                BayDoorControl = 1.0F;
                            }
                        if(weaponFireMode == 2 || (Weapons[k1][i3] instanceof RocketGun4andHalfInch) || (Weapons[k1][i3] instanceof RocketGun) && iToggleRocketSide == RIGHT || (Weapons[k1][i3] instanceof RocketBombGun) && iToggleRocketSide == LEFT || (Weapons[k1][i3] instanceof BombGun) && iToggleBombSide == RIGHT)
                            Weapons[k1][i3].shots(i2);
                    }
                    if((Weapons[k1][i3] instanceof BombGun) && weaponFireMode == 0 || (Weapons[k1][i3] instanceof RocketGun) && !(Weapons[k1][i3] instanceof MissileGun) && weaponFireMode == 0)
                    {
                        Weapons[k1][i3].shots(i2);
                        continue;
                    }
                    if((!(Weapons[k1][i3] instanceof BombGun) || ((BombGun)Weapons[k1][i3]).isCassette() || weaponFireMode == 0) && (!(Weapons[k1][i3] instanceof RocketGun) || ((RocketGun)Weapons[k1][i3]).isCassette() || weaponFireMode == 0) && (!(Weapons[k1][i3] instanceof RocketBombGun) || ((RocketBombGun)Weapons[k1][i3]).isCassette() || weaponFireMode == 0))
                        continue;
                    j2 = i3;
                    lWeaponTime = System.currentTimeMillis();
                    break;
                }

                if(j2 != -1)
                    if(Weapons[k1][j2] instanceof BombGun)
                    {
                        if(iToggleBombSide == LEFT)
                            iToggleBombSide = RIGHT;
                        else
                            iToggleBombSide = LEFT;
                    } else
                    if(!(Weapons[k1][j2] instanceof RocketGun4andHalfInch))
                        if(iToggleRocketSide == LEFT)
                            iToggleRocketSide = RIGHT;
                        else
                            iToggleRocketSide = LEFT;
                if(weaponFireMode == 2)
                    WeaponControl[k1] = false;
                break;

            default:
                boolean flag2 = false;
                for(int k2 = 0; k2 < Weapons[k1].length; k2++)
                {
                    Weapons[k1][k2].shots(WeaponControl[k1] ? -1 : 0);
                    flag2 = flag2 || Weapons[k1][k2].haveBullets();
                }

                if(WeaponControl[k1] && !flag2 && FM.isPlayers())
                    ForceFeedback.fxTriggerShake(k1, false);
                break;
            }
        }

    }

    private boolean isPlayers(Aircraft aircraft)
    {
        if(aircraft.FM.isPlayers() && (aircraft.FM instanceof RealFlightModel) && ((RealFlightModel)aircraft.FM).isRealMode())
            return true;
        else
            return aircraft.isNetPlayer();
    }

    public float getWeaponMass()
    {
        int i = Weapons.length;
        float f = 0.0F;
        for(int j = 0; j < i; j++)
        {
            if(Weapons[j] == null)
                continue;
            int k = Weapons[j].length;
            for(int l = 0; l < k; l++)
            {
                BulletEmitter bulletemitter = Weapons[j][l];
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
        int j = Weapons[i].length;
        int k;
        int l = k = 0;
        for(; k < j; k++)
        {
            BulletEmitter bulletemitter = Weapons[i][k];
            if(bulletemitter != null && !(bulletemitter instanceof FuelTankGun))
                l += bulletemitter.countBullets();
        }

        return l;
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
        int i = 0;
        for(int j = 0; j < Weapons.length; j++)
        {
            if(Weapons[j] == null)
                continue;
            for(int k = 0; k < Weapons[j].length; k++)
                if(Weapons[j][k] instanceof FuelTankGun)
                    i++;

        }

        FuelTank afueltank[] = new FuelTank[i];
        int l;
        for(int i1 = l = 0; i1 < Weapons.length; i1++)
        {
            if(Weapons[i1] == null)
                continue;
            for(int j1 = 0; j1 < Weapons[i1].length; j1++)
                if(Weapons[i1][j1] instanceof FuelTankGun)
                    afueltank[l++] = ((FuelTankGun)Weapons[i1][j1]).getFuelTank();

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
            if(i != 3 && WeaponControl[i])
                return true;

        return false;
    }

    public float getBrakeRight()
    {
        return BrakeRight;
    }

    public float getBrakeLeft()
    {
        return BrakeLeft;
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

    public void doSetWeaponFireMode(int i)
    {
        weaponFireMode = i;
    }

    public void toggleWeaponFireMode(int i)
    {
        if(Weapons[2] == null && Weapons[3] == null)
            return;
        switch(weaponFireMode)
        {
        case 0: // '\0'
            doSetWeaponFireMode(1);
            HUD.log(i, "Single Fire Selected");
            break;

        default:
            doSetWeaponFireMode(2);
            HUD.log(i, "Default Fire Selected");
            break;

        case 2: // '\002'
            doSetWeaponFireMode(0);
            HUD.log(i, "Full Salvo Selected");
            break;
        }
    }

    public void doSetWeaponReleaseDelay(long l)
    {
        weaponReleaseDelay = l;
    }

    public void doChangeWeaponReleaseDelay(long l)
    {
        weaponReleaseDelay += l;
    }

    public void toggleWeaponReleaseDelay(int i)
    {
        if(Weapons[2] == null && Weapons[3] == null)
            return;
        if(weaponReleaseDelay == 33L)
            doChangeWeaponReleaseDelay(92L);
        else
        if(weaponReleaseDelay >= 125L)
            if(weaponReleaseDelay >= 250L)
                doChangeWeaponReleaseDelay(250L);
            else
                doChangeWeaponReleaseDelay(125L);
        if(weaponReleaseDelay > 1000L)
            doSetWeaponReleaseDelay(33L);
        HUD.log(i, "Release Delay: " + (float)weaponReleaseDelay / 1000F + " sec");
    }

    public float Sensitivity;
    public float PowerControl;
    public boolean afterburnerControl;
    public float GearControl;
    public float wingControl;
    public float cockpitDoorControl;
    public float arrestorControl;
    public float FlapsControl;
    public float AileronControl;
    public float ElevatorControl;
    public float RudderControl;
    public float BrakeControl;
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
    public boolean WeaponControl[];
    public boolean saveWeaponControl[];
    public boolean bHasGearControl;
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
    private float Ailerons;
    private float Elevators;
    private float Rudder;
    private float Brake;
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
    public float PowerControlArr[];
    public float StepControlArr[];
    private FlightModelMain FM;
    private static float tmpF;
    public boolean bDropWithPlayer;
    public Aircraft dropWithPlayer;
    boolean bDropWithMe;
    private static Vector3d tmpV3d = new Vector3d();
    public int DiffBrakesType;
    public float BrakeRightControl;
    public float BrakeLeftControl;
    private float BrakeRight;
    private float BrakeLeft;
    public boolean bHasDragChuteControl;
    public float DragChuteControl;
    public float dvDragchute;
    private float dragChute;
    public boolean bHasRefuelControl;
    public float RefuelControl;
    public float dvRefuel;
    private float refuel;
    public boolean bHasBayDoors;
    private float fSaveCockpitDoor;
    private float fSaveCockpitDoorControl;
    private float fSaveSideDoor;
    private float fSaveSideDoorControl;
    public boolean bMoveSideDoor;
    private int SIDE_DOOR;
    public int LEFT;
    public int RIGHT;
    public int iToggleRocketSide;
    public int iToggleBombSide;
    public long lWeaponTime;
    public int weaponFireMode;
    public long weaponReleaseDelay;
    public int rocketHookSelected;
    public String rocketNameSelected;
    public int nFlapStages;
    public float FlapStageMax;
    public float FlapStage[];
    public float VarWingControl;
    private float VarWing;
    public int nVarWingStages;
    public float VarWingStageMax;
    public float VarWingStage[];
    public float dvVarWing;
    public boolean bHasVarWingControl;
    public boolean bHasVarIncidence;
    public boolean bHasStabilizerControl;
    public boolean bHasBlownFlaps;
    public float BlownFlapsControl;
    private float BlownFlaps;
    public float dvBlownFlaps;
    public String BlownFlapsType;
    public boolean bNoCarrierCanopyOpen;
    public boolean bHasSideDoor;
    public int FlapsControlSwitch;
    public boolean bHasFlapsControlSwitch;
    public String FlapStageText[];

}
