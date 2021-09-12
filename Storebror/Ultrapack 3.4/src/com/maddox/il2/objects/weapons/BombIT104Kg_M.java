package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT104Kg_M extends Bomb {
    static {
        Class class1 = BombIT104Kg_M.class;
        Property.set(class1, "mesh", "3do/arms/IT104Kg_M/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 30F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.254F);
        Property.set(class1, "massa", 104F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
