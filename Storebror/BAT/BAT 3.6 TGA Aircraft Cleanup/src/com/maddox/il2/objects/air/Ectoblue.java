package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Ectoblue extends SaettaSxyz {

    public Ectoblue() {
    }

    static {
        Class class1 = Ectoblue.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "BlueBlaze");
        Property.set(class1, "meshName", "3DO/Plane/EctoBlue(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ectoblue.fmd:Ectoblue_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitG50.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01" });
    }
}
