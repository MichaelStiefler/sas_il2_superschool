package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb50kg_VNo2 extends Bomb {
    static {
        Class class1 = Bomb50kg_VNo2.class;
        Property.set(class1, "mesh", "3do/arms/50kg_VNo2/mono.sim");
        Property.set(class1, "radius", 75F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
