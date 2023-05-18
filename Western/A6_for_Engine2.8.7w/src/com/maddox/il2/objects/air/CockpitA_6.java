
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.Time;


public class CockpitA_6 extends CockpitPilot
{
    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((A_6)aircraft()).startCockpitSounds();
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        stopSoundEffects();
        ((A_6)aircraft()).stopCockpitSounds();
        super.doFocusLeave();
    }


    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            float radioaltnow = fm.getAltitude() - (float)Engine.land().HQ_Air(fm.Loc.x, fm.Loc.y);
//            radioaltnow = radioaltnow / (float)Math.cos(Math.toRadians(Math.abs(fm.Or.getKren()))) / (float)Math.cos(Math.toRadians(Math.abs(fm.Or.getTangage())));
            setNew.radioaltimeter = (5F * setOld.radioaltimeter + radioaltnow) / 6F;
            setNew.throttlel = (10F * setOld.throttlel + fm.EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + fm.EI.engines[1].getControlThrottle()) / 11F;
            setNew.fuelpressl = (10F * setOld.fuelpressl + (fm.M.fuel <= 1.0F ? 0.0F : 56F * fm.EI.engines[0].getPowerOutput() * fm.EI.engines[0].getReadyness())) / 11F;
            setNew.fuelpressr = (10F * setOld.fuelpressr + (fm.M.fuel <= 1.0F ? 0.0F : 56F * fm.EI.engines[1].getPowerOutput() * fm.EI.engines[1].getReadyness())) / 11F;
            if(fm.getOverload() > setOld.accelloMax)
                setNew.accelloMax = fm.getOverload();
            if(fm.getOverload() < setOld.accelloMin)
                setNew.accelloMin = fm.getOverload();
            float f2 = waypointAzimuth();
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f2);
                setOld.waypointAzimuth.setDeg(f2);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                if(fm.AS.listenLorenzBlindLanding && fm.AS.isILSBL)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                    bHSIILS = true;
                    bHSIDL = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
                } else
                if(fm.AS.listenTACAN)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = 0.0F;
                    bHSITAC = true;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITGT = bHSIUHF = false;
                } else
                if(fm.AS.listenNDBeacon || fm.AS.listenYGBeacon)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = 0.0F;
                    bHSIUHF = true;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = false;
                } else
                {
                    setNew.hsiLoc = 0.0F;
                    setNew.ilsLoc = 0.0F;
                    setNew.ilsGS = 0.0F;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
                }
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f2);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f2 - setOld.azimuth.getDeg(0.1F) - 90F);
                bHSINAV = true;
                bHSIDL = bHSIILS = bHSIMAN = bHSITAC = bHSITGT = bHSIUHF = false;
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
            setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            if(cockpitDimControl)
            {
                if(setNew.dimPosition > 0.0F)
                    setNew.dimPosition = setOld.dimPosition - 0.05F;
            } else
            if(setNew.dimPosition < 1.0F)
                setNew.dimPosition = setOld.dimPosition + 0.05F;
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float altimeter;
        float radioaltimeter;
        float vspeed;
        float throttlel;
        float throttler;
        float fuelpressl;
        float fuelpressr;
        float accelloMax;
        float accelloMin;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float beaconDirection;
        float beaconRange;
        float hsiLoc;
        float ilsLoc;
        float ilsGS;
        float dimPosition;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
            accelloMax = 1.0F;
            accelloMin = 1.0F;
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

    public CockpitA_6()
    {
        super("3DO/Cockpit/A6E/A6E.him", "he111");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        tmpP = new Point3d();
        tmpV = new Vector3d();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bU4 = false;
        bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
        cockpitNightMats = (new String[] {
            "instr7", "Gauges_10", "Needles", "Needles2", "Needles2gr"
        });
        setNightMats(false);
        setNew.dimPosition = 1.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
        printCompassHeading = true;
    }

    public void removeCanopy()
    {
        mesh.chunkVisible("Blister", false);
        mesh.chunkVisible("Z_Holes2_D1", false);
        mesh.chunkVisible("Z_Holes1_D1", false);
    }

    public void reflectWorldToInstruments(float f)
    {
        float feetmultiplier = 3.28084F;
        float newAltimeterFeet = setNew.altimeter * feetmultiplier;
        float oldAltimeterFeet = setOld.altimeter * feetmultiplier;
        float newRadioAltimeterFeet = setNew.radioaltimeter * feetmultiplier;
        float oldRadioAltimeterFeet = setOld.radioaltimeter * feetmultiplier;
        float AOAunits = fm.getAOA() * 1.84F + 4.28F;
        Cockpit.xyz[0] = Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        if(bNeedSetUp)
        {
            reflectPlaneMats();

            mesh.chunkSetAngles("Z_Altimeter1m_1", cvt(standardbar / 10F, 0.0F, 4000F, 0.0F, 144000F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_Altimeter1m_10", cvt((float)Math.floor(standardbar / 100F), 0.0F, 400F, 0.0F, 14400F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_Altimeter1m_100", cvt((float)Math.floor(standardbar / 1000F), 0.0F, 40F, 0.0F, 1440F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_Altimeter1m_1000", cvt((float)Math.floor(standardbar / 10000F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);

            bNeedSetUp = false;
        }
        if(fm.isTick(44, 0))
        {
            mesh.chunkVisible("Z_GearLGreen", fm.CT.getGear() == 1.0F && fm.Gears.lgear);
            mesh.chunkVisible("Z_GearRGreen", fm.CT.getGear() == 1.0F && fm.Gears.rgear);
            mesh.chunkVisible("Z_GearCGreen", fm.CT.getGear() == 1.0F && fm.Gears.cgear);
            mesh.chunkVisible("Z_FlapLEGreen", fm.CT.FlapsControl > 0.2F);
            mesh.chunkVisible("Z_FlapTEGreen", fm.CT.FlapsControl > 0.2F);
            mesh.chunkVisible("Z_ddi_1_3_LWingPinUnlocked", fm.CT.getWing() > 0.001F);
            mesh.chunkVisible("Z_ddi_2_3_RWingPinUnlocked", fm.CT.getWing() > 0.001F);
            mesh.chunkVisible("Z_ddi_1_8_LExtFuel", false);
            mesh.chunkVisible("Z_ddi_2_8_RExtFuel", false);
            mesh.chunkVisible("Z_ddi_3_8_CtrExtFuel", false);
            mesh.chunkVisible("Z_ddi_2_11_SpeedBrakeOut", fm.CT.getAirBrake() > 0.01F);
            mesh.chunkVisible("Z_ddi_1_12_FuelLevelLow", fm.M.fuel < 500F);
            mesh.chunkVisible("Z_ddi_1_14_CanopyUnlocked", fm.CT.getCockpitDoor() > 0.001F);
        }
        if(!fm.CT.bMoveSideDoor)
        {
            float door = fm.CT.getCockpitDoor();
            resetYPRmodifier();
            if(door < 0.05F)
            {
                Cockpit.xyz[1] = cvt(door, 0.01F, 0.04F, 0.0F, 0.01F);
                Cockpit.xyz[2] = cvt(door, 0.01F, 0.04F, 0.0F, 0.01F);
            } else
            {
                Cockpit.xyz[1] = cvt(door, 0.1F, 0.99F, 0.01F, 1.1F);
                Cockpit.xyz[2] = cvt(door, 0.04F, 0.99F, 0.01F, 0.08F);
            }
            mesh.chunkSetLocate("Blister",  Cockpit.xyz, Cockpit.ypr);
        }
        resetYPRmodifier();
        mesh.chunkSetAngles("Gearcontrol", 0.0F, 0.0F, -15F * fm.CT.GearControl);
        mesh.chunkSetAngles("Hookcontrol", 0.0F, 0.0F, 15F * fm.CT.arrestorControl);
        resetYPRmodifier();
        mesh.chunkSetAngles("Z_Column", 10F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 10F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl));
        resetYPRmodifier();
        Cockpit.xyz[1] = 0.034F * fm.CT.RudderControl;
        Cockpit.ypr[0] = -10F * Math.max(fm.CT.BrakeControl, fm.CT.BrakeLeftControl);
        mesh.chunkSetLocate("Z_LeftPedal",  Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[1] = -0.034F * fm.CT.RudderControl;
        Cockpit.ypr[0] = 10F * Math.max(fm.CT.BrakeControl, fm.CT.BrakeRightControl);
        mesh.chunkSetLocate("Z_RightPedal",  Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_ThrottleL", 0.0F, -75F * interp(setNew.throttlel, setOld.throttlel, f), 0.0F);
        mesh.chunkSetAngles("Z_ThrottleR", 0.0F, -75F * interp(setNew.throttler, setOld.throttler, f), 0.0F);
        if(fm.getSpeedKMH() < 400F)
            mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (fm.Loc)).z, fm.getSpeedKMH()), 0.0F, 1500.24F, 0.0F, 56F), speedometerIndScale), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(fm.getSpeedKMH(), 0.0F, 1500.24F, 0.0F, 56F), speedometerIndScale), 0.0F, 0.0F);
        if(fm.getSpeedKMH() < 1500F)
            mesh.chunkSetAngles("Z_MachMeter", floatindex(cvt(Atmosphere.sonicSpeed((float)((Tuple3d) (fm.Loc)).z) / 334F - 1.0F, -0.54F, 0.0F, 0.0F, 9F), machmeterScale), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_MachMeter", floatindex(cvt((Atmosphere.sonicSpeed((float)((Tuple3d) (fm.Loc)).z) / 334F - 1.0F) + (1500F - fm.getSpeedKMH()) / 2000F, -0.54F, 0.0F, 0.0F, 9F), machmeterScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TAS_1", cvt(fm.getSpeedKMH(), 0.0F, 3704F, 0.0F, 72000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TAS_10", cvt((float)Math.floor(fm.getSpeedKMH() / 18.52F), 0.0F, 200F, 0.0F, 7200F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TAS_100", cvt((float)Math.floor(fm.getSpeedKMH() / 185.2F), 0.0F, 20F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TAS_1000", cvt((float)Math.floor(fm.getSpeedKMH() / 1852F), 0.0F, 2.0F, 0.0F, 72F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(newAltimeterFeet, oldAltimeterFeet, f), 0.0F, 110000F, 0.0F, 39600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1_1k", cvt((float)Math.floor(newAltimeterFeet / 1000F), 0.0F, 110F, 0.0F, 3960F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1_10k", cvt((float)Math.floor(newAltimeterFeet / 10000F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        if(newRadioAltimeterFeet < 100F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(newRadioAltimeterFeet, 0.0F, 100F, 0.0F, 83F), 0.0F, 0.0F);
        else if(newRadioAltimeterFeet < 200F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(newRadioAltimeterFeet - 100F, 0.0F, 100F, 83F, 123F), 0.0F, 0.0F);
        else if(newRadioAltimeterFeet < 400F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(newRadioAltimeterFeet - 200F, 0.0F, 200F, 123F, 165F), 0.0F, 0.0F);
        else if(newRadioAltimeterFeet < 600F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(newRadioAltimeterFeet - 400F, 0.0F, 200F, 165F, 189F), 0.0F, 0.0F);
        else if(newRadioAltimeterFeet < 1000F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(newRadioAltimeterFeet - 600F, 0.0F, 400F, 189F, 220F), 0.0F, 0.0F);
        else if(newRadioAltimeterFeet < 2000F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(newRadioAltimeterFeet - 1000F, 0.0F, 1000F, 220F, 262F), 0.0F, 0.0F);
        else if(newRadioAltimeterFeet < 3000F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(newRadioAltimeterFeet - 2000F, 0.0F, 1000F, 262F, 288F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_Altimeter3", cvt(newRadioAltimeterFeet - 3000F, 0.0F, 2000F, 288F, 326F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Compass1", setNew.azimuth.getDeg(f) + 180F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Compass3", -setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Azimuth_2", setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("Z_horizont1", 0.0F, 0.0F, fm.Or.getKren());
        mesh.chunkSetAngles("Z_horizont1b", 0.0F, fm.Or.getTangage(), 0.0F);
        mesh.chunkSetAngles("Z_horizont1a", setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_horizont2", 0.0F, 0.0F, fm.Or.getKren());
        mesh.chunkSetAngles("Z_horizont2b", 0.0F, fm.Or.getTangage(), 0.0F);
        mesh.chunkSetAngles("zBall", 0.0F, cvt(getBall(6D), -6F, 6F, -9F, 9F), 0.0F);
        mesh.chunkSetAngles("Z_LiquidOxygen", cvt(((A_6)aircraft()).OxygenMiliLitter, 0F, 20000F, 0F, 180F), 0.0F, 0.0F);
        float cabinAltFeet = 0F;
        if(fm.getAltitude() < 2286F)
            cabinAltFeet = fm.getAltitude() * 3.28084F;
        else if(fm.getAltitude() < 7010F)
            cabinAltFeet = 2286F * 3.28084F;
        else
            cabinAltFeet = cvt(fm.getAltitude(), 7010F, 15240F, 2286F * 3.28084F, 20000F);
        mesh.chunkSetAngles("Z_CabinPressAlt", cvt(cabinAltFeet, 0F, 50000F, 0F, 295F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ElevTrim", 160F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_AilTrim", -68F * fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RudTrim", 160F * fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        resetYPRmodifier();
        float percent = cvt(fm.EI.engines[0].getRPM(), 0.0F, 4400F, 0.0F, 1.0F);
        if(percent < 0.636364F)
            Cockpit.xyz[2] = -0.0007955F * 0.31F * (percent / 0.636364F);
        else
            Cockpit.xyz[2] = -0.0007955F * (0.69F * (percent - 0.636364F) / 0.363636F + 0.31F);
        mesh.chunkSetLocate("Z_EngineRPML", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        percent = cvt(fm.EI.engines[1].getRPM(), 0.0F, 4400F, 0.0F, 1.0F);
        if(percent < 0.636364F)
            Cockpit.xyz[2] = -0.0007955F * 0.31F * (percent / 0.636364F);
        else
            Cockpit.xyz[2] = -0.0007955F * (0.69F * (percent - 0.636364F) / 0.363636F + 0.31F);
        mesh.chunkSetLocate("Z_EngineRPMR", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        float temper = cvt(fm.EI.engines[0].tWaterOut, 0.0F, 800F, 0.0F, 8.0F);
        if(temper < 4F)
            Cockpit.xyz[2] = -0.0007955F * 0.30256F * (temper / 4F);
        else if(temper < 7F)
            Cockpit.xyz[2] = -0.0007955F * (0.55385F * (temper - 4F) / 3F + 0.30256F);
        else
            Cockpit.xyz[2] = -0.0007955F * (0.14359F * (temper - 7F) + 0.85641F);
        mesh.chunkSetLocate("Z_EngineTempL", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        temper = cvt(fm.EI.engines[1].tWaterOut, 0.0F, 800F, 0.0F, 8.0F);
        if(temper < 4F)
            Cockpit.xyz[2] = -0.0007955F * 0.30256F * (temper / 4F);
        else if(temper < 7F)
            Cockpit.xyz[2] = -0.0007955F * (0.55385F * (temper - 4F) / 3F + 0.30256F);
        else
            Cockpit.xyz[2] = -0.0007955F * (0.14359F * (temper - 7F) + 0.85641F);
        mesh.chunkSetLocate("Z_EngineTempR", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        float fuelfeed = cvt(interp(setNew.fuelpressl, setOld.fuelpressl, f), 0.0F, 100F, 0.0F, 10F);
        if(fuelfeed < 0.5F)
            Cockpit.xyz[2] = -0.0007955F * 0.05128F * fuelfeed * 2F;
        else if(fuelfeed < 1F)
            Cockpit.xyz[2] = -0.0007955F * (0.08462F * (fuelfeed - 0.5F) * 2F + 0.05128F);
        else if(fuelfeed < 5F)
            Cockpit.xyz[2] = -0.0007955F * (0.16859F * (fuelfeed - 1F) + 0.1359F);
        else
            Cockpit.xyz[2] = -0.0007955F * (0.18974F * (fuelfeed - 5F) / 5F + 0.81026F);
        mesh.chunkSetLocate("Z_EngineFuelFeedL", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        fuelfeed = cvt(interp(setNew.fuelpressr, setOld.fuelpressr, f), 0.0F, 100F, 0.0F, 10F);
        if(fuelfeed < 0.5F)
            Cockpit.xyz[2] = -0.0007955F * 0.05128F * fuelfeed * 2F;
        else if(fuelfeed < 1F)
            Cockpit.xyz[2] = -0.0007955F * (0.08462F * (fuelfeed - 0.5F) * 2F + 0.05128F);
        else if(fuelfeed < 5F)
            Cockpit.xyz[2] = -0.0007955F * (0.16859F * (fuelfeed - 1F) + 0.1359F);
        else
            Cockpit.xyz[2] = -0.0007955F * (0.18974F * (fuelfeed - 5F) / 5F + 0.81026F);
        mesh.chunkSetLocate("Z_EngineFuelFeedR", Cockpit.xyz, Cockpit.ypr);

        float ptdeg;
        if(fm.EI.engines[0].getRPM() < 1000F)
            ptdeg = cvt(fm.EI.engines[0].getRPM(), 0.0F, 1000F, 0.0F, 143F);
        else if(fm.EI.engines[0].getRPM() < 4080F)
            ptdeg = cvt(fm.EI.engines[0].getRPM(), 1000F, 4080F, 143F, 165F);
        else
            ptdeg = cvt(fm.EI.engines[0].getRPM(), 4080F, 5000F, 165F, 180F);
        mesh.chunkSetAngles("Z_PT1", ptdeg, 0.0F, 0.0F);
        if(fm.EI.engines[1].getRPM() < 1000F)
            ptdeg = cvt(fm.EI.engines[1].getRPM(), 0.0F, 1000F, 0.0F, 143F);
        else if(fm.EI.engines[1].getRPM() < 4080F)
            ptdeg = cvt(fm.EI.engines[1].getRPM(), 1000F, 4080F, 143F, 165F);
        else
            ptdeg = cvt(fm.EI.engines[1].getRPM(), 4080F, 5000F, 165F, 180F);
        mesh.chunkSetAngles("Z_PT2", ptdeg, 0.0F, 0.0F);
        float psi = ((A_6)aircraft()).engineOilPressurePSI[0];
        if(psi < 40F)
            mesh.chunkSetAngles("Z_OilPress1", cvt(psi, 0F, 40F, 0F, 144F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_OilPress1", cvt(psi, 40F, 100F, 144F, 324F), 0.0F, 0.0F);
        psi = ((A_6)aircraft()).engineOilPressurePSI[1];
        if(psi < 40F)
            mesh.chunkSetAngles("Z_OilPress2", cvt(psi, 0F, 40F, 0F, 144F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_OilPress2", cvt(psi, 40F, 100F, 144F, 324F), 0.0F, 0.0F);

        resetYPRmodifier();
        float f7 = setNew.ilsLoc * setNew.ilsLoc * (setNew.ilsLoc < 0.0F ? -1F : 1.0F);
        Cockpit.xyz[0] = -cvt(f7, -10000F, 10000F, -0.02F, 0.02F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(fm.Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("BL_Vert", Cockpit.xyz, Cockpit.ypr);
        float f8 = setNew.ilsGS * setNew.ilsGS * (setNew.ilsGS < 0.0F ? -1F : 1.0F);
        Cockpit.xyz[1] = -cvt(f8, -0.25F, 0.25F, -0.02F, 0.02F);
        Cockpit.xyz[0] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(fm.Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("BL_Horiz", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkVisible("BL_Vert", bHSIDL || bHSIILS || bHSIMAN || bHSINAV || bHSITAC || bHSITGT || bHSIUHF);
        mesh.chunkVisible("BL_Horiz", bHSIILS);
        resetYPRmodifier();
        float f9 = setNew.hsiLoc * setNew.hsiLoc * (setNew.hsiLoc < 0.0F ? -1F : 1.0F);
        Cockpit.xyz[1] = cvt(f9, -20000F, 20000F, -0.036F, 0.036F);
        Cockpit.xyz[0] = Cockpit.xyz[2] = 0.0F;
        mesh.chunkSetLocate("Z_Azimuth_3", Cockpit.xyz, Cockpit.ypr);
        if(useRealisticNavigationInstruments())
        {
            mesh.chunkSetAngles("Z_Compass4", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            float f10 = (float)Math.floor(setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F);
            if(f10 < 0.0F)
                f10 += 360F;
            mesh.chunkSetAngles("Z_course_1", cvt(f10, 0.0F, 1000F, 0.0F, 36000F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_course_10", cvt((float)Math.floor(f10 / 10F), 0.0F, 100F, 0.0F, 3600F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_course_100", cvt((float)Math.floor(f10 / 100F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        }
        float f11 = getBeaconDistance() / 1852F;
        if(!useRealisticNavigationInstruments())
        {
            WayPoint waypoint = fm.AP.way.curr();
            if(waypoint != null)
            {
                Point3d point3d = new Point3d();
                Vector3d vector3d = new Vector3d();
                waypoint.getP(point3d);
                vector3d.sub(point3d, fm.Loc);
                f11 = (float)Math.sqrt(vector3d.x * vector3d.x + vector3d.y * vector3d.y) / 1852F;
            }
        }
        mesh.chunkSetAngles("Z_miles_01", cvt(f11, 0.0F, 1000F, 0.0F, 360000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_miles_1", cvt((float)Math.floor(f11), 0.0F, 1000F, 0.0F, 36000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_miles_10", cvt((float)Math.floor(f11 / 10F), 0.0F, 100F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_miles_100", cvt((float)Math.floor(f11 / 100F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkVisible("Z_miles_hide", !bHSIILS && !bHSITAC && !bHSINAV && !bHSITGT);
        mesh.chunkVisible("Z_HSItext_DL", bHSIDL);
        mesh.chunkVisible("Z_HSItext_ILS", bHSIILS);
        mesh.chunkVisible("Z_HSItext_MAN", bHSIMAN);
        mesh.chunkVisible("Z_HSItext_NAV", bHSINAV);
        mesh.chunkVisible("Z_HSItext_TAC", bHSITAC);
        mesh.chunkVisible("Z_HSItext_TGT", bHSITGT);
        mesh.chunkVisible("Z_HSItext_UHF", bHSIUHF);
        mesh.chunkSetAngles("Z_AOA", cvt(30F - AOAunits, 0.0F, 30F, 0.0F, 270F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Accello1", cvt(fm.getOverload(), -5F, 10F, -220F, 114F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Accello2", cvt(setNew.accelloMax, -5F, 10F, -220F, 114F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Accello3", cvt(setNew.accelloMin, -5F, 10F, -220F, 114F), 0.0F, 0.0F);
        float f12 = fm.M.fuel * 2.20463F;
        mesh.chunkSetAngles("Z_Fuel", cvt(f12, 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_10", cvt((float)(Math.floor(f12 / 10F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_100", cvt((float)(Math.floor(f12 / 100F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_1000", cvt((float)(Math.floor(f12 / 1000F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_10000", cvt((float)(Math.floor(f12 / 10000F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkVisible("Z_EngineFireL", fm.AS.astateEngineStates[0] > 3);
        mesh.chunkVisible("Z_EngineFireR", fm.AS.astateEngineStates[1] > 3);
        mesh.chunkVisible("Z_EngineOverheatL", fm.AS.astateEngineStates[0] > 1 || fm.EI.engines[0].tWaterOut > fm.EI.engines[0].tWaterCritMax);
        mesh.chunkVisible("Z_EngineOverheatR", fm.AS.astateEngineStates[1] > 1 || fm.EI.engines[1].tWaterOut > fm.EI.engines[1].tWaterCritMax);
        if(fm.CT.getGear() == 1.0F && fm.CT.FlapsControl > 0.2F)
        {
            mesh.chunkVisible("Z_AOALanding1", AOAunits > 19.499F);
            mesh.chunkVisible("Z_AOALanding2", AOAunits < 20.1F && AOAunits > 17.9F);
            mesh.chunkVisible("Z_AOALanding3", AOAunits < 18.5F);
        } else
        {
            mesh.chunkVisible("Z_AOALanding1", false);
            mesh.chunkVisible("Z_AOALanding2", false);
            mesh.chunkVisible("Z_AOALanding3", false);
        }
        mesh.chunkVisible("Z_Warn_WHEELS", fm.CT.getGear() < 1.0F && fm.CT.FlapsControl > 0.2F);
        drawSound(f);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("HullDamage2", true);
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("Speedometer1", false);
            mesh.chunkVisible("Speedometer1_D1", true);
            mesh.chunkVisible("Z_Speedometer1", false);
            mesh.chunkVisible("Z_Speedometer2", false);
            mesh.chunkVisible("RPML", false);
            mesh.chunkVisible("RPML_D1", true);
            mesh.chunkVisible("Z_RPML", false);
            mesh.chunkVisible("FuelRemainV", false);
            mesh.chunkVisible("FuelRemainV_D1", true);
            mesh.chunkVisible("Z_FuelRemainV", false);
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("HullDamage4", true);
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("Altimeter1", false);
            mesh.chunkVisible("Altimeter1_D1", true);
            mesh.chunkVisible("Z_Altimeter1", false);
            mesh.chunkVisible("Z_Altimeter3", false);
            mesh.chunkVisible("GasPressureL", false);
            mesh.chunkVisible("GasPressureL_D1", true);
            mesh.chunkVisible("Z_GasPressureL", false);
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("HullDamage1", true);
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("RPMR", false);
            mesh.chunkVisible("RPMR_D1", true);
            mesh.chunkVisible("Z_RPMR", false);
            mesh.chunkVisible("FuelPressR", false);
            mesh.chunkVisible("FuelPressR_D1", true);
            mesh.chunkVisible("Z_FuelPressR", false);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("HullDamage3", true);
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("GasPressureR", false);
            mesh.chunkVisible("GasPressureR_D1", true);
            mesh.chunkVisible("Z_GasPressureR", false);
        }
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("XGlassDamage1", true);
            mesh.chunkVisible("XGlassDamage2", true);
            mesh.chunkVisible("Climb", false);
            mesh.chunkVisible("Climb_D1", true);
            mesh.chunkVisible("Z_Climb1", false);
            mesh.chunkVisible("FuelPressR", false);
            mesh.chunkVisible("FuelPressR_D1", true);
            mesh.chunkVisible("Z_FuelPressR", false);
        }
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("XGlassDamage1", true);
            mesh.chunkVisible("HullDamage1", true);
            mesh.chunkVisible("HullDamage2", true);
            mesh.chunkVisible("Revi_D0", false);
            mesh.chunkVisible("Revi_D1", true);
            mesh.chunkVisible("Z_Z_RETICLE", false);
            mesh.chunkVisible("Z_Z_MASK", false);
            mesh.chunkVisible("FuelPressL", false);
            mesh.chunkVisible("FuelPressL_D1", true);
            mesh.chunkVisible("Z_FuelPressL", false);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("HullDamage1", true);
            mesh.chunkVisible("Altimeter1", false);
            mesh.chunkVisible("Altimeter1_D1", true);
            mesh.chunkVisible("Z_Altimeter1", false);
            mesh.chunkVisible("Z_Altimeter3", false);
            mesh.chunkVisible("Climb", false);
            mesh.chunkVisible("Climb_D1", true);
            mesh.chunkVisible("Z_Climb1", false);
            mesh.chunkVisible("AFN", false);
            mesh.chunkVisible("AFN_D1", true);
            mesh.chunkVisible("Z_AFN1", false);
            mesh.chunkVisible("Z_AFN2", false);
            mesh.chunkVisible("FuelPressL", false);
            mesh.chunkVisible("FuelPressL_D1", true);
            mesh.chunkVisible("Z_FuelPressL", false);
            mesh.chunkVisible("FuelRemainIn", false);
            mesh.chunkVisible("FuelRemainIn_D1", true);
            mesh.chunkVisible("Z_FuelRemainIn", false);
        }
        fm.AS.getClass();
        retoggleLight();
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleDim()
    {
        cockpitDimControl = !cockpitDimControl;
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    protected void drawSound(float f)
    {
    }

    private void stopSoundEffects()
    {
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private Point3d tmpP;
    private Vector3d tmpV;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private boolean bU4;
    private boolean bHSIDL;
    private boolean bHSIILS;
    private boolean bHSIMAN;
    private boolean bHSINAV;
    private boolean bHSITAC;
    private boolean bHSITGT;
    private boolean bHSIUHF;
    private static final float standardbar = 29970F;
    private static final float speedometerIndScale[] = {
        0.0F, 2.41F, 4.82F, 10.36F, 16.14F, 23.44F, 35.11F, 47.26F, 60.07F, 73.8F, 
        87.14F, 99.47F, 111.75F, 123.91F, 135.64F, 142.87F, 150.1F, 157.33F, 164.56F, 171.81F, 
        179.09F, 186.37F, 193.65F, 200.93F, 206.65F, 212.3F, 217.94F, 223.59F, 229.03F, 234.1F, 
        239.17F, 244.24F, 249.31F, 253.44F, 257.23F, 261.03F, 264.83F, 268.63F, 272.49F, 276.34F, 
        280.2F, 284.06F, 287.8F, 291.43F, 295.07F, 298.71F, 302.34F, 305.49F, 308.6F, 311.7F, 
        314.8F, 317.84F, 320.74F, 323.65F, 326.55F, 329.45F, 332.35F
    };
    private static final float machmeterScale[] = {
        -91.28F, -85.41F, -77.6F, -64.06F, -50.94F, -41.61F, -32.3F, -22.31F, -10.87F, 0.0F
    };
    private static final float variometerScale[] = {
        -170F, -160F, -145F, -125F, -88F, -55F, 0.0F, 55F, 88F, 125F, 
        145F, 160F, 170F
    };


}