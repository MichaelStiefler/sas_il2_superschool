package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_45_HEI extends KI_45 {

    static {
        Class class1 = KI_45_HEI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-45");
        Property.set(class1, "meshName", "3do/plane/Ki-45(ja)/HEI_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-45-Hei.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_45_HeiTei.class, CockpitKI_45_Gunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 1, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01" });
    }
}
