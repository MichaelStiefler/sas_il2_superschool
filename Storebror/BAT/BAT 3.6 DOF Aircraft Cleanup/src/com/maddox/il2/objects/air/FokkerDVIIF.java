package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class FokkerDVIIF extends Fokker {

    public FokkerDVIIF() {
    }

    static {
        Class class1 = FokkerDVIIF.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FokkerDVIIF");
        Property.set(class1, "meshName", "3do/plane/Fokker_D_VIIF/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1917F);
        Property.set(class1, "yearExpired", 1922.8F);
        Property.set(class1, "FlightModel", "FlightModels/CK_FokkerDVII_BMW.fmd:CK2009");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFokkerDVIIF.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
