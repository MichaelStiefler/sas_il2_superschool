package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MC_202_7 extends MC_202xyz {

    public MC_202_7() {
    }

    static {
        Class class1 = MC_202_7.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "M.C.202");
        Property.set(class1, "meshName_it", "3DO/Plane/MC-202_VII(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeFCSPar02());
        Property.set(class1, "meshName", "3DO/Plane/MC-202_VII(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/MC-202.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMC_202.class });
        Property.set(class1, "LOSElevation", 0.81305F);
        weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
