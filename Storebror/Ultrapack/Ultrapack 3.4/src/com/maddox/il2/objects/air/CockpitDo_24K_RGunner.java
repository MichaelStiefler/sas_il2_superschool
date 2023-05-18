package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitDo_24K_RGunner extends CockpitGunner {

    protected void moveVator(float f1) {
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("Turret3A", -0F, f, 0.0F);
        this.mesh.chunkSetAngles("Turret3B", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Turret3C", 0.0F, -f, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
        else {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            if (f < -75F) f = -75F;
            if (f > 75F) f = 75F;
            if (f1 > 45F) f1 = 45F;
            if (f1 < -40F) f1 = -40F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            else this.bGunFire = flag;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("Z_Holes1_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitDo_24K_RGunner() {
        super("3DO/Cockpit/Do_24K_RGun/hier.him", "i16");
        this.setbNeedSetUp(true);
        this.setPrevTime(-1L);
        this.setPrevA0(0.0F);
        this.hook1 = null;
    }

    public boolean isbNeedSetUp() {
        return this.bNeedSetUp;
    }

    public void setbNeedSetUp(boolean bNeedSetUp) {
        this.bNeedSetUp = bNeedSetUp;
    }

    public long getPrevTime() {
        return this.prevTime;
    }

    public void setPrevTime(long prevTime) {
        this.prevTime = prevTime;
    }

    public float getPrevA0() {
        return this.prevA0;
    }

    public void setPrevA0(float prevA0) {
        this.prevA0 = prevA0;
    }

    private boolean bNeedSetUp;
    private long    prevTime;
    private float   prevA0;
    private Hook    hook1;

    static {
        Property.set(CockpitDo_24K_RGunner.class, "aiTuretNum", 2);
        Property.set(CockpitDo_24K_RGunner.class, "weaponControlNum", 12);
        Property.set(CockpitDo_24K_RGunner.class, "astatePilotIndx", 3);
    }
}
