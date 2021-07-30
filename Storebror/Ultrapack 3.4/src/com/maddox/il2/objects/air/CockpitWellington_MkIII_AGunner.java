package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitWellington_MkIII_AGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("CF_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.mesh.chunkVisible("Turret", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (Math.abs(f) < 2.2F) if (f1 > -2F && f1 < 0.0F) f1 = -2F;
        else if (f1 < 2.3F && f1 >= 0.0F) f1 = 2.3F;
        this.mesh.chunkSetAngles("TurretA", 0.0F, -f, 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("TurretC", 0.0F, this.cvt(f, -38F, 38F, -15F, 15F), 0.0F);
        this.mesh.chunkSetAngles("TurretE", -f, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretF", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("TurretG", -this.cvt(f, -33F, 33F, -33F, 33F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretH", 0.0F, this.cvt(f1, -10F, 32F, -10F, 32F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(Math.max(Math.abs(f), Math.abs(f1)), 0.0F, 20F, 0.0F, 0.3F);
        this.mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 2.3F, 0.0F);
        else {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            if (f < -38F) f = -38F;
            if (f > 38F) f = 38F;
            if (f1 > 38F) f1 = 38F;
            if (f1 < -41F) f1 = -41F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN04");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN04");
                if (this.hook3 == null) this.hook3 = new HookNamed(this.aircraft(), "_MGUN05");
                this.doHitMasterAircraft(this.aircraft(), this.hook3, "_MGUN05");
                if (this.hook4 == null) this.hook4 = new HookNamed(this.aircraft(), "_MGUN06");
                this.doHitMasterAircraft(this.aircraft(), this.hook4, "_MGUN06");
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
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public CockpitWellington_MkIII_AGunner() {
        super("3DO/Cockpit/BoultonPaulWellington/WellingtonMkIIIAGunner.him", "bf109");
        this.hook1 = null;
        this.hook2 = null;
        this.hook3 = null;
        this.hook4 = null;
    }

    private Hook hook1;
    private Hook hook2;
    private Hook hook3;
    private Hook hook4;

    static {
        Property.set(CockpitWellington_MkIII_AGunner.class, "aiTuretNum", 1);
        Property.set(CockpitWellington_MkIII_AGunner.class, "weaponControlNum", 11);
        Property.set(CockpitWellington_MkIII_AGunner.class, "astatePilotIndx", 3);
    }
}
