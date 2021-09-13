package com.maddox.il2.objects.air;

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

public class CockpitCa133_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = ((CA_133) CockpitCa133_Bombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = ((CA_133) CockpitCa133_Bombardier.this.aircraft()).fSightCurSideslip;
            CockpitCa133_Bombardier.this.mesh.chunkSetAngles("BlackBox", 0.0F, -f1, f);
            if (CockpitCa133_Bombardier.this.bEntered) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setSimpleAimOrient(CockpitCa133_Bombardier.this.aAim + f1, CockpitCa133_Bombardier.this.tAim + f, 0.0F);
            }
            CockpitCa133_Bombardier.this.mesh.chunkSetAngles("TurretA", 0.0F, CockpitCa133_Bombardier.this.aircraft().FM.turret[0].tu[0], 0.0F);
            CockpitCa133_Bombardier.this.mesh.chunkSetAngles("TurretB", 0.0F, CockpitCa133_Bombardier.this.aircraft().FM.turret[0].tu[1], 0.0F);
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((CA_133) this.fm.actor).bPitUnfocused = false;
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            this.enter();
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            ((CA_133) this.fm.actor).bPitUnfocused = false;
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

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
    }

    public CockpitCa133_Bombardier() {
        super("3DO/Cockpit/BombardierCa133/BombardierCa133.him", "he111");
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
        this.cockpitNightMats = (new String[] { "clocks1", "clocks2", "clocks4", "clocks5" });
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
        if (this.bEntered) {
            this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((CA_133) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), CockpitCa133_Bombardier.angleScale), 0.0F, 0.0F);
            boolean flag = ((CA_133) this.aircraft()).fSightCurReadyness > 0.93F;
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
        this.mesh.chunkSetAngles("zAltWheel", 0.0F, this.cvt(((CA_133) this.aircraft()).fSightCurAltitude, 0.0F, 10000F, 0.0F, 375.8333F), 0.0F);
        this.mesh.chunkSetAngles("zAnglePointer", 0.0F, ((CA_133) this.aircraft()).fSightCurForwardAngle, 0.0F);
        this.mesh.chunkSetAngles("zAngleWheel", 0.0F, -10F * ((CA_133) this.aircraft()).fSightCurForwardAngle, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 16F), CockpitCa133_Bombardier.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zSpeedPointer", 0.0F, this.cvt(((CA_133) this.aircraft()).fSightCurSpeed, 150F, 600F, 0.0F, 60F), 0.0F);
        this.mesh.chunkSetAngles("zSpeedWheel", 0.0F, 0.333F * ((CA_133) this.aircraft()).fSightCurSpeed, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", -this.cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkVisible("zRed1", this.fm.CT.BayDoorControl > 0.66F);
        this.mesh.chunkVisible("zYellow1", this.fm.CT.BayDoorControl < 0.33F);
    }

    private static final float angleScale[]       = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private static final float speedometerScale[] = { 0.0F, 0.1F, 19F, 37.25F, 63.5F, 91.5F, 112F, 135.5F, 159.5F, 186.5F, 213F, 238F, 264F, 289F, 314.5F, 339.5F, 359.5F, 360F, 360F, 360F };
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;

    static {
        Property.set(CockpitCa133_Bombardier.class, "astatePilotIndx", 0);
    }

}
