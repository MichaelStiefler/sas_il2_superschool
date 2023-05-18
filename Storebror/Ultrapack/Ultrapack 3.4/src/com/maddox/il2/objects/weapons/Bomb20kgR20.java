package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb20kgR20 extends Bomb {
    static {
        Class class1 = Bomb20kgR20.class;
        Property.set(class1, "mesh", "3do/arms/R20-20kg/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 10F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.25F);
        Property.set(class1, "massa", 20F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
