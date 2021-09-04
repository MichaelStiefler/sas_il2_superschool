package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb100M30Bo extends Bomb {
    static {
        Class class1 = Bomb100M30Bo.class;
        Property.set(class1, "mesh", "3do/arms/Bofors100kgM30/mono.sim");
        Property.set(class1, "radius", 65F);
        Property.set(class1, "power", 45F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.2497F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
