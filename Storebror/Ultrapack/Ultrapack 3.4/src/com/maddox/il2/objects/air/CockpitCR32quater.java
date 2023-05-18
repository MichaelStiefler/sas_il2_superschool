package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;

public class CockpitCR32quater extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitCR32quater.this.bNeedSetUp) {
                CockpitCR32quater.this.reflectPlaneMats();
                CockpitCR32quater.this.bNeedSetUp = false;
            }
            CR_32quater CR_32quater = (CR_32quater) CockpitCR32quater.this.aircraft();
            if (CR_32quater.bChangedPit) {
                CockpitCR32quater.this.reflectPlaneToModel();
                CR_32quater.bChangedPit = false;
            }
            CockpitCR32quater.this.setTmp = CockpitCR32quater.this.setOld;
            CockpitCR32quater.this.setOld = CockpitCR32quater.this.setNew;
            CockpitCR32quater.this.setNew = CockpitCR32quater.this.setTmp;
            CockpitCR32quater.this.setNew.altimeter = CockpitCR32quater.this.fm.getAltitude();
            if (Math.abs(CockpitCR32quater.this.fm.Or.getKren()) < 30F) {
                CockpitCR32quater.this.setNew.azimuth = ((35F * CockpitCR32quater.this.setOld.azimuth) + CockpitCR32quater.this.fm.Or.azimut()) / 36F;
            }
            if ((CockpitCR32quater.this.setOld.azimuth > 270F) && (CockpitCR32quater.this.setNew.azimuth < 90F)) {
                CockpitCR32quater.this.setOld.azimuth -= 360F;
            }
            if ((CockpitCR32quater.this.setOld.azimuth < 90F) && (CockpitCR32quater.this.setNew.azimuth > 270F)) {
                CockpitCR32quater.this.setOld.azimuth += 360F;
            }
            CockpitCR32quater.this.setNew.mix = ((10F * CockpitCR32quater.this.setOld.mix) + CockpitCR32quater.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitCR32quater.this.setNew.throttle = ((10F * CockpitCR32quater.this.setOld.throttle) + CockpitCR32quater.this.fm.CT.PowerControl) / 11F;
            CockpitCR32quater.this.w.set(CockpitCR32quater.this.fm.getW());
            CockpitCR32quater.this.fm.Or.transform(CockpitCR32quater.this.w);
            CockpitCR32quater.this.setNew.turn = ((33F * CockpitCR32quater.this.setOld.turn) + CockpitCR32quater.this.w.z) / 34F;
            CockpitCR32quater.this.setNew.power = (0.85F * CockpitCR32quater.this.setOld.power) + (CockpitCR32quater.this.fm.EI.engines[0].getPowerOutput() * 0.15F);
            CockpitCR32quater.this.setNew.fuelpressure = (0.9F * CockpitCR32quater.this.setOld.fuelpressure) + (((CockpitCR32quater.this.fm.M.fuel > 1.0F) && (CockpitCR32quater.this.fm.EI.engines[0].getStage() == 6) ? 0.026F * (10F + (float) Math.sqrt(CockpitCR32quater.this.setNew.power)) : 0.0F) * 0.1F);
            return true;
        }

        Interpolater() {
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

        private Variables() {
        }

    }

    public CockpitCR32quater() {
        super("3DO/Cockpit/CR32/hier.him", "u2");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bEntered = false;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 500F, 0.0F, 25F), CockpitCR32quater.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.0F, 5.0F, 0.0F, 320F), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F + this.interp(-this.setNew.azimuth, -this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, this.cvt(this.setNew.fuelpressure, 0.0F, 1.0F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 268F), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 7F), CockpitCR32quater.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("zWaterOut", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 7F), CockpitCR32quater.waterTempScale), 0.0F);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3200F, 0.0F, 16F), CockpitCR32quater.rpmScale), 0.0F);
        this.mesh.chunkSetAngles("zSlide", 0.0F, this.cvt(this.getBall(3D), -2F, 2F, -4F, 4F), 0.0F);
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
        if (!this.isFocused()) {
            return;
        }
        if (this.isToggleAim() == flag) {
            return;
        }
        if (flag) {
            this.enter();
        } else {
            this.leave();
        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        this.mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        this.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        this.mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        this.mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
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
    private static final float speedometerScale[] = { 0.0F, 1.0F, 3F, 6.2F, 12F, 26.5F, 39F, 51F, 67.5F, 85.5F, 108F, 131.5F, 154F, 180F, 205.7F, 228.2F, 251F, 272.9F, 291.9F, 314.5F, 336.5F, 354F, 360F, 363F, 364F, 365F };
    private static final float rpmScale[]         = { 0.0F, 12F, 26F, 48F, 70F, 93F, 115F, 137F, 158F, 180F, 200F, 223F, 245F, 268F, 290F, 312F, 335F };
    private static final float oilTempScale[]     = { 0.0F, 40F, 84F, 128F, 170F, 215F, 260F, 303F };
    private static final float waterTempScale[]   = { 0.0F, 40F, 84F, 128F, 170F, 215F, 260F, 303F };
    private float              saveFov;
    private boolean            bEntered;
}
