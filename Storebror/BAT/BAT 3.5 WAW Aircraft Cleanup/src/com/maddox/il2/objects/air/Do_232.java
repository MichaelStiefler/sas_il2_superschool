package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class Do_232 extends DO_335 {

    public Do_232() {
    }

    static {
        Class class1 = Do_232.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Do-232");
        Property.set(class1, "meshName", "3DO/Plane/Do-232/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Do232.fmd:Do_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDO_335V13.class });
        Property.set(class1, "LOSElevation", 1.00705F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN02", "_MGUN03", "_MGUN01" });
    }
}
