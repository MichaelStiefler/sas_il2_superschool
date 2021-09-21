package com.maddox.il2.objects.air;

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

public class CockpitB26_B35_Bombardier extends CockpitPilot {
    private class Variables {

        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitB26_B35_Bombardier.this.setTmp = CockpitB26_B35_Bombardier.this.setOld;
            CockpitB26_B35_Bombardier.this.setOld = CockpitB26_B35_Bombardier.this.setNew;
            CockpitB26_B35_Bombardier.this.setNew = CockpitB26_B35_Bombardier.this.setTmp;
            float f = ((B_26B35) CockpitB26_B35_Bombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = ((B_26B35) CockpitB26_B35_Bombardier.this.aircraft()).fSightCurSideslip;
            CockpitB26_B35_Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if (CockpitB26_B35_Bombardier.this.bEntered) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setSimpleAimOrient(CockpitB26_B35_Bombardier.this.aAim + (10F * f1), CockpitB26_B35_Bombardier.this.tAim + f, 0.0F);
            }
            CockpitB26_B35_Bombardier.this.mesh.chunkSetAngles("TurretA", 0.0F, CockpitB26_B35_Bombardier.this.aircraft().FM.turret[0].tu[0], 0.0F);
            CockpitB26_B35_Bombardier.this.mesh.chunkSetAngles("TurretB", 0.0F, CockpitB26_B35_Bombardier.this.aircraft().FM.turret[0].tu[1], 0.0F);
            float f2 = CockpitB26_B35_Bombardier.this.waypointAzimuth();
            CockpitB26_B35_Bombardier.this.setNew.azimuth.setDeg(CockpitB26_B35_Bombardier.this.setOld.azimuth.getDeg(1.0F), CockpitB26_B35_Bombardier.this.fm.Or.azimut());
            CockpitB26_B35_Bombardier.this.setNew.waypointAzimuth.setDeg(CockpitB26_B35_Bombardier.this.setOld.waypointAzimuth.getDeg(0.1F), f2);
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
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(this.aAim, this.tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
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
        }
    }

    public void destroy() {
        super.destroy();
        this.leave();
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

    public CockpitB26_B35_Bombardier() {
        super("3DO/Cockpit/B-26B35-Bombardier/hier.him", "bf109");
        this.bEntered = false;
        this.setOld = new Variables();
        this.setNew = new Variables();
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.cockpitNightMats = (new String[] { "textrbm9", "texture25" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
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
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), CockpitB26_B35_Bombardier.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1", 0.0F, this.floatindex(this.cvt(this.fm.getSpeedKMH(), 0.0F, 836.859F, 0.0F, 13F), CockpitB26_B35_Bombardier.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSecond", 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, this.setNew.waypointAzimuth.getDeg(0.1F), 0.0F);
        if (this.bEntered) {
            this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((B_26B35) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), CockpitB26_B35_Bombardier.angleScale), 0.0F, 0.0F);
            boolean flag = ((B_26B35) this.aircraft()).fSightCurReadyness > 0.93F;
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
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
        }
    }

    private static final float angleScale[]       = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private static final float speedometerScale[] = { 0.0F, 2.5F, 54F, 104F, 154.5F, 205.5F, 224F, 242F, 259.5F, 277.5F, 296.25F, 314F, 334F, 344.5F };
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;

    static {
        Property.set(CockpitB26_B35_Bombardier.class, "astatePilotIndx", 0);
    }

}
