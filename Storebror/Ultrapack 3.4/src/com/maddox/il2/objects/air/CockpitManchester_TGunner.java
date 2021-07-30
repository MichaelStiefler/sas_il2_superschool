package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitManchester_TGunner extends CockpitGunner {

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable || this.suppressGunFire) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN07");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN07");
            if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN08");
            this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN08");
        }
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", -orient.getYaw() - orient.getTangage() * 0.03F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret2B", 0.0F, orient.getTangage() + 1.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage() + 1.0F, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for (; f < -180F; f += 360F)
            ;
        for (; f > 180F; f -= 360F)
            ;
        if (this.isRealMode()) {
            for (; this.prevA0 < -180F; this.prevA0 += 360F)
                ;
            for (; this.prevA0 > 180F; this.prevA0 -= 360F)
                ;
        } else {
            this.prevA0 = f;
            return;
        }
        if (this.bNeedSetUp) {
            this.prevTime = Time.current() - 1L;
            this.bNeedSetUp = false;
            this.reflectPlaneMats();
        }
        if (f < -120F && this.prevA0 > 120F) f += 360F;
        else if (f > 120F && this.prevA0 < -120F) this.prevA0 += 360F;
        float f2 = f - this.prevA0;
        float f3 = 0.001F * (Time.current() - this.prevTime);
        float f4 = Math.abs(f2 / f3);
        if (f4 > 120F) if (f > this.prevA0) f = this.prevA0 + 120F * f3;
        else if (f < this.prevA0) f = this.prevA0 - 120F * f3;
        this.prevTime = Time.current();
        this.suppressGunFire = false;
        if (f >= 3F && f < 11F) {
            if (f1 < 27F) this.suppressGunFire = true;
        } else if (f >= 11F && f < 38F) {
            if (f1 < -6F) this.suppressGunFire = true;
        } else if (f >= 102F && f < 173F) {
            if (f1 < -1F) this.suppressGunFire = true;
        } else if (f >= 173F || f < -168F) {
            if (f1 < 6F) this.suppressGunFire = true;
        } else if (f >= -168F && f < -91F) {
            if (f1 < 1.5F) this.suppressGunFire = true;
        } else if (f >= -25F && f < 3F && f1 < -6F) this.suppressGunFire = true;
        if (f1 > 75F) f1 = 75F;
        if (f1 < -20F) f1 = -20F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        this.prevA0 = f;
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable || this.suppressGunFire) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret2A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Wire", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            this.aircraft().hierMesh().chunkVisible("Turret2A_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Wire", true);
            super.doFocusLeave();
            return;
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
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("Z_Holes1_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitManchester_TGunner() {
        super("3DO/Cockpit/Stirling-TGun/hier_manchester.him", "bf109");
        this.bNeedSetUp = true;
        this.prevTime = -1L;
        this.prevA0 = 0.0F;
        this.hook1 = null;
        this.hook2 = null;
        this.suppressGunFire = false;
    }

    private boolean bNeedSetUp;
    private long    prevTime;
    private float   prevA0;
    private Hook    hook1;
    private Hook    hook2;
    private boolean suppressGunFire;

    static {
        Property.set(CockpitManchester_TGunner.class, "aiTuretNum", 2);
        Property.set(CockpitManchester_TGunner.class, "weaponControlNum", 12);
        Property.set(CockpitManchester_TGunner.class, "astatePilotIndx", 1);
    }
}
