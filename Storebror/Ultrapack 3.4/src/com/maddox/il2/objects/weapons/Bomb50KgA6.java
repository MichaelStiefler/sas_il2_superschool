package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb50KgA6 extends Bomb {
    static {
        Class class1 = Bomb50KgA6.class;
        Property.set(class1, "mesh", "3do/arms/HispaniaA6-50Kg/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 24.4F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 55.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
