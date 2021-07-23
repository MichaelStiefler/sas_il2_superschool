package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitSB103 extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            if(RPK)
            {
                float f = fm.EI.engines[0].getRPM() > 0.0F ? 1.0F : 0.0F;
                float f2 = fm.EI.engines[1].getRPM() > 0.0F ? 1.0F : 0.0F;
                setNew.Stoichion1 = 0.95F * setOld.Stoichion1 - 0.05F * f * (3F * setNew.mix1 + cvt(fm.EI.engines[0].getDistabilisationAmplitude(), 0.0F, 3F, 0.0F, 35F));
                setNew.Stoichion2 = 0.95F * setOld.Stoichion2 - 0.05F * f2 * (3F * setNew.mix2 + cvt(fm.EI.engines[1].getDistabilisationAmplitude(), 0.0F, 3F, 0.0F, 35F));
            }
            setNew.altimeter = fm.getAltitude();
            if(cockpitDimControl)
            {
                if(setNew.dimPosition > 0.0F && setNew.ironSight == 0.0F)
                    setNew.dimPosition = setNew.dimPosition - 0.05F;
            } else
            if(setNew.dimPosition < 1.0F && setNew.ironSight == 0.0F)
                setNew.dimPosition = setNew.dimPosition + 0.05F;
            setNew.throttle1 = 0.91F * setOld.throttle1 + 0.09F * fm.EI.engines[0].getControlThrottle();
            setNew.throttle2 = 0.91F * setOld.throttle2 + 0.09F * fm.EI.engines[1].getControlThrottle();
            setNew.mix1 = 0.88F * setOld.mix1 + 0.12F * fm.EI.engines[0].getControlMix();
            setNew.mix2 = 0.88F * setOld.mix2 + 0.12F * fm.EI.engines[1].getControlMix();
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), -(-fm.Or.azimut() - 90F));
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(1.0F), fm.Or.azimut() + 90F);
                setNew.beaconDirection.setDeg(setOld.beaconDirection.getDeg(1.0F), getBeaconDirection());
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), waypointAzimuth(1.0F));
                setNew.beaconDirection.setDeg(setOld.beaconDirection.getDeg(1.0F), waypointAzimuth(1.0F) - fm.Or.azimut() - 90F);
            }
            w.set(fm.getW());
            fm.Or.transform(w);
            setNew.turn = (12F * setOld.turn + w.z) / 13F;
            setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            setNew.manifold1 = 0.8F * setOld.manifold1 + 0.2F * fm.EI.engines[0].getManifoldPressure() * 76F;
            setNew.manifold2 = 0.8F * setOld.manifold2 + 0.2F * fm.EI.engines[1].getManifoldPressure() * 76F;
            float f1 = 25F;
            if(gearsLever != 0.0F && gears == fm.CT.getGear())
            {
                gearsLever = gearsLever * 0.8F;
                if(Math.abs(gearsLever) < 0.1F)
                    gearsLever = 0.0F;
            } else
            if(gears < fm.CT.getGear())
            {
                gears = fm.CT.getGear();
                gearsLever = gearsLever + 2.0F;
                if(gearsLever > f1)
                    gearsLever = f1;
            } else
            if(gears > fm.CT.getGear())
            {
                gears = fm.CT.getGear();
                gearsLever = gearsLever - 2.0F;
                if(gearsLever < -f1)
                    gearsLever = -f1;
            }
            f1 = 20F;
            if(flapsLever != 0.0F && flaps == fm.CT.getFlap())
            {
                flapsLever = flapsLever * 0.8F;
                if(Math.abs(flapsLever) < 0.1F)
                    flapsLever = 0.0F;
            } else
            if(flaps < fm.CT.getFlap())
            {
                flaps = fm.CT.getFlap();
                flapsLever = flapsLever + 2.0F;
                if(flapsLever > f1)
                    flapsLever = f1;
            } else
            if(flaps > fm.CT.getFlap())
            {
                flaps = fm.CT.getFlap();
                flapsLever = flapsLever - 2.0F;
                if(flapsLever < -f1)
                    flapsLever = -f1;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float altimeter;
        float throttle1;
        float throttle2;
        float dimPosition;
        float ironSight;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork beaconDirection;
        float turn;
        float mix1;
        float mix2;
        float vspeed;
        private float manifold1;
        private float manifold2;
        float Stoichion1;
        float Stoichion2;





        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            beaconDirection = new AnglesFork();
            manifold1 = 0.0F;
            manifold2 = 0.0F;
        }

    }


    protected float waypointAzimuth()
    {
        return waypointAzimuthInvertMinus(10F);
    }

    protected void reflectPlaneToModel()
    {
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    public CockpitSB103()
    {
        super("3DO/Cockpit/SB_103/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        gears = 0.0F;
        gearsLever = 0.0F;
        flapsLever = 0.0F;
        flaps = 0.0F;
        w = new Vector3f();
        isSlideRight = false;
        bNeedSetUp = true;
        RPK = false;
        RPK = false;
        setNew.dimPosition = 0.0F;
        cockpitDimControl = !cockpitDimControl;
        printCompassHeading = true;
        cockpitNightMats = (new String[] {
            "arrow", "DPrib_one", "DPrib_one_new", "DPrib_two", "DPrib_three", "DPrib_four", "DPrib_six", "Prib_one", "Prib_one_new", "Prib_two", 
            "Prib_three", "Prib_four", "Prib_five", "Prib_six", "Shkala"
        });
        setNightMats(false);
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK01", light1);
        hooknamed = new HookNamed(mesh, "LAMPHOOK02");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light2.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK02", light2);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public CockpitSB103(String s)
    {
        super(s, "bf109");
        setOld = new Variables();
        setNew = new Variables();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        gears = 0.0F;
        gearsLever = 0.0F;
        flapsLever = 0.0F;
        flaps = 0.0F;
        w = new Vector3f();
        isSlideRight = false;
        bNeedSetUp = true;
        RPK = false;
        RPK = true;
        setNew.dimPosition = 0.0F;
        cockpitDimControl = !cockpitDimControl;
        printCompassHeading = true;
        cockpitNightMats = (new String[] {
            "arrow", "DPrib_one", "DPrib_one_new", "DPrib_two", "DPrib_three", "DPrib_four", "DPrib_six", "Prib_one", "Prib_one_new", "Prib_two", 
            "Prib_three", "Prib_four", "Prib_five", "Prib_six", "Shkala", "Alpha", "AVR", "arrow2", "Alpha_dmg", "AVR_dmg", 
            "RPK", "RPK_dmg"
        });
        setNightMats(false);
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK01", light1);
        hooknamed = new HookNamed(mesh, "LAMPHOOK02");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light2.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK02", light2);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        Cockpit.xyz[2] = cvt(fm.CT.getCockpitDoor(), 0.15F, 0.99F, 0.0F, 0.655F);
        mesh.chunkSetLocate("Z_cannopy", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetLocate("Z_cannopyGlass", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_cannopy_lock", cvt(fm.CT.getCockpitDoor(), 0.01F, 0.15F, 0.0F, 90F), 0.0F, 0.0F);
        mesh.chunkVisible("ZFlareRedR", fm.CT.getGearR() < 0.01F || !fm.Gears.rgear);
        mesh.chunkVisible("ZFlareRedL", fm.CT.getGearL() < 0.01F || !fm.Gears.lgear);
        mesh.chunkVisible("ZFlareGreenR", fm.CT.getGearR() > 0.99F && fm.Gears.rgear);
        mesh.chunkVisible("ZFlareGreenL", fm.CT.getGearL() > 0.99F && fm.Gears.lgear);
        pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl;
        mesh.chunkSetAngles("Z_column", pictElev * 15F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_column_tros", -pictElev * 15F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_wheel", -(pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 45F, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_gears", gearsLever, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Elevator_Trim", 600F * fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_trim_L", 600F * fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_trim_R", 600F * fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_flaps", 0.0F, 0.0F, -flapsLever);
        mesh.chunkSetAngles("Z_flaps_tros", flapsLever, 0.0F, 0.0F);
        float f1 = interp(setNew.throttle1, setOld.throttle1, f) * 70F - 35F;
        mesh.chunkSetAngles("Z_throtle_L", f1, 0.0F, 0.0F);
        f1 = floatindex(cvt(interp(setNew.throttle1, setOld.throttle1, f), 0.0F, 1.1F, 0.0F, 2.0F), trosThrottleScale);
        mesh.chunkSetAngles("Z_throtle_tros_L", f1, 0.0F, 0.0F);
        f1 = interp(setNew.throttle2, setOld.throttle2, f) * 70F - 35F;
        mesh.chunkSetAngles("Z_throtle_R", f1, 0.0F, 0.0F);
        f1 = floatindex(cvt(interp(setNew.throttle2, setOld.throttle2, f), 0.0F, 1.1F, 0.0F, 2.0F), trosThrottleScale);
        mesh.chunkSetAngles("Z_throtle_tros_R", f1, 0.0F, 0.0F);
        f1 = cvt(interp(setNew.mix1, setOld.mix1, f), 0.0F, 1.2F, 25F, -25F);
        mesh.chunkSetAngles("Z_mix_L", f1, 0.0F, 0.0F);
        f1 = floatindex(cvt(interp(setNew.mix1, setOld.mix1, f), 0.0F, 1.2F, 0.0F, 2.0F), trosMixScale);
        mesh.chunkSetAngles("Z_mix_tros_L", f1, 0.0F, 0.0F);
        f1 = cvt(interp(setNew.mix2, setOld.mix2, f), 0.0F, 1.2F, 25F, -25F);
        mesh.chunkSetAngles("Z_mix_R", f1, 0.0F, 0.0F);
        f1 = floatindex(cvt(interp(setNew.mix2, setOld.mix2, f), 0.0F, 1.2F, 0.0F, 2.0F), trosMixScale);
        mesh.chunkSetAngles("Z_mix_tros_R", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_pitch_L", 0.0F, 0.0F, fm.EI.engines[0].getControlProp() * 360F);
        mesh.chunkSetAngles("Z_pitch_R", 0.0F, 0.0F, fm.EI.engines[1].getControlProp() * 360F);
        f1 = fm.EI.engines[0].getControlRadiator() * 60F - 30F;
        mesh.chunkSetAngles("Z_radiator_L", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_radiator_tros_L", -f1, 0.0F, 0.0F);
        f1 = fm.EI.engines[1].getControlRadiator() * 60F - 30F;
        mesh.chunkSetAngles("Z_radiator_R", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_radiator_tros_R", -f1, 0.0F, 0.0F);
        f1 = cvt(fm.EI.engines[0].getEngineLoad(), 0.0F, 0.018F, -15F, 20F);
        mesh.chunkSetAngles("Z_fire_L", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fire_tros_L", 0.0F, 0.0F, -f1);
        f1 = cvt(fm.EI.engines[1].getEngineLoad(), 0.0F, 0.018F, -15F, 20F);
        mesh.chunkSetAngles("Z_fire_R", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_fire_tros_R", 0.0F, 0.0F, -f1);
        mesh.chunkSetAngles("Z_ND_variometr2", cvt(setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_rpm_L", -cvt(fm.EI.engines[0].getRPM(), 0.0F, 2800F, 0.0F, 249F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_rpm_R", -cvt(fm.EI.engines[1].getRPM(), 0.0F, 2800F, 0.0F, 249F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_airspeed", -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeed2KMH()), 0.0F, 400F, 0.0F, 9F), speedometerScale), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_alt_m", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_alt_km", -cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        f1 = getBall(4D);
        mesh.chunkSetAngles("Z_ND_ball", cvt(f1, -4F, 4F, 8F, -8F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_boost_L", cvt(interp(setNew.manifold1, setOld.manifold1, f), 30F, 120F, 0.0F, -298F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_boost_R", cvt(interp(setNew.manifold2, setOld.manifold2, f), 30F, 120F, 0.0F, -298F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_Turn", cvt(setNew.turn, -0.23562F, 0.23562F, -30F, 30F), 0.0F, 0.0F);
        float f2 = fm.M.fuel / fm.M.maxFuel;
        mesh.chunkSetAngles("Z_ND_fuel_1", cvt(f2, 0.0F, 0.5F, 0.0F, -71F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_fuel_2", cvt(f2, 0.5F, 1.0F, 0.0F, -71F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_fuel_3", cvt(f2, 0.0F, 0.5F, 0.0F, -71F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_fuel_4", cvt(f2, 0.5F, 1.0F, 0.0F, -71F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Pedal_L", -15F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Pedal_R", 15F * fm.CT.getRudder(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Pedal_brake_L", -15F * fm.CT.getBrakeL(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Pedal_brake_R", 15F * fm.CT.getBrakeR(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_nd_k5_arrow2", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        SB sb = (SB)aircraft();
        mesh.chunkSetAngles("Z_nd_k5_arrow1", -sb.headingBug, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_oil_tem_L", cvt(fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, -86F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_oil_tem_R", cvt(fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, -86F), 0.0F, 0.0F);
        mesh.chunkSetAngles("hMagn_L", -cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 84F), 0.0F, 0.0F);
        mesh.chunkSetAngles("hMagn_R", cvt(fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 84F), 0.0F, 0.0F);
        mesh.chunkVisible("L_RED", fm.CT.pdiLights == 1);
        mesh.chunkVisible("L_WHITE", fm.CT.pdiLights == 2);
        mesh.chunkVisible("L_GREEN", fm.CT.pdiLights == 3);
        mesh.chunkSetAngles("Z_ND_Air_pres", -122F * fm.CT.getBrake(), 0.0F, 0.0F);
        float f3 = cvt(fm.EI.engines[0].getRPM(), 0.0F, 1700F, 0.0F, 28F);
        if(fm.AS.bLandingLightOn)
            f3--;
        if(fm.AS.bNavLightsOn)
            f3 -= 2.5F;
        mesh.chunkSetAngles("Z_ND_Voltmeter", -f3, 0.0F, 0.0F);
        float f4 = fm.CT.getFlap();
        if(f4 < 0.2F)
            mesh.chunkSetAngles("Z_ND_Flaps_position", -cvt(f4, 0.0F, 0.2F, 0.0F, 31F), 0.0F, 0.0F);
        else
        if(f4 < 0.33F)
            mesh.chunkSetAngles("Z_ND_Flaps_position", -cvt(f4, 0.2F, 0.33F, 31F, 50F), 0.0F, 0.0F);
        else
            mesh.chunkSetAngles("Z_ND_Flaps_position", -cvt(f4, 0.33F, 1.0F, 50F, 71F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_clock_hour", -cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_clock_min", -cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_clock_sec", -cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        if((double)f2 > 0.5D)
            f2 = cvt(f2, 0.5F, 0.505F, -200F, -290F);
        else
        if((double)f2 > 0.4995D)
            f2 = cvt(f2, 0.4995F, 0.5F, -290F, -200F);
        else
            f2 = cvt(f2, 0.0F, 0.005F, 0.0F, -290F);
        mesh.chunkSetAngles("Z_ND_fuelpress_L", f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_fuelpress_R", f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_oilpress_L", cvt(1.0F + 0.05F * fm.EI.engines[0].tOilIn, 0.0F, 8.5F, 0.0F, -270F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_oilpress_R", cvt(1.0F + 0.05F * fm.EI.engines[1].tOilIn, 0.0F, 8.5F, 0.0F, -270F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_water_tem_L", cvt(fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, -86F), 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_ND_water_tem_R", cvt(fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, -86F), 0.0F, 0.0F);
        if(RPK)
        {
            mesh.chunkSetAngles("Z_ND_RPK", -cvt(setNew.beaconDirection.getDeg(1.0F), -30F, 30F, -45F, 45F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_Alpha_L", -setNew.Stoichion1, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_Alpha_R", -setNew.Stoichion2, 0.0F, 0.0F);
            resetYPRmodifier();
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = cvt(fm.Or.getTangage(), -45F, 45F, -0.034F, 0.034F);
            Cockpit.xyz[2] = 0.0F;
            mesh.chunkSetLocate("Z_ND_ag3", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkSetAngles("Z_ND_fon_AG3", fm.Or.getKren(), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_Dirrect", setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_switch01", 90F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_switch02", 90F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_switch03", 90F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_switch04", 90F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_switch05", 90F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_switch06", cockpitLightControl ? 90F : 0.0F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_ufo_sw", cockpitLightControl ? 90F : 0.0F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_ANO_switch", fm.AS.bNavLightsOn ? 90F : 0.0F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_land_l_sw", fm.AS.bLandingLightOn ? 90F : 0.0F, 0.0F, 0.0F);
        } else
        {
            resetYPRmodifier();
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = cvt(fm.Or.getTangage(), -45F, 45F, -0.0365F, 0.0365F);
            Cockpit.xyz[2] = 0.0F;
            mesh.chunkSetLocate("Z_ND_ag2", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkSetAngles("Z_ND_fon_AG2", fm.Or.getKren(), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_ball2", cvt(f1, -4F, 4F, 5F, -5F), 0.0F, 0.0F);
            mesh.chunkSetAngles("Z_ND_Dirrectional", 0.0F, setNew.waypointAzimuth.getDeg(f), 0.0F);
            mesh.chunkSetAngles("Z_ND_switch01", 0.0F, 0.0F, -30F);
            mesh.chunkSetAngles("Z_ND_switch02", 0.0F, 0.0F, -30F);
            mesh.chunkSetAngles("Z_ND_switch03", 0.0F, 0.0F, -30F);
            mesh.chunkSetAngles("Z_ND_switch04", 0.0F, 0.0F, -30F);
            mesh.chunkSetAngles("Z_ND_switch05", 0.0F, 0.0F, -30F);
            mesh.chunkSetAngles("Z_ND_pit_light_switch", 0.0F, 0.0F, cockpitLightControl ? -30F : 30F);
            mesh.chunkSetAngles("Z_ND_landing_light_switch", 0.0F, 0.0F, fm.AS.bLandingLightOn ? -30F : 30F);
            mesh.chunkSetAngles("Z_ND_ANO_switch", 0.0F, 0.0F, fm.AS.bNavLightsOn ? -30F : 30F);
        }
    }

    public void toggleDim()
    {
        cockpitDimControl = !cockpitDimControl;
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            light1.light.setEmit(0.5F, 0.6F);
            light2.light.setEmit(0.5F, 0.6F);
            mesh.materialReplace("shturval2", "shturval2_light");
            mesh.materialReplace("clepki2", "clepki2_light");
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            mesh.materialReplace("shturval2", "shturval2");
            mesh.materialReplace("clepki2", "clepki2");
        }
        setNightMats(false);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("front_cannopy_DM2", true);
            mesh.chunkVisible("front_cannopy_DM1", true);
            mesh.chunkVisible("Main_DM1", true);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("prib_02", false);
            mesh.chunkVisible("Dprib_02", true);
            mesh.chunkVisible("prib_05", false);
            mesh.chunkVisible("Dprib_05", true);
            mesh.chunkVisible("Panel_DM", true);
            mesh.chunkVisible("Z_ND_airspeed", false);
            mesh.chunkVisible("Z_ND_Turn", false);
            mesh.chunkVisible("Z_ND_ball", false);
            mesh.chunkVisible("Z_ND_variometr2", false);
            mesh.chunkVisible("Z_ND_alt_km", false);
            mesh.chunkVisible("Z_ND_alt_m", false);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("Z_cannopy_DM2", true);
            mesh.chunkVisible("Z_cannopy_DM5", true);
            mesh.chunkVisible("prib_04", false);
            mesh.chunkVisible("Dprib_04", true);
            mesh.chunkVisible("Panel_DM", true);
            mesh.chunkVisible("Main_DM2", true);
            mesh.chunkVisible("Z_ND_oiltemp_R", false);
            mesh.chunkVisible("Z_ND_fuelpress_R", false);
            mesh.chunkVisible("Z_ND_oilpress_L", false);
            mesh.chunkVisible("Z_ND_fuel_1", false);
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("Z_cannopy_DM1", true);
            mesh.chunkVisible("Z_cannopy_DM4", true);
            mesh.chunkVisible("prib_01", false);
            mesh.chunkVisible("Dprib_01", true);
            mesh.chunkVisible("prib_06", false);
            mesh.chunkVisible("Dprib_06", true);
            mesh.chunkVisible("Panel_DM", true);
            mesh.chunkVisible("Main_DM3", true);
            mesh.chunkVisible("Z_ND_clock_hour", false);
            mesh.chunkVisible("Z_ND_clock_min", false);
            mesh.chunkVisible("Z_ND_ag2", false);
            mesh.chunkVisible("Z_ND_ag1", false);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("Z_cannopy_DM3", false);
            mesh.chunkVisible("prib_03", false);
            mesh.chunkVisible("Dprib_03", true);
            mesh.chunkVisible("Panel_DM", true);
            mesh.chunkVisible("Z_ND_rpm_R", false);
            mesh.chunkVisible("Z_ND_boost_R", false);
            mesh.chunkVisible("Z_ND_watertemp_L", false);
        }
    }

    public boolean isViewRight()
    {
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        HookPilot.current.computePos(this, loc, loc1);
        float f = loc1.getOrient().getYaw();
        if(f < 0.0F)
            isSlideRight = true;
        else
            isSlideRight = false;
        return isSlideRight;
    }

    protected boolean doFocusEnter()
    {
        return super.doFocusEnter();
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            super.doFocusLeave();
            return;
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private float pictAiler;
    private float pictElev;
    private float gears;
    private float gearsLever;
    private float flapsLever;
    private float flaps;
    public Vector3f w;
    private boolean isSlideRight;
    private boolean bNeedSetUp;
    private LightPointActor light1;
    private LightPointActor light2;
    private boolean RPK;
    private static final float speedometerScale[] = {
        0.0F, 30F, 47F, 5F, 85F, 133.5F, 190F, 248.5F, 305F, 360F
    };
    private static final float trosThrottleScale[] = {
        -31F, 3.5F, 40.5F
    };
    private static final float trosMixScale[] = {
        -23.5F, 0.0F, 24.5F
    };

    static 
    {
        Property.set(CockpitSB103.class, "normZN", 0.9F);
    }















}
