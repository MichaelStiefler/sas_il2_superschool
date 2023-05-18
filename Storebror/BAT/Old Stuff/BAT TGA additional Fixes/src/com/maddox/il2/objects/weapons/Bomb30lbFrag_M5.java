package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb30lbFrag_M5 extends Bomb {
    static {
        Class class1 = Bomb30lbFrag_M5.class;
        Property.set(class1, "mesh", "3do/arms/30lbFrag_M5/mono.sim");
        Property.set(class1, "radius", 34F);
        Property.set(class1, "power", 2.1F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.11F);
        Property.set(class1, "massa", 13.66F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_MK_219.class })));
    }
}
