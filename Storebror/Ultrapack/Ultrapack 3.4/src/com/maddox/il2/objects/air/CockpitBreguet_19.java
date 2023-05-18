package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;

public class CockpitBreguet_19 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitBreguet_19.this.setTmp = CockpitBreguet_19.this.setOld;
            CockpitBreguet_19.this.setOld = CockpitBreguet_19.this.setNew;
            CockpitBreguet_19.this.setNew = CockpitBreguet_19.this.setTmp;
            CockpitBreguet_19.this.setNew.altimeter = CockpitBreguet_19.this.fm.getAltitude();
            if (Math.abs(CockpitBreguet_19.this.fm.Or.getKren()) < 30F) {
                CockpitBreguet_19.this.setNew.azimuth = ((35F * CockpitBreguet_19.this.setOld.azimuth) + CockpitBreguet_19.this.fm.Or.azimut()) / 36F;
            }
            if ((CockpitBreguet_19.this.setOld.azimuth > 270F) && (CockpitBreguet_19.this.setNew.azimuth < 90F)) {
                CockpitBreguet_19.this.setOld.azimuth -= 360F;
            }
            if ((CockpitBreguet_19.this.setOld.azimuth < 90F) && (CockpitBreguet_19.this.setNew.azimuth > 270F)) {
                CockpitBreguet_19.this.setOld.azimuth += 360F;
            }
            CockpitBreguet_19.this.setNew.throttle = ((10F * CockpitBreguet_19.this.setOld.throttle) + CockpitBreguet_19.this.fm.CT.PowerControl) / 11F;
            CockpitBreguet_19.this.w.set(CockpitBreguet_19.this.fm.getW());
            CockpitBreguet_19.this.fm.Or.transform(CockpitBreguet_19.this.w);
            CockpitBreguet_19.this.setNew.turn = ((33F * CockpitBreguet_19.this.setOld.turn) + CockpitBreguet_19.this.w.z) / 34F;
            CockpitBreguet_19.this.setNew.power = (0.85F * CockpitBreguet_19.this.setOld.power) + (CockpitBreguet_19.this.fm.EI.engines[0].getPowerOutput() * 0.15F);
            CockpitBreguet_19.this.setNew.fuelpressure = (0.9F * CockpitBreguet_19.this.setOld.fuelpressure) + (((CockpitBreguet_19.this.fm.M.fuel > 1.0F) && (CockpitBreguet_19.this.fm.EI.engines[0].getStage() == 6) ? 0.026F * (10F + (float) Math.sqrt(CockpitBreguet_19.this.setNew.power)) : 0.0F) * 0.1F);
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

    public CockpitBreguet_19(String hier, String name) {
        super(hier, name);
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9000F, 0.0F, 338.5F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 280F, 0.0F, 14F), CockpitBreguet_19.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.72421F, 1.27579F, -160F, 160F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F + this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 24F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 24F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Column_Cam", 0.0F, 24F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("Column_Rod", 0.0F, -24F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, this.cvt(this.setNew.fuelpressure, 0.0F, 0.5F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zFuelQty", 0.0F, this.cvt(this.fm.M.fuel * 1.25F, 50F, 310F, 0.0F, 275F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zOilIn", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 140F, 0.0F, 7F), CockpitBreguet_19.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), CockpitBreguet_19.oilTempScale), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -20F, 20F, 0.0385F, -0.0385F);
        this.mesh.chunkSetLocate("zPitch", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 15F), CockpitBreguet_19.rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Rudder", 26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableL", -26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableR", -26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.6F, 0.6F, 1.8F, -1.8F), 0.0F);
        this.mesh.chunkSetAngles("zSlide", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -10F, 10F), 0.0F);
        this.mesh.chunkSetAngles("Boost", 0.0F, 0.0F, -90F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -90F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
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

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 31");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
        this.mesh.chunkVisible("SuperReticle", true);
        this.mesh.chunkVisible("Sight", false);
    }

    private void leave() {
        if (!this.bEntered) {
            return;
        } else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            this.mesh.chunkVisible("Sight", true);
            this.mesh.chunkVisible("SuperReticle", false);
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
            this.enter();
        } else {
            this.leave();
        }
    }

    public void reflectCockpitState() {
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 10F, 20F, 30F, 50F, 70F, 90F, 120F, 150F, 180F, 210F, 240F, 270F, 300F, 330F };
    private static final float rpmScale[]         = { 0.0F, 10F, 21F, 36F, 51F, 66F, 87F, 109F, 133F, 156F, 184F, 211F, 241F, 270F, 300F, 330F };
    private static final float oilTempScale[]     = { 0.0F, 18F, 36F, 63F, 115F, 180F, 245F, 351F };
    private float              saveFov;
    private boolean            bEntered;

}
