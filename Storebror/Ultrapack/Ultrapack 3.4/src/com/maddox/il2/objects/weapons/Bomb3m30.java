package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb3m30 extends Bomb {
    static {
        Class class1 = Bomb3m30.class;
        Property.set(class1, "mesh", "3do/arms/3kg_M1930/mono.sim");
        Property.set(class1, "radius", 8F);
        Property.set(class1, "power", 2F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.075F);
        Property.set(class1, "massa", 3F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
