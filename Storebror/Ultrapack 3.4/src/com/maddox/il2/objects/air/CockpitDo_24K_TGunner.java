package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitDo_24K_TGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("Turret2A", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("Turret2B", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -90F) f = -90F;
        if (f > 90F) f = 90F;
        if (f1 > this.cvt(Math.abs(f), 0.0F, 50F, 40F, 25F)) f1 = this.cvt(Math.abs(f), 0.0F, 50F, 40F, 25F);
        if (f1 < this.cvt(Math.abs(f), 0.0F, 50F, -10F, -3.5F)) f1 = this.cvt(Math.abs(f), 0.0F, 50F, -10F, -3.5F);
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.iCocking > 0) this.iCocking = 0;
            else this.iCocking = 1;
        } else this.iCocking = 0;
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -0.07F * this.iCocking;
        Cockpit.ypr[1] = 0.0F;
        this.mesh.chunkSetLocate("Turret3C", Cockpit.xyz, Cockpit.ypr);
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitDo_24K_TGunner() {
        super("3DO/Cockpit/Do_24K_TGun/hier.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.iCocking = 0;
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
    }

    public void reflectCockpitState() {
    }

    private boolean bNeedSetUp;
    private int     iCocking;

    static {
        Property.set(CockpitDo_24K_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitDo_24K_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitDo_24K_TGunner.class, "astatePilotIndx", 2);
    }
}
