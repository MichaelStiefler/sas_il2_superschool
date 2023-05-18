package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombHales10lb extends Bomb {
    static {
        Class class1 = BombHales10lb.class;
        Property.set(class1, "mesh", "3DO/Arms/Hales_10lb/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 5F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.15F);
        Property.set(class1, "massa", 4.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
