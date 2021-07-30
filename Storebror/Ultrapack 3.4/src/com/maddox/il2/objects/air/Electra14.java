package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Electra14 extends Electra14XYZ implements TypeTransport, TypeScout {

    static {
        Class class1 = Electra14.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Electra-14");
        Property.set(class1, "meshName", "3DO/Plane/Electra14(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/SuperElectra.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitElectra14.class });
        Property.set(class1, "LOSElevation", 0.5265F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01", "_BombSpawn02" });
    }
}
