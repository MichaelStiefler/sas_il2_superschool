package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitB10B extends CockpitPilot
{
    private class Variables
    {

        float throttle1;
        float throttle2;
        float prop1;
        float prop2;
        float mix1;
        float mix2;
        float man1;
        float man2;
        float altimeter;
        AnglesFork azimuth;
        float vspeed;

        private Variables()
        {
            azimuth = new AnglesFork();
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
                setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
                setNew.prop1 = 0.9F * setOld.prop1 + 0.1F * fm.EI.engines[0].getControlProp();
                setNew.mix1 = 0.8F * setOld.mix1 + 0.2F * fm.EI.engines[0].getControlMix();
                setNew.throttle2 = 0.9F * setOld.throttle2 + 0.1F * fm.EI.engines[1].getControlThrottle();
                setNew.prop2 = 0.9F * setOld.prop2 + 0.1F * fm.EI.engines[1].getControlProp();
                setNew.mix2 = 0.8F * setOld.mix2 + 0.2F * fm.EI.engines[1].getControlMix();
                setNew.man1 = 0.92F * setOld.man1 + 0.08F * fm.EI.engines[0].getManifoldPressure();
                setNew.man2 = 0.92F * setOld.man2 + 0.08F * fm.EI.engines[1].getManifoldPressure();
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        WayPoint waypoint = fm.AP.way.curr();
        if(waypoint == null)
            return 0.0F;
        waypoint.getP(tmpP);
        tmpV.sub(tmpP, fm.Loc);
        float f;
        for(f = (float)(57.295779513082323D * Math.atan2(-tmpV.y, tmpV.x)); f <= -180F; f += 180F);
        for(; f > 180F; f -= 180F);
        return f;
    }

    public CockpitB10B()
    {
        super("3DO/Cockpit/B-10B-FRONT/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        cockpitNightMats = (new String[] {
            "256-10_Gauges6", "256-16_Gauges7", "256-5_Gauges1", "256-6_Gauges2", "256-7_Gauges3_DMG", "256-7_Gauges3", "256-8_Gauges4", "256-9_Gauges5_DMG", "256-9_Gauges5", "256-18", 
            "256-21", "256-22"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(acoustics != null)
            acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f)
    {
        resetYPRmodifier();
        mesh.chunkSetAngles("Canopy", 0.0F, cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, -120F), 0.0F);
        mesh.chunkSetAngles("Z_Throtle1", 0.0F, -70F * interp(setNew.throttle1, setOld.throttle1, f), 0.0F);
        mesh.chunkSetAngles("Z_Throtle2", 0.0F, -70F * interp(setNew.throttle2, setOld.throttle2, f), 0.0F);
        mesh.chunkSetAngles("Z_Prop1", 0.0F, -70F * interp(setNew.prop1, setOld.prop1, f), 0.0F);
        mesh.chunkSetAngles("Z_Prop2", 0.0F, -70F * interp(setNew.prop2, setOld.prop2, f), 0.0F);
        mesh.chunkSetAngles("Z_Mixture1", 0.0F, -47F * interp(setNew.mix1, setOld.mix1, f), 0.0F);
        mesh.chunkSetAngles("Z_Mixture2", 0.0F, -47F * interp(setNew.mix2, setOld.mix2, f), 0.0F);
        mesh.chunkSetAngles("Z_Supercharger1", 0.0F, 73F - 73F * (float)fm.EI.engines[0].getControlCompressor(), 0.0F);
        mesh.chunkSetAngles("Z_Supercharger2", 0.0F, 73F - 73F * (float)fm.EI.engines[1].getControlCompressor(), 0.0F);
        mesh.chunkSetAngles("Z_BombBay1", 0.0F, 40F * fm.CT.BayDoorControl, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = -0.1F * fm.CT.getRudder();
        mesh.chunkSetLocate("Z_LeftPedal1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = 0.1F * fm.CT.getRudder();
        mesh.chunkSetLocate("Z_RightPedal1", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Column1", 0.0F, 0.0F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 8.8F);
        mesh.chunkSetAngles("Z_Column2", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 72.5F, 0.0F);
        if(fm.Gears.lgear)
            mesh.chunkSetAngles("Z_GearLInd", 0.0F, 86F * fm.CT.getGear(), 0.0F);
        if(fm.Gears.rgear)
            mesh.chunkSetAngles("Z_GearRInd", 0.0F, -86F * fm.CT.getGear(), 0.0F);
        if(fm.Gears.cgear)
            mesh.chunkSetAngles("Z_GearCInd", 0.0F, 86F * fm.CT.getGear(), 0.0F);
        mesh.chunkSetAngles("Z_FlapInd", 0.0F, -67.5F * fm.CT.getFlap(), 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        mesh.chunkSetAngles("Z_Altimeter2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        mesh.chunkSetAngles("Z_Speedometer1", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 804.6721F, 0.0F, 10F), speedometerScale), 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("Z_TurnBank1", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        mesh.chunkSetAngles("Z_TurnBank2", 0.0F, cvt(getBall(8D), -8F, 8F, -16F, 16F), 0.0F);
        if((fm.AS.astateCockpitState & 0x40) == 0)
            mesh.chunkSetAngles("Z_Climb1", 0.0F, cvt(setNew.vspeed, -10.159F, 10.159F, -180F, 180F), 0.0F);
        mesh.chunkSetAngles("Z_Compass1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_Compass2", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        if((fm.AS.astateCockpitState & 0x40) == 0)
            mesh.chunkSetAngles("Z_RPM1", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 305F), 0.0F);
        mesh.chunkSetAngles("Z_RPM2", 0.0F, cvt(fm.EI.engines[1].getRPM(), 0.0F, 5000F, 0.0F, 305F), 0.0F);
        mesh.chunkSetAngles("Z_Fuel1", 0.0F, cvt(fm.M.fuel, 0.0F, 1344F, 0.0F, 70.5F), 0.0F);
        mesh.chunkSetAngles("Z_FuelPres1", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.3F, 0.0F, 301F), 0.0F);
        mesh.chunkSetAngles("Z_FuelPres2", 0.0F, cvt(fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.3F, 0.0F, 301F), 0.0F);
        mesh.chunkSetAngles("Z_Temp1", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 87.5F), 0.0F);
        mesh.chunkSetAngles("Z_Temp2", 0.0F, cvt(fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 87.5F), 0.0F);
        if((fm.AS.astateCockpitState & 0x40) == 0)
            mesh.chunkSetAngles("Z_Pres1", 0.0F, cvt(setNew.man1, 0.3386378F, 1.693189F, 0.0F, 285.5F), 0.0F);
        else
            mesh.chunkSetAngles("Z_Pres1", 0.0F, cvt(setNew.man1, 0.3386378F, World.Rnd().nextFloat(1.0F, 2.0F), 0.0F, 285.5F), 0.0F);
        mesh.chunkSetAngles("Z_Pres2", 0.0F, cvt(setNew.man2, 0.3386378F, 1.693189F, 0.0F, 285.5F), 0.0F);
        mesh.chunkSetAngles("Z_Oil1", 0.0F, cvt(fm.EI.engines[0].tOilOut, 50F, 150F, 0.0F, 77F), 0.0F);
        mesh.chunkSetAngles("Z_Oil2", 0.0F, cvt(fm.EI.engines[1].tOilOut, 50F, 150F, 0.0F, 77F), 0.0F);
        mesh.chunkSetAngles("Z_Oilpres1", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, 302.5F), 0.0F);
        mesh.chunkSetAngles("Z_Oilpres2", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness(), 0.0F, 7.45F, 0.0F, 302.5F), 0.0F);
        mesh.chunkSetAngles("Z_CarbIn1", 0.0F, cvt(Atmosphere.temperature((float)fm.Loc.z) - 273.15F, -15F, 55F, -35F, 35F), 0.0F);
        mesh.chunkSetAngles("Z_CarbIn2", 0.0F, cvt(Atmosphere.temperature((float)fm.Loc.z) - 273.15F, -15F, 55F, -35F, 35F), 0.0F);
        mesh.chunkSetAngles("Z_Hydro1", 0.0F, cvt(fm.Gears.isHydroOperable() ? 0.8F : 0.0F, 0.0F, 1.0F, 0.0F, 180F), 0.0F);
        mesh.chunkSetAngles("Z_OxyPres1", 0.0F, 228F, 0.0F);
        mesh.chunkSetAngles("Z_OxyQ1", 0.0F, 279F, 0.0F);
        mesh.chunkSetAngles("Z_AH1", 0.0F, fm.Or.getKren(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, -0.025F, 0.025F);
        mesh.chunkSetLocate("Z_AH2", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("zFreeAir", 0.0F, floatindex(cvt(Atmosphere.temperature((float)fm.Loc.z), 223.09F, 323.09F, 0.0F, 4F), temperatureScale), 0.0F);
        mesh.chunkSetAngles("Z_Hour1", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Z_Minute1", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Z_Second1", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        float f1 = 0.5F * fm.EI.engines[0].getRPM() + 0.5F * fm.EI.engines[1].getRPM();
        f1 = 2.5F * (float)Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        mesh.chunkSetAngles("zSuction", 0.0F, cvt(f1, 0.0F, 10F, 0.0F, 297F), 0.0F);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("XHullDamage1", true);
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("XGlassDamage3", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("XHullDamage1", true);
            mesh.chunkVisible("Panel_D0", false);
            mesh.chunkVisible("Z_Pres2", false);
            mesh.chunkVisible("Z_Altimeter1", false);
            mesh.chunkVisible("Z_Altimeter2", false);
            mesh.chunkVisible("Z_Oilpres1", false);
            mesh.chunkVisible("Z_FuelPres1", false);
            mesh.chunkVisible("Panel_D1", true);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("XGlassDamage1", true);
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("XHullDamage2", true);
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("XGlassDamage2", true);
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("XGlassDamage4", true);
            mesh.chunkVisible("XHullDamage3", true);
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

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private static final float speedometerScale[] = {
        0.0F, 16.5F, 79.5F, 143F, 206.5F, 229.5F, 251F, 272.5F, 294F, 316F, 
        339.5F
    };
    private static final float temperatureScale[] = {
        -46F, 0.0F, 19.5F, 43F, 67F
    };
    private Point3d tmpP;
    private Vector3d tmpV;
}
