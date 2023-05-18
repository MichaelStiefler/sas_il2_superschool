// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 8/11/2013 3:16:03 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitYak_36.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Yak_36, AircraftLH, Aircraft

public class CockpitYak_36 extends CockpitPilot
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
                setNew.throttle2 = 0.85F * setOld.throttle2 + fm.EI.engines[1].getControlThrottle() * 0.15F;
                setNew.starter1 = 0.94F * setOld.starter1 + 0.06F * (fm.EI.engines[0].getStage() <= 0 || fm.EI.engines[0].getStage() >= 6 ? 0.0F : 1.0F);
                setNew.starter2 = 0.94F * setOld.starter2 + 0.06F * (fm.EI.engines[1].getStage() <= 0 || fm.EI.engines[1].getStage() >= 6 ? 0.0F : 1.0F);
                setNew.altimeter = fm.getAltitude();
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), getBeaconDirection());
                    float f = waypointAzimuth();
                    setNew.compassRim.setDeg(f - 90F);
                    setOld.compassRim.setDeg(f - 90F);
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), waypointAzimuth() - setOld.azimuth.getDeg(1.0F));
                    setNew.compassRim.setDeg(0.0F);
                    setOld.compassRim.setDeg(0.0F);
                }
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
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
        float starter1;
        float starter2;
        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork compassRim;
        float vspeed;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            compassRim = new AnglesFork();
        }

    }
    
    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("CF_D0", false);
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
            aircraft().hierMesh().chunkVisible("CF_D0", true);
            super.doFocusLeave();
            return;
        }
    }


    public CockpitYak_36()
    {
        super("3DO/Cockpit/MiG-9/yak.him", "bf109");
        bNeedSetUp = true;
        setOld = new Variables();
        setNew = new Variables();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictETP = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictTLck = 0.0F;
        pictMet1 = 0.0F;
        pictMet2 = 0.0F;
        pictETrm = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        cockpitNightMats = (new String[] {
            "gauges_01", "gauges_02", "gauges_03", "gauges_04", "gauges_05", "Dgauges_01", "Dgauges_02", "Dgauges_03", "Dgauges_05"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(acoustics != null)
            acoustics.globFX = new ReverbFXRoom(0.45F);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f)
    {
        mesh.chunkSetAngles("Canopy", 0.0F, cvt(fm.CT.getCockpitDoor(), 0.01F, 0.8F, 0.0F, 94F), 0.0F);
        mesh.chunkSetAngles("CnOpenLvr", cvt(fm.CT.getCockpitDoor(), 0.01F, 0.08F, 0.0F, -94F), 0.0F, 0.0F);
        mesh.chunkSetAngles("GearHandle", 0.0F, 0.0F, 50F * (pictGear = 0.82F * pictGear + 0.18F * fm.CT.GearControl));
        mesh.chunkSetAngles("FlapHandle", 0.0F, 0.0F, 111F * (pictFlap = 0.88F * pictFlap + 0.12F * fm.CT.FlapsControl));
        mesh.chunkSetAngles("TQHandle1", 0.0F, 0.0F, -40.909F * interp(setNew.throttle1, setOld.throttle1, f));
        mesh.chunkSetAngles("TQHandle2", 0.0F, 0.0F, -40.909F * interp(setNew.throttle2, setOld.throttle2, f));
        mesh.chunkSetAngles("NossleLvr1", 0.0F, 0.0F, -40.909F * interp(setNew.throttle1, setOld.throttle1, f));
        mesh.chunkSetAngles("NossleLvr2", 0.0F, 0.0F, -40.909F * interp(setNew.throttle2, setOld.throttle2, f));
        mesh.chunkSetAngles("Lvr1", 0.0F, 0.0F, -25F * (pictTLck = 0.85F * pictTLck + 0.15F * (fm.Gears.bTailwheelLocked ? 1.0F : 0.0F)));
        if(fm.CT.getTrimElevatorControl() != pictETP)
        {
            if(fm.CT.getTrimElevatorControl() - pictETP > 0.0F)
            {
                mesh.chunkSetAngles("ElevTrim", 0.0F, -30F, 0.0F);
                pictETrm = Time.current();
            } else
            {
                mesh.chunkSetAngles("ElevTrim", 0.0F, 30F, 0.0F);
                pictETrm = Time.current();
            }
            pictETP = fm.CT.getTrimElevatorControl();
        } else
        if((float)Time.current() > pictETrm + 500F)
        {
            mesh.chunkSetAngles("ElevTrim", 0.0F, 0.0F, 0.0F);
            pictETrm = Time.current() + 0x7a120L;
        }
        resetYPRmodifier();
        xyz[1] = cvt(fm.CT.getRudder(), -1F, 1.0F, -0.035F, 0.035F);
        mesh.chunkSetLocate("Pedal_L", xyz, ypr);
        xyz[1] = -xyz[1];
        mesh.chunkSetLocate("Pedal_R", xyz, ypr);
        mesh.chunkSetAngles("FLCSA", 0.0F, 0.0F, 10F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl));
        mesh.chunkSetAngles("FLCSB", 0.0F, 10F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F);
        mesh.chunkSetAngles("NeedRPM1", 0.0F, floatindex(cvt(fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 14F), rpmScale), 0.0F);
        pictMet1 = 0.96F * pictMet1 + 0.04F * (0.6F * fm.EI.engines[0].getThrustOutput() * fm.EI.engines[0].getControlThrottle() * (fm.EI.engines[0].getStage() != 6 ? 0.02F : 1.0F));
        mesh.chunkSetAngles("NeedExhstPress1", 0.0F, cvt(pictMet1, 0.0F, 1.0F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("NeedFuelPress1", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.55F, 0.0F, 1.0F, 0.0F, 284F), 0.0F, 0.0F);
        mesh.chunkSetAngles("NeedExstT1", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 1200F, 0.0F, 112F), 0.0F);
        mesh.chunkSetAngles("NeedOilP1", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 6.46F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("NeedRPM2", 0.0F, floatindex(cvt(fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 14F), rpmScale), 0.0F);
        pictMet2 = 0.96F * pictMet2 + 0.04F * (0.6F * fm.EI.engines[1].getThrustOutput() * fm.EI.engines[1].getControlThrottle() * (fm.EI.engines[1].getStage() != 6 ? 0.02F : 1.0F));
        mesh.chunkSetAngles("NeedExhstPress2", 0.0F, cvt(pictMet2, 0.0F, 1.0F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("NeedFuelPress2", cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.55F, 0.0F, 1.0F, 0.0F, 284F), 0.0F, 0.0F);
        mesh.chunkSetAngles("NeedExstT2", 0.0F, cvt(fm.EI.engines[1].tWaterOut, 0.0F, 1200F, 0.0F, 112F), 0.0F);
        mesh.chunkSetAngles("NeedOilP2", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness(), 0.0F, 6.46F, 0.0F, 270F), 0.0F);
        mesh.chunkSetAngles("NeedFuel1", 0.0F, floatindex(cvt(fm.M.fuel, 0.0F, 864F, 0.0F, 4F), fuelScale), 0.0F);
        mesh.chunkSetAngles("NeedFuel2", 0.0F, floatindex(cvt(fm.M.fuel, 864F, 1728F, 0.0F, 4F), fuelScale), 0.0F);
        mesh.chunkSetAngles("NeedAlt_Km", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 60000F, 0.0F, 2160F), 0.0F);
        mesh.chunkSetAngles("NeedAlt_M", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 60000F, 0.0F, 21600F), 0.0F);
        if(useRealisticNavigationInstruments())
        {
            mesh.chunkSetAngles("NeedCompassB", 0.0F, setNew.azimuth.getDeg(f) - setNew.compassRim.getDeg(f), 0.0F);
            mesh.chunkSetAngles("NeedCompassA", 0.0F, -setNew.compassRim.getDeg(f), 0.0F);
        } else
        {
            mesh.chunkSetAngles("NeedCompassA", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
            mesh.chunkSetAngles("NeedCompassB", 0.0F, 0.0F, 0.0F);
        }
        mesh.chunkSetAngles("NeedSpeed", 0.0F, cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 1200F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("NeedClimb", 0.0F, cvt(setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        mesh.chunkSetAngles("NeedAHCyl", 0.0F, -fm.Or.getKren() + 180F, 0.0F);
        mesh.chunkSetAngles("NeedAHBar", 0.0F, 0.0F, -fm.Or.getTangage());
        mesh.chunkSetAngles("NeedTurn", 0.0F, cvt(getBall(8D), -8F, 8F, -15F, 15F), 0.0F);
        mesh.chunkSetAngles("NeedDF", 0.0F, cvt(setNew.waypointAzimuth.getDeg(f * 0.2F), -90F, 90F, -16.5F, 16.5F), 0.0F);
        mesh.chunkSetAngles("NeedHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("NeedMin", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("NeedStarter1", cvt(setNew.starter1, 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        mesh.chunkSetAngles("NeedStarter2", cvt(setNew.starter2, 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        mesh.chunkSetAngles("NeedEmrgAirP", -63.5F, 0.0F, 0.0F);
        mesh.chunkSetAngles("NeedAirSysP", fm.Gears.isHydroOperable() ? -133.5F : 0.0F, 0.0F, 0.0F);
        mesh.chunkVisible("FlareGearUp_R", fm.CT.getGear() < 0.01F || !fm.Gears.rgear);
        mesh.chunkVisible("FlareGearUp_L", fm.CT.getGear() < 0.01F || !fm.Gears.lgear);
        mesh.chunkVisible("FlareGearUp_C", fm.CT.getGear() < 0.01F);
        mesh.chunkVisible("FlareGearDn_R", fm.CT.getGear() > 0.99F && fm.Gears.rgear);
        mesh.chunkVisible("FlareGearDn_L", fm.CT.getGear() > 0.99F && fm.Gears.lgear);
        mesh.chunkVisible("FlareGearDn_C", fm.CT.getGear() > 0.99F);
        mesh.chunkVisible("FlareFuel", fm.M.fuel < 296.1F);
    }

    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(10F);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("Z_Z_RETICLE", false);
            mesh.chunkVisible("Z_Z_MASK", false);
        }
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("DamageGlass2", true);
            mesh.chunkVisible("DamageGlass3", true);
        }
        if((fm.AS.astateCockpitState & 0x80) == 0);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Gages1_D0", false);
            mesh.chunkVisible("Gages1_D1", true);
            mesh.chunkVisible("NeedSpeed", false);
            mesh.chunkVisible("NeedClimb", false);
            mesh.chunkVisible("NeedAlt_Km", false);
            mesh.chunkVisible("NeedAlt_M", false);
            mesh.chunkVisible("NeedDF", false);
            mesh.chunkVisible("NeedCompassA", false);
            mesh.chunkVisible("NeedCompassB", false);
            mesh.chunkVisible("DamageHull1", true);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("Gages3_D0", false);
            mesh.chunkVisible("Gages3_D1", true);
            mesh.chunkVisible("NeedHour", false);
            mesh.chunkVisible("NeedMin", false);
            mesh.chunkVisible("NeedRPM1", false);
            mesh.chunkVisible("NeedExhstPress1", false);
            mesh.chunkVisible("DamageHull3", true);
        }
        if((fm.AS.astateCockpitState & 8) == 0);
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("Gages4_D0", false);
            mesh.chunkVisible("Gages4_D1", true);
            mesh.chunkVisible("NeedRPM2", false);
            mesh.chunkVisible("NeedOilP1", false);
            mesh.chunkVisible("NeedFuel1", false);
            mesh.chunkVisible("NeedExstT1", false);
            mesh.chunkVisible("DamageHull2", true);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("Gages5_D0", false);
            mesh.chunkVisible("Gages5_D1", true);
            mesh.chunkVisible("NeedOilP2", false);
            mesh.chunkVisible("NeedExhstPress2", false);
            mesh.chunkVisible("NeedExstT2", false);
            mesh.chunkVisible("NeedFuel2", false);
            mesh.chunkVisible("", false);
            mesh.chunkVisible("", false);
            mesh.chunkVisible("", false);
            mesh.chunkVisible("", false);
        }
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

    public void doToggleDim()
    {
    }

    private boolean bNeedSetUp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private float pictAiler;
    private float pictElev;
    private float pictETP;
    private float pictFlap;
    private float pictGear;
    private float pictTLck;
    private float pictMet1;
    private float pictMet2;
    private float pictETrm;
    private static final float rpmScale[] = {
        0.0F, 8F, 23.5F, 40F, 58.5F, 81F, 104.5F, 130.2F, 158.5F, 187F, 
        217.5F, 251.1F, 281.5F, 289.5F, 295.5F
    };
    private static final float fuelScale[] = {
        0.0F, 18.5F, 49F, 80F, 87F
    };
    private Point3d tmpP;
    private Vector3d tmpV;







}