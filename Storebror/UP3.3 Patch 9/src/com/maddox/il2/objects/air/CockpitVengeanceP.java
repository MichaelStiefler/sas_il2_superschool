package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitVengeanceP extends CockpitPilot
{
    private class Variables
    {

        float throttle;
        float prop;
        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float vspeed;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
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
                setNew.throttle = 0.85F * setOld.throttle + fm.CT.PowerControl * 0.15F;
                setNew.prop = 0.85F * setOld.prop + fm.CT.getStepControl() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), f);
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    public CockpitVengeanceP()
    {
        super("3DO/Cockpit/VengeanceP/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictFlap = 0.0F;
        pictGear = 0.0F;
        pictManf = 1.0F;
        bNeedSetUp = true;
        cockpitNightMats = (new String[] {
            "aigtemp", "instru01", "instru01_D", "instru02", "instru02_D", "instru03", "instru03_D", "instru04", "instru04_D", "instru05", 
            "instru05_D"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(acoustics != null)
            acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.625F);
        Cockpit.xyz[2] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.06845F);
        Cockpit.ypr[2] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 1.0F);
        mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Trim1", 0.0F, 722F * fm.CT.getTrimAileronControl(), 0.0F);
        mesh.chunkSetAngles("Z_Trim2", 0.0F, 722F * fm.CT.getTrimRudderControl(), 0.0F);
        mesh.chunkSetAngles("Z_Trim3", 0.0F, 722F * fm.CT.getTrimElevatorControl(), 0.0F);
        mesh.chunkSetAngles("Z_Flaps1", 0.0F, -37F * (pictFlap = 0.75F * pictFlap + 0.25F * fm.CT.FlapsControl), 0.0F);
        mesh.chunkSetAngles("Z_Gear1", 0.0F, 46F * (pictGear = 0.8F * pictGear + 0.2F * fm.CT.GearControl), 0.0F);
        mesh.chunkSetAngles("Z_Throtle1", 0.0F, 51.8F * interp(setNew.throttle, setOld.throttle, f), 0.0F);
        mesh.chunkSetAngles("Z_Prop1", 0.0F, 35F - 35F * interp(setNew.prop, setOld.prop, f), 0.0F);
        mesh.chunkSetAngles("Z_Mixture1", 0.0F, 47F * fm.EI.engines[0].getControlMix(), 0.0F);
        mesh.chunkSetAngles("Z_Supercharger1", 0.0F, -15F * (float)fm.EI.engines[0].getControlCompressor(), 0.0F);
        mesh.chunkSetAngles("Z_RightPedal", 0.0F, 10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("Z_LeftPedal", 0.0F, -10F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("Z_Columnbase", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 8F, 0.0F);
        mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 8F);
        mesh.chunkSetAngles("Z_WingFold1", 0.0F, 54F * fm.CT.wingControl, 0.0F);
        mesh.chunkSetAngles("Z_Hook1", 0.0F, -57F * fm.CT.arrestorControl, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        mesh.chunkSetAngles("Z_Speedometer1", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 796.3598F, 0.0F, 11F), speedometerScale), 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("Z_TurnBank1", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        mesh.chunkSetAngles("Z_TurnBank2", 0.0F, cvt(getBall(7D), -7F, 7F, -10F, 10F), 0.0F);
        mesh.chunkSetAngles("Z_TurnBank3", 0.0F, -fm.Or.getKren(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(fm.Or.getTangage(), -45F, 45F, -0.0285F, 0.0285F);
        mesh.chunkSetLocate("Z_TurnBank4", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_TurnBank5", 0.0F, -fm.Or.getKren(), 0.0F);
        mesh.chunkSetAngles("Z_Climb1", 0.0F, floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        mesh.chunkSetAngles("Z_Compass1", 0.0F, -90F + setNew.waypointAzimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_Compass2", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_Compass3", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_RPM1", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        mesh.chunkSetAngles("Z_RPM2", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Z_Minute1", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Z_Hour1", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Z_Fuel1", 0.0F, cvt(fm.M.fuel, 0.0F, 600F, 0.0F, 300F), 0.0F);
        mesh.chunkSetAngles("Z_FuelPres1", 0.0F, cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 1.0F, 0.0F, 255F), 0.0F);
        mesh.chunkSetAngles("Z_TankPres1", 0.0F, cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 1.0F, 0.0F, 255F), 0.0F);
        mesh.chunkSetAngles("Z_HydPres1", 0.0F, fm.Gears.bIsHydroOperable ? 200F : 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Temp1", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 97.5F), 0.0F);
        if((fm.AS.astateCockpitState & 0x40) == 0)
            mesh.chunkSetAngles("Z_Pres1", 0.0F, pictManf = 0.9F * pictManf + 0.1F * cvt(fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 344.5F), 0.0F);
        mesh.chunkSetAngles("Z_Oil1", 0.0F, cvt(fm.EI.engines[0].tOilOut, 20F, 120F, 0.0F, 78F), 0.0F);
        mesh.chunkSetAngles("Z_Oilpres1", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, 256F), 0.0F);
        resetYPRmodifier();
        if(fm.Gears.cgear)
            Cockpit.xyz[2] = -0.028F * fm.CT.getGear();
        mesh.chunkSetLocate("Z_GearInd1", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        if(fm.Gears.lgear)
            Cockpit.xyz[2] = -0.028F * fm.CT.getGear();
        mesh.chunkSetLocate("Z_GearInd2", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        if(fm.Gears.rgear)
            Cockpit.xyz[2] = -0.028F * fm.CT.getGear();
        mesh.chunkSetLocate("Z_GearInd3", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkVisible("Z_WaterInjL1", fm.EI.engines[0].getControlAfterburner());
        mesh.chunkVisible("Z_StallWarnL1", fm.getAOA() > fm.AOA_Crit);
        mesh.chunkVisible("Z_CarbAirL1", false);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("Z_OilSplats_D1", true);
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("XGlassDamage2", true);
            mesh.chunkVisible("Z_Z_RETICLE", false);
            mesh.chunkVisible("Z_Z_MASK", false);
            mesh.chunkVisible("Pricel_D0", false);
            mesh.chunkVisible("Pricel_D1", true);
        }
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("XGlassDamage1", true);
            mesh.chunkVisible("XGlassDamage3", true);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Panel_D0", false);
            mesh.chunkVisible("Panel_D1", true);
            mesh.chunkVisible("Z_RPM1", false);
            mesh.chunkVisible("Z_RPM2", false);
            mesh.chunkVisible("Z_Altimeter1", false);
            mesh.chunkVisible("Z_Altimeter2", false);
            mesh.chunkVisible("Z_TurnBank1", false);
            mesh.chunkVisible("Z_TurnBank2", false);
            mesh.chunkVisible("Z_Climb1", false);
            mesh.chunkVisible("Z_Hour1", false);
            mesh.chunkVisible("Z_Minute1", false);
            mesh.chunkVisible("Z_FuelPres1", false);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("XGlassDamage4", true);
//        if((fm.AS.astateCockpitState & 8) == 0);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("XHullDamage1", true);
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("XHullDamage2", true);
        retoggleLight();
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
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
    private float pictManf;
    private boolean bNeedSetUp;
    private static final float speedometerScale[] = {
        0.0F, 15.5F, 77F, 175F, 275F, 360F, 412F, 471F, 539F, 610.5F, 
        669.75F, 719F
    };
    private static final float variometerScale[] = {
        -175.5F, -160.5F, -145.5F, -128F, -100F, -65.5F, 0.0F, 65.5F, 100F, 128F, 
        145.5F, 160.5F, 175.5F
    };
}
