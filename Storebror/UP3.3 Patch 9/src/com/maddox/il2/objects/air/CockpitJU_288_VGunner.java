package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.BaseGameVersion;

public class CockpitJU_288_VGunner extends CockpitJU_288_Gunner {

    public void clipAnglesGun(Orient orient) {
        this.bGunfireEnabled = true;
        float yaw = orient.getYaw();
        float pitch = orient.getTangage();

        if (pitch < -86F) {
            pitch = -86F;
        }
        if (pitch > 0F) {
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

        if (BaseGameVersion.is412orLater()) {
            if ((this.fm.CT.getGearC() > 0.02F) && (Math.abs(this.turretYaw) < 10F) && (this.turretPitch > -20F)) {
                retVal = false;
            }
        } else {
            if ((this.fm.CT.getGear() > 0.02F) && (Math.abs(this.turretYaw) < 10F) && (this.turretPitch > -20F)) {
                retVal = false;
            }
        }

        return retVal;
    }

    public CockpitJU_288_VGunner() {
        super("3DO/Cockpit/Ju_288-RemoteTurrets/VGunner.him", "he111_gunner", 5);
    }

    static {
        Class class1 = CockpitJU_288_VGunner.class;
        Property.set(class1, "aiTuretNum", 1);
        Property.set(class1, "weaponControlNum", 11);
        Property.set(class1, "astatePilotIndx", 2);
        Property.set(class1, "normZN", 0.2F);
    }
}
