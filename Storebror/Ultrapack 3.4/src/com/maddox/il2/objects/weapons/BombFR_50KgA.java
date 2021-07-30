package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_50KgA extends Bomb {

    static {
        Class class1 = BombFR_50KgA.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr50KgA/mono.sim");
        Property.set(class1, "radius", 40F);
        Property.set(class1, "power", 18F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.28F);
        Property.set(class1, "massa", 59.31F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
