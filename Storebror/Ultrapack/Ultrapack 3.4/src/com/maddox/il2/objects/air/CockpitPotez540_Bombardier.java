package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
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

public class CockpitPotez540_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitPotez540_Bombardier.this.setTmp = CockpitPotez540_Bombardier.this.setOld;
            CockpitPotez540_Bombardier.this.setOld = CockpitPotez540_Bombardier.this.setNew;
            CockpitPotez540_Bombardier.this.setNew = CockpitPotez540_Bombardier.this.setTmp;
            CockpitPotez540_Bombardier.this.setNew.azimuth.setDeg(CockpitPotez540_Bombardier.this.setOld.azimuth.getDeg(1.0F), 90F + CockpitPotez540_Bombardier.this.fm.Or.azimut());
            float f = ((Potez540) CockpitPotez540_Bombardier.this.aircraft()).fSightCurSpeed;
            float f1 = ((Potez540) CockpitPotez540_Bombardier.this.aircraft()).fSightCurAltitude;
            CockpitPotez540_Bombardier.this.curAlt = ((19F * CockpitPotez540_Bombardier.this.curAlt) + f1) / 20F;
            CockpitPotez540_Bombardier.this.curSpd = ((19F * CockpitPotez540_Bombardier.this.curSpd) + f) / 20F;
            CockpitPotez540_Bombardier.this.mesh.chunkSetAngles("zScaleKM", 0.04F * CockpitPotez540_Bombardier.this.curAlt, 0.0F, 0.0F);
            CockpitPotez540_Bombardier.this.mesh.chunkSetAngles("zScaleM", 0.36F * CockpitPotez540_Bombardier.this.curAlt, 0.0F, 0.0F);
            CockpitPotez540_Bombardier.this.mesh.chunkSetAngles("zScaleKMH", -0.8F * (CockpitPotez540_Bombardier.this.curSpd - 50F), 0.0F, 0.0F);
            float f2 = 0.5F * (float) Math.tan(Math.atan((83.333335876464844D * Math.sqrt((2.0F * CockpitPotez540_Bombardier.this.curAlt) / Atmosphere.g())) / CockpitPotez540_Bombardier.this.curAlt));
            float f3 = (float) Math.tan(Math.atan(((CockpitPotez540_Bombardier.this.curSpd / 3.6F) * Math.sqrt((2.0F * CockpitPotez540_Bombardier.this.curAlt) / Atmosphere.g())) / CockpitPotez540_Bombardier.this.curAlt));
            Cockpit.xyz[0] = -0.0005F * CockpitPotez540_Bombardier.this.curAlt;
            Cockpit.xyz[1] = -1F * (f2 - f3);
            CockpitPotez540_Bombardier.this.mesh.chunkSetLocate("zScaleCurve", Cockpit.xyz, Cockpit.ypr);
            if (Math.abs(CockpitPotez540_Bombardier.this.fm.Or.getKren()) < 30D) {
                CockpitPotez540_Bombardier.this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -CockpitPotez540_Bombardier.this.fm.Or.getYaw(), 0.0F);
            }
            if (Math.abs(CockpitPotez540_Bombardier.this.fm.Or.getKren()) > 3.5D) {
                CockpitPotez540_Bombardier.this.pencilDisp -= 0.0004F * CockpitPotez540_Bombardier.this.fm.Or.getKren();
                if (CockpitPotez540_Bombardier.this.pencilDisp > 0.1725F) {
                    CockpitPotez540_Bombardier.this.pencilDisp = 0.1725F;
                }
                if (CockpitPotez540_Bombardier.this.pencilDisp < -0.2529F) {
                    CockpitPotez540_Bombardier.this.pencilDisp = -0.2529F;
                }
                Cockpit.xyz[0] = 0.0F;
                Cockpit.xyz[1] = CockpitPotez540_Bombardier.this.pencilDisp;
                CockpitPotez540_Bombardier.this.mesh.chunkSetLocate("Z_Pencil1", Cockpit.xyz, Cockpit.ypr);
                CockpitPotez540_Bombardier.this.mesh.chunkSetAngles("Z_Pencilrot1", 0.0F, 11459.16F * CockpitPotez540_Bombardier.this.pencilDisp, 0.0F);
            }
            CockpitPotez540_Bombardier.this.mesh.chunkSetAngles("Z_ANO1", 0.0F, CockpitPotez540_Bombardier.this.fm.AS.bNavLightsOn ? -50F : 0.0F, 0.0F);
            CockpitPotez540_Bombardier.this.mesh.chunkSetAngles("Z_CockpLight1", 0.0F, CockpitPotez540_Bombardier.this.cockpitLightControl ? -50F : 0.0F, 0.0F);
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        AnglesFork azimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
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

    public CockpitPotez540_Bombardier() {
        super("3DO/Cockpit/Potez540-Bombardier/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
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
        this.mesh.chunkSetAngles("VionHA83_Card", 0.0F, 90F - this.setNew.azimuth.getDeg(f), 0.0F);
        this.resetYPRmodifier();
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

    private float     pencilDisp;
    private float     curAlt;
    private float     curSpd;
    private float     saveFov;
    private float     aAim;
    private float     tAim;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private boolean   bEntered;

    static {
        Property.set(CockpitPotez540_Bombardier.class, "astatePilotIndx", 3);
    }

}
