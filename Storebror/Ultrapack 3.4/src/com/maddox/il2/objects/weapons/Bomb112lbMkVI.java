package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb112lbMkVI extends Bomb {

    static {
        Class class1 = Bomb112lbMkVI.class;
        Property.set(class1, "mesh", "3DO/Arms/RFC_MkVI_112lb/mono.sim");
        Property.set(class1, "radius", 40F);
        Property.set(class1, "power", 18F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.4F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
