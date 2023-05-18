package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class IL_2T extends IL_2 {

    public IL_2T() {
    }

    static {
        Class class1 = IL_2T.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "IL2");
        Property.set(class1, "meshName", "3do/plane/Il-2T(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "meshName_ru", "3do/plane/Il-2T/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeBCSPar03());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Il-2M3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitIL_2_1942.class, CockpitIL2_Gunner.class });
        Property.set(class1, "LOSElevation", 0.81F);
        Property.set(class1, "Handicap", 1.2F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb07" });
    }
}
