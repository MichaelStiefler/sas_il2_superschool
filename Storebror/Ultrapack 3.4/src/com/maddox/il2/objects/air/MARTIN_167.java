package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MARTIN_167 extends MARTIN_Maryland {
    static {
        Class class1 = MARTIN_167.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Maryland");
        Property.set(class1, "meshName", "3DO/Plane/MARTIN-167/hier_MkII.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/M167.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMARTIN_Maryland.class, CockpitMARTIN_167_Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 10, 11, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04" });
    }
}
