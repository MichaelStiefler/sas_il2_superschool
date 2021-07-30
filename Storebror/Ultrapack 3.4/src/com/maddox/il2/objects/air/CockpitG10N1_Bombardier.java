package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.air.electronics.RadarFuG200Equipment;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitG10N1_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = ((G10N1) aircraft()).fSightCurForwardAngle;
            float f1 = ((G10N1) aircraft()).fSightCurSideslip;
            CockpitG10N1_Bombardier.calibrAngle = 360F - fm.Or.getPitch();
            mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, calibrAngle + f);
            if (bEntered) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setSimpleAimOrient(aAim + 10F * f1, tAim + calibrAngle + f, 0.0F);
            }
            mesh.chunkSetAngles("Turret1A", 0.0F, -aircraft().FM.turret[0].tu[0], 0.0F);
            mesh.chunkSetAngles("Turret1B", 0.0F, aircraft().FM.turret[0].tu[1], 0.0F);
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0F, -55F, 0F);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!isFocused()) {
            return;
        } else {
            leave();
            super.doFocusLeave();
            return;
        }
    }

    private void enter() {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
        ((TypeRadarFuG200Carrier) this.aircraft()).setCurPilot(1);
    }

    private void leave() {
        if (!bEntered) return;
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
        CmdEnv.top().exec("fov " + saveFov);
        HookPilot hookpilot = HookPilot.current;
        hookpilot.doAim(false);
//        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        hookpilot.setSimpleAimOrient(0F, -55F, 0F);
        hookpilot.setSimpleUse(false);
        boolean flag = HotKeyEnv.isEnabled("aircraftView");
        HotKeyEnv.enable("PanView", flag);
        HotKeyEnv.enable("SnapView", flag);
        bEntered = false;
        ((TypeRadarFuG200Carrier) this.aircraft()).setCurPilot(0);
    }

    public void destroy() {
        super.destroy();
        leave();
    }

    public void doToggleAim(boolean flag) {
        if (!isFocused()) return;
        if (isToggleAim() == flag) return;
        if (flag) enter();
        else leave();
    }

    public CockpitG10N1_Bombardier() {
        super("3DO/Cockpit/G10N1-Bombardier/hier.him", "he111");
        bEntered = false;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(mesh, "CAMERAAIM");
            hooknamed.computePos(this, pos.getAbs(), loc);
            aAim = loc.getOrient().getAzimut();
            tAim = loc.getOrient().getTangage();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.cockpitRadarFuG200 = new RadarFuG200Equipment(this, 40, 1500F, 5F, 0.2F, 0.012F, 0.011637F, 0.073296F);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (bEntered) {
            mesh.chunkSetAngles("zAngleMark", -floatindex(cvt(((G10N1) aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((G10N1) aircraft()).fSightCurReadyness > 0.93F;
            mesh.chunkVisible("BlackBox", true);
            mesh.chunkVisible("zReticle", flag);
            mesh.chunkVisible("zAngleMark", flag);
        } else {
            mesh.chunkVisible("BlackBox", false);
            mesh.chunkVisible("zReticle", false);
            mesh.chunkVisible("zAngleMark", false);
            // +++ RadarFuG200 +++
            this.cockpitRadarFuG200.updateRadar();
            // --- RadarFuG200 ---
        }
    }

    private static final float angleScale[] = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;
    private static float calibrAngle = 3F;

    // +++ RadarFuG200 +++
    private RadarFuG200Equipment cockpitRadarFuG200;
    // --- RadarFuG200 ---

    static {
        Class class1 = CockpitG10N1_Bombardier.class;
        Property.set(class1, "astatePilotIndx", 2);
    }
}
