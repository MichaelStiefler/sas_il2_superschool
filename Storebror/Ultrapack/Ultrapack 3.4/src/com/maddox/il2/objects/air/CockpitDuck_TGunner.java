package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitDuck_TGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", this.aircraft().hierMesh().isChunkVisible("Turret1A_D0"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, 0.0F, -orient.getTangage());
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) {
            return;
        }
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -31F) {
            f = -31F;
        }
        if (f > 31F) {
            f = 31F;
        }
        if (f1 > 52F) {
            f1 = 52F;
        }
        if (f1 < -10F) {
            f1 = -10F;
        }
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) {
                    this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) {
            return;
        }
        if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
            this.bGunFire = false;
        } else {
            this.bGunFire = flag;
        }
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitDuck_TGunner() {
        super("3DO/Cockpit/Duck-TGun/hier.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.hook1 = null;
    }

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
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
    }

    private boolean bNeedSetUp;
    private Hook    hook1;

    static {
        Property.set(CockpitDuck_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitDuck_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitDuck_TGunner.class, "astatePilotIndx", 1);
    }
}
