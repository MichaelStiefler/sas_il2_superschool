package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MOSQUITO6 extends MOSQUITO implements TypeFighter, TypeStormovik {

    public MOSQUITO6() {
    }

    static {
        Class class1 = MOSQUITO6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mosquito");
        Property.set(class1, "meshName", "3DO/Plane/Mosquito_FB_MkVI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_gb", "3DO/Plane/Mosquito_FB_MkVI(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mosquito-FBMkVI.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMosquito6.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02" });
    }
}
