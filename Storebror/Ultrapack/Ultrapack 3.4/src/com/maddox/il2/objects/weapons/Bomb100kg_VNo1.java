package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb100kg_VNo1 extends Bomb {
    static {
        Class class1 = Bomb100kg_VNo1.class;
        Property.set(class1, "mesh", "3do/arms/100kg_VNo1/mono.sim");
        Property.set(class1, "radius", 100F);
        Property.set(class1, "power", 50F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
