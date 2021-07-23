
package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitF1M2_TGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
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
        if (f < -28F) {
            f = -28F;
        }
        if (f > 28F) {
            f = 28F;
        }
        if (f1 > 45F) {
            f1 = 45F;
        }
        if (f1 < -3F) {
            f1 = -3F;
        }
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) {
            return;
        }
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

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret1A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret1C_D0", true);
        super.doFocusLeave();
    }

    public CockpitF1M2_TGunner() {
        super("3DO/Cockpit/F1M-TG/hier.him", "he111_gunner");
        this.hook1 = null;
        this.bNeedSetUp = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("CF_D3", hiermesh.isChunkVisible("CF_D3"));
        this.mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
        this.mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
        this.mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
        this.mesh.chunkVisible("Tail1_D3", hiermesh.isChunkVisible("Tail1_D3"));
        this.mesh.chunkVisible("Keel1_D0", hiermesh.isChunkVisible("Keel1_D0"));
        this.mesh.chunkVisible("Keel1_D1", hiermesh.isChunkVisible("Keel1_D1"));
        this.mesh.chunkVisible("Keel1_D2", hiermesh.isChunkVisible("Keel1_D2"));
    }

    private Hook    hook1;
    private boolean bNeedSetUp;

    static {
        Property.set(com.maddox.il2.objects.air.CockpitF1M2_TGunner.class, "aiTuretNum", 0);
        Property.set(com.maddox.il2.objects.air.CockpitF1M2_TGunner.class, "weaponControlNum", 10);
        Property.set(com.maddox.il2.objects.air.CockpitF1M2_TGunner.class, "astatePilotIndx", 1);
    }
}
