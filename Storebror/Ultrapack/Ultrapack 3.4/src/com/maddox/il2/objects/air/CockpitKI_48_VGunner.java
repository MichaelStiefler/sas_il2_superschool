package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitKI_48_VGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", -14.2F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -30F) f = -30F;
        if (f > 30F) f = 30F;
        if (f1 > 15F) f1 = 15F;
        if (f1 < -45F) f1 = -45F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public void reflectCockpitState() {
        if (this.fm.AS.astateCockpitState != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage2", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
    }

    public CockpitKI_48_VGunner() {
        super("3DO/Cockpit/Ki-48-BGun/BGunnerki48.him", "he111_gunner");
        this.hook1 = null;
    }

    private Hook hook1;

    static {
        Property.set(CockpitKI_48_VGunner.class, "aiTuretNum", 2);
        Property.set(CockpitKI_48_VGunner.class, "weaponControlNum", 12);
        Property.set(CockpitKI_48_VGunner.class, "astatePilotIndx", 3);
    }
}
