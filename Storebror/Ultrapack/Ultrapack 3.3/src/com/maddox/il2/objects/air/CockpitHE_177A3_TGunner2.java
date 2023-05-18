package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;

public class CockpitHE_177A3_TGunner2 extends CockpitGunner {

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
            hiermesh.chunkVisible("Turret5B_D0", false);
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
            hiermesh.chunkVisible("Turret5B_D0", true);
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
            float f11 = -10F;
            if (f < -177F || f > 175F) f11 = 5F;
            else if (f >= -177F && f < -172F) {
                float f3 = this.cvt(f, -177F, -172F, 5F, 1.0F);
                if (f1 < f3) f11 = f3;
            } else if (f > 170F && f <= 175F) {
                float f4 = this.cvt(f, 170F, 175F, 1.0F, 5F);
                if (f1 < f4) f11 = f4;
            } else if (f < -98.5F || f > 98.5F) f11 = 1.0F;
            else if (f >= -98.5F && f < -93.5F) {
                float f5 = this.cvt(f, -98.5F, -93.5F, 0.0F, -10F);
                if (f1 < f5) f11 = f5;
            } else if (f > 93.5F && f <= 98.5F) {
                float f6 = this.cvt(f, 93.5F, 98.5F, -10F, 0.0F);
                if (f1 < f6) f11 = f6;
            } else if (f >= -8F && f <= 8F) f11 = 0.0F;
            else if (f >= -32F && f < -8F) {
                float f7 = this.cvt(f, -32F, -8F, -4.5F, 0.0F);
                if (f1 < f7) f11 = f7;
            } else if (f > 8F && f <= 32F) {
                float f8 = this.cvt(f, 8F, 32F, 0.0F, -4.5F);
                if (f1 < f8) f11 = f8;
            } else if (f >= -35F && f < -32F) {
                float f9 = this.cvt(f, -35F, -32F, -10F, -4.5F);
                if (f1 < f9) f11 = f9;
            } else if (f > 32F && f <= 35F) {
                float f10 = this.cvt(f, 32F, 35F, -4.5F, -10F);
                if (f1 < f10) f11 = f10;
            }
            if (f < 2.5F && f > -2F && f1 < 21.5F) this.bDontShot = true;
            else this.bDontShot = false;
            if (f < -180F) f = -180F;
            if (f > 180F) f = 180F;
            if (f1 > 80F) f1 = 80F;
            else if (f1 < f11) f1 = f11;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void interpTick() {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.bDontShot) this.bGunFire = false;
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
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState() {
    }

    public void reflectWorldToInstruments(float f) {
    }

    public CockpitHE_177A3_TGunner2() {
        super("3DO/Cockpit/He-177A-3-TGun2/hier.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.bEntered = false;
        this.bDontShot = false;
        this.normZN = 0.5F;
        this.gsZN = 0.5F;
    }

    private boolean bNeedSetUp;
    private boolean bEntered;
    private float   saveFov;
    private boolean bDontShot;

    static {
        Class class1 = CockpitHE_177A3_TGunner2.class;
        Property.set(class1, "aiTuretNum", 4);
        Property.set(class1, "weaponControlNum", 14);
        Property.set(class1, "astatePilotIndx", 4);
        Property.set(class1, "normZN", 0.5F);
    }
}
