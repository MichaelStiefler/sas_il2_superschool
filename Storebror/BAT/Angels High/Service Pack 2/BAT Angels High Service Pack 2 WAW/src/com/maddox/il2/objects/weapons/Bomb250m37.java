package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250m37 extends Bomb {
    static {
        Class class1 = Bomb250m37.class;
        Property.set(class1, "mesh", "3do/arms/250kg_M1937/mono.sim");
        Property.set(class1, "radius", 77F);
        Property.set(class1, "power", 130F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.421F);
        Property.set(class1, "massa", 250F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
