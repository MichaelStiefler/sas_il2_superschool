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

public class CockpitSM81x extends CockpitPilot {
    private class Variables {

        float      throttleL;
        float      throttleR;
        float      throttleC;
        float      propL;
        float      propR;
        float      propC;
        float      MixL;
        float      MixR;
        float      MixC;
        float      man0;
        float      man1;
        float      man2;
        float      altimeter;
        float      elevTrim;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      beaconDirection;
        float      beaconRange;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitSM81x.this.fm != null) {
                CockpitSM81x.this.setTmp = CockpitSM81x.this.setOld;
                CockpitSM81x.this.setOld = CockpitSM81x.this.setNew;
                CockpitSM81x.this.setNew = CockpitSM81x.this.setTmp;
                CockpitSM81x.this.setNew.throttleL = (0.9F * CockpitSM81x.this.setOld.throttleL) + (0.1F * CockpitSM81x.this.fm.EI.engines[0].getControlThrottle());
                CockpitSM81x.this.setNew.propL = (0.9F * CockpitSM81x.this.setOld.propL) + (0.1F * CockpitSM81x.this.fm.EI.engines[0].getControlProp());
                CockpitSM81x.this.setNew.MixL = (0.8F * CockpitSM81x.this.setOld.MixL) + (0.2F * CockpitSM81x.this.fm.EI.engines[0].getControlMix());
                CockpitSM81x.this.setNew.man0 = (0.92F * CockpitSM81x.this.setOld.man0) + (0.08F * CockpitSM81x.this.fm.EI.engines[0].getManifoldPressure());
                CockpitSM81x.this.setNew.throttleR = (0.9F * CockpitSM81x.this.setOld.throttleR) + (0.1F * CockpitSM81x.this.fm.EI.engines[2].getControlThrottle());
                CockpitSM81x.this.setNew.propR = (0.9F * CockpitSM81x.this.setOld.propR) + (0.1F * CockpitSM81x.this.fm.EI.engines[2].getControlProp());
                CockpitSM81x.this.setNew.MixR = (0.8F * CockpitSM81x.this.setOld.MixR) + (0.2F * CockpitSM81x.this.fm.EI.engines[2].getControlMix());
                CockpitSM81x.this.setNew.man2 = (0.92F * CockpitSM81x.this.setOld.man2) + (0.08F * CockpitSM81x.this.fm.EI.engines[2].getManifoldPressure());
                CockpitSM81x.this.setNew.throttleC = (0.9F * CockpitSM81x.this.setOld.throttleR) + (0.1F * CockpitSM81x.this.fm.EI.engines[1].getControlThrottle());
                CockpitSM81x.this.setNew.propC = (0.9F * CockpitSM81x.this.setOld.propR) + (0.1F * CockpitSM81x.this.fm.EI.engines[1].getControlProp());
                CockpitSM81x.this.setNew.MixC = (0.8F * CockpitSM81x.this.setOld.MixR) + (0.2F * CockpitSM81x.this.fm.EI.engines[1].getControlMix());
                CockpitSM81x.this.setNew.man1 = (0.92F * CockpitSM81x.this.setOld.man1) + (0.08F * CockpitSM81x.this.fm.EI.engines[1].getManifoldPressure());
                CockpitSM81x.this.setNew.altimeter = CockpitSM81x.this.fm.getAltitude();
                CockpitSM81x.this.setNew.azimuth.setDeg(CockpitSM81x.this.setOld.azimuth.getDeg(1.0F), 90F + CockpitSM81x.this.fm.Or.azimut());
                float f = CockpitSM81x.this.waypointAzimuth();
                CockpitSM81x.this.setNew.vspeed = ((199F * CockpitSM81x.this.setOld.vspeed) + CockpitSM81x.this.fm.getVertSpeed()) / 200F;
                if (CockpitSM81x.this.useRealisticNavigationInstruments()) {
                    CockpitSM81x.this.setNew.beaconDirection = ((10F * CockpitSM81x.this.setOld.beaconDirection) + CockpitSM81x.this.getBeaconDirection()) / 11F;
                    CockpitSM81x.this.setNew.beaconRange = ((10F * CockpitSM81x.this.setOld.beaconRange) + CockpitSM81x.this.getBeaconRange()) / 11F;
                } else {
                    CockpitSM81x.this.setNew.waypointAzimuth.setDeg(CockpitSM81x.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitSM81x.this.setOld.azimuth.getDeg(1.0F)) + 90F);
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((SM81x) this.fm.actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Interior_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            ((SM81x) this.fm.actor).bPitUnfocused = true;
            this.aircraft().hierMesh().chunkVisible("Interior_D0", true);
            super.doFocusLeave();
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        }
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float f = (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x)));
        while (f <= -180F) {
            f += 360F;
        }
        while (f > 180F) {
            f -= 360F;
        }
        return f;
    }

    public CockpitSM81x() {
        super("3DO/Cockpit/SM81xPilot/hier.him", "he111");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(109F, 99F, 90F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        HookNamed hooknamed1 = new HookNamed(this.mesh, "LAMPHOOK2");
        Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed1.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
        this.light2 = new LightPointActor(new LightPoint(), loc1.getPoint());
        this.light2.light.setColor(109F, 99F, 90F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "SM79_gauges_1", "SM79_gauges_2", "SM79_gauges_3", "SM79_gauges_1_dmg", "Ita_Needles" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.loadBuzzerFX();
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("ZThrottleL", 0.0F, -49F * this.interp(this.setNew.throttleL, this.setOld.throttleL, f), 0.0F);
        this.mesh.chunkSetAngles("ZThrottleR", 0.0F, -49F * this.interp(this.setNew.throttleR, this.setOld.throttleR, f), 0.0F);
        this.mesh.chunkSetAngles("ZThrottleC", 0.0F, -49F * this.interp(this.setNew.throttleC, this.setOld.throttleC, f), 0.0F);
        this.mesh.chunkSetAngles("PitchL", 0.0F, -40F * this.interp(this.setNew.propL, this.setOld.propL, f), 0.0F);
        this.mesh.chunkSetAngles("PitchR", 0.0F, -40F * this.interp(this.setNew.propR, this.setOld.propR, f), 0.0F);
        this.mesh.chunkSetAngles("PitchC", 0.0F, -40F * this.interp(this.setNew.propC, this.setOld.propC, f), 0.0F);
        this.mesh.chunkSetAngles("MixturreL", 0.0F, 35F * this.interp(this.setNew.MixL, this.setOld.MixL, f), 0.0F);
        this.mesh.chunkSetAngles("MixturreR", 0.0F, 35F * this.interp(this.setNew.MixR, this.setOld.MixR, f), 0.0F);
        this.mesh.chunkSetAngles("MixturreC", 0.0F, 35F * this.interp(this.setNew.MixC, this.setOld.MixC, f), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = 0.07F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Pedal_LeftL", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Pedal_LeftR", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        this.mesh.chunkSetLocate("Pedal_RightL", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Pedal_RightR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Wheel_Stick", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Wheel_PilotL", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Wheel_PilotR", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Z_BrkpresL", 0.0F, 7F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_BrkpresR", 0.0F, -7F * this.fm.CT.getBrake(), 0.0F);
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("ZmagnetoL", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("ZmagnetoR", 0.0F, this.cvt(this.fm.EI.engines[2].getControlMagnetos(), 0.0F, 3F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("ZmagnetoC", 0.0F, this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 110F), 0.0F);
        float f1 = this.interp(this.setNew.altimeter, this.setOld.altimeter, f) * 0.036F;
        this.mesh.chunkSetAngles("Zalt1L", 0.0F, f1 * 10F, 0.0F);
        this.mesh.chunkSetAngles("Zalt1R", 0.0F, f1 * 10F, 0.0F);
        this.mesh.chunkSetAngles("Zalt2L", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Zalt2R", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("ZSpeedL", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 460F, 0.0F, 23F), CockpitSM81x.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("ZSpeedR", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 460F, 0.0F, 23F), CockpitSM81x.speedometerScale), 0.0F);
        this.resetYPRmodifier();
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Zbank2L", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("Zbank1L", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -20.5F, 20.5F), 0.0F);
        this.mesh.chunkSetAngles("Zbank2R", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("Zbank1R", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -20.5F, 20.5F), 0.0F);
        this.mesh.chunkSetAngles("ZAH1R", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.011F, -0.015F);
        this.mesh.chunkSetLocate("ZcompassR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zAH1L", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.02F, -0.02F);
        this.mesh.chunkSetLocate("ZcompassL", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_plane_FuelGaugeL", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 1720F, 0.0F, 245F), 0.0F);
        this.mesh.chunkSetAngles("Z_plane_FuelGaugeR", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 1720F, 0.0F, 245F), 0.0F);
        this.mesh.chunkSetAngles("Ztrim1", 0.0F, this.cvt(this.interp(this.setNew.elevTrim, this.setOld.elevTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
        this.resetYPRmodifier();
        float f2 = this.setNew.vspeed * 18F;
        this.mesh.chunkSetAngles("ZclimbL", 0.0F, f2, 0.0F);
        this.mesh.chunkSetAngles("ZclimbR", 0.0F, f2, 0.0F);
        this.mesh.chunkSetAngles("Zdirectional_GiroL", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Zdirectional_GiroC", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Zdirectional_GiroR", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("ZRPML", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F);
        this.mesh.chunkSetAngles("ZRPMR", 0.0F, this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F);
        this.mesh.chunkSetAngles("ZRPMC", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3000F, 15F, 345F), 0.0F);
        this.mesh.chunkSetAngles("ZclockHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("ZclockMin", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrL", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.0F, 2.0F, 0.0F, 312F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrR", 0.0F, this.cvt(this.fm.EI.engines[2].getManifoldPressure(), 0.0F, 2.0F, 0.0F, 312F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrC", 0.0F, this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.0F, 2.0F, 0.0F, 312F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrEL", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilIn, 40F, 160F, 0.0F, 6F), CockpitSM81x.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrUL", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 160F, 0.0F, 6F), CockpitSM81x.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrER", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[2].tOilIn, 40F, 160F, 0.0F, 6F), CockpitSM81x.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrUR", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[2].tOilOut, 40F, 160F, 0.0F, 6F), CockpitSM81x.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrEC", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[1].tOilIn, 40F, 160F, 0.0F, 6F), CockpitSM81x.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrUC", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 40F, 160F, 0.0F, 6F), CockpitSM81x.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZoilL", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 10F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("ZoilR", 0.0F, this.cvt(this.fm.EI.engines[2].tOilIn, 0.0F, 10F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("ZoilC", 0.0F, this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 10F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("ZfuelL", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZfuelR", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZfuelC", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -this.cvt(this.interp(this.setNew.elevTrim, this.setOld.elevTrim, f), -0.5F, 0.5F, -0.057F, 0.058F);
        this.mesh.chunkSetLocate("Z_Trim_Indicator", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_OAT", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -40F, 40F, 0.0F, 93F), 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            float f3 = -this.cvt(this.setNew.beaconDirection, -55F, 55F, -55F, 55F);
            this.mesh.chunkSetAngles("Zcourse", 0.0F, f3, 0.0F);
        } else {
            float f4 = -this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.1F), -55F, 55F, -55F, 55F);
            this.mesh.chunkSetAngles("Zcourse", 0.0F, f4, 0.0F);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0032F, 7.2F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private LightPointActor    light1;
    private LightPointActor    light2;
    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 10F, 20F, 30F, 50F, 68F, 88F, 109F, 126F, 142F, 159F, 176F, 190F, 206F, 220F, 238F, 253F, 270F, 285F, 300F, 312F, 325F, 337F, 350F, 360F };
    private static final float oilTempScale[]     = { 0.0F, 26F, 54F, 95F, 154F, 244F, 359F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
