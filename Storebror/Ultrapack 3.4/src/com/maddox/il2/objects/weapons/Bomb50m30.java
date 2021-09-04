package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb50m30 extends Bomb {
    static {
        Class class1 = Bomb50m30.class;
        Property.set(class1, "mesh", "3do/arms/50kg_M1930/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 20F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.2025F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
