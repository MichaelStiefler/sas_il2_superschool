package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombPuW100Sp extends Bomb {
    static {
        Class class1 = BombPuW100Sp.class;
        Property.set(class1, "mesh", "3DO/Arms/PuW-100Sp/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 62F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 115F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
