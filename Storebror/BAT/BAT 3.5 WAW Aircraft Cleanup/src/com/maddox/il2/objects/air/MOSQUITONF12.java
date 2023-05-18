package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MOSQUITONF12 extends MOSQUITO implements TypeFighter, TypeStormovik {

    public MOSQUITONF12() {
    }

    static {
        Class class1 = MOSQUITONF12.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mosquito");
        Property.set(class1, "meshName", "3DO/Plane/MOSQUITONF12(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mosquito-NFMkXII.fmd:NF1213MRL23NO2");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMosquitoNF8.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 9, 9, 9, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
