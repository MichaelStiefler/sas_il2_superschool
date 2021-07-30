package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHalifaxBMkII_AGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretC", 0.0F, this.cvt(orient.getYaw(), -38F, 38F, -15F, 15F), 0.0F);
        this.mesh.chunkSetAngles("TurretE", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretF", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretG", -this.cvt(orient.getYaw(), -33F, 33F, -33F, 33F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretH", 0.0F, this.cvt(orient.getTangage(), -10F, 32F, -10F, 32F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(Math.max(Math.abs(orient.getYaw()), Math.abs(orient.getTangage())), 0.0F, 20F, 0.0F, 0.3F);
        this.mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
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
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN02");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN02");
                if (this.hook3 == null) this.hook3 = new HookNamed(this.aircraft(), "_MGUN09");
                this.doHitMasterAircraft(this.aircraft(), this.hook3, "_MGUN09");
                if (this.hook4 == null) this.hook4 = new HookNamed(this.aircraft(), "_MGUN10");
                this.doHitMasterAircraft(this.aircraft(), this.hook4, "_MGUN10");
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

    public CockpitHalifaxBMkII_AGunner() {
        super("3DO/Cockpit/HalifaxBMkII-AGun/hier.him", "bf109");
        this.setbNeedSetUp(true);
        this.hook1 = null;
        this.hook2 = null;
        this.hook3 = null;
        this.hook4 = null;
    }

    public boolean isbNeedSetUp() {
        return this.bNeedSetUp;
    }

    public void setbNeedSetUp(boolean bNeedSetUp) {
        this.bNeedSetUp = bNeedSetUp;
    }

    private boolean bNeedSetUp;
    private Hook    hook1;
    private Hook    hook2;
    private Hook    hook3;
    private Hook    hook4;

    static {
        Property.set(CockpitHalifaxBMkII_AGunner.class, "aiTuretNum", 0);
        Property.set(CockpitHalifaxBMkII_AGunner.class, "weaponControlNum", 10);
        Property.set(CockpitHalifaxBMkII_AGunner.class, "astatePilotIndx", 4);
    }
}
