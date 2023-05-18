package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitMBR_2AM34_TGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret2A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret2A_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("zTurret1A", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -orient.getTangage());
        float f = orient.getTangage();
        float f1 = 0.0F;
        if (f > 45F) f1 = 45F - f;
        else if (f < -10F) f1 = -10F - f;
        this.mesh.chunkSetAngles("CameraRodB", 0.0F, 0.0F, -f1);
        this.mesh.chunkSetAngles("CameraRodC", 0.0F, 0.0F, f1);
    }

    public void clipAnglesGun(Orient orient) {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for (; f < -180F; f += 360F)
            ;
        for (; f > 180F; f -= 360F)
            ;
        for (; this.prevA0 < -180F; this.prevA0 += 360F)
            ;
        for (; this.prevA0 > 180F; this.prevA0 -= 360F)
            ;
        if (!this.isRealMode()) {
            this.prevA0 = f;
            return;
        }
        if (f < -120F && this.prevA0 > 120F) f += 360F;
        else if (f > 120F && this.prevA0 < -120F) this.prevA0 += 360F;
        float f3 = f - this.prevA0;
        float f13 = 0.001F * (Time.current() - this.prevTime);
        float f14 = Math.abs(f3 / f13);
        if (f14 > 120F) if (f > this.prevA0) f = this.prevA0 + 120F * f13;
        else if (f < this.prevA0) f = this.prevA0 - 120F * f13;
        this.prevTime = Time.current();
        this.bDontShot = false;
        f3 = 0.0F;
        if (f1 > 89.5F) f1 = 89.5F;
        if (f >= -1.43F && f <= 1.43F) {
            if (f1 < 30.8F) {
                this.bDontShot = true;
                if (f1 < -2F) f1 = -2F;
            } else this.bDontShot = false;
        } else if (f >= -2.5F && f <= 2.5F) {
            float f4 = this.cvt(Math.abs(f), 1.43F, 2.5F, 30.8F, -2F);
            this.MinDontShoot(f4, f1);
            if (f1 < -2F) f1 = -2F;
        } else if (f >= -2.9F && f <= 2.9F) {
            float f5 = this.cvt(Math.abs(f), 2.5F, 2.9F, -2F, -3.4F);
            if (f1 < f5) f1 = f5;
        } else if (f >= -15.2F && f <= 15.2F) {
            float f6 = this.cvt(Math.abs(f), 2.9F, 15.2F, -3.4F, -13.09F);
            if (f1 < f6) f1 = f6;
        } else if (f >= -65.38F && f <= 65.38F) {
            float f7 = this.cvt(Math.abs(f), 15.2F, 65.38F, -13.09F, -40F);
            if (f1 < f7) f1 = f7;
        } else if (f >= -100F && f <= 100F) {
            if (f1 < -40F) f1 = -40F;
        } else if (f >= -105F && f <= 105F) {
            float f8 = this.cvt(Math.abs(f), 100F, 105F, -40F, 2.0F);
            if (f1 < f8) f1 = f8;
        } else if (f >= -110.4F && f <= 110.4F) {
            float f9 = this.cvt(Math.abs(f), 105.7F, 110.4F, 2.0F, 3F);
            if (f1 < f9) f1 = f9;
        } else if (f >= -146.2F && f <= 146.2F) {
            if (f1 < 3F) f1 = 3F;
        } else {
            if (f1 < 3F) f1 = 3F;
            if (f < -146.2F) f = -146.2F;
            else if (f > 146.2F) f = 146.2F;
        }
        if (f >= -38.5F && f <= 38.5F && f1 > 8.5F && f1 < 16.5F && !this.bDontShot) if (f1 > 13.6F) {
            float f10 = this.cvt(Math.abs(f), 0.0F, 38.5F, 16.5F, 13.6F);
            this.MinDontShoot(f10, f1);
        } else if (Math.abs(f) < 32.31F) {
            float f11 = this.cvt(Math.abs(f), 0.0F, 32.31F, 9.5F, 8.5F);
            this.MaxDontShoot(f11, f1);
        } else {
            float f12 = this.cvt(Math.abs(f), 32.31F, 38.5F, 8.5F, 13.6F);
            this.MaxDontShoot(f12, f1);
        }
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        this.prevA0 = f;
    }

    private void MinDontShoot(float f, float f1) {
        if (f1 < f) this.bDontShot = true;
        else this.bDontShot = false;
    }

    private void MaxDontShoot(float f, float f1) {
        if (f1 > f) this.bDontShot = true;
        else this.bDontShot = false;
    }

    protected void interpTick() {
        if (this.bDontShot) this.bGunFire = false;
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        if (this.bDontShot) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.bNeedSetUp = false;
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
        }
        if (((MBR_2xyz) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((MBR_2xyz) this.aircraft()).bChangedPit = false;
        }
        if (this.Gun1 != null) {
            if (this.bGunFire && this.Gun1.haveBullets() && this.PatronsMat != null) {
                this.Patrons -= -0.35F * f;
                this.PatronsMat.setLayer(0);
                this.PatronsMat.set((byte) 11, this.Patrons);
            }
            if (World.cur().diffCur.Limited_Ammo && this.Gun1.countBullets() < 5) this.mesh.chunkVisible("Patrons", false);
        } else this.mesh.chunkVisible("Patrons", false);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
        this.mesh.materialReplace("Matt1D2o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2") || hiermesh.isChunkVisible("CF_D3"));
        this.mesh.chunkVisible("Tail1_D0_00", hiermesh.isChunkVisible("Tail1_D0"));
        this.mesh.chunkVisible("Tail1_D1_00", hiermesh.isChunkVisible("Tail1_D1"));
        this.mesh.chunkVisible("Tail1_D2_00", hiermesh.isChunkVisible("Tail1_D2") || hiermesh.isChunkVisible("Tail1_D3"));
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("xHullDm1", true);
        if ((this.fm.AS.astateCockpitState & 4) != 0) if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xHullDm3", true);
        else this.mesh.chunkVisible("xHullDm4", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xHullDm2", true);
        else this.mesh.chunkVisible("xHullDm5", true);
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) if (World.Rnd().nextFloat() < 0.75F) this.mesh.chunkVisible("xHullDm6", true);
        else this.mesh.chunkVisible("xHullDm7", true);
    }

    public CockpitMBR_2AM34_TGunner() {
        super("3DO/Cockpit/MBR-2-TGun/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.Gun1 = null;
        this.bDontShot = false;
        this.prevTime = -1L;
        this.prevA0 = 0.0F;
        this.hook1 = null;
//		this.hidePilot = true;
        Aircraft aircraft = this.aircraft();
        try {
            if (!(aircraft.getGunByHookName("_MGUN02") instanceof GunEmpty)) this.Gun1 = aircraft.getGunByHookName("_MGUN02");
        } catch (Exception exception) {}
        this.Patrons = 0.5F;
        this.PatronsMat = null;
        int i = -1;
        i = this.mesh.materialFind("patron");
        if (i != -1) {
            this.PatronsMat = this.mesh.material(i);
            this.PatronsMat.setLayer(0);
        }
    }

    private boolean bNeedSetUp;
    private Gun     Gun1;
    private float   Patrons;
    private Mat     PatronsMat;
    private boolean bDontShot;
    private long    prevTime;
    private float   prevA0;
    private Hook    hook1;

    static {
        Class class1 = CockpitMBR_2AM34_TGunner.class;
        Property.set(class1, "aiTuretNum", 1);
        Property.set(class1, "weaponControlNum", 11);
        Property.set(class1, "astatePilotIndx", 2);
        Property.set(class1, "normZN", 0.5F);
    }
}
