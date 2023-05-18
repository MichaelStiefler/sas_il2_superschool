package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class HurricaneMkIIb extends Hurricane implements TypeFighter, TypeStormovik {

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        hierMesh.chunkVisible("filter", World.cur().camouflage == 2);
        Aircraft._WeaponSlot[] arrayOf_WeaponSlot = Aircraft.getWeaponSlotsRegistered(aircraftClass, thisWeaponsName);
        if ((arrayOf_WeaponSlot == null) || (arrayOf_WeaponSlot.length < 2)) {
          return;
        }
        hierMesh.chunkVisible("OuterMGL_D0", arrayOf_WeaponSlot[8] != null);
        hierMesh.chunkVisible("OuterMGR_D0", arrayOf_WeaponSlot[8] != null);
        hierMesh.chunkVisible("PylonL_D0", arrayOf_WeaponSlot[12] != null || arrayOf_WeaponSlot[14] != null);
        hierMesh.chunkVisible("PylonR_D0", arrayOf_WeaponSlot[12] != null || arrayOf_WeaponSlot[14] != null);
    }
    
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    static {
        Class class1 = HurricaneMkIIb.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hurri");
        Property.set(class1, "meshName", "3DO/Plane/HurricaneMkIIb(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIIa.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHURRII.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
    }
}
