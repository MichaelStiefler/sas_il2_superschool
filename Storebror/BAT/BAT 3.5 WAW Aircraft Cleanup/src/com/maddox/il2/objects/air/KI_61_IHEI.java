package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_61_IHEI extends KI_61 {

    public KI_61_IHEI() {
    }

    static {
        Class class1 = KI_61_IHEI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-61");
        Property.set(class1, "meshName", "3DO/Plane/Ki-61-I(Hei)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-61-I(Hei)(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-61-IHei.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_61.class });
        Property.set(class1, "LOSElevation", 0.81055F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
    }
}
