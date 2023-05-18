package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class FiatBr20 extends FiatBr2x {
    static {
        Class class1 = FiatBr20.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Br.20");
        Property.set(class1, "meshName", "3DO/Plane/FiatBr20/hierS.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1936.8F);
        Property.set(class1, "yearExpired", 1944.5F);
        Property.set(class1, "FlightModel", "FlightModels/Br20.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFiatBr2x.class, CockpitFiatBr2x_Bombardier.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 12, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", "_BombSpawn27", "_BombSpawn28", "_BombSpawn29", "_BombSpawn30", "_BombSpawn31", "_BombSpawn32", "_BombSpawn33", "_BombSpawn34", "_BombSpawn35", "_BombSpawn36", "_BombSpawn37", "_BombSpawn38", "_BombSpawn39", "_BombSpawn40", "_BombSpawn41", "_BombSpawn42", "_BombSpawn43", "_BombSpawn44", "_BombSpawn45", "_BombSpawn46", "_BombSpawn47", "_BombSpawn48", "_BombSpawn49", "_BombSpawn50", "_BombSpawn51", "_BombSpawn52", "_BombSpawn53", "_BombSpawn54" });
    }
}
