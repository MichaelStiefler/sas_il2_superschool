package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombAF16 extends Bomb {
    static {
        Class class1 = BombAF16.class;
        Property.set(class1, "mesh", "3do/arms/AF-16/mono.sim");
        Property.set(class1, "radius", 15F);
        Property.set(class1, "power", 8F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.17F);
        Property.set(class1, "massa", 17F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
