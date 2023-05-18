package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombPuW50 extends Bomb {
    static {
        Class class1 = BombPuW50.class;
        Property.set(class1, "mesh", "3DO/Arms/PuW-50/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 24F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.162F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
