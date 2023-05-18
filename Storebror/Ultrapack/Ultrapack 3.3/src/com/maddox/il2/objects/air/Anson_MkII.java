package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Anson_MkII extends Anson {

    static {
        Class class1 = Anson_MkII.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Anson");
        Property.set(class1, "meshName", "3DO/Plane/Anson/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Avro_Anson.fmd");
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_Anson.class });
        Property.set(class1, "LOSElevation", 1.0728F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 3, 3, 3, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01" });
    }
}
