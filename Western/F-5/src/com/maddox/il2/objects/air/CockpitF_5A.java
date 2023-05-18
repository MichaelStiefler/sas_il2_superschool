
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, TypeGuidedMissileCarrier, F_5, Cockpit

public class CockpitF_5A extends CockpitPilot
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
                setNew.throttlel = 0.85F * setOld.throttlel + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle() * 0.15F;
                setNew.throttler = 0.85F * setOld.throttler + ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                setNew.flap = fm.CT.getFlap();
                float nozzle = 100F;
                float tr = ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle();
                if(tr > 1.00F)
                    nozzle = 100F;
                else if(tr > 0.95F)
                    nozzle = cvt(tr, 0.95F, 1.00F, 75F, 100F);
                else if(tr > 0.85F)
                    nozzle = cvt(tr, 0.85F, 0.95F, 10F, 75F);
                else if(tr > 0.60F)
                    nozzle = cvt(tr, 0.60F, 0.85F, 30F, 10F);
                else if(tr > 0.35F)
                    nozzle = cvt(tr, 0.35F, 0.60F, 60F, 30F);
                else if(tr > 0.05F)
                    nozzle = cvt(tr, 0.05F, 0.35F, 80F, 60F);
                else
                    nozzle = 100F;
                setNew.nozzlel = (4F * setOld.nozzlel + nozzle) / 5F;
                tr = ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle();
                if(tr > 1.00F)
                    nozzle = 100F;
                else if(tr > 0.95F)
                    nozzle = cvt(tr, 0.95F, 1.00F, 75F, 100F);
                else if(tr > 0.85F)
                    nozzle = cvt(tr, 0.85F, 0.95F, 10F, 75F);
                else if(tr > 0.60F)
                    nozzle = cvt(tr, 0.60F, 0.85F, 30F, 10F);
                else if(tr > 0.35F)
                    nozzle = cvt(tr, 0.35F, 0.60F, 60F, 30F);
                else if(tr > 0.05F)
                    nozzle = cvt(tr, 0.05F, 0.35F, 80F, 60F);
                else
                    nozzle = 100F;
                setNew.nozzler = (4F * setOld.nozzler + nozzle) / 5F;
                setNew.altimeter = fm.getAltitude();
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
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                float f1 = ((F_5)aircraft()).k14Distance;
                setNew.k14w = (5F * CockpitF_5A.k14TargetWingspanScale[((F_5)aircraft()).k14WingspanType]) / f1;
                setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
                setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitF_5A.k14TargetMarkScale[((F_5)aircraft()).k14WingspanType];
                setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((F_5)aircraft()).k14Mode;
                com.maddox.JGP.Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
                double d = 0.00125D * (double)f1;
                float f2 = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
                float f3 = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
                float f4 = floatindex((f1 - 200F) * 0.04F, CockpitF_5A.k14BulletDrop) - CockpitF_5A.k14BulletDrop[0];
                f3 += (float)Math.toDegrees(Math.atan(f4 / f1));
                setNew.k14x = 0.92F * setOld.k14x + 0.08F * f2;
                setNew.k14y = 0.92F * setOld.k14y + 0.08F * f3;
                if(setNew.k14x > 7F)
                    setNew.k14x = 7F;
                if(setNew.k14x < -7F)
                    setNew.k14x = -7F;
                if(setNew.k14y > 7F)
                    setNew.k14y = 7F;
                if(setNew.k14y < -7F)
                    setNew.k14y = -7F;
                buzzerFX(fm.getSpeed() < 63F && ((FlightModelMain) (fm)).Gears.nOfGearsOnGr < 1);
                setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
                setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
                if(((FlightModelMain) (fm)).EI.engines[0].getStage() < 6)
                {
                    if(setNew.stbyPositionl > 0.0F)
                        setNew.stbyPositionl = setOld.stbyPositionl - 0.002F;
                }
                else if(setNew.stbyPositionl < 1.0F)
                    setNew.stbyPositionl = setOld.stbyPositionl + 0.002F;
                if(((FlightModelMain) (fm)).EI.engines[1].getStage() < 6)
                {
                    if(setNew.stbyPositionr > 0.0F)
                        setNew.stbyPositionr = setOld.stbyPositionr - 0.002F;
                }
                else if(setNew.stbyPositionr < 1.0F)
                    setNew.stbyPositionr = setOld.stbyPositionr + 0.002F;
                if(setNew.stbyPosition2 < 1.0F)
                    setNew.stbyPosition2 = setOld.stbyPosition2 + 0.002F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttlel;
        float throttler;
        float altimeter;
        float vspeed;
        float nozzlel;
        float nozzler;
        float flap;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float beaconDirection;
        float beaconRange;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;
        float stbyPositionl;
        float stbyPositionr;
        float stbyPosition2;

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

    public CockpitF_5A()
    {
        super("3DO/Cockpit/F-5/F-5A.him", "bf109");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        super.cockpitNightMats = (new String[] {
            "Sabre_Compass1", "Needles86", "Needles", "Needles2", "F5Gaug86_01", "F5Gaug86_02", "F5Gaug86_03", "Gaug86_03", "F5Gaug86_04", "F5Gaug86_05"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        loadBuzzerFX();
    }

    private int iLockState()
    {
        if(!(super.aircraft() instanceof TypeGuidedMissileCarrier))
            return 0;
        else
            return ((TypeGuidedMissileCarrier)super.aircraft()).getGuidedMissileUtils().getMissileLockState();
    }

    private float machNumber()
    {
        return ((F_5)super.aircraft()).calculateMach();
    }

    public void reflectWorldToInstruments(float f)
    {
        float AOAunits = ((FlightModelMain) (fm)).getAOA() * 1.84F + 4.28F;

        if((super.fm.AS.astateCockpitState & 2) == 0)
        {
// Not implemented msh nodes in F-5A
//            if(F_5.hunted != null)
//                super.mesh.chunkVisible("Z_RadarTarget", true);
//            else
//                super.mesh.chunkVisible("Z_RadarTarget", false);
            int i = ((F_5)aircraft()).k14Mode;
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
            boolean flag = i < 2;
            super.mesh.chunkVisible("Z_CollimateurLamp", true);
            super.mesh.chunkVisible("Z_GunLamp", true);
            super.mesh.chunkVisible("Colli01", false);
            super.mesh.chunkVisible("Colli02", true);
            super.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            super.mesh.chunkVisible("Gun01", false);
            super.mesh.chunkVisible("Gun02", true);
            super.mesh.chunkVisible("Contact02a", false);
            super.mesh.chunkVisible("Contact02b", true);
            resetYPRmodifier();
            Cockpit.xyz[0] = setNew.k14w;
            for(int j = 1; j < 11; j++)
            {
                super.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
                super.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
            }

            if(i == 1)
            {
                super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, setNew.k14x, setNew.k14y);
// Not implemented msh nodes in F-5A
//                super.mesh.chunkVisible("Z_CageSelect1", false);
//                super.mesh.chunkVisible("Z_CageSelect2", true);
            }
            if(i == 0)
            {
                super.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, 0.0F, 0.0F);
//                super.mesh.chunkVisible("Z_CageSelect1", true);
//                super.mesh.chunkVisible("Z_CageSelect2", false);
            }
            if(!flag)
            {
                super.mesh.chunkVisible("Z_SightCaged", false);
                super.mesh.chunkVisible("Z_SightUncaged", false);
                super.mesh.chunkVisible("Z_CollimateurLamp", false);
                super.mesh.chunkVisible("Z_GunLamp", false);
                super.mesh.chunkVisible("Colli01", true);
                super.mesh.chunkVisible("Colli02", false);
                super.mesh.chunkVisible("Gun01", true);
                super.mesh.chunkVisible("Gun02", false);
                super.mesh.chunkVisible("Contact02a", true);
                super.mesh.chunkVisible("Contact02b", false);
            }
        }
        super.mesh.chunkSetAngles("Z_PedalRight", 10F * super.fm.CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_PedalLeft", -10F * super.fm.CT.getRudder(), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Throtle86", 50F * setNew.throttlel, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Throtle86-2", 50F * setNew.throttler, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Flaps1", -18F * super.fm.CT.FlapsControl, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_AirBrakes", 29F * super.fm.CT.AirBrakeControl, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Hydro-Pres86", 0.0F, cvt(interp(setNew.stbyPositionl, setOld.stbyPositionl, f), 0.0F, 1.0F, -253F, 10F), 0.0F);
        super.mesh.chunkSetAngles("Z_Hydro-Pres86_2", 0.0F, cvt(interp(setNew.stbyPositionr, setOld.stbyPositionr, f), 0.0F, 1.0F, -253F, 10F), 0.0F);
// Not implemented msh nodes in F-5A
//        super.mesh.chunkSetAngles("Z_Ammeter86", 0.0F, cvt(interp(setNew.stbyPosition2, setOld.stbyPosition2, f), 0.0F, 1.0F, -55F, 30F), 0.0F);
        resetYPRmodifier();
        float f1 = super.fm.EI.engines[0].getStage();
        if(f1 > 0.0F && f1 < 7F)
            f1 = 0.0345F;
        else
            f1 = -0.05475F;
        Cockpit.xyz[2] = f1;
        super.mesh.chunkSetAngles("Z_Stick01", (pictElev = 0.7F * pictElev + 0.15F * super.fm.CT.ElevatorControl) * 16F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * super.fm.CT.AileronControl) * 15F);
        super.mesh.chunkSetAngles("Z_Stick02", (pictElev = 0.7F * pictElev + 0.15F * super.fm.CT.ElevatorControl) * 16F, 0.0F, -(pictAiler = 0.85F * pictAiler + 0.15F * super.fm.CT.AileronControl) * 15F);
        resetYPRmodifier();
        if(super.fm.CT.saveWeaponControl[0])
            Cockpit.xyz[2] = -0.0029F;
