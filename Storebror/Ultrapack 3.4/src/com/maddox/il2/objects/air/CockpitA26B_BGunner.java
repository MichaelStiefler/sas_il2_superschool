package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;

public class CockpitA26B_BGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (!super.doFocusEnter()) {
            return false;
        }
        this.saveFov = Main3D.FOVX;
        if (this.aiTurret().bIsOperable) {
            CmdEnv.top().exec("fov 67.5");
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        }
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
        this.aircraft().FM.turret[1].bIsAIControlled = true;
        this.aircraft().FM.turret[0].bIsAIControlled = this.onAuto;
        return true;
    }

    protected void doFocusLeave() {
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
        CmdEnv.top().exec("fov " + this.saveFov);
        boolean flag = HotKeyEnv.isEnabled("aircraftView");
        HotKeyEnv.enable("PanView", flag);
        HotKeyEnv.enable("SnapView", flag);
        this.bEntered = false;
        this.onAuto = this.aircraft().FM.turret[0].bIsAIControlled;
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
        this.mesh.chunkSetAngles("Turret1B", 180F, -orient.getTangage(), 180F);
        this.mesh.chunkSetAngles("Body", 180F, 0.0F, 180F);
    }

    public void clipAnglesGun(Orient paramOrient) {
        if (!this.isRealMode()) {
            return;
        }
        if (!this.aiTurret().bIsOperable) {
            paramOrient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f1 = paramOrient.getYaw();
        float f2 = paramOrient.getTangage();
        if (f1 < -66F) {
            f1 = -66F;
        }
        if (f1 > 66F) {
            f1 = 66F;
        }
        if (f2 < -70F) {
            f2 = -70F;
        }
        float fClip = (Math.abs(f1) / 66F) * 2.5F;
        if (f2 > fClip) {
            f2 = fClip;
        }
        paramOrient.setYPR(f1, f2, 0.0F);
        paramOrient.wrap();
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) {
                    this.hook1 = new HookNamed(this.aircraft(), "_MGUN15");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN15");
                if (this.hook2 == null) {
                    this.hook2 = new HookNamed(this.aircraft(), "_MGUN16");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN16");
            }
        }
        if (!this.bEntered) {
            return;
        }
        float af[] = { 0.0F, 0.0F, 0.0F };
        float af1[] = { 0.0F, 0.0F, 0.0F };
        if (!this.aiTurret().bIsOperable) {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Peribox", false);
            af[1] = 6.9F;
            af[2] = 1.75F;
            af1[0] = 180F;
            af1[1] = 0.0F;
            af1[2] = 180F;
        } else {
            CmdEnv.top().exec("fov 67.5");
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            this.mesh.chunkVisible("Z_Z_RETICLE", true);
            this.mesh.chunkVisible("Peribox", true);
            af1[0] = 180F;
            af1[1] = 0.0F;
            af1[2] = 180F;
        }
        this.mesh.chunkSetLocate("Body", af, af1);
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            } else {
                this.bGunFire = flag;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
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

    public CockpitA26B_BGunner() {
        super("3DO/Cockpit/A-20G-TGun/BottomGunnerA26_B.him", "bf109");
        this.bEntered = false;
        this.hook1 = null;
        this.hook2 = null;
        this.onAuto = true;
    }

    private float   saveFov;
    private boolean bEntered;
    private Hook    hook1;
    private Hook    hook2;
    private boolean onAuto;

    static {
        Class localClass = CockpitA26B_BGunner.class;
        Property.set(localClass, "aiTuretNum", 0);
        Property.set(localClass, "weaponControlNum", 10);
        Property.set(localClass, "astatePilotIndx", 2);
    }
}
