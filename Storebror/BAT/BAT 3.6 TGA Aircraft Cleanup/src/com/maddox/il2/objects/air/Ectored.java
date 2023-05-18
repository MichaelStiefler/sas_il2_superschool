package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Ectored extends FolgoreSxyz {

    public Ectored() {
    }

    static {
        Class class1 = Ectored.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "RedRaider");
        Property.set(class1, "meshName", "3DO/Plane/EctoRed(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ectored.fmd:Ectored_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitG50.class });
        Property.set(class1, "LOSElevation", 0.81305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
