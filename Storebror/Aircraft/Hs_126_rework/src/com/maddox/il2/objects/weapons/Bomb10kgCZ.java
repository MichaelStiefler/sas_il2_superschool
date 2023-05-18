package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb10kgCZ extends Bomb {
    static {
        Class class1 = Bomb10kgCZ.class;
        Property.set(class1, "mesh", "3do/arms/S328bomb-10kg/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.15F);
        Property.set(class1, "massa", 10F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
