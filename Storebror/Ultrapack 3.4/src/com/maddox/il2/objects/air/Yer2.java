package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Yer2 extends Yermolayev {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("2xFAB2000")) {
            this.FM.M.massEmpty -= 800F;
            this.FM.M.maxFuel = 1200F;
        }
        if (this.thisWeaponsName.equals("2xFAB1000")) {
            this.FM.M.massEmpty -= 600F;
            this.FM.M.maxFuel = 1700F;
        }
    }

    static {
        Class class1 = Yer2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yer-2");
        Property.set(class1, "meshName", "3DO/Plane/Yermolayev-Yer2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/ER2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYermolayev.class, CockpitYermolayev_Bombardier.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10",
                "_BombSpawn11", "_BombSpawn12", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb06" });
    }
}
