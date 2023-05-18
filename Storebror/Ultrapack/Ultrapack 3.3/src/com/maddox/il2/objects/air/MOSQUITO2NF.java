package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MOSQUITO2NF extends MOSQUITO implements TypeFighter, TypeStormovik {

    public MOSQUITO2NF() {
    }

    static {
        Class class1 = MOSQUITO2NF.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mosquito");
        Property.set(class1, "meshName", "3DO/Plane/Mosquito_FB_MkIINF(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mosquito-FBMkVI.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMosquito6.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04" });
    }
}
