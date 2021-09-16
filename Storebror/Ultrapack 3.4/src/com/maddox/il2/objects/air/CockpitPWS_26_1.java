package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitPWS_26_1 extends CockpitPilot {
    private class Variables {

        float altimeter;
        float azimuth;
        float mix;
        float throttle;
        float turn;
        float power;
        float fuelpressure;

        private Variables() {
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitPWS_26_1.this.fm = World.getPlayerFM();
            if (CockpitPWS_26_1.this.fm == null) {
                return true;
            }
            if (CockpitPWS_26_1.this.bNeedSetUp) {
                CockpitPWS_26_1.this.reflectPlaneMats();
                CockpitPWS_26_1.this.reflectPlaneToModel();
                CockpitPWS_26_1.this.bNeedSetUp = false;
            }
            if (PWS_26.bChangedPit) {
                CockpitPWS_26_1.this.reflectPlaneToModel();
                PWS_26.bChangedPit = false;
            }
            CockpitPWS_26_1.this.setTmp = CockpitPWS_26_1.this.setOld;
            CockpitPWS_26_1.this.setOld = CockpitPWS_26_1.this.setNew;
            CockpitPWS_26_1.this.setNew = CockpitPWS_26_1.this.setTmp;
            CockpitPWS_26_1.this.setNew.altimeter = CockpitPWS_26_1.this.fm.getAltitude();
            if (Math.abs(CockpitPWS_26_1.this.fm.Or.getKren()) < 30F) {
                CockpitPWS_26_1.this.setNew.azimuth = ((35F * CockpitPWS_26_1.this.setOld.azimuth) + CockpitPWS_26_1.this.fm.Or.azimut()) / 36F;
            }
            if ((CockpitPWS_26_1.this.setOld.azimuth > 270F) && (CockpitPWS_26_1.this.setNew.azimuth < 90F)) {
                CockpitPWS_26_1.this.setOld.azimuth -= 360F;
            }
            if ((CockpitPWS_26_1.this.setOld.azimuth < 90F) && (CockpitPWS_26_1.this.setNew.azimuth > 270F)) {
                CockpitPWS_26_1.this.setOld.azimuth += 360F;
            }
            CockpitPWS_26_1.this.setNew.mix = ((10F * CockpitPWS_26_1.this.setOld.mix) + CockpitPWS_26_1.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitPWS_26_1.this.setNew.throttle = ((10F * CockpitPWS_26_1.this.setOld.throttle) + CockpitPWS_26_1.this.fm.CT.PowerControl) / 11F;
            CockpitPWS_26_1.this.w.set(CockpitPWS_26_1.this.fm.getW());
            CockpitPWS_26_1.this.fm.Or.transform(CockpitPWS_26_1.this.w);
            CockpitPWS_26_1.this.setNew.turn = ((33F * CockpitPWS_26_1.this.setOld.turn) + CockpitPWS_26_1.this.w.z) / 34F;
            CockpitPWS_26_1.this.setNew.power = (0.85F * CockpitPWS_26_1.this.setOld.power) + (CockpitPWS_26_1.this.fm.EI.engines[0].getPowerOutput() * 0.15F);
            CockpitPWS_26_1.this.setNew.fuelpressure = (0.9F * CockpitPWS_26_1.this.setOld.fuelpressure) + (((CockpitPWS_26_1.this.fm.M.fuel <= 1.0F) || (CockpitPWS_26_1.this.fm.EI.engines[0].getStage() != 6) ? 0.0F : 0.026F * (10F + (float) Math.sqrt(CockpitPWS_26_1.this.setNew.power))) * 0.1F);
            float f = CockpitPWS_26_1.this.fm.CT.getAileron();
            CockpitPWS_26_1.this.mesh.chunkSetAngles("AroneL_D0", 0.0F, -20F * f, 0.0F);
            CockpitPWS_26_1.this.mesh.chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
            CockpitPWS_26_1.this.mesh.chunkSetAngles("AroneL1_D0", 0.0F, 20F * f, 0.0F);
            CockpitPWS_26_1.this.mesh.chunkSetAngles("AroneR1_D0", 0.0F, 20F * f, 0.0F);
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitPWS_26_1() {
        super("3DO/Cockpit/PWS-26-1/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 7000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F + this.interp(-this.setNew.azimuth, -this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zFuel", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 112F, 0.0F, 7F), CockpitPWS_26_1.fuelScale), 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, this.cvt(this.setNew.fuelpressure, 0.0F, 0.6F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zHour1", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zHour2", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zMinute1", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zMinute2", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 268F), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), CockpitPWS_26_1.oilTempScale), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -20F, 20F, -0.04F, 0.04F);
        this.mesh.chunkSetLocate("zPitch1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("zPitch2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2600F, 0.0F, 13F), CockpitPWS_26_1.rpmScale), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 280F, 0.0F, 14F), CockpitPWS_26_1.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zSpeed2", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 280F, 0.0F, 14F), CockpitPWS_26_1.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zSlide", 0.0F, this.cvt(this.getBall(3D), -2F, 2.0F, -4F, 4F), 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.7F, 0.7F, -1.5F, 1.5F), 0.0F);
        this.mesh.chunkSetAngles("StickBase", 0.0F, 15F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 0.0F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("StickRod", 0.0F, 0.0F, -this.pictElev * 12F);
        this.mesh.chunkSetAngles("Rudder", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, 0.0F + (48F * this.interp(this.setNew.throttle, this.setOld.throttle, f)));
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, 0.0F + (48F * this.interp(this.setNew.mix, this.setOld.mix, f)));
        this.mesh.chunkSetAngles("MagnetoSw", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingLOut_D0", hiermesh.isChunkVisible("WingLOut_D0"));
        this.mesh.chunkVisible("WingLOut_D1", hiermesh.isChunkVisible("WingLOut_D1"));
        this.mesh.chunkVisible("WingLOut_D2", hiermesh.isChunkVisible("WingLOut_D2"));
        this.mesh.chunkVisible("WingROut_D0", hiermesh.isChunkVisible("WingROut_D0"));
        this.mesh.chunkVisible("WingROut_D1", hiermesh.isChunkVisible("WingROut_D1"));
        this.mesh.chunkVisible("WingROut_D2", hiermesh.isChunkVisible("WingROut_D2"));
        this.mesh.chunkVisible("WingLOut_CAP", hiermesh.isChunkVisible("WingLOut_CAP"));
        this.mesh.chunkVisible("WingROut_CAP", hiermesh.isChunkVisible("WingROut_CAP"));
        this.mesh.chunkVisible("AroneL_D0", hiermesh.isChunkVisible("AroneL_D0"));
        this.mesh.chunkVisible("AroneL_D1", hiermesh.isChunkVisible("AroneL_D1"));
        this.mesh.chunkVisible("AroneL_D2", hiermesh.isChunkVisible("AroneL_D2"));
        this.mesh.chunkVisible("AroneR_D0", hiermesh.isChunkVisible("AroneR_D0"));
        this.mesh.chunkVisible("AroneR_D1", hiermesh.isChunkVisible("AroneR_D1"));
        this.mesh.chunkVisible("AroneR_D2", hiermesh.isChunkVisible("AroneR_D2"));
        this.mesh.chunkVisible("AroneL1_D0", hiermesh.isChunkVisible("AroneL1_D0"));
        this.mesh.chunkVisible("AroneR1_D0", hiermesh.isChunkVisible("AroneR1_D0"));
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 6F, 12F, 18F, 39F, 63F, 90F, 118F, 145F, 182F, 217F, 245F, 273F, 304F, 335F };
    private static final float rpmScale[]         = { 0.0F, 12F, 25F, 50F, 75F, 100F, 125F, 150F, 175F, 200F, 225F, 250F, 275F, 300F };
    private static final float fuelScale[]        = { 0.0F, 60F, 120F, 180F, 210F, 240F, 270F, 300F };
    private static final float oilTempScale[]     = { 0.0F, 20F, 40F, 80F, 120F, 160F, 240F, 320F };

}
