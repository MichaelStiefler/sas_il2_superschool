package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombMk81 extends com.maddox.il2.objects.weapons.Bomb {
    static {
        Class class1 = BombMk81.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk81/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 64F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 113F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", new Object[] { Fuze_AN_M100.class, Fuze_M115.class, Fuze_M112.class });
    }
}
