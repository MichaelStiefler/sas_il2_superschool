package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Breguet_CT extends Breguet_GIO {

    public Breguet_CT() {
    }

    static {
        Class class1 = Breguet_CT.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BreguetCT");
        Property.set(class1, "meshName", "3do/plane/BreguetCT(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1967.8F);
        Property.set(class1, "FlightModel", "FlightModels/U-2VS.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBreguetCM.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
