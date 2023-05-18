package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGr30lbFrag extends Bomb {
    static {
        Class class1 = BombGr30lbFrag.class;
        Property.set(class1, "mesh", "3do/arms/Gr30lbFrag/mono.sim");
        Property.set(class1, "radius", 34F);
        Property.set(class1, "power", 2.1F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.11F);
        Property.set(class1, "massa", 13.66F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
