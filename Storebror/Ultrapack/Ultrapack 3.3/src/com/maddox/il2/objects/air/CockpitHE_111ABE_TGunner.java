package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111ABE_TGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, 0.0F, -orient.getTangage());
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -45F) f = -45F;
        if (f > 45F) f = 42F;
        if (f1 > 45F) f1 = 45F;
        if (f1 < -3F) f1 = -3F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
            if (this.iCocking > 0) this.iCocking = 0;
            else this.iCocking = 1;
        } else this.iCocking = 0;
        this.mesh.chunkSetAngles("CockingLever", -0.75F * this.iCocking, 0.0F, 0.0F);
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public void reflectCockpitState() {
        if (this.fm.AS.astateCockpitState != 0) this.mesh.chunkVisible("Holes_D1", true);
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

    public CockpitHE_111ABE_TGunner() {
        super("3DO/Cockpit/He-111ABE-TGun/hier.him", "he111_gunner");
        this.hook1 = null;
        this.iCocking = 0;
    }

    private Hook hook1;
    private int  iCocking;

    static {
        Property.set(CockpitHE_111ABE_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitHE_111ABE_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitHE_111ABE_TGunner.class, "astatePilotIndx", 2);
    }
}
