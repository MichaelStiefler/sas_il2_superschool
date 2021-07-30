package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Time;

public class CockpitPZL23_42_BASE extends CockpitPilot {
    class Interpolater extends InterpolateRef {
        private Cockpit cockpit;

        public boolean tick() {
            if (CockpitPZL23_42_BASE.this.bNeedSetUp) {
                CockpitPZL23_42_BASE.this.reflectPlaneMats();
                CockpitPZL23_42_BASE.this.bNeedSetUp = false;
            }
            if (this.cockpit instanceof CockpitPZL23B) {
                if (PZL23B.bChangedPit) {
                    CockpitPZL23_42_BASE.this.reflectPlaneToModel();
                    PZL23B.bChangedPit = false;
                }
            } else if (this.cockpit instanceof CockpitPZL42) if (PZL42.bChangedPit) {
                CockpitPZL23_42_BASE.this.reflectPlaneToModel();
                PZL42.bChangedPit = false;
            }
            CockpitPZL23_42_BASE.this.setTmp = CockpitPZL23_42_BASE.this.setOld;
            CockpitPZL23_42_BASE.this.setOld = CockpitPZL23_42_BASE.this.setNew;
            CockpitPZL23_42_BASE.this.setNew = CockpitPZL23_42_BASE.this.setTmp;
            CockpitPZL23_42_BASE.this.setNew.altimeter = CockpitPZL23_42_BASE.this.fm.getAltitude();
            if (Math.abs(CockpitPZL23_42_BASE.this.fm.Or.getKren()) < 30F) CockpitPZL23_42_BASE.this.setNew.azimuth = (35F * CockpitPZL23_42_BASE.this.setOld.azimuth + CockpitPZL23_42_BASE.this.fm.Or.azimut()) / 36F;
            if (CockpitPZL23_42_BASE.this.setOld.azimuth > 270F && CockpitPZL23_42_BASE.this.setNew.azimuth < 90F) CockpitPZL23_42_BASE.this.setOld.azimuth -= 360F;
            if (CockpitPZL23_42_BASE.this.setOld.azimuth < 90F && CockpitPZL23_42_BASE.this.setNew.azimuth > 270F) CockpitPZL23_42_BASE.this.setOld.azimuth += 360F;
            CockpitPZL23_42_BASE.this.setNew.mix = (10F * CockpitPZL23_42_BASE.this.setOld.mix + CockpitPZL23_42_BASE.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitPZL23_42_BASE.this.setNew.throttle = (10F * CockpitPZL23_42_BASE.this.setOld.throttle + CockpitPZL23_42_BASE.this.fm.CT.PowerControl) / 11F;
            CockpitPZL23_42_BASE.this.w.set(CockpitPZL23_42_BASE.this.fm.getW());
            CockpitPZL23_42_BASE.this.fm.Or.transform(CockpitPZL23_42_BASE.this.w);
            CockpitPZL23_42_BASE.this.setNew.turn = (33F * CockpitPZL23_42_BASE.this.setOld.turn + ((Tuple3f) CockpitPZL23_42_BASE.this.w).z) / 34F;
            CockpitPZL23_42_BASE.this.setNew.power = 0.85F * CockpitPZL23_42_BASE.this.setOld.power + CockpitPZL23_42_BASE.this.fm.EI.engines[0].getPowerOutput() * 0.15F;
            CockpitPZL23_42_BASE.this.setNew.fuelpressure = 0.9F * CockpitPZL23_42_BASE.this.setOld.fuelpressure
                    + (CockpitPZL23_42_BASE.this.fm.M.fuel <= 1.0F || CockpitPZL23_42_BASE.this.fm.EI.engines[0].getStage() != 6 ? 0.0F : 0.026F * (10F + (float) Math.sqrt(CockpitPZL23_42_BASE.this.setNew.power))) * 0.1F;
            CockpitPZL23_42_BASE.this.setNew.vspeed = (199F * CockpitPZL23_42_BASE.this.setOld.vspeed + CockpitPZL23_42_BASE.this.fm.getVertSpeed()) / 200F;
            CockpitPZL23_42_BASE.this.mesh.chunkSetAngles("Turret1A", 0.0F, ((SndAircraft) CockpitPZL23_42_BASE.this.aircraft()).FM.turret[0].tu[0], 0.0F);
            CockpitPZL23_42_BASE.this.mesh.chunkSetAngles("Turret1B", 0.0F, ((SndAircraft) CockpitPZL23_42_BASE.this.aircraft()).FM.turret[0].tu[1], 0.0F);
            return true;
        }

        Interpolater(Cockpit cockpit) {
            this.cockpit = cockpit;
        }
    }

