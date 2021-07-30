package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB36_T6Gunner extends CockpitB36_Gunner {

    public void clipAnglesGun(Orient orient) {
        this.bGunfireEnabled = true;
        float yaw = orient.getYaw();
        float pitch = orient.getTangage();
        
        if (pitch < -86F) pitch = -86F;
        if (pitch > 60F) pitch = 60F;
        if (yaw > 15F && yaw < 90F) yaw = 15F;
        if (yaw < 165F && yaw > 90F) yaw = 165F;
        
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
        if (turretYaw > 10F && turretYaw < 90F) turretYaw = 10F;
        if (turretYaw < 170F && turretYaw > 90F) turretYaw = 170F;
        
        if (turretPitch < -85F) {
            turretPitch = -85F;
            retVal = false;
        }
        
        float maxPitch = (float)Math.sin(Math.toRadians(turretYaw)) * -25.5F - 5.5F;
        if (turretPitch > maxPitch) {
            turretPitch = maxPitch;
            retVal = false;
        }
        
        if (turretYaw < 0F) {
            if (this.fm.Gears.lgear && turretYaw < -25F && turretYaw > -50F) {
                if (turretPitch > -10F) retVal = false;
            } else if (turretYaw <= -50F && turretYaw > -62F) {
                if (turretPitch > 7F) retVal = false;
            } else if (turretYaw <= -62F && turretYaw > -78F) {
                if (turretPitch > 2F) retVal = false;
            } else if (turretYaw <= -78F && turretYaw > -86F) {
                if (turretPitch > 5F) retVal = false;
            }
        }
        return retVal;
    }
    
    public CockpitB36_T6Gunner() {
        super("3DO/Cockpit/B36-RemoteTurrets/T6Gunner.him", "he111_gunner", 15);
    }

    static {
        Class class1 = CockpitB36_T6Gunner.class;
        Property.set(class1, "aiTuretNum", 7);
        Property.set(class1, "weaponControlNum", 17);
        Property.set(class1, "astatePilotIndx", 8);
        Property.set(class1, "normZN", 0.2F);
    }
}
