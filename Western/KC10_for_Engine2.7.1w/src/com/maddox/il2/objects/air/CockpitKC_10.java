
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;


public class CockpitKC_10 extends CockpitPilot
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
                setNew.throttle1 = 0.85F * setOld.throttle1 + fm.EI.engines[0].getControlThrottle() * 0.15F;
                setNew.throttle2 = 0.85F * setOld.throttle2 + fm.EI.engines[2].getControlThrottle() * 0.15F;
                setNew.prop1 = 0.85F * setOld.prop1 + fm.EI.engines[0].getControlProp() * 0.15F;
                setNew.prop2 = 0.85F * setOld.prop2 + fm.EI.engines[2].getControlProp() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f - 90F);
                    setOld.waypointAzimuth.setDeg(f - 90F);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                    if(fm.AS.listenLorenzBlindLanding && fm.AS.isAAFIAS)
                    {
                        setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                        setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                    } else
                    {
                        setNew.ilsLoc = 0.0F;
                        setNew.ilsGS = 0.0F;
                    }
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                    setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                }
                if(b25J != null)
                    setNew.PDI = b25J.fSightCurSideslip;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
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
        float prop1;
        float prop2;
        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float vspeed;
        float PDI;
        float ilsLoc;
        float ilsGS;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }

    }


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public CockpitKC_10()
    {
        super("3DO/Cockpit/B-25J/KC10.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictManf1 = 1.0F;
        pictManf2 = 1.0F;
        b25J = null;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        cockpitNightMats = (new String[] {
            "texture01_dmg", "texture01", "texture02_dmg", "texture02", "texture03_dmg", "texture03", "texture04_dmg", "texture04", "texture05_dmg", "texture05", 
            "texture06_dmg", "texture06", "texture21_dmg", "texture21", "texture25"
        });
        setNightMats(false);
        if(aircraft() instanceof B_25J1)
            b25J = (B_25J1)aircraft();
        interpPut(new Interpolater(), null, Time.current(), null);
        printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f)
    {
        mesh.chunkSetAngles("Z_Column", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 8F, 0.0F);
        mesh.chunkSetAngles("Z_AroneL", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 68F, 0.0F);
        mesh.chunkSetAngles("Z_AroneR", 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 68F, 0.0F);
        mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("Z_RPedalStep", 0.0F, -10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("Z_PedalWireR", 0.0F, 0.0F, -10F * fm.CT.getRudder());
        mesh.chunkSetAngles("Z_PedalWireL", 0.0F, 0.0F, 10F * fm.CT.getRudder());
        mesh.chunkSetAngles("zFlaps1", 0.0F, 38F * (pictFlap = 0.75F * pictFlap + 0.25F * fm.CT.FlapsControl), 0.0F);
        mesh.chunkSetAngles("zOilFlap1", 0.0F, -35F * fm.EI.engines[0].getControlRadiator(), 0.0F);
        mesh.chunkSetAngles("zOilFlap2", 0.0F, -35F * fm.EI.engines[2].getControlRadiator(), 0.0F);
        mesh.chunkSetAngles("zMix1", 0.0F, -45.8F * fm.EI.engines[0].getControlMix(), 0.0F);
        mesh.chunkSetAngles("zMix2", 0.0F, -45.8F * fm.EI.engines[2].getControlMix(), 0.0F);
        mesh.chunkSetAngles("zPitch1", 0.0F, -54F * interp(setNew.prop1, setOld.prop1, f), 0.0F);
        mesh.chunkSetAngles("zPitch2", 0.0F, -54F * interp(setNew.prop2, setOld.prop2, f), 0.0F);
        mesh.chunkSetAngles("zThrottle1", 0.0F, -49F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
        mesh.chunkSetAngles("zThrottle2", 0.0F, -49F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
        mesh.chunkSetAngles("zCompressor1", 0.0F, cvt(fm.EI.engines[0].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        mesh.chunkSetAngles("zCompressor2", 0.0F, cvt(fm.EI.engines[2].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        if(fm.Gears.cgear)
        {
            resetYPRmodifier();
            xyz[1] = cvt(fm.CT.getGearC(), 0.0F, 1.0F, 0.0F, 0.0135F);
            mesh.chunkSetLocate("Z_Gearc1", xyz, ypr);
        }
        if(fm.Gears.lgear)
        {
            resetYPRmodifier();
            xyz[1] = cvt(fm.CT.getGearL(), 0.0F, 1.0F, 0.0F, 0.0135F);
            mesh.chunkSetLocate("Z_GearL1", xyz, ypr);
        }
        if(fm.Gears.rgear)
        {
            resetYPRmodifier();
            xyz[1] = cvt(fm.CT.getGearR(), 0.0F, 1.0F, 0.0F, 0.0135F);
            mesh.chunkSetLocate("Z_GearR1", xyz, ypr);
        }
        mesh.chunkSetAngles("zFlaps2", 0.0F, 90F * fm.CT.getFlap(), 0.0F);
        mesh.chunkSetAngles("zHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("zMinute", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zSecond", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zAH1", 0.0F, -fm.Or.getKren(), 0.0F);
        resetYPRmodifier();
        xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        mesh.chunkSetLocate("zAH2", xyz, ypr);
        if((fm.AS.astateCockpitState & 0x40) == 0)
            mesh.chunkSetAngles("Z_Climb1", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("zTurnBank", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        mesh.chunkSetAngles("zBall", 0.0F, cvt(getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        mesh.chunkSetAngles("zPDI", 0.0F, cvt(setNew.PDI, -30F, 30F, -46.5F, 46.5F), 0.0F);
        mesh.chunkSetAngles("zSpeed", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("zAlt1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        mesh.chunkSetAngles("zAlt2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        mesh.chunkSetAngles("zCompass1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("zCompass2", 0.0F, -0.5F * setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("zCompass3", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
        if((fm.AS.astateCockpitState & 0x40) == 0)
        {
            mesh.chunkSetAngles("zMagnetic", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
            mesh.chunkSetAngles("zNavP", 0.0F, setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        }
        if((fm.AS.astateCockpitState & 0x40) == 0)
        {
            mesh.chunkSetAngles("zRPM1", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            mesh.chunkSetAngles("zRPM2", 0.0F, cvt(fm.EI.engines[2].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            mesh.chunkSetAngles("Z_Pres1", 0.0F, pictManf1 = 0.9F * pictManf1 + 0.1F * cvt(fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            mesh.chunkSetAngles("Z_Pres2", 0.0F, pictManf2 = 0.9F * pictManf2 + 0.1F * cvt(fm.EI.engines[2].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
        }
        mesh.chunkSetAngles("Z_FuelPres1", 0.0F, cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("Z_FuelPres2", 0.0F, cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("Z_Carbair1", 0.0F, -cvt(Atmosphere.temperature((float)fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        mesh.chunkSetAngles("Z_Carbair2", 0.0F, cvt(Atmosphere.temperature((float)fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        mesh.chunkSetAngles("Z_Temp1", 0.0F, -cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 102.5F), 0.0F);
        mesh.chunkSetAngles("Z_Temp2", 0.0F, cvt(fm.EI.engines[2].tWaterOut, 0.0F, 350F, 0.0F, 102.5F), 0.0F);
        mesh.chunkSetAngles("zOilTemp1", 0.0F, cvt(fm.EI.engines[0].tOilIn, -70F, 150F, 34F, -76F), 0.0F);
        mesh.chunkSetAngles("zOilTemp2", 0.0F, -cvt(fm.EI.engines[2].tOilIn, -70F, 150F, 34F, -76F), 0.0F);
        mesh.chunkSetAngles("Z_Oilpres1", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[2].tOilIn * fm.EI.engines[2].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("Z_Oilpres2", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilIn * fm.EI.engines[0].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("Z_Brkpres1", 0.0F, 118F * fm.CT.getBrake(), 0.0F);
        mesh.chunkSetAngles("zFuel1", 0.0F, -cvt(fm.M.fuel, 0.0F, 2332F, 0.0F, 95F), 0.0F);
        mesh.chunkSetAngles("zFuel2", 0.0F, cvt(fm.M.fuel, 0.0F, 2332F, 0.0F, 95F), 0.0F);
        mesh.chunkSetAngles("zFuel3", 0.0F, -cvt(fm.M.fuel, 0.0F, 1916F, 0.0F, 90.5F), 0.0F);
        mesh.chunkSetAngles("zFuel4", 0.0F, cvt(fm.M.fuel, 0.0F, 1916F, 0.0F, 90.5F), 0.0F);
        mesh.chunkSetAngles("zFuel5", 0.0F, -cvt(fm.M.fuel, 1916F, 3084F, 0.0F, 102.5F), 0.0F);
        mesh.chunkSetAngles("zFuel6", 0.0F, cvt(fm.M.fuel, 1916F, 3084F, 0.0F, 102.5F), 0.0F);
        if((fm.AS.astateCockpitState & 0x40) == 0)
            mesh.chunkSetAngles("zFreeAir", 0.0F, cvt(Atmosphere.temperature((float)fm.Loc.z) - 273.15F, -70F, 150F, -26.6F, 57F), 0.0F);
        mesh.chunkSetAngles("zHydPres", 0.0F, fm.Gears.bIsHydroOperable ? 165.5F : 0.0F, 0.0F);
        float f1 = 0.5F * fm.EI.engines[0].getRPM() + 0.5F * fm.EI.engines[2].getRPM();
        f1 = 2.5F * (float)Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        mesh.chunkSetAngles("zSuction", 0.0F, cvt(f1, 0.0F, 10F, 0.0F, 297F), 0.0F);
        mesh.chunkSetAngles("BL_Vert", cvt(setNew.ilsLoc, -63F, 63F, -45F, 45F), 0.0F, 0.0F);
        if(setNew.ilsGS >= 0.0F)
            mesh.chunkSetAngles("BL_Horiz", cvt(setNew.ilsGS, 0.0F, 0.5F, 0.0F, 40F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("BL_Horiz", cvt(setNew.ilsGS, -0.3F, 0.0F, -40F, 0.0F), 0.0F, 0.0F);
        if(fm.AS.isAAFIAS)
            mesh.chunkVisible("BL_Light", isOnBlindLandingMarker());
        mesh.chunkVisible("Z_GearRed1", fm.CT.getGear() > 0.01F && fm.CT.getGear() < 0.99F);
        mesh.chunkVisible("Z_GearCGreen1", fm.CT.getGearC() > 0.99F && fm.Gears.cgear);
        mesh.chunkVisible("Z_GearLGreen1", fm.CT.getGearL() > 0.99F && fm.Gears.lgear);
        mesh.chunkVisible("Z_GearRGreen1", fm.CT.getGearR() > 0.99F && fm.Gears.rgear);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 0x80) == 0);
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("Pricel_D0", false);
            mesh.chunkVisible("Pricel_D1", true);
            mesh.chunkVisible("Z_Z_RETICLE", false);
            mesh.chunkVisible("Z_Z_MASK", false);
        }
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("XGlassDamage", true);
            mesh.chunkVisible("XGlassDamage1", true);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Panel_D0", false);
            mesh.chunkVisible("Panel_D1", true);
            mesh.chunkVisible("zCompass3", false);
            mesh.chunkVisible("Z_FuelPres1", false);
            mesh.chunkVisible("Z_FuelPres2", false);
            mesh.chunkVisible("Z_Oilpres1", false);
            mesh.chunkVisible("Z_Oilpres2", false);
            mesh.chunkVisible("zOilTemp1", false);
            mesh.chunkVisible("zOilTemp2", false);
            mesh.chunkVisible("Z_Brkpres1", false);
            mesh.chunkVisible("zHydPres", false);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("XGlassDamage", true);
            mesh.chunkVisible("XHullDamage2", true);
        }
        if((fm.AS.astateCockpitState & 8) != 0)
            mesh.chunkVisible("XGlassDamage2", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("XHullDamage1", true);
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("XGlassDamage2", true);
        retoggleLight();
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
    private B_25J1 b25J;
    private static final float speedometerScale[] = {
        0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 
        296.5F, 308.5F, 324F, 338.5F
    };
    private static final float variometerScale[] = {
        -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 
        130F, 157F, 180F
    };
    private Point3d tmpP;
    private Vector3d tmpV;

    static 
    {
        Property.set(CLASS.THIS(), "normZNs", new float[] {
            0.75F, 0.75F, 1.27F, 1.27F
        });
    }







}