    private class Variables {

        float altimeter;
        float azimuth;
        float mix;
        float throttle;
        float turn;
        float power;
        float fuelpressure;
        float vspeed;

        private Variables() {
        }

    }

    public CockpitPZL23_42_BASE(String hierHim, String nameRef) {
        super(hierHim, nameRef);
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.interpPut(new Interpolater(this), null, Time.current(), null);
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(0.8980392F, 0.8117647F, 0.9235294F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(0.8980392F, 0.8117647F, 0.9235294F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = new String[] { "dials1", "dials2", "dials3", "dials4", "dials5", "gauges" };
        this.setNightMats(false);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 1000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 500F, 0.0F, 25F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.72421F, 1.27579F, -160F, 160F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, 90F + this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, 90F + this.interp(-this.setNew.azimuth, -this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 16F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 12F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, this.cvt(this.setNew.fuelpressure, 0.0F, 0.6F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 268F), 0.0F);
        this.mesh.chunkSetAngles("zOilIn", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 140F, 0.0F, 7F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zMagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -20F, 20F, 0.0485F, -0.0485F);
        this.mesh.chunkSetLocate("zPitch1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3200F, 0.0F, 16F), rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Rudder", 26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.3F, 0.3F, -1.8F, 1.8F), 0.0F);
        this.mesh.chunkSetAngles("zSlide1", 0.0F, this.cvt(this.getBall(3D), -3F, 3F, -9F, 9F), 0.0F);
        this.mesh.chunkSetAngles("zSlide2", 0.0F, this.cvt(this.getBall(3D), -3F, 3F, -6F, 6F), 0.0F);
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, -33F + 30F * this.interp(this.setNew.mix, this.setOld.mix, f));
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -55F + 50F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("zAH", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("zVspeed", 0.0F, this.cvt(this.setNew.vspeed, -18F, 18F, -180F, 180F), 0.0F);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("Engine1_D0", hiermesh.isChunkVisible("Engine1_D0"));
        this.mesh.chunkVisible("Engine1_D1", hiermesh.isChunkVisible("Engine1_D1"));
        this.mesh.chunkVisible("Engine1_D2", hiermesh.isChunkVisible("Engine1_D2"));
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
        this.mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
    }

    protected void reflectPlaneMats() {
        this.mesh.materialReplace("Gloss1D0o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss1D0o")));
        this.mesh.materialReplace("Gloss1D1o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss1D1o")));
        this.mesh.materialReplace("Gloss1D2o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss1D2o")));
        this.mesh.materialReplace("Matt1D0o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Matt1D0o")));
        this.mesh.materialReplace("Matt2D0o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Matt2D0o")));
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.3F, 0.3F);
            this.light2.light.setEmit(0.3F, 0.3F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 1.0F, 3F, 6.2F, 12F, 26.5F, 39F, 51F, 67.5F, 85.5F, 108F, 131.5F, 154F, 180F, 205.7F, 228.2F, 251F, 272.9F, 291.9F, 314.5F, 336.5F, 354F, 360F, 363F, 364F, 365F };
    private static final float rpmScale[]         = { 0.0F, 12F, 25F, 47F, 68F, 90F, 112F, 134F, 157F, 180F, 203F, 226F, 248F, 270F, 292F, 314F, 335F };
    private static final float oilTempScale[]     = { 0.0F, 20F, 40F, 80F, 120F, 160F, 240F, 320F };
}
