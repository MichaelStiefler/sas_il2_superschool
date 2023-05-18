package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitKI_51_I extends CockpitPilot {
    private class Variables {

        float      dimPos;
        float      throttle;
        float      prop;
        float      mix;
        float      altimeter;
        float      man;
        float      vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork waypointDeviation;

        private Variables() {
            this.dimPos = 0.0F;
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.waypointDeviation = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitKI_51_I.this.fm != null) {
                CockpitKI_51_I.this.setTmp = CockpitKI_51_I.this.setOld;
                CockpitKI_51_I.this.setOld = CockpitKI_51_I.this.setNew;
                CockpitKI_51_I.this.setNew = CockpitKI_51_I.this.setTmp;
                if (CockpitKI_51_I.this.cockpitDimControl) {
                    if (CockpitKI_51_I.this.setNew.dimPos < 1.0F) CockpitKI_51_I.this.setNew.dimPos = CockpitKI_51_I.this.setOld.dimPos + 0.03F;
                } else if (CockpitKI_51_I.this.setNew.dimPos > 0.0F) CockpitKI_51_I.this.setNew.dimPos = CockpitKI_51_I.this.setOld.dimPos - 0.03F;
                CockpitKI_51_I.this.setNew.throttle = 0.8F * CockpitKI_51_I.this.setOld.throttle + 0.2F * CockpitKI_51_I.this.fm.CT.PowerControl;
                CockpitKI_51_I.this.setNew.prop = 0.8F * CockpitKI_51_I.this.setOld.prop + 0.2F * CockpitKI_51_I.this.fm.EI.engines[0].getControlProp();
                CockpitKI_51_I.this.setNew.mix = 0.8F * CockpitKI_51_I.this.setOld.mix + 0.2F * CockpitKI_51_I.this.fm.EI.engines[0].getControlMix();
                CockpitKI_51_I.this.setNew.man = 0.92F * CockpitKI_51_I.this.setOld.man + 0.08F * CockpitKI_51_I.this.fm.EI.engines[0].getManifoldPressure();
                CockpitKI_51_I.this.setNew.altimeter = CockpitKI_51_I.this.fm.getAltitude();
                float f = CockpitKI_51_I.this.waypointAzimuth();
                CockpitKI_51_I.this.setNew.azimuth.setDeg(CockpitKI_51_I.this.setOld.azimuth.getDeg(1.0F), CockpitKI_51_I.this.fm.Or.azimut());
                if (CockpitKI_51_I.this.useRealisticNavigationInstruments()) {
                    CockpitKI_51_I.this.setNew.waypointDeviation.setDeg(CockpitKI_51_I.this.setOld.waypointDeviation.getDeg(1.0F), CockpitKI_51_I.this.getBeaconDirection());
                    CockpitKI_51_I.this.setNew.waypointAzimuth.setDeg(CockpitKI_51_I.this.setOld.waypointAzimuth.getDeg(1.0F), f);
                } else {
                    CockpitKI_51_I.this.setNew.waypointDeviation.setDeg(CockpitKI_51_I.this.setOld.waypointDeviation.getDeg(0.1F), f - CockpitKI_51_I.this.setOld.azimuth.getDeg(1.0F));
                    CockpitKI_51_I.this.setNew.waypointAzimuth.setDeg(CockpitKI_51_I.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitKI_51_I.this.fm.Or.azimut() + 90F);
                }
                CockpitKI_51_I.this.setNew.vspeed = 0.5F * CockpitKI_51_I.this.setOld.vspeed + 0.5F * CockpitKI_51_I.this.fm.getVertSpeed();
                CockpitKI_51_I.this.mesh.chunkSetAngles("Turret1A_D0", 0.0F, -CockpitKI_51_I.this.aircraft().FM.turret[0].tu[0], 0.0F);
                CockpitKI_51_I.this.mesh.chunkSetAngles("Turret1B_D0", 0.0F, CockpitKI_51_I.this.aircraft().FM.turret[0].tu[1], 0.0F);
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
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
        if (hookpilot.isPadlock()) hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
        this.mesh.chunkVisible("SuperReticle", true);
        this.mesh.chunkVisible("Z_BoxTinter", true);
    }

    private void leave() {
        if (!this.bEntered) return;
        else {
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
            this.mesh.chunkVisible("Z_BoxTinter", false);
            return;
        }
    }

    public void destroy() {
        this.leave();
        super.destroy();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused()) return;
        if (this.isToggleAim() == flag) return;
        if (flag) this.enter();
        else this.leave();
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(5F);
    }

    public CockpitKI_51_I() {
        super("3DO/Cockpit/Ki-51-I/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.bEntered = false;
        this.cockpitNightMats = new String[] { "gauges1_d1", "gauges1", "gauges2_d1", "gauges2", "gauges3_d1", "gauges3", "gauges4_d1", "gauges4", "gauges5", "gauges6", "gauges7", "turnbank_d1", "turnbank", "Fuel", "gauge1_D1", "gauge1", "gauge2_D2",
                "gauge2", "gauge3", "gauge4_D4", "gauge4", "gauge" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
            this.bNeedSetUp = false;
        }
        if (KI_51_I.bChangedPit) {
            this.reflectPlaneToModel();
            KI_51_I.bChangedPit = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.44F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) Cockpit.xyz[2] = -0.0036F;
        this.mesh.chunkSetLocate("Z_Trigger1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_ReViTinter", this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BoxTinter", this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiat", 0.0F, -45F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 64.5F * this.setNew.throttle, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPitch1", 58.25F * this.setNew.prop, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 48F * this.setNew.mix, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps", 70F * this.fm.CT.getFlap(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.EI.engines[0].getControlRadiator(), -15F, 15F, 0.253F, -0.253F);
        this.mesh.chunkSetLocate("Z_Razi", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zPedalL", -this.fm.CT.getRudder() * 10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPedalR", this.fm.CT.getRudder() * 10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 8F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 21600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 2160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1000F, 0.0F, 20F), speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.mesh.chunkSetAngles("Z_Compass2", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.5F, 6.5F), vsiNeedleScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 335F), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 2.0F, 0.0F, -360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Oil1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 310F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.setNew.man, 0.400051F, 1.333305F, -202.5F, 112.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 5.5F, 0.0F, 295.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 360F, 0.0F, 76.8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 324F, 0.0F, 75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 282.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel3", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 247F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel4", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 247F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.setNew.vspeed, -15F, 15F, -0.053F, 0.053F);
        this.mesh.chunkSetAngles("Z_TurnBank0", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.fm.Or.getTangage(), -52F, 52F, 26F, -26F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(this.getBall(8D), -8F, 8F, -16F, 16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats1_D1", true);
            this.mesh.chunkVisible("Z_OilSplats2_D1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Panel1_D0", false);
            this.mesh.chunkVisible("Panel1_D1", true);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_TurnBank3", false);
            this.mesh.chunkVisible("Z_Hour1", false);
            this.mesh.chunkVisible("Z_Minute1", false);
            this.mesh.chunkVisible("Z_Oil1", false);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
        this.mesh.materialReplace("Pilot2", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
        this.mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
        this.mesh.chunkVisible("Turret1B_D0", hiermesh.isChunkVisible("Turret1B_D0"));
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 10F, 35F, 70F, 105F, 145F, 190F, 230F, 275F, 315F, 360F, 397.5F, 435F, 470F, 505F, 537.5F, 570F, 600F, 630F, 655F, 680F };
    private static final float vsiNeedleScale[]   = { -200F, -160F, -125F, -90F, 90F, 125F, 160F, 200F };
    private float              saveFov;
    private boolean            bEntered;
}
