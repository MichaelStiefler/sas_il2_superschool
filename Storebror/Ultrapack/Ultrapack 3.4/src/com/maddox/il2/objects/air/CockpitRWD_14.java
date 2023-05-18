package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitRWD_14 extends CockpitPilot {
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

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitRWD_14.this.bNeedSetUp) {
                CockpitRWD_14.this.reflectPlaneMats();
                CockpitRWD_14.this.bNeedSetUp = false;
            }
            if (RWD_14.bChangedPit) {
                CockpitRWD_14.this.reflectPlaneToModel();
                RWD_14.bChangedPit = false;
            }
            CockpitRWD_14.this.setTmp = CockpitRWD_14.this.setOld;
            CockpitRWD_14.this.setOld = CockpitRWD_14.this.setNew;
            CockpitRWD_14.this.setNew = CockpitRWD_14.this.setTmp;
            CockpitRWD_14.this.setNew.altimeter = CockpitRWD_14.this.fm.getAltitude();
            if (Math.abs(CockpitRWD_14.this.fm.Or.getKren()) < 30F) {
                CockpitRWD_14.this.setNew.azimuth = ((35F * CockpitRWD_14.this.setOld.azimuth) + CockpitRWD_14.this.fm.Or.azimut()) / 36F;
            }
            if ((CockpitRWD_14.this.setOld.azimuth > 270F) && (CockpitRWD_14.this.setNew.azimuth < 90F)) {
                CockpitRWD_14.this.setOld.azimuth -= 360F;
            }
            if ((CockpitRWD_14.this.setOld.azimuth < 90F) && (CockpitRWD_14.this.setNew.azimuth > 270F)) {
                CockpitRWD_14.this.setOld.azimuth += 360F;
            }
            CockpitRWD_14.this.setNew.mix = ((10F * CockpitRWD_14.this.setOld.mix) + CockpitRWD_14.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitRWD_14.this.setNew.throttle = ((10F * CockpitRWD_14.this.setOld.throttle) + CockpitRWD_14.this.fm.CT.PowerControl) / 11F;
            CockpitRWD_14.this.w.set(CockpitRWD_14.this.fm.getW());
            CockpitRWD_14.this.fm.Or.transform(CockpitRWD_14.this.w);
            CockpitRWD_14.this.setNew.turn = ((33F * CockpitRWD_14.this.setOld.turn) + CockpitRWD_14.this.w.z) / 34F;
            CockpitRWD_14.this.setNew.power = (0.85F * CockpitRWD_14.this.setOld.power) + (CockpitRWD_14.this.fm.EI.engines[0].getPowerOutput() * 0.15F);
            CockpitRWD_14.this.setNew.fuelpressure = (0.9F * CockpitRWD_14.this.setOld.fuelpressure) + (((CockpitRWD_14.this.fm.M.fuel <= 1.0F) || (CockpitRWD_14.this.fm.EI.engines[0].getStage() != 6) ? 0.0F : 0.026F * (10F + (float) Math.sqrt(CockpitRWD_14.this.setNew.power))) * 0.1F);
            CockpitRWD_14.this.setNew.vspeed = ((199F * CockpitRWD_14.this.setOld.vspeed) + CockpitRWD_14.this.fm.getVertSpeed()) / 200F;
            CockpitRWD_14.this.mesh.chunkSetAngles("Turret1A", 0.0F, -CockpitRWD_14.this.aircraft().FM.turret[0].tu[0], 0.0F);
            CockpitRWD_14.this.mesh.chunkSetAngles("Turret1B", 0.0F, CockpitRWD_14.this.aircraft().FM.turret[0].tu[1], 0.0F);
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitRWD_14() {
        super("3DO/Cockpit/RWD-14/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(0.8980392F, 0.8117647F, 0.9235294F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.cockpitNightMats = (new String[] { "compass", "dials1", "dials2", "dials3", "gauges" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 7000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 20F), CockpitRWD_14.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F + this.interp(-this.setNew.azimuth, -this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, this.cvt(this.setNew.fuelpressure, 0.0F, 0.6F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zFuel", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 250F, 0.0F, 18F), CockpitRWD_14.fuelScale), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 268F), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), CockpitRWD_14.oilTempScale), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -20F, 20F, 0.0385F, -0.0385F);
        this.mesh.chunkSetLocate("zPitch", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2400F, 0.0F, 12F), CockpitRWD_14.rpmScale), 0.0F);
        this.mesh.chunkSetAngles("zSlide", 0.0F, this.cvt(this.getBall(3D), -2F, 2.0F, -4F, 4F), 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.7F, 0.7F, -1.5F, 1.5F), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 0.0F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Rudder", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, 0.0F + (40F * this.interp(this.setNew.throttle, this.setOld.throttle, f)));
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, 0.0F + (38F * this.interp(this.setNew.mix, this.setOld.mix, f)));
        this.mesh.chunkSetAngles("zMagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVspeed", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Cockpit", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (Actor.isAlive(this.aircraft())) {
            this.aircraft().hierMesh().chunkVisible("Cockpit", true);
        }
        super.doFocusLeave();
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
        this.mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
        this.mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.2F, 0.2F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 3F, 6F, 9F, 13F, 28F, 41.5F, 54F, 71.5F, 90F, 112.5F, 135F, 156F, 180F, 203.5F, 224.5F, 247F, 270F, 289.5F, 313.5F, 336F };
    private static final float rpmScale[]         = { 0.0F, 15F, 30F, 60F, 90F, 120F, 150F, 180F, 210F, 240F, 270F, 300F, 330F };
    private static final float fuelScale[]        = { 0.0F, 15F, 30F, 44F, 56F, 70F, 83F, 95F, 106F, 124F, 141F, 158F, 175F, 192F, 210F, 230F, 247F, 268F, 288F };
    private static final float oilTempScale[]     = { 0.0F, 20F, 40F, 80F, 120F, 160F, 240F, 320F };

}
