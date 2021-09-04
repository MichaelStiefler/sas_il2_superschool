package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombPuW12_5Sp extends Bomb {
    static {
        Class class1 = BombPuW12_5Sp.class;
        Property.set(class1, "mesh", "3DO/Arms/PuW-12_5Sp/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 1.01F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.14F);
        Property.set(class1, "massa", 12.5F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bomb_cassette");
    }
}
