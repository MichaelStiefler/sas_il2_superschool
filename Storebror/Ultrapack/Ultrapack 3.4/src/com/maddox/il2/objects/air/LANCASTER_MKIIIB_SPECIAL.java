package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class LANCASTER_MKIIIB_SPECIAL extends LancasterX implements TypeTransport, TypeBomber {
    static {
        Class class1 = LANCASTER_MKIIIB_SPECIAL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Lancaster");
        Property.set(class1, "meshName", "3DO/Plane/LANCASTER_MKIIIB_SPECIAL/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/LANCASTER_MKIIIB_SPECIAL.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLanc.class, CockpitLanc_Bombardier.class, CockpitLanc_FGunner.class, CockpitLanc_AGunner.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN09", "_MGUN10", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06",
                "_BombSpawn07", "_BombSpawn08", "_BombSpawn09" });
    }
}
