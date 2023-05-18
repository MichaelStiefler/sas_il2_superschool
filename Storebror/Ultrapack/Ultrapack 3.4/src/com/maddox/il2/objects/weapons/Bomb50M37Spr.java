package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb50M37Spr extends Bomb {
    static {
        Class class1 = Bomb50M37Spr.class;
        Property.set(class1, "mesh", "3do/arms/SprangBomb50M37/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 20F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.2025F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
