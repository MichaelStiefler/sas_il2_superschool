package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbs extends Bomb {

    static {
        Class class1 = Bomb1000lbs.class;
        Property.set(class1, "mesh", "3DO/Arms/1000LbsBomb/mono.sim");
        Property.set(class1, "power", 250F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 500F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
