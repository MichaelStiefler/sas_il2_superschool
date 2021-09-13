package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitFiatBr2x extends CockpitPilot
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
                setNew.throttleL = 0.9F * setOld.throttleL + 0.1F * fm.EI.engines[0].getControlThrottle();
                setNew.propL = 0.9F * setOld.propL + 0.1F * fm.EI.engines[0].getControlProp();
                setNew.MixL = 0.8F * setOld.MixL + 0.2F * fm.EI.engines[0].getControlMix();
                setNew.man1 = 0.92F * setOld.man1 + 0.08F * fm.EI.engines[0].getManifoldPressure();
                setNew.throttleR = 0.9F * setOld.throttleR + 0.1F * fm.EI.engines[1].getControlThrottle();
                setNew.propR = 0.9F * setOld.propR + 0.1F * fm.EI.engines[1].getControlProp();
                setNew.MixR = 0.8F * setOld.MixR + 0.2F * fm.EI.engines[1].getControlMix();
                setNew.man2 = 0.92F * setOld.man2 + 0.08F * fm.EI.engines[1].getManifoldPressure();
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), 90F + fm.Or.azimut());
                float f = waypointAzimuth();
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
                if(useRealisticNavigationInstruments())
                {
                    setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
                    setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), (f - setOld.azimuth.getDeg(1.0F)) + 90F);
                }
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttleL;
        float throttleR;
        float propL;
        float propR;
        float MixL;
        float MixR;
        float man1;
        float man2;
        float altimeter;
        float elevTrim;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float vspeed;
        float beaconDirection;
        float beaconRange;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((FiatBr2x)this.fm.actor).bPitUnfocused = false;
            aircraft().hierMesh().chunkVisible("Interior_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(isFocused())
        {
            ((FiatBr2x)this.fm.actor).bPitUnfocused = true;
            aircraft().hierMesh().chunkVisible("Interior_D0", true);
            super.doFocusLeave();
        }
    }

    protected float waypointAzimuth()
    {
        WayPoint waypoint = this.fm.AP.way.curr();
        if(waypoint == null)
            return 0.0F;
        waypoint.getP(tmpP);
        tmpV.sub(tmpP, this.fm.Loc);
        float f = (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x)));
        while (f <= -180F) {
            f += 360F;
        }
        while (f > 180F) {
            f -= 360F;
        }
        return f;
    }

    public CockpitFiatBr2x()
    {
        super("3DO/Cockpit/FiatBr2xPilot/hier.him", "he111");
        bNeedSetUp = true;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictGear = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(109F, 99F, 90F);
        light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        HookNamed hooknamed1 = new HookNamed(this.mesh, "LAMPHOOK2");
        Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed1.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
        light2 = new LightPointActor(new LightPoint(), loc1.getPoint());
        light2.light.setColor(109F, 99F, 90F);
        light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        this.cockpitNightMats = (new String[] {
            "SM79_gauges_1", "SM79_gauges_2", "SM79_gauges_3", "SM79_gauges_1_dmg", "Ita_Needles"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        loadBuzzerFX();
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        this.mesh.chunkSetAngles("ZThrottleL", 0.0F, -49F * interp(setNew.throttleL, setOld.throttleL, f), 0.0F);
        this.mesh.chunkSetAngles("ZThrottleR", 0.0F, -49F * interp(setNew.throttleR, setOld.throttleR, f), 0.0F);
        this.mesh.chunkSetAngles("PitchL", 0.0F, -40F * interp(setNew.propL, setOld.propL, f), 0.0F);
        this.mesh.chunkSetAngles("PitchR", 0.0F, -40F * interp(setNew.propR, setOld.propR, f), 0.0F);
        this.mesh.chunkSetAngles("MixturreL", 0.0F, 35F * interp(setNew.MixL, setOld.MixL, f), 0.0F);
        this.mesh.chunkSetAngles("MixturreR", 0.0F, 35F * interp(setNew.MixR, setOld.MixR, f), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = 0.07F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Pedal_LeftL", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Pedal_LeftR", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        this.mesh.chunkSetLocate("Pedal_RightL", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Pedal_RightR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Wheel_Stick", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Wheel_PilotL", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Wheel_PilotR", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * this.fm.CT.AileronControl) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Z_BrkpresL", 0.0F, 7F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_BrkpresR", 0.0F, -7F * this.fm.CT.getBrake(), 0.0F);
        resetYPRmodifier();
        this.mesh.chunkSetAngles("ZmagnetoL", 0.0F, cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("ZmagnetoR", 0.0F, cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 110F), 0.0F);
        float f1 = interp(setNew.altimeter, setOld.altimeter, f) * 0.036F;
        this.mesh.chunkSetAngles("Zalt1L", 0.0F, f1 * 10F, 0.0F);
        this.mesh.chunkSetAngles("Zalt1R", 0.0F, f1 * 10F, 0.0F);
        this.mesh.chunkSetAngles("Zalt2L", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Zalt2R", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("ZSpeedL", 0.0F, floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 460F, 0.0F, 23F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("ZSpeedR", 0.0F, floatindex(cvt(Pitot.Indicator((float)this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 460F, 0.0F, 23F), speedometerScale), 0.0F);
        resetYPRmodifier();
        w.set(this.fm.getW());
        this.fm.Or.transform(w);
        this.mesh.chunkSetAngles("Zbank2L", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("Zbank1L", 0.0F, cvt(getBall(6D), -6F, 6F, -20.5F, 20.5F), 0.0F);
        this.mesh.chunkSetAngles("Zbank2R", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("Zbank1R", 0.0F, cvt(getBall(6D), -6F, 6F, -20.5F, 20.5F), 0.0F);
        this.mesh.chunkSetAngles("ZAH1R", 0.0F, -this.fm.Or.getKren(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(this.fm.Or.getTangage(), -45F, 45F, 0.011F, -0.015F);
        this.mesh.chunkSetLocate("ZcompassR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zAH1L", 0.0F, -this.fm.Or.getKren(), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = cvt(this.fm.Or.getTangage(), -45F, 45F, 0.02F, -0.02F);
        this.mesh.chunkSetLocate("ZcompassL", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_plane_FuelGaugeL", 0.0F, cvt(this.fm.M.fuel, 0.0F, 1720F, 0.0F, 245F), 0.0F);
        this.mesh.chunkSetAngles("Z_plane_FuelGaugeR", 0.0F, cvt(this.fm.M.fuel, 0.0F, 1720F, 0.0F, 245F), 0.0F);
        this.mesh.chunkSetAngles("Ztrim1", 0.0F, cvt(interp(setNew.elevTrim, setOld.elevTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
        resetYPRmodifier();
        float f2 = setNew.vspeed * 18F;
        this.mesh.chunkSetAngles("ZclimbL", 0.0F, f2, 0.0F);
        this.mesh.chunkSetAngles("ZclimbR", 0.0F, f2, 0.0F);
        this.mesh.chunkSetAngles("Zdirectional_GiroC", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Zdirectional_GiroR", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Zdirectional_GiroL", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("ZRPML", 0.0F, cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F);
        this.mesh.chunkSetAngles("ZRPMR", 0.0F, cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F);
        this.mesh.chunkSetAngles("ZclockHour", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("ZclockMin", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrL", 0.0F, cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.0F, 2.0F, 0.0F, 312F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrR", 0.0F, cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.0F, 2.0F, 0.0F, 312F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrEL", 0.0F, floatindex(cvt(this.fm.EI.engines[0].tOilIn, 40F, 160F, 0.0F, 6F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrUL", 0.0F, floatindex(cvt(this.fm.EI.engines[0].tOilOut, 40F, 160F, 0.0F, 6F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrER", 0.0F, floatindex(cvt(this.fm.EI.engines[1].tOilIn, 40F, 160F, 0.0F, 6F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrUR", 0.0F, floatindex(cvt(this.fm.EI.engines[1].tOilOut, 40F, 160F, 0.0F, 6F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZoilL", 0.0F, cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 10F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("ZoilR", 0.0F, cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 10F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("ZfuelL", 0.0F, cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZfuelR", 0.0F, cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", cvt(setNew.man1, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres2", cvt(setNew.man2, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FlapPos", cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkVisible("ZlampRedL", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("ZlampRedR", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.rgear);
        buzzerFX(this.fm.CT.getPowerControl() < 0.15F && this.fm.CT.getGear() < 0.99F);
        this.mesh.chunkVisible("ZlampGreenL", this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("ZlampGreenR", this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear);
        this.mesh.chunkSetAngles("Mplane_LandingGear_L", 0.0F, 0.0F, 10F * this.fm.CT.getGear());
        this.mesh.chunkSetAngles("Mplane_LandingGear_R", 0.0F, 0.0F, 10F * this.fm.CT.getGear());
        this.mesh.chunkSetAngles("Mplane_LandingGear_L1", 0.0F, 90F * this.fm.CT.getGear(), 0.0F);
        this.mesh.chunkSetAngles("Mplane_LandingGear_R1", 0.0F, -90F * this.fm.CT.getGear(), 0.0F);
        this.mesh.chunkSetAngles("Zgear", 30F * (pictGear = 0.89F * pictGear + 0.11F * this.fm.CT.GearControl), 0.0F, 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = -cvt(interp(setNew.elevTrim, setOld.elevTrim, f), -0.5F, 0.5F, -0.057F, 0.058F);
        this.mesh.chunkSetLocate("Z_Trim_Indicator", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_OAT", 0.0F, cvt(Atmosphere.temperature((float)this.fm.Loc.z) - 273.15F, -40F, 40F, 0.0F, 93F), 0.0F);
        if(useRealisticNavigationInstruments())
        {
            float f3 = -cvt(setNew.beaconDirection, -55F, 55F, -55F, 55F);
            this.mesh.chunkSetAngles("Zcourse", 0.0F, f3, 0.0F);
        } else
        {
            float f4 = -cvt(setNew.waypointAzimuth.getDeg(f * 0.1F), -55F, 55F, -55F, 55F);
            this.mesh.chunkSetAngles("Zcourse", 0.0F, f4, 0.0F);
        }
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
        {
            light1.light.setEmit(0.0032F, 7.2F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private LightPointActor light1;
    private LightPointActor light2;
    private boolean bNeedSetUp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictGear;
    private static final float speedometerScale[] = {
        0.0F, 10F, 20F, 30F, 50F, 68F, 88F, 109F, 126F, 142F, 
        159F, 176F, 190F, 206F, 220F, 238F, 253F, 270F, 285F, 300F, 
        312F, 325F, 337F, 350F, 360F
    };
    private static final float oilTempScale[] = {
        0.0F, 26F, 54F, 95F, 154F, 244F, 359F
    };
    private Point3d tmpP;
    private Vector3d tmpV;

}
