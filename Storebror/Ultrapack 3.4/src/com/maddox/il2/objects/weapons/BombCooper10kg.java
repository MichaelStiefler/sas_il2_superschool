package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombCooper10kg extends Bomb {
    static {
        Class class1 = BombCooper10kg.class;
        Property.set(class1, "mesh", "3do/arms/Cooper-10kg/mono.sim");
        Property.set(class1, "radius", 15F);
        Property.set(class1, "power", 4F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.15F);
        Property.set(class1, "massa", 10F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
