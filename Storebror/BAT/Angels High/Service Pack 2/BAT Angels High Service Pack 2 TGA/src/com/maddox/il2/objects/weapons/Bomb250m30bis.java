package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250m30bis extends Bomb {
    static {
        Class class1 = Bomb250m30bis.class;
        Property.set(class1, "mesh", "3do/arms/250kg_M1930bis/mono.sim");
        Property.set(class1, "radius", 75F);
        Property.set(class1, "power", 125F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3683F);
        Property.set(class1, "massa", 248.2F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
