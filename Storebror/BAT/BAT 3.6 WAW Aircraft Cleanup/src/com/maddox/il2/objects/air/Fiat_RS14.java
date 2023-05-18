package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Fiat_RS14 extends Fiat_RS {

    public Fiat_RS14() {
    }

    static {
        Class class1 = Fiat_RS14.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Fiat-RS14");
        Property.set(class1, "meshName", "3DO/Plane/FIAT-RS14/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/RS14.fmd:FiatRS14_FM");
        Property.set(class1, "LOSElevation", 1.0728F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitFiatRS14.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03" });
    }
}
