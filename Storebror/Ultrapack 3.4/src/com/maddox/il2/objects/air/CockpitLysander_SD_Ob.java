package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitLysander_SD_Ob extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("TurretA", -0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
        this.mesh.chunkSetLocate("IBone", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("CasingsTube", 0.0F, -0.5F * orient.getTangage() + 7.5F, 0.33F * orient.getYaw());
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
        else {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            if (f < -55F) f = -55F;
            if (f > 55F) f = 55F;
            if (f1 > 45F) f1 = 45F;
            if (f1 < -45F) f1 = -45F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[10] = this.bGunFire;
            if (this.bGunFire) {
                HookNamed hook1 = null;
                if (hook1 == null) hook1 = new HookNamed(this.aircraft(), "_MGUN03");
                this.doHitMasterAircraft(this.aircraft(), hook1, "_MGUN03");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            else this.bGunFire = flag;
            this.fm.CT.WeaponControl[10] = this.bGunFire;
        }
    }

    public CockpitLysander_SD_Ob() {
        super("3DO/Cockpit/Lysander_SD_Ob/hier.him", "bf109");
        this.bNeedSetUp = true;
    }

    public void reflectWorldToInstruments1(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("CF_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("CF_D0", true);
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private boolean bNeedSetUp;
    static {
        Property.set(CockpitLysander_SD_Ob.class, "aiTuretNum", 0);
        Property.set(CockpitLysander_SD_Ob.class, "weaponControlNum", 10);
        Property.set(CockpitLysander_SD_Ob.class, "astatePilotIndx", 1);
    }
}
