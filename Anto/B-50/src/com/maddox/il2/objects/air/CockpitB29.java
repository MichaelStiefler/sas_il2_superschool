package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;

public class CockpitB29 extends CockpitPilot
{
    private class Variables
    {

        float throttle1;
        float throttle2;
        float throttle3;
        float throttle4;
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
                setNew.throttle2 = 0.85F * setOld.throttle2 + fm.EI.engines[1].getControlThrottle() * 0.15F;
                setNew.throttle3 = 0.85F * setOld.throttle3 + fm.EI.engines[2].getControlThrottle() * 0.15F;
                setNew.throttle4 = 0.85F * setOld.throttle4 + fm.EI.engines[3].getControlThrottle() * 0.15F;
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
                if(b29X != null)
                    setNew.PDI = b29X.fSightCurSideslip;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Pilot2_D" + aircraft().chunkDamageVisible("Pilot3"), false);
            aircraft().hierMesh().chunkVisible("Pilot3_D" + aircraft().chunkDamageVisible("Pilot3"), false);
            aircraft().hierMesh().chunkVisible("Head3_D0", false);
            aircraft().hierMesh().chunkVisible("HMask3_D0", false);
            aircraft().hierMesh().chunkVisible("Head2_D0", false);
            aircraft().hierMesh().chunkVisible("HMask2_D0", false);
            aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("Blister2_D0", false);
            aircraft().hierMesh().chunkVisible("Blister3_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Pilot2_D" + aircraft().chunkDamageVisible("Pilot3"), true);
        aircraft().hierMesh().chunkVisible("Pilot3_D" + aircraft().chunkDamageVisible("Pilot3"), true);
        aircraft().hierMesh().chunkVisible("Head3_D0", true);
        aircraft().hierMesh().chunkVisible("HMask3_D0", true);
        aircraft().hierMesh().chunkVisible("Head2_D0", true);
        aircraft().hierMesh().chunkVisible("HMask2_D0", true);
        aircraft().hierMesh().chunkVisible("Cockpit_D0", true);
        aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        aircraft().hierMesh().chunkVisible("Blister2_D0", true);
        aircraft().hierMesh().chunkVisible("Blister3_D0", true);
        super.doFocusLeave();
    }

    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public CockpitB29()
    {
        super("3DO/Cockpit/B-29/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictManf1 = 1.0F;
        pictManf2 = 1.0F;
        pictManf3 = 1.0F;
        pictManf4 = 1.0F;
        b29X = null;
        cockpitNightMats = (new String[] {
            "gauges1", "gauges1dmg", "gauges2", "gauges2dmg", "gauges3", "gauges3dmg", "gauges4", "gauges4dmg", "gauges5", "gauges5dmg", 
            "texture25"
        });
        setNightMats(false);
        if(aircraft() instanceof B_29X)
            b29X = (B_29X)aircraft();
        interpPut(new Interpolater(), null, Time.current(), null);
        printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f)
    {
        resetYPRmodifier();
        mesh.chunkSetAngles("ColumnL", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 8F, 0.0F);
        mesh.chunkSetAngles("ColumnL2", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 68F, 0.0F);
        mesh.chunkSetAngles("ColumnR", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 8F, 0.0F);
        mesh.chunkSetAngles("ColumnR2", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 68F, 0.0F);
        mesh.chunkSetAngles("rudderpedalR", 0.0F, -10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("rudderpedalL", 0.0F, 10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("rudderR01", 0.0F, -10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("rudderL01", 0.0F, 10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("mixture", 0.0F, -30F * fm.EI.engines[0].getControlMix(), 0.0F);
        mesh.chunkSetAngles("power4L", 0.0F, -49F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
        mesh.chunkSetAngles("power3L", 0.0F, -49F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
        mesh.chunkSetAngles("power2L", 0.0F, -49F * interp(setNew.throttle3, setOld.throttle3, f), 0.0F);
        mesh.chunkSetAngles("power1L", 0.0F, -49F * interp(setNew.throttle4, setOld.throttle4, f), 0.0F);
        float f1;
        if(Math.abs(fm.CT.FlapsControl - fm.CT.getFlap()) > 0.02F)
        {
            if(fm.CT.FlapsControl - fm.CT.getFlap() > 0.0F)
                f1 = -0.0299F;
            else
                f1 = -0F;
        } else
        {
            f1 = -0.0144F;
        }
        pictFlap = 0.8F * pictFlap + 0.2F * f1;
        resetYPRmodifier();
        Cockpit.xyz[1] = pictFlap;
        mesh.chunkSetAngles("Zflap", cvt(fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 125F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Zclock2R", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Zclock1R", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Zclock2L", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Zclock1L", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("ZAH1R", 0.0F, -fm.Or.getKren(), 0.0F);
        mesh.chunkSetAngles("ZAH1L", 0.0F, -fm.Or.getKren(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        mesh.chunkSetLocate("ZAH2R", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetLocate("ZAH2L", Cockpit.xyz, Cockpit.ypr);
        if((fm.AS.astateCockpitState & 0x40) == 0)
            mesh.chunkSetAngles("ZclimbL", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        mesh.chunkSetAngles("ZclimbR", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("ZlageL01", 0.0F, cvt(getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        mesh.chunkSetAngles("ZlageL", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        mesh.chunkSetAngles("ZlageR01", 0.0F, cvt(getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        mesh.chunkSetAngles("ZlageR", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        mesh.chunkSetAngles("zpdi", 0.0F, cvt(setNew.PDI, -30F, 30F, -46.5F, 46.5F), 0.0F);
        mesh.chunkSetAngles("ZspeedL", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("ZspeedR", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("zAlt2L", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        mesh.chunkSetAngles("zAlt1L", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        mesh.chunkSetAngles("zAlt2Rc", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        mesh.chunkSetAngles("zAlt1R", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        mesh.chunkSetAngles("zComp01", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("zComp02", 0.0F, setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        mesh.chunkSetAngles("zRComp", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
        if((fm.AS.astateCockpitState & 0x40) == 0)
        {
            mesh.chunkSetAngles("zCompass2L", 0.0F, -0.5F * setNew.azimuth.getDeg(f), 0.0F);
            mesh.chunkSetAngles("zCompass2R", 0.0F, -0.5F * setNew.azimuth.getDeg(f), 0.0F);
        }
        if((fm.AS.astateCockpitState & 0x40) == 0)
        {
            mesh.chunkSetAngles("ZrpmL", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            mesh.chunkSetAngles("ZrpmR", 0.0F, cvt(fm.EI.engines[2].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            mesh.chunkSetAngles("ZpressLside2", 0.0F, pictManf1 = 0.9F * pictManf1 + 0.1F * cvt(fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            mesh.chunkSetAngles("ZpressLside1", 0.0F, pictManf2 = 0.9F * pictManf2 + 0.1F * cvt(fm.EI.engines[1].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            mesh.chunkSetAngles("ZpressRside1", 0.0F, pictManf3 = 0.9F * pictManf3 + 0.1F * cvt(fm.EI.engines[2].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            mesh.chunkSetAngles("ZpressRside2", 0.0F, pictManf4 = 0.9F * pictManf4 + 0.1F * cvt(fm.EI.engines[3].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
        }
        mesh.chunkSetAngles("TrimwheelR", -104F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        mesh.chunkSetAngles("TrimwheelL", -104F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        mesh.chunkSetAngles("BL_Vert", cvt(setNew.ilsLoc, -63F, 63F, -45F, 45F), 0.0F, 0.0F);
        if(setNew.ilsGS >= 0.0F)
            mesh.chunkSetAngles("BL_Horiz", cvt(setNew.ilsGS, 0.0F, 0.5F, 0.0F, 40F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("BL_Horiz", cvt(setNew.ilsGS, -0.3F, 0.0F, -40F, 0.0F), 0.0F, 0.0F);
        if(fm.Gears.cgear)
            resetYPRmodifier();
        if(fm.Gears.lgear)
            resetYPRmodifier();
        if(fm.Gears.rgear)
            resetYPRmodifier();
        mesh.chunkVisible("Z_GearRed1", fm.CT.getGear() > 0.01F && fm.CT.getGear() < 0.99F);
        mesh.chunkVisible("Z_GearCGreen1", fm.CT.getGear() > 0.99F && fm.Gears.cgear);
        mesh.chunkVisible("Z_GearLGreen1", fm.CT.getGear() > 0.99F && fm.Gears.lgear);
        mesh.chunkVisible("Z_GearRGreen1", fm.CT.getGear() > 0.99F && fm.Gears.rgear);
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

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("XGlassDamage8", true);
            mesh.chunkVisible("gauges5L_dmg", true);
            mesh.chunkVisible("gauges5L", false);
            mesh.chunkVisible("gauges4L_dmg", true);
            mesh.chunkVisible("gauges4L", false);
            mesh.chunkVisible("zpdi", false);
            mesh.chunkVisible("zAlt2L", false);
            mesh.chunkVisible("zAlt1L", false);
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("XGlassDamage7", true);
            mesh.chunkVisible("gauges3L_dmg", true);
            mesh.chunkVisible("gauges3L", false);
            mesh.chunkVisible("gauges1L_dmg", true);
            mesh.chunkVisible("gauges1L", false);
            mesh.chunkVisible("stabiL_dmg", true);
            mesh.chunkVisible("stabiL", false);
            mesh.chunkVisible("ZclimbL", false);
            mesh.chunkVisible("ZspeedL", false);
            mesh.chunkVisible("ZrpmL", false);
            mesh.chunkVisible("ZrpmLR", false);
            mesh.chunkVisible("ZpressLside2", false);
            mesh.chunkVisible("ZpressLside1", false);
            mesh.chunkVisible("ZpressRside2", false);
            mesh.chunkVisible("ZpressRside1", false);
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("XGlassDamage2", true);
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("gauges5R_dmg", true);
            mesh.chunkVisible("gauges5R", false);
            mesh.chunkVisible("gauges4R_dmg", true);
            mesh.chunkVisible("gauges4R", false);
            mesh.chunkVisible("stabiR_dmg", true);
            mesh.chunkVisible("stabiR", false);
            mesh.chunkVisible("zRPM1", false);
            mesh.chunkVisible("zBoost2", false);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("XGlassDamage5", true);
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("XGlassDamage6", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("gauges2R_dmg", true);
            mesh.chunkVisible("gauges2R", false);
            mesh.chunkVisible("zFuel2", false);
            mesh.chunkVisible("zAlt2R", false);
            mesh.chunkVisible("zAlt1R", false);
        }
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("XGlassDamage1", true);
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictFlap;
    private float pictManf1;
    private float pictManf2;
    private float pictManf3;
    private float pictManf4;
    private B_29X b29X;
    private static final float speedometerScale[] = {
        0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 
        296.5F, 308.5F, 324F, 338.5F
    };
    private static final float variometerScale[] = {
        -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 
        130F, 157F, 180F
    };

    static 
    {
        Property.set(CLASS.THIS(), "normZNs", new float[] {
            1.0F, 1.0F, 1.25F, 1.0F
        });
    }
}