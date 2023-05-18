package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class TB_1_ANT4b extends TB_1_ANT4 {

    public TB_1_ANT4b() {
    }

    static {
        Class class1 = TB_1_ANT4b.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "meshName", "3DO/Plane/TB_1_ANT4b/hier.him");
        Property.set(class1, "iconFar_shortClassName", "ANT4");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/TB_1_ANT_4.fmd:ANT4_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitANT_4T.class, CockpitANT_4T_Bombardier.class });
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_BombSpawn01", "_BombSpawn02" });
    }
}
