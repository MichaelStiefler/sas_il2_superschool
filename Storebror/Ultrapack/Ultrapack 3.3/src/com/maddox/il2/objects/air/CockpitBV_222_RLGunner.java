package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitBV_222_RLGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretLA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretLB", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
        else {
            float f = orient.getYaw();
            float f1 = orient.getTangage();
            if (f < -55F) f = -55F;
            if (f > 10F) f = 10F;
            if (f1 > 30F) f1 = 30F;
            if (f1 < -30F) f1 = -30F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
                if (this.iCocking > 0) this.iCocking = 0;
                else this.iCocking = 1;
                this.iNewVisDrums = (int) (this.emitter.countBullets() / 250F);
                if (this.iNewVisDrums < this.iOldVisDrums) {
                    this.iOldVisDrums = this.iNewVisDrums;
                    this.mesh.chunkVisible("DrumL1", this.iNewVisDrums > 1);
                    this.mesh.chunkVisible("DrumL2", this.iNewVisDrums > 0);
                    this.sfxClick(13);
                }
            } else this.iCocking = 0;
            this.resetYPRmodifier();
            Cockpit.xyz[0] = -0.07F * this.iCocking;
            this.mesh.chunkSetLocate("LeverL", Cockpit.xyz, Cockpit.ypr);
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            else this.bGunFire = flag;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public CockpitBV_222_RLGunner() {
        super("3DO/Cockpit/He-111H-2-LGun/RLGunnerBV222.him", "he111_gunner");
        this.hook1 = null;
        this.iCocking = 0;
        this.iOldVisDrums = 2;
        this.iNewVisDrums = 2;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.mesh.chunkVisible("Flare", true);
            this.setNightMats(true);
        } else {
            this.mesh.chunkVisible("Flare", false);
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if (this.fm.AS.astateCockpitState != 0) this.mesh.chunkVisible("Holes_D1", true);
    }

    private Hook hook1;
    private int  iCocking;
    private int  iOldVisDrums;
    private int  iNewVisDrums;

    static {
        Property.set(CockpitBV_222_RLGunner.class, "aiTuretNum", 2);
        Property.set(CockpitBV_222_RLGunner.class, "weaponControlNum", 12);
        Property.set(CockpitBV_222_RLGunner.class, "astatePilotIndx", 5);
    }
}
