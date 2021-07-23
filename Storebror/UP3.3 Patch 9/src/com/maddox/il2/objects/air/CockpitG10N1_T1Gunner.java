package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.CommonTools;

public class CockpitG10N1_T1Gunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
//        DecimalFormat df = new DecimalFormat("0.00");
//        HUD.training("y=" + df.format(orient.getYaw()) + ", t=" + df.format(orient.getTangage()));
        this.mesh.chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1A", 180F - orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        bGunfireEnabled = true;
        float yaw = orient.getYaw();
        float yawAbs = Math.abs(orient.getYaw());
        float pitch = orient.getTangage();
        if (pitch > 73F) pitch = 73F;
        if (pitch < -5F) pitch = -5F;
        
        if (yawAbs > 178F && pitch < 15F) bGunfireEnabled = false;
        if (yawAbs > 152F) pitch = Math.max(pitch, CommonTools.smoothCvt(yawAbs, 152F, 162F, 0F, 5F));
        else if (yawAbs > 80F) pitch = Math.max(pitch, CommonTools.smoothCvt(yawAbs, 80F, 90F, -5F, 0F));
        else if (yawAbs < 40F) pitch = Math.max(pitch, CommonTools.smoothCvt(yawAbs, 30F, 40F, 0F, -5F));
        
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
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN02");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN02");
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

    public CockpitG10N1_T1Gunner() {
        super("3DO/Cockpit/G10N1-RemoteTurrets/T1GunnerG10N1.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
        this.bGunfireEnabled = false;
    }

    private Hook    hook1;
    private Hook    hook2;
    private boolean bGunfireEnabled;

    static {
        Class class1 = CockpitG10N1_T1Gunner.class;
        Property.set(class1, "aiTuretNum", 0);
        Property.set(class1, "weaponControlNum", 10);
        Property.set(class1, "astatePilotIndx", 4);
        Property.set(class1, "normZN", 0.2F);
    }
}
