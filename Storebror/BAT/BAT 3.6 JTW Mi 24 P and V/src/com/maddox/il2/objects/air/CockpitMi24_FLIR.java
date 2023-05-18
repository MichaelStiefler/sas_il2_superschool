package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;

public class CockpitMi24_FLIR extends CockpitGunner {

    public void moveGun(Orient orient) {
        Orient orient1 = this.hookGunner().getGunMove();
        float f = orient1.getYaw();
        float f1 = orient1.getTangage();
        Mi24X.LaserOr = this.hookGunner().getGunMove();
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", -f, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) {
            if (!this.aiTurret().bIsOperable) {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if (f < -60F) {
                    f = -60F;
                }
                if (f > 60F) {
                    f = 60F;
                }
                if (f1 > 20F) {
                    f1 = 20F;
                }
                if (f1 < -80F) {
                    f1 = -80F;
                }
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
        }
    }

    protected void interpTick() {
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
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
            ((Mi24X) this.aircraft()).FLIR = false;
            this.leave();
            super.doFocusLeave();
            return;
        }
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        CmdEnv.top().exec("fov 33.3");
        this.bEntered = true;
    }

    private void leave() {
        if (!this.bEntered) {
            return;
        } else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            return;
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
    }

    public CockpitMi24_FLIR() {
        super("3DO/Cockpit/A-20G-TGun/TGunnerMi24FLIR.him", "he111_gunner");
    }

    public static Orient tmpOr = new Orient();
    private boolean      bEntered;
    private float        saveFov;
    public Actor         victim;

    static {
        Class class1 = CockpitMi24_FLIR.class;
        Property.set(class1, "aiTuretNum", 0);
        Property.set(class1, "weaponControlNum", 10);
        Property.set(class1, "astatePilotIndx", 0);
    }
}
