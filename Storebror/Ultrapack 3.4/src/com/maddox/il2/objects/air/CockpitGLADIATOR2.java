package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitGLADIATOR2 extends CockpitPilot {
    private class Variables {

        float altimeter;
        float azimuth;
        float throttle;
        float mix;
        float prop;
        float turn;
        float vspeed;
        float stbyPosition;

        private Variables() {
        }
    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitGLADIATOR2.this.bNeedSetUp) {
                CockpitGLADIATOR2.this.reflectPlaneMats();
                CockpitGLADIATOR2.this.bNeedSetUp = false;
            }
            if (((GLADIATOR2) CockpitGLADIATOR2.this.aircraft()).bChangedPit) {
                CockpitGLADIATOR2.this.reflectPlaneToModel();
                ((GLADIATOR2) CockpitGLADIATOR2.this.aircraft()).bChangedPit = false;
            }
            CockpitGLADIATOR2.this.setTmp = CockpitGLADIATOR2.this.setOld;
            CockpitGLADIATOR2.this.setOld = CockpitGLADIATOR2.this.setNew;
            CockpitGLADIATOR2.this.setNew = CockpitGLADIATOR2.this.setTmp;
            if ((CockpitGLADIATOR2.this.fm.AS.astateCockpitState & 2) != 0 && CockpitGLADIATOR2.this.setNew.stbyPosition < 1.0F) {
                CockpitGLADIATOR2.this.setNew.stbyPosition = CockpitGLADIATOR2.this.setOld.stbyPosition + 0.0125F;
                CockpitGLADIATOR2.this.setOld.stbyPosition = CockpitGLADIATOR2.this.setNew.stbyPosition;
            }
            CockpitGLADIATOR2.this.setNew.altimeter = CockpitGLADIATOR2.this.fm.getAltitude();
            if (Math.abs(CockpitGLADIATOR2.this.fm.Or.getKren()) < 30F) CockpitGLADIATOR2.this.setNew.azimuth = (21F * CockpitGLADIATOR2.this.setOld.azimuth + CockpitGLADIATOR2.this.fm.Or.azimut()) / 22F;
            if (CockpitGLADIATOR2.this.setOld.azimuth > 270F && CockpitGLADIATOR2.this.setNew.azimuth < 90F) CockpitGLADIATOR2.this.setOld.azimuth -= 360F;
            if (CockpitGLADIATOR2.this.setOld.azimuth < 90F && CockpitGLADIATOR2.this.setNew.azimuth > 270F) CockpitGLADIATOR2.this.setOld.azimuth += 360F;
            CockpitGLADIATOR2.this.setNew.throttle = (10F * CockpitGLADIATOR2.this.setOld.throttle + CockpitGLADIATOR2.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitGLADIATOR2.this.setNew.mix = (10F * CockpitGLADIATOR2.this.setOld.mix + CockpitGLADIATOR2.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitGLADIATOR2.this.setNew.prop = CockpitGLADIATOR2.this.setOld.prop;
            if (CockpitGLADIATOR2.this.setNew.prop < CockpitGLADIATOR2.this.fm.EI.engines[0].getControlProp() - 0.01F) CockpitGLADIATOR2.this.setNew.prop += 0.0025F;
            if (CockpitGLADIATOR2.this.setNew.prop > CockpitGLADIATOR2.this.fm.EI.engines[0].getControlProp() + 0.01F) CockpitGLADIATOR2.this.setNew.prop -= 0.0025F;
            CockpitGLADIATOR2.this.w.set(CockpitGLADIATOR2.this.fm.getW());
            CockpitGLADIATOR2.this.fm.Or.transform(CockpitGLADIATOR2.this.w);
            CockpitGLADIATOR2.this.setNew.turn = (12F * CockpitGLADIATOR2.this.setOld.turn + CockpitGLADIATOR2.this.w.z) / 13F;
            CockpitGLADIATOR2.this.setNew.vspeed = (199F * CockpitGLADIATOR2.this.setOld.vspeed + CockpitGLADIATOR2.this.fm.getVertSpeed()) / 200F;
            CockpitGLADIATOR2.this.pictSupc = 0.8F * CockpitGLADIATOR2.this.pictSupc + 0.2F * CockpitGLADIATOR2.this.fm.EI.engines[0].getControlCompressor();
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitGLADIATOR2() {
        super("3DO/Cockpit/Gladiator/hier.him", "u2");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictSupc = 0.0F;
        this.cockpitNightMats = new String[] { "PRIB_01", "PRIB_02", "PRIB_03", "PRIB_04", "PRIB_05", "PRIB_06", "PRIB_07" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (((GLADIATOR2) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((GLADIATOR2) this.aircraft()).bChangedPit = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.65F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("FONAR_GLASS2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_ManPrs1", 0.0F, 0.0F, 4F - this.cvt(this.pictManifold = 0.85F * this.pictManifold + 0.15F * this.fm.EI.engines[0].getManifoldPressure(), 0.600034F, 1.66661F, -124.8F, 208F));
        this.mesh.chunkSetAngles("Flap01_D0", 0.0F, -50F * this.fm.CT.getFlap(), 0.0F);
        this.mesh.chunkSetAngles("Flap04_D0", 0.0F, -50F * this.fm.CT.getFlap(), 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 14F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 14F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("Z_PedalStrut", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", 0.0F, 0.0F, -51.8F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("Z_Mix", 0.0F, 0.0F, -52.3F * this.interp(this.setNew.mix, this.setOld.mix, f));
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 180.0555F, 0.0F, 35F), speedometerScale));
        this.mesh.chunkSetAngles("Z_Compass1", this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 1200F, 3400F, 0.0F, -328F));
        this.mesh.chunkSetAngles("Z_OilPress1", 0.0F, 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 12F, 0.0F, -309F));
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F));
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F));
        this.mesh.chunkSetAngles("Z_Second1", 0.0F, 0.0F, this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, -360F));
        this.mesh.chunkSetAngles("Z_FuelPress1", 0.0F, 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 282F, 0.0F, 71F));
        this.mesh.chunkSetAngles("Z_OilTemp1", 0.0F, 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, -315.5F));
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.getAOA(), -20F, 20F, 0.042F, -0.042F);
        this.mesh.chunkSetLocate("Z_AoA1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Turn1", 0.0F, 0.0F, this.cvt(this.setNew.turn, -0.2F, 0.2F, -22.5F, 22.5F));
        this.mesh.chunkSetAngles("Turn2", 0.0F, 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 16.9F, -16.9F));
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, 0.0F, this.cvt(this.setNew.vspeed, -15F, 15F, 180F, -180F));
        this.mesh.chunkSetAngles("Z_Oxypres1", 0.0F, 0.0F, -260F);
        this.mesh.chunkSetAngles("Z_Oxypres2", 0.0F, 0.0F, -320F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Revi_D0", false);
            this.mesh.chunkVisible("Revi_D1", true);
        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        this.mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        this.mesh.chunkVisible("WingLMid_D3", hiermesh.isChunkVisible("WingLMid_D3"));
        this.mesh.chunkVisible("WingLMid_CAP", hiermesh.isChunkVisible("WingLMid_CAP"));
        this.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        this.mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        this.mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        this.mesh.chunkVisible("WingRMid_D3", hiermesh.isChunkVisible("WingRMid_D3"));
        this.mesh.chunkVisible("WingRMid_CAP", hiermesh.isChunkVisible("WingRMid_CAP"));
        this.mesh.chunkVisible("WingLOut_D0", hiermesh.isChunkVisible("WingLOut_D0"));
        this.mesh.chunkVisible("WingLOut_D1", hiermesh.isChunkVisible("WingLOut_D1"));
        this.mesh.chunkVisible("WingLOut_D2", hiermesh.isChunkVisible("WingLOut_D2"));
        this.mesh.chunkVisible("WingLOut_D3", hiermesh.isChunkVisible("WingLOut_D3"));
        this.mesh.chunkVisible("WingROut_D0", hiermesh.isChunkVisible("WingROut_D0"));
        this.mesh.chunkVisible("WingROut_D1", hiermesh.isChunkVisible("WingROut_D1"));
        this.mesh.chunkVisible("WingROut_D2", hiermesh.isChunkVisible("WingROut_D2"));
        this.mesh.chunkVisible("WingROut_D3", hiermesh.isChunkVisible("WingROut_D3"));
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("CF_D3", hiermesh.isChunkVisible("CF_D3"));
        this.mesh.chunkVisible("WireL_D0", hiermesh.isChunkVisible("WireL_D0"));
        this.mesh.chunkVisible("WireR_D0", hiermesh.isChunkVisible("WireR_D0"));
        this.mesh.chunkVisible("Flap01_D0", hiermesh.isChunkVisible("Flap01_D0"));
        this.mesh.chunkVisible("Flap04_D0", hiermesh.isChunkVisible("Flap04_D0"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        this.mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        this.mesh.materialReplace("Matt2D2o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictSupc;
    private float              pictManifold;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 1.0F, 3F, 7.5F, 34.5F, 46F, 63F, 76F, 94F, 112.5F, 131F, 150F, 168.5F, 187F, 203F, 222F, 242.5F, 258.5F, 277F, 297F, 315.5F, 340F, 360F, 376.5F, 392F, 407F, 423.5F, 442F, 459F, 476F,
            492.5F, 513F, 534.5F, 552F, 569.5F };
    private Vector3f           w;
}
