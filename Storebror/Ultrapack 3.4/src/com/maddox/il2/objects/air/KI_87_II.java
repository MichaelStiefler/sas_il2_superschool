package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_87_II extends KI_87 {
    static {
        Class class1 = KI_87_II.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Property.set(class1, "iconFar_shortClassName", "Ki-87-II");
        Property.set(class1, "meshName", "3DO/Plane/Ki-87-II/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1955F);
        Property.set(class1, "FlightModel", "FlightModels/Ki87-II.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_87.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_ExternalDev01" });
    }
}
