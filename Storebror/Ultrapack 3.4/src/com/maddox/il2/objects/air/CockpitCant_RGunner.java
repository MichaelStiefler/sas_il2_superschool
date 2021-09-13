package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitCant_RGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret4A", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Turret4B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) {
            return;
        }
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -63F) {
            f = -63F;
        }
        if (f > 17F) {
            f = 17F;
        }
        if (f1 < -45F) {
            f1 = -45F;
        }
        if (f1 > 45F) {
            f1 = 45F;
        }
        if (f < -30F) {
            if (f1 < this.cvt(f, -60F, -30F, -10F, -16F)) {
                f1 = this.cvt(f, -60F, -30F, -10F, -16F);
            }
        } else if (f < 0.0F) {
            if (f1 < this.cvt(f, -30F, 0.0F, -16F, -23F)) {
                f1 = this.cvt(f, -30F, 0.0F, -16F, -23F);
            }
            if (f1 > this.cvt(f, -10F, 0.0F, 45F, 33F)) {
                f1 = this.cvt(f, -10F, 0.0F, 45F, 33F);
            }
        } else {
            if (f1 < this.cvt(f, 0.0F, 30F, -23F, -6F)) {
                f1 = this.cvt(f, 0.0F, 30F, -23F, -6F);
            }
            if (f1 > this.cvt(f, 0.0F, 30F, 33F, 22F)) {
                f1 = this.cvt(f, 0.0F, 30F, 33F, 22F);
            }
        }
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
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
                this.hook1 = new HookNamed(this.aircraft(), "_MGUN04");
            }
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN04");
            if (this.iCocking > 0) {
                this.iCocking = 0;
            } else {
                this.iCocking = 1;
            }
            this.iNewVisDrums = (int) (this.emitter.countBullets() / 250F);
            if (this.iNewVisDrums < this.iOldVisDrums) {
                this.iOldVisDrums = this.iNewVisDrums;
                this.mesh.chunkVisible("DrumR1", this.iNewVisDrums > 1);
                this.mesh.chunkVisible("DrumR2", this.iNewVisDrums > 0);
                this.sfxClick(13);
            }
        } else {
            this.iCocking = 0;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -0.07F * this.iCocking;
        this.mesh.chunkSetLocate("LeverR", Cockpit.xyz, Cockpit.ypr);
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

    public CockpitCant_RGunner() {
        super("3DO/Cockpit/Cant-RGun/hier.him", "he111_gunner");
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
        if (this.fm.AS.astateCockpitState != 0) {
            this.mesh.chunkVisible("Holes_D1", true);
        }
    }

    private Hook hook1;
    private int  iCocking;
    private int  iOldVisDrums;
    private int  iNewVisDrums;

    static {
        Property.set(CockpitCant_RGunner.class, "aiTuretNum", 0);
        Property.set(CockpitCant_RGunner.class, "weaponControlNum", 13);
        Property.set(CockpitCant_RGunner.class, "astatePilotIndx", 4);
    }
}
