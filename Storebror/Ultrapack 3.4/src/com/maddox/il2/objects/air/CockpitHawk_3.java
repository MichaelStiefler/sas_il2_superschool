package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHawk_3 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHawk_3.this.bNeedSetUp) {
                CockpitHawk_3.this.reflectPlaneMats();
                CockpitHawk_3.this.bNeedSetUp = false;
            }
            CockpitHawk_3.this.setTmp = CockpitHawk_3.this.setOld;
            CockpitHawk_3.this.setOld = CockpitHawk_3.this.setNew;
            CockpitHawk_3.this.setNew = CockpitHawk_3.this.setTmp;
            CockpitHawk_3.this.setNew.altimeter = CockpitHawk_3.this.fm.getAltitude();
            if (Math.abs(CockpitHawk_3.this.fm.Or.getKren()) < 30F) {
                CockpitHawk_3.this.setNew.azimuth = ((21F * CockpitHawk_3.this.setOld.azimuth) + CockpitHawk_3.this.fm.Or.azimut()) / 22F;
            }
            if ((CockpitHawk_3.this.setOld.azimuth > 270F) && (CockpitHawk_3.this.setNew.azimuth < 90F)) {
                CockpitHawk_3.this.setOld.azimuth -= 360F;
            }
            if ((CockpitHawk_3.this.setOld.azimuth < 90F) && (CockpitHawk_3.this.setNew.azimuth > 270F)) {
                CockpitHawk_3.this.setOld.azimuth += 360F;
            }
            CockpitHawk_3.this.setNew.throttle = ((10F * CockpitHawk_3.this.setOld.throttle) + CockpitHawk_3.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitHawk_3.this.setNew.mix = ((10F * CockpitHawk_3.this.setOld.mix) + CockpitHawk_3.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitHawk_3.this.setNew.prop = CockpitHawk_3.this.setOld.prop;
            if (CockpitHawk_3.this.setNew.prop < (CockpitHawk_3.this.fm.EI.engines[0].getControlProp() - 0.01F)) {
                CockpitHawk_3.this.setNew.prop += 0.0025F;
            }
            if (CockpitHawk_3.this.setNew.prop > (CockpitHawk_3.this.fm.EI.engines[0].getControlProp() + 0.01F)) {
                CockpitHawk_3.this.setNew.prop -= 0.0025F;
            }
            CockpitHawk_3.this.w.set(CockpitHawk_3.this.fm.getW());
            CockpitHawk_3.this.fm.Or.transform(CockpitHawk_3.this.w);
            CockpitHawk_3.this.setNew.turn = ((12F * CockpitHawk_3.this.setOld.turn) + CockpitHawk_3.this.w.z) / 13F;
            CockpitHawk_3.this.setNew.vspeed = ((299F * CockpitHawk_3.this.setOld.vspeed) + CockpitHawk_3.this.fm.getVertSpeed()) / 300F;
            CockpitHawk_3.this.pictSupc = (0.8F * CockpitHawk_3.this.pictSupc) + (0.2F * CockpitHawk_3.this.fm.EI.engines[0].getControlCompressor());
            boolean flag = false;
            if (CockpitHawk_3.this.setNew.gCrankAngle < (CockpitHawk_3.this.fm.CT.getGear() - 0.05F)) {
                if ((Math.abs(CockpitHawk_3.this.setNew.gCrankAngle - CockpitHawk_3.this.fm.CT.getGear()) > 0.05F) && (Math.abs(CockpitHawk_3.this.setNew.gCrankAngle - CockpitHawk_3.this.fm.CT.getGear()) < 0.9F)) {
                    CockpitHawk_3.this.setNew.gCrankAngle += 0.0025F;
                    flag = true;
                } else {
                    CockpitHawk_3.this.setNew.gCrankAngle = CockpitHawk_3.this.fm.CT.getGear();
                    CockpitHawk_3.this.setOld.gCrankAngle = CockpitHawk_3.this.fm.CT.getGear();
                }
            }
            if (CockpitHawk_3.this.setNew.gCrankAngle > (CockpitHawk_3.this.fm.CT.getGear() + 0.05F)) {
                if ((Math.abs(CockpitHawk_3.this.setNew.gCrankAngle - CockpitHawk_3.this.fm.CT.getGear()) > 0.05F) && (Math.abs(CockpitHawk_3.this.setNew.gCrankAngle - CockpitHawk_3.this.fm.CT.getGear()) < 0.9F)) {
                    CockpitHawk_3.this.setNew.gCrankAngle -= 0.0025F;
                    flag = true;
                } else {
                    CockpitHawk_3.this.setNew.gCrankAngle = CockpitHawk_3.this.fm.CT.getGear();
                    CockpitHawk_3.this.setOld.gCrankAngle = CockpitHawk_3.this.fm.CT.getGear();
                }
            }
            if (flag != this.sfxPlaying) {
                if (flag) {
                    CockpitHawk_3.this.sfxStart(16);
                } else {
                    CockpitHawk_3.this.sfxStop(16);
                }
                this.sfxPlaying = flag;
            }
            return true;
        }

        boolean sfxPlaying;

        Interpolater() {
            this.sfxPlaying = false;
        }
    }

    private class Variables {

        float altimeter;
        float azimuth;
        float throttle;
        float mix;
        float prop;
        float turn;
        float vspeed;
        float gCrankAngle;

        private Variables() {
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            aircraft().hierMesh().chunkVisible("CF_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            aircraft().hierMesh().chunkVisible("CF_D0", true);
            this.leave();
            super.doFocusLeave();
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
        this.mesh.chunkVisible("Fonar", false);
        this.mesh.chunkVisible("target", false);
        this.mesh.chunkVisible("Pricel_D0", false);
        this.mesh.chunkVisible("truba", false);
    }

    private void leave() {
        if (this.bEntered) {
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
            this.mesh.chunkVisible("SuperReticle", false);
            this.mesh.chunkVisible("Fonar", true);
            this.mesh.chunkVisible("target", true);
            this.mesh.chunkVisible("Pricel_D0", true);
            this.mesh.chunkVisible("truba", true);
        }
    }

    public void destroy() {
        this.leave();
        super.destroy();
    }

    public void doToggleAim(boolean flag) {
        if (this.isFocused() && (this.isToggleAim() != flag)) {
            if (flag) {
                this.enter();
            } else {
                this.leave();
            }
        }
    }

    public CockpitHawk_3() {
        super("3DO/Cockpit/Hawk_3/Hawk3_hier.him", "u2");
        this.lights = new LightPointActor[4];
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictSupc = 0.0F;
        this.bEntered = false;
        for (int i = 0; i < 4; i++) {
            HookNamed hooknamed = new HookNamed(this.mesh, "_light0" + (i + 1));
            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
            this.lights[i] = new LightPointActor(new LightPoint(), loc.getPoint());
            this.lights[i].light.setColor(0.8980392F, 0.8117647F, 0.6235294F);
            this.lights[i].light.setEmit(0.0F, 0.0F);
            this.pos.base().draw.lightMap().put("_light0" + (i + 1), this.lights[i]);
        }

        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("crank", 40F * this.interp(this.setNew.mix, this.setOld.mix, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("cr_tross", 0.0F, 0.0F, -40F * this.interp(this.setNew.mix, this.setOld.mix, f));
        this.mesh.chunkSetAngles("handle", 65F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("hand_tross", 0.0F, 0.0F, -65F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("lever", -50F * this.pictSupc, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("lev_tross", 0.0F, 0.0F, 50F * this.pictSupc);
        this.mesh.chunkSetAngles("magto", 44F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("prop", -2160F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 16F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 10.2F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Ped_trossL", -15.65F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Ped_trossR", -15.65F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 600F, 0.0F, 12F), CockpitHawk_3.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, 40F, -40F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, -this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        if ((this.fm.AS.astateCockpitState & 4) == 0) {
            this.mesh.chunkSetAngles("zRPS1a", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2400F, 0.0F, 11F), CockpitHawk_3.rpmScale), 0.0F);
        }
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.setNew.turn, -0.2F, 0.2F, 26F, -26F), 0.0F);
        this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 26F, -26F), 0.0F);
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zTOilOut1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs1a", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zGasPrs1a", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 8F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zTwater1a", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 300F, 0.0F, -60F), 0.0F);
        this.mesh.chunkSetAngles("zManifold1a", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.399966F, 2.133152F, 3F, 16F), CockpitHawk_3.manifoldScale), 0.0F);
        this.mesh.chunkSetAngles("Fire1", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[0] ? -15F : 0.0F);
        this.mesh.chunkSetAngles("Fire2", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[2] ? -15F : 0.0F);
        this.mesh.chunkSetAngles("Fire3", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Fire4", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[3] ? -15F : 0.0F);
        this.mesh.chunkSetAngles("Fire5", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[1] ? -15F : 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("pricel", false);
            this.mesh.chunkVisible("pricel_d1", true);
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_dd", true);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("zTOilOut1a", false);
            this.mesh.chunkVisible("zOilPrs1a", false);
            this.mesh.chunkVisible("zGasPrs1a", false);
            this.mesh.chunkVisible("zVariometer1a", false);
            this.mesh.chunkVisible("zManifold1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_dd", true);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zTurn1a", false);
            this.mesh.chunkVisible("zSlide1a", false);
            this.mesh.chunkVisible("zTwater1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            ;
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            ;
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            ;
        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        this.mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        this.mesh.chunkVisible("WingLMid_D3", hiermesh.isChunkVisible("WingLMid_D3"));
        this.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        this.mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        this.mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        this.mesh.chunkVisible("WingRMid_D3", hiermesh.isChunkVisible("WingRMid_D3"));
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("CF_D3", hiermesh.isChunkVisible("CF_D3"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.materialReplace("Gloss1D0o", hiermesh.material(hiermesh.materialFind("Gloss1D0o")));
        this.mesh.materialReplace("Gloss1D1o", hiermesh.material(hiermesh.materialFind("Gloss1D1o")));
        this.mesh.materialReplace("Gloss1D2o", hiermesh.material(hiermesh.materialFind("Gloss1D2o")));
        this.mesh.materialReplace("Gloss2D2o", hiermesh.material(hiermesh.materialFind("Gloss2D2o")));
        this.mesh.materialReplace("Matt1D0o", hiermesh.material(hiermesh.materialFind("Matt1D0o")));
        this.mesh.materialReplace("Matt2D0o", hiermesh.material(hiermesh.materialFind("Matt2D0o")));
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        this.sfxClick(1);
        if (this.cockpitLightControl) {
            for (int i = 0; i < 4; i++) {
                this.lights[i].light.setEmit(0.5F, 0.5F);
            }

        } else {
            for (int j = 0; j < 4; j++) {
                this.lights[j].light.setEmit(0.0F, 0.0F);
            }

        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictSupc;
    private LightPointActor    lights[];
    private static final float speedometerScale[] = { 0.0F, 0.0F, 18F, 45F, 75.5F, 107F, 137.5F, 170F, 206.5F, 243.75F, 286.5F, 329.5F, 374.5F };
    private static final float rpmScale[]         = { 0.0F, 5.5F, 18.5F, 59F, 99.5F, 134.5F, 165.75F, 198F, 228F, 255.5F, 308F, 345F };
    private static final float manifoldScale[]    = { 0.0F, 0.0F, 0.0F, 0.0F, 26F, 52F, 79F, 106F, 132F, 160F, 185F, 208F, 235F, 260F, 286F, 311F, 336F };
    private float              saveFov;
    private boolean            bEntered;
    
    static {
        Property.set(CockpitHawk_3.class, "normZN", 0.5F);
    }
}
