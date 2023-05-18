// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 12/10/2015 03:34:12 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitMig_15UTIi.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, Mig_15F

public class CockpitMig_15UTIi extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.9F * setOld.throttle + ((FlightModelMain) (fm)).CT.PowerControl * 0.1F;
                setNew.altimeter = fm.getAltitude();
                if(cockpitDimControl)
                {
                    if(setNew.dimPosition > 0.0F)
                        setNew.dimPosition = setOld.dimPosition - 0.05F;
                } else
                if(setNew.dimPosition < 1.0F)
                    setNew.dimPosition = setOld.dimPosition + 0.05F;
                float a = waypointAzimuth();
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(a - 90F);
                    setOld.waypointAzimuth.setDeg(a - 90F);
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), a - setOld.azimuth.getDeg(1.0F));
                }
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
                setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
                setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                if(((FlightModelMain) (fm)).EI.engines[0].getStage() < 6 && !((FlightModelMain) (fm)).CT.bHasAileronControl && !((FlightModelMain) (fm)).CT.bHasElevatorControl && !((FlightModelMain) (fm)).CT.bHasGearControl && !((FlightModelMain) (fm)).CT.bHasAirBrakeControl && !((FlightModelMain) (fm)).CT.bHasFlapsControl)
                {
                    if(setNew.stbyPosition3 > 0.0F)
                        setNew.stbyPosition3 = setOld.stbyPosition3 - 0.005F;
                } else
                if(setNew.stbyPosition3 < 1.0F)
                    setNew.stbyPosition3 = setOld.stbyPosition3 + 0.005F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttle;
        float dimPosition;
        float vspeed;
        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float beaconDirection;
        float beaconRange;
        float stbyPosition2;
        float stbyPosition3;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    public CockpitMig_15UTIi()
    {
        super("3DO/Cockpit/MiG-15/MiG_15UTIi.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictGear = 0.0F;
        pictMet1 = 0.0F;
        super.cockpitNightMats = (new String[] {
            "Gauges_01", "Gauges_02", "Gauges_03", "Gauges_04", "Gauges_05", "Gauges_06", "Gauges_08", "MiG-15_Compass"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(30F);
    }

    private float machNumber()
    {
        return ((Mig_15F)super.aircraft()).calculateMach();
    }

    public void reflectWorldToInstruments(float f)
    {
        Mig_15F Mig_15F = (Mig_15F)aircraft();
        if(Mig_15F.bChangedPit)
        {
            Mig_15F Mig_15F_3_ = (Mig_15F)aircraft();
            Mig_15F.bChangedPit = false;
        }
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.01F, 0.99F, -0.49F, 0.0F);
        Cockpit.xyz[2] = cvt(((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.01F, 0.99F, -0.065F, 0.0F);
        Cockpit.ypr[2] = cvt(((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.99F, 0.01F, -3F, 0.0F);
        super.mesh.chunkSetLocate("CanopyOpen01", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen02", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen03", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen04", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen05", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen06", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen07", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen08", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("CanopyOpen09", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("GearHandle", 0.0F, 0.0F, 50F * (pictGear = 0.82F * pictGear + 0.18F * ((FlightModelMain) (super.fm)).CT.GearControl));
        super.mesh.chunkSetAngles("Z_FlapsLever", -35F * ((FlightModelMain) (super.fm)).CT.FlapsControl, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Gas1a", 0.0F, cvt(((FlightModelMain) (super.fm)).M.fuel / 2.0F, 0.0F, 700F, 0.0F, 180F), 0.0F);
        super.mesh.chunkSetAngles("Z_Amp", 0.0F, cvt(interp(setNew.stbyPosition2, setOld.stbyPosition2, f), 0.0F, 1.0F, 0.0F, 40F), 0.0F);
        super.mesh.chunkSetAngles("Z_HydroPressure", 0.0F, cvt(interp(setNew.stbyPosition3, setOld.stbyPosition3, f), 0.0F, 1.0F, 0.0F, 190F), 0.0F);
        w.set(super.fm.getW());
        ((FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_Turn1a", 0.0F, cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
        super.mesh.chunkSetAngles("Z_Slide1a", 0.0F, cvt(getBall(8D), -8F, 8F, -24F, 24F), 0.0F);
        super.mesh.chunkSetAngles("Z_Slide1a2", 0.0F, cvt(getBall(8D), -8F, 8F, -20F, 20F), 0.0F);
        if(useRealisticNavigationInstruments())
            super.mesh.chunkSetAngles("Z_Compass2", 90F + setNew.azimuth.getDeg(f * 0.1F) + setNew.beaconDirection, 0.0F, 0.0F);
        else
            super.mesh.chunkSetAngles("Z_Compass2", 90F + setNew.azimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_horizont1a", 0.0F, -((FlightModelMain) (super.fm)).Or.getKren(), 0.0F);
        super.mesh.chunkSetAngles("Z_GasPrs1a", 0.0F, cvt(((FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3050F, 0.0F, 4F), 0.0F, 5F, -45F, 225F), 0.0F);
        super.mesh.chunkSetAngles("Z_GasPrs2a", 0.0F, cvt(((FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3050F, 0.0F, 4F), 0.0F, 5F, -180F, 0.0F), 0.0F);
        super.mesh.chunkSetAngles("Z_TOilOut1a", 0.0F, cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 110F, 5F, 300F), 0.0F);
        super.mesh.chunkSetAngles("Z_OilPrs1a", 0.0F, cvt(1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 15F, -155F, -360F), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(((FlightModelMain) (super.fm)).CT.getRudder(), -1F, 1.0F, -0.035F, 0.035F);
        super.mesh.chunkSetLocate("Pedal_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        super.mesh.chunkSetLocate("Pedal_R", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(((FlightModelMain) (super.fm)).Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
        super.mesh.chunkSetLocate("Z_horizont1b", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Stick01", 0.0F, 0.0F, 10F * (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl));
        super.mesh.chunkSetAngles("Stick02", 0.0F, 10F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 0.0F);
        super.mesh.chunkSetAngles("Stick03", 0.0F, 10F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 0.0F);
        super.mesh.chunkSetAngles("Stick04", 0.0F, 10F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 0.0F);
        super.mesh.chunkSetAngles("Stick05", 0.0F, 10F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 0.0F);
        super.mesh.chunkSetAngles("Stick06", 0.0F, 10F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 0.0F);
        super.mesh.chunkSetAngles("Stick07", 0.0F, 10F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 0.0F);
        super.mesh.chunkSetAngles("Z_RPM1", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3480F, 90F, 625F), 0.0F, 0.0F);
        pictMet1 = 0.96F * pictMet1 + 0.04F * (0.6F * ((FlightModelMain) (super.fm)).EI.engines[0].getThrustOutput() * ((FlightModelMain) (super.fm)).EI.engines[0].getControlThrottle() * (((FlightModelMain) (super.fm)).EI.engines[0].getStage() != 6 ? 0.02F : 1.0F));
        super.mesh.chunkSetAngles("Z_FuelPress1", cvt(((FlightModelMain) (super.fm)).M.fuel <= 1.0F ? 0.0F : 0.55F, 0.0F, 1.0F, 0.0F, 284F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_ExstT1", 0.0F, cvt(((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut, 0.0F, 1200F, 0.0F, 112F), 0.0F);
        super.mesh.chunkSetAngles("Z_Azimuth1", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass1", 0.0F, -setNew.azimuth.getDeg(f * 0.1F), 0.0F);
        super.mesh.chunkSetAngles("Z_Oxypres1", 142.5F, 0.0F, 0.0F);
        if(machNumber() < 0.95F || machNumber() > 1.0F)
        {
            super.mesh.chunkSetAngles("Z_Alt_Km", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 60000F, 0.0F, 2160F), 0.0F);
            super.mesh.chunkSetAngles("Z_Alt_M", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 60000F, 0.0F, 21600F), 0.0F);
            super.mesh.chunkSetAngles("Z_Alt2_Km", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 12000F, 225F, 495F), 0.0F);
            super.mesh.chunkSetAngles("Z_Alt3_Km", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 12000F, 295F, 395F), 0.0F);
            super.mesh.chunkSetAngles("Z_Speed", 0.0F, cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 1200F, 0.0F, 360F), 0.0F);
            super.mesh.chunkSetAngles("Z_AirFlow", 0.0F, cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH()), 0.0F, 800F, -90F, 160F), 0.0F);
            super.mesh.chunkSetAngles("Z_Climb", 0.0F, cvt(setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        }
        super.mesh.chunkSetAngles("Z_Vibrations", 0.0F, floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4500F, 5.5F, 14F), rpmScale), 0.0F);
        super.mesh.chunkSetAngles("Z_Throttle", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3480F, -110F, 170F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkVisible("FlareGearUp_R", ((FlightModelMain) (super.fm)).CT.getGear() < 0.01F || !((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("FlareGearUp_L", ((FlightModelMain) (super.fm)).CT.getGear() < 0.01F || !((FlightModelMain) (super.fm)).Gears.lgear);
        super.mesh.chunkVisible("FlareGearUp_C", ((FlightModelMain) (super.fm)).CT.getGear() < 0.01F);
        super.mesh.chunkVisible("FlareGearDn_R", ((FlightModelMain) (super.fm)).CT.getGear() > 0.99F && ((FlightModelMain) (super.fm)).Gears.rgear);
        super.mesh.chunkVisible("FlareGearDn_L", ((FlightModelMain) (super.fm)).CT.getGear() > 0.99F && ((FlightModelMain) (super.fm)).Gears.lgear);
        super.mesh.chunkVisible("FlareGearDn_C", ((FlightModelMain) (super.fm)).CT.getGear() > 0.99F);
        super.mesh.chunkVisible("FlareFuel", ((FlightModelMain) (super.fm)).M.fuel < 296.1F);
        super.mesh.chunkVisible("FlareTankFuel", ((FlightModelMain) (super.fm)).M.fuel < 1215F && ((FlightModelMain) (super.fm)).M.fuel > 1207F);
        super.mesh.chunkVisible("FlareFire", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
        resetYPRmodifier();
        Cockpit.ypr[0] = interp(setNew.throttle, setOld.throttle, f) * 65F;
        Cockpit.xyz[2] = cvt(Cockpit.ypr[0], 7.5F, 11.5F, 0.0F, 0.0F);
        super.mesh.chunkSetLocate("Throttle", Cockpit.xyz, Cockpit.ypr);
        float f_5_ = World.Rnd().nextFloat(0.87F, 1.04F);
        if(((FlightModelMain) (super.fm)).CT.getCockpitDoor() == 1.0F)
        {
            super.mesh.chunkVisible("V_G", false);
            super.mesh.chunkVisible("V_D", false);
        } else
        {
            super.mesh.chunkVisible("V_G", true);
            super.mesh.chunkVisible("V_D", true);
        }
        if(((FlightModelMain) (super.fm)).CT.getCockpitDoor() == 1.0F && !((FlightModelMain) (super.fm)).AS.bIsAboutToBailout)
            super.mesh.chunkVisible("CanopyOpen07", true);
        else
            super.mesh.chunkVisible("CanopyOpen07", false);
        if(((FlightModelMain) (super.fm)).CT.getCockpitDoor() < 1.0F && ((FlightModelMain) (super.fm)).CT.bHasCockpitDoorControl)
            super.mesh.chunkVisible("CanopyOpen06", true);
        else
            super.mesh.chunkVisible("CanopyOpen06", false);
        if(((FlightModelMain) (super.fm)).AS.bIsAboutToBailout)
        {
            super.mesh.chunkVisible("CanopyOpen01", false);
            super.mesh.chunkVisible("CanopyOpen02", false);
            super.mesh.chunkVisible("CanopyOpen03", false);
            super.mesh.chunkVisible("CanopyOpen04", false);
            super.mesh.chunkVisible("CanopyOpen05", false);
            super.mesh.chunkVisible("CanopyOpen08", false);
            super.mesh.chunkVisible("CanopyOpen09", false);
            super.mesh.chunkVisible("XGlassDamage4", false);
        }
        if(((FlightModelMain) (super.fm)).CT.BayDoorControl == 1.0F)
        {
            super.mesh.chunkVisible("Stick04", false);
            super.mesh.chunkVisible("Stick05", true);
        }
        if(((FlightModelMain) (super.fm)).CT.BayDoorControl == 0.0F)
        {
            super.mesh.chunkVisible("Stick04", true);
            super.mesh.chunkVisible("Stick05", false);
        }
    }

    public void toggleDim()
    {
        super.cockpitDimControl = !super.cockpitDimControl;
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("Instruments", false);
            super.mesh.chunkVisible("InstrumentsD", true);
            super.mesh.chunkVisible("Z_Z_MASK", false);
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage2", true);
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
            super.mesh.chunkVisible("Z_Speed", false);
            super.mesh.chunkVisible("Z_Compass1", false);
            super.mesh.chunkVisible("Z_Azimuth1", false);
            super.mesh.chunkVisible("Z_GasPrs1a", false);
            super.mesh.chunkVisible("Z_GasPrs2a", false);
            super.mesh.chunkVisible("Z_Alt_Km", false);
            super.mesh.chunkVisible("Z_Alt_M", false);
            super.mesh.chunkVisible("Z_Turn", false);
            super.mesh.chunkVisible("Z_Turn1a", false);
            super.mesh.chunkVisible("Z_Slide1a", false);
            super.mesh.chunkVisible("Z_RPM1", false);
        }
        if((((FlightModelMain) (super.fm)).AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage2", true);
        }
        if(((((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x80) != 0 || (((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x40) != 0) && (((FlightModelMain) (super.fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage2", true);
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
        }
        if(((((FlightModelMain) (super.fm)).AS.astateCockpitState & 8) != 0 || (((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x10) != 0) && (((FlightModelMain) (super.fm)).AS.astateCockpitState & 0x20) != 0)
            retoggleLight();
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(super.cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictGear;
    private float pictMet1;
    private static final float rpmScale[] = {
        0.0F, 8F, 23.5F, 40F, 58.5F, 81F, 104.5F, 130.2F, 158.5F, 187F, 
        217.5F, 251.1F, 281.5F, 289.5F, 295.5F
    };

    static 
    {
        Property.set(CLASS.THIS(), "astatePilotIndx", 1);
    }






}
