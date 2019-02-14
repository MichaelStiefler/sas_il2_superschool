package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class FW_190A4142ATA extends FW_190 implements TypeFighter, TypeBNZFighter {

    public FW_190A4142ATA() {
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

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            FW_190A4142ATA.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            FW_190A4142ATA.bChangedPit = true;
        }
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

    public static boolean bChangedPit = false;

    static {
        Class class1 = FW_190A4142ATA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3do/plane/Fw-190A-4(Beta)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1942.1F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190A4.class });
        Property.set(class1, "FlightModel", "FlightModels/fw-190a-4_1.42.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 2, 2, 9, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01" });
    }
}
