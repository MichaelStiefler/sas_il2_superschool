package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class FW_190A9 extends FW_190 {

    public FW_190A9() {
    }

    protected void moveGear(float f) {
        FW_190.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        if (this.getGunByHookName("_MGUN01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("7mmC_D0", false);
            this.hierMesh().chunkVisible("7mmCowl_D0", true);
        }
        if (this.getGunByHookName("_CANNON01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmL1_D0", false);
        }
        if (this.getGunByHookName("_CANNON02") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmR1_D0", false);
        }
    }

    static {
        Class class1 = FW_190A9.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190A-9(Beta)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190A-9.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190A9.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 9, 9, 1, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 9, 9, 1, 1, 9, 9, 2, 2, 9, 9, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalDev02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev05", "_ExternalDev06", "_CANNON09", "_CANNON10", "_ExternalDev07", "_ExternalDev08", "_CANNON11", "_CANNON12", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalDev11", "_ExternalDev12", "_CANNON13", "_CANNON14" });
    }
}
