package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class LA_7B20 extends LA_X {

    public LA_7B20() {
    }

    static {
        Class class1 = LA_7B20.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "La");
        Property.set(class1, "meshName", "3DO/Plane/La-7(3xB20)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/La-7.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLA_7B20.class });
        Property.set(class1, "LOSElevation", 0.730618F);
        weaponTriggersRegister(class1, new int[] { 1, 1, 1, 3, 3, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
