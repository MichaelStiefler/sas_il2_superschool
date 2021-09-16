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

public class CockpitP_7 extends CockpitPilot {
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
            if (CockpitP_7.this.bNeedSetUp) {
                CockpitP_7.this.reflectPlaneMats();
                CockpitP_7.this.bNeedSetUp = false;
            }
            if (P_7.bChangedPit) {
                CockpitP_7.this.reflectPlaneToModel();
                P_7.bChangedPit = false;
            }
            CockpitP_7.this.setTmp = CockpitP_7.this.setOld;
            CockpitP_7.this.setOld = CockpitP_7.this.setNew;
            CockpitP_7.this.setNew = CockpitP_7.this.setTmp;
            CockpitP_7.this.setNew.altimeter = CockpitP_7.this.fm.getAltitude();
            if (Math.abs(CockpitP_7.this.fm.Or.getKren()) < 30F) {
                CockpitP_7.this.setNew.azimuth = ((35F * CockpitP_7.this.setOld.azimuth) + CockpitP_7.this.fm.Or.azimut()) / 36F;
            }
            if ((CockpitP_7.this.setOld.azimuth > 270F) && (CockpitP_7.this.setNew.azimuth < 90F)) {
                CockpitP_7.this.setOld.azimuth -= 360F;
            }
            if ((CockpitP_7.this.setOld.azimuth < 90F) && (CockpitP_7.this.setNew.azimuth > 270F)) {
                CockpitP_7.this.setOld.azimuth += 360F;
            }
            CockpitP_7.this.setNew.mix = ((10F * CockpitP_7.this.setOld.mix) + CockpitP_7.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitP_7.this.setNew.throttle = ((10F * CockpitP_7.this.setOld.throttle) + CockpitP_7.this.fm.CT.PowerControl) / 11F;
            CockpitP_7.this.w.set(CockpitP_7.this.fm.getW());
            CockpitP_7.this.fm.Or.transform(CockpitP_7.this.w);
            CockpitP_7.this.setNew.turn = ((33F * CockpitP_7.this.setOld.turn) + CockpitP_7.this.w.z) / 34F;
            CockpitP_7.this.setNew.power = (0.85F * CockpitP_7.this.setOld.power) + (CockpitP_7.this.fm.EI.engines[0].getPowerOutput() * 0.15F);
            CockpitP_7.this.setNew.fuelpressure = (0.9F * CockpitP_7.this.setOld.fuelpressure) + (((CockpitP_7.this.fm.M.fuel <= 1.0F) || (CockpitP_7.this.fm.EI.engines[0].getStage() != 6) ? 0.0F : 0.026F * (10F + (float) Math.sqrt(CockpitP_7.this.setNew.power))) * 0.1F);
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitP_7() {
        super("3DO/Cockpit/P-7/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.interpPut(new Interpolater(), null, Time.current(), null);
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(1.0F, 1.0F, 1.0F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.cockpitNightMats = (new String[] { "compass", "dials1", "dials2", "dials3", "gauges" });
        this.setNightMats(false);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 8000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 420F, 0.0F, 21F), CockpitP_7.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.72421F, 1.27579F, -160F, 160F), 0.0F);
        this.mesh.chunkSetAngles("zSecond", 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F - this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, this.cvt(this.setNew.fuelpressure, 0.0F, 0.6F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zFuelQty", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 300F, 0.0F, 15F), CockpitP_7.fuelScale), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 268F), 0.0F);
        this.mesh.chunkSetAngles("zOilIn", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 140F, 0.0F, 7F), CockpitP_7.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), CockpitP_7.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 15F), CockpitP_7.rpmScale), 0.0F);
        this.mesh.chunkSetAngles("zSlide", 0.0F, this.cvt(this.getBall(3D), -3F, 3F, -5F, 5F), 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.9F, 0.9F, -4.5F, 4.5F), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 20F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 20F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("StickRod", 0.0F, 0.0F, this.pictElev * -20F);
        this.mesh.chunkSetAngles("StickCardan1", 0.0F, 20F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("StickCardan2", 20F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Rudder", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RudderCableL", -20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RudderCableR", -20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -35F + (70F * this.interp(this.setNew.throttle, this.setOld.throttle, f)));
        this.mesh.chunkSetAngles("ThrottleRod", 0.0F, 0.0F, 35F + (-70F * this.interp(this.setNew.throttle, this.setOld.throttle, f)));
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, -35F + (70F * this.interp(this.setNew.mix, this.setOld.mix, f)));
        this.mesh.chunkSetAngles("MixtureRod", 0.0F, 0.0F, 35F + (-70F * this.interp(this.setNew.mix, this.setOld.mix, f)));
        this.mesh.chunkSetAngles("MagnetoSw", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 45F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -20F, -15F, 0.0F, 0.0172F);
        this.mesh.chunkSetLocate("zPitch1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -15F, -10F, 0.0F, 0.0172F);
        this.mesh.chunkSetLocate("zPitch2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -10F, -5F, 0.0F, 0.0172F);
        this.mesh.chunkSetLocate("zPitch3", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -5F, 0.0F, 0.0F, 0.0172F);
        this.mesh.chunkSetLocate("zPitch4", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), 0.0F, 5F, 0.0F, 0.0172F);
        this.mesh.chunkSetLocate("zPitch5", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), 5F, 10F, 0.0F, 0.0172F);
        this.mesh.chunkSetLocate("zPitch6", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), 10F, 15F, 0.0F, 0.0172F);
        this.mesh.chunkSetLocate("zPitch7", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), 15F, 20F, 0.0F, 0.0172F);
        this.mesh.chunkSetLocate("zPitch8", Cockpit.xyz, Cockpit.ypr);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Sight", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (Actor.isAlive(this.aircraft())) {
            this.aircraft().hierMesh().chunkVisible("Sight", true);
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
        this.mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
        this.mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
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
    private static final float speedometerScale[] = { 0.0F, 3F, 6F, 9F, 13F, 28F, 41.5F, 54F, 71.5F, 90F, 112.5F, 135F, 156F, 180F, 203.5F, 224.5F, 247F, 270F, 289.5F, 313.5F, 336F, 358.5F };
    private static final float rpmScale[]         = { 0.0F, 7.5F, 15F, 30F, 45F, 75F, 105F, 135F, 165F, 195F, 225F, 255F, 285F, 315F, 345F, 360F };
    private static final float fuelScale[]        = { 0.0F, 30F, 55F, 83F, 106F, 141F, 155F, 168F, 183F, 197F, 211F, 226F, 242F, 257F, 272F, 288F };
    private static final float oilTempScale[]     = { 0.0F, 20F, 40F, 80F, 120F, 160F, 240F, 320F };

}
