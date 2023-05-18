package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BV_155 extends BV155xyz {

    public BV_155() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("R4") || this.thisWeaponsName.equals("default")) {
            this.hierMesh().chunkVisible("MGs_D0", false);
            return;
        } else {
            return;
        }
    }

    static {
        Class class1 = BV_155.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BV155");
        Property.set(class1, "meshName", "3DO/Plane/BV-155/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1945.12F);
        Property.set(class1, "FlightModel", "FlightModels/BV155B.fmd:BV155_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D9early.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1, 1, 9, 9, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_CANNON04", "_CANNON05" });
    }
}
