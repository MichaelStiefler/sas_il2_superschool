package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SPITFIRE5BLFCLP extends SPITFIRE5 {

    public SPITFIRE5BLFCLP() {
    }

    static {
        Class class1 = SPITFIRE5BLFCLP.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/SpitfireMkVbCLP(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_gb", "3DO/Plane/SpitfireMkVbCLP(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Spitfire-LF-Vb-M45M-18-CW.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSpit5B.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02" });
    }
}
