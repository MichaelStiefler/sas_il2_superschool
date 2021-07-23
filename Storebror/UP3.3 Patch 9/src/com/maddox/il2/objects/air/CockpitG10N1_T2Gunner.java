package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.CommonTools;

public class CockpitG10N1_T2Gunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
//        DecimalFormat df = new DecimalFormat("0.00");
//        HUD.training("y=" + df.format(orient.getYaw()) + ", t=" + df.format(orient.getTangage()));
        this.mesh.chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1A", 180F-orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        bGunfireEnabled = true;
        float yaw = orient.getYaw();
        float yawAbs = Math.abs(orient.getYaw());
        float pitch = orient.getTangage();
        if (pitch > 73F) pitch = 73F;
        if (pitch < 0F) pitch = 0F;

        bGunfireEnabled = true;
        if (yawAbs > 176F && pitch < 25F) bGunfireEnabled = false;
        if (yawAbs > 152F) pitch = Math.max(pitch, CommonTools.smoothCvt(yawAbs, 152F, 162F, 0F, 5F));
        else if (yawAbs < 15F) pitch = Math.max(pitch, CommonTools.smoothCvt(yawAbs, 5F, 15F, 2F, 0F));
        
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
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN03");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN03");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN04");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN04");
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

    public CockpitG10N1_T2Gunner() {
        super("3DO/Cockpit/G10N1-RemoteTurrets/T2GunnerG10N1.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
        this.bGunfireEnabled = false;
    }

    private Hook    hook1;
    private Hook    hook2;
    private boolean bGunfireEnabled;

    static {
        Class class1 = CockpitG10N1_T2Gunner.class;
        Property.set(class1, "aiTuretNum", 1);
        Property.set(class1, "weaponControlNum", 11);
        Property.set(class1, "astatePilotIndx", 5);
        Property.set(class1, "normZN", 0.2F);
    }
}
