package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb25kg_VSNo1 extends Bomb {
    static {
        Class class1 = Bomb25kg_VSNo1.class;
        Property.set(class1, "mesh", "3do/arms/25kg_VSNo1/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 12F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.25F);
        Property.set(class1, "massa", 25F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
