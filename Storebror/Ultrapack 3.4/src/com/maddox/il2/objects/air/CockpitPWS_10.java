package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;

public class CockpitPWS_10 extends CockpitPilot {
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
            if (CockpitPWS_10.this.bNeedSetUp) {
                CockpitPWS_10.this.reflectPlaneMats();
                CockpitPWS_10.this.reflectPlaneToModel();
                CockpitPWS_10.this.bNeedSetUp = false;
            }
            if (PWS_10.bChangedPit) {
                CockpitPWS_10.this.reflectPlaneToModel();
                PWS_10.bChangedPit = false;
            }
            CockpitPWS_10.this.setTmp = CockpitPWS_10.this.setOld;
            CockpitPWS_10.this.setOld = CockpitPWS_10.this.setNew;
            CockpitPWS_10.this.setNew = CockpitPWS_10.this.setTmp;
            CockpitPWS_10.this.setNew.altimeter = CockpitPWS_10.this.fm.getAltitude();
            if (Math.abs(CockpitPWS_10.this.fm.Or.getKren()) < 30F) {
                CockpitPWS_10.this.setNew.azimuth = ((35F * CockpitPWS_10.this.setOld.azimuth) + CockpitPWS_10.this.fm.Or.azimut()) / 36F;
            }
            if ((CockpitPWS_10.this.setOld.azimuth > 270F) && (CockpitPWS_10.this.setNew.azimuth < 90F)) {
                CockpitPWS_10.this.setOld.azimuth -= 360F;
            }
            if ((CockpitPWS_10.this.setOld.azimuth < 90F) && (CockpitPWS_10.this.setNew.azimuth > 270F)) {
                CockpitPWS_10.this.setOld.azimuth += 360F;
            }
            CockpitPWS_10.this.setNew.mix = ((10F * CockpitPWS_10.this.setOld.mix) + CockpitPWS_10.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitPWS_10.this.setNew.throttle = ((10F * CockpitPWS_10.this.setOld.throttle) + CockpitPWS_10.this.fm.CT.PowerControl) / 11F;
            CockpitPWS_10.this.w.set(CockpitPWS_10.this.fm.getW());
            CockpitPWS_10.this.fm.Or.transform(CockpitPWS_10.this.w);
            CockpitPWS_10.this.setNew.turn = ((33F * CockpitPWS_10.this.setOld.turn) + CockpitPWS_10.this.w.z) / 34F;
            CockpitPWS_10.this.setNew.power = (0.85F * CockpitPWS_10.this.setOld.power) + (CockpitPWS_10.this.fm.EI.engines[0].getPowerOutput() * 0.15F);
            CockpitPWS_10.this.setNew.fuelpressure = (0.9F * CockpitPWS_10.this.setOld.fuelpressure) + (((CockpitPWS_10.this.fm.M.fuel <= 1.0F) || (CockpitPWS_10.this.fm.EI.engines[0].getStage() != 6) ? 0.0F : 0.026F * (10F + (float) Math.sqrt(CockpitPWS_10.this.setNew.power))) * 0.1F);
            float f = CockpitPWS_10.this.fm.CT.getAileron();
            CockpitPWS_10.this.mesh.chunkSetAngles("AroneL_D0", 0.0F, -20F * f, 0.0F);
            CockpitPWS_10.this.mesh.chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.25D, 0.0D, 0.0D);
            hookpilot.setTubeSight(point3d);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            this.leave();
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter() {
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        this.enteringAim = true;
    }

    private void enter() {
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setSimpleUse(true);
        this.doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
    }

    public void doSetSimpleUse(boolean flag) {
        super.doSetSimpleUse(flag);
        if (flag) {
            this.saveFov = Main3D.FOVX;
            CmdEnv.top().exec("fov 31");
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            this.bEntered = true;
            this.mesh.chunkVisible("SuperReticle", true);
            this.mesh.chunkVisible("Sight", false);
        }
    }

