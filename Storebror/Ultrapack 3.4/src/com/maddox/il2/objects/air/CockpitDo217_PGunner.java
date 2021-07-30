package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.HookNamed;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Property;

public class CockpitDo217_PGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((Do217) this.fm.actor).bPitUnfocused = false;
            this.saveFov = com.maddox.il2.game.Main3D.FOVX;
            CmdEnv.top().exec("fov 25.0");
            com.maddox.il2.game.Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (this.isFocused()) super.doFocusLeave();
        com.maddox.il2.game.Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
        CmdEnv.top().exec("fov " + this.saveFov);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    public void moveGun(com.maddox.il2.engine.Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("z_Turret6A", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("z_Turret6B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(com.maddox.il2.engine.Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f1 > 0.0F) f1 = 0.0F;
        if (f1 < 0.0F) f1 = 0.0F;
        if (f > 0.0F) f = 0.0F;
        if (f < 0.0F) f = 0.0F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN06");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN06");
            if (this.hook2 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN07");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN07");
            if (this.hook3 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN08");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN08");
            if (this.hook4 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN09");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN09");
            if (this.iCocking > 0) this.iCocking = 0;
            else this.iCocking = 1;
        } else this.iCocking = 0;
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    protected void reflectPlaneMats() {
    }

    public CockpitDo217_PGunner() {
        super("3DO/Cockpit/Do217k1/hierpGun.him", "he111_gunner");
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.hook1 = null;
        this.hook2 = null;
        this.hook3 = null;
        this.hook4 = null;
        this.iCocking = 0;
    }

    private float                      saveFov;
    public com.maddox.JGP.Vector3f     w;
    private boolean                    bNeedSetUp;
    private com.maddox.il2.engine.Hook hook1;
    private com.maddox.il2.engine.Hook hook2;
    private com.maddox.il2.engine.Hook hook3;
    private com.maddox.il2.engine.Hook hook4;
    private int                        iCocking;

    static {
        Property.set(CockpitDo217_PGunner.class, "aiTuretNum", 5);
        Property.set(CockpitDo217_PGunner.class, "weaponControlNum", 15);
        Property.set(CockpitDo217_PGunner.class, "astatePilotIndx", 1); // Patch Pack 107, change pilot index from "6"
                                                                        // to "1" because Do-217 has 4 crew members
                                                                        // only!
    }
}
