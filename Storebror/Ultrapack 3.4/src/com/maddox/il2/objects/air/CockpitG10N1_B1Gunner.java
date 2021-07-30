package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.CommonTools;

public class CockpitG10N1_B1Gunner extends CockpitGunner {

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
        
        if (yawAbs < 15F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, 5F, 15F, -3F, 0F));
        else if (yawAbs > (this.fm.CT.getGear() > 0.01F?30F:50F) && yawAbs < 90F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, this.fm.CT.getGear() > 0.01F?30F:50F, this.fm.CT.getGear() > 0.01F?40F:60F, 0F, -10F));
        else if (yawAbs > 90F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, 90F, 100F, -10F, 0F));
        if (this.fm.CT.getGear() > 0.01F && yawAbs > 115F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, 115F, 125F, 0F, -45F));
        
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
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN07");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN07");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN08");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN08");
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

    public CockpitG10N1_B1Gunner() {
        super("3DO/Cockpit/G10N1-RemoteTurrets/B1GunnerG10N1.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
        this.bGunfireEnabled = false;
    }

    private Hook    hook1;
    private Hook    hook2;
    private boolean bGunfireEnabled;

    static {
        Class class1 = CockpitG10N1_B1Gunner.class;
        Property.set(class1, "aiTuretNum", 3);
        Property.set(class1, "weaponControlNum", 13);
        Property.set(class1, "astatePilotIndx", 7);
        Property.set(class1, "normZN", 0.2F);
    }
}
