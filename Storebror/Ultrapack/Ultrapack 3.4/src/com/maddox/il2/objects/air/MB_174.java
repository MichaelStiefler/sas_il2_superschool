package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MB_174 extends MB_174xyz
{
    static 
    {
        Class class1 = MB_174.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MB174");
        Property.set(class1, "meshName", "3DO/Plane/MB-174/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1952F);
        Property.set(class1, "FlightModel", "FlightModels/MB_174.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMB_174.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 10, 10, 11, 11, 11, 3, 3, 3, 
            3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", 
            "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08"
        });
    }
}
