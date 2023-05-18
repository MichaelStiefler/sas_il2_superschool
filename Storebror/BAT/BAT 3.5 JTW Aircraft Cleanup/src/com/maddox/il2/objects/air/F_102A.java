package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class F_102A extends F_102 {

    public F_102A() {
        this.counter = 0;
    }

    public void rareAction(float f, boolean flag) {
        boolean flag1 = false;
        if ((this.counter++ % 5) == 0) {
            flag1 = this.TrackingSystem(1, 25000, 600);
        }
        if (!flag1 && ((this.counter++ % 12) == 3)) {
            this.TrackingSystem(2, 50000, 15);
        }
        super.rareAction(f, flag);
    }

    private int counter;

    static {
        Class class1 = F_102A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "meshName", "3DO/Plane/F-102A/hier102late.him");
        Property.set(class1, "iconFar_shortClassName", "F-102A");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1955F);
        Property.set(class1, "yearExpired", 1970F);
        Property.set(class1, "FlightModel", "FlightModels/F-102.fmd:F102");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_102.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_InternalRock01", "_InternalRock02", "_InternalRock03", "_InternalRock04", "_InternalRock05", "_InternalRock05", "_InternalRock06", "_InternalRock06", "_InternalRock07", "_InternalRock07", "_InternalRock08", "_InternalRock08", "_InternalRock09", "_InternalRock09", "_InternalRock10", "_InternalRock10", "_ExternalDev01", "_ExternalDev02" });
    }
}
