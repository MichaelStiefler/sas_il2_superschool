package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;

public class CockpitPE2_402_RGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.bEntered = true;
            this.saveFov = Main3D.FOVX;
            CmdEnv.top().exec("fov 30");
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            HotKeyEnv.enable("PanView", false);
            HotKeyEnv.enable("SnapView", false);
            this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret4B_D0", false);
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
            this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret4B_D0", true);
        }
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
        else {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            if (f < -30F) f = -30F;
            if (f > 30F) f = 30F;
            if (f1 > 30F) f1 = 30F;
            if (f1 < -30F) f1 = -30F;
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
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN04");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN04");
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

    public CockpitPE2_402_RGunner() {
        this("3DO/Cockpit/Pe-2series402-RGun/hier.him");
        this.bNeedSetUp = true;
        this.bEntered = false;
        this.hook1 = null;
    }

    public CockpitPE2_402_RGunner(String s) {
        super(s, "he111_gunner");
        this.bNeedSetUp = true;
        this.bEntered = false;
        this.hook1 = null;
    }

    private boolean bNeedSetUp;
    private boolean bEntered;
    private float   saveFov;
    private Hook    hook1;

    static {
        Property.set(CockpitPE2_402_RGunner.class, "aiTuretNum", 3);
        Property.set(CockpitPE2_402_RGunner.class, "weaponControlNum", 13);
        Property.set(CockpitPE2_402_RGunner.class, "astatePilotIndx", 2);
        Property.set(CockpitPE2_402_RGunner.class, "normZN", 1.25F);
    }
}
