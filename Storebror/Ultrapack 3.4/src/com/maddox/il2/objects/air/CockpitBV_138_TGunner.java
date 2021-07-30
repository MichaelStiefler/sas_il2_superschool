package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitBV_138_TGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("Turret1A", f, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Turret2B", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
        else {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            float f2 = Math.abs(f);
            if (f1 < -20F) f1 = -20F;
            if (f1 > 45F) f1 = 45F;
            if (f2 < 147F) {
                if (f1 < 0.5964912F * f2 - 117.6842F) f1 = 0.5964912F * f2 - 117.6842F;
            } else if (f2 < 157F) {
                if (f1 < 0.3F * f2 - 74.1F) f1 = 0.3F * f2 - 74.1F;
            } else if (f1 < 0.2173913F * f2 - 61.13044F) f1 = 0.2173913F * f2 - 61.13044F;
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

    public CockpitBV_138_TGunner() {
        super("3DO/Cockpit/BV138B-TGun/hier.him", "i16");
        this.hook1 = null;
        BulletEmitter abulletemitter[] = this.aircraft().FM.CT.Weapons[this.weaponControlNum()];
        if (abulletemitter != null) for (int i = 0; i < abulletemitter.length; i++)
            if (abulletemitter[i] instanceof Actor) ((Actor) abulletemitter[i]).visibilityAsBase(false);
    }

    private Hook hook1;

    static {
        Property.set(CockpitBV_138_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitBV_138_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitBV_138_TGunner.class, "astatePilotIndx", 4);
    }
}
