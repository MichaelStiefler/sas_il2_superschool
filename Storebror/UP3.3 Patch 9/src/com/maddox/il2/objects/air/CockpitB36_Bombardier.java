package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB36_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = ((B_36X) CockpitB36_Bombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = ((B_36X) CockpitB36_Bombardier.this.aircraft()).fSightCurSideslip;
            CockpitB36_Bombardier.calibrAngle = 360F - CockpitB36_Bombardier.this.fm.Or.getPitch();
            CockpitB36_Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, CockpitB36_Bombardier.calibrAngle + f);
            if (CockpitB36_Bombardier.this.bEntered) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setInstantOrient(CockpitB36_Bombardier.this.aAim + (10F * f1), CockpitB36_Bombardier.this.tAim + CockpitB36_Bombardier.calibrAngle + f, 0.0F);
            }
            if (CockpitB36_Bombardier.this.fm != null) {
                if (CockpitB36_Bombardier.this.bNeedSetUp) {
                    CockpitB36_Bombardier.this.reflectPlaneMats();
                    CockpitB36_Bombardier.this.bNeedSetUp = false;
                }
                CockpitB36_Bombardier.this.setTmp = CockpitB36_Bombardier.this.setOld;
                CockpitB36_Bombardier.this.setOld = CockpitB36_Bombardier.this.setNew;
                CockpitB36_Bombardier.this.setNew = CockpitB36_Bombardier.this.setTmp;
                CockpitB36_Bombardier.this.w.set(CockpitB36_Bombardier.this.fm.getW());
                CockpitB36_Bombardier.this.fm.Or.transform(CockpitB36_Bombardier.this.w);
                CockpitB36_Bombardier.this.setNew.altimeter = CockpitB36_Bombardier.this.fm.getAltitude();
                float f2 = CockpitB36_Bombardier.this.waypointAzimuth();
                CockpitB36_Bombardier.this.setNew.azimuth.setDeg(CockpitB36_Bombardier.this.setOld.azimuth.getDeg(1.0F), CockpitB36_Bombardier.this.fm.Or.azimut());
                if (CockpitB36_Bombardier.this.useRealisticNavigationInstruments()) {
                    CockpitB36_Bombardier.this.setNew.waypointAzimuth.setDeg(f2 - 90F);
                    CockpitB36_Bombardier.this.setOld.waypointAzimuth.setDeg(f2 - 90F);
                } else {
                    CockpitB36_Bombardier.this.setNew.waypointAzimuth.setDeg(CockpitB36_Bombardier.this.setOld.waypointAzimuth.getDeg(0.1F), f2);
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.15D, 0.0D, -0.1D);
            hookpilot.setTubeSight(point3d);
            this.aircraft().hierMesh().chunkVisible("CF_D" + this.aircraft().chunkDamageVisible("CF"), false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D" + this.aircraft().chunkDamageVisible("Pilot3"), false);
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
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
            this.aircraft().hierMesh().chunkVisible("CF_D" + this.aircraft().chunkDamageVisible("CF"), true);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D" + this.aircraft().chunkDamageVisible("Pilot3"), true);
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", true);
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
        hookpilot.setSimpleAimOrient(0.0F, -33F, 0.0F);
        this.enteringAim = true;
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setInstantOrient(this.aAim, this.tAim, 0.0F);
        hookpilot.setSimpleUse(true);
        this.doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, -33F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!this.bEntered) {
            return;
        } else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, -33F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            this.doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            return;
        }
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused()) {
            return;
        }
        if (this.isToggleAim() == flag) {
            return;
        }
        if (flag) {
            this.prepareToEnter();
        } else {
            this.leave();
        }
    }

    public CockpitB36_Bombardier() {
        super("3DO/Cockpit/B36/hierBombardier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bEntered = false;
        this.bNeedSetUp = true;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        this.cockpitNightMats = (new String[] { "Gauges1", "textrbm4" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.printCompassHeading = true;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Zspeedbomb", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), CockpitB36_Bombardier.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Zalt2bomb", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("Zalt01bomb", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("Zclock1bomb", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Zclock2bomb", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Zcompassbomb", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Zcompass02bomb", 0.0F, this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isAimReached()) {
                this.enteringAim = false;
                this.enter();
            } else if (!hookpilot.isAim()) {
                this.enteringAim = false;
            }
        }
        if (this.bEntered) {
            this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((B_36X) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), CockpitB36_Bombardier.angleScale), 0.0F, 0.0F);
            boolean flag = ((B_36X) this.aircraft()).fSightCurReadyness > 0.93F;
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage8", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage7", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage5", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage6", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
    }

    protected void reflectPlaneMats() {
    }

    public Vector3f            w;
    private boolean            enteringAim;
    private static final float angleScale[]       = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private static final float speedometerScale[] = { 0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 296.5F, 308.5F, 324F, 338.5F };
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private static float       calibrAngle        = 0.0F;
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bNeedSetUp;
    private boolean            bEntered;

    static {
        Property.set(CockpitB36_Bombardier.class, "astatePilotIndx", 2);
    }
}
