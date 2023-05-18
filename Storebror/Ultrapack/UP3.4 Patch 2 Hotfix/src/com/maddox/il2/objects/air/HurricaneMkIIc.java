package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class HurricaneMkIIc extends Hurricane implements TypeFighter, TypeStormovik {

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        hierMesh.chunkVisible("filter", World.cur().camouflage == 2);
        Aircraft._WeaponSlot[] arrayOf_WeaponSlot = Aircraft.getWeaponSlotsRegistered(aircraftClass, thisWeaponsName);
        if ((arrayOf_WeaponSlot == null) || (arrayOf_WeaponSlot.length < 2)) {
          return;
        }
        hierMesh.chunkVisible("CannonL1_D0", arrayOf_WeaponSlot[0] != null);
        hierMesh.chunkVisible("CannonBaseL1_D0", arrayOf_WeaponSlot[0] != null);
        hierMesh.chunkVisible("CannonR1_D0", arrayOf_WeaponSlot[1] != null);
        hierMesh.chunkVisible("CannonBaseR1_D0", arrayOf_WeaponSlot[1] != null);
        hierMesh.chunkVisible("CannonL2_D0", arrayOf_WeaponSlot[2] != null);
        hierMesh.chunkVisible("CannonBaseL2_D0", arrayOf_WeaponSlot[2] != null);
        hierMesh.chunkVisible("CannonR2_D0", arrayOf_WeaponSlot[3] != null);
        hierMesh.chunkVisible("CannonBaseR2_D0", arrayOf_WeaponSlot[3] != null);
        hierMesh.chunkVisible("PylonL_D0", arrayOf_WeaponSlot[4] != null || arrayOf_WeaponSlot[8] != null);
        hierMesh.chunkVisible("PylonR_D0", arrayOf_WeaponSlot[5] != null || arrayOf_WeaponSlot[9] != null);
    }
    
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    static {
        Class class1 = HurricaneMkIIc.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hurri");
        Property.set(class1, "meshName", "3DO/Plane/HurricaneMkIIc(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/HurricaneMkII.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHURRII.class });
        Property.set(class1, "LOSElevation", 0.66895F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01",
                        "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock06", "_ExternalRock06", "_ExternalRock05", "_ExternalRock05", "_ExternalRock04", "_ExternalRock04", "_ExternalRock07",
                        "_ExternalRock07", "_ExternalRock08", "_ExternalRock08" });
    }
}
