package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB36_T3Gunner extends CockpitB36_Gunner {

    public void clipAnglesGun(Orient orient) {
        this.bGunfireEnabled = true;
        float yaw = orient.getYaw();
        float pitch = orient.getTangage();
        
        if (pitch > 86F) pitch = 86F;
        if (pitch < -38F) pitch = -38F;
        if (yaw < -8F && yaw > -90F) yaw = -8F;
        if (yaw > -152.5F && yaw < -90F) yaw = -152.5F;
        
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
            minPitch = (float)Math.sin(Math.toRadians(turretYaw)) * -26F + 6F;
        } else {
            minPitch = (float)Math.sin(Math.toRadians(turretYaw)) * -32F + 12F;
        }
        if (turretPitch < minPitch) {
            turretPitch = minPitch;
            retVal = false;
        }  
        
        if (turretYaw > -90F && turretYaw < 86F) {
            if (turretPitch < 0F) retVal = false;
        } else if (turretYaw > 138F && turretYaw < 175F) {
            if (turretPitch < -2F) retVal = false;
        } else if (Math.abs(turretYaw) > 175F) {
            if (turretPitch < 30F) retVal = false;
        }
        
        return retVal;
    }


    public CockpitB36_T3Gunner() {
        super("3DO/Cockpit/B36-RemoteTurrets/T3Gunner.him", "he111_gunner", 9);
    }

    static {
        Class class1 = CockpitB36_T3Gunner.class;
        Property.set(class1, "aiTuretNum", 4);
        Property.set(class1, "weaponControlNum", 14);
        Property.set(class1, "astatePilotIndx", 7);
        Property.set(class1, "normZN", 0.2F);
    }
}
