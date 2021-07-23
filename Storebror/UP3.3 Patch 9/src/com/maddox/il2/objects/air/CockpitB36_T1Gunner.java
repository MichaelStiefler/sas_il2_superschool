package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.CommonTools;

public class CockpitB36_T1Gunner extends CockpitB36_Gunner {

    public void clipAnglesGun(Orient orient) {
        this.bGunfireEnabled = true;
        float yaw = orient.getYaw();
        float pitch = orient.getTangage();
        
        if (pitch > 86F) pitch = 86F;
        if (pitch < -38F) pitch = -38F;
        if (yaw < -27.5F && yaw > -90F) yaw = -27.5F;
        if (yaw > -172F && yaw < -90F) yaw = -172F;
        
        orient.setYPR(yaw, pitch, 0.0F);
        orient.wrap();
        this.bGunfireEnabled = clipAnglesTurret(orient);
        this.mesh.chunkVisible("Z_Z_RETICLE", this.bGunfireEnabled);
        this.mesh.chunkVisible("Z_Z_RETICLE2", !this.bGunfireEnabled);
    }

    boolean clipAnglesTurret(Orient orient) {
        boolean retVal = true;
        this.turretYaw = orient.getYaw();
        this.turretPitch = orient.getTangage();
        if (turretYaw < -10F && turretYaw > -90F) turretYaw = -10F;
        if (turretYaw > -170F && turretYaw < -90F) turretYaw = -170F;
        
        if (turretPitch > 85F) {
            turretPitch = 85F;
            retVal = false;
        }
        
        float minPitch = -20F;
        if (turretYaw < 90F && turretYaw > -90F) {
            minPitch = (float)Math.sin(Math.toRadians(turretYaw)) * -24.5F + 4.5F;
        } else {
            minPitch = (float)Math.sin(Math.toRadians(turretYaw)) * -30F + 10F;
        }
        if (turretPitch < minPitch) {
            turretPitch = minPitch;
            retVal = false;
        }        
        if (turretYaw > 100F && turretYaw < 120F) {
            if (turretPitch < CommonTools.smoothCvt(turretYaw, 100F, 120F, minPitch, 0F)) retVal = false;
        } else if (turretYaw >= 120F && turretYaw < 170F) {
            if (turretPitch < 0F) retVal = false;
        } else {
            if (turretPitch < CommonTools.smoothCvt(Math.abs(turretYaw), 170F, 180F, minPitch, 15F)) retVal = false;
        }
        
        return retVal;
    }

    public CockpitB36_T1Gunner() {
        super("3DO/Cockpit/B36-RemoteTurrets/T1Gunner.him", "he111_gunner", 5);
    }

    static {
        Class class1 = CockpitB36_T1Gunner.class;
        Property.set(class1, "aiTuretNum", 2);
        Property.set(class1, "weaponControlNum", 12);
        Property.set(class1, "astatePilotIndx", 6);
        Property.set(class1, "normZN", 0.2F);
    }
}
