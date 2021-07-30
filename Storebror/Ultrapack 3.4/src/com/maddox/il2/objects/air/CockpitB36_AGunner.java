package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB36_AGunner extends CockpitB36_Gunner {

    public void clipAnglesGun(Orient orient) {
        float yaw = orient.getYaw();
        float pitch = orient.getTangage();
        if (pitch > 45F) {
            pitch = 45F;
        }
        if (pitch < -50F) {
            pitch = -50F;
        }
        if (yaw > 60F) {
            yaw = 60F;
        }
        if (yaw < -60F) {
            yaw = -60F;
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
        if (turretYaw < -45F) {
            turretYaw = -45F;
            retVal = false;
        }
        if (turretYaw > 45F) {
            turretYaw = 45F;
            retVal = false;
        }
        if (turretPitch < -36.5F) {
            turretPitch = -36.5F;
            retVal = false;
        }
        if (turretPitch > 37.5F) {
            turretPitch = 37.5F;
            retVal = false;
        }
        return retVal;
    }
    
    public CockpitB36_AGunner() {
        super("3DO/Cockpit/B36-RemoteTurrets/AGunner.him", "he111_gunner", 1);
        this.bGunfireEnabled = true;
    }

    static {
        Class class1 = CockpitB36_AGunner.class;
        Property.set(class1, "aiTuretNum", 0);
        Property.set(class1, "weaponControlNum", 10);
        Property.set(class1, "astatePilotIndx", 4);
        Property.set(class1, "normZN", 0.2F);
    }
}
