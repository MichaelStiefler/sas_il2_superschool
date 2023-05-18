package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class PylonNebelgerat_S200_V extends Pylon {
    static {
        Class class1 = PylonNebelgerat_S200_V.class;
        Property.set(class1, "mesh", "3do/arms/Nebelgerat_S200_V/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 17.7F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
