package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.CommonTools;

public class CockpitG10N1_B2Gunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
//        DecimalFormat df = new DecimalFormat("0.00");
//        HUD.training("y=" + df.format(orient.getYaw()) + ", t=" + df.format(orient.getTangage()));
        this.mesh.chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        float yaw = orient.getYaw();
        float yawAbs = Math.abs(orient.getYaw());
        float pitch = orient.getTangage();
        if (pitch < -73F) pitch = -73F;
        if (pitch > 0F) pitch = 0F;

        bGunfireEnabled = true;
        
        if (yawAbs > 115F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, 115F, 125F, 0F, -10F));
        
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
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN09");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN09");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN10");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN10");
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

    public CockpitG10N1_B2Gunner() {
        super("3DO/Cockpit/G10N1-RemoteTurrets/B2GunnerG10N1.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
        this.bGunfireEnabled = false;
    }

    private Hook    hook1;
    private Hook    hook2;
    private boolean bGunfireEnabled;

    static {
        Class class1 = CockpitG10N1_B2Gunner.class;
        Property.set(class1, "aiTuretNum", 4);
        Property.set(class1, "weaponControlNum", 14);
        Property.set(class1, "astatePilotIndx", 8);
        Property.set(class1, "normZN", 0.2F);
    }
}
