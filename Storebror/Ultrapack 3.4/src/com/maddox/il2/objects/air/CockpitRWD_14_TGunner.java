package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitRWD_14_TGunner extends CockpitGunner {
    private class Variables {

        float azimuth;

        private Variables() {
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitRWD_14_TGunner.this.bNeedSetUp) {
                CockpitRWD_14_TGunner.this.reflectPlaneMats();
                CockpitRWD_14_TGunner.this.bNeedSetUp = false;
            }
            if (RWD_14.bChangedPit) {
                CockpitRWD_14_TGunner.this.reflectPlaneToModel();
                RWD_14.bChangedPit = false;
            }
            CockpitRWD_14_TGunner.this.setTmp = CockpitRWD_14_TGunner.this.setOld;
            CockpitRWD_14_TGunner.this.setOld = CockpitRWD_14_TGunner.this.setNew;
            CockpitRWD_14_TGunner.this.setNew = CockpitRWD_14_TGunner.this.setTmp;
            if (Math.abs(CockpitRWD_14_TGunner.this.fm.Or.getKren()) < 30F) {
                CockpitRWD_14_TGunner.this.setNew.azimuth = ((35F * CockpitRWD_14_TGunner.this.setOld.azimuth) + CockpitRWD_14_TGunner.this.fm.Or.azimut()) / 36F;
            }
            if ((CockpitRWD_14_TGunner.this.setOld.azimuth > 270F) && (CockpitRWD_14_TGunner.this.setNew.azimuth < 90F)) {
                CockpitRWD_14_TGunner.this.setOld.azimuth -= 360F;
            }
            if ((CockpitRWD_14_TGunner.this.setOld.azimuth < 90F) && (CockpitRWD_14_TGunner.this.setNew.azimuth > 270F)) {
                CockpitRWD_14_TGunner.this.setOld.azimuth += 360F;
            }
            return true;
        }

        Interpolater() {
        }
    }

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
        if (f < -90F) {
            f = -90F;
        }
        if (f > 90F) {
            f = 90F;
        }
        if (f1 > 45F) {
            f1 = 45F;
        }
        if (f1 < -30F) {
            f1 = -30F;
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

    public CockpitRWD_14_TGunner() {
        super("3DO/Cockpit/RWD-14_TGun/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.hook1 = null;
        this.bNeedSetUp = true;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
        this.mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
        this.mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private boolean   bNeedSetUp;
    private Hook      hook1;

    static {
        Property.set(CockpitRWD_14_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitRWD_14_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitRWD_14_TGunner.class, "astatePilotIndx", 1);
    }

}
