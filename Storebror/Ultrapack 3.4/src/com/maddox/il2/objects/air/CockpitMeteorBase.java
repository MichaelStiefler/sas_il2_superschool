package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitMeteorBase extends CockpitPilot {
    class Interpolater extends InterpolateRef {
        public boolean tick() {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            setNew.throttlel = (10F * setOld.throttlel + fm.EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + fm.EI.engines[1].getControlThrottle()) / 11F;
            float f = waypointAzimuth();
            if (useRealisticNavigationInstruments()) {
                setNew.waypointAzimuth.setDeg(f - 90F);
                setOld.waypointAzimuth.setDeg(f - 90F);
            } else {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
            setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
            if (cockpitDimControl) {
                if (setNew.dimPosition > 0.0F)
                    setNew.dimPosition = setOld.dimPosition - 0.05F;
            } else if (setNew.dimPosition < 1.0F)
                setNew.dimPosition = setOld.dimPosition + 0.05F;
            setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
            setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
            return true;
        }
    }

    private class Variables {

        float      altimeter;
        float      throttlel;
        float      throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      dimPosition;
        float      beaconDirection;
        float      beaconRange;

        private Variables() {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return waypointAzimuthInvertMinus(30F);
    }
    
    public CockpitMeteorBase(String hierFile, String shortName) {
        super(hierFile, shortName);
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        bNeedSetUp = true;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictGear = 0.0F;
        cockpitNightMats = (new String[] { "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", "petitfla", "turnbank" });
        setNightMats(false);
        setNew.dimPosition = 1.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (bNeedSetUp) {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        Cockpit.xyz[0] = -cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
        mesh.chunkSetLocate("canopy", Cockpit.xyz, Cockpit.ypr);
        if (fm.isTick(44, 0)) {
            mesh.chunkVisible("Z_GearLGreen1", fm.CT.getGear() == 1.0F && fm.Gears.lgear);
            mesh.chunkVisible("Z_GearRGreen1", fm.CT.getGear() == 1.0F && fm.Gears.rgear);
            mesh.chunkVisible("Z_GearCGreen1", fm.CT.getGear() == 1.0F);
            mesh.chunkVisible("Z_GearLRed1", fm.CT.getGear() == 0.0F || fm.Gears.isAnyDamaged());
            mesh.chunkVisible("Z_GearRRed1", fm.CT.getGear() == 0.0F || fm.Gears.isAnyDamaged());
            mesh.chunkVisible("Z_GearCRed1", fm.CT.getGear() == 0.0F);
            mesh.chunkVisible("Z_MachLamp", fm.getSpeed() / Atmosphere.sonicSpeed((float) ((Tuple3d) (fm.Loc)).z) > 0.8F);
            mesh.chunkVisible("Z_EngineFireLampR", fm.AS.astateEngineStates[1] > 2);
            mesh.chunkVisible("Z_EngineFireLampL", fm.AS.astateEngineStates[0] > 2);
            mesh.chunkVisible("Z_FuelLampV", fm.M.fuel < 200F);
            mesh.chunkVisible("Z_FuelLampIn", fm.M.fuel < 200F);
        }
        resetYPRmodifier();
        Cockpit.xyz[1] = pictGear = 0.85F * pictGear + 0.011F * fm.CT.GearControl;
        mesh.chunkSetLocate("Z_GearEin", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_FlapEin", 0.0F, 0.0F, -(fm.CT.FlapsControl - 0.08F) * 115F);
        mesh.chunkSetAngles("Z_Columnbase", 8F * (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Column", 30F * (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl), 0.0F, 0.0F);
        resetYPRmodifier();
        if (fm.CT.saveWeaponControl[0])
            Cockpit.xyz[2] = -0.0025F;
        resetYPRmodifier();
        if (fm.CT.saveWeaponControl[2] || fm.CT.saveWeaponControl[3])
            Cockpit.xyz[2] = -0.00325F;
        mesh.chunkSetAngles("Z_PedalStrut", 20F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_LeftPedal", -20F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RightPedal", -20F * fm.CT.getRudder(), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(fm.EI.engines[0].getControlThrottle(), 0.01F, 0.99F, 0.0F, 0.55F) / 5F;
        mesh.chunkSetLocate("Z_ThrottleL", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(fm.EI.engines[1].getControlThrottle(), 0.01F, 0.99F, 0.0F, 0.55F) / 5F;
        mesh.chunkSetLocate("Z_ThrottleR", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Speedometer1", floatindex(cvt(Pitot.Indicator((float) fm.Loc.z, fm.getSpeed()), 0.0F, 257.2222F, 0.0F, 10F), speedometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Speedometer2", floatindex(cvt(fm.getSpeedKMH(), 100F, 1000F, 1.0F, 10F), speedometerTruScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter2", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Altimeter1", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Hour1", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Minute1", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Second1", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_TurnBank1", fm.Or.getTangage(), 0.0F, fm.Or.getKren());
        mesh.chunkSetAngles("Z_TurnBank2", 0.0F, 0.0F, -cvt(getBall(8D), -8F, 8F, 35F, -35F));
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, -cvt(w.z, -0.23562F, 0.23562F, -48F, 48F));
        mesh.chunkSetAngles("Z_Climb1", floatindex(cvt(setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPML", cvt(fm.EI.engines[0].getRPM(), 10000F, 20000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPML2", cvt(fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPMR", cvt(fm.EI.engines[1].getRPM(), 10000F, 20000F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_RPMR2", cvt(fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        if (useRealisticNavigationInstruments())
            mesh.chunkSetAngles("Z_Compass2", setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_Compass2", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_GasTempL", cvt(fm.EI.engines[1].tOilOut, 20F, 95F, -150F, -25F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_GasTempR", cvt(fm.EI.engines[0].tOilOut, 20F, 95F, -150F, -25F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_FuelRemainV", floatindex(cvt(fm.M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), fuelScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_FuelRemainIn", floatindex(cvt(fm.M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), fuelScale), 0.0F, 0.0F);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleDim() {
        cockpitDimControl = !cockpitDimControl;
    }

    public void toggleLight() {
        cockpitLightControl = !cockpitLightControl;
        if (cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictGear;
    private static final float speedometerScale[]    = { 0.0F, 15.5F, 76F, 153.5F, 234F, 304F, 372.5F, 440F, 504F, 566F, 630F };
    private static final float speedometerTruScale[] = { 0.0F, 32.75F, 65.5F, 98.25F, 131F, 164F, 200F, 237F, 270.5F, 304F, 336F };
    private static final float variometerScale[]     = { -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F };
    private static final float fuelScale[]           = { 0.0F, 11F, 31F, 57F, 84F, 103.5F };

}
