package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitF_105Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = ((F_105) CockpitF_105Bombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = ((F_105) CockpitF_105Bombardier.this.aircraft()).fSightCurSideslip;
            CockpitF_105Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if (CockpitF_105Bombardier.this.bEntered && CockpitF_105Bombardier.this.bBAiming) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setSimpleAimOrient(CockpitF_105Bombardier.this.aAim + 10F * f1, CockpitF_105Bombardier.this.tAim + f, 0.0F);
            }
            float f2 = CockpitF_105Bombardier.this.fm.getAltitude();
            float f3 = (float) (-(Math.abs(((FlightModelMain) (CockpitF_105Bombardier.this.fm)).Vwld.length()) * Math.sin(Math.toRadians(Math.abs(((FlightModelMain) (CockpitF_105Bombardier.this.fm)).Or.getTangage())))) * 0.10189999639987946D);
            f3 += (float) Math.sqrt(f3 * f3 + 2.0F * f2 * 0.1019F);
            float f4 = Math.abs((float) ((FlightModelMain) (CockpitF_105Bombardier.this.fm)).Vwld.length()) * (float) Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (CockpitF_105Bombardier.this.fm)).Or.getTangage())));
            float f5 = (f4 * f3 + 10F) - 10F;
            CockpitF_105Bombardier.this.alpha = 90F - Math.abs(((FlightModelMain) (CockpitF_105Bombardier.this.fm)).Or.getTangage()) - (float) Math.toDegrees(Math.atan(f5 / f2));
            return true;
        }

        Interpolater() {}
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            this.enter();
            this.go_top();
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
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
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
            return;
        }
    }

    private void go_top() {
        this.bBAiming = false;
        CmdEnv.top().exec("fov 33.3");
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(false);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(this.aDiv, this.tDiv, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
    }

    private void go_bottom() {
        this.bBAiming = true;
        CmdEnv.top().exec("fov 23.913");
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(this.aAim, this.tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused())
            return;
        if (this.isToggleAim() == flag)
            return;
        if (flag)
            this.go_bottom();
        else
            this.go_top();
    }

    public CockpitF_105Bombardier() {
        super("3DO/Cockpit/Ar-234B-2-Bombardier/hier.him", "he111");
        this.bEntered = false;
        this.bBAiming = false;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(super.mesh, "CAMERAAIM");
            hooknamed.computePos(this, super.pos.getAbs(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        try {
            Loc loc1 = new Loc();
            HookNamed hooknamed1 = new HookNamed(super.mesh, "CAMERA");
            hooknamed1.computePos(this, super.pos.getAbs(), loc1);
            this.aDiv = loc1.getOrient().getAzimut();
            this.tDiv = loc1.getOrient().getTangage();
        } catch (Exception exception1) {
            System.out.println(exception1.getMessage());
            exception1.printStackTrace();
        }
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bBAiming) {
            super.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((F_105) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((F_105) this.aircraft()).fSightCurReadyness > 0.93F;
            super.mesh.chunkVisible("zReticle", flag);
            super.mesh.chunkVisible("zAngleMark", flag);
        } else {
            super.mesh.chunkSetAngles("zGSDimm", -this.alpha, 0.0F, 0.0F);
        }
    }

    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private float              aDiv;
    private float              tDiv;
    private float              alpha;
    private boolean            bEntered;
    private boolean            bBAiming;
    private static final float angleScale[] = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };

    static {
        Property.set(CockpitF_105Bombardier.class, "astatePilotIndx", 0);
    }

}