package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb200kg_VNo2 extends Bomb {
    static {
        Class class1 = Bomb200kg_VNo2.class;
        Property.set(class1, "mesh", "3do/arms/200kg_VNo2/mono.sim");
        Property.set(class1, "radius", 125F);
        Property.set(class1, "power", 100F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3683F);
        Property.set(class1, "massa", 200F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
