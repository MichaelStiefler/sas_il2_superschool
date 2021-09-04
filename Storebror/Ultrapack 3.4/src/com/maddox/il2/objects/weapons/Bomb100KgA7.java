package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb100KgA7 extends Bomb {
    static {
        Class class1 = Bomb100KgA7.class;
        Property.set(class1, "mesh", "3do/arms/HispaniaA7-100Kg/mono.sim");
        Property.set(class1, "radius", 100F);
        Property.set(class1, "power", 48.8F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
