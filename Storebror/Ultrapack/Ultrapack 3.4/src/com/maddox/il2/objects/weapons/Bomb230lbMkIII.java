package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb230lbMkIII extends Bomb {
    static {
        Class class1 = Bomb230lbMkIII.class;
        Property.set(class1, "mesh", "3do/arms/RFC_MkIII_230lb/mono.sim");
        Property.set(class1, "radius", 125F);
        Property.set(class1, "power", 50F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
