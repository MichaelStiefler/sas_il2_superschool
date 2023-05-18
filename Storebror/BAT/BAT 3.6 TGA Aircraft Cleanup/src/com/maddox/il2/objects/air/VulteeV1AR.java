package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class VulteeV1AR extends Vulteexyz implements TypeStormovikArmored {

    public VulteeV1AR() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        Math.max(-f * 800F, -70F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 120F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -120F * f, 0.0F);
    }

    protected void moveGear(float f) {
        VulteeV1AR.moveGear(this.hierMesh(), f);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("BayDoorL_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayDoorR_D0", 0.0F, -90F * f, 0.0F);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("default")) {
            this.hierMesh().chunkVisible("BayDoorC_D0", true);
            this.hierMesh().chunkVisible("BayDoorL_D0", false);
            this.hierMesh().chunkVisible("BayDoorR_D0", false);
            return;
        }
        if (this.thisWeaponsName.equals("4xR25")) {
            this.hierMesh().chunkVisible("BayDoorC_D0", true);
            this.hierMesh().chunkVisible("BayDoorL_D0", false);
            this.hierMesh().chunkVisible("BayDoorR_D0", false);
            return;
        }
        if (this.thisWeaponsName.equals("8xR25")) {
            this.hierMesh().chunkVisible("BayDoorC_D0", false);
            this.hierMesh().chunkVisible("BayDoorL_D0", true);
            this.hierMesh().chunkVisible("BayDoorR_D0", true);
            return;
        }
        if (this.thisWeaponsName.equals("2xFAB50+1xFAB100")) {
            this.hierMesh().chunkVisible("BayDoorC_D0", true);
            this.hierMesh().chunkVisible("BayDoorL_D0", false);
            this.hierMesh().chunkVisible("BayDoorR_D0", false);
            return;
        }
        if (this.thisWeaponsName.equals("4xFAB50")) {
            this.hierMesh().chunkVisible("BayDoorC_D0", false);
            this.hierMesh().chunkVisible("BayDoorL_D0", true);
            this.hierMesh().chunkVisible("BayDoorR_D0", true);
            return;
        } else {
            return;
        }
    }

    static {
        Class class1 = VulteeV1AR.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Vultee");
        Property.set(class1, "meshName", "3DO/Plane/Vultee/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/VulteeV1A.fmd:VulteeV1A_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitVultee.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb07", "_ExternalDev04", "_ExternalDev05" });
    }
}
