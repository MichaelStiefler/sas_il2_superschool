package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.CommonTools;

public class CockpitME_264_T2Gunner extends CockpitGunner {

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
        if (pitch > 73F) pitch = 73F;
        if (pitch < -5F) pitch = -5F;
        if (yawAbs < 13F) pitch = Math.max(pitch,  0F);
        else if (yawAbs < 18F) pitch = Math.max(pitch,  CommonTools.smoothCvt(yawAbs, 13F, 18F, 0F, 10F));
        else if (yawAbs < 30F) pitch = Math.max(pitch,  10F);
        else if (yawAbs < 35F) pitch = Math.max(pitch,  CommonTools.smoothCvt(yawAbs, 30F, 35F, 10F, -5F));
        else if (yawAbs < 85F) pitch = Math.max(pitch,  -5F);
        else if (yawAbs < 90F) pitch = Math.max(pitch,  CommonTools.smoothCvt(yawAbs, 85F, 90F, -5F, 2F));
        else pitch = Math.max(pitch,  2F);
        orient.setYPR(yaw, pitch, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
                if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN03");
                this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN03");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            else this.bGunFire = flag;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public CockpitME_264_T2Gunner() {
        super("3DO/Cockpit/Me-264-RemoteTurrets/T2GunnerMe264.him", "he111_gunner");
        this.hook1 = null;
        this.hook2 = null;
    }

    private Hook    hook1;
    private Hook    hook2;

    static {
        Class class1 = CockpitME_264_T2Gunner.class;
        Property.set(class1, "aiTuretNum", 1);
        Property.set(class1, "weaponControlNum", 11);
        Property.set(class1, "astatePilotIndx", 5);
        Property.set(class1, "normZN", 0.2F);
    }
}
