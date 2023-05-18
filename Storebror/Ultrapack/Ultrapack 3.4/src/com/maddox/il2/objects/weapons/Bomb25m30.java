package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb25m30 extends Bomb {
    static {
        Class class1 = Bomb25m30.class;
        Property.set(class1, "mesh", "3do/arms/25kg_M1930/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.1252F);
        Property.set(class1, "massa", 25F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
