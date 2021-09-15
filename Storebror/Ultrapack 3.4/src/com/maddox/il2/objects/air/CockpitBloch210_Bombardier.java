package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitBloch210_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitBloch210_Bombardier.this.resetYPRmodifier();
            float f = ((Bloch210) CockpitBloch210_Bombardier.this.aircraft()).fSightCurSpeed;
            float f1 = ((Bloch210) CockpitBloch210_Bombardier.this.aircraft()).fSightCurAltitude;
            CockpitBloch210_Bombardier.this.curAlt = ((19F * CockpitBloch210_Bombardier.this.curAlt) + f1) / 20F;
            CockpitBloch210_Bombardier.this.curSpd = ((19F * CockpitBloch210_Bombardier.this.curSpd) + f) / 20F;
            CockpitBloch210_Bombardier.this.mesh.chunkSetAngles("zScaleKM", 0.04F * CockpitBloch210_Bombardier.this.curAlt, 0.0F, 0.0F);
            CockpitBloch210_Bombardier.this.mesh.chunkSetAngles("zScaleM", 0.36F * CockpitBloch210_Bombardier.this.curAlt, 0.0F, 0.0F);
            CockpitBloch210_Bombardier.this.mesh.chunkSetAngles("zScaleKMH", -0.8F * (CockpitBloch210_Bombardier.this.curSpd - 50F), 0.0F, 0.0F);
            float f2 = 0.5F * (float) Math.tan(Math.atan((83.333335876464844D * Math.sqrt((2.0F * CockpitBloch210_Bombardier.this.curAlt) / Atmosphere.g())) / CockpitBloch210_Bombardier.this.curAlt));
            float f3 = (float) Math.tan(Math.atan((CockpitBloch210_Bombardier.this.curSpd / 3.6F * Math.sqrt((2.0F * CockpitBloch210_Bombardier.this.curAlt) / Atmosphere.g())) / CockpitBloch210_Bombardier.this.curAlt));
            Cockpit.xyz[0] = -0.0005F * CockpitBloch210_Bombardier.this.curAlt;
            Cockpit.xyz[1] = -1F * (f2 - f3);
            CockpitBloch210_Bombardier.this.mesh.chunkSetLocate("zScaleCurve", Cockpit.xyz, Cockpit.ypr);
            if (Math.abs(CockpitBloch210_Bombardier.this.fm.Or.getKren()) < 30D) {
                CockpitBloch210_Bombardier.this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -CockpitBloch210_Bombardier.this.fm.Or.getYaw(), 0.0F);
            }
            if (Math.abs(CockpitBloch210_Bombardier.this.fm.Or.getKren()) > 3.5D) {
                CockpitBloch210_Bombardier.this.pencilDisp = 0.0004F * CockpitBloch210_Bombardier.this.fm.Or.getKren();
                if (CockpitBloch210_Bombardier.this.pencilDisp > 0.1725F) {
                    CockpitBloch210_Bombardier.this.pencilDisp = 0.1725F;
                }
                if (CockpitBloch210_Bombardier.this.pencilDisp < -0.2529F) {
                    CockpitBloch210_Bombardier.this.pencilDisp = -0.2529F;
                }
                Cockpit.xyz[0] = 0.0F;
                Cockpit.xyz[1] = CockpitBloch210_Bombardier.this.pencilDisp;
                CockpitBloch210_Bombardier.this.mesh.chunkSetLocate("Z_Pencil1", Cockpit.xyz, Cockpit.ypr);
                CockpitBloch210_Bombardier.this.mesh.chunkSetAngles("Z_Pencilrot1", 0.0F, 11459.16F * CockpitBloch210_Bombardier.this.pencilDisp, 0.0F);
            }
            CockpitBloch210_Bombardier.this.mesh.chunkSetAngles("Z_ANO1", 0.0F, CockpitBloch210_Bombardier.this.fm.AS.bNavLightsOn ? -50F : 0.0F, 0.0F);
            CockpitBloch210_Bombardier.this.mesh.chunkSetAngles("Z_CockpLight1", 0.0F, CockpitBloch210_Bombardier.this.cockpitLightControl ? -50F : 0.0F, 0.0F);
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
        CmdEnv.top().exec("fov 45.0");
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
        if (!this.isFocused() || (this.isToggleAim() == flag)) {
            return;
        }
        if (flag) {
            this.enter();
        } else {
            this.leave();
        }
    }

    public CockpitBloch210_Bombardier() {
        super("3DO/Cockpit/Bloch210-Bombardier/hier.him", "he111");
        this.pencilDisp = 0.0F;
        this.curAlt = 300F;
        this.curSpd = 50F;
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
        this.cockpitNightMats = (new String[] { "BombGauges", "Gauge03" });
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

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("HullDamage1", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("HullDamage2", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("HullDamage3", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("HullDamage4", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", false);
            this.mesh.chunkVisible("XGlassDamage2", false);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", false);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bEntered) {
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", true);
            this.mesh.chunkVisible("zScaleCurve", true);
            this.mesh.chunkVisible("zScaleM", true);
            this.mesh.chunkVisible("zScaleKM", true);
            this.mesh.chunkVisible("zScaleKMH", true);
        } else {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zScaleCurve", false);
            this.mesh.chunkVisible("zScaleM", false);
            this.mesh.chunkVisible("zScaleKM", false);
            this.mesh.chunkVisible("zScaleKMH", false);
        }
    }

    private float   pencilDisp;
    private float   curAlt;
    private float   curSpd;
    private float   saveFov;
    private float   aAim;
    private float   tAim;
    private boolean bEntered;

    static {
        Property.set(CockpitBloch210_Bombardier.class, "astatePilotIndx", 3);
    }

}
