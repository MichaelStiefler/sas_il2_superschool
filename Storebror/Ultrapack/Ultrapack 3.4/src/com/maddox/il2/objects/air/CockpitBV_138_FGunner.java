package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitBV_138_FGunner extends CockpitGunner {

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

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("TurretA", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
        if (f1 > 45F) f1 = 45F;
        this.mesh.chunkSetAngles("Cam_B", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = Math.abs(f);
        for (; f < -90F; f += 180F)
            ;
        for (; f > 90F; f -= 180F)
            ;
        for (; this.prevA0 < -90F; this.prevA0 += 180F)
            ;
        for (; this.prevA0 > 90F; this.prevA0 -= 180F)
            ;
        if (!this.isRealMode()) this.prevA0 = f;
        else {
            if (this.bNeedSetUp) {
                this.prevTime = Time.current() - 1L;
                this.bNeedSetUp = false;
            }
            if (f < -120F && this.prevA0 > 120F) f += 180F;
            else if (f > 120F && this.prevA0 < -120F) this.prevA0 += 180F;
            float f3 = f - this.prevA0;
            float f4 = 0.002F * (Time.current() - this.prevTime);
            float f5 = Math.abs(f3 / f4);
            if (f5 > 120F) if (f > this.prevA0) f = this.prevA0 + 120F * f4;
            else if (f < this.prevA0) f = this.prevA0 - 120F * f4;
            this.prevTime = Time.current();
            if (f1 > 45F) f1 = 45F;
            float f6;
            if (f2 <= 90F) f6 = Math.max((float) -Math.pow(0.1F * f2, 2D) - 3F, -15F);
            else f6 = Math.max((float) -Math.pow(0.13F * (180F - f2), 2D), -15F);
            if (f1 < f6) f1 = f6;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
            this.prevA0 = f;
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_CANNON01");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_CANNON01");
            }
        }
        if (this.fm != null) {
            this.azimuthOld = this.azimuthNew;
            this.azimuthNew.setDeg(this.azimuthOld.getDeg(1.0F), this.fm.Or.azimut());
            this.altOld = this.altNew;
            this.altNew = this.fm.getAltitude();
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            else this.bGunFire = flag;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public CockpitBV_138_FGunner() {
        super("3DO/Cockpit/BV138B-FGun/hier.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.bNeedSetUp1 = true;
        this.prevTime = -1L;
        this.prevA0 = 0.0F;
        this.hook1 = null;
        this.azimuthOld = new AnglesFork();
        this.azimuthNew = new AnglesFork();
        this.cockpitNightMats = new String[] { "Prib_One", "Prib_Four", "Shkala128" };
        this.setNightMats(false);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp1) {
            this.reflectPlaneMats();
            this.bNeedSetUp1 = false;
        }
        this.retoggleLight();
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    protected void reflectPlaneMats() {
    }

    private boolean bNeedSetUp;
    private boolean bNeedSetUp1;
    private long    prevTime;
    private float   prevA0;
    private Hook    hook1;
    AnglesFork      azimuthOld;
    AnglesFork      azimuthNew;
    float           altOld;
    float           altNew;

    static {
        Property.set(CockpitBV_138_FGunner.class, "aiTuretNum", 0);
        Property.set(CockpitBV_138_FGunner.class, "weaponControlNum", 10);
        Property.set(CockpitBV_138_FGunner.class, "astatePilotIndx", 2);
    }
}
