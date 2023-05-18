package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitKI_67_I_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = ((KI_67) CockpitKI_67_I_Bombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = ((KI_67) CockpitKI_67_I_Bombardier.this.aircraft()).fSightCurSideslip;
            CockpitKI_67_I_Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, f);
            if (CockpitKI_67_I_Bombardier.this.bEntered) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setSimpleAimOrient(CockpitKI_67_I_Bombardier.this.aAim + 10F * f1, CockpitKI_67_I_Bombardier.this.tAim + f, 0.0F);
            }
            CockpitKI_67_I_Bombardier.this.mesh.chunkSetAngles("Turret1A", 0.0F, -CockpitKI_67_I_Bombardier.this.aircraft().FM.turret[0].tu[0], 0.0F);
            CockpitKI_67_I_Bombardier.this.mesh.chunkSetAngles("Turret1B", 0.0F, CockpitKI_67_I_Bombardier.this.aircraft().FM.turret[0].tu[1], 0.0F);
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("NoseAXX_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            this.aircraft().hierMesh().chunkVisible("NoseAXX_D0", this.aircraft().isChunkAnyDamageVisible("CF_D"));
            this.aircraft().hierMesh().chunkVisible("Pilot3_D0", this.aircraft().FM.AS.astatePilotStates[2] < 95);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D1", this.aircraft().FM.AS.astatePilotStates[2] > 95 && this.aircraft().FM.AS.astateBailoutStep < 12);
            this.leave();
            super.doFocusLeave();
            return;
        }
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(this.aAim, this.tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
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
            return;
        }
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused()) return;
        if (this.isToggleAim() == flag) return;
        if (flag) this.enter();
        else this.leave();
    }

    public CockpitKI_67_I_Bombardier() {
        super("3DO/Cockpit/Ki67-I-Bombardier/hier.him", "he111");
        this.bEntered = false;
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
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bEntered) {
            this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((KI_67) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((KI_67) this.aircraft()).fSightCurReadyness > 0.93F;
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
    }

    private static final float angleScale[] = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;

    static {
        Property.set(CockpitKI_67_I_Bombardier.class, "astatePilotIndx", 0);
    }
}
