package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
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

public class CockpitF1M extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitF1M.this.fm != null) {
                CockpitF1M.this.setTmp = CockpitF1M.this.setOld;
                CockpitF1M.this.setOld = CockpitF1M.this.setNew;
                CockpitF1M.this.setNew = CockpitF1M.this.setTmp;
                if (CockpitF1M.this.cockpitDimControl) {
                    if (CockpitF1M.this.setNew.dimPos < 1.0F) CockpitF1M.this.setNew.dimPos = CockpitF1M.this.setOld.dimPos + 0.03F;
                } else if (CockpitF1M.this.setNew.dimPos > 0.0F) CockpitF1M.this.setNew.dimPos = CockpitF1M.this.setOld.dimPos - 0.03F;
                CockpitF1M.this.setNew.manifold = 0.8F * CockpitF1M.this.setOld.manifold + 0.2F * CockpitF1M.this.fm.EI.engines[0].getManifoldPressure();
                CockpitF1M.this.setNew.throttle = 0.8F * CockpitF1M.this.setOld.throttle + 0.2F * CockpitF1M.this.fm.CT.PowerControl;
                CockpitF1M.this.setNew.prop = 0.8F * CockpitF1M.this.setOld.prop + 0.2F * CockpitF1M.this.fm.EI.engines[0].getControlProp();
                CockpitF1M.this.setNew.mix = 0.8F * CockpitF1M.this.setOld.mix + 0.2F * CockpitF1M.this.fm.EI.engines[0].getControlMix();
                CockpitF1M.this.setNew.man = 0.92F * CockpitF1M.this.setOld.man + 0.08F * CockpitF1M.this.fm.EI.engines[0].getManifoldPressure();
                CockpitF1M.this.setNew.altimeter = CockpitF1M.this.fm.getAltitude();
                float f = CockpitF1M.this.waypointAzimuth();
                CockpitF1M.this.setNew.azimuth.setDeg(CockpitF1M.this.setOld.azimuth.getDeg(1.0F), CockpitF1M.this.fm.Or.azimut());
                CockpitF1M.this.setNew.waypointDeviation.setDeg(CockpitF1M.this.setOld.waypointDeviation.getDeg(0.1F), f - CockpitF1M.this.setOld.azimuth.getDeg(1.0F) + World.Rnd().nextFloat(-5F, 5F));
                CockpitF1M.this.setNew.waypointAzimuth.setDeg(CockpitF1M.this.setOld.waypointAzimuth.getDeg(1.0F), f);
                CockpitF1M.this.setNew.vspeed = 0.5F * CockpitF1M.this.setOld.vspeed + 0.5F * CockpitF1M.this.fm.getVertSpeed();
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
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) return 0.0F;
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float f;
        for (f = (float) (57.295779513082323D * Math.atan2(-this.tmpV.y, this.tmpV.x)); f <= -180F; f += 180F)
            ;
        for (; f > 180F; f -= 180F)
            ;
        return f;
    }

    public CockpitF1M() {
        super("3DO/Cockpit/F1MPete/hier.him", "u2");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.bEntered = false;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = new String[] { "Fuel", "gauge1_D1", "gauge1", "gauge2_D2", "gauge2", "gauge3", "gauge4_D4", "gauge4", "gauge" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) this.bNeedSetUp = false;
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.59F);
        this.mesh.chunkSetAngles("Z_Throtle1", 70.45F * this.setNew.throttle, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPitch1", 77F * this.setNew.prop, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMix1", 64.1F * this.setNew.mix, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalBase", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 8F, 0.0F, -(this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1000F, 0.0F, 20F), speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(8D), -8F, 8F, -16F, 16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.5F, 6.5F), vsiNeedleScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 335F), 0.0F, 0.0F);
//        if ((this.fm.AS.astateCockpitState & 0x40) != 0);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 5.5F, 0.0F, 295.5F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
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
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XHullDamage3", true);
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("Z_OilSplats_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XHullDamage2", true);
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
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
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
