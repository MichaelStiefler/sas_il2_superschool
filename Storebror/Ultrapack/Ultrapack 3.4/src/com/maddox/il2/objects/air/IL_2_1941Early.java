package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class IL_2_1941Early extends IL_2 {

    public IL_2_1941Early() {
    }

    static {
        Class class1 = IL_2_1941Early.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "IL2");
        Property.set(class1, "meshName", "3do/plane/Il-2-1941Early(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "meshName_ru", "3do/plane/Il-2-1941Early/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeBCSPar02());
        Property.set(class1, "yearService", 1941.7F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Il-2-1941.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitIL_2_1940.class });
        Property.set(class1, "LOSElevation", 0.81F);
        Property.set(class1, "Handicap", 1.0F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 3, 3 });
        weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_Cannon01", "_Cannon02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalBomb01",
                        "_ExternalBomb02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_BombSpawn01", "_BombSpawn02" });
    }
}
