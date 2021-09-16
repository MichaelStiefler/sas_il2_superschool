package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb200kg_VNo1 extends Bomb {
    static {
        Class class1 = Bomb200kg_VNo1.class;
        Property.set(class1, "mesh", "3do/arms/200kg_VNo1/mono.sim");
        Property.set(class1, "radius", 115F);
        Property.set(class1, "power", 95F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3683F);
        Property.set(class1, "massa", 200F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
