package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class FolgoreSea extends FolgoreSxyz {

    public FolgoreSea() {
    }

    static {
        Class class1 = FolgoreSea.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Folgore");
        Property.set(class1, "meshName", "3DO/Plane/FolgoreSea(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/FolgoreSea.fmd:MacchiSea_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFolgoreSea.class });
        Property.set(class1, "LOSElevation", 0.81305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
