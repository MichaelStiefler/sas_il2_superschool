package com.maddox.il2.objects.air;

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

public class CockpitPotez_25 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitPotez_25.this.setTmp = CockpitPotez_25.this.setOld;
            CockpitPotez_25.this.setOld = CockpitPotez_25.this.setNew;
            CockpitPotez_25.this.setNew = CockpitPotez_25.this.setTmp;
            CockpitPotez_25.this.setNew.altimeter = CockpitPotez_25.this.fm.getAltitude();
            if (Math.abs(CockpitPotez_25.this.fm.Or.getKren()) < 30F) {
                CockpitPotez_25.this.setNew.azimuth = ((35F * CockpitPotez_25.this.setOld.azimuth) + CockpitPotez_25.this.fm.Or.azimut()) / 36F;
            }
            if ((CockpitPotez_25.this.setOld.azimuth > 270F) && (CockpitPotez_25.this.setNew.azimuth < 90F)) {
                CockpitPotez_25.this.setOld.azimuth -= 360F;
            }
            if ((CockpitPotez_25.this.setOld.azimuth < 90F) && (CockpitPotez_25.this.setNew.azimuth > 270F)) {
                CockpitPotez_25.this.setOld.azimuth += 360F;
            }
            CockpitPotez_25.this.setNew.mix = ((10F * CockpitPotez_25.this.setOld.mix) + CockpitPotez_25.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitPotez_25.this.setNew.throttle = ((10F * CockpitPotez_25.this.setOld.throttle) + CockpitPotez_25.this.fm.CT.PowerControl) / 11F;
            CockpitPotez_25.this.w.set(CockpitPotez_25.this.fm.getW());
            CockpitPotez_25.this.fm.Or.transform(CockpitPotez_25.this.w);
            CockpitPotez_25.this.setNew.turn = ((33F * CockpitPotez_25.this.setOld.turn) + CockpitPotez_25.this.w.z) / 34F;
            CockpitPotez_25.this.setNew.vspeed = ((199F * CockpitPotez_25.this.setOld.vspeed) + CockpitPotez_25.this.fm.getVertSpeed()) / 200F;
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
        float vspeed;

        private Variables() {
        }

    }

    public CockpitPotez_25() {
        super("3DO/Cockpit/Potez_25/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.interpPut(new Interpolater(), null, Time.current(), null);
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
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 1000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 500F, 0.0F, 25F), CockpitPotez_25.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.72421F, 1.27579F, -160F, 160F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, 90F + this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, 90F + this.interp(-this.setNew.azimuth, -this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 16F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("zFuelPrs", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 8F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zFuelQty", 0.0F, this.cvt(this.fm.M.fuel * 1.25F, 50F, 310F, 0.0F, 275F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zOilIn", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 20F, 140F, 29F, 330F), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 20F, 140F, 29F, 330F), 0.0F);
        this.mesh.chunkSetAngles("zMagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -20F, 20F, 0.0485F, -0.0485F);
        this.mesh.chunkSetLocate("zPitch", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 317F), 0.0F);
        this.mesh.chunkSetAngles("Rudder", 26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.6F, 0.6F, 1.8F, -1.8F), 0.0F);
        this.mesh.chunkSetAngles("zSlide1", 0.0F, this.cvt(this.getBall(5D), -5F, 5F, -8F, 8F), 0.0F);
        this.mesh.chunkSetAngles("zSlide2", 0.0F, this.cvt(this.getBall(3D), -3F, 3F, -6F, 6F), 0.0F);
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, -33F + (30F * this.interp(this.setNew.mix, this.setOld.mix, f)));
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -55F + (50F * this.interp(this.setNew.throttle, this.setOld.throttle, f)));
        this.mesh.chunkSetAngles("z_AH", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("zVspeed", this.cvt(this.setNew.vspeed, -18F, 18F, -180F, 180F), 0.0F, 0.0F);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("Engine1_D0", hiermesh.isChunkVisible("Engine1_D0"));
        this.mesh.chunkVisible("Engine1_D1", hiermesh.isChunkVisible("Engine1_D1"));
        this.mesh.chunkVisible("Engine1_D2", hiermesh.isChunkVisible("Engine1_D2"));
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_D3", hiermesh.isChunkVisible("WingLIn_D3"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_D3", hiermesh.isChunkVisible("WingRIn_D3"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.5F, 0.5F);
            this.light2.light.setEmit(0.5F, 0.5F);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
        }
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private Vector3f           w;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 1.0F, 3F, 6.2F, 12F, 26.5F, 39F, 51F, 67.5F, 85.5F, 108F, 131.5F, 154F, 180F, 205.7F, 228.2F, 251F, 272.9F, 291.9F, 314.5F, 336.5F, 354F, 360F, 363F, 364F, 365F };
    private float              saveFov;
    private boolean            bEntered;

}
