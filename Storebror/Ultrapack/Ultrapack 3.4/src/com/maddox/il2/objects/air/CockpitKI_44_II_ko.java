package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
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
import com.maddox.sound.ReverbFXRoom;

public class CockpitKI_44_II_ko extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitKI_44_II_ko.this.fm != null) {
                CockpitKI_44_II_ko.this.setTmp = CockpitKI_44_II_ko.this.setOld;
                CockpitKI_44_II_ko.this.setOld = CockpitKI_44_II_ko.this.setNew;
                CockpitKI_44_II_ko.this.setNew = CockpitKI_44_II_ko.this.setTmp;
                if (CockpitKI_44_II_ko.this.cockpitDimControl) {
                    if (CockpitKI_44_II_ko.this.setNew.dimPos < 1.0F) {
                        CockpitKI_44_II_ko.this.setNew.dimPos = CockpitKI_44_II_ko.this.setOld.dimPos + 0.03F;
                    }
                } else if (CockpitKI_44_II_ko.this.setNew.dimPos > 0.0F) {
                    CockpitKI_44_II_ko.this.setNew.dimPos = CockpitKI_44_II_ko.this.setOld.dimPos - 0.03F;
                }
                CockpitKI_44_II_ko.this.setNew.manifold = (0.8F * CockpitKI_44_II_ko.this.setOld.manifold) + (0.2F * CockpitKI_44_II_ko.this.fm.EI.engines[0].getManifoldPressure());
                CockpitKI_44_II_ko.this.setNew.throttle = (0.8F * CockpitKI_44_II_ko.this.setOld.throttle) + (0.2F * CockpitKI_44_II_ko.this.fm.CT.PowerControl);
                CockpitKI_44_II_ko.this.setNew.prop = (0.8F * CockpitKI_44_II_ko.this.setOld.prop) + (0.2F * CockpitKI_44_II_ko.this.fm.EI.engines[0].getControlProp());
                CockpitKI_44_II_ko.this.setNew.mix = (0.8F * CockpitKI_44_II_ko.this.setOld.mix) + (0.2F * CockpitKI_44_II_ko.this.fm.EI.engines[0].getControlMix());
                CockpitKI_44_II_ko.this.setNew.man = (0.92F * CockpitKI_44_II_ko.this.setOld.man) + (0.08F * CockpitKI_44_II_ko.this.fm.EI.engines[0].getManifoldPressure());
                CockpitKI_44_II_ko.this.setNew.altimeter = CockpitKI_44_II_ko.this.fm.getAltitude();
                float f = CockpitKI_44_II_ko.this.waypointAzimuth();
                CockpitKI_44_II_ko.this.setNew.azimuth.setDeg(CockpitKI_44_II_ko.this.setOld.azimuth.getDeg(1.0F), CockpitKI_44_II_ko.this.fm.Or.azimut());
                CockpitKI_44_II_ko.this.setNew.waypointDeviation.setDeg(CockpitKI_44_II_ko.this.setOld.waypointDeviation.getDeg(0.1F), (f - CockpitKI_44_II_ko.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-5F, 5F));
                CockpitKI_44_II_ko.this.setNew.waypointAzimuth.setDeg(CockpitKI_44_II_ko.this.setOld.waypointAzimuth.getDeg(1.0F), f);
                CockpitKI_44_II_ko.this.setNew.vspeed = (0.5F * CockpitKI_44_II_ko.this.setOld.vspeed) + (0.5F * CockpitKI_44_II_ko.this.fm.getVertSpeed());
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      dimPos;
        float      throttle;
        float      prop;
        float      mix;
        float      altimeter;
        float      man;
        float      vspeed;
        float      manifold;
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
        if (this.isFocused()) {
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
        this.mesh.chunkVisible("Z_BoxTinter", true);
    }

    private void leave() {
        if (this.bEntered) {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean bool = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", bool);
            HotKeyEnv.enable("SnapView", bool);
            this.bEntered = false;
            this.mesh.chunkVisible("SuperReticle", false);
            this.mesh.chunkVisible("Z_BoxTinter", false);
        }
    }

    public void destroy() {
        this.leave();
        super.destroy();
    }

    public void doToggleAim(boolean bool) {
        if (this.isFocused() && (this.isToggleAim() != bool)) {
            if (bool) {
                this.enter();
            } else {
                this.leave();
            }
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        }
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float f = (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x)));
        while (f > 180F) {
            f -= 360F;
        }
        while (f <= -180F) {
            f += 360F;
        }
        return f;
    }

    public CockpitKI_44_II_ko() {
        super("3DO/Cockpit/Ki-44II-ko/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.bEntered = false;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "Fuel", "gauge1_D1", "gauge1", "gauge2_D2", "gauge2", "gauge3", "gauge4_D4", "gauge4", "gauge" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.59F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_ReViTinter", this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BoxTinter", this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiat", 0.0F, -45F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 70.45F * this.setNew.throttle, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPitch1", 77F * this.setNew.prop, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMix1", 64.1F * this.setNew.mix, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalBase", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 8F, 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1000F, 0.0F, 20F), CockpitKI_44_II_ko.speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(8D), -8F, 8F, -16F, 16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.5F, 6.5F), CockpitKI_44_II_ko.vsiNeedleScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 335F), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 2.0F, 0.0F, -360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Oil1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 295.5F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.setNew.manifold, 0.2000681F, 2.066576F, -144.75F, 193F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 5.5F, 0.0F, 295.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 76.8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 200F, 0.0F, 64F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 282.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel3", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 247F), 0.0F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("Z_Red1", this.fm.CT.getGear() < 0.01F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_Pres1", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_Temp2", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
        this.mesh.materialReplace("Pilot2", mat);
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
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
