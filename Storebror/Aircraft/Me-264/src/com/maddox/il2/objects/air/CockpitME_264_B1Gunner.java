package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.BaseGameVersion;
import com.maddox.sas1946.il2.util.CommonTools;
import com.maddox.sas1946.il2.util.Reflection;

public class CockpitME_264_B1Gunner extends CockpitGunner {

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
        if (yawAbs < 5F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, 0F, 5F, -2F, 0F));
        else if (yawAbs < 45F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, 5F, 45F, 0F, 5F));
        else if (yawAbs > 135F) pitch = Math.min(pitch, CommonTools.smoothCvt(yawAbs, 135F, 180F, 5F, 0F));
        float bayDoor = 0F;
        if (BaseGameVersion.is412orLater()) {
            bayDoor = this.aircraft().getBayDoor();
        } else {
            bayDoor = Reflection.getFloat(this, "BayDoor_");
        }
        if (yawAbs < 80F && bayDoor > 0.01F) {
            pitch = Math.min(pitch, (float)Math.cos(yawAbs / 160F * Math.PI) * -73F * bayDoor);
        }
        
        bGunfireEnabled = true;
        
        float gearL = this.fm.CT.getGear();
        float gearR = this.fm.CT.getGear();
        float gearC = this.fm.CT.getGear();
        if (BaseGameVersion.is412orLater()) {
            gearL = this.fm.CT.getGearL();
            gearR = this.fm.CT.getGearR();
            gearC = this.fm.CT.getGearC();
        }
        if (gearL > 0F && yaw > 35F && yaw < 65F && pitch > -15F) bGunfireEnabled = false;
        if (gearR > 0F && yaw < -35F && yaw > -65F && pitch > -15F) bGunfireEnabled = false;
        if (gearC > 0F && yawAbs > 140F && pitch > -45F) bGunfireEnabled = false;

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
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN04");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN04");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN05");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN05");
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

    public CockpitME_264_B1Gunner() {
        super("3DO/Cockpit/Me-264-RemoteTurrets/B1GunnerMe264.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
        this.bGunfireEnabled = false;
    }

    private Hook    hook1;
    private Hook    hook2;
    private boolean bGunfireEnabled;

    static {
        Class class1 = CockpitME_264_B1Gunner.class;
        Property.set(class1, "aiTuretNum", 2);
        Property.set(class1, "weaponControlNum", 12);
        Property.set(class1, "astatePilotIndx", 6);
        Property.set(class1, "normZN", 0.2F);
    }
}
