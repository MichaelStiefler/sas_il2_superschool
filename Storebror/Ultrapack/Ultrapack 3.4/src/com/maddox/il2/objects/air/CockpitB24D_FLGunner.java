package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB24D_FLGunner extends CockpitB24D_FGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret8A_Axis", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Turret8B_D0", 0.0F, orient.getTangage(), 0.0F);
        float f = -this.cvt(orient.getYaw(), 10F, 50F, 10F, 50F) - this.cvt(orient.getTangage(), -40F, -30F, 20F, 0.0F);
        float f1 = this.cvt(orient.getTangage(), -30F, 4F, -30F, 4F);
        this.mesh.chunkSetAngles("TurretLeft", f, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = this.cvt(f1, -31F, -5F, 20F, -5F);
        if (f < f2) f = f2;
        if (f > 50F) f = 50F;
        if (f1 > 15F) f1 = 15F;
        if (f1 < -40F) f1 = -40F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        super.interpTick();
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN12");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN12");
        }
    }

    public CockpitB24D_FLGunner() {
        super("3DO/Cockpit/B-24D-Bombardier/left_gun_hier.him");
        this.hook1 = null;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Turret7A_Axis", 0.0F, 40F, 0.0F);
        this.mesh.chunkSetAngles("Turret7B_D0", 0.0F, -40F, 0.0F);
        super.reflectWorldToInstruments(f);
    }

    private Hook hook1;

    static {
        Property.set(CockpitB24D_FLGunner.class, "aiTuretNum", 0);
        Property.set(CockpitB24D_FLGunner.class, "weaponControlNum", 10);
        Property.set(CockpitB24D_FLGunner.class, "astatePilotIndx", 2);
        Property.set(CockpitB24D_FLGunner.class, "normZN", 1.8F);
    }
}
