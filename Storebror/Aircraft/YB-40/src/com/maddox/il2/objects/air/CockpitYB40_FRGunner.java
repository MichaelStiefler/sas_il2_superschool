package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class CockpitYB40_FRGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("CF_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
            if (this.aircraft() == World.getPlayerAircraft()) {
                ((CockpitGunner) Main3D.cur3D().cockpits[2]).setRealMode(false);
            }
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("CF_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretRA", 0.0F, -orient.getYaw() + 78.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretRB", 25F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretRC", 0.0F, -orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) {
            if (!this.aiTurret().bIsOperable) {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if (f < -5F) {
                    f = -5F;
                }
                if (f > 85F) {
                    f = 85F;
                }
                if (f1 > 32F) {
                    f1 = 32F;
                }
                if (f1 < -40F) {
                    f1 = -40F;
                }
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) {
                    this.hook1 = new HookNamed(this.aircraft(), "_MGUN04");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN04");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            } else {
                this.bGunFire = flag;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public CockpitYB40_FRGunner() {
        super("3DO/Cockpit/YB-40-RGun/FRGunnerYB40.him", "he111_gunner");
        this.hook1 = null;
    }

    public void reflectWorldToInstruments(float f) {
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
    }

    private Hook hook1;

    static {
        Property.set(CockpitYB40_FRGunner.class, "aiTuretNum", 2);
        Property.set(CockpitYB40_FRGunner.class, "weaponControlNum", 12);
        Property.set(CockpitYB40_FRGunner.class, "astatePilotIndx", 3);
    }
}
