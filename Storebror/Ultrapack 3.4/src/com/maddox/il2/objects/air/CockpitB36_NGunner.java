package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB36_NGunner extends CockpitB36_Gunner {

    public void clipAnglesGun(Orient orient) {
        this.bGunfireEnabled = true;
        float yaw = orient.getYaw();
        float pitch = orient.getTangage();
        if (pitch > 85F) {
            pitch = 85F;
        }
        if (pitch < -85F) {
            pitch = -85F;
        }
        if (yaw > 90F) {
            yaw = 90F;
        }
        if (yaw < -90F) {
            yaw = -90F;
        }
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
        if (turretYaw < -30F) {
            turretYaw = -30F;
            retVal = false;
        }
        if (turretYaw > 30F) {
            turretYaw = 30F;
            retVal = false;
        }
        if (turretPitch < 0F) {
            turretPitch = 0F;
            retVal = false;
        }
        if (turretPitch > 70.5F) {
            turretPitch = 70.5F;
            retVal = false;
        }
        return retVal;
    }
    
    public CockpitB36_NGunner() {
        super("3DO/Cockpit/B36-RemoteTurrets/NGunner.him", "he111_gunner", 3);
        this.bGunfireEnabled = true;
    }

    static {
        Class class1 = CockpitB36_NGunner.class;
        Property.set(class1, "aiTuretNum", 1);
        Property.set(class1, "weaponControlNum", 11);
        Property.set(class1, "astatePilotIndx", 5);
        Property.set(class1, "normZN", 0.2F);
    }
}
