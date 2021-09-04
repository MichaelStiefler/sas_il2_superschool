package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500KgCatalana extends Bomb {
    static {
        Class class1 = Bomb500KgCatalana.class;
        Property.set(class1, "mesh", "3do/arms/Catalana_500Kg/mono.sim");
        Property.set(class1, "radius", 250F);
        Property.set(class1, "power", 275F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.678F);
        Property.set(class1, "massa", 500F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