// Not implemented msh nodes in F-5A
//        super.mesh.chunkSetAngles("Z_Target1", -1.2F * setNew.k14wingspan, 0.0F, 0.0F);
        if(machNumber() < 0.95F || machNumber() > 1.0F)
        {
            super.mesh.chunkSetAngles("Z_Speedometer86-1", floatindex(cvt(Pitot.Indicator((float)((Tuple3d) (super.fm.Loc)).z, super.fm.getSpeedKMH()) * 1.131F, 74.07994F, 1222.319F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Speedometer86-2", floatindex(cvt(machNumber() * 1.4F - 0.4F, 0.3F, 1.5F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Speedometer86-3", floatindex(cvt(super.fm.getSpeedKMH() * 1.131F, 74.07994F, 1222.319F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Climb86", floatindex(cvt(setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F, 0.0F);
            Cockpit.xyz[1] = cvt(super.fm.Or.getTangage(), -45F, 45F, 0.0362F, -0.0362F);
            super.mesh.chunkSetAngles("Z_Altimeter86_1", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Altimeter86_2", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Altimeter86_3", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F), 0.0F, 0.0F);
            super.mesh.chunkSetAngles("Z_Altimeter86_4", cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, -180F, 145F), 0.0F, 0.0F);
            resetYPRmodifier();
        }
        super.mesh.chunkSetAngles("Z_Hour86", cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Minute86", cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Second86", cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Nozzle-Pos", cvt(setNew.nozzlel, 0.0F, 100F, -305F, -55F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Nozzle-Pos_2", cvt(setNew.nozzler, 0.0F, 100F, -305F, -55F), 0.0F, 0.0F);
        f1 = super.fm.EI.engines[0].getPowerOutput();
        super.mesh.chunkSetAngles("Z_Fuel-Flow", cvt((float)Math.sqrt(f1), 0.0F, 1.0F, 96F, 360F), 0.0F, 0.0F);
        f1 = super.fm.EI.engines[1].getPowerOutput();
        super.mesh.chunkSetAngles("Z_Fuel-Flow_2", cvt((float)Math.sqrt(f1), 0.0F, 1.0F, 96F, 360F), 0.0F, 0.0F);
        f1 = cvt(super.fm.M.fuel, 0.0F, 1000F, 0.0F, 270F);
        if(f1 < 45F)
            f1 = cvt(f1, 0.0F, 45F, -58F, 45F);
        f1 += 58F;
        super.mesh.chunkSetAngles("Z_Oilpres86", cvt(1.0F + 0.05F * super.fm.EI.engines[0].tOilOut * super.fm.EI.engines[0].getReadyness(), 0.0F, 18F, 115F, 410F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Oilpres86_2", cvt(1.0F + 0.05F * super.fm.EI.engines[1].tOilOut * super.fm.EI.engines[1].getReadyness(), 0.0F, 18F, 115F, 410F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM86-1", cvt(super.fm.EI.engines[0].getRPM(), 0.0F, 2550F, -110F, 180F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM86-2", cvt(super.fm.EI.engines[0].getRPM() * 0.955F, 2550F, 3500F, -10F, 335F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM86-1_2", cvt(super.fm.EI.engines[1].getRPM(), 0.0F, 2550F, -110F, 180F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_RPM86-2_2", cvt(super.fm.EI.engines[1].getRPM() * 0.955F, 2550F, 3500F, -10F, 335F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Exhaust_Temp", cvt(super.fm.EI.engines[0].tOilOut, 20F, 95F, -150F, -25F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Exhaust_Temp_2", cvt(super.fm.EI.engines[1].tOilOut, 20F, 95F, -150F, -25F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass-Emerg1", cvt(super.fm.Or.getTangage(), -30F, 30F, -30F, 30F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass-Emerg3", cvt(super.fm.Or.getKren(), -45F, 45F, -45F, 45F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass-Emerg2", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank86-1", cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_TurnBank86-2", cvt(((Tuple3f) (w)).z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        w.set(super.fm.getW());
        super.fm.Or.transform(w);
        super.mesh.chunkSetAngles("Z_Fuel86", cvt(super.fm.M.fuel, 0.0F, 1650F, -150F, 150F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_G-Factor", cvt(super.fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        super.mesh.chunkVisible("Z_Gear86Green1", super.fm.CT.getGearC() > 0.99F && super.fm.Gears.cgear);
        super.mesh.chunkVisible("Z_Gear86Green2", super.fm.CT.getGearL() > 0.99F && super.fm.Gears.lgear);
        super.mesh.chunkVisible("Z_Gear86Green3", super.fm.CT.getGearR() > 0.99F && super.fm.Gears.rgear);
        super.mesh.chunkVisible("Z_FuelLamp", super.fm.M.fuel < 200F);
// Not implemented msh nodes in F-5A
//        if(super.fm.EI.engines[0].getRPM() > 50F)
//            super.mesh.chunkVisible("Z_TrimLamp", Math.abs(super.fm.CT.getTrimElevatorControl()) < 0.05F);
//        if(super.fm.EI.engines[0].getRPM() < 50F)
//            super.mesh.chunkVisible("Z_TrimLamp", false);
//        if(super.fm.Gears.nOfGearsOnGr < 3)
//            super.mesh.chunkVisible("Z_TrimLamp", false);
        super.mesh.chunkVisible("Z_FireLamp1", super.fm.AS.astateEngineStates[0] > 2);
        super.mesh.chunkVisible("Z_FireLamp2", super.fm.AS.astateEngineStates[1] > 2);
        super.mesh.chunkSetAngles("Z_horizont1a", 0.0F, -super.fm.Or.getKren(), 0.0F);
        super.mesh.chunkSetAngles("Z_horizont2b", 0.0F, super.fm.Or.getTangage(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(super.fm.Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
        super.mesh.chunkSetLocate("Z_horizont1b", Cockpit.xyz, Cockpit.ypr);
        if(useRealisticNavigationInstruments())
            super.mesh.chunkSetAngles("Z_Azimuth86_2", (setNew.azimuth.getDeg(f) - 270F) + setNew.beaconDirection, 0.0F, 0.0F);
        else
            super.mesh.chunkSetAngles("Z_Azimuth86_2", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Compass86_1", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        super.mesh.chunkSetAngles("Z_AOA", cvt(super.fm.getAOA(), -10F, 35F, 180F, -50F), 0.0F, 0.0F);
        if(super.fm.CT.getGear() == 1.0F)
        {
            mesh.chunkVisible("Z_AOALanding1", AOAunits > 19.499F);
            mesh.chunkVisible("Z_AOALanding2", AOAunits < 20.100F && AOAunits > 17.900F);
            mesh.chunkVisible("Z_AOALanding3", AOAunits < 18.500F);
        }
        else
        {
            mesh.chunkVisible("Z_AOALanding1", false);
            mesh.chunkVisible("Z_AOALanding2", false);
            mesh.chunkVisible("Z_AOALanding3", false);
        }
        super.mesh.chunkSetAngles("Z_Azimuth86_1", -setNew.azimuth.getDeg(f) + setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(f1, 0.01F, 0.08F, 0.0F, 0.1F);
        super.mesh.chunkSetAngles("Z_GearHandle", 0.0F, 0.0F, 50F * (pictGear = 0.82F * pictGear + 0.18F * super.fm.CT.GearControl));
        resetYPRmodifier();
        super.mesh.chunkSetAngles("Canopy", 0.0F, 0.0F, 30F * super.fm.CT.getCockpitDoor());
        if(super.fm.AS.bIsAboutToBailout)
        {
            super.mesh.chunkVisible("Canopy-Open1", false);
            super.mesh.chunkVisible("Canopy-Open2", false);
            super.mesh.chunkVisible("Canopy-Open3", false);
            super.mesh.chunkVisible("Canopy-Open4", false);
            super.mesh.chunkVisible("Canopy-Open5", false);
        }
// Not implemented msh nodes in F-5A
//        if(((Interpolate) (super.fm)).actor instanceof TypeGuidedMissileCarrier)
//        {
//            if(iLockState() == 2)
//                super.mesh.chunkVisible("Z_Locked", true);
//            else
//                super.mesh.chunkVisible("Z_Locked", false);
//            if(super.fm.getOverload() > 2.0F || super.fm.getOverload() < 0.0F)
//                super.mesh.chunkVisible("Z_OverG", true);
//            else
//                super.mesh.chunkVisible("Z_OverG", false);
//        }
        int flapindex = 0;
        if(setNew.stbyPositionl < 0.01F && setNew.stbyPositionr < 0.01F)
            flapindex = 0;
        else if(setNew.flap == 0.0F)
            flapindex = 1;
        else if(setNew.flap == 1.0F)
            flapindex = 4;
        else if(setNew.flap == setOld.flap)
            flapindex = 3;
        else
            flapindex = 0;
        super.mesh.chunkSetAngles("Z_FlapPosInd", 0.0F, (float)(-60 * flapindex), 0.0F);
        super.mesh.chunkVisible("Z_GearWarnLamp", (super.fm.CT.GearControl < 0.1F && setNew.altimeter < 2890F && super.fm.getSpeedKMH() < 390F
                                && (super.fm.EI.engines[0].getPowerOutput() < 0.7F || super.fm.EI.engines[1].getPowerOutput() < 0.7F))
                                || !super.fm.Gears.cgear || !super.fm.Gears.lgear || !super.fm.Gears.rgear);
        resetYPRmodifier();
        if(super.fm.CT.DragChuteControl == 1.0F)
            bChuteOnceDeployed = true;
        if(bChuteOnceDeployed && !bChuteJettisoned)
        {
            if(super.fm.CT.DragChuteControl == 1.0F)
            {
                Cockpit.xyz[1] = -0.020F;
                super.mesh.chunkSetLocate("Z_Dragchute", Cockpit.xyz, Cockpit.ypr);
            }
            else
            {
                Cockpit.xyz[1] = -0.030F;
                Cockpit.ypr[1] = -60F;
                super.mesh.chunkSetLocate("Z_Dragchute", Cockpit.xyz, Cockpit.ypr);
                bChuteJettisoned = true;
            }
        }
    }

    public void reflectCockpitState()
    {
        if((super.fm.AS.astateCockpitState & 2) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage2", true);
            super.mesh.chunkVisible("Glass_Dam", true);
            super.mesh.chunkVisible("Z_Z_RETICLE", false);
            super.mesh.chunkVisible("Z_Z_RETICLE1", false);
            for(int i = 1; i < 11; i++)
                super.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);

            super.mesh.chunkVisible("Gaug86_02", false);
            super.mesh.chunkVisible("Gaug86_02_Dam", true);
            super.mesh.chunkVisible("Gaug86_03", false);
            super.mesh.chunkVisible("Gaug86_03_Dam", true);
            super.mesh.chunkVisible("Gaug86_05", false);
            super.mesh.chunkVisible("Gaug86_05_Dam", true);
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("Z_InstrLamp", true);
            super.mesh.chunkVisible("Z_Speedometer86-1", false);
            super.mesh.chunkVisible("Z_Speedometer86-3", false);
            super.mesh.chunkVisible("Z_Altimeter86_1", false);
            super.mesh.chunkVisible("Z_Altimeter86_2", false);
            super.mesh.chunkVisible("Z_Altimeter86_3", false);
            super.mesh.chunkVisible("Z_Altimeter86_4", false);
            super.mesh.chunkVisible("Z_Compass86_1", false);
            super.mesh.chunkVisible("Z_Azimuth86_1", false);
            super.mesh.chunkVisible("Z_Azimuth86_2", false);
            super.mesh.chunkVisible("Z_horizont2", false);
            super.mesh.chunkVisible("Z_horizont2b", false);
            super.mesh.chunkVisible("Z_Compass-Emerg1", false);
            super.mesh.chunkVisible("Z_Compass-Emerg3", false);
            super.mesh.chunkVisible("Z_Compass-Emerg2", false);
            super.mesh.chunkVisible("Z_horizont1a", false);
            super.mesh.chunkVisible("Z_horizont1b", false);
            super.mesh.chunkVisible("Z_Exhaust_Temp", false);
            super.mesh.chunkVisible("Z_Hour86", false);
            super.mesh.chunkVisible("Z_Minute86", false);
            super.mesh.chunkVisible("Z_Second86", false);
            super.mesh.chunkVisible("Z_Ammeter86", false);
        }
        if((super.fm.AS.astateCockpitState & 1) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage1", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
        }
        if((super.fm.AS.astateCockpitState & 0x40) != 0)
        {
            super.mesh.chunkVisible("Gaug86_02", false);
            super.mesh.chunkVisible("Gaug86_02_Dam", true);
            super.mesh.chunkVisible("Gaug86_03", false);
            super.mesh.chunkVisible("Gaug86_03_Dam", true);
            super.mesh.chunkVisible("Gaug86_05", false);
            super.mesh.chunkVisible("Gaug86_05_Dam", true);
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("Z_InstrLamp", true);
            super.mesh.chunkVisible("Z_Speedometer86-1", false);
            super.mesh.chunkVisible("Z_Speedometer86-3", false);
            super.mesh.chunkVisible("Z_Altimeter86_1", false);
            super.mesh.chunkVisible("Z_Altimeter86_2", false);
            super.mesh.chunkVisible("Z_Altimeter86_3", false);
            super.mesh.chunkVisible("Z_Altimeter86_4", false);
            super.mesh.chunkVisible("Z_Compass86_1", false);
            super.mesh.chunkVisible("Z_Azimuth86_1", false);
            super.mesh.chunkVisible("Z_Azimuth86_2", false);
            super.mesh.chunkVisible("Z_horizont2", false);
            super.mesh.chunkVisible("Z_horizont2b", false);
            super.mesh.chunkVisible("Z_Compass-Emerg1", false);
            super.mesh.chunkVisible("Z_Compass-Emerg3", false);
            super.mesh.chunkVisible("Z_Compass-Emerg2", false);
            super.mesh.chunkVisible("Z_horizont1a", false);
            super.mesh.chunkVisible("Z_horizont1b", false);
            super.mesh.chunkVisible("Z_Exhaust_Temp", false);
            super.mesh.chunkVisible("Z_Hour86", false);
            super.mesh.chunkVisible("Z_Minute86", false);
            super.mesh.chunkVisible("Z_Second86", false);
            super.mesh.chunkVisible("Z_Ammeter86", false);
        }
        if((super.fm.AS.astateCockpitState & 4) != 0)
        {
            super.mesh.chunkVisible("XGlassDamage3", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
        }
        if((super.fm.AS.astateCockpitState & 8) != 0)
            super.mesh.chunkVisible("XGlassDamage4", true);
        if((super.fm.AS.astateCockpitState & 0x80) != 0);
        if((super.fm.AS.astateCockpitState & 0x10) != 0)
            super.mesh.chunkVisible("XGlassDamage4", true);
        if((super.fm.AS.astateCockpitState & 0x20) != 0)
        {
            super.mesh.chunkVisible("XGlasslDamage2", true);
            super.mesh.chunkVisible("XGlassDamage4", true);
        }
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
    private float pictGear;
    private boolean bChuteOnceDeployed = false;
    private boolean bChuteJettisoned = false;
    private static final float speedometerScale[] = {
        0.0F, 22.5F, 45F, 67.5F, 90F, 112.5F, 135F, 157.5F, 180F, 202.5F, 
        225F, 247.5F, 270F, 292.5F, 315F, 336.5F, 360F
    };
    private static final float variometerScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };
    private static final float k14TargetMarkScale[] = {
        0.0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F
    };
    private static final float k14TargetWingspanScale[] = {
        9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F
    };
    private static final float k14BulletDrop[] = {
        5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 
        8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 
        10.603F, 10.704F, 10.739F, 10.782F, 10.789F
    };


}