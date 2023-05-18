package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class FW_190A3 extends FW_190A_BASE implements TypeStormovik, TypeStormovikArmored {

    static {
        Class class1 = FW_190A3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190A-3(Beta)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1942.1F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190A-3 (Ultrapack).fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190A4.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 2, 2, 9, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08",
                "_ExternalRock01", "_ExternalRock02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01" });
    }
}
