package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitYB40_AGunner extends CockpitGunner {

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        this.mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretC", 0.0F, this.cvt(orient.getYaw(), -38F, 38F, -15F, 15F), 0.0F);
        this.mesh.chunkSetAngles("TurretD", 0.0F, this.cvt(orient.getTangage(), -43F, 43F, -10F, 10F), 0.0F);
        this.mesh.chunkSetAngles("TurretE", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretF", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretG", -this.cvt(orient.getYaw(), -33F, 33F, -33F, 33F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretH", 0.0F, this.cvt(orient.getTangage(), -10F, 32F, -10F, 32F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(Math.max(Math.abs(orient.getYaw()), Math.abs(orient.getTangage())), 0.0F, 20F, 0.0F, 0.3F);
        this.mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) {
            if (!this.aiTurret().bIsOperable) {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if (f < -38F) {
                    f = -38F;
                }
                if (f > 38F) {
                    f = 38F;
                }
                if (f1 > 43F) {
                    f1 = 43F;
                }
                if (f1 < -41F) {
                    f1 = -41F;
                }
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) {
                    this.hook1 = new HookNamed(this.aircraft(), "_MGUN11");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN11");
                if (this.hook2 == null) {
                    this.hook2 = new HookNamed(this.aircraft(), "_MGUN12");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN12");
            }
        }
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

    public CockpitYB40_AGunner() {
        super("3DO/Cockpit/YB-40-AGun/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.hook1 = null;
        this.hook2 = null;
    }

    private boolean bNeedSetUp;
    private Hook    hook1;
    private Hook    hook2;

    static {
        Property.set(CockpitYB40_AGunner.class, "aiTuretNum", 7);
        Property.set(CockpitYB40_AGunner.class, "weaponControlNum", 17);
        Property.set(CockpitYB40_AGunner.class, "astatePilotIndx", 8);
    }
}
