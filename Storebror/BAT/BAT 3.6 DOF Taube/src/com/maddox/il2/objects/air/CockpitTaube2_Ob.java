package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitTaube2_Ob extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("Turret1A_D0", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("Turret1B_D0", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) {
            if (!this.aiTurret().bIsOperable) {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else {
                float f = orient.getYaw();
                float f1 = orient.getTangage();

                float fMax = -10F;
                if (f < -120F) {
                    f = -120F;
                } else if (f < -70F) {
                    fMax = 0F;
                } else if ((f >= -70F) && (f < -65F)) {
                    fMax = Aircraft.cvt(f, -70F, -65F, 0F, -10F);
                } else if ((f >= -51.5F) && (f < -46.5F)) {
                    fMax = Aircraft.cvt(f, -51.5F, -46.5F, -10F, 35F);
                } else if ((f >= -46.5F) && (f < -11.75F)) {
                    fMax = Aircraft.cvt(f, -46.5F, -11.75F, 35F, 45F);
                } else if ((f >= -11.75F) && (f < 23F)) {
                    fMax = Aircraft.cvt(f, -11.75F, 23F, 45F, 35F);
                } else if ((f >= 23F) && (f < 28F)) {
                    fMax = Aircraft.cvt(f, 23F, 28F, 35F, -10F);
                } else if ((f > 55F) && (f <= 60F)) {
                    fMax = Aircraft.cvt(f, 55F, 60F, -10F, 0F);
                } else if (f > 60F) {
                    fMax = 0F;
                }
                if (f > 100F) {
                    f = 100F;
                }
                if (f1 < fMax) {
                    f1 = fMax;
                }
                if (f1 > 80F) {
                    f1 = 80F;
                }

                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Pilot2_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        super.doFocusLeave();
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            }
            if (this.bGunFire) {
                if (this.hook2 == null) {
                    this.hook2 = new HookNamed(this.aircraft(), "_MGUN02");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN02");
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = false;
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

    public CockpitTaube2_Ob() {
        super("3DO/Cockpit/Taube2_Ob/hier.him", "i16");
        this.setbNeedSetUp(true);
    }

    public boolean isbNeedSetUp() {
        return this.bNeedSetUp;
    }

    public void setbNeedSetUp(boolean flag) {
        this.bNeedSetUp = flag;
    }

    private boolean bNeedSetUp;
    private Hook    hook2;

    static {
        Property.set(CockpitTaube2_Ob.class, "aiTuretNum", 0);
        Property.set(CockpitTaube2_Ob.class, "weaponControlNum", 10);
        Property.set(CockpitTaube2_Ob.class, "astatePilotIndx", 1);
        Property.set(CockpitTaube2_Ob.class, "normZN", 0.01F);
    }
}
