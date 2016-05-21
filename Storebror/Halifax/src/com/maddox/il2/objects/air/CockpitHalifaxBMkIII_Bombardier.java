package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CLASS;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.BaseGameVersion;

public class CockpitHalifaxBMkIII_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = ((HalifaxBMkIII) CockpitHalifaxBMkIII_Bombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = ((HalifaxBMkIII) CockpitHalifaxBMkIII_Bombardier.this.aircraft()).fSightCurSideslip;
            CockpitHalifaxBMkIII_Bombardier.this.mesh.chunkSetAngles("BlackBox", 0.0F, -f1, f);
            if (CockpitHalifaxBMkIII_Bombardier.this.bEntered) {
                if (bEntered) {
                    if (BaseGameVersion.is411orLater()) {
                        HookPilot.current.setInstantOrient(aAim + 10F * f1, tAim + f, 0.0F);
                    } else {
                        HookPilot.current.setSimpleAimOrient(aAim + f1, tAim + f, 0.0F);
                    }
                }
//                HookPilot hookpilot = HookPilot.current;
//                hookpilot.setSimpleAimOrient(CockpitHalifaxBMkIII_Bombardier.this.aAim + f1, CockpitHalifaxBMkIII_Bombardier.this.tAim + f, 0.0F);
            }
            return true;
        }

        Interpolater() {}
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            if (BaseGameVersion.is411orLater()) {
                Point3d point3d = new Point3d();
                point3d.set(0.15D, 0.0D, -0.1D);
                hookpilot.setTubeSight(point3d);
            }
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
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter() {
        if (!BaseGameVersion.is411orLater())
            return;
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(-5F, -33F, 0.0F);
        enteringAim = true;
    }

    private void enter411andLater() {
        saveFov = Main3D.FOVX;
        HookPilot hookpilot = HookPilot.current;
        CmdEnv.top().exec("fov 23.913");
        if (hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.setInstantOrient(aAim, tAim, 0.0F);
        if (BaseGameVersion.is412orLater())
            Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
        else
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        hookpilot.setSimpleUse(true);
        doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void enter410andEarlier() {
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void enter() {
        if (BaseGameVersion.is411orLater())
            enter411andLater();
        else
            enter410andEarlier();
    }

    private void leave411andLater() {
        HookPilot hookpilot = HookPilot.current;
        if (enteringAim) {
            hookpilot.doAim(false);
            hookpilot.setInstantOrient(-5F, -33F, 0.0F);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!bEntered)
            return;
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
        CmdEnv.top().exec("fov " + saveFov);
        hookpilot.doAim(false);
        hookpilot.setInstantOrient(-5F, -33F, 0.0F);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        hookpilot.setSimpleUse(false);
        doSetSimpleUse(false);
        boolean flag = HotKeyEnv.isEnabled("aircraftView");
        HotKeyEnv.enable("PanView", flag);
        HotKeyEnv.enable("SnapView", flag);
        bEntered = false;
    }

    private void leave410andEarlier() {
        if (!bEntered)
            return;
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
        CmdEnv.top().exec("fov " + saveFov);
        HookPilot hookpilot1 = HookPilot.current;
        hookpilot1.doAim(false);
        hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        hookpilot1.setSimpleUse(false);
        boolean flag = HotKeyEnv.isEnabled("aircraftView");
        HotKeyEnv.enable("PanView", flag);
        HotKeyEnv.enable("SnapView", flag);
        bEntered = false;
    }

    private void leave() {
        if (BaseGameVersion.is411orLater())
            leave411andLater();
        else
            leave410andEarlier();
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
            if (BaseGameVersion.is411orLater())
                prepareToEnter();
            else
                enter();
        else
            this.leave();
    }

    public CockpitHalifaxBMkIII_Bombardier() {
        super("3DO/Cockpit/Halifax-Bombardier/hier.him", "he111");
//        super("3DO/Cockpit/A-20C-Bombardier/BombardierHalifaxBMkIII.him", "he111");
        enteringAim = false;
        this.w = new Vector3f();
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
        this.cockpitNightMats = (new String[] { "4_gauges" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl)
            this.setNightMats(true);
        else
            this.setNightMats(false);
    }

    public void reflectWorldToInstruments(float f) {
        if (BaseGameVersion.is411orLater()) {
            if (enteringAim) {
                HookPilot hookpilot = HookPilot.current;
                if (hookpilot.isAimReached()) {
                    enteringAim = false;
                    enter();
                } else if (!hookpilot.isAim())
                    enteringAim = false;
            }
        }
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 804.6721F, 0.0F, 10F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, this.fm.Or.getAzimut(), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.fm.getAOS(), -8F, 8F, -12F, 12F), 0.0F);
        if (this.bEntered) {
            this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((HalifaxBMkIII) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), angleScale), 0.0F, 0.0F);
            boolean flag = ((HalifaxBMkIII) this.aircraft()).fSightCurReadyness > 0.93F;
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
        this.mesh.chunkSetAngles("Turret1B", this.fm.turret[0].tu[0], this.fm.turret[0].tu[1], 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0)
            this.mesh.chunkVisible("XGlassDamage1", true);
        if ((this.fm.AS.astateCockpitState & 8) != 0)
            this.mesh.chunkVisible("XGlassDamage2", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0)
            this.mesh.chunkVisible("XGlassDamage2", true);
        if ((this.fm.AS.astateCockpitState & 2) != 0)
            this.mesh.chunkVisible("XGlassDamage3", true);
        if ((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("XGlassDamage4", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("XGlassDamage4", true);
    }

    public Vector3f            w;
    private static final float angleScale[]       = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private static final float speedometerScale[] = { 0.0F, 17.5F, 82F, 143.5F, 205F, 226.5F, 248.5F, 270F, 292F, 315F, 338.5F };
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;
    private boolean enteringAim;

    static {
        Property.set(CLASS.THIS(), "astatePilotIndx", 0);
    }
}
