package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitKI_48_I_TGunner extends CockpitGunner {

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
        this.mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        if (f < -135F) f = -135F;
        if (f > 135F) f = 135F;
        float f1 = orient.getTangage();
        if (f1 > 45F) f1 = 45F;
        if (f1 < -69F) f1 = -69F;
        float f2;
        for (f2 = Math.abs(f); f2 > 180F; f2 -= 180F)
            ;
        if (f1 < -this.floatindex(this.cvt(f2, 0.0F, 180F, 0.0F, 36F), angles)) f1 = -this.floatindex(this.cvt(f2, 0.0F, 180F, 0.0F, 36F), angles);
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
            if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN04");
            this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN04");
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("Z_Holes1_D1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitKI_48_I_TGunner() {
        super("3DO/Cockpit/Ki48-II-TGun/TGunnerki48_I.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.bHandleMoved = false;
        this.hook1 = null;
        this.hook2 = null;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) this.bNeedSetUp = false;
        this.resetYPRmodifier();
        if (this.fm.CT.WeaponControl[11]) {
            if (this.bHandleMoved) {
                Cockpit.xyz[1] = 0.0F;
                this.bHandleMoved = false;
            } else {
                Cockpit.xyz[1] = 0.01F;
                this.bHandleMoved = true;
            }
        } else {
            Cockpit.xyz[1] = 0.0F;
            this.bHandleMoved = false;
        }
        this.mesh.chunkSetLocate("Z_CockingHdl", Cockpit.xyz, Cockpit.ypr);
    }

    private static final float angles[] = { 4F, 5.5F, 5.5F, 7F, 10.5F, 15.5F, 24F, 33F, 40F, 46F, 52.5F, 59F, 64.5F, 69F, 69F, 69F, 69F, 69F, 69F, 69F, 69F, 69F, 69F, 66.5F, 62.5F, 55F, 49.5F, -40F, -74.5F, -77F, -77F, -77F, -77F, -77F, -77F, -77F,
            -77F };
    private boolean            bNeedSetUp;
    private boolean            bHandleMoved;
    private Hook               hook1;
    private Hook               hook2;

    static {
        Property.set(CockpitKI_48_I_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitKI_48_I_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitKI_48_I_TGunner.class, "astatePilotIndx", 2);
    }
}
