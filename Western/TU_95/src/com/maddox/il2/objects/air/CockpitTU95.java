
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit

public class CockpitTU95 extends CockpitPilot
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
                setNew.throttle1 = 0.85F * setOld.throttle1 + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle() * 0.15F;
                setNew.throttle2 = 0.85F * setOld.throttle2 + ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle() * 0.15F;
                setNew.throttle3 = 0.85F * setOld.throttle3 + ((FlightModelMain) (fm)).EI.engines[2].getControlThrottle() * 0.15F;
                setNew.throttle4 = 0.85F * setOld.throttle4 + ((FlightModelMain) (fm)).EI.engines[3].getControlThrottle() * 0.15F;
                setNew.prop1 = 0.85F * setOld.prop1 + ((FlightModelMain) (fm)).EI.engines[0].getControlProp() * 0.15F;
                setNew.prop2 = 0.85F * setOld.prop2 + ((FlightModelMain) (fm)).EI.engines[1].getControlProp() * 0.15F;
                setNew.prop3 = 0.85F * setOld.prop3 + ((FlightModelMain) (fm)).EI.engines[2].getControlProp() * 0.15F;
                setNew.prop4 = 0.85F * setOld.prop4 + ((FlightModelMain) (fm)).EI.engines[3].getControlProp() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                float radioaltnow = ((FlightModelMain) (fm)).getAltitude() - (float)Engine.land().HQ_Air(((FlightModelMain) (fm)).Loc.x, ((FlightModelMain) (fm)).Loc.y);
                radioaltnow = radioaltnow / (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getKren()))) / (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())));
                setNew.radioaltimeter = (5F * setOld.radioaltimeter + radioaltnow) / 6F;
                float brakepressurenow = ((FlightModelMain) (fm)).CT.BrakeControl * 9.0F;
                setNew.brakepressuremeter = (39F * setOld.brakepressuremeter + brakepressurenow) / 40F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                float f = waypointAzimuth();
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f);
                    setOld.waypointAzimuth.setDeg(f);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                    if(((FlightModelMain) (fm)).AS.listenLorenzBlindLanding && ((FlightModelMain) (fm)).AS.isILSBL)
                    {
                        setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                        setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                        bHSIILS = true;
                        bHSIDL = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
                    } else if(((FlightModelMain) (fm)).AS.listenTACAN)
                    {
                        setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                        setNew.ilsGS = 0.0F;
                        bHSITAC = true;
                        bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITGT = bHSIUHF = false;
                    } else if(((FlightModelMain) (fm)).AS.listenNDBeacon || ((FlightModelMain) (fm)).AS.listenYGBeacon)
                    {
                        setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                        setNew.ilsGS = 0.0F;
                        bHSIUHF = true;
                        bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = false;
                    } else
                    {
                        setNew.ilsLoc = 0.0F;
                        setNew.ilsGS = 0.0F;
                        bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
                    }
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                    bHSINAV = true;
                    bHSIDL = bHSIILS = bHSIMAN = bHSITAC = bHSITGT = bHSIUHF = false;
                }
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttle1;
        float throttle2;
        float throttle3;
        float throttle4;
        float prop1;
        float prop2;
        float prop3;
        float prop4;
        float altimeter;
        float radioaltimeter;
        float brakepressuremeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float vspeed;
        AnglesFork waypointDeviation;
        AnglesFork radioCompassAzimuth;
        float beaconDirection;
        float beaconRange;
        float ilsLoc;
        float ilsGS;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
            waypointDeviation = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    protected float waypointAzimuth()
    {
        WayPoint waypoint = ((FlightModelMain) (fm)).AP.way.curr();
        if(waypoint == null)
            return 0.0F;
        waypoint.getP(tmpP);
        tmpV.sub(tmpP, ((FlightModelMain) (fm)).Loc);
        float f;
        for(f = (float)(57.295779513082323D * Math.atan2(-((Tuple3d) (tmpV)).y, ((Tuple3d) (tmpV)).x)); f <= -180F; f += 180F);
        for(; f > 180F; f -= 180F);
        return f;
    }

    public CockpitTU95()
    {
        super("3DO/Cockpit/TU95/CockpitTU95.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictManf1 = 1.0F;
        pictManf2 = 1.0F;
        pictManf3 = 1.0F;
        pictManf4 = 1.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
        super.cockpitNightMats = (new String[] {
            "texture01_dmg", "texture01", "texture02_dmg", "texture02", "texture03_dmg", "texture03", "texture04_dmg", "texture04", "texture05_dmg", "texture05", 
            "texture06_dmg", "texture06", "texture21_dmg", "texture21", "texture25"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        super.mesh.chunkSetAngles("Z_Column", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * ((FlightModelMain) (fm)).CT.ElevatorControl) * 8F, 0.0F);
        super.mesh.chunkSetAngles("Z_AroneL", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (fm)).CT.AileronControl) * 68F, 0.0F);
        super.mesh.chunkSetAngles("Z_AroneR", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * ((FlightModelMain) (fm)).CT.AileronControl) * 68F, 0.0F);
        super.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * ((FlightModelMain) (fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * ((FlightModelMain) (fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, -10F * ((FlightModelMain) (fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 10F * ((FlightModelMain) (fm)).CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("zFlaps1", 0.0F, 38F * (pictFlap = 0.75F * pictFlap + 0.25F * ((FlightModelMain) (fm)).CT.FlapsControl), 0.0F);
        super.mesh.chunkSetAngles("zThrottle1", 0.0F, -49F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
        super.mesh.chunkSetAngles("zThrottle2", 0.0F, -49F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
        super.mesh.chunkSetAngles("zThrottle3", 0.0F, -49F * interp(setNew.throttle3, setOld.throttle3, f), 0.0F);
        super.mesh.chunkSetAngles("zThrottle4", 0.0F, -49F * interp(setNew.throttle4, setOld.throttle4, f), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(((FlightModelMain) (fm)).CT.GearControl, 0.0F, 1.0F, 0.0F, 0.0135F);
        super.mesh.chunkSetLocate("Z_GearL1", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetAngles("zFlaps2", 0.0F, 180F * ((FlightModelMain) (fm)).CT.getFlap(), 0.0F);
        super.mesh.chunkSetAngles("zHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("zMinute", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zSecond", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("zAH1", 0.0F, -((FlightModelMain) (fm)).Or.getKren(), 0.0F);
        super.mesh.chunkSetAngles("zAH11", 0.0F, -((FlightModelMain) (fm)).Or.getKren(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(((FlightModelMain) (fm)).Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        super.mesh.chunkSetLocate("zAH2", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("zAH22", Cockpit.xyz, Cockpit.ypr);
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x40) == 0)
        {
            super.mesh.chunkSetAngles("Z_Climb1", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
            super.mesh.chunkSetAngles("Z_Climb2", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        }
        w.set(fm.getW());
        ((FlightModelMain) (fm)).Or.transform(w);
        super.mesh.chunkSetAngles("zTurnBank", 0.0F, cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        super.mesh.chunkSetAngles("zTurnBank2", 0.0F, cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        super.mesh.chunkSetAngles("zBall", 0.0F, cvt(getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        super.mesh.chunkSetAngles("zBall2", 0.0F, cvt(getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        super.mesh.chunkSetAngles("zBall3", 0.0F, cvt(getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        super.mesh.chunkSetAngles("zPDI", 0.0F, cvt(setNew.waypointDeviation.getDeg(f), -90F, 90F, -46.5F, 46.5F), 0.0F);
        super.mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (fm)).Loc)).z, fm.getSpeedKMH()), 0.0F, 750.0F, 0.0F, 14F), speedometerScale), 0.0F);
        super.mesh.chunkSetAngles("zSpeed2", 0.0F, floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (fm)).Loc)).z, fm.getSpeedKMH()), 0.0F, 750.0F, 0.0F, 14F), speedometerScale), 0.0F);
        super.mesh.chunkSetAngles("zAlt1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F);
        super.mesh.chunkSetAngles("zAlt2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 1080F), 0.0F);
        super.mesh.chunkSetAngles("zAlt11", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 10800F), 0.0F);
        super.mesh.chunkSetAngles("zAlt22", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 1080F), 0.0F);
        super.mesh.chunkSetAngles("zCompass1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("zCompass2", 0.0F, -0.5F * setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("zCompass3", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("zCompass4", -setNew.azimuth.getDeg(f)-90F, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("zCompass44", -setNew.azimuth.getDeg(f)-90F, 0.0F, 0.0F);
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x40) == 0)
        {
            super.mesh.chunkSetAngles("zMagnetic", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
            super.mesh.chunkSetAngles("zNavP", 0.0F, setNew.waypointAzimuth.getDeg(f), 0.0F);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x40) == 0)
        {
            super.mesh.chunkSetAngles("zRPM1", 0.0F, cvt(((FlightModelMain) (fm)).EI.engines[0].getRPM(), 700.0F, 3600F, 0.0F, 323F), 0.0F);
            super.mesh.chunkSetAngles("zRPM2", 0.0F, cvt(((FlightModelMain) (fm)).EI.engines[1].getRPM(), 700.0F, 3600F, 0.0F, 323F), 0.0F);
            super.mesh.chunkSetAngles("zRPM3", 0.0F, cvt(((FlightModelMain) (fm)).EI.engines[2].getRPM(), 700.0F, 3600F, 0.0F, 323F), 0.0F);
            super.mesh.chunkSetAngles("zRPM4", 0.0F, cvt(((FlightModelMain) (fm)).EI.engines[3].getRPM(), 700.0F, 3600F, 0.0F, 323F), 0.0F);
        }
        super.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, cvt((1.0F + 0.05F * ((FlightModelMain) (fm)).EI.engines[0].tOilOut) * ((FlightModelMain) (fm)).EI.engines[0].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, cvt((1.0F + 0.05F * ((FlightModelMain) (fm)).EI.engines[1].tOilOut) * ((FlightModelMain) (fm)).EI.engines[1].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres3", 0.0F, cvt((1.0F + 0.05F * ((FlightModelMain) (fm)).EI.engines[2].tOilOut) * ((FlightModelMain) (fm)).EI.engines[2].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres4", 0.0F, cvt((1.0F + 0.05F * ((FlightModelMain) (fm)).EI.engines[3].tOilOut) * ((FlightModelMain) (fm)).EI.engines[3].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        super.mesh.chunkSetAngles("zFuel5", 0.0F, cvt(((FlightModelMain) (fm)).M.fuel, 0F, ((FlightModelMain) (fm)).M.maxFuel, -118.0F, 180.0F), 0.0F);
        super.mesh.chunkSetAngles("zFuel6", 0.0F, cvt(((FlightModelMain) (fm)).M.fuel, 0F, ((FlightModelMain) (fm)).M.maxFuel, -118.0F, 180.0F), 0.0F);
        super.mesh.chunkVisible("Z_GearRed1", ((FlightModelMain) (fm)).CT.getGear() > 0.01F && ((FlightModelMain) (fm)).CT.getGear() < 0.99F);
        super.mesh.chunkVisible("Z_GearCGreen1", ((FlightModelMain) (fm)).CT.getGear() > 0.99F && ((FlightModelMain) (fm)).Gears.cgear);
        super.mesh.chunkVisible("Z_GearLGreen1", ((FlightModelMain) (fm)).CT.getGear() > 0.99F && ((FlightModelMain) (fm)).Gears.lgear);
        super.mesh.chunkVisible("Z_GearRGreen1", ((FlightModelMain) (fm)).CT.getGear() > 0.99F && ((FlightModelMain) (fm)).Gears.rgear);
        super.mesh.chunkSetAngles("zRadioAltimeter", 0.0F, floatindex(cvt(setNew.radioaltimeter, 0.0F, 1500F, 0.0F, 31F), radioaltimeterScale), 0.0F);
        super.mesh.chunkSetAngles("Z_Brkpres1", 0.0F, floatindex(cvt(setNew.brakepressuremeter, 0.0F, 12.0F, 0.0F, 12.0F), brakepressureScale), 0.0F);
        super.mesh.chunkSetAngles("zTAS", 0.0F, floatindex(cvt(((FlightModelMain) (fm)).getSpeedKMH(), 400F, 1100F, 0.0F, 2.0F), tasScale), 0.0F);
        super.mesh.chunkSetAngles("zTAS2", 0.0F, floatindex(cvt(((FlightModelMain) (fm)).getSpeedKMH(), 400F, 1100F, 0.0F, 2.0F), tasScale), 0.0F);
        float ilsloctmp = setNew.ilsLoc * setNew.ilsLoc * ((setNew.ilsLoc < 0)? -1F : 1F);
        Cockpit.xyz[0] = -cvt(ilsloctmp, -10000F, 10000F, -0.020F, 0.020F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        super.mesh.chunkSetLocate("BL_Vert", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("BL_Vert2", Cockpit.xyz, Cockpit.ypr);
        float ilsgstmp = setNew.ilsGS * setNew.ilsGS * ((setNew.ilsGS < 0)? -1F : 1F);
        Cockpit.xyz[1] = cvt(ilsgstmp, -0.25F, 0.25F, -0.016F, 0.016F);
        Cockpit.xyz[0] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        super.mesh.chunkSetLocate("BL_Horiz", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkSetLocate("BL_Horiz2", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        float BeaconDistanceInKm = getBeaconDistance() / 1000F;
        if(!useRealisticNavigationInstruments())
        {
            WayPoint waypoint = ((FlightModelMain) (fm)).AP.way.curr();
            if(waypoint != null)
            {
                Point3d P1 = new Point3d();
                Vector3d V = new Vector3d();
                waypoint.getP(P1);
                V.sub(P1, ((FlightModelMain) (fm)).Loc);
                BeaconDistanceInKm = (float)Math.sqrt(V.x * V.x + V.y * V.y) / 1000F;
            }
        }
        mesh.chunkSetAngles("Z_kilometers_1", cvt((float)Math.floor(BeaconDistanceInKm), 0.0F, 1000F, 0.0F, 36000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_kilometers_10", cvt((float)Math.floor(BeaconDistanceInKm / 10F), 0.0F, 100F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_kilometers_100", cvt((float)Math.floor(BeaconDistanceInKm / 100F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_kilometers_1_2", cvt((float)Math.floor(BeaconDistanceInKm), 0.0F, 1000F, 0.0F, 36000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_kilometers_10_2", cvt((float)Math.floor(BeaconDistanceInKm / 10F), 0.0F, 100F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_kilometers_100_2", cvt((float)Math.floor(BeaconDistanceInKm / 100F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("HSI", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F);
        mesh.chunkSetAngles("HSI2", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F);
        float course = (float)Math.floor(setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F);
        if(course < 0F)
            course += 360F;
        mesh.chunkSetAngles("Z_course_1", cvt(course, 0.0F, 1000F, 0.0F, 36000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_course_10", cvt((float)Math.floor(course / 10F), 0.0F, 100F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_course_100", cvt((float)Math.floor(course / 100F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_course_1_2", cvt(course, 0.0F, 1000F, 0.0F, 36000F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_course_10_2", cvt((float)Math.floor(course / 10F), 0.0F, 100F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_course_100_2", cvt((float)Math.floor(course / 100F), 0.0F, 10F, 0.0F, 360F), 0.0F, 0.0F);
    }

    public void reflectCockpitState()
    {
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x80) == 0);
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("Pricel_D0", false);
            super.mesh.chunkVisible("Pricel_D1", true);
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
            super.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage", true);
            super.mesh.chunkVisible("XGlassDamage1", true);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("Panel_D0", false);
            super.mesh.chunkVisible("Panel_D1", true);
            super.mesh.chunkVisible("zCompass3", false);
            super.mesh.chunkVisible("Z_FuelPres1", false);
            super.mesh.chunkVisible("Z_FuelPres2", false);
            super.mesh.chunkVisible("Z_FuelPres3", false);
            super.mesh.chunkVisible("Z_FuelPres4", false);
            super.mesh.chunkVisible("Z_Oilpres1", false);
            super.mesh.chunkVisible("Z_Oilpres2", false);
            super.mesh.chunkVisible("Z_Oilpres3", false);
            super.mesh.chunkVisible("Z_Oilpres4", false);
            super.mesh.chunkVisible("zOilTemp1", false);
            super.mesh.chunkVisible("zOilTemp2", false);
            super.mesh.chunkVisible("Z_Brkpres1", false);
            super.mesh.chunkVisible("zHydPres", false);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage", true);
            super.mesh.chunkVisible("XHullDamage2", true);
        }
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 8) != 0)
            super.mesh.chunkVisible("XGlassDamage2", true);
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x10) != 0)
            super.mesh.chunkVisible("XHullDamage1", true);
        if((((FlightModelMain) (fm)).AS.astateCockpitState & 0x20) != 0)
            super.mesh.chunkVisible("XGlassDamage2", true);
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
    private float pictFlap;
    private float pictGear;
    private float pictManf1;
    private float pictManf2;
    private float pictManf3;
    private float pictManf4;
    private boolean bHSIDL;
    private boolean bHSIILS;
    private boolean bHSIMAN;
    private boolean bHSINAV;
    private boolean bHSITAC;
    private boolean bHSITGT;
    private boolean bHSIUHF;
    private static final float speedometerScale[] = {
        0.0F, 4.5F, 31.4F, 62.95F, 92.9F, 119.8F, 146.5F, 171.5F, 195.6F, 220.4F, 
        244.4F, 267.3F, 293F, 316.7F, 344F
    };
    private static final float variometerScale[] = {
        -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 
        130F, 157F, 180F
    };
    private static final float radioaltimeterScale[] = {
          0F,  50F, 100F, 159F, 187F, 208F, 225F, 237F, 249F, 257F, 
        265F, 271F, 277F, 282F, 286F, 289F, 293F, 296F, 298F, 301F,
        303F, 303.8F, 304.6F, 305.4F, 306.2F, 307.0F, 307.8F, 308.5F, 309.2F, 309.9F,
        310.6F, 311.3F
    };
    private static final float brakepressureScale[] = {
          0.0F,  22.5F,  45.0F,  67.5F,  90.0F, 100.5F, 111.0F, 122.5F, 134.0F, 180.0F,
        225.0F, 247.5F, 270.0F
    };
    private static final float tasScale[] = {
        -163F, 0.0F, 163F
    };
    private Point3d tmpP;
    private Vector3d tmpV;

}