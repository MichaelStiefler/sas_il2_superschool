package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class EA_6B extends A_6fuelReceiver {

    public EA_6B() {
        this.ratdeg = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.fuelReceiveRate = 10.093F;
        if (this.thisWeaponsName.endsWith("__ALQ3")) {
            this.hierMesh().chunkVisible("ALQ99Body_1", true);
            this.hierMesh().chunkVisible("ALQ99prop_1", true);
            this.hierMesh().chunkVisible("ALQ99Body_3", true);
            this.hierMesh().chunkVisible("ALQ99prop_3", true);
            this.hierMesh().chunkVisible("ALQ99Body_5", true);
            this.hierMesh().chunkVisible("ALQ99prop_5", true);
            this.FM.M.massEmpty += 1446F;
            this.FM.Sq.dragProducedCx += 0.08400001F;
        }
        if (this.thisWeaponsName.endsWith("__ALQ4")) {
            this.hierMesh().chunkVisible("ALQ99Body_1", true);
            this.hierMesh().chunkVisible("ALQ99prop_1", true);
            this.hierMesh().chunkVisible("ALQ99Body_2", true);
            this.hierMesh().chunkVisible("ALQ99prop_2", true);
            this.hierMesh().chunkVisible("ALQ99Body_4", true);
            this.hierMesh().chunkVisible("ALQ99prop_4", true);
            this.hierMesh().chunkVisible("ALQ99Body_5", true);
            this.hierMesh().chunkVisible("ALQ99prop_5", true);
            this.FM.M.massEmpty += 1928F;
            this.FM.Sq.dragProducedCx += 0.112F;
        }
        if (this.thisWeaponsName.endsWith("__ALQ5")) {
            this.hierMesh().chunkVisible("ALQ99Body_1", true);
            this.hierMesh().chunkVisible("ALQ99prop_1", true);
            this.hierMesh().chunkVisible("ALQ99Body_2", true);
            this.hierMesh().chunkVisible("ALQ99prop_2", true);
            this.hierMesh().chunkVisible("ALQ99Body_3", true);
            this.hierMesh().chunkVisible("ALQ99prop_3", true);
            this.hierMesh().chunkVisible("ALQ99Body_4", true);
            this.hierMesh().chunkVisible("ALQ99prop_4", true);
            this.hierMesh().chunkVisible("ALQ99Body_5", true);
            this.hierMesh().chunkVisible("ALQ99prop_5", true);
            this.FM.M.massEmpty += 2410F;
            this.FM.Sq.dragProducedCx += 0.14F;
        }
    }

    public void update(float f) {
        if (this.FM.getSpeedKMH() > 185F) {
            this.RATrot();
        }
        super.update(f);
    }

    public void missionStarting() {
        super.missionStarting();
    }

    private void RATrot() {
        if (this.FM.getSpeedKMH() < 250F) {
            this.ratdeg += 10F;
        } else if (this.FM.getSpeedKMH() < 400F) {
            this.ratdeg += 20F;
        } else if (this.FM.getSpeedKMH() < 550F) {
            this.ratdeg += 25F;
        } else {
            this.ratdeg += 31F;
        }
        if (this.ratdeg > 720F) {
            this.ratdeg -= 1440F;
        }
        for (int i = 1; i < 6; i++) {
            this.hierMesh().chunkSetAngles("ALQ99prop_" + i, 0.0F, 0.0F, this.ratdeg);
            if (this.hierMesh().isChunkVisible("ALQ99Body_" + i)) {
                if (this.FM.getSpeedKMH() > 300F) {
                    this.hierMesh().chunkVisible("ALQ99proprot_" + i, true);
                    this.hierMesh().chunkVisible("ALQ99prop_" + i, false);
                } else {
                    this.hierMesh().chunkVisible("ALQ99proprot_" + i, false);
                    this.hierMesh().chunkVisible("ALQ99prop_" + i, true);
                }
            }
        }

    }

    private float ratdeg;

    static {
        Class class1 = EA_6B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "EA-6B");
        Property.set(class1, "meshName", "3DO/Plane/EA-6B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 2002F);
        Property.set(class1, "FlightModel", "FlightModels/EA6B.fmd:A6");
        Property.set(class1, "cockpitClass", new Class[] { CockpitEA_6B.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 7, 8 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalDev06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36", "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_Flare01", "_Chaff01" });
    }
}
