package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitP1Y1_NGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("Turret1A", 0.0F, -f, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -35F) f = -35F;
        if (f > 35F) f = 35F;
        if (f1 > 25F) f1 = 25F;
        if (f1 < -45F) f1 = -45F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.iCocking > 0) this.iCocking = 0;
            else this.iCocking = 1;
        } else this.iCocking = 0;
        this.resetYPRmodifier();
        xyz[1] = -0.07F * this.iCocking;
        ypr[1] = 0.0F;
        this.mesh.chunkSetLocate("Turret1C", xyz, ypr);
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitP1Y1_NGunner() {
        super("3DO/Cockpit/P1Y1-NGun/hier.him", "he111");
        this.iCocking = 0;
    }

    public void reflectCockpitState() {
    }

    private int iCocking;

    static {
        Property.set(CockpitP1Y1_NGunner.class, "aiTuretNum", 1);
        Property.set(CockpitP1Y1_NGunner.class, "weaponControlNum", 11);
        Property.set(CockpitP1Y1_NGunner.class, "astatePilotIndx", 1);
    }
}
