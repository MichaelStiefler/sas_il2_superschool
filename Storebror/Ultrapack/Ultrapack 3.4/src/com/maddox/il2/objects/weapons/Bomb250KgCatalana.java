package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250KgCatalana extends Bomb {
    static {
        Class class1 = Bomb250KgCatalana.class;
        Property.set(class1, "mesh", "3do/arms/Catalana_250Kg/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 120F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.408F);
        Property.set(class1, "massa", 250F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
