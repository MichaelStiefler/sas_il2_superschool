package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class J1N1_Sa extends J1N1 {
    static {
        Class class1 = J1N1_Sa.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "J1N");
        Property.set(class1, "meshName", "3do/plane/J1N1Sa/S_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/J1N-1SA.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJ1N1_Sa.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 10, 3, 3, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON02", "_CANNON03", "_CANNON04", "_MGUN01", "_BombSpawn02", "_BombSpawn03", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05" });
    }
}
