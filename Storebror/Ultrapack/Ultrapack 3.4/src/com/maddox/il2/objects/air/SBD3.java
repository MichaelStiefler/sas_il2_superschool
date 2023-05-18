package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SBD3 extends SBD implements TypeStormovik, TypeDiveBomber {

    public SBD3() {
        this.numFlapps = 3;
    }

    static {
        Class class1 = SBD3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SBD");
        Property.set(class1, "meshName", "3DO/Plane/SBD-3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_us", "3DO/Plane/SBD-3(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/SBD-3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSBD3.class, CockpitSBD3_TGunner.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01" });
    }
}
