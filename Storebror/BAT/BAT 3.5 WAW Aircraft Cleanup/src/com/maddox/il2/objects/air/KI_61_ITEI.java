package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_61_ITEI extends KI_61 {

    public KI_61_ITEI() {
    }

    static {
        Class class1 = KI_61_ITEI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-61");
        Property.set(class1, "meshName", "3DO/Plane/Ki-61-I(Tei)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-61-I(Tei)(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-61-ITei.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_61.class });
        Property.set(class1, "LOSElevation", 0.81055F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02" });
    }
}
