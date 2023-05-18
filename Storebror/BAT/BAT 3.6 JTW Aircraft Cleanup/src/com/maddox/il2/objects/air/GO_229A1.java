package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class GO_229A1 extends GO_229 implements TypeFighter, TypeBNZFighter {

    public GO_229A1() {
    }

    static {
        Class class1 = GO_229A1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Go-229");
        Property.set(class1, "meshName", "3DO/Plane/Go-229A-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.5F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/Ho-229.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitGO_229.class });
        Property.set(class1, "LOSElevation", 0.51305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02" });
    }
}
