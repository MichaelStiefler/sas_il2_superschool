package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.CommonTools;

public class CockpitME_264_B2Gunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Wire1_D0", false);
            return true;
        } else
            return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Wire1_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        float yaw = orient.getYaw();
        float yawAbs = Math.abs(orient.getYaw());
        float pitch = orient.getTangage();
        if (pitch < -73F) pitch = -73F;
        if (pitch > 5F) pitch = 5F;
        if (yawAbs < 45F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, 0F, 45F, 0F, 5F));
        else if (yawAbs > 135F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, 135F, 180F, 5F, -2F));
        
        bGunfireEnabled = true;
        
        float gearL = this.fm.CT.getGearL();
        float gearR = this.fm.CT.getGearR();
        float gearC = this.fm.CT.getGearC();
        if (gearL > 0F && yaw > 115F && yaw < 145F && pitch > -15F) bGunfireEnabled = false;
        if (gearR > 0F && yaw < -115F && yaw > -145F && pitch > -15F) bGunfireEnabled = false;
        if (gearC > 0F && yawAbs > 175F && pitch > -10F) bGunfireEnabled = false;
        this.mesh.chunkVisible("Z_Z_RETICLE", bGunfireEnabled);
        this.mesh.chunkVisible("Z_Z_RETICLE2", !bGunfireEnabled);
        
        orient.setYPR(yaw, pitch, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable || !this.bGunfireEnabled) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN06");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN06");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN07");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN07");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable || !this.bGunfireEnabled) this.bGunFire = false;
            else this.bGunFire = flag;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public CockpitME_264_B2Gunner() {
        super("3DO/Cockpit/Me-264-RemoteTurrets/B2GunnerMe264.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
        this.bGunfireEnabled = false;
    }

    private Hook    hook1;
    private Hook    hook2;
    private boolean bGunfireEnabled;

    static {
        Class class1 = CockpitME_264_B2Gunner.class;
        Property.set(class1, "aiTuretNum", 3);
        Property.set(class1, "weaponControlNum", 13);
        Property.set(class1, "astatePilotIndx", 7);
        Property.set(class1, "normZN", 0.2F);
    }
}
