package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitBarracuda_TGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", this.aircraft().hierMesh().isChunkVisible("Turret1A_D0"));
        super.doFocusLeave();
    }

    public void setRealMode(boolean flag) {
        super.setRealMode(flag);
        if (flag) ((Barracuda) this.aircraft()).activateGunner(1);
        ((Barracuda) this.aircraft()).setGunnerRealMode(flag);
    }

    public void moveGun(Orient orient) {
        if (((Barracuda) this.aircraft()).getTopGunnerPosition() < 1.0F) return;
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret_Base", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("MGun", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("Turret_Base2", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("MGun2", 0.0F, this.cvt(orient.getTangage(), -20F, 45F, -20F, 45F), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        if (f < -135F) f = -135F;
        if (f > 135F) f = 135F;
        float f1 = orient.getTangage();
        if (f1 > 45F) f1 = 45F;
        if (f1 < -69F) f1 = -69F;
        float f2;
        for (f2 = Math.abs(f); f2 > 180F; f2 -= 180F)
            ;
        if (f1 < -this.floatindex(this.cvt(f2, 0.0F, 180F, 0.0F, 36F), angles)) f1 = -this.floatindex(this.cvt(f2, 0.0F, 180F, 0.0F, 36F), angles);
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (this.bGunFire) this.mesh.chunkSetAngles("Trigger", 0.0F, 17.5F, 0.0F);
        else this.mesh.chunkSetAngles("Trigger", 0.0F, 0.0F, 0.0F);
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
            if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN02");
            this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN02");
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitBarracuda_TGunner() {
        super("3DO/Cockpit/SBD-3-TGun/hier_barracuda.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
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
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private static final float angles[] = { 4F, 5.5F, 5.5F, 7F, 10.5F, 15.5F, 24F, 25F, 25F, 25F, 25F, 25F, 25F, 25F, 25F, 25F, 25F, 25F, 15F, -10F, -10F, -20F, -30F, -40F, -50F, -60F, -70F, -77F, -77F, -77F, -77F, -77F, -77F, -77F, -77F, -77F, -77F };

    private Hook               hook1;
    private Hook               hook2;
    private boolean            bNeedSetUp;

    static {
        Property.set(CockpitBarracuda_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitBarracuda_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitBarracuda_TGunner.class, "astatePilotIndx", 2);
    }
}
