package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombPuW300Sp extends Bomb {
    static {
        Class class1 = BombPuW300Sp.class;
        Property.set(class1, "mesh", "3DO/Arms/PuW-300Sp/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 145F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 300F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
