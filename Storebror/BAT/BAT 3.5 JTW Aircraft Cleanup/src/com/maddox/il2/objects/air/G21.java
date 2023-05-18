package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class G21 extends Goose {

    public G21() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("Passengers")) {
            this.hierMesh().chunkVisible("Amiral", true);
            this.hierMesh().chunkVisible("Pass1", true);
            this.hierMesh().chunkVisible("Pass2", true);
            return;
        } else {
            return;
        }
    }

    static {
        Class class1 = G21.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G21");
        Property.set(class1, "meshName", "3DO/Plane/Goose/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar04());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 2018F);
        Property.set(class1, "FlightModel", "FlightModels/Goose.fmd:Goose_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitGoose.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 9, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
