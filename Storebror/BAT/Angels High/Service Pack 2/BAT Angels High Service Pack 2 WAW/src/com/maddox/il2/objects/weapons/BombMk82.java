package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombMk82 extends Bomb {
    static {
        Class class1 = BombMk82.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk82/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 125F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 226F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", new Object[] { Fuze_AN_M100.class, Fuze_M115.class, Fuze_M112.class });
    }
}
