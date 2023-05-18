package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitYB40_LGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Tail1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret6B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret7B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot7_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head7_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot8_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head8_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Tail1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret6B_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret7B_D0", true);
        this.aircraft().hierMesh().chunkVisible("Pilot7_D0", true);
        this.aircraft().hierMesh().chunkVisible("Head7_D0", true);
        this.aircraft().hierMesh().chunkVisible("Pilot8_D0", true);
        this.aircraft().hierMesh().chunkVisible("Head8_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretLA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretLB", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretLC", 0.0F, -orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) {
            if (!this.aiTurret().bIsOperable) {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if (f < -34F) {
                    f = -34F;
                }
                if (f > 50F) {
                    f = 50F;
                }
                if (f1 > 32F) {
                    f1 = 32F;
                }
                if (f1 < -30F) {
                    f1 = -30F;
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
                    this.hook1 = new HookNamed(this.aircraft(), "_MGUN10");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN10");
                if (this.hook2 == null) {
                    this.hook2 = new HookNamed(this.aircraft(), "_MGUN10a");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN10a");
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

    public CockpitYB40_LGunner() {
        super("3DO/Cockpit/YB-40-LGun/LGunnerYB40.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("TurretRA", 0.0F, this.aircraft().FM.turret[3].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretRB", 0.0F, this.aircraft().FM.turret[3].tu[1], 0.0F);
        this.mesh.chunkSetAngles("TurretRC", 0.0F, this.aircraft().FM.turret[3].tu[1], 0.0F);
        this.mesh.chunkVisible("TurretRC", false);
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

    private Hook    hook1;
    private Hook    hook2;

    static {
        Property.set(CockpitYB40_LGunner.class, "aiTuretNum", 5);
        Property.set(CockpitYB40_LGunner.class, "weaponControlNum", 16);
        Property.set(CockpitYB40_LGunner.class, "astatePilotIndx", 6);
    }
}
