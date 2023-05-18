package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT31Kg_M extends Bomb {
    static {
        Class class1 = BombIT31Kg_M.class;
        Property.set(class1, "mesh", "3do/arms/IT31Kg_M/mono.sim");
        Property.set(class1, "radius", 10F);
        Property.set(class1, "power", 10.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.162F);
        Property.set(class1, "massa", 31F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
