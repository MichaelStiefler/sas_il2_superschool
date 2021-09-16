package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitFokkerTVIII_TGunner extends CockpitGunner {

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("Turret1A", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Turret1C", 0.0F, this.cvt(f, -45F, 45F, -45F, 45F), 0.0F);
        this.mesh.chunkSetAngles("Turret1D", 0.0F, f1, 0.0F);
        this.resetYPRmodifier();
        if (f < 0.0F) {
            Cockpit.xyz[0] = this.cvt(f, -45F, 0.0F, 0.25F, 0.0F);
        } else {
            Cockpit.xyz[0] = this.cvt(f, 0.0F, 45F, 0.0F, 0.25F);
        }
        this.mesh.chunkSetLocate("Turret1E", Cockpit.xyz, Cockpit.ypr);
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
        if (f < -75F) {
            f = -75F;
        }
        if (f > 75F) {
            f = 75F;
        }
        if (f1 > 45F) {
            f1 = 45F;
        }
        if (f1 < -5F) {
            f1 = -5F;
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
                this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
            }
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
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

    public void reflectCockpitState() {
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public CockpitFokkerTVIII_TGunner() {
        super("3DO/Cockpit/FokkerTVIII-TGun/hier.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.hook1 = null;
    }

    private boolean bNeedSetUp;
    private Hook    hook1;

    static {
        Property.set(CockpitFokkerTVIII_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitFokkerTVIII_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitFokkerTVIII_TGunner.class, "astatePilotIndx", 1);
    }
}
