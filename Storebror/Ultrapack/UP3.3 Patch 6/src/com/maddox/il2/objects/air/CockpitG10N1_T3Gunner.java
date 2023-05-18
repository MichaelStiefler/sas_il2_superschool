package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.CommonTools;

public class CockpitG10N1_T3Gunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
//        DecimalFormat df = new DecimalFormat("0.00");
//        HUD.training("y=" + df.format(orient.getYaw()) + ", t=" + df.format(orient.getTangage()));
        this.mesh.chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1A", 180F-orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        float yaw = orient.getYaw();
        float yawAbs = Math.abs(orient.getYaw());
        float pitch = orient.getTangage();
        if (pitch > 73F) pitch = 73F;
        if (pitch < -5F) pitch = -5F;

        bGunfireEnabled = true;
        
        if (yawAbs > 173F && pitch < 45F) bGunfireEnabled = false;
        if (yawAbs > 125F) pitch = Math.max(pitch, CommonTools.smoothCvt(yawAbs, 125F, 135F, -5F, 0F));
        else if (yawAbs < 15F) pitch = Math.max(pitch, CommonTools.smoothCvt(yawAbs, 5F, 15F, 3F, 0F));
        else if (yawAbs < 80F) pitch = Math.max(pitch, CommonTools.smoothCvt(yawAbs, 70F, 80F, 0F, -5F));
        
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
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN05");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN05");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN06");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN06");
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

    public CockpitG10N1_T3Gunner() {
        super("3DO/Cockpit/G10N1-RemoteTurrets/T3GunnerG10N1.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
        this.bGunfireEnabled = false;
    }

    private Hook    hook1;
    private Hook    hook2;
    private boolean bGunfireEnabled;

    static {
        Class class1 = CockpitG10N1_T3Gunner.class;
        Property.set(class1, "aiTuretNum", 2);
        Property.set(class1, "weaponControlNum", 12);
        Property.set(class1, "astatePilotIndx", 6);
        Property.set(class1, "normZN", 0.1F);
    }
}
