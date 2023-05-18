package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB25BRaider_TGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret2B_D0", this.aircraft().hierMesh().isChunkVisible("Turret2A_D0"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretC", 0.0F, this.cvt(orient.getTangage(), -10F, 58F, -10F, 58F), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        float f = orient.getYaw();
        float f_0_ = orient.getTangage();
        for (; f < -180F; f += 360F)
            ;
        for (; f > 180F; f -= 360F)
            ;
        for (; this.prevA0 < -180F; this.prevA0 += 360F)
            ;
        for (; this.prevA0 > 180F; this.prevA0 -= 360F)
            ;
        if (!this.isRealMode()) this.prevA0 = f;
        else {
            if (this.bNeedSetUp) {
                this.prevTime = Time.current() - 1L;
                this.bNeedSetUp = false;
                this.reflectPlaneMats();
            }
            if (f < -120F && this.prevA0 > 120F) f += 360F;
            else if (f > 120F && this.prevA0 < -120F) this.prevA0 += 360F;
            float f_2_ = f - this.prevA0;
            float f_3_ = 0.001F * (Time.current() - this.prevTime);
            float f_4_ = Math.abs(f_2_ / f_3_);
            if (f_4_ > 120F) if (f > this.prevA0) f = this.prevA0 + 120F * f_3_;
            else if (f < this.prevA0) f = this.prevA0 - 120F * f_3_;
            this.prevTime = Time.current();
            if (f_0_ > 73F) f_0_ = 73F;
            if (f_0_ < 0.0F) f_0_ = 0.0F;
            orient.setYPR(f, f_0_, 0.0F);
            orient.wrap();
            this.prevA0 = f;
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN04");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN04");
            }
        }
    }

    public void doGunFire(boolean bool) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            else this.bGunFire = bool;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.prevTime = Time.current() - 1L;
            this.bNeedSetUp = false;
            this.reflectPlaneMats();
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
    }

    public CockpitB25BRaider_TGunner() {
        super("3DO/Cockpit/B-25J-TGun/TGunnerB25C25.him", "bf109");
        this.bNeedSetUp = true;
        this.prevTime = -1L;
        this.prevA0 = 0.0F;
        this.hook1 = null;
        this.hook2 = null;
    }

    private boolean bNeedSetUp;
    private long    prevTime;
    private float   prevA0;
    private Hook    hook1;
    private Hook    hook2;

    static {
        Property.set(CockpitB25BRaider_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitB25BRaider_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitB25BRaider_TGunner.class, "astatePilotIndx", 3);
    }
}
