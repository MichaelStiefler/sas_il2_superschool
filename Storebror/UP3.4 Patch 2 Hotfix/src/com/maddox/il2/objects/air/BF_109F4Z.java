package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BF_109F4Z extends BF_109Fx {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.initIntakeAndFlaps();
    }
    
    static {
        Class class1 = BF_109F4Z.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109F-4Z/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109F2.class });
        Property.set(class1, "FlightModel", "FlightModels/Bf-109F-4Z.fmd");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 9, 9, 1, 1, 9, 9, 3, 3, 3, 3, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON01", "_ExternalDev02", "_ExternalDev03", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02",
                "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev04" });
    }
}
