// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/10/2015 11:54:36 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitMeteor.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit

public class CockpitMeteor extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            setNew.throttlel = (10F * setOld.throttlel + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle()) / 11F;
            float f = waypointAzimuth();
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f - 90F);
                setOld.waypointAzimuth.setDeg(f - 90F);
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
            setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
            if(cockpitDimControl)
            {
                if(setNew.dimPosition > 0.0F)
                    setNew.dimPosition = setOld.dimPosition - 0.05F;
            } else
            if(setNew.dimPosition < 1.0F)
                setNew.dimPosition = setOld.dimPosition + 0.05F;
            setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
            setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float altimeter;
        float throttlel;
        float throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float vspeed;
        float dimPosition;
        float beaconDirection;
        float beaconRange;

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


    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(30F);
    }

    public CockpitMeteor()
    {
        super("3DO/Cockpit/Meteor/hier.him", "he111");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictGear = 0.0F;
        super.cockpitNightMats = (new String[] {
            "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", 
            "petitfla", "turnbank"
        });
        setNightMats(false);
        setNew.dimPosition = 1.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        Cockpit.xyz[0] = -cvt(((FlightModelMain) (super.fm)).CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
        super.mesh.chunkSetLocate("canopy", Cockpit.xyz, Cockpit.ypr);
        if(super.fm.isTick(44, 0))
        {
            super.mesh.chunkVisible("Z_GearLGreen1", ((FlightModelMain) (super.fm)).CT.getGear() == 1.0F && ((FlightModelMain) (super.fm)).Gears.lgear);
            super.mesh.chunkVisible("Z_GearRGreen1", ((FlightModelMain) (super.fm)).CT.getGear() == 1.0F && ((FlightModelMain) (super.fm)).Gears.rgear);
            super.mesh.chunkVisible("Z_GearCGreen1", ((FlightModelMain) (super.fm)).CT.getGear() == 1.0F);
            super.mesh.chunkVisible("Z_GearLRed1", ((FlightModelMain) (super.fm)).CT.getGear() == 0.0F || ((FlightModelMain) (super.fm)).Gears.isAnyDamaged());
            super.mesh.chunkVisible("Z_GearRRed1", ((FlightModelMain) (super.fm)).CT.getGear() == 0.0F || ((FlightModelMain) (super.fm)).Gears.isAnyDamaged());
            super.mesh.chunkVisible("Z_GearCRed1", ((FlightModelMain) (super.fm)).CT.getGear() == 0.0F);
            super.mesh.chunkVisible("Z_MachLamp", super.fm.getSpeed() / Atmosphere.sonicSpeed((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z) > 0.8F);
            super.mesh.chunkVisible("Z_EngineFireLampR", ((FlightModelMain) (super.fm)).AS.astateEngineStates[1] > 2);
            super.mesh.chunkVisible("Z_EngineFireLampL", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
            super.mesh.chunkVisible("Z_FuelLampV", ((FlightModelMain) (super.fm)).M.fuel < 200F);
            super.mesh.chunkVisible("Z_FuelLampIn", ((FlightModelMain) (super.fm)).M.fuel < 200F);
        }
        resetYPRmodifier();
        Cockpit.xyz[1] = pictGear = 0.85F * pictGear + 0.011F * ((FlightModelMain) (super.fm)).CT.GearControl;
        super.mesh.chunkSetLocate("Z_GearEin", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_FlapEin", 0.0F, 0.0F, -(((FlightModelMain) (super.fm)).CT.FlapsControl - 0.08F) * 115F);
        super.mesh.chunkSetAngles("Z_Columnbase", 8F * (pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (super.fm)).CT.ElevatorControl), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Column", 30F * (pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (super.fm)).CT.AileronControl), 0.0F, 0.0F);
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).CT.saveWeaponControl[0])
            Cockpit.xyz[2] = -0.0025F;
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).CT.saveWeaponControl[2] || ((FlightModelMain) (super.fm)).CT.saveWeaponControl[3])
            Cockpit.xyz[2] = -0.00325F;
        super.mesh.chunkSetAngles("Z_PedalStrut", 20F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_LeftPedal", -20F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RightPedal", -20F * ((FlightModelMain) (super.fm)).CT.getRudder(), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getControlThrottle(), 0.01F, 0.99F, 0.0F, 0.55F) / 5F;
        super.mesh.chunkSetLocate("Z_ThrottleL", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(((FlightModelMain) (super.fm)).EI.engines[1].getControlThrottle(), 0.01F, 0.99F, 0.0F, 0.55F) / 5F;
        super.mesh.chunkSetLocate("Z_ThrottleR", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeed()), 0.0F, 257.2222F, 0.0F, 10F), speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Speedometer2", floatindex(cvt(super.fm.getSpeedKMH(), 100F, 1000F, 1.0F, 10F), speedometerTruScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter2", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Altimeter1", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank1", ((FlightModelMain) (super.fm)).Or.getTangage(), 0.0F, ((FlightModelMain) (super.fm)).Or.getKren());
        super.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, 0.0F, -cvt(getBall(8D), -8F, 8F, 35F, -35F));
        w.set(super.fm.getW());
        ((FlightModelMain) (super.fm)).Or.transform(w);
        super.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, -cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, -48F, 48F));
        super.mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPML", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 10000F, 20000F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPML2", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPMR", cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 10000F, 20000F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPMR2", cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        if(useRealisticNavigationInstruments())
            super.mesh.chunkSetAngles("Z_Compass2", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        else
            super.mesh.chunkSetAngles("Z_Compass2", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_GasTempL", cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 20F, 95F, -150F, -25F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_GasTempR", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 20F, 95F, -150F, -25F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_FuelRemainV", floatindex(cvt(((FlightModelMain) (super.fm)).M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), fuelScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_FuelRemainIn", floatindex(cvt(((FlightModelMain) (super.fm)).M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), fuelScale), 0.0F, 0.0F);
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleDim()
    {
        super.cockpitDimControl = !super.cockpitDimControl;
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
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private float pictGear;
    private static final float speedometerScale[] = {
        0.0F, 15.5F, 76F, 153.5F, 234F, 304F, 372.5F, 440F, 504F, 566F, 
        630F
    };
    private static final float speedometerTruScale[] = {
        0.0F, 32.75F, 65.5F, 98.25F, 131F, 164F, 200F, 237F, 270.5F, 304F, 
        336F
    };
    private static final float variometerScale[] = {
        -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F
    };
    private static final float fuelScale[] = {
        0.0F, 11F, 31F, 57F, 84F, 103.5F
    };







}
