package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500M41Min extends Bomb {
    static {
        Class class1 = Bomb500M41Min.class;
        Property.set(class1, "mesh", "3do/arms/MinBomb500M41/mono.sim");
        Property.set(class1, "radius", 82F);
        Property.set(class1, "power", 220F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.598F);
        Property.set(class1, "massa", 500F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
