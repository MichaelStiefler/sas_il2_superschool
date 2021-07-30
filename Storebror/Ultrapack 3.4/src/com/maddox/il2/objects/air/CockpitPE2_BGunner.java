package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;

public class CockpitPE2_BGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.bEntered = true;
            this.saveFov = Main3D.FOVX;
            CmdEnv.top().exec("fov 31");
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            HotKeyEnv.enable("PanView", false);
            HotKeyEnv.enable("SnapView", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (this.bEntered) {
            this.bEntered = false;
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
        }
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, orient.getYaw(), -90F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, 0.0F, -orient.getTangage());
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
        else {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            if (f < -30F) f = -30F;
            if (f > 30F) f = 30F;
            if (f1 > 0.0F) f1 = 0.0F;
            if (f1 < -45F) f1 = -45F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void interpTick() {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            else this.bGunFire = flag;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    protected void reflectPlaneMats() {
    }

    public void reflectCockpitState() {
    }

    public CockpitPE2_BGunner() {
        super("3DO/Cockpit/Pe-2-BGun/hier.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.bEntered = false;
        this.hook1 = null;
    }

    private boolean bNeedSetUp;
    private boolean bEntered;
    private float   saveFov;
    private Hook    hook1;

    static {
        Property.set(CockpitPE2_BGunner.class, "aiTuretNum", 1);
        Property.set(CockpitPE2_BGunner.class, "weaponControlNum", 11);
        Property.set(CockpitPE2_BGunner.class, "astatePilotIndx", 2);
        Property.set(CockpitPE2_BGunner.class, "normZN", 0.9F);
    }
}
