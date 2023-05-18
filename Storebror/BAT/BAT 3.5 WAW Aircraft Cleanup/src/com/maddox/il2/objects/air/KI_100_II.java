package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_100_II extends KI_100 {

    public KI_100_II() {
    }

    static {
        Class class1 = KI_100_II.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-100");
        Property.set(class1, "meshName", "3DO/Plane/Ki-100-II(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_ja", "3DO/Plane/Ki-100-II(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1945.9F);
        Property.set(class1, "yearExpired", 1945.9F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-100-II.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_100.class });
        Property.set(class1, "LOSElevation", 0.85935F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
