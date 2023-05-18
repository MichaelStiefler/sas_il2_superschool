
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.Time;
import com.maddox.sound.*;
import com.maddox.sas1946.il2.util.CrossVersion;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, TypeGuidedMissileCarrier, Cockpit, Aircraft

public class CockpitF_14 extends CockpitPilot
{
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
        float nozzlel;
        float nozzler;
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

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            float f = fm.getAltitude() - (float)Engine.land().HQ_Air(fm.Loc.x, fm.Loc.y);
            f = f / (float)Math.cos(Math.toRadians(Math.abs(fm.Or.getKren()))) / (float)Math.cos(Math.toRadians(Math.abs(fm.Or.getTangage())));
            setNew.radioaltimeter = (5F * setOld.radioaltimeter + f) / 6F;
            setNew.throttlel = (10F * setOld.throttlel + fm.EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + fm.EI.engines[1].getControlThrottle()) / 11F;
            setNew.fuelpressl = (10F * setOld.fuelpressl + (fm.M.fuel > 1.0F ? 80F * fm.EI.engines[0].getPowerOutput() * fm.EI.engines[0].getReadyness() : 0.0F)) / 11F;
            setNew.fuelpressr = (10F * setOld.fuelpressr + (fm.M.fuel > 1.0F ? 80F * fm.EI.engines[1].getPowerOutput() * fm.EI.engines[1].getReadyness() : 0.0F)) / 11F;
            if(fm.getOverload() > setOld.accelloMax)
                setNew.accelloMax = fm.getOverload();
            if(fm.getOverload() < setOld.accelloMin)
                setNew.accelloMin = fm.getOverload();
            setNew.nozzlel = (4F * setOld.nozzlel + ((F_14)aircraft()).fNozzleOpenL) / 5F;
            setNew.nozzler = (4F * setOld.nozzler + ((F_14)aircraft()).fNozzleOpenR) / 5F;
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


    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(30F);
    }

    public CockpitF_14()
    {
        super("3DO/Cockpit/F-14/F-14A.him", "he111");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        tmpP = new Point3d();
        tmpV = new Vector3d();
        gun = new Gun[4];
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bU4 = false;
        bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
        cockpitNightMats = (new String[] {
            "Gauges_02", "Gauges_03", "Gauges_04", "Gauges_05", "Gauges_06", "Gauges_08", "Gauges_10", "Needles", "Needles2"
        });
        setNightMats(false);
        setNew.dimPosition = 1.0F;
        EngineFireLeftWarn = aircraft().newSound("aircraft.buzzshort2", false);
        EngineFireRightWarn = aircraft().newSound("aircraft.buzzshort2", false);
        lLastEngineFireLeftWarned = -1L;
        lLastEngineFireRightWarned = -1L;
        BingoFuelWarn = aircraft().newSound("aircraft.buzzshort1", false);
        iBingoFuelWarned = 0;
        hydroStat = 0;
        interpPut(new Interpolater(), null, Time.current(), null);
        CrossVersion.setPrintCompassHeading(this, true);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((F_14)aircraft()).startCockpitSounds();
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
        ((F_14)aircraft()).stopCockpitSounds();
        super.doFocusLeave();
    }

    public void removeCanopy()
    {
        mesh.chunkVisible("Blister", false);
        mesh.chunkVisible("Z_Holes2_D1", false);
        mesh.chunkVisible("Z_Holes1_D1", false);
    }

    public void reflectWorldToInstruments(float f)
    {
        float f1 = 3.28084F;
        float f2 = setNew.altimeter * f1;
        float f3 = setOld.altimeter * f1;
        float f4 = setNew.radioaltimeter * f1;
        float f5 = setOld.radioaltimeter * f1;
        float f6 = ((F_14)aircraft()).unitsAoA;
        Cockpit.xyz[0] = Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        if(fm.isTick(44, 0))
        {
            if(((F_14)aircraft()).hydroPress < 50F)
            {
                if(hydroStat != 0)
                {
                    mesh.materialReplace("HydroStat000", "HydroStat000");
                    hydroStat = 0;
                }
            }
            else if(((F_14)aircraft()).hydroPress < 150F)
            {
                if(hydroStat != 1)
                {
                    mesh.materialReplace("HydroStat000", "HydroStat001");
                    hydroStat = 1;
                }
            }
            else if(((F_14)aircraft()).hydroPress < 200F)
            {
                if(hydroStat != 2)
                {
                    mesh.materialReplace("HydroStat000", "HydroStat011");
                    hydroStat = 2;
                }
            }
            else
            {
                if(hydroStat != 3)
                {
                    mesh.materialReplace("HydroStat000", "HydroStat111");
                    hydroStat = 3;
                }
            }
            mesh.chunkVisible("Z_GearTransRed", (fm.CT.getGearC() > 0.01F && fm.CT.getGearC() < 0.99F) || (fm.CT.getGearL() > 0.01F && fm.CT.getGearL() < 0.99F) || (fm.CT.getGearR() > 0.01F && fm.CT.getGearR() < 0.99F));
            mesh.chunkVisible("Z_ddi_1_8_LExtFuel", false);
            mesh.chunkVisible("Z_ddi_2_8_RExtFuel", false);
            mesh.chunkVisible("Z_ddi_3_8_CtrExtFuel", false);
            mesh.chunkVisible("Z_ddi_3_6_RefuelReady", fm.CT.getRefuel() > 0.99F);
            mesh.chunkVisible("Z_ddi_1_12_FuelLevelLow", fm.M.fuel < 500F);
            mesh.chunkVisible("Z_ddi_1_14_CanopyUnlocked", fm.CT.getCockpitDoor() > 0.001F);
        }
        resetYPRmodifier();
        mesh.chunkSetAngles("Blister", 0.0F, 0.0F, 60F * fm.CT.getCockpitDoor());
        mesh.chunkSetAngles("Gearcontrol", 0.0F, 0.0F, -15F * fm.CT.GearControl);
        mesh.chunkSetAngles("Hookcontrol", 0.0F, 0.0F, 15F * fm.CT.arrestorControl);
        resetYPRmodifier();
        mesh.chunkSetAngles("Z_Column", 10F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 10F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl));
        mesh.chunkSetAngles("Z_LeftPedal", -20F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RightPedal", -20F * fm.CT.getRudder(), 0.0F, 0.0F);
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
        mesh.chunkSetAngles("Z_Altimeter1", cvt(interp(f2, f3, f), 0.0F, 110000F, 0.0F, 39600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1_1k", cvt((float)Math.floor(f2 / 1000F), 0.0F, 110F, 0.0F, 3960F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1_10k", cvt((float)Math.floor(f2 / 10000F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1m_1", cvt(29970F / 10F, 0.0F, 4000F, 0.0F, 144000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1m_10", cvt((float)Math.floor(29970F / 100F), 0.0F, 400F, 0.0F, 14400F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1m_100", cvt((float)Math.floor(29970F / 1000F), 0.0F, 40F, 0.0F, 1440F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1m_1000", cvt((float)Math.floor(29970F / 10000F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        if(f4 < 500F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(f4, 0.0F, 500F, 0.0F, 180F), 0.0F, 0.0F);
        else
        if(f2 < 1000F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(f4, 0.0F, 1000F, 90F, 270F), 0.0F, 0.0F);
        else
        if(f2 < 5000F)
            mesh.chunkSetAngles("Z_Altimeter3", cvt(f4, 0.0F, 5000F, 254F, 336F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_Altimeter3", 336F, 0.0F, 0.0F);
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
        float f7 = setNew.ilsLoc * setNew.ilsLoc * (setNew.ilsLoc >= 0.0F ? 1.0F : -1F);
        Cockpit.xyz[0] = -cvt(f7, -10000F, 10000F, -0.02F, 0.02F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(fm.Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("BL_Vert", Cockpit.xyz, Cockpit.ypr);
        float f8 = setNew.ilsGS * setNew.ilsGS * (setNew.ilsGS >= 0.0F ? 1.0F : -1F);
        Cockpit.xyz[1] = -cvt(f8, -0.25F, 0.25F, -0.02F, 0.02F);
        Cockpit.xyz[0] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(fm.Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("BL_Horiz", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkVisible("BL_Vert", bHSIDL || bHSIILS || bHSIMAN || bHSINAV || bHSITAC || bHSITGT || bHSIUHF);
        mesh.chunkVisible("BL_Horiz", bHSIILS);
        resetYPRmodifier();
        float f9 = setNew.hsiLoc * setNew.hsiLoc * (setNew.hsiLoc >= 0.0F ? 1.0F : -1F);
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
//        mesh.chunkVisible("Z_HSItext_DL", bHSIDL);
//        mesh.chunkVisible("Z_HSItext_ILS", bHSIILS);
//        mesh.chunkVisible("Z_HSItext_MAN", bHSIMAN);
//        mesh.chunkVisible("Z_HSItext_NAV", bHSINAV);
//        mesh.chunkVisible("Z_HSItext_TAC", bHSITAC);
//        mesh.chunkVisible("Z_HSItext_TGT", bHSITGT);
//        mesh.chunkVisible("Z_HSItext_UHF", bHSIUHF);
        resetYPRmodifier();
        Cockpit.xyz[0] = -cvt(f6, 0F, 30F, 0.0F, 0.001429F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        mesh.chunkSetLocate("Z_AOABar", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Accello1", cvt(fm.getOverload(), -5F, 10F, -220F, 114F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Accello2", cvt(setNew.accelloMax, -5F, 10F, -220F, 114F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Accello3", cvt(setNew.accelloMin, -5F, 10F, -220F, 114F), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        float fext = fm.EI.engines[0].tWaterOut * 1.2F;
        Cockpit.xyz[0] = (fext < 600F) ? -cvt(fext, 0F, 600F, 0.0F, 0.00023478F) :
          -cvt(fext, 600F, 1360F, 0.00023478F, 0.001763F);
        mesh.chunkSetLocate("Z_Exhaust_Temp1", Cockpit.xyz, Cockpit.ypr);
        fext = fm.EI.engines[1].tWaterOut * 1.2F;
        Cockpit.xyz[0] = (fext < 600F) ? -cvt(fext, 0F, 600F, 0.0F, 0.00023478F) :
          -cvt(fext, 600F, 1360F, 0.00023478F, 0.001763F);
        mesh.chunkSetLocate("Z_Exhaust_Temp2", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        float frpm = fm.EI.engines[0].getRPM() * 0.23F;
        Cockpit.xyz[0] = (frpm < 600F) ? -cvt(frpm, 0F, 600F, 0.0F, 0.00038234F) :
          -cvt(frpm, 600F, 1100F, 0.00038234F, 0.001763F);
        mesh.chunkSetLocate("Z_RPM1", Cockpit.xyz, Cockpit.ypr);
        frpm = fm.EI.engines[1].getRPM() * 0.23F;
        Cockpit.xyz[0] = (frpm < 600F) ? -cvt(frpm, 0F, 600F, 0.0F, 0.00038234F) :
          -cvt(frpm, 600F, 1100F, 0.00038234F, 0.001763F);
        mesh.chunkSetLocate("Z_RPM2", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        float ffuelp = interp(setNew.fuelpressl, setOld.fuelpressl, f) * 10F;
        Cockpit.xyz[0] = (ffuelp < 500F) ? -cvt(ffuelp, 0F, 500F, 0.0F, 0.0010672F) :
          -cvt(ffuelp, 500F, 1260F, 0.0010672F, 0.001763F);
        mesh.chunkSetLocate("Z_FuelPress1", Cockpit.xyz, Cockpit.ypr);
        ffuelp = interp(setNew.fuelpressr, setOld.fuelpressr, f) * 10F;
        Cockpit.xyz[0] = (ffuelp < 500F) ? -cvt(ffuelp, 0F, 500F, 0.0F, 0.0010672F) :
          -cvt(ffuelp, 500F, 1260F, 0.0010672F, 0.001763F);
        mesh.chunkSetLocate("Z_FuelPress2", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Nozzle1", cvt(setNew.nozzlel, 0.0F, 1.0F, -51F, 51F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Nozzle2", cvt(setNew.nozzler, 0.0F, 1.0F, -51F, 51F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_OilPress1", cvt(((F_14)aircraft()).engineOilPressurePSI[0], 0.0F, 100F, -210.0F, 90.0F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_OilPress2", cvt(((F_14)aircraft()).engineOilPressurePSI[1], 0.0F, 100F, -210.0F, 90.0F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Hydropress1", cvt(((F_14)aircraft()).hydroPress, 0.0F, 400F, 0.0F, -120F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Hydropress2", cvt(((F_14)aircraft()).hydroPress, 0.0F, 400F, 0.0F, 120F), 0.0F, 0.0F);
        resetYPRmodifier();
        float f12 = fm.M.fuel * 2.20463F;
        Cockpit.xyz[0] = -cvt(f12, 900F, 10000F, 0.0F, 0.00158F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        mesh.chunkSetLocate("Z_FuelBarL", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetLocate("Z_FuelBarR", Cockpit.xyz, Cockpit.ypr);
        if(((F_14) aircraft()).bHasUnderTank) f12 += (((F_14) aircraft()).checkfuel(0) + ((F_14) aircraft()).checkfuel(1)) * 2.20463F;
        mesh.chunkSetAngles("Z_fuelnum_1", cvt((float)(Math.floor(f12) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_10", cvt((float)(Math.floor(f12 / 10F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_100", cvt((float)(Math.floor(f12 / 100F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_1000", cvt((float)(Math.floor(f12 / 1000F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelnum_10000", cvt((float)(Math.floor(f12 / 10000F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        f12 = ((F_14) aircraft()).Bingofuel;
        mesh.chunkSetAngles("Z_fuelbingo_1", cvt((float)(Math.floor(f12) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelbingo_10", cvt((float)(Math.floor(f12 / 10F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelbingo_100", cvt((float)(Math.floor(f12 / 100F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelbingo_1000", cvt((float)(Math.floor(f12 / 1000F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fuelbingo_10000", cvt((float)(Math.floor(f12 / 10000F) % 10D), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = -cvt(fm.CT.getVarWing(), 0.0F, 0.8F, 0.0F, 0.000894F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        mesh.chunkSetLocate("Z_WingSweepAct", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -cvt(fm.CT.getVarWing(), 0.0F, 0.8F, 0.0F, 0.035329F);
        Cockpit.xyz[1] = Cockpit.xyz[0] = 0.0F;
        mesh.chunkSetLocate("Z_WingSweepCmd", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -cvt(((F_14) aircraft()).varWingProgram, 0.0F, 0.8F, 0.0F, 0.035329F);
        Cockpit.xyz[1] = Cockpit.xyz[0] = 0.0F;
        mesh.chunkSetLocate("Z_WingSweepPGM", Cockpit.xyz, Cockpit.ypr);
        if(fm.CT.getFlap() < 0.01F)
            mesh.chunkSetAngles("Z_Flap", 0.0F, 0.0F, 0.0F);
        else if(fm.CT.getFlap() < 0.286F)
            mesh.chunkSetAngles("Z_Flap", -cvt(fm.CT.getFlap(), 0.0F, 0.286F, 0.0F, 36.0F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_Flap", -cvt(fm.CT.getFlap(), 0.286F, 1.0F, 36.0F, 90.0F), 0.0F, 0.0F);
        if(!((F_14) aircraft()).isGeneratorAlive)
            mesh.chunkSetAngles("Z_Slat", 0.0F, -180.0F, 0.0F);
        else if(fm.CT.FlapsControl < 0.01F && fm.CT.getFlap() < 0.01F)
            mesh.chunkSetAngles("Z_Slat", 0.0F, 0.0F, 0.0F);
        else
        {
            if(fm.CT.getFlap() < 0.5F)
                mesh.chunkSetAngles("Z_Slat", 0.0F, -180.0F, 0.0F);
            else
                mesh.chunkSetAngles("Z_Slat", 0.0F, -90.0F, 0.0F);
        }
        if(!((F_14) aircraft()).isGeneratorAlive)
        {
            mesh.chunkSetAngles("Z_GearC", 0.0F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_GearL", 0.0F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_GearR", 0.0F, 0.0F, 0.0F);
        }
        else
        {
            if(fm.CT.getGearC() == 0.0F)
                mesh.chunkSetAngles("Z_GearC", 0.0F, -90.0F, 0.0F);
            else if(fm.CT.getGearC() == 1.0F && fm.Gears.cgear)
                mesh.chunkSetAngles("Z_GearC", 0.0F, -180.0F, 0.0F);
            else
                mesh.chunkSetAngles("Z_GearC", 0.0F, 0.0F, 0.0F);
            if(fm.CT.getGearL() == 0.0F)
                mesh.chunkSetAngles("Z_GearL", 0.0F, -90.0F, 0.0F);
            else if(fm.CT.getGearL() == 1.0F && fm.Gears.lgear)
                mesh.chunkSetAngles("Z_GearL", 0.0F, -180.0F, 0.0F);
            else
                mesh.chunkSetAngles("Z_GearL", 0.0F, 0.0F, 0.0F);
            if(fm.CT.getGearR() == 0.0F)
                mesh.chunkSetAngles("Z_GearR", 0.0F, -90.0F, 0.0F);
            else if(fm.CT.getGearR() == 1.0F && fm.Gears.rgear)
                mesh.chunkSetAngles("Z_GearR", 0.0F, -180.0F, 0.0F);
            else
                mesh.chunkSetAngles("Z_GearR", 0.0F, 0.0F, 0.0F);
        }
        if(!((F_14) aircraft()).isGeneratorAlive)
            mesh.chunkSetAngles("Z_Airbrake", 0.0F, -180.0F, 0.0F);
        else if(fm.CT.getAirBrake() < 0.01F)
            mesh.chunkSetAngles("Z_Airbrake", 0.0F, 0.0F, 0.0F);
        else if(fm.CT.getAirBrake() < 0.99F)
            mesh.chunkSetAngles("Z_Airbrake", 0.0F, -270.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_Airbrake", 0.0F, -90.0F, 0.0F);

        mesh.chunkVisible("Z_EngineFireL", fm.AS.astateEngineStates[0] > 3);
        mesh.chunkVisible("Z_EngineFireR", fm.AS.astateEngineStates[1] > 3);
        mesh.chunkVisible("Z_EngineOverheatL", fm.AS.astateEngineStates[0] > 1 || fm.EI.engines[0].tWaterOut > fm.EI.engines[0].tWaterCritMax);
        mesh.chunkVisible("Z_EngineOverheatR", fm.AS.astateEngineStates[1] > 1 || fm.EI.engines[1].tWaterOut > fm.EI.engines[1].tWaterCritMax);
        if(fm.CT.getGear() > 0.2F)
        {
            mesh.chunkVisible("Z_AOALanding", true);
            if(((F_14)aircraft()).bDLCengaged)
                f6 -= 3.0F;
            if(f6 > 16.0F)   // SLOW, Green
                mesh.materialReplace("WlightAOA", "WlightAOA_H");
            else if(f6 > 15.5F)
                mesh.materialReplace("WlightAOA", "WlightAOA_HM");
            else if(f6 > 14.5F)   // ON SPEED, Yellow
                mesh.materialReplace("WlightAOA", "WlightAOA_M");
            else if(f6 > 14.0F)
                mesh.materialReplace("WlightAOA", "WlightAOA_ML");
            else   // FAST, Red
                mesh.materialReplace("WlightAOA", "WlightAOA_L");
        } else
        {
            mesh.chunkVisible("Z_AOALanding", false);
        }
        mesh.chunkVisible("Z_Warn_WHEELS", fm.CT.getGear() < 1.0F && fm.CT.FlapsControl > 0.2F &&
                          fm.EI.engines[0].getControlThrottle() < 0.96F && fm.EI.engines[1].getControlThrottle() < 0.96F);

        drawSound(f);

/*        boolean flag = false;
        if(fm.CT.Weapons[5] != null && "AIM-7E".equals(fm.CT.rocketNameSelected))
        {
            int i;
            for(i = 0; i < fm.CT.Weapons[5].length; i++)
            {
                if(!fm.CT.Weapons[5][i].haveBullets() || !(fm.CT.Weapons[5][i] instanceof RocketGunAIM7E))
                    continue;
                if(i == 0)
                {
                    mesh.chunkVisible("Z_MisSelect_RdL1", false);
                    mesh.chunkVisible("Z_MisSelect_RdL2", false);
                    mesh.chunkVisible("Z_MisSelect_RdR2", false);
                    mesh.chunkVisible("Z_MisSelect_SelL1", false);
                    mesh.chunkVisible("Z_MisSelect_SelL2", false);
                    mesh.chunkVisible("Z_MisSelect_SelR2", false);
                    mesh.chunkVisible("Z_MisSelect_RdR1", ((TypeGuidedMissileCarrier)fm.actor).getGuidedMissileUtils().getMissileLockState() == 2);
                    mesh.chunkVisible("Z_MisSelect_SelR1", true);
                    break;
                }
                if(i == 2 && aircraft().thisWeaponsName.indexOf("4xAIM7E") != -1)
                {
                    mesh.chunkVisible("Z_MisSelect_RdL2", false);
                    mesh.chunkVisible("Z_MisSelect_RdR1", false);
                    mesh.chunkVisible("Z_MisSelect_RdR2", false);
                    mesh.chunkVisible("Z_MisSelect_SelL2", false);
                    mesh.chunkVisible("Z_MisSelect_SelR1", false);
                    mesh.chunkVisible("Z_MisSelect_SelR2", false);
                    mesh.chunkVisible("Z_MisSelect_RdL1", ((TypeGuidedMissileCarrier)fm.actor).getGuidedMissileUtils().getMissileLockState() == 2);
                    mesh.chunkVisible("Z_MisSelect_SelL1", true);
                    break;
                }
                if(i == 4 && aircraft().thisWeaponsName.indexOf("4xAIM7E") != -1 || i == 2 && aircraft().thisWeaponsName.indexOf("3xAIM7E") != -1)
                {
                    mesh.chunkVisible("Z_MisSelect_RdL1", false);
                    mesh.chunkVisible("Z_MisSelect_RdR1", false);
                    mesh.chunkVisible("Z_MisSelect_RdR2", false);
                    mesh.chunkVisible("Z_MisSelect_SelL1", false);
                    mesh.chunkVisible("Z_MisSelect_SelR1", false);
                    mesh.chunkVisible("Z_MisSelect_SelR2", false);
                    mesh.chunkVisible("Z_MisSelect_RdL2", ((TypeGuidedMissileCarrier)fm.actor).getGuidedMissileUtils().getMissileLockState() == 2);
                    mesh.chunkVisible("Z_MisSelect_SelL2", true);
                    break;
                }
                if((i != 6 || aircraft().thisWeaponsName.indexOf("4xAIM7E") == -1) && (i != 4 || aircraft().thisWeaponsName.indexOf("3xAIM7E") == -1))
                    continue;
                mesh.chunkVisible("Z_MisSelect_RdL1", false);
                mesh.chunkVisible("Z_MisSelect_RdL2", false);
                mesh.chunkVisible("Z_MisSelect_RdR1", false);
                mesh.chunkVisible("Z_MisSelect_SelL1", false);
                mesh.chunkVisible("Z_MisSelect_SelL2", false);
                mesh.chunkVisible("Z_MisSelect_SelR1", false);
                mesh.chunkVisible("Z_MisSelect_RdR2", ((TypeGuidedMissileCarrier)fm.actor).getGuidedMissileUtils().getMissileLockState() == 2);
                mesh.chunkVisible("Z_MisSelect_SelR2", true);
                break;
            }

            if(i == fm.CT.Weapons[5].length)
            {
                mesh.chunkVisible("Z_MisSelect_RdL1", false);
                mesh.chunkVisible("Z_MisSelect_RdL2", false);
                mesh.chunkVisible("Z_MisSelect_RdR1", false);
                mesh.chunkVisible("Z_MisSelect_RdR2", false);
                mesh.chunkVisible("Z_MisSelect_SelL1", false);
                mesh.chunkVisible("Z_MisSelect_SelL2", false);
                mesh.chunkVisible("Z_MisSelect_SelR1", false);
                mesh.chunkVisible("Z_MisSelect_SelR2", false);
            }
        } else
        {
            mesh.chunkVisible("Z_MisSelect_RdL1", false);
            mesh.chunkVisible("Z_MisSelect_RdL2", false);
            mesh.chunkVisible("Z_MisSelect_RdR1", false);
            mesh.chunkVisible("Z_MisSelect_RdR2", false);
            mesh.chunkVisible("Z_MisSelect_SelL1", false);
            mesh.chunkVisible("Z_MisSelect_SelL2", false);
            mesh.chunkVisible("Z_MisSelect_SelR1", false);
            mesh.chunkVisible("Z_MisSelect_SelR2", false);
        }
        boolean flag1 = false;
        if(fm.CT.Weapons[6] != null && "AIM-9D".equals(fm.CT.rocketNameSelected))
        {
            int j;
            for(j = 0; j < fm.CT.Weapons[6].length; j++)
            {
                if(!fm.CT.Weapons[6][j].haveBullets() || !(fm.CT.Weapons[6][j] instanceof RocketGunAIM9D))
                    continue;
                if(j == 0)
                {
                    mesh.chunkVisible("Z_MisSelect_RdRW", false);
                    mesh.chunkVisible("Z_MisSelect_SwL1", false);
                    mesh.chunkVisible("Z_MisSelect_SwR1", false);
                    mesh.chunkVisible("Z_MisSelect_SwR2", false);
                    mesh.chunkVisible("Z_MisSelect_RdLW", ((TypeGuidedMissileCarrier)fm.actor).getGuidedMissileUtils().getMissileLockState() == 2);
                    mesh.chunkVisible("Z_MisSelect_SwL2", true);
                    break;
                }
                if(j == 2)
                {
                    mesh.chunkVisible("Z_MisSelect_RdLW", false);
                    mesh.chunkVisible("Z_MisSelect_SwL1", false);
                    mesh.chunkVisible("Z_MisSelect_SwL2", false);
                    mesh.chunkVisible("Z_MisSelect_SwR2", false);
                    mesh.chunkVisible("Z_MisSelect_RdRW", ((TypeGuidedMissileCarrier)fm.actor).getGuidedMissileUtils().getMissileLockState() == 2);
                    mesh.chunkVisible("Z_MisSelect_SwR1", true);
                    break;
                }
                if(j == 4)
                {
                    mesh.chunkVisible("Z_MisSelect_RdRW", false);
                    mesh.chunkVisible("Z_MisSelect_SwL2", false);
                    mesh.chunkVisible("Z_MisSelect_SwR1", false);
                    mesh.chunkVisible("Z_MisSelect_SwR2", false);
                    mesh.chunkVisible("Z_MisSelect_RdLW", ((TypeGuidedMissileCarrier)fm.actor).getGuidedMissileUtils().getMissileLockState() == 2);
                    mesh.chunkVisible("Z_MisSelect_SwL1", true);
                    break;
                }
                if(j != 6)
                    continue;
                mesh.chunkVisible("Z_MisSelect_RdLW", false);
                mesh.chunkVisible("Z_MisSelect_SwL1", false);
                mesh.chunkVisible("Z_MisSelect_SwL2", false);
                mesh.chunkVisible("Z_MisSelect_SwR1", false);
                mesh.chunkVisible("Z_MisSelect_RdRW", ((TypeGuidedMissileCarrier)fm.actor).getGuidedMissileUtils().getMissileLockState() == 2);
                mesh.chunkVisible("Z_MisSelect_SwR2", true);
                break;
            }

            if(j == fm.CT.Weapons[6].length)
            {
                mesh.chunkVisible("Z_MisSelect_RdLW", false);
                mesh.chunkVisible("Z_MisSelect_RdRW", false);
                mesh.chunkVisible("Z_MisSelect_SwL1", false);
                mesh.chunkVisible("Z_MisSelect_SwL2", false);
                mesh.chunkVisible("Z_MisSelect_SwR1", false);
                mesh.chunkVisible("Z_MisSelect_SwR2", false);
            }
        } else
        {
            mesh.chunkVisible("Z_MisSelect_RdLW", false);
            mesh.chunkVisible("Z_MisSelect_RdRW", false);
            mesh.chunkVisible("Z_MisSelect_SwL1", false);
            mesh.chunkVisible("Z_MisSelect_SwL2", false);
            mesh.chunkVisible("Z_MisSelect_SwR1", false);
            mesh.chunkVisible("Z_MisSelect_SwR2", false);
        }
*/
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
            mesh.chunkVisible("Z_GasPressureL", false);
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("HullDamage1", true);
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("Z_Oilpress2", false);
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
            mesh.chunkVisible("Z_Oilpress2", false);
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
            mesh.chunkVisible("Z_Oilpress1", false);
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
            mesh.chunkVisible("Z_Oilpress1", false);
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

    protected void drawSound(float f)  //TODO SOUND
    {
        if(Time.current() < 100L) return; // soon from mission start, some flight values are not set correctly by game system

        if(fm.AS.astateEngineStates[0] > 3 && (lLastEngineFireLeftWarned == -1L || Time.current() - lLastEngineFireLeftWarned > 7200L) && EngineFireLeftWarn != null)
        {
            EngineFireLeftWarn.play();
            lLastEngineFireLeftWarned = Time.current();
        }
        if(fm.AS.astateEngineStates[1] > 3 && (lLastEngineFireRightWarned == -1L || Time.current() - lLastEngineFireRightWarned > 7200L) && EngineFireRightWarn != null)
        {
            EngineFireRightWarn.play();
            lLastEngineFireRightWarned = Time.current();
        }
        float ffuel = fm.M.fuel * 2.20462262F;
        if(((F_14) aircraft()).bHasUnderTank) ffuel += (((F_14) aircraft()).checkfuel(0) + ((F_14) aircraft()).checkfuel(1)) * 2.20463F;
        if((((F_14) aircraft()).Bingofuel > ffuel && iBingoFuelWarned == 0) || (((F_14) aircraft()).Bingofuel - 100F > ffuel && iBingoFuelWarned == 1) && BingoFuelWarn != null)
        {
            BingoFuelWarn.play();
            iBingoFuelWarned++;
        }
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
    private Gun gun[];
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
    private SoundFX EngineFireLeftWarn;
    private SoundFX EngineFireRightWarn;
    private long lLastEngineFireLeftWarned;
    private long lLastEngineFireRightWarned;
    private SoundFX BingoFuelWarn;
    private int iBingoFuelWarned;
    private int hydroStat;
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
    private static final float rpmScale[] = {
        0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F,
        192F, 224F, 254F, 255.5F, 260F
    };


}