    private void leave() {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!this.bEntered) {
            return;
        } else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            this.doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            this.mesh.chunkVisible("SuperReticle", false);
            this.mesh.chunkVisible("Sight", true);
            return;
        }
    }

    public void destroy() {
        this.leave();
        super.destroy();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused() || (this.isToggleAim() == flag)) {
            return;
        }
        if (flag) {
            this.prepareToEnter();
        } else {
            this.leave();
        }
    }

    public CockpitPWS_10() {
        super("3DO/Cockpit/PWS-10/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.enteringAim = false;
        this.bEntered = false;
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
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isAimReached()) {
                this.enteringAim = false;
                this.enter();
            } else if (!hookpilot.isAim()) {
                this.enteringAim = false;
            }
        }
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 500F, 0.0F, 25F), CockpitPWS_10.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.0F, 0.8F, 0.5F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F + this.interp(-this.setNew.azimuth, -this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, this.cvt(this.setNew.fuelpressure, 0.0F, 0.6F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 268F), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), CockpitPWS_10.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zWaterOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 7F), CockpitPWS_10.waterTempScale), 0.0F);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2600F, 0.0F, 13F), CockpitPWS_10.rpmScale), 0.0F);
        this.mesh.chunkSetAngles("zSlide", 0.0F, this.cvt(this.getBall(3D), -2F, 2.0F, -4F, 4F), 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.7F, 0.7F, -1.5F, 1.5F), 0.0F);
        this.mesh.chunkSetAngles("StickBase", 0.0F, 12F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 0.0F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("StickElevRod", 0.0F, this.pictElev * 12F, 0.0F);
        this.mesh.chunkSetAngles("Rudder", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableL", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableR", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, 0.0F + (40F * this.interp(this.setNew.throttle, this.setOld.throttle, f)));
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, 0.0F + (38F * this.interp(this.setNew.mix, this.setOld.mix, f)));
        this.mesh.chunkSetAngles("zMagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("Engine1_D0", hiermesh.isChunkVisible("Engine1_D0"));
        this.mesh.chunkVisible("Engine1_D1", hiermesh.isChunkVisible("Engine1_D1"));
        this.mesh.chunkVisible("Engine1_D2", hiermesh.isChunkVisible("Engine1_D2"));
        this.mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
        this.mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
        this.mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
        this.mesh.chunkVisible("Tail1_CAP", hiermesh.isChunkVisible("Tail1_CAP"));
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
        this.mesh.chunkVisible("AroneL_D0", hiermesh.isChunkVisible("AroneL_D0"));
        this.mesh.chunkVisible("AroneL_D1", hiermesh.isChunkVisible("AroneL_D1"));
        this.mesh.chunkVisible("AroneL_D2", hiermesh.isChunkVisible("AroneL_D2"));
        this.mesh.chunkVisible("AroneR_D0", hiermesh.isChunkVisible("AroneR_D0"));
        this.mesh.chunkVisible("AroneR_D1", hiermesh.isChunkVisible("AroneR_D1"));
        this.mesh.chunkVisible("AroneR_D2", hiermesh.isChunkVisible("AroneR_D2"));
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

    public void reflectCockpitState() {
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
    private static final float speedometerScale[] = { 0.0F, 1.0F, 3F, 6.2F, 12F, 26.5F, 39F, 51F, 67.5F, 85.5F, 108F, 131.5F, 154F, 180F, 205.7F, 228.2F, 251F, 272.9F, 291.9F, 314.5F, 336.5F, 354F, 360F, 363F, 364F, 365F };
    private static final float rpmScale[]         = { 0.0F, 12F, 25F, 50F, 75F, 100F, 125F, 150F, 175F, 200F, 225F, 250F, 275F, 300F };
    private static final float oilTempScale[]     = { 0.0F, 20F, 40F, 80F, 120F, 160F, 240F, 320F };
    private static final float waterTempScale[]   = { 0.0F, 20F, 40F, 80F, 120F, 160F, 240F, 320F };
    private float              saveFov;
    private boolean            bEntered;
    private boolean            enteringAim;

}
