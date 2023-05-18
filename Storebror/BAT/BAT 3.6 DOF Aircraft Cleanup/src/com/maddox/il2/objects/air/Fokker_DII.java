package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Fokker_DII extends Fokker_ED {

    public Fokker_DII() {
    }

    static {
        Class class1 = Fokker_DII.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Fokker_DII");
        Property.set(class1, "meshName", "3do/plane/Fokker_DII/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParWW1());
        Property.set(class1, "yearService", 1915F);
        Property.set(class1, "yearExpired", 1916F);
        Property.set(class1, "FlightModel", "FlightModels/FokkerDII.fmd:FokkerED");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFokkerDII.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
