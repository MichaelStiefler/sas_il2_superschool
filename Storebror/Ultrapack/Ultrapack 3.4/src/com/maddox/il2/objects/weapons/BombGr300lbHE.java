package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGr300lbHE extends Bomb {
    static {
        Class class1 = BombGr300lbHE.class;
        Property.set(class1, "mesh", "3do/arms/Gr300lbHE/mono.sim");
        Property.set(class1, "radius", 61F);
        Property.set(class1, "power", 62.14F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.28F);
        Property.set(class1, "massa", 124.3F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
