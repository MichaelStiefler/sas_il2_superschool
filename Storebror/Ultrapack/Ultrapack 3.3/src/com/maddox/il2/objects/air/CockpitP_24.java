package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitP_24 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_24.this.bNeedSetUp) {
                CockpitP_24.this.reflectPlaneMats();
                CockpitP_24.this.reflectCockpitMats();
                CockpitP_24.this.bNeedSetUp = false;
            }
            if ((P_24) CockpitP_24.this.aircraft() != null);
            if (P_24.bChangedPit) {
                CockpitP_24.this.reflectPlaneToModel();
                if ((P_24) CockpitP_24.this.aircraft() != null);
                P_24.bChangedPit = false;
            }
            CockpitP_24.this.setTmp = CockpitP_24.this.setOld;
            CockpitP_24.this.setOld = CockpitP_24.this.setNew;
            CockpitP_24.this.setNew = CockpitP_24.this.setTmp;
            CockpitP_24.this.setNew.altimeter = CockpitP_24.this.fm.getAltitude();
            if (Math.abs(CockpitP_24.this.fm.Or.getKren()) < 30F) CockpitP_24.this.setNew.azimuth = (35F * CockpitP_24.this.setOld.azimuth + CockpitP_24.this.fm.Or.azimut()) / 36F;
            if (CockpitP_24.this.setOld.azimuth > 270F && CockpitP_24.this.setNew.azimuth < 90F) CockpitP_24.this.setOld.azimuth -= 360F;
            if (CockpitP_24.this.setOld.azimuth < 90F && CockpitP_24.this.setNew.azimuth > 270F) CockpitP_24.this.setOld.azimuth += 360F;
            CockpitP_24.this.setNew.throttle = (10F * CockpitP_24.this.setOld.throttle + CockpitP_24.this.fm.CT.PowerControl) / 11F;
            CockpitP_24.this.w.set(CockpitP_24.this.fm.getW());
            CockpitP_24.this.fm.Or.transform(CockpitP_24.this.w);
            CockpitP_24.this.setNew.turn = (33F * CockpitP_24.this.setOld.turn + CockpitP_24.this.w.z) / 34F;
            CockpitP_24.this.setNew.power = 0.85F * CockpitP_24.this.setOld.power + CockpitP_24.this.fm.EI.engines[0].getPowerOutput() * 0.15F;
            CockpitP_24.this.setNew.fuelpressure = 0.9F * CockpitP_24.this.setOld.fuelpressure
                    + (CockpitP_24.this.fm.M.fuel > 1.0F && CockpitP_24.this.fm.EI.engines[0].getStage() == 6 ? 0.026F * (10F + (float) Math.sqrt(CockpitP_24.this.setNew.power)) : 0.0F) * 0.1F;
            if (CockpitP_24.this.fm.getAltitude() > 5000F) CockpitP_24.oxyf = 12F;
            else if (CockpitP_24.this.fm.getAltitude() > 3000F) CockpitP_24.oxyf = (CockpitP_24.this.fm.getAltitude() - 2600F) / 200F;
            if (CockpitP_24.this.fm.getAltitude() > 3000F) CockpitP_24.oxyp = CockpitP_24.oxyp <= 0.0F ? 0.0F : CockpitP_24.oxyp - 1E-006F * CockpitP_24.oxyf;
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float altimeter;
        float azimuth;
        float throttle;
        float turn;
        float power;
        float fuelpressure;

        private Variables() {
        }

    }

    public CockpitP_24() {
        super("3DO/Cockpit/P-24/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.light1 = new LightPointActor(new LightPoint(), new Point3d(-0.92D, -0.003D, 0.625D));
        this.light1.light.setColor(0.96F, 0.87F, 0.74F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void removeCanopy() {
        this.mesh.chunkVisible("Canopy", false);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectCockpitMats();
            this.bNeedSetUp = false;
        }
        float f1 = this.fm.CT.getCockpitDoor();
        this.resetYPRmodifier();
        if (f1 > 0.01D) if (f1 < 0.1F) {
            Cockpit.ypr[1] = this.cvt(f1, 0.01F, 0.08F, 0.0F, 2.0F);
            Cockpit.xyz[0] = 0F;
            Cockpit.xyz[2] = 0F;
        } else {
            Cockpit.ypr[1] = this.cvt(f1, 0.15F, 0.99F, 2.0F, 120F);
            Cockpit.xyz[0] = 0F;
            Cockpit.xyz[2] = 0F;
        }
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 600F, 0.0F, 30F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.72421F, 1.27579F, -160F, 160F), 0.0F);
        this.mesh.chunkSetAngles("zSecond", 0.0F, this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F - this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 24F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 24F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("Column_Cam", 0.0F, 24F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("Column_Rod", 0.0F, -24F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, this.cvt(this.setNew.fuelpressure, 0.0F, 0.5F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zFuelQty", 0.0F, this.cvt(this.fm.M.fuel * 1.25F, 50F, 310F, 0.0F, 275F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zOilIn", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 140F, 0.0F, 7F), oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), oilTempScale), 0.0F);
        this.resetYPRmodifier();
        xyz[0] = this.cvt(this.fm.Or.getTangage(), -20F, 20F, 0.0385F, -0.0385F);
        this.mesh.chunkSetLocate("zPitch", xyz, ypr);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 15F), rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Rudder", 26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableL", -26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableR", -26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.6F, 0.6F, 1.8F, -1.8F), 0.0F);
        this.mesh.chunkSetAngles("zSlide", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -10F, 10F), 0.0F);
        this.mesh.chunkSetAngles("Boost", 0.0F, 0.0F, -90F + 90F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -90F + 90F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("zU1", 0.0F, 0.0F, this.cvt(oxyp, 0.0F, 1.0F, 0.0F, 260F));
        this.mesh.chunkSetAngles("zU2", 0.0F, 0.0F, this.cvt(oxyf, 0.0F, 12F, 0.0F, 260F));
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Cart_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (Actor.isAlive(this.aircraft())) this.aircraft().hierMesh().chunkVisible("Cart_D0", true);
        super.doFocusLeave();
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
        this.mesh.chunkVisible("WingLOut_CAP", hiermesh.isChunkVisible("WingLOut_CAP"));
        this.mesh.chunkVisible("WingROut_CAP", hiermesh.isChunkVisible("WingROut_CAP"));
    }

    protected void reflectPlaneMats() {
        this.mesh.materialReplace("Gloss1D0o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss1D0o")));
        this.mesh.materialReplace("Gloss1D1o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss1D1o")));
        this.mesh.materialReplace("Gloss2D2o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss2D2o")));
    }

    private void reflectCockpitMats() {
        mat1_lit = this.mesh.material(this.mesh.materialFind("dials3_lit"));
        mat2_lit = this.mesh.material(this.mesh.materialFind("ldials_lit"));
        mat3_lit = this.mesh.material(this.mesh.materialFind("sdials_lit"));
        mat4_lit = this.mesh.material(this.mesh.materialFind("dials4_lit"));
        this.setLitMats(false);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setLitMats(true);
            this.light1.light.setEmit(0.3F, 0.05F);
        } else {
            this.setLitMats(false);
            this.light1.light.setEmit(0.0F, 0.0F);
        }
    }

    private void setLitMats(boolean flag) {
        shine = flag ? 0.5F : 0.1F;
        if (mat1_lit.isValidLayer(0)) {
            mat1_lit.setLayer(0);
            mat1_lit.set((byte) 24, shine);
            mat1_lit.set((short) 0, true);
        }
        if (mat2_lit.isValidLayer(0)) {
            mat2_lit.setLayer(0);
            mat2_lit.set((byte) 24, shine);
            mat2_lit.set((short) 0, true);
        }
        if (mat3_lit.isValidLayer(0)) {
            mat3_lit.setLayer(0);
            mat3_lit.set((byte) 24, shine);
            mat3_lit.set((short) 0, true);
        }
        if (mat4_lit.isValidLayer(0)) {
            mat4_lit.setLayer(0);
            mat4_lit.set((byte) 24, shine);
            mat4_lit.set((short) 0, true);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private LightPointActor    light1;
    private static float       shine;
    private static Mat         mat1_lit;
    private static Mat         mat2_lit;
    private static Mat         mat3_lit;
    private static Mat         mat4_lit;
    private static float       oxyf               = 0.0F;
    private static float       oxyp               = 1.0F;
    private static final float speedometerScale[] = { 0.0F, 3F, 6F, 9F, 12F, 16F, 27F, 38F, 46F, 53.5F, 63.5F, 73.5F, 85.5F, 96.5F, 109F, 120.5F, 134F, 147.5F, 163.5F, 179F, 193F, 208F, 222.5F, 237F, 251F, 264.5F, 278F, 292F, 304F, 317F, 329.5F };
    private static final float rpmScale[]         = { 0.0F, 10F, 21F, 36F, 51F, 66F, 87F, 109F, 133F, 156F, 184F, 211F, 241F, 270F, 300F, 330F };
    private static final float oilTempScale[]     = { 0.0F, 18F, 36F, 63F, 115F, 180F, 245F, 351F };
}
