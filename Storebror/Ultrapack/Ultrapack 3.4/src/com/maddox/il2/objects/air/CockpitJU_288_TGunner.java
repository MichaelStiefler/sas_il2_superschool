package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitJU_288_TGunner extends CockpitJU_288_Gunner {

    public void clipAnglesGun(Orient orient) {
        this.bGunfireEnabled = true;
        float yaw = orient.getYaw();
        float pitch = orient.getTangage();

        if (pitch > 86F) {
            pitch = 86F;
        }
        if (pitch < 0F) {
            pitch = 0F;
        }
        if (yaw < -90F) {
            yaw = -90F;
        }
        if (yaw > 90F) {
            yaw = 90F;
        }

        orient.setYPR(yaw, pitch, 0.0F);
        orient.wrap();
        this.bGunfireEnabled = this.clipAnglesTurret(orient);
        this.mesh.chunkVisible("Z_Z_RETICLE", this.bGunfireEnabled);
        this.mesh.chunkVisible("Z_Z_RETICLE2", !this.bGunfireEnabled);
    }

    boolean clipAnglesTurret(Orient orient) {
        boolean retVal = true;
        this.turretYaw = orient.getYaw();
        this.turretPitch = orient.getTangage();

        if ((Math.abs(this.turretYaw) > 15F) && (Math.abs(this.turretYaw) < 20F) && (this.turretPitch < 6.5F)) {
            retVal = false;
        }
        if ((Math.abs(this.turretYaw) > 50F) && (this.turretPitch < 5F)) {
            retVal = false;
        }
        if ((Math.abs(this.turretYaw) > 70F) && (this.turretPitch < 15F)) {
            retVal = false;
        }

        return retVal;
    }

    public CockpitJU_288_TGunner() {
        super("3DO/Cockpit/Ju_288-RemoteTurrets/TGunner.him", "he111_gunner", 3);
    }

    static {
        Class class1 = CockpitJU_288_TGunner.class;
        Property.set(class1, "aiTuretNum", 0);
        Property.set(class1, "weaponControlNum", 10);
        Property.set(class1, "astatePilotIndx", 1);
        Property.set(class1, "normZN", 0.2F);
    }
}
