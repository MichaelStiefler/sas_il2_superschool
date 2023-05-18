package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class AVIA_B534 extends Avia_B5xx {

    public AVIA_B534() {
    }

    public void update(float f) {
        super.update(f);
    }

    static {
        Class class1 = AVIA_B534.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-534");
        Property.set(class1, "meshName_sk", "3DO/Plane/AviaB-534/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar00s());
        Property.set(class1, "meshName_de", "3DO/Plane/AviaB-534(de)/hier.him");
        Property.set(class1, "PaintScheme_de", new PaintSchemeFMPar00s());
        Property.set(class1, "meshName_hu", "3DO/Plane/AviaB-534(hu)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar00s());
        Property.set(class1, "meshName", "3DO/Plane/AviaB-534(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00s());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1950F);
        Property.set(class1, "FlightModel", "FlightModels/AviaB-534.fmd");
        Property.set(class1, "originCountry", PaintScheme.countrySlovakia);
        Property.set(class1, "cockpitClass", new Class[] { CockpitAVIA_B534.class });
        Property.set(class1, "LOSElevation", 0.66F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10" });
    }
}
