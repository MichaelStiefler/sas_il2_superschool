package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class FW_190A3SANI1 extends FW_190 implements TypeFighter, TypeStormovik, TypeStormovikArmored {

    public FW_190A3SANI1() {
    }

    protected void moveGear(float f) {
        FW_190.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (((FlightModelMain) (super.FM)).CT.getGear() >= 0.98F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
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
        if (this.getGunByHookName("_CANNON03") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmL_D0", false);
        }
        if (this.getGunByHookName("_CANNON04") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmR_D0", false);
        }
        if (!(this.getGunByHookName("_ExternalDev05") instanceof GunEmpty)) {
            this.hierMesh().chunkVisible("Flap01_D0", false);
            this.hierMesh().chunkVisible("Flap01Holed_D0", true);
        }
        if (!(this.getGunByHookName("_ExternalDev06") instanceof GunEmpty)) {
            this.hierMesh().chunkVisible("Flap04_D0", false);
            this.hierMesh().chunkVisible("Flap04Holed_D0", true);
        }
    }

    static {
        Class class1 = FW_190A3SANI1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190A-3/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1942.1F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190A-3Sani1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190A3.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 2, 2, 9, 9, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalDev10" });
    }
}
