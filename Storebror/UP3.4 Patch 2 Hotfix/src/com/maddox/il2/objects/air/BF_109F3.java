package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BF_109F3 extends BF_109Fx {

    public BF_109F3() {
        this.burst_fire = new int[2][2];
    }

    public void update(float f) {
        super.update(f);
        if (!this.thisWeaponsName.equalsIgnoreCase("none")) this.checkCannonJamming();
    }

    static {
        Class class1 = BF_109F3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109F-4/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109F-4_Mod.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109F2.class });
        Property.set(class1, "LOSElevation", 0.74205F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
    }
}
