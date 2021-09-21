package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitA26K_TGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
        this.mesh.chunkSetAngles("Turret1B", 180F, -orient.getTangage(), 180F);
        this.mesh.chunkSetAngles("Body", 180F, 0.0F, 180F);
    }

    public void clipAnglesGun(Orient paramOrient) {
        if (!this.isRealMode()) {
            return;
        }
        if (!this.aiTurret().bIsOperable) {
            paramOrient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f1 = paramOrient.getYaw();
        float f2 = paramOrient.getTangage();
        if (f1 < -180F) {
            f1 = -180F;
        }
        if (f1 > 180F) {
            f1 = 180F;
        }
        if (f2 > 70F) {
            f2 = 70F;
        }
        float fClip = 0.0F;
        if (Math.abs(f1) > 170F) {
            fClip += Math.min(Math.abs(f1) - 170F, 4F);
        }
        if (Math.abs(f1) < 9F) {
            fClip += ((9F - Math.abs(f1)) / 9F) * 25F;
        }
        if (f2 < fClip) {
            f2 = fClip;
        }
        paramOrient.setYPR(f1, f2, 0.0F);
        paramOrient.wrap();
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
                this.hook1 = new HookNamed(this.aircraft(), "_MGUN17");
            }
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN17");
            if (this.hook2 == null) {
                this.hook2 = new HookNamed(this.aircraft(), "_MGUN18");
            }
            this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN18");
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
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
    }

    public CockpitA26K_TGunner() {
        super("3DO/Cockpit/A-20G-TGun/TopGunnerA26_B.him", "bf109");
        this.hook1 = null;
        this.hook2 = null;
    }

    private Hook hook1;
    private Hook hook2;

    static {
        Property.set(CockpitA_20G_BGunner.class, "aiTuretNum", 0);
        Property.set(CockpitA_20G_BGunner.class, "weaponControlNum", 10);
        Property.set(CockpitA_20G_BGunner.class, "astatePilotIndx", 2);
    }
}
