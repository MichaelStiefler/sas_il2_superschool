package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class G4M2A extends G4M implements TypeBomber {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
    }

    static {
        Class class1 = G4M2A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G4M");
        Property.set(class1, "meshName", "3DO/Plane/G4M2A(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ja", "3DO/Plane/G4M2A(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/G4M2-2A.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitG4M2A.class, CockpitG4M2A_Bombardier.class, CockpitG4M2A_NGunner.class, CockpitG4M2A_AGunner.class, CockpitG4M2A_TGunner.class, CockpitG4M2A_RGunner.class, CockpitG4M2A_LGunner.class });
        Property.set(class1, "LOSElevation", 1.4078F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 15, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_MGUN04", "_MGUN05", "_CANNON02", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn02", "_BombSpawn03", "_BombSpawn03", "_BombSpawn04",
                "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_BombSpawn07", "_BombSpawn07", "_BombSpawn08", "_BombSpawn08", "_BombSpawn09", "_BombSpawn09" });
    }
}
