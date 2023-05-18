package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class AMIOT_143M extends AMIOT_143 {
    static {
        Class class1 = AMIOT_143M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Amiot");
        Property.set(class1, "meshName", "3DO/Plane/AMIOT-143/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1940F);
        Property.set(class1, "FlightModel", "FlightModels/A143.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAMIOT_143.class, CockpitAMIOT_143M_Bombardier.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_BombSpawn06" });
    }
}
