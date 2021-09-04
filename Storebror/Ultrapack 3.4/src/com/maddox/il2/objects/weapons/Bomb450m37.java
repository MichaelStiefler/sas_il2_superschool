package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb450m37 extends Bomb {
    static {
        Class class1 = Bomb450m37.class;
        Property.set(class1, "mesh", "3do/arms/450kg_M1937/mono.sim");
        Property.set(class1, "radius", 80F);
        Property.set(class1, "power", 200F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.465F);
        Property.set(class1, "massa", 450F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
