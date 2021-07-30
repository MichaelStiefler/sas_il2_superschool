package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;

public class CockpitHE_177A3_HGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.bEntered = true;
            this.saveFov = Main3D.FOVX;
            if (Config.cur.windowsWideScreenFoV) CmdEnv.top().exec("fov 35");
            else CmdEnv.top().exec("fov 30");
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            HotKeyEnv.enable("PanView", false);
            HotKeyEnv.enable("SnapView", false);
            HierMesh hiermesh = this.aircraft().hierMesh();
            hiermesh.chunkVisible("Turret6B_D0", false);
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
            HierMesh hiermesh = this.aircraft().hierMesh();
            hiermesh.chunkVisible("Turret6B_D0", true);
        }
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Z_TurretA", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Z_TurretB", 0.0F, -orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
        else {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            if (f < -22.5F) f = -22.5F;
            if (f > 22.5F) f = 22.5F;
            if (f1 > 40F) f1 = 40F;
            if (f1 < -20F) f1 = -20F;
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

    public void reflectWorldToInstruments(float f) {
    }

    public CockpitHE_177A3_HGunner() {
        super("3DO/Cockpit/He-177A-3-HGun/hier.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.bEntered = false;
        this.normZN = 0.3F;
        this.gsZN = 0.3F;
    }

    private boolean bNeedSetUp;
    private boolean bEntered;
    private float   saveFov;

    static {
        Class class1 = CockpitHE_177A3_HGunner.class;
        Property.set(class1, "aiTuretNum", 5);
        Property.set(class1, "weaponControlNum", 15);
        Property.set(class1, "astatePilotIndx", 5);
        Property.set(class1, "normZN", 0.3F);
    }
}
