package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F4FS_3 extends F4FXX implements TypeScout, TypeSailPlane, TypeStormovik {

    public F4FS_3() {
    }

    static {
        Class class1 = F4FS_3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F4F_Catfish");
        Property.set(class1, "meshName", "3DO/Plane/F4FCatfish(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F4FCatfish.fmd:F4FCatfish_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF4F3X.class });
        Property.set(class1, "LOSElevation", 1.28265F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 9, 9, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05" });
    }
}
