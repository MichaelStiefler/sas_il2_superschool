package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class BF_109F2U extends BF_109Fx {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        _WeaponSlot[] weaponSlotsRegistered = Aircraft.getWeaponSlotsRegistered(aircraftClass, thisWeaponsName);
        hierMesh.chunkVisible("NoseCannon1_D0", weaponSlotsRegistered[2] != null);
        if (weaponSlotsRegistered[2] != null) {
            hierMesh.chunkVisible("Mg131_D0", true);
            hierMesh.chunkVisible("MgFFL_D0", false);
            hierMesh.chunkVisible("MgFFR_D0", false);
        } else {
            hierMesh.chunkVisible("Mg131_D0", false);
            hierMesh.chunkVisible("MgFFL_D0", true);
            hierMesh.chunkVisible("MgFFR_D0", true);
        }
    }

    static {
        Class class1 = BF_109F2U.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109F-2(Galland)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109F2.class });
        Property.set(class1, "FlightModel", "FlightModels/Bf-109F-2_Mod.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 9, 9, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
