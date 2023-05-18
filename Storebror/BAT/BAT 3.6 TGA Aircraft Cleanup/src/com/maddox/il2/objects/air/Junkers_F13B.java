package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class Junkers_F13B extends Junkers_F13 {

    public Junkers_F13B() {
    }

    static {
        Class class1 = Junkers_F13B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju.F13B");
        Property.set(class1, "meshName", "3DO/Plane/Junkers_F13B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1923F);
        Property.set(class1, "yearExpired", 1957F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitJunkers_F13B.class, CockpitJunkers_F13B_TGunner.class });
        Property.set(class1, "FlightModel", "FlightModels/JuF13.fmd:JuF13_FM");
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_ExternalDev01", "_BombSpawn01" });
    }
}
