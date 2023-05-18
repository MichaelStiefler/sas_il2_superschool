package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_150KgI2 extends Bomb {
    static {
        Class class1 = BombFR_150KgI2.class;
        Property.set(class1, "mesh", "3do/arms/BombFr150KgI2/mono.sim");
        Property.set(class1, "radius", 250F);
        Property.set(class1, "power", 101F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.355F);
        Property.set(class1, "massa", 149F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
