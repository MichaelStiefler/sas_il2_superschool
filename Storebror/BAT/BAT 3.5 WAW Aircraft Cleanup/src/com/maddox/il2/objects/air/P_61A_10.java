package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class P_61A_10 extends P_61X {

    public P_61A_10() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.endsWith("gunpod")) {
            this.hierMesh().chunkVisible("Turret1A_D0", true);
            this.hierMesh().chunkVisible("Turret1B_D0", true);
        } else {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
        }
        if (this.thisWeaponsName.startsWith("2x")) {
            this.hierMesh().chunkVisible("PylonDTK_L2", true);
            this.hierMesh().chunkVisible("PylonDTK_R2", true);
        } else {
            this.hierMesh().chunkVisible("PylonDTK_L2", false);
            this.hierMesh().chunkVisible("PylonDTK_R2", false);
        }
    }

    static {
        Class class1 = P_61A_10.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-61");
        Property.set(class1, "meshName", "3DO/Plane/P-61A/hierA.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-61A.fmd:P61_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_61B.class });
        Property.set(class1, "LOSElevation", 0.69215F);
        Property.set(class1, "Handicap", 1.0F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 10, 10, 10, 10, 9, 9, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14" });
    }
}